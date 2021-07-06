/*
 * (C) Copyright IBM Corp. 2018, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.search.test;

import static com.ibm.fhir.model.test.TestUtil.isResourceInResponse;
import static com.ibm.fhir.model.type.String.string;
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

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.model.resource.Basic;
import com.ibm.fhir.model.resource.Composition;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.code.CompositionStatus;
import com.ibm.fhir.persistence.SingleResourceResult;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.test.common.AbstractPersistenceTest;

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
            persistence.delete(getDefaultPersistenceContext(), Basic.class, savedResource.getId());
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
        SingleResourceResult<Basic> result = persistence.create(getDefaultPersistenceContext(), resource);
        assertTrue(result.isSuccess());
        resource = result.getResource();
        assertNotNull(resource);
        assertNotNull(resource.getId());
        assertNotNull(resource.getMeta());
        assertNotNull(resource.getMeta().getVersionId().getValue());
        assertEquals("1", resource.getMeta().getVersionId().getValue());

        // update the resource to verify that historical versions won't be returned in search results
        result = persistence.update(getDefaultPersistenceContext(), resource.getId(), resource);
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
        List<? extends Resource> resources = runQueryTest(resourceTypeToSearch, queryParms, Integer.MAX_VALUE);
        assertNotNull(resources);
        return isResourceInResponse(expectedResource, resources);
    }

    /**
     * Create a Composition with a reference to savedResource for chained search tests.
     * @throws Exception
     */
    protected Composition createCompositionReferencingSavedResource() throws Exception {

        Reference ref = Reference.builder()
                .reference(com.ibm.fhir.model.type.String.of("Basic/" + savedResource.getId()))
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

        SingleResourceResult<Composition> result = persistence.create(getDefaultPersistenceContext(), composition);
        assertTrue(result.isSuccess());
        composition = result.getResource();
        assertNotNull(composition);
        assertNotNull(composition.getId());
        assertNotNull(composition.getMeta());
        assertNotNull(composition.getMeta().getVersionId().getValue());
        assertEquals("1", composition.getMeta().getVersionId().getValue());

        // update the resource to verify that historical versions won't be returned in search results
        result = persistence.update(getDefaultPersistenceContext(), composition.getId(), composition);
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
