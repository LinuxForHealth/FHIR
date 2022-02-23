/*
 * (C) Copyright IBM Corp. 2022
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

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.persistence.bulkdata.InputDTO;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;
import jakarta.json.JsonValue;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonGeneratorFactory;

/**
 * Job Output is an DTO to facilitate larger outputs.
 */
public class JobOutput {
    private String source;
    private String incomingUrl;
    private String bucketPathPrefix;
    private String jobType;
    private String lastUpdatedTime;
    private String exportOutput;
    private String importOutput;
    private List<InputDTO> inputs = new ArrayList<>();

    public String getSource() {
        return source;
    }

    public String getBucketPathPrefix() {
        return bucketPathPrefix;
    }

    public String getJobType() {
        return jobType;
    }

    public List<InputDTO> getInputs() {
        return inputs;
    }

    public String getIncomingUrl() {
        return incomingUrl;
    }

    public String getExportOutput() {
        return exportOutput;
    }

    public String getImportOutput() {
        return importOutput;
    }

    public boolean isExport() {
        return jobType.startsWith("EXPORT");
    }

    public boolean isImport() {
        return jobType.startsWith("IMPORT");
    }

    public String getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static JobOutput parse(String blob) throws FHIROperationException {
        return Parser.parse(blob);
    }

    /**
     * Builder
     */
    public static class Builder {

        private JobOutput output = new JobOutput();

        private Builder() {
            // NOP
        }

        public Builder source(String source) {
            output.source = source;
            return this;
        }

        public Builder incomingUrl(String incomingUrl) {
            output.incomingUrl = incomingUrl;
            return this;
        }

        public Builder bucketPathPrefix(String bucketPathPrefix) {
            output.bucketPathPrefix = bucketPathPrefix;
            return this;
        }

        public Builder jobType(String jobType) {
            output.jobType = jobType;
            return this;
        }

        public Builder lastUpdatedTime(String lastUpdatedTime) {
            output.lastUpdatedTime = lastUpdatedTime;
            return this;
        }

        public Builder exportOutput(String exportOutput) {
            output.exportOutput = exportOutput;
            return this;
        }

        public Builder importOutput(String importOutput) {
            output.importOutput = importOutput;
            return this;
        }

        public Builder inputs(List<InputDTO> inputs) {
            output.inputs = inputs;
            return this;
        }

        public JobOutput build() {
            return output;
        }
    }

    public static class Parser {
        private Parser() {
            // NO Op
        }

        private static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);

        public static JobOutput parse(String jsonString) throws FHIROperationException {
            try (InputStream in = new ByteArrayInputStream(jsonString.getBytes())) {
                return JobOutput.Parser.parse(in);
            } catch (Exception e) {
                throw new FHIROperationException("Problem parsing the submission response from the job server", e);
            }
        }

        public static JobOutput parse(InputStream in) throws FHIROperationException {
            try (JsonReader jsonReader = JSON_READER_FACTORY.createReader(in, StandardCharsets.UTF_8)) {
                JsonObject jsonObject = jsonReader.readObject();
                return parse(jsonObject);
            } catch (Exception e) {
                throw new FHIROperationException("Problem parsing the Bulk Export Job's response from the server", e);
            }
        }

        public static JobOutput parse(JsonObject jsonObject) throws FHIROperationException {
            JobOutput.Builder builder = JobOutput.builder();

            if (jsonObject.containsKey("source")) {
                builder.source(jsonObject.getString("source"));
            }

            if (jsonObject.containsKey("incomingUrl")) {
                builder.incomingUrl(jsonObject.getString("incomingUrl"));
            }

            if (jsonObject.containsKey("bucketPathPrefix")) {
                builder.bucketPathPrefix(jsonObject.getString("bucketPathPrefix"));
            }

            if (jsonObject.containsKey("jobType")) {
                builder.jobType(jsonObject.getString("jobType"));
            }

            if (jsonObject.containsKey("lastUpdatedTime")) {
                builder.lastUpdatedTime(jsonObject.getString("lastUpdatedTime"));
            }

            if (jsonObject.containsKey("exportOutput")) {
                builder.exportOutput(jsonObject.getString("exportOutput"));
            }

            if (jsonObject.containsKey("importOutput")) {
                builder.importOutput(jsonObject.getString("importOutput"));
            }
            if (jsonObject.containsKey("inputs")) {
                JsonArray arr = jsonObject.getJsonArray("inputs");
                ListIterator<JsonValue> iter = arr.listIterator();
                List<InputDTO> inputs = new ArrayList<>();
                while (iter.hasNext()) {
                    JsonValue v = iter.next();
                    JsonObject vObj = v.asJsonObject();

                    if (vObj.containsKey("type") && vObj.containsKey("url")) {
                        String type = vObj.getString("type");
                        String url = vObj.getString("url");
                        inputs.add(new InputDTO(type, url));
                    }
                }
                builder.inputs(inputs);
            }
            return builder.build();
        }

    }

    /*
     * This is an internal only class not intended to be used out of bulkdata. The
     * class serializes the PollingLocationResponse into a JSON object.
     */
    public static class Writer {
        private Writer() {
            // No Operation
        }

        public static String generate(JobOutput response) throws IOException {
            Boolean pretty = FHIRConfigHelper.getBooleanProperty(FHIRConfiguration.PROPERTY_DEFAULT_PRETTY_PRINT,
                    false);
            final Map<java.lang.String, Object> properties = Collections.singletonMap(JsonGenerator.PRETTY_PRINTING,
                    pretty);
            final JsonGeneratorFactory factory = Json.createGeneratorFactory(properties);

            String o = "{}";
            try (StringWriter writer = new StringWriter();) {
                try (JsonGenerator generator = factory.createGenerator(writer);) {

                    generator.writeStartObject();
                    if (response.getSource() != null) {
                        generator.write("source", response.getSource());
                    }

                    if (response.getIncomingUrl() != null) {
                        generator.write("incomingUrl", response.getIncomingUrl());
                    }

                    if (response.getBucketPathPrefix() != null) {
                        generator.write("bucketPathPrefix", response.getBucketPathPrefix());
                    }

                    if (response.getJobType() != null) {
                        generator.write("jobType", response.getJobType());
                    }

                    if (response.getLastUpdatedTime() != null) {
                        generator.write("lastUpdatedTime", response.getLastUpdatedTime());
                    }

                    if (response.getExportOutput() != null) {
                        generator.write("exportOutput", response.getExportOutput());
                    }

                    if (response.getImportOutput() != null) {
                        generator.write("importOutput", response.getImportOutput());
                    }

                    if (response.getInputs() != null) {
                        // outputs the output array.
                        generator.writeStartArray("inputs");
                        for (InputDTO input : response.getInputs()) {
                            generator.writeStartObject();
                            generator.write("type", input.getType());
                            generator.write("url", input.getUrl());
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