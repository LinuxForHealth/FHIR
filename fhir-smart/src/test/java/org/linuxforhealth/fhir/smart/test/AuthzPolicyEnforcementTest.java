/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.smart.test;

import static org.linuxforhealth.fhir.core.ResourceType.CONDITION;
import static org.linuxforhealth.fhir.core.ResourceType.OBSERVATION;
import static org.linuxforhealth.fhir.core.ResourceType.PATIENT;
import static org.linuxforhealth.fhir.core.ResourceType.PROVENANCE;
import static org.linuxforhealth.fhir.core.ResourceType.RESOURCE;
import static org.linuxforhealth.fhir.core.ResourceType.BINARY;
import static org.linuxforhealth.fhir.model.type.String.string;
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
import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.core.ResourceType;
import org.linuxforhealth.fhir.model.resource.Binary;
import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.Condition;
import org.linuxforhealth.fhir.model.resource.Observation;
import org.linuxforhealth.fhir.model.resource.Parameters;
import org.linuxforhealth.fhir.model.resource.Parameters.Parameter;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.resource.Practitioner;
import org.linuxforhealth.fhir.model.resource.Provenance;
import org.linuxforhealth.fhir.model.test.TestUtil;
import org.linuxforhealth.fhir.model.type.Element;
import org.linuxforhealth.fhir.model.type.Id;
import org.linuxforhealth.fhir.model.type.Instant;
import org.linuxforhealth.fhir.model.type.Meta;
import org.linuxforhealth.fhir.model.type.Reference;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.BundleType;
import org.linuxforhealth.fhir.model.type.code.HTTPVerb;
import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.persistence.context.FHIRPersistenceEvent;
import org.linuxforhealth.fhir.persistence.context.FHIRSystemHistoryContext;
import org.linuxforhealth.fhir.persistence.context.impl.FHIRSystemHistoryContextImpl;
import org.linuxforhealth.fhir.search.SearchConstants.Type;
import org.linuxforhealth.fhir.search.context.impl.FHIRSearchContextImpl;
import org.linuxforhealth.fhir.search.parameters.QueryParameter;
import org.linuxforhealth.fhir.search.parameters.QueryParameterValue;
import org.linuxforhealth.fhir.server.spi.interceptor.FHIRPersistenceInterceptorException;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationContext;
import org.linuxforhealth.fhir.smart.AuthzPolicyEnforcementPersistenceInterceptor;
import org.linuxforhealth.fhir.smart.Scope.Permission;

public class AuthzPolicyEnforcementTest {
    private static final String PATIENT_ID =     "11111111-1111-1111-1111-111111111111";
    private static final String OBSERVATION_ID = "11111111-1111-1111-1111-111111111111";
    private static final String CONDITION_ID =   "11111111-1111-1111-1111-111111111111";
    private static final String PATIENT_PROVENANCE_ID =   "11111111-1111-1111-1111-111111111111";
    private static final String OBSERVATION_PROVENANCE_ID =   "11111111-1111-1111-1111-111111111111";

    private static final List<Permission> READ_APPROVED = Arrays.asList(Permission.READ, Permission.ALL);
    private static final List<Permission> WRITE_APPROVED = Arrays.asList(Permission.WRITE, Permission.ALL);

    private AuthzPolicyEnforcementPersistenceInterceptor interceptor;
    private Patient patient;
    private Observation observation;
    private Condition condition;
    private Provenance patientProvenance;
    private Provenance observationProvenance;
    private Map<String, Object> properties;
    private Binary binary;
    private Binary binaryWithSecurityContext;


