/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

import com.ibm.fhir.database.utils.api.TenantStatus;

/**
 * DTO representing a tenant
 */
public class Tenant {
    
    // The database id for the tenant
    private int tenantId = -1;

    // The tenant's name
    private String tenantName;
    
    // The allocation status of the tenant
    private TenantStatus tenantStatus;

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
