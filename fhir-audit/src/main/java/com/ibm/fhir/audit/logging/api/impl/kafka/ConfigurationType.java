/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.audit.logging.api.impl.kafka;


/**
 * Where to load the configuration from
 */
public enum ConfigurationType {

    CONFIG("config"),
    ENVIRONMENT("environment");

    private String value;

    ConfigurationType(String value) {
        this.value = value;
    }

    public ConfigurationType from(String value) {
        for(ConfigurationType mt : ConfigurationType.values()) {
            if (mt.value.equals(value)) {
                return mt;
            }
        }
        throw new IllegalArgumentException("Failed to find the mapper type");
    }

    public String value() {
        return value;
    }
}