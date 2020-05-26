/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.ITransaction;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.api.IVersionHistoryService;
import com.ibm.fhir.database.utils.api.LockException;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;
import com.ibm.fhir.task.api.ITaskCollector;
import com.ibm.fhir.task.api.ITaskGroup;

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
    public String getTypeAndName() {
        return getObjectType().name() + ":" + getQualifiedName();
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
    public ITaskGroup collect(final ITaskCollector tc, final IDatabaseAdapter target, final ITransactionProvider tp, final IVersionHistoryService vhs) {
        // Make sure that anything we depend on gets processed first
        List<ITaskGroup> children = null;
        if (!this.dependencies.isEmpty()) {
            children = new ArrayList<>(this.dependencies.size());
            for (IDatabaseObject obj: dependencies) {
                children.add(obj.collect(tc, target, tp, vhs));
            }
        }

        // create a new task group representing this node, pointing to any dependencies
        // we collected above. We need to use the type and name for the task group, to
        // ensure we allow for the different namespaces (e.g. procedures vs tables).
        return tc.makeTaskGroup(this.getTypeAndName(), () -> applyTx(target, tp, vhs), children);
    }

    @Override
    public void applyTx(IDatabaseAdapter target, ITransactionProvider tp, IVersionHistoryService vhs) {
        // Wrap the apply operation in its own transaction, as this is likely
        // being executed from a thread-pool. DB2 has some issues with deadlocks
        // on its catalog tables (SQLCODE=-911, SQLSTATE=40001, SQLERRMC=2) when
        // applying schema changes in parallel, so we need a little retry loop.
        int remainingAttempts = 10;
        while (remainingAttempts-- > 0) {
            try (ITransaction tx = tp.getTransaction()) {
                try {
                    applyVersion(target, vhs);
                    remainingAttempts = 0; // exit the retry loop
                }
                catch (LockException x) {
                    // Either a deadlock, or lock timeout, we allow the transaction to be
                    // tried again.
                    if (x.isDeadlock()) {
                        logger.warning("Deadlock detected processing: " + this.getTypeAndName() + "; retrying up to " + remainingAttempts + " more times");
                    }
                    else {
                        logger.warning("Lock timeout detected processing: " + this.getTypeAndName() + "; retrying up to " + remainingAttempts + " more times");
                    }
                    tx.setRollbackOnly();

                    if (remainingAttempts == 0) {
                        // end of the road on this one
                        logger.log(Level.SEVERE, "[FAILED] retries exhausted for: " + this.getTypeAndName());
                        throw x;
                    }
                }
                catch (Exception x) {
                    logger.log(Level.SEVERE, "[FAILED] " + this.getTypeAndName());
                    tx.setRollbackOnly();
                    throw x;
                }
            }

            // now we're outside the transaction, if we need to try again, then sleep
            // for a random period. This hopefully avoids things getting into lock-step
            // which may further increase the chance of a deadlock when we retry
            if (remainingAttempts > 0) {
                safeSleep();
            }
        }
    }

    /**
     * Apply the change, but only if it has a newer version than we already have
     * recorded in the database
     * @param target
     * @param vhs the service used to manage the version history table
     */
    @Override
    public void applyVersion(IDatabaseAdapter target, IVersionHistoryService vhs) {
        if (vhs.applies(getSchemaName(), getObjectType().name(), getObjectName(), version)) {
            logger.fine("Applying change [v" + version + "]: " + this.getTypeAndName());

            // Apply this change to the target database
            apply(vhs.getVersion(getSchemaName(), getObjectType().name(), getObjectName()), target);

            // call back to the version history service to add the new version to the table
            // being used to track the change history
            vhs.addVersion(getSchemaName(), getObjectType().name(), getObjectName(), getVersion());
        }
    }

    /**
     * Sleep a random amount of time.
     */
    protected void safeSleep() {
        long ms = random.nextInt(5000);
        try {
            Thread.sleep(ms);
        }
        catch (InterruptedException ix) {
            // NOP
        }
    }

    @Override
    public Map<String, String> getTags() {
        return Collections.unmodifiableMap(this.tags);
    }

    @Override
    public void grant(IDatabaseAdapter target, String groupName, String toUser) {

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
    protected void grantGroupPrivileges(IDatabaseAdapter target, Set<Privilege> group, String toUser) {
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
}
