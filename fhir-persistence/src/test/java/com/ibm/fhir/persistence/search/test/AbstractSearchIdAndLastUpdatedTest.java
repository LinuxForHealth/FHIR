/*
 * (C) Copyright IBM Corp. 2018,2020
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
import java.time.ZoneId;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.fhir.model.resource.Basic;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.test.TestUtil;

/**
 * <a href="https://hl7.org/fhir/search.html#date">FHIR Specification: Search
 * - _id and _lastUpdated</a> Tests
 */
public abstract class AbstractSearchIdAndLastUpdatedTest extends AbstractPLSearchTest {
    private Boolean DEBUG = Boolean.TRUE;

    protected Basic getBasicResource() throws Exception {
        return TestUtil.readExampleResource("json/ibm/basic/BasicDate.json");
    }

    protected void setTenant() throws Exception {
        FHIRRequestContext.get().setTenantId("default");

        // this might deserve its own method, but just use setTenant for now 
        // since its called before creating any resources
        TimeZone.setDefault(TimeZone.getTimeZone("GMT-4:00"));
        System.out.println(ZoneId.systemDefault());
    }

    @Test
    public void testSearchWholeSystemUsingIdAndLastUpdated() throws Exception {
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
    public void testSearchWholeSystemUsingIdAndLastUpdatedWithSort() throws Exception {
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

    @Test
    public void testSearchWholeSystemUsingIdAndLastUpdatedResource() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        List<String> savedId = Collections.singletonList(savedResource.getId());

        String dateTime = savedResource.getMeta().getLastUpdated().getValue().toString();
        List<String> savedLastUpdated = Collections.singletonList(dateTime);
        queryParms.put("_id", savedId);
        queryParms.put("_lastUpdated", savedLastUpdated);

        if (DEBUG) {
            generateOutput(savedResource);
        }

        List<Resource> resources = runQueryTest(Basic.class, queryParms);
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }

    @Test
    public void testSearchWholeSystemUsingIdAndLastUpdatedResourceWithSort() throws Exception {
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

        List<Resource> resources = runQueryTest(Basic.class, queryParms);
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
}
