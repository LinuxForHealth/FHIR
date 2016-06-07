/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.context;

import com.ibm.watsonhealth.fhir.core.context.FHIRPagingContext;
import com.ibm.watsonhealth.fhir.model.Instant;

public interface FHIRHistoryContext extends FHIRPagingContext {
    Instant getSince();
    void setSince(Instant since);
}
