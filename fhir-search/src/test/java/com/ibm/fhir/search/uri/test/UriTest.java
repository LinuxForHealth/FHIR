/*
 * (C) Copyright IBM Corp. 2019, 2021
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
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.context.FHIRSearchContextFactory;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.parameters.QueryParameterValue;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * Test class for the URI Builder
 *
 * @author pbastide
 *
 */
public class UriTest {

    @Test
    public void testUriBadSecurity() throws URISyntaxException {
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

        assertEquals(SearchUtil.buildSearchSelfUri(requestUriString, ctx),
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

        assertEquals(SearchUtil.buildSearchSelfUri(requestUriString, ctx), expectedUri);
    }

}
