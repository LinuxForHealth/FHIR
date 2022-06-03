/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.remote.index.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.derby.DerbyTranslator;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.remote.index.api.IdentityCache;

/**
 * Derby variant of the plain schema message handler which is needed because Derby
 * needs slightly different syntax for some queries
 */
public class PlainDerbyMessageHandler extends PlainMessageHandler {
    private static final Logger logger = Logger.getLogger(PlainDerbyMessageHandler.class.getName());

    /**
     * Public constructor
     * @param connection
     * @param schemaName
     * @param cache
     * @param maxReadyTimeMs
     */
    public PlainDerbyMessageHandler(Connection connection, String schemaName, IdentityCache cache, long maxReadyTimeMs) {
        super(new DerbyTranslator(), connection, schemaName, cache, maxReadyTimeMs);
    }

    @Override
    protected String onConflict() {
        return "";
    }

    @Override
    protected PreparedStatement buildLogicalResourceIdentSelectStatement(List<LogicalResourceIdentValue> values) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("SELECT rt.resource_type, lri.logical_id, lri.logical_resource_id ");
        query.append("  FROM logical_resource_ident AS lri ");
        query.append("  JOIN resource_types AS rt ON (rt.resource_type_id = lri.resource_type_id)");
        query.append(" WHERE ");
        for (int i=0; i<values.size(); i++) {
            if (i > 0) {
                query.append(" OR ");
            }
            query.append("(lri.resource_type_id = ? AND lri.logical_id = ?)");
        }
        PreparedStatement ps = connection.prepareStatement(query.toString());
        // bind the parameter values
        int param = 1;
        for (LogicalResourceIdentValue val: values) {
            ps.setInt(param++, val.getResourceTypeId());
            ps.setString(param++, val.getLogicalId());
        }
        logger.fine(() -> "logicalResourceIdents: " + query.toString());
        return ps;
    }

    @Override
    protected Integer createParameterName(String parameterName) throws SQLException {
        Integer parameterNameId = getNextRefId();
        final String insertParameterName = ""
                + "INSERT INTO parameter_names (parameter_name_id, parameter_name) "
                + "     VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertParameterName)) {
            stmt.setInt(1, parameterNameId);
            stmt.setString(2, parameterName);
            stmt.execute();
        }

        return parameterNameId;
    }

    @Override
    protected PreparedStatementWrapper buildCommonTokenValueSelectStatement(List<CommonTokenValue> values) throws SQLException {
        StringBuilder query = new StringBuilder();
        // need the code_system name - so we join back to the code_systems table as well
        query.append("SELECT cs.code_system_name, c.token_value, c.common_token_value_id ");
        query.append("  FROM common_token_values c");
        query.append("  JOIN code_systems cs ON (cs.code_system_id = c.code_system_id)");
        query.append(" WHERE ");

        // Create a (codeSystem, tokenValue) tuple for each of the CommonTokenValue records
        boolean first = true;
        for (CommonTokenValue ctv: values) {
            if (first) {
                first = false;
            } else {
                query.append(" OR ");
            }
            query.append("(c.code_system_id = ");
            query.append(ctv.getCodeSystemValue().getCodeSystemId()); // literal for code_system_id
            query.append(" AND c.token_value = ?)");
        }

        // Create the prepared statement and bind the values
        final String statementText = query.toString();
        PreparedStatement ps = connection.prepareStatement(statementText);

        // bind the parameter values
        int param = 1;
        for (CommonTokenValue ctv: values) {
            ps.setString(param++, ctv.getTokenValue());
        }
        return new PreparedStatementWrapper(statementText, ps);
    }
    @Override
    protected void addMissingCommonCanonicalValues(List<CommonCanonicalValue> missing) throws FHIRPersistenceException {

        final String nextVal = translator.nextValue(schemaName, "fhir_sequence");
        StringBuilder insert = new StringBuilder();
        insert.append("INSERT INTO common_canonical_values (url, canonical_id) ");
        insert.append("     VALUES (?,");
        insert.append(nextVal); // next sequence value
        insert.append(") ");

        final String DML = insert.toString();
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("addMissingCanonicalIds: " + DML);
        }
        try (PreparedStatement ps = connection.prepareStatement(DML)) {
            int count = 0;
            for (CommonCanonicalValue ctv: missing) {
                logger.finest(() -> "Adding canonical value [" + ctv.toString() + "]");
                ps.setString(1, ctv.getUrl());
                ps.addBatch();
                if (++count == this.maxCommonCanonicalValuesPerStatement) {
                    // not too many statements in a single batch
                    ps.executeBatch();
                    count = 0;
                }
            }
            if (count > 0) {
                // final batch
                ps.executeBatch();
            }
        } catch (SQLException x) {
            logger.log(Level.SEVERE, "failed: " + insert.toString(), x);
            throw new FHIRPersistenceException("failed inserting new common canonical values");
        }
    }

}