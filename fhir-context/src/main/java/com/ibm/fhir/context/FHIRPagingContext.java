/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.context;

public interface FHIRPagingContext {
    /**
     * @return
     */
    int getLastPageNumber();

    /**
     * @return
     */
    int getPageNumber();

    /**
     * @return
     */
    int getPageSize();

    /**
     * @return
     */
    int getTotalCount();

    /**
     * @param lastPageNumber
     */
    void setLastPageNumber(int lastPageNumber);

    /**
     * @param pageNumber
     */
    void setPageNumber(int pageNumber);

    /**
     * @param pageSize
     */
    void setPageSize(int pageSize);

    /**
     * @param totalCount
     */
    void setTotalCount(int totalCount);

    /**
     * @return
     */
    boolean isLenient();

    /**
     * @param lenient
     */
    void setLenient(boolean lenient);
}
