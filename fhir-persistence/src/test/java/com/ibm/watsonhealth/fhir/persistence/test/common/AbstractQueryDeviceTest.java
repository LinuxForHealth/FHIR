/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.test.common;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.List;

import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.model.Device;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.persistence.FHIRPersistence;

/**
 *  This class contains a collection of tests that will be run against
 *  each of the various persistence layer implementations.
 *  There will be a subclass in each persistence project.
 */
public abstract class AbstractQueryDeviceTest extends AbstractPersistenceTest {
    
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for a Device.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa" })
    public void testCreateDevice() throws Exception {
        Device device = readResource(Device.class, "Device.json");

        persistence.create(device);
        assertNotNull(device);
        assertNotNull(device.getId());
        assertNotNull(device.getId().getValue());
        assertNotNull(device.getMeta());
        assertNotNull(device.getMeta().getVersionId().getValue());
        assertEquals("1", device.getMeta().getVersionId().getValue());
    }        
	
	/**
	 * Tests a query for a Device with manufacturer = 'Acme Devices, Inc' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateDevice" })
	public void testDeviceQuery_manufacturer() throws Exception {
		List<Resource> resources = runQueryTest(Device.class, persistence, "manufacturer", "Acme Devices, Inc");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Device)resources.get(0)).getManufacturer().getValue(),"Acme Devices, Inc");
	}
	
	/**
	 * Tests a query for a Device with model = 'AB45-J' which should yield no results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateDevice" })
	public void testDeviceQuery_model() throws Exception {
		List<Resource> resources = runQueryTest(Device.class, persistence, "model", "AB45-J");
		assertNotNull(resources);
		assertTrue(resources.size() == 0);
	}
	
	/**
	 * Tests a query for a Device with udi = '(01)00000123000017(10)ABC123(17)120415' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateDevice" })
	public void testDeviceQuery_udi() throws Exception {
		List<Resource> resources = runQueryTest(Device.class, persistence, "udi", "(01)00000123000017(10)ABC123(17)120415");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((Device)resources.get(0)).getUdi().getValue(),"(01)00000123000017(10)ABC123(17)120415");
	}

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.persistence.test.common.AbstractPersistenceTest#getPersistenceImpl()
     */
    @Override
    public FHIRPersistence getPersistenceImpl() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
}
