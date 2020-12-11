/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.audit.configuration;

/**
 * The ConfigurationType indicates where the configuration is going to be loaded.
 */
public enum ConfigurationType {

    CONFIG("config"),
    ENVIRONMENT("environment");

    private String value;

    ConfigurationType(String value) {
        this.value = value;
    }

    public static ConfigurationType from(String value) {
        for(ConfigurationType mt : ConfigurationType.values()) {
            if (mt.value.equals(value)) {
                return mt;
            }
        }
        throw new IllegalArgumentException("Failed to find the configuration type");
    }

    public String value() {
        return value;
    }
}