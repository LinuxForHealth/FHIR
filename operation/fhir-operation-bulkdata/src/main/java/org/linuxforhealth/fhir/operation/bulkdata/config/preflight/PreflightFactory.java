/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.operation.bulkdata.config.preflight;

import java.util.List;

import org.linuxforhealth.fhir.operation.bulkdata.OperationConstants;
import org.linuxforhealth.fhir.operation.bulkdata.config.ConfigurationAdapter;
import org.linuxforhealth.fhir.operation.bulkdata.config.ConfigurationFactory;
import org.linuxforhealth.fhir.operation.bulkdata.config.OperationContextAdapter;
import org.linuxforhealth.fhir.operation.bulkdata.config.preflight.impl.AzurePreflight;
import org.linuxforhealth.fhir.operation.bulkdata.config.preflight.impl.FilePreflight;
import org.linuxforhealth.fhir.operation.bulkdata.config.preflight.impl.HttpsPreflight;
import org.linuxforhealth.fhir.operation.bulkdata.config.preflight.impl.NopPreflight;
import org.linuxforhealth.fhir.operation.bulkdata.config.preflight.impl.S3Preflight;
import org.linuxforhealth.fhir.operation.bulkdata.model.type.Input;
import org.linuxforhealth.fhir.operation.bulkdata.model.type.StorageType;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationContext;

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