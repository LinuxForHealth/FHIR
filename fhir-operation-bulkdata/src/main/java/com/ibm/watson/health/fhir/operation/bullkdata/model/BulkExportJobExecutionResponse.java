/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.watson.health.fhir.operation.bullkdata.model;

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

import com.ibm.watson.health.fhir.exception.FHIROperationException;

/**
 * BulkImportJob's JSON response
 * 
 * <code>
 *  {
   “jobName”: “BulkImportJob”,
   “instanceId”: 9,
   “appName”: “fhir-bulkimportexport-webapp#fhir-bulkimportexport-webapp-4.0.0-SNAPSHOT.war”,
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
 *  </code>
 * 
 * @author pbastide
 *
 */
public class BulkExportJobExecutionResponse {
    private String jobName;
    private Integer instanceId;
    private String appName;
    private String submitter;
    private String batchStatus;
    private String jobXMLName;
    private String instanceName;
    private String lastUpdatedTime;
    private List<Link> _links = new ArrayList<>();

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

    /**
     * Link is a sub class reflecting the link to the parts of the Export Job.
     * 
     * @author pbastide
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
     * @author pbastide
     *
     */
    public static class Builder {

        private BulkExportJobExecutionResponse response = new BulkExportJobExecutionResponse();

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

        public Builder link(String rel, String href) {
            Link link = new Link();
            link.setHref(href);
            link.setRel(rel);
            response.addLink(link);
            return this;
        }

        public BulkExportJobExecutionResponse build() {
            return response;
        }

    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * 
     * @author pbastide
     *
     */
    public static class Parser {

        private static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);

        public static BulkExportJobExecutionResponse parse(InputStream in) throws FHIROperationException {
            try (JsonReader jsonReader =
                    JSON_READER_FACTORY.createReader(in, StandardCharsets.UTF_8)) {
                JsonObject jsonObject = jsonReader.readObject();
                BulkExportJobExecutionResponse.Builder builder = BulkExportJobExecutionResponse.builder();

                String jobName = jsonObject.getString("jobName");
                if (jobName != null) {
                    builder.jobName(jobName);
                }

                Integer instanceId = jsonObject.getInt("instanceId");
                if (instanceId != null) {
                    builder.instanceId(instanceId);
                }

                String appName = jsonObject.getString("appName");
                if (appName != null) {
                    builder.appName(appName);
                }

                String batchStatus = jsonObject.getString("batchStatus");
                if (batchStatus != null) {
                    builder.batchStatus(batchStatus);
                }

                String jobXMLName = jsonObject.getString("jobXMLName");
                if (jobXMLName != null) {
                    builder.jobXMLName(jobXMLName);
                }

                String instanceName = jsonObject.getString("instanceName");
                if (instanceName != null) {
                    builder.instanceName(instanceName);
                }

                String lastUpdatedTime = jsonObject.getString("lastUpdatedTime");
                if (lastUpdatedTime != null) {
                    builder.lastUpdatedTime(lastUpdatedTime);
                }

                JsonArray arr = jsonObject.getJsonArray("_links");
                if (arr != null) {
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

                return builder.build();
            } catch (Exception e) {
                throw new FHIROperationException("Problem parsing the Bulk Export Job's response from the server", e);
            }
        }

    }

    /**
     * Generates JSON from this object.
     * 
     * @author pbastide
     *
     */
    public static class Writer {

        private static final Map<java.lang.String, Object> properties =
                Collections.singletonMap(JsonGenerator.PRETTY_PRINTING, true);
        private static final JsonGeneratorFactory PRETTY_PRINTING_GENERATOR_FACTORY =
                Json.createGeneratorFactory(properties);

        public static String generate(BulkExportJobExecutionResponse obj) throws IOException {
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

                    generator.writeEnd();
                }
                o = writer.toString();
            }
            return o;
        }

    }

}
