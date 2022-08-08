/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.test.profiles.uscore.v311;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.client.FHIRParameters;
import org.linuxforhealth.fhir.client.FHIRResponse;
import org.linuxforhealth.fhir.ig.us.core.tool.USCoreExamplesUtil;
import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.Medication;
import org.linuxforhealth.fhir.server.test.profiles.ProfilesTestBase.ProfilesTestBaseV2;

/**
 * Tests the US Core 3.1.1 Profile with Medication.
 * https://www.hl7.org/fhir/us/core/StructureDefinition-us-core-medication.html
 */
public class USCoreMedicationTest extends ProfilesTestBaseV2 {

    private String medicationId1 = null;
    private String medicationId2 = null;

    @Override
    public List<String> getRequiredProfiles() {
        return Arrays.asList("http://hl7.org/fhir/us/core/StructureDefinition/us-core-medication|3.1.1");
    }

    @Override
    public void loadResources() throws Exception {
        loadMedication1();
        loadMedication2();
    }

    public void loadMedication1() throws Exception {
        String resource = "Medication-uscore-med1.json";
        Medication medication = USCoreExamplesUtil.readLocalJSONResource("311", resource);
        medicationId1 = createResourceAndReturnTheLogicalId("Medication", medication);
    }

    public void loadMedication2() throws Exception {
        String resource = "Medication-uscore-med2.json";
        Medication medication = USCoreExamplesUtil.readLocalJSONResource("311", resource);
        medicationId2 = createResourceAndReturnTheLogicalId("Medication", medication);
    }

    @Test
    public void testSearchForCode() throws Exception {
        // There are no specific search terms for Medication, using the base spec.
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("code", "http://www.nlm.nih.gov/research/umls/rxnorm|206765,http://www.nlm.nih.gov/research/umls/rxnorm|582620");
        parameters.searchParam("_sort", "-_lastUpdated");
        FHIRResponse response = client.search(Medication.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, medicationId1);
        assertContainsIds(bundle, medicationId2);
    }
}