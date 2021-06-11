/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.DataAccessException;
import com.ibm.fhir.database.utils.api.IDatabaseSupplier;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;

/**
 * DAO to encapsulate all the SQL/DML used to retrieve and persist data
 * in the schema
 */
public class RegisterLoaderInstance implements IDatabaseSupplier<Long> {
    private static final Logger logger = Logger.getLogger(RegisterLoaderInstance.class.getName());

    // unique key (UUID) representing this loader instance
    private final String loaderInstanceKey;

    // the host we are running on
    private final String host;

    // the process id, if we've been able to get it
    private final int pid;

    private final String schemaName;

    /**
     * Public constructor
     * @param schemaName
     * @param loaderInstanceKey
     * @param host
     * @param pid
     */
    public RegisterLoaderInstance(String schemaName, String loaderInstanceKey, String host, int pid) {
        this.schemaName = schemaName;
        this.loaderInstanceKey = loaderInstanceKey;
        this.host = host;
        this.pid = pid;
    }

    @Override
    public Long run(IDatabaseTranslator translator, Connection c) {
        Long loaderInstanceId;

        // loader_instance_id is generated always as identity
        final String tableName = DataDefinitionUtil.getQualifiedName(schemaName, "loader_instances");
        final String currentTimestamp = translator.currentTimestampString();
        final String SQL = "INSERT INTO " + tableName + "(loader_instance_key, hostname, pid, heartbeat_tstamp, status) "
                + " VALUES (?, ?, ?, " + currentTimestamp + ", 'RUNNING')";
        try (PreparedStatement ps = c.prepareStatement(SQL, 1)) {
            ps.setString(1, loaderInstanceKey);
            ps.setString(2, host);
            ps.setLong(3, pid);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                loaderInstanceId = rs.getLong(1);
            } else {
                throw new DataAccessException("register loader instance did not generate an id");
            }
        } catch (SQLException x) {
            // log this, but don't propagate values in the exception
            logger.log(Level.SEVERE, "Error registering loader instance: " + SQL + "; "
                + loaderInstanceKey + ", " + host + ", " + pid);
            throw translator.translate(x);
        }


        return loaderInstanceId;
    }


}
