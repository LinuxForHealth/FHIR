/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.test.common;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.time.ZoneOffset;
import java.util.UUID;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.Device;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.persistence.InteractionStatus;
import com.ibm.fhir.persistence.SingleResourceResult;

/**
 * This class contains If-None-Match tests.
 */
public abstract class AbstractIfNoneMatchTest extends AbstractPersistenceTest {
    // Some ids we know don't exist
    private static final String ID1 = UUID.randomUUID().toString();
    private static final String ID2 = UUID.randomUUID().toString();
    private static final String ID3 = UUID.randomUUID().toString();
    
    /**
     * Clean up any  resources we may have created
     */
    @AfterClass
    public void tearDown() throws Exception {
        String[] resourceIds = {ID1, ID2, ID3};
        if (persistence.isDeleteSupported()) {
            // as this is AfterClass, we need to manually start/end the transaction
            startTrx();
            for (String resourceId : resourceIds) {
                try {
                    persistence.delete(getDefaultPersistenceContext(), Device.class, resourceId);
                } catch (Exception e) {
                    // Swallow any exception.
                }
            }
            commitTrx();
        }
    }

    @Test
    public void testCreate() throws Exception {
        final com.ibm.fhir.model.type.Instant lastUpdated = com.ibm.fhir.model.type.Instant.now(ZoneOffset.UTC);

        Device device1 = TestUtil.getMinimalResource(Device.class);
        device1 = copyAndSetResourceMetaFields(device1, ID1, 1, lastUpdated);
        SingleResourceResult<Device> result = persistence.createWithMeta(getPersistenceContextIfNoneMatch(), device1);
        assertEquals(result.getStatus(), InteractionStatus.MODIFIED);
    }

    @Test
    public void testUpdate() throws Exception {
        final com.ibm.fhir.model.type.Instant lastUpdated = com.ibm.fhir.model.type.Instant.now(ZoneOffset.UTC);
        
        Device device2 = TestUtil.getMinimalResource(Device.class);
        device2 = copyAndSetResourceMetaFields(device2, ID2, 1, lastUpdated);
        SingleResourceResult<Device> result = persistence.updateWithMeta(getPersistenceContextIfNoneMatch(), device2);
        assertEquals(result.getStatus(), InteractionStatus.MODIFIED);

        // Update the device for version 2 so we can try to update it
        Device device2v2 = updateVersionMeta(device2);
        result = persistence.updateWithMeta(getPersistenceContextIfNoneMatch(), device2v2);
        assertEquals(result.getStatus(), InteractionStatus.IF_NONE_MATCH_EXISTED);
        
        // Now read the device back to make sure that we didn't update it
        result = persistence.read(getDefaultPersistenceContext(), Device.class, ID2);
        assertNotNull(result);
        assertNotNull(result.getResource());
        assertEquals(result.getStatus(), InteractionStatus.READ);
        assertEquals(result.getResource().getMeta().getVersionId().getValue(), "1");
    }
    
    public void testDeleted() throws Exception {
        final com.ibm.fhir.model.type.Instant lastUpdated = com.ibm.fhir.model.type.Instant.now(ZoneOffset.UTC);

        // Create a device
        Device device3 = TestUtil.getMinimalResource(Device.class);
        device3 = copyAndSetResourceMetaFields(device3, ID3, 1, lastUpdated);
        SingleResourceResult<Device> result = persistence.updateWithMeta(getDefaultPersistenceContext(), device3);
        assertEquals(result.getStatus(), InteractionStatus.MODIFIED);
        
        // and delete it
        result = persistence.delete(getDefaultPersistenceContext(), Device.class, ID3);
        assertNotNull(result);

        // now run an update with if-not-match - we created the resource
        device3 = updateVersionMeta(device3); // V2 is the deletion marker
        device3 = updateVersionMeta(device3); // V3 is the new version we want
        result = persistence.updateWithMeta(getPersistenceContextIfNoneMatch(), device3);
        assertEquals(result.getStatus(), InteractionStatus.MODIFIED);
        
        // Now read the device back to make sure that we get V3
        result = persistence.read(getDefaultPersistenceContext(), Device.class, ID3);
        assertNotNull(result);
        assertNotNull(result.getResource());
        assertEquals(result.getStatus(), InteractionStatus.READ);
        assertEquals(result.getResource().getMeta().getVersionId().getValue(), "3");
    }    
}