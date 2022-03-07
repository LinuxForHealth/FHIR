/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.uri.test;

import static org.testng.Assert.assertEquals;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.testng.annotations.Test;

import com.ibm.fhir.search.SearchConstants.Type;
import com.ibm.fhir.search.TotalValueSet;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.context.FHIRSearchContextFactory;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.parameters.QueryParameterValue;
import com.ibm.fhir.search.util.SearchHelper;

/**
 * Test class for the URI Builder
 */
public class UriTest {

    @Test
    public void testUriTrimmedUrl() throws URISyntaxException {
        String incoming =
                "https://localhost:9443/fhir-server/api/v4/_search?_count=10&_security=http://ibm.com/fhir/security&_fudge=tag&_page=1";
        String requestUriString = incoming.split("\\?")[0];

        QueryParameterValue value = new QueryParameterValue();
        value.setValueString("http://ibm.com/fhir/security");
        List<QueryParameterValue> values = Arrays.asList(value);
        QueryParameter parameter = new QueryParameter(Type.TOKEN, "_security", null, null, values);

        List<QueryParameter> searchParameters = new ArrayList<>();

        searchParameters.add(parameter);

        QueryParameterValue value2 = new QueryParameterValue();
        value2.setValueString("tag");
        List<QueryParameterValue> values2 = Arrays.asList(value2);
        QueryParameter parameter2 = new QueryParameter(Type.TOKEN, "_fudge", null, null, values2);
        searchParameters.add(parameter2);


        FHIRSearchContext ctx = FHIRSearchContextFactory.createSearchContext();
        ctx.setPageNumber(1);
        ctx.setPageSize(10);
        ctx.setSearchParameters(searchParameters);

        assertEquals(SearchHelper.buildSearchSelfUri(requestUriString, ctx),
            incoming);
    }

    @Test
    public void testUriBadSecurity() throws URISyntaxException {
        String incoming =
                "https://localhost:9443/fhir-server/api/v4/_search?_count=10&_security=http://ibm.com/fhir/security&_fudge=tag&_page=1";
        String requestUriString = incoming;

        QueryParameterValue value = new QueryParameterValue();
        value.setValueString("http://ibm.com/fhir/security");
        List<QueryParameterValue> values = Arrays.asList(value);
        QueryParameter parameter = new QueryParameter(Type.TOKEN, "_security", null, null, values);

        List<QueryParameter> searchParameters = new ArrayList<>();

        searchParameters.add(parameter);

        QueryParameterValue value2 = new QueryParameterValue();
        value2.setValueString("tag");
        List<QueryParameterValue> values2 = Arrays.asList(value2);
        QueryParameter parameter2 = new QueryParameter(Type.TOKEN, "_fudge", null, null, values2);
        searchParameters.add(parameter2);


        FHIRSearchContext ctx = FHIRSearchContextFactory.createSearchContext();
        ctx.setPageNumber(1);
        ctx.setPageSize(10);
        ctx.setSearchParameters(searchParameters);

        assertEquals(SearchHelper.buildSearchSelfUri(requestUriString, ctx),
            incoming);
    }

    @Test
    public void testUriWithOnlyCompartmentInclusionSearchParmeter() throws URISyntaxException {
        String expectedUri = "https://localhost:9443/fhir-server/api/v4/Patient/1234/Observation?_count=10&_page=1";
        String requestUriString = expectedUri.split("\\?")[0];

        FHIRSearchContext ctx = FHIRSearchContextFactory.createSearchContext();
        ctx.setPageNumber(1);
        ctx.setPageSize(10);
        QueryParameter inclusionParameter = new QueryParameter(Type.REFERENCE, null, null, null, true);
        QueryParameterValue value = new QueryParameterValue();
        value.setValueString("Patient/1234");
        inclusionParameter.getValues().add(value);
        ctx.setSearchParameters(Collections.singletonList(inclusionParameter));

        assertEquals(SearchHelper.buildSearchSelfUri(requestUriString, ctx), expectedUri);
    }

