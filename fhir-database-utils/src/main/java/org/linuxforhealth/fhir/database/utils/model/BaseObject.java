/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.model;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.database.utils.api.ISchemaAdapter;
import org.linuxforhealth.fhir.database.utils.api.ITransaction;
import org.linuxforhealth.fhir.database.utils.api.ITransactionProvider;
import org.linuxforhealth.fhir.database.utils.api.IVersionHistoryService;
import org.linuxforhealth.fhir.database.utils.api.LockException;
import org.linuxforhealth.fhir.database.utils.api.SchemaApplyContext;
import org.linuxforhealth.fhir.database.utils.common.DataDefinitionUtil;
import org.linuxforhealth.fhir.database.utils.thread.ThreadHandler;
import org.linuxforhealth.fhir.task.api.ITaskCollector;
import org.linuxforhealth.fhir.task.api.ITaskGroup;

/**
 * BaseObject
 */
public abstract class BaseObject implements IDatabaseObject {

    private static final Logger logger = Logger.getLogger(BaseObject.class.getName());

    // Used to randomize a sleep after a deadlock failure
    private static final SecureRandom random = new SecureRandom();

    private final String schemaName;
    private final String objectName;
    private final DatabaseObjectType objectType;

    // tag map
    private final Map<String,String> tags = new HashMap<>();

    // the database objects we depend on
    private final Set<IDatabaseObject> dependencies = new HashSet<>();

    // The privileges granted to different types of user
    private final Map<String, Set<Privilege>> userPrivilegeMap = new HashMap<>();

    // The version number of the application schema this object applies to
    protected final int version;

    // Steps to perform to upgrade from any previous version of the schema to the new version
    protected final List<Migration> migrations;

    /**
     * Public constructor
     *
     * @param schemaName
     * @param objectName
     * @param objectType
     * @param version
     */
    public BaseObject(String schemaName, String objectName, DatabaseObjectType objectType, int version) {
        this(schemaName, objectName, objectType, version, Collections.emptyList());
    }

    /**
     * Public constructor
     *
     * @param schemaName
     * @param objectName
     * @param objectType
     * @param version
     */
    public BaseObject(String schemaName, String objectName, DatabaseObjectType objectType, int version, List<Migration> migrations) {
        this.schemaName = schemaName;
        this.objectName = objectName;
        this.objectType = objectType;
        this.version = version;
        this.migrations = migrations;
    }

    @Override
    public int getVersion() {
        return this.version;
    }

    @Override
    public DatabaseObjectType getObjectType() {
        return this.objectType;
    }

    @Override
    public int hashCode() {
        return this.objectType.hashCode() + 23 * (37 * schemaName.hashCode() + objectName.hashCode());
    }

    public String getSchemaName() {
        return this.schemaName;
    }

    public String getObjectName() {
        return this.objectName;
    }

    public String getQualifiedName() {
        return DataDefinitionUtil.getQualifiedName(schemaName, objectName);
    }

    @Override
    public String getTypeNameVersion() {
        return getObjectType().name() + ":" + getQualifiedName() + ":" + this.version;
    }

    /**
     * Add the given tags to our tag map
     * @param tags
     */
    public void addTags(Map<String,String> tags) {
        this.tags.putAll(tags);
    }

    @Override
    public void addTag(String tagName, String tagValue) {
        this.tags.put(tagName, tagValue);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            throw new IllegalArgumentException("Object other is null");
        }

