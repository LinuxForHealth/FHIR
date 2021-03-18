/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.core.context;

/**
 * The paging context for given request
 */
public interface FHIRPagingContext {
    /**
     * @return the last page number
     */
    int getLastPageNumber();

    /**
     * @return the current page number
     */
    int getPageNumber();

    /**
     * @return the number of resources in a single page
     * @see <a href="https://www.hl7.org/fhir/r4/search.html#count">https://www.hl7.org/fhir/r4/search.html#count</a>
     * @implSpec this number only applies to resources with {@code entry.search.mode = search}
     *           and does not include included resources or operation outcomes
     */
    int getPageSize();

    /**
     * @return the total number of matching resources for the corresponding query, or null if total count is not available
     * @see <a href="https://www.hl7.org/fhir/r4/search.html#count">https://www.hl7.org/fhir/r4/search.html#count</a>
     * @implSpec this number only includes the total number of matching resources; it does not count extra resources
     *           such as OperationOutcome or included resources that may also be returned
     */
    Integer getTotalCount();

    /**
     * @return the number of matching resources returned for the corresponding query
     * @see <a href="https://www.hl7.org/fhir/r4/search.html#count">https://www.hl7.org/fhir/r4/search.html#count</a>
     * @implSpec this number only includes the number of matching resources returned; it does not count extra resources
     *           such as OperationOutcome or included resources that may also be returned
     */
    int getMatchCount();

    /**
     * @param lastPageNumber the last page of results that can be requested for the corresponding query
     */
    void setLastPageNumber(int lastPageNumber);

    /**
     * @param pageNumber the current page number
     */
    void setPageNumber(int pageNumber);

    /**
     * @param pageSize the number of resources to include in a single page
     * @see <a href="https://www.hl7.org/fhir/r4/search.html#count">https://www.hl7.org/fhir/r4/search.html#count</a>
     * @implSpec this number only applies to resources with {@code entry.search.mode = search}
     *           and does not include included resources or operation outcomes
     *
     */
    void setPageSize(int pageSize);

    /**
     * @param totalCount the total number of matching resources for the corresponding query
     * @see <a href="https://www.hl7.org/fhir/r4/search.html#count">https://www.hl7.org/fhir/r4/search.html#count</a>
     * @implSpec this number only includes the total number of matching resources; it does not count extra resources
     *           such as OperationOutcome or included resources that may also be returned
     */
    void setTotalCount(int totalCount);

    /**
     * @param matchCount the number of matching resources returned for the corresponding query
     * @see <a href="https://www.hl7.org/fhir/r4/search.html#count">https://www.hl7.org/fhir/r4/search.html#count</a>
     * @implSpec this number only includes the number of matching resources returned; it does not count extra resources
     *           such as OperationOutcome or included resources that may also be returned
     */
    void setMatchCount(int matchCount);

    /**
     * @return whether the request should be handled with leniency
     */
    boolean isLenient();

    /**
     * @param lenient whether the request should be handled with leniency
     */
    void setLenient(boolean lenient);
}
