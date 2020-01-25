/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.bulkdata.model;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ResponseMetadata to manipulate the response back to the client.
 * This response object is intent for the polling location.
 */
public class PollingLocationResponse {
    private String transactionTime;
    private String request;
    private Boolean requiresAccessToken;
    private List<Output> output;

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

    public static class Output {
        private String type;
        private String url;
        private String count;

        public Output(String type, String url, String count) {
            super();
            this.type  = type;
            this.url   = url;
            this.count = count;
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

        @Override
        public String toString() {
            return "{ \"type\" : \"" + type + "\", \"url\": \"" + url + "\", \"count\": " + count + "}";
        }

    }

    public String toJsonString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append("\n");

        if (transactionTime != null) {
            builder.append("\"transactionTime\": \"");
            builder.append(getTransactionTime());
            builder.append("\"");
            builder.append(",");
        }

        if (request != null) {
            builder.append("\"request\": \"");
            builder.append(getRequest());
            builder.append("\"");
            builder.append(",");
        }

        if (requiresAccessToken != null) {
            builder.append("\"requiresAccessToken\": ");
            builder.append(Boolean.toString(getRequiresAccessToken()));
            builder.append("");
            builder.append(",");
        }

        builder.append("\"output\" : [");
        if (getOutput() != null) {
            builder.append(getOutput().stream().map(s -> s.toString()).collect(Collectors.joining(",")));
        }
        builder.append("]");

        builder.append("\n");
        builder.append("}");
        return builder.toString();
    }
}