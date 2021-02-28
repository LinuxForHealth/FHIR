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
    int getCoreCosMaxResources();

    /**
     * The minimum size of resources in a COS part.
     *
     * @implNote System value.
     *
     * @return
     */
    int getCoreCosMinSize();

    /**
     * The maximum size of resources in a COS part.
     *
     * @implNote System value.
     *
     * @return
     */
    int getCoreCosMaxSize();

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
     * gets the source type which aligns with the StorageType
     * @param source
     * @return
     */
    String getSourceType(String source);

    /**
     * gets the source's valid base urls for HTTPS sources.
     *
     * @param source
     * @return
     */
    List<String> getSourceValidBaseUrls(String source);

    /**
     * gets the source's bucket name for cos sources.
     *
     * @param source
     * @return
     */
    String getSourceBucketName(String source);

    String getSourceLocation(String source);

    String getSourceEndpointInternal(String source);

    String getSourceEndpointExternal(String source);

    String getSourceAuthType(String source);

    boolean isSourceAuthTypeIam(String source);

    String getSourceAuthTypeIamApiKey(String source);

    String getSourceAuthTypeIamApiResourceInstanceId(String source);

    boolean isSourceAuthTypeHmac(String source);

    String getSourceAuthTypeHmacAccessKey(String source);

    String getSourceAuthTypeHmacSecretKey(String source);

    boolean isSourceAuthTypeBasic(String source);

    String getSourceAuthTypeUsername(String source);

    String getSourceAuthTypePassword(String source);

    boolean isSourceParquetEnabled(String source);

    boolean shouldSourceValidateBaseUrl(String source);

    boolean isSourceExportPublic(String source);

    boolean shouldSourceCollectOperationOutcomes(String source);

    boolean shouldSourceCheckDuplicate(String source);

    boolean shouldSourceValidateResources(String source);

    boolean shouldSourceCreate(String source);

    boolean isStorageTypeAllowed(String storageType);

    /**
     *
     * @param type
     *            of the storage type
     * @return
     */
    StorageType getSourceStorageType(String type);

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
     * @param source
     * @return
     */
    String getBaseFileLocation(String source);

    /**
     * @param source
     * @return
     */
    boolean isSourceHmacPresigned(String source);
}