/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.test.common;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertNull;

import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.Device;
import com.ibm.fhir.model.resource.Device.UdiCarrier;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.persistence.SingleResourceResult;
import com.ibm.fhir.persistence.context.FHIRHistoryContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.fhir.persistence.exception.FHIRPersistenceResourceDeletedException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceResourceNotFoundException;

/**
 * This class contains resource deletion tests.
 * @author markd
 *
 */

public abstract class AbstractDeleteTest extends AbstractPersistenceTest {
    protected String deviceId1;
    protected String deviceId2;

    @BeforeClass
    public void createResources() throws Exception {
        Device device = TestUtil.getMinimalResource(Device.class);

        Device device1 = persistence.create(getDefaultPersistenceContext(), device).getResource();
        assertNotNull(device1.getId());
        assertNotNull(device1.getMeta());
        assertNotNull(device1.getMeta().getVersionId().getValue());
        assertEquals("1", device1.getMeta().getVersionId().getValue());
        this.deviceId1 = device1.getId();

        Device device2 = persistence.create(getDefaultPersistenceContext(), device).getResource();
        assertNotNull(device2.getId());
        assertNotNull(device2.getMeta());
        assertNotNull(device2.getMeta().getVersionId().getValue());
        assertEquals("1", device2.getMeta().getVersionId().getValue());
        this.deviceId2 = device2.getId();
    }

    @Test(expectedExceptions = FHIRPersistenceResourceNotFoundException.class)
    public void testDeleteInvalidDevice() throws Exception {

        SingleResourceResult<Device> result = persistence.delete(getDefaultPersistenceContext(), Device.class, "invalid-device-id");
        assertNull(result.getResource());
    }

    @Test
    public void testReadInvalidDevice() throws Exception {

        SingleResourceResult<Device> result = persistence.read(getDefaultPersistenceContext(), Device.class, "invalid-device-id");
        assertNull(result.getResource());
    }

    @Test
    public void testDeleteValidDevice() throws Exception {

        persistence.delete(getDefaultPersistenceContext(), Device.class, this.deviceId1);
    }

    @Test(dependsOnMethods = { "testDeleteValidDevice" },
                    expectedExceptions = FHIRPersistenceResourceDeletedException.class)
    public void testReadDeletedDevice() throws Exception {

        persistence.read(getDefaultPersistenceContext(), Device.class, this.deviceId1);

    }

    @Test(dependsOnMethods = { "testDeleteValidDevice" })
    public void testVReadNonDeletedDevice() throws Exception {

        // Retrieve version 1 of the resource. It should NOT be indicated as deleted in the history context.
        Device device = persistence.vread(getDefaultPersistenceContext(), Device.class, this.deviceId1, "1").getResource();
        assertNotNull(device);
        assertEquals(this.deviceId1, device.getId());
        assertEquals("1",device.getMeta().getVersionId().getValue());

    }

    @Test(dependsOnMethods = { "testDeleteValidDevice" },
            expectedExceptions = FHIRPersistenceResourceDeletedException.class)
    public void testVReadDeletedDevice() throws Exception {

        // Retrieve version 2 which IS logically deleted.
        persistence.vread(getDefaultPersistenceContext(), Device.class, this.deviceId1, "2");
    }

    @Test(dependsOnMethods = { "testDeleteValidDevice" })
    public void testSearchDeletedDevice() throws Exception {
        List<Resource> resources = runQueryTest(Device.class, "model", "eq:delete-test-model");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
    }

    @Test(dependsOnMethods = { "testDeleteValidDevice" })
    public void testHistoryDeletedDevice() throws Exception {
        FHIRHistoryContext historyContext = FHIRPersistenceContextFactory.createHistoryContext();
        FHIRPersistenceContext context = this.getPersistenceContextForHistory(historyContext);
        Map<String, List<Integer>> deletedResources;
        List<Integer> deletedVersions;

        List<Device> resources = persistence.history(context, Device.class, this.deviceId1).getResource();
        assertNotNull(resources);
        assertTrue(resources.size() == 2);
        deletedResources = historyContext.getDeletedResources();
        assertNotNull(deletedResources);
        assertTrue(deletedResources.size() == 1);
        assertNotNull(deletedResources.get(this.deviceId1));
        deletedVersions = deletedResources.get(this.deviceId1);
        assertEquals(1,deletedVersions.size());
        assertEquals(new Integer(2),deletedVersions.get(0));
    }

    @Test(dependsOnMethods = { "testDeleteValidDevice" })
    public void testReDeleteValidDevice() throws Exception {

        persistence.delete(getDefaultPersistenceContext(), Device.class, this.deviceId1);
    }

    @Test
    public void testUpdateDeletedDevice() throws Exception {

        FHIRHistoryContext historyContext = FHIRPersistenceContextFactory.createHistoryContext();
        FHIRPersistenceContext context = this.getPersistenceContextForHistory(historyContext);
        Map<String, List<Integer>> deletedResources;
        List<Integer> deletedVersions;
        String updatedUdiValue = "updated-udi-value";

        // Read previously created device
        Device device = persistence.read(getDefaultPersistenceContext(), Device.class, this.deviceId2).getResource();

        // Delete previously created device
        persistence.delete(getDefaultPersistenceContext(), Device.class, this.deviceId2);
        
        // Update the meta so that the version matches
        final com.ibm.fhir.model.type.Instant lastUpdated = com.ibm.fhir.model.type.Instant.now(ZoneOffset.UTC);
        final int newVersionId = Integer.parseInt(device.getMeta().getVersionId().getValue()) + 2; // skip the version created by delete
        device = copyAndSetResourceMetaFields(device, device.getId(), newVersionId, lastUpdated);

        // Then update deleted device
        device = device.toBuilder().udiCarrier(UdiCarrier.builder().deviceIdentifier(string(updatedUdiValue)).build()).build();
        persistence.updateWithMeta(getDefaultPersistenceContext(), device);

        // Verify device history
        List<Device> resources = persistence.history(context, Device.class, this.deviceId2).getResource();
        assertNotNull(resources);
        assertTrue(resources.size() == 3);
        deletedResources = historyContext.getDeletedResources();
        assertNotNull(deletedResources);
        assertTrue(deletedResources.size() == 1);
        assertNotNull(deletedResources.get(this.deviceId2));
        deletedVersions = deletedResources.get(this.deviceId2);
        assertEquals(1,deletedVersions.size());
        assertEquals(new Integer(2),deletedVersions.get(0));

        // Verify latest device
        Device latestDeviceVersion = resources.get(0);
        assertEquals(updatedUdiValue,latestDeviceVersion.getUdiCarrier().get(0).getDeviceIdentifier().getValue());
    }
}
