/*
 * (C) Copyright IBM Corp. 2016, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.context;

import org.linuxforhealth.fhir.core.context.FHIRPagingContext;
import org.linuxforhealth.fhir.model.type.Instant;
import org.linuxforhealth.fhir.persistence.HistorySortOrder;

public interface FHIRHistoryContext extends FHIRPagingContext {

    Instant getSince();
    void setSince(Instant since);

    HistorySortOrder getHistorySortOrder();

    void setHistorySortOrder(HistorySortOrder value);

}
