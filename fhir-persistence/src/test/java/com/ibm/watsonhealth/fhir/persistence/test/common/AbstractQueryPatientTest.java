/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.test.common;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.model.type.Address;
import com.ibm.watsonhealth.fhir.model.type.HumanName;
import com.ibm.watsonhealth.fhir.model.resource.Patient;
import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRHistoryContext;
import com.ibm.watsonhealth.fhir.persistence.util.FHIRPersistenceUtil;
import com.ibm.watsonhealth.fhir.search.context.FHIRSearchContext;
import com.ibm.watsonhealth.fhir.search.exception.FHIRSearchException;
import com.ibm.watsonhealth.fhir.search.util.SearchUtil;

/**
 *  This class contains a collection of tests that will be run against
 *  each of the various persistence layer implementations.
 *  There will be a subclass in each persistence project.
 */
public abstract class AbstractQueryPatientTest extends AbstractPersistenceTest {
    
    private static Patient savedPatient;
    
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for a Patient.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" })
    public void testCreatePatient1() throws Exception {
           Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");

        persistence.create(getDefaultPersistenceContext(), patient);
        assertNotNull(patient);
        assertNotNull(patient.getId());
        assertNotNull(patient.getId().getValue());
        assertNotNull(patient.getMeta());
        assertNotNull(patient.getMeta().getVersionId().getValue());
        assertEquals("1", patient.getMeta().getVersionId().getValue());
        savedPatient = patient;
    }
    
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for a Patient.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" })
    public void testCreatePatient2() throws Exception {
           Patient patient = readResource(Patient.class, "patient-example.canonical.json");

        persistence.create(getDefaultPersistenceContext(), patient);
        assertNotNull(patient);
        assertNotNull(patient.getId());
        assertNotNull(patient.getId().getValue());
        assertNotNull(patient.getMeta());
        assertNotNull(patient.getMeta().getVersionId().getValue());
        assertEquals("1", patient.getMeta().getVersionId().getValue());
    }
    
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for a Patient.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" })
    public void testCreatePatient3() throws Exception {
           Patient patient = readResource(Patient.class, "patient-glossy-example.canonical.json");

        persistence.create(getDefaultPersistenceContext(), patient);
        assertNotNull(patient);
        assertNotNull(patient.getId());
        assertNotNull(patient.getId().getValue());
        assertNotNull(patient.getMeta());
        assertNotNull(patient.getMeta().getVersionId().getValue());
        assertEquals("1", patient.getMeta().getVersionId().getValue());
    } 
    
    
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for a Patient.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" })
    public void testCreatePatient4() throws Exception {
        Patient patient = readResource(Patient.class, "Patient1.json");
        persistence.create(getDefaultPersistenceContext(), patient);
        assertNotNull(patient);
        assertNotNull(patient.getId());
        assertNotNull(patient.getId().getValue());
        assertNotNull(patient.getMeta());
        assertNotNull(patient.getMeta().getVersionId().getValue());
        assertEquals("1", patient.getMeta().getVersionId().getValue());
    }
    
    /**
     * Tests the FHIRPersistenceCloudantImpl create API for a Patient.
     * 
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" })
    public void testCreatePatient5() throws Exception {
        Patient patient = readResource(Patient.class, "patient-example-c.canonical.json");
        persistence.create(getDefaultPersistenceContext(), patient);
        assertNotNull(patient);
    }
    
    /**
     * Tests a query with a resource type but without any query parameters. This should yield all the resources created so far.
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient1" })
    public void testPatient_noparams() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, null, null);
        assertTrue(resources.size() != 0);
    }    
    
    /**
     * Tests a query for a Patient with family name = 'Doe' which should yield correct results
     * @throws Exception
     * TODO fix test
     */
//    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient1" })
//    public void testPatient_family() throws Exception {
//        List<Resource> resources = runQueryTest(Patient.class, persistence, "family", "Doe");
//        assertTrue(resources.size() != 0);
//        List<HumanName> hnList = ((Patient)resources.get(0)).getName();
//        assertEquals(hnList.get(0).getFamily().get(0).getValue(),"Doe");
//    }
    
