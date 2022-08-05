/*
 * (C) Copyright IBM Corp. 2016, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.audit;

import org.linuxforhealth.fhir.audit.beans.AuditLogEntry;
import org.linuxforhealth.fhir.config.PropertyGroup;

/**
 * Defines the internal FHIR Server APIs for audit logging
 */
public interface AuditLogService {

    /**
     * @return true if the audit log service is enabled; false if not enabled.
     */
    boolean isEnabled();

    /**
     * Performs any required audit log service initialization using the passed
     * Properties file.
     *
     * @param auditLogProperties
     *            - Contains audit log related properties which are
     *            configured in fhir-server-config.json.
     * @throws Exception
     *             - Any non-recoverable exception thrown during audit log
     *             service initialization.
     *
     */
    void initialize(PropertyGroup auditLogProperties) throws Exception;

    /**
     * Performs any required audit log service stops
     *
     * @param auditLogProperties
     *            - Contains audit log related properties which are
     *            configured in fhir-server-config.json.
     * @throws Exception
     *             - Any non-recoverable exception thrown during audit log
     *             service initialization.
     */
    void stop(PropertyGroup auditLogProperties) throws Exception;

    /**
     * Persists the passed audit log entry in a location determined by the log
     * service.
     *
     * @param logEntry
     *            - The audit log entry to be saved.
     * @throws Exception
     */
    void logEntry(AuditLogEntry logEntry) throws Exception;

    /**
     * Checks if the current logEntry is loggable.
     *
     * @param logEntry
     * @return
     */
    default boolean isLoggableOperation(AuditLogEntry logEntry) {
        return logEntry != null && logEntry.getContext() != null
                && !(logEntry.getContext().getOperationName() != null
                        && AuditLogServiceConstants.IGNORED_OPERATIONS
                            .contains(logEntry.getContext().getOperationName()));
    }
}