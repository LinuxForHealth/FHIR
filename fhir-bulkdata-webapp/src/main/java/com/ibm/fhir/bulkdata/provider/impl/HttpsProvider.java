/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.provider.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import com.ibm.fhir.bulkdata.common.BulkDataUtils;
import com.ibm.fhir.bulkdata.jbatch.export.data.ExportTransientUserData;
import com.ibm.fhir.bulkdata.jbatch.load.data.ImportTransientUserData;
import com.ibm.fhir.bulkdata.provider.Provider;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.Resource;

/**
 * Https Provider
 */
public class HttpsProvider implements Provider {
    private long parseFailures = 0l;
    private ImportTransientUserData transientUserData = null;
    private List<Resource> resources = new ArrayList<>();

    @SuppressWarnings("unused")
    private String source = null;
    public HttpsProvider(String source) {
        this.source = source;
    }

    @Override
    public long getSize(String workItem) throws FHIRException {
        HttpsURLConnection httpsConnection = null;
        try {
            // Check before trying to use 'http://' with an 'https://' url connection.
            if (workItem.startsWith("http://")) {
                throw new FHIROperationException("No support for 'http'");
            }

            httpsConnection = (HttpsURLConnection) new URL(workItem).openConnection();
            httpsConnection.setRequestMethod("HEAD");
            return httpsConnection.getContentLengthLong();
        } catch(Exception e){
            throw new FHIRException("Unable to calculate the size of the file ", e);
        } finally {
          if (httpsConnection != null) {
              httpsConnection.disconnect();
          }
        }
    }

    @Override
    public List<Resource> getResources() throws FHIRException {
        return resources;
    }

    @Override
    public long getNumberOfParseFailures() throws FHIRException {
        return parseFailures;
    }

    @Override
    public void registerTransient(ImportTransientUserData transientUserData) {
        this.transientUserData = transientUserData;
    }

    @Override
    public void readResources(long numOfLinesToSkip, String workItem) throws FHIRException {
        try {
            parseFailures = BulkDataUtils.readFhirResourceFromHttps(workItem, (int) numOfLinesToSkip, resources, transientUserData);
        } catch (Exception e) {
            throw new FHIRException("Unable to read from Https File", e);
        }
    }

    @Override
    public long getNumberOfLoaded() throws FHIRException {
        return this.resources.size();
    }

    @Override
    public void registerTransient(long executionId, ExportTransientUserData transientUserData, String cosBucketPathPrefix, String fhirResourceType) {

    }

    @Override
    public void close() throws Exception {

    }
}