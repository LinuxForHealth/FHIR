/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.citus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseSupplier;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;

/**
 * DAO to check if the table is already distributed
 */
public class CitusDistributionCheckDAO implements IDatabaseSupplier<Boolean> {
    private static final Logger logger = Logger.getLogger(CitusDistributionCheckDAO.class.getName());

    private final String schemaName;
    private final String tableName;

    /**
     * Public constructor
     * 
     * @param schemaName
     * @param tableName
     */
    public CitusDistributionCheckDAO(String schemaName, String tableName) {
        DataDefinitionUtil.assertValidName(schemaName);
        DataDefinitionUtil.assertValidName(tableName);
        this.schemaName = schemaName.toLowerCase();
        this.tableName = tableName.toLowerCase();
    }

    @Override
    public Boolean run(IDatabaseTranslator translator, Connection c) {
        Boolean result = Boolean.FALSE;

        final String relname = DataDefinitionUtil.getQualifiedName(schemaName, this.tableName);
        final String SQL = "SELECT 1 FROM pg_dist_partition WHERE logicalrelid = ?::regclass";

        try (PreparedStatement ps = c.prepareStatement(SQL)) {
            ps.setString(1, relname);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = Boolean.TRUE;
            }
        } catch (SQLException x) {
            // Translate the exception into something a little more meaningful
            // for this database type and application
            logger.severe("select failed: " + SQL + " for logicalrelid = '" + relname + "'");
            throw translator.translate(x);
        }
        return result;
    }
}