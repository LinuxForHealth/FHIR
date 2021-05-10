/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.context;

import java.util.List;

import com.ibm.fhir.core.context.FHIRPagingContext;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.search.SummaryValueSet;
import com.ibm.fhir.search.TotalValueSet;
import com.ibm.fhir.search.parameters.InclusionParameter;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.parameters.SortParameter;

/**
 * Interface for FHIR Search Context.
 */
public interface FHIRSearchContext extends FHIRPagingContext {

    List<String> getSearchResourceTypes();

    void setSearchResourceTypes(List<String> searchResourceTypes);

    List<QueryParameter> getSearchParameters();

    void setSearchParameters(List<QueryParameter> searchParameters);

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
     */
    void addElementsParameter(String elementToInclude);

    /**
     * @return true when the summary parameter is not null
     */
    boolean hasSummaryParameter();

    /**
     * Get the summary parameter.
     *
     * @return the value of the summary parameter
     */
    SummaryValueSet getSummaryParameter();

    /**
     * Set the value of the summary parameter.
     * @param summary the value
     */
    void setSummaryParameter(SummaryValueSet summary);

    /**
     * @return true when the total parameter is not null
     */
    boolean hasTotalParameter();

    /**
     * Get the total parameter.
     *
     * @return the value of the total parameter
     */
    TotalValueSet getTotalParameter();

    /**
     * Set the value of the total parameter.
     * @param total the value
     */
    void setTotalParameter(TotalValueSet total);

    /**
     * Get the list of issues to be returned in the search outcome.
     * @return a list of issues to be returned in the search outcome
     */
    List<Issue> getOutcomeIssues();

    /**
     * Adds an issue the list of issues to be returned in the search outcome.
     * @param outcomeIssue the issue
     */
    void addOutcomeIssue(Issue outcomeIssue);

}
