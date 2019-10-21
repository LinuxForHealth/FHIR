/*
 * (C) Copyright IBM Corp. 2016,2019
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

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.Basic;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Uri;

public abstract class AbstractWholeSystemSearchTest extends AbstractPersistenceTest {
    
    Resource savedResource;
    String savedResourceId;
    String lastUpdated;

    @BeforeClass
    public void createResources() throws Exception {
        
        Basic basic = TestUtil.readExampleResource("json/ibm/minimal/Basic-1.json");
        
        Coding tag = Coding.builder()
                .system(Uri.of("http://ibm.com/fhir/tag"))
                .code(Code.of("tag")).build();
        Coding security = Coding.builder()
                .system(Uri.of("http://ibm.com/fhir/security"))
                .code(Code.of("security")).build();

        Meta meta = Meta.builder()
                        .tag(tag)
                        .security(security)
                        .profile(Canonical.of("http://ibm.com/fhir/profile/Profile"))
                        .build();
        
        basic = basic.toBuilder().meta(meta).build();
        
        savedResource = persistence.create(getDefaultPersistenceContext(), basic).getResource();
        assertNotNull(savedResource);
        assertNotNull(savedResource.getId());
        assertNotNull(savedResource.getId().getValue());
        assertNotNull(savedResource.getMeta());
        assertNotNull(savedResource.getMeta().getVersionId().getValue());
        assertEquals("1", savedResource.getMeta().getVersionId().getValue());
        
        savedResourceId = savedResource.getId().getValue();
        lastUpdated = savedResource.getMeta().getLastUpdated().getValue().toString();
    }
    
    @Test
    public void testSearchAllUsingId() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, "_id", savedResource.getId().getValue());
        assertNotNull(resources);
        assertTrue(resources.size() > 0);
    }
    
    @Test(singleThreaded = true)
    public void testSearchAllUsingLastUpdated() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, "_lastUpdated", savedResource.getMeta().getLastUpdated().getValue().toString());
        assertNotNull(resources);
        assertTrue(resources.size() > 0);
    }
    
    @Test(singleThreaded = true)
    public void testSearchAllUsingIdAndLastUpdated() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<>();
        queryParms.put("_id", Collections.singletonList(savedResourceId));
        queryParms.put("_lastUpdated", Collections.singletonList(lastUpdated));
        List<Resource> resources = runQueryTest(Resource.class, queryParms);
        assertNotNull(resources);
        assertTrue(resources.size() > 0);
    }
    
    @Test
    public void testSearchAllUsingInvalidIdAndLastUpdated() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<>();
        queryParms.put("_id", Collections.singletonList("a-totally-stinking-phony-id"));
        queryParms.put("_lastUpdated", Collections.singletonList(lastUpdated));
        List<Resource> resources = runQueryTest(Resource.class, queryParms);
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
    }
    
    @Test
    public void testSearchAllUsingMultipleIds() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<>();
        queryParms.put("_id", Collections.singletonList(savedResourceId + ",a-totally-stinking-phony-id"));
        List<Resource> resources = runQueryTest(Resource.class, queryParms);
        assertNotNull(resources);
        assertTrue(resources.size() > 0);
    }
    
    @Test
    public void testSearchAllUsingMultipleInvalidIds() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<>();
        queryParms.put("_id", Collections.singletonList("a-totally-stinking-phony-id,a-second-phony-id"));
        List<Resource> resources = runQueryTest(Resource.class, queryParms);
        assertNotNull(resources);
        assertTrue(resources.size() == 0);
    }
    
    @Test
    public void testSearchAllUsingTag() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, "_tag", "http://ibm.com/fhir/tag|tag");
        assertNotNull(resources);
        assertTrue(resources.size() > 0);
    }
    
    @Test
    public void testSearchAllUsingSecurity() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, "_security", "http://ibm.com/fhir/security|security");
        assertNotNull(resources);
        assertTrue(resources.size() > 0);
    }
    
    @Test
    public void testSearchAllUsingProfile() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, "_profile", "http://ibm.com/fhir/profile/Profile");
        assertNotNull(resources);
        assertTrue(resources.size() > 0);
    }
    
    @Test
    public void testSearchAllUsingElements() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, "_elements", "meta");
        assertNotNull(resources);
        assertTrue(resources.size() > 0);
    }
}
