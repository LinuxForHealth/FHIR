/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.util;

import static org.linuxforhealth.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.config.FHIRConfigHelper;
import org.linuxforhealth.fhir.config.FHIRConfiguration;
import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.config.PropertyGroup;
import org.linuxforhealth.fhir.config.ResourcesConfigAdapter;
import org.linuxforhealth.fhir.core.FHIRVersionParam;
import org.linuxforhealth.fhir.exception.FHIRException;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.type.HumanName;
import org.linuxforhealth.fhir.persistence.context.FHIRSystemHistoryContext;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;

/**
 * Unit tests for FHIRPersistenceUtil
 */
public class FHIRPersistenceUtilTest {

    @BeforeClass
    public void setUp() throws Exception {
        // TODO: it would be better for our unit tests if we could load config files from the classpath
        FHIRConfiguration.setConfigHome("target/test-classes");
    }

    /**
     * Test the injection of id and meta when the source resource does not have an id
     */
    @Test
    public void testCopyAndSetMetaNoId() {
        Patient patient = Patient.builder()
                .name(HumanName.builder()
                    .given(string("John"))
                    .family(string("Doe"))
                    .build())
                .build();

        final org.linuxforhealth.fhir.model.type.Instant lastUpdated = FHIRPersistenceUtil.getUpdateTime();
        final String logicalId = "one";
        Patient prepared = FHIRPersistenceUtil.copyAndSetResourceMetaFields(patient, logicalId, 1, lastUpdated);

        assertEquals(prepared.getId(), logicalId);
        assertNotNull(prepared.getMeta());
        assertNotNull(prepared.getMeta().getVersionId());
        assertEquals(prepared.getMeta().getVersionId().getValue(), "1");
        assertNotNull(prepared.getMeta().getLastUpdated());
        assertEquals(prepared.getMeta().getLastUpdated(), lastUpdated);
    }

    /**
     * Test the injection of id and meta when the source resource has an id
     */
    @Test
    public void testCopyAndSetMetaId() {
        Patient patient = Patient.builder()
                .id("orig")
                .name(HumanName.builder()
                    .given(string("John"))
                    .family(string("Doe"))
                    .build())
                .build();

        final org.linuxforhealth.fhir.model.type.Instant lastUpdated = FHIRPersistenceUtil.getUpdateTime();
        final String logicalId = "one";
        Patient prepared = FHIRPersistenceUtil.copyAndSetResourceMetaFields(patient, logicalId, 1, lastUpdated);

        assertEquals(prepared.getId(), logicalId);
        assertNotNull(prepared.getMeta());
        assertNotNull(prepared.getMeta().getVersionId());
        assertEquals(prepared.getMeta().getVersionId().getValue(), "1");
        assertNotNull(prepared.getMeta().getLastUpdated());
        assertEquals(prepared.getMeta().getLastUpdated(), lastUpdated);
    }

    /**
     * Test the parsing of system history parameters into a HistoryContext
     * with no implicit or explicit _type filter.
     */
    @Test
    public void testParseSystemHistoryParameters() throws FHIRException {
        String originalTenantId = FHIRRequestContext.get().getTenantId();
        try {
            // change to a tenant (any tenant) that has `useImplicitTypeScopingForWholeSystemInteractions = false`
            FHIRRequestContext.get().setTenantId("all");

            // no explicit _type param
            Map<String, List<String>> params = new HashMap<>();
            FHIRSystemHistoryContext historyContext = FHIRPersistenceUtil.parseSystemHistoryParameters(params, false, newResourcesConfigAdapter());

            assertTrue(historyContext.getResourceTypes().isEmpty(), historyContext.getResourceTypes().toString());
        } finally {
            FHIRRequestContext.get().setTenantId(originalTenantId);
        }
    }

    /**
     * Test the parsing of system history parameters into a HistoryContext
     * with an implicit _type filter.
     */
    @Test
    public void testParseSystemHistoryParameters_implicitTypes() throws FHIRException {
        String originalTenantId = FHIRRequestContext.get().getTenantId();
        try {
            // change to a tenant (any tenant) that has `useImplicitTypeScopingForWholeSystemInteractions = true`
            FHIRRequestContext.get().setTenantId("default");

            // no explicit _type param
            Map<String, List<String>> params = new HashMap<>();
            FHIRSystemHistoryContext historyContext = FHIRPersistenceUtil.parseSystemHistoryParameters(params, false, newResourcesConfigAdapter());

            assertEquals(historyContext.getResourceTypes().size(), 141, "implicitly scoped to all R4B resource types");
        } finally {
            FHIRRequestContext.get().setTenantId(originalTenantId);
        }
    }

