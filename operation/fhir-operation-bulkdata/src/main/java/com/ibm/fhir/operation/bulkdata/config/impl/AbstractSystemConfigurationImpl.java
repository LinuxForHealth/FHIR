/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.config.impl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.operation.bulkdata.OperationConstants;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.model.type.StorageType;
import com.ibm.fhir.search.SearchConstants;

/**
 * Common between the Legacy and V2 Configuration and Implementation
 * Also the system defaults.
 */
public abstract class AbstractSystemConfigurationImpl implements ConfigurationAdapter {

    private static final String CLASSNAME = AbstractSystemConfigurationImpl.class.getName();
    private static final Logger logger = Logger.getLogger(CLASSNAME);

    private static final String APPLICATION_NAME = "fhir-bulkdata-webapp";
    private static final String MODULE_NAME = "fhir-bulkdata-webapp.war";
    private static final String JOB_XML_NAME = "jobXMLName";

    private static final String IAM_ENDPOINT = "https://iam.cloud.ibm.com/oidc/token";

    // gets the maximum number of current threads that are supported in the bulkdata processing.
    private static final int MAX_PARTITIONPROCESSING_THREADNUMBER = 5;

    // The minimal size (10MiB) for COS multiple-parts upload (NDJSON-only).
    // 10M 10485760;
    private static final int COS_PART_MINIMALSIZE = 10;

    // The threshold size (200MiB) for when to start writing to a new file (NDJSON-only).
    protected static final int DEFAULT_COSFILE_MAX_SIZE = 209715200;

    // The number of resources at which the server will start a new file for the next page of results (NDJSON and
    // Parquet). 200,000 at 1 KB/file would lead to roughly 200 MB files; similar to the DEFAULT_COSFILE_MAX_SIZE.
    protected static final int DEFAULT_COSFILE_MAX_RESOURCESNUMBER = 200000;

    private static final String FHIR_BULKDATA_ALLOWED_TYPES = "FHIR_BULKDATA_ALLOWED_TYPES";
    private static final Set<String> ALLOWED_STORAGE_TYPES = determineAllowedStorageType();

    private static final byte[] NDJSON_LINESEPERATOR = "\r\n".getBytes();

    public static final int IMPORT_NUMOFFHIRRESOURCES_PERREAD = 20;
    public static final int IMPORT_INFLY_RATE_NUMOFFHIRRESOURCES = 2000;

    // The following are set on startup:
    private static final long coreCosMaxResources = defaultCoreCosMaxResources();
    private static final long coreCosMinSize = defaultCoreCosMinSize();
    private static final long coreCosMaxSize = defaultCoreCosMaxSize();
    private static final boolean coreCosUseServerTruststore = defaultCoreCosUseServerTruststore();
    private static final int coreCosRequestTimeout = defaultCoreCosRequestTimeout();
    private static final int coreCosSocketTimeout = defaultCoreCosSocketTimeout();
    private static final String coreBatchIdEncryptionKey = defaultCoreBatchIdEncryptionKey();
    private static final int coreMaxParititions = defaultCoreMaxParititions();
    private static final int inputLimits = defaultInputLimits();

    @Override
    public boolean getCoreCosTcpKeepAlive() {
        return true;
    }

    @Override
    public int getCoreMaxPartitions() {
        return coreMaxParititions;
    }

    private static final int defaultCoreMaxParititions() {
        return FHIRConfigHelper.getIntProperty("fhirServer/bulkdata/core/max-partitions", MAX_PARTITIONPROCESSING_THREADNUMBER);
    }

    @Override
    public String getCoreBatchIdEncryptionKey() {
        return coreBatchIdEncryptionKey;
    }

    private static final String defaultCoreBatchIdEncryptionKey() {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/core/batch-id-encryption-key", null);
    }

    @Override
    public int getCoreCosSocketTimeout() {
        return coreCosSocketTimeout;
    }

    private static final int defaultCoreCosSocketTimeout() {
        return FHIRConfigHelper.getIntProperty("fhirServer/bulkdata/core/cos/socket-timeout", 12000);
    }

    @Override
    public int getInputLimit() {
        return inputLimits;
    }

    private static final int defaultInputLimits() {
        return FHIRConfigHelper.getIntProperty("fhirServer/bulkdata/core/max-inputs", OperationConstants.IMPORT_MAX_DEFAULT_INPUTS);
    }

    @Override
    public int getCoreCosRequestTimeout() {
        return coreCosRequestTimeout;
    }

    private static final int defaultCoreCosRequestTimeout() {
        return FHIRConfigHelper.getIntProperty("fhirServer/bulkdata/core/cos/request-timeout", 10000);
    }

    @Override
    public boolean shouldCoreCosUseServerTruststore() {
        return coreCosUseServerTruststore;
    }

    private static final boolean defaultCoreCosUseServerTruststore() {
        return FHIRConfigHelper.getBooleanProperty("fhirServer/bulkdata/core/cos/use-server-truststore", Boolean.TRUE);
    }

    @Override
    public long getCoreCosThresholdSize() {
        return coreCosMaxSize;
    }

