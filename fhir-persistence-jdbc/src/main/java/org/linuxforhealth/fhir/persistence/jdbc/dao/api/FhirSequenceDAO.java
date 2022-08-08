/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.dao.api;

import java.sql.SQLException;

/**
 * gets the next value in the database's FHIR_SEQUENCE sequence
 */
public interface FhirSequenceDAO {

    /**
     * Get the next value from the FHIR_SEQUENCE
     * @return
     * @throws SQLException
     */
    long nextValue() throws SQLException;
}
