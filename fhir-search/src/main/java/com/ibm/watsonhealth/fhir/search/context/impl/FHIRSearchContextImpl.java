/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.context.impl;

import java.util.List;

import com.ibm.watsonhealth.fhir.search.Parameter;
import com.ibm.watsonhealth.fhir.search.context.FHIRSearchContext;

public class FHIRSearchContextImpl implements FHIRSearchContext {
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DEFAULT_PAGE_NUMBER = 1;
    
    private int lastPageNumber;
    private int pageNumber;
    private int pageSize;
    private List<Parameter> searchParameters = null;
    private long totalCount;
    
    public FHIRSearchContextImpl() {
        this(null);
    }
    
    public FHIRSearchContextImpl(List<Parameter> searchParameters) {
        this.pageNumber = DEFAULT_PAGE_NUMBER;
        this.pageSize = DEFAULT_PAGE_SIZE;
        this.searchParameters = searchParameters;
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
    public List<Parameter> getSearchParameters() {
        return searchParameters;
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
    public void setSearchParameters(List<Parameter> searchParameters) {
        this.searchParameters = searchParameters;
    }

    @Override
    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
}
