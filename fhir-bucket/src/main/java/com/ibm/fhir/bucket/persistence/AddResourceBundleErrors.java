/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.persistence;

import static com.ibm.fhir.bucket.persistence.SchemaConstants.ERROR_TEXT_LEN;
import static com.ibm.fhir.bucket.persistence.SchemaConstants.HTTP_STATUS_TEXT_LEN;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.bucket.api.ResourceBundleError;
import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;

/**
 * DAO to encapsulate all the SQL/DML used to retrieve and persist data
 * in the schema
 */
public class AddResourceBundleErrors implements IDatabaseStatement {
    private static final Logger logger = Logger.getLogger(AddResourceBundleErrors.class.getName());

    // The schema with the FHIRBUCKET tables
    private final String schemaName;

    // The list of resource types we want to add
    private final List<ResourceBundleError> errors;

    // The resource bundle where we hit the error
    private final long resourceBundleLoadId;

    // The SQL batch size
    private final int batchSize;

    /**
     * Public constructor
     * @param schemaName
     * @param resourceBundleLoadId
     * @param errors
     * @param batchSize
     */
    public AddResourceBundleErrors(String schemaName, long resourceBundleLoadId, Collection<ResourceBundleError> errors,
        int batchSize) {
        this.schemaName = schemaName;
        this.resourceBundleLoadId = resourceBundleLoadId;
        this.errors = new ArrayList<>(errors);
        this.batchSize = batchSize;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {

        // Use a batch to insert the freshly minted logical ids in one go.
        // Bundles can be large (O(1000) resources), so we periodically execute
        // the batch as we go
        final String resourceBundleErrors = DataDefinitionUtil.getQualifiedName(schemaName, "resource_bundle_errors");
        final String currentTimestamp = translator.currentTimestampString();
        final String INS =
                "INSERT INTO " + resourceBundleErrors + "("
                + "          resource_bundle_load_id, line_number, error_tstamp, error_text, "
                + "          response_time_ms, http_status_code, http_status_text) "
                + "   VALUES (?, ?, " + currentTimestamp + ", ?, ?, ?, ?)";

        int batchCount = 0;
        try (PreparedStatement ps = c.prepareStatement(INS)) {
            for (ResourceBundleError error: errors) {

                // Note that for bundles we don't include a response time for each created logical
                // response because it doesn't make sense
                ps.setLong(1, resourceBundleLoadId);
                ps.setInt(2, error.getLineNumber());
                setField(ps, 3, error.getErrorText(), ERROR_TEXT_LEN);
                setField(ps, 4, error.getResponseTimeMs());
                setField(ps, 5, error.getHttpStatusCode());
                setField(ps, 6, error.getHttpStatusText(), HTTP_STATUS_TEXT_LEN);
                ps.addBatch();

                if (++batchCount == this.batchSize) {
                    ps.executeBatch();
                }
            }

            if (batchCount > 0) {
                // final batch
                ps.executeBatch();
            }
        } catch (SQLException x) {
            // log this, but don't propagate values in the exception
            logger.log(Level.SEVERE, "Error adding resource types: " + INS + ";");
            throw translator.translate(x);
        }
    }

    /**
     * Convenience function to set a nullable int field
     * @param ps
     * @param nbr
     * @param value
     * @throws SQLException
     */
    private void setField(PreparedStatement ps, int nbr, Integer value) throws SQLException {
        if (value != null) {
            ps.setInt(nbr, value);
        } else {
            ps.setNull(nbr, Types.INTEGER);
        }
    }

    /**
     * Convenience function to set a nullable text field with a max length
     * @param ps
     * @param nbr
     * @param value
     * @param maxLen
     * @throws SQLException
     */
    private void setField(PreparedStatement ps, int nbr, String value, int maxLen) throws SQLException {
        // trim the string so it will fit into the field
        if (value != null && value.length() > maxLen) {
            value = value.substring(0, maxLen);
        }

        if (value != null) {
            ps.setString(nbr, value);
        } else {
            ps.setNull(nbr, Types.VARCHAR);
        }
    }

}