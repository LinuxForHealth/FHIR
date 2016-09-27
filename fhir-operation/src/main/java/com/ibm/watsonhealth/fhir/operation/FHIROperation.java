/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.operation;

import com.ibm.watsonhealth.fhir.model.OperationDefinition;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.operation.exception.FHIROperationException;
import com.ibm.watsonhealth.fhir.persistence.FHIRPersistence;

/**
 * @author johntimm
 *
 */
public interface FHIROperation {
    /**
     * @return
     */
    String getName();
    
    /**
     * @param resource
     * @param persistence
     * @return
     * @throws FHIROperationException
     */
    Resource invoke(Resource resource, FHIRPersistence persistence) throws FHIROperationException;
    
    /**
     * @param resourceType
     * @param resource
     * @param persistence
     * @return
     * @throws FHIROperationException
     */
    Resource invoke(Class<? extends Resource> resourceType, Resource resource, FHIRPersistence persistence) throws FHIROperationException;
    
    /**
     * @param resourceType
     * @param logicalId
     * @param resource
     * @param persistence
     * @return
     * @throws FHIROperationException
     */
    Resource invoke(Class<? extends Resource> resourceType, String logicalId, Resource resource, FHIRPersistence persistence) throws FHIROperationException;
    
    /**
     * @param resourceType
     * @param logicalId
     * @param versionId
     * @param resource
     * @param persistence
     * @return
     * @throws FHIROperationException
     */
    Resource invoke(Class<? extends Resource> resourceType, String logicalId, String versionId, Resource resource, FHIRPersistence persistence) throws FHIROperationException;
    
    /**
     * @return
     */
    OperationDefinition getDefinition();
}
