/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.params.batch;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.index.LocationParameter;
import com.ibm.fhir.persistence.params.api.IBatchParameterProcessor;
import com.ibm.fhir.persistence.params.api.BatchParameterValue;
import com.ibm.fhir.persistence.params.model.ParameterNameValue;

/**
 * A location parameter we are collecting to batch
 */
public class BatchLocationParameter extends BatchParameterValue {
    private final LocationParameter parameter;
    
    /**
     * Canonical constructor
     * 
     * @param requestShard
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param parameterNameValue
     * @param parameter
     */
    public BatchLocationParameter(String requestShard, String resourceType, String logicalId, long logicalResourceId, ParameterNameValue parameterNameValue, LocationParameter parameter) {
        super(requestShard, resourceType, logicalId, logicalResourceId, parameterNameValue);
        this.parameter = parameter;
    }

    @Override
    public void apply(IBatchParameterProcessor processor) throws FHIRPersistenceException {
        processor.process(requestShard, resourceType, logicalId, logicalResourceId, parameterNameValue, parameter);
    }
}
