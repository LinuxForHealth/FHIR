/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.context;

import java.util.List;

import com.ibm.watsonhealth.fhir.core.context.FHIRPagingContext;
import com.ibm.watsonhealth.fhir.search.parameters.InclusionParameter;
import com.ibm.watsonhealth.fhir.search.parameters.Parameter;
import com.ibm.watsonhealth.fhir.search.parameters.SortParameter;

/**
 * Interface for FHIR Search Context. 
 * 
 * @author pbastide
 *
 */
public interface FHIRSearchContext extends FHIRPagingContext {

    List<Parameter> getSearchParameters();

    void setSearchParameters(List<Parameter> searchParameters);

    List<SortParameter> getSortParameters();

    void setSortParameters(List<SortParameter> sortParameters);

    boolean hasSortParameters();

    List<InclusionParameter> getIncludeParameters();

    boolean hasIncludeParameters();

    List<InclusionParameter> getRevIncludeParameters();

    boolean hasRevIncludeParameters();

    /**
     * Get the list of element names requested to be included in the returned resources.
     * 
     * @return a list of strings or null to indicate that there is currently no elements filter associated with the
     *         search
     */
    List<String> getElementsParameters();

    /**
     * Set the list of element names requested to be included in the returned resources. An empty list of strings will
     * indicate that only mandatory elements should be included in the search result. Null is used to represent "no
     * filter".
     */
    void setElementsParameters(List<String> elementsToInclude);

    /**
     * @return true when the elements parameter is not null
     */
    boolean hasElementsParameters();

    /**
     * @param elementToInclude
     * @return
     */
    void addElementsParameter(String elementToInclude);
}
