/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.client;

import static org.testng.Assert.assertNotNull;

import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.model.type.StorageType;

/**
 * Test Bulk Data Client
 */
public class BulkDataClientTest {

    private static ConfigurationAdapter CONFIGURATION_ADAPTER = generateTestConfigurationAdapter();

    @BeforeClass
    public void setup() {
        FHIRConfiguration.setConfigHome("target/test-classes/testdata");
    }

    @Test(enabled = false)
    public void testBulkDataClient() throws Exception {
        String bulkdataSource = "default";
        String outcomeSource = "default";
        String incomingUrl = "https://localhost:9443/fhir-server/api/v4/$buldata-status";
        String baseUri = "https://baseuri/";

        BulkDataClient client = new BulkDataClient(bulkdataSource, outcomeSource, incomingUrl, baseUri, CONFIGURATION_ADAPTER);

        assertNotNull(client);
    }

    private static ConfigurationAdapter generateTestConfigurationAdapter() {
        return new ConfigurationAdapter() {

            @Override
            public String getApplicationName() {

                return null;
            }

            @Override
            public String getModuleName() {

                return null;
            }

            @Override
            public String getJobXMLName() {

                return null;
            }

            @Override
            public void registerRequestContext(String tenantId, String datastoreId, String incomingUrl) throws FHIRException {

            }

            @Override
            public boolean legacy() {

                return false;
            }

            @Override
            public boolean enabled() {

                return false;
            }

            @Override
            public String getCoreApiBatchUrl() {

                return null;
            }

            @Override
            public String getCoreApiBatchUser() {

                return null;
            }

            @Override
            public String getCoreApiBatchPassword() {

                return null;
            }

            @Override
            public String getCoreApiBatchTrustStore() {

                return null;
            }

            @Override
            public String getCoreApiBatchTrustStorePassword() {

                return null;
            }

            @Override
            public int getCoreCosMaxResources() {

                return 0;
            }

            @Override
            public int getCoreCosMinSize() {

                return 0;
            }

            @Override
            public int getCoreCosMaxSize() {

                return 0;
            }

            @Override
            public boolean shouldCoreCosUseServerTruststore() {

                return false;
            }

            @Override
            public int getCoreCosRequestTimeout() {

                return 0;
            }

            @Override
            public int getCoreCosSocketTimeout() {

                return 0;
            }

            @Override
            public int getCorePageSize() {

                return 0;
            }

            @Override
            public String getCoreBatchIdEncryptionKey() {

                return null;
            }

            @Override
            public int getCoreMaxPartitions() {

                return 0;
            }

            @Override
            public String getCoreIamEndpoint() {

                return null;
            }

            @Override
            public int getCoreFastTxTimeout() {

                return 0;
            }

            @Override
            public String getSourceType(String source) {

                return null;
            }

            @Override
            public List<String> getSourceValidBaseUrls(String source) {

                return null;
            }

            @Override
            public String getSourceBucketName(String source) {

                return null;
            }

            @Override
            public String getSourceLocation(String source) {

                return null;
            }

            @Override
            public String getSourceEndpointInternal(String source) {

                return null;
            }

            @Override
            public String getSourceEndpointExternal(String source) {

                return null;
            }

            @Override
            public String getSourceAuthType(String source) {

                return null;
            }

            @Override
            public boolean isSourceAuthTypeIam(String source) {

                return false;
            }

            @Override
            public String getSourceAuthTypeIamApiKey(String source) {

                return null;
            }

            @Override
            public String getSourceAuthTypeIamApiResourceInstanceId(String source) {

                return null;
            }

            @Override
            public boolean isSourceAuthTypeHmac(String source) {

                return false;
            }

            @Override
            public String getSourceAuthTypeHmacAccessKey(String source) {

                return null;
            }

            @Override
            public String getSourceAuthTypeHmacSecretKey(String source) {

                return null;
            }

            @Override
            public boolean isSourceAuthTypeBasic(String source) {

                return false;
            }

            @Override
            public String getSourceAuthTypeUsername(String source) {

                return null;
            }

            @Override
            public String getSourceAuthTypePassword(String source) {

                return null;
            }

            @Override
            public boolean isSourceParquetEnabled(String source) {

                return false;
            }

            @Override
            public boolean shouldSourceValidateBaseUrl(String source) {

                return false;
            }

            @Override
            public boolean isSourceExportPublic(String source) {

                return false;
            }

            @Override
            public boolean shouldSourceCollectOperationOutcomes(String source) {

                return false;
            }

            @Override
            public boolean shouldSourceCheckDuplicate(String source) {

                return false;
            }

            @Override
            public boolean shouldSourceValidateResources(String source) {

                return false;
            }

            @Override
            public boolean shouldSourceCreate(String source) {

                return false;
            }

            @Override
            public boolean isStorageTypeAllowed(String storageType) {

                return false;
            }

            @Override
            public StorageType getSourceStorageType(String type) {

                return null;
            }

            @Override
            public int getInputLimit() {

                return 0;
            }

            @Override
            public String getTenant() {

                return null;
            }

            @Override
            public boolean isFastExport() {

                return false;
            }

            @Override
            public boolean getCoreCosTcpKeepAlive() {

                return false;
            }

            @Override
            public String getBaseFileLocation(String source) {

                return null;
            }

            @Override
            public boolean isSourceHmacPresigned(String source) {

                return false;
            }

            @Override
            public boolean shouldCoreApiBatchTrustAll() {

                return false;
            }
        };
    }
}