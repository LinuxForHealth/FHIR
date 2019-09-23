/*
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.test.common;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Uri;

public abstract class AbstractSearchAllTest extends AbstractPersistenceTest {
    protected String patientId;
    protected String lastUpdated;

    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" })
    public void testCreatePatient() throws Exception {
        Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");
        
        Coding tag = Coding.builder()
        		.system(Uri.of("http://ibm.com/fhir/tag"))
        		.code(Code.of("tag")).build();
        Coding security = Coding.builder()
        		.system(Uri.of("http://ibm.com/fhir/security"))
        		.code(Code.of("security")).build();

        Meta meta = patient.getMeta();
        Meta.Builder mb = meta == null ? Meta.builder() : meta.toBuilder();
        mb.tag(tag);
        mb.security(security);
        mb.profile(Canonical.of("http://ibm.com/fhir/profile/Profile"));
        
        patient = patient.toBuilder().meta(mb.build()).build();
        
        Resource resource = persistence.create(getDefaultPersistenceContext(), patient);
        assertNotNull(resource);
        assertNotNull(resource.getId());
        assertNotNull(resource.getId().getValue());
        assertNotNull(resource.getMeta());
        assertNotNull(resource.getMeta().getVersionId().getValue());
        assertEquals("1", resource.getMeta().getVersionId().getValue());
        
        patientId = resource.getId().getValue();
        lastUpdated = resource.getMeta().getLastUpdated().getValue().toString();
    }
    
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingId() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, persistence, "_id", patientId);
        assertNotNull(resources);
        assertTrue(resources.size() > 0);
    }
    
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingLastUpdated() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, persistence, "_lastUpdated", lastUpdated);
        assertNotNull(resources);
        assertTrue(resources.size() > 0);
    }
    
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingIdAndLastUpdated() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_id", Collections.singletonList(patientId));
        queryParms.put("_lastUpdated", Collections.singletonList(lastUpdated));
        List<Resource> resources = runQueryTest(Resource.class, persistence, queryParms);
        assertNotNull(resources);
        assertTrue(resources.size() > 0);
    }
    
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingInvalidIdAndLastUpdated() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_id", Collections.singletonList("a-totally-stinking-phony-id"));
        queryParms.put("_lastUpdated", Collections.singletonList(lastUpdated));
        List<Resource> resources = runQueryTest(Resource.class, persistence, queryParms);
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
    }
    
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingMultipleIds() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_id", Collections.singletonList(patientId + ",a-totally-stinking-phony-id"));
        List<Resource> resources = runQueryTest(Resource.class, persistence, queryParms);
        assertNotNull(resources);
        assertTrue(resources.size() > 0);
    }
    
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingMultipleInvalidIds() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_id", Collections.singletonList("a-totally-stinking-phony-id,a-second-phony-id"));
        List<Resource> resources = runQueryTest(Resource.class, persistence, queryParms);
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
    }
    
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingTag() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, persistence, "_tag", "http://ibm.com/fhir/tag|tag");
        assertNotNull(resources);
        assertTrue(resources.size() > 0);
    }
    
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingSecurity() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, persistence, "_security", "http://ibm.com/fhir/security|security");
        assertNotNull(resources);
        assertTrue(resources.size() > 0);
    }
    
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingProfile() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, persistence, "_profile", "http://ibm.com/fhir/profile/Profile");
        assertNotNull(resources);
        assertTrue(resources.size() > 0);
    }
    
    @Test(groups = { "cloudant", "jpa", "jdbc", "jdbc-normalized" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingElements() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, persistence, "_elements", "meta");
        assertNotNull(resources);
        assertTrue(resources.size() > 0);
    }
}
