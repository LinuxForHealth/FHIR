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
   “jobName”: “BulkImportJob”,
   “instanceId”: 9,
   “appName”: “fhir-bulkdata-webapp#fhir-bulkdata-webapp-4.0.0-SNAPSHOT.war”,
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
public class JobInstanceResponse {
    private String jobName;
    private Integer instanceId;
    private String appName;
    private String submitter;
    private String batchStatus;
    private String jobXMLName;
    private String instanceName;
    private String lastUpdatedTime;
    private String instanceState;
    private List<Integer> executionId;

    private List<Link> _links = new ArrayList<>();

    public List<Integer> getExecutionId() {
        return executionId;
    }

    public void setExecutionId(List<Integer> executionId) {
        this.executionId = executionId;
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

    public void addLink(Link link) {
        this._links.add(link);
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
    public static class Builder {

        private JobInstanceResponse response = new JobInstanceResponse();

        private Builder() {
            // Intentionally hiding from external callers.
        }

        public Builder executionId(List<Integer> executionId) {
            response.setExecutionId(executionId);
            return this;
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

        public Builder instanceState(String instanceState) {
            response.setInstanceState(instanceState);
            return this;
        }

        public Builder link(String rel, String href) {
            Link link = new Link();
            link.setHref(href);
            link.setRel(rel);
            response.addLink(link);
            return this;
        }

        public JobInstanceResponse build() {
            return response;
        }

    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Parser
     */
    public static class Parser {
        private Parser() {
            // NO Op
        }

        private static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);

        public static JobInstanceResponse parse(String jsonString) throws FHIROperationException {
            try (InputStream in = new ByteArrayInputStream(jsonString.getBytes())) {
                return JobInstanceResponse.Parser.parse(in);
            } catch (Exception e) {
                throw new FHIROperationException(
                        "Problem parsing the submission response from the job server", e);
            }
        }

        /**
         * parses an array of JobInstanceResponses
         * @param jsonString
         * @return
         * @throws FHIROperationException
         */
        public static List<JobInstanceResponse> parseArray(String jsonString) throws FHIROperationException {
            List<JobInstanceResponse> jers = new ArrayList<>();
            try (InputStream in = new ByteArrayInputStream(jsonString.getBytes());
                    JsonReader jsonReader = JSON_READER_FACTORY.createReader(in, StandardCharsets.UTF_8)) {
                JsonArray arr = jsonReader.readArray();
                Iterator<JsonValue> iter = arr.iterator();
                while (iter.hasNext()) {
                    JsonObject jsonObject = iter.next().asJsonObject();
                    jers.add(JobInstanceResponse.Parser.parse(jsonObject));
                }
            } catch (Exception e) {
                throw new FHIROperationException(
                        "Problem parsing the submission response from the job server", e);
            }
            return jers;
        }

        public static JobInstanceResponse parse(InputStream in) throws FHIROperationException {
            try (JsonReader jsonReader = JSON_READER_FACTORY.createReader(in, StandardCharsets.UTF_8)) {
                JsonObject jsonObject = jsonReader.readObject();
                return parse(jsonObject);
            } catch (Exception e) {
                throw new FHIROperationException("Problem parsing the Bulk Export Job's response from the server", e);
            }
        }

        public static JobInstanceResponse parse(JsonObject jsonObject) throws FHIROperationException {
            JobInstanceResponse.Builder builder = JobInstanceResponse.builder();

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

            if (jsonObject.containsKey("jobXMLName")) {
                String jobXMLName = jsonObject.getString("jobXMLName");
                builder.jobXMLName(jobXMLName);
            }

            if (jsonObject.containsKey("instanceName")) {
                String instanceName = jsonObject.getString("instanceName");
                builder.instanceName(instanceName);
            }

            if (jsonObject.containsKey("instanceState")) {
                String instanceState = jsonObject.getString("instanceState");
                builder.instanceState(instanceState);
            }

            if (jsonObject.containsKey("submitter")) {
                String submitter = jsonObject.getString("submitter");
                builder.submitter(submitter);
            }

            if (jsonObject.containsKey("lastUpdatedTime")) {
                String lastUpdatedTime = jsonObject.getString("lastUpdatedTime");
                builder.lastUpdatedTime(lastUpdatedTime);
            }

            List<Integer> jobExecutionId = new ArrayList<>();
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
                        if (rel.equalsIgnoreCase("job execution")) {
                            // e.g, https://localhost:9443/ibm/api/batch/jobinstances/9/jobexecutions/2
                            // Get the job execution id of the job instance at the end of the url, because the same job
                            // instance can be
                            // started, stopped and then restarted multipe times, so need to find the last job execution
                            // id and use it
                            // as the current job execution id.
                            int tmpJobExecutionId =
                                    Integer.parseInt(href.substring(href.indexOf("jobexecutions") + 14));
                            jobExecutionId.add(tmpJobExecutionId);
                        }
                    }
                }
            }
            if (jobExecutionId.isEmpty()) {
                jobExecutionId = Collections.singletonList(0);
            }
            builder.executionId(jobExecutionId);

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

        public static String generate(JobInstanceResponse obj) throws IOException {
            String o = "{}";
            try (StringWriter writer = new StringWriter();) {
                try (JsonGenerator generator = PRETTY_PRINTING_GENERATOR_FACTORY.createGenerator(writer);) {
                    generator.writeStartObject();

                    if (obj.getJobName() != null) {
                        generator.write("jobName", obj.getJobName());
                    }

                    if (obj.getInstanceId() != null) {
                        generator.write("instanceId", obj.getInstanceId());
                    }

                    if (obj.getInstanceState() != null) {
                        generator.write("instanceState", obj.getInstanceState());
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

                    if (obj.getJobXMLName() != null) {
                        generator.write("jobXMLName", obj.getJobXMLName());
                    }
                    if (obj.getInstanceName() != null) {
                        generator.write("instanceName", obj.getInstanceName());
                    }

                    if (obj.getLastUpdatedTime() != null) {
                        generator.write("lastUpdatedTime", obj.getLastUpdatedTime());
                    }

                    // Never empty, so ok to write.
                    generator.writeStartArray("_links");

                    // References
                    for (Link link : obj.getLinks()) {
                        generator.writeStartObject();
                        generator.write("href", link.getHref());
                        generator.write("rel", link.getRel());
                        generator.writeEnd();
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