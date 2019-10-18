/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.core.context;

public interface FHIRPagingContext {
    int getLastPageNumber();
    int getPageNumber();
    int getPageSize();
    long getTotalCount();
    void setLastPageNumber(int lastPageNumber);
    void setPageNumber(int pageNumber);
    void setPageSize(int pageSize);
    void setTotalCount(long totalCount);
}
