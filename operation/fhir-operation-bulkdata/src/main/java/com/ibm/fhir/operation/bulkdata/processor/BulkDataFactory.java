/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.processor;

import com.ibm.fhir.operation.bulkdata.config.OperationContextAdapter;
import com.ibm.fhir.operation.bulkdata.processor.impl.ExportImportImpl;
import com.ibm.fhir.server.spi.operation.FHIROperationContext;

/**
 * The BulkData Factory simplifies the creation of an instance of the Import and Export feature.
 */
public class BulkDataFactory {
    private BulkDataFactory() {
        // No Op
    }

    public static ExportImportBulkData getInstance(FHIROperationContext operationContext) {
        return getInstance(operationContext, false);
    }


    public static ExportImportBulkData getInstance(FHIROperationContext operationContext, boolean isImport) {
        OperationContextAdapter sourceAdapter = new OperationContextAdapter(operationContext, isImport);
        String bulkdataSource = sourceAdapter.getStorageProvider();
        String outcomeSource = sourceAdapter.getStorageProviderOutcomes();
        String baseUri = sourceAdapter.getBaseUri();
        return new ExportImportImpl(bulkdataSource, outcomeSource, baseUri);
    }
}