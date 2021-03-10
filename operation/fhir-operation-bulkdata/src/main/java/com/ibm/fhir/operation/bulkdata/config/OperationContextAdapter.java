/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.config;

import com.ibm.fhir.server.operation.spi.FHIROperationContext;

/**
 * Adapts the OperationContext to the output type.
 */
public class OperationContextAdapter {
    private FHIROperationContext operationContext = null;
    public OperationContextAdapter(FHIROperationContext operationContext) {
        this.operationContext = operationContext;
    }

    /**
     * gets the storage provider
     * @return
     */
    public String getStorageProvider() {
        String bulkdataSource = operationContext.getHeaderString("X-FHIR-BULKDATA-PROVIDER");
        return bulkdataSource == null ? "default" : bulkdataSource;
    }

    /**
     * gets the storage provider for the outcomes
     * @return
     */
    public String getStorageProviderOutcomes() {
        String outcomeSource = operationContext.getHeaderString("X-FHIR-BULKDATA-PROVIDER-OUTCOME");
        return outcomeSource == null ? "default" : outcomeSource;
    }

    /**
     * get the base uri
     * @return
     */
    public String getBaseUri() {
        String baseUri = (String) operationContext.getProperty(FHIROperationContext.PROPNAME_REQUEST_BASE_URI);
        return baseUri;
    }
}