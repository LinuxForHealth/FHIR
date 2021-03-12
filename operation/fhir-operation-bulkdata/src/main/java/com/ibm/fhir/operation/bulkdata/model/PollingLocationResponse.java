/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.model;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;

/**
 * ResponseMetadata to manipulate the response back to the client.
 * This response object is intent for the polling location.
 */
public class PollingLocationResponse {
    private String transactionTime;
    private String request;
    private Boolean requiresAccessToken;
    private List<Output> output;
    private List<Output> error;

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public Boolean getRequiresAccessToken() {
        return requiresAccessToken;
    }

    public void setRequiresAccessToken(Boolean requiresAccessToken) {
        this.requiresAccessToken = requiresAccessToken;
    }

    public List<Output> getOutput() {
        return output;
    }

    public void setOutput(List<Output> output) {
        this.output = output;
    }

    public List<Output> getError() {
        return error;
    }

    public void setError(List<Output> error) {
        this.error = error;
    }

    public static class Output {
        private String type;
        private String url;
        private String count;
        private String inputUrl;

        // constructor is used for $export
        public Output(String type, String url, String count) {
            super();
            this.type  = type;
            this.url   = url;
            this.count = count;
        }

        // constructor is used for $import
        public Output(String type, String url, String count, String inputUrl) {
            super();
            this.type     = type;
            this.url      = url;
            this.count    = count;
            this.inputUrl = inputUrl;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getInputUrl() {
            return inputUrl;
        }

        public void setInputUrl(String inputUrl) {
            this.inputUrl = inputUrl;
        }

        @Override
        public String toString() {
            return "Output [type=" + type + ", url=" + url + ", count=" + count + ", inputUrl=" + inputUrl + "]";
        }

        /*
         * This is an internal only class not intended to be used out of bulkdata.
         * The class serializes the Output into a JSON object.
         */
        public static class Writer {
            private Writer() {
                // No Operation
            }

            public static void generate(JsonGenerator generatorOutput, Output output) throws IOException {
                generatorOutput.writeStartObject();
                if (output.getType() != null) {
                    generatorOutput.write("type", output.getType());
                }

                if (output.getUrl() != null) {
                    generatorOutput.write("url", output.getUrl());
                }

                if (output.getCount() != null) {
                    generatorOutput.write("count", Long.parseLong(output.getCount()));

                }

                if (output.getInputUrl() != null) {
                    generatorOutput.write("inputUrl", output.getInputUrl());

                }

                generatorOutput.writeEnd();
            }
        }
    }

    /*
     * This is an internal only class not intended to be used out of bulkdata.
     * The class serializes the PollingLocationResponse into a JSON object.
     */
    public static class Writer {
        private Writer() {
            // No Operation
        }

        public static String generate(PollingLocationResponse response) throws IOException {
            Boolean pretty =
                    FHIRConfigHelper.getBooleanProperty(FHIRConfiguration.PROPERTY_DEFAULT_PRETTY_PRINT, false);
            final Map<java.lang.String, Object> properties =
                    Collections.singletonMap(JsonGenerator.PRETTY_PRINTING, pretty);
            final JsonGeneratorFactory factory = Json.createGeneratorFactory(properties);

            String o = "{}";
            try (StringWriter writer = new StringWriter();) {
                try (JsonGenerator generator = factory.createGenerator(writer);) {

                    generator.writeStartObject();
                    if (response.getTransactionTime() != null) {
                        generator.write("transactionTime", response.getTransactionTime());
                    }

                    if (response.getRequest() != null) {
                        generator.write("request", response.getRequest());
                    }

                    if (response.getRequiresAccessToken() != null) {
                        generator.write("requiresAccessToken", response.getRequiresAccessToken());
                    }

                    if (response.getOutput() != null) {
                        // outputs the output array.
                        generator.writeStartArray("output");
                        for (Output output : response.getOutput()) {
                            Output.Writer.generate(generator, output);
                        }
                        generator.writeEnd();
                    }

                    if (response.getError() != null) {
                        // outputs the output array.
                        generator.writeStartArray("error");
                        for (Output output : response.getError()) {
                            Output.Writer.generate(generator, output);
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