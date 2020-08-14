/*
 * (C) Copyright IBM Corp. 2020
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

/**
 * Updates the last seen timestamp of the LOADER_INSTANCES record
 * to indicate this particular instance is still alive
 */
public class LoaderInstanceHeartbeat implements IDatabaseStatement {
    private static final Logger logger = Logger.getLogger(RegisterLoaderInstance.class.getName());

    // PK of the loader instance to update
    private final long loaderInstanceId;
    
    /**
     * Public constructor
     * @param loaderInstanceId
     */
    public LoaderInstanceHeartbeat(long loaderInstanceId) {
        this.loaderInstanceId = loaderInstanceId;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {
        
        final String DML = ""
                + "UPDATE loader_instances "
                + "   SET heartbeat_tstamp = CURRENT TIMESTAMP "
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
