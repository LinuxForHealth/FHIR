/*
 * (C) Copyright IBM Corp. 2016, 2021
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

/**
 * This class defines the Context section of the FHIR server AuditLogEntry.
 */
public class Context {
    private String requestUniqueId;
    private Data data;
    private String action;
    private String operationName;
    private String queryParameters;
    private String purpose;
    private String resourceName;
    private String startTime;
    private String endTime;
    private ApiParameters apiParameters;
    private Batch batch;

    public Context() {
        super();
    }

    public Context(Context fromObj) {
        super();
        this.action          = fromObj.action;
        this.apiParameters   = fromObj.apiParameters;
        this.batch           = fromObj.batch;
        this.data            = fromObj.data;
        this.endTime         = fromObj.endTime;
        this.operationName   = fromObj.operationName;
        this.purpose         = fromObj.purpose;
        this.queryParameters = fromObj.queryParameters;
        this.requestUniqueId = fromObj.requestUniqueId;
        this.resourceName    = fromObj.resourceName;
        this.startTime       = fromObj.startTime;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getQueryParameters() {
        return queryParameters;
    }

    public void setQueryParameters(String queryParms) {
        this.queryParameters = queryParms;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public ApiParameters getApiParameters() {
        return apiParameters;
    }

    public void setApiParameters(ApiParameters apiParameters) {
        this.apiParameters = apiParameters;
    }

    public Batch getBatch() {
        return batch;
    }

    public void setBatch(Batch batch) {
        this.batch = batch;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getRequestUniqueId() {
        return requestUniqueId;
    }

    public void setRequestUniqueId(String requestUniqueId) {
        this.requestUniqueId = requestUniqueId;
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
         * @param obj
         * @return
         * @throws IOException
         */
        public static String generate(Context obj)
                throws IOException {
            String o = "{}";
            try (StringWriter writer = new StringWriter();) {
                try (JsonGenerator generator =
                        PRETTY_PRINTING_GENERATOR_FACTORY.createGenerator(writer);) {
                    generator.writeStartObject();

                    //@JsonPropertyOrder({ "request_unique_id", "action", "operation_name", "purpose",
                    // "resource_name", "start_time",
                    //"end_time", "api_parameters", "query", "data", "batch" })
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
    public static class Parser {
        private static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);

        private Parser() {
            // No Impl
        }

        public static Context parse(InputStream in)
                throws FHIRException {
            try (JsonReader jsonReader =
                    JSON_READER_FACTORY.createReader(in, StandardCharsets.UTF_8)) {
                JsonObject jsonObject = jsonReader.readObject();
                Context.Builder builder =
                        Context.builder();

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
    public static class Builder {
        private Context context = new Context();

        protected Builder() {
            // No Operation
        }

        public Builder action(String action) {
            context.setAction(action);
            return this;
        }

        public Builder apiParameters(ApiParameters apiParameters) {
            context.setApiParameters(apiParameters);
            return this;
        }

        public Builder batch(Batch batch) {
            context.setBatch(batch);
            return this;
        }

        public Builder data(Data data) {
            context.setData(data);
            return this;
        }

        public Builder endTime(String endTime) {
            context.setEndTime(endTime);
            return this;
        }

        public Builder startTime(String startTime) {
            context.setStartTime(startTime);
            return this;
        }

        public Builder operationName(String operationName) {
            context.setOperationName(operationName);
            return this;
        }

        public Builder purpose(String purpose) {
            context.setPurpose(purpose);
            return this;
        }

        public Builder queryParameters(String queryParameters) {
            context.setQueryParameters(queryParameters);
            return this;
        }

        public Builder requestUniqueId(String requestUniqueId) {
            context.setRequestUniqueId(requestUniqueId);
            return this;
        }

        public Builder resourceName(String resourceName) {
            context.setResourceName(resourceName);
            return this;
        }

        public Context build() {
            return context;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * @return the resourceName
     */
    public String getResourceName() {
        return resourceName;
    }

    /**
     * @param resourceName the resourceName to set
     */
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
}