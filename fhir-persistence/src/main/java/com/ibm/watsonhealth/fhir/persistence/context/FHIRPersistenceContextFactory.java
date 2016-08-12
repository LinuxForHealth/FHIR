/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.context;

import com.ibm.watsonhealth.fhir.persistence.context.impl.FHIRHistoryContextImpl;
import com.ibm.watsonhealth.fhir.persistence.context.impl.FHIRPersistenceContextImpl;
import com.ibm.watsonhealth.fhir.persistence.interceptor.FHIRPersistenceEvent;
import com.ibm.watsonhealth.fhir.search.context.FHIRSearchContext;

/**
 * This is a factory used to create instances of the FHIRPersistenceContext interface.
 */
public class FHIRPersistenceContextFactory {

    /**
     * Hide the default ctor.
     */
    private FHIRPersistenceContextFactory() {
    }
    
    /**
     * Returns a FHIRPersistenceContext that contains a FHIRPersistenceEvent instance.
     * @param event the FHIRPersistenceEvent instance to be contained in the FHIRPersistenceContext instance
     */
    public static FHIRPersistenceContext createPersistenceContext(FHIRPersistenceEvent event) {
        return new FHIRPersistenceContextImpl(event);
    }
    
    /**
     * Returns a FHIRPersistenceContext that contains a FHIRPersistenceEvent and a FHIRHistoryContext.
     * @param event the FHIRPersistenceEvent instance to be contained in the FHIRPersistenceContext instance
     * @param historyContext the FHIRHistoryContext instance to be contained in the FHIRPersistenceContext instance
     */
    public static FHIRPersistenceContext createPersistenceContext(FHIRPersistenceEvent event, FHIRHistoryContext historyContext) {
        return new FHIRPersistenceContextImpl(event, historyContext);
    }
    
    /**
     * Returns a FHIRPersistenceContext that contains a FHIRPersistenceEvent and a FHIRSearchContext.
     * @param event the FHIRPersistenceEvent instance to be contained in the FHIRPersistenceContext instance
     * @param searchContext the FHIRSearchContext instance to be contained in the FHIRPersistenceContext instance
     */
    public static FHIRPersistenceContext createPersistenceContext(FHIRPersistenceEvent event, FHIRSearchContext searchContext) {
        return new FHIRPersistenceContextImpl(event, searchContext);
    }
    
    /**
     * Returns a FHIRHistoryContext instance with default values.
     */
    public static FHIRHistoryContext createHistoryContext() {
        return new FHIRHistoryContextImpl();
    }
}
