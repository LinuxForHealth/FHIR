/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.model.type;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;
import jakarta.json.JsonValue;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonGeneratorFactory;

import com.ibm.fhir.exception.FHIROperationException;

/**
 * Common Configuration Parameters for the Job Request/Response.
 */
public class JobParameter {
    private String fhirResourceType;
    private String fhirSearchFromDate;
    private String fhirTenant;
    private String fhirDataStoreId;
    private String fhirPatientGroupId;
    private String fhirTypeFilters;
    private String fhirExportFormat;
    private List<Input> inputs;
    private StorageDetail storageDetail;
    private String source;
    private String outcome;

    private String incomingUrl;

    private String cosBucketPathPrefix;

    public String getFhirPatientGroupId() {
        return fhirPatientGroupId;
    }

    public void setFhirPatientGroupId(String fhirPatientGroupId) {
        this.fhirPatientGroupId = fhirPatientGroupId;
    }

    public String getCosBucketPathPrefix() {
        return cosBucketPathPrefix;
    }

    public void setCosBucketPathPrefix(String cosBucketPathPrefix) {
        this.cosBucketPathPrefix = cosBucketPathPrefix;
    }

    public String getFhirResourceType() {
        return fhirResourceType;
    }

    public void setFhirResourceType(String fhirResourceType) {
        this.fhirResourceType = fhirResourceType;
    }

    public String getFhirSearchFromDate() {
        return fhirSearchFromDate;
    }

    public void setFhirSearchFromDate(String fhirSearchFromDate) {
        this.fhirSearchFromDate = fhirSearchFromDate;
    }

    public String getFhirTenant() {
        return fhirTenant;
    }

    public void setFhirTenant(String fhirTenant) {
        this.fhirTenant = fhirTenant;
    }

    public String getFhirDataStoreId() {
        return fhirDataStoreId;
    }

    public void setFhirDataStoreId(String fhirDataStoreId) {
        this.fhirDataStoreId = fhirDataStoreId;
    }

    public String getFhirTypeFilters() {
        return fhirTypeFilters;
    }

    public void setFhirTypeFilters(String fhirTypeFilters) {
        this.fhirTypeFilters = fhirTypeFilters;
    }

    public String getFhirExportFormat() {
        return fhirExportFormat;
    }

    public void setFhirExportFormat(String fhirExportFormat) {
        this.fhirExportFormat = fhirExportFormat;
    }

    public List<Input> getInputs() {
        return inputs;
    }

    public void setInputs(List<Input> inputs) {
        this.inputs = inputs;
    }

    public StorageDetail getStorageDetails() {
        return storageDetail;
    }

    public void setStorageDetails(StorageDetail storageDetail) {
        this.storageDetail = storageDetail;
    }

    public void setIncomingUrl(String incomingUrl) {
        this.incomingUrl = incomingUrl;
    }

