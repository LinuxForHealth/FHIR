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
 */
public interface ConfigurationAdapter {

    /**
     * get the application name used in the javabatch framework.
     *
     * @return
     */
    String getApplicationName();

    /**
     * get the module name used in javabatch framework.
     *
     * @return
     */
    String getModuleName();

    /**
     * get the job xml used in the javabatch framework.
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
     * The maximum number of resources in a COS part.
     *
     * @return
     */
    int getCoreCosMaxResources();

    /**
     * The minimum size of resources in a COS part.
     *
     * @return
     */
    int getCoreCosMinSize();

    /**
     * The maximum size of resources in a COS part.
     *
     * @return
     */
    int getCoreCosMaxSize();

    /**
     *
     * @return the system wide setting for using the server truststore.
     */
    boolean shouldCoreCosUseServerTruststore();

    /**
     *
     * @return the timeout for the s3 request
     */
    int getCoreCosRequestTimeout();

    /**
     *
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
     * @return
     */
    String getCoreBatchIdEncryptionKey();

    /**
     * get core max partitions
     *
     * @return
     */
    int getCoreMaxPartitions();

    /**
     * get the core iam endpoint (it is set one time for the whole system)
     *
     * @return
     */
    String getCoreIamEndpoint();

    /**
     * get the tx for the fast endpoint
     *
     * @return
     */
    int getCoreFastTxTimeout();

    String getSourceType(String source);

    List<String> getSourceValidBaseUrls(String source);

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
     * checks the given file name and it's absolute path are accepted and valid
     *
     * @param source
     * @param fileName
     * @return
     */
    boolean checkValidFileBase(String source, String fileName);

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
     * @return
     */
    boolean getCoreCosTcpKeepAlive();

    /**
     * get the base file location
     * @param source
     * @return
     */
    String getBaseFileLocation(String source);
}