    /**
     * Tests a query for a Patient with name:contains = 'so' which should yield correct results
     * @throws Exception
     * TODO fix test
     */
//    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient5" })
//    public void testPatient_nameContains() throws Exception {
//        List<Resource> resources = runQueryTest(Patient.class, persistence, "name:contains", "so");
//        assertTrue(resources.size() != 0);
//        List<HumanName> hnList = ((Patient)resources.get(0)).getName();
//        assertEquals(hnList.get(0).getFamily().get(0).getValue(),"Notsowell");
//    }

    /**
     * Tests a query for a Patient with family name = 'Non-existent' which should yield no results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient1" })
    public void testPatient_family_noResults() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "family", "Non-existent");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
    }
    
    /**
     * Tests a query for a Patient with given name = 'Clanton' which should yield correct results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient4" })
    public void testPatient_given() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "given", "Clanton");
        assertTrue(resources.size() != 0);
        List<HumanName> hnList = ((Patient)resources.get(0)).getName();
        assertEquals(hnList.get(0).getGiven().get(0).getValue(),"Clanton");
    }

    /**
     * Tests a query for a Patient with given name = 'Clantons' which should yield no results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient4" })
    public void testPatient_given_noResults() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "given", "Clantons");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
    }
    
    /**
     * Tests a query for a Patient with name = 'Clanton Mussenden' which should yield correct results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient4" })
    public void testPatient_name() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "name", "Clanton Mussenden");
        assertTrue(resources.size() != 0);
        List<HumanName> hnList = ((Patient)resources.get(0)).getName();
        assertEquals(hnList.get(0).getText().getValue(),"Clanton Mussenden");
    }
    
    /**
     * Tests a query for a Patient with name = 'Clanton Mussenden' which should yield correct results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient4" })
    public void testPatientByLastName() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "name", "Mussenden");
        assertTrue(resources.size() != 0);
        List<HumanName> hnList = ((Patient)resources.get(0)).getName();
        assertEquals(hnList.get(0).getText().getValue(),"Clanton Mussenden");
    }

    /**
     * Tests a query for a Patient with name = 'Clantons' which should yield no results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient4" })
    public void testPatient_name_noResults() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "name", "Clantons");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
    }
    
    /**
     * Tests a query for a Patient with address-city = 'Amsterdam' which should yield correct results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient1" })
    public void testPatient_city() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "address-city", "Amsterdam");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        List<Address> addrList = ((Patient)resources.get(0)).getAddress();
        assertEquals(addrList.get(0).getCity().getValue(),"Amsterdam");
    }
    
    /**
     * Tests a query for a Patient with address-city = 'Amsterdam' which should yield correct results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient1" })
    public void testPatientAdressByCity() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "address", "Amsterdam");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        List<Address> addrList = ((Patient)resources.get(0)).getAddress();
        assertEquals(addrList.get(0).getCity().getValue(),"Amsterdam");
    }
    
    /**
     * Tests a query for a Patient with address-city = 'Non-existent' which should yield no results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient1" })
    public void testPatient_city_noResults() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "address-city", "Non-existent");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
    }
    
    /**
     * Tests a query for a Patient with address-country = 'NLD' which should yield correct results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient1" })
    public void testPatient_country() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "address-country", "NLD");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        List<Address> addrList = ((Patient)resources.get(0)).getAddress();
        assertEquals(addrList.get(0).getCountry().getValue(),"NLD");
    }
    
    
    /**
     * Tests a query for a Patient with address-country = 'NLD' which should yield correct results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient1" })
    public void testPatientAdressByCountry() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "address", "NLD");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        List<Address> addrList = ((Patient)resources.get(0)).getAddress();
        assertEquals(addrList.get(0).getCountry().getValue(),"NLD");
    }
    
    
    /**
     * Tests a query for a Patient with address-country = 'Non-existent' which should yield no results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient1" })
    public void testPatient_country_noResults() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "address-country", "Non-existent");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
    }
    
    /**
     * Tests a query for a Patient with address-state = 'UT' which should yield correct results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient4" })
    public void testPatient_state() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "address-state", "UT");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        List<Address> addrList = ((Patient)resources.get(0)).getAddress();
        assertEquals(addrList.get(0).getState().getValue(),"UT");
    }
    
    
    /**
     * Tests a query for a Patient with address-state = 'UT' which should yield correct results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient4" })
    public void testPatientAddressByState() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "address", "UT");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        List<Address> addrList = ((Patient)resources.get(0)).getAddress();
        assertEquals(addrList.get(0).getState().getValue(),"UT");
    }
    
    /**
     * Tests a query for a Patient with address-state = 'Non-existent' which should yield no results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient4" })
    public void testPatient_state_noResults() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "address-state", "Non-existent");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
    }
    
    /**
     * Tests a query for a Patient with address-postalcode = '841131103' which should yield correct results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient4" })
    public void testPatient_postalCode() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "address-postalcode", "841131103");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        List<Address> addrList = ((Patient)resources.get(0)).getAddress();
        assertEquals(addrList.get(0).getPostalCode().getValue(),"841131103");
    }
    
    /**
     * Tests a query for a Patient with address-postalcode = '841131103' which should yield correct results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient4" })
    public void testPatientAdressByPostalCode() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "address", "841131103");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        List<Address> addrList = ((Patient)resources.get(0)).getAddress();
        assertEquals(addrList.get(0).getPostalCode().getValue(),"841131103");
    }
    
    /**
     * Tests a query for a Patient with address-postalcode = 'Non-existent' which should yield no results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient4" })
    public void testPatient_postalCode_noResults() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "address-postalcode", "Non-existent");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
    }
    
    
    /**
     * Tests a query for a Patient with gender = 'Male' which should yield correct results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient4" })
    public void testPatient_gender() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "gender", "Male");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        assertEquals(((Patient)resources.get(0)).getGender().getValue(),"Male");
    }
    
    /**
     * Tests a query for a Patient with gender = 'Female' which should yield no results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient4" })
    public void testPatient_gender_noResults() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "gender", "Female");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
    }
    
    /**
     * Tests a query for a Patient with phone = '8016626839' which should yield correct results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient4" })
    public void testPatient_phone() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "phone", "8016626839");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        assertEquals(((Patient)resources.get(0)).getTelecom().get(1).getValue().getValue(),"8016626839");
    }
    
    /**
     * Tests a query for a Patient with phone = '8016626839' OR '3039999999' which should yield correct results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient4" })
    public void testPatient_phone_multivalue() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "phone", "8016626839,3039999999");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        assertEquals(((Patient)resources.get(0)).getTelecom().get(1).getValue().getValue(),"8016626839");
    }
    
    /**
     * Tests a query for a Patient with phone = '80166268396' which should yield no results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient4" })
    public void testPatient_phone_noResults() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "phone", "80166268396");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
    }
    
    /**
     * Tests a query for a Patient with link = 'Patient/pat2' which should yield correct results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient1" })
    public void testPatient_link() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "link", "Patient/pat2");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        List<Patient.Link> linkList = ((Patient)resources.get(0)).getLink();
        assertEquals(linkList.get(0).getOther().getReference().getValue(),"Patient/pat2");
    }
    
    /**
     * Tests a query for a Patient with organization = 'Organization/1' which should yield correct results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient2" })
    public void testPatient_organization() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "organization", "Organization/1");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        assertEquals(((Patient)resources.get(0)).getManagingOrganization().getReference().getValue(),"Organization/1");
    }
    
    /**
     * Tests a query for a Patient with organization = 'Organization/1' OR 'Organization/2' which should yield correct results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient2" })
    public void testPatient_organization_mulitvalue() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "organization", "Organization/1,Organization/2");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        assertEquals(((Patient)resources.get(0)).getManagingOrganization().getReference().getValue(),"Organization/1");
    }
    
    /**
     * Tests a query for a Patient with organization:Organization = '1' which should yield correct results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient2" })
    public void testPatient_organization1() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "organization:Organization", "1");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        assertEquals(((Patient)resources.get(0)).getManagingOrganization().getReference().getValue(),"Organization/1");
    }
    
    /**
     * Tests a query for a Patient with careprovider = 'Practitioner/practID' which should yield correct results
     * @throws Exception
     * TODO fix test
     */
//    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient3" })
//    public void testPatient_careProvider() throws Exception {
//        List<Resource> resources = runQueryTest(Patient.class, persistence, "careprovider", "Practitioner/practID");
//        assertNotNull(resources);
//        assertTrue(resources.size() != 0);
//        assertEquals(((Patient)resources.get(0)).getCareProvider().get(0).getReference().getValue(),"Practitioner/practID");
//    }
    
