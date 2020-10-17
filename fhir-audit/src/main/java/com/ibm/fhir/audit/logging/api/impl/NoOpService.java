/*
 * (C) Copyright IBM Corp. 2016,2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.audit.logging.api.impl;

import com.ibm.fhir.audit.logging.api.AuditLogService;
import com.ibm.fhir.audit.logging.beans.AuditLogEntry;
import com.ibm.fhir.config.PropertyGroup;

/**
 * This class serves as a no-op audit service logger.
 * Audit logging is disabled.
 */
public class NoOpService implements AuditLogService {

    public NoOpService() {
        super();
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public void initialize(PropertyGroup auditLogProperties) throws Exception {
        // No Operation
    }

    @Override
    public void stop(PropertyGroup auditLogProperties) throws Exception {
        // No Operation
    }

    @Override
    public void logEntry(AuditLogEntry logEntry) {
        // No Operation
    }
}