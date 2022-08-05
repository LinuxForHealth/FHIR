/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence;


/**
 * The ordering specified in the client request for whole system history
 * interactions. This is not defined in the R4 specification but has been
 * proposed for R5. The IBM FHIR Server implements it for R4 because it is 
 * useful to support system-system synchronization scenarios
 * <pre>
 * _sort=_lastUpdated            - ORDER BY last_updated ASC
 * _sort=-_lastUpdated (default) - ORDER BY last_updated DESC
 * _sort=none                    - ORDER BY resource_id ASC
 * </pre>
 */
public enum HistorySortOrder {
    ASC_LAST_UPDATED("_lastUpdated"),
    DESC_LAST_UPDATED("-_lastUpdated"),
    NONE("none");

    public static final String STR_LAST_UPDATED = "_lastUpdated";
    public static final String STR_DESC_LAST_UPDATED = "-_lastUpdated";
    public static final String STR_NONE = "none";

    // The value string for the resource
    private final String sortValue;
    
    /**
     * Private constructor
     * @param sortValue
     */
    private HistorySortOrder(String sortValue) {
        this.sortValue = sortValue;
    }

    @Override
    public String toString() {
        return this.sortValue;
    }

    /**
     * Get the enum value for the sort parameter value
     * @param sortValue
     * @return
     */
    public static HistorySortOrder of(String sortValue) {
        switch (sortValue) {
        case STR_LAST_UPDATED:
            return ASC_LAST_UPDATED;
        case STR_DESC_LAST_UPDATED:
            return DESC_LAST_UPDATED;
        case STR_NONE:
            return NONE;
        }
        
        throw new IllegalArgumentException("Invalid sortValue: " + sortValue);
    }
}