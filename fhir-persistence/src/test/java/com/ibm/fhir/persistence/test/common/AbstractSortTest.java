/*
 * (C) Copyright IBM Corp. 2016, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.test.common;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.model.resource.Basic;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Integer;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.persistence.ResourceResult;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.util.FHIRPersistenceTestSupport;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.exception.FHIRSearchException;

/**
 * This class contains a collection of search result sorting related tests that will be run against
 * each of the various persistence layer implementations that implement a subclass of this class.
 *
 */
public abstract class AbstractSortTest extends AbstractPersistenceTest {
    Basic resource1a;
    Basic resource2a;
    Basic resource3a;
    Basic resource1b;
    Basic resource2b;
    Basic resource3b;

    @BeforeClass
    public void createResources() throws Exception {
        FHIRRequestContext.get().setTenantId("all");

        Basic resource = TestUtil.getMinimalResource(Basic.class);

        Basic.Builder resource1Builder = resource.toBuilder();
        Basic.Builder resource2Builder = resource.toBuilder();
        Basic.Builder resource3Builder = resource.toBuilder();

        // number
        resource1Builder.extension(extension("http://example.org/integer", Integer.of(1)));
        resource2Builder.extension(extension("http://example.org/integer", Integer.of(2)));
        resource3Builder.extension(extension("http://example.org/integer", Integer.of(3)));
        // date
        resource1Builder.extension(extension("http://example.org/date", Date.of("2019-01-01")));
        resource2Builder.extension(extension("http://example.org/date", Date.of("2019-01-02")));
        resource3Builder.extension(extension("http://example.org/date", Date.of("2019-01-03")));
        // reference
        resource1Builder.extension(extension("http://example.org/Reference", reference("urn:1")));
        resource2Builder.extension(extension("http://example.org/Reference", reference("urn:2")));
        resource3Builder.extension(extension("http://example.org/Reference", reference("urn:3")));
        // quantity
        resource1Builder.extension(extension("http://example.org/Quantity", quantity(1, "s")));
        resource2Builder.extension(extension("http://example.org/Quantity", quantity(2, "s")));
        resource3Builder.extension(extension("http://example.org/Quantity", quantity(3, "s")));
        // uri
        resource1Builder.extension(extension("http://example.org/uri", Uri.of("value1")));
        resource2Builder.extension(extension("http://example.org/uri", Uri.of("value2")));
        resource3Builder.extension(extension("http://example.org/uri", Uri.of("value3")));
        // string
        resource1Builder.extension(extension("http://example.org/string", string("value1")));
        resource2Builder.extension(extension("http://example.org/string", string("value2")));
        resource3Builder.extension(extension("http://example.org/string", string("value3")));
        // token
        resource1Builder.extension(extension("http://example.org/code", Code.of("value1")));
        resource2Builder.extension(extension("http://example.org/code", Code.of("value2")));
        resource3Builder.extension(extension("http://example.org/code", Code.of("value3")));

        // transaction required to ensure that all the search parameter values are persisted (flushed)
        // before we attempt to run any search queries
        persistence.getTransaction().begin();
        try {
            // save them in-order so that lastUpdated goes from 1 -> 3 as well
            resource1a = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), resource1Builder.meta(tag("a")).build()).getResource();
            resource1b = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), resource1Builder.meta(tag("b")).build()).getResource();
            resource2a = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), resource2Builder.meta(tag("a")).build()).getResource();
            resource2b = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), resource2Builder.meta(tag("b")).build()).getResource();
            resource3a = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), resource3Builder.meta(tag("a")).build()).getResource();
            resource3b = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), resource3Builder.meta(tag("b")).build()).getResource();
        } finally {
            persistence.getTransaction().end();
        }
    }

    @AfterClass
    public void removeSavedResourcesAndResetTenant() throws Exception {
        Resource[] resources = {resource1a, resource1b, resource2a, resource2b, resource3a, resource3b};
        if (persistence.isDeleteSupported()) {
            if (persistence.isTransactional()) {
                persistence.getTransaction().begin();
            }
            for (Resource resource : resources) {
                FHIRPersistenceTestSupport.delete(persistence, getDefaultPersistenceContext(), resource);
            }
            if (persistence.isTransactional()) {
                persistence.getTransaction().end();
            }
        }
        FHIRRequestContext.get().setTenantId("default");
    }

    @Test
    public void testNumberSort() throws Exception {
        List<Resource> results;

        results = runQueryTest(Basic.class, "_sort", "integer", 100);
        assertAscendingOrder(results);
    }
    @Test
    public void testDateSort() throws Exception {
        List<Resource> results = runQueryTest(Basic.class, "_sort", "date", 100);
        assertAscendingOrder(results);
    }
    @Test
    public void testReferenceSort() throws Exception {
        List<Resource> results;

        results = runQueryTest(Basic.class, "_sort", "Reference", 100);
        assertAscendingOrder(results);
    }
    @Test
    public void testQuantitySort() throws Exception {
        List<Resource> results;

        results = runQueryTest(Basic.class, "_sort", "Quantity", 100);
        assertAscendingOrder(results);
    }
    @Test
    public void testUriSort() throws Exception {
        List<Resource> results;

        results = runQueryTest(Basic.class, "_sort", "uri", 100);
        assertAscendingOrder(results);
    }
    @Test
    public void testStringSort() throws Exception {
        List<Resource> results;

        results = runQueryTest(Basic.class, "_sort", "string", 100);
        assertAscendingOrder(results);
    }
    @Test
    public void testTokenSort() throws Exception {
        List<Resource> results;

        results = runQueryTest(Basic.class, "_sort", "code", 100);
        assertAscendingOrder(results);
    }

    @Test
    public void testMultiSort() throws Exception {
        List<Resource> results;

        results = runQueryTest(Basic.class, "_sort", "integer,_tag", 100);
        assertAscendingOrder(results);
        assertSecondarySort(results);

        results = runQueryTest(Basic.class, "_sort", "date,_tag", 100);
        assertAscendingOrder(results);
        assertSecondarySort(results);

        results = runQueryTest(Basic.class, "_sort", "Reference,_tag", 100);
        assertAscendingOrder(results);
        assertSecondarySort(results);

        results = runQueryTest(Basic.class, "_sort", "Quantity,_tag", 100);
        assertAscendingOrder(results);
        assertSecondarySort(results);

        results = runQueryTest(Basic.class, "_sort", "uri,_tag", 100);
        assertAscendingOrder(results);
        assertSecondarySort(results);

        results = runQueryTest(Basic.class, "_sort", "string,_tag", 100);
        assertAscendingOrder(results);
        assertSecondarySort(results);

        results = runQueryTest(Basic.class, "_sort", "code,_tag", 100);
        assertAscendingOrder(results);
        assertSecondarySort(results);

        results = runQueryTest(Basic.class, "_sort", "integer,_id", 100);
        assertAscendingOrder(results);
        assertSecondarySort(results);

        results = runQueryTest(Basic.class, "_sort", "date,_id", 100);
        assertAscendingOrder(results);
        assertSecondarySort(results);

        results = runQueryTest(Basic.class, "_sort", "Reference,_id", 100);
        assertAscendingOrder(results);
        assertSecondarySort(results);

        results = runQueryTest(Basic.class, "_sort", "Quantity,_id", 100);
        assertAscendingOrder(results);
        assertSecondarySort(results);

        results = runQueryTest(Basic.class, "_sort", "uri,_id", 100);
        assertAscendingOrder(results);
        assertSecondarySort(results);

        results = runQueryTest(Basic.class, "_sort", "string,_id", 100);
        assertAscendingOrder(results);
        assertSecondarySort(results);

        results = runQueryTest(Basic.class, "_sort", "code,_id", 100);
        assertAscendingOrder(results);
        assertSecondarySort(results);

        results = runQueryTest(Basic.class, "_sort", "integer,_lastUpdated", 100);
        assertAscendingOrder(results);
        assertSecondarySort(results);

        results = runQueryTest(Basic.class, "_sort", "date,_lastUpdated", 100);
        assertAscendingOrder(results);
        assertSecondarySort(results);

        results = runQueryTest(Basic.class, "_sort", "Reference,_lastUpdated", 100);
        assertAscendingOrder(results);
        assertSecondarySort(results);

        results = runQueryTest(Basic.class, "_sort", "Quantity,_lastUpdated", 100);
        assertAscendingOrder(results);
        assertSecondarySort(results);

        results = runQueryTest(Basic.class, "_sort", "uri,_lastUpdated", 100);
        assertAscendingOrder(results);
        assertSecondarySort(results);

        results = runQueryTest(Basic.class, "_sort", "string,_lastUpdated", 100);
        assertAscendingOrder(results);
        assertSecondarySort(results);

        results = runQueryTest(Basic.class, "_sort", "code,_lastUpdated", 100);
        assertAscendingOrder(results);
        assertSecondarySort(results);
    }

    private void assertAscendingOrder(List<Resource> results) {
        assertTrue(results.indexOf(resource1a) < results.indexOf(resource2a));
        assertTrue(results.indexOf(resource2a) < results.indexOf(resource3a));

        assertTrue(results.indexOf(resource1b) < results.indexOf(resource2b));
        assertTrue(results.indexOf(resource2b) < results.indexOf(resource3b));
    }

    @SuppressWarnings("unused")
    private void assertDescendingOrder(List<Resource> results) {
        assertTrue(results.indexOf(resource3a) < results.indexOf(resource2a));
        assertTrue(results.indexOf(resource2a) < results.indexOf(resource1a));

        assertTrue(results.indexOf(resource3b) < results.indexOf(resource2b));
        assertTrue(results.indexOf(resource2b) < results.indexOf(resource1b));
    }

    private void assertSecondarySort(List<Resource> results) {
        assertTrue(results.indexOf(resource1a) < results.indexOf(resource1b));
        assertTrue(results.indexOf(resource2a) < results.indexOf(resource2b));
        assertTrue(results.indexOf(resource3a) < results.indexOf(resource3b));
    }

    /**
     * Tests a system-level search with a sort parameter not defined for the FHIR Resource type.
     * @throws Exception
     */
    @Test
    public void testResourceInvalidSortParm1_lenient() throws Exception {
        Class<Resource> resourceType = Resource.class;
        FHIRSearchContext searchContext;
        FHIRPersistenceContext persistenceContext;
        Map<String, List<String>> queryParameters = new HashMap<>();

        queryParameters.put("_lastUpdated", Collections.singletonList("ge2018-03-27"));
        queryParameters.put("_sort", Arrays.asList(new String[] {"bogus"}));

        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters, true, true);
        searchContext.setPageSize(100);
        persistenceContext = getPersistenceContextForSearch(searchContext);
        List<ResourceResult<? extends Resource>> resources = persistence.search(persistenceContext, resourceType).getResourceResults();
        assertNotNull(resources);
        assertTrue(resources.size() > 0);
    }

    /**
     * Tests a system-level search with a sort parameter not defined for the FHIR Resource type.
     * @throws Exception
     */
    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testResourceInvalidSortParm1_strict() throws Exception {
        Class<Resource> resourceType = Resource.class;
        Map<String, List<String>> queryParameters = new HashMap<>();

        queryParameters.put("_lastUpdated", Collections.singletonList("ge2018-03-27"));
        queryParameters.put("_sort", Arrays.asList(new String[] {"bogus"}));

        searchHelper.parseQueryParameters(resourceType, queryParameters, false, true);
    }
    /**
     * Tests a system-level search with a sort parameter that is defined for the FHIR Resource type,
     * but not supported by our FHIR server
     * @throws Exception
     */
    @Test(expectedExceptions = { FHIRSearchException.class })
    public void testResourceInvalidSortParm2() throws Exception {
        Class<Resource> resourceType = Resource.class;
        FHIRSearchContext searchContext;
        FHIRPersistenceContext persistenceContext;
        Map<String, List<String>> queryParameters = new HashMap<>();

        queryParameters.put("_lastUpdated", Collections.singletonList("ge2018-03-27"));
        queryParameters.put("_sort", Arrays.asList(new String[] {"_profile"}));

        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
        searchContext.setPageSize(100);
        persistenceContext = getPersistenceContextForSearch(searchContext);
        persistence.search(persistenceContext, resourceType);
    }

    /**
     * Tests a system-level search with a valid search parameter and a valid sort parameter
     * of _id.
     * @throws Exception
     */
    @Test
    public void testResourceValidSortParm1() throws Exception {
        Class<Resource> resourceType = Resource.class;
        FHIRSearchContext searchContext;
        FHIRPersistenceContext persistenceContext;
        Map<String, List<String>> queryParameters = new HashMap<>();

        queryParameters.put("_lastUpdated", Collections.singletonList("ge2018-03-27"));
        queryParameters.put("_sort", Arrays.asList(new String[] {"_id"}));

        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
        searchContext.setPageSize(1000);
        persistenceContext = getPersistenceContextForSearch(searchContext);
        List<ResourceResult<? extends Resource>> resources = persistence.search(persistenceContext, resourceType).getResourceResults();
        assertNotNull(resources);
        assertFalse(resources.isEmpty());

        String previousId = null;
        String currentId = null;
        // Verify that resources are sorted in ascending order of logical id.
        for (ResourceResult<? extends Resource> resourceResult : resources) {
            Resource resource = resourceResult.getResource();
            if (previousId == null) {
                previousId = resource.getId();
            } else {
                currentId = resource.getId();
                assertTrue(previousId.compareTo(resource.getId()) <=0);
                previousId = currentId;
            }
        }
    }

    /**
     * Tests a system-level search with a valid search parameter and a valid sort parameter
     * of _lastUpdated.
     * @throws Exception
     */
    @Test
    public void testResourceValidSortParm2() throws Exception {
        Class<Resource> resourceType = Resource.class;
        FHIRSearchContext searchContext;
        FHIRPersistenceContext persistenceContext;
        Map<String, List<String>> queryParameters = new HashMap<>();

        queryParameters.put("_tag:missing", Collections.singletonList("false"));
        queryParameters.put("_sort", Arrays.asList(new String[] {"_lastUpdated"}));

        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
        searchContext.setPageSize(1000);
        persistenceContext = getPersistenceContextForSearch(searchContext);
        List<ResourceResult<? extends Resource>> resources = persistence.search(persistenceContext, resourceType).getResourceResults();
        assertNotNull(resources);
        assertFalse(resources.isEmpty());

        Instant previousLastUpdated = null;
        Instant currentLastUpdated = null;
        // Verify that resources are sorted in ascending order of last updated.
        for (ResourceResult<? extends Resource> resourceResult : resources) {
            Resource resource = resourceResult.getResource();
            if (previousLastUpdated == null) {
                previousLastUpdated = resource.getMeta().getLastUpdated();
            }
            else {
                currentLastUpdated = resource.getMeta().getLastUpdated();
                assertTrue(previousLastUpdated.getValue().compareTo(currentLastUpdated.getValue()) <=0);
                previousLastUpdated = currentLastUpdated;
            }
        }
    }

    /**
     * Tests a system-level search with a valid search parameter and a valid sort parameter
     * of _id descending.
     * @throws Exception
     */
    @Test
    public void testResourceValidSortParm3() throws Exception {
        Class<Resource> resourceType = Resource.class;
        FHIRSearchContext searchContext;
        FHIRPersistenceContext persistenceContext;
        Map<String, List<String>> queryParameters = new HashMap<>();

        queryParameters.put("_tag:missing", Collections.singletonList("false"));
        queryParameters.put("_sort", Arrays.asList(new String[] {"-_id"}));

        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
        searchContext.setPageSize(1000);
        persistenceContext = getPersistenceContextForSearch(searchContext);
        List<ResourceResult<? extends Resource>> resources = persistence.search(persistenceContext, resourceType).getResourceResults();
        assertNotNull(resources);
        assertFalse(resources.isEmpty());

        String previousId = null;
        String currentId = null;
        // Verify that resources are sorted in descending order of logical id.
        for (ResourceResult<? extends Resource> resourceResult : resources) {
            Resource resource = resourceResult.getResource();
            if (previousId == null) {
                previousId = resource.getId();
            }
            else {
                currentId = resource.getId();
                assertTrue(previousId.compareTo(resource.getId()) >=0);
                previousId = currentId;
            }
        }
    }

    /**
     * Tests a system-level search with a valid search parameter and a valid sort parameter
     * of _lastUpdated descending.
     * @throws Exception
     */
    @Test
    public void testResourceValidSortParm4() throws Exception {
        Class<Resource> resourceType = Resource.class;
        FHIRSearchContext searchContext;
        FHIRPersistenceContext persistenceContext;
        Map<String, List<String>> queryParameters = new HashMap<>();

        queryParameters.put("_tag:missing", Collections.singletonList("false"));
        queryParameters.put("_sort", Arrays.asList(new String[] {"-_lastUpdated"}));

        searchContext = searchHelper.parseQueryParameters(resourceType, queryParameters);
        searchContext.setPageSize(1000);
        persistenceContext = getPersistenceContextForSearch(searchContext);
        List<ResourceResult<? extends Resource>> resources = persistence.search(persistenceContext, resourceType).getResourceResults();
        assertNotNull(resources);
        assertFalse(resources.isEmpty());

        Instant previousLastUpdated = null;
        Instant currentLastUpdated = null;
        // Verify that resources are sorted in descending order of last updated.
        for (ResourceResult<? extends Resource> resourceResult : resources) {
            Resource resource = resourceResult.getResource();
            if (previousLastUpdated == null) {
                previousLastUpdated = resource.getMeta().getLastUpdated();
            }
            else {
                currentLastUpdated = resource.getMeta().getLastUpdated();
                assertTrue(previousLastUpdated.getValue().compareTo(currentLastUpdated.getValue()) >=0);
                previousLastUpdated = currentLastUpdated;
            }
        }
    }

    /**
     * Tests a system-level _id search.
     * @throws Exception
     */
    @Test
    public void testIdSortSystemLevel() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, "_sort", "_id", 1000);
        assertNotNull(resources);
        assertFalse(resources.isEmpty());

        String previousId = null;
        String currentId = null;
        // Verify that resources are sorted in ascending order of logical id.
        for (Resource resource : resources) {
            if (previousId == null) {
                previousId = resource.getId();
            }
            else {
                currentId = resource.getId();
                assertTrue(previousId.compareTo(resource.getId()) <=0);
                previousId = currentId;
            }
        }
    }

    /**
     * Tests a system-level _id search descending.
     * @throws Exception
     */
    @Test
    public void testIdSortSystemLevelDescending() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, "_sort", "-_id", 1000);
        assertNotNull(resources);
        assertFalse(resources.isEmpty());

        String previousId = null;
        String currentId = null;
        // Verify that resources are sorted in descending order of logical id.
        for (Resource resource : resources) {
            if (previousId == null) {
                previousId = resource.getId();
            }
            else {
                currentId = resource.getId();
                assertTrue(previousId.compareTo(resource.getId()) >=0);
                previousId = currentId;
            }
        }
    }

    /**
     * Tests a system-level _lastUpdated search.
     * @throws Exception
     */
    @Test
    public void testLastUpdatedSortSystemLevel() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, "_sort", "_lastUpdated", 1000);
        assertNotNull(resources);
        assertFalse(resources.isEmpty());

        Instant previousLastUpdated = null;
        Instant currentLastUpdated = null;
        // Verify that resources are sorted in ascending order of last updated.
        for (Resource resource : resources) {
            if (previousLastUpdated == null) {
                previousLastUpdated = resource.getMeta().getLastUpdated();
            }
            else {
                currentLastUpdated = resource.getMeta().getLastUpdated();
                assertTrue(previousLastUpdated.getValue().compareTo(currentLastUpdated.getValue()) <=0);
                previousLastUpdated = currentLastUpdated;
            }
        }
    }

    /**
     * Tests a system-level _lastUpdated search descending.
     * @throws Exception
     */
    @Test
    public void testLastUpdatedSortSystemLevelDescending() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, "_sort", "-_lastUpdated", 1000);
        assertNotNull(resources);
        assertFalse(resources.isEmpty());

        Instant previousLastUpdated = null;
        Instant currentLastUpdated = null;
        // Verify that resources are sorted in descending order of last updated.
        for (Resource resource : resources) {
            if (previousLastUpdated == null) {
                previousLastUpdated = resource.getMeta().getLastUpdated();
            }
            else {
                currentLastUpdated = resource.getMeta().getLastUpdated();
                assertTrue(previousLastUpdated.getValue().compareTo(currentLastUpdated.getValue()) >=0);
                previousLastUpdated = currentLastUpdated;
            }
        }
    }

    /**
     * Tests a resource-level _id search.
     * @throws Exception
     */
    @Test
    public void testIdSortResourceLevel() throws Exception {
        List<Resource> resources = runQueryTest(Basic.class, "_sort", "_id", 100);
        assertAscendingOrder(resources);
    }

    /**
     * Tests a system-level _id search descending.
     * @throws Exception
     */
    @Test
    public void testIdSortResourceLevelDescending() throws Exception {
        List<Resource> resources = runQueryTest(Basic.class, "_sort", "-_id", 1000);
        assertDescendingOrder(resources);
    }

    /**
     * Tests a system-level _lastUpdated search.
     * @throws Exception
     */
    @Test
    public void testLastUpdatedSortResourceLevel() throws Exception {
        List<Resource> resources = runQueryTest(Basic.class, "_sort", "_lastUpdated", 1000);
        assertAscendingOrder(resources);
    }

    /**
     * Tests a system-level _lastUpdated search descending.
     * @throws Exception
     */
    @Test
    public void testLastUpdatedSortResourceLevelDescending() throws Exception {
        List<Resource> resources = runQueryTest(Basic.class, "_sort", "-_lastUpdated", 1000);
        assertDescendingOrder(resources);
    }

    private Meta tag(String tag) {
        return Meta.builder()
                   .tag(Coding.builder()
                              .code(Code.of(tag))
                              .build())
                   .build();
    }

    private Reference reference(String url) {
        return Reference.builder()
                        .reference(string(url))
                        .build();
    }

    private Quantity quantity(double value, String unit) {
        return Quantity.builder()
                       .value(Decimal.of(value))
                       .unit(string(unit))
                       .build();
    }

    private Extension extension(String url, Element value) {
        return Extension.builder()
                        .url(url)
                        .value(value)
                        .build();
    }
}
