/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

import com.ibm.fhir.database.utils.api.ISchemaAdapter;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.api.IVersionHistoryService;
import com.ibm.fhir.database.utils.api.SchemaApplyContext;
import com.ibm.fhir.task.api.ITaskCollector;
import com.ibm.fhir.task.api.ITaskGroup;

/**
 * Defines the Database Object's expected methods.
 */
public interface IDatabaseObject {

    /**
     * Getter for the schema version number this object applies to
     * @return
     */
    public int getVersion();

    /**
     * Apply the DDL for this object to the target database
     * @param priorVersion
     * @param target the database target
     * @param context context to control the schema apply process
     */
    public void apply(ISchemaAdapter target, SchemaApplyContext context);

    /**
     * Apply migration logic to bring the target database to the current level of this object
     * @param priorVersion
     * @param target the database target
     * @param context to control the schema apply process
     */
    public void apply(Integer priorVersion, ISchemaAdapter target, SchemaApplyContext context);

    /**
     * Apply the DDL, but within its own transaction
     * @param target the target database we apply to
     * @param cp of thread-specific transactions
     * @param vhs the service interface for adding this object to the version history table
     */
    public void applyTx(ISchemaAdapter target, SchemaApplyContext context, ITransactionProvider cp, IVersionHistoryService vhs);

    /**
     * Apply any distribution rules associated with the object (usually a table)
     * @param target the target database we apply the operation to
     * @param pass multiple pass number
     */
    public void applyDistributionRules(ISchemaAdapter target, int pass);

    /**
     * Apply the change, but only if it has a newer version than we already have
     * recorded in the database
     * @param target
     * @param context
     * @param vhs the service used to manage the version history table
     */
    public void applyVersion(ISchemaAdapter target, SchemaApplyContext context, IVersionHistoryService vhs);

    /**
     * DROP this object from the target database
     * @param target
     */
    public void drop(ISchemaAdapter target);

    /**
     * Grant the given privileges to the user
     * @param target
     * @param groupName
     * @param toUser
     */
    public void grant(ISchemaAdapter target, String groupName, String toUser);

    /**
     * Visit this object, calling the consumer for itself, or its children if any
     * @param c
     */
    public void visit(Consumer<IDatabaseObject> c);
    
    /**
     * Visit this {@link IDatabaseObject} with the given {@link DataModelVisitor}.
     * Any sub-objects should be visited in creation order.
     * @param v
     */
    public void visit(DataModelVisitor v);

    /**
     * Visit this {@link IDatabaseObject} with the given {@link DataModelVisitor}.
     * Any sub-objects should be visited in reverse order.
     * @param v
     */
    public void visitReverse(DataModelVisitor v);

    /**
     * Collect the tasks into a dependency tree so that they can be
     * executed concurrently (but in the right order)
     * @param tc
     * @param target
     * @param tp
     * @param vhs
     */
    public ITaskGroup collect(ITaskCollector tc, ISchemaAdapter target, SchemaApplyContext context, ITransactionProvider tp, IVersionHistoryService vhs);

    /**
     * Return the qualified name for this object (e.g. schema.name).
     * @return
     */
    public String getName();

    /**
     * Get the qualified name for this object:
     *   objectType:objectName:objectVersion
     * @return
     */
    public String getTypeNameVersion();

    /**
     * Get the map of tags associated with this object. Used to find
     * things in the PhysicalDataModel
     * @return
     */
    public Map<String,String> getTags();

    /**
     * Add the tag name/value to the tag map for this object
     * @param tagName
     * @param tagValue
     */
    public void addTag(String tagName, String tagValue);


    /**
     * The type enum of this object
     * @return
     */
    public DatabaseObjectType getObjectType();

    /**
     * Add the collection of dependencies to this object
     * @param deps
     */
    public void addDependencies(Collection<IDatabaseObject> deps);

    /**
     * Fetch dependencies from this into the given out list
     * @param out
     */
    public void fetchDependenciesTo(Collection<IDatabaseObject> out);

}
