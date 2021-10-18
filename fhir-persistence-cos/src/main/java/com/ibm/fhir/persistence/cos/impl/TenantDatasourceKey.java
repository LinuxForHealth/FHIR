/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.cos.impl;

import java.util.Objects;

/**
 * Key used to represent a tenant/datasource pair
 */
public class TenantDatasourceKey {

    private final String tenantId;
    
    private final String datasourceId;
    
    /**
     * Public constructor
     * @param tenantId
     * @param datasourceId
     */
    public TenantDatasourceKey(String tenantId, String datasourceId) {
        this.tenantId = tenantId;
        this.datasourceId = datasourceId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tenantId, datasourceId);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TenantDatasourceKey) {
            TenantDatasourceKey that = (TenantDatasourceKey)obj;
            return this.tenantId.equals(that.tenantId)
                    && this.datasourceId.equals(that.datasourceId);
        } else {
            throw new IllegalArgumentException("obj is not a TenantDatasourceKey");
        }
    }

    
    /**
     * @return the tenantId
     */
    public String getTenantId() {
        return tenantId;
    }

    
    /**
     * @return the datasourceId
     */
    public String getDatasourceId() {
        return datasourceId;
    }
}
