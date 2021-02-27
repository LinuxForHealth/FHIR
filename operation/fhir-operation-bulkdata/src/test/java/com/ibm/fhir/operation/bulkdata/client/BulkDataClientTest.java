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
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String getModuleName() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String getJobXMLName() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void registerRequestContext(String tenantId, String datastoreId, String incomingUrl) throws FHIRException {
                // TODO Auto-generated method stub

            }

            @Override
            public boolean legacy() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean enabled() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public String getCoreApiBatchUrl() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String getCoreApiBatchUser() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String getCoreApiBatchPassword() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String getCoreApiBatchTrustStore() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String getCoreApiBatchTrustStorePassword() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public int getCoreCosMaxResources() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public int getCoreCosMinSize() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public int getCoreCosMaxSize() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public boolean shouldCoreCosUseServerTruststore() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public int getCoreCosRequestTimeout() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public int getCoreCosSocketTimeout() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public int getCorePageSize() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public String getCoreBatchIdEncryptionKey() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public int getCoreMaxPartitions() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public String getCoreIamEndpoint() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public int getCoreFastTxTimeout() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public String getSourceType(String source) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public List<String> getSourceValidBaseUrls(String source) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String getSourceBucketName(String source) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String getSourceLocation(String source) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String getSourceEndpointInternal(String source) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String getSourceEndpointExternal(String source) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String getSourceAuthType(String source) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public boolean isSourceAuthTypeIam(String source) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public String getSourceAuthTypeIamApiKey(String source) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String getSourceAuthTypeIamApiResourceInstanceId(String source) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public boolean isSourceAuthTypeHmac(String source) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public String getSourceAuthTypeHmacAccessKey(String source) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String getSourceAuthTypeHmacSecretKey(String source) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public boolean isSourceAuthTypeBasic(String source) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public String getSourceAuthTypeUsername(String source) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String getSourceAuthTypePassword(String source) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public boolean isSourceParquetEnabled(String source) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean shouldSourceValidateBaseUrl(String source) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean isSourceExportPublic(String source) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean shouldSourceCollectOperationOutcomes(String source) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean shouldSourceCheckDuplicate(String source) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean shouldSourceValidateResources(String source) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean shouldSourceCreate(String source) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean isStorageTypeAllowed(String storageType) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean checkValidFileBase(String source, String fileName) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public StorageType getSourceStorageType(String type) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public int getInputLimit() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public String getTenant() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public boolean isFastExport() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean getCoreCosTcpKeepAlive() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public String getBaseFileLocation(String source) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public boolean isSourceHmacPresigned(String source) {
                // TODO Auto-generated method stub
                return false;
            }
        };
    }
}