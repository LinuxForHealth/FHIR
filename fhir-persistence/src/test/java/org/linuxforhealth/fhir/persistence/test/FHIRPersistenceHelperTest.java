/*
 * (C) Copyright IBM Corp. 2017, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.fail;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.config.FHIRConfiguration;
import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.persistence.FHIRPersistence;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceNotSupportedException;
import org.linuxforhealth.fhir.persistence.helper.FHIRPersistenceHelper;
import org.linuxforhealth.fhir.persistence.helper.PersistenceHelper;
import org.linuxforhealth.fhir.search.util.SearchHelper;

/**
 * Tests associated with the FHIRPersistenceHelper class.
 */
public class FHIRPersistenceHelperTest {
    SearchHelper searchHelper;

    @BeforeClass
    public void setup() {
        FHIRConfiguration.setConfigHome("target/test-classes");
        searchHelper = new SearchHelper();
    }

    @Test
    public void test1() throws Exception {
        PersistenceHelper helper = new FHIRPersistenceHelper(searchHelper);

        FHIRPersistence persistence = helper.getFHIRPersistenceImplementation();
        assertNotNull(persistence);
        assertEquals(MockPersistenceImpl.class.getName(), persistence.getClass().getName());

        assertFalse(persistence.isDeleteSupported());
    }

    @Test
    public void test2() throws Exception {
        PersistenceHelper helper = new FHIRPersistenceHelper(searchHelper);

        FHIRPersistence persistence = helper.getFHIRPersistenceImplementation("persistenceFactoryName");
        assertNotNull(persistence);
        assertEquals(MockPersistenceImpl.class.getName(), persistence.getClass().getName());
    }

    @Test
    public void test3() throws Exception {
        PersistenceHelper helper = new FHIRPersistenceHelper(searchHelper);
        assertFalse(helper.isValidFHIRPersistenceImplementation("foo"));
    }

    @Test(expectedExceptions = {FHIRPersistenceNotSupportedException.class})
    public void test4() throws Exception {
        PersistenceHelper helper = new FHIRPersistenceHelper(searchHelper);

        FHIRPersistence persistence = helper.getFHIRPersistenceImplementation();
        assertNotNull(persistence);

        assertFalse(persistence.isDeleteSupported());
        persistence.delete(null, null, null, -1, null);
    }

    @Test(expectedExceptions = {FHIRPersistenceException.class})
    public void test5() throws Exception {
        PersistenceHelper helper = new FHIRPersistenceHelper(searchHelper);

        helper.getFHIRPersistenceImplementation("badFactoryName");
        fail("Expected exception was not thrown!");
    }

    @Test(expectedExceptions = {FHIRPersistenceException.class})
    public void test6() throws Exception {
        PersistenceHelper helper = new FHIRPersistenceHelper(searchHelper);

        helper.getFHIRPersistenceImplementation("notAFactoryName");
        fail("Expected exception was not thrown!");
    }

    @Test(expectedExceptions = {FHIRPersistenceException.class})
    public void test7() throws Exception {
        PersistenceHelper helper = new FHIRPersistenceHelper(searchHelper);

        helper.getFHIRPersistenceImplementation("exceptionFactoryName");
        fail("Expected exception was not thrown!");
    }

    @Test(expectedExceptions = {FHIRPersistenceException.class})
    public void test8() throws Exception {
        PersistenceHelper helper = new FHIRPersistenceHelper(searchHelper);

        helper.getFHIRPersistenceImplementation("noFactoryProperty");
        fail("Expected exception was not thrown!");
    }

    @Test(expectedExceptions = {FHIRPersistenceException.class})
    public void test9() throws Exception {
        FHIRRequestContext.set(new FHIRRequestContext("syntax-error"));
        PersistenceHelper helper = new FHIRPersistenceHelper(searchHelper);

        helper.getFHIRPersistenceImplementation("noFactoryProperty");
        fail("Expected exception was not thrown!");
    }
}
