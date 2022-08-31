/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.operation.bulkdata.config;

import java.util.List;

import org.linuxforhealth.fhir.exception.FHIRException;
import org.linuxforhealth.fhir.operation.bulkdata.config.s3.S3HostStyle;
import org.linuxforhealth.fhir.operation.bulkdata.model.type.StorageType;

/**
 * Configuration provides a common interface to the fhir-server-config.json.
 * and enables seamless reads of constant values which may be set in the future.
 *
 * The interface adds an @implNote for fixed or system (default), All other are dynamic.
 */
public interface ConfigurationAdapter {

    /**
     * get the application name used in the javabatch framework.
     *
     * @implNote FIXED value.
     *
     * @return
     */
    String getApplicationName();

    /**
     * get the module name used in javabatch framework.
     *
     * @implNote FIXED value.
     *
     * @return
     */
    String getModuleName();

    /**
     * get the job xml used in the javabatch framework.
     *
     * @implNote FIXED value.
     *
     * @return
     */
    String getJobXMLName();

    /**
     * creates a uniform request context for the $import and $export operations.
     *
     * @param tenantId
     * @param datastoreId
     * @param incomingUrl
     * @throws FHIRException
     */
    void registerRequestContext(String tenantId, String datastoreId, String incomingUrl) throws FHIRException;

    /**
     * identifies the configuration as legacy or not.
     *
     * @return
     */
    boolean legacy();

    /**
     * @return the status of the bulkdata feature, enabled = true, disabled = false
     */
    boolean enabled();

    /**
     * @return the local batch api URL
     */
    String getCoreApiBatchUrl();

    /**
     *
     * @return the local batch api user, it should be an admin
     */
    String getCoreApiBatchUser();

    /**
     *
     * @return the local batch api user password
     */
    String getCoreApiBatchPassword();

    /**
     *
     * @return the local batch api user password
     */
    String getCoreApiBatchTrustStore();

    /**
     * @return the local batch api user password
     */
    String getCoreApiBatchTrustStorePassword();

    /**
     * @return should we trust the calls to the backend
     */
    boolean shouldCoreApiBatchTrustAll();

    /**
     * The size (in bytes) at which to begin writing a part for a COS multi-part upload.
     *
     * @implNote System value.
     * @implNote The S3 API requires parts to be between 5 MB (5e+6 bytes) and 5 GB (5e+9 bytes).
     * @implNote The S3 API does not allow more than 10,000 parts total (per Object).
     *
     * @return
     */
    int getCoreCosPartUploadTriggerSize();

    /**
     * The size (in bytes) at which to finish writing to a given COS object,
     * or 0 to indicate that there is no object size threshold.
     *
     * @implNote System value.
     * @implNote S3 objects have a maximum size of 5 TB (5e+12 bytes).
     *
     * @return
     */
    long getCoreCosObjectSizeThreshold();

    /**
     * The number of resources at which to finish writing to a given COS object,
     * or 0 to indicate that there is no resource count threshold.
     *
     * @implNote System value.
     *
     * @return
     */
    int getCoreCosObjectResourceCountThreshold();

    /**
     * The size (in bytes) at which to finish writing to a given AzureBlob object,
     *
     * @implNote System value.
     *
     * @return
     */
    long getCoreAzureObjectSizeThreshold();

    /**
     * The serviceVersion for the Azure API
     * @param provider
     * @return
     */
    String getProviderAzureServiceVersion(String provider);

    /**
     * The number of resources at which to finish writing to a given AzureBlob object,
     *
     * @implNote System value.
     *
     * @return
     */
    int getCoreAzureObjectResourceCountThreshold();

    /**
     * @implNote System value.
     * @return the system wide setting for using the server truststore.
     */
    boolean shouldCoreCosUseServerTruststore();

    /**
     *
     * @implNote System value.
     * @return the timeout for the s3 request
     */
    int getCoreCosRequestTimeout();

    /**
     * @implNote System value.
     * @return the timeout for the s3 socket
     */
    int getCoreCosSocketTimeout();

    /**
     * The size (in bytes) to buffer before writing to file.
     *
     * @implNote System value.
     *
     * @return
     */
    int getCoreFileWriteTriggerSize();

    /**
     * The size (in bytes) at which to finish writing to a given file,
     * or 0 to indicate that there is no file size threshold.
     *
     * @implNote System value.
     *
     * @return
     */
    long getCoreFileSizeThreshold();

    /**
     * The number of resources at which to finish writing to a given file,
     * or 0 to indicate that there is no resource count threshold.
     *
     * @implNote System value.
     *
     * @return
     */
    int getCoreFileResourceCountThreshold();

    /**
     * get the core page size used in Search.
     *
     * @return
     */
    int getCorePageSize();

    /**
     * get core batch id encryption key for the job id that is returned
     *
     * @implNote System value. We want to minimize the conflict possiblity.
     *
     * @return
     */
    String getCoreBatchIdEncryptionKey();

    /**
     * get core max partitions
     *
     * @implNote System value.
     *
     * @return
     */
    int getCoreMaxPartitions();

    /**
     * get the core iam endpoint (it is set one time for the whole system)
     *
     * @implNote System value. Only able to set once per system.
     *
     * @return
     */
    String getCoreIamEndpoint();

    /**
     * get the number ms to read payloads from the persistence layer before stopping to checkpoint
     *
     * @return
     */
    long getCoreFastMaxReadTimeout();

    /**
     * get default import provider
     * @return
     */
    String getDefaultImportProvider();

    /**
     * get default export provider
     * @return
     */
    String getDefaultExportProvider();

    /**
     * get the OperationOutcome provider
     * @param provider the storage provider
     * @return
     */
    String getOperationOutcomeProvider(String provider);

