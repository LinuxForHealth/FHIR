/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.search.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.AssertJUnit.assertTrue;

import java.io.FileInputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.LogManager;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.FHIRPersistenceSupport;
import com.ibm.fhir.persistence.MultiResourceResult;
import com.ibm.fhir.persistence.SingleResourceResult;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.test.util.PersistenceTestSupport;
import com.ibm.fhir.persistence.util.FHIRPersistenceUtil;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.util.SearchHelper;

/**
 * <a href="https://www.hl7.org/fhir/r4/location.html#positional">FHIR
 * Specification: Positional Searching for Location Resource</a>
 * <br>
 * Original LONG/LAT
 *
 * <pre>
 *  "position": {
 *      "longitude": -83.6945691,
 *      "latitude": 42.25475478,
 *      "altitude": 0
 * }
 * </pre>
 */
public class JDBCSearchNearTest {

    protected Location savedResource;

    protected static FHIRPersistence persistence;
    protected static SearchHelper searchHelper;

    // Container to hide the instantiation of the persistence impl used for tests
    private PersistenceTestSupport testSupport;
    
    @BeforeClass
    public void startup() throws Exception {
        LogManager.getLogManager().readConfiguration(
                new FileInputStream("../fhir-persistence/src/test/resources/logging.unitTest.properties"));

        FHIRConfiguration.setConfigHome("../fhir-persistence/target/test-classes");
        searchHelper = new SearchHelper();
        FHIRRequestContext.get().setTenantId("default");

        testSupport = new PersistenceTestSupport();

        savedResource = TestUtil.readExampleResource("json/spec/location-example.json");
        savedResource = savedResource.toBuilder()
                .id(UUID.randomUUID().toString())
                .meta(savedResource.getMeta().toBuilder()
                    .lastUpdated(Instant.now())
                    .versionId(Id.of("1"))
                    .build())
                .build();

        persistence   = testSupport.getPersistenceImpl();

        SingleResourceResult<Location> result =
                persistence.create(FHIRPersistenceContextFactory.createPersistenceContext(null), savedResource);
        assertTrue(result.isSuccess());
        assertNotNull(result.getResource());
        savedResource = result.getResource();

    }

    @AfterClass
    public void teardown() throws Exception {
        if (savedResource != null && persistence.isDeleteSupported()) {
            Objects.requireNonNull(savedResource.getMeta(), "savedResource must have a meta element");
            if (persistence.isTransactional()) {
                persistence.getTransaction().begin();
            }

            FHIRSearchContext ctx = searchHelper.parseQueryParameters(Location.class, Collections.emptyMap(), true, true);
            FHIRPersistenceContext persistenceContext =
                    FHIRPersistenceContextFactory.createPersistenceContext(null, ctx, null);
            com.ibm.fhir.model.type.Instant lastUpdated = FHIRPersistenceUtil.getUpdateTime();
            persistence.delete(persistenceContext, savedResource.getClass(), savedResource.getId(), FHIRPersistenceSupport.getMetaVersionId(savedResource), lastUpdated);
            if (persistence.isTransactional()) {
                persistence.getTransaction().end();
            }
        }
        FHIRRequestContext.get().setTenantId("default");
        if (testSupport != null) {
            testSupport.shutdown();
        }
    }

