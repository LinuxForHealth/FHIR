/*
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.test.common;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.List;
import java.util.UUID;

import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.Encounter;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.util.FHIRUtil;

/**
 *  This class contains a collection of tests that will be run against
 *  each of the various persistence layer implementations.
 *  There will be a subclass in each persistence project.
 */
public abstract class AbstractQueryMultiResourceTest extends AbstractPersistenceTest {
    
    /**
     * Create a resource with the same id but a different 
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" })
    public void testCreateEncounterAndObservationWithSameId() throws Exception {
        String commonId = UUID.randomUUID().toString();
        Encounter encounter = readResource(Encounter.class, "Encounter.json");
        
        // Update the id on the resource
        encounter = encounter.toBuilder().id(Id.of(commonId)).build();        
        
//        encounter.setLength(f.createDuration()
//            .withCode(FHIRUtil.code("d"))
//            .withValue(FHIRUtil.decimal(7)));
        persistence.create(getDefaultPersistenceContext(), encounter);
        assertNotNull(encounter);
        assertNotNull(encounter.getId());
        assertNotNull(encounter.getId().getValue());
        assertNotNull(encounter.getMeta());
        assertNotNull(encounter.getMeta().getVersionId().getValue());
        assertEquals("1", encounter.getMeta().getVersionId().getValue());
        
        Observation observation = readResource(Observation.class, "observation-example.canonical.json");

        // update the id on the resource
        observation = observation.toBuilder().id(Id.of(commonId)).build();        
        Resource resource = persistence.create(getDefaultPersistenceContext(), observation);
        resource = persistence.update(getDefaultPersistenceContext(), commonId, resource);
        assertNotNull(resource);
        assertNotNull(resource.getId());
        assertNotNull(resource.getId().getValue());
        assertNotNull(resource.getMeta());
        assertNotNull(resource.getMeta().getVersionId().getValue());
        assertEquals("2", resource.getMeta().getVersionId().getValue());
    } 
    
    /**
     * Tests a query for Encounters with length = '60.0' which should yield correct results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreateEncounterAndObservationWithSameId" })
    public void testEncounter_length_whenDuplicateResourceIdExists() throws Exception {
        List<Resource> resources = runQueryTest(Encounter.class, persistence, "length", "60");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
    }
    
}
