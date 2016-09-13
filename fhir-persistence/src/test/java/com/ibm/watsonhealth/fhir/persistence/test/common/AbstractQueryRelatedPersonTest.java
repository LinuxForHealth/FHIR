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

import com.ibm.watsonhealth.fhir.model.RelatedPerson;
import com.ibm.watsonhealth.fhir.model.Resource;

/**
 *  This class contains a collection of tests that will be run against
 *  each of the various persistence layer implementations.
 *  There will be a subclass in each persistence project.
 */
public abstract class AbstractQueryRelatedPersonTest extends AbstractPersistenceTest {
    
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for a Device.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa" })
    public void testCreateRelatedPerson() throws Exception {
    	RelatedPerson relatedPerson = readResource(RelatedPerson.class, "relatedperson-example.canonical.json");

        persistence.create(getDefaultPersistenceContext(), relatedPerson);
        assertNotNull(relatedPerson);
        assertNotNull(relatedPerson.getId());
        assertNotNull(relatedPerson.getId().getValue());
        assertNotNull(relatedPerson.getMeta());
        assertNotNull(relatedPerson.getMeta().getVersionId().getValue());
        assertEquals("1", relatedPerson.getMeta().getVersionId().getValue());
    }        
	
	/**
	 * Tests a query for a RelatedPerson with name = 'Bénédicte' which should yield correct results
	 * @throws Exception
	 */
	@Test(groups = { "cloudant", "jpa" }, dependsOnMethods = { "testCreateRelatedPerson" })
	public void testRelatedPersonQuery_name() throws Exception {
		List<Resource> resources = runQueryTest(RelatedPerson.class, persistence, "name", "Bénédicte");
		assertNotNull(resources);
		assertTrue(resources.size() != 0);
		assertEquals(((RelatedPerson)resources.get(0)).getName().getGiven().get(0).getValue(),"Bénédicte");
	}
}
