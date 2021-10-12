/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.profiles.uscore.v311;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.client.FHIRParameters;
import com.ibm.fhir.client.FHIRResponse;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.ig.us.core.tool.USCoreExamplesUtil;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Device;
import com.ibm.fhir.server.test.profiles.ProfilesTestBaseV2;

/**
 * Tests the US Core 3.1.1 Profile with Device.
 * https://www.hl7.org/fhir/us/core/StructureDefinition-us-core-implantable-device.html
 */
public class USCoreImplantableDeviceTest extends ProfilesTestBaseV2 {

    private static final String CLASSNAME = USCoreImplantableDeviceTest.class.getName();
    private static final Logger logger = Logger.getLogger(CLASSNAME);

    public Boolean skip = Boolean.TRUE;
    public Boolean DEBUG = Boolean.FALSE;

    private String deviceId1 = null;
    private String deviceId2 = null;
    private String deviceId3 = null;

    @Override
    public List<String> getRequiredProfiles() {
        //@formatter:off
        return Arrays.asList("http://hl7.org/fhir/us/core/StructureDefinition/us-core-implantable-device|3.1.1");
        //@formatter:on
    }

    @Override
    public void setCheck(Boolean check) {
        this.skip = check;
        if (skip) {
            logger.info("Skipping Tests for 'fhir-ig-us-core - Device', the profiles don't exist");
        }
    }

    @BeforeClass
    public void loadResources() throws Exception {
        if (!skip) {
            loadDevice1();
            loadDevice2();
            loadDevice3();
        }
    }

    @AfterClass
    public void deleteResources() throws Exception {
        if (!skip) {
            deleteDevice1();
            deleteDevice2();
            deleteDevice3();
        }
    }

    public void loadDevice1() throws Exception {
        String resource = "Device-udi-1.json";
        WebTarget target = getWebTarget();

        Device Device = USCoreExamplesUtil.readLocalJSONResource("311", resource);

        Entity<Device> entity = Entity.entity(Device, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Device").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        deviceId1 = getLocationLogicalId(response);
        response = target.path("Device/" + deviceId1).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    public void loadDevice2() throws Exception {
        String resource = "Device-udi-2.json";
        WebTarget target = getWebTarget();

        Device Device = USCoreExamplesUtil.readLocalJSONResource("311", resource);

        Entity<Device> entity = Entity.entity(Device, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Device").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        deviceId2 = getLocationLogicalId(response);
        response = target.path("Device/" + deviceId2).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    public void loadDevice3() throws Exception {
        String resource = "Device-udi-3.json";
        WebTarget target = getWebTarget();

        Device Device = USCoreExamplesUtil.readLocalJSONResource("311", resource);

        Entity<Device> entity = Entity.entity(Device, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Device").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        deviceId3 = getLocationLogicalId(response);
        response = target.path("Device/" + deviceId3).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    public void deleteDevice1() throws Exception {
        WebTarget target = getWebTarget();
        Response response = target.path("Device/" + deviceId1).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    public void deleteDevice2() throws Exception {
        WebTarget target = getWebTarget();
        Response response = target.path("Device/" + deviceId2).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    public void deleteDevice3() throws Exception {
        WebTarget target = getWebTarget();
        Response response = target.path("Device/" + deviceId3).request(FHIRMediaType.APPLICATION_FHIR_JSON).delete();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test
    public void testSearchForPatient() throws Exception {
        // SHALL support searching for all devices for a patient, including implantable devices using the patient search
        // parameter:
        // GET [base]/Device?patient=1137192
        if (!skip) {
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
    }

    @Test
    public void testSearchForPatientAndMultipleTypes() throws Exception {
        // SHOULD support searching using the combination of the patient and type search parameters:
        // GET [base]/Device?patient=[reference]&type={[system]}|[code]
        if (!skip) {
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
    }

    @Test
    public void testSearchForPatientAndType() throws Exception {
        // SHOULD support searching using the combination of the patient and type search parameters:
        // GET [base]/Device?patient=[reference]&type={[system]}|[code]
        if (!skip) {
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
}