    /**
     * Tests a query for Patients with birthdate = '1944-08-11' which should yield correct results
     * @throws Exception
     */
    
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = {"testCreatePatient4"})
    public void testPatient_birthdate() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "birthdate", "eq1944-08-11");
        assertTrue(resources.size() > 0);
        assertEquals(((Patient)resources.get(0)).getBirthDate().getValue(),"1944-08-11");
    }
    
    /**
     * Tests a query for Patients with birthdate = '1944-08-10' which should yield no results
     * @throws Exception
     */
    
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = {"testCreatePatient4"})
    public void testPatient_birthdate_noResults() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "birthdate", "eq1944-08-11");
        assertTrue(resources.size() > 0);
        assertEquals(((Patient)resources.get(0)).getBirthDate().getValue(),"1944-08-11");
    }
    
    /**
     * Tests a query for Patients with deathdate = '2015-02-14T13:42:00+10:00' which should yield correct results
     * @throws Exception
     * TODO fix test
     */
//    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient5" })
//    public void testPatient_deathDate() throws Exception {
//        List<Resource> resources = runQueryTest(Patient.class, persistence, "deathdate", "eq2015-02-14T13:42:00+10:00");
//        assertNotNull(resources);
//        assertTrue(resources.size() != 0);
//        assertTrue(((Patient)resources.get(0)).getDeceasedDateTime().getValue().equals("2015-02-14T13:42:00+10:00"));
//    }
    
    /**
     * Tests a query for Patients with deathdate = '2020-02-14T13:42:00+10:00' which should yield no results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient5" })
    public void testPatient_deathDate_noResults() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "deathdate", "eq2020-02-14T13:42:00+10:00");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
    }
    
    /**
     * Tests a query for a Patient with family name = 'Doe' using :exact modifier which should yield correct results
     * @throws Exception
     * TODO fix test
     */
