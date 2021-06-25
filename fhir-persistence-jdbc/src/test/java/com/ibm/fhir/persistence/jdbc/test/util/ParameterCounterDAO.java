/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ibm.fhir.database.utils.api.IDatabaseSupplier;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;

/**
 * Simple DAO to count the number of rows in a search parameter table
 */
public class ParameterCounterDAO implements IDatabaseSupplier<Integer> {
    private final String schemaName;
    private final String resourceType;
    private final String parameterTable;

    public ParameterCounterDAO(String schemaName, String resourceType, String parameterTable) {
        this.schemaName = schemaName;
        this.resourceType = resourceType;
        this.parameterTable = parameterTable;
    }

    @Override
    public Integer run(IDatabaseTranslator translator, Connection c) {
        final String tableName = resourceType + "_" + parameterTable;
        final String qTableName = DataDefinitionUtil.getQualifiedName(schemaName, tableName);
        final String SQL = "SELECT COUNT(*) FROM " + qTableName;
        try (Statement s = c.createStatement()) {
            ResultSet rs = s.executeQuery(SQL);
            rs.next();
            return rs.getInt(1);
        } catch (SQLException x) {
            throw translator.translate(x);
        }
    }
}