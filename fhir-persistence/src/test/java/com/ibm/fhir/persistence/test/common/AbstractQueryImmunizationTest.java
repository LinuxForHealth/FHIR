/**
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.test.common;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.Immunization;

/**
 *  This class contains a collection of tests that will be run against
 *  each of the various persistence layer implementations.
 *  There will be a subclass in each persistence project.
 *  
 *  
 */
public abstract class AbstractQueryImmunizationTest extends AbstractPersistenceTest {
    
//    /**
//     *  Ensures an immunization with a "partial identifier" (one without a value) can be created without a failure.
//     * @throws Exception
//     */
//    @Test(groups = { "jdbc-normalized" })
//    public void testCreateImmunization_partial_identifier() throws Exception {
//        Immunization imm = readResource(Immunization.class, "immunization-partial-identifier.json");
//
//        persistence.create(getDefaultPersistenceContext(), imm);
//        assertNotNull(imm);
//        assertNotNull(imm.getId());
//        assertNotNull(imm.getId().getValue());
//        assertNotNull(imm.getMeta());
//        assertNotNull(imm.getMeta().getVersionId().getValue());
//        assertEquals("1", imm.getMeta().getVersionId().getValue());
//    }
//    
//    /**
//     * Tests the creation of an immunization with an identifier containing both a system and a value.
//     * @throws Exception
//     */
//    @Test(groups = { "jdbc-normalized" })
//    public void testCreateImmunization_full_identifier() throws Exception {
//        Immunization imm = readResource(Immunization.class, "immunization.json");
//
//        persistence.create(getDefaultPersistenceContext(), imm);
//        assertNotNull(imm);
//        assertNotNull(imm.getId());
//        assertNotNull(imm.getId().getValue());
//        assertNotNull(imm.getMeta());
//        assertNotNull(imm.getMeta().getVersionId().getValue());
//        assertEquals("1", imm.getMeta().getVersionId().getValue());
//    }
}