//    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient1" })
//    public void testPatient_exactModifier() throws Exception {
//        List<Resource> resources = runQueryTest(Patient.class, persistence, "family:exact", "Doe");
//        assertNotNull(resources);
//        assertTrue(resources.size() != 0);
//        List<HumanName> hnList = ((Patient)resources.get(0)).getName();
//        assertEquals(hnList.get(0).getFamily().get(0).getValue(),"Doe");
//    }
    
    /**
     * Tests a query for Patients with address field missing using :missing 
     * which should return at least the patient-glossy-example (testCreatePatient3)
     * @throws Exception
     */
    @Test(groups = {"jdbc-normalized"}, dependsOnMethods = { "testCreatePatient1", "testCreatePatient2", "testCreatePatient3" })
    public void testPatientQuery_missingModifier() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "address:missing", "true");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        for (Resource resource : resources) {
            assertTrue(((Patient)resource).getAddress().size() == 0);
        }
    }
    
    /**
     * Tests a query for Patients with address field missing using :text modifier which is not supported and should result in an exception
     * @throws Exception
     */
    @Test(groups = {"jdbc-normalized"}, dependsOnMethods = { "testCreatePatient1", "testCreatePatient2", "testCreatePatient3" },
                    expectedExceptions = {FHIRSearchException.class})
    public void testPatientQuery_textModifier() throws Exception {
        runQueryTest(Patient.class, persistence, "address:text", "false");
        
    }
    
    /**
     * Tests a query for Patients with address field missing using an invalid modifier which is not supported and should result in an exception
     * @throws Exception
     */
    @Test(groups = {"jdbc-normalized"}, dependsOnMethods = { "testCreatePatient1", "testCreatePatient2", "testCreatePatient3" },
                    expectedExceptions = {FHIRSearchException.class})
    public void testPatientQuery_invalid_Modifier() throws Exception {
        runQueryTest(Patient.class, persistence, "address:invalid-modifier", "false");
        
    }
    
    /**
     * Tests a query for Patients with address field containing partial matches using :contains modifier which should yield correct results
     * @throws Exception
     */

    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient1", "testCreatePatient2", "testCreatePatient3" })
    public void testPatient_containsModifier() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "address-city:contains", "Amsterdam");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        List<Address> addrList = ((Patient)resources.get(0)).getAddress();
        assertTrue(addrList.get(0).getCity().getValue().equalsIgnoreCase("Amsterdam"));
    }
    
    /**
     * Tests a query for Patients with deathdate = 'eq2015-02-14T13:42:00+10:00' which should yield correct results
     * @throws Exception
     * TODO fix test
     */
