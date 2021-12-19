/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.context;

import java.util.List;

import com.ibm.fhir.core.HTTPReturnPreference;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.persistence.HistorySortOrder;

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
    
    /**
     * Get the list of resource types
     * @return an immutable list of resource type names
     */
    List<String> getResourceTypes();
    
    /**
     * Should we exclude resources that fall inside the server's transaction timeout window?
     * @return
     */
    boolean isExcludeTransactionTimeoutWindow();
    
    /**
     * Get the whole system history sort order
     * @return
     */
    HistorySortOrder getHistorySortOrder();
    
    /**
     * Get the return preference
     * @formatter:off
     *   Prefer: return=minimal          response bundle summary without Resources
     *   Prefer: return=representation   response bundle includes Resources
     *   Prefer: return=OperationOutcome 400 Bad Request
     * @formatter:on
     *
     * @return
     */
    HTTPReturnPreference getReturnPreference();
}