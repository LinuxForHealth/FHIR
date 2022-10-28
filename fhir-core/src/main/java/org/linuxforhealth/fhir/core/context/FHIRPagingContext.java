/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.core.context;

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
     * @return the number of matching resources in a single page
     * @see <a href="https://www.hl7.org/fhir/r4/search.html#count">https://www.hl7.org/fhir/r4/search.html#count</a>
     * @implSpec this number only applies to resources with {@code entry.search.mode = match}
     *           and does not include included resources or operation outcomes
     */
    int getPageSize();

    /**
     * @return the maximum number of matching resources allowed in a single page
     * @see <a href="https://www.hl7.org/fhir/r4/search.html#count">https://www.hl7.org/fhir/r4/search.html#count</a>
     * @implSpec this number only applies to resources with {@code entry.search.mode = match}
     *           and does not include included resources or operation outcomes
     */
    int getMaxPageSize();

    /**
     * @return the maximum number of included resources allowed in a single page
     * @see <a href="https://www.hl7.org/fhir/r4/search.html#count">https://www.hl7.org/fhir/r4/search.html#count</a>
     * @implSpec this number only applies to resources with {@code entry.search.mode = include}
     *           and does not include matching resources or operation outcomes
     */
    int getMaxPageIncludeCount();

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
     * 
     * @return the expected id of the first entry of the requested page; this id should
     *        uniquely identify a particular version of a particular resource type with a particular logical id
     */
    Long getFirstId();
    
    /**
     * 
     * @return the expected id of the last entry of the requested page; this id should
     *        uniquely identify a particular version of a particular resource type with a particular logical id
     */
    Long getLastId();

    /**
     * @param lastPageNumber the last page of results that can be requested for the corresponding query
     */
    void setLastPageNumber(int lastPageNumber);

    /**
     * @param pageNumber the current page number
     */
    void setPageNumber(int pageNumber);

    /**
     * @param pageSize the number of matching resources to include in a single page
     * @see <a href="https://www.hl7.org/fhir/r4/search.html#count">https://www.hl7.org/fhir/r4/search.html#count</a>
     * @implSpec this number only applies to resources with {@code entry.search.mode = match}
     *           and does not include included resources or operation outcomes
     */
    void setPageSize(int pageSize);

    /**
     * @param maxPageSize the maximum number of matching resources allowed in a single page
     * @see <a href="https://www.hl7.org/fhir/r4/search.html#count">https://www.hl7.org/fhir/r4/search.html#count</a>
     * @implSpec this number only applies to resources with {@code entry.search.mode = match}
     *           and does not include included resources or operation outcomes
     */
    void setMaxPageSize(int maxPageSize);

    /**
     * @param maxPageIncludeCount the maximum number of included resources allowed in a single page
     * @implSpec this number only applies to resources with {@code entry.search.mode = include}
     *           and does not include matching resources or operation outcomes
     */
    void setMaxPageIncludeCount(int maxPageIncludeCount);

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
    
    /**
     * Set the expected id of the first entry of the requested page; this id should
     *        uniquely identify a particular version of a particular resource type with a particular logical id
     * @param firstId
     */
    void setFirstId(Long firstId);
    
    /**
     * Set the expected id of the last entry of the requested page; this id should
     *        uniquely identify a particular version of a particular resource type with a particular logical id
     * @param lastId
     */
    void setLastId(Long lastId);
}