    private static final long defaultCoreCosMaxSize() {
        final String PATH = "fhirServer/bulkdata/core/cos/maxFileSizeThreshold";
        return 1024 * 1024 * FHIRConfigHelper.getIntProperty(PATH, DEFAULT_COSFILE_MAX_SIZE);
    }

    @Override
    public long getCoreCosMultiPartMinSize() {
        return coreCosMinSize;
    }

    private static final long defaultCoreCosMinSize() {
        return 1024 * 1024 * FHIRConfigHelper.getIntProperty("fhirServer/bulkdata/core/cos/minPartSize", COS_PART_MINIMALSIZE);
    }

    @Override
    public long getCoreCosMaxResources() {
        return coreCosMaxResources;
    }

    private static final long defaultCoreCosMaxResources() {
        final String PATH = "fhirServer/bulkdata/core/cos/maxResourcesThreshold";
        return 1000 * 1000 * FHIRConfigHelper.getIntProperty(PATH, DEFAULT_COSFILE_MAX_RESOURCESNUMBER);
    }

    @Override
    public String getApplicationName() {
        return APPLICATION_NAME;
    }

    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }

    @Override
    public String getJobXMLName() {
        return JOB_XML_NAME;
    }

    @Override
    public void registerRequestContext(String tenantId, String datastoreId, String incomingUrl) throws FHIRException {
        // Create a new FHIRRequestContext and set it on the current thread.
        FHIRRequestContext context = new FHIRRequestContext(tenantId, datastoreId);
        // Don't try using FHIRConfigHelper before setting the context!
        FHIRRequestContext.set(context);
        context.setOriginalRequestUri(incomingUrl);
        context.setBulk(true);
    }

    private static final Set<String> determineAllowedStorageType() {
        /*
         * Restricts the Allowed Storage Types.
         */
        Set<String> allowedStorageTypes = new HashSet<>();

        Map<String, String> envs = System.getenv();
        String env = envs.get(FHIR_BULKDATA_ALLOWED_TYPES);

        if (env != null) {
            String[] storageTypes = env.split(",");
            for (String storageType : storageTypes) {
                if (storageType != null && !storageType.isEmpty()) {
                    try {
                        StorageType type = StorageType.from(storageType);
                        allowedStorageTypes.add(type.value());
                    } catch (IllegalArgumentException iae) {
                        logger.warning("Invalid Storage Type passed in, skipping '" + storageType + "'");
                    }
                } else {
                    logger.warning("Empty BulkData StorageType passed in");
                }
            }
        } else {
            // We're allowing them all.
            for (StorageType t : StorageType.values()) {
                allowedStorageTypes.add(t.value());
            }
        }
        return allowedStorageTypes;
    }

    @Override
    public boolean enabled() {
        return FHIRConfigHelper.getBooleanProperty("fhirServer/bulkdata/enabled", Boolean.TRUE);
    }

    @Override
    public int getCorePageSize() {
        int pageSize = FHIRConfigHelper.getIntProperty("fhirServer/bulkdata/core/page-size", SearchConstants.MAX_PAGE_SIZE);
        return Math.min(SearchConstants.MAX_PAGE_SIZE, pageSize);
    }

    @Override
    public String getCoreIamEndpoint() {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/core/iam-endpoint", IAM_ENDPOINT);
    }

    @Override
    public int getCoreFastTxTimeout() {
        return FHIRConfigHelper.getIntProperty("fhirServer/bulkdata/core/fast-tx-timeout", MAX_PARTITIONPROCESSING_THREADNUMBER);
    }

    @Override
    public boolean isStorageTypeAllowed(String storageType) {
        return ALLOWED_STORAGE_TYPES.contains(storageType);
    }

    @Override
    public StorageType getStorageProviderStorageType(String provider) {
        String type = FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/storageProviders/" + provider + "/type", "none");
        return StorageType.from(type);
    }

    @Override
    public String getTenant() {
        return FHIRRequestContext.get().getTenantId();
    }

    @Override
    public String getBaseFileLocation(String provider) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/storageProviders/" + provider + "/file-base", null);
    }

    @Override
    public boolean isStorageProviderHmacPresigned(String provider) {
        return this.isStorageProviderAuthTypeHmac(provider)
                && FHIRConfigHelper.getBooleanProperty("fhirServer/bulkdata/storageProviders/" + provider + "/presigned", Boolean.FALSE);
    }

    @Override
    public boolean shouldCoreApiBatchTrustAll() {
        return FHIRConfigHelper.getBooleanProperty("fhirServer/bulkdata/core/api/trust-all", Boolean.FALSE);
    }

    @Override
    public byte[] getEndOfFileDelimiter(String provider) {
        return NDJSON_LINESEPERATOR;
    }

    @Override
    public int getImportNumberOfFhirResourcesPerRead(String provider) {
        // The number of resources to commit to DB in each batch, the slower the DB connection, the smaller
        // this value should be set.
        return IMPORT_NUMOFFHIRRESOURCES_PERREAD;
    }

    @Override
    public int getImportInflyRateNumberOfFhirResources(String provider) {
        return IMPORT_INFLY_RATE_NUMOFFHIRRESOURCES;
    }
}