/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.config.preflight.impl;

import static com.ibm.fhir.operation.bulkdata.util.CommonUtil.buildExceptionWithIssue;

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

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.operation.bulkdata.OperationConstants;
import com.ibm.fhir.operation.bulkdata.model.type.StorageDetail;
import com.ibm.fhir.operation.bulkdata.model.type.StorageType;
import com.ibm.fhir.operation.bulkdata.util.BulkDataExportUtil;
import com.ibm.fhir.persistence.bulkdata.InputDTO;

/**
 * Verifies the Export/Import is valid for Https
 */
public class HttpsPreflight extends NopPreflight {
    private static final BulkDataExportUtil export = new BulkDataExportUtil();

    public HttpsPreflight(String source, String outcome, List<InputDTO> inputs, OperationConstants.ExportType exportType, String format) {
        super(source, outcome, inputs, exportType, format);
    }

    @Override
    public void preflight() throws FHIROperationException {
        super.preflight();
        // If inputs are null, then we know we want to stop this right away.
        // We just don't export to HTTPS. Only S3 Compatible and File
        if (getInputs() == null) {
            throw export.buildOperationException("Export does not support 'https' destination", IssueType.INVALID);
        }

        checkFormat();

        // We know this MUST be an $import + Check valid URL
        // It's already validated in BulkDataImportUtil

        // Check that we can access it.
        Set<Callable<Boolean>> callables = new HashSet<>();
        for (InputDTO input : getInputs()) {
            HttpsUrlConnectionCallable callable = new HttpsUrlConnectionCallable(input.getUrl());
            callables.add(callable);
        }

        ExecutorService executor = Executors.newFixedThreadPool(10);
        try {
            List<Future<Boolean>> futures = executor.invokeAll(callables, 60, TimeUnit.SECONDS);
            for (Future<Boolean> future : futures) {
                if (!future.get()) {
                    throw export.buildOperationException("Unable to access input urls during timeout", IssueType.INVALID);
                }
            }
        } catch (ExecutionException ee) {
            throw export.buildOperationException("Failed to execute the URL access, check the urls", IssueType.INVALID);
        } catch (InterruptedException e) {
            throw export.buildOperationException("Timeout hit trying to access URL, check the urls", IssueType.INVALID);
        }
        executor.shutdown();
    }

    /*
     * @implNote Checks the Access to the url/input.
     */
    private class HttpsUrlConnectionCallable implements Callable<Boolean> {

        private String workItem = null;

        public HttpsUrlConnectionCallable(String workItem) {
            this.workItem = workItem;
        }

        @Override
        public Boolean call() throws Exception {
            boolean result = false;
            // Check can connect to host (HEAD)
            HttpsURLConnection httpsConnection = null;
            try {
                httpsConnection = (HttpsURLConnection) new URL(workItem).openConnection();
                httpsConnection.setRequestMethod("HEAD");
                httpsConnection.getContentLengthLong();
                result = true;
            } catch (Exception e) {
                throw new FHIRException("Unable to connect to https '" + workItem + '"', e);
            } finally {
                if (httpsConnection != null) {
                    httpsConnection.disconnect();
                }
            }
            return result;
        }
    }

    private void checkFormat() throws FHIROperationException {
        if (!FHIRMediaType.APPLICATION_NDJSON.equals(getFormat())) {
            throw buildExceptionWithIssue("Https: the requested storageProvider '" + getSource() +
                    "' does not support format '" + getFormat() + "'", IssueType.INVALID);
        }
    }

    @Override
    public void checkStorageAllowed(StorageDetail storageDetail) throws FHIROperationException {
        if (storageDetail != null && !StorageType.HTTPS.value().equals(storageDetail.getType())){
            throw buildExceptionWithIssue("Https: Configuration not set to import from storageDetail '" + getSource() + "'", IssueType.INVALID);
        }
    }
}