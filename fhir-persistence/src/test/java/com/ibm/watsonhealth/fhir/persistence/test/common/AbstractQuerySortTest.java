/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.test.common;

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.model.Patient;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.watsonhealth.fhir.search.context.FHIRSearchContext;
import com.ibm.watsonhealth.fhir.search.exception.FHIRSearchException;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;

/**
 *  This class contains a collection of search result sorting related tests that will be run against
 *  each of the various persistence layer implementations that implement a subclass of this class.
 *   
 */
public abstract class AbstractQuerySortTest extends AbstractPersistenceTest {
    
        
    @BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {
        
        super.setUp();
        Patient patient;
        String patientFile;
               
        // Load 5 patients
        for (int i = 1; i <=5; i++) 
        {
            patientFile = "sortTest.patient" + i + ".json";
            patient = readResource(Patient.class, patientFile);
            if(persistence.isTransactional()) {
                persistence.getTransaction().begin();
            }
             persistence.create(getDefaultPersistenceContext(), patient);
             if(persistence.isTransactional()) {
                persistence.getTransaction().commit();
            }
             assertNotNull(patient);
             assertNotNull(patient.getId());
            assertNotNull(patient.getId().getValue());
        }
        
    }
         
    /**
     * Tests a single ascending sort parameter on a Patient search.
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" })
    public void testSingleSortParmAsc() throws Exception {
            
        Class<Patient> resourceType = Patient.class;
        FHIRSearchContext searchContext;
        FHIRPersistenceContext persistenceContext;
        Map<String, List<String>> queryParameters = new HashMap<>();
        String queryString;
        Patient patient;
        String currentFamilyName;
        String previousFamilyName;
            
        queryString = "&deceased=false&_sort=family";
        queryParameters.put("deceased", Collections.singletonList("false"));
        queryParameters.put("_sort", Arrays.asList(new String[] {"family"}));
                
        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
        searchContext.setPageSize(100);
        persistenceContext = getPersistenceContextForSearch(searchContext);
        List<Resource> resources = persistence.search(persistenceContext, resourceType);
        assertNotNull(resources);
        assertTrue(resources.size() >= 4);
        
        previousFamilyName = null;
        for (Resource resource : resources) {
            patient = (Patient) resource;
            assertFalse(patient.getDeceasedBoolean().isValue().booleanValue());
            currentFamilyName = patient.getName().get(0).getFamily().get(0).getValue();
            assertNotNull(currentFamilyName);
            if (previousFamilyName != null) {
                assertTrue(currentFamilyName.compareTo(previousFamilyName) >= 0);
            }
            previousFamilyName = currentFamilyName;
        }
    }
    
    /**
     * Tests a single descending sort parameter on a Patient search.
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" })
    public void testSingleSortParmDesc() throws Exception {
            
        Class<Patient> resourceType = Patient.class;
        FHIRSearchContext searchContext;
        FHIRPersistenceContext persistenceContext;
        Map<String, List<String>> queryParameters = new HashMap<>();
        String queryString;
        Patient patient;
        String currentFamilyName;
        String previousFamilyName;
            
        queryString = "&deceased=false&_sort:desc=family";
        queryParameters.put("deceased", Collections.singletonList("false"));
        queryParameters.put("_sort:desc", Arrays.asList(new String[] {"family"}));
                
        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
        searchContext.setPageSize(100);
        persistenceContext = getPersistenceContextForSearch(searchContext);
        List<Resource> resources = persistence.search(persistenceContext, resourceType);
        assertNotNull(resources);
        assertTrue(resources.size() >= 4);
        
        previousFamilyName = null;
        for (Resource resource : resources) {
            patient = (Patient) resource;
            assertFalse(patient.getDeceasedBoolean().isValue().booleanValue());
            currentFamilyName = patient.getName().get(0).getFamily().get(0).getValue();
            assertNotNull(currentFamilyName);
            if (previousFamilyName != null) {
                assertTrue(currentFamilyName.compareTo(previousFamilyName) <= 0);
            }
            previousFamilyName = currentFamilyName;
        }
    }
    
    /**
     * Tests an ascending sort parameter in combination with a descending sort parameter on a Patient search.
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" })
    public void testMultiSortParms() throws Exception {
            
        Class<Patient> resourceType = Patient.class;
        FHIRSearchContext searchContext;
        FHIRPersistenceContext persistenceContext;
        Map<String, List<String>> queryParameters = new HashMap<>();
        String queryString;
        Patient patient;
        String currentOrg;
        String previousOrg;
        String currentPhoneNumber;
        String previousPhoneNumber;
            
        queryString = "&deceased=false&careprovider=Organization/TheCommission&_sort:asc=organization&_sort:desc=telecom";
        queryParameters.put("deceased", Collections.singletonList("false"));
        queryParameters.put("careprovider", Collections.singletonList("Organization/TheCommission"));
        queryParameters.put("_sort:asc", Collections.singletonList("organization"));
        queryParameters.put("_sort:desc", Collections.singletonList("telecom"));
                
        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
        searchContext.setPageSize(100);
        persistenceContext = getPersistenceContextForSearch(searchContext);
        List<Resource> resources = persistence.search(persistenceContext, resourceType);
        assertNotNull(resources);
        assertTrue(resources.size() >= 4);
        
        previousOrg = null;
        previousPhoneNumber = null;
        for (Resource resource : resources) {
            patient = (Patient) resource;
            assertFalse(patient.getDeceasedBoolean().isValue().booleanValue());
            currentOrg = patient.getManagingOrganization().getReference().getValue();
            assertNotNull(currentOrg);
            if (previousOrg != null) {
                assertTrue(currentOrg.compareTo(previousOrg) >= 0);
            }
               currentPhoneNumber = patient.getTelecom().get(0).getValue().getValue();
            assertNotNull(currentPhoneNumber);
            if (previousPhoneNumber != null && currentOrg.equals(previousOrg)) {
                assertTrue(currentPhoneNumber.compareTo(previousPhoneNumber) <= 0);
            }
            previousOrg = currentOrg;
            previousPhoneNumber = currentPhoneNumber;
        }
    }
    
    /**
     * Tests a system-level search with a sort parameter not defined for the FHIR Resource type.
     * @throws Exception
     */
    @Test(groups = { "jdbc-normalized" },expectedExceptions = { FHIRSearchException.class })
    public void testResourceInvalidSortParm1() throws Exception {
        Class<Resource> resourceType = Resource.class;
        FHIRSearchContext searchContext;
        FHIRPersistenceContext persistenceContext;
        Map<String, List<String>> queryParameters = new HashMap<>();
        String queryString;
                    
        queryString = "&_lastUpdated=ge2018-03-27&_sort=bogus";
        queryParameters.put("_lastUpdated", Collections.singletonList("ge2018-03-27"));
        queryParameters.put("_sort", Arrays.asList(new String[] {"bogus"}));
                
        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
        searchContext.setPageSize(100);
        persistenceContext = getPersistenceContextForSearch(searchContext);
        persistence.search(persistenceContext, resourceType);
    }
    /**
     * Tests a system-level search with a sort parameter that is defined for the FHIR Resource type, 
     * but not supported by our FHIR server
     * @throws Exception
     */
    @Test(groups = { "jdbc-normalized" },expectedExceptions = { FHIRSearchException.class })
    public void testResourceInvalidSortParm2() throws Exception {
        Class<Resource> resourceType = Resource.class;
        FHIRSearchContext searchContext;
        FHIRPersistenceContext persistenceContext;
        Map<String, List<String>> queryParameters = new HashMap<>();
        String queryString;
                    
        queryString = "&_lastUpdated=ge2018-03-27&_sort=_profile";
        queryParameters.put("_lastUpdated", Collections.singletonList("ge2018-03-27"));
        queryParameters.put("_sort", Arrays.asList(new String[] {"_profile"}));
                
        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
        searchContext.setPageSize(100);
        persistenceContext = getPersistenceContextForSearch(searchContext);
        persistence.search(persistenceContext, resourceType);
    }
    
