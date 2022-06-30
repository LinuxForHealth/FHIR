/*
 * (C) Copyright IBM Corp. 2016, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.search.test;

import static com.ibm.fhir.model.test.TestUtil.isResourceInResponse;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.testng.annotations.Test;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.fhir.model.resource.Basic;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;

public abstract class AbstractWholeSystemSearchTest extends AbstractPLSearchTest {
    public static final boolean DEBUG = false;

    protected final String TAG_SYSTEM = "http://ibm.com/fhir/tag";
    protected final String TAG = UUID.randomUUID().toString();
    protected final String TAG2 = UUID.randomUUID().toString();
    protected final String TAG3 = UUID.randomUUID().toString();
    protected final String SECURITY_SYSTEM = "http://ibm.com/fhir/security";
    protected final String SECURITY = UUID.randomUUID().toString();
    // v3-ActReason is a "polyhierarchy" codesystem; we need https://github.com/LinuxForHealth/FHIR/issues/3448 for that one
    //protected final String CODE_SYSTEM_V3_ACT_REASON = "http://terminology.hl7.org/CodeSystem/v3-ActReason";
    //protected final String TAG4 = "HSYSADMIN";
    protected final String CODE_SYSTEM_V3_PARTICIPATION_MODE = "http://terminology.hl7.org/CodeSystem/v3-ParticipationMode";
    protected final String TAG4 = "ONLINEWRIT";
    protected final String TAG4TEXT = "someSearchText";
    protected final String PROFILE = "http://ibm.com/fhir/profile/" + UUID.randomUUID().toString();
    protected final String AUTHOR = "Practitioner/" + UUID.randomUUID().toString();
    protected final String SOURCE = "http://ibm.com/fhir/source";

    @Override
    protected void setTenant() throws Exception {
        // nothing to do since we only use built-in search parameter
    }

    @Override
    public Basic getBasicResource() throws Exception {
        Basic basic = TestUtil.getMinimalResource(Basic.class);

        Coding tag =
                Coding.builder()
                        .system(Uri.of(TAG_SYSTEM))
                        .code(Code.of(TAG)).build();
        Coding tag2 =
                Coding.builder()
                        .system(Uri.of(TAG_SYSTEM))
                        .code(Code.of(TAG2)).build();
        Coding tag3 =
                Coding.builder()
                        .system(Uri.of(CODE_SYSTEM_V3_PARTICIPATION_MODE))
                        .code(Code.of(TAG4))
                        .display(com.ibm.fhir.model.type.String.of(TAG4TEXT))
                        .build();

        Coding security =
                Coding.builder()
                        .system(Uri.of(SECURITY_SYSTEM))
                        .code(Code.of(SECURITY)).build();

        Meta meta =
                Meta.builder()
                        .tag(tag)
                        .tag(tag)
                        .tag(tag2)
                        .tag(tag3)
                        .security(security)
                        .profile(Canonical.of(PROFILE))
                        .source(Uri.of(SOURCE))
                        .build();

        return basic.toBuilder()
                .meta(meta)
                .author(Reference.builder().reference(com.ibm.fhir.model.type.String.of(AUTHOR)).build())
                .build();
    }

    @Test
    public void testSearchAllUsingId() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, "_id", savedResource.getId());
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }

    @Test
    public void testSearchAllUsingLastUpdated() throws Exception {
        List<Resource> resources =
                runQueryTest(Resource.class, "_lastUpdated",
                        savedResource.getMeta().getLastUpdated().getValue().toString());
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }

    @Test
    public void testSearchAllUsingIdAndLastUpdated() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        List<String> savedId = Collections.singletonList(savedResource.getId());

        String dateTime = savedResource.getMeta().getLastUpdated().getValue().toString();
        List<String> savedLastUpdated = Collections.singletonList(dateTime);
        queryParms.put("_id", savedId);
        queryParms.put("_lastUpdated", savedLastUpdated);

        if (DEBUG) {
            generateOutput(savedResource);
        }

        List<Resource> resources = runQueryTest(Resource.class, queryParms);
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }

    @Test
    public void testSearchAllUsingIdAndLastUpdatedWithSort() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        List<String> savedId = Collections.singletonList(savedResource.getId());

        String dateTime = savedResource.getMeta().getLastUpdated().getValue().toString();
        List<String> savedLastUpdated = Collections.singletonList(dateTime);
        queryParms.put("_id", savedId);
        queryParms.put("_lastUpdated", savedLastUpdated);

        // Sort id and then lastUpdated
        queryParms.put("_sort", Collections.singletonList("_id,-_lastUpdated"));

        if (DEBUG) {
            generateOutput(savedResource);
        }

        List<Resource> resources = runQueryTest(Resource.class, queryParms);
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }

    /*
     * generates the output into a resource.
     */
    public static void generateOutput(Resource resource) {
        try (StringWriter writer = new StringWriter();) {
            FHIRGenerator.generator(Format.JSON, true).generate(resource, System.out);
            System.out.println(writer.toString());
        } catch (FHIRGeneratorException e) {
            fail("unable to generate the fhir resource to JSON", e);
        } catch (IOException e1) {
            e1.printStackTrace();
            fail("unable to generate the fhir resource to JSON (io problem) ", e1);
        }
    }

    @Test
    public void testSearchAllUsingInvalidIdAndLastUpdated() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        List<String> savedId = Collections.singletonList("a-totally-stinking-phony-id");
        List<String> savedLastUpdated =
                Collections.singletonList(savedResource.getMeta().getLastUpdated().getValue().toString());
        queryParms.put("_id", savedId);
        queryParms.put("_lastUpdated", savedLastUpdated);

        List<Resource> resources = runQueryTest(Resource.class, queryParms);
        assertNotNull(resources);
        assertEquals(resources.size(), 0, "Number of resources returned");
    }

    @Test
    public void testSearchAllUsingMultipleIds() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        List<String> multipleIds = Collections.singletonList(savedResource.getId() + ",a-totally-stinking-phony-id");
        queryParms.put("_id", multipleIds);

        List<Resource> resources = runQueryTest(Resource.class, queryParms);
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource not found in the response");
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
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }

    @Test
    public void testSearchAllUsingSecurity() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, "_security", SECURITY_SYSTEM + "|" + SECURITY);
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }

    @Test
    public void testSearchAllUsingSecuritySystemOnly() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, "_security", SECURITY_SYSTEM + "|");
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }

    @Test
    public void testSearchAllUsingSource() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, "_source", SOURCE);
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }

    @Test
    public void testSearchAllUsingTagModifierText() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, "_tag:text", "someSearch");
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }

    @Test
    public void testSearchAllUsingTagModifierIn() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, "_tag:in", "http://terminology.hl7.org/ValueSet/v3-ParticipationMode");
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }

    @Test
    public void testSearchAllUsingTagModifierNotIn() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_tag:not-in", Collections.singletonList("http://hl7.org/fhir/ValueSet/common-tags"));
        queryParms.put("_type", Collections.singletonList("Basic"));

        if (DEBUG) {
            generateOutput(savedResource);
        }

        List<Resource> resources = runQueryTest(Resource.class, queryParms);
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }


    @Test
    public void testSearchAllUsingTagModifierAbove() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, "_tag:above", CODE_SYSTEM_V3_PARTICIPATION_MODE + "|EMAILWRIT");
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }

    @Test
    public void testSearchAllUsingTagModifierBelow() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, "_tag:below", CODE_SYSTEM_V3_PARTICIPATION_MODE + "|WRITTEN");
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }

    @Test
    public void testSearchAllUsingTagSystemOnly() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, "_tag", CODE_SYSTEM_V3_PARTICIPATION_MODE + "|");
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }

    @Test
    public void testSearchAllUsingProfile() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, "_profile", PROFILE);
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }

    @Test
    public void testSearchAllUsingProfileAndTag() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_tag", Collections.singletonList(TAG_SYSTEM + "|" + TAG));
        queryParms.put("_profile", Collections.singletonList(PROFILE));

        if (DEBUG) {
            generateOutput(savedResource);
        }

        List<Resource> resources = runQueryTest(Resource.class, queryParms);
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }

    @Test
    public void testSearchAllUsingSecurityAndTag() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_tag", Collections.singletonList(TAG_SYSTEM + "|" + TAG));
        queryParms.put("_security", Collections.singletonList(SECURITY_SYSTEM + "|" + SECURITY));

        if (DEBUG) {
            generateOutput(savedResource);
        }

        List<Resource> resources = runQueryTest(Resource.class, queryParms);
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }

    @Test
    public void testSearchAllUsingProfileAndSecurityAndTag() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_profile", Collections.singletonList(PROFILE));
        queryParms.put("_tag", Collections.singletonList(TAG_SYSTEM + "|" + TAG));
        queryParms.put("_security", Collections.singletonList(SECURITY_SYSTEM + "|" + SECURITY));

        if (DEBUG) {
            generateOutput(savedResource);
        }

        List<Resource> resources = runQueryTest(Resource.class, queryParms);
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }

    @Test
    public void testSearchAllUsingProfileAndSecurityAndSource() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_profile", Collections.singletonList(PROFILE));
        queryParms.put("_source", Collections.singletonList(SOURCE));
        queryParms.put("_security", Collections.singletonList(SECURITY_SYSTEM + "|" + SECURITY));

        if (DEBUG) {
            generateOutput(savedResource);
        }

        List<Resource> resources = runQueryTest(Resource.class, queryParms);
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }

    @Test
    public void testSearchAllUsingElements() throws Exception {
        // This might fail if there are more than 1000 resources in the test db
        List<Resource> resources = runQueryTest(Resource.class, "_elements", "meta", 1000);
        assertNotNull(resources);
        assertTrue(resources.size() > 0);
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }

    @Test
    public void testSearchAllUsing2TagsAndNoExistingTag() throws Exception {
        List<Resource> resources =
                runQueryTest(Resource.class,
                        "_tag", TAG_SYSTEM + "|" + "tag88," + TAG + "," + TAG2);
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }

    @Test
    public void testSearchAllUsing2Tags() throws Exception {
        List<Resource> resources =
                runQueryTest(Resource.class,
                        "_tag", TAG_SYSTEM + "|" + TAG + "," + TAG2);
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }

    @Test
    public void testSearchAllUsing2FullTags() throws Exception {
        List<Resource> resources =
                runQueryTest(Resource.class,
                        "_tag", TAG_SYSTEM + "|" + TAG + "," + TAG_SYSTEM + "|" + TAG2);
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }

    @Test
    public void testSearchAllUsingOneSimpleTag() throws Exception {
        List<Resource> resources =
                runQueryTest(Resource.class,
                        "_tag", TAG);
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }

    @Test
    public void testSearchAllUsingIdAndLastUpdatedAndAnyTagOrProfile() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        List<String> savedId = Collections.singletonList(savedResource.getId());

        String dateTime = savedResource.getMeta().getLastUpdated().getValue().toString();
        List<String> savedLastUpdated = Collections.singletonList(dateTime);
        List<String> falseString = Collections.singletonList("false");
        queryParms.put("_id", savedId);
        queryParms.put("_lastUpdated", savedLastUpdated);
        queryParms.put("_tag:missing", falseString);
        queryParms.put("_profile:missing", falseString);

        if (DEBUG) {
            generateOutput(savedResource);
        }

        List<Resource> resources = runQueryTest(Resource.class, queryParms);
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }

    @Test
    public void testSearchAllUsingTagNot_Results() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_id", Collections.singletonList(savedResource.getId()));
        queryParms.put("_tag:not", Collections.singletonList(TAG_SYSTEM + "|" + TAG3));
        List<Resource> resources = runQueryTest(Resource.class, queryParms);
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }

    @Test
    public void testSearchAllUsingTagNot_NoResults() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_id", Collections.singletonList(savedResource.getId()));
        queryParms.put("_tag:not", Collections.singletonList(TAG_SYSTEM + "|" + TAG));
        List<Resource> resources = runQueryTest(Resource.class, queryParms);
        assertNotNull(resources);
        assertEquals(resources.size(), 0, "Number of resources returned");
    }

    @Test
    public void testSearchAllUsingType() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, "_type", "Basic,EvidenceVariable,ServiceRequest");
        assertNotNull(resources);
        assertTrue(resources.size() > 0, "At least one resource returned");
        assertTrue(resources.size() == resources.stream().distinct().count(), "No repeats");
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource is in the response");
        for (Resource resource : resources) {
            if (!(resource instanceof Basic) && !(resource instanceof Basic) && !(resource instanceof Basic)) {
                fail("query retrieved unexpected resource of type " + resource.getClass());
            }
        }
    }

    @Test
    public void testSearchAllUsingTypeAndProfile() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_type", Collections.singletonList("Basic,Patient,Observation"));
        queryParms.put("_profile", Collections.singletonList(PROFILE));
        List<Resource> resources = runQueryTest(Resource.class, queryParms);
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }

    @Test
    public void testSearchAllUsingTypeAndSecurity() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_type", Collections.singletonList("Basic,Patient,Observation"));
        queryParms.put("_security", Collections.singletonList(SECURITY_SYSTEM + "|" + SECURITY));
        List<Resource> resources = runQueryTest(Resource.class, queryParms);
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }

    @Test
    public void testSearchAllUsingTypeAndSource() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_type", Collections.singletonList("Basic,Patient,Observation"));
        queryParms.put("_source", Collections.singletonList(SOURCE));
        List<Resource> resources = runQueryTest(Resource.class, queryParms);
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }

    @Test
    public void testSearchAllUsingTypeAndProfileAndSourceAndTag() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_type", Collections.singletonList("Basic,Patient,Observation"));
        queryParms.put("_profile", Collections.singletonList(PROFILE));
        queryParms.put("_source", Collections.singletonList(SOURCE));
        queryParms.put("_tag", Collections.singletonList(TAG_SYSTEM + "|" + TAG));
        List<Resource> resources = runQueryTest(Resource.class, queryParms);
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }

    @Test
    public void testSearchAllUsingTypeAndNonGlobalSearchParm() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_type", Collections.singletonList("Basic,DetectedIssue,DocumentReference"));
        queryParms.put("author", Collections.singletonList(AUTHOR));
        List<Resource> resources = runQueryTest(Resource.class, queryParms);
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }

    @Test
    public void testSearchAllUsingTypeAndNonGlobalSearchParmAndProfile() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_type", Collections.singletonList("Basic,DetectedIssue,DocumentReference"));
        queryParms.put("author", Collections.singletonList(AUTHOR));
        queryParms.put("_profile", Collections.singletonList(PROFILE));
        List<Resource> resources = runQueryTest(Resource.class, queryParms);
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }

    @Test
    public void testSearchAllUsingTypeAndNonGlobalSearchParmAndSecurity() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_type", Collections.singletonList("Basic,DetectedIssue,DocumentReference"));
        queryParms.put("author", Collections.singletonList(AUTHOR));
        queryParms.put("_security", Collections.singletonList(SECURITY_SYSTEM + "|" + SECURITY));
        List<Resource> resources = runQueryTest(Resource.class, queryParms);
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }

    @Test
    public void testSearchAllUsingTypeAndNonGlobalSearchParmAndSource() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        queryParms.put("_type", Collections.singletonList("Basic,DetectedIssue,DocumentReference"));
        queryParms.put("author", Collections.singletonList(AUTHOR));
        queryParms.put("_source", Collections.singletonList(SOURCE));
        List<Resource> resources = runQueryTest(Resource.class, queryParms);
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }

    @Test
    public void testSearchAllUsingTypeNoResults() throws Exception {
        List<Resource> resources = runQueryTest(Resource.class, "_type", "EvidenceVariable,ServiceRequest");
        assertNotNull(resources);
        assertEquals(resources.size(), 0, "Number of resources returned");
    }

}
