/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.config;

/**
 * Interaction constants to the allowed values of the
 * fhirServer/resources/[resourceType]/interactions config property
 */
public enum Interaction {
    CREATE("create"),
    DELETE("delete"),
    HISTORY("history"),
    PATCH("patch"),
    READ("read"),
    SEARCH("search"),
    UPDATE("update"),
    VREAD("vread");

    private final String value;

    Interaction(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static Interaction from(String value) {
        for (Interaction interaction : Interaction.values()) {
            if (interaction.value.equals(value)) {
                return interaction;
            }
        }
        throw new IllegalArgumentException(value);
    }
}