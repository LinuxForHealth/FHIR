/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.parameters.cache;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.core.TenantSpecificFileBasedCache;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.search.parameters.ParametersMap;
import com.ibm.fhir.search.parameters.ParametersUtil;

/**
 * This class implements a cache of SearchParameters organized by tenantId. Each object stored in the cache will be a
 * two-level map of SearchParameters organized first by resource type, then by search parameter code.
 *
 * Note: While we support json format only, to enable XML, it's best to create a new cache specific to XML. This change
 * should change one line in this class, and be instantiated in the SearchUtil, and embedded in the call to Parameters.
 * Alternatively, one could, upon not finding the JSON file, load the XML file.
 */
public class TenantSpecificSearchParameterCache extends TenantSpecificFileBasedCache<Map<String, ParametersMap>> {

    private static final String CLASSNAME = TenantSpecificSearchParameterCache.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

    private static final String SP_FILE_BASENAME_JSON = "extension-search-parameters.json";

    private static final String CACHE_NAME = "SearchParameters";

    private static final String OPERATION_EXCEPTION = "An error occurred while loading one of the tenant files: %s";

    private static final String LOG_FILE_LOAD = "The file loaded is [%s]";

    public TenantSpecificSearchParameterCache() {
        super(CACHE_NAME);
    }

    @Override
    public String getCacheEntryFilename(String tenantId) {
        return FHIRConfiguration.getConfigHome() + FHIRConfiguration.CONFIG_LOCATION + File.separator + tenantId + File.separator + SP_FILE_BASENAME_JSON;
    }

    @Override
    public Map<String, ParametersMap> createCachedObject(File f) throws Exception {
        // Added logging to help diagnose issues while loading the files.
        if (log.isLoggable(Level.FINE)) {
            log.fine(String.format(LOG_FILE_LOAD, f.toURI()));
        }
        try (Reader reader = new FileReader(f);) {
            // Default is to use JSON in R4
            Bundle bundle = FHIRParser.parser(Format.JSON).parse(reader);
            return ParametersUtil.buildSearchParametersMapFromBundle(bundle);
        } catch (Throwable t) {
            throw new FHIROperationException(String.format(OPERATION_EXCEPTION, f.getAbsolutePath()), t);
        }
    }
}