    /**
     * Test the parsing of system history parameters into a HistoryContext
     * with an explicit _type filter.
     */
    @Test
    public void testParseSystemHistoryParameters_explicitTypes() throws FHIRException {
        String originalTenantId = FHIRRequestContext.get().getTenantId();

        // add a _type parameter value with a list of resource types
        List<String> explicitTypes = Arrays.asList("Patient","Device","Observation","Condition","Medication");
        Map<String, List<String>> params = new HashMap<>();
        params.put("_type", Collections.singletonList(String.join(",",explicitTypes)));

        try {
            // change to a tenant (any tenant) that has `useImplicitTypeScopingForWholeSystemInteractions = true`
            FHIRRequestContext.get().setTenantId("default");
            FHIRSystemHistoryContext historyContext = FHIRPersistenceUtil.parseSystemHistoryParameters(params, false, newResourcesConfigAdapter());
            assertEquals(historyContext.getResourceTypes(), explicitTypes);

            // change to a tenant (any tenant) that has `useImplicitTypeScopingForWholeSystemInteractions = false`
            FHIRRequestContext.get().setTenantId("all");
            historyContext = FHIRPersistenceUtil.parseSystemHistoryParameters(params, false, newResourcesConfigAdapter());
            assertEquals(historyContext.getResourceTypes(), explicitTypes);
        } finally {
            FHIRRequestContext.get().setTenantId(originalTenantId);
        }
    }

    /**
     * Test the parsing of system history parameters into a HistoryContext
     * with no implicit or explicit _type filter.
     */
    @Test
    public void testParseSystemHistoryParameters_r4() throws FHIRException {
        String originalTenantId = FHIRRequestContext.get().getTenantId();
        try {
            // change to a tenant (any tenant) that has `useImplicitTypeScopingForWholeSystemInteractions = false`
            FHIRRequestContext.get().setTenantId("all");

            // no explicit _type param
            Map<String, List<String>> params = new HashMap<>();
            FHIRSystemHistoryContext historyContext = FHIRPersistenceUtil.parseSystemHistoryParameters(params, false, newResourcesConfigAdapter(FHIRVersionParam.VERSION_40));

            assertTrue(historyContext.getResourceTypes().isEmpty(), historyContext.getResourceTypes().toString());
        } finally {
            FHIRRequestContext.get().setTenantId(originalTenantId);
        }
    }

    /**
     * Test the parsing of system history parameters into a HistoryContext
     * with an implicit _type filter.
     */
    @Test
    public void testParseSystemHistoryParameters_implicitTypes_r4() throws FHIRException {
        String originalTenantId = FHIRRequestContext.get().getTenantId();
        try {
            // change to a tenant (any tenant) that has `useImplicitTypeScopingForWholeSystemInteractions = true`
            FHIRRequestContext.get().setTenantId("default");

            // no explicit _type param
            Map<String, List<String>> params = new HashMap<>();
            FHIRSystemHistoryContext historyContext = FHIRPersistenceUtil.parseSystemHistoryParameters(params, false, newResourcesConfigAdapter(FHIRVersionParam.VERSION_40));

            assertEquals(historyContext.getResourceTypes().size(), 126, "implicitly scoped to all R4 resource types");
        } finally {
            FHIRRequestContext.get().setTenantId(originalTenantId);
        }
    }

