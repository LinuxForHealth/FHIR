/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.cli.invoker;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.ibm.fhir.cli.Operations;
import com.ibm.fhir.client.FHIRResponse;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;

/**
 * This class serves as a holder of context information for a particular operation invocation.
 * 
 * @author padams
 */
public class InvocationContext {

    public class NameValuePair {
        private String name;
        private String value;

        public NameValuePair(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }

    private Operations operation;
    private String mimeType;
    private Properties clientProperties;
    private String resourceType;
    private String resourceId;
    private String versionId;
    private String outputFile;
    private String propertiesFile;
    private String resourceFile;
    private boolean verbose;
    private FHIRResponse response;
    private List<NameValuePair> queryParameters = new ArrayList<>();
    private List<NameValuePair> headers = new ArrayList<>();

    public InvocationContext() {
    }

    public Operations getOperation() {
        return operation;
    }

    public void setOperation(Operations operation) {
        this.operation = operation;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Properties getClientProperties() {
        return clientProperties;
    }

    public void setClientProperties(Properties clientProperties) {
        this.clientProperties = clientProperties;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public String getPropertiesFile() {
        return propertiesFile;
    }

    public void setPropertiesFile(String propertiesFile) {
        this.propertiesFile = propertiesFile;
    }

    public String getResourceFile() {
        return resourceFile;
    }

    public void setResourceFile(String resourceFile) {
        this.resourceFile = resourceFile;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public FHIRResponse getResponse() {
        return response;
    }

    public void setResponse(FHIRResponse response) {
        this.response = response;
    }

    public List<NameValuePair> getQueryParameters() {
        return queryParameters;
    }

    public List<NameValuePair> getHeaders() {
        return headers;
    }

    public void addQueryParameter(String name, String value) {
        queryParameters.add(new NameValuePair(name, value));
    }

    public void addHeader(String name, String value) {
        headers.add(new NameValuePair(name, value));
    }

    public String getResourceTypeWithExcp() throws Exception {
        if (resourceType == null || resourceType.isEmpty()) {
            throw new IllegalArgumentException("The 'type' parameter was missing.");
        }

        return resourceType;
    }

    public String getResourceIdWithExcp() throws Exception {
        if (resourceId == null || resourceId.isEmpty()) {
            throw new IllegalArgumentException("The 'id' parameter was missing.");
        }

        return resourceId;
    }

    public String getVersionIdWithExcp() throws Exception {
        if (versionId == null || versionId.isEmpty()) {
            throw new IllegalArgumentException("The 'versionId' parameter was missing.");
        }

        return versionId;
    }

    public Object getRequestResourceWithExcp() throws Exception {
        Reader reader = null;
        InputStream is = null;
        
        try {
            // If the user specified the resource filename then we'll try to read
            // the resource from there; otherwise, we'll get it from System.in.
            if (resourceFile != null && !resourceFile.isEmpty()) {
                is = new FileInputStream(resourceFile);
                reader = new InputStreamReader(is);
            } else {
                reader = new InputStreamReader(System.in);
            }
            
            return FHIRParser.parser(Format.JSON).parse(reader);
        } catch (Throwable t) {
            throw t;
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (is != null) {
                is.close();
            }
        }
    }
}
