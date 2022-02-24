/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.config.preflight.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.model.HeadBucketRequest;
import com.ibm.cloud.objectstorage.services.s3.model.HeadBucketResult;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.operation.bulkdata.OperationConstants;
import com.ibm.fhir.operation.bulkdata.client.S3ClientGenerator;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.operation.bulkdata.config.s3.S3HostStyle;
import com.ibm.fhir.operation.bulkdata.model.type.Input;
import com.ibm.fhir.operation.bulkdata.model.type.StorageDetail;
import com.ibm.fhir.operation.bulkdata.model.type.StorageType;
import com.ibm.fhir.operation.bulkdata.util.BulkDataExportUtil;

/**
 * Checks the S3 Configuration.
 */
public class S3Preflight extends NopPreflight {
    private static final Logger LOG = Logger.getLogger(S3Preflight.class.getName());

    private static final S3ClientGenerator GENERATOR = new S3ClientGenerator();
    private static final BulkDataExportUtil EXPORT = new BulkDataExportUtil();

    public S3Preflight(String source, String outcome, List<Input> inputs, OperationConstants.ExportType exportType, String format) {
        super(source, outcome, inputs, exportType, format);
    }

    @Override
    public void preflight() throws FHIROperationException {
        preflight(false);
    }

    @Override
    public void preflight(boolean write) throws FHIROperationException {
        super.preflight();

        ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
        boolean outcomes = adapter.shouldStorageProviderCollectOperationOutcomes(getSource());
        String bucketName = adapter.getStorageProviderBucketName(getSource());

        // Check that we can access it.
        List<Callable<BucketResult>> callables = new ArrayList<>(2);
        AmazonS3 s3Source = validate(getSource());
        BucketHostS3Callable s3Callable = new BucketHostS3Callable(getOutcome(), s3Source, bucketName, write);

        // Only add to the list to check IFF there is a different outcome storageProvider
        // And the StorageProvider is S3... this means we don't mix preflights with different outcome StorageTypes.
        StorageType type = adapter.getStorageProviderStorageType(getOutcome());
        if (outcomes && !getSource().equals(getOutcome())
                && (StorageType.AWSS3.equals(type) || StorageType.IBMCOS.equals(type))) {
            AmazonS3 s3Outcome = validate(getOutcome());
            String bucketNameOutcome = adapter.getStorageProviderBucketName(getOutcome());
            callables.add(new BucketHostS3Callable(getOutcome(), s3Outcome, bucketNameOutcome, true));
        } else if (getSource().equals(getOutcome())
                    && adapter.shouldStorageProviderCollectOperationOutcomes(getSource())) {
            s3Callable.setWrite();
        }
        callables.add(s3Callable);

        /*
         * The executor stops execution at the expiration of the 60 second timeout.
         * The callable unit executes and then wraps the failure
         */
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            List<Future<BucketResult>> futures = executor.invokeAll(callables, 60, TimeUnit.SECONDS);
            for (Future<BucketResult> future : futures) {
                if (!future.get().result) {
                    if (future.get().ex == null) {
                        throw EXPORT.buildOperationException("Unable to access s3 storageProvider during timeout '" + future.get().source + "'", IssueType.EXCEPTION, future.get().ex);
                    } else {
                        throw future.get().ex;
                    }
                }
            }
        } catch (ExecutionException ee) {
            throw EXPORT.buildOperationException("Failed to execute the s3 access, check the s3 configuration", IssueType.NO_STORE, ee);
        } catch (InterruptedException e) {
            throw EXPORT.buildOperationException("Timeout hit trying to access s3, check the s3 configuration", IssueType.NO_STORE, e);
        }
        executor.shutdown();
    }

    /**
     * validates the s3 is properly configured.
     *
     * @param source
     * @return AmazonS3
     * @throws FHIROperationException
     */
    public AmazonS3 validate(String source) throws FHIROperationException {
        ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
        boolean iam = false;
        String access = null;
        String secret = null;
        if (adapter.isStorageProviderAuthTypeIam(source)) {
            String apiKey = adapter.getStorageProviderAuthTypeIamApiKey(source);
            String resourceId = adapter.getStorageProviderAuthTypeIamApiResourceInstanceId(source);
            if (apiKey == null || resourceId == null || apiKey.isEmpty() || resourceId.isEmpty()) {
                throw EXPORT.buildOperationException("bad configuration for the iam configuration", IssueType.EXCEPTION);
            }
            iam = true;
            access = apiKey;
            secret = resourceId;
        } else if (adapter.isStorageProviderAuthTypeHmac(source)) {
            String accessKey = adapter.getStorageProviderAuthTypeHmacAccessKey(source);
            String secretKey = adapter.getStorageProviderAuthTypeHmacSecretKey(source);
            if (accessKey == null || secretKey == null || accessKey.isEmpty() || secretKey.isEmpty()) {
                throw EXPORT.buildOperationException("bad configuration for the hmac configuration", IssueType.EXCEPTION);
            }
            access = accessKey;
            secret = secretKey;
        } else if (adapter.isStorageProviderAuthTypeBasic(source)) {
            String user = adapter.getStorageProviderAuthTypeUsername(source);
            String password = adapter.getStorageProviderAuthTypePassword(source);
            if (user == null || password == null || user.isEmpty() || password.isEmpty()) {
                throw EXPORT.buildOperationException("bad configuration for the basic configuration", IssueType.EXCEPTION);
            }
            access = user;
            secret = password;
        } else if (adapter.getStorageProviderBucketName(source) == null || adapter.getStorageProviderBucketName(source).isEmpty()) {
            throw EXPORT.buildOperationException("bad configuration for the basic configuration with bucketname", IssueType.EXCEPTION);
        } else {
            throw EXPORT.buildOperationException("Failed to specify the source or outcome bucket's authentication mechanism", IssueType.EXCEPTION);
        }

        String url = adapter.getStorageProviderEndpointInternal(source);
        if (url == null || url.isEmpty()) {
            throw EXPORT.buildOperationException("endpoint internal is undefined.", IssueType.EXCEPTION);
        }
        // There are two styles PATH/VIRTUAL_HOST
        S3HostStyle kind = adapter.getS3HostStyleByStorageProvider(source);
        boolean withPathStyle = S3HostStyle.PATH.equals(kind);

        String location = adapter.getStorageProviderLocation(source);
        if (location == null || location.isEmpty()) {
            throw EXPORT.buildOperationException("endpoint location is undefined.", IssueType.EXCEPTION);
        }

        // Create a COS/S3 client if it's not created yet.
        boolean useFhirServerTrustStore = adapter.shouldCoreCosUseServerTruststore();
        return GENERATOR.getClient(iam, access, secret, url, location, useFhirServerTrustStore, withPathStyle);
    }

    /*
     * @implNote Checks the Access to the url/input.
     */
    protected static class BucketHostS3Callable implements Callable<BucketResult> {
        private AmazonS3 client;
        private String source;
        private String bucketName;
        @SuppressWarnings("unused")
        private boolean write;

        // Result
        BucketResult result = new BucketResult();

        public BucketHostS3Callable(String source, AmazonS3 client, String bucketName, boolean write) throws FHIROperationException {
            this.source = source;
            this.bucketName = bucketName;
            this.client = client;
            this.write = write;
        }

        /**
         * Identifies this as a write (not just a read)
         */
        public void setWrite() {
            this.write = true;
        }

        @Override
        public BucketResult call() throws Exception {
            result.source = source;
            try {
                HeadBucketRequest req = new HeadBucketRequest(bucketName)
                                            .withSdkRequestTimeout(59000);
                HeadBucketResult r = client.headBucket(req);
                LOG.fine(() -> "Source is valid and in region " + source + "/" + r.getBucketRegion());

                // We don't check if the bucket is writeable, as the call to check the ACL
                // can be unintentionally costly. If there is a further optimization...
                // We want to make it right here.

                result.result = true;
            } catch (com.ibm.cloud.objectstorage.AmazonServiceException ex) {
                LOG.throwing("S3Preflight.BucketHostS3Callable", "call", ex);
                switch (ex.getStatusCode()) {
                case 301: // the bucket is in a different region than the client is configured with
                    result.ex = EXPORT.buildOperationException("Storage provider's region is incorrect" + source + "'", IssueType.EXCEPTION);
                    break;
                case 403: // if the user does not have access to the bucket
                    result.ex = EXPORT.buildOperationException("Storage provider's credentials are incorrect '" + source + "'", IssueType.EXCEPTION);
                    break;
                case 404: // the bucket does not exist
                    result.ex = EXPORT.buildOperationException("The bucket does not exist '" + source + "/" + bucketName + "'", IssueType.EXCEPTION);
                    break;
                default:
                    LOG.throwing("S3Preflight.BucketHostS3Callable", "call()", ex);
                    result.ex = EXPORT.buildOperationException("Unexpected Exception '" + source + "'", IssueType.EXCEPTION, ex);
                }
            }
            return result;
        }
    }

    /**
     * Wraps the results of the Callable
     */
    private static class BucketResult {
        private String source = null;
        private boolean result = false;
        private FHIROperationException ex;
    }

    @Override
    public void checkStorageAllowed(StorageDetail storageDetail) throws FHIROperationException {
        if (storageDetail != null && !(StorageType.AWSS3.value().equals(storageDetail.getType()) || StorageType.IBMCOS.value().equals(storageDetail.getType()))){
            throw EXPORT.buildOperationException("S3: Configuration not set to import from storageDetail '" + getSource() + "'", IssueType.INVALID);
        }
    }
}