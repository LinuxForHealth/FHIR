/*
 * (C) Copyright IBM Corp. 2019, 2020
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

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.json.JsonValue;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.operation.bulkdata.BulkDataConstants;

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

    private String incomingUrl;

    private String cosBucketName;
    private String cosOperationBucketNameOo;
    private String cosLocation;
    private String cosEndpointInternal;
    private String cosEndpointExternal;
    private String cosCredentialIbm;
    private String cosApiKey;
    private String cosSrvInstId;
    private String cosBucketPathPrefix;

    public String getCosOperationBucketNameOo() {
        return cosOperationBucketNameOo;
    }

    public void setCosOperationBucketNameOo(String cosOperationBucketNameOo) {
        this.cosOperationBucketNameOo = cosOperationBucketNameOo;
    }

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

    public String getCosBucketName() {
        return cosBucketName;
    }

    public void setCosBucketName(String cosBucketName) {
        this.cosBucketName = cosBucketName;
    }

    public String getCosLocation() {
        return cosLocation;
    }

    public void setCosLocation(String cosLocation) {
        this.cosLocation = cosLocation;
    }

    public String getCosEndpointInternal() {
        return cosEndpointInternal;
    }

    public String getCosEndpointExternal() {
        return cosEndpointExternal;
    }

    public void setCosEndpointInternal(String cosEndpointUrl) {
        this.cosEndpointInternal = cosEndpointUrl;
    }

    public void setCosEndpointExternal(String cosEndpointUrl) {
        this.cosEndpointExternal = cosEndpointUrl;
    }

    public String getCosCredentialIbm() {
        return cosCredentialIbm;
    }

    public void setCosCredentialIbm(String cosCredentialIbm) {
        this.cosCredentialIbm = cosCredentialIbm;
    }

    public String getCosApiKey() {
        return cosApiKey;
    }

    public void setCosApiKey(String cosApiKey) {
        this.cosApiKey = cosApiKey;
    }

    public String getCosSrvInstId() {
        return cosSrvInstId;
    }

    public void setCosSrvInstId(String cosSrvInstId) {
        this.cosSrvInstId = cosSrvInstId;
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

            if (parameter.getCosOperationBucketNameOo() != null) {
                generator.write("cos.operationoutcomes.bucket.name", parameter.getCosOperationBucketNameOo());
            }

            if (parameter.getCosEndpointInternal() != null) {
                generator.write("cos.endpoint.internal", parameter.getCosEndpointInternal());
            }
            if (parameter.getCosEndpointExternal() != null) {
                generator.write("cos.endpoint.external", parameter.getCosEndpointExternal());
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

            if (parameter.getFhirPatientGroupId() != null) {
                generator.write("fhir.search.patientgroupid", parameter.getFhirPatientGroupId());
            }

            if (withSensitive) {
                if (parameter.getCosBucketPathPrefix() != null) {
                    generator.write("cos.bucket.pathprefix", parameter.getCosBucketPathPrefix());
                }
            }

            if (parameter.getFhirDataStoreId() != null) {
                generator.write("fhir.datastoreid", parameter.getFhirDataStoreId());
            }

            if (parameter.getFhirTypeFilters() != null) {
                generator.write("fhir.typeFilters", parameter.getFhirTypeFilters());
            }

            if (parameter.getFhirExportFormat() != null) {
                generator.write("fhir.exportFormat", parameter.getFhirExportFormat());
            }

            if (parameter.getInputs() != null) {
                generator.write("fhir.dataSourcesInfo", writeToBase64(parameter.getInputs()));
            }

            String type = BulkDataConstants.DataSourceStorageType.HTTPS.value();
            if (parameter.getStorageDetails() != null) {
                type = parameter.getStorageDetails().getType();
            }

            if (parameter.getInputs() != null) {
                generator.write("fhir.dataSourcesInfo", writeToBase64(parameter.getInputs()));
            }
            generator.write("import.fhir.storagetype", type);

            if (parameter.getIncomingUrl() != null) {
                generator.write("incomingUrl", parameter.getIncomingUrl());
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

        public Builder cosBucketName(String cosBucketName);

        public Builder cosBucketNameOperationOutcome(String cosBucketNameOperationOutcome);

        public Builder cosLocation(String cosLocation);

        public Builder cosEndpointInternal(String cosEndpointUrl);

        public Builder cosEndpointExternal(String cosEndpointUrl);

        public Builder cosCredentialIbm(String cosCredentialIbm);

        public Builder cosApiKey(String cosApiKey);

        public Builder cosSrvInstId(String cosSrvInstId);

        public Builder fhirTenant(String fhirTenant);

        public Builder fhirDataStoreId(String fhirDataStoreId);

        public Builder fhirPatientGroupId(String fhirPatientGroupId);

        public Builder cosBucketPathPrefix(String cosBucketPathPrefix);

        public Builder fhirTypeFilters(String fhirTypeFilters);

        public Builder fhirDataSourcesInfo(List<Input> inputs);

        public Builder fhirStorageType(StorageDetail storageDetail);

        public Builder fhirExportFormat(String mediaType);

        public Builder incomingUrl(String incomingUrl);
    }

    public static class Parser {
        private static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);

        private Parser() {
            // No Op
        }

        public static void parse(Builder builder, JsonObject obj) throws FHIROperationException, IOException {
            if (obj.containsKey("fhir.resourcetype")) {
                String fhirResourceType = obj.getString("fhir.resourcetype");
                builder.fhirResourceType(fhirResourceType);
            }

            if (obj.containsKey("fhir.search.fromdate")) {
                String fhirSearchFromdate = obj.getString("fhir.search.fromdate");
                builder.fhirSearchFromDate(fhirSearchFromdate);
            }

            if (obj.containsKey("cos.bucket.name")) {
                String cosBucketName = obj.getString("cos.bucket.name");
                builder.cosBucketName(cosBucketName);
            }

            if (obj.containsKey("cos.operationoutcomes.bucket.name")) {
                String cosBucketNameOperationOutcome = obj.getString("cos.operationoutcomes.bucket.name");
                builder.cosBucketNameOperationOutcome(cosBucketNameOperationOutcome);
            }

            if (obj.containsKey("cos.location")) {
                String cosLocation = obj.getString("cos.location");
                builder.cosLocation(cosLocation);
            }

            if (obj.containsKey("cos.endpoint.internal")) {
                String cosEndpointUrl = obj.getString("cos.endpoint.internal");
                builder.cosEndpointInternal(cosEndpointUrl);
            }

            if (obj.containsKey("cos.endpoint.external")) {
                String cosEndpointUrl = obj.getString("cos.endpoint.external");
                builder.cosEndpointExternal(cosEndpointUrl);
            }

            if (obj.containsKey("cos.credential.ibm")) {
                String cosCredentialIbm = obj.getString("cos.credential.ibm");
                builder.cosCredentialIbm(cosCredentialIbm);
            }

            if (obj.containsKey("cos.api.key")) {
                String cosApiKey = obj.getString("cos.api.key");
                builder.cosApiKey(cosApiKey);
            }

            if (obj.containsKey("cos.srvinst.id")) {
                String cosSrvinstId = obj.getString("cos.srvinst.id");
                builder.cosSrvInstId(cosSrvinstId);
            }

            if (obj.containsKey("fhir.tenant")) {
                String fhirTenant = obj.getString("fhir.tenant");
                builder.fhirTenant(fhirTenant);
            }

            if (obj.containsKey("fhir.datastoreid")) {
                String fhirDataStoreId = obj.getString("fhir.datastoreid");
                builder.fhirDataStoreId(fhirDataStoreId);
            }

            if (obj.containsKey("cos.bucket.pathprefix")) {
                String cosBucketPathPrefix = obj.getString("cos.bucket.pathprefix");
                builder.cosBucketPathPrefix(cosBucketPathPrefix);
            }

            if (obj.containsKey("fhir.typeFilters")) {
                String fhirTypeFilters = obj.getString("fhir.typeFilters");
                builder.fhirTypeFilters(fhirTypeFilters);
            }

            if (obj.containsKey("fhir.search.patientgroupid")) {
                String fhirPatientGroupId = obj.getString("fhir.search.patientgroupid");
                builder.fhirPatientGroupId(fhirPatientGroupId);
            }

            if (obj.containsKey("fhir.dataSourcesInfo")) {
                String dataSourcesInfo = obj.getString("fhir.dataSourcesInfo");
                // Base64 at this point, and we just dump it into an intermediate value until it's needed.
                builder.fhirDataSourcesInfo(parseInputsFromString(dataSourcesInfo));
            }

            if (obj.containsKey("import.fhir.storagetype")) {
                String storageType = obj.getString("import.fhir.storagetype");
                builder.fhirStorageType(new StorageDetail(storageType, Collections.emptyList()));
            }

            if (obj.containsKey("incomingUrl")) {
                String incomingUrl = obj.getString("incomingUrl");
                builder.incomingUrl(incomingUrl);
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
                ", cosBucketName=" + cosBucketName +
                ", cosOperationBucketNameOo=" + cosOperationBucketNameOo +
                ", cosLocation=" + cosLocation +
                ", cosEndpointInternal=" + cosEndpointInternal +
                ", cosEndpointExternal=" + cosEndpointExternal +
                ", cosCredentialIbm=" + cosCredentialIbm +
                ", cosApiKey=" + cosApiKey +
                ", cosSrvInstId=" + cosSrvInstId +
                ", cosBucketPathPrefix=" + cosBucketPathPrefix + "]";
    }
}
