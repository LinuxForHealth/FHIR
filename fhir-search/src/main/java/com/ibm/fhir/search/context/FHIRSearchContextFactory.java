/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.context;

import java.util.logging.Logger;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.core.FHIRConstants;
import com.ibm.fhir.search.context.impl.FHIRSearchContextImpl;

/**
 * This factory class can be used to create instances of the FHIRSearchContext interface.
 */
public class FHIRSearchContextFactory {
    private static Logger log = Logger.getLogger(FHIRSearchContextFactory.class.getName());

    /**
     * Hide the default ctor.
     */
    private FHIRSearchContextFactory() {
    }

    /**
     * Returns a new instance of the FHIRSearchContext interface.
     */
    public static FHIRSearchContext createSearchContext() {
        int maxPageSize = FHIRConfigHelper.getIntProperty(FHIRConfiguration.PROPERTY_MAX_PAGE_SIZE, FHIRConstants.FHIR_PAGE_SIZE_DEFAULT_MAX);
        int pageSize = FHIRConfigHelper.getIntProperty(FHIRConfiguration.PROPERTY_DEFAULT_PAGE_SIZE, FHIRConstants.FHIR_PAGE_SIZE_DEFAULT);
        if (pageSize > maxPageSize) {
            log.warning(String.format("Server configuration %s = %d exceeds maximum allowed page size %d; using %d",
                FHIRConfiguration.PROPERTY_DEFAULT_PAGE_SIZE, pageSize, maxPageSize, maxPageSize));
            pageSize = maxPageSize;
        }
        int maxPageIncludeCount = FHIRConfigHelper.getIntProperty(FHIRConfiguration.PROPERTY_MAX_PAGE_INCLUDE_COUNT, FHIRConstants.FHIR_PAGE_INCLUDE_COUNT_DEFAULT_MAX);

        FHIRSearchContext ctx = new FHIRSearchContextImpl();
        ctx.setPageSize(pageSize);
        ctx.setMaxPageSize(maxPageSize);
        ctx.setMaxPageIncludeCount(maxPageIncludeCount);
        return ctx;
    }
}