    @Test
    public void testUriWithUnencodedPipe() throws URISyntaxException {
        String expectedUri = "https://test?_count=10&param=system%7Cvalue&_page=1";
        String requestUriString = "https://test?param=system|value";

        FHIRSearchContext ctx = FHIRSearchContextFactory.createSearchContext();
        ctx.setPageNumber(1);
        ctx.setPageSize(10);
        QueryParameterValue paramVal = new QueryParameterValue();
        paramVal.setValueSystem("system");
        paramVal.setValueCode("value");
        QueryParameter queryParameter = new QueryParameter(Type.TOKEN, "param", null, null, Collections.singletonList(paramVal));
        ctx.setSearchParameters(Collections.singletonList(queryParameter));

        assertEquals(SearchHelper.buildSearchSelfUri(requestUriString, ctx), expectedUri);
    }

    @Test
    public void testUriWithTotalParameter() throws URISyntaxException {
        String expectedUri = "https://test?_count=10&_total=none&_page=1";
        String requestUriString = "https://test?_total=none";

        FHIRSearchContext ctx = FHIRSearchContextFactory.createSearchContext();
        ctx.setPageNumber(1);
        ctx.setPageSize(10);
        ctx.setTotalParameter(TotalValueSet.NONE);

        assertEquals(SearchHelper.buildSearchSelfUri(requestUriString, ctx), expectedUri);
    }

    @Test
    public void testUriWithSystemOnly() throws URISyntaxException {
        String expectedUri = "https://test?_count=10&param=system%7C&_page=1";
        String requestUriString = "https://test?param=system|";

        FHIRSearchContext ctx = FHIRSearchContextFactory.createSearchContext();
        ctx.setPageNumber(1);
        ctx.setPageSize(10);
        QueryParameterValue paramVal = new QueryParameterValue();
        paramVal.setValueSystem("system");
        QueryParameter queryParameter = new QueryParameter(Type.TOKEN, "param", null, null, Collections.singletonList(paramVal));
        ctx.setSearchParameters(Collections.singletonList(queryParameter));

        assertEquals(SearchHelper.buildSearchSelfUri(requestUriString, ctx), expectedUri);
    }

    @Test
    public void testUriWithDateTimeWithTimeZone() throws URISyntaxException {
        String expectedUri = "https://test?_count=10&date=gt1940-10-04T00:00:00%2B00:00&_page=1";
        String requestUriString = "https://test?date=gt1940-10-04T00:00:00+00:00";
        String val = "gt1940-10-04T00:00:00+00:00";

        FHIRSearchContext ctx = FHIRSearchContextFactory.createSearchContext();
        ctx.setPageNumber(1);
        ctx.setPageSize(10);
        QueryParameterValue paramVal = new QueryParameterValue();
        paramVal.setValueDate(val);

        QueryParameter queryParameter = new QueryParameter(Type.DATE, "date", null, null, Collections.singletonList(paramVal));
        ctx.setSearchParameters(Collections.singletonList(queryParameter));

        assertEquals(SearchHelper.buildSearchSelfUri(requestUriString, ctx), expectedUri);
    }

    @Test
    public void testUriWithMultipleDateTimeWithTimeZone() throws URISyntaxException {
        String expectedUri = "https://test?_count=10&date1=gt1940-10-04T00:00:00%2B00:00&date2=lt1949-10-04T00:00:00%2B00:00&_page=1";
        String requestUriString = "https://test?date1=gt1940-10-04T00:00:00+00:00&date2=lt1949-10-04T00:00:00+00:00";
        String val1 = "gt1940-10-04T00:00:00+00:00";
        String val2 = "lt1949-10-04T00:00:00+00:00";

        FHIRSearchContext ctx = FHIRSearchContextFactory.createSearchContext();
        ctx.setPageNumber(1);
        ctx.setPageSize(10);
        QueryParameterValue paramVal1 = new QueryParameterValue();
        paramVal1.setValueDate(val1);

        QueryParameterValue paramVal2 = new QueryParameterValue();
        paramVal2.setValueDate(val2);

        QueryParameter queryParameter1 = new QueryParameter(Type.DATE, "date1", null, null, Arrays.asList(paramVal1));
        QueryParameter queryParameter2 = new QueryParameter(Type.DATE, "date2", null, null, Arrays.asList(paramVal2));
        ctx.setSearchParameters(Arrays.asList(queryParameter1, queryParameter2));

        assertEquals(SearchHelper.buildSearchSelfUri(requestUriString, ctx), expectedUri);
    }
}