    public MultiResourceResult runQueryTest(String searchParamCode, String queryValue) throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>(1);
        if (searchParamCode != null && queryValue != null) {
            queryParms.put(searchParamCode, Collections.singletonList(queryValue));
        }
        return runQueryTest(queryParms);
    }

    public MultiResourceResult runQueryTestMultiples(String searchParamCode, String... queryValues)
            throws Exception {
        Map<String, List<String>> queryParms = new LinkedHashMap<String, List<String>>(queryValues.length);
        for (String queryValue : queryValues) {
            if (searchParamCode != null && queryValue != null) {
                queryParms.put(searchParamCode, Collections.singletonList(queryValue));
            }
        }
        System.out.println(queryParms);
        return runQueryTest(queryParms);
    }

    public MultiResourceResult runQueryTest(Map<String, List<String>> queryParms) throws Exception {
        FHIRSearchContext ctx = searchHelper.parseQueryParameters(Location.class, queryParms, true, true);
        FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(null, ctx, null);
        MultiResourceResult result = persistence.search(persistenceContext, Location.class);
        return result;
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
            persistence.getTransaction().end();
        }
    }

    @Test
    public void testSearchPositionSearchExactSmallRangeMatch() throws Exception {
        // Should match the loaded resource with a real range
        String searchParamCode = "near";
        String queryValue = "42.25475478|-83.6945691|10.0|km";

        MultiResourceResult result = runQueryTest(searchParamCode, queryValue);
        assertNotNull(result);
        assertFalse(result.getResourceResults().size() == 0);
        assertNull(result.getOutcome());
    }

    @Test
    public void testSearchPositionSearchExactLargeRangeMatch() throws Exception {
        // Should match the loaded resource with a real range
        String searchParamCode = "near";
        String queryValue = "42.25475478|0|10000.0|km";

        MultiResourceResult result = runQueryTest(searchParamCode, queryValue);
        assertNotNull(result);
        assertNotEquals(result.getResourceResults().size(), 0);
        assertNull(result.getOutcome());
    }

    @Test
    public void testSearchPositionSearchExactMatchWithinSmallRange() throws Exception {
        // Should match the loaded resource
        String searchParamCode = "near";
        String queryValue = "42.0|-83.0|500.0|km";

        MultiResourceResult result = runQueryTest(searchParamCode, queryValue);
        assertNotNull(result);
        assertNotEquals(result.getResourceResults().size(), 0);
        assertNull(result.getOutcome());
    }

    @Test
    public void testSearchPositionSearchExactMatchNotMatchingRange() throws Exception {
        // Should not match (opposite the loaded resource)
        String searchParamCode = "near";
        String queryValue = "-83.0|42.0|1.0|km";

        MultiResourceResult result = runQueryTest(searchParamCode, queryValue);
        assertNotNull(result);
        assertTrue(result.getResourceResults().size() == 0);
    }

    @Test
    public void testSearchPositionSearchExactMatchWithinRangeNot() throws Exception {
        // Difference to expected location is greater than 523.3km
        String searchParamCode = "near";
        String queryValue = "-79|40|523.3|km";

        MultiResourceResult result = runQueryTest(searchParamCode, queryValue);
        assertNotNull(result);
        assertTrue(result.getResourceResults().size() == 0);
    }

    @Test
    public void testSearchPositionSearchExactMatchWithinRange() throws Exception {
        // 40, -79

        // https://www.movable-type.co.uk/scripts/latlong.html says the expected distance is 466.2 km
        String searchParamCode = "near";
        String queryValue = "40|-79|1046.6|km";

        MultiResourceResult result = runQueryTest(searchParamCode, queryValue);
        assertNotNull(result);
        assertNotEquals(result.getResourceResults().size(), 0);
    }

    @Test
    public void testSearchPositionSearchExactMatch() throws Exception {
        // Should match the loaded resource
        String searchParamCode = "near";
        String queryValue = "42.25475478|-83.6945691|0.0|km";

        MultiResourceResult result = runQueryTest(searchParamCode, queryValue);
        assertNotNull(result);
        assertNotEquals(result.getResourceResults().size(), 0);
        assertNull(result.getOutcome());
    }

    @Test
    public void testSearchPositionSearchExactMatchMultiples() throws Exception {
        // Should match the loaded resource
        String searchParamCode = "near";
        String queryValue1 = "42.25475478|-83.6945691|0.0|km";
        String queryValue2 = "42.25475478|-83.6945691|0.0|km";

        MultiResourceResult result = runQueryTestMultiples(searchParamCode, queryValue1, queryValue2);
        assertNotNull(result);
        assertNotEquals(result.getResourceResults().size(), 0);
        assertNull(result.getOutcome());
    }

    @Test
    public void testSearchPositionSearchExactMatchNotMatching() throws Exception {
        // Should not match (opposite the loaded resource)
        String searchParamCode = "near";
        String queryValue = "83.6945691|-42.25475478|0.0|km";

        MultiResourceResult result = runQueryTest(searchParamCode, queryValue);
        assertNotNull(result);
        assertEquals(result.getResourceResults().size(), 0);
    }

    @Test
    public void testSearchPositionSearchExactMatchUnitMiles() throws Exception {
        // Should match the loaded resource
        String searchParamCode = "near";
        String queryValue = "42.25475478|-83.6945691|0.0|mi_us";

        MultiResourceResult result = runQueryTest(searchParamCode, queryValue);
        assertNotNull(result);
        assertFalse(result.getResourceResults().size() == 0);
        assertNull(result.getOutcome());
    }

    @Test(expectedExceptions = { FHIRPersistenceException.class })
    public void testSearchPositionSearchBadPrefix() throws Exception {
        // Should not match (opposite the loaded resource)
        String searchParamCode = "near";
        String queryValue = "ap83.6945691|-42.25475478|0.0|km";

        MultiResourceResult result = runQueryTest(searchParamCode, queryValue);
        assertNotNull(result);
        assertTrue(result.getResourceResults().size() == 0);
    }

    @Test(expectedExceptions = { FHIRPersistenceException.class })
    public void testSearchPositionSearchBadInputLon() throws Exception {
        // Bad Input - Latitude
        String searchParamCode = "near";
        String queryValue = "-42.25475478|FUDGESHOULDNOTMATCH|0.0|km";

        MultiResourceResult result = runQueryTest(searchParamCode, queryValue);
        assertNotNull(result);
        assertTrue(result.getResourceResults().size() == 0);
    }

    @Test(expectedExceptions = { FHIRPersistenceException.class })
    public void testSearchPositionSearchBadInputLat() throws Exception {
        // Bad Input - Latitude
        String searchParamCode = "near";
        String queryValue = "FUDGESHOULDNOTMATCH|-42.25475478|0.0|km";

        MultiResourceResult result = runQueryTest(searchParamCode, queryValue);
        assertNotNull(result);
        assertTrue(result.getResourceResults().size() == 0);
    }

    @Test(expectedExceptions = { FHIRPersistenceException.class })
    public void testSearchPositionSearchBadInputRadius() throws Exception {
        // Bad Input - Latitude
        String searchParamCode = "near";
        String queryValue = "-42.25475478|-42.25475478|FUDGESHOULDNOTMATCH|km";

        MultiResourceResult result = runQueryTest(searchParamCode, queryValue);
        assertNotNull(result);
        assertTrue(result.getResourceResults().size() == 0);
    }

    @Test(expectedExceptions = { FHIRPersistenceException.class })
    public void testSearchPositionSearchBadInputUnit() throws Exception {
        // Bad Input - Latitude
        String searchParamCode = "near";
        String queryValue = "-42.25475478|-42.25475478|0.0|FUDGESHOULDNOTMATCH";

        MultiResourceResult result = runQueryTest(searchParamCode, queryValue);
        assertNotNull(result);
        assertTrue(result.getResourceResults().size() == 0);
    }

    @Test
    public void testSearchPositionSearchExactMatchGoodPrefix() throws Exception {
        // Should match the loaded resource
        String searchParamCode = "near";
        String queryValue = "eq42.25475478|-83.6945691|0.0|km";

        MultiResourceResult result = runQueryTest(searchParamCode, queryValue);
        assertNotNull(result);
        assertFalse(result.getResourceResults().size() == 0);
        assertNull(result.getOutcome());
    }
}