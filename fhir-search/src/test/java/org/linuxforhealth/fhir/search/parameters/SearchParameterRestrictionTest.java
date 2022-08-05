/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.search.parameters;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.exception.FHIRException;
import org.linuxforhealth.fhir.model.resource.CarePlan;
import org.linuxforhealth.fhir.model.resource.ExplanationOfBenefit;
import org.linuxforhealth.fhir.model.resource.MedicationRequest;
import org.linuxforhealth.fhir.model.resource.Organization;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.resource.Person;
import org.linuxforhealth.fhir.model.resource.Practitioner;
import org.linuxforhealth.fhir.model.resource.RelatedPerson;
import org.linuxforhealth.fhir.search.exception.FHIRSearchException;
import org.linuxforhealth.fhir.search.test.BaseSearchTest;

/**
 * Tests the detection of search restrictions defined by a SearchParameter.
 */
public class SearchParameterRestrictionTest extends BaseSearchTest {

    private static final String DEFAULT_TENANT_ID = "default";
    private static final String TENANT_ID = "tenant7";

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

        searchHelper.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testMultipleOrDisllowed() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("multiple-birth-count-basic", Collections.singletonList("eq2,eq3"));

        searchHelper.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test
    public void testMultipleAndAllowed() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("multiple-birth-count", Arrays.asList("eq2","eq3"));

        searchHelper.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testMultipleAndDisallowed() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("multiple-birth-count-basic", Arrays.asList("eq2","eq3"));

        searchHelper.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test
    public void testComparatorAllowed() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("multiple-birth-count", Collections.singletonList("eq2"));

        searchHelper.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testComparatorDisallowed() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("multiple-birth-count-basic", Collections.singletonList("eq2"));

        searchHelper.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test
    public void testOtherComparatorDisallowed() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("multiple-birth-count-basic", Collections.singletonList("gt2"));

        searchHelper.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test
    public void testModifierAllowed() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("multiple-birth-count:missing", Collections.singletonList("true"));

        searchHelper.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testModifierDisallowed() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("multiple-birth-count-basic:missing", Collections.singletonList("true"));

