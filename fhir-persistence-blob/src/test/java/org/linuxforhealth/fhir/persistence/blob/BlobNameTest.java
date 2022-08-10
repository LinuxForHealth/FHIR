/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.blob;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.linuxforhealth.fhir.persistence.jdbc.cache.ResourceTypeMaps;
import org.linuxforhealth.fhir.schema.model.ResourceType;
import org.testng.annotations.Test;

/**
 * Unit tests for {@link BlobName}
 */
public class BlobNameTest {

    @Test
    public void testFullName() {
        List<ResourceType> resourceTypes = new ArrayList<>();
        resourceTypes.add(new ResourceType(1, "Patient"));
        resourceTypes.add(new ResourceType(2, "Claim"));
        ResourceTypeMaps rtms = new ResourceTypeMaps();
        rtms.init(resourceTypes);
        final String path = "Patient/patient-42/1/a-resource-payload-key1";
        BlobName bn = BlobName.create(rtms, path);
        assertEquals(bn.toString(), path);
        assertEquals(bn.toBlobPath(), "1/patient-42/1/a-resource-payload-key1");
    }
    
    @Test
    public void testPartialName() {
        List<ResourceType> resourceTypes = new ArrayList<>();
        resourceTypes.add(new ResourceType(1, "Patient"));
        resourceTypes.add(new ResourceType(2, "Claim"));
        ResourceTypeMaps rtms = new ResourceTypeMaps();
        rtms.init(resourceTypes);
        final String path = "Patient/patient-42/1";
        BlobName bn = BlobName.create(rtms, path);
        assertEquals(bn.toString(), path + "/");
        assertEquals(bn.toBlobPath(), "1/patient-42/1/");
    }
    
    @Test
    public void testResourceTypeId() {
        List<ResourceType> resourceTypes = new ArrayList<>();
        resourceTypes.add(new ResourceType(1, "Patient"));
        resourceTypes.add(new ResourceType(2, "Claim"));
        ResourceTypeMaps rtms = new ResourceTypeMaps();
        rtms.init(resourceTypes);
        final String path = "1/patient-42/1/a-resource-payload-key1";
        BlobName bn = BlobName.create(rtms, path);
        assertEquals(bn.toString(), "Patient/patient-42/1/a-resource-payload-key1");
        assertEquals(bn.toBlobPath(), path);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testTooShort() {
        List<ResourceType> resourceTypes = new ArrayList<>();
        resourceTypes.add(new ResourceType(1, "Patient"));
        resourceTypes.add(new ResourceType(2, "Claim"));
        ResourceTypeMaps rtms = new ResourceTypeMaps();
        rtms.init(resourceTypes);
        final String path = "Patient";
        BlobName bn = BlobName.create(rtms, path);
    }
    
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testTooLong() {
        List<ResourceType> resourceTypes = new ArrayList<>();
        resourceTypes.add(new ResourceType(1, "Patient"));
        resourceTypes.add(new ResourceType(2, "Claim"));
        ResourceTypeMaps rtms = new ResourceTypeMaps();
        rtms.init(resourceTypes);
        final String path = "Patient/patient-42/1/foo/bar";
        BlobName bn = BlobName.create(rtms, path);
    }

    @Test
    public void testEncode() {
        List<ResourceType> resourceTypes = new ArrayList<>();
        resourceTypes.add(new ResourceType(1, "Patient"));
        resourceTypes.add(new ResourceType(2, "Claim"));
        ResourceTypeMaps rtms = new ResourceTypeMaps();
        rtms.init(resourceTypes);
        
        // The . in the logicalId needs to be encoded
        final String path = "Patient/patient.42/1/a-resource-payload-key1";
        BlobName bn = BlobName.create(rtms, path);
        assertEquals(bn.toString(), path);
        assertEquals(bn.toBlobPath(), "1/patient*42/1/a-resource-payload-key1");
    }

    @Test
    public void testDecode() {
        List<ResourceType> resourceTypes = new ArrayList<>();
        resourceTypes.add(new ResourceType(1, "Patient"));
        resourceTypes.add(new ResourceType(2, "Claim"));
        ResourceTypeMaps rtms = new ResourceTypeMaps();
        rtms.init(resourceTypes);

        final String path = "Patient/patient*42/1/a-resource-payload-key1";
        BlobName bn = BlobName.create(rtms, path);
        assertEquals(bn.toString(), "Patient/patient.42/1/a-resource-payload-key1");
        assertEquals(bn.toBlobPath(), "1/patient*42/1/a-resource-payload-key1");
    }
    @Test
    public void testNoVersionIsPartial() {
        BlobName.Builder builder = BlobName.builder();
        builder.resourceTypeId(104);
        builder.logicalId("hello");
        BlobName blobName = builder.build();
        assertTrue(blobName.isPartial());
        assertEquals(blobName.toBlobPath(), "104/hello/");
    }
    @Test
    public void testNoPayloadKeyIsPartial() {
        BlobName.Builder builder = BlobName.builder();
        builder.resourceTypeId(104);
        builder.logicalId("hello");
        builder.version(1);
        BlobName blobName = builder.build();
        assertTrue(blobName.isPartial());
        assertEquals(blobName.toBlobPath(), "104/hello/1/");
    }
}