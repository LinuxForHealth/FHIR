/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.core.context.impl;

import com.ibm.watsonhealth.fhir.core.context.FHIRPagingContext;

public class FHIRPagingContextImpl implements FHIRPagingContext {
    protected static final int DEFAULT_PAGE_SIZE = 10;
    protected static final int DEFAULT_PAGE_NUMBER = 1;
    
    protected int lastPageNumber;
    protected int pageNumber;
    protected int pageSize;
    protected long totalCount;
    
    public FHIRPagingContextImpl() {
        this.pageNumber = DEFAULT_PAGE_NUMBER;
        this.pageSize = DEFAULT_PAGE_SIZE;
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
    public long getTotalCount() {
        return totalCount;
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
    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
}
