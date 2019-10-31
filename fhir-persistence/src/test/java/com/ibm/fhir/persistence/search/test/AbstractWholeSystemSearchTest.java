/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.search.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static com.ibm.fhir.model.test.TestUtil.findResourceInResponse;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.Basic;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Uri;

public abstract class AbstractWholeSystemSearchTest extends AbstractPLSearchTest {
    protected final String TAG_SYSTEM = "http://ibm.com/fhir/tag";
    protected final String TAG = UUID.randomUUID().toString();
    protected final String TAG2 = UUID.randomUUID().toString();
    protected final String SECURITY_SYSTEM = "http://ibm.com/fhir/security";
    protected final String SECURITY = UUID.randomUUID().toString();
    protected final String PROFILE = "http://ibm.com/fhir/profile/" + UUID.randomUUID().toString();
    
    @Override
    protected void setTenant() throws Exception {
        // nothing to do since we only use built-in search parameter
    }

    @Override
    public Basic getBasicResource() throws Exception {
        Basic basic = TestUtil.readExampleResource("json/ibm/minimal/Basic-1.json");
        
        Coding tag = Coding.builder()
                .system(Uri.of(TAG_SYSTEM))
                .code(Code.of(TAG)).build();
        Coding tag2 = Coding.builder()
                .system(Uri.of(TAG_SYSTEM))
                .code(Code.of(TAG2)).build();
        Coding security = Coding.builder()
                .system(Uri.of(SECURITY_SYSTEM))
                .code(Code.of(SECURITY)).build();

        Meta meta = Meta.builder()
                        .tag(tag)
                        .tag(tag)
                        .tag(tag2)
                        .security(security)
                        .profile(Canonical.of(PROFILE))
                        .build();
        
        return basic.toBuilder().meta(meta).build();
    }
    
    @Test
    public void testSearchAllUsingId() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, "_id", savedResource.getId().getValue());
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(findResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }
    
    @Test
    public void testSearchAllUsingLastUpdated() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, "_lastUpdated", savedResource.getMeta().getLastUpdated().getValue().toString());
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(findResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }
    
    @Test
    public void testSearchAllUsingIdAndLastUpdated() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        List<String> savedId = Collections.singletonList(savedResource.getId().getValue());
        List<String> savedLastUpdated = Collections.singletonList(savedResource.getMeta().getLastUpdated().getValue().toString());
        queryParms.put("_id", savedId);
        queryParms.put("_lastUpdated", savedLastUpdated);

        List<Resource> resources = runQueryTest(Resource.class, queryParms);
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(findResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }
    
    @Test
    public void testSearchAllUsingInvalidIdAndLastUpdated() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        List<String> savedId = Collections.singletonList("a-totally-stinking-phony-id");
        List<String> savedLastUpdated = Collections.singletonList(savedResource.getMeta().getLastUpdated().getValue().toString());
        queryParms.put("_id", savedId);
        queryParms.put("_lastUpdated", savedLastUpdated);

        List<Resource> resources = runQueryTest(Resource.class, queryParms);
        assertNotNull(resources);
        assertEquals(0, resources.size(), "Number of resources returned");
    }
    
    @Test
    public void testSearchAllUsingMultipleIds() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        List<String> multipleIds = Collections.singletonList(savedResource.getId().getValue() + ",a-totally-stinking-phony-id");
        queryParms.put("_id", multipleIds);

        List<Resource> resources = runQueryTest(Resource.class, queryParms);
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(findResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }
    
    @Test
    public void testSearchAllUsingMultipleInvalidIds() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        List<String> multipleIds = Collections.singletonList("a-totally-stinking-phony-id,a-second-phony-id");
        queryParms.put("_id", multipleIds);

        List<Resource> resources = runQueryTest(Resource.class, queryParms);
        assertNotNull(resources);
        assertEquals(0, resources.size(), "Number of resources returned");
    }
    
    @Test
    public void testSearchAllUsingTag() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, "_tag", TAG_SYSTEM + "|" + TAG);
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(findResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }
    
    @Test
    public void testSearchAllUsingSecurity() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, "_security", SECURITY_SYSTEM + "|" + SECURITY);
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(findResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }
    
    @Test
    public void testSearchAllUsingProfile() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, "_profile", PROFILE);
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(findResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }
    
    @Test
    public void testSearchAllUsingElements() throws Exception {
        // This might fail if there are more than 1000 resources in the test db
        List<Resource> resources = runQueryTest(Resource.class, "_elements", "meta", 1000);
        assertNotNull(resources);
        assertTrue(resources.size() > 0);
        assertTrue(findResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }
    
    
    @Test
    public void testSearchAllUsing2TagsAndNoExistingTag() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, 
                "_tag", TAG_SYSTEM + "|" + "tag88," + TAG + "," + TAG2);
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(findResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }
    
    @Test
    public void testSearchAllUsing2Tags() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, 
                "_tag", TAG_SYSTEM + "|" + TAG + "," + TAG2);
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(findResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }
    
    @Test
    public void testSearchAllUsing2FullTags() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, 
                "_tag", TAG_SYSTEM + "|" + TAG + "," + TAG_SYSTEM + "|" + TAG2);
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(findResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }

    @Test
    public void testSearchAllUsingOneSimpleTag() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, 
                "_tag", TAG);
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(findResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }
}
