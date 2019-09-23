/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.audit.kafka;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventStreamsCredentials {

    private String apiKey, user, password;
    private String[] kafkaBrokersSasl;

    @JsonProperty("api_key")
    public String getApiKey() {
        return apiKey;
    }

    @JsonProperty("api_key")
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @JsonProperty
    public String getUser() {
        return user;
    }

    @JsonProperty
    public void setUser(String user) {
        this.user = user;
    }

    @JsonProperty
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("kafka_brokers_sasl")
    public String[] getKafkaBrokersSasl() {
        return kafkaBrokersSasl;
    }

    @JsonProperty("kafka_brokers_sasl")
    public void setKafkaBrokersSasl(String[] kafkaBrokersSasl) {
        this.kafkaBrokersSasl = kafkaBrokersSasl;
    }
}
