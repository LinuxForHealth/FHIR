/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

import java.util.Set;
import java.util.function.Supplier;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;

/**
 * The definition of a stored procedure, whose content is provided by a Supplier<String> function
 */
public class ProcedureDef extends BaseObject {

    // supplier provides the procedure body when requested
    private Supplier<String> supplier;

    /**
     * Public constructor
     * @param schemaName
     * @param procedureName
     * @param version
     * @param supplier
     */
    public ProcedureDef(String schemaName, String procedureName, int version, Supplier<String> supplier) {
        super(schemaName, procedureName, DatabaseObjectType.PROCEDURE, version);
        this.supplier = supplier;
    }

    @Override
    public void apply(IDatabaseAdapter target) {
        // Serialize the execution of the procedure, to try and avoid the
        // horrible deadlocks we keep getting
        synchronized(target) {
            target.createProcedure(getSchemaName(), getObjectName(), supplier);
        }
    }

    @Override
    public void apply(Integer priorVersion, IDatabaseAdapter target) {
        // Serialize the execution of the procedure, to try and avoid the
        // horrible deadlocks we keep getting
        synchronized(target) {
            // TODO migration logic here?
            target.createProcedure(getSchemaName(), getObjectName(), supplier);
        }
    }

    @Override
    public void drop(IDatabaseAdapter target) {
        target.dropProcedure(getSchemaName(), getObjectName());
    }

    @Override
    protected void grantGroupPrivileges(IDatabaseAdapter target, Set<Privilege> group, String toUser) {
        target.grantProcedurePrivileges(getSchemaName(), getObjectName(), group, toUser);
    }
}
