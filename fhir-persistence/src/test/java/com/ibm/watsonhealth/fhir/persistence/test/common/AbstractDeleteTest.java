/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.test.common;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.model.Device;
import com.ibm.watsonhealth.fhir.model.ObjectFactory;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRHistoryContext;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceResourceDeletedException;

/**
 * This class contains resource deletion tests.
 * @author markd
 *
 */

public abstract class AbstractDeleteTest extends AbstractPersistenceTest {
    protected ObjectFactory objFactory = new ObjectFactory();
    protected String deviceId;
    protected String deviceId1;
    
    @Test(groups = { "jdbc-normalized" })
    public void testCreateDevice() throws Exception {
        Device device = readResource(Device.class, "Device-delete-test.json");
        
        persistence.create(getDefaultPersistenceContext(), device);
        assertNotNull(device.getId());
        assertNotNull(device.getId().getValue());
        assertNotNull(device.getMeta());
        assertNotNull(device.getMeta().getVersionId().getValue());
        assertEquals("1", device.getMeta().getVersionId().getValue());
        this.deviceId = device.getId().getValue();
    }
    
    @Test(groups = { "jdbc-normalized" })
    public void testCreateDevice1() throws Exception {
        Device device = readResource(Device.class, "Device-delete-test1.json");
        
        persistence.create(getDefaultPersistenceContext(), device);
        assertNotNull(device.getId());
        assertNotNull(device.getId().getValue());
        assertNotNull(device.getMeta());
        assertNotNull(device.getMeta().getVersionId().getValue());
        assertEquals("1", device.getMeta().getVersionId().getValue());
        this.deviceId1 = device.getId().getValue();
    }
    
    @Test(groups = { "jdbc-normalized" }, dependsOnMethods = { "testCreateDevice" })
    public void testReadDevice() throws Exception {
    	
    	Device device = (Device) persistence.read(getDefaultPersistenceContext(), Device.class, this.deviceId);
    	assertNotNull(device);
    	assertEquals(this.deviceId, device.getId().getValue());
    	assertEquals("1",device.getMeta().getVersionId().getValue());
    }
    
    @Test(groups = { "jdbc-normalized" })
    public void testDeleteInvalidDevice() throws Exception {
    	
    	Resource result = persistence.delete(getDefaultPersistenceContext(), Device.class, "invalid-device-id");
    	assertNull(result);
    }
    
    @Test(groups = { "jdbc-normalized" })
    public void testReadInvalidDevice() throws Exception {
    	
    	Device device = (Device) persistence.read(getDefaultPersistenceContext(), Device.class, "invalid-device-id");
    	assertNull(device);
    }
    
    @Test(groups = { "jdbc-normalized" }, dependsOnMethods = { "testCreateDevice", "testReadDevice" })
    public void testDeleteValidDevice() throws Exception {
    	
    	persistence.delete(getDefaultPersistenceContext(), Device.class, this.deviceId);
    }
    
    @Test(groups = { "jdbc-normalized" }, dependsOnMethods = { "testDeleteValidDevice" }, 
    				expectedExceptions = FHIRPersistenceResourceDeletedException.class)
    public void testReadDeletedDevice() throws Exception {
    	
    	persistence.read(getDefaultPersistenceContext(), Device.class, this.deviceId);
     
    }
    
    @Test(groups = { "jdbc-normalized" }, dependsOnMethods = { "testDeleteValidDevice" })
    public void testVReadNonDeletedDevice() throws Exception {
	
    	// Retrieve version 1 of the resource. It should NOT be indicated as deleted in the history context.
    	Device device = (Device) persistence.vread(getDefaultPersistenceContext(), Device.class, this.deviceId, "1");
    	assertNotNull(device);
    	assertEquals(this.deviceId, device.getId().getValue());
    	assertEquals("1",device.getMeta().getVersionId().getValue());
    	    	
    }
    
    @Test(groups = { "jdbc-normalized" }, dependsOnMethods = { "testDeleteValidDevice" },
			expectedExceptions = FHIRPersistenceResourceDeletedException.class)
    public void testVReadDeletedDevice() throws Exception {
			
		// Retrieve version 2 which IS logically deleted.
		persistence.vread(getDefaultPersistenceContext(), Device.class, this.deviceId, "2");
	}
    
    @Test(groups = { "jdbc-normalized" }, dependsOnMethods = { "testDeleteValidDevice" })
    public void testSearchDeletedDevice() throws Exception {
		List<Resource> resources = runQueryTest(Device.class, persistence, "model", "eq:delete-test-model");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
    }
    
    @Test(groups = { "jdbc-normalized" }, dependsOnMethods = { "testDeleteValidDevice" })
    public void testHistoryDeletedDevice() throws Exception {
    	FHIRHistoryContext historyContext = FHIRPersistenceContextFactory.createHistoryContext();
    	FHIRPersistenceContext context = this.getPersistenceContextForHistory(historyContext);
    	Map<String, List<Integer>> deletedResources;
    	List<Integer> deletedVersions;
    	
		List<Resource> resources = persistence.history(context, Device.class, this.deviceId);
		assertNotNull(resources);
		assertTrue(resources.size() == 2);
		deletedResources = historyContext.getDeletedResources();
		assertNotNull(deletedResources);
		assertTrue(deletedResources.size() == 1);
		assertNotNull(deletedResources.get(this.deviceId));
		deletedVersions = deletedResources.get(this.deviceId);
		assertEquals(1,deletedVersions.size());
		assertEquals(new Integer(2),deletedVersions.get(0));
	}
    
    @Test(groups = { "jdbc-normalized" }, dependsOnMethods = { "testDeleteValidDevice" })
    public void testReDeleteValidDevice() throws Exception {
    	
    	persistence.delete(getDefaultPersistenceContext(), Device.class, this.deviceId);
    }
    
    @Test(groups = { "jdbc-normalized" }, dependsOnMethods = { "testCreateDevice1" })
    public void testUpdateDeletedDevice() throws Exception { 
    	
    	ObjectFactory objectFactory = new ObjectFactory();
    	FHIRHistoryContext historyContext = FHIRPersistenceContextFactory.createHistoryContext();
    	FHIRPersistenceContext context = this.getPersistenceContextForHistory(historyContext);
    	Map<String, List<Integer>> deletedResources;
    	List<Integer> deletedVersions;
    	String updatedUdiValue = "updated-udi-value";
    	
    	// Read previously created device
    	Device device = (Device) persistence.read(getDefaultPersistenceContext(), Device.class, this.deviceId);
    	
    	// Delete previously created device
    	persistence.delete(getDefaultPersistenceContext(), Device.class, this.deviceId1);
    	
    	// Then update deleted device
    	device.setUdi(objectFactory.createString().withValue(updatedUdiValue));
    	persistence.update(getDefaultPersistenceContext(), deviceId1, device);
    	
    	// Verify device history
		List<Resource> resources = persistence.history(context, Device.class, this.deviceId1);
		assertNotNull(resources);
		assertTrue(resources.size() == 3);
		deletedResources = historyContext.getDeletedResources();
		assertNotNull(deletedResources);
		assertTrue(deletedResources.size() == 1);
		assertNotNull(deletedResources.get(this.deviceId1));
		deletedVersions = deletedResources.get(this.deviceId1);
		assertEquals(1,deletedVersions.size());
		assertEquals(new Integer(2),deletedVersions.get(0));
		
		// Verify latest device
		Device latestDeviceVersion = (Device)resources.get(0);
		assertEquals(updatedUdiValue,latestDeviceVersion.getUdi().getValue());
    	
    	
    	
     
    }
}
