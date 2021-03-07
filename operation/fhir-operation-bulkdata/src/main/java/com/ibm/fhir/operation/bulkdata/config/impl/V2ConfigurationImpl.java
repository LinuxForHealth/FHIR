/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.config.impl;

import java.util.Collections;
import java.util.List;

import com.ibm.fhir.config.FHIRConfigHelper;

/**
 * Starting with 4.6
 */
public class V2ConfigurationImpl extends AbstractSystemConfigurationImpl {

    @Override
    public boolean legacy() {
        return false;
    }

    @Override
    public String getCoreApiBatchUrl() {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/core/api/url", null);
    }

    @Override
    public String getCoreApiBatchUser() {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/core/api/user", null);
    }

    @Override
    public String getCoreApiBatchPassword() {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/core/api/password", null);
    }

    @Override
    public String getCoreApiBatchTrustStore() {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/core/api/truststore", null);
    }

    @Override
    public String getCoreApiBatchTrustStorePassword() {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/core/api/truststore-password", null);
    }

    @Override
    public String getStorageProviderType(String provider) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/storageProviders/" + provider + "/type", "ibm-cos");
    }

    @Override
    public List<String> getStorageProviderValidBaseUrls(String provider) {
        final String PATH = "fhirServer/bulkdata/storageProviders/" + provider + "/valid-base-urls";
        List<String> results = FHIRConfigHelper.getStringListProperty(PATH);
        if (results == null) {
            results = Collections.emptyList();
        }
        return results;
    }

    @Override
    public String getStorageProviderBucketName(String provider) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/storageProviders/" + provider + "/bucket-name", null);
    }

    @Override
    public String getStorageProviderLocation(String provider) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/storageProviders/" + provider + "/location", null);
    }

    @Override
    public String getStorageProviderEndpointInternal(String provider) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/storageProviders/" + provider + "/endpoint-internal", null);
    }

    @Override
    public String getStorageProviderEndpointExternal(String provider) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/storageProviders/" + provider + "/endpoint-external", null);
    }

    @Override
    public String getStorageProviderAuthType(String provider) {
        String path = "fhirServer/bulkdata/storageProviders/" + provider + "/auth/type";
        return FHIRConfigHelper.getStringProperty(path, null);
    }

    @Override
    public boolean isStorageProviderAuthTypeIam(String provider) {
        String auth = getStorageProviderAuthType(provider);
        return "iam".equalsIgnoreCase(auth);
    }

    @Override
    public String getStorageProviderAuthTypeIamApiKey(String provider) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/storageProviders/" + provider + "/auth/iam-api-key", null);
    }

    @Override
    public String getStorageProviderAuthTypeIamApiResourceInstanceId(String provider) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/storageProviders/" + provider + "/auth/iam-resource-instance-id", null);
    }

    @Override
    public boolean isStorageProviderAuthTypeHmac(String provider) {
        String auth = getStorageProviderAuthType(provider);
        return "hmac".equalsIgnoreCase(auth);
    }

    @Override
    public String getStorageProviderAuthTypeHmacAccessKey(String provider) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/storageProviders/" + provider + "/auth/access_key_id", null);
    }

    @Override
    public String getStorageProviderAuthTypeHmacSecretKey(String provider) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/storageProviders/" + provider + "/auth/secret_access_key", null);
    }

    @Override
    public boolean isStorageProviderAuthTypeBasic(String provider) {
        String auth = getStorageProviderAuthType(provider);
        return "basic".equalsIgnoreCase(auth);
    }

    @Override
    public String getStorageProviderAuthTypeUsername(String provider) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/storageProviders/" + provider + "/auth/username", null);
    }

    @Override
    public String getStorageProviderAuthTypePassword(String provider) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/storageProviders/" + provider + "/auth/password", null);
    }

    @Override
    public boolean isStorageProviderParquetEnabled(String provider) {
        return FHIRConfigHelper.getBooleanProperty("fhirServer/bulkdata/storageProviders/" + provider + "/enableParquet", Boolean.FALSE);
    }

    @Override
    public boolean shouldStorageProviderValidateBaseUrl(String provider) {
        return FHIRConfigHelper.getBooleanProperty("fhirServer/bulkdata/storageProviders/" + provider + "/disable-base-url-validation", Boolean.FALSE);
    }

    @Override
    public boolean isStorageProviderExportPublic(String provider) {
        return FHIRConfigHelper.getBooleanProperty("fhirServer/bulkdata/storageProviders/" + provider + "/export-public", Boolean.FALSE);
    }

    @Override
    public boolean shouldStorageProviderCollectOperationOutcomes(String provider) {
        return FHIRConfigHelper.getBooleanProperty("fhirServer/bulkdata/storageProviders/" + provider + "/disable-operation-outcomes", Boolean.FALSE);
    }

    @Override
    public boolean shouldStorageProviderCheckDuplicate(String provider) {
        return FHIRConfigHelper.getBooleanProperty("fhirServer/bulkdata/storageProviders/" + provider + "/duplication-check", Boolean.TRUE);
    }

    @Override
    public boolean shouldStorageProviderValidateResources(String provider) {
        return FHIRConfigHelper.getBooleanProperty("fhirServer/bulkdata/storageProviders/" + provider + "/validate-resources", Boolean.TRUE);
    }

    @Override
    public boolean shouldStorageProviderCreate(String provider) {
        return FHIRConfigHelper.getBooleanProperty("fhirServer/bulkdata/storageProviders/" + provider + "/create", Boolean.FALSE);
    }

    @Override
    public boolean isFastExport() {
        String type = FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/core/systemExportImpl", "fast");
        return "fast".equals(type);
    }
}