/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.database.utils.api.IDatabaseStatement;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.model.resource.Patient;

/**
 * Fetch a batch of roughly random patientIds. Should not be used for any
 * statistical purposes, because the randomness is not guaranteed.
 */
public class SelectRandomPatientIds implements IDatabaseStatement {
    private static final Logger logger = Logger.getLogger(SelectRandomPatientIds.class.getName());

    // The list to fill with random patient ids
    final List<String> patientIds;
    
    // How many random patient ids to fetch
    private final int patientCount;
    
    /**
     * Public constructor
     * @param loaderInstanceId
     */
    public SelectRandomPatientIds(List<String> patientIds, int patientCount) {
        this.patientIds = patientIds;
        this.patientCount = patientCount;
    }

    @Override
    public void run(IDatabaseTranslator translator, Connection c) {

        // This select statement relies on the logical_resource_id being (roughly) evenly distributed
        // Using this, we pick a random starting point and go from there. It's therefore not perfectly
        // random, but it is an efficient way to grab large numbers of patients fairly quickly in order
        // to drive some load. The multiple patients returned will be correlated by their creation time
        // which means that there could be locality of reference issues. Good enough for what we need.
        final String SQL = ""
                + "  SELECT lr.logical_id " + 
                "      FROM fhirbucket.logical_resources lr," + 
                "           (SELECT sub.resource_type_id, round(random() * (sub.max_id - sub.min_id)) + sub.min_id AS pick_id" + 
                "              FROM (SELECT lr.resource_type_id, MIN(logical_resource_id) min_id, MAX(logical_resource_id) max_id" + 
                "                      FROM fhirbucket.logical_resources lr," + 
                "                           fhirbucket.resource_types rt" + 
                "                     WHERE lr.resource_type_id = rt.resource_type_id" + 
                "                       AND rt.resource_type = ? " + 
                "                  GROUP BY lr.resource_type_id) AS sub) AS pick " + 
                "     WHERE lr.logical_resource_id >= pick.pick_id" + 
                "       AND lr.resource_type_id = pick.resource_type_id" + 
                "     FETCH FIRST ? ROWS ONLY;";
        try (PreparedStatement ps = c.prepareStatement(SQL)) {
            ps.setString(1, Patient.class.getSimpleName());
            ps.setInt(2, this.patientCount);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                patientIds.add(rs.getString(1));
            }
        } catch (SQLException x) {
            // log this, but don't propagate values in the exception
            logger.log(Level.SEVERE, "Error select random patient ids: " + SQL);
            throw translator.translate(x);
        }
    }
}