        searchHelper.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test
    public void testIncludeAllowedByDefault() throws Exception {
        setRequestContext(DEFAULT_TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_include", Collections.singletonList("Person:organization"));

        searchHelper.parseQueryParameters(Person.class, queryParameters);
    }

    @Test
    public void testIncludeAllowedWithOptionalTargetTypeNotSpecified() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_include", Collections.singletonList("Patient:general-practitioner"));

        searchHelper.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test
    public void testIncludeAllowedWithOptionalTargetTypeSpecified() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_include", Collections.singletonList("Patient:general-practitioner:Practitioner"));

        searchHelper.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test
    public void testIncludeAllowedWithRequiredTargetTypeSpecified() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_include", Collections.singletonList("ExplanationOfBenefit:care-team:Practitioner"));

        searchHelper.parseQueryParameters(ExplanationOfBenefit.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testIncludeNotAllowedWithRequiredTargetTypeNotSpecified() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_include", Collections.singletonList("ExplanationOfBenefit:care-team"));

        searchHelper.parseQueryParameters(ExplanationOfBenefit.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testIncludeNotAllowedWithRequiredTargetTypeNotMatched() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_include", Collections.singletonList("ExplanationOfBenefit:care-team:Organization"));

        searchHelper.parseQueryParameters(ExplanationOfBenefit.class, queryParameters);
    }

    @Test
    public void testIncludeWildcardAllowed() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_include", Collections.singletonList("ExplanationOfBenefit:*"));

        searchHelper.parseQueryParameters(ExplanationOfBenefit.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testIncludeWildcardNotAllowed() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_include", Collections.singletonList("Patient:*"));

        searchHelper.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test
    public void testIncludeAllowedByBaseResource() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_include", Collections.singletonList("MedicationRequest:patient"));

        searchHelper.parseQueryParameters(MedicationRequest.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testIncludeDisallowed() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_include", Collections.singletonList("Patient:organization"));

        searchHelper.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testIncludeDisallowedByBaseResource() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_include", Collections.singletonList("Person:organization"));

        searchHelper.parseQueryParameters(Person.class, queryParameters);
    }

    @Test
    public void testRevIncludeAllowedByDefault() throws Exception {
        setRequestContext(DEFAULT_TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_revinclude", Collections.singletonList("Person:organization"));

        searchHelper.parseQueryParameters(Organization.class, queryParameters);
    }

    @Test
    public void testRevIncludeAllowedWithOptionalTargetTypeNotSpecified() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_revinclude", Collections.singletonList("MedicationRequest:intended-performer"));

        searchHelper.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test
    public void testRevIncludeAllowedWithOptionalTargetTypeSpecified() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_revinclude", Collections.singletonList("MedicationRequest:intended-performer:Patient"));

        searchHelper.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test
    public void testRevIncludeAllowedWithRequiredTargetTypeSpecified() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_revinclude", Collections.singletonList("ExplanationOfBenefit:payee:Patient"));

        searchHelper.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testRevIncludeNotAllowedWithRequiredTargetTypeNotSpecified() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_revinclude", Collections.singletonList("ExplanationOfBenefit:payee"));

        searchHelper.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testRevIncludeNotAllowedWithRequiredTargetTypeNotMatched() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_revinclude", Collections.singletonList("ExplanationOfBenefit:payee:Practitioner"));

        searchHelper.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test
    public void testRevIncludeAllowedByBaseResource() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_revinclude", Collections.singletonList("Provenance:target"));

        searchHelper.parseQueryParameters(Person.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testRevIncludeDisallowed() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_revinclude", Collections.singletonList("MedicationRequest:requester"));

        searchHelper.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testRevIncludeDisallowedByBaseResource() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_revinclude", Collections.singletonList("MedicationRequest:intended-performer"));

        searchHelper.parseQueryParameters(Practitioner.class, queryParameters);
    }

    @Test
    public void testEmptySearchParamAllowedByDefault() throws Exception {
        setRequestContext(DEFAULT_TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();

        searchHelper.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test
    public void testEmptySearchParamAllowedByBaseResource() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();

        searchHelper.parseQueryParameters(ExplanationOfBenefit.class, queryParameters);
    }

    @Test
    public void testEmptySearchParamAllowed() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();

        searchHelper.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testEmptySearchParamDisallowed() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();

        searchHelper.parseQueryParameters(CarePlan.class, queryParameters);
    }

    @Test
    public void testSearchParamCombinationAllowedByDefault() throws Exception {
        setRequestContext(DEFAULT_TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_id", Collections.singletonList("abcd-1234"));
        queryParameters.put("active", Collections.singletonList("true"));

        searchHelper.parseQueryParameters(Patient.class, queryParameters);
    }

    @Test
    public void testSearchParamCombinationAllowedByBaseResource() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_id", Collections.singletonList("abcd-1234"));

        searchHelper.parseQueryParameters(ExplanationOfBenefit.class, queryParameters);
    }

    @Test
    public void testSearchParamCombinationAllowed() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("patient", Collections.singletonList("Patient/abcd-1234"));
        queryParameters.put("category", Collections.singletonList("system|code"));

        searchHelper.parseQueryParameters(CarePlan.class, queryParameters);
    }

    @Test
    public void testAnySearchParamCombinationAllowed() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_id", Collections.singletonList("abcd-1234"));
        queryParameters.put("active", Collections.singletonList("true"));

        searchHelper.parseQueryParameters(RelatedPerson.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testSearchParamCombinationDisallowedByBaseResource() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("status", Collections.singletonList("active"));

        searchHelper.parseQueryParameters(ExplanationOfBenefit.class, queryParameters);
    }

    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testSearchParamCombinationDisallowed() throws Exception {
        setRequestContext(TENANT_ID);

        Map<String, List<String>> queryParameters = new HashMap<>();
        queryParameters.put("_id", Collections.singletonList("abcd-1234"));
        queryParameters.put("active", Collections.singletonList("true"));

        searchHelper.parseQueryParameters(Patient.class, queryParameters);
    }
}