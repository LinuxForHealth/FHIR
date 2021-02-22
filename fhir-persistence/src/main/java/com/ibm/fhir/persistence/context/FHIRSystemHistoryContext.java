/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.context;

import com.ibm.fhir.model.type.Instant;

/**
 *
 */
public interface FHIRSystemHistoryContext {

    /**
     * Get the value of the _since parameter, or null if not given
     * @return
     */
    Instant getSince();

    /**
     * Get the value of the _afterHistoryId parameter, or null if not given
     * @return
     */
    Long getAfterHistoryId();

    /**
     * Get the value of the _count parameter, or null if not given
     * @return
     */
    Integer getCount();

    /**
     * Get the value of the lenient parameter
     * @return
     */
    boolean isLenient();
}
