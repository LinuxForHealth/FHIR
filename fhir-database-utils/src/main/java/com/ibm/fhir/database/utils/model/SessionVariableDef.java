/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

import java.util.Set;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;

/**
 * Adds a session variable to the database
 */
public class SessionVariableDef extends BaseObject {

    public SessionVariableDef(String schemaName, String variableName, int version) {
        super(schemaName, variableName, DatabaseObjectType.VARIABLE, version);
    }

    @Override
    public void apply(IDatabaseAdapter target) {
        target.createIntVariable(getSchemaName(), getObjectName());
    }

    @Override
    public void apply(Integer priorVersion, IDatabaseAdapter target) {
        target.createIntVariable(getSchemaName(), getObjectName());
    }

    @Override
    public void drop(IDatabaseAdapter target) {
        target.dropVariable(getSchemaName(), getObjectName());
    }

    @Override
    protected void grantGroupPrivileges(IDatabaseAdapter target, Set<Privilege> group, String toUser) {
        if (target.useSessionVariable()) {
            target.grantVariablePrivileges(getSchemaName(), getObjectName(), group, toUser);
        }
    }
}