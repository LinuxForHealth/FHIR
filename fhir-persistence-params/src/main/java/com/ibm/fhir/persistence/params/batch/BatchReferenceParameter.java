/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.params.batch;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.index.ReferenceParameter;
import com.ibm.fhir.persistence.params.api.IBatchParameterProcessor;
import com.ibm.fhir.persistence.params.api.BatchParameterValue;
import com.ibm.fhir.persistence.params.model.LogicalResourceIdentValue;
import com.ibm.fhir.persistence.params.model.ParameterNameValue;

/**
 * A reference parameter we are collecting to batch
 */
public class BatchReferenceParameter extends BatchParameterValue {
    private final ReferenceParameter parameter;
    private final LogicalResourceIdentValue refLogicalResourceId;
    
    /**
     * Canonical constructor
     * 
     * @param requestShard
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param parameterNameValue
     * @param parameter
     * @param refLogicalResourceId
     */
    public BatchReferenceParameter(String requestShard, String resourceType, String logicalId, long logicalResourceId, ParameterNameValue parameterNameValue, ReferenceParameter parameter, LogicalResourceIdentValue refLogicalResourceId) {
        super(requestShard, resourceType, logicalId, logicalResourceId, parameterNameValue);
        this.parameter = parameter;
        this.refLogicalResourceId = refLogicalResourceId;
    }

    @Override
    public void apply(IBatchParameterProcessor processor) throws FHIRPersistenceException {
        processor.process(requestShard, resourceType, logicalId, logicalResourceId, parameterNameValue, parameter, refLogicalResourceId);
    }
}
