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

    public String getBulkDataSourceFromConfiguration() {
        String bulkdataSource = operationContext.getHeaderString("X-FHIR-BULKDATA-SOURCE");
        return bulkdataSource == null ? "default" : bulkdataSource;
    }

    public String getOutcomeSourceFromConfiguration() {
        String outcomeSource = operationContext.getHeaderString("X-FHIR-BULKDATA-OUTCOME");
        return outcomeSource == null ? "default" : outcomeSource;
    }

    public String getBaseUri() {
        String baseUri = (String) operationContext.getProperty(FHIROperationContext.PROPNAME_REQUEST_BASE_URI);
        return baseUri;
    }
}