    /**
     * Test the parsing of system history parameters into a HistoryContext
     * with an explicit _type filter.
     */
    @Test
    public void testParseSystemHistoryParameters_explicitTypes_r4() throws FHIRException {
        String originalTenantId = FHIRRequestContext.get().getTenantId();

        // add a _type parameter value with a list of resource types
        List<String> explicitTypes = Arrays.asList("Patient","Device","Observation","Condition","Medication");
        Map<String, List<String>> params = new HashMap<>();
        params.put("_type", Collections.singletonList(String.join(",",explicitTypes)));

        try {
            // change to a tenant (any tenant) that has `useImplicitTypeScopingForWholeSystemInteractions = true`
            FHIRRequestContext.get().setTenantId("default");
            FHIRSystemHistoryContext historyContext = FHIRPersistenceUtil.parseSystemHistoryParameters(params, false, newResourcesConfigAdapter(FHIRVersionParam.VERSION_40));
            assertEquals(historyContext.getResourceTypes(), explicitTypes);

            // change to a tenant (any tenant) that has `useImplicitTypeScopingForWholeSystemInteractions = false`
            FHIRRequestContext.get().setTenantId("all");
            historyContext = FHIRPersistenceUtil.parseSystemHistoryParameters(params, false, newResourcesConfigAdapter(FHIRVersionParam.VERSION_40));
            assertEquals(historyContext.getResourceTypes(), explicitTypes);
        } finally {
            FHIRRequestContext.get().setTenantId(originalTenantId);
        }
    }

    /**
     * Test the parsing of system history parameters into a HistoryContext
     * when multiple _type parameters are specified.
     */
    @Test
    public void testParseSystemHistoryParameters_multipleTypeParam() throws FHIRException {
        String originalTenantId = FHIRRequestContext.get().getTenantId();

        // add a _type parameter value with a list of resource types
        List<String> explicitTypes1 = Arrays.asList("Patient","Device","Observation","Condition","Medication");
        List<String> explicitTypes2 = Arrays.asList("Practitioner","PractitionerRole","Organization");
        List<String> combinedExplicitTypes = Stream.concat(explicitTypes1.stream(), explicitTypes2.stream()).collect(Collectors.toList());

        Map<String, List<String>> params = new HashMap<>();
        params.put("_type", Arrays.asList(String.join(",",explicitTypes1), String.join(",", explicitTypes2)));

        try {
            // change to a tenant (any tenant) that has `useImplicitTypeScopingForWholeSystemInteractions = true`
            FHIRRequestContext.get().setTenantId("default");
            FHIRSystemHistoryContext historyContext = FHIRPersistenceUtil.parseSystemHistoryParameters(params, false, newResourcesConfigAdapter());
            assertEquals(historyContext.getResourceTypes(), combinedExplicitTypes);

            // change to a tenant (any tenant) that has `useImplicitTypeScopingForWholeSystemInteractions = false`
            FHIRRequestContext.get().setTenantId("all");
            historyContext = FHIRPersistenceUtil.parseSystemHistoryParameters(params, false, newResourcesConfigAdapter());
            assertEquals(historyContext.getResourceTypes(), combinedExplicitTypes);
        } finally {
            FHIRRequestContext.get().setTenantId(originalTenantId);
        }
    }

    @Test(expectedExceptions = FHIRPersistenceException.class)
    public void testParseSystemHistoryParameters_nullTypeParam() throws FHIRException {
        // no explicit _type param
        Map<String, List<String>> params = new HashMap<>();
        params.put("_type", null);
        FHIRSystemHistoryContext historyContext = FHIRPersistenceUtil.parseSystemHistoryParameters(params, false, newResourcesConfigAdapter());

        assertTrue(historyContext.getResourceTypes().isEmpty(), historyContext.getResourceTypes().toString());
    }

    @Test(expectedExceptions = FHIRPersistenceException.class)
    public void testParseSystemHistoryParameters_emptyTypeParam() throws FHIRException {
        // no explicit _type param
        Map<String, List<String>> params = new HashMap<>();
        params.put("_type", Collections.emptyList());
        FHIRSystemHistoryContext historyContext = FHIRPersistenceUtil.parseSystemHistoryParameters(params, false, newResourcesConfigAdapter());

        assertTrue(historyContext.getResourceTypes().isEmpty(), historyContext.getResourceTypes().toString());
    }

    private ResourcesConfigAdapter newResourcesConfigAdapter() {
        return newResourcesConfigAdapter(FHIRVersionParam.VERSION_43);
    }

    private ResourcesConfigAdapter newResourcesConfigAdapter(FHIRVersionParam fhirVersion) {
        PropertyGroup resourcesPG = FHIRConfigHelper.getPropertyGroup(FHIRConfiguration.PROPERTY_RESOURCES);
        return new ResourcesConfigAdapter(resourcesPG, fhirVersion);
    }
}
