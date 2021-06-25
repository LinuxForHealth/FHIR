/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;

/**
 * Updates the last seen timestamp of the LOADER_INSTANCES record
 * to indicate this particular instance is still alive
 */
public class LoaderInstanceHeartbeat implements IDatabaseStatement {
    private static final Logger logger = Logger.getLogger(RegisterLoaderInstance.class.getName());

    // The schema holding the fhirbucket tables
    private final String schemaName;

    // PK of the loader instance to update
    private final long loaderInstanceId;

    /**
     * Public constructor
     * @param schemaName
     * @param loaderInstanceId
     */
    public LoaderInstanceHeartbeat(String schemaName, long loaderInstanceId) {
        this.schemaName = schemaName;
        this.loaderInstanceId = loaderInstanceId;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        final String currentTimestamp = translator.currentTimestampString();

        final String loaderInstances = DataDefinitionUtil.getQualifiedName(schemaName, "loader_instances");
        final String DML = ""
                + "UPDATE " + loaderInstances
                + "   SET heartbeat_tstamp = " + currentTimestamp
                + " WHERE loader_instance_id = ?";
        try (PreparedStatement ps = c.prepareStatement(DML)) {
            ps.setLong(1, loaderInstanceId);
            ps.executeUpdate();
        } catch (SQLException x) {
            // log this, but don't propagate values in the exception
            logger.log(Level.SEVERE, "Error updating loader instance heartbeat: " + DML + "; "
                + loaderInstanceId);
            throw translator.translate(x);
        }
    }


}