    /**
     * @param storageProvider
     * @return True, iff the provider exists in the configuration.
     */
    boolean hasStorageProvider(String storageProvider);

    /**
     * gets the StorageProvider type which aligns with the StorageType
     * @param provider
     * @return
     */
    String getStorageProviderType(String provider);

    /**
     * gets the StorageProvider's valid base urls for HTTPS sources.
     *
     * @param provider
     * @return
     */
    List<String> getStorageProviderValidBaseUrls(String provider);

    /**
     * gets the StorageProvider's bucket name for cos sources.
     *
     * @param provider
     * @return
     */
    String getStorageProviderBucketName(String provider);

    /**
     * gets the location of the cos endpoint.
     * @param provider
     * @return
     */
    String getStorageProviderLocation(String provider);

    /**
     * get the internal endpoint for the storage provider.
     *
     * @param provider
     * @return
     */
    String getStorageProviderEndpointInternal(String provider);

    /**
     * get the external endpoint for the storage provider.
     * @param provider
     * @return
     */
    String getStorageProviderEndpointExternal(String provider);

    /**
     *
     * @param provider
     * @return
     */
    String getStorageProviderAuthType(String provider);

    /**
     *
     * @param provider
     * @return
     */
    boolean isStorageProviderAuthTypeIam(String provider);

    /**
     *
     * @param provider
     * @return
     */
    String getStorageProviderAuthTypeIamApiKey(String provider);

    /**
     *
     * @param provider
     * @return
     */
    String getStorageProviderAuthTypeIamApiResourceInstanceId(String provider);

    /**
     *
     * @param provider
     * @return
     */
    boolean isStorageProviderAuthTypeHmac(String provider);

    /**
     *
     * @param provider
     * @return
     */
    String getStorageProviderAuthTypeHmacAccessKey(String provider);

    /**
     *
     * @param provider
     * @return
     */
    String getStorageProviderAuthTypeHmacSecretKey(String provider);

    /**
     *
     * @param provider
     * @return
     */
    boolean isStorageProviderAuthTypeBasic(String provider);

    /**
     *
     * @param provider
     * @return
     */
    String getStorageProviderAuthTypeUsername(String provider);

    /**
     *
     * @param provider
     * @return
     */
    String getStorageProviderAuthTypePassword(String provider);

    /**
     *
     * @param provider
     * @return
     */
    @Deprecated
    default boolean isStorageProviderParquetEnabled(String provider) {
        return Boolean.FALSE;
    }

    /**
     *
     * @param provider
     * @return
     */
    boolean shouldStorageProviderValidateBaseUrl(String provider);

    /**
     *
     * @param provider
     * @return
     */
    boolean shouldStorageProviderCollectOperationOutcomes(String provider);

    /**
     *
     * @param provider
     * @return
     */
    boolean shouldStorageProviderCheckDuplicate(String provider);

    /**
     *
     * @param provider
     * @return
     */
    boolean shouldStorageProviderValidateResources(String provider);

    /**
     *
     * @param provider
     * @return
     */
    boolean shouldStorageProviderCreate(String provider);

    /**
     * checks if the
     * @param storageType
     * @return
     */
    boolean isStorageTypeAllowed(String storageType);

    /**
     *
     * @param provider
     *            of the storage type
     * @return
     */
    StorageType getStorageProviderStorageType(String provider);

    /**
     * limit of inputs
     *
     * @implNote System value.
     *
     * @return
     */
    int getInputLimit();

    /**
     * gets a tenant in the current context
     *
     * @return
     */
    String getTenant();

    /**
     * indicating if it's the new fast export
     *
     * @return
     */
    boolean isFastExport();

    /**
     * get the COS TCP Keep alive setting (true or false)
     *
     * @implNote System value.
     *
     * @return
     */
    boolean getCoreCosTcpKeepAlive();

    /**
     * get the base file location
     *
     * @param provider
     * @return
     */
    String getBaseFileLocation(String provider);

    /**
     * @param provider
     * @return
     */
    boolean isStorageProviderHmacPresigned(String provider);

    /**
     * @param provider
     * @return
     */
    byte[] getEndOfFileDelimiter(String provider);

    /**
     * get the import fhir resources per read
     * @param provider
     * @return
     */
    int getImportNumberOfFhirResourcesPerRead(String provider);

    /**
     * the infly rate for the import
     * @param provider
     * @return
     */
    int getImportInflyRateNumberOfFhirResources(String provider);

    /**
     * the expiry time of the generated presigned urls.
     * @return
     */
    int getPresignedUrlExpiry();

    /**
     * get the connection string for the azure type account with a connection string.
     * @param provider
     * @return
     */
    String getStorageProviderAuthTypeConnectionString(String provider);

    /**
     * checks the provider to see if this is an azure connection string.
     * @param provider
     * @return true, if connectionString
     */
    boolean isStorageProviderAuthTypeConnectionString(String provider);

    /**
     * @return the status of skippable updates
     */
    boolean enableSkippableUpdates();

    /**
     * gets the storage type
     * @param provider
     * @return
     */
    S3HostStyle getS3HostStyleByStorageProvider(String provider);

    /**
     * reports back to the client if the StorageProvider supports requestAccessTokens
     *
     * @implNote per the spec, presigned URLS do NOT require access tokens.
     *
     * @param provider
     * @return
     */
    boolean getStorageProviderUsesRequestAccessToken(String provider);

    /**
     * allows multiple resources in a single file. 
     * 
     * @implNote this default is false. 
     * 
     * @param source
     * @return
     */
    boolean shouldStorageProviderAllowAllResources(String source);
}