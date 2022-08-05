/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.context.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.linuxforhealth.fhir.core.HTTPReturnPreference;
import org.linuxforhealth.fhir.model.type.Instant;
import org.linuxforhealth.fhir.persistence.HistorySortOrder;
import org.linuxforhealth.fhir.persistence.context.FHIRSystemHistoryContext;


/**
 * Holds the context for system history requests
 */
public class FHIRSystemHistoryContextImpl implements FHIRSystemHistoryContext {

    // Fetch records with lastUpdated >= since
    private Instant since;

    // Fetch records with lastUpdated <= before
    private Instant before;

    // Support for pagination with the change id (resourceId)
    private Long changeIdMarker;

    // Fetch up to count records
    private Integer count;

    // Run in lenient mode
    private boolean lenient;

    // List of resource type names to include given by _type
    private final List<String> resourceTypes = new ArrayList<>();
    
    // Flag to determine if we exclude resources falling inside the transaction timeout window
    private boolean excludeTransactionTimeoutWindow;

    // The order in which we traverse the history
    private HistorySortOrder historySortOrder;

    // Should we include resources in the response or just return the urls
    private HTTPReturnPreference returnPreference;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("_count=");
        result.append(count);
        result.append(", _since=");
        result.append(since);
        result.append(", _before=");
        result.append(before);
        result.append(", _changeIdMarker=");
        result.append(changeIdMarker);
        
        final String typeNames = String.join(",", this.resourceTypes);
        if (typeNames.length() > 0) {
            result.append(", _type=");
            result.append(typeNames);
        }
        return result.toString();
    }

    @Override
    public Instant getSince() {
        return this.since;
    }

    /**
     * Set the since value
     * @param since
     */
    public void setSince(Instant since) {
        this.since = since;
    }

    /**
     * Set the before value
     * @param before
     */
    public void setBefore(Instant before) {
        this.before = before;
    }

    @Override
    public Instant getBefore() {
        return this.before;
    }

    @Override
    public Long getChangeIdMarker() {
        return this.changeIdMarker;
    }

    /**
     * Set the changeIdMarker
     * @param id
     */
    public void setChangeIdMarker(long id) {
        this.changeIdMarker = id;
    }

    /**
     * Add the resource type to include in the response
     * @param resourceType
     */
    public void addResourceType(String resourceType) {
        this.resourceTypes.add(resourceType);
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

    @Override
    public List<String> getResourceTypes() {
        return Collections.unmodifiableList(this.resourceTypes);
    }
    
    /**
     * Set the excludeTransactionTimeoutWindow flag
     * @param flag
     */
    public void setExcludeTransactionTimeoutWindow(boolean flag) {
        this.excludeTransactionTimeoutWindow = flag;
    }

    @Override
    public boolean isExcludeTransactionTimeoutWindow() {
        return this.excludeTransactionTimeoutWindow;
    }

    /**
     * Set the historySortOrder
     * @param historySortOrder
     */
    public void setHistorySortOrder(HistorySortOrder historySortOrder) {
        this.historySortOrder = historySortOrder;
    }

    /**
     * Set the returnPreference
     * @param returnPreference
     */
    public void setReturnPreference(HTTPReturnPreference returnPreference) {
        this.returnPreference = returnPreference;
    }

    @Override
    public HistorySortOrder getHistorySortOrder() {
        return this.historySortOrder;
    }

    @Override
    public HTTPReturnPreference getReturnPreference() {
        return returnPreference;
    }
}