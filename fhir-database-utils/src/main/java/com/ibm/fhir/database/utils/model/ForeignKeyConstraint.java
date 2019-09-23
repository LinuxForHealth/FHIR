/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;

/**
 * Represents a FOREIGN KEY constraint referencing the primary key
 * of a parent table
 * @author rarnold
 *
 */
public class ForeignKeyConstraint extends Constraint {
    private final String targetSchema;
    private final String targetTable;
    private final List<String> columns = new ArrayList<>();

    /**
     * @param constraintName
     */
    protected ForeignKeyConstraint(String constraintName, String targetSchema, String targetTable,
        Collection<String> columns) {
        super(constraintName);
        this.targetSchema = targetSchema;
        this.targetTable = targetTable;
        this.columns.addAll(columns);
    }

    /**
     * Getter for the target table name
     * @return
     */
    public String getTargetTable() {
        return targetTable;
    }

    /**
     * Getter for the target schema name
     * @return
     */
    public String getTargetSchema() {
        return this.targetSchema;
    }
    
    public String getQualifiedTargetName() {
        return DataDefinitionUtil.getQualifiedName(targetSchema, targetTable);
    }

    /**
     * @param name
     * @param target
     */
    public void apply(String schemaName, String name, String tenantColumnName, IDatabaseAdapter target) {
        target.createForeignKeyConstraint(getConstraintName(), schemaName, name, targetSchema, targetTable, tenantColumnName, columns);
    }

}
