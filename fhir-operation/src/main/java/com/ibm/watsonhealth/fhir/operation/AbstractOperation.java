/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.operation;

import com.ibm.watsonhealth.fhir.model.OperationDefinition;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.operation.exception.FHIROperationException;
import com.ibm.watsonhealth.fhir.operation.exception.FHIROperationNotSupportedException;
import com.ibm.watsonhealth.fhir.persistence.FHIRPersistence;

public abstract class AbstractOperation implements FHIROperation {
    @Override
    public abstract String getName();

    @Override
    public Resource invoke(Resource resource, FHIRPersistence persistence) throws FHIROperationException {
        throw new FHIROperationNotSupportedException("Operation not supported");
    }

    @Override
    public Resource invoke(Class<? extends Resource> resourceType, Resource resource, FHIRPersistence persistence) throws FHIROperationException {
        throw new FHIROperationNotSupportedException("Operation not supported");
    }

    @Override
    public Resource invoke(Class<? extends Resource> resourceType, String logicalId, Resource resource, FHIRPersistence persistence)
        throws FHIROperationException {
        throw new FHIROperationNotSupportedException("Operation not supported");
    }

    @Override
    public Resource invoke(Class<? extends Resource> resourceType, String logicalId, String versionId, Resource resource, FHIRPersistence persistence)
        throws FHIROperationException {
        throw new FHIROperationNotSupportedException("Operation not supported");
    }

    @Override
    public abstract OperationDefinition getDefinition();
}
