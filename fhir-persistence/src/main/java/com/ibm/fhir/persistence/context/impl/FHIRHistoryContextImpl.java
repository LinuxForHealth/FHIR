/*
 * (C) Copyright IBM Corp. 2016, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.context.impl;

import com.ibm.fhir.core.context.impl.FHIRPagingContextImpl;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.persistence.context.FHIRHistoryContext;

public class FHIRHistoryContextImpl extends FHIRPagingContextImpl implements FHIRHistoryContext {
    private Instant since = null;
    
    public FHIRHistoryContextImpl() {
    }

    @Override
    public Instant getSince() {
        return since;
    }

    @Override
    public void setSince(Instant since) {
        this.since = since;
    }
}
