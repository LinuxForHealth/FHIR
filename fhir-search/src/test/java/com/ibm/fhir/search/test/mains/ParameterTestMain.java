/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.test.mains;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.test.BaseSearchTest;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * from prior to r4
 * 
 * 
 * @author others
 * @author pbastide@us.ibm.com 
 *
 */
public class ParameterTestMain extends BaseSearchTest {
    
    /**
     * main entry point for the test-utility 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {        
        Class<? extends Resource> resourceType = Observation.class;
        Map<String, List<String>> queryParameters = new HashMap<String, List<String>>();
        queryParameters.put("subject", Collections.singletonList("http://localhost:9080/fhir-server/api/v4/Patient/1234"));
        for (QueryParameter parameter : SearchUtil.parseQueryParameters(resourceType, queryParameters).getSearchParameters()) {
            System.out.println(parameter);
            System.out.println("");
        }
    }
}
