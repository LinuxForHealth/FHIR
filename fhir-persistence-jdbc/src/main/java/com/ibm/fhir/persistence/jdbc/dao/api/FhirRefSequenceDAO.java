/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.api;

import java.sql.SQLException;

/**
 *
 */
public interface FhirRefSequenceDAO {

    /**
     * Get the next value from the FHIR_REF_SEQUENCE
     * @return
     * @throws SQLException
     */
    int nextValue() throws SQLException;
}
