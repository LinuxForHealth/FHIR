/*
 * (C) Copyright IBM Corp. 2018, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.test.common;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.Encounter;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.persistence.SingleResourceResult;

/**
 * This class contains a collection of tests that will be run against
 * each of the various persistence layer implementations.
 * There will be a subclass in each persistence project.
 */
public abstract class AbstractMultiResourceTest extends AbstractPersistenceTest {
    String commonId = UUID.randomUUID().toString();

    /**
     * Create two different resources with the same id
     */
    @BeforeClass
    public void createResources() throws Exception {
        
        final com.ibm.fhir.model.type.Instant lastUpdated = com.ibm.fhir.model.type.Instant.now(ZoneOffset.UTC);
        final int versionId = 1;
        Encounter encounter = TestUtil.getMinimalResource(Encounter.class);
        
        // Update the id on the resource
        encounter = encounter.toBuilder().id(commonId).build();
        
        // Inject meta data
        encounter = copyAndSetResourceMetaFields(encounter, commonId, versionId, lastUpdated);
        
        encounter = persistence.updateWithMeta(getDefaultPersistenceContext(), encounter).getResource();
        assertNotNull(encounter);
        assertNotNull(encounter.getId());
        assertNotNull(encounter.getMeta());
        assertNotNull(encounter.getMeta().getVersionId().getValue());
        assertEquals("1", encounter.getMeta().getVersionId().getValue());

        Observation observation = TestUtil.getMinimalResource(Observation.class);

        // update the id on the resource
        observation = observation.toBuilder().id(commonId).build();
        observation = copyAndSetResourceMetaFields(observation, commonId, versionId, lastUpdated);
        observation = persistence.updateWithMeta(getDefaultPersistenceContext(), observation).getResource();
        assertNotNull(observation);
        assertNotNull(observation.getId());
        assertNotNull(observation.getMeta());
        assertNotNull(observation.getMeta().getVersionId().getValue());
        assertEquals("1", observation.getMeta().getVersionId().getValue());
    }

    /**
     * Tests a normal read when a different resource type has the same id
     */
    @Test
    public void testRead() throws Exception {
        SingleResourceResult<? extends Resource> result;

        result = persistence.read(getDefaultPersistenceContext(), Encounter.class, commonId);
        assertTrue(result.isSuccess());
        assertNotNull(result.getResource());
        assertTrue(result.getResource() instanceof Encounter);

        result = persistence.read(getDefaultPersistenceContext(), Observation.class, commonId);
        assertTrue(result.isSuccess());
        assertNotNull(result.getResource());
        assertTrue(result.getResource() instanceof Observation);
    }

    /**
     * Tests searching by id when a different resource type has the same id
     */
    @Test
    public void testSearchById() throws Exception {
        List<Resource> resources;

        resources = runQueryTest(Encounter.class, "_id", commonId);
        assertNotNull(resources);
        assertTrue(resources.size() == 1);
        assertTrue(resources.get(0) instanceof Encounter);

        resources = runQueryTest(Observation.class, "_id", commonId);
        assertNotNull(resources);
        assertTrue(resources.size() == 1);
        assertTrue(resources.get(0) instanceof Observation);

        resources = runQueryTest(Resource.class, "_id", commonId);
        assertNotNull(resources);
        assertTrue(resources.size() == 2);
    }
}
