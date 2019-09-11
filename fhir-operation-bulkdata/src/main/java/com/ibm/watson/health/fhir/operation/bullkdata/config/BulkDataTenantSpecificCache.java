/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.operation.bullkdata.config;

import java.io.File;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.watson.health.fhir.config.FHIRConfiguration;
import com.ibm.watson.health.fhir.core.TenantSpecificFileBasedCache;
import com.ibm.watson.health.fhir.exception.FHIROperationException;

/**
 * This class implements a cache of BulkData configuration organized by tenantId. Each object stored in the cache will be a
 * two-level map of SearchParameters organized first by resource type, then by search parameter name.
 *
 * @author pbastide
 */
public class BulkDataTenantSpecificCache extends TenantSpecificFileBasedCache<Map<String, String>> {

    private static final String CLASSNAME = BulkDataTenantSpecificCache.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

    private static final String SP_FILE_BASENAME_JSON = "bulkdata.json";

    private static final String CACHE_NAME = "BulkData";

    private static final String OPERATION_EXCEPTION = "An error occurred while loading one of the tenant files: %s";

    private static final String LOG_FILE_LOAD = "The file loaded is [%s]";

    public BulkDataTenantSpecificCache() {
        super(CACHE_NAME);
    }

    /*
     * (non-Javadoc)
     * @see com.ibm.watson.health.fhir.core.TenantSpecificFileBasedCache#getCacheEntryFilename(java.lang.String)
     */
    @Override
    public String getCacheEntryFilename(String tenantId) {
        return FHIRConfiguration.getConfigHome() + FHIRConfiguration.CONFIG_LOCATION + File.separator + tenantId + File.separator + SP_FILE_BASENAME_JSON;
    }

    /*
     * (non-Javadoc)
     * @see com.ibm.watson.health.fhir.core.TenantSpecificFileBasedCache#createCachedObject(java.lang.String)
     */
    @Override
    public Map<String, String> createCachedObject(File f) throws Exception {
        try {
            // Added logging to help diagnose issues while loading the files.
            if (log.isLoggable(Level.FINE)) {
                log.fine(String.format(LOG_FILE_LOAD, f.toURI()));
            }
            return BulkDataConfigUtil.populateConfiguration(f);
        } catch (Throwable t) {
            // In R4, there are two files used with postfix JSON.
            // Default is to use JSON in R4
            throw new FHIROperationException(String.format(OPERATION_EXCEPTION, f.getAbsolutePath()), t);
        }
    }
}
