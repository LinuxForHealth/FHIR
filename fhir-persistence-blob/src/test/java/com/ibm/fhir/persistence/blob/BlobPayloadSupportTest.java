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

    @Test
    public void testPathDecode() {
        // periods have to be encoded for Azure Blob paths, so test
        // that the decode works as expected
        final String path = "1/patient1#/2/my-payload-key";
        ResourceRecord rr = BlobPayloadSupport.buildResourceRecordFromPath(path);
        assertEquals(rr.getResourceTypeId(), 1);
        assertEquals(rr.getLogicalId(), "patient1."); // # decoded to .
        assertEquals(rr.getVersion(), 2);
        assertEquals(rr.getResourcePayloadKey(), "my-payload-key");
    }

    @Test
    public void testPathWithEncode() {
        final int resourceTypeId = 42;
        final String logicalId = "patient.";
        final int version = 1;
        final String resourcePayloadKey = "key1";
        final String path = BlobPayloadSupport.getPayloadPath(resourceTypeId, logicalId, version, resourcePayloadKey);
        // Make sure that the logical id was encoded correctly
        assertEquals(path, "42/patient#/1/key1");
    }

    @Test
    public void testEncode() {
        assertEquals("#", BlobPayloadSupport.encodeLogicalId("."));
    }

    @Test
    public void testDecode() {
        assertEquals(".", BlobPayloadSupport.decodeLogicalId("#"));
    }
}