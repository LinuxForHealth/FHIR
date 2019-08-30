/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.persistence.jdbc.dao.api;

import java.sql.SQLException;

/**
 * @author rarnold
 *
 */
public interface FhirSequenceDAO {

    /**
     * Get the next value from the FHIR_SEQUENCE
     * @return
     * @throws SQLException
     */
    long nextValue() throws SQLException;
}
