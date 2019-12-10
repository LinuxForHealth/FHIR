/*
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.search.test;

import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.LogManager;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.model.resource.Basic;
import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.MultiResourceResult;
import com.ibm.fhir.persistence.SingleResourceResult;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.fhir.persistence.jdbc.impl.FHIRPersistenceJDBCImpl;
import com.ibm.fhir.persistence.jdbc.test.util.DerbyInitializer;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * <a href="https://www.hl7.org/fhir/r4/location.html#positional">FHIR
 * Specification: Positional Searching for Location Resource</a>
 */
public class JDBCSearchNearTest {
    private Properties testProps;

    protected Location savedResource;

    protected static FHIRPersistence persistence = null;

    @BeforeClass
    public void startup() throws Exception {
        InputStream inputStream = JDBCSearchNearTest.class.getResourceAsStream("/near.unitTest.properties");
        LogManager.getLogManager().readConfiguration(inputStream);

        FHIRConfiguration.setConfigHome("../fhir-persistence/target/test-classes");
        FHIRRequestContext.get().setTenantId("default");

        testProps = TestUtil.readTestProperties("test.jdbc.properties");

        DerbyInitializer derbyInit;
        String dbDriverName = this.testProps.getProperty("dbDriverName");
        if (dbDriverName != null && dbDriverName.contains("derby")) {
            derbyInit = new DerbyInitializer(this.testProps);
            derbyInit.bootstrapDb();
        }

        savedResource = TestUtil.readExampleResource("json/spec/location-example.json");

        persistence   = new FHIRPersistenceJDBCImpl(this.testProps);

        SingleResourceResult<Location> result =
                persistence.create(FHIRPersistenceContextFactory.createPersistenceContext(null), savedResource);
        assertTrue(result.isSuccess());

    }

    public MultiResourceResult<Resource> runQueryTest(String searchParamCode, String queryValue) throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>(1);
        if (searchParamCode != null && queryValue != null) {
            queryParms.put(searchParamCode, Collections.singletonList(queryValue));
        }

        FHIRSearchContext ctx = SearchUtil.parseQueryParameters(Location.class, queryParms);
        FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(null, ctx);
        MultiResourceResult<Resource> result = persistence.search(persistenceContext, Location.class);
        return result;
    }

    @AfterClass
    public void teardown() throws Exception {
        FHIRRequestContext.get().setTenantId("default");
    }

    @BeforeMethod(alwaysRun = true)
    public void startTrx() throws Exception {
        if (persistence != null && persistence.isTransactional()) {
            persistence.getTransaction().begin();
        }
    }

    @AfterMethod(alwaysRun = true)
    public void commitTrx() throws Exception {
        if (persistence != null && persistence.isTransactional()) {
            persistence.getTransaction().commit();
        }
    }

    @Test
    public void testSearchPositionSearch() throws Exception {
        String searchParamCode = "near";
        String queryValue = "-83.6945691|42.25475478|10.0|km";

        MultiResourceResult<Resource> result = runQueryTest(searchParamCode, queryValue);
        assertNotNull(result);
        System.out.println(result.getResource());
        System.out.println(result.getOutcome());
        //assertSearchDoesntReturnSavedResource("near", "-83.694810|42.256500|11.20|km");
    }
}