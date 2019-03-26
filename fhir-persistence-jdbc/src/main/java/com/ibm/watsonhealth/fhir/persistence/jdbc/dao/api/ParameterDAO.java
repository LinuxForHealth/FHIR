/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api;

import java.util.List;

import com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Parameter;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;

/**
 * This Data Access Object interface defines methods for creating, updating, and retrieving rows in the FHIR Parameter table.
 * @author markd
 *
 */
public interface ParameterDAO extends FHIRDbDAO {

    /**
     * Performs a batch insert of the passed Parameter objects into the FHIR database.
     * @param parameters - A List of search parameters associated with a FHIR Resource.
     * @throws FHIRPersistenceDataAccessException
     * @throws FHIRPersistenceDBConnectException
     */
    void insert(List<Parameter> parameters) throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException;

    /**
     * Deletes from the Parameter table all rows associated with the passed resource id.
     * @param resourceId - The id of the resource for which Parameter rows should be deleted.
     * @throws FHIRPersistenceDBConnectException
     * @throws FHIRPersistenceDataAccessException 
     */
    default void deleteByResource(long resourceId) throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException {};

}
