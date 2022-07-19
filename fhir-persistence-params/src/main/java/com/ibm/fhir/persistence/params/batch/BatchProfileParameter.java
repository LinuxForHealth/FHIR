/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.params.batch;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.index.ProfileParameter;
import com.ibm.fhir.persistence.params.api.IBatchParameterProcessor;
import com.ibm.fhir.persistence.params.api.BatchParameterValue;
import com.ibm.fhir.persistence.params.model.CommonCanonicalValue;
import com.ibm.fhir.persistence.params.model.ParameterNameValue;

/**
 * A profile parameter we are collecting to batch
 */
public class BatchProfileParameter extends BatchParameterValue {
    private final ProfileParameter parameter;
    private final CommonCanonicalValue commonCanonicalValue;
    
    /**
     * Canonical constructor
     * 
     * @param requestShard
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param parameterNameValue
     * @param parameter
     * @param commonCanonicalValue
     */
    public BatchProfileParameter(String requestShard, String resourceType, String logicalId, long logicalResourceId, 
            ParameterNameValue parameterNameValue, ProfileParameter parameter, CommonCanonicalValue commonCanonicalValue) {
        super(requestShard, resourceType, logicalId, logicalResourceId, parameterNameValue);
        this.parameter = parameter;
        this.commonCanonicalValue = commonCanonicalValue;
    }

    @Override
    public void apply(IBatchParameterProcessor processor) throws FHIRPersistenceException {
        processor.process(requestShard, resourceType, logicalId, logicalResourceId, parameterNameValue, parameter, commonCanonicalValue);
    }
}
