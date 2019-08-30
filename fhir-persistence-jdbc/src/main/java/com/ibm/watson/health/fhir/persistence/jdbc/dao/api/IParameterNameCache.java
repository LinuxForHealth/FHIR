/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.persistence.jdbc.dao.api;

import com.ibm.watson.health.fhir.persistence.exception.FHIRPersistenceException;

/**
 * Abstraction of the cache service managing parameter names.
 * @author rarnold
 *
 */
public interface IParameterNameCache {

    /**
     * Reads the id associated with the name of the passed Parameter from the Parameter_Names table. If the id for the passed name is not present
     * in the database, an id is generated, persisted, and returned.
     * @param String A valid FHIR search  parameter name.
     * @return Integer - the id associated with the name of the passed Parameter.
     * @throws FHIRPersistenceDataAccessException
     */
    int readOrAddParameterNameId(String parameterName) throws FHIRPersistenceException;

}
