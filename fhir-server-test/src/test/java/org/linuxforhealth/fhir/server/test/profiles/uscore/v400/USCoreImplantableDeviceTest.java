/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.test.profiles.uscore.v400;

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
import org.linuxforhealth.fhir.model.resource.Device;
import org.linuxforhealth.fhir.server.test.profiles.ProfilesTestBase.ProfilesTestBaseV2;

/**
 * Tests the US Core 4.0.0 Profile with Device.
 * https://www.hl7.org/fhir/us/core/StructureDefinition-us-core-implantable-device.html
 */
public class USCoreImplantableDeviceTest extends ProfilesTestBaseV2 {

    private String deviceId1 = null;
    private String deviceId2 = null;
    private String deviceId3 = null;

    @Override
    public List<String> getRequiredProfiles() {
        return Arrays.asList("http://hl7.org/fhir/us/core/StructureDefinition/us-core-implantable-device|4.0.0");
    }

    @Override
    public void loadResources() throws Exception {
        loadDevice1();
        loadDevice2();
        loadDevice3();
    }

    public void loadDevice1() throws Exception {
        String resource = "Device-udi-1.json";
        Device device = USCoreExamplesUtil.readLocalJSONResource("400", resource);
        deviceId1 = createResourceAndReturnTheLogicalId("Device", device);
    }

    public void loadDevice2() throws Exception {
        String resource = "Device-udi-2.json";
        Device device = USCoreExamplesUtil.readLocalJSONResource("400", resource);
        deviceId2 = createResourceAndReturnTheLogicalId("Device", device);
    }

    public void loadDevice3() throws Exception {
        String resource = "Device-udi-3.json";
        Device device = USCoreExamplesUtil.readLocalJSONResource("400", resource);
        deviceId3 = createResourceAndReturnTheLogicalId("Device", device);
    }

    @Test
    public void testSearchForPatient() throws Exception {
        // SHALL support searching for all devices for a patient, including implantable devices using the patient search
        // parameter:
        // GET [base]/Device?patient=1137192
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/example");
        FHIRResponse response = client.search(Device.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, deviceId1);
        assertContainsIds(bundle, deviceId2);
        assertContainsIds(bundle, deviceId3);
    }

    @Test
    public void testSearchForPatientAndMultipleTypes() throws Exception {
        // SHOULD support searching using the combination of the patient and type search parameters:
        // GET [base]/Device?patient=[reference]&type={[system]}|[code]
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/example");
        parameters.searchParam("type", "http://snomed.info/sct|468063009,http://snomed.info/sct|19257004");
        FHIRResponse response = client.search(Device.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertContainsIds(bundle, deviceId1);
        assertContainsIds(bundle, deviceId2);
        assertDoesNotContainsIds(bundle, deviceId3);
    }

    @Test
    public void testSearchForPatientAndType() throws Exception {
        // SHOULD support searching using the combination of the patient and type search parameters:
        // GET [base]/Device?patient=[reference]&type={[system]}|[code]
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("patient", "Patient/example");
        parameters.searchParam("type", "19257004");
        FHIRResponse response = client.search(Device.class.getSimpleName(), parameters);
        assertSearchResponse(response, Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
        assertDoesNotContainsIds(bundle, deviceId1);
        assertContainsIds(bundle, deviceId2);
        assertDoesNotContainsIds(bundle, deviceId3);
    }
}