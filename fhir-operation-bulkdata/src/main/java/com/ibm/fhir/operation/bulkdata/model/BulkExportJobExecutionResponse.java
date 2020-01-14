/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.bulkdata.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.json.JsonValue;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;

import com.ibm.fhir.exception.FHIROperationException;

/**
 * BulkImportJob's JSON response
 *
 * <pre>
 *  {
   “jobName”: “bulkexportchunkjob”,
   “instanceId”: 9,
   “appName”: “fhir-bulkimportexport-webapp#fhir-bulkimportexport.war”,
   “submitter”: “fhiruser”,
   “batchStatus”: “STARTING”,
   “jobXMLName”: “FhirBulkExportChunkJob”,
   “instanceState”: “SUBMITTED”,
   “lastUpdatedTime”: “2019/09/12 15:18:01.416 -0400”,
   “_links”: [
       {
           “rel”: “self”,
           “href”: “https://localhost:9443/ibm/api/batch/jobinstances/9”
       },
       {
           “rel”: “job logs”,
           “href”: “https://localhost:9443/ibm/api/batch/jobinstances/9/joblogs”
       }
   ]
    }
 *  </pre
 *
 */
public class BulkExportJobExecutionResponse {
    private String jobName;
    private Integer instanceId;
    private String appName;
    private String submitter;
    private String batchStatus;
    private String exitStatus;
    private String jobXMLName;
    private String instanceName;
    private String lastUpdatedTime;
    private List<Link> _links = new ArrayList<>();

    private JobParameter jobParameters;

    public JobParameter getJobParameters() {
        return jobParameters;
    }

