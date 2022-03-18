/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.ITransaction;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.api.IVersionHistoryService;
import com.ibm.fhir.database.utils.api.SchemaApplyContext;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;
import com.ibm.fhir.task.api.ITaskCollector;

/**
 * Represents the set of tables and other schema objects that make up the
 * schema we want to build and manage. This is created as code and not in a DDL text file
 * on purpose. Doing so makes it much easier to test.
 */
public class PhysicalDataModel implements IDataModel {
    private static final Logger logger = Logger.getLogger(PhysicalDataModel.class.getName());

    // Schema objects will be applied in the order they are added to this list
    private final List<IDatabaseObject> allObjects = new ArrayList<>();

    // Map of the table objects
    private final Map<String, Table> tables = new HashMap<>();

    // A list of just procedures, which we may want to update separately
    private final List<ProcedureDef> procedures = new ArrayList<>();

    // A list of just functions, which we may want to update separately
    private final List<FunctionDef> functions = new ArrayList<>();

    // A map of tags which can be used to look up objects in the model
    private final Map<String, Map<String, Set<IDatabaseObject>>> tagMap = new HashMap<>();

    // Common models that we rely on (e.g. for FK constraints)
    private final List<PhysicalDataModel> federatedModels = new ArrayList<>();

    // Is this model configured to operate with a distributed (sharded) database?
    private final boolean distributed;

    /**
     * Default constructor. No federated models
     */
    public PhysicalDataModel(boolean distributed) {
        this.distributed = distributed;
    }

    /**
     * Constructor supporting federated data models
     * @param federatedModels
     */
    public PhysicalDataModel(PhysicalDataModel... federatedModels) {
        boolean dist = false;
        if (federatedModels != null) {
            this.federatedModels.addAll(Arrays.asList(federatedModels));
    
            // If any of the federated models are distributed, then assume we must be
            for (PhysicalDataModel dm: federatedModels) {
                if (dm.isDistributed()) {
                    dist = true;
                    break;
                }
            }
        }
        this.distributed = dist;
    }

    /**
     * Add the table to the list of objects in this model
     * @param t
     */
    public void addTable(Table t) {
        if (tables.containsKey(t.getName())) {
            throw new IllegalStateException("Duplicate table definition: " + t.getName());
        }

        // Update our tag index for this object
        collectTags(t);
        tables.put(t.getName(), t);
    }

    /**
     * Just a general object we don't need to know the details of
     * @param obj
     */
    public void addObject(IDatabaseObject obj) {
        // Update our tag index for this object
        collectTags(obj);
        allObjects.add(obj);
    }

    /**
     * Collect all the database objects we know of, describing their
     * interdependencies so that the task collector implementation can
     * execute them in parallel. This greatly reduces the amount of
     * time it takes to provision a schema.
     * @param tc collects and manages the object creation tasks and their dependencies
     * @param target the target database adapter
     * @param context to control how the schema is built
     * @param tp
     * @param vhs
     */
    public void collect(ITaskCollector tc, IDatabaseAdapter target, SchemaApplyContext context, ITransactionProvider tp, IVersionHistoryService vhs) {
        for (IDatabaseObject obj: allObjects) {
            obj.collect(tc, target, context, tp, vhs);
        }
    }

    /**
     * Apply the entire model to the target in order
     * @param target
     * @param context
     */
    public void apply(IDatabaseAdapter target, SchemaApplyContext context) {
        int total = allObjects.size();
        int count = 1;
        for (IDatabaseObject obj: allObjects) {
            logger.fine(String.format("Creating [%d/%d] %s", count++, total, obj.toString()));
            obj.apply(target, context);
        }
    }

