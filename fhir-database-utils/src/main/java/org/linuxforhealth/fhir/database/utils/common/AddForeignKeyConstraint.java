/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.common;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.linuxforhealth.fhir.database.utils.api.IDatabaseStatement;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;
import org.linuxforhealth.fhir.database.utils.model.ForeignKeyConstraint;

/**
 * Add one or more foreign keys to the schema.table
 */
public class AddForeignKeyConstraint implements IDatabaseStatement {
    private final String schemaName;
    private final String tableName;
    private final String tenantColumnName;
    private final List<ForeignKeyConstraint> constraints;

    /**
     * Public constructor
     * @param schemaName
     * @param tableName
     * @param constraint
     */
    public AddForeignKeyConstraint(String schemaName, String tableName, ForeignKeyConstraint... constraint) {
        this(schemaName, tableName, null, constraint);
    }

    /**
     * Public constructor
     * @param schemaName
     * @param tableName
     * @param tenantColumnName
     * @param constraint
     */
    public AddForeignKeyConstraint(String schemaName, String tableName, String tenantColumnName, ForeignKeyConstraint... constraint) {
        DataDefinitionUtil.assertValidName(schemaName);
        DataDefinitionUtil.assertValidName(tableName);
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.tenantColumnName = tenantColumnName;
        this.constraints = Arrays.asList(constraint);
    }

    /**
     * @return the schemaName
     */
    public String getSchemaName() {
        return schemaName;
    }

    /**
     * @return the tableName
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @return the tenantColumnName
     */
    public String getTenantColumnName() {
        return tenantColumnName;
    }

    /**
     * @return the constraints
     */
    public List<ForeignKeyConstraint> getConstraints() {
        return constraints;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        String qname = DataDefinitionUtil.getQualifiedName(schemaName, this.tableName);
        StringBuilder ddl = new StringBuilder("ALTER TABLE " + qname);

        for (ForeignKeyConstraint constraint : constraints) {
            // Add the tenant column as a prefix to the list of columns if we have a multi-tenant table
            List<String> columns = constraint.getColumns();
            List<String> cols = new ArrayList<>(columns.size() + 1);
            if (tenantColumnName != null) {
                cols.add(tenantColumnName);
            }
            cols.addAll(columns);


            ddl.append(" ADD CONSTRAINT ");
            ddl.append(constraint.getConstraintName());
            ddl.append(" FOREIGN KEY(");
            ddl.append(DataDefinitionUtil.join(cols));
            ddl.append(") REFERENCES ");
            ddl.append(constraint.getQualifiedTargetName());
            if (!constraint.isEnforced()) {
                ddl.append(" NOT ENFORCED");
            }
        }

        try (Statement s = c.createStatement()) {
            s.executeUpdate(ddl.toString());
        }
        catch (SQLException x) {
            throw translator.translate(x);
        }
    }
}
