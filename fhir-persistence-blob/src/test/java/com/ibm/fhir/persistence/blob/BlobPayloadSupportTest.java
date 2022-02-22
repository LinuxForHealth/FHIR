/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.blob;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.ibm.fhir.persistence.jdbc.dao.api.ResourceRecord;

/**
 * Unit tests for {@link BlobPayloadSupport}
 */
public class BlobPayloadSupportTest {

    @Test
    public void testPathToResourceRecord() {
        final String path = "1/patient1/2/my-payload-key";
        ResourceRecord rr = BlobPayloadSupport.buildResourceRecordFromPath(path);
        assertEquals(rr.getResourceTypeId(), 1);
        assertEquals(rr.getLogicalId(), "patient1");
        assertEquals(rr.getVersion(), 2);
        assertEquals(rr.getResourcePayloadKey(), "my-payload-key");
    }
    
    @Test(expectedExceptions = NumberFormatException.class)
    public void testBadPath1() {
        final String path = "not-a-number/patient1/2/my-payload-key";
        BlobPayloadSupport.buildResourceRecordFromPath(path);
    }

    @Test(expectedExceptions = NumberFormatException.class)
    public void testBadPath2() {
        final String path = "1/patient1/not-a-number/my-payload-key";
        BlobPayloadSupport.buildResourceRecordFromPath(path);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testBadPath3() {
        final String path = "1/patient1/2/my-payload-key/something";
        BlobPayloadSupport.buildResourceRecordFromPath(path);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testBadPath4() {
        final String path = "1/patient1/2";
        BlobPayloadSupport.buildResourceRecordFromPath(path);
    }
}