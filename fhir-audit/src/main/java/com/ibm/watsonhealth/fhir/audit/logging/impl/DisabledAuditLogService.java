/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.audit.logging.impl;

import com.ibm.watsonhealth.fhir.audit.logging.beans.AuditLogEntry;

/**
 * This log service class represents a disabled logging service. That is, one that does not write or persist any log entries.
 * @author markd
 *
 */
public class DisabledAuditLogService extends AbstractAuditLogService {

	public DisabledAuditLogService() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.ibm.watsonhealth.fhir.core.audit.logging.api.AuditLogService#logEntry(com.ibm.watsonhealth.fhir.core.audit.logging.beans.AuditLogEntry)
	 */
	@Override
	public void logEntry(AuditLogEntry logEntry) {
	
	}

	/* (non-Javadoc)
	 * @see com.ibm.watsonhealth.fhir.core.audit.logging.api.AuditLogService#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return false;
	}

}
