/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

import java.util.Set;
import java.util.function.Supplier;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.SchemaApplyContext;

/**
 * The definition of a stored procedure, whose content is provided by a Supplier<String> function
 */
public class ProcedureDef extends BaseObject {
    private static final Logger logger = Logger.getLogger(ProcedureDef.class.getName());

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
    public void apply(IDatabaseAdapter target, SchemaApplyContext context) {
        // Serialize the execution of the procedure, to try and avoid the
        // horrible deadlocks we keep getting
        synchronized (this) {
            target.dropProcedure(getSchemaName(), getObjectName());
            target.createOrReplaceProcedure(getSchemaName(), getObjectName(), supplier);
        }
    }

    @Override
    public void apply(Integer priorVersion, IDatabaseAdapter target, SchemaApplyContext context) {
        if (priorVersion != null && priorVersion > 0 && this.getVersion() > priorVersion && !migrations.isEmpty()) {
            logger.warning("Found '" + migrations.size() + "' migration steps, but performing 'create or replace' instead");
        }

        // we need to drop and then apply.
        drop(target);
        apply(target, context);
    }

    @Override
    public void drop(IDatabaseAdapter target) {
        target.dropProcedure(getSchemaName(), getObjectName());
    }

    @Override
    protected void grantGroupPrivileges(IDatabaseAdapter target, Set<Privilege> group, String toUser) {
        target.grantProcedurePrivileges(getSchemaName(), getObjectName(), group, toUser);
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
