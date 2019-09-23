/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.uri.test;

import static org.testng.Assert.assertEquals;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import com.ibm.fhir.search.SearchConstants.Type;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.context.FHIRSearchContextFactory;
import com.ibm.fhir.search.parameters.Parameter;
import com.ibm.fhir.search.parameters.ParameterValue;
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

        ParameterValue value = new ParameterValue();
        value.setValueString("http://ibm.com/fhir/security");
        List<ParameterValue> values = Arrays.asList(value);
        Parameter parameter = new Parameter(Type.TOKEN, "_security", null, null, values);

        List<Parameter> searchParameters = new ArrayList<>();
        
        searchParameters.add(parameter);
        
        ParameterValue value2 = new ParameterValue();
        value2.setValueString("tag");
        List<ParameterValue> values2 = Arrays.asList(value2);
        Parameter parameter2 = new Parameter(Type.TOKEN, "_fudge", null, null, values2);
        searchParameters.add(parameter2);
        

        FHIRSearchContext ctx = FHIRSearchContextFactory.createSearchContext();
        ctx.setPageNumber(1);
        ctx.setPageSize(10);
        ctx.setSearchParameters(searchParameters);

        assertEquals(SearchUtil.buildSearchSelfUri(requestUriString, ctx), 
            incoming);
    }

}
