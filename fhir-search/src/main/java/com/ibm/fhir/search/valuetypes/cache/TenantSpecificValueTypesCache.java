/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.valuetypes.cache;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.core.TenantSpecificFileBasedCache;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.search.valuetypes.ValueTypesFactory;

/**
 * This class implements a cache of ValueTypes organized by tenantId. Each object stored in the cache will be a
 * two-level map of SearchParameters organized first by resource type, then by search parameter name.
 * 
 * @author pbastide
 */
public class TenantSpecificValueTypesCache extends TenantSpecificFileBasedCache<Map<String,Set<Class<?>>>> {

    private static final String CLASSNAME = TenantSpecificValueTypesCache.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

    private static final String VT_SP_FILE_BASENAME_JSON = "extension-search-parameters-valuetypes.json";

    private static final String CACHE_NAME = "ValueTypes";

    private static final String OPERATION_EXCEPTION = "An error occurred while loading one of the tenant value types files: %s";

    private static final String LOG_FILE_LOAD = "The value types file loaded is [%s]";

    public TenantSpecificValueTypesCache() {
        super(CACHE_NAME);
    }

    /*
     * (non-Javadoc)
     * @see com.ibm.fhir.core.TenantSpecificFileBasedCache#getCacheEntryFilename(java.lang.String)
     */
    @Override
    public String getCacheEntryFilename(String tenantId) {
        return FHIRConfiguration.getConfigHome() + FHIRConfiguration.CONFIG_LOCATION + File.separator + tenantId + File.separator + VT_SP_FILE_BASENAME_JSON;
    }

    /*
     * (non-Javadoc)
     * @see com.ibm.fhir.core.TenantSpecificFileBasedCache#createCachedObject(java.lang.String)
     */
    @Override
    public Map<String,Set<Class<?>>> createCachedObject(File f) throws Exception {
        try {
            // Added logging to help diagnose issues while loading the files.
            if (log.isLoggable(Level.FINE)) {
                log.fine(String.format(LOG_FILE_LOAD, f.toURI()));
            }
            return ValueTypesFactory.getValueTypesProcessor().loadFromFile(f);
        } catch (Throwable t) {
            // In R4, there are two files used with postfix JSON.
            // Default is to use JSON in R4
            throw new FHIROperationException(String.format(OPERATION_EXCEPTION, f.getAbsolutePath()), t);
        }
    }
}
