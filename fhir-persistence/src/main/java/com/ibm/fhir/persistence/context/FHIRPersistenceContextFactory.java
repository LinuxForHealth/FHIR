/*
 * (C) Copyright IBM Corp. 2016, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.context;

import java.util.logging.Logger;

import com.ibm.fhir.config.FHIRConfigHelper;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.core.FHIRConstants;
import com.ibm.fhir.persistence.context.impl.FHIRHistoryContextImpl;
import com.ibm.fhir.persistence.context.impl.FHIRPersistenceContextImpl;
import com.ibm.fhir.search.context.FHIRSearchContext;

/**
 * This is a factory used to create instances of the FHIRPersistenceContext interface.
 */
public class FHIRPersistenceContextFactory {
    private static Logger log = Logger.getLogger(FHIRPersistenceContextFactory.class.getName());

    private FHIRPersistenceContextFactory() {
        // No Operation
    }

    /**
     * Returns a FHIRPersistenceContext that contains a FHIRPersistenceEvent instance.
     * @param event the FHIRPersistenceEvent instance to be contained in the FHIRPersistenceContext instance
     */
    public static FHIRPersistenceContext createPersistenceContext(FHIRPersistenceEvent event) {
        return FHIRPersistenceContextImpl.builder(event)
                .build();
    }

    /**
     * Returns a FHIRPersistenceContext that contains a FHIRPersistenceEvent instance.
     * @param event the FHIRPersistenceEvent instance to be contained in the FHIRPersistenceContext instance
     * @param ifNoneMatch flag to tell the persistence layer to apply conditional create-on-update logic.
     */
    public static FHIRPersistenceContext createPersistenceContext(FHIRPersistenceEvent event, Integer ifNoneMatch) {
        return FHIRPersistenceContextImpl.builder(event)
                .withIfNoneMatch(ifNoneMatch)
                .build();
    }

    /**
     * Returns a FHIRPersistenceContext that contains a FHIRPersistenceEvent and a FHIRHistoryContext.
     * @param event the FHIRPersistenceEvent instance to be contained in the FHIRPersistenceContext instance
     * @param historyContext the FHIRHistoryContext instance to be contained in the FHIRPersistenceContext instance
     */
    public static FHIRPersistenceContext createPersistenceContext(FHIRPersistenceEvent event, FHIRHistoryContext historyContext) {
        return FHIRPersistenceContextImpl.builder(event)
                .withHistoryContext(historyContext)
                .build();
    }

    /**
     * Returns a FHIRPersistenceContext that contains a FHIRPersistenceEvent and a FHIRSearchContext.
     * @param event the FHIRPersistenceEvent instance to be contained in the FHIRPersistenceContext instance
     * @param searchContext the FHIRSearchContext instance to be contained in the FHIRPersistenceContext instance
     */
    public static FHIRPersistenceContext createPersistenceContext(FHIRPersistenceEvent event, FHIRSearchContext searchContext) {
        return FHIRPersistenceContextImpl.builder(event)
                .withSearchContext(searchContext)
                .build();
    }

    /**
     * Returns a FHIRHistoryContext instance with default values.
     */
    public static FHIRHistoryContext createHistoryContext() {
        int maxPageSize = FHIRConfigHelper.getIntProperty(FHIRConfiguration.PROPERTY_MAX_PAGE_SIZE, FHIRConstants.FHIR_PAGE_SIZE_DEFAULT_MAX);
        int pageSize = FHIRConfigHelper.getIntProperty(FHIRConfiguration.PROPERTY_DEFAULT_PAGE_SIZE, FHIRConstants.FHIR_PAGE_SIZE_DEFAULT);
        if (pageSize > maxPageSize) {
            log.warning(String.format("Server configuration %s = %d exceeds maximum allowed page size %d; using %d",
                FHIRConfiguration.PROPERTY_DEFAULT_PAGE_SIZE, pageSize, maxPageSize, maxPageSize));
            pageSize = maxPageSize;
        }
        int maxPageIncludeCount = FHIRConfigHelper.getIntProperty(FHIRConfiguration.PROPERTY_MAX_PAGE_INCLUDE_COUNT, FHIRConstants.FHIR_PAGE_INCLUDE_COUNT_DEFAULT_MAX);

        FHIRHistoryContext ctx = new FHIRHistoryContextImpl();
        ctx.setPageSize(pageSize);
        ctx.setMaxPageSize(maxPageSize);
        ctx.setMaxPageIncludeCount(maxPageIncludeCount);
        return ctx;
    }
}
