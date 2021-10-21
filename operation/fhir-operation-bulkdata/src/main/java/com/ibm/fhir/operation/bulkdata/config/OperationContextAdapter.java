/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.config;

import com.ibm.fhir.server.spi.operation.FHIROperationContext;

/**
 * Adapts the OperationContext to the output type.
 */
public class OperationContextAdapter {
    private FHIROperationContext operationContext = null;
    private boolean isImport = false;
    public OperationContextAdapter(FHIROperationContext operationContext, boolean isImport) {
        this.operationContext = operationContext;
        this.isImport = isImport;
    }

    /**
     * gets the storage provider
     * @return
     */
    public String getStorageProvider() {
        String bulkDataSource = operationContext.getHeaderString("X-FHIR-BULKDATA-PROVIDER");
        return bulkDataSource == null ? getSource() : bulkDataSource;
    }

    /**
     * gets the storage provider for the outcomes
     * @return
     */
    public String getStorageProviderOutcomes() {
        String outcomeSource = operationContext.getHeaderString("X-FHIR-BULKDATA-PROVIDER-OUTCOME");
        return outcomeSource == null ?
                ConfigurationFactory.getInstance().getOperationOutcomeProvider(getStorageProvider())
                    : outcomeSource;
    }

    /**
     * get the base uri
     * @return
     */
    public String getBaseUri() {
        return (String) operationContext.getProperty(FHIROperationContext.PROPNAME_REQUEST_BASE_URI);
    }

    /**
     * get the source based on the operation type.
     * @return
     */
    private String getSource() {
        if (isImport) {
            return ConfigurationFactory.getInstance().getDefaultImportProvider();
        } else {
            return ConfigurationFactory.getInstance().getDefaultExportProvider();
        }
    }
}