/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.config.preflight.impl;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.operation.bulkdata.OperationConstants;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.operation.bulkdata.model.type.Input;
import com.ibm.fhir.operation.bulkdata.model.type.StorageDetail;
import com.ibm.fhir.operation.bulkdata.model.type.StorageType;
import com.ibm.fhir.operation.bulkdata.util.BulkDataExportUtil;

/**
 * Checks the S3 Configuration.
 */
public class S3Preflight extends NopPreflight {

    private static final BulkDataExportUtil export = new BulkDataExportUtil();

    public S3Preflight(String source, String outcome, List<Input> inputs, OperationConstants.ExportType exportType, String format) {
        super(source, outcome, inputs, exportType, format);
    }

    @Override
    public void preflight() throws FHIROperationException {
        super.preflight();

        ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
        boolean outcomes = adapter.shouldStorageProviderCollectOperationOutcomes(getSource());

        // Check that we can access it.
        Set<Callable<Boolean>> callables = new HashSet<>(2);
        BucketHostCallable sourceCallable = new BucketHostCallable(getSource());
        callables.add(sourceCallable);

        // Check Configuration for Type.
        validate(getSource());

        if (outcomes) {
            validate(getOutcome());

            BucketHostCallable outcomeCallable = new BucketHostCallable(getOutcome());
            callables.add(outcomeCallable);
        }

        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            List<Future<Boolean>> futures = executor.invokeAll(callables, 60, TimeUnit.SECONDS);
            for (Future<Boolean> future : futures) {
                if (!future.get()) {
                    throw export.buildOperationException("Unable to access s3 source or outcome during timeout", IssueType.EXCEPTION);
                }
            }
        } catch (ExecutionException ee) {
            throw export.buildOperationException("Failed to execute the s3 access, check the s3 configuration", IssueType.NO_STORE, ee);
        } catch (InterruptedException e) {
            throw export.buildOperationException("Timeout hit trying to access s3, check the s3 configuration", IssueType.NO_STORE, e);
        }
        executor.shutdown();
    }

    /**
     * validates the s3 is properly configured.
     *
     * @param source
     * @throws FHIROperationException
     */
    public void validate(String source) throws FHIROperationException {
        ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
        if (adapter.isStorageProviderAuthTypeIam(source)) {
            String apiKey = adapter.getStorageProviderAuthTypeIamApiKey(source);
            String resourceId = adapter.getStorageProviderAuthTypeIamApiResourceInstanceId(source);
            if (apiKey == null || resourceId == null || apiKey.isEmpty() || resourceId.isEmpty()) {
                throw export.buildOperationException("bad configuration for the iam configuration", IssueType.EXCEPTION);
            }
        } else if (adapter.isStorageProviderAuthTypeHmac(source)) {
            String accessKey = adapter.getStorageProviderAuthTypeHmacAccessKey(source);
            String secretKey = adapter.getStorageProviderAuthTypeHmacSecretKey(source);
            if (accessKey == null || secretKey == null || accessKey.isEmpty() || secretKey.isEmpty()) {
                throw export.buildOperationException("bad configuration for the hmac configuration", IssueType.EXCEPTION);
            }
        } else if (adapter.isStorageProviderAuthTypeBasic(source)) {
            String user = adapter.getStorageProviderAuthTypeUsername(source);
            String password = adapter.getStorageProviderAuthTypePassword(source);
            if (user == null || password == null || user.isEmpty() || password.isEmpty()) {
                throw export.buildOperationException("bad configuration for the basic configuration", IssueType.EXCEPTION);
            }
        } else if (adapter.getStorageProviderBucketName(source) == null || adapter.getStorageProviderBucketName(source).isEmpty()) {
            throw export.buildOperationException("bad configuration for the basic configuration with bucketname", IssueType.EXCEPTION);
        } else {
            throw export.buildOperationException("Failed to specify the source or outcome bucket's authentication mechanism", IssueType.EXCEPTION);
        }
    }

    /*
     * @implNote Checks the Access to the url/input.
     */
    private class BucketHostCallable implements Callable<Boolean> {

        private String source = null;
        private String url = null;

        public BucketHostCallable(String source) throws FHIROperationException {
            this.source = source;
            ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
            url = adapter.getStorageProviderEndpointExternal(source);
            if (url == null || url.isEmpty()) {
                throw export.buildOperationException("endpoint-internal is undefined.", IssueType.EXCEPTION);
            }
        }

        @Override
        public Boolean call() throws Exception {
            boolean result = false;
            // Check can connect to host (HEAD)
            HttpsURLConnection httpsConnection = null;
            try {
                httpsConnection = (HttpsURLConnection) new URL(url).openConnection();
                httpsConnection.setRequestMethod("HEAD");
                httpsConnection.getContentLengthLong();
                result = true;
            } catch (Exception e) {
                throw new FHIRException("Unable to connect to s3 endpoint '" + source + '"', e);
            } finally {
                if (httpsConnection != null) {
                    httpsConnection.disconnect();
                }
            }
            return result;
        }
    }

    @Override
    public void checkStorageAllowed(StorageDetail storageDetail) throws FHIROperationException {
        if (storageDetail != null && !(StorageType.AWSS3.value().equals(storageDetail.getType()) || StorageType.IBMCOS.value().equals(storageDetail.getType()))){
            throw util.buildExceptionWithIssue("S3: Configuration not set to import from storageDetail '" + getSource() + "'", IssueType.INVALID);
        }
    }

    @Override
    public boolean checkParquet() {
        return ConfigurationFactory.getInstance().isStorageProviderParquetEnabled(getSource());
    }
}