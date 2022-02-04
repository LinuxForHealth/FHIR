/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.smart.test;

import static com.ibm.fhir.core.ResourceType.CONDITION;
import static com.ibm.fhir.core.ResourceType.OBSERVATION;
import static com.ibm.fhir.core.ResourceType.PATIENT;
import static com.ibm.fhir.core.ResourceType.PROVENANCE;
import static com.ibm.fhir.core.ResourceType.RESOURCE;
import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.core.ResourceType;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Condition;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Practitioner;
import com.ibm.fhir.model.resource.Provenance;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.persistence.context.FHIRPersistenceEvent;
import com.ibm.fhir.search.SearchConstants.Type;
import com.ibm.fhir.search.context.impl.FHIRSearchContextImpl;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.parameters.QueryParameterValue;
import com.ibm.fhir.server.spi.interceptor.FHIRPersistenceInterceptorException;
import com.ibm.fhir.server.spi.operation.FHIROperationContext;
import com.ibm.fhir.smart.AuthzPolicyEnforcementPersistenceInterceptor;
import com.ibm.fhir.smart.Scope.Permission;

public class AuthzPolicyEnforcementTest {
    private static final String PATIENT_ID =     "11111111-1111-1111-1111-111111111111";
    private static final String OBSERVATION_ID = "11111111-1111-1111-1111-111111111111";
    private static final String CONDITION_ID =   "11111111-1111-1111-1111-111111111111";

    private static final List<Permission> READ_APPROVED = Arrays.asList(Permission.READ, Permission.ALL);
    private static final List<Permission> WRITE_APPROVED = Arrays.asList(Permission.WRITE, Permission.ALL);

    private AuthzPolicyEnforcementPersistenceInterceptor interceptor;
    private Patient patient;
    private Observation observation;
    private Condition condition;
    private Provenance patientProvenance;
    private Provenance observationProvenance;
    private Map<String, Object> properties;

    @BeforeClass
    public void setup() throws Exception {
        System.setProperty("java.util.logging.config.file", ClassLoader.getSystemResource("logging.properties").getPath());

        FHIRRequestContext requestContext = new FHIRRequestContext();
        requestContext.setTenantId("default");
        FHIRRequestContext.set(requestContext);

        interceptor = new AuthzPolicyEnforcementPersistenceInterceptor();

        Provenance provenanceBase = TestUtil.getMinimalResource(Provenance.class);

        patient = TestUtil.getMinimalResource(Patient.class);
        patient = patient.toBuilder()
                .id(PATIENT_ID)
                .build();

        patientProvenance = provenanceBase.toBuilder()
                .target(Reference.builder()
                    .reference(string("Patient/" + PATIENT_ID))
                    .build())
                .build();

        observation = TestUtil.getMinimalResource(Observation.class);
        observation = observation.toBuilder()
                .id(OBSERVATION_ID)
                .subject(Reference.builder().reference(string("Patient/" + PATIENT_ID)).build())
                .encounter(Reference.builder().reference(string("Encounter/" + MockPersistenceImpl.ENCOUNTER_ID_GOOD)).build())
                .build();

        observationProvenance = provenanceBase.toBuilder()
                .target(Reference.builder()
                    .reference(string("Observation/" + OBSERVATION_ID))
                    .build())
                .build();

        condition = TestUtil.getMinimalResource(Condition.class);
        condition = condition.toBuilder()
                .subject(Reference.builder()
                    .reference(string("Patient/" + CONDITION_ID + "/_history/1"))
                    .build())
                .build();

        properties = new HashMap<String, Object>();

        MockPersistenceImpl mockPersistenceImpl = new MockPersistenceImpl(patient, observation);
        properties.put(FHIRPersistenceEvent.PROPNAME_PERSISTENCE_IMPL, mockPersistenceImpl);
    }

