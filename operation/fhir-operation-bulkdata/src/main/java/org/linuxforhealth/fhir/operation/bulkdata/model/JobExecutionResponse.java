/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.operation.bulkdata.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.operation.bulkdata.model.type.Input;
import org.linuxforhealth.fhir.operation.bulkdata.model.type.JobParameter;
import org.linuxforhealth.fhir.operation.bulkdata.model.type.StorageDetail;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;
import jakarta.json.JsonValue;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonGeneratorFactory;

/**
 * BulkImportJob's JSON response
 *
 * <pre>
 *  {
   “jobName”: “bulkexportchunkjob”,
   “instanceId”: 9,
   “appName”: “fhir-bulkdata-webapp#fhir-bulkdata-webapp.war”,
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
 * </pre>
 */
public class JobExecutionResponse {
    private String jobName;
    private Integer executionId;
    private Integer instanceId;
    private String appName;
    private String submitter;
    private String batchStatus;
    private String exitStatus;
    private String jobXMLName;
    private String instanceName;
    private String lastUpdatedTime;
    private String instanceState;
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

    public Integer getExecutionId() {
        return executionId;
    }

    public void setExecutionId(Integer executionId) {
        this.executionId = executionId;
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

    public void addLink(Link link) {
        this._links.add(link);
    }

    public String getExitStatus() {
        return exitStatus;
    }

    public void setExitStatus(String exitStatus) {
        this.exitStatus = exitStatus;
    }

    public String getInstanceState() {
        return instanceState;
    }

    public void setInstanceState(String instanceState) {
        this.instanceState = instanceState;
    }

    /**
     * Link is a sub class reflecting the link to the parts of the Export Job.
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
     */
    public static class Builder implements JobParameter.Builder {
        private JobExecutionResponse response = new JobExecutionResponse();
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

        public Builder executionId(Integer executionId) {
            response.setExecutionId(executionId);
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

        @Override
        public Builder fhirResourceType(String fhirResourceType) {
            jobParameter.setFhirResourceType(fhirResourceType);
            return this;
        }

        @Override
        public Builder fhirSearchFromDate(String fhirSearchFromDate) {
            jobParameter.setFhirSearchFromDate(fhirSearchFromDate);
            return this;
        }

        @Override
        public Builder cosBucketPathPrefix(String cosBucketPathPrefix) {
            jobParameter.setCosBucketPathPrefix(cosBucketPathPrefix);
            return this;
        }

        @Override
        public Builder fhirTenant(String fhirTenant) {
            jobParameter.setFhirTenant(fhirTenant);
            return this;
        }

        @Override
        public Builder fhirDataStoreId(String fhirDataStoreId) {
            jobParameter.setFhirDataStoreId(fhirDataStoreId);
            return this;
        }

        @Override
        public Builder fhirTypeFilters(String fhirTypeFilters) {
            jobParameter.setFhirTypeFilters(fhirTypeFilters);
            return this;
        }

        @Override
        public Builder fhirExportFormat(String mediaType) {
            jobParameter.setFhirExportFormat(mediaType);
            return this;
        }

        @Override
        public Builder fhirPatientGroupId(String fhirPatientGroupId) {
            jobParameter.setFhirPatientGroupId(fhirPatientGroupId);
            return this;
        }

        public Builder link(String rel, String href) {
            Link link = new Link();
            link.setHref(href);
            link.setRel(rel);
            response.addLink(link);
            return this;
        }

        public Builder instanceState(String instanceState) {
            response.setInstanceState(instanceState);
            return this;
        }

        public JobExecutionResponse build() {
            response.setJobParameters(jobParameter);
            return response;
        }

        @Override
        public Builder fhirDataSourcesInfo(List<Input> inputs) {
            jobParameter.setInputs(inputs);
            return this;
        }

        @Override
        public Builder fhirStorageType(StorageDetail storageDetails) {
            jobParameter.setStorageDetails(storageDetails);
            return this;
        }

        @Override
        public Builder incomingUrl(String incomingUrl) {
            jobParameter.setIncomingUrl(incomingUrl);
            return this;
        }

        @Override
        public Builder source(String source) {
            jobParameter.setSource(source);
            return this;
        }

        @Override
        public Builder outcome(String outcome) {
            jobParameter.setOutcome(outcome);
            return this;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Parser
     */
    public static class Parser {
        private static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);

        private Parser() {
            // No Op
        }

        public static JobExecutionResponse parse(String jsonString) throws FHIROperationException {
            try (InputStream in = new ByteArrayInputStream(jsonString.getBytes())) {
                return JobExecutionResponse.Parser.parse(in);
            } catch (Exception e) {
                throw new FHIROperationException(
                        "Problem parsing the Bulk Export Job's from jsonString response from the server", e);
            }
        }

        /**
         * parses an array of job execution response
         * @param jsonString
         * @return
         * @throws FHIROperationException
         */
        public static List<JobExecutionResponse> parseArray(String jsonString) throws FHIROperationException {
            try (InputStream in = new ByteArrayInputStream(jsonString.getBytes())) {
                try (JsonReader jsonReader =
                        JSON_READER_FACTORY.createReader(in, StandardCharsets.UTF_8)) {
                    List<JobExecutionResponse> res = new ArrayList<>();
                    JsonArray arr = jsonReader.readArray();
                    Iterator<JsonValue> iter = arr.iterator();
                    while(iter.hasNext()) {
                        JsonObject jsonObject = iter.next().asJsonObject();
                        res.add(parse(jsonObject));
                    }
                    return res;
                }
            } catch (Exception e) {
                throw new FHIROperationException(
                        "Problem parsing the Bulk Export Job's from jsonString response from the server", e);
            }
        }

        public static JobExecutionResponse parse(InputStream in) throws FHIROperationException {
            try (JsonReader jsonReader =
                    JSON_READER_FACTORY.createReader(in, StandardCharsets.UTF_8)) {
                JsonObject jsonObject = jsonReader.readObject();
                return parse(jsonObject);
            } catch (Exception e) {
                throw new FHIROperationException(
                        "Problem parsing the Bulk Export Job's from jsonString response from the server", e);
            }
        }

        public static JobExecutionResponse parse(JsonObject jsonObject) throws FHIROperationException, IOException {

                JobExecutionResponse.Builder builder = JobExecutionResponse.builder();

                if (jsonObject.containsKey("jobName")) {
                    String jobName = jsonObject.getString("jobName");
                    builder.jobName(jobName);
                }

                if (jsonObject.containsKey("instanceId")) {
                    Integer instanceId = jsonObject.getInt("instanceId");
                    builder.instanceId(instanceId);
                }

                if (jsonObject.containsKey("executionId")) {
                    Integer executionId = jsonObject.getInt("executionId");
                    builder.executionId(executionId);
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

                        if (vObj.containsKey("rel") && vObj.containsKey("href")) {
                            String rel = vObj.getString("rel");
                            String href = vObj.getString("href");
                            builder.link(rel, href);
                        }
                    }
                }

                if (jsonObject.containsKey("submitter")) {
                    String submitter = jsonObject.getString("submitter");
                    builder.submitter(submitter);
                }

                if (jsonObject.containsKey("instanceState")) {
                    String instanceState = jsonObject.getString("instanceState");
                    builder.instanceState(instanceState);
                }

                if (jsonObject.containsKey("jobParameters")) {
                    JsonObject obj = jsonObject.getJsonObject("jobParameters");
                    JobParameter.Parser.parse(builder, obj);
                }

                return builder.build();
        }
    }

    /**
     * Generates JSON from this object.
     */
    public static class Writer {
        // This is an internal model and does not need to honor _pretty printing as it is only communicating with the java batch framework.
        private static final Map<java.lang.String, Object> properties =
                Collections.singletonMap(JsonGenerator.PRETTY_PRINTING, true);
        private static final JsonGeneratorFactory PRETTY_PRINTING_GENERATOR_FACTORY =
                Json.createGeneratorFactory(properties);

        private Writer() {
            // No Op
        }

        public static String generate(JobExecutionResponse obj, boolean withSensitive) throws IOException {
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

                    if (obj.getExecutionId() != null) {
                        generator.write("executionId", obj.getExecutionId());
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

                    if (obj.getInstanceState() != null) {
                        generator.write("instanceState", obj.getInstanceState());
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

                    if (obj.getJobParameters() != null) {
                        generator.writeStartObject("jobParameters");
                        JobParameter parameter = obj.getJobParameters();
                        JobParameter.Writer.generate(generator, parameter, withSensitive);
                    }

                    generator.writeEnd();
                }
                o = writer.toString();
            }
            return o;
        }
    }

    @Override
    public String toString() {
        return "JobExecutionResponse [jobName=" + jobName + ", executionId=" + executionId + ", instanceId=" + instanceId + ", appName=" + appName
                + ", submitter=" + submitter + ", batchStatus=" + batchStatus + ", exitStatus=" + exitStatus + ", jobXMLName=" + jobXMLName + ", instanceName="
                + instanceName + ", lastUpdatedTime=" + lastUpdatedTime + ", instanceState=" + instanceState + ", _links=" + _links + ", jobParameters="
                + jobParameters + "]";
    }
}