    public void setJobParameters(JobParameter jobParameters) {
        this.jobParameters = jobParameters;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Integer getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Integer instanceId) {
        this.instanceId = instanceId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public String getBatchStatus() {
        return batchStatus;
    }

    public void setBatchStatus(String batchStatus) {
        this.batchStatus = batchStatus;
    }

    public String getJobXMLName() {
        return jobXMLName;
    }

    public void setJobXMLName(String jobXMLName) {
        this.jobXMLName = jobXMLName;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(String lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public List<Link> getLinks() {
        return _links;
    }

    public void setLinks(List<Link> _links) {
        this._links = _links;
    }

    public void addLink(Link link) {
        this._links.add(link);
    }

    public String getExitStatus() {
        return exitStatus;
    }

    public void setExitStatus(String exitStatus) {
        this.exitStatus = exitStatus;
    }

    /**
     * Link is a sub class reflecting the link to the parts of the Export Job.
     *
     */
    public static class Link {
        private String rel;
        private String href;

        public String getRel() {
            return rel;
        }

        public void setRel(String rel) {
            this.rel = rel;
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

    }

    /**
     * Builder is a convenience pattern to assemble to Java Object that reflects the BatchManagement pattern.
     *
     */
    public static class Builder {

        private BulkExportJobExecutionResponse response = new BulkExportJobExecutionResponse();
        private JobParameter jobParameter = new JobParameter();

        private Builder() {
            // Intentionally hiding from external callers.
        }

        public Builder jobName(String jobName) {
            response.setJobName(jobName);
            return this;
        }

        public Builder instanceId(Integer instanceId) {
            response.setInstanceId(instanceId);
            return this;
        }

        public Builder appName(String appName) {
            response.setAppName(appName);
            return this;
        }

        public Builder submitter(String submitter) {
            response.setSubmitter(submitter);
            return this;
        }

        public Builder batchStatus(String batchStatus) {
            response.setBatchStatus(batchStatus);
            return this;
        }

        public Builder exitStatus(String exitStatus) {
            response.setExitStatus(exitStatus);
            return this;
        }

        public Builder jobXMLName(String jobXMLName) {
            response.setJobXMLName(jobXMLName);
            return this;
        }

        public Builder instanceName(String instanceName) {
            response.setInstanceName(instanceName);
            return this;
        }

        public Builder lastUpdatedTime(String lastUpdatedTime) {
            response.setLastUpdatedTime(lastUpdatedTime);
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

        public Builder cosBucketPathPrefix(String cosBucketPathPrefix) {
            jobParameter.setCosBucketPathPrefix(cosBucketPathPrefix);
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

        public Builder link(String rel, String href) {
            Link link = new Link();
            link.setHref(href);
            link.setRel(rel);
            response.addLink(link);
            return this;
        }

        public BulkExportJobExecutionResponse build() {
            response.setJobParameters(jobParameter);
            return response;
        }

    }

    public static Builder builder() {
        return new Builder();
    }


    public static class Parser {

        private Parser() {
            // No Op
        }

        private static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);

        public static BulkExportJobExecutionResponse parse(String jsonString) throws FHIROperationException {

            try(InputStream in = new ByteArrayInputStream(jsonString.getBytes())){

                return BulkExportJobExecutionResponse.Parser.parse(in);

            } catch(Exception e) {
                throw new FHIROperationException("Problem parsing the Bulk Export Job's from jsonString response from the server", e);
            }
        }

        public static BulkExportJobExecutionResponse parse(InputStream in) throws FHIROperationException {
            try (JsonReader jsonReader =
                    JSON_READER_FACTORY.createReader(in, StandardCharsets.UTF_8)) {
                JsonObject jsonObject = jsonReader.readObject();
                BulkExportJobExecutionResponse.Builder builder = BulkExportJobExecutionResponse.builder();


                if (jsonObject.containsKey("jobName")) {
                    String jobName = jsonObject.getString("jobName");
                    builder.jobName(jobName);
                }


                if (jsonObject.containsKey("instanceId")) {
                    Integer instanceId = jsonObject.getInt("instanceId");
                    builder.instanceId(instanceId);
                }

                if (jsonObject.containsKey("appName")) {
                    String appName = jsonObject.getString("appName");
                    builder.appName(appName);
                }

                if (jsonObject.containsKey("batchStatus")) {
                    String batchStatus = jsonObject.getString("batchStatus");
                    builder.batchStatus(batchStatus);
                }

                if (jsonObject.containsKey("exitStatus")) {
                    String exitStatus = jsonObject.getString("exitStatus");
                    builder.exitStatus(exitStatus);
                }

                if (jsonObject.containsKey("jobXMLName")) {
                    String jobXMLName = jsonObject.getString("jobXMLName");
                    builder.jobXMLName(jobXMLName);
                }

                if (jsonObject.containsKey("instanceName")) {
                    String instanceName = jsonObject.getString("instanceName");
                    builder.instanceName(instanceName);
                }

                if (jsonObject.containsKey("lastUpdatedTime")) {
                    String lastUpdatedTime = jsonObject.getString("lastUpdatedTime");
                    builder.lastUpdatedTime(lastUpdatedTime);
                }


                if (jsonObject.containsKey("_links")) {
                    JsonArray arr = jsonObject.getJsonArray("_links");
                    ListIterator<JsonValue> iter = arr.listIterator();
                    while (iter.hasNext()) {
                        JsonValue v = iter.next();
                        JsonObject vObj = v.asJsonObject();

                        String rel = vObj.getString("rel");
                        String href = vObj.getString("href");

                        if (rel != null && href != null) {
                            builder.link(rel, href);
                        }
                    }
                }


                if (jsonObject.containsKey("jobParameters")) {
                    JsonObject obj = jsonObject.getJsonObject("jobParameters");
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
                e.printStackTrace();
                throw new FHIROperationException("Problem parsing the Bulk Export Job's response from the server", e);
            }
        }

    }

    /**
     * Generates JSON from this object.
     *
     */
    public static class Writer {

        private static final Map<java.lang.String, Object> properties =
                Collections.singletonMap(JsonGenerator.PRETTY_PRINTING, true);
        private static final JsonGeneratorFactory PRETTY_PRINTING_GENERATOR_FACTORY =
                Json.createGeneratorFactory(properties);

        public static String generate(BulkExportJobExecutionResponse obj, boolean withSensitive) throws IOException {
            String o = "{}";
            try (StringWriter writer = new StringWriter();) {
                try (JsonGenerator generator =
                        PRETTY_PRINTING_GENERATOR_FACTORY.createGenerator(writer);) {
                    generator.writeStartObject();

                    if (obj.getJobName() != null) {
                        generator.write("jobName", obj.getJobName());
                    }

                    if (obj.getInstanceId() != null) {
                        generator.write("instanceId", obj.getInstanceId());
                    }

                    if (obj.getAppName() != null) {
                        generator.write("appName", obj.getAppName());
                    }

                    if (obj.getSubmitter() != null) {
                        generator.write("submitter", obj.getSubmitter());
                    }

                    if (obj.getBatchStatus() != null) {
                        generator.write("batchStatus", obj.getBatchStatus());
                    }

                    if (obj.getExitStatus() != null) {
                        generator.write("exitStatus", obj.getExitStatus());
                    }

                    if (obj.getJobXMLName() != null) {
                        generator.write("jobXMLName", obj.getJobXMLName());
                    }
                    if (obj.getInstanceName() != null) {
                        generator.write("instanceName", obj.getInstanceName());
                    }

                    if (obj.getLastUpdatedTime() != null) {
                        generator.write("lastUpdatedTime", obj.getLastUpdatedTime());
                    }

                    if (obj.getLinks() != null) {

                        generator.writeStartArray("_links");

                        // References
                        for (Link link : obj.getLinks()) {
                            generator.writeStartObject();
                            generator.write("href", link.getHref());
                            generator.write("rel", link.getRel());
                            generator.writeEnd();
                        }

                        generator.writeEnd();
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

                    if (withSensitive) {
                        if (parameter.getCosBucketPathPrefix() != null) {
                            generator.write("cos.bucket.pathprefix", parameter.getCosBucketPathPrefix());
                        }
                    }

                    if (parameter.getFhirResourceType() != null) {
                        generator.write("fhir.resourcetype", parameter.getFhirResourceType());
                    }

                    if (parameter.getFhirSearchFromDate() != null) {
                        generator.write("fhir.search.fromdate", parameter.getFhirSearchFromDate());
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
