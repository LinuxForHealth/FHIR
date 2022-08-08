/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.linuxforhealth.fhir.database.utils.api.IDatabaseTranslator;
import org.linuxforhealth.fhir.schema.control.FhirSchemaConstants;

/**
 * Handles the acquisition of sequence values from REFERENCES_SEQUENCE. For optimization, the
 * sequence increments by a larger value, allowing us to allocate numbers without having to ask
 * the database each time. This helps, because we assume each persistence operation will need to
 * use a handful of values, and anything we can do to avoid database round-trips is a good thing
 */
public class ReferencesSequenceDAO {

    // connection to the database
    private final Connection connection;

    // the name of the schema
    private final String schemaName;

    // the translator to help us create the right syntax for the target database
    private final IDatabaseTranslator translator;

    // the number of values we've allocated from the current base
    private int allocated = 0;
    
    // The next value we most recently obtained from the database
    private long nextValueBase = -1;
    
    public ReferencesSequenceDAO(Connection c, String schemaName, IDatabaseTranslator tx) {
        this.connection = c;
        this.schemaName = schemaName;
        this.translator = tx;
    }

    /**
     * Get the next value from the REFERENCES_SEQUENCE
     * @return
     */
    public long nextValue() {

        if (nextValueBase < 0 || allocated == FhirSchemaConstants.REFERENCES_SEQUENCE_INCREMENT) {
            // recharge
            try (Statement s = connection.createStatement()) {
                ResultSet rs = s.executeQuery(translator.selectSequenceNextValue(schemaName, FhirSchemaConstants.REFERENCES_SEQUENCE));
                if (rs.next()) {
                    nextValueBase = rs.getLong(1);
                    allocated = 0;
                } else {
                    throw new IllegalStateException("sequence did not return a value!");
                }
            } catch (SQLException x) {
                throw translator.translate(x);
            }
        }
        return nextValueBase + allocated++;
    }
}