    /**
     * Tests a system-level search with a valid search parameter and a valid sort parameter.  
     * @throws Exception
     */
    @Test(groups = { "jdbc-normalized" })
    public void testResourceValidSortParm1() throws Exception {
        Class<Resource> resourceType = Resource.class;
        FHIRSearchContext searchContext;
        FHIRPersistenceContext persistenceContext;
        Map<String, List<String>> queryParameters = new HashMap<>();
        String queryString;
                    
        queryString = "&_lastUpdated=ge2018-03-27&_sort:asc=_id";
        queryParameters.put("_lastUpdated", Collections.singletonList("ge2018-03-27"));
        queryParameters.put("_sort:asc", Arrays.asList(new String[] {"_id"}));
                
        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
        searchContext.setPageSize(1000);
        persistenceContext = getPersistenceContextForSearch(searchContext);
        List<Resource> resources = persistence.search(persistenceContext, resourceType);
        assertNotNull(resources);
        assertFalse(resources.isEmpty());
        
        String previousId = null;
        String currentId = null;
        // Verify that resources are sorted in ascending order of logical id.
        for (Resource resource : resources) {
            if (previousId == null) {
                previousId = resource.getId().getValue();
            }
            else {
                currentId = resource.getId().getValue();
                assertTrue(previousId.compareTo(resource.getId().getValue()) <=0);
                previousId = currentId;
            }
        }
    }
    
    /**
     * Tests a system-level search with a 2 valid sort parameters.  
     * @throws Exception
     */
    @Test(groups = { "jdbc-normalized" })
    public void testResourceValidSortParm2() throws Exception {
        Class<Resource> resourceType = Resource.class;
        FHIRSearchContext searchContext;
        FHIRPersistenceContext persistenceContext;
        Map<String, List<String>> queryParameters = new HashMap<>();
        String queryString;
                    
        queryString = "&_sort:desc=_lastUpdated&_sort:asc=_id";
        queryParameters.put("_sort:desc", Collections.singletonList("_lastUpdated"));
        queryParameters.put("_sort:asc", Arrays.asList(new String[] {"_id"}));
                
        searchContext = SearchUtil.parseQueryParameters(resourceType, queryParameters, queryString);
        searchContext.setPageSize(1000);
        persistenceContext = getPersistenceContextForSearch(searchContext);
        List<Resource> resources = persistence.search(persistenceContext, resourceType);
        assertNotNull(resources);
        assertFalse(resources.isEmpty());
        
        GregorianCalendar previousLastUpdated = null;
        GregorianCalendar currentLastUpdated = null;
        // Verify that resources are sorted in descending order of last updated date/time.
        for (Resource resource : resources) {
            if (previousLastUpdated == null) {
                previousLastUpdated = resource.getMeta().getLastUpdated().getValue().toGregorianCalendar();
            }
            else {
                currentLastUpdated = resource.getMeta().getLastUpdated().getValue().toGregorianCalendar();
                assertTrue(previousLastUpdated.compareTo(currentLastUpdated) >=0);
                previousLastUpdated = currentLastUpdated;
            }
        }
    }
    
}
