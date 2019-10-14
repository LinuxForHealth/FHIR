/*
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.search.test;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.fail;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.List;

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
import com.ibm.fhir.model.util.FHIRUtil;
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
        saveBasicResource(getBasicResource());
        createCompositionReferencingSavedResource();
    }

    @AfterClass
    public void removeSavedResourcesAndResetTenant() throws Exception {
        if (savedResource != null && persistence.isDeleteSupported()) {
            persistence.delete(getDefaultPersistenceContext(), Basic.class, savedResource.getId().getValue());
            if (persistence.isTransactional()) {
                persistence.getTransaction().commit();
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
        assertNotNull(resource.getId().getValue());
        assertNotNull(resource.getMeta());
        assertNotNull(resource.getMeta().getVersionId().getValue());
        assertEquals("1", resource.getMeta().getVersionId().getValue());
        
        // update the resource to verify that historical versions won't be returned in search results
        result = persistence.update(getDefaultPersistenceContext(), resource.getId().getValue(), resource);
        assertTrue(result.isSuccess());
        resource = result.getResource();
        assertNotNull(resource);
        assertNotNull(resource.getId());
        assertNotNull(resource.getId().getValue());
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
     * Asserts that the savedResource is *not* in the search result set
     * @throws Exception
     */
    protected void assertSearchDoesntReturnSavedResource(String searchParamName, String queryValue) throws Exception {
        assertFalse("Unexpected resource was returned from the search",
            searchReturnsResource(searchParamName, queryValue, savedResource));
    }

    /**
     * Executes the query test and returns whether the expected resource was in the result set
     * @throws Exception
     */
    protected boolean searchReturnsResource(String searchParamName, String queryValue, Resource expectedResource) throws Exception {
        List<? extends Resource> resources = runQueryTest(expectedResource.getClass(), persistence, searchParamName, queryValue, Integer.MAX_VALUE);
        assertNotNull(resources);
        if (resources.size() > 0) {
            Resource returnedResource = findResourceInResponse(expectedResource, resources);
            if (returnedResource != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * If the {@code resourceToFind} is contained in the list of resources this method returns the resource.
     * Otherwise it returns null.
     * @param resources
     */
    protected Resource findResourceInResponse(Resource resourceToFind, List<? extends Resource> resources) {
        Resource returnedResource = null;
        boolean alreadyFound = false;
        int count = 0;
        
        String resourceTypeToFind = FHIRUtil.getResourceTypeName(resourceToFind);
        String idToFind = resourceToFind.getId().getValue();
        String versionToFind = resourceToFind.getMeta().getVersionId().getValue();
        
        for (Resource r : resources) {
            String resourceType = FHIRUtil.getResourceTypeName(r);
            String id = r.getId().getValue();
            String version = r.getMeta().getVersionId().getValue();
            if (idToFind.equals(id) && resourceTypeToFind.equals(resourceType)) {
                if (versionToFind.equals(version)) {
                    count++;
                    if (alreadyFound) {
                        System.out.println("found resource with id " + id + " " + count + " times.");
                        fail("Resource with id '" + id + "' was returned multiple times in the search.");
                    }
                    returnedResource = r;
                    alreadyFound = true;
                } else {
                    fail("Search has returned historical resource for resource id '" + id + "'.\n"
                            + "Expected: version " + resourceToFind.getMeta().getVersionId().getValue() + "\n"
                            + "Actual: version " + version);
                }
            }
        }
        return returnedResource;
    }

    /**
     * Create a Composition with a reference to savedResource for chained search tests. 
     * @throws Exception
     */
    protected Composition createCompositionReferencingSavedResource() throws Exception {
        
        Reference ref = Reference.builder()
                .reference(com.ibm.fhir.model.type.String.of("Basic/" + savedResource.getId().getValue()))
                .build();
        
        composition = Composition.builder()
                .subject(ref)
                .status(CompositionStatus.builder().value(CompositionStatus.ValueSet.PRELIMINARY).build())
                .type(CodeableConcept.builder().text(string("test")).build())
                .category(CodeableConcept.builder().text(string("test")).build())
                .date(DateTime.of("2019"))
                .author(Reference.builder().display(string("Some Guy")).build())
                .title(string("TEST"))
                .build();
        
        SingleResourceResult<Composition> result = persistence.create(getDefaultPersistenceContext(), composition);
        assertTrue(result.isSuccess());
        composition = result.getResource();
        assertNotNull(composition);
        assertNotNull(composition.getId());
        assertNotNull(composition.getId().getValue());
        assertNotNull(composition.getMeta());
        assertNotNull(composition.getMeta().getVersionId().getValue());
        assertEquals("1", composition.getMeta().getVersionId().getValue());
        
        // update the resource to verify that historical versions won't be returned in search results
        result = persistence.update(getDefaultPersistenceContext(), composition.getId().getValue(), composition);
        assertTrue(result.isSuccess());
        composition = result.getResource();
        assertNotNull(composition);
        assertNotNull(composition.getId());
        assertNotNull(composition.getId().getValue());
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
