/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.audit.logging.api;

import com.ibm.watsonhealth.fhir.audit.logging.beans.AuditLogEntry;

/**
 * Defines the internal FHIR Server APIs for audit logging
 * @author markd
 *
 */
public interface AuditLogService {
	
	/**
	 * Builds and returns an AuditLogEntry with the minimum required fields populated.
	 * @param eventType - A valid type of audit log event
	 * @return AuditLogEntry with the minimum required fields populated.
	 */
	AuditLogEntry initLogEntry(AuditLogEventType eventType);
	
	/**
	 * Persists the passed audit log entry in a location determined by the log service.
	 * @param logEntry - The audit log entry to be saved.
	 */
	void logEntry(AuditLogEntry logEntry);
	
	/**
	 * Converts the passed AuditLogEntry object to an encrypted JSON string.
	 * @param logEntry - The audit log entry to be recorded
	 * @return String - An encrypted json string representation of the AuditLogEntry.
	 */
	String convertLogEntry(AuditLogEntry logEntry);
}
