/**
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.search.test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.List;

import org.testng.annotations.AfterClass;

import com.ibm.watsonhealth.fhir.config.FHIRRequestContext;
import com.ibm.watsonhealth.fhir.model.Basic;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.watsonhealth.fhir.persistence.test.common.AbstractPersistenceTest;

/**
 * An abstract parent for the persistence layer search tests
 * @author lmsurpre
 *
 */
public abstract class AbstractPLSearchTest extends AbstractPersistenceTest{

    protected Basic savedResource;
    
    @AfterClass
    public void removeTenant() throws Exception {
        if (savedResource != null && persistence.isDeleteSupported()) {
            persistence.delete(getDefaultPersistenceContext(), Basic.class, savedResource.getId().getValue());
        }
        FHIRRequestContext.get().setTenantId("default");
    }
    
    /**
     * Saves the passed resource in the configured persistence layer.
     * Modifies the resource in-place and saves it to {@code savedResource}.
     */
    protected void saveBasicResource(Basic resource) throws FHIRPersistenceException, Exception {
        persistence.create(getDefaultPersistenceContext(), resource);
        assertNotNull(resource);
        assertNotNull(resource.getId());
        assertNotNull(resource.getId().getValue());
        assertNotNull(resource.getMeta());
        assertNotNull(resource.getMeta().getVersionId().getValue());
        assertEquals("1", resource.getMeta().getVersionId().getValue());
        savedResource = resource;
    }

    /**
     * Asserts that the savedResource was in the search result set
     * @throws Exception
     */
    protected void assertSearchReturnsSavedResource(String searchParamName, String queryValue) throws Exception {
        assertTrue("Expected resource was not returned from the search", 
            searchReturnsSavedResource(searchParamName, queryValue));
    }

    /**
     * Asserts that the savedResource is *not* in the search result set
     * @throws Exception
     */
    protected void assertSearchDoesntReturnSavedResource(String searchParamName, String queryValue) throws Exception {
        assertFalse("Unexpected resource was returned from the search", 
            searchReturnsSavedResource(searchParamName, queryValue));
    }

    /**
     * Executes the query test and returns whether the saved resource was in the result set
     * @throws Exception
     */
    private boolean searchReturnsSavedResource(String searchParamName, String queryValue) throws Exception {
        List<Resource> resources = runQueryTest(Basic.class, persistence, searchParamName, queryValue, Integer.MAX_VALUE);
        assertNotNull(resources);
        if (resources.size() > 0) {
            Resource returnedResource = findSavedResourceInResponse(resources);
            if (returnedResource != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the first resource in resources with an id matching the savedResource, otherwise null
     * @param resources
     */
    private Resource findSavedResourceInResponse(List<Resource> resources) {
        Resource returnedResource = null;
        for (Resource r : resources) {
            assertTrue(r instanceof Basic);
            String id = ((Basic)r).getId().getValue();
            if (savedResource.getId().getValue().equals(id)) {
                returnedResource = r;
                break;
            }
        }
        return returnedResource;
    }

}
