/*
 * (C) Copyright IBM Corp. 2016, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.context.impl;

import com.ibm.fhir.persistence.context.FHIRHistoryContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceEvent;
import com.ibm.fhir.persistence.payload.PayloadPersistenceResponse;
import com.ibm.fhir.search.context.FHIRSearchContext;

/**
 * This class provides a concrete implementation of the FHIRPersistenceContext
 * interface and is used to pass request context-related information to the persistence layer.
 */
public class FHIRPersistenceContextImpl implements FHIRPersistenceContext {

    private FHIRPersistenceEvent persistenceEvent;
    private FHIRHistoryContext historyContext;
    private FHIRSearchContext searchContext;
    private Integer ifNoneMatch;

    // The response from the payload persistence (offloading) call, if any
    private PayloadPersistenceResponse offloadResponse;

    /**
     * Factory function to create a FHIRPersistenceContext builder
     * @param event
     * @return
     */
    public static Builder builder(FHIRPersistenceEvent event) {
        return new Builder(event);
    }

    /**
     * Builder to create new instances of FHIRPersistenceContextImpl
     */
    public static class Builder {
        private FHIRPersistenceEvent persistenceEvent;
        private FHIRHistoryContext historyContext;
        private FHIRSearchContext searchContext;
        private Integer ifNoneMatch;
        private PayloadPersistenceResponse offloadResponse;

        /**
         * Protected constructor
         * @param event
         */
        protected Builder(FHIRPersistenceEvent event) {
            this.persistenceEvent = event;
        }

        /**
         * Build the FHIRPersistenceContext implementation
         * @return
         */
        public FHIRPersistenceContext build() {
            FHIRPersistenceContextImpl impl;

            if (historyContext != null) {
                impl = new FHIRPersistenceContextImpl(persistenceEvent, historyContext);
            } else if (searchContext != null) {
                impl = new FHIRPersistenceContextImpl(persistenceEvent, searchContext);
            } else {
                impl = new FHIRPersistenceContextImpl(persistenceEvent);
            }
            impl.setIfNoneMatch(ifNoneMatch);
            impl.setOffloadResponse(offloadResponse);

            return impl;
        }

        /**
         * Build with the given searchContext
         * @param searchContext
         * @return
         */
        public Builder withSearchContext(FHIRSearchContext searchContext) {
            this.searchContext = searchContext;
            return this;
        }

        /**
         * Build with the given historyContext
         * @param historyContext
         * @return
         */
        public Builder withHistoryContext(FHIRHistoryContext historyContext) {
            this.historyContext = historyContext;
            return this;
        }

        /**
         * Build with the ifNoneMatch value
         * @param ifNoneMatch
         * @return
         */
        public Builder withIfNoneMatch(Integer ifNoneMatch) {
            this.ifNoneMatch = ifNoneMatch;
            return this;
        }

        /**
         * Build with the given offloadResponse
         * @param offloadResponse
         * @return
         */
        public Builder withOffloadResponse(PayloadPersistenceResponse offloadResponse) {
            this.offloadResponse = offloadResponse;
            return this;
        }
    }

    /**
     * Private constructor
     * @param pe
     */
    private FHIRPersistenceContextImpl(FHIRPersistenceEvent pe) {
        this.persistenceEvent = pe;
    }

    /**
     * Public constructor
     * @param pe
     * @param ifNoneMatch
     */
    private FHIRPersistenceContextImpl(FHIRPersistenceEvent pe, Integer ifNoneMatch) {
        this.persistenceEvent = pe;
        setIfNoneMatch(ifNoneMatch);
    }

    private FHIRPersistenceContextImpl(FHIRPersistenceEvent pe, FHIRHistoryContext hc) {
        this.persistenceEvent = pe;
        this.historyContext = hc;
    }
    private FHIRPersistenceContextImpl(FHIRPersistenceEvent pe, FHIRSearchContext sc) {
        this.persistenceEvent = pe;
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

    @Override
    public PayloadPersistenceResponse getOffloadResponse() {
        return this.offloadResponse;
    }

    /**
     * @param offloadResponse the offloadResponse to set
     */
    public void setOffloadResponse(PayloadPersistenceResponse offloadResponse) {
        this.offloadResponse = offloadResponse;
    }
}