/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.linuxforhealth.fhir.persistence.jdbc.dao.api.FhirSequenceDAO;

/**
 * DAO to obtain the next value from FHIR_SEQUENCE
 */
public class FhirSequenceDAOImpl implements FhirSequenceDAO {
    private final Connection conn;

    /**
     * Public constructor
     */
    public FhirSequenceDAOImpl(Connection c) {
        this.conn = c;
    }

    @Override
    public long nextValue() throws SQLException {
        long result;
        final String SEQ = "select nextval('fhir_sequence')";

        try (PreparedStatement stmt = conn.prepareStatement(SEQ)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                result = rs.getLong(1);
            } else {
                // not gonna happen
                throw new IllegalStateException("no value returned from fhir_sequence!");
            }
        }

        return result;
    }

}