    /**
     * Make a pass over all the objects and apply any distribution rules they
     * may have (e.g. for Citus)
     * @param target
     */
    public void applyDistributionRules(IDatabaseAdapter target) {

        // make a first pass to apply reference rules
        for (IDatabaseObject obj: allObjects) {
            obj.applyDistributionRules(target, 0);
        }
        
        // and another pass to apply sharding rules
        for (IDatabaseObject obj: allObjects) {
            obj.applyDistributionRules(target, 1);
        }
    }

    /**
     * Apply all the objects linearly, but using the version history service to determine
     * what's new and what already exists
     * @param target
     * @param vhs
     */
    public void applyWithHistory(IDatabaseAdapter target, SchemaApplyContext context, IVersionHistoryService vhs) {
        int total = allObjects.size();
        int count = 1;
        for (IDatabaseObject obj: allObjects) {
            logger.fine(String.format("Creating [%d/%d] %s", count++, total, obj.toString()));
            obj.applyVersion(target, context, vhs);
        }
    }

    /**
     * Apply all the procedures in the order in which they were added to the model
     * @param adapter
     */
    public void applyProcedures(IDatabaseAdapter adapter, SchemaApplyContext context) {
        int total = procedures.size();
        int count = 1;
        for (ProcedureDef obj: procedures) {
            logger.fine(String.format("Applying [%d/%d] %s", count++, total, obj.toString()));
            obj.drop(adapter);
            obj.apply(adapter, context);
        }
    }

    /**
     * Apply all the functions in the order in which they were added to the model
     * @param adapter
     */
    public void applyFunctions(IDatabaseAdapter adapter, SchemaApplyContext context) {
        int total = functions.size();
        int count = 1;
        for (FunctionDef obj: functions) {
            logger.fine(String.format("Applying [%d/%d] %s", count++, total, obj.toString()));
            obj.apply(adapter, context);
        }
    }

    /**
     * Drop the model from the target database. This is done
     * in reverse order
     *
     * @param target
     * @param tagGroup
     * @param tag
     */
    public void drop(IDatabaseAdapter target, String tagGroup, String tag) {
        // The simplest way to reverse the list is add everything into an array list
        // which we then simply traverse end to start
        ArrayList<IDatabaseObject> copy = new ArrayList<>();
        copy.addAll(allObjects);

        int total = allObjects.size();
        int count = 1;
        for (int i=total-1; i>=0; i--) {
            IDatabaseObject obj = copy.get(i);

            if (tag == null || obj.getTags().get(tagGroup) != null && tag.equals(obj.getTags().get(tagGroup))) {
                logger.fine(String.format("Dropping [%d/%d] %s", count++, total, obj.toString()));
                obj.drop(target);
            }
            else {
                logger.fine(String.format("Skipping [%d/%d] %s", count++, total, obj.toString()));
            }
        }

    }
    
    /**
     * Split the drop in multiple (smaller) transactions, which can be helpful to
     * reduce memory utilization in some scenarios
     * @param target
     * @param transactionProvider
     * @param tagGroup
     * @param tag
     */
    public void dropSplitTransaction(IDatabaseAdapter target, ITransactionProvider transactionProvider, String tagGroup, String tag) {
        
        ArrayList<IDatabaseObject> copy = new ArrayList<>();
        copy.addAll(allObjects);

        int total = allObjects.size();
        int count = 1;
        for (int i=total-1; i>=0; i--) {
            IDatabaseObject obj = copy.get(i);

            // Each object (which often represents a group of tables) will be dropped
            // in its own transaction...so clearly this needs to be an idempotent
            // operation
            try (ITransaction tx = transactionProvider.getTransaction()) {
                try {
                    if (tag == null || obj.getTags().get(tagGroup) != null && tag.equals(obj.getTags().get(tagGroup))) {
                        logger.info(String.format("Dropping [%d/%d] %s", count++, total, obj.toString()));
                        obj.drop(target);
                    } else {
                        logger.info(String.format("Skipping [%d/%d] %s", count++, total, obj.toString()));
                    }
                } catch (RuntimeException x) {
                    tx.setRollbackOnly();
                    throw x;
                }
            }
        }
    }

