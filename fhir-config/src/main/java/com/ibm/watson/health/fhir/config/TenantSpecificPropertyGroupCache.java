/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.ibm.watson.health.fhir.core.TenantSpecificFileBasedCache;

/**
 * This class implements a tenant-specific cache that holds PropertyGroup objects (i.e. FHIR Server configuration
 * information). Each cache entry holds the in-memory representation of the fhir-server-config.json file found for a
 * particular tenant.
 * 
 * @author padams
 */
public class TenantSpecificPropertyGroupCache extends TenantSpecificFileBasedCache<PropertyGroup> {

    public TenantSpecificPropertyGroupCache() {
        super("PropertyGroup");
    }

    /*
     * (non-Javadoc)
     * @see com.ibm.watson.health.fhir.core.TenantSpecificFileBasedCache#getCacheEntryFilename(java.lang.String)
     */
    @Override
    public String getCacheEntryFilename(String tenantId) {
        return FHIRConfiguration.getConfigHome() + FHIRConfiguration.CONFIG_LOCATION + File.separator + tenantId + File.separator
                + FHIRConfiguration.CONFIG_FILE_BASENAME;
    }

    /*
     * (non-Javadoc)
     * @see com.ibm.watson.health.fhir.core.TenantSpecificFileBasedCache#createCachedObject(java.lang.String)
     */
    @Override
    public PropertyGroup createCachedObject(File f) throws Exception {
        try (InputStream is = new FileInputStream(f)) {
            return ConfigurationService.loadConfiguration(is);
        }
    }
}
