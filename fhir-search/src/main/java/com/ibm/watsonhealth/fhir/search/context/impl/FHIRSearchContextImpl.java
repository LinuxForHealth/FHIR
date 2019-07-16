/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.context.impl;

import java.util.ArrayList;
import java.util.List;

import com.ibm.watsonhealth.fhir.core.context.impl.FHIRPagingContextImpl;
import com.ibm.watsonhealth.fhir.search.context.FHIRSearchContext;
import com.ibm.watsonhealth.fhir.search.parameters.InclusionParameter;
import com.ibm.watsonhealth.fhir.search.parameters.Parameter;
import com.ibm.watsonhealth.fhir.search.parameters.SortParameter;

public class FHIRSearchContextImpl extends FHIRPagingContextImpl implements FHIRSearchContext {
    
    private List<Parameter> searchParameters = new ArrayList<>(); 
    private List<SortParameter> sortParameters = new ArrayList<>();
    private List<InclusionParameter> includeParameters = new ArrayList<>();
    private List<InclusionParameter> revIncludeParameters = new ArrayList<>();
    private List<String> elementsParameters = null;
    
    public FHIRSearchContextImpl() {
        searchParameters = new ArrayList<>();
    }

    @Override
    public List<Parameter> getSearchParameters() {
        return searchParameters;
    }

    @Override
    public void setSearchParameters(List<Parameter> searchParameters) {
        this.searchParameters = searchParameters;
    }

    @Override
    public List<SortParameter> getSortParameters() {
        return this.sortParameters;
    }

    @Override
    public void setSortParameters(List<SortParameter> sortParameters) {
        this.sortParameters = sortParameters;
    }

    @Override
    public boolean hasSortParameters() {
        
        return this.sortParameters != null && !this.sortParameters.isEmpty();
    }

    @Override
    public List<InclusionParameter> getIncludeParameters() {
        return this.includeParameters;
    }

    @Override
    public boolean hasIncludeParameters() {
        return this.includeParameters != null && !this.includeParameters.isEmpty();
    }
    
    @Override
    public List<InclusionParameter> getRevIncludeParameters() {
        return this.revIncludeParameters;
    }

    @Override
    public boolean hasRevIncludeParameters() {
        return this.revIncludeParameters != null && !this.revIncludeParameters.isEmpty();
    }

    @Override
    public List<String> getElementsParameters() {
        return this.elementsParameters;
    }

    @Override
    public void setElementsParameters(List<String> elementsParameters) {
        this.elementsParameters = elementsParameters;
    }
    

    @Override
    public void addElementsParameter(String elementToInclude) {
        if (elementsParameters == null) {
            elementsParameters = new ArrayList<>();
        }
        elementsParameters.add(elementToInclude);
    }

    @Override
    public boolean hasElementsParameters() {
        return this.getElementsParameters() != null;
    }

    @Override
    public String toString() {
        return "FHIRSearchContextImpl [searchParameters=" + searchParameters + ", sortParameters=" + sortParameters + ", includeParameters=" + includeParameters
                + ", revIncludeParameters=" + revIncludeParameters + ", elementsParameters=" + elementsParameters + "]";
    }
}
