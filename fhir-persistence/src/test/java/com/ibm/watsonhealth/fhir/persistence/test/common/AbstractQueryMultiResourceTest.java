/**
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.test.common;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.List;
import java.util.UUID;

import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.model.Encounter;
import com.ibm.watsonhealth.fhir.model.Observation;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;

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
        encounter.setId(FHIRUtil.id(commonId));
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
        observation.setId(FHIRUtil.id(commonId));
        persistence.create(getDefaultPersistenceContext(), observation);
        persistence.update(getDefaultPersistenceContext(), commonId, observation);
        assertNotNull(observation);
        assertNotNull(observation.getId());
        assertNotNull(observation.getId().getValue());
        assertNotNull(observation.getMeta());
        assertNotNull(observation.getMeta().getVersionId().getValue());
        assertEquals("2", observation.getMeta().getVersionId().getValue());
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
