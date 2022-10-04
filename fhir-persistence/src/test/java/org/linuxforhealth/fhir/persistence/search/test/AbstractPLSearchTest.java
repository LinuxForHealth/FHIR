/*
 * (C) Copyright IBM Corp. 2018, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.search.test;

import static org.linuxforhealth.fhir.model.test.TestUtil.isResourceInResponse;
import static org.linuxforhealth.fhir.model.type.String.string;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.model.resource.Basic;
import org.linuxforhealth.fhir.model.resource.Composition;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.DateTime;
import org.linuxforhealth.fhir.model.type.Reference;
import org.linuxforhealth.fhir.model.type.code.CompositionStatus;
import org.linuxforhealth.fhir.persistence.ResourceResult;
import org.linuxforhealth.fhir.persistence.SingleResourceResult;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.persistence.test.common.AbstractPersistenceTest;
import org.linuxforhealth.fhir.persistence.util.FHIRPersistenceTestSupport;
import org.linuxforhealth.fhir.search.context.FHIRSearchContext;

/**
 * An abstract parent for the persistence layer search tests.
 *
 * Abstract subclasses in this package implement the logic of the search tests and should
 * be extended by concrete subclasses in each persistence layer implementation.
 *
 * Abstract subclasses that implement the test logic should implement {@code setTenant()}
 * and {@code getBasicResource()} to set up for the tests.
 *
 * @implNote Previously, we used the {@code dependsOnMethod} argument to the {@code @Test} annotation,
 * but this was preventing us from executing single test methods from Eclipse due to
 * https://github.com/cbeust/testng-eclipse/issues/435
 */
public abstract class AbstractPLSearchTest extends AbstractPersistenceTest {
    protected Basic savedResource;
    protected Composition composition;

    /**
     * Each search test must implement this method to configure the tenant to use.
     */
    protected abstract void setTenant() throws Exception;

    /**
     * Each search test must implement this method to specify the basic resource to use for the test.
     * @return
     *      the Basic resource to use in the search tests
     */
    protected abstract Basic getBasicResource() throws Exception;

    @BeforeClass
    public void createResources() throws Exception {
        setTenant();

        // Must have a transaction in place because this done before the class test methods
        persistence.getTransaction().begin();
        try {
            saveBasicResource(getBasicResource());
            createCompositionReferencingSavedResource();
        } finally {
            persistence.getTransaction().end();
        }
    }

    @AfterClass
    public void removeSavedResourcesAndResetTenant() throws Exception {
        if (savedResource != null && persistence.isDeleteSupported()) {
            if (persistence.isTransactional()) {
                persistence.getTransaction().begin();
            }
            FHIRPersistenceTestSupport.delete(persistence, getDefaultPersistenceContext(), savedResource);
            if (persistence.isTransactional()) {
                persistence.getTransaction().end();
            }
        }
        FHIRRequestContext.get().setTenantId("default");
    }

