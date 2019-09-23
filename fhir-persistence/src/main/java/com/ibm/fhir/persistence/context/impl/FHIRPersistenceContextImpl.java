/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.context.impl;

import com.ibm.fhir.persistence.context.FHIRHistoryContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.interceptor.FHIRPersistenceEvent;
import com.ibm.fhir.search.context.FHIRSearchContext;

/**
 * This class provides a concrete implementation of the FHIRPersistenceContext
 * interface and is used to pass request context-related information to the persistence layer.
 */
public class FHIRPersistenceContextImpl implements FHIRPersistenceContext {

    private FHIRPersistenceEvent persistenceEvent;
    private FHIRHistoryContext historyContext;
    private FHIRSearchContext searchContext;
    private boolean includeDeleted;
    
    public FHIRPersistenceContextImpl(FHIRPersistenceEvent pe) {
        this.persistenceEvent = pe;
    }
    
    public FHIRPersistenceContextImpl(FHIRPersistenceEvent pe, boolean includeDeleted) {
        this.persistenceEvent = pe;
        setIncludeDeleted(includeDeleted);
    }
    
    public FHIRPersistenceContextImpl(FHIRPersistenceEvent pe, FHIRHistoryContext hc) {
        this.persistenceEvent = pe;
        this.historyContext = hc;
    }
    public FHIRPersistenceContextImpl(FHIRPersistenceEvent pe, FHIRSearchContext sc) {
        this.persistenceEvent = pe;
        this.searchContext = sc;
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.persistence.context.FHIRPersistenceContext#getPersistenceEvent()
     */
    @Override
    public FHIRPersistenceEvent getPersistenceEvent() {
        return this.persistenceEvent;
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.persistence.context.FHIRPersistenceContext#getHistoryContext()
     */
    @Override
    public FHIRHistoryContext getHistoryContext() {
        return this.historyContext;
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.persistence.context.FHIRPersistenceContext#getSearchContext()
     */
    @Override
    public FHIRSearchContext getSearchContext() {
        return this.searchContext;
    }
    
    /* (non-Javadoc)
     * @see com.ibm.fhir.persistence.context.FHIRPersistenceContext#includeDeleted()
     */
    @Override
    public boolean includeDeleted() {
        return includeDeleted;
    }
    
    public void setIncludeDeleted(boolean includeDeleted) {
        this.includeDeleted = includeDeleted;
    }
}