//    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient5" })
//    public void testPatient_deathDateEQ() throws Exception {
//        List<Resource> resources = runQueryTest(Patient.class, persistence, "deathdate", "eq2015-02-14T13:42:00+10:00");
//        assertNotNull(resources);
//        assertTrue(resources.size() != 0);
//        assertTrue(((Patient)resources.get(0)).getDeceasedDateTime().getValue().equals("2015-02-14T13:42:00+10:00"));
//    }
    
    /**
     * Tests a query for Patients with deathdate = 'ne2015-02-14T13:42:00+10:00' which should yield no results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient5" })
    public void testPatient_deathDateNE() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "deathdate", "ne2015-02-14T13:42:00+10:00");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
    }
    
    /**
     * Tests a query for Patients with deathdate = 'gt2015-02-14T13:42:00+10:00' which should yield no results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient5" })
    public void testPatient_deathDateGT() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "deathdate", "gt2015-02-14T13:42:00+10:00");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
    }
    
    /**
     * Tests a query for Patients with deathdate = 'lt2015-02-14T13:42:00+10:00' which should yield no results
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient5" })
    public void testPatient_deathDateLT() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "deathdate", "lt2015-02-14T13:42:00+10:00");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
    }
    
    // TODO fix tests
//    /**
//     * Tests a query for Patients with deathdate = 'ge2015-02-14T13:42:00+10:00' which should yield correct results
//     * @throws Exception
//     */
//    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient5" })
//    public void testPatient_deathDateGE() throws Exception {
//        List<Resource> resources = runQueryTest(Patient.class, persistence, "deathdate", "ge2015-02-14T13:42:00+10:00");
//        assertNotNull(resources);
//        assertTrue(resources.size() != 0);
//        assertTrue(((Patient)resources.get(0)).getDeceasedDateTime().getValue().equals("2015-02-14T13:42:00+10:00"));
//    }
//    
//    /**
//     * Tests a query for Patients with deathdate = 'le2015-02-14T13:42:00+10:00' which should yield correct results
//     * @throws Exception
//     */
//    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient5" })
//    public void testPatient_deathDateLE() throws Exception {
//        List<Resource> resources = runQueryTest(Patient.class, persistence, "deathdate", "le2015-02-14T13:42:00+10:00");
//        assertNotNull(resources);
//        assertTrue(resources.size() != 0);
//        assertTrue(((Patient)resources.get(0)).getDeceasedDateTime().getValue().equals("2015-02-14T13:42:00+10:00"));
//    }
//    
//    /**
//     * Tests a query for Patients with active = 'true' which should yield correct results
//     * @throws Exception
//     */
//
//    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient5" })
//    public void testPatient_active() throws Exception {
//        List<Resource> resources = runQueryTest(Patient.class, persistence, "active", "true");
//        assertNotNull(resources);
//        assertTrue(resources.size() != 0);
//        assertTrue(((Patient)resources.get(0)).getActive().isValue().toString().equals("true"));
//    }
//    
//    /**
//     * Tests a query for Patients with address-use = 'home' which should yield correct results
//     * @throws Exception
//     */
//
//    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient1" })
//    public void testPatient_addressUse() throws Exception {
//        List<Resource> resources = runQueryTest(Patient.class, persistence, "address-use", "home");
//        assertNotNull(resources);
//        assertTrue(resources.size() != 0);
//        assertTrue(((Patient)resources.get(0)).getAddress().get(0).getUse().getValue().value().equals("home"));
//    }
//    
//    /**
//     * Tests a query for Patients with name = 'Clanton,Mussenden' which should yield correct results
//     * @throws Exception
//     */
//    
//    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient4" })
//    public void nameOrTest() throws Exception {
//        List<Resource> resources = runQueryTest(Patient.class, persistence, "name", "Clanton,Mussenden");
//        assertNotNull(resources);
//        assertTrue(resources.size() != 0);
//        assertTrue(((Patient)resources.get(0)).getActive().isValue().toString().equals("true"));
//    }
    
    /**
     * Tests a query for Patients with name = 'Clanton1' which should yield no results
     * @throws Exception
     */

    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient4" })
    public void nameTestNoResult() throws Exception {
        List<Resource> resources = runQueryTest(Patient.class, persistence, "name", "Clanton1");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
    }
    
    /*
     * Pagination Testcases
     */
    
    /**
     * Tests a query with a resource type but without any query parameters. This should yield correct results using pagination
     * 
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient1", "testCreatePatient2", "testCreatePatient3", "testCreatePatient4", "testCreatePatient5" })
    public void testPatientPagination_001() throws Exception {
        
        Class<? extends Resource> resourceType = Patient.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms, null);
        context.setPageNumber(1);
        List<Resource> resources = persistence.search(getPersistenceContextForSearch(context), Patient.class);
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        long count = context.getTotalCount();
        int pageSize = context.getPageSize();
        int lastPgNum = context.getLastPageNumber();
        assertEquals(context.getLastPageNumber(), (int) ((count + pageSize - 1) / pageSize));
        assertTrue((count > 10) ? (lastPgNum > 1) : (lastPgNum == 1));
    }
    
    /**
     * Tests a query for Patients with deathdate = 'le2015-02-14T13:42:00+10:00' which should yield correct results using pagination
     * @throws Exception
     * TODO fix test
     */
