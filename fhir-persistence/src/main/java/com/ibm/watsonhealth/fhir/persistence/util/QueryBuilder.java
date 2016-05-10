/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.util;

import java.util.List;

import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.watsonhealth.fhir.search.Parameter;

/**
 * Defines methods for for building persistence layer queries.
 * @param <T>
 */
public interface QueryBuilder<T> {
	
	/**
	 * Build and return query for the passed resource type and search parameters.
	 * @param resourceType - A FHIR Resource subclass.
	 * @param searchParameters - A List of Parameters to be used for constructing the query.
	 * @return T - An instance of <T> representing the constructed query.
	 * @throws FHIRPersistenceException - Thrown for any non-recoverable failure that occurs during query construction.
	 */
    T buildQuery(Class<? extends Resource> resourceType, List<Parameter> searchParameters) throws FHIRPersistenceException;
}
