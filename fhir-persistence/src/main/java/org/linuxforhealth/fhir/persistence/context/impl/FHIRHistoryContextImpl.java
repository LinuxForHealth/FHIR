/*
 * (C) Copyright IBM Corp. 2016, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.context.impl;

import org.linuxforhealth.fhir.core.context.impl.FHIRPagingContextImpl;
import org.linuxforhealth.fhir.model.type.Instant;
import org.linuxforhealth.fhir.persistence.HistorySortOrder;
import org.linuxforhealth.fhir.persistence.context.FHIRHistoryContext;
import org.linuxforhealth.fhir.search.parameters.QueryParameter;
import org.linuxforhealth.fhir.search.parameters.SortParameter;

import java.util.List;

public class FHIRHistoryContextImpl extends FHIRPagingContextImpl implements FHIRHistoryContext {
    private Instant since = null;
    private HistorySortOrder historySortOrder = null;

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

    @Override
    public HistorySortOrder getHistorySortOrder() {
        return historySortOrder;
    }

    @Override
    public void setHistorySortOrder(HistorySortOrder value) {
        historySortOrder = value;
    }


}
