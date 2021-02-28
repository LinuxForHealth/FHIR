/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.bulkdata.config.impl;

import java.util.Collections;
import java.util.List;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.search.SearchConstants;

/**
 * Configuration Prior to 4.6
 */
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
    public int getCoreCosMaxResources() {
        final String PATH = "fhirServer/bulkdata/cosFileMaxResources";
        return FHIRConfigHelper.getIntProperty(PATH, DEFAULT_COSFILE_MAX_RESOURCESNUMBER);
    }

    @Override
    public int getCoreCosMaxSize() {
        final String PATH = "fhirServer/bulkdata/cosFileMaxSize";
        return FHIRConfigHelper.getIntProperty(PATH, DEFAULT_COSFILE_MAX_SIZE);
    }

    @Override
    public int getCorePageSize() {
        int pageSize = FHIRConfigHelper.getIntProperty("fhirServer/bulkdata/patientExportPageSize", SearchConstants.MAX_PAGE_SIZE);
        return Math.min(SearchConstants.MAX_PAGE_SIZE, pageSize);
    }

    @Override
    public String getCoreBatchIdEncryptionKey() {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/bulkDataBatchJobIdEncryptionKey", null);
    }

    @Override
    public String getSourceType(String source) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/type", "ibm-cos");
    }

    @Override
    public List<String> getSourceValidBaseUrls(String source) {
        final String PATH = "fhirServer/bulkdata/validBaseUrls";
        List<String> results = FHIRConfigHelper.getStringListProperty(PATH);
        if (results == null) {
            results = Collections.emptyList();
        }
        return results;
    }

    @Override
    public String getSourceBucketName(String source) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/jobParameters/cos.bucket.name", null);
    }

    @Override
    public String getSourceLocation(String source) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/jobParameters/cos.location", null);
    }

    @Override
    public String getSourceEndpointInternal(String source) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/jobParameters/cos.endpoint.internal", null);
    }

    @Override
    public String getSourceEndpointExternal(String source) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/jobParameters/cos.endpoint.external", null);
    }

    @Override
    public String getSourceAuthType(String source) {
        String iam = FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/jobParameters/cos.credential.ibm", "N");
        String auth = "hmac";
        if ("Y".equalsIgnoreCase(iam)) {
            auth = "iam";
        }
        return auth;
    }

    @Override
    public boolean isSourceAuthTypeIam(String source) {
        String iam = FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/jobParameters/cos.credential.ibm", "N");
        return "Y".equalsIgnoreCase(iam);
    }

    @Override
    public String getSourceAuthTypeIamApiKey(String source) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/jobParameters/cos.api.key", null);
    }

    @Override
    public String getSourceAuthTypeIamApiResourceInstanceId(String source) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/jobParameters/cos.srvinst.id", null);
    }

    @Override
    public boolean isSourceAuthTypeHmac(String source) {
        String iam = FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/jobParameters/cos.credential.ibm", "N");
        return !"Y".equalsIgnoreCase(iam);
    }

    @Override
    public String getSourceAuthTypeHmacAccessKey(String source) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/jobParameters/cos.api.key", null);
    }

    @Override
    public String getSourceAuthTypeHmacSecretKey(String source) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/jobParameters/cos.srvinst.id", null);
    }

    @Override
    public boolean isSourceAuthTypeBasic(String source) {
        String iam = FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/jobParameters/cos.credential.ibm", "N");
        return !"Y".equalsIgnoreCase(iam);
    }

    @Override
    public String getSourceAuthTypeUsername(String source) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/jobParameters/cos.api.key", null);
    }

    @Override
    public String getSourceAuthTypePassword(String source) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/jobParameters/cos.srvinst.id", null);
    }

    @Override
    public boolean isSourceParquetEnabled(String source) {
        return FHIRConfigHelper.getBooleanProperty("fhirServer/bulkdata/enableParquet", Boolean.FALSE);
    }

    @Override
    public boolean shouldSourceValidateBaseUrl(String source) {
        return !FHIRConfigHelper.getBooleanProperty("fhirServer/bulkdata/validBaseUrlsDisabled", Boolean.FALSE);
    }

    @Override
    public boolean isSourceExportPublic(String source) {
        return FHIRConfigHelper.getBooleanProperty("fhirServer/bulkdata/isExportPublic", Boolean.FALSE);
    }

    @Override
    public boolean shouldSourceCollectOperationOutcomes(String source) {
        return FHIRConfigHelper.getBooleanProperty("fhirServer/bulkdata/ignoreImportOutcomes", Boolean.FALSE);
    }

    @Override
    public boolean shouldSourceCheckDuplicate(String source) {
        return false;
    }

    @Override
    public boolean shouldSourceValidateResources(String source) {
        return true;
    }

    @Override
    public boolean shouldSourceCreate(String source) {
        return false;
    }

    @Override
    public int getInputLimit() {
        return FHIRConfigHelper.getIntProperty("fhirServer/bulkdata/maxInputPerRequest", 5);
    }

    @Override
    public boolean isFastExport() {
        String type = FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/systemExportImpl", null);
        return "fast".equals(type);
    }

    @Override
    public boolean isSourceHmacPresigned(String source) {
        return false;
    }
}