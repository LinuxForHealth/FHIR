/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.bulkdata.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;

import com.ibm.fhir.exception.FHIROperationException;

/**
 * BulkExport Job Instance Request
 *
 * <pre>
 * {
    "applicationName": "fhir-bulkimportexport-webapp",
    "moduleName": "fhir-bulkimportexport.war",
    "jobXMLName": "FhirBulkExportChunkJob",
    "jobParameters": {
        "fhir.resourcetype": "Patient",
        "cos.bucket.name": "fhir-r4-connectathon",
        "cos.location": "us",
        "cos.endpointurl": "https://fake.cloud",
        "cos.credential.ibm": "Y",
        "cos.api.key": "key",
        "cos.srvinst.id": "crn:v1:bluemix:public:cloud-object-storage:global:a/fake::",
        "fhir.search.fromdate": "2019-08-01"
    }
    }</pre>
 *
 *
 *
 */
public class BulkExportJobInstanceRequest {

    private String applicationName;
    private String moduleName;
    private String jobXMLName;
    private JobParameter jobParameters;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getJobXMLName() {
        return jobXMLName;
    }

    public void setJobXMLName(String jobXMLName) {
        this.jobXMLName = jobXMLName;
    }

    public JobParameter getJobParameters() {
        return jobParameters;
    }

    public void setJobParameters(JobParameter jobParameters) {
        this.jobParameters = jobParameters;
    }


    /**
     * Builder is a convenience pattern to assemble to Java Object that reflects the BatchManagement pattern.
     *
     *
     */
    public static class Builder {

        private BulkExportJobInstanceRequest request = new BulkExportJobInstanceRequest();
        private JobParameter jobParameter = new JobParameter();

        private Builder() {
            // Intentionally hiding from external callers.
        }

        public Builder applicationName(String applicationName) {
            request.setApplicationName(applicationName);
            return this;
        }

        public Builder moduleName(String moduleName) {
            request.setModuleName(moduleName);
            return this;
        }

        public Builder jobXMLName(String jobXMLName) {
            request.setJobXMLName(jobXMLName);
            return this;
        }

        public Builder fhirResourceType(String fhirResourceType) {
            jobParameter.setFhirResourceType(fhirResourceType);
            return this;
        }

        public Builder fhirSearchFromDate(String fhirSearchFromDate) {
            jobParameter.setFhirSearchFromDate(fhirSearchFromDate);
            return this;
        }

        public Builder cosBucketName(String cosBucketName) {
            jobParameter.setCosBucketName(cosBucketName);
            return this;
        }

        public Builder cosLocation(String cosLocation) {
            jobParameter.setCosLocation(cosLocation);
            return this;
        }

        public Builder cosEndpointUrl(String cosEndpointUrl) {
            jobParameter.setCosEndpointUrl(cosEndpointUrl);
            return this;
        }

        public Builder cosCredentialIbm(String cosCredentialIbm) {
            jobParameter.setCosCredentialIbm(cosCredentialIbm);
            return this;
        }

        public Builder cosApiKey(String cosApiKey) {
            jobParameter.setCosApiKey(cosApiKey);
            return this;
        }

        public Builder cosSrvInstId(String cosSrvInstId) {
            jobParameter.setCosSrvInstId(cosSrvInstId);
            return this;
        }

        public Builder fhirTenant(String fhirTenant) {
            jobParameter.setFhirTenant(fhirTenant);
            return this;
        }

        public Builder fhirDataStoreId(String fhirDataStoreId) {
            jobParameter.setFhirDataStoreId(fhirDataStoreId);
            return this;
        }

        public Builder cosBucketPathPrefix(String cosBucketPathPrefix) {
            jobParameter.setCosBucketPathPrefix(cosBucketPathPrefix);
            return this;
        }

        public BulkExportJobInstanceRequest build() {
            request.setJobParameters(jobParameter);
            return request;
        }

    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Parser
     *
     *
     */
    public static class Parser {

        private Parser() {
            // No Imp
        }

        private static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);

