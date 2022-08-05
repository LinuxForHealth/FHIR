/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.model;

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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.database.utils.api.ISchemaAdapter;
import org.linuxforhealth.fhir.database.utils.api.ITransaction;
import org.linuxforhealth.fhir.database.utils.api.ITransactionProvider;
import org.linuxforhealth.fhir.database.utils.api.IVersionHistoryService;
import org.linuxforhealth.fhir.database.utils.api.SchemaApplyContext;
import org.linuxforhealth.fhir.database.utils.common.DataDefinitionUtil;
import org.linuxforhealth.fhir.task.api.ITaskCollector;

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
    public void collect(ITaskCollector tc, ISchemaAdapter target, SchemaApplyContext context, ITransactionProvider tp, IVersionHistoryService vhs) {
        for (IDatabaseObject obj: allObjects) {
            obj.collect(tc, target, context, tp, vhs);
        }
    }

    /**
     * Apply the entire model to the target in order
     * @param target
     * @param context
     */
    public void apply(ISchemaAdapter target, SchemaApplyContext context) {
        int total = allObjects.size();
        int count = 1;
        for (IDatabaseObject obj: allObjects) {
            logger.fine(String.format("Creating [%d/%d] %s", count++, total, obj.toString()));
            obj.apply(target, context);
        }
    }

    /**
     * Make a pass over all the objects and apply any distribution rules they
     * may have (e.g. for Citus). We have to process a large number of tables,
     * which can cause shared memory issues for Citus if we try and do this in
     * a single transaction, hence the need for a transactionSupplier
     * @param target
     */
    public void applyDistributionRules(ISchemaAdapter target, Supplier<ITransaction> transactionSupplier) {

        // takes a long time, so track progress
        int total = allObjects.size() * 2;
        int count = 0;
        int objectsPerMessage = total / 100; // 1% increments
        int nextCount = objectsPerMessage;
        // make a first pass to apply reference rules
        for (IDatabaseObject obj: allObjects) {
            try (ITransaction tx = transactionSupplier.get()) {
                try {
                    obj.applyDistributionRules(target, 0);
                    
                    if (++count >= nextCount) {
                        int pc = 100 * nextCount / total;
                        logger.info("Progress: [" + pc + "% complete]");
                        nextCount += objectsPerMessage;
                    }
                } catch (RuntimeException x) {
                    tx.setRollbackOnly();
                    throw x;
                }                    
            }
        }
        
        // and another pass to apply sharding rules
        for (IDatabaseObject obj: allObjects) {
            try (ITransaction tx = transactionSupplier.get()) {
                try {
                    obj.applyDistributionRules(target, 1);

                    if (++count >= nextCount) {
                        int pc = 100 * nextCount / total;
                        logger.info("Progress: [" + pc + "% complete]");
                        nextCount += objectsPerMessage;
                    }
                } catch (RuntimeException x) {
                    tx.setRollbackOnly();
                    throw x;
                }                    
            }
        }
    }

    /**
     * Apply all the objects linearly, but using the version history service to determine
     * what's new and what already exists
     * @param target
     * @param vhs
     */
    public void applyWithHistory(ISchemaAdapter target, SchemaApplyContext context, IVersionHistoryService vhs) {
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
    public void applyProcedures(ISchemaAdapter adapter, SchemaApplyContext context) {
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
    public void applyFunctions(ISchemaAdapter adapter, SchemaApplyContext context) {
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
    public void drop(ISchemaAdapter target, String tagGroup, String tag) {
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
    public void dropSplitTransaction(ISchemaAdapter target, ITransactionProvider transactionProvider, String tagGroup, String tag) {
        
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
    public void dropForeignKeyConstraints(ISchemaAdapter target, String tagGroup, String tag) {
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
     * @param transactionSupplier
     */
    public void visit(DataModelVisitor v, final String tagGroup, final String tag, Supplier<ITransaction> transactionSupplier) {
        // visit just the matching subset of objects. If a transactionSupplier has been provided, we break up the
        // operation into multiple transactions to avoid transaction size limitations (e.g. with Citus FK creation)
        if (transactionSupplier != null) {

            
            ITransaction tx = transactionSupplier.get();
            try {
                int count = 0;
                for (IDatabaseObject obj: allObjects) {
                    if (tag == null || obj.getTags().get(tagGroup) != null && tag.equals(obj.getTags().get(tagGroup))) {
                        try {
                            if (logger.isLoggable(Level.FINE)) {
                                logger.fine("Visiting[" + count + "] " + obj.getName());
                            }
                            obj.visit(v);
                        } catch (RuntimeException x) {
                            tx.setRollbackOnly();
                            throw x;
                        }
                        // Limit the size of transaction to avoid SHM errors with Citus (running in docker). If that is
                        // ever addressed, then consider increasing the number of groups we process per transaction to
                        // perhaps get better performance.
                        if (++count == 1) {
                            // commit the current transaction and start a fresh one
                            tx.close();
                            tx = transactionSupplier.get();
                            count = 0;
                        }
                    }
                    
                }
            } finally {
                tx.close();
            }
        } else {
            // the old way, which will visit everything in the scope of one transaction
            this.allObjects.stream()
                .filter(obj -> tag == null || obj.getTags().get(tagGroup) != null && tag.equals(obj.getTags().get(tagGroup)))
                .forEach(obj -> obj.visit(v));
        }
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
    public void drop(ISchemaAdapter target) {
        drop(target, null, null);
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
    public void applyGrants(ISchemaAdapter target, String groupName, String username) {
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
    public void applyProcedureAndFunctionGrants(ISchemaAdapter target, String groupName, String username) {
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

    @Override
    public boolean isDistributed() {
        return this.distributed;
    }
}