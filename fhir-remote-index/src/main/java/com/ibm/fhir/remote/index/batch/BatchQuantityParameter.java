/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.remote.index.batch;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.index.QuantityParameter;
import com.ibm.fhir.remote.index.api.BatchParameterProcessor;
import com.ibm.fhir.remote.index.api.BatchParameterValue;
import com.ibm.fhir.remote.index.database.CodeSystemValue;
import com.ibm.fhir.remote.index.database.ParameterNameValue;

/**
 * A quantity parameter we are collecting to batch
 */
public class BatchQuantityParameter extends BatchParameterValue {
    private final QuantityParameter parameter;
    private final CodeSystemValue codeSystemValue;
    
    /**
     * Canonical constructor
     * 
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param parameterNameValue
     * @param parameter
     * @param csv
     */
    public BatchQuantityParameter(String resourceType, String logicalId, long logicalResourceId, ParameterNameValue parameterNameValue, QuantityParameter parameter, CodeSystemValue csv) {
        super(resourceType, logicalId, logicalResourceId, parameterNameValue);
        this.parameter = parameter;
        this.codeSystemValue = csv;
    }

    @Override
    public void apply(BatchParameterProcessor processor) throws FHIRPersistenceException {
        processor.process(resourceType, logicalId, logicalResourceId, parameterNameValue, parameter, codeSystemValue);
    }
}
