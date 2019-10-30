/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.api;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * Abstraction of the cache service managing parameter names.
 *
 */
public interface IParameterNameCache {

    /**
     * Reads the id associated with the name of the passed Parameter from the Parameter_Names table. If the id for the passed name is not present
     * in the database, an id is generated, persisted, and returned.
     * @param parameterName A valid FHIR search  parameter name.
     * @return the id associated with the name of the passed Parameter.
     * @throws FHIRPersistenceException
     */
    int readOrAddParameterNameId(String parameterName) throws FHIRPersistenceException;

}
