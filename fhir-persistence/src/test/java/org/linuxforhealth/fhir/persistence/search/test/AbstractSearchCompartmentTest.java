/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.search.test;

import static org.linuxforhealth.fhir.model.test.TestUtil.isResourceInResponse;
import static org.linuxforhealth.fhir.model.type.String.string;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.model.resource.Basic;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.test.TestUtil;
import org.linuxforhealth.fhir.model.type.Reference;

/**
 * Unit tests for compartment-based searches
 * @see https://hl7.org/fhir/r4/search.html
 * GET [base]/Patient/[id]/[type]?parameter(s)
 */
public abstract class AbstractSearchCompartmentTest extends AbstractPLSearchTest {
    private static final String PATIENT = "Patient";
    private static final String PATIENT_ID = "123";

    private static final String PRACTITIONER = "Practitioner";
    private static final String PRACTITIONER_ID = "abc";

    private static final String RELATED_PERSON = "RelatedPerson";
    private static final String OTHER_ID = "xyz";

    @Override
    protected Basic getBasicResource() throws Exception {
        Basic basic = TestUtil.readExampleResource("json/basic/BasicWithAllTypes.json");
        // Add a references to a patient and a practitioner so we can exercise those compartments
        return basic.toBuilder()
                .subject(Reference.builder()
                    .reference(string(PATIENT + "/" + PATIENT_ID))
                    .build())
                .author(Reference.builder()
                    .reference(string(PRACTITIONER + "/" + PRACTITIONER_ID))
                    .build())
                .build();
    }

    @Override
    protected void setTenant() throws Exception {
        FHIRRequestContext.get().setTenantId("compartment");

        // Need to set the original request URI before storing the resource.
        // The server-url is now used to determine if an absolute reference is local (can be served
        // from this FHIR server).
        FHIRRequestContext.get().setOriginalRequestUri("https://example.com/Patient/123");
    }

    @Test
    public void testSearchPatientCompartment_relativeReference_number() throws Exception {
        assertCompartmentSearchReturnsSavedResource(PATIENT, PATIENT_ID, "integer", "12");
        assertCompartmentSearchReturnsSavedResource(PRACTITIONER, PRACTITIONER_ID, "integer", "12");
        assertCompartmentSearchDoesntReturnSavedResource(PATIENT, OTHER_ID, "integer", "12");
        assertCompartmentSearchDoesntReturnSavedResource(RELATED_PERSON, PATIENT_ID, "integer", "12");
    }

    @Test
    public void testSearchPatientCompartment_relativeReference_quantity() throws Exception {
        assertCompartmentSearchReturnsSavedResource(PATIENT, PATIENT_ID, "Quantity", "25|http://unitsofmeasure.org|s");
        assertCompartmentSearchReturnsSavedResource(PRACTITIONER, PRACTITIONER_ID, "Quantity", "25|http://unitsofmeasure.org|s");
        assertCompartmentSearchDoesntReturnSavedResource(PATIENT, OTHER_ID, "Quantity", "25|http://unitsofmeasure.org|s");
        assertCompartmentSearchDoesntReturnSavedResource(RELATED_PERSON, PATIENT_ID, "Quantity", "25|http://unitsofmeasure.org|s");
    }

    @Test
    public void testSearchPatientCompartment_relativeReference_reference() throws Exception {
        // Note that "Reference just happens to be another searchable parameter in the Basic resource.
        assertCompartmentSearchReturnsSavedResource(PATIENT, PATIENT_ID, "Reference", "Patient/123");
        assertCompartmentSearchReturnsSavedResource(PRACTITIONER, PRACTITIONER_ID, "Reference", "Patient/123");
        assertCompartmentSearchDoesntReturnSavedResource(PATIENT, OTHER_ID, "Reference", "Patient/123");
        assertCompartmentSearchDoesntReturnSavedResource(RELATED_PERSON, PATIENT_ID, "Reference", "Patient/123");
    }

    @Test
    public void testSearchPatientCompartment_relativeReference_string() throws Exception {
        assertCompartmentSearchReturnsSavedResource(PATIENT, PATIENT_ID, "string", "testString");
        assertCompartmentSearchReturnsSavedResource(PRACTITIONER, PRACTITIONER_ID, "string", "testString");
        assertCompartmentSearchDoesntReturnSavedResource(PATIENT, OTHER_ID, "string", "testString");
        assertCompartmentSearchDoesntReturnSavedResource(RELATED_PERSON, PATIENT_ID, "string", "testString");
    }

    @Test
    public void testSearchPatientCompartment_relativeReference_string_or() throws Exception {
        assertCompartmentSearchReturnsSavedResource(PATIENT, PATIENT_ID, "string", "testString,otherString");
        assertCompartmentSearchReturnsSavedResource(PRACTITIONER, PRACTITIONER_ID, "string", "testString,otherString");
        assertCompartmentSearchDoesntReturnSavedResource(PATIENT, OTHER_ID, "string", "testString,otherString");
        assertCompartmentSearchDoesntReturnSavedResource(RELATED_PERSON, PATIENT_ID, "string", "testString,otherString");
    }

