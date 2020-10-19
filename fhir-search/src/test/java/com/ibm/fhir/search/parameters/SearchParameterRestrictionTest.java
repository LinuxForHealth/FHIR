/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.parameters;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.test.BaseSearchTest;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * Tests the detection of search restrictions defined by a SearchParameter.
 */
public class SearchParameterRestrictionTest extends BaseSearchTest {

    private static final String TENANT_ID = "tenant7";
    
    @Override
    @BeforeClass
    public void setup() {
        FHIRConfiguration.setConfigHome("src/test/resources");
    }

    @Test
    public void testMultipleOrAllowed() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext(TENANT_ID));

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("multiple-birth-count", Collections.singletonList("eq2,eq3,eq4"));
        
        SearchUtil.parseQueryParameters(Patient.class, queryParameters);
    }
    
    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testMultipleOrDisllowed() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext(TENANT_ID));

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("multiple-birth-count-basic", Collections.singletonList("eq2,eq3"));
        
        SearchUtil.parseQueryParameters(Patient.class, queryParameters);
    }
    
    @Test
    public void testMultipleAndAllowed() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext(TENANT_ID));

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("multiple-birth-count", Arrays.asList("eq2","eq3"));
        
        SearchUtil.parseQueryParameters(Patient.class, queryParameters);
    }
    
    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testMultipleAndDisallowed() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext(TENANT_ID));

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("multiple-birth-count-basic", Arrays.asList("eq2","eq3"));
        
        SearchUtil.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test
    public void testComparatorAllowed() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext(TENANT_ID));

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("multiple-birth-count", Collections.singletonList("eq2"));

        SearchUtil.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testComparatorDisallowed() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext(TENANT_ID));

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("multiple-birth-count-basic", Collections.singletonList("eq2"));

        SearchUtil.parseQueryParameters(Patient.class, queryParameters);
    }
    
    @Test
    public void testOtherComparatorDisallowed() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext(TENANT_ID));

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("multiple-birth-count-basic", Collections.singletonList("gt2"));

        SearchUtil.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test
    public void testModifierAllowed() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext(TENANT_ID));

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("multiple-birth-count:missing", Collections.singletonList("true"));
        
        SearchUtil.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testModifierDisallowed() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext(TENANT_ID));

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("multiple-birth-count-basic:missing", Collections.singletonList("true"));

        SearchUtil.parseQueryParameters(Patient.class, queryParameters);
    }

}
