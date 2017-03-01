/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.test.common;

import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.coding;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.uri;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.List;

import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.model.Coding;
import com.ibm.watsonhealth.fhir.model.ObjectFactory;
import com.ibm.watsonhealth.fhir.model.Patient;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.model.Uri;

public abstract class AbstractSearchAllTest extends AbstractPersistenceTest {
    protected ObjectFactory objFactory = new ObjectFactory();
    protected String patientId;
    protected String lastUpdated;

    @Test(groups = { "cloudant", "jpa", "jdbc" })
    public void testCreatePatient() throws Exception {
        Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");
        
        Coding tag = coding("http://ibm.com/watsonhealth/fhir/tag", "tag");
        Coding security = coding("http://ibm.com/watsonhealth/fhir/security", "security");
        Uri profile = uri("http://ibm.com/watsonhealth/fhir/profile/Profile");
        
        patient.setMeta(objFactory.createMeta().withTag(tag).withSecurity(security).withProfile(profile));
        
        persistence.create(getDefaultPersistenceContext(), patient);
        assertNotNull(patient);
        assertNotNull(patient.getId());
        assertNotNull(patient.getId().getValue());
        assertNotNull(patient.getMeta());
        assertNotNull(patient.getMeta().getVersionId().getValue());
        assertEquals("1", patient.getMeta().getVersionId().getValue());
        
        patientId = patient.getId().getValue();
        lastUpdated = patient.getMeta().getLastUpdated().getValue().toString();
    }
    
    @Test(groups = { "cloudant", "jpa", "jdbc" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingId() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, persistence, "_id", patientId);
        assertNotNull(resources);
        assertTrue(resources.size() > 0);
    }
    
    @Test(groups = { "cloudant", "jpa", "jdbc" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingLastUpdated() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, persistence, "_lastUpdated", lastUpdated);
        assertNotNull(resources);
        assertTrue(resources.size() > 0);
    }
    
    @Test(groups = { "cloudant", "jpa", "jdbc" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingTag() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, persistence, "_tag", "http://ibm.com/watsonhealth/fhir/tag|tag");
        assertNotNull(resources);
        assertTrue(resources.size() > 0);
    }
    
    @Test(groups = { "cloudant", "jpa", "jdbc" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingSecurity() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, persistence, "_security", "http://ibm.com/watsonhealth/fhir/security|security");
        assertNotNull(resources);
        assertTrue(resources.size() > 0);
    }
    
    @Test(groups = { "cloudant", "jpa", "jdbc" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingProfile() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, persistence, "_profile", "http://ibm.com/watsonhealth/fhir/profile/Profile");
        assertNotNull(resources);
        assertTrue(resources.size() > 0);
    }
}
