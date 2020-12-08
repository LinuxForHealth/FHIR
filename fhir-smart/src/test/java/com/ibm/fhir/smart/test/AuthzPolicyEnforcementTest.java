/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.smart.test;

import static com.ibm.fhir.model.type.String.string;
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
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.model.type.code.ResourceType;
import com.ibm.fhir.model.type.code.ResourceType.ValueSet;
import com.ibm.fhir.persistence.interceptor.FHIRPersistenceEvent;
import com.ibm.fhir.persistence.interceptor.FHIRPersistenceInterceptorException;
import com.ibm.fhir.smart.AuthzPolicyEnforcementPersistenceInterceptor;
import com.ibm.fhir.smart.Scope.Permission;

public class AuthzPolicyEnforcementTest {
    private static final String PATIENT_ID = "11111111-1111-1111-1111-111111111111";

    private static final List<ResourceType.ValueSet> PATIENT_APPROVED =
            Arrays.asList(ResourceType.ValueSet.PATIENT, ResourceType.ValueSet.RESOURCE);
    private static final List<ResourceType.ValueSet> OBSERVATION_APPROVED =
            Arrays.asList(ResourceType.ValueSet.OBSERVATION, ResourceType.ValueSet.RESOURCE);
    private static final List<ResourceType.ValueSet> PATIENT_AND_OBSERVATION_APPROVED =
            Arrays.asList(ResourceType.ValueSet.RESOURCE);
    private static final List<Permission> READ_APPROVED = Arrays.asList(Permission.READ, Permission.ALL);
    private static final List<Permission> WRITE_APPROVED = Arrays.asList(Permission.WRITE, Permission.ALL);

    private AuthzPolicyEnforcementPersistenceInterceptor interceptor;
    private Patient patient;
    private Observation observation;
    private Map<String, Object> properties;

    @BeforeClass
    public void setup() throws Exception {
        System.setProperty("java.util.logging.config.file", ClassLoader.getSystemResource("logging.properties").getPath());

        FHIRRequestContext requestContext = new FHIRRequestContext();
        requestContext.setTenantId("default");
        FHIRRequestContext.set(requestContext);

        interceptor = new AuthzPolicyEnforcementPersistenceInterceptor();

        patient = TestUtil.readExampleResource("json/ibm/minimal/Patient-1.json");
        patient = patient.toBuilder()
                .id(PATIENT_ID)
                .build();

        observation = TestUtil.readExampleResource("json/ibm/minimal/Observation-1.json");
        observation = observation.toBuilder()
                .subject(Reference.builder().reference(string("Patient/" + PATIENT_ID)).build())
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
    public void testReadPatient() throws FHIRPersistenceInterceptorException {
        FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders("patient/Patient.read"));

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient");
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID, PATIENT_ID);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
            interceptor.beforeRead(event);
        } catch (FHIRPersistenceInterceptorException e) {
            fail("Patient read was not allowed but should have been");
        }


        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient");
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID, "bogus");
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
            interceptor.beforeRead(event);
            fail("Patient read was allowed but should not be");
        } catch (FHIRPersistenceInterceptorException e) {
            // success
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

            assertTrue(shouldSucceed(resourceType, PATIENT_AND_OBSERVATION_APPROVED, permission, READ_APPROVED));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceType, PATIENT_AND_OBSERVATION_APPROVED, permission, READ_APPROVED));
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
