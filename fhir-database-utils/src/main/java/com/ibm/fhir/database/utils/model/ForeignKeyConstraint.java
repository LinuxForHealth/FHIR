/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;

/**
 * Represents a FOREIGN KEY constraint referencing the primary key
 * of a parent table
 */
public class ForeignKeyConstraint extends Constraint {
    private final boolean enforced;
    private final String targetSchema;
    private final String targetTable;
    private final List<String> columns = new ArrayList<>();

    /**
     * @param constraintName
     * @param enforced
     * @param targetSchema
     * @param targetTable
     * @param column
     */
    public ForeignKeyConstraint(String constraintName, boolean enforced, String targetSchema, String targetTable,
            String... column) {
        super(constraintName);
        this.enforced = enforced;
        this.targetSchema = targetSchema;
        this.targetTable = targetTable;
        this.columns.addAll(Arrays.asList(column));
    }

    /**
     * Getter for the target table name
     * @return
     */
    public String getTargetTable() {
        return targetTable;
    }

    /**
     * Getter for the enforced boolean flag
     * @return
     */
    public boolean isEnforced() {
        return enforced;
    }

    /**
     * Getter for the target schema name
     * @return
     */
    public String getTargetSchema() {
        return this.targetSchema;
    }

    /**
     * Getter for the target schema name
     * @return
     */
    public List<String> getColumns() {
        return this.columns;
    }

    public String getQualifiedTargetName() {
        return DataDefinitionUtil.getQualifiedName(targetSchema, targetTable);
    }

    /**
     * @param name
     * @param target
     */
    public void apply(String schemaName, String name, String tenantColumnName, IDatabaseAdapter target) {
        target.createForeignKeyConstraint(getConstraintName(), schemaName, name, targetSchema, targetTable, tenantColumnName, columns, enforced);
    }

}
