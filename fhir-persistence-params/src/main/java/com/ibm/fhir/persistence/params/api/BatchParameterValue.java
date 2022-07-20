/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.params.api;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.params.model.ParameterNameValue;

/**
 * A parameter value batched for later processing
 */
public abstract class BatchParameterValue {
    protected final String requestShard;
    protected final ParameterNameValue parameterNameValue;
    protected final String resourceType;
    protected final String logicalId;
    protected final long logicalResourceId;

    /**
     * Protected constructor
     * @param requestShard
     * @param resourceType
     * @param logicalId
     * @param logicalResourceId
     * @param parameterNameValue
     */
    protected BatchParameterValue(String requestShard, String resourceType, String logicalId, long logicalResourceId, ParameterNameValue parameterNameValue) {
        this.requestShard = requestShard;
        this.resourceType = resourceType;
        this.logicalId = logicalId;
        this.logicalResourceId = logicalResourceId;
        this.parameterNameValue = parameterNameValue;
    }

    /**
     * Apply this parameter value to the target processor
     * @param processor
     */
    public abstract void apply(IBatchParameterProcessor processor) throws FHIRPersistenceException;
}
