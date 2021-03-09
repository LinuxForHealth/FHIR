/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.config;

import java.util.List;

import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.operation.bulkdata.model.type.StorageType;

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
     *
     * @return the local batch api user password
     */
    String getCoreApiBatchTrustStorePassword();

    /**
     *
     * @return should we trust the calls to the backend.
     */
    boolean shouldCoreApiBatchTrustAll();

    /**
     * The maximum number of resources in a COS part.
     *
     * @implNote System value.
     *
     * @return
     */
    long getCoreCosMaxResources();

    /**
     * The minimum size of resources in a COS part.
     *
     * @implNote System value.
     *
     * @return
     */
    long getCoreCosMultiPartMinSize();

    /**
     * The maximum size of resources in a COS part.
     *
     * @implNote System value.
     *
     * @return
     */
    long getCoreCosThresholdSize();

    /**
     *
     * @implNote System value.
     *
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
     * get the tx for the fast endpoint
     *
     * @implNote System value.
     *
     * @return
     */
    int getCoreFastTxTimeout();

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
    boolean isStorageProviderParquetEnabled(String provider);

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
    boolean isStorageProviderExportPublic(String provider);

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
     * @param type
     *            of the storage type
     * @return
     */
    StorageType getStorageProviderStorageType(String type);

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
}