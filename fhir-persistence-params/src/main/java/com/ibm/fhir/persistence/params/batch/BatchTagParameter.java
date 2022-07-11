/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.params.batch;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.index.TagParameter;
import com.ibm.fhir.persistence.params.api.BatchParameterProcessor;
import com.ibm.fhir.persistence.params.api.BatchParameterValue;
import com.ibm.fhir.persistence.params.model.CommonTokenValue;
import com.ibm.fhir.persistence.params.model.ParameterNameValue;

/**
 * A tag parameter we are collecting to batch
 */
public class BatchTagParameter extends BatchParameterValue {
    private final TagParameter parameter;
    private final CommonTokenValue commonTokenValue;
    
    /**
     * Canonical constructor
     * 
     * @param requestShard
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param parameterNameValue
     * @param parameter
     * @param commonTokenValue
     */
    public BatchTagParameter(String requestShard, String resourceType, String logicalId, long logicalResourceId, ParameterNameValue parameterNameValue, TagParameter parameter, CommonTokenValue commonTokenValue) {
        super(requestShard, resourceType, logicalId, logicalResourceId, parameterNameValue);
        this.parameter = parameter;
        this.commonTokenValue = commonTokenValue;
    }

    @Override
    public void apply(BatchParameterProcessor processor) throws FHIRPersistenceException {
        processor.process(requestShard, resourceType, logicalId, logicalResourceId, parameterNameValue, parameter, commonTokenValue);
    }
}
