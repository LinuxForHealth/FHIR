/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.audit.logging.beans;

/**
 * This class defines the Configuration Data section of the FHIR server AuditLogEntry.
 * @author markd
 *
 */
public class ConfigData {
	
	private String serverStartupParms;

	public String getServerStartupParms() {
		return serverStartupParms;
	}

	public void setServerStartupParms(String serverStartupParms) {
		this.serverStartupParms = serverStartupParms;
	}
	
	public ConfigData withServerStartupParms(String serverStartupParms) {
		this.serverStartupParms = serverStartupParms;
		return this;
	}

}
