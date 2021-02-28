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
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.operation.bulkdata.model.type.Input;
import com.ibm.fhir.operation.bulkdata.util.BulkDataExportUtil;

/**
 *
 */
public class S3Preflight extends NopPreflight {

    public S3Preflight(String source, String outcome, List<Input> inputs) {
        super(source, outcome, inputs);
    }

    @Override
    public void preflight() throws FHIROperationException {
        ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
        boolean outcomes = adapter.shouldSourceCollectOperationOutcomes(getSource());

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
                    throw BulkDataExportUtil.buildOperationException("Unable to access s3 source or outcome during timeout", IssueType.INVALID);
                }
            }
        } catch (ExecutionException ee) {
            throw BulkDataExportUtil.buildOperationException("Failed to execute the s3 access, check the s3 configuration", IssueType.INVALID);
        } catch (InterruptedException e) {
            throw BulkDataExportUtil.buildOperationException("Timeout hit trying to access s3, check the s3 configuration", IssueType.INVALID);
        }
        executor.shutdown();

        super.preflight();
    }

    /**
     * validates the s3 is properly configured.
     *
     * @param source
     * @throws FHIROperationException
     */
    public void validate(String source) throws FHIROperationException {
        ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
        if (adapter.isSourceAuthTypeIam(source)) {
            String apiKey = adapter.getSourceAuthTypeIamApiKey(source);
            String resourceId = adapter.getSourceAuthTypeIamApiResourceInstanceId(source);
            if (apiKey == null || resourceId == null || apiKey.isEmpty() || resourceId.isEmpty()) {
                throw BulkDataExportUtil.buildOperationException("bad configuration for the iam configuration", IssueType.INVALID);
            }
        } else if (adapter.isSourceAuthTypeHmac(source)) {
            String accessKey = adapter.getSourceAuthTypeHmacAccessKey(source);
            String secretKey = adapter.getSourceAuthTypeHmacSecretKey(source);
            if (accessKey == null || secretKey == null || accessKey.isEmpty() || secretKey.isEmpty()) {
                throw BulkDataExportUtil.buildOperationException("bad configuration for the hmac configuration", IssueType.INVALID);
            }
        } else if (adapter.isSourceAuthTypeBasic(source)) {
            String user = adapter.getSourceAuthTypeUsername(source);
            String password = adapter.getSourceAuthTypePassword(source);
            if (user == null || password == null || user.isEmpty() || password.isEmpty()) {
                throw BulkDataExportUtil.buildOperationException("bad configuration for the basic configuration", IssueType.INVALID);
            }
        } else {
            throw BulkDataExportUtil.buildOperationException("Failed to specify the source or outcome bucket's authentication mechanism", IssueType.INVALID);
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
            url = adapter.getSourceEndpointExternal(source);
            if (url == null || url.isEmpty()) {
                throw BulkDataExportUtil.buildOperationException("endpoint-internal is undefined.", IssueType.INVALID);
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
}