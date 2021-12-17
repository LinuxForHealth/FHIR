/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.context.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    // List of resource type names to include given by _type
    private final List<String> resourceTypes = new ArrayList<>();
    
    // Flag to determine if we exclude resources falling inside the transaction timeout window
    private boolean excludeTransactionTimeoutWindow;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("_count=");
        result.append(count);
        result.append(", _since=");
        result.append(since);
        result.append(", _afterResourceId=");
        result.append(afterHistoryId);
        
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
}