/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.api;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;

/**
 * Abstraction of the cache service managing code systems
 */
public interface ICodeSystemCache {

    /**
     * Reads the id associated with the name of the passed Parameter from the code_systems table. 
     * If the id for the passed name is not present in the database, an id is generated, persisted, 
     * and returned. This is done in a thread-safe way
     * @param codeSystem A valid FHIR search  parameter name.
     * @return the id associated with the name of the passed Parameter.
     * @throws FHIRPersistenceException
     */
    int readOrAddCodeSystem(String codeSystem) throws FHIRPersistenceException;

}
