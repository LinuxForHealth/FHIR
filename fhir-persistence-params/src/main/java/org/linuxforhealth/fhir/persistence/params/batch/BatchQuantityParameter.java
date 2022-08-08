/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.params.batch;

import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.persistence.index.QuantityParameter;
import org.linuxforhealth.fhir.persistence.params.api.IBatchParameterProcessor;
import org.linuxforhealth.fhir.persistence.params.api.BatchParameterValue;
import org.linuxforhealth.fhir.persistence.params.model.CodeSystemValue;
import org.linuxforhealth.fhir.persistence.params.model.ParameterNameValue;

/**
 * A quantity parameter we are collecting to batch
 */
public class BatchQuantityParameter extends BatchParameterValue {
    private final QuantityParameter parameter;
    private final CodeSystemValue codeSystemValue;
    
    /**
     * Canonical constructor
     * 
     * @param requestShard
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param parameterNameValue
     * @param parameter
     * @param csv
     */
    public BatchQuantityParameter(String requestShard, String resourceType, String logicalId, long logicalResourceId, ParameterNameValue parameterNameValue, QuantityParameter parameter, CodeSystemValue csv) {
        super(requestShard, resourceType, logicalId, logicalResourceId, parameterNameValue);
        this.parameter = parameter;
        this.codeSystemValue = csv;
    }

    @Override
    public void apply(IBatchParameterProcessor processor) throws FHIRPersistenceException {
        processor.process(requestShard, resourceType, logicalId, logicalResourceId, parameterNameValue, parameter, codeSystemValue);
    }
}
