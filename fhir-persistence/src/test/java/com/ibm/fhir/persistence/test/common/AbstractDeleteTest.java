/*
 * (C) Copyright IBM Corp. 2017, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.test.common;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertNull;

import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.Device;
import com.ibm.fhir.model.resource.Device.UdiCarrier;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.persistence.ResourceResult;
import com.ibm.fhir.persistence.SingleResourceResult;
import com.ibm.fhir.persistence.context.FHIRHistoryContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.fhir.persistence.exception.FHIRPersistenceResourceNotFoundException;
import com.ibm.fhir.persistence.util.FHIRPersistenceTestSupport;

/**
 * This class contains resource deletion tests.
 */
public abstract class AbstractDeleteTest extends AbstractPersistenceTest {
    protected String deviceId1;
    protected String deviceId2;

    @BeforeClass
    public void createResources() throws Exception {
        Device device = TestUtil.getMinimalResource(Device.class);

        Device device1 = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), device).getResource();
        assertNotNull(device1.getId());
        assertNotNull(device1.getMeta());
        assertNotNull(device1.getMeta().getVersionId().getValue());
        assertEquals("1", device1.getMeta().getVersionId().getValue());
        this.deviceId1 = device1.getId();

        Device device2 = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), device).getResource();
        assertNotNull(device2.getId());
        assertNotNull(device2.getMeta());
        assertNotNull(device2.getMeta().getVersionId().getValue());
        assertEquals("1", device2.getMeta().getVersionId().getValue());
        this.deviceId2 = device2.getId();
    }

    @Test(expectedExceptions = FHIRPersistenceResourceNotFoundException.class)
    public void testDeleteInvalidDevice() throws Exception {

        // When the persistence layer attempts to read the device it should kick
        // back with FHIRPersistenceResourceNotFoundException
        Device device = TestUtil.getMinimalResource(Device.class);
        device = FHIRPersistenceTestSupport.setIdAndMeta(persistence, device, "invalid-device-id", 1);
        delete(getDefaultPersistenceContext(), device);
    }

    @Test
    public void testReadInvalidDevice() throws Exception {
        SingleResourceResult<Device> result = persistence.read(getDefaultPersistenceContext(), Device.class, "invalid-device-id");
        assertNull(result.getResource());
    }

    @Test
    public void testDeleteValidDevice() throws Exception {

        // Try to delete the device created @BeforeClass
        Device device = TestUtil.getMinimalResource(Device.class);
        device = FHIRPersistenceTestSupport.setIdAndMeta(persistence, device, this.deviceId1, 1);
        delete(getDefaultPersistenceContext(), device);
    }

    @Test(dependsOnMethods = { "testDeleteValidDevice" })
    public void testReadDeletedDevice() throws Exception {
        SingleResourceResult<Device> readResult = persistence.read(getDefaultPersistenceContext(), Device.class, this.deviceId1);
        assertTrue(readResult.isSuccess());
        assertTrue(readResult.isDeleted());
        assertNull(readResult.getResource());
    }

    /**
     * Test that we can read a device after it has been deleted as long
     * as we set up the context correctly
     * @throws Exception
     */
    @Test
    public void testReadIncludeDeletedDevice() throws Exception {
        Device myDevice = TestUtil.getMinimalResource(Device.class);
        final String myDeviceId = UUID.randomUUID().toString();
        myDevice = FHIRPersistenceTestSupport.setIdAndMeta(persistence, myDevice, myDeviceId, 1);

        // try reading the device before it exists
        SingleResourceResult<? extends Resource> srr = persistence.read(getDefaultPersistenceContext(), Device.class, myDeviceId);
        assertNotNull(srr);
        assertFalse(srr.isSuccess());
        assertNull(srr.getResource());

        // Create the initial version
        srr = persistence.create(getDefaultPersistenceContext(), myDevice);
        assertNotNull(srr);
        assertTrue(srr.isSuccess());
        assertNotNull(srr.getResource());
        assertEquals("1", srr.getResource().getMeta().getVersionId().getValue());
        assertEquals(myDeviceId, srr.getResource().getId());

        // Delete the device
        delete(getDefaultPersistenceContext(), myDevice);

        // Try reading the deleted resource
        FHIRPersistenceContext context = FHIRPersistenceContextFactory.createPersistenceContext(null);
        srr = persistence.read(context, Device.class, myDeviceId);
        assertNotNull(srr);
        assertTrue(srr.isSuccess());
        assertNull(srr.getResource());
        assertTrue(srr.isDeleted());
        assertEquals(2, srr.getVersion());

        // Check that we can undelete it. New version will be 3
        myDevice = FHIRPersistenceTestSupport.setIdAndMeta(persistence, myDevice, myDeviceId, 3);
        persistence.update(getDefaultPersistenceContext(), myDevice);

        // Confirm we can read the undeleted resource
        srr = persistence.read(getDefaultPersistenceContext(), Device.class, myDeviceId);
        assertNotNull(srr);
        assertTrue(srr.isSuccess());
        assertNotNull(srr.getResource());
        assertEquals("3", srr.getResource().getMeta().getVersionId().getValue());
        assertEquals(3, srr.getVersion());
    }

    @Test(dependsOnMethods = { "testDeleteValidDevice" })
    public void testVReadNonDeletedDevice() throws Exception {

        // Retrieve version 1 of the resource. It should NOT be indicated as deleted in the history context.
        Device device = persistence.vread(getDefaultPersistenceContext(), Device.class, this.deviceId1, "1").getResource();
        assertNotNull(device);
        assertEquals(device.getId(), this.deviceId1);
        assertEquals(device.getMeta().getVersionId().getValue(), "1");
    }

    @Test(dependsOnMethods = { "testDeleteValidDevice" })
    public void testVReadDeletedDevice() throws Exception {

        // Retrieve version 2 which IS logically deleted.
        SingleResourceResult<Device> vreadResult = persistence.vread(getDefaultPersistenceContext(), Device.class, this.deviceId1, "2");
        assertTrue(vreadResult.isSuccess());
        assertTrue(vreadResult.isDeleted());
        assertNull(vreadResult.getResource());
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

        List<ResourceResult<? extends Resource>> resources = persistence.history(context, Device.class, this.deviceId1).getResourceResults();
        assertNotNull(resources);
        assertTrue(resources.size() == 2);
        List<ResourceResult<? extends Resource>> deletedResources = resources.stream().filter(ResourceResult::isDeleted).collect(Collectors.toList());

        assertNotNull(deletedResources);
        assertTrue(deletedResources.size() == 1);
        assertEquals(deletedResources.get(0).getLogicalId(), this.deviceId1);
        assertEquals(deletedResources.get(0).getVersion(), 2);
    }

    @Test(dependsOnMethods = { "testDeleteValidDevice" })
    public void testReDeleteValidDevice() throws Exception {

        Device device = TestUtil.getMinimalResource(Device.class);

        // Deleted version is 2, so the new delete request needs would be version 3
        // in order to get past the concurrent update check. But because the
        // resource is currently deleted, we expect the delete call to throw
        // an exception
        device = FHIRPersistenceTestSupport.setIdAndMeta(persistence, device, this.deviceId1, 3);
        delete(getDefaultPersistenceContext(), device);
    }

    @Test
    public void testUpdateDeletedDevice() throws Exception {

        FHIRHistoryContext historyContext = FHIRPersistenceContextFactory.createHistoryContext();
        FHIRPersistenceContext context = this.getPersistenceContextForHistory(historyContext);
        String updatedUdiValue = "updated-udi-value";

        // Read previously created device
        Device device = persistence.read(getDefaultPersistenceContext(), Device.class, this.deviceId2).getResource();

        // Delete previously created device
        FHIRPersistenceTestSupport.delete(persistence, getDefaultPersistenceContext(), device); // deviceId2

        // Update the meta so that the version matches
        final com.ibm.fhir.model.type.Instant lastUpdated = com.ibm.fhir.model.type.Instant.now(ZoneOffset.UTC);
        final int newVersionId = Integer.parseInt(device.getMeta().getVersionId().getValue()) + 2; // skip the version created by delete
        device = copyAndSetResourceMetaFields(device, device.getId(), newVersionId, lastUpdated);

        // Then update deleted device
        device = device.toBuilder().udiCarrier(UdiCarrier.builder().deviceIdentifier(string(updatedUdiValue)).build()).build();
        persistence.update(getDefaultPersistenceContext(), device);

        // Verify device history
        List<ResourceResult<? extends Resource>> resources = persistence.history(context, Device.class, this.deviceId2).getResourceResults();
        assertNotNull(resources);
        assertTrue(resources.size() == 3);
        List<ResourceResult<? extends Resource>> deletedResources = resources.stream().filter(ResourceResult::isDeleted).collect(Collectors.toList());
        assertEquals(deletedResources.size(), 1);
        assertEquals(deletedResources.get(0).getLogicalId(), this.deviceId2);
        assertEquals(deletedResources.get(0).getVersion(), 2);

        // Verify latest device
        Device latestDeviceVersion = resources.get(0).getResource().as(Device.class);
        assertEquals(latestDeviceVersion.getUdiCarrier().get(0).getDeviceIdentifier().getValue(), updatedUdiValue);
    }
}
