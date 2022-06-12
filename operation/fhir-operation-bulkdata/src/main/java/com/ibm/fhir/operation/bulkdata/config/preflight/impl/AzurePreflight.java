/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.config.preflight.impl;

import static com.ibm.fhir.operation.bulkdata.util.CommonUtil.buildExceptionWithIssue;

import java.util.List;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.operation.bulkdata.OperationConstants;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.operation.bulkdata.model.type.Input;
import com.ibm.fhir.operation.bulkdata.model.type.StorageDetail;
import com.ibm.fhir.operation.bulkdata.model.type.StorageType;
import com.ibm.fhir.operation.bulkdata.util.BulkDataExportUtil;

/**
 * Verifies the Export/Import is valid for Azure
 */
public class AzurePreflight extends NopPreflight {
    private static final BulkDataExportUtil export = new BulkDataExportUtil();

    public AzurePreflight(String source, String outcome, List<Input> inputs, OperationConstants.ExportType exportType, String format) {
        super(source, outcome, inputs, exportType, format);
    }

    @Override
    public void preflight() throws FHIROperationException {
        super.preflight();
        validate(getSource());
        validate(getOutcome());
    }


    /**
     * validates the azure provider is properly configured.
     *
     * @param storageProvider
     * @throws FHIROperationException
     */
    public void validate(String storageProvider) throws FHIROperationException {
        ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
        if (adapter.isStorageProviderAuthTypeConnectionString(storageProvider)) {
            String conn = adapter.getStorageProviderAuthTypeConnectionString(storageProvider);
            if (conn == null || conn.isEmpty()) {
                throw export.buildOperationException("bad configuration for the Azure Blob Container's connection configuration", IssueType.EXCEPTION);
            }
        } else {
            throw export.buildOperationException("Failed to specify the source or outcome container's authentication mechanism", IssueType.EXCEPTION);
        }

        // Used to get the Azure Container
        if (adapter.getStorageProviderBucketName(storageProvider) == null || adapter.getStorageProviderBucketName(storageProvider).isEmpty()) {
            throw export.buildOperationException("bad configuration for the basic configuration with bucketname", IssueType.EXCEPTION);
        }
    }

    @Override
    public void checkStorageAllowed(StorageDetail storageDetail) throws FHIROperationException {
        if (storageDetail != null && !StorageType.AZURE.value().equals(storageDetail.getType())){
            throw buildExceptionWithIssue("Azure: Configuration not set to import from storageDetail '" + getSource() + "'", IssueType.INVALID);
        }
    }
}