    @Test
    public void testSearchPatientCompartment_relativeReference_token() throws Exception {
        assertCompartmentSearchReturnsSavedResource(PATIENT, PATIENT_ID, "CodeableConcept", "http://example.org/codesystem|code");
        assertCompartmentSearchReturnsSavedResource(PRACTITIONER, PRACTITIONER_ID, "CodeableConcept", "http://example.org/codesystem|code");
        assertCompartmentSearchDoesntReturnSavedResource(PATIENT, OTHER_ID, "CodeableConcept", "http://example.org/codesystem|code");
        assertCompartmentSearchDoesntReturnSavedResource(RELATED_PERSON, PATIENT_ID, "CodeableConcept", "http://example.org/codesystem|code");
    }

    @Test
    public void testSearchPatientCompartment_relativeReference_uri() throws Exception {
        assertCompartmentSearchReturnsSavedResource(PATIENT, PATIENT_ID, "uri", "urn:uuid:53fefa32-1111-2222-3333-55ee120877b7");
        assertCompartmentSearchReturnsSavedResource(PRACTITIONER, PRACTITIONER_ID, "uri", "urn:uuid:53fefa32-1111-2222-3333-55ee120877b7");
        assertCompartmentSearchDoesntReturnSavedResource(PATIENT, OTHER_ID, "uri", "urn:uuid:53fefa32-1111-2222-3333-55ee120877b7");
        assertCompartmentSearchDoesntReturnSavedResource(RELATED_PERSON, PATIENT_ID, "uri", "urn:uuid:53fefa32-1111-2222-3333-55ee120877b7");
    }

    @Test
    public void testSearchPatientCompartment_relativeReference_chained_number() throws Exception {
        assertCompartmentSearchReturnsComposition(PATIENT, PATIENT_ID, "subject:Basic.integer", "12");
        assertCompartmentSearchReturnsComposition(PRACTITIONER, PRACTITIONER_ID, "subject:Basic.integer", "12");
        assertCompartmentSearchDoesntReturnComposition(PATIENT, OTHER_ID, "subject:Basic.integer", "12");
        assertCompartmentSearchDoesntReturnComposition(RELATED_PERSON, PATIENT_ID, "subject:Basic.integer", "12");
    }

    @Test
    public void testSearchPatientCompartment_relativeReference_chained_quantity() throws Exception {
        assertCompartmentSearchReturnsComposition(PATIENT, PATIENT_ID, "subject:Basic.Quantity", "25|http://unitsofmeasure.org|s");
        assertCompartmentSearchReturnsComposition(PRACTITIONER, PRACTITIONER_ID, "subject:Basic.Quantity", "25|http://unitsofmeasure.org|s");
        assertCompartmentSearchDoesntReturnComposition(PATIENT, OTHER_ID, "subject:Basic.Quantity", "25|http://unitsofmeasure.org|s");
        assertCompartmentSearchDoesntReturnComposition(RELATED_PERSON, PATIENT_ID, "subject:Basic.Quantity", "25|http://unitsofmeasure.org|s");
    }

    @Test
    public void testSearchPatientCompartment_relativeReference_chained_reference() throws Exception {
        // Note that "Reference just happens to be another searchable parameter in the Basic resource.
        assertCompartmentSearchReturnsComposition(PATIENT, PATIENT_ID, "subject:Basic.Reference", "Patient/123");
        assertCompartmentSearchReturnsComposition(PRACTITIONER, PRACTITIONER_ID, "subject:Basic.Reference", "Patient/123");
        assertCompartmentSearchDoesntReturnComposition(PATIENT, OTHER_ID, "subject:Basic.Reference", "Patient/123");
        assertCompartmentSearchDoesntReturnComposition(RELATED_PERSON, PATIENT_ID, "subject:Basic.Reference", "Patient/123");
    }

    @Test
    public void testSearchPatientCompartment_relativeReference_chained_string() throws Exception {
        assertCompartmentSearchReturnsComposition(PATIENT, PATIENT_ID, "subject:Basic.string", "testString");
        assertCompartmentSearchReturnsComposition(PRACTITIONER, PRACTITIONER_ID, "subject:Basic.string", "testString");
        assertCompartmentSearchDoesntReturnComposition(PATIENT, OTHER_ID, "subject:Basic.string", "testString");
        assertCompartmentSearchDoesntReturnComposition(RELATED_PERSON, PATIENT_ID, "subject:Basic.string", "testString");
    }

    @Test
    public void testSearchPatientCompartment_relativeReference_chained_token() throws Exception {
        assertCompartmentSearchReturnsComposition(PATIENT, PATIENT_ID, "subject:Basic.CodeableConcept", "http://example.org/codesystem|code");
        assertCompartmentSearchReturnsComposition(PRACTITIONER, PRACTITIONER_ID, "subject:Basic.CodeableConcept", "http://example.org/codesystem|code");
        assertCompartmentSearchDoesntReturnComposition(PATIENT, OTHER_ID, "subject:Basic.CodeableConcept", "http://example.org/codesystem|code");
        assertCompartmentSearchDoesntReturnComposition(RELATED_PERSON, PATIENT_ID, "subject:Basic.CodeableConcept", "http://example.org/codesystem|code");
    }

