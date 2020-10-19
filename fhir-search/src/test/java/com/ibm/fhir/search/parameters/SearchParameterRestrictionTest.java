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
import com.ibm.fhir.model.resource.ExplanationOfBenefit;
import com.ibm.fhir.model.resource.MedicationRequest;
import com.ibm.fhir.model.resource.Organization;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Person;
import com.ibm.fhir.model.resource.RelatedPerson;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.test.BaseSearchTest;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * Tests the detection of search restrictions defined by a SearchParameter.
 */
public class SearchParameterRestrictionTest extends BaseSearchTest {

    private static final String DEFAULT_TENANT_ID = "default";
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
        
    @Test
    public void testIncludeAllowedByDefault() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext(DEFAULT_TENANT_ID));

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_include", Collections.singletonList("Person:organization"));
        
        SearchUtil.parseQueryParameters(Person.class, queryParameters);
    }
    
    @Test
    public void testIncludeAllowed() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext(TENANT_ID));

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_include", Collections.singletonList("Patient:general-practitioner"));
        
        SearchUtil.parseQueryParameters(Patient.class, queryParameters);
    }
    
    @Test
    public void testIncludeWildcardAllowed() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext(TENANT_ID));

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_include", Collections.singletonList("ExplanationOfBenefit:*"));
        
        SearchUtil.parseQueryParameters(ExplanationOfBenefit.class, queryParameters);
    }
    
    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testIncludeWildcardNotAllowed() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext(TENANT_ID));

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_include", Collections.singletonList("Patient:*"));
        
        SearchUtil.parseQueryParameters(Patient.class, queryParameters);
    }
        
    @Test
    public void testIncludeAllowedByBaseResource() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext(TENANT_ID));

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_include", Collections.singletonList("MedicationRequest:patient"));
        
        SearchUtil.parseQueryParameters(MedicationRequest.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testIncludeDisallowed() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext(TENANT_ID));

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_include", Collections.singletonList("Patient:organization"));

        SearchUtil.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testIncludeDisallowedByBaseResource() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext(TENANT_ID));

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_include", Collections.singletonList("Person:organization"));

        SearchUtil.parseQueryParameters(Person.class, queryParameters);
    }

    @Test
    public void testRevIncludeAllowedByDefault() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext(DEFAULT_TENANT_ID));

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_revinclude", Collections.singletonList("Person:organization"));
        
        SearchUtil.parseQueryParameters(Organization.class, queryParameters);
    }
    
    @Test
    public void testRevIncludeAllowed() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext(TENANT_ID));

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_revinclude", Collections.singletonList("MedicationRequest:intended-performer"));
        
        SearchUtil.parseQueryParameters(Patient.class, queryParameters);
    }
        
    @Test
    public void testRevIncludeAllowedByBaseResource() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext(TENANT_ID));

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_revinclude", Collections.singletonList("Provenance:target"));
        
        SearchUtil.parseQueryParameters(Person.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testRevIncludeDisallowed() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext(TENANT_ID));

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_revinclude", Collections.singletonList("MedicationRequest:requester"));

        SearchUtil.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testRevIncludeDisallowedByBaseResource() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext(TENANT_ID));

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_revinclude", Collections.singletonList("MedicationRequest:intended-performer"));

        SearchUtil.parseQueryParameters(RelatedPerson.class, queryParameters);
    }
}
