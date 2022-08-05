/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.model;

import java.util.Set;
import java.util.function.Supplier;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.database.utils.api.ISchemaAdapter;
import org.linuxforhealth.fhir.database.utils.api.SchemaApplyContext;

/**
 * The definition of a function, whose content is provided by a Supplier<String> function
 */
public class FunctionDef extends BaseObject {
    private static final Logger logger = Logger.getLogger(FunctionDef.class.getName());

    // supplier provides the procedure body when requested
    private Supplier<String> supplier;

    // When >0, indicates that this function should be distributed
    private final int distributeByParamNum;

    /**
     * Public constructor. Supports distribution of the function by the given parameter number
     * 
     * @param schemaName
     * @param procedureName
     * @param version
     * @param supplier
     * @param distributeByParamNum
     */
    public FunctionDef(String schemaName, String procedureName, int version, Supplier<String> supplier, int distributeByParamNum) {
        super(schemaName, procedureName, DatabaseObjectType.PROCEDURE, version);
        this.supplier = supplier;
        this.distributeByParamNum = distributeByParamNum;
    }

    @Override
    public void apply(ISchemaAdapter target, SchemaApplyContext context) {
        target.createOrReplaceFunction(getSchemaName(), getObjectName(), supplier);
        if (distributeByParamNum > 0) {
            target.distributeFunction(getSchemaName(), getObjectName(), distributeByParamNum);
        }
    }

    @Override
    public void apply(Integer priorVersion, ISchemaAdapter target, SchemaApplyContext context) {
        if (priorVersion != null && priorVersion > 0 && this.getVersion() > priorVersion && !migrations.isEmpty()) {
            logger.warning("Found '" + migrations.size() + "' migration steps, but performing 'create or replace' instead");
        }

        // Functions are applied with "Create or replace", so just do a regular apply
        apply(target, context);
    }

    @Override
    public void drop(ISchemaAdapter target) {
        target.dropFunction(getSchemaName(), getObjectName());
    }

    @Override
    protected void grantGroupPrivileges(ISchemaAdapter target, Set<Privilege> group, String toUser) {
        target.grantFunctionPrivileges(getSchemaName(), getObjectName(), group, toUser);
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