/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

import java.util.Set;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;

/**
 * Modify an existing sequence to start with a higher value
 */
public class AlterSequenceStartWith extends BaseObject {
    // the value we want the sequence to start with
    private final long startWith;
    
    // caching sequence values for tuning
    private final int cache;

    /**
     * Public constructor
     * 
     * @param schemaName
     * @param sequenceName
     * @param cache
     */
    public AlterSequenceStartWith(String schemaName, String sequenceName, int version, long startWith, int cache) {
        super(schemaName, sequenceName, DatabaseObjectType.SEQUENCE, version);
        this.startWith = startWith;
        this.cache = cache;
    }

    @Override
    public void apply(IDatabaseAdapter target) {
        target.alterSequenceRestartWith(getSchemaName(), getObjectName(), startWith, this.cache);
    }

    @Override
    public void apply(Integer priorVersion, IDatabaseAdapter target) {
        apply(target);
    }

    @Override
    public void drop(IDatabaseAdapter target) {
        target.dropSequence(getSchemaName(), getObjectName());
    }

    @Override
    protected void grantGroupPrivileges(IDatabaseAdapter target, Set<Privilege> group, String toUser) {
        target.grantSequencePrivileges(getSchemaName(), getObjectName(), group, toUser);
    }

    @Override
    public void visit(DataModelVisitor v) {
        v.visited(this);
    }

    @Override
    public void visitReverse(DataModelVisitor v) {
        v.visited(this);
    }

}
