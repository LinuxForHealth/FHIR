/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.context.impl;

import java.util.ArrayList;
import java.util.List;

import com.ibm.fhir.core.context.impl.FHIRPagingContextImpl;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.search.SummaryValueSet;
import com.ibm.fhir.search.TotalValueSet;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.parameters.InclusionParameter;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.parameters.SortParameter;


public class FHIRSearchContextImpl extends FHIRPagingContextImpl implements FHIRSearchContext {
    private List<String> searchResourceTypes = null;
    private List<QueryParameter> searchParameters = new ArrayList<>();
    private List<SortParameter> sortParameters = new ArrayList<>();
    private List<InclusionParameter> includeParameters = new ArrayList<>();
    private List<InclusionParameter> revIncludeParameters = new ArrayList<>();
    private List<String> elementsParameters = null;
    private SummaryValueSet summaryParameter = null;
    private TotalValueSet totalParameter = null;
    private List<Issue> outcomeIssues = null;

    public FHIRSearchContextImpl() {
        searchParameters = new ArrayList<>();
    }

    @Override
    public List<QueryParameter> getSearchParameters() {
        return searchParameters;
    }

    @Override
    public void setSearchParameters(List<QueryParameter> searchParameters) {
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
    public boolean hasSummaryParameter() {
        return this.summaryParameter != null;
    }

    @Override
    public SummaryValueSet getSummaryParameter() {
        return this.summaryParameter;
    }

    @Override
    public void setSummaryParameter(SummaryValueSet summary) {
        this.summaryParameter = summary;
    }

    @Override
    public boolean hasTotalParameter() {
        return this.totalParameter != null;
    }

    @Override
    public TotalValueSet getTotalParameter() {
        return this.totalParameter;
    }

    @Override
    public void setTotalParameter(TotalValueSet total) {
        this.totalParameter = total;
    }

    @Override
    public List<String> getSearchResourceTypes() {
        return this.searchResourceTypes;
    }

    @Override
    public void setSearchResourceTypes(List<String> searchResourceTypes) {
        this.searchResourceTypes = searchResourceTypes;
    }

    @Override
    public List<Issue> getOutcomeIssues() {
        return this.outcomeIssues;
    }

    @Override
    public void addOutcomeIssue(Issue outcomeIssue) {
        if (outcomeIssues == null) {
            outcomeIssues = new ArrayList<>();
        }
        outcomeIssues.add(outcomeIssue);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("FHIRSearchContextImpl [searchResourceTypes=");
        builder.append(searchResourceTypes);
        builder.append(", searchParameters=");
        builder.append(searchParameters);
        builder.append(", sortParameters=");
        builder.append(sortParameters);
        builder.append(", includeParameters=");
        builder.append(includeParameters);
        builder.append(", revIncludeParameters=");
        builder.append(revIncludeParameters);
        builder.append(", elementsParameters=");
        builder.append(elementsParameters);
        builder.append(", summaryParameter=");
        builder.append(summaryParameter);
        builder.append(", totalParameter=");
        builder.append(totalParameter);
        builder.append(", outcomeIssues=");
        builder.append(outcomeIssues);
        builder.append("]");
        return builder.toString();
    }
}