    /**
     * Drop all foreign key constraints on tables in this model. Typically done prior to dropping
     * the actual tables when removing a schema
     * @param target
     * @param tagGroup
     * @param tag
     */
    public void dropForeignKeyConstraints(IDatabaseAdapter target, String tagGroup, String tag) {
        // The simplest way to reverse the list is add everything into an array list
        // which we then simply traverse end to start
        ArrayList<IDatabaseObject> copy = new ArrayList<>();
        copy.addAll(allObjects);

        int total = allObjects.size();
        int count = 1;
        for (int i=total-1; i>=0; i--) {
            IDatabaseObject obj = copy.get(i);

            if (tag == null || obj.getTags().get(tagGroup) != null && tag.equals(obj.getTags().get(tagGroup))) {
                logger.fine(String.format("Dropping [%d/%d] %s", count++, total, obj.toString()));
                obj.drop(target);
            }
            else {
                logger.fine(String.format("Skipping [%d/%d] %s", count++, total, obj.toString()));
            }
        }

    }

    /**
     * Visit all objects which have the given tagGroup and tag
     * @param v
     * @param tagGroup
     * @param tag
     */
    public void visit(DataModelVisitor v, final String tagGroup, final String tag) {
        // visit just the matching subset of objects
        this.allObjects.stream()
            .filter(obj -> tag == null || obj.getTags().get(tagGroup) != null && tag.equals(obj.getTags().get(tagGroup)))
            .forEach(obj -> obj.visit(v));
    }

    /**
     * Visits all objects in the data model
     * @param v
     */
    public void visit(DataModelVisitor v) {
        // visit every object
        this.allObjects.forEach(obj -> obj.visit(v));
    }

    /**
     * Visit all objects in reverse order
     * @param v
     */
    public void visitReverse(DataModelVisitor v) {
        ArrayList<IDatabaseObject> copy = new ArrayList<>();
        copy.addAll(allObjects);

        int total = allObjects.size();
        for (int i=total-1; i>=0; i--) {
            IDatabaseObject obj = copy.get(i);
            obj.visitReverse(v);
        }
    }

    /**
     * Drop the lot
     * @param target
     */
    public void drop(IDatabaseAdapter target) {
        drop(target, null, null);
    }

    /**
     * Return all the tables partitioned by the given column name
     * @return
     */
    public Collection<Table> getTenantPartitionedTables(String partitionColumn) {
        final List<Table> result = new ArrayList<>();

        // Need to make sure we follow creation order here (important when
        // detaching partitions to avoid FK violation issues).
        for (IDatabaseObject obj: this.allObjects) {
            obj.visit(v -> {
                // a little clunky, but gets the job done
                if (v instanceof Table) {
                    Table t = (Table)v;
                    String cn = t.getTenantColumnName();
                    if (cn != null && cn.equals(partitionColumn)) {
                        result.add(t);
                    }
                }
            });
        }
        return result;
    }

    /**
     * Make sure every tenant-partitioned table has a partition for the given
     * tenantId
     *
     * @param adapter
     * @param schemaName
     * @param tenantId
     * @param extentSizeKB
     */
    public void addTenantPartitions(IDatabaseAdapter adapter, String schemaName, int tenantId, int extentSizeKB) {
        final String tenantIdColumn = "MT_ID";

        // We have to delegate all the fun to the adapter, which knows how
        // to manage this most efficiently
        adapter.createTenantPartitions(getTenantPartitionedTables(tenantIdColumn), schemaName, tenantId, extentSizeKB);
    }

    /**
     * Ensure that each partitioned table has a partition matching the given tenantId. This
     * method is use to add partitions to new tables added by a schema update (after the
     * tenant was allocated).
     * @param adapter
     * @param schemaName
     * @param tenantId
     */
    public void addNewTenantPartitions(IDatabaseAdapter adapter, String schemaName, int tenantId) {
        final String tenantIdColumn = "MT_ID";
        adapter.addNewTenantPartitions(getTenantPartitionedTables(tenantIdColumn), schemaName, tenantId);
    }

