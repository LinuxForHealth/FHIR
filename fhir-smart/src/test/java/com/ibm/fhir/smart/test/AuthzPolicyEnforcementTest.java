/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.smart.test;

import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.model.type.code.ResourceType.ValueSet.OBSERVATION;
import static com.ibm.fhir.model.type.code.ResourceType.ValueSet.PATIENT;
import static com.ibm.fhir.model.type.code.ResourceType.ValueSet.PROVENANCE;
import static com.ibm.fhir.model.type.code.ResourceType.ValueSet.RESOURCE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Practitioner;
import com.ibm.fhir.model.resource.Provenance;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.type.code.ResourceType;
import com.ibm.fhir.persistence.interceptor.FHIRPersistenceEvent;
import com.ibm.fhir.persistence.interceptor.FHIRPersistenceInterceptorException;
import com.ibm.fhir.search.SearchConstants.Type;
import com.ibm.fhir.search.context.impl.FHIRSearchContextImpl;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.parameters.QueryParameterValue;
import com.ibm.fhir.smart.AuthzPolicyEnforcementPersistenceInterceptor;
import com.ibm.fhir.smart.Scope.Permission;

public class AuthzPolicyEnforcementTest {
    private static final String PATIENT_ID =     "11111111-1111-1111-1111-111111111111";
    private static final String OBSERVATION_ID = "11111111-1111-1111-1111-111111111111";

    private static final List<Permission> READ_APPROVED = Arrays.asList(Permission.READ, Permission.ALL);
    private static final List<Permission> WRITE_APPROVED = Arrays.asList(Permission.WRITE, Permission.ALL);

    private AuthzPolicyEnforcementPersistenceInterceptor interceptor;
    private Patient patient;
    private Observation observation;
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

        Provenance provenanceBase = TestUtil.getMinimalResource(ResourceType.PROVENANCE);

        patient = TestUtil.readExampleResource("json/ibm/minimal/Patient-1.json");
        patient = patient.toBuilder()
                .id(PATIENT_ID)
                .build();

        patientProvenance = provenanceBase.toBuilder()
                .target(Reference.builder()
                    .reference(string("Patient/" + PATIENT_ID))
                    .build())
                .build();

        observation = TestUtil.readExampleResource("json/ibm/minimal/Observation-1.json");
        observation = observation.toBuilder()
                .id(OBSERVATION_ID)
                .subject(Reference.builder().reference(string("Patient/" + PATIENT_ID)).build())
                .build();

        observationProvenance = provenanceBase.toBuilder()
                .target(Reference.builder()
                    .reference(string("Observation/" + OBSERVATION_ID))
                    .build())
                .build();

        properties = new HashMap<String, Object>();

