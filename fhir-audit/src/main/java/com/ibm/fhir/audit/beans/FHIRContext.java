/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.audit.beans;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;
import jakarta.json.JsonValue;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonGeneratorFactory;

import com.ibm.fhir.exception.FHIRException;

public class FHIRContext extends Context {
    private String event_type;
    private String desc;
    private String client_cert_cn;
    private String client_cert_issuer_ou;
    private String location;

    public FHIRContext() {
        // No implementation
    }

    public FHIRContext(Context fromObj) {
        super(fromObj);
    }

    public String getEventType() {
        return event_type;
    }

    public void setEventType(String eventType) {
        this.event_type = eventType;
    }

    public String getDescription() {
        return desc;
    }

    public void setDescription(String desc) {
        this.desc = desc;
    }

    public String getClient_cert_cn() {
        return client_cert_cn;
    }

    public void setClient_cert_cn(String client_cert_cn) {
        this.client_cert_cn = client_cert_cn;
    }

    public String getClient_cert_issuer_ou() {
        return client_cert_issuer_ou;
    }

    public void setClient_cert_issuer_ou(String clientCertIssuerOu) {
        this.client_cert_issuer_ou = clientCertIssuerOu;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Generates JSON from this object.
     */
    public static class FHIRWriter {
        private static final Map<java.lang.String, Object> properties =
                Collections.singletonMap(JsonGenerator.PRETTY_PRINTING, true);
        private static final JsonGeneratorFactory PRETTY_PRINTING_GENERATOR_FACTORY =
                Json.createGeneratorFactory(properties);

        private FHIRWriter() {
            // No Operation
        }

        /**
         * @param obj
         * @return
         * @throws IOException
         */
        public static String generate(FHIRContext obj)
                throws IOException {
            String o = "{}";
            try (StringWriter writer = new StringWriter();) {
                try (JsonGenerator generator =
                        PRETTY_PRINTING_GENERATOR_FACTORY.createGenerator(writer);) {
                    generator.writeStartObject();

                    if (obj.getRequestUniqueId() != null) {
                        generator.write("request_unique_id", obj.getRequestUniqueId());
                    }

                    if (obj.getAction() != null) {
                        generator.write("action", obj.getAction());
                    }

                    if (obj.getOperationName() != null) {
                        generator.write("operation_name", obj.getOperationName());
                    }

                    if (obj.getPurpose() != null) {
                        generator.write("purpose", obj.getPurpose());
                    }

                    if (obj.getStartTime() != null) {
                        generator.write("start_time", obj.getStartTime());
                    }

                    if (obj.getEndTime() != null) {
                        generator.write("end_time", obj.getEndTime());
                    }

                    if (obj.getApiParameters() != null) {
                        generator.writeStartObject("api_parameters");
                        ApiParameters.Writer.generate(obj.getApiParameters(), generator);
                        generator.writeEnd();
                    }

                    if (obj.getQueryParameters() != null) {
                        generator.write("query", obj.getQueryParameters());
                    }

                    if (obj.getData() != null) {
                        generator.writeStartObject("data");
                        Data.Writer.generate(obj.getData(), generator);
                        generator.writeEnd();
                    }

                    if (obj.getBatch() != null) {
                        generator.writeStartObject("batch");
                        Batch.Writer.generate(obj.getBatch(), generator);
                        generator.writeEnd();
                    }

                    if (obj.getEventType() != null) {
                        generator.write("event_type", obj.getEventType());
                    }

                    if (obj.getDescription() != null) {
                        generator.write("description", obj.getDescription());
                    }

                    if (obj.getClient_cert_cn() != null) {
                        generator.write("client_cert_cn", obj.getClient_cert_cn());
                    }

                    if (obj.getClient_cert_issuer_ou() != null) {
                        generator.write("client_cert_issuer_ou", obj.getClient_cert_issuer_ou());
                    }

                    if (obj.getLocation() != null) {
                        generator.write("location", obj.getLocation());
                    }

                    if (obj.getResourceName() != null) {
                        generator.write("resource_name", obj.getResourceName());
                    }

                    generator.writeEnd();
                }
                o = writer.toString();
            }
            return o;
        }
    }

    /**
     * Parser
     */
    public static class FHIRParser {
        private static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);

        private FHIRParser() {
            // No Impl
        }

        public static FHIRContext parse(InputStream in)
                throws FHIRException {
            try (JsonReader jsonReader =
                    JSON_READER_FACTORY.createReader(in, StandardCharsets.UTF_8)) {
                JsonObject jsonObject = jsonReader.readObject();
                FHIRContext.FHIRBuilder builder =
                        FHIRContext.fhirBuilder();

                JsonValue t = jsonObject.get("request_unique_id");
                if (t != null) {
                    String requestUniqueId = jsonObject.getString("request_unique_id");
                    builder.requestUniqueId(requestUniqueId);
                }

                t = jsonObject.get("action");
                if (t != null) {
                    String action = jsonObject.getString("action");
                    builder.action(action);
                }

                t = jsonObject.get("operation_name");
                if (t != null) {
                    String operationName = jsonObject.getString("operation_name");
                    builder.operationName(operationName);
                }

                t = jsonObject.get("purpose");
                if (t != null) {
                    String purpose = jsonObject.getString("purpose");
                    builder.purpose(purpose);
                }

                t = jsonObject.get("start_time");
                if (t != null) {
                    String startTime = jsonObject.getString("start_time");
                    builder.startTime(startTime);
                }

                t = jsonObject.get("end_time");
                if (t != null) {
                    String endTime = jsonObject.getString("end_time");
                    builder.endTime(endTime);
                }

                t = jsonObject.get("api_parameters");
                if (t != null) {
                    JsonObject apiParameters = jsonObject.getJsonObject("api_parameters");
                    ApiParameters p = ApiParameters.Parser.parse(apiParameters);
                    builder.apiParameters(p);
                }

                t = jsonObject.get("query");
                if (t != null) {
                    String queryParameters = jsonObject.getString("query");
                    builder.queryParameters(queryParameters);
                }

                t = jsonObject.get("data");
                if (t != null) {
                    JsonObject data = jsonObject.getJsonObject("data");
                    Data d = Data.Parser.parse(data);
                    builder.data(d);
                }

                t = jsonObject.get("batch");
                if (t != null) {
                    JsonObject batch = jsonObject.getJsonObject("batch");
                    Batch b = Batch.Parser.parse(batch);
                    builder.batch(b);
                }

                t = jsonObject.get("location");
                if (t != null) {
                    String location = jsonObject.getString("location");
                    builder.location(location);
                }

                t = jsonObject.get("client_cert_issuer_ou");
                if (t != null) {
                    String ou = jsonObject.getString("client_cert_issuer_ou");
                    builder.clientCertIssuerOu(ou);
                }

                t = jsonObject.get("client_cert_cn");
                if (t != null) {
                    String cn = jsonObject.getString("client_cert_cn");
                    builder.clientCertCn(cn);
                }

                t = jsonObject.get("description");
                if (t != null) {
                    String description = jsonObject.getString("description");
                    builder.description(description);
                }

                t = jsonObject.get("event_type");
                if (t != null) {
                    String eventType = jsonObject.getString("event_type");
                    builder.eventType(eventType);
                }

                t = jsonObject.get("resource_name");
                if (t != null) {
                    String resourceName = jsonObject.getString("resource_name");
                    builder.resourceName(resourceName);
                }

                return builder.build();
            } catch (Exception e) {
                throw new FHIRException("Problem parsing the Context", e);
            }
        }
    }