//    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient5" })
//    public void testPatientPagination_002() throws Exception {
//        
//        String parmName = "deathdate";
//        String parmValue = "le2015-02-14T13:42:00+10:00";
//        Class<? extends Resource> resourceType = Patient.class;
//        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
//        
//        queryParms.put(parmName, Collections.singletonList(parmValue));
//        FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms, null);
//        context.setPageNumber(1);
//        List<Resource> resources = persistence.search(getPersistenceContextForSearch(context), Patient.class);
//        assertNotNull(resources);
//        assertTrue(resources.size() != 0);
//        assertTrue(((Patient)resources.get(0)).getDeceasedDateTime().getValue().equals("2015-02-14T13:42:00+10:00"));
//        long count = context.getTotalCount();
//        int pageSize = context.getPageSize();
//        int lastPgNum = context.getLastPageNumber();
//        assertEquals(context.getLastPageNumber(), (int) ((count + pageSize - 1) / pageSize));
//        assertTrue((count > 10) ? (lastPgNum > 1) : (lastPgNum == 1));
//    }
    
    /**
     * Tests a query for Patients with deathdate = 'gt2015-02-14T13:42:00+10:00' which should yield no results using pagination
     * @throws Exception
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient5" })
    public void testPatientPagination_003() throws Exception {
        
        String parmName = "deathdate";
        String parmValue = "gt2015-02-14T13:42:00+10:00";
        Class<? extends Resource> resourceType = Patient.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        
        queryParms.put(parmName, Collections.singletonList(parmValue));
        FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms, null);
        context.setPageNumber(1);
        List<Resource> resources = persistence.search(getPersistenceContextForSearch(context), Patient.class);
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
        long count = context.getTotalCount();
//        int lastPgNum = context.getLastPageNumber();
        assertTrue((count == 0)/* && (lastPgNum == Integer.MAX_VALUE)*/);
    }
    
    /*
     * History Pagination Testcases
     */
    
    /**
     * Tests retrieval of update history of a Patient. This should yield correct results using pagination
     * 
     */
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient1" })
    public void testPatientHistoryPgn_001() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_page", Collections.singletonList("1"));
        queryParms.put("_since", Collections.singletonList("2015-06-10T21:32:59.076Z"));
        FHIRHistoryContext context = FHIRPersistenceUtil.parseHistoryParameters(queryParms);
        
        List<Resource> resources = persistence.history(getPersistenceContextForHistory(context), Patient.class, savedPatient.getId().getValue());
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        long count = context.getTotalCount();
        int pageSize = context.getPageSize();
        int lastPgNum = context.getLastPageNumber();
        assertEquals(context.getLastPageNumber(), (int) ((count + pageSize - 1) / pageSize));
        assertTrue((count > 10) ? (lastPgNum > 1) : (lastPgNum == 1));
    }
    
    /*
     * Performance testcases for Pagination - create 1000 records of the same resource type and retrieve count
     * NOTE: This test is not being run at this time because when running against a Derby DB, it takes over 40 minutes to
     * complete. This would be an unacceptable increase to project build time.  When running against a DB2 database,
     * this method completes in about 34 seconds.
     * @throws Exception
     */
    //@Test(groups = { "cloudant-broken", "jpa", "jdbc", "jdbc-normalized" })
    public void testPerformanceTestPatient() throws Exception {
           Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");

           for(int i=0; i<1000; i++) {
               persistence.create(getDefaultPersistenceContext(), patient);
           }
        
           Class<? extends Resource> resourceType = Patient.class;
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        FHIRSearchContext context = SearchUtil.parseQueryParameters(resourceType, queryParms, null);
        context.setPageNumber(1);
        List<Resource> resources = persistence.search(getPersistenceContextForSearch(context), Patient.class);
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        long count = context.getTotalCount();
        int pageSize = context.getPageSize();
        int lastPgNum = context.getLastPageNumber();
        assertEquals(context.getLastPageNumber(), (int) ((count + pageSize - 1) / pageSize));
        assertTrue(count >= 1000);
        assertTrue((count > 10) ? (lastPgNum > 1) : (lastPgNum == 1));
    }
    
    /**
     * 
     * Compartment search testcases
     * 
     */
    
    /**
     * Tests a query with a resource type but without any query parameters. This should yield all the resources created so far.
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient1" })
    public void testSingleInclusionCriteriaPatient_noparams() throws Exception {
        List<Resource> resources = runQueryTest("Patient", "pat2", Patient.class, persistence, null, null);
        assertTrue(resources.size() != 0);
    }    
    
    /**
     * Tests a query for a Patient with family name = 'Doe' which should yield correct results
     * @throws Exception
     * TODO fix test
     */