    @Test(dataProvider = "scopeStringProvider")
    public void testCreate(String scopeString, List<String> contextIds, Set<ResourceType> resourceTypesPermittedByScope, Permission permission) {
        FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders(scopeString, contextIds));

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient");
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
            interceptor.beforeCreate(event);
            assertTrue(shouldSucceed(resourceTypesPermittedByScope, PATIENT, WRITE_APPROVED, permission));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceTypesPermittedByScope, PATIENT, WRITE_APPROVED, permission));
        }

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Observation");
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observation, properties);
            interceptor.beforeCreate(event);
            assertTrue(shouldSucceed(resourceTypesPermittedByScope, OBSERVATION, WRITE_APPROVED, permission));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceTypesPermittedByScope, OBSERVATION, WRITE_APPROVED, permission));
        }

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Condition");
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(condition, properties);
            interceptor.beforeCreate(event);
            assertTrue(shouldSucceed(resourceTypesPermittedByScope, CONDITION, WRITE_APPROVED, permission));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceTypesPermittedByScope, CONDITION, WRITE_APPROVED, permission));
        }
    }

    @Test(dataProvider = "scopeStringProvider")
    public void testUpdate(String scopeString, List<String> contextIds, Set<ResourceType> resourceTypesPermittedByScope, Permission permission) {
        FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders(scopeString, contextIds));

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient");
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
            event.setPrevFhirResource(patient);
            interceptor.beforeUpdate(event);
            assertTrue(shouldSucceed(resourceTypesPermittedByScope, PATIENT, READ_APPROVED, permission) &&
                    shouldSucceed(resourceTypesPermittedByScope, PATIENT, WRITE_APPROVED, permission));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceTypesPermittedByScope, PATIENT, READ_APPROVED, permission) &&
                    shouldSucceed(resourceTypesPermittedByScope, PATIENT, WRITE_APPROVED, permission));
        }

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Observation");
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observation, properties);
            event.setPrevFhirResource(observation);
            interceptor.beforeUpdate(event);
            assertTrue(shouldSucceed(resourceTypesPermittedByScope, OBSERVATION, READ_APPROVED, permission) &&
                        shouldSucceed(resourceTypesPermittedByScope, OBSERVATION, WRITE_APPROVED, permission));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceTypesPermittedByScope, OBSERVATION, READ_APPROVED, permission) &&
                        shouldSucceed(resourceTypesPermittedByScope, OBSERVATION, WRITE_APPROVED, permission));
        }

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Condition");
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(condition, properties);
            event.setPrevFhirResource(condition);
            interceptor.beforeUpdate(event);
            assertTrue(shouldSucceed(resourceTypesPermittedByScope, CONDITION, READ_APPROVED, permission) &&
                        shouldSucceed(resourceTypesPermittedByScope, CONDITION, WRITE_APPROVED, permission));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceTypesPermittedByScope, CONDITION, READ_APPROVED, permission) &&
                        shouldSucceed(resourceTypesPermittedByScope, CONDITION, WRITE_APPROVED, permission));
        }
    }

    @Test(dataProvider = "scopeStringProvider")
    public void testDelete(String scopeString, List<String> contextIds, Set<ResourceType> resourceTypesPermittedByScope, Permission permission) {
        FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders(scopeString, contextIds));

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient");
            FHIRPersistenceEvent event = new FHIRPersistenceEvent();
            event.setPrevFhirResource(patient);
            interceptor.beforeDelete(event);
            assertTrue(shouldSucceed(resourceTypesPermittedByScope, PATIENT, WRITE_APPROVED, permission));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceTypesPermittedByScope, PATIENT, WRITE_APPROVED, permission));
        }

        try {properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Observation");
            FHIRPersistenceEvent event = new FHIRPersistenceEvent();
            event.setPrevFhirResource(observation);
            interceptor.beforeDelete(event);
            assertTrue(shouldSucceed(resourceTypesPermittedByScope, OBSERVATION, WRITE_APPROVED, permission));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceTypesPermittedByScope, OBSERVATION, WRITE_APPROVED, permission));
        }

        try {properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Condition");
            FHIRPersistenceEvent event = new FHIRPersistenceEvent();
            event.setPrevFhirResource(condition);
            interceptor.beforeDelete(event);
            assertTrue(shouldSucceed(resourceTypesPermittedByScope, CONDITION, WRITE_APPROVED, permission));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceTypesPermittedByScope, CONDITION, WRITE_APPROVED, permission));
        }
    }

    @Test
    public void testDirectPatientInteraction() throws FHIRPersistenceInterceptorException {
        FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders("patient/Patient.read", PATIENT_ID));

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient");
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID, PATIENT_ID);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
            interceptor.beforeRead(event);
            interceptor.beforeVread(event);
            interceptor.beforeHistory(event);
        } catch (FHIRPersistenceInterceptorException e) {
            fail("Patient interaction was not allowed but should have been", e);
        }

        properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient");
        properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID, "bogus");
        FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);

        try {
            interceptor.beforeRead(event);
            fail("Patient read was allowed but should not be");
        } catch (FHIRPersistenceInterceptorException e) {
            // success
        }

        try {
            interceptor.beforeVread(event);
            fail("Patient vread was allowed but should not be");
        } catch (FHIRPersistenceInterceptorException e) {
            // success
        }

        try {
            interceptor.beforeHistory(event);
            fail("Patient history was allowed but should not be");
        } catch (FHIRPersistenceInterceptorException e) {
            // success
        }
    }

    @Test
    public void testBeforeSearch() throws FHIRPersistenceInterceptorException {
        FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders("patient/Patient.read patient/Observation.read patient/Practitioner.read", PATIENT_ID));
        Practitioner practitioner = null;
        try {
            practitioner = TestUtil.getMinimalResource(Practitioner.class);
        } catch (Exception e) {
            fail("Practitioner resource could not be created");
        }
        QueryParameterValue queryParameterValue = new QueryParameterValue();
        QueryParameter queryParameter = new QueryParameter(Type.REFERENCE, "test", null, null, true);
        queryParameter.getValues().add(queryParameterValue);
        FHIRSearchContextImpl searchContext = new FHIRSearchContextImpl();
        searchContext.getSearchParameters().add(queryParameter);

        // Valid compartment search
        try {
            queryParameterValue.setValueString("Patient/" + PATIENT_ID);
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Observation");
            properties.put(FHIRPersistenceEvent.PROPNAME_SEARCH_CONTEXT_IMPL, searchContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observation, properties);
            interceptor.beforeSearch(event);
        } catch (FHIRPersistenceInterceptorException e) {
            fail("Patient compartment interaction was not allowed but should have been", e);
        }

        // Invalid compartment search: wrong Patient compartment
        try {
            queryParameterValue.setValueString("Patient/bogus");
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Observation");
            properties.put(FHIRPersistenceEvent.PROPNAME_SEARCH_CONTEXT_IMPL, searchContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observation, properties);
            interceptor.beforeSearch(event);
            fail("Patient compartment interaction was allowed but should not be");
        } catch (FHIRPersistenceInterceptorException e) {
            // success
            assertEquals(1, e.getIssues().size());
            assertEquals(IssueType.FORBIDDEN, e.getIssues().get(0).getCode());
            assertEquals(e.getIssues().get(0).getDetails().getText().getValue(),
                    "Interaction with 'Patient/bogus' is not permitted for patient context [" + PATIENT_ID + "]");
        }

        // Valid compartment search: Encounter in Patient compartment
        try {
            queryParameterValue.setValueString("Encounter/" + MockPersistenceImpl.ENCOUNTER_ID_GOOD);
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Observation");
            properties.put(FHIRPersistenceEvent.PROPNAME_SEARCH_CONTEXT_IMPL, searchContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observation, properties);
            interceptor.beforeSearch(event);
        } catch (FHIRPersistenceInterceptorException e) {
            fail("Patient compartment interaction was not allowed but should have been", e);
        }

        // Invalid compartment search: Encounter not in Patient compartment
        try {
            queryParameterValue.setValueString("Encounter/" + MockPersistenceImpl.ENCOUNTER_ID_BAD);
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Observation");
            properties.put(FHIRPersistenceEvent.PROPNAME_SEARCH_CONTEXT_IMPL, searchContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observation, properties);
            interceptor.beforeSearch(event);
            fail("Patient compartment interaction was allowed but should not be");
        } catch (FHIRPersistenceInterceptorException e) {
            // success
            assertEquals(e.getIssues().size(), 1);
            assertEquals(e.getIssues().get(0).getCode(), IssueType.FORBIDDEN);
            assertEquals(e.getIssues().get(0).getDetails().getText().getValue(),
                "Interaction with 'Encounter/" + MockPersistenceImpl.ENCOUNTER_ID_BAD + "' is not permitted for patient context [" + PATIENT_ID + "]");
        }

        // Invalid compartment search: Encounter compartment resource does not exist
        try {
            queryParameterValue.setValueString("Encounter/bogus");
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Observation");
            properties.put(FHIRPersistenceEvent.PROPNAME_SEARCH_CONTEXT_IMPL, searchContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observation, properties);
            interceptor.beforeSearch(event);
            fail("Patient compartment interaction was allowed but should not be");
        } catch (FHIRPersistenceInterceptorException e) {
            // success
            assertEquals(e.getIssues().size(), 1);
            assertEquals(e.getIssues().get(0).getCode(), IssueType.FORBIDDEN);
            assertEquals(e.getIssues().get(0).getDetails().getText().getValue(),
                "The resource 'Encounter/bogus' does not exist.");
        }

        // Invalid compartment search: Device compartment
        try {
            queryParameterValue.setValueString("Device/" + PATIENT_ID);
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Observation");
            properties.put(FHIRPersistenceEvent.PROPNAME_SEARCH_CONTEXT_IMPL, searchContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observation, properties);
            interceptor.beforeSearch(event);
            fail("Patient compartment interaction was allowed but should not be");
        } catch (FHIRPersistenceInterceptorException e) {
            // success
            assertEquals(e.getIssues().size(), 1);
            assertEquals(e.getIssues().get(0).getCode(), IssueType.FORBIDDEN);
            assertEquals(e.getIssues().get(0).getDetails().getText().getValue(),
                "Compartment search for compartment type 'Device' is not permitted.");
        }

        // Invalid compartment search: Practitioner compartment
        try {
            queryParameterValue.setValueString("Practitioner/" + PATIENT_ID);
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Observation");
            properties.put(FHIRPersistenceEvent.PROPNAME_SEARCH_CONTEXT_IMPL, searchContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observation, properties);
            interceptor.beforeSearch(event);
            fail("Patient compartment interaction was allowed but should not be");
        } catch (FHIRPersistenceInterceptorException e) {
            // success
            assertEquals(e.getIssues().size(), 1);
            assertEquals(e.getIssues().get(0).getCode(), IssueType.FORBIDDEN);
            assertEquals(e.getIssues().get(0).getDetails().getText().getValue(),
                "Compartment search for compartment type 'Practitioner' is not permitted.");
        }

        // Invalid compartment search: RelatedPerson compartment
        try {
            queryParameterValue.setValueString("RelatedPerson/" + PATIENT_ID);
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Observation");
            properties.put(FHIRPersistenceEvent.PROPNAME_SEARCH_CONTEXT_IMPL, searchContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observation, properties);
            interceptor.beforeSearch(event);
            fail("Patient compartment interaction was allowed but should not be");
        } catch (FHIRPersistenceInterceptorException e) {
            // success
            assertEquals(e.getIssues().size(), 1);
            assertEquals(e.getIssues().get(0).getCode(), IssueType.FORBIDDEN);
            assertEquals(e.getIssues().get(0).getDetails().getText().getValue(),
                "Compartment search for compartment type 'RelatedPerson' is not permitted.");
        }

        // Invalid compartment search: resource type not in list of provided scopes
        try {
            queryParameterValue.setValueString("Patient/" + PATIENT_ID);
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "AllergyIntolerance");
            properties.put(FHIRPersistenceEvent.PROPNAME_SEARCH_CONTEXT_IMPL, searchContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(practitioner, properties);
            interceptor.beforeSearch(event);
            fail("AllergyIntolerance interaction was allowed but should not be");
        } catch (FHIRPersistenceInterceptorException e) {
            // success
            assertEquals(e.getIssues().size(), 1);
            assertEquals(e.getIssues().get(0).getCode(), IssueType.FORBIDDEN);
            assertEquals(e.getIssues().get(0).getDetails().getText().getValue(),
                "read permission for 'AllergyIntolerance' is not granted by any of the provided scopes: " +
                "[patient/Patient.read, patient/Observation.read, patient/Practitioner.read]");
        }

        // Valid non-compartment search: converted to Patient compartment search
        try {
            searchContext = new FHIRSearchContextImpl();
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Observation");
            properties.put(FHIRPersistenceEvent.PROPNAME_SEARCH_CONTEXT_IMPL, searchContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observation, properties);
            interceptor.beforeSearch(event);
            assertEquals(1, searchContext.getSearchParameters().size());
            for (QueryParameter searchParameter : searchContext.getSearchParameters()) {
                assertTrue(searchParameter.isInclusionCriteria());
                assertEquals("Patient/" + PATIENT_ID, searchParameter.getValues().get(0).getValueString());
            }
        } catch (FHIRPersistenceInterceptorException e) {
            fail("Patient interaction was not allowed but should have been", e);
        }

        // Valid non-compartment search: converted to Patient compartment search and compartment search parm is first in list
        try {
            searchContext = new FHIRSearchContextImpl();
            QueryParameterValue queryParm1Value = new QueryParameterValue();
            queryParm1Value.setValueCode("final");
            QueryParameter queryParm1 = new QueryParameter(Type.TOKEN, "status", null, null, Collections.singletonList(queryParm1Value));
            searchContext.getSearchParameters().add(queryParm1);
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Observation");
            properties.put(FHIRPersistenceEvent.PROPNAME_SEARCH_CONTEXT_IMPL, searchContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observation, properties);
            interceptor.beforeSearch(event);
            assertEquals(2, searchContext.getSearchParameters().size());
            List<QueryParameter> searchParms = searchContext.getSearchParameters();
            QueryParameter compartmentSearchParm = searchParms.get(0);
            assertTrue("ibm-internal-Patient-Compartment".equals(compartmentSearchParm.getCode()));
            assertEquals(Type.REFERENCE, compartmentSearchParm.getType());
            assertTrue(compartmentSearchParm.isInclusionCriteria());
            assertFalse(compartmentSearchParm.isChained());
            assertEquals(1, compartmentSearchParm.getValues().size());
            assertEquals("Patient/11111111-1111-1111-1111-111111111111", compartmentSearchParm.getValues().get(0).getValueString());
            compartmentSearchParm = compartmentSearchParm.getNextParameter();
            assertEquals("status", searchParms.get(1).getCode());
            assertEquals("final", searchParms.get(1).getValues().get(0).getValueCode());
        } catch (FHIRPersistenceInterceptorException e) {
            fail("Patient interaction was not allowed but should have been", e);
        }

        // Valid non-compartment search: Patient search is now allowed, but converted to a compartment search
        try {
            searchContext = new FHIRSearchContextImpl();
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient");
            properties.put(FHIRPersistenceEvent.PROPNAME_SEARCH_CONTEXT_IMPL, searchContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observation, properties);
            interceptor.beforeSearch(event);
            assertEquals(1, searchContext.getSearchParameters().size());
            for (QueryParameter searchParameter : searchContext.getSearchParameters()) {
                assertTrue(searchParameter.isInclusionCriteria());
                assertEquals("Patient/" + PATIENT_ID, searchParameter.getValues().get(0).getValueString());
            }
        } catch (FHIRPersistenceInterceptorException e) {
            fail("Patient interaction was not allowed but should have been", e);
        }

        // Valid non-compartment search: resource type not in Patient compartment so not converted to compartment search
        try {
            searchContext = new FHIRSearchContextImpl();
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Practitioner");
            properties.put(FHIRPersistenceEvent.PROPNAME_SEARCH_CONTEXT_IMPL, searchContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(practitioner, properties);
            interceptor.beforeSearch(event);
            assertEquals(searchContext.getSearchParameters().size(), 0);
        } catch (FHIRPersistenceInterceptorException e) {
            fail("Patient interaction was not allowed but should have been", e);
        }

        // Invalid non-compartment search: resource type not in list of provided scopes
        try {
            searchContext = new FHIRSearchContextImpl();
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "AllergyIntolerance");
            properties.put(FHIRPersistenceEvent.PROPNAME_SEARCH_CONTEXT_IMPL, searchContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(practitioner, properties);
            interceptor.beforeSearch(event);
            fail("AllergyIntolerance interaction was allowed but should not be");
        } catch (FHIRPersistenceInterceptorException e) {
            // success
            assertEquals(e.getIssues().size(), 1);
            assertEquals(e.getIssues().get(0).getCode(), IssueType.FORBIDDEN);
            assertEquals(e.getIssues().get(0).getDetails().getText().getValue(),
                "read permission for 'AllergyIntolerance' is not granted by any of the provided scopes: " +
                "[patient/Patient.read, patient/Observation.read, patient/Practitioner.read]");
        }

        // Invalid non-compartment search: non-compartment resource type not in list of provided scopes
        try {
            searchContext = new FHIRSearchContextImpl();
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Medication");
            properties.put(FHIRPersistenceEvent.PROPNAME_SEARCH_CONTEXT_IMPL, searchContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(practitioner, properties);
            interceptor.beforeSearch(event);
            fail("Medication interaction was allowed but should not be");
        } catch (FHIRPersistenceInterceptorException e) {
            // success
            assertEquals(e.getIssues().size(), 1);
            assertEquals(e.getIssues().get(0).getCode(), IssueType.FORBIDDEN);
            assertEquals(e.getIssues().get(0).getDetails().getText().getValue(),
                "read permission for 'Medication' is not granted by any of the provided scopes: " +
                "[patient/Patient.read, patient/Observation.read, patient/Practitioner.read]");
        }
    }

    @Test(dataProvider = "scopeStringProvider")
    public void testRead(String scopeString, List<String> contextIds, Set<ResourceType> resourceTypesPermittedByScope, Permission permission) {
        FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders(scopeString, contextIds));

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient");
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
            interceptor.afterRead(event);
            assertTrue(shouldSucceed(resourceTypesPermittedByScope, PATIENT, READ_APPROVED, permission));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceTypesPermittedByScope, PATIENT, READ_APPROVED, permission));
        }

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Observation");
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observation, properties);
            interceptor.afterRead(event);
            assertTrue(shouldSucceed(resourceTypesPermittedByScope, OBSERVATION, READ_APPROVED, permission));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceTypesPermittedByScope, OBSERVATION, READ_APPROVED, permission));
        }

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Condition");
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(condition, properties);
            interceptor.afterRead(event);
            assertTrue(shouldSucceed(resourceTypesPermittedByScope, CONDITION, READ_APPROVED, permission));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceTypesPermittedByScope, CONDITION, READ_APPROVED, permission));
        }

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Provenance");
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(patientProvenance, properties);
            interceptor.afterRead(event);
            assertTrue(shouldSucceed(resourceTypesPermittedByScope, PROVENANCE, READ_APPROVED, permission));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceTypesPermittedByScope, PROVENANCE, READ_APPROVED, permission));
        }

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Provenance");
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observationProvenance, properties);
            interceptor.afterRead(event);
            assertTrue(shouldSucceed(resourceTypesPermittedByScope, PROVENANCE, READ_APPROVED, permission));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceTypesPermittedByScope, PROVENANCE, READ_APPROVED, permission));
        }
    }

    @Test(dataProvider = "scopeStringProvider")
    public void testVRead(String scopeString, List<String> contextIds, Set<ResourceType> typesPermittedByScopes, Permission permission) {
        FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders(scopeString, contextIds));

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient");
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
            interceptor.afterVread(event);
            assertTrue(shouldSucceed(typesPermittedByScopes, PATIENT, READ_APPROVED, permission));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(typesPermittedByScopes, PATIENT, READ_APPROVED, permission));
        }

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Observation");
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observation, properties);
            interceptor.afterVread(event);
            assertTrue(shouldSucceed(typesPermittedByScopes, OBSERVATION, READ_APPROVED, permission));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(typesPermittedByScopes, OBSERVATION, READ_APPROVED, permission));
        }

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Condition");
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(condition, properties);
            interceptor.afterVread(event);
            assertTrue(shouldSucceed(typesPermittedByScopes, CONDITION, READ_APPROVED, permission));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(typesPermittedByScopes, CONDITION, READ_APPROVED, permission));
        }
    }

    @Test(dataProvider = "scopeStringProvider")
    public void testHistory(String scopeString, List<String> contextIds, Set<ResourceType> resourceTypesPermittedByScope, Permission permission) {
        FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders(scopeString, contextIds));

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient");
            Bundle historyBundle = Bundle.builder()
                    .type(BundleType.HISTORY)
                    .entry(Bundle.Entry.builder().resource(patient).build())
                    .build();
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(historyBundle, properties);
            interceptor.afterHistory(event);
            assertTrue(shouldSucceed(resourceTypesPermittedByScope, PATIENT, READ_APPROVED, permission));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceTypesPermittedByScope, PATIENT, READ_APPROVED, permission));
        }

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Observation");
            Bundle historyBundle = Bundle.builder()
                    .type(BundleType.HISTORY)
                    .entry(Bundle.Entry.builder().resource(observation).build())
                    .build();
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(historyBundle, properties);
            interceptor.afterHistory(event);
            assertTrue(shouldSucceed(resourceTypesPermittedByScope, OBSERVATION, READ_APPROVED, permission));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceTypesPermittedByScope, OBSERVATION, READ_APPROVED, permission));
        }

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Condition");
            Bundle historyBundle = Bundle.builder()
                    .type(BundleType.HISTORY)
                    .entry(Bundle.Entry.builder().resource(condition).build())
                    .build();
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(historyBundle, properties);
            interceptor.afterHistory(event);
            assertTrue(shouldSucceed(resourceTypesPermittedByScope, CONDITION, READ_APPROVED, permission));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceTypesPermittedByScope, CONDITION, READ_APPROVED, permission));
        }
    }

    @Test(dataProvider = "scopeStringProvider")
    public void testSearch(String scopeString, List<String> contextIds, Set<ResourceType> resourceTypesPermittedByScope, Permission permission) {
        FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders(scopeString, contextIds));

        try {
            Bundle searchBundle = Bundle.builder()
                    .type(BundleType.SEARCHSET)
                    .entry(Bundle.Entry.builder().resource(patient).build())
                    .build();
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(searchBundle, properties);
            interceptor.afterSearch(event);
            assertTrue(shouldSucceed(resourceTypesPermittedByScope, PATIENT, READ_APPROVED, permission));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceTypesPermittedByScope, PATIENT, READ_APPROVED, permission));
        }

        try {
            Bundle searchBundle = Bundle.builder()
                    .type(BundleType.SEARCHSET)
                    .entry(Bundle.Entry.builder().resource(observation).build())
                    .build();
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(searchBundle, properties);
            interceptor.afterSearch(event);

            assertTrue(shouldSucceed(resourceTypesPermittedByScope, OBSERVATION, READ_APPROVED, permission));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceTypesPermittedByScope, OBSERVATION, READ_APPROVED, permission));
        }

        try {
            Bundle searchBundle = Bundle.builder()
                    .type(BundleType.SEARCHSET)
                    .entry(Bundle.Entry.builder().resource(condition).build())
                    .build();
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(searchBundle, properties);
            interceptor.afterSearch(event);

            assertTrue(shouldSucceed(resourceTypesPermittedByScope, CONDITION, READ_APPROVED, permission));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceTypesPermittedByScope, CONDITION, READ_APPROVED, permission));
        }

        try {
            Bundle searchBundle = Bundle.builder()
                    .type(BundleType.SEARCHSET)
                    .entry(Bundle.Entry.builder().resource(patient).build())
                    .entry(Bundle.Entry.builder().resource(observation).build())
                    .build();
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(searchBundle, properties);
            interceptor.afterSearch(event);

            assertTrue(shouldSucceed(resourceTypesPermittedByScope, PATIENT, READ_APPROVED, permission)
                    && shouldSucceed(resourceTypesPermittedByScope, OBSERVATION, READ_APPROVED, permission));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceTypesPermittedByScope, PATIENT, READ_APPROVED, permission)
                    && shouldSucceed(resourceTypesPermittedByScope, OBSERVATION, READ_APPROVED, permission));
        }

        try {
            Bundle searchBundle = Bundle.builder()
                    .type(BundleType.SEARCHSET)
                    .entry(Bundle.Entry.builder().resource(patient).build())
                    .entry(Bundle.Entry.builder().resource(patientProvenance).build())
                    .build();
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(searchBundle, properties);
            interceptor.afterSearch(event);

            assertTrue(shouldSucceed(resourceTypesPermittedByScope, PATIENT, READ_APPROVED, permission)
                && shouldSucceed(resourceTypesPermittedByScope, PROVENANCE, READ_APPROVED, permission));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceTypesPermittedByScope, PATIENT, READ_APPROVED, permission)
                && shouldSucceed(resourceTypesPermittedByScope, PROVENANCE, READ_APPROVED, permission));
        }

        try {
            Bundle searchBundle = Bundle.builder()
                    .type(BundleType.SEARCHSET)
                    .entry(Bundle.Entry.builder().resource(observation).build())
                    .entry(Bundle.Entry.builder().resource(observationProvenance).build())
                    .build();
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(searchBundle, properties);
            interceptor.afterSearch(event);

            assertTrue(shouldSucceed(resourceTypesPermittedByScope, OBSERVATION, READ_APPROVED, permission)
                && shouldSucceed(resourceTypesPermittedByScope, PROVENANCE, READ_APPROVED, permission));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceTypesPermittedByScope, OBSERVATION, READ_APPROVED, permission)
                && shouldSucceed(resourceTypesPermittedByScope, PROVENANCE, READ_APPROVED, permission));
        }
    }

    @Test
    public void testBeforeInvoke() throws FHIRPersistenceInterceptorException {
        FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders("system/*.read", PATIENT_ID));

        // Resource Type (Null Type)
        try {
            List<Parameter> parameters = new ArrayList<>();
            parameters.add(Parameter.builder().name(string("_type")).value((Element)null).build());
            Parameters.Builder builder = Parameters.builder();
            builder.id(UUID.randomUUID().toString());
            builder.parameter(parameters);
            Parameters ps = builder.build();

            FHIROperationContext ctx = FHIROperationContext.createResourceTypeOperationContext("export");
            ctx.setProperty(FHIROperationContext.PROPNAME_REQUEST_PARAMETERS, ps);
            interceptor.beforeInvoke(ctx);
        } catch (FHIRPersistenceInterceptorException e) {
            fail("Null Check failed", e);
        }

        // Instance Type (Null Type)
        try {
            List<Parameter> parameters = new ArrayList<>();
            parameters.add(Parameter.builder().name(string("_type")).value((Element)null).build());
            Parameters.Builder builder = Parameters.builder();
            builder.id(UUID.randomUUID().toString());
            builder.parameter(parameters);
            Parameters ps = builder.build();

            FHIROperationContext ctx = FHIROperationContext.createInstanceOperationContext("export");
            ctx.setProperty(FHIROperationContext.PROPNAME_REQUEST_PARAMETERS, ps);
            interceptor.beforeInvoke(ctx);
        } catch (FHIRPersistenceInterceptorException e) {
            fail("Null Check failed", e);
        }

        // Instance Type (Null Type)
        try {
            List<Parameter> parameters = new ArrayList<>();
            parameters.add(Parameter.builder().name(string("_type")).value((Element)null).build());
            Parameters.Builder builder = Parameters.builder();
            builder.id(UUID.randomUUID().toString());
            builder.parameter(parameters);
            Parameters ps = builder.build();

            FHIROperationContext ctx = FHIROperationContext.createSystemOperationContext("export");
            ctx.setProperty(FHIROperationContext.PROPNAME_REQUEST_PARAMETERS, ps);
            interceptor.beforeInvoke(ctx);
        } catch (FHIRPersistenceInterceptorException e) {
            fail("Null Check failed", e);
        }

        // Resource Type ("Observation")
        try {
            List<Parameter> parameters = new ArrayList<>();
            parameters.add(Parameter.builder().name(string("_type")).value("Observation").build());
            Parameters.Builder builder = Parameters.builder();
            builder.id(UUID.randomUUID().toString());
            builder.parameter(parameters);
            Parameters ps = builder.build();

            FHIROperationContext ctx = FHIROperationContext.createResourceTypeOperationContext("export");
            ctx.setProperty(FHIROperationContext.PROPNAME_REQUEST_PARAMETERS, ps);
            interceptor.beforeInvoke(ctx);
        } catch (FHIRPersistenceInterceptorException e) {
            fail("Actual Check failed", e);
        }

        // Instance Type ("Observation")
        try {
            List<Parameter> parameters = new ArrayList<>();
            parameters.add(Parameter.builder().name(string("_type")).value("Observation").build());
            Parameters.Builder builder = Parameters.builder();
            builder.id(UUID.randomUUID().toString());
            builder.parameter(parameters);
            Parameters ps = builder.build();

            FHIROperationContext ctx = FHIROperationContext.createInstanceOperationContext("export");
            ctx.setProperty(FHIROperationContext.PROPNAME_REQUEST_PARAMETERS, ps);
            interceptor.beforeInvoke(ctx);
        } catch (FHIRPersistenceInterceptorException e) {
            fail("Actual Check failed", e);
        }

        // Instance Type ("Observation")
        try {
            List<Parameter> parameters = new ArrayList<>();
            parameters.add(Parameter.builder().name(string("_type")).value("Observation").build());
            Parameters.Builder builder = Parameters.builder();
            builder.id(UUID.randomUUID().toString());
            builder.parameter(parameters);
            Parameters ps = builder.build();

            FHIROperationContext ctx = FHIROperationContext.createSystemOperationContext("export");
            ctx.setProperty(FHIROperationContext.PROPNAME_REQUEST_PARAMETERS, ps);
            interceptor.beforeInvoke(ctx);
        } catch (FHIRPersistenceInterceptorException e) {
            fail("Actual Check failed", e);
        }

        // Instance Type ("Bundle")
        try {
            List<Parameter> parameters = new ArrayList<>();
            parameters.add(Parameter.builder().name(string("_type")).value("Bundle").build());
            Parameters.Builder builder = Parameters.builder();
            builder.id(UUID.randomUUID().toString());
            builder.parameter(parameters);
            Parameters ps = builder.build();

            FHIROperationContext ctx = FHIROperationContext.createInstanceOperationContext("export");
            ctx.setProperty(FHIROperationContext.PROPNAME_REQUEST_PARAMETERS, ps);
            interceptor.beforeInvoke(ctx);

        } catch (FHIRPersistenceInterceptorException e) {
            fail("Requested Resource outside Compartment Check failed", e);
        } catch (java.lang.IllegalStateException ise) {
            assertEquals(ise.getMessage(), "Requested resource is outside of the Patient Compartment");
        }

        // Instance Type ("DoesntExist")
        try {
            List<Parameter> parameters = new ArrayList<>();
            parameters.add(Parameter.builder().name(string("_type")).value("DoesntExist").build());
            Parameters.Builder builder = Parameters.builder();
            builder.id(UUID.randomUUID().toString());
            builder.parameter(parameters);
            Parameters ps = builder.build();

            FHIROperationContext ctx = FHIROperationContext.createInstanceOperationContext("export");
            ctx.setProperty(FHIROperationContext.PROPNAME_REQUEST_PARAMETERS, ps);
            interceptor.beforeInvoke(ctx);

        } catch (FHIRPersistenceInterceptorException e) {
            fail("Requested Resource outside Compartment Check failed", e);
        } catch (java.lang.IllegalStateException ise) {
            assertEquals(ise.getMessage(), "Requested resource is not configured");
        }

        // Instance Type ("DoesntExist")
        try {
            List<Parameter> parameters = new ArrayList<>();
            parameters.add(Parameter.builder().name(string("_type")).value("DoesntExist").build());
            Parameters.Builder builder = Parameters.builder();
            builder.id(UUID.randomUUID().toString());
            builder.parameter(parameters);
            Parameters ps = builder.build();

            FHIROperationContext ctx = FHIROperationContext.createSystemOperationContext("export");
            ctx.setProperty(FHIROperationContext.PROPNAME_REQUEST_PARAMETERS, ps);
            interceptor.beforeInvoke(ctx);

        } catch (FHIRPersistenceInterceptorException e) {
            fail("Requested Resource outside Compartment Check failed", e);
        } catch (java.lang.IllegalStateException ise) {
            assertEquals(ise.getMessage(), "Requested resource is not configured");
        }
    }

    /**
     * Build a requestHeaders map with an Authorization header with a Bearer token constructed from the passed
     * scopeString and contextId
     */
    private Map<String, List<String>> buildRequestHeaders(String scopeString, String contextId) {
        // Uses LinkedHashMap just to preserve the order.
        Map<String, List<String>> requestHeaders = new LinkedHashMap<String, List<String>>();

        List<String> authHeader = Collections.singletonList("Bearer " + JWT.create()
                .withClaim("patient_id", contextId)
                .withClaim("scope", scopeString)
                .sign(Algorithm.none()));

        requestHeaders.put("Authorization", authHeader);

        return requestHeaders;
    }

    /**
     * Build a requestHeaders map with an Authorization header with a Bearer token constructed from the passed
     * scopeString and contextIds
     */
    private Map<String, List<String>> buildRequestHeaders(String scopeString, List<String> contextIds) {
        // Uses LinkedHashMap just to preserve the order.
        Map<String, List<String>> requestHeaders = new LinkedHashMap<String, List<String>>();

        List<String> authHeader = Collections.singletonList("Bearer " + JWT.create()
                .withClaim("patient_id", contextIds)
                .withClaim("scope", scopeString)
                .sign(Algorithm.none()));

        requestHeaders.put("Authorization", authHeader);

        return requestHeaders;
    }

    /**
     * @return true if the interaction should succeed, otherwise false
     */
    private boolean shouldSucceed(Set<ResourceType> resourceTypesPermittedByScope, ResourceType requiredResourceType,
            List<Permission> permissionsPermittedByScope, Permission requiredPermission) {
        if (resourceTypesPermittedByScope.contains(ResourceType.RESOURCE) && permissionsPermittedByScope.contains(requiredPermission)) {
            return true;
        }
        if (resourceTypesPermittedByScope.contains(requiredResourceType) && permissionsPermittedByScope.contains(requiredPermission)) {
            return true;
        }
        return false;
    }

    @DataProvider(name = "scopeStringProvider")
    public static Object[][] scopeStrings() {
        final Set<ResourceType> all_resources = Collections.singleton(RESOURCE);
        final Set<ResourceType> patient = Collections.singleton(PATIENT);
        final Set<ResourceType> observation = Collections.singleton(OBSERVATION);
        final Set<ResourceType> provenance = Collections.singleton(PROVENANCE);

        final List<String> CONTEXT_IDS = Collections.singletonList(PATIENT_ID);

        return new Object[][] {
            //String scopeString, String context, Set<ResourceType.Value> resourceTypesPermittedByScope, Permission permission
            {"patient/*.*", CONTEXT_IDS, all_resources, Permission.ALL},
            {"patient/*.read", CONTEXT_IDS, all_resources, Permission.READ},
            {"patient/*.write", CONTEXT_IDS, all_resources, Permission.WRITE},

            {"patient/Patient.*", CONTEXT_IDS, patient, Permission.ALL},
            {"patient/Patient.read", CONTEXT_IDS, patient, Permission.READ},
            {"patient/Patient.write", CONTEXT_IDS, patient, Permission.WRITE},
            {"patient/Patient.* patient/Provenance.*", CONTEXT_IDS, union(patient, provenance), Permission.ALL},

            {"patient/Observation.*", CONTEXT_IDS, observation, Permission.ALL},
            {"patient/Observation.read", CONTEXT_IDS, observation, Permission.READ},
            {"patient/Observation.write", CONTEXT_IDS, observation, Permission.WRITE},
            {"patient/Observation.* patient/Provenance.*", CONTEXT_IDS, union(observation, provenance), Permission.ALL},

            {"openid profile patient/Patient.*", CONTEXT_IDS, patient, Permission.ALL},
            {"openid patient/Patient.read profile", CONTEXT_IDS, patient, Permission.READ},
            {"patient/Patient.write openid profile", CONTEXT_IDS, patient, Permission.WRITE},

            {"patient/Patient.read patient/Observation.read", CONTEXT_IDS, union(patient, observation), Permission.READ},

            {"user/*.*", CONTEXT_IDS, all_resources, Permission.ALL},
            {"user/Patient.read", CONTEXT_IDS, patient, Permission.READ},
            {"user/Observation.write", CONTEXT_IDS, observation, Permission.WRITE},

            {"system/*.*", CONTEXT_IDS, all_resources, Permission.ALL},
            {"system/Patient.read", CONTEXT_IDS, patient, Permission.READ},
            {"system/Observation.write", CONTEXT_IDS, observation, Permission.WRITE},

            {"openid profile", CONTEXT_IDS, Collections.EMPTY_SET, null},
        };
    }

    private static <T> Set<T> union(Set<T> a, Set<T> b) {
        return Stream.concat(a.stream(), b.stream()).collect(Collectors.toSet());
    }
}