    @BeforeClass
    public void setup() throws Exception {
        System.setProperty("java.util.logging.config.file", ClassLoader.getSystemResource("logging.properties").getPath());

        FHIRRequestContext requestContext = new FHIRRequestContext("default");
        FHIRRequestContext.set(requestContext);

        interceptor = new AuthzPolicyEnforcementPersistenceInterceptor();

        Provenance provenanceBase = TestUtil.getMinimalResource(Provenance.class);

        patient = TestUtil.getMinimalResource(Patient.class);
        patient = patient.toBuilder()
                .id(PATIENT_ID)
                .meta(Meta.builder().lastUpdated(Instant.now()).versionId(Id.of("1")).build())
                .build();

        patientProvenance = provenanceBase.toBuilder()
                .id(PATIENT_PROVENANCE_ID)
                .target(Reference.builder()
                    .reference(string("Patient/" + PATIENT_ID))
                    .build())
                .meta(Meta.builder().lastUpdated(Instant.now()).versionId(Id.of("1")).build())
                .build();

        observation = TestUtil.getMinimalResource(Observation.class);
        observation = observation.toBuilder()
                .id(OBSERVATION_ID)
                .subject(Reference.builder().reference(string("Patient/" + PATIENT_ID)).build())
                .encounter(Reference.builder().reference(string("Encounter/" + MockPersistenceImpl.ENCOUNTER_ID_GOOD)).build())
                .meta(Meta.builder().lastUpdated(Instant.now()).versionId(Id.of("1")).build())
                .build();

        observationProvenance = provenanceBase.toBuilder()
                .id(OBSERVATION_PROVENANCE_ID)
                .target(Reference.builder()
                    .reference(string("Observation/" + OBSERVATION_ID))
                    .build())
                .meta(Meta.builder().lastUpdated(Instant.now()).versionId(Id.of("1")).build())
                .build();

        condition = TestUtil.getMinimalResource(Condition.class);
        condition = condition.toBuilder()
                .id(CONDITION_ID)
                .subject(Reference.builder()
                    .reference(string("Patient/" + PATIENT_ID + "/_history/1"))
                    .build())
                .meta(Meta.builder().lastUpdated(Instant.now()).versionId(Id.of("1")).build())
                .build();

        binary = TestUtil.getMinimalResource(Binary.class);
        binaryWithSecurityContext = TestUtil.readExampleResource("json/spec/binary-example.json");
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

        // Test create Binary Resource which does not have a securityContext. Should Succeed
        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Binary");
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(binary, properties);
            interceptor.beforeCreate(event);
            assertTrue(shouldSucceed(resourceTypesPermittedByScope, BINARY, WRITE_APPROVED, permission));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceTypesPermittedByScope, BINARY, WRITE_APPROVED, permission));
        }

        // Test create Binary Resource which has a securityContext. Should Fail since securityContext is not supported.
        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Binary");
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(binaryWithSecurityContext, properties);
            interceptor.beforeCreate(event);
            fail("Did not receive the expected FHIRPersistenceInterceptorException");
        } catch (FHIRPersistenceInterceptorException e) {
            if (shouldSucceed(resourceTypesPermittedByScope, BINARY, WRITE_APPROVED, permission)) {
                assertTrue(e.getMessage().equals("securityContext is not supported for resource type Binary"));
            }
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

        // Test update Binary Resource which does not have a securityContext. Should Succeed
        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Binary");
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(binary, properties);
            event.setPrevFhirResource(binary);
            interceptor.beforeUpdate(event);
            assertTrue(shouldSucceed(resourceTypesPermittedByScope, BINARY, READ_APPROVED, permission) &&
                        shouldSucceed(resourceTypesPermittedByScope, BINARY, WRITE_APPROVED, permission));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceTypesPermittedByScope, BINARY, READ_APPROVED, permission) &&
                        shouldSucceed(resourceTypesPermittedByScope, BINARY, WRITE_APPROVED, permission));
        }

        // Test update Binary Resource which has a securityContext. Should Fail since securityContext is not supported.
        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Binary");
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(binaryWithSecurityContext, properties);
            event.setPrevFhirResource(binaryWithSecurityContext);
            interceptor.beforeUpdate(event);
            fail("Did not receive the expected FHIRPersistenceInterceptorException");
        } catch (FHIRPersistenceInterceptorException e) {
            if (shouldSucceed(resourceTypesPermittedByScope, BINARY, READ_APPROVED, permission) &&
                    shouldSucceed(resourceTypesPermittedByScope, BINARY, WRITE_APPROVED, permission)) {
                assertTrue(e.getMessage().equals("securityContext is not supported for resource type Binary"));
            }
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
        event.setFhirResource(null);

        try {
            interceptor.beforeRead(event);
            interceptor.afterRead(event);
            fail("Patient read was allowed but should not be");
        } catch (FHIRPersistenceInterceptorException e) {
            // success
        }

        try {
            interceptor.beforeVread(event);
            interceptor.afterVread(event);
            fail("Patient vread was allowed but should not be");
        } catch (FHIRPersistenceInterceptorException e) {
            // success
        }

        try {
            interceptor.beforeHistory(event);
            event.setFhirResource(Bundle.builder()
                    .type(BundleType.HISTORY)
                    .build());
            interceptor.afterHistory(event);
            fail("Patient history was allowed but should not be");
        } catch (FHIRPersistenceInterceptorException e) {
            // success
        }
    }

    @Test
    public void testUserInteraction() throws FHIRPersistenceInterceptorException {
        // don't use buildRequestHeaders because we want to test without a "patient_id" claim
        Map<String, List<String>> requestHeaders = new LinkedHashMap<String, List<String>>();
        List<String> authHeader = Collections.singletonList("Bearer " + JWT.create()
                .withClaim("scope", "user/Patient.read")
                .sign(Algorithm.none()));
        requestHeaders.put("Authorization", authHeader);
        FHIRRequestContext.get().setHttpHeaders(requestHeaders);

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

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient");
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID, PATIENT_ID);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
            interceptor.beforeRead(event);
            interceptor.beforeVread(event);
            interceptor.beforeHistory(event);
        } catch (FHIRPersistenceInterceptorException e) {
            // success
        }
    }

    @Test
    public void testBeforeSearch_systemLevel() throws FHIRPersistenceInterceptorException {
        FHIRSearchContextImpl searchContext = new FHIRSearchContextImpl();

        // Valid system-level search
        try {
            FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders("user/*.read", PATIENT_ID));

            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Resource");
            properties.put(FHIRPersistenceEvent.PROPNAME_SEARCH_CONTEXT_IMPL, searchContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(null, properties);
            interceptor.beforeSearch(event);
        } catch (FHIRPersistenceInterceptorException e) {
            fail("System search interaction was not allowed but should have been", e);
        }

        // Invalid system-level search
        try {
            FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders("patient/*.read", PATIENT_ID));

            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Resource");
            properties.put(FHIRPersistenceEvent.PROPNAME_SEARCH_CONTEXT_IMPL, searchContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(null, properties);
            interceptor.beforeSearch(event);
            fail("System search interaction was allowed but should not be");
        } catch (FHIRPersistenceInterceptorException e) {
            // success
            assertEquals(e.getIssues().size(), 1);
            assertEquals(e.getIssues().get(0).getCode(), IssueType.FORBIDDEN);
            assertEquals(e.getIssues().get(0).getDetails().getText().getValue(),
                    "Whole-system interactions require a user or system scope with a wildcard resource type: ('user'|'system') '/' '*' '.' ('read'|'*')");
        }

        // Valid system-level search with types
        try {
            FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders("system/*.read", PATIENT_ID));
            searchContext.setSearchResourceTypes(List.of("Patient","Observation"));

            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Resource");
            properties.put(FHIRPersistenceEvent.PROPNAME_SEARCH_CONTEXT_IMPL, searchContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(null, properties);
            interceptor.beforeSearch(event);
        } catch (FHIRPersistenceInterceptorException e) {
            fail("System search interaction was not allowed but should have been", e);
        }

        // Valid system-level search with types
        try {
            FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders("system/Patient.read system/Observation.read", PATIENT_ID));
            searchContext.setSearchResourceTypes(List.of("Patient","Observation"));

            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Resource");
            properties.put(FHIRPersistenceEvent.PROPNAME_SEARCH_CONTEXT_IMPL, searchContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(null, properties);
            interceptor.beforeSearch(event);
        } catch (FHIRPersistenceInterceptorException e) {
            fail("System search interaction was not allowed but should have been", e);
        }

        // Invalid system-level search with types
        try {
            FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders("patient/Patient.read patient/Observation.read", PATIENT_ID));
            searchContext.setSearchResourceTypes(List.of("Patient","Observation"));

            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Resource");
            properties.put(FHIRPersistenceEvent.PROPNAME_SEARCH_CONTEXT_IMPL, searchContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(null, properties);
            interceptor.beforeSearch(event);
            fail("System search interaction was allowed but should not be");
        } catch (FHIRPersistenceInterceptorException e) {
            // success
            assertEquals(e.getIssues().size(), 1);
            assertEquals(e.getIssues().get(0).getCode(), IssueType.FORBIDDEN);
            assertEquals(e.getIssues().get(0).getDetails().getText().getValue(),
                    "'patient' scoped access tokens are not supported for system-level interactions against patient compartment resource types like Patient");
        }

        // Valid system-level search for non-patient-compartment resource type
        try {
            FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders("patient/Practitioner.read", PATIENT_ID));
            searchContext.setSearchResourceTypes(List.of("Practitioner"));

            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Resource");
            properties.put(FHIRPersistenceEvent.PROPNAME_SEARCH_CONTEXT_IMPL, searchContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(null, properties);
            interceptor.beforeSearch(event);
        } catch (FHIRPersistenceInterceptorException e) {
            fail("System search interaction was not allowed but should have been", e);
        }
    }

    @Test(dataProvider = "scopeStringForSearch")
    public void testBeforeSearch(String scopeString, List<String> contextIds, Set<ResourceType> implicitCompartmentScopeResourceTypes)
            throws FHIRPersistenceInterceptorException {

        FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders(scopeString, PATIENT_ID));

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
            if (implicitCompartmentScopeResourceTypes.contains(OBSERVATION)) {
                fail("Patient compartment interaction was allowed but should not be");
            }
        } catch (FHIRPersistenceInterceptorException e) {
            if (implicitCompartmentScopeResourceTypes.contains(OBSERVATION)) {
                // success
                assertEquals(e.getIssues().size(), 1);
                assertEquals(e.getIssues().get(0).getCode(), IssueType.FORBIDDEN);
                assertEquals(e.getIssues().get(0).getDetails().getText().getValue(),
                        "Interaction with 'Patient/bogus' is not permitted for patient context [" + PATIENT_ID + "]");
            } else {
                fail("Patient compartment interaction was not allowed but should have been", e);
            }
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
            if (implicitCompartmentScopeResourceTypes.contains(OBSERVATION)) {
                fail("Encounter compartment interaction was allowed but should not be");
            }
        } catch (FHIRPersistenceInterceptorException e) {
            if (implicitCompartmentScopeResourceTypes.contains(OBSERVATION)) {
                // success
                assertEquals(e.getIssues().size(), 1);
                assertEquals(e.getIssues().get(0).getCode(), IssueType.FORBIDDEN);
                assertEquals(e.getIssues().get(0).getDetails().getText().getValue(),
                    "Interaction with 'Encounter/" + MockPersistenceImpl.ENCOUNTER_ID_BAD + "' is not permitted for patient context [" + PATIENT_ID + "]");
            } else {
                fail("Encounter compartment interaction was not allowed but should have been", e);
            }
        }

        // Invalid compartment search: Encounter compartment resource does not exist
        try {
            queryParameterValue.setValueString("Encounter/bogus");
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Observation");
            properties.put(FHIRPersistenceEvent.PROPNAME_SEARCH_CONTEXT_IMPL, searchContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observation, properties);
            interceptor.beforeSearch(event);
            if (implicitCompartmentScopeResourceTypes.contains(OBSERVATION)) {
                fail("Encounter compartment interaction was allowed but should not be");
            }
        } catch (FHIRPersistenceInterceptorException e) {
            if (implicitCompartmentScopeResourceTypes.contains(OBSERVATION)) {
                // success
                assertEquals(e.getIssues().size(), 1);
                assertEquals(e.getIssues().get(0).getCode(), IssueType.FORBIDDEN);
                assertEquals(e.getIssues().get(0).getDetails().getText().getValue(),
                        "Interaction with 'Encounter/bogus' is not permitted for patient context [11111111-1111-1111-1111-111111111111]");
            } else {
                fail("Encounter compartment interaction was not allowed but should have been", e);
            }
        }

        // Invalid compartment search: Device compartment
        try {
            queryParameterValue.setValueString("Device/" + PATIENT_ID);
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Observation");
            properties.put(FHIRPersistenceEvent.PROPNAME_SEARCH_CONTEXT_IMPL, searchContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observation, properties);
            interceptor.beforeSearch(event);
            if (implicitCompartmentScopeResourceTypes.contains(OBSERVATION)) {
                fail("Device compartment interaction was allowed but should not be");
            }
        } catch (FHIRPersistenceInterceptorException e) {
            if (implicitCompartmentScopeResourceTypes.contains(OBSERVATION)) {
                // success
                assertEquals(e.getIssues().size(), 1);
                assertEquals(e.getIssues().get(0).getCode(), IssueType.FORBIDDEN);
                assertEquals(e.getIssues().get(0).getDetails().getText().getValue(),
                        "Compartment search for compartment type 'Device' is not permitted.");
            } else {
                fail("Device compartment interaction was not allowed but should have been", e);
            }
        }

        // Invalid compartment search: Practitioner compartment
        try {
            queryParameterValue.setValueString("Practitioner/" + PATIENT_ID);
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Observation");
            properties.put(FHIRPersistenceEvent.PROPNAME_SEARCH_CONTEXT_IMPL, searchContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observation, properties);
            interceptor.beforeSearch(event);
            if (implicitCompartmentScopeResourceTypes.contains(OBSERVATION)) {
                fail("Practitioner compartment interaction was allowed but should not be");
            }
        } catch (FHIRPersistenceInterceptorException e) {
            if (implicitCompartmentScopeResourceTypes.contains(OBSERVATION)) {
                // success
                assertEquals(e.getIssues().size(), 1);
                assertEquals(e.getIssues().get(0).getCode(), IssueType.FORBIDDEN);
                assertEquals(e.getIssues().get(0).getDetails().getText().getValue(),
                        "Compartment search for compartment type 'Practitioner' is not permitted.");
            } else {
                fail("Practitioner compartment interaction was not allowed but should have been", e);
            }
        }

        // Invalid compartment search: RelatedPerson compartment
        try {
            queryParameterValue.setValueString("RelatedPerson/" + PATIENT_ID);
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Observation");
            properties.put(FHIRPersistenceEvent.PROPNAME_SEARCH_CONTEXT_IMPL, searchContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observation, properties);
            interceptor.beforeSearch(event);
            if (implicitCompartmentScopeResourceTypes.contains(OBSERVATION)) {
                fail("RelatedPerson compartment interaction was allowed but should not be");
            }
        } catch (FHIRPersistenceInterceptorException e) {
            if (implicitCompartmentScopeResourceTypes.contains(OBSERVATION)) {
                // success
                assertEquals(e.getIssues().size(), 1);
                assertEquals(e.getIssues().get(0).getCode(), IssueType.FORBIDDEN);
                assertEquals(e.getIssues().get(0).getDetails().getText().getValue(),
                        "Compartment search for compartment type 'RelatedPerson' is not permitted.");
            } else {
                fail("RelatedPerson compartment interaction was not allowed but should have been", e);
            }
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
                "read permission for 'AllergyIntolerance' is not granted by any of the provided SMART scopes: " +
                "[[" + scopeString.replace(" ", ", ") + "]]");
        }

        // Valid non-compartment search: converted to Patient compartment search
        try {
            searchContext = new FHIRSearchContextImpl();
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Observation");
            properties.put(FHIRPersistenceEvent.PROPNAME_SEARCH_CONTEXT_IMPL, searchContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observation, properties);
            interceptor.beforeSearch(event);
            if (implicitCompartmentScopeResourceTypes.contains(OBSERVATION)) {
                assertEquals(searchContext.getSearchParameters().size(), 1);
                for (QueryParameter searchParameter : searchContext.getSearchParameters()) {
                    assertTrue(searchParameter.isInclusionCriteria());
                    assertEquals(searchParameter.getValues().get(0).getValueString(), "Patient/" + PATIENT_ID);
                }
            } else {
                assertEquals(searchContext.getSearchParameters().size(), 0);
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
            if (implicitCompartmentScopeResourceTypes.contains(OBSERVATION)) {
                assertEquals(searchContext.getSearchParameters().size(), 2);
                List<QueryParameter> searchParms = searchContext.getSearchParameters();
                QueryParameter compartmentSearchParm = searchParms.get(0);
                assertTrue("internal-Patient-Compartment".equals(compartmentSearchParm.getCode()));
                assertEquals(compartmentSearchParm.getType(), Type.REFERENCE);
                assertTrue(compartmentSearchParm.isInclusionCriteria());
                assertFalse(compartmentSearchParm.isChained());
                assertEquals(compartmentSearchParm.getValues().size(), 1);
                assertEquals(compartmentSearchParm.getValues().get(0).getValueString(), "Patient/11111111-1111-1111-1111-111111111111");
                compartmentSearchParm = compartmentSearchParm.getNextParameter();
                assertEquals(searchParms.get(1).getCode(), "status");
                assertEquals(searchParms.get(1).getValues().get(0).getValueCode(), "final");
            } else {
                assertEquals(searchContext.getSearchParameters().size(), 1);
            }
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
            if (implicitCompartmentScopeResourceTypes.contains(PATIENT)) {
                assertEquals(searchContext.getSearchParameters().size(), 1);
                for (QueryParameter searchParameter : searchContext.getSearchParameters()) {
                    assertTrue(searchParameter.isInclusionCriteria());
                    assertEquals(searchParameter.getValues().get(0).getValueString(), "Patient/" + PATIENT_ID);
                }
            } else {
                assertEquals(searchContext.getSearchParameters().size(), 0);
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
                "read permission for 'AllergyIntolerance' is not granted by any of the provided SMART scopes: " +
                "[[" + scopeString.replace(" ", ", ") + "]]");
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
                "read permission for 'Medication' is not granted by any of the provided SMART scopes: " +
                "[[" + scopeString.replace(" ", ", ") + "]]");
        }
    }

    @Test(dataProvider = "scopeStringProvider")
    public void testRead(String scopeString, List<String> contextIds, Set<ResourceType> resourceTypesPermittedByScope, Permission permission) {
        FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders(scopeString, contextIds));

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient");
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID, PATIENT_ID);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
            interceptor.afterRead(event);
            assertTrue(shouldSucceed(resourceTypesPermittedByScope, PATIENT, READ_APPROVED, permission));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceTypesPermittedByScope, PATIENT, READ_APPROVED, permission), e.getMessage());
        }

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Observation");
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID, OBSERVATION_ID);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observation, properties);
            interceptor.afterRead(event);
            assertTrue(shouldSucceed(resourceTypesPermittedByScope, OBSERVATION, READ_APPROVED, permission));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceTypesPermittedByScope, OBSERVATION, READ_APPROVED, permission));
        }

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Condition");
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID, CONDITION_ID);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(condition, properties);
            interceptor.afterRead(event);
            assertTrue(shouldSucceed(resourceTypesPermittedByScope, CONDITION, READ_APPROVED, permission));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceTypesPermittedByScope, CONDITION, READ_APPROVED, permission));
        }

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Provenance");
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID, PATIENT_PROVENANCE_ID);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(patientProvenance, properties);
            interceptor.afterRead(event);
            assertTrue(shouldSucceed(resourceTypesPermittedByScope, PROVENANCE, READ_APPROVED, permission));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(resourceTypesPermittedByScope, PROVENANCE, READ_APPROVED, permission));
        }

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Provenance");
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID, OBSERVATION_PROVENANCE_ID);
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
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID, PATIENT_ID);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(patient, properties);
            interceptor.afterVread(event);
            assertTrue(shouldSucceed(typesPermittedByScopes, PATIENT, READ_APPROVED, permission));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(typesPermittedByScopes, PATIENT, READ_APPROVED, permission));
        }

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Observation");
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID, OBSERVATION_ID);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observation, properties);
            interceptor.afterVread(event);
            assertTrue(shouldSucceed(typesPermittedByScopes, OBSERVATION, READ_APPROVED, permission));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(typesPermittedByScopes, OBSERVATION, READ_APPROVED, permission));
        }

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Condition");
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID, CONDITION_ID);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(condition, properties);
            interceptor.afterVread(event);
            assertTrue(shouldSucceed(typesPermittedByScopes, CONDITION, READ_APPROVED, permission));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(typesPermittedByScopes, CONDITION, READ_APPROVED, permission));
        }

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Provenance");
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID, PATIENT_PROVENANCE_ID);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(patientProvenance, properties);
            interceptor.afterVread(event);
            assertTrue(shouldSucceed(typesPermittedByScopes, PROVENANCE, READ_APPROVED, permission));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(typesPermittedByScopes, PROVENANCE, READ_APPROVED, permission));
        }

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Provenance");
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID, OBSERVATION_PROVENANCE_ID);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observationProvenance, properties);
            interceptor.afterVread(event);
            assertTrue(shouldSucceed(typesPermittedByScopes, PROVENANCE, READ_APPROVED, permission));
        } catch (FHIRPersistenceInterceptorException e) {
            assertFalse(shouldSucceed(typesPermittedByScopes, PROVENANCE, READ_APPROVED, permission));
        }
    }

    @Test
    public void testBeforeHistory() throws FHIRPersistenceInterceptorException {

        // Valid system-level history
        try {
            FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders("system/*.read", PATIENT_ID));
            FHIRSystemHistoryContext systemHistoryContext = new FHIRSystemHistoryContextImpl();

            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Resource");
            properties.remove(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID);
            properties.put(FHIRPersistenceEvent.PROPNAME_SYSTEM_HISTORY_CONTEXT_IMPL, systemHistoryContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observation, properties);
            interceptor.beforeHistory(event);
        } catch (FHIRPersistenceInterceptorException e) {
            fail("System history interaction was not allowed but should have been", e);
        }

        // Invalid system-level history
        try {
            FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders("patient/*.read", PATIENT_ID));
            FHIRSystemHistoryContext systemHistoryContext = new FHIRSystemHistoryContextImpl();

            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Resource");
            properties.remove(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID);
            properties.put(FHIRPersistenceEvent.PROPNAME_SYSTEM_HISTORY_CONTEXT_IMPL, systemHistoryContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observation, properties);
            interceptor.beforeHistory(event);
            fail("System history interaction was allowed but should not be");
        } catch (FHIRPersistenceInterceptorException e) {
            // success
            assertEquals(e.getIssues().size(), 1);
            assertEquals(e.getIssues().get(0).getCode(), IssueType.FORBIDDEN);
            assertEquals(e.getIssues().get(0).getDetails().getText().getValue(),
                    "Whole-system interactions require a user or system scope with a wildcard resource type: ('user'|'system') '/' '*' '.' ('read'|'*')");
        }

        // Valid system-level history with types
        try {
            FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders("system/*.read", PATIENT_ID));
            FHIRSystemHistoryContextImpl systemHistoryContext = new FHIRSystemHistoryContextImpl();
            systemHistoryContext.addResourceType("Patient");
            systemHistoryContext.addResourceType("Observation");

            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Resource");
            properties.remove(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID);
            properties.put(FHIRPersistenceEvent.PROPNAME_SYSTEM_HISTORY_CONTEXT_IMPL, systemHistoryContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observation, properties);
            interceptor.beforeHistory(event);
        } catch (FHIRPersistenceInterceptorException e) {
            fail("System history interaction was not allowed but should have been", e);
        }

        // Valid system-level history with types
        try {
            FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders("system/Patient.read system/Observation.read", PATIENT_ID));
            FHIRSystemHistoryContextImpl systemHistoryContext = new FHIRSystemHistoryContextImpl();
            systemHistoryContext.addResourceType("Patient");
            systemHistoryContext.addResourceType("Observation");

            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Resource");
            properties.remove(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID);
            properties.put(FHIRPersistenceEvent.PROPNAME_SYSTEM_HISTORY_CONTEXT_IMPL, systemHistoryContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observation, properties);
            interceptor.beforeHistory(event);
        } catch (FHIRPersistenceInterceptorException e) {
            fail("System history interaction was not allowed but should have been", e);
        }

        // Invalid system-level history with types
        try {
            FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders("patient/Patient.read patient/Observation.read", PATIENT_ID));
            FHIRSystemHistoryContextImpl systemHistoryContext = new FHIRSystemHistoryContextImpl();
            systemHistoryContext.addResourceType("Patient");
            systemHistoryContext.addResourceType("Observation");

            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Resource");
            properties.remove(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID);
            properties.put(FHIRPersistenceEvent.PROPNAME_SYSTEM_HISTORY_CONTEXT_IMPL, systemHistoryContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observation, properties);
            interceptor.beforeHistory(event);
            fail("System history interaction was allowed but should not be");
        } catch (FHIRPersistenceInterceptorException e) {
            // success
            assertEquals(e.getIssues().size(), 1);
            assertEquals(e.getIssues().get(0).getCode(), IssueType.FORBIDDEN);
            assertEquals(e.getIssues().get(0).getDetails().getText().getValue(),
                    "'patient' scoped access tokens are not supported for system-level interactions against patient compartment resource types like Patient");
        }

        // Valid type-level history
        try {
            FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders("system/*.read", PATIENT_ID));
            FHIRSystemHistoryContext systemHistoryContext = new FHIRSystemHistoryContextImpl();

            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Observation");
            properties.remove(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID);
            properties.put(FHIRPersistenceEvent.PROPNAME_SYSTEM_HISTORY_CONTEXT_IMPL, systemHistoryContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observation, properties);
            interceptor.beforeHistory(event);
        } catch (FHIRPersistenceInterceptorException e) {
            fail("System history interaction was not allowed but should have been", e);
        }

        // Valid type-level history
        try {
            FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders("system/Observation.read", PATIENT_ID));
            FHIRSystemHistoryContext systemHistoryContext = new FHIRSystemHistoryContextImpl();

            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Observation");
            properties.remove(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID);
            properties.put(FHIRPersistenceEvent.PROPNAME_SYSTEM_HISTORY_CONTEXT_IMPL, systemHistoryContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observation, properties);
            interceptor.beforeHistory(event);
        } catch (FHIRPersistenceInterceptorException e) {
            fail("System history interaction was not allowed but should have been", e);
        }

        // Invalid type-level history
        try {
            FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders("patient/Observation.read", PATIENT_ID));
            FHIRSystemHistoryContext systemHistoryContext = new FHIRSystemHistoryContextImpl();

            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Observation");
            properties.remove(FHIRPersistenceEvent.PROPNAME_RESOURCE_ID);
            properties.put(FHIRPersistenceEvent.PROPNAME_SYSTEM_HISTORY_CONTEXT_IMPL, systemHistoryContext);
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(observation, properties);
            interceptor.beforeHistory(event);
            fail("System history interaction was allowed but should not be");
        } catch (FHIRPersistenceInterceptorException e) {
            // success
            assertEquals(e.getIssues().size(), 1);
            assertEquals(e.getIssues().get(0).getCode(), IssueType.FORBIDDEN);
            assertEquals(e.getIssues().get(0).getDetails().getText().getValue(),
                    "'patient' scoped access tokens are not supported for system-level interactions against patient compartment resource types like Observation");
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

    @Test
    public void testHistoryWithDeletes() {
        FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders("patient/*.read", PATIENT_ID));

        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Resource");
            Bundle historyBundle = Bundle.builder()
                    .type(BundleType.HISTORY)
                    .entry(Bundle.Entry.builder()
                            .fullUrl(Uri.of("Patient/" + PATIENT_ID))
                            .request(Bundle.Entry.Request.builder()
                                    .method(HTTPVerb.DELETE)
                                    .url(Uri.of("Patient/" + PATIENT_ID))
                                    .build())
                            .response(Bundle.Entry.Response.builder()
                                    .status("200")
                                    .etag("W/\"2\"")
                                    .lastModified(Instant.now())
                                    .build())
                            .build())
                    .entry(Bundle.Entry.builder()
                            .fullUrl(Uri.of("Patient/" + PATIENT_ID))
                            .request(Bundle.Entry.Request.builder()
                                    .method(HTTPVerb.POST)
                                    .url(Uri.of("Patient"))
                                    .build())
                            .response(Bundle.Entry.Response.builder()
                                    .status("200")
                                    .etag("W/\"1\"")
                                    .lastModified(Instant.now())
                                    .build())
                            .resource(patient)
                            .build())
                    .build();
            FHIRPersistenceEvent event = new FHIRPersistenceEvent(historyBundle, properties);
            interceptor.afterHistory(event);
        } catch (FHIRPersistenceInterceptorException e) {
            fail("Patient history was not allowed but should have been", e);
        }
    }

    @Test(dataProvider = "scopeStringPreferReturnMinimalProvider")
    public void testHistoryPreferReturnMinimal(String scopeString, List<String> contextIds, Set<ResourceType> resourceTypesPermittedByScope, Permission permission) {
        FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders(scopeString, contextIds));

        // Simulate when 'Prefer: return=minimal' HTTP header is used by only setting fullUrl and not resource in bundle
        try {
            properties.put(FHIRPersistenceEvent.PROPNAME_RESOURCE_TYPE, "Patient");
            Bundle historyBundle = Bundle.builder()
                    .type(BundleType.HISTORY)
                    .entry(Bundle.Entry.builder().fullUrl(Uri.of("Patient/" + PATIENT_ID)).build())
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
                    .entry(Bundle.Entry.builder().fullUrl(Uri.of("Observation/" + OBSERVATION_ID)).build())
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
                    .entry(Bundle.Entry.builder().fullUrl(Uri.of("Condition/" + CONDITION_ID)).build())
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

    @Test(dataProvider = "scopeStringPreferReturnMinimalProvider")
    public void testSearchPreferReturnMinimal(String scopeString, List<String> contextIds, Set<ResourceType> resourceTypesPermittedByScope, Permission permission) {
        FHIRRequestContext.get().setHttpHeaders(buildRequestHeaders(scopeString, contextIds));

        // Simulate when 'Prefer: return=minimal' HTTP header is used by only setting fullUrl and not resource in bundle
        try {
            Bundle searchBundle = Bundle.builder()
                    .type(BundleType.SEARCHSET)
                    .entry(Bundle.Entry.builder().fullUrl(Uri.of("Patient/" + PATIENT_ID)).build())
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
                    .entry(Bundle.Entry.builder().fullUrl(Uri.of("Observation/" + OBSERVATION_ID)).build())
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
                    .entry(Bundle.Entry.builder().fullUrl(Uri.of("Condition/" + CONDITION_ID)).build())
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
                    .entry(Bundle.Entry.builder().fullUrl(Uri.of("Patient/" + PATIENT_ID)).build())
                    .entry(Bundle.Entry.builder().fullUrl(Uri.of("Observation/" + OBSERVATION_ID)).build())
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
                    .entry(Bundle.Entry.builder().fullUrl(Uri.of("Patient/" + PATIENT_ID)).build())
                    .entry(Bundle.Entry.builder().fullUrl(Uri.of("Provenance/" + PATIENT_PROVENANCE_ID)).build())
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
                    .entry(Bundle.Entry.builder().fullUrl(Uri.of("Observation/" + OBSERVATION_ID)).build())
                    .entry(Bundle.Entry.builder().fullUrl(Uri.of("Provenance/" + OBSERVATION_PROVENANCE_ID)).build())
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
            {"patient/*.*", null, all_resources, null},
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

            {"user/*.*", null, all_resources, Permission.ALL},
            {"system/*.*", null, all_resources, Permission.ALL},
        };
    }

    @DataProvider(name = "scopeStringPreferReturnMinimalProvider")
    public static Object[][] scopeStringPreferReturnMinimal() {
        final Set<ResourceType> all_resources = Collections.singleton(RESOURCE);
        final Set<ResourceType> patient = Collections.singleton(PATIENT);
        final Set<ResourceType> observation = Collections.singleton(OBSERVATION);
        final Set<ResourceType> provenance = Collections.singleton(PROVENANCE);

        final List<String> CONTEXT_IDS = Collections.singletonList(PATIENT_ID);

        return new Object[][] {
            //String scopeString, String context, Set<ResourceType.Value> resourceTypesPermittedByScope, Permission permission

            {"patient/*.*", null, all_resources, null},
            // even though the scopes apply to many types, because the resource contents are missing
            // we can only assume compartment membership for the Patient resource (the "identity" of the compartment)
            // and the provenance that points to that
            {"patient/*.*", CONTEXT_IDS, union(patient, provenance), Permission.ALL},
            {"patient/*.read", CONTEXT_IDS, union(patient, provenance), Permission.READ},
            {"patient/*.write", CONTEXT_IDS, union(patient, provenance), Permission.WRITE},
            {"patient/Patient.* patient/Provenance.*", CONTEXT_IDS, union(patient, provenance), Permission.ALL},
            {"patient/Observation.* patient/Provenance.*", CONTEXT_IDS, Collections.EMPTY_SET, null},

            {"user/*.*", CONTEXT_IDS, all_resources, Permission.ALL},
            {"user/*.read", CONTEXT_IDS, all_resources, Permission.READ},
            {"user/*.write", CONTEXT_IDS, all_resources, Permission.WRITE},
            {"user/Patient.* user/Provenance.*", CONTEXT_IDS, union(patient, provenance), Permission.ALL},
            {"user/Observation.* user/Provenance.*", CONTEXT_IDS, union(observation, provenance), Permission.ALL},

            {"system/*.*", CONTEXT_IDS, all_resources, Permission.ALL},
            {"system/*.read", CONTEXT_IDS, all_resources, Permission.READ},
            {"system/*.write", CONTEXT_IDS, all_resources, Permission.WRITE},
            {"system/Patient.* system/Provenance.*", CONTEXT_IDS, union(patient, provenance), Permission.ALL},
            {"system/Observation.* system/Provenance.*", CONTEXT_IDS, union(observation, provenance), Permission.ALL},

            {"openid profile", CONTEXT_IDS, Collections.EMPTY_SET, null},

            {"user/*.*", null, all_resources, Permission.ALL},
            {"system/*.*", null, all_resources, Permission.ALL},
        };
    }

    @DataProvider(name = "scopeStringForSearch")
    public static Object[][] scopeStringsForSearch() {
        final Set<ResourceType> patient = Collections.singleton(PATIENT);
        final Set<ResourceType> observation = Collections.singleton(OBSERVATION);

        final List<String> CONTEXT_IDS = Collections.singletonList(PATIENT_ID);

        return new Object[][] {
            //String scopeString, String context, Set<ResourceType.Value> implicitCompartmentScopeResourceTypes
            {"patient/Patient.read patient/Observation.read patient/Practitioner.read", CONTEXT_IDS, union(patient, observation)},
            {"user/Patient.read user/Observation.read user/Practitioner.read", CONTEXT_IDS, Collections.EMPTY_SET},
            {"system/Patient.read system/Observation.read system/Practitioner.read", CONTEXT_IDS, Collections.EMPTY_SET},
        };
    }

    private static <T> Set<T> union(Set<T> a, Set<T> b) {
        return Stream.concat(a.stream(), b.stream()).collect(Collectors.toSet());
    }
}