//    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient1" })
//    public void testSingleInclusionCriteriaPatient_family() throws Exception {
//        List<Resource> resources = runQueryTest("Patient", "pat2", Patient.class, persistence, "family", "Doe");
//        assertTrue(resources.size() != 0);
//        List<HumanName> hnList = ((Patient)resources.get(0)).getName();
//        assertEquals(hnList.get(0).getFamily().get(0).getValue(),"Doe");
//    }    
    
    /**
     * Tests a query for a Patient with family name = 'Non-existent' which should yield no results
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient1" })
    public void testSingleInclusionCriteriaPatient_family_noResults() throws Exception {
        List<Resource> resources = runQueryTest("Patient", "pat2", Patient.class, persistence, "family", "Non-existent");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
    }
    
    /**
     * Tests a query for a Patient with address-city = 'Amsterdam' which should yield correct results
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient1" })
    public void testSingleInclusionCriteriaPatient_city() throws Exception {
        List<Resource> resources = runQueryTest("Patient", "pat2", Patient.class, persistence, "address-city", "Amsterdam");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        List<Address> addrList = ((Patient)resources.get(0)).getAddress();
        assertEquals(addrList.get(0).getCity().getValue(),"Amsterdam");
    }
    
    /**
     * Tests a query for a Patient with address-city = 'Amsterdam' which should yield correct results
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient1" })
    public void testSingleInclusionCriteriaPatient_adressByCity() throws Exception {
        List<Resource> resources = runQueryTest("Patient", "pat2", Patient.class, persistence, "address", "Amsterdam");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        List<Address> addrList = ((Patient)resources.get(0)).getAddress();
        assertEquals(addrList.get(0).getCity().getValue(),"Amsterdam");
    }
    
    /**
     * Tests a query for a Patient with address-city = 'Non-existent' which should yield no results
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient1" })
    public void testSingleInclusionCriteriaPatient_city_noResults() throws Exception {
        List<Resource> resources = runQueryTest("Patient", "pat2", Patient.class, persistence, "address-city", "Non-existent");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
    }
    
    /**
     * Tests a query for a Patient with address-country = 'NLD' which should yield correct results
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient1" })
    public void testSingleInclusionCriteriaPatient_country() throws Exception {
        List<Resource> resources = runQueryTest("Patient", "pat2", Patient.class, persistence, "address-country", "NLD");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        List<Address> addrList = ((Patient)resources.get(0)).getAddress();
        assertEquals(addrList.get(0).getCountry().getValue(),"NLD");
    }
    
    
    /**
     * Tests a query for a Patient with address-country = 'NLD' which should yield correct results
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient1" })
    public void testSingleInclusionCriteriaPatientAdressByCountry() throws Exception {
        List<Resource> resources = runQueryTest("Patient", "pat2", Patient.class, persistence, "address", "NLD");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        List<Address> addrList = ((Patient)resources.get(0)).getAddress();
        assertEquals(addrList.get(0).getCountry().getValue(),"NLD");
    }
    
    /**
     * Tests a query for a Patient with address-country = 'Non-existent' which should yield no results
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient1" })
    public void testSingleInclusionCriteriaPatient_country_noResults() throws Exception {
        List<Resource> resources = runQueryTest("Patient", "pat2", Patient.class, persistence, "address-country", "Non-existent");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
    }
    
    /**
     * Tests a query for a Patient with link = 'Patient/pat2' which should yield correct results
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient1" })
    public void testSingleInclusionCriteriaPatient_link() throws Exception {
        List<Resource> resources = runQueryTest("Patient", "pat2", Patient.class, persistence, "link", "Patient/pat2");
        assertNotNull(resources);
        assertTrue(resources.size() != 0);
        List<Patient.Link> linkList = ((Patient)resources.get(0)).getLink();
        assertEquals(linkList.get(0).getOther().getReference().getValue(),"Patient/pat2");
    }
    
    /**
     * Tests a query for a Patient with family name = 'Doe' using :exact modifier which should yield correct results
     * @throws Exception
     * TODO fix test
     */
