/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.remote.index.batch;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.index.DateParameter;
import com.ibm.fhir.remote.index.api.BatchParameterProcessor;
import com.ibm.fhir.remote.index.api.BatchParameterValue;
import com.ibm.fhir.remote.index.database.ParameterNameValue;

/**
 * A date parameter we are collecting to batch
 */
public class BatchDateParameter extends BatchParameterValue {
    private final DateParameter parameter;
    
    /**
     * Canonical constructor
     * 
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param parameterNameValue
     * @param parameter
     */
    public BatchDateParameter(String resourceType, String logicalId, long logicalResourceId, ParameterNameValue parameterNameValue, DateParameter parameter) {
        super(resourceType, logicalId, logicalResourceId, parameterNameValue);
        this.parameter = parameter;
    }

    @Override
    public void apply(BatchParameterProcessor processor) throws FHIRPersistenceException {
        processor.process(resourceType, logicalId, logicalResourceId, parameterNameValue, parameter);
    }
}
