/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.params.batch;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.index.StringParameter;
import com.ibm.fhir.persistence.params.api.BatchParameterProcessor;
import com.ibm.fhir.persistence.params.api.BatchParameterValue;
import com.ibm.fhir.persistence.params.model.ParameterNameValue;

/**
 * A string parameter we are collecting to batch
 */
public class BatchStringParameter extends BatchParameterValue {
    private final StringParameter parameter;
    
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
    public BatchStringParameter(String requestShard, String resourceType, String logicalId, long logicalResourceId, ParameterNameValue parameterNameValue, StringParameter parameter) {
        super(requestShard, resourceType, logicalId, logicalResourceId, parameterNameValue);
        this.parameter = parameter;
    }

    @Override
    public void apply(BatchParameterProcessor processor) throws FHIRPersistenceException {
        processor.process(requestShard, resourceType, logicalId, logicalResourceId, parameterNameValue, parameter);
    }
}
