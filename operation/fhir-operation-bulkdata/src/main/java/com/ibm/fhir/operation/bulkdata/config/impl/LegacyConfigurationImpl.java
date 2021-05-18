/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.config.impl;

import java.util.Collections;
import java.util.List;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.core.FHIRConstants;

/**
 * Configuration Prior to 4.6, and it will be phased out.
 */
@Deprecated
public class LegacyConfigurationImpl extends AbstractSystemConfigurationImpl {

    @Override
    public boolean legacy() {
        return true;
    }

    @Override
    public String getCoreApiBatchUrl() {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/batch-uri", null);
    }

    @Override
    public String getCoreApiBatchUser() {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/batch-user", null);
    }

    @Override
    public String getCoreApiBatchPassword() {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/batch-user-password", null);
    }

    @Override
    public String getCoreApiBatchTrustStore() {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/batch-truststore", null);
    }

    @Override
    public String getCoreApiBatchTrustStorePassword() {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/batch-truststore-password", null);
    }

    @Override
    public int getCoreCosObjectResourceCountThreshold() {
        final String PATH = "fhirServer/bulkdata/cosFileMaxResources";
        return FHIRConfigHelper.getIntProperty(PATH, DEFAULT_COS_OBJ_MAX_RESOURCE_COUNT);
    }

    @Override
    public long getCoreCosObjectSizeThreshold() {
        final String PATH = "fhirServer/bulkdata/cosFileMaxSize";
        return FHIRConfigHelper.getIntProperty(PATH, DEFAULT_COS_OBJ_MAX_SIZE_MB * 1024 * 1024);
    }

    @Override
    public int getCoreFileResourceCountThreshold() {
        final String PATH = "fhirServer/bulkdata/cosFileMaxResources";
        return FHIRConfigHelper.getIntProperty(PATH, DEFAULT_FILE_MAX_RESOURCE_COUNT);
    }

    @Override
    public long getCoreFileSizeThreshold() {
        final String PATH = "fhirServer/bulkdata/cosFileMaxSize";
        return FHIRConfigHelper.getIntProperty(PATH, DEFAULT_FILE_MAX_SIZE_MB * 1024 * 1024);
    }

    @Override
    public int getCorePageSize() {
        int pageSize = FHIRConfigHelper.getIntProperty("fhirServer/bulkdata/patientExportPageSize", FHIRConstants.FHIR_PAGE_SIZE_DEFAULT_MAX);
        return Math.min(FHIRConstants.FHIR_PAGE_SIZE_DEFAULT_MAX, pageSize);
    }

    @Override
    public String getCoreBatchIdEncryptionKey() {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/bulkDataBatchJobIdEncryptionKey", null);
    }

    @Override
    public String getStorageProviderType(String provider) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/type", "ibm-cos");
    }

    @Override
    public List<String> getStorageProviderValidBaseUrls(String provider) {
        final String PATH = "fhirServer/bulkdata/validBaseUrls";
        List<String> results = FHIRConfigHelper.getStringListProperty(PATH);
        if (results == null) {
            results = Collections.emptyList();
        }
        return results;
    }

    @Override
    public String getStorageProviderBucketName(String provider) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/jobParameters/cos.bucket.name", null);
    }

    @Override
    public String getStorageProviderLocation(String provider) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/jobParameters/cos.location", null);
    }

    @Override
    public String getStorageProviderEndpointInternal(String provider) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/jobParameters/cos.endpoint.internal", null);
    }

    @Override
    public String getStorageProviderEndpointExternal(String provider) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/jobParameters/cos.endpoint.external", null);
    }

    @Override
    public String getStorageProviderAuthType(String provider) {
        String iam = FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/jobParameters/cos.credential.ibm", "N");
        String auth = "hmac";
        if ("Y".equalsIgnoreCase(iam)) {
            auth = "iam";
        }
        return auth;
    }

    @Override
    public boolean isStorageProviderAuthTypeIam(String provider) {
        String iam = FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/jobParameters/cos.credential.ibm", "N");
        return "Y".equalsIgnoreCase(iam);
    }

    @Override
    public String getStorageProviderAuthTypeIamApiKey(String provider) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/jobParameters/cos.api.key", null);
    }

    @Override
    public String getStorageProviderAuthTypeIamApiResourceInstanceId(String provider) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/jobParameters/cos.srvinst.id", null);
    }

    @Override
    public boolean isStorageProviderAuthTypeHmac(String provider) {
        String iam = FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/jobParameters/cos.credential.ibm", "N");
        return !"Y".equalsIgnoreCase(iam);
    }

    @Override
    public String getStorageProviderAuthTypeHmacAccessKey(String provider) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/jobParameters/cos.api.key", null);
    }

    @Override
    public String getStorageProviderAuthTypeHmacSecretKey(String provider) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/jobParameters/cos.srvinst.id", null);
    }

    @Override
    public boolean isStorageProviderAuthTypeBasic(String provider) {
        String iam = FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/jobParameters/cos.credential.ibm", "N");
        return !"Y".equalsIgnoreCase(iam);
    }

    @Override
    public String getStorageProviderAuthTypeUsername(String provider) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/jobParameters/cos.api.key", null);
    }

    @Override
    public String getStorageProviderAuthTypePassword(String provider) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/jobParameters/cos.srvinst.id", null);
    }

    @Override
    public boolean isStorageProviderParquetEnabled(String provider) {
        return FHIRConfigHelper.getBooleanProperty("fhirServer/bulkdata/enableParquet", Boolean.FALSE);
    }

    @Override
    public boolean shouldStorageProviderValidateBaseUrl(String provider) {
        return !FHIRConfigHelper.getBooleanProperty("fhirServer/bulkdata/validBaseUrlsDisabled", Boolean.FALSE);
    }

    @Override
    public boolean isStorageProviderExportPublic(String provider) {
        return FHIRConfigHelper.getBooleanProperty("fhirServer/bulkdata/isExportPublic", Boolean.FALSE);
    }

    @Override
    public boolean shouldStorageProviderCollectOperationOutcomes(String provider) {
        return FHIRConfigHelper.getBooleanProperty("fhirServer/bulkdata/ignoreImportOutcomes", Boolean.FALSE);
    }

    @Override
    public boolean shouldStorageProviderCheckDuplicate(String provider) {
        return false;
    }

    @Override
    public boolean shouldStorageProviderValidateResources(String provider) {
        return true;
    }

    @Override
    public boolean shouldStorageProviderCreate(String provider) {
        return false;
    }

    @Override
    public int getInputLimit() {
        return FHIRConfigHelper.getIntProperty("fhirServer/bulkdata/maxInputPerRequest", 5);
    }

    @Override
    public boolean isFastExport() {
        String type = FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/systemExportImpl", "fast");
        return "fast".equals(type);
    }

    @Override
    public boolean isStorageProviderHmacPresigned(String provider) {
        return false;
    }
}