/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.schema.control;

import com.ibm.fhir.database.utils.api.TenantStatus;

/**
 * DTO for tenant information fetched from the admin schema
 */
public class TenantInfo {
    
    // the name of the tenant (identifier)
    private String tenantName;
    
    // the unique id of the tenant (used to identify partitions)
    private int tenantId;
    
    // the data schema where the tenant is located
    private String tenantSchema;
    
    // the provisioned status of the tenant
    private TenantStatus tenantStatus;

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String schema = tenantSchema != null ? tenantSchema : "<not-known>";
        return String.format("%9d %10s %16s %s", tenantId, tenantStatus, tenantName, schema);
    }

    /**
     * Get a header matching the above format
     * @return
     */
    public static String getHeader() {
        return String.format("%9s %10s %16s %s", "TenantId", "Status", "TenantName", "Schema");
    }
    
    /**
     * @return the tenantName
     */
    public String getTenantName() {
        return tenantName;
    }

    
    /**
     * @param tenantName the tenantName to set
     */
    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    
    /**
     * @return the tenantId
     */
    public int getTenantId() {
        return tenantId;
    }

    
    /**
     * @param tenantId the tenantId to set
     */
    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    
    /**
     * @return the tenantSchema
     */
    public String getTenantSchema() {
        return tenantSchema;
    }

    
    /**
     * @param tenantSchema the tenantSchema to set
     */
    public void setTenantSchema(String tenantSchema) {
        this.tenantSchema = tenantSchema;
    }

    /**
     * @return the tenantStatus
     */
    public TenantStatus getTenantStatus() {
        return tenantStatus;
    }

    /**
     * @param tenantStatus the tenantStatus to set
     */
    public void setTenantStatus(TenantStatus tenantStatus) {
        this.tenantStatus = tenantStatus;
    }
}
