/*
 * (C) Copyright IBM Corp. 2020, 2021
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
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.model.resource.CarePlan;
import com.ibm.fhir.model.resource.ExplanationOfBenefit;
import com.ibm.fhir.model.resource.MedicationRequest;
import com.ibm.fhir.model.resource.Organization;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Person;
import com.ibm.fhir.model.resource.Practitioner;
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

    private void setRequestContext(String tenant) throws FHIRException {
        // Inject a reasonable uri into the request context - it gets used to
        // calculate the service base address which is used when processing
        // reference params
        FHIRRequestContext context = new FHIRRequestContext(tenant);
        context.setOriginalRequestUri(BASE);
        FHIRRequestContext.set(context);

    }

    @Test
    public void testMultipleOrAllowed() throws Exception {

        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("multiple-birth-count", Collections.singletonList("eq2,eq3,eq4"));

        SearchUtil.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testMultipleOrDisllowed() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("multiple-birth-count-basic", Collections.singletonList("eq2,eq3"));

        SearchUtil.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test
    public void testMultipleAndAllowed() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("multiple-birth-count", Arrays.asList("eq2","eq3"));

        SearchUtil.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testMultipleAndDisallowed() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("multiple-birth-count-basic", Arrays.asList("eq2","eq3"));

        SearchUtil.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test
    public void testComparatorAllowed() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("multiple-birth-count", Collections.singletonList("eq2"));

        SearchUtil.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testComparatorDisallowed() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("multiple-birth-count-basic", Collections.singletonList("eq2"));

        SearchUtil.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test
    public void testOtherComparatorDisallowed() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("multiple-birth-count-basic", Collections.singletonList("gt2"));

        SearchUtil.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test
    public void testModifierAllowed() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("multiple-birth-count:missing", Collections.singletonList("true"));

        SearchUtil.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testModifierDisallowed() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("multiple-birth-count-basic:missing", Collections.singletonList("true"));

        SearchUtil.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test
    public void testIncludeAllowedByDefault() throws Exception {
        setRequestContext(DEFAULT_TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_include", Collections.singletonList("Person:organization"));

        SearchUtil.parseQueryParameters(Person.class, queryParameters);
    }

    @Test
    public void testIncludeAllowedWithOptionalTargetTypeNotSpecified() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_include", Collections.singletonList("Patient:general-practitioner"));

        SearchUtil.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test
    public void testIncludeAllowedWithOptionalTargetTypeSpecified() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_include", Collections.singletonList("Patient:general-practitioner:Practitioner"));

        SearchUtil.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test
    public void testIncludeAllowedWithRequiredTargetTypeSpecified() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_include", Collections.singletonList("ExplanationOfBenefit:care-team:Practitioner"));

        SearchUtil.parseQueryParameters(ExplanationOfBenefit.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testIncludeNotAllowedWithRequiredTargetTypeNotSpecified() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_include", Collections.singletonList("ExplanationOfBenefit:care-team"));

        SearchUtil.parseQueryParameters(ExplanationOfBenefit.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testIncludeNotAllowedWithRequiredTargetTypeNotMatched() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_include", Collections.singletonList("ExplanationOfBenefit:care-team:Organization"));

        SearchUtil.parseQueryParameters(ExplanationOfBenefit.class, queryParameters);
    }

    @Test
    public void testIncludeWildcardAllowed() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_include", Collections.singletonList("ExplanationOfBenefit:*"));

        SearchUtil.parseQueryParameters(ExplanationOfBenefit.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testIncludeWildcardNotAllowed() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_include", Collections.singletonList("Patient:*"));

        SearchUtil.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test
    public void testIncludeAllowedByBaseResource() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_include", Collections.singletonList("MedicationRequest:patient"));

        SearchUtil.parseQueryParameters(MedicationRequest.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testIncludeDisallowed() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_include", Collections.singletonList("Patient:organization"));

        SearchUtil.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testIncludeDisallowedByBaseResource() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_include", Collections.singletonList("Person:organization"));

        SearchUtil.parseQueryParameters(Person.class, queryParameters);
    }

    @Test
    public void testRevIncludeAllowedByDefault() throws Exception {
        setRequestContext(DEFAULT_TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_revinclude", Collections.singletonList("Person:organization"));

        SearchUtil.parseQueryParameters(Organization.class, queryParameters);
    }

    @Test
    public void testRevIncludeAllowedWithOptionalTargetTypeNotSpecified() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_revinclude", Collections.singletonList("MedicationRequest:intended-performer"));

        SearchUtil.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test
    public void testRevIncludeAllowedWithOptionalTargetTypeSpecified() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_revinclude", Collections.singletonList("MedicationRequest:intended-performer:Patient"));

        SearchUtil.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test
    public void testRevIncludeAllowedWithRequiredTargetTypeSpecified() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_revinclude", Collections.singletonList("ExplanationOfBenefit:payee:Patient"));

        SearchUtil.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testRevIncludeNotAllowedWithRequiredTargetTypeNotSpecified() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_revinclude", Collections.singletonList("ExplanationOfBenefit:payee"));

        SearchUtil.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testRevIncludeNotAllowedWithRequiredTargetTypeNotMatched() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_revinclude", Collections.singletonList("ExplanationOfBenefit:payee:Practitioner"));

        SearchUtil.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test
    public void testRevIncludeAllowedByBaseResource() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_revinclude", Collections.singletonList("Provenance:target"));

        SearchUtil.parseQueryParameters(Person.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testRevIncludeDisallowed() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_revinclude", Collections.singletonList("MedicationRequest:requester"));

        SearchUtil.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testRevIncludeDisallowedByBaseResource() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_revinclude", Collections.singletonList("MedicationRequest:intended-performer"));

        SearchUtil.parseQueryParameters(Practitioner.class, queryParameters);
    }

    @Test
    public void testEmptySearchParamAllowedByDefault() throws Exception {
        setRequestContext(DEFAULT_TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();

        SearchUtil.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test
    public void testEmptySearchParamAllowedByBaseResource() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();

        SearchUtil.parseQueryParameters(ExplanationOfBenefit.class, queryParameters);
    }

    @Test
    public void testEmptySearchParamAllowed() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();

        SearchUtil.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testEmptySearchParamDisallowed() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();

        SearchUtil.parseQueryParameters(CarePlan.class, queryParameters);
    }

    @Test
    public void testSearchParamCombinationAllowedByDefault() throws Exception {
        setRequestContext(DEFAULT_TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_id", Collections.singletonList("abcd-1234"));
        queryParameters.put("active", Collections.singletonList("true"));

        SearchUtil.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test
    public void testSearchParamCombinationAllowedByBaseResource() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_id", Collections.singletonList("abcd-1234"));

        SearchUtil.parseQueryParameters(ExplanationOfBenefit.class, queryParameters);
    }

    @Test
    public void testSearchParamCombinationAllowed() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("patient", Collections.singletonList("Patient/abcd-1234"));
        queryParameters.put("category", Collections.singletonList("system|code"));

        SearchUtil.parseQueryParameters(CarePlan.class, queryParameters);
    }

    @Test
    public void testAnySearchParamCombinationAllowed() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_id", Collections.singletonList("abcd-1234"));
        queryParameters.put("active", Collections.singletonList("true"));

        SearchUtil.parseQueryParameters(RelatedPerson.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testSearchParamCombinationDisallowedByBaseResource() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("status", Collections.singletonList("active"));

        SearchUtil.parseQueryParameters(ExplanationOfBenefit.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testSearchParamCombinationDisallowed() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_id", Collections.singletonList("abcd-1234"));
        queryParameters.put("active", Collections.singletonList("true"));

        SearchUtil.parseQueryParameters(Patient.class, queryParameters);
    }
}