        if (other instanceof BaseObject) {
            BaseObject that = (BaseObject)other;
            return this.objectType == that.objectType
                    && this.schemaName.equals(that.schemaName)
                    && this.objectName.equals(that.objectName);
        }
        else {
            throw new IllegalArgumentException("Object other is not a " + this.getClass().getName());
        }
    }

    /**
     * Add the given obj as a dependency of this (obj must be
     * created before this, or dropped after this)
     * @param obj
     */
    public void addDependency(IDatabaseObject obj) {
        this.dependencies.add(obj);
    }

    /**
     * Add the given collection of dependencies to our set
     * @param obj
     */
    @Override
    public void addDependencies(Collection<IDatabaseObject> obj) {
        this.dependencies.addAll(obj);
    }

    @Override
    public void fetchDependenciesTo(Collection<IDatabaseObject> out) {
        out.addAll(dependencies);
    }

    /**
     * Return the unique name for this object
     * @return
     */
    @Override
    public String getName() {
        return getQualifiedName();
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public ITaskGroup collect(final ITaskCollector tc, final ISchemaAdapter target, final SchemaApplyContext context, final ITransactionProvider tp, final IVersionHistoryService vhs) {
        // Make sure that anything we depend on gets processed first
        List<ITaskGroup> children = null;
        if (!this.dependencies.isEmpty()) {
            children = new ArrayList<>(this.dependencies.size());
            for (IDatabaseObject obj: dependencies) {
                children.add(obj.collect(tc, target, context, tp, vhs));
            }
        }

        // create a new task group representing this node, pointing to any dependencies
        // we collected above. We need to use the type and name for the task group, to
        // ensure we allow for the different namespaces (e.g. procedures vs tables).
        return tc.makeTaskGroup(this.getTypeNameVersion(), () -> applyTx(target, context, tp, vhs), children);
    }

    @Override
    public void applyTx(ISchemaAdapter target, SchemaApplyContext context, ITransactionProvider tp, IVersionHistoryService vhs) {
        // Wrap the apply operation in its own transaction, as this is likely
        // being executed from a thread-pool. The retry loop is required to
        // cover any deadlocks we might encounter caused by issuing DDL in
        // parallel.
        int remainingAttempts = 10;
        while (remainingAttempts-- > 0) {
            try (ITransaction tx = tp.getTransaction()) {
                try {
                    applyVersion(target, context, vhs);
                    remainingAttempts = 0; // exit the retry loop
                }
                catch (LockException x) {
                    // Either a deadlock, or lock timeout, we allow the transaction to be
                    // tried again.
                    if (x.isDeadlock()) {
                        logger.warning("Deadlock detected processing: " + this.getTypeNameVersion() + "; retrying up to " + remainingAttempts + " more times");
                    }
                    else {
                        logger.warning("Lock timeout detected processing: " + this.getTypeNameVersion() + "; retrying up to " + remainingAttempts + " more times");
                    }
                    tx.setRollbackOnly();

                    if (remainingAttempts == 0) {
                        // end of the road on this one
                        logger.log(Level.SEVERE, "[FAILED] retries exhausted for: " + this.getTypeNameVersion());
                        throw x;
                    }
                }
                catch (Exception x) {
                    logger.log(Level.SEVERE, "[FAILED] " + this.getTypeNameVersion());
                    tx.setRollbackOnly();
                    throw x;
                }
            }

            // now we're outside the transaction, if we need to try again, then sleep
            // for a random period. This hopefully avoids things getting into lock-step
            // which may further increase the chance of a deadlock when we retry
            if (remainingAttempts > 0) {
                long ms = random.nextInt(5000);
                ThreadHandler.safeSleep(ms);
            }
        }
    }

    @Override
    public void applyVersion(ISchemaAdapter target, SchemaApplyContext context, IVersionHistoryService vhs) {
        // Only for Procedures do we skip the Version History Service check, and apply.
        if (vhs.applies(getSchemaName(), getObjectType().name(), getObjectName(), version)
                    || getObjectType() == DatabaseObjectType.PROCEDURE) {
            logger.fine("Applying change [v" + version + "]: " + this.getTypeNameVersion());

            // Apply this change to the target database
            apply(vhs.getVersion(getSchemaName(), getObjectType().name(), getObjectName()), target, context);

            // Check if the PROCEDURE is this exact version (Applies to FunctionDef and ProcedureDef)
            if (DatabaseObjectType.PROCEDURE.equals(getObjectType())
                    && !vhs.applies(getSchemaName(), getObjectType().name(), getObjectName(), version)) {
                logger.info("Version History is already current, refreshing the definition " + getVersion() + " " + vhs.getVersion(schemaName, objectName, objectName));
            } else {
                // call back to the version history service to add the new version to the table
                // being used to track the change history
                vhs.addVersion(getSchemaName(), getObjectType().name(), getObjectName(), getVersion());
            }
        }
    }

    @Override
    public Map<String, String> getTags() {
        return Collections.unmodifiableMap(this.tags);
    }

    @Override
    public void grant(ISchemaAdapter target, String groupName, String toUser) {

        // The group is optional. Some objects may not have a group corresponding with
        // the requested groupName, in which case no privileges will be granted
        Set<Privilege> group = this.userPrivilegeMap.get(groupName);
        if (group != null) {
            grantGroupPrivileges(target, group, toUser);
        }
    }

    /**
     * Internal method which can be override by different object types which may need
     * to call a different grant method on the adapter
     * @param target
     * @param group
     * @param toUser
     */
    protected void grantGroupPrivileges(ISchemaAdapter target, Set<Privilege> group, String toUser) {
        target.grantObjectPrivileges(this.schemaName, this.objectName, group, toUser);
    }


    /**
     * Add the privilege to the named privilege group
     * @param groupName
     * @param p
     */
    public void addPrivilege(String groupName, Privilege p) {
        Set<Privilege> group = this.userPrivilegeMap.get(groupName);
        if (group == null) {
            group = new HashSet<>();
            this.userPrivilegeMap.put(groupName, group);
        }
        group.add(p);
    }

    @Override
    public void visit(Consumer<IDatabaseObject> c) {
        c.accept(this);
    }

    @Override
    public void applyDistributionRules(ISchemaAdapter target, int pass) {
        // NOP. Only applies to Table
    }
}