    /**
     * remove the partition from each of the tenant-based tables
     *
     * @param adapter
     * @param schemaName
     * @param tenantId
     * @param partitionStagingTable
     */
    public void detachTenantPartitions(IDatabaseAdapter adapter, String schemaName, int tenantId) {
        final String tenantIdColumn = "MT_ID";

        // Get a list of all the partitioned tables, in creation order
        Collection<Table> partitionedTables = getTenantPartitionedTables(tenantIdColumn);

        // Need to process drops in reverse, to avoid FK issues
        ArrayList<Table> tabList = new ArrayList<>(partitionedTables);
        Collections.reverse(tabList);

        // Remove the tenant partition from each of the tables in tabList
        adapter.removeTenantPartitions(tabList, schemaName, tenantId);
    }

    /**
     * Drop the tables generated by the removeTenantPartitions call
     * @param adapter
     * @param schemaName
     * @param tenantId
     */
    public void dropDetachedPartitions(IDatabaseAdapter adapter, String schemaName, int tenantId) {
        final String tenantIdColumn = "MT_ID";
        adapter.dropDetachedPartitions(getTenantPartitionedTables(tenantIdColumn), schemaName, tenantId);
    }



    /**
     * Add a stored procedure definition. The given {@link Supplier} will be called upon
     * to provide the DDL body for the procedure at the point in time it is being applied
     * to the database, not when constructing the model.
     *
     * @param schemaName
     * @param objectName the name of the procedure object
     * @param version
     * @param templateProvider supplier of the procedure text
     * @param dependencies
     * @param privileges
     * @return
     */
    public ProcedureDef addProcedure(String schemaName, String objectName, int version, Supplier<String> templateProvider,
            Collection<IDatabaseObject> dependencies, Collection<GroupPrivilege> privileges) {
        ProcedureDef proc = new ProcedureDef(schemaName, objectName, version, templateProvider);
        privileges.forEach(p -> p.addToObject(proc));

        if (dependencies != null) {
            proc.addDependencies(dependencies);
        }
        allObjects.add(proc);
        procedures.add(proc);

        return proc;
    }

    /**
     * adds the function to the model.
     *
     * @param schemaName
     * @param objectName
     * @param version
     * @param templateProvider
     * @param dependencies
     * @param privileges
     * @return
     */
    public FunctionDef addFunction(String schemaName, String objectName, int version, Supplier<String> templateProvider,
        Collection<IDatabaseObject> dependencies, Collection<GroupPrivilege> privileges) {
        return addFunction(schemaName, objectName, version, templateProvider, dependencies, privileges, 0);
    }

    /**
     * adds the function to the model.
     *
     * @param schemaName
     * @param objectName
     * @param version
     * @param templateProvider
     * @param dependencies
     * @param privileges
     * @param distributeByParamNum
     * @return
     */
    public FunctionDef addFunction(String schemaName, String objectName, int version, Supplier<String> templateProvider,
        Collection<IDatabaseObject> dependencies, Collection<GroupPrivilege> privileges, int distributeByParamNum) {
        FunctionDef func = new FunctionDef(schemaName, objectName, version, templateProvider, distributeByParamNum);
        privileges.forEach(p -> p.addToObject(func));

        if (dependencies != null) {
            func.addDependencies(dependencies);
        }
        allObjects.add(func);
        functions.add(func);
        return func;
    }

    @Override
    public Table findTable(String schemaName, String tableName) {
        Table result = tables.get(DataDefinitionUtil.getQualifiedName(schemaName, tableName));

        if (result == null) {
            // Look up the object in one of our federated models
            for (int i=0; i<this.federatedModels.size() && result == null; i++) {
                result = federatedModels.get(i).findTable(schemaName, tableName);
            }
        }

        return result;
    }