        MockPersistenceImpl mockPersistenceImpl = new MockPersistenceImpl(patient, observation);
        properties.put(FHIRPersistenceEvent.PROPNAME_PERSISTENCE_IMPL, mockPersistenceImpl);
    }

    @Test(dataProvider = "scopeStringProvider")
    public void testCreate(String scopeString, Set<ResourceType.ValueSet> resourceTypesPermittedByScope, Permission permission) {
        FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders(scopeString));

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
    }

    @Test(dataProvider = "scopeStringProvider")
    public void testUpdate(String scopeString, Set<ResourceType.ValueSet> resourceTypesPermittedByScope, Permission permission) {
        FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders(scopeString));

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
    }

    @Test(dataProvider = "scopeStringProvider")
    public void testDelete(String scopeString, Set<ResourceType.ValueSet> resourceTypesPermittedByScope, Permission permission) {
        FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders(scopeString));

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
    }

    @Test
    public void testDirectPatientInteraction() throws FHIRPersistenceInterceptorException {
        FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders("patient/Patient.read"));

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient");
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID, PATIENT_ID);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
            interceptor.beforeRead(event);
            interceptor.beforeVread(event);
            interceptor.beforeHistory(event);
        } catch (FHIRPersistenceInterceptorException e) {
            fail("Patient interaction was not allowed but should have been");
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
        FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders("patient/Patient.read"));
        Practitioner practitioner = null;
        try {
            practitioner = TestUtil.readExampleResource("json/ibm/minimal/Practitioner-1.json");
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
            fail("Patient interaction was not allowed but should have been");
        }

        // Invalid compartment search: not a Patient compartment
        try {
            queryParameterValue.setValueString("Encounter/" + PATIENT_ID);
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Observation");
            properties.put(FHIRPersistenceEvent.PROPNAME_SEARCH_CONTEXT_IMPL, searchContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observation, properties);
            interceptor.beforeSearch(event);
            fail("Patient interaction was allowed but should not be");
        } catch (FHIRPersistenceInterceptorException e) {
            // success
            assertEquals(1, e.getIssues().size());
            assertEquals(IssueType.FORBIDDEN, e.getIssues().get(0).getCode());
            assertEquals("Compartment search with compartment 'Encounter' is not permitted.", e.getIssues().get(0).getDetails().getText().getValue());
        }

        // Invalid compartment search: wrong Patient compartment
        try {
            queryParameterValue.setValueString("Patient/bogus");
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Observation");
            properties.put(FHIRPersistenceEvent.PROPNAME_SEARCH_CONTEXT_IMPL, searchContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observation, properties);
            interceptor.beforeSearch(event);
            fail("Patient interaction was allowed but should not be");
        } catch (FHIRPersistenceInterceptorException e) {
            // success
            assertEquals(1, e.getIssues().size());
            assertEquals(IssueType.FORBIDDEN, e.getIssues().get(0).getCode());
            assertEquals("Interaction with 'Patient/bogus' is not permitted under patient context '[" + PATIENT_ID + "]'.", e.getIssues().get(0).getDetails().getText().getValue());
        }

        // Invalid compartment search: resource type not in Patient compartment
        try {
            queryParameterValue.setValueString("Patient/" + PATIENT_ID);
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Practitioner");
            properties.put(FHIRPersistenceEvent.PROPNAME_SEARCH_CONTEXT_IMPL, searchContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(practitioner, properties);
            interceptor.beforeSearch(event);
            fail("Patient interaction was allowed but should not be");
        } catch (FHIRPersistenceInterceptorException e) {
            // success
            assertEquals(1, e.getIssues().size());
            assertEquals(IssueType.INVALID, e.getIssues().get(0).getCode());
            assertEquals("Resource type 'Practitioner' is not valid for Patient compartment search.", e.getIssues().get(0).getDetails().getText().getValue());
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
            fail("Patient interaction was not allowed but should have been");
        }

        // Valid non-compartment search: converted to Patient compartment search and duplicate search parameters removed
        try {
            searchContext = new FHIRSearchContextImpl();
            QueryParameterValue queryParm1Value = new QueryParameterValue();
            queryParm1Value.setValueString("Patient/1");
            QueryParameter queryParm1 = new QueryParameter(Type.REFERENCE, "subject", null, null, Collections.singletonList(queryParm1Value));
            searchContext.getSearchParameters().add(queryParm1);
            QueryParameterValue queryParm2Value = new QueryParameterValue();
            queryParm2Value.setValueString("Patient/11111111-1111-1111-1111-111111111111");
            QueryParameter queryParm2 = new QueryParameter(Type.REFERENCE, "performer", null, null, Collections.singletonList(queryParm2Value));
            searchContext.getSearchParameters().add(queryParm2);
            QueryParameterValue queryParm3Value = new QueryParameterValue();
            queryParm3Value.setValueString("Patient/11111111-1111-1111-1111-111111111111");
            QueryParameter queryParm3 = new QueryParameter(Type.REFERENCE, "patient", null, null, Collections.singletonList(queryParm2Value));
            searchContext.getSearchParameters().add(queryParm3);
            QueryParameterValue queryParm4Value = new QueryParameterValue();
            queryParm4Value.setValueCode("final");
            QueryParameter queryParm4 = new QueryParameter(Type.TOKEN, "status", null, null, Collections.singletonList(queryParm4Value));
            searchContext.getSearchParameters().add(queryParm4);
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Observation");
            properties.put(FHIRPersistenceEvent.PROPNAME_SEARCH_CONTEXT_IMPL, searchContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observation, properties);
            interceptor.beforeSearch(event);
            assertEquals(3, searchContext.getSearchParameters().size());
            List<QueryParameter> searchParms = searchContext.getSearchParameters();
            QueryParameter compartmentSearchParm = searchParms.get(0);
            int parmCount = 0;
            while (compartmentSearchParm != null) {
                parmCount++;
                assertTrue((compartmentSearchParm.getCode().equals("performer") || compartmentSearchParm.getCode().equals("subject")));
                assertEquals(Type.REFERENCE, compartmentSearchParm.getType());
                assertTrue(compartmentSearchParm.isInclusionCriteria());
                assertFalse(compartmentSearchParm.isChained());
                assertEquals(1, compartmentSearchParm.getValues().size());
                assertEquals("Patient/11111111-1111-1111-1111-111111111111", compartmentSearchParm.getValues().get(0).getValueString());
                compartmentSearchParm = compartmentSearchParm.getNextParameter();
            }
            assertEquals(2, parmCount);
            assertEquals("subject", searchParms.get(1).getCode());
            assertEquals("Patient/1", searchParms.get(1).getValues().get(0).getValueString());
            assertEquals("status", searchParms.get(2).getCode());
            assertEquals("final", searchParms.get(2).getValues().get(0).getValueCode());
        } catch (FHIRPersistenceInterceptorException e) {
            fail("Patient interaction was not allowed but should have been");
        }

        // Valid non-compartment search: resource type not in Patient compartment so not converted to compartment search
        try {
            searchContext = new FHIRSearchContextImpl();
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Practitioner");
            properties.put(FHIRPersistenceEvent.PROPNAME_SEARCH_CONTEXT_IMPL, searchContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(practitioner, properties);
            interceptor.beforeSearch(event);
            assertEquals(0, searchContext.getSearchParameters().size());
        } catch (FHIRPersistenceInterceptorException e) {
            fail("Patient interaction was not allowed but should have been");
        }

        // Invalid non-compartment search: resource type is Patient
        try {
            searchContext = new FHIRSearchContextImpl();
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient");
            properties.put(FHIRPersistenceEvent.PROPNAME_SEARCH_CONTEXT_IMPL, searchContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
            interceptor.beforeSearch(event);
            fail("Patient interaction was allowed but should not be");
        } catch (FHIRPersistenceInterceptorException e) {
            // success
            assertEquals(1, e.getIssues().size());
            assertEquals(IssueType.FORBIDDEN, e.getIssues().get(0).getCode());
            assertEquals("Non-compartment Patient search is not permitted.", e.getIssues().get(0).getDetails().getText().getValue());
        }
    }

    @Test(dataProvider = "scopeStringProvider")
    public void testRead(String scopeString, Set<ResourceType.ValueSet> resourceTypesPermittedByScope, Permission permission) {
        FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders(scopeString));

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
    public void testVRead(String scopeString, Set<ResourceType.ValueSet> typesPermittedByScopes, Permission permission) {
        FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders(scopeString));

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
    }

    @Test(dataProvider = "scopeStringProvider")
    public void testHistory(String scopeString, Set<ResourceType.ValueSet> resourceTypesPermittedByScope, Permission permission) {
        FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders(scopeString));

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
    }

    @Test(dataProvider = "scopeStringProvider")
    public void testSearch(String scopeString, Set<ResourceType.ValueSet> resourceTypesPermittedByScope, Permission permission) {
        FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders(scopeString));

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

    /**
     * Build a requestHeaders map by constructing a Bearer token from the passed scopeString
     * and setting the Authorization header
     */
    private Map<String, List<String>> buildRequestHeaders(String scopeString) {
        // Uses LinkedHashMap just to preserve the order.
        Map<String, List<String>> requestHeaders = new LinkedHashMap<String, List<String>>();

        List<String> authHeader = Collections.singletonList("Bearer " + JWT.create()
                .withClaim("patient_id", PATIENT_ID)
                .withClaim("scope", scopeString)
                .sign(Algorithm.none()));

        requestHeaders.put("Authorization", authHeader);

        return requestHeaders;
    }

    /**
     * @return true if the interaction should succeed, otherwise false
     */
    private boolean shouldSucceed(Set<ResourceType.ValueSet> resourceTypesPermittedByScope, ResourceType.ValueSet requiredResourceType,
            List<Permission> permissionsPermittedByScope, Permission requiredPermission) {
        if (resourceTypesPermittedByScope.contains(ResourceType.ValueSet.RESOURCE) && permissionsPermittedByScope.contains(requiredPermission)) {
            return true;
        }
        if (resourceTypesPermittedByScope.contains(requiredResourceType) && permissionsPermittedByScope.contains(requiredPermission)) {
            return true;
        }
        return false;
    }

    @DataProvider(name = "scopeStringProvider")
    public static Object[][] scopeStrings() {
        final Set<ResourceType.ValueSet> all_resources = Collections.singleton(RESOURCE);
        final Set<ResourceType.ValueSet> patient = Collections.singleton(PATIENT);
        final Set<ResourceType.ValueSet> observation = Collections.singleton(OBSERVATION);
        final Set<ResourceType.ValueSet> provenance = Collections.singleton(PROVENANCE);

        return new Object[][] {
            {"patient/*.*", all_resources, Permission.ALL},
            {"patient/*.read", all_resources, Permission.READ},
            {"patient/*.write", all_resources, Permission.WRITE},

            {"patient/Patient.*", patient, Permission.ALL},
            {"patient/Patient.read", patient, Permission.READ},
            {"patient/Patient.write", patient, Permission.WRITE},
            {"patient/Patient.* patient/Provenance.*", union(patient, provenance), Permission.ALL},

            {"patient/Observation.*", observation, Permission.ALL},
            {"patient/Observation.read", observation, Permission.READ},
            {"patient/Observation.write", observation, Permission.WRITE},
            {"patient/Observation.* patient/Provenance.*", union(observation, provenance), Permission.ALL},

            {"openid profile patient/Patient.*", patient, Permission.ALL},
            {"openid patient/Patient.read profile", patient, Permission.READ},
            {"patient/Patient.write openid profile", patient, Permission.WRITE},

            {"patient/Patient.read patient/Observation.read", union(patient, observation), Permission.READ},

            {"openid profile", Collections.EMPTY_SET, null},
        };
    }

    private static <T> Set<T> union(Set<T> a, Set<T> b) {
        return Stream.concat(a.stream(), b.stream()).collect(Collectors.toSet());
    }
}
