/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.core.context.impl;

import com.ibm.fhir.core.FHIRConstants;
import com.ibm.fhir.core.context.FHIRPagingContext;

public class FHIRPagingContextImpl implements FHIRPagingContext {
    protected static final int DEFAULT_LAST_PAGE_NUMBER = Integer.MAX_VALUE;

    protected int lastPageNumber;
    protected int pageNumber;
    protected int pageSize;
    protected Integer totalCount;
    protected int matchCount;
    protected boolean lenient = true;

    /**
     * Create a FHIRPagingContextImpl with the default values:
     * <pre>
     * page number: 1
     * page size: 10
     * last page: 214748364
     * </pre>
     */
    public FHIRPagingContextImpl() {
        this.pageNumber = FHIRConstants.FHIR_PAGE_NUMBER_DEFAULT;
        this.pageSize = FHIRConstants.FHIR_PAGE_SIZE_DEFAULT;
        this.lastPageNumber = DEFAULT_LAST_PAGE_NUMBER;
    }

    @Override
    public int getLastPageNumber() {
        return lastPageNumber;
    }

    @Override
    public int getPageNumber() {
        return pageNumber;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public Integer getTotalCount() {
        return totalCount;
    }

    @Override
    public int getMatchCount() {
        return matchCount;
    }

    @Override
    public void setLastPageNumber(int lastPageNumber) {
        this.lastPageNumber = lastPageNumber;
    }

    @Override
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    @Override
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public void setMatchCount(int matchCount) {
        this.matchCount = matchCount;
    }

    @Override
    public boolean isLenient() {
        return lenient;
    }

    @Override
    public void setLenient(boolean lenient) {
        this.lenient = lenient;
    }
}
