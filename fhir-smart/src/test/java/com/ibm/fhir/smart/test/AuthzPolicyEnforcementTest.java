/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.smart.test;

import static com.ibm.fhir.model.type.String.string;
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
import com.ibm.fhir.model.type.code.ResourceType.ValueSet;
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
    private static final String OBSERVATION_ID = "22222222-2222-2222-2222-222222222222";

    private static final List<ResourceType.ValueSet> PATIENT_APPROVED =
            Arrays.asList(ResourceType.ValueSet.PATIENT, ResourceType.ValueSet.RESOURCE);
    private static final List<ResourceType.ValueSet> OBSERVATION_APPROVED =
            Arrays.asList(ResourceType.ValueSet.OBSERVATION, ResourceType.ValueSet.RESOURCE);
    private static final List<ResourceType.ValueSet> ALL_RESOURCES_APPROVED =
            Arrays.asList(ResourceType.ValueSet.RESOURCE);
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
    }

    @Test(dataProvider = "scopeStringProvider")
    public void testCreate(String scopeString, ResourceType.ValueSet resourceType, Permission permission) {
        FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders(scopeString));

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient");
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
            interceptor.beforeCreate(event);
            assertTrue(shouldSucceed(resourceType, PATIENT_APPROVED, permission, WRITE_APPROVED));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceType, PATIENT_APPROVED, permission, WRITE_APPROVED));
        }

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Observation");
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observation, properties);
            interceptor.beforeCreate(event);
            assertTrue(shouldSucceed(resourceType, OBSERVATION_APPROVED, permission, WRITE_APPROVED));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceType, OBSERVATION_APPROVED, permission, WRITE_APPROVED));
        }
    }

    @Test(dataProvider = "scopeStringProvider")
    public void testUpdate(String scopeString, ResourceType.ValueSet resourceType, Permission permission) {
        FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders(scopeString));

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient");
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
            event.setPrevFhirResource(patient);
            interceptor.beforeUpdate(event);
            assertTrue(shouldSucceed(resourceType, PATIENT_APPROVED, permission, READ_APPROVED) &&
                    shouldSucceed(resourceType, PATIENT_APPROVED, permission, WRITE_APPROVED));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceType, PATIENT_APPROVED, permission, READ_APPROVED) &&
                    shouldSucceed(resourceType, PATIENT_APPROVED, permission, WRITE_APPROVED));
        }

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Observation");
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observation, properties);
            event.setPrevFhirResource(observation);
            interceptor.beforeUpdate(event);
            assertTrue(shouldSucceed(resourceType, OBSERVATION_APPROVED, permission, READ_APPROVED) &&
                        shouldSucceed(resourceType, OBSERVATION_APPROVED, permission, WRITE_APPROVED));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceType, OBSERVATION_APPROVED, permission, READ_APPROVED) &&
                        shouldSucceed(resourceType, OBSERVATION_APPROVED, permission, WRITE_APPROVED));
        }
    }

    @Test(dataProvider = "scopeStringProvider")
    public void testDelete(String scopeString, ResourceType.ValueSet resourceType, Permission permission) {
        FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders(scopeString));

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient");
            FHIRPersistenceEvent event = new FHIRPersistenceEvent();
            event.setPrevFhirResource(patient);
            interceptor.beforeDelete(event);
            assertTrue(shouldSucceed(resourceType, PATIENT_APPROVED, permission, WRITE_APPROVED));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceType, PATIENT_APPROVED, permission, WRITE_APPROVED));
        }

        try {properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Observation");
            FHIRPersistenceEvent event = new FHIRPersistenceEvent();
            event.setPrevFhirResource(observation);
            interceptor.beforeDelete(event);
            assertTrue(shouldSucceed(resourceType, OBSERVATION_APPROVED, permission, WRITE_APPROVED));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceType, OBSERVATION_APPROVED, permission, WRITE_APPROVED));
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
    public void testRead(String scopeString, ResourceType.ValueSet resourceType, Permission permission) {
        FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders(scopeString));

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient");
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
            interceptor.afterRead(event);
            assertTrue(shouldSucceed(resourceType, PATIENT_APPROVED, permission, READ_APPROVED));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceType, PATIENT_APPROVED, permission, READ_APPROVED));
        }

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Observation");
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observation, properties);
            interceptor.afterRead(event);
            assertTrue(shouldSucceed(resourceType, OBSERVATION_APPROVED, permission, READ_APPROVED));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceType, OBSERVATION_APPROVED, permission, READ_APPROVED));
        }
    }

    @Test(dataProvider = "scopeStringProvider")
    public void testVRead(String scopeString, ResourceType.ValueSet resourceType, Permission permission) {
        FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders(scopeString));

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient");
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
            interceptor.afterVread(event);
            assertTrue(shouldSucceed(resourceType, PATIENT_APPROVED, permission, READ_APPROVED));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceType, PATIENT_APPROVED, permission, READ_APPROVED));
        }

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Observation");
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observation, properties);
            interceptor.afterVread(event);
            assertTrue(shouldSucceed(resourceType, OBSERVATION_APPROVED, permission, READ_APPROVED));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceType, OBSERVATION_APPROVED, permission, READ_APPROVED));
        }
    }

    @Test(dataProvider = "scopeStringProvider")
    public void testHistory(String scopeString, ResourceType.ValueSet resourceType, Permission permission) {
        FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders(scopeString));

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient");
            Bundle historyBundle = Bundle.builder()
                    .type(BundleType.HISTORY)
                    .entry(Bundle.Entry.builder().resource(patient).build())
                    .build();
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(historyBundle, properties);
            interceptor.afterHistory(event);
            assertTrue(shouldSucceed(resourceType, PATIENT_APPROVED, permission, READ_APPROVED));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceType, PATIENT_APPROVED, permission, READ_APPROVED));
        }

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Observation");
            Bundle historyBundle = Bundle.builder()
                    .type(BundleType.HISTORY)
                    .entry(Bundle.Entry.builder().resource(observation).build())
                    .build();
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(historyBundle, properties);
            interceptor.afterHistory(event);
            assertTrue(shouldSucceed(resourceType, OBSERVATION_APPROVED, permission, READ_APPROVED));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceType, OBSERVATION_APPROVED, permission, READ_APPROVED));
        }
    }

    @Test(dataProvider = "scopeStringProvider")
    public void testSearch(String scopeString, ResourceType.ValueSet resourceType, Permission permission) {
        FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders(scopeString));

        try {
            Bundle searchBundle = Bundle.builder()
                    .type(BundleType.SEARCHSET)
                    .entry(Bundle.Entry.builder().resource(patient).build())
                    .build();
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(searchBundle, properties);
            interceptor.afterSearch(event);
            assertTrue(shouldSucceed(resourceType, PATIENT_APPROVED, permission, READ_APPROVED));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceType, PATIENT_APPROVED, permission, READ_APPROVED));
        }

        try {
            Bundle searchBundle = Bundle.builder()
                    .type(BundleType.SEARCHSET)
                    .entry(Bundle.Entry.builder().resource(observation).build())
                    .build();
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(searchBundle, properties);
            interceptor.afterSearch(event);

            assertTrue(shouldSucceed(resourceType, OBSERVATION_APPROVED, permission, READ_APPROVED));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceType, OBSERVATION_APPROVED, permission, READ_APPROVED));
        }

        try {
            Bundle searchBundle = Bundle.builder()
                    .type(BundleType.SEARCHSET)
                    .entry(Bundle.Entry.builder().resource(patient).build())
                    .entry(Bundle.Entry.builder().resource(observation).build())
                    .build();
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(searchBundle, properties);
            interceptor.afterSearch(event);

            assertTrue(shouldSucceed(resourceType, ALL_RESOURCES_APPROVED, permission, READ_APPROVED));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceType, ALL_RESOURCES_APPROVED, permission, READ_APPROVED));
        }
    }

    @Test(dataProvider = "scopeStringProvider")
    public void testSearchWithProvenance(String scopeString, ResourceType.ValueSet resourceType, Permission permission) throws Exception {
        FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders(scopeString));

        // hack the scope string to include patient/Provenance.read
        scopeString = scopeString + " patient/Provenance.read";

        try {
            Bundle searchBundle = Bundle.builder()
                    .type(BundleType.SEARCHSET)
                    .entry(Bundle.Entry.builder().resource(patient).build())
                    .entry(Bundle.Entry.builder().resource(patientProvenance).build())
                    .build();
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(searchBundle, properties);
            interceptor.afterSearch(event);

            assertTrue(shouldSucceed(resourceType, PATIENT_APPROVED, permission, READ_APPROVED));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceType, PATIENT_APPROVED, permission, READ_APPROVED));
        }

        try {
            Bundle searchBundle = Bundle.builder()
                    .type(BundleType.SEARCHSET)
                    .entry(Bundle.Entry.builder().resource(observation).build())
                    .entry(Bundle.Entry.builder().resource(observationProvenance).build())
                    .build();
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(searchBundle, properties);
            interceptor.afterSearch(event);

            assertTrue(shouldSucceed(resourceType, OBSERVATION_APPROVED, permission, READ_APPROVED));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceType, OBSERVATION_APPROVED, permission, READ_APPROVED));
        }

        Provenance bogusProvenance = TestUtil.getMinimalResource(ResourceType.PROVENANCE);
        try {
            Bundle searchBundle = Bundle.builder()
                    .type(BundleType.SEARCHSET)
                    .entry(Bundle.Entry.builder().resource(bogusProvenance).build())
                    .build();
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(searchBundle, properties);
            interceptor.afterSearch(event);

            fail("Provenance resources should not be readable unless they target resources that are readable");
        } catch (FHIRPersistenceInterceptorException e) {
            // expected
        }

        bogusProvenance = TestUtil.readExampleResource("json/ibm/complete-mock/Provenance-1.json");
        try {
            Bundle searchBundle = Bundle.builder()
                    .type(BundleType.SEARCHSET)
                    .entry(Bundle.Entry.builder().resource(bogusProvenance).build())
                    .build();
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(searchBundle, properties);
            interceptor.afterSearch(event);

            fail("Provenance resources should not be readable unless they target resources that are readable");
        } catch (FHIRPersistenceInterceptorException e) {
            // expected
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
    private boolean shouldSucceed(ValueSet resourceType, List<ValueSet> resourceTypes, Permission permission, List<Permission> permissions) {
        if (resourceTypes.contains(resourceType) && permissions.contains(permission)) {
            return true;
        }
        return false;
    }

    @DataProvider(name = "scopeStringProvider")
    public static Object[][] scopeStrings() {
       return new Object[][] {
           {"patient/*.*", ResourceType.ValueSet.RESOURCE, Permission.ALL},
           {"patient/*.read", ResourceType.ValueSet.RESOURCE, Permission.READ},
           {"patient/*.write", ResourceType.ValueSet.RESOURCE, Permission.WRITE},

           {"patient/Patient.*", ResourceType.ValueSet.PATIENT, Permission.ALL},
           {"patient/Patient.read", ResourceType.ValueSet.PATIENT, Permission.READ},
           {"patient/Patient.write", ResourceType.ValueSet.PATIENT, Permission.WRITE},

           {"patient/Observation.*", ResourceType.ValueSet.OBSERVATION, Permission.ALL},
           {"patient/Observation.read", ResourceType.ValueSet.OBSERVATION, Permission.READ},
           {"patient/Observation.write", ResourceType.ValueSet.OBSERVATION, Permission.WRITE},

           {"openid profile patient/Patient.*", ResourceType.ValueSet.PATIENT, Permission.ALL},
           {"openid patient/Patient.read profile", ResourceType.ValueSet.PATIENT, Permission.READ},
           {"patient/Patient.write openid profile", ResourceType.ValueSet.PATIENT, Permission.WRITE},

           {"patient/Patient.read patient/Observation.read", ResourceType.ValueSet.RESOURCE, Permission.READ},

           {"openid profile", null, null},
           };
    }
}
