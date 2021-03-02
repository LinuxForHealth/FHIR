/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.context.impl;

import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.persistence.context.FHIRSystemHistoryContext;


/**
 * Holds the context for system history requests
 */
public class FHIRSystemHistoryContextImpl implements FHIRSystemHistoryContext {

    // Fetch records with lastUpdated >= since
    private Instant since;

    // Fetch records with a historyId > afterHistoryId
    private Long afterHistoryId;

    // Fetch up to count records
    private Integer count;

    // Run in lenient mode
    private boolean lenient;

    @Override
    public String toString() {
        return "_count=" + count + ", _since=" + since
                + ", afterResourceId=" + afterHistoryId;
    }

    @Override
    public Instant getSince() {
        return this.since;
    }

    public void setSince(Instant since) {
        this.since = since;
    }

    @Override
    public Long getAfterHistoryId() {
        return this.afterHistoryId;
    }

    public void setAfterHistoryId(long id) {
        this.afterHistoryId = id;
    }

    @Override
    public Integer getCount() {
        return this.count;
    }

    public void setCount(int c) {
        this.count = c;
    }

    /**
     * @param lenient
     */
    public void setLenient(boolean lenient) {
        this.lenient = lenient;
    }

    @Override
    public boolean isLenient() {
        return this.lenient;
    }
}