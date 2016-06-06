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

    private List<Parameter> searchParameters = null;
    
    public FHIRSearchContextImpl(List<Parameter> searchParameters) {
        this.searchParameters = searchParameters;
    }
    
    @Override
    public List<Parameter> getSearchParameters() {
        return searchParameters;
    }
}
