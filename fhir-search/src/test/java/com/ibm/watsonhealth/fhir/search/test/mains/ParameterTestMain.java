/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.test.mains;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.watsonhealth.fhir.model.Observation;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.search.Parameter;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;

public class ParameterTestMain {
    
    public static void main(String[] args) throws Exception {        
        Class<? extends Resource> resourceType = Observation.class;
        Map<String, List<String>> queryParameters = new HashMap<String, List<String>>();
//        queryParameters.put("subject", Collections.singletonList("1234"));
//        queryParameters.put("subject", Collections.singletonList("Patient/1234"));
        queryParameters.put("subject", Collections.singletonList("http://localhost:9080/fhir-server/api/v1/Patient/1234"));
        for (Parameter parameter : SearchUtil.parseQueryParameters(resourceType, queryParameters, null).getSearchParameters()) {
            System.out.println(parameter);
            System.out.println("");
        }
    }
}