        public static BulkExportJobInstanceRequest parse(InputStream in)
            throws FHIROperationException {
            try (JsonReader jsonReader =
                    JSON_READER_FACTORY.createReader(in, StandardCharsets.UTF_8)) {
                JsonObject jsonObject = jsonReader.readObject();
                BulkExportJobInstanceRequest.Builder builder =
                        BulkExportJobInstanceRequest.builder();

                String applicationName = jsonObject.getString("applicationName");
                if (applicationName != null) {
                    builder.applicationName(applicationName);
                }

                String moduleName = jsonObject.getString("moduleName");
                if (moduleName != null) {
                    builder.moduleName(moduleName);
                }

                String jobXMLName = jsonObject.getString("jobXMLName");
                if (jobXMLName != null) {
                    builder.jobXMLName(jobXMLName);
                }

                JsonObject obj = jsonObject.getJsonObject("jobParameters");
                if (obj != null) {

                    String fhirResourceType = obj.getString("fhir.resourcetype");
                    if (fhirResourceType != null) {
                        builder.fhirResourceType(fhirResourceType);
                    }

                    String fhirSearchFromdate = obj.getString("fhir.search.fromdate");
                    if (fhirSearchFromdate != null) {
                        builder.fhirSearchFromDate(fhirSearchFromdate);
                    }

                    String cosBucketName = obj.getString("cos.bucket.name");
                    if (cosBucketName != null) {
                        builder.cosBucketName(cosBucketName);
                    }

                    String cosLocation = obj.getString("cos.location");
                    if (cosLocation != null) {
                        builder.cosLocation(cosLocation);
                    }

                    String cosEndpointUrl = obj.getString("cos.endpointurl");
                    if (cosEndpointUrl != null) {
                        builder.cosEndpointUrl(cosEndpointUrl);
                    }

                    String cosCredentialIbm = obj.getString("cos.credential.ibm");
                    if (cosCredentialIbm != null) {
                        builder.cosCredentialIbm(cosCredentialIbm);
                    }

                    String cosApiKey = obj.getString("cos.api.key");
                    if (cosApiKey != null) {
                        builder.cosApiKey(cosApiKey);
                    }

                    String cosSrvinstId = obj.getString("cos.srvinst.id");
                    if (cosSrvinstId != null) {
                        builder.cosSrvInstId(cosSrvinstId);
                    }

                    String fhirTenant = obj.getString("fhir.tenant");
                    if (fhirTenant != null) {
                        builder.fhirTenant(fhirTenant);
                    }

                    String fhirDataStoreId = obj.getString("fhir.datastoreid");
                    if (fhirDataStoreId != null) {
                        builder.fhirDataStoreId(fhirDataStoreId);
                    }

                    String cosBucketPathPrefix = obj.getString("cos.bucket.pathprefix");
                    if (cosBucketPathPrefix != null) {
                        builder.cosBucketPathPrefix(cosBucketPathPrefix);
                    }
                }

                return builder.build();
            } catch (Exception e) {
                throw new FHIROperationException("Problem parsing the Bulk Export Job's instance request from the server", e);
            }
        }

    }

    /**
     * Generates JSON from this object.
     *
     *
     */
    public static class Writer {

        private static final Map<java.lang.String, Object> properties =
                Collections.singletonMap(JsonGenerator.PRETTY_PRINTING, true);
        private static final JsonGeneratorFactory PRETTY_PRINTING_GENERATOR_FACTORY =
                Json.createGeneratorFactory(properties);

        /**
         *
         * @param obj
         * @param withSensitive
         *            - indicates it should or should not be included.
         * @return
         * @throws IOException
         */
        public static String generate(BulkExportJobInstanceRequest obj, boolean withSensitive)
            throws IOException {
            String o = "{}";
            try (StringWriter writer = new StringWriter();) {
                try (JsonGenerator generator =
                        PRETTY_PRINTING_GENERATOR_FACTORY.createGenerator(writer);) {
                    generator.writeStartObject();

                    if (obj.getApplicationName() != null) {
                        generator.write("applicationName", obj.getApplicationName());
                    }

                    if (obj.getModuleName() != null) {
                        generator.write("moduleName", obj.getModuleName());
                    }

                    if (obj.getJobXMLName() != null) {
                        generator.write("jobXMLName", obj.getJobXMLName());
                    }

                    generator.writeStartObject("jobParameters");

                    JobParameter parameter = obj.getJobParameters();

                    if (withSensitive) {
                        if (parameter.getCosApiKey() != null) {
                            generator.write("cos.api.key", parameter.getCosApiKey());
                        }
                    }

                    if (withSensitive) {
                        if (parameter.getCosBucketName() != null) {
                            generator.write("cos.bucket.name", parameter.getCosBucketName());
                        }
                    }

                    if (parameter.getCosCredentialIbm() != null) {
                        generator.write("cos.credential.ibm", parameter.getCosCredentialIbm());
                    }

                    if (withSensitive) {
                        if (parameter.getCosEndpointUrl() != null) {
                            generator.write("cos.endpointurl", parameter.getCosEndpointUrl());
                        }
                    }
                    if (parameter.getCosLocation() != null) {
                        generator.write("cos.location", parameter.getCosLocation());
                    }

                    if (withSensitive) {
                        if (parameter.getCosSrvInstId() != null) {
                            generator.write("cos.srvinst.id", parameter.getCosSrvInstId());
                        }
                    }

                    if (parameter.getFhirResourceType() != null) {
                        generator.write("fhir.resourcetype", parameter.getFhirResourceType());
                    }

                    if (parameter.getFhirSearchFromDate() != null) {
                        generator.write("fhir.search.fromdate", parameter.getFhirSearchFromDate());
                    }

                    if (parameter.getFhirTenant() != null) {
                        generator.write("fhir.tenant", parameter.getFhirTenant());
                    }

                    if (withSensitive) {
                        if (parameter.getCosBucketPathPrefix() != null) {
                            generator.write("cos.bucket.pathprefix", parameter.getCosBucketPathPrefix());
                        }
                    }

                    if (parameter.getFhirDataStoreId() != null) {
                        generator.write("fhir.datastoreid", parameter.getFhirDataStoreId());
                    }

                    generator.writeEnd();

                    generator.writeEnd();
                }
                o = writer.toString();
            }
            return o;
        }

    }

}
