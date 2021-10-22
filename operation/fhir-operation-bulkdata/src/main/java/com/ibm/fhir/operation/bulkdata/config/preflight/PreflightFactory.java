/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.config.preflight;

import java.util.List;

import com.ibm.fhir.operation.bulkdata.OperationConstants;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.operation.bulkdata.config.OperationContextAdapter;
import com.ibm.fhir.operation.bulkdata.config.preflight.impl.AzurePreflight;
import com.ibm.fhir.operation.bulkdata.config.preflight.impl.FilePreflight;
import com.ibm.fhir.operation.bulkdata.config.preflight.impl.HttpsPreflight;
import com.ibm.fhir.operation.bulkdata.config.preflight.impl.NopPreflight;
import com.ibm.fhir.operation.bulkdata.config.preflight.impl.S3Preflight;
import com.ibm.fhir.operation.bulkdata.model.type.Input;
import com.ibm.fhir.operation.bulkdata.model.type.StorageType;
import com.ibm.fhir.server.spi.operation.FHIROperationContext;

/**
 * Generates a Preflight ConfigurationTest Object based on the storage type
 */
public class PreflightFactory {

    private PreflightFactory() {
        // No Operation
    }

    /**
     * gets an instance of the Preflight check based on the source,outcome
     * @param operationContext
     * @param inputs
     * @param exportType
     * @return
     */
    public static Preflight getInstance(FHIROperationContext operationContext, List<Input> inputs,
            OperationConstants.ExportType exportType, String format) {
        // Get the Source
        // If inputs != null, then we're dealing with an ImportOperation.
        OperationContextAdapter adapter = new OperationContextAdapter(operationContext, inputs != null);
        String source = adapter.getStorageProvider();
        String outcome = adapter.getStorageProviderOutcomes();

        ConfigurationAdapter config = ConfigurationFactory.getInstance();

        Preflight preflight = new NopPreflight(source, outcome, inputs, exportType, format);
        if (!config.legacy()) {
            StorageType storageType = config.getStorageProviderStorageType(source);

            switch (storageType) {
            case HTTPS:
                preflight = new HttpsPreflight(source, outcome, inputs, exportType, format);
                break;
            case FILE:
                preflight = new FilePreflight(source, outcome, inputs, exportType, format);
                break;
            case AWSS3:
            case IBMCOS:
                preflight = new S3Preflight(source, outcome, inputs, exportType, format);
                break;
            case AZURE:
                preflight = new AzurePreflight(source, outcome, inputs, exportType, format);
                break;
            }
        }
        return preflight;
    }
}