    /**
     * Builder is a convenience pattern to assemble to Java Object
     */
    public static class FHIRBuilder {
        private FHIRContext fhirContext = new FHIRContext();

        public FHIRBuilder() {
            // No Op
        }

        public FHIRBuilder eventType(String eventType) {
            fhirContext.setEventType(eventType);
            return this;
        }

        public FHIRBuilder description(String description) {
            fhirContext.setDescription(description);
            return this;
        }

        public FHIRBuilder clientCertCn(String clientCertCn) {
            fhirContext.setClient_cert_cn(clientCertCn);
            return this;
        }

        public FHIRBuilder clientCertIssuerOu(String clientCertIssuerOu) {
            fhirContext.setClient_cert_issuer_ou(clientCertIssuerOu);
            return this;
        }

        public FHIRBuilder action(String action) {
            fhirContext.setAction(action);
            return this;
        }

        public FHIRBuilder location(String location) {
            fhirContext.setLocation(location);
            return this;
        }

        public FHIRBuilder apiParameters(ApiParameters apiParameters) {
            fhirContext.setApiParameters(apiParameters);
            return this;
        }

        public FHIRBuilder batch(Batch batch) {
            fhirContext.setBatch(batch);
            return this;
        }

        public FHIRBuilder data(Data data) {
            fhirContext.setData(data);
            return this;
        }

        public FHIRBuilder endTime(String endTime) {
            fhirContext.setEndTime(endTime);
            return this;
        }

        public FHIRBuilder startTime(String startTime) {
            fhirContext.setStartTime(startTime);
            return this;
        }

        public FHIRBuilder operationName(String operationName) {
            fhirContext.setOperationName(operationName);
            return this;
        }

        public FHIRBuilder purpose(String purpose) {
            fhirContext.setPurpose(purpose);
            return this;
        }

        public FHIRBuilder queryParameters(String queryParameters) {
            fhirContext.setQueryParameters(queryParameters);
            return this;
        }

        public FHIRBuilder requestUniqueId(String requestUniqueId) {
            fhirContext.setRequestUniqueId(requestUniqueId);
            return this;
        }

        public FHIRBuilder resourceName(String resourceName) {
            fhirContext.setResourceName(resourceName);
            return this;
        }

        public FHIRContext build() {
            return fhirContext;
        }
    }

    public static FHIRBuilder fhirBuilder() {
        return new FHIRContext.FHIRBuilder();
    }
}