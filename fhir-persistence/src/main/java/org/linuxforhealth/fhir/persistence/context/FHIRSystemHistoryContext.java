/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.context;

import java.util.List;

import org.linuxforhealth.fhir.core.HTTPReturnPreference;
import org.linuxforhealth.fhir.model.type.Instant;
import org.linuxforhealth.fhir.persistence.HistorySortOrder;

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
     * Get the value of the _before parameter, or null if not given
     * @return
     */
    Instant getBefore();

    /**
     * Get the value of the _changeIdMarker parameter, or null if not given
     * @return
     */
    Long getChangeIdMarker();

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
     * <pre>
     *   Prefer: return=minimal          response bundle summary without Resources
     *   Prefer: return=representation   response bundle includes Resources
     *   Prefer: return=OperationOutcome 400 Bad Request
     * </pre>
     *
     * @return
     */
    HTTPReturnPreference getReturnPreference();
}