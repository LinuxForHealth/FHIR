/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.context.impl;

import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.instant;

import com.ibm.watsonhealth.fhir.core.context.impl.FHIRPagingContextImpl;
import com.ibm.watsonhealth.fhir.model.Instant;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRHistoryContext;

public class FHIRHistoryContextImpl extends FHIRPagingContextImpl implements FHIRHistoryContext {
    private Instant since = instant("1970-01-01T00:00:00Z");
    
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
