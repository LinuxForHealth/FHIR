/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.context.impl;

import com.ibm.watsonhealth.fhir.persistence.context.FHIRHistoryContext;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.watsonhealth.fhir.persistence.interceptor.FHIRPersistenceEvent;
import com.ibm.watsonhealth.fhir.search.context.FHIRSearchContext;

/**
 * This class provides a concrete implementation of the FHIRPersistenceContext
 * interface and is used to pass request context-related information to the persistence layer.
 */
public class FHIRPersistenceContextImpl implements FHIRPersistenceContext {

    FHIRPersistenceEvent persistenceEvent;
    FHIRHistoryContext historyContext;
    FHIRSearchContext searchContext;
    
    // Hide the default ctor.
    protected FHIRPersistenceContextImpl() {
    }
    
    public FHIRPersistenceContextImpl(FHIRPersistenceEvent pe, FHIRHistoryContext hc, FHIRSearchContext sc) {
        this.persistenceEvent = pe;
        this.historyContext = hc;
        this.searchContext = sc;
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContext#getPersistenceEvent()
     */
    @Override
    public FHIRPersistenceEvent getPersistenceEvent() {
        return this.persistenceEvent;
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContext#getHistoryContext()
     */
    @Override
    public FHIRHistoryContext getHistoryContext() {
        return this.historyContext;
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContext#getSearchContext()
     */
    @Override
    public FHIRSearchContext getSearchContext() {
        return this.searchContext;
    }
}
