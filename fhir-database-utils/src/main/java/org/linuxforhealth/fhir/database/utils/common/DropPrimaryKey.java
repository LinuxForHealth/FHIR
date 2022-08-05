/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.database.utils.api.DataAccessException;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseStatement;
import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;

/**
 * Drop the primary key constraint on a table
 */
public class DropPrimaryKey implements IDatabaseStatement {
    private static final Logger logger = Logger.getLogger(DropPrimaryKey.class.getName());
    private final String schemaName;
    private final String tableName;

    // Ignore an error which may occur because the PK doesn't exist
    private final boolean ignoreError;

    /**
     * Public constructor
     * @param schemaName
     * @param tableName
     */
    public DropPrimaryKey(String schemaName, String tableName, boolean ignoreError) {
        DataDefinitionUtil.assertValidName(schemaName);
        DataDefinitionUtil.assertValidName(tableName);
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.ignoreError = ignoreError;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        // ought to be doing this via an adapter, which hides the differences between databases
        final String qname = DataDefinitionUtil.getQualifiedName(this.schemaName, this.tableName);
        final String ddl;
        if (translator.isFamilyPostgreSQL()) {
            // There could be some schemas built between releases which don't have the ROW_ID PK
            // so for PostgreSQL we need to check if the constraint exists otherwise the whole
            // transaction fails.
            final String pkConstraintName = getPostgresPrimaryKeyConstraint(translator, c);
            if (pkConstraintName != null) {
                DataDefinitionUtil.assertValidName(pkConstraintName); // one would hope
                ddl = "ALTER TABLE " + qname + " DROP CONSTRAINT " + pkConstraintName;
            } else {
                if (this.ignoreError) {
                    ddl = null;
                } else {
                    // to be complete, we need to throw an error here because we're not supposed to ignore it
                    final String qn = DataDefinitionUtil.getQualifiedName(this.schemaName, this.tableName);
                    throw new DataAccessException("Primary key expected but not found for table '" + qn + "'");
                }
            }
        } else {
            ddl = "ALTER TABLE " + qname + " DROP PRIMARY KEY";
        }

        if (ddl != null) {
            try (Statement s = c.createStatement()) {
                s.executeUpdate(ddl);
            } catch (SQLException x) {
                if (this.ignoreError) {
                    // just log as a warning
                    logger.warning("DropPrimaryKey failed: DDL='" + ddl + "'; -- " + x.getMessage());
                } else {
                    throw translator.translate(x);
                }
            }
        }
    }

    /**
     * Check if the PostgreSQL table has a primary key defined
     * @param translator
     * @param c
     * @param constraintName
     * @return
     */
    private String getPostgresPrimaryKeyConstraint(IDatabaseTranslator translator, Connection c) {
        // need to be careful with case here, because PostgreSQL uses lower case for names
        // not quoted (all our stuff).
        String result;

        final String SQL = ""
                + " SELECT constraint_name "
                + "   FROM information_schema.table_constraints "
                + "  WHERE table_schema = ? "
                + "    AND table_name = ? "
                + "    AND constraint_type = 'PRIMARY KEY'";

        try (PreparedStatement ps = c.prepareStatement(SQL)) {
            ps.setString(1, this.schemaName.toLowerCase());
            ps.setString(2, this.tableName.toLowerCase());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getString(1);
            } else {
                result = null;
            }
        } catch (SQLException x) {
            throw translator.translate(x);
        }

        return result;
    }
}