    /**
     * Saves the passed resource in the configured persistence layer.
     * Modifies the resource in-place and saves it to {@code savedResource}.
     */
    protected void saveBasicResource(Basic resource) throws FHIRPersistenceException, Exception {
        SingleResourceResult<Basic> result = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), resource);
        assertTrue(result.isSuccess());
        resource = result.getResource();
        assertNotNull(resource);
        assertNotNull(resource.getId());
        assertNotNull(resource.getMeta());
        assertNotNull(resource.getMeta().getVersionId().getValue());
        assertEquals("1", resource.getMeta().getVersionId().getValue());

        // update the resource to verify that historical versions won't be returned in search results
        result = FHIRPersistenceTestSupport.update(persistence, getDefaultPersistenceContext(), resource.getId(), resource);
        assertTrue(result.isSuccess());
        resource = result.getResource();
        assertNotNull(resource);
        assertNotNull(resource.getId());
        assertNotNull(resource.getMeta());
        assertNotNull(resource.getMeta().getVersionId().getValue());
        assertEquals("2", resource.getMeta().getVersionId().getValue());

        savedResource = resource;
    }

    /**
     * Asserts that the savedResource is in the search result set
     * @throws Exception
     */
    protected void assertSearchReturnsSavedResource(String searchParamName, String queryValue) throws Exception {
        assertTrue("Expected resource was not returned from the search",
                searchReturnsResource(searchParamName, queryValue, savedResource));
    }

    /**
     * Asserts that the savedResource is in the search result set.
     * @throws Exception
     */
    protected void assertSearchReturnsSavedResource(Map<String, List<String>> queryParms) throws Exception {
        assertTrue("Expected resource was not returned from the search",
                searchReturnsResource(savedResource.getClass(), queryParms, savedResource));
    }

    /**
     * Asserts that the savedResource is *not* in the search result set
     * @throws Exception
     */
    protected void assertSearchDoesntReturnSavedResource(String searchParamName, String queryValue) throws Exception {
        assertFalse("Unexpected resource was returned from the search",
                searchReturnsResource(searchParamName, queryValue, savedResource));
    }

    /**
     * Asserts that the savedResource is *not* in the search result set.
     * @throws Exception
     */
    protected void assertSearchDoesntReturnSavedResource(Map<String, List<String>> queryParms) throws Exception {
        assertFalse("Unexpected resource was returned from the search",
                searchReturnsResource(savedResource.getClass(), queryParms, savedResource));
    }

    /**
     * Executes the query test and returns whether the expected resource was in the result set
     * @throws Exception
     */
    protected boolean searchReturnsResource(String searchParamCode, String queryValue, Resource expectedResource) throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>(1);
        if (searchParamCode != null && queryValue != null) {
            queryParms.put(searchParamCode, Collections.singletonList(queryValue));
        }
        return searchReturnsResource(expectedResource.getClass(), queryParms, expectedResource);
    }

    /**
     * Executes the query test and returns whether the expected resource was in the result set
     * @throws Exception
     */
    protected boolean searchReturnsResource(Class<? extends Resource> resourceTypeToSearch, Map<String, List<String>> queryParms, Resource expectedResource) throws Exception {
        List<? extends Resource> resources = runQueryTest(resourceTypeToSearch, queryParms, Integer.MAX_VALUE - 2);
        assertNotNull(resources);
        return isResourceInResponse(expectedResource, resources);
    }

    /**
     * Executes the query test and returns whether the expected resource logicalId was found in
     * the result set
     * @param resourceTypeToSearch the resource type class to use as the base of the search
     * @param queryParms
     * @param expectedLogicalId
     * @param includesData
     * @return
     * @throws Exception
     */
    protected boolean searchReturnsResourceResult(Class<? extends Resource> resourceTypeToSearch, Map<String, List<String>> queryParms, String expectedLogicalId,
            boolean includesData) throws Exception {
        FHIRSearchContext searchContext = searchHelper.parseQueryParameters(resourceTypeToSearch, queryParms);
        searchContext.setIncludeResourceData(includesData);
        List<ResourceResult<? extends Resource>> resourceResults = runQueryTest(
                searchContext,
                resourceTypeToSearch, queryParms, Integer.MAX_VALUE - 2).getResourceResults();

        assertNotNull(resourceResults);

        // Find the logicalId in the output and check that the includesData matches
        boolean result = false;
        for (ResourceResult<? extends Resource> rr: resourceResults) {
            if (rr.getLogicalId().equals(expectedLogicalId)) {
                result = (rr.getResource() != null) == includesData;
                break;
            }
        }
        return result;
    }

    /**
     * Create a Composition with a reference to savedResource for chained search tests.
     * @throws Exception
     */
    protected Composition createCompositionReferencingSavedResource() throws Exception {

        Reference ref = Reference.builder()
                .reference(org.linuxforhealth.fhir.model.type.String.of("Basic/" + savedResource.getId()))
                .build();

        composition = Composition.builder()
                .subject(ref)
                .status(CompositionStatus.builder().value(CompositionStatus.Value.PRELIMINARY).build())
                .type(CodeableConcept.builder().text(string("test")).build())
                .category(CodeableConcept.builder().text(string("test")).build())
                .date(DateTime.of("2019"))
                .author(Reference.builder().reference(string("Patient/123")).build())
                .author(Reference.builder().reference(string("Practitioner/abc")).build())
                .title(string("TEST"))
                .build();

        SingleResourceResult<Composition> result = FHIRPersistenceTestSupport.create(persistence, getDefaultPersistenceContext(), composition);
        assertTrue(result.isSuccess());
        composition = result.getResource();
        assertNotNull(composition);
        assertNotNull(composition.getId());
        assertNotNull(composition.getMeta());
        assertNotNull(composition.getMeta().getVersionId().getValue());
        assertEquals("1", composition.getMeta().getVersionId().getValue());

        // update the resource to verify that historical versions won't be returned in search results
        result = FHIRPersistenceTestSupport.update(persistence, getDefaultPersistenceContext(), composition.getId(), composition);
        assertTrue(result.isSuccess());
        composition = result.getResource();
        assertNotNull(composition);
        assertNotNull(composition.getId());
        assertNotNull(composition.getMeta());
        assertNotNull(composition.getMeta().getVersionId().getValue());
        assertEquals("2", composition.getMeta().getVersionId().getValue());

        return composition;
    }

    /**
     * Asserts that the Composition which references the savedResource is in the search result set
     * @throws Exception
     */
    protected void assertSearchReturnsComposition(String searchParamName, String queryValue) throws Exception {
        assertTrue("Expected resource was not returned from the search",
            searchReturnsResource(searchParamName, queryValue, composition));
    }

    /**
     * Asserts that the Composition which references the savedResource is *not* in the search result set
     * @throws Exception
     */
    protected void assertSearchDoesntReturnComposition(String searchParamName, String queryValue) throws Exception {
        assertFalse("Unexpected resource was returned from the search",
            searchReturnsResource(searchParamName, queryValue, composition));
    }
}
