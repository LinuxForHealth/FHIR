/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.audit.mapper;

/**
 * The various output types that need to be mapped from the AuditLogEntry.
 */
public enum MapperType {
    CADF("cadf"),
    AUDITEVENT("auditevent");

    private String value;

    MapperType(String value) {
        this.value = value;
    }

    public static MapperType from(String value) {
        for(MapperType mt : MapperType.values()) {
            if (mt.value.equals(value)) {
                return mt;
            }
        }
        throw new IllegalArgumentException("Failed to find the mapper type");
    }
}