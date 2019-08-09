/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.derby;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.FhirSequenceDAO;

/**
 * DAO to obtain the next value from FHIR_SEQUENCE
 * @author rarnold
 *
 */
public class FhirSequenceDAOImpl implements FhirSequenceDAO {
    private final Connection conn;

    /**
     * Public constructor
     * @param c
     */
    public FhirSequenceDAOImpl(Connection c) {
        this.conn = c;
    }

    @Override
    public long nextValueFromFhirSequence() throws SQLException {
        long result;
        final String SEQ = "VALUES NEXT VALUE FOR fhir_sequence";
        
        try (PreparedStatement stmt = conn.prepareStatement(SEQ)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);                    
            }
            else {
                // not gonna happen
                throw new IllegalStateException("no value returned from fhir_sequence!");
            }
        }

        return result;
    }

}
