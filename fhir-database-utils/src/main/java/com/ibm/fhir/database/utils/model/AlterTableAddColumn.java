/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.SchemaApplyContext;

/**
 * Add new columns to an existing table. This alter will change the version history of the underlying table.
 */
public class AlterTableAddColumn extends BaseObject {
    private final List<ColumnBase> columns;

    /**
     * Public constructor
     * @param schemaName
     * @param tableName
     * @param version
     * @param columns
     */
    public AlterTableAddColumn(String schemaName, String tableName, int version, List<ColumnBase> columns) {
        super(schemaName, tableName, DatabaseObjectType.TABLE, version);
        this.columns = new ArrayList<>(columns);
    }

    /**
     * Public constructor. Convenience for adding an arbitrary list of ColumnBase args
     * @param schemaName
     * @param tableName
     * @param column
     * @param version
     */
    public AlterTableAddColumn(String schemaName, String tableName, int version, ColumnBase... columns) {
        this(schemaName, tableName, version, Arrays.asList(columns));
    }
    

    @Override
    public String getTypeNameVersion() {
        // Make sure this alter is specific to the column change (for tracking when building the create
        // schema task dependency tree)
        StringBuilder result = new StringBuilder();
        result.append(getObjectType().name());
        result.append(":");
        result.append(getQualifiedName());
        for (ColumnBase c: columns) {
            result.append(":");
            result.append(c.getName());
        }
        result.append(":");
        result.append(this.version);
        return result.toString();
    }

    @Override
    public void apply(IDatabaseAdapter target, SchemaApplyContext context) {
        // To keep things simple, just add each column in its own statement
        for (ColumnBase c: columns) {
            target.alterTableAddColumn(getSchemaName(), getObjectName(), c);
        }
    }

    @Override
    public void apply(Integer priorVersion, IDatabaseAdapter target, SchemaApplyContext context) {
        apply(target, context);
    }

    @Override
    public void drop(IDatabaseAdapter target) {
        // NOP
    }

    @Override
    protected void grantGroupPrivileges(IDatabaseAdapter target, Set<Privilege> group, String toUser) {
        // NOP
    }

    @Override
    public void visit(DataModelVisitor v) {
        // NOP
    }

    @Override
    public void visitReverse(DataModelVisitor v) {
        // NOP
    }
}