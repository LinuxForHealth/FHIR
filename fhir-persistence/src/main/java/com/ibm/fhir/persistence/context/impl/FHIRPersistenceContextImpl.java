/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.context.impl;

import com.ibm.fhir.persistence.context.FHIRHistoryContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceEvent;
import com.ibm.fhir.search.context.FHIRSearchContext;

/**
 * This class provides a concrete implementation of the FHIRPersistenceContext
 * interface and is used to pass request context-related information to the persistence layer.
 */
public class FHIRPersistenceContextImpl implements FHIRPersistenceContext {

    private FHIRPersistenceEvent persistenceEvent;
    private FHIRHistoryContext historyContext;
    private FHIRSearchContext searchContext;
    private boolean includeDeleted = false;
    private Integer ifNoneMatch;

    public FHIRPersistenceContextImpl(FHIRPersistenceEvent pe) {
        this.persistenceEvent = pe;
    }

    public FHIRPersistenceContextImpl(FHIRPersistenceEvent pe, boolean includeDeleted) {
        this.persistenceEvent = pe;
        setIncludeDeleted(includeDeleted);
    }

    /**
     * Public constructor
     * @param pe
     * @param ifNoneMatch
     */
    public FHIRPersistenceContextImpl(FHIRPersistenceEvent pe, Integer ifNoneMatch) {
        this.persistenceEvent = pe;
        setIfNoneMatch(ifNoneMatch);
    }
    
    public FHIRPersistenceContextImpl(FHIRPersistenceEvent pe, FHIRHistoryContext hc) {
        this.persistenceEvent = pe;
        this.historyContext = hc;
    }
    public FHIRPersistenceContextImpl(FHIRPersistenceEvent pe, FHIRSearchContext sc) {
        this.persistenceEvent = pe;
        this.searchContext = sc;
    }

    public FHIRPersistenceContextImpl(FHIRPersistenceEvent pe, boolean includeDeleted, FHIRSearchContext sc) {
        this.persistenceEvent = pe;
        setIncludeDeleted(includeDeleted);
        this.searchContext = sc;
    }

    @Override
    public FHIRPersistenceEvent getPersistenceEvent() {
        return this.persistenceEvent;
    }

    @Override
    public FHIRHistoryContext getHistoryContext() {
        return this.historyContext;
    }

    @Override
    public FHIRSearchContext getSearchContext() {
        return this.searchContext;
    }

    @Override
    public boolean includeDeleted() {
        return includeDeleted;
    }

    /**
     * Setter for the includeDeleted flag
     * @param includeDeleted
     */
    public void setIncludeDeleted(boolean includeDeleted) {
        this.includeDeleted = includeDeleted;
    }

    /**
     * Setter for the If-None-Match header value
     * @param ifNoneMatch
     */
    public void setIfNoneMatch(Integer ifNoneMatch) {
        this.ifNoneMatch = ifNoneMatch;
    }

    @Override
    public Integer getIfNoneMatch() {
        return this.ifNoneMatch;
    }
}