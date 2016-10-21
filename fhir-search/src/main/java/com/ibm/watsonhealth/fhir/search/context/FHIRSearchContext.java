/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.context;

import java.util.List;

import com.ibm.watsonhealth.fhir.core.context.FHIRPagingContext;
import com.ibm.watsonhealth.fhir.search.Parameter;
import com.ibm.watsonhealth.fhir.search.SortParameter;

public interface FHIRSearchContext extends FHIRPagingContext {
	
    List<Parameter> getSearchParameters();
    
    void setSearchParameters(List<Parameter> searchParameters);
    
    List<SortParameter> getSortParameters();
    
    void setSortParameters(List<SortParameter> sortParameters);
    
    boolean hasSortParameters();
}
