/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.operation.bulkdata.config.impl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.config.FHIRConfigHelper;
import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.exception.FHIRException;
import org.linuxforhealth.fhir.operation.bulkdata.OperationConstants;
import org.linuxforhealth.fhir.operation.bulkdata.config.ConfigurationAdapter;
import org.linuxforhealth.fhir.operation.bulkdata.config.s3.S3HostStyle;
import org.linuxforhealth.fhir.operation.bulkdata.model.type.StorageType;

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

    // The default size (10MiB) at which to start writing a part for a COS multi-part upload (NDJSON-only).
    private static final int DEFAULT_COS_PART_MIN_SIZE_MB = 10;

    // The default size (200MiB) at which to finish writing a given COS object (NDJSON-only).
    protected static final int DEFAULT_COS_OBJ_MAX_SIZE_MB = 200;

    // The number of resources at which to finish writing a given COS object (NDJSON).
    // 200,000 at 1 KB/file would lead to roughly 200 MB files; similar to the DEFAULT_COS_OBJ_MAX_SIZE_MB.
    protected static final int DEFAULT_COS_OBJ_MAX_RESOURCE_COUNT = 200000;

    // The default size (200MiB) at which to finish writing a given AZURE object (NDJSON-only).
    protected static final int DEFAULT_AZURE_OBJ_MAX_SIZE_MB = 200;

    // The number of resources at which to finish writing a given AZURE object (NDJSON).
    // 200,000 at 1 KB/file would lead to roughly 200 MB files; similar to the COS
    protected static final int DEFAULT_AZURE_OBJ_MAX_RESOURCE_COUNT = 200000;

    // The default size (1MiB) at which to write to file (NDJSON-only).
    private static final int DEFAULT_FILE_WRITE_TRIGGER_SIZE_MB = 1;

    // The default size (200MiB) at which to finish writing a given file (NDJSON-only).
    protected static final int DEFAULT_FILE_MAX_SIZE_MB = 200;

    // The number of resources at which to finish writing a given file (NDJSON).
    // 200,000 at 1 KB/file would lead to roughly 200 MB files; similar to the DEFAULT_COS_OBJ_MAX_SIZE_MB.
    protected static final int DEFAULT_FILE_MAX_RESOURCE_COUNT = 200000;

    // The default number of resources per page
    protected static final int DEFAULT_PAGE_SIZE = 100;

    private static final String FHIR_BULKDATA_ALLOWED_TYPES = "FHIR_BULKDATA_ALLOWED_TYPES";
    private static final Set<String> ALLOWED_STORAGE_TYPES = determineAllowedStorageType();

    private static final byte[] NDJSON_LINESEPERATOR = "\r\n".getBytes();

    public static final int IMPORT_NUMOFFHIRRESOURCES_PERREAD = 50;
    public static final int IMPORT_INFLY_RATE_NUMOFFHIRRESOURCES = 2000;

    // The following are set on startup:
    private static final int coreCosObjectResourceCountThreshold = defaultCoreCosObjectResourceCountThreshold();
    private static final int coreCosPartUploadTriggerSize = defaultCoreCosPartUploadTriggerSize();
    private static final long coreCosObjectSizeThreshold = defaultCoreCosObjectSizeThreshold();
    private static final boolean coreCosUseServerTruststore = defaultCoreCosUseServerTruststore();
    private static final int coreCosRequestTimeout = defaultCoreCosRequestTimeout();
    private static final int coreCosSocketTimeout = defaultCoreCosSocketTimeout();

    private static final int coreAzureObjectResourceCountThreshold = defaultCoreAzureObjectResourceCountThreshold();
    private static final long coreAzureObjectSizeThreshold = defaultCoreAzureObjectSizeThreshold();

    private static final int coreFileResourceCountThreshold = defaultCoreFileResourceCountThreshold();
    private static final int coreFileWriteTriggerSize = defaultCoreFileWriteTriggerSize();
    private static final long coreFileSizeThreshold = defaultCoreFileSizeThreshold();
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
        return FHIRConfigHelper.getIntProperty("fhirServer/bulkdata/core/maxPartitions", MAX_PARTITIONPROCESSING_THREADNUMBER);
    }

    @Override
    public String getCoreBatchIdEncodingKey() {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/core/batchIdEncodingKey", null);
    }

    @Override
    public int getCoreCosSocketTimeout() {
        return coreCosSocketTimeout;
    }

    private static final int defaultCoreCosSocketTimeout() {
        return FHIRConfigHelper.getIntProperty("fhirServer/bulkdata/core/cos/socketTimeout", 12000);
    }

    @Override
    public int getInputLimit() {
        return inputLimits;
    }

    private static final int defaultInputLimits() {
        return FHIRConfigHelper.getIntProperty("fhirServer/bulkdata/core/maxInputs", OperationConstants.IMPORT_MAX_DEFAULT_INPUTS);
    }

    @Override
    public int getCoreCosRequestTimeout() {
        return coreCosRequestTimeout;
    }

    private static final int defaultCoreCosRequestTimeout() {
        return FHIRConfigHelper.getIntProperty("fhirServer/bulkdata/core/cos/requestTimeout", 10000);
    }

    @Override
    public boolean shouldCoreCosUseServerTruststore() {
        return coreCosUseServerTruststore;
    }

    private static final boolean defaultCoreCosUseServerTruststore() {
        return FHIRConfigHelper.getBooleanProperty("fhirServer/bulkdata/core/cos/useServerTruststore", Boolean.TRUE);
    }

    @Override
    public int getCoreCosPartUploadTriggerSize() {
        return coreCosPartUploadTriggerSize;
    }

    private static final int defaultCoreCosPartUploadTriggerSize() {
        return 1024 * 1024 * FHIRConfigHelper.getIntProperty("fhirServer/bulkdata/core/cos/partUploadTriggerSizeMB", DEFAULT_COS_PART_MIN_SIZE_MB);
    }

    @Override
    public long getCoreCosObjectSizeThreshold() {
        return coreCosObjectSizeThreshold;
    }

    private static final long defaultCoreCosObjectSizeThreshold() {
        final String PATH = "fhirServer/bulkdata/core/cos/objectSizeThresholdMB";
        return 1024l * 1024l * FHIRConfigHelper.getIntProperty(PATH, DEFAULT_COS_OBJ_MAX_SIZE_MB);
    }

    @Override
    public int getCoreCosObjectResourceCountThreshold() {
        return coreCosObjectResourceCountThreshold;
    }

    private static final int defaultCoreCosObjectResourceCountThreshold() {
        final String PATH = "fhirServer/bulkdata/core/cos/objectResourceCountThreshold";
        return FHIRConfigHelper.getIntProperty(PATH, DEFAULT_COS_OBJ_MAX_RESOURCE_COUNT);
    }

    @Override
    public long getCoreAzureObjectSizeThreshold() {
        return coreAzureObjectSizeThreshold;
    }

    private static final long defaultCoreAzureObjectSizeThreshold() {
        final String PATH = "fhirServer/bulkdata/core/azure/objectSizeThresholdMB";
        return 1024l * 1024l * FHIRConfigHelper.getIntProperty(PATH, DEFAULT_AZURE_OBJ_MAX_SIZE_MB);
    }

    @Override
    public int getCoreAzureObjectResourceCountThreshold() {
        return coreAzureObjectResourceCountThreshold;
    }

    private static final int defaultCoreAzureObjectResourceCountThreshold() {
        final String PATH = "fhirServer/bulkdata/core/azure/objectResourceCountThreshold";
        return FHIRConfigHelper.getIntProperty(PATH, DEFAULT_AZURE_OBJ_MAX_RESOURCE_COUNT);
    }

    @Override
    public int getCoreFileWriteTriggerSize() {
        return coreFileWriteTriggerSize;
    }

    private static final int defaultCoreFileWriteTriggerSize() {
        return 1024 * 1024 * FHIRConfigHelper.getIntProperty("fhirServer/bulkdata/core/file/writeTriggerSizeMB", DEFAULT_FILE_WRITE_TRIGGER_SIZE_MB);
    }

    @Override
    public long getCoreFileSizeThreshold() {
        return coreFileSizeThreshold;
    }

    private static final long defaultCoreFileSizeThreshold() {
        final String PATH = "fhirServer/bulkdata/core/file/sizeThresholdMB";
        return 1024l * 1024l * FHIRConfigHelper.getIntProperty(PATH, DEFAULT_FILE_MAX_SIZE_MB);
    }

    @Override
    public int getCoreFileResourceCountThreshold() {
        return coreFileResourceCountThreshold;
    }

    private static final int defaultCoreFileResourceCountThreshold() {
        final String PATH = "fhirServer/bulkdata/core/file/resourceCountThreshold";
        return FHIRConfigHelper.getIntProperty(PATH, DEFAULT_FILE_MAX_RESOURCE_COUNT);
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
        int pageSize = FHIRConfigHelper.getIntProperty("fhirServer/bulkdata/core/pageSize", DEFAULT_PAGE_SIZE);
        return Math.min(DEFAULT_PAGE_SIZE, pageSize);
    }

    @Override
    public String getCoreIamEndpoint() {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/core/iamEndpoint", IAM_ENDPOINT);
    }

    @Override
    public long getCoreFastMaxReadTimeout() {
        return Long.parseLong(FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/core/maxChunkReadTime", "90000"));
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
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/storageProviders/" + provider + "/fileBase", null);
    }

    @Override
    public boolean isStorageProviderHmacPresigned(String provider) {
        return this.isStorageProviderAuthTypeHmac(provider)
                && FHIRConfigHelper.getBooleanProperty("fhirServer/bulkdata/storageProviders/" + provider + "/presigned", Boolean.FALSE);
    }

    @Override
    public boolean shouldCoreApiBatchTrustAll() {
        return FHIRConfigHelper.getBooleanProperty("fhirServer/bulkdata/core/api/trustAll", Boolean.FALSE);
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

    @Override
    public int getPresignedUrlExpiry() {
        int expirySeconds = FHIRConfigHelper.getIntProperty("fhirServer/bulkdata/core/cos/presignedExpiry", 86400);
        return Math.max(1, expirySeconds);
    }

    @Override
    public String getDefaultImportProvider() {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/core/defaultImportProvider", "default");
    }

    @Override
    public String getDefaultExportProvider() {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/core/defaultExportProvider", "default");
    }

    @Override
    public String getOperationOutcomeProvider(String provider) {
        String outcomeProvider = FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/storageProviders/" + provider + "/operationOutcomeProvider", null);

        // now we check the system level
        if (outcomeProvider == null) {
            outcomeProvider = FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/core/defaultOutcomeProvider", provider);
        }

        return outcomeProvider;
    }

    @Override
    public boolean hasStorageProvider(String storageProvider) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/storageProviders/" + storageProvider + "/type", null) != null;
    }

    @Override
    public S3HostStyle getS3HostStyleByStorageProvider(String provider) {
        String hostSyle = FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/storageProviders/" + provider + "/accessType", "path");
        return S3HostStyle.from(hostSyle);
    }

    @Override
    public boolean enableSkippableUpdates() {
        return FHIRConfigHelper.getBooleanProperty("fhirServer/bulkdata/core/enableSkippableUpdates", Boolean.TRUE);
    }

    @Override
    public String getStorageProviderAuthTypeConnectionString(String provider) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/storageProviders/" + provider + "/auth/connection", null);
    }

    @Override
    public String getProviderAzureServiceVersion(String provider) {
        return FHIRConfigHelper.getStringProperty("fhirServer/bulkdata/storageProviders/" + provider + "/serviceVersion", null);
    }

    @Override
    public boolean isStorageProviderAuthTypeConnectionString(String provider) {
        String auth = getStorageProviderAuthType(provider);
        return "connection".equalsIgnoreCase(auth);
    }
}