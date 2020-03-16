/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

import java.util.Set;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;

/**
 * Sequence related to the SQL sequence
 */
public class Sequence extends BaseObject {
    private final int cache;

    /**
     * Public constructor
     * @param schemaName
     * @param sequenceName
     * @param cache
     */
    public Sequence(String schemaName, String sequenceName, int version, int cache) {
        super(schemaName, sequenceName, DatabaseObjectType.SEQUENCE, version);
        this.cache = cache;
    }

    @Override
    public void apply(IDatabaseAdapter target) {
        target.createSequence(getSchemaName(), getObjectName(), this.cache);
    }

    @Override
    public void apply(Integer priorVersion, IDatabaseAdapter target) {
        target.createSequence(getSchemaName(), getObjectName(), this.cache);
    }

    @Override
    public void drop(IDatabaseAdapter target) {
        target.dropSequence(getSchemaName(), getObjectName());
    }

    @Override
    protected void grantGroupPrivileges(IDatabaseAdapter target, Set<Privilege> group, String toUser) {
         target.grantSequencePrivileges(getSchemaName(), getObjectName(), group, toUser);
     }

}
