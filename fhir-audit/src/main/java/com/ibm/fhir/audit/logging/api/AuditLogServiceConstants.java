/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.audit.logging.api;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Audit Log Service Constants
 */
public final class AuditLogServiceConstants {

    public static final String PROPERTY_AUDIT_KAFKA_TOPIC = "auditTopic";
    public static final String PROPERTY_AUDIT_GEO_CITY = "geoCity";
    public static final String PROPERTY_AUDIT_GEO_STATE = "geoState";
    public static final String PROPERTY_AUDIT_GEO_COUNTRY = "geoCounty";
    public static final String PROPERTY_AUDIT_KAFKA_BOOTSTRAPSERVERS = "kafkaServers";
    public static final String PROPERTY_AUDIT_KAFKA_APIKEY = "kafkaApiKey";
    public static final String PROPERTY_AUDIT_MAPPER = "mapper";

    public static final String DEFAULT_MAPPER = "cadf";

    public static final String KAFKA_USERNAME = "token";

    public static final String DEFAULT_AUDIT_KAFKA_TOPIC = "FHIR_AUDIT";
    public static final String DEFAULT_AUDIT_GEO_CITY = "Dallas";
    public static final String DEFAULT_AUDIT_GEO_STATE = "TX";
    public static final String DEFAULT_AUDIT_GEO_COUNTRY = "US";

    public static final String FIELD_LOAD = "load";

    // The health check operation
    public static final String HEALTHCHECKOP = "healthcheck";

    public static final Set<String> IGNORED_AUDIT_EVENT_TYPE = createIgnoredLogEventType();

    /*
     * creates an internal map of ignored Event Types.
     */
    private static Set<String> createIgnoredLogEventType() {
        Set<String> set = new HashSet<>();
        set.add(AuditLogEventType.FHIR_CONFIGDATA.value());
        set.add(AuditLogEventType.FHIR_METADATA.value());
        return set;
    }

    // Ignore Operations
    public static Set<String> IGNORED_OPERATIONS = new HashSet<>(Arrays.asList(HEALTHCHECKOP));

    private AuditLogServiceConstants() {
        // No Operation
    }
}