    /**
     * Add the object's tags to our tag map
     * @param obj
     */
    private void collectTags(IDatabaseObject obj) {
        for (Map.Entry<String,String> tag: obj.getTags().entrySet()) {
            String tagName = tag.getKey();
            String tagValue = tag.getValue();

            Map<String, Set<IDatabaseObject>> tm = tagMap.get(tagName);
            if (tm == null) {
                tm = new HashMap<>();
                tagMap.put(tagName, tm);
            }

            Set<IDatabaseObject> tagSet = tm.get(tagValue);
            if (tagSet == null) {
                tagSet = new HashSet<>();
                tm.put(tagValue, tagSet);
            }

            // Add this object to the set of objects associated with this particular tag
            tagSet.add(obj);
        }
    }

    /**
     * Find all the objects matching the given tag name and value. Think of the tagName
     * as the indexed field, and the tagValue as the matching rows (which are the objects
     * we're trying to locate).
     * @param tagName
     * @param tagValue
     * @return
     */
    public Collection<IDatabaseObject> searchByTag(String tagName, String tagValue) {

        Map<String, Set<IDatabaseObject>> tm = tagMap.get(tagName);
        if (tm != null) {
            // at least we've seen the name. So check the value
            Set<IDatabaseObject> tagSet = tm.get(tagValue);
            if (tagSet != null) {
                return Collections.unmodifiableSet(tagSet);
            }
            else {
                // value doesn't match anything
                return Collections.emptySet();
            }
        }
        else {
            // name doesn't match anything
            return Collections.emptySet();
        }

    }

    /**
     * Call the given {@link Consumer} for each of the tables found in the
     * schema identified by schemaName
     * @param schemaName
     * @param c
     */
    public void processTablesInSchema(String schemaName, Consumer<Table> c) {
        for (Table t: tables.values()) {
            if (t.getSchemaName().equals(schemaName)) {
                c.accept(t);
            }
        }
    }

    /**
     * Call the consumer for each object matching the given tag name/value tuple
     * @param tagName
     * @param tagValue
     * @param c
     */
    public void processObjectsWithTag(String tagName, String tagValue, Consumer<IDatabaseObject> c) {
        for (IDatabaseObject obj: searchByTag(tagName, tagValue)) {
            c.accept(obj);
        }
    }

    /**
     * Apply the grants for the given group to the user
     * @param target
     * @param groupName
     * @param username
     */
    public void applyGrants(IDatabaseAdapter target, String groupName, String username) {
        int total = allObjects.size();
        int count = 1;
        for (IDatabaseObject obj: allObjects) {
            logger.fine(String.format("Granting privileges [%d/%d] %s", count++, total, obj.toString()));
            obj.grant(target, groupName, username);
        }
    }

    /**
     * Grant the privileges for any registered procedures and functions to the given username
     * @param target
     * @param groupName
     * @param username
     */
    public void applyProcedureAndFunctionGrants(IDatabaseAdapter target, String groupName, String username) {
        int total = functions.size() + procedures.size();
        int count = 1;
        
        for (IDatabaseObject fn: procedures) {
            logger.fine(String.format("Granting privileges [%d/%d] %s", count++, total, fn.toString()));
            fn.grant(target, groupName, username);
        }

        for (IDatabaseObject fn: functions) {
            logger.fine(String.format("Granting privileges [%d/%d] %s", count++, total, fn.toString()));
            fn.grant(target, groupName, username);
        }
    }

    /**
     * Drop the tablespace associated with the given tenant.
     * @param tenantId
     */
    public void dropTenantTablespace(IDatabaseAdapter adapter, int tenantId) {
        adapter.dropTenantTablespace(tenantId);
    }

    @Override
    public boolean isDistributed() {
        return this.distributed;
    }
}