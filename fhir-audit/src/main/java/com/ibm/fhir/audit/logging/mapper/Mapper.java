/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.audit.logging.mapper;

import com.ibm.fhir.audit.logging.beans.AuditLogEntry;

/**
 * Each implementing class is expected to be stateful.
 */
public interface Mapper {

    Mapper map(AuditLogEntry entry);

    String serialize();
}
