/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence;

import java.util.List;

import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.search.Parameter;

public interface FHIRPersistence {
	void create(Resource resource) throws FHIRPersistenceException;
	Resource read(Class<? extends Resource> resourceType, String logicalId) throws FHIRPersistenceException;
	Resource vread(Class<? extends Resource> resourceType, String logicalId, String versionId) throws FHIRPersistenceException;
	void update(String logicalId, Resource resource) throws FHIRPersistenceException;
	void delete(String logicalId) throws FHIRPersistenceException;
	List<Resource> search(List<Parameter> searchParameters) throws FHIRPersistenceException;
}
