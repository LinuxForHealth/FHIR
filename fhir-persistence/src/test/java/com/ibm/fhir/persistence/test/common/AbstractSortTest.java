/*
 * (C) Copyright IBM Corp. 2016, 2019, 2020
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
import com.ibm.fhir.model.type.Integer;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.util.SearchUtil;

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
        
        Basic resource = TestUtil.readExampleResource("json/ibm/minimal/Basic-1.json");
        
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
        
        // save them in-order so that lastUpdated goes from 1 -> 3 as well
        resource1a = persistence.create(getDefaultPersistenceContext(), resource1Builder.meta(tag("a")).build()).getResource();
        resource1b = persistence.create(getDefaultPersistenceContext(), resource1Builder.meta(tag("b")).build()).getResource();
        resource2a = persistence.create(getDefaultPersistenceContext(), resource2Builder.meta(tag("a")).build()).getResource();
        resource2b = persistence.create(getDefaultPersistenceContext(), resource2Builder.meta(tag("b")).build()).getResource();
        resource3a = persistence.create(getDefaultPersistenceContext(), resource3Builder.meta(tag("a")).build()).getResource();
        resource3b = persistence.create(getDefaultPersistenceContext(), resource3Builder.meta(tag("b")).build()).getResource();
    }
    
    @AfterClass
    public void removeSavedResourcesAndResetTenant() throws Exception {
        Resource[] resources = {resource1a, resource1b, resource2a, resource2b, resource3a, resource3b};
        if (persistence.isDeleteSupported()) {
            if (persistence.isTransactional()) {
                persistence.getTransaction().begin();
            }
            for (Resource resource : resources) {
                persistence.delete(getDefaultPersistenceContext(), Basic.class, resource.getId());
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
        
        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters, true);
        searchContext.setPageSize(100);
        persistenceContext = getPersistenceContextForSearch(searchContext);
        List<Resource> resources = persistence.search(persistenceContext, resourceType).getResource();
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
                
        SearchUtil.parseQueryParameters(resourceType, queryParameters, false);
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
                
        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters);
        searchContext.setPageSize(100);
        persistenceContext = getPersistenceContextForSearch(searchContext);
        persistence.search(persistenceContext, resourceType);
    }
    
    /**
     * Tests a system-level search with a valid search parameter and a valid sort parameter.  
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
                
        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters);
        searchContext.setPageSize(1000);
        persistenceContext = getPersistenceContextForSearch(searchContext);
        List<Resource> resources = persistence.search(persistenceContext, resourceType).getResource();
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
