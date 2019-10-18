/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.audit.logging.beans;

import com.google.gson.annotations.SerializedName;

/**
 * This class defines the Configuration Data section of the FHIR server AuditLogEntry.
 * @author markd
 *
 */
public class ConfigData {
    
    @SerializedName("server_startup_parameters")
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
