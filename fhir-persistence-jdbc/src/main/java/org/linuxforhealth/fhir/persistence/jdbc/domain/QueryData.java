/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.domain;

import org.linuxforhealth.fhir.database.utils.query.SelectAdapter;

/**
 * A simple wrapper used by the {@link SearchQueryRenderer} to hold the current
 * query - {@link SelectAdapter} - and the table alias values if this query
 * wants to advertise them.
 * Immutable.
 */
public class QueryData {
    // The query being wrapped
    private final SelectAdapter query;

    // The alias of the parameter table which is needed as the target for reverse chaining
    private final String paramAlias;

    // The alias of the xx_LOGICAL_RESOURCES table
    private final String lrAlias;

    // Track the depth of the join when building chain/reverse-chain searches
    private final int chainDepth;

    // The resource type associated with this part of the query
    private final String resourceType;

    /**
     * Public constructor
     * @param query
     * @param lrAlias
     * @param paramAlias
     * @param resourceType
     * @param chainDepth
     */
    public QueryData(SelectAdapter query, String lrAlias, String paramAlias, String resourceType, int chainDepth) {
        this.query = query;
        this.lrAlias = lrAlias;
        this.paramAlias = paramAlias;
        this.resourceType = resourceType;
        this.chainDepth = chainDepth;
    }


    /**
     * Convenience function to generate the alias we use for parent xx_logical_resource tables
     * Typically used when correlating a parameter sub-join with the parent query
     * @return
     */
    public String getLRAlias() {
        return this.lrAlias;
    }

    /**
     * Get the alias of the parameter table if one is advertised by this query block
     * @return
     */
    public String getParamAlias() {
        return this.paramAlias;
    }

    /**
     * @return the query
     */
    public SelectAdapter getQuery() {
        return query;
    }

    /**
     * Getter for the chainDepth
     * @return the chainDepth which is >= 0
     */
    public int getChainDepth() {
        return this.chainDepth;
    }

    /**
     * @return the resourceType
     */
    public String getResourceType() {
        return resourceType;
    }
}