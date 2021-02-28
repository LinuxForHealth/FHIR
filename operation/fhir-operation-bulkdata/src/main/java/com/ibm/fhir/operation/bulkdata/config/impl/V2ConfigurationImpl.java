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
    public String getSourceType(String source) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/source/" + source + "/type", "ibm-cos");
    }

    @Override
    public List<String> getSourceValidBaseUrls(String source) {
        final String PATH = "fhirServer/bulkdata/source/" + source + "/valid-base-urls";
        List<String> results = FHIRConfigHelper.getStringListProperty(PATH);
        if (results == null) {
            results = Collections.emptyList();
        }
        return results;
    }

    @Override
    public String getSourceBucketName(String source) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/source/" + source + "/bucket-name", null);
    }

    @Override
    public String getSourceLocation(String source) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/source/" + source + "/location", null);
    }

    @Override
    public String getSourceEndpointInternal(String source) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/source/" + source + "/endpoint-internal", null);
    }

    @Override
    public String getSourceEndpointExternal(String source) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/source/" + source + "/endpoint-external", null);
    }

    @Override
    public String getSourceAuthType(String source) {
        String path = "fhirServer/bulkdata/source/" + source + "/auth/type";
        return FHIRConfigHelper.getStringProperty(path, null);
    }

    @Override
    public boolean isSourceAuthTypeIam(String source) {
        String auth = getSourceAuthType(source);
        return "iam".equalsIgnoreCase(auth);
    }

    @Override
    public String getSourceAuthTypeIamApiKey(String source) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/source/" + source + "/auth/iam-api-key", null);
    }

    @Override
    public String getSourceAuthTypeIamApiResourceInstanceId(String source) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/source/" + source + "/auth/iam-resource-instance-id", null);
    }

    @Override
    public boolean isSourceAuthTypeHmac(String source) {
        String auth = getSourceAuthType(source);
        return "hmac".equalsIgnoreCase(auth);
    }

    @Override
    public String getSourceAuthTypeHmacAccessKey(String source) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/source/" + source + "/auth/access_key_id", null);
    }

    @Override
    public String getSourceAuthTypeHmacSecretKey(String source) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/source/" + source + "/auth/secret_access_key", null);
    }

    @Override
    public boolean isSourceAuthTypeBasic(String source) {
        String auth = getSourceAuthType(source);
        return "basic".equalsIgnoreCase(auth);
    }

    @Override
    public String getSourceAuthTypeUsername(String source) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/source/" + source + "/auth/username", null);
    }

    @Override
    public String getSourceAuthTypePassword(String source) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/source/" + source + "/auth/password", null);
    }

    @Override
    public boolean isSourceParquetEnabled(String source) {
        return FHIRConfigHelper.getBooleanProperty("fhirServer/bulkdata/source/" + source + "/enableParquet", Boolean.FALSE);
    }

    @Override
    public boolean shouldSourceValidateBaseUrl(String source) {
        return FHIRConfigHelper.getBooleanProperty("fhirServer/bulkdata/source/" + source + "/disable-base-url-validation", Boolean.FALSE);
    }

    @Override
    public boolean isSourceExportPublic(String source) {
        return FHIRConfigHelper.getBooleanProperty("fhirServer/bulkdata/source/" + source + "/export-public", Boolean.FALSE);
    }

    @Override
    public boolean shouldSourceCollectOperationOutcomes(String source) {
        return FHIRConfigHelper.getBooleanProperty("fhirServer/bulkdata/source/" + source + "/disable-operation-outcomes", Boolean.FALSE);
    }

    @Override
    public boolean shouldSourceCheckDuplicate(String source) {
        return FHIRConfigHelper.getBooleanProperty("fhirServer/bulkdata/source/" + source + "/duplication-check", Boolean.TRUE);
    }

    @Override
    public boolean shouldSourceValidateResources(String source) {
        return FHIRConfigHelper.getBooleanProperty("fhirServer/bulkdata/source/" + source + "/validate-resources", Boolean.TRUE);
    }

    @Override
    public boolean shouldSourceCreate(String source) {
        return FHIRConfigHelper.getBooleanProperty("fhirServer/bulkdata/source/" + source + "/create", Boolean.FALSE);
    }

    @Override
    public boolean isFastExport() {
        String type = FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/systemExportImpl", null);
        return "fast".equals(type);
    }
}