    public String getIncomingUrl() {
        return this.incomingUrl;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getOutcome() {
        return this.outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    /**
     * Generates JSON from this object.
     */
    public static class Writer {
        private static final Map<java.lang.String, Object> properties =
                Collections.singletonMap(JsonGenerator.PRETTY_PRINTING, true);
        private static final JsonGeneratorFactory PRETTY_PRINTING_GENERATOR_FACTORY =
                Json.createGeneratorFactory(properties);

        private Writer() {
            // No Operation
        }

        /**
         * @param generator
         * @param parameter     job parameter details.
         * @param withSensitive indicates sensitive information should or should not be included.
         * @throws IOException
         */
        public static void generate(JsonGenerator generator, JobParameter parameter, boolean withSensitive)
                throws IOException {

            if (parameter.getFhirResourceType() != null) {
                generator.write(OperationFields.FHIR_RESOURCE_TYPES, parameter.getFhirResourceType());
            }

            if (parameter.getFhirSearchFromDate() != null) {
                generator.write(OperationFields.FHIR_SEARCH_FROM_DATE, parameter.getFhirSearchFromDate());
            }

            if (parameter.getFhirTenant() != null) {
                generator.write(OperationFields.FHIR_TENANT_ID, parameter.getFhirTenant());
            }

            if (parameter.getFhirPatientGroupId() != null) {
                generator.write(OperationFields.FHIR_SEARCH_PATIENT_GROUP_ID, parameter.getFhirPatientGroupId());
            }

            if (withSensitive) {
                if (parameter.getCosBucketPathPrefix() != null) {
                    generator.write(OperationFields.COS_BUCKET_PATH_PREFIX, parameter.getCosBucketPathPrefix());
                }
            }

            if (parameter.getFhirDataStoreId() != null) {
                generator.write(OperationFields.FHIR_DATASTORE_ID, parameter.getFhirDataStoreId());
            }

            if (parameter.getFhirTypeFilters() != null) {
                generator.write(OperationFields.FHIR_SEARCH_TYPE_FILTERS, parameter.getFhirTypeFilters());
            }

            if (parameter.getFhirExportFormat() != null) {
                generator.write(OperationFields.FHIR_EXPORT_FORMAT, parameter.getFhirExportFormat());
            }

            if (parameter.getInputs() != null) {
                generator.write(OperationFields.FHIR_DATA_SOURCES_INFO, writeToBase64(parameter.getInputs()));
            }

            if (parameter.getSource() != null) {
                generator.write(OperationFields.FHIR_BULKDATA_SOURCE, parameter.getSource());
            }

            if (parameter.getOutcome() != null) {
                generator.write(OperationFields.FHIR_BULKDATA_OUTCOME, parameter.getOutcome());
            }

            String type = com.ibm.fhir.operation.bulkdata.model.type.StorageType.HTTPS.value();
            if (parameter.getStorageDetails() != null) {
                type = parameter.getStorageDetails().getType();
            }

            generator.write(OperationFields.FHIR_IMPORT_STORAGE_TYPE, type);

            if (parameter.getIncomingUrl() != null) {
                generator.write(OperationFields.FHIR_INCOMING_URL, parameter.getIncomingUrl());
            }

            generator.writeEnd();
        }

        public static String writeToBase64(List<Input> inputs) throws IOException {
            String sources = "";
            try (StringWriter writer = new StringWriter();) {
                try (JsonGenerator generator = PRETTY_PRINTING_GENERATOR_FACTORY.createGenerator(writer);) {
                    generator.writeStartArray();
                    for (Input input : inputs) {
                        generator.writeStartObject();
                        generator.write("type", input.getType());
                        generator.write("url", input.getUrl());
                        generator.writeEnd();
                    }
                    generator.writeEnd();
                }
                sources = writer.toString();
            }

            // Intentionally NOT returning immediately as there may be a need to debug
            String base64 = Base64.getEncoder().encodeToString(sources.getBytes());
            return base64;
        }
    }

    /**
     * common build parameters for JobParameters
     */
    public interface Builder {
        public Builder fhirResourceType(String fhirResourceType);

        public Builder fhirSearchFromDate(String fhirSearchFromDate);

        public Builder fhirTenant(String fhirTenant);

        public Builder fhirDataStoreId(String fhirDataStoreId);

        public Builder fhirPatientGroupId(String fhirPatientGroupId);

        public Builder cosBucketPathPrefix(String cosBucketPathPrefix);

        public Builder fhirTypeFilters(String fhirTypeFilters);

        public Builder fhirDataSourcesInfo(List<Input> inputs);

        public Builder fhirStorageType(StorageDetail storageDetail);

        public Builder fhirExportFormat(String mediaType);

        public Builder incomingUrl(String incomingUrl);

        public Builder source(String source);

        public Builder outcome(String outcome);
    }

    public static class Parser {
        private static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);

        private Parser() {
            // No Op
        }

        public static void parse(Builder builder, JsonObject obj) throws FHIROperationException, IOException {
            if (obj.containsKey(OperationFields.FHIR_RESOURCE_TYPES)) {
                String fhirResourceType = obj.getString(OperationFields.FHIR_RESOURCE_TYPES);
                builder.fhirResourceType(fhirResourceType);
            }

            if (obj.containsKey(OperationFields.FHIR_SEARCH_FROM_DATE)) {
                String fhirSearchFromdate = obj.getString(OperationFields.FHIR_SEARCH_FROM_DATE);
                builder.fhirSearchFromDate(fhirSearchFromdate);
            }

            if (obj.containsKey(OperationFields.FHIR_TENANT_ID)) {
                String fhirTenant = obj.getString(OperationFields.FHIR_TENANT_ID);
                builder.fhirTenant(fhirTenant);
            }

            if (obj.containsKey(OperationFields.FHIR_DATASTORE_ID)) {
                String fhirDataStoreId = obj.getString(OperationFields.FHIR_DATASTORE_ID);
                builder.fhirDataStoreId(fhirDataStoreId);
            }

            if (obj.containsKey(OperationFields.COS_BUCKET_PATH_PREFIX)) {
                String cosBucketPathPrefix = obj.getString(OperationFields.COS_BUCKET_PATH_PREFIX);
                builder.cosBucketPathPrefix(cosBucketPathPrefix);
            }

            if (obj.containsKey(OperationFields.FHIR_SEARCH_TYPE_FILTERS)) {
                String fhirTypeFilters = obj.getString(OperationFields.FHIR_SEARCH_TYPE_FILTERS);
                builder.fhirTypeFilters(fhirTypeFilters);
            }

            if (obj.containsKey(OperationFields.FHIR_SEARCH_PATIENT_GROUP_ID)) {
                String fhirPatientGroupId = obj.getString(OperationFields.FHIR_SEARCH_PATIENT_GROUP_ID);
                builder.fhirPatientGroupId(fhirPatientGroupId);
            }

            if (obj.containsKey(OperationFields.FHIR_DATA_SOURCES_INFO)) {
                String dataSourcesInfo = obj.getString(OperationFields.FHIR_DATA_SOURCES_INFO);
                // Base64 at this point, and we just dump it into an intermediate value until it's needed.
                builder.fhirDataSourcesInfo(parseInputsFromString(dataSourcesInfo));
            }

            if (obj.containsKey(OperationFields.FHIR_IMPORT_STORAGE_TYPE)) {
                String storageType = obj.getString(OperationFields.FHIR_IMPORT_STORAGE_TYPE);
                builder.fhirStorageType(new StorageDetail(storageType, Collections.emptyList()));
            }

            if (obj.containsKey(OperationFields.FHIR_INCOMING_URL)) {
                String incomingUrl = obj.getString(OperationFields.FHIR_INCOMING_URL);
                builder.incomingUrl(incomingUrl);
            }

            if (obj.containsKey(OperationFields.FHIR_BULKDATA_OUTCOME)) {
                String outcome = obj.getString(OperationFields.FHIR_BULKDATA_OUTCOME);
                builder.outcome(outcome);
            }

            if (obj.containsKey(OperationFields.FHIR_BULKDATA_SOURCE)) {
                String source = obj.getString(OperationFields.FHIR_BULKDATA_SOURCE);
                builder.source(source);
            }
        }

        /**
         * converts back from input string to objects
         *
         * @param input
         * @return
         * @throws IOException
         */
        public static List<Input> parseInputsFromString(String input) throws IOException {
            List<Input> inputs = new ArrayList<>();
            Decoder decoder = Base64.getDecoder();
            byte[] bytes = decoder.decode(input);

            try (ByteArrayInputStream in = new ByteArrayInputStream(bytes)) {
                try (JsonReader jsonReader = JSON_READER_FACTORY.createReader(in, StandardCharsets.UTF_8)) {
                    JsonArray jsonArray = jsonReader.readArray();
                    /*
                     * the input is intentionally base64 to capture the JSON Array and maintain integrity as part of a
                     * name:value pair when submitted to the Batch framework.
                     */
                    ListIterator<JsonValue> iter = jsonArray.listIterator();
                    while (iter.hasNext()) {
                        JsonObject obj = iter.next().asJsonObject();
                        inputs.add(new Input(obj.getString("type"), obj.getString("url")));
                    }
                }
            }
            return inputs;
        }
    }

    @Override
    public String toString() {
        return "JobParameter [fhirResourceType=" + fhirResourceType +
                ", fhirSearchFromDate=" + fhirSearchFromDate +
                ", fhirTenant=" + fhirTenant +
                ", fhirDataStoreId=" + fhirDataStoreId +
                ", fhirPatientGroupId=" + fhirPatientGroupId +
                ", fhirTypeFilters=" + fhirTypeFilters +
                ", fhirExportFormat=" + fhirExportFormat +
                ", inputs=" + inputs +
                ", storageDetail=" + storageDetail +
                ", incomingUrl=" + incomingUrl +
                ", cosBucketPathPrefix=" + cosBucketPathPrefix + "]";
    }
}
