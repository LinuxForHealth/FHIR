/*
 * (C) Copyright IBM Corp. 2019, 2020
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
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.api.IVersionHistoryService;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;
import com.ibm.fhir.database.utils.db2.Db2Adapter;
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

    /**
     * Default constructor. No federated models
     */
    public PhysicalDataModel() {
        // No Op
    }

    /**
     * Constructor supporting federated data models
     * @param federatedModels
     */
    public PhysicalDataModel(PhysicalDataModel... federatedModels) {
        this.federatedModels.addAll(Arrays.asList(federatedModels));
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
     * @param tp
     * @param vhs
     */
    public void collect(ITaskCollector tc, IDatabaseAdapter target, ITransactionProvider tp, IVersionHistoryService vhs) {
        for (IDatabaseObject obj: allObjects) {
            obj.collect(tc, target, tp, vhs);
        }
    }

    /**
     * Apply the entire model to the target in order
     * @param target
     */
    public void apply(IDatabaseAdapter target) {
        int total = allObjects.size();
        int count = 1;
        for (IDatabaseObject obj: allObjects) {
            logger.fine(String.format("Creating [%d/%d] %s", count++, total, obj.toString()));
            obj.apply(target);
        }
    }

    /**
     * Apply all the objects linearly, but using the version history service to determine
     * what's new and what already exists
     * @param target
     * @param vhs
     */
    public void applyWithHistory(IDatabaseAdapter target, IVersionHistoryService vhs) {
        int total = allObjects.size();
        int count = 1;
        for (IDatabaseObject obj: allObjects) {
            logger.fine(String.format("Creating [%d/%d] %s", count++, total, obj.toString()));
            obj.applyVersion(target, vhs);
        }
    }

    /**
     * Apply all the procedures in the order in which they were added to the model
     * @param adapter
     */
    public void applyProcedures(IDatabaseAdapter adapter) {
        int total = procedures.size();
        int count = 1;
        for (ProcedureDef obj: procedures) {
            logger.fine(String.format("Applying [%d/%d] %s", count++, total, obj.toString()));
            obj.apply(adapter);
        }
    }

    /**
     * Apply all the functions in the order in which they were added to the model
     * @param adapter
     */
    public void applyFunctions(IDatabaseAdapter adapter) {
        int total = functions.size();
        int count = 1;
        for (FunctionDef obj: functions) {
            logger.fine(String.format("Applying [%d/%d] %s", count++, total, obj.toString()));
            obj.apply(adapter);
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
        List<Table> result = new ArrayList<>();

        for (Table t: this.tables.values()) {
            String cn = t.getTenantColumnName();
            if (cn != null && cn.equals(partitionColumn)) {
                result.add(t);
            }
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
     * remove the partition from each of the tenant-based tables
     *
     * @param adapter
     * @param schemaName
     * @param tenantId
     * @param partitionStagingTable
     */
    public void removeTenantPartitions(IDatabaseAdapter adapter, String schemaName, int tenantId, String partitionStagingTable) {
        final String tenantIdColumn = "MT_ID";

        // We have to delegate all the fun to the adapter, which knows how
        // to manage this most efficiently
        adapter.removeTenantPartitions(getTenantPartitionedTables(tenantIdColumn), schemaName, tenantId, partitionStagingTable);
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
        FunctionDef func = new FunctionDef(schemaName, objectName, version, templateProvider);
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

    public void removeTenantPartitions(Db2Adapter adapter, String schemaName, int tenantId) {
        // TODO: Need Implementation
    }

    public void dropOldTenantTables() {
        // TODO: Needs Implementation
    }

    public void dropTenantTablespace() {
        // TODO: Needs implementation
    }
}