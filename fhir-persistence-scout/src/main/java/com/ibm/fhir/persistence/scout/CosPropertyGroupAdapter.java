/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.scout;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.config.PropertyGroup;

/**
 * Provides a facade on top of the fhir-server-config PropertyGroup structure
 * to simplify access to configuration elements we need for connecting to
 * COS
 */
public class CosPropertyGroupAdapter {
    private static final Logger logger = Logger.getLogger(CosPropertyGroupAdapter.class.getName());

    // Property key constants
    public static final String PROP_TENANT_BUCKET = "tenantBucket"; 

    // The property group we are wrapping
    private final PropertyGroup propertyGroup;
    
    public CosPropertyGroupAdapter(PropertyGroup pg) {
        this.propertyGroup = pg;
    }

    /**
     * Return the bucket to use for this tenant
     * @return
     */
    public String getTenantBucketName() {
        try {
            return propertyGroup.getStringProperty(PROP_TENANT_BUCKET);
        } catch (Exception x) {
            logger.log(Level.SEVERE, PROP_TENANT_BUCKET, x);
            throw new IllegalArgumentException("property not configured: " + PROP_TENANT_BUCKET);
        }
    }
}
