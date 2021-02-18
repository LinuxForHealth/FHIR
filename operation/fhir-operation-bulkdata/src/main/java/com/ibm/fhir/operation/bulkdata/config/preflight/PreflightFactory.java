/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.bulkdata.config.preflight;

import java.util.List;

import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.operation.bulkdata.config.OperationContextAdapter;
import com.ibm.fhir.operation.bulkdata.config.preflight.impl.FilePreflight;
import com.ibm.fhir.operation.bulkdata.config.preflight.impl.HttpsPreflight;
import com.ibm.fhir.operation.bulkdata.config.preflight.impl.NopPreflight;
import com.ibm.fhir.operation.bulkdata.config.preflight.impl.S3Preflight;
import com.ibm.fhir.operation.bulkdata.model.type.Input;
import com.ibm.fhir.operation.bulkdata.model.type.StorageType;
import com.ibm.fhir.server.operation.spi.FHIROperationContext;

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
     * @return
     */
    public static Preflight getInstance(FHIROperationContext operationContext, List<Input> inputs) {
        // Get the Source
        OperationContextAdapter adapter = new OperationContextAdapter(operationContext);
        String source = adapter.getBulkDataSourceFromConfiguration();
        String outcome = adapter.getOutcomeSourceFromConfiguration();

        ConfigurationAdapter config = ConfigurationFactory.getInstance();

        Preflight preflight = new NopPreflight(source, outcome, inputs);
        if (!config.legacy()) {
            StorageType storageType = config.getSourceStorageType(source);

            switch (storageType) {
            case HTTPS:
                preflight = new HttpsPreflight(source, outcome, inputs);
                break;
            case FILE:
                preflight = new FilePreflight(source, outcome, inputs);
                break;
            case AWSS3:
            case IBMCOS:
                preflight = new S3Preflight(source, outcome, inputs);
                break;
            }
        }
        return preflight;
    }
}