//    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient1" })
//    public void testSingleInclusionCriteriaPatient_exactModifier() throws Exception {
//        List<Resource> resources = runQueryTest("Patient", "pat2", Patient.class, persistence, "family:exact", "Doe");
//        assertNotNull(resources);
//        assertTrue(resources.size() != 0);
//        List<HumanName> hnList = ((Patient)resources.get(0)).getName();
//        assertEquals(hnList.get(0).getFamily().get(0).getValue(),"Doe");
//    }
    
    /**
     * Tests a query for Patients with address-use = 'home' which should yield correct results
     * @throws Exception
     * TODO fix test
     */
//    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient1" })
//    public void testSingleInclusionCriteriaPatient_addressUse() throws Exception {
//        List<Resource> resources = runQueryTest("Patient", "pat2", Patient.class, persistence, "address-use", "home");
//        assertNotNull(resources);
//        assertTrue(resources.size() != 0);
//        assertTrue(((Patient)resources.get(0)).getAddress().get(0).getUse().getValue().value().equals("home"));
//    }
    
    /**
     * Tests a query with a resource type but without any query parameters. This should yield all the resources created so far.
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient3" })
    public void test1InclusionCriteriaPatient_noparams_PractCompmt() throws Exception {
        List<Resource> resources = runQueryTest("Practitioner", "practID", Patient.class, persistence, null, null);
        assertTrue(resources.size() != 0);
    }    
    
    /**
     * Tests a query for a Patient with family name = 'Levin' which should yield correct results
     * @throws Exception
     * TODO fix test
     */
//    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient3" })
//    public void test1InclusionCriteriaPatient_family_PractCompmt() throws Exception {
//        List<Resource> resources = runQueryTest("Practitioner", "practID", Patient.class, persistence, "family", "Levin");
//        assertTrue(resources.size() != 0);
//        List<HumanName> hnList = ((Patient)resources.get(0)).getName();
//        assertEquals(hnList.get(0).getFamily().get(0).getValue(),"Levin");
//    }   
    
    /**
     * Tests a query for a Patient with family name = 'Non-existent' which should yield no results
     * @throws Exception
     */
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient1" })
    public void test1InclusionCriteriaPatient_family_noResults_PractCompmt() throws Exception {
        List<Resource> resources = runQueryTest("Practitioner", "Organization/2", Patient.class, persistence, "family", "Non-existent");
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
    }
    
    /**
     * Tests a query for Patients with birthdate = '1932-09-24' which should yield correct results
     * @throws Exception
     */
    
    @Test(groups = { "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = {"testCreatePatient3"})
    public void testPatient_birthdate_PractCompmt() throws Exception {
        List<Resource> resources = runQueryTest("Practitioner", "practID", Patient.class, persistence, "birthdate", "eq1932-09-24");
        assertTrue(resources.size() > 0);
        assertEquals(((Patient)resources.get(0)).getBirthDate().getValue(),"1932-09-24");
    }
}