    @Test
    public void testSearchPatientCompartment_relativeReference_chained_uri() throws Exception {
        assertCompartmentSearchReturnsComposition(PATIENT, PATIENT_ID, "subject:Basic.uri", "urn:uuid:53fefa32-1111-2222-3333-55ee120877b7");
        assertCompartmentSearchReturnsComposition(PRACTITIONER, PRACTITIONER_ID, "subject:Basic.uri", "urn:uuid:53fefa32-1111-2222-3333-55ee120877b7");
        assertCompartmentSearchDoesntReturnComposition(PATIENT, OTHER_ID, "subject:Basic.uri", "urn:uuid:53fefa32-1111-2222-3333-55ee120877b7");
        assertCompartmentSearchDoesntReturnComposition(RELATED_PERSON, PATIENT_ID, "subject:Basic.uri", "urn:uuid:53fefa32-1111-2222-3333-55ee120877b7");
    }


    protected void assertCompartmentSearchReturnsSavedResource(String compartmentType, String compartmentId,
            String searchParamName, String queryValue) throws Exception {
        assertTrue("Expected resource was not returned from the search",
                compartmentSearchReturnsResource(compartmentType, compartmentId, searchParamName, queryValue, savedResource));
    }

    protected void assertCompartmentSearchDoesntReturnSavedResource(String compartmentType, String compartmentId,
            String searchParamName, String queryValue) throws Exception {
        assertFalse("Unexpected resource was returned from the search",
                compartmentSearchReturnsResource(compartmentType, compartmentId, searchParamName, queryValue, savedResource));
    }

    protected void assertCompartmentSearchReturnsComposition(String compartmentType, String compartmentId,
            String searchParamName, String queryValue) throws Exception {
        assertTrue("Expected resource was not returned from the search",
            compartmentSearchReturnsResource(compartmentType, compartmentId, searchParamName, queryValue, composition));
    }

    protected void assertCompartmentSearchDoesntReturnComposition(String compartmentType, String compartmentId,
            String searchParamName, String queryValue) throws Exception {
        assertFalse("Unexpected resource was returned from the search",
            compartmentSearchReturnsResource(compartmentType, compartmentId, searchParamName, queryValue, composition));
    }

    /**
     * Executes the compartment query and returns whether the expected resource was in the result set
     * @throws Exception
     */
    protected boolean compartmentSearchReturnsResource(String compartmentType, String compartmentId,
            String searchParamCode, String queryValue, Resource expectedResource) throws Exception {
        List<? extends Resource> resources = runCompartmentQueryTest(compartmentType, compartmentId,
                expectedResource.getClass(), searchParamCode, queryValue, Integer.MAX_VALUE - 2);
        assertNotNull(resources);
        return isResourceInResponse(expectedResource, resources);
    }

    @Test
    public void testSearchPatientCompartment_relativeReference_multiCompartment_string() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<>(1);
        queryParms.put("string", Collections.singletonList("testString"));
        assertTrue("Expected resource was not returned from the search",
            multiCompartmentSearchReturnsResource(PATIENT, new HashSet<>(Arrays.asList(new String[]{PATIENT_ID, OTHER_ID})),
                    queryParms, savedResource));
        assertTrue("Expected resource was not returned from the search",
            multiCompartmentSearchReturnsResource(PATIENT, new HashSet<>(Arrays.asList(new String[]{OTHER_ID, PATIENT_ID})),
                    queryParms, savedResource));
        assertFalse("Unexpected resource was returned from the search",
            multiCompartmentSearchReturnsResource(PATIENT, new HashSet<>(Arrays.asList(new String[]{PRACTITIONER_ID, OTHER_ID})),
                    queryParms, savedResource));

        queryParms.clear();
        queryParms.put("subject:Basic.string", Collections.singletonList("testString"));
        assertTrue("Expected resource was not returned from the search",
            multiCompartmentSearchReturnsResource(PATIENT, new HashSet<>(Arrays.asList(new String[]{PATIENT_ID, OTHER_ID})),
                    queryParms, composition));
        assertTrue("Expected resource was not returned from the search",
            multiCompartmentSearchReturnsResource(PATIENT, new HashSet<>(Arrays.asList(new String[]{OTHER_ID, PATIENT_ID})),
                    queryParms, composition));
        assertFalse("Unexpected resource was returned from the search",
            multiCompartmentSearchReturnsResource(PATIENT, new HashSet<>(Arrays.asList(new String[]{PRACTITIONER_ID, OTHER_ID})),
                    queryParms, composition));
    }

    /**
     * Executes the compartment query and returns whether the expected resource was in the result set
     * @throws Exception
     */
    protected boolean multiCompartmentSearchReturnsResource(String compartmentType, Set<String> compartmentIds,
            Map<String, List<String>> queryParms, Resource expectedResource) throws Exception {

        List<? extends Resource> resources = runCompartmentQueryTest(compartmentType, compartmentIds,
                expectedResource.getClass(), queryParms, Integer.MAX_VALUE - 2);
        assertNotNull(resources);
        return isResourceInResponse(expectedResource, resources);
    }
}
