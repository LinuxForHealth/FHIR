/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ibm.fhir.persistence.jdbc.dao.api.FhirRefSequenceDAO;

/**
 * DAO to obtain the next value from FHIR_REF_SEQUENCE
 *
 */
public class FhirRefSequenceDAOImpl implements FhirRefSequenceDAO {
    private final Connection conn;

    /**
     * Public constructor
     */
    public FhirRefSequenceDAOImpl(Connection c) {
        this.conn = c;
    }

    @Override
    public int nextValue() throws SQLException {
        int result;
        final String SEQ = "select nextval('fhir_ref_sequence')";

        try (PreparedStatement stmt = conn.prepareStatement(SEQ)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            } else {
                // not gonna happen
                throw new IllegalStateException("no value returned from fhir_ref_sequence!");
            }
        }

        return result;
    }

}
