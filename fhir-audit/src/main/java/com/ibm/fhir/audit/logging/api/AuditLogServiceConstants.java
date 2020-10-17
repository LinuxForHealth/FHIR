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

    private AuditLogServiceConstants() {
        // No Operation
    }

    // the default topic
    public static final String TOPIC = "fhir-audit";

    public static final String FIELD_TOPIC_NAME = "topicName";

    public static final String FIELD_LOAD = "load";

    // The health check operation
    public static final String HEALTHCHECKOP = "healthcheck";

    // Ignore Operations
    public static Set<String> IGNORED_OPERATIONS = new HashSet<>(Arrays.asList(HEALTHCHECKOP));
}
