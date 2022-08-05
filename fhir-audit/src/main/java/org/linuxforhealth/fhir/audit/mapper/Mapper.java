/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.audit.mapper;

import org.linuxforhealth.fhir.audit.beans.AuditLogEntry;
import org.linuxforhealth.fhir.config.PropertyGroup;

/**
 * Each implementing class is expected to be stateful.
 */
public interface Mapper {

    /**
     * initializes and loads the default values from the tenant configuration.
     * @param auditLogProperties
     * @return
     * @throws Exception
     */
    Mapper init(PropertyGroup auditLogProperties) throws Exception;

    /**
     * map the audit log entry to the intended format.
     * @param entry
     * @return
     * @throws Exception
     */
    Mapper map(AuditLogEntry entry) throws Exception;

    /**
     * serializes to the specific format in a string format.
     * @return
     * @throws Exception
     */
    String serialize() throws Exception;
}