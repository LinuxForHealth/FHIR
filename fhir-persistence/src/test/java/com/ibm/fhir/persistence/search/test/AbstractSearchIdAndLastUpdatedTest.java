/*
 * (C) Copyright IBM Corp. 2018, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.search.test;

import static com.ibm.fhir.model.test.TestUtil.isResourceInResponse;
import static com.ibm.fhir.model.type.String.string;
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
import java.util.TimeZone;

import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.fhir.model.resource.Basic;
import com.ibm.fhir.model.resource.Device;
import com.ibm.fhir.model.resource.Encounter;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Practitioner;
import com.ibm.fhir.model.resource.RelatedPerson;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.persistence.MultiResourceResult;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * <a href="https://hl7.org/fhir/search.html#date">FHIR Specification: Search
 * - _id and _lastUpdated</a> Tests
 */
public abstract class AbstractSearchIdAndLastUpdatedTest extends AbstractPLSearchTest {
    private Boolean DEBUG = Boolean.FALSE;

    @Override
    protected Basic getBasicResource() throws Exception {
        return TestUtil.readExampleResource("json/ibm/basic/BasicDate.json");
    }

    private Reference buildReference(Resource resource) {
        assertNotNull(resource);
        assertNotNull(resource.getId());

        String resourceTypeName = resource.getClass().getSimpleName();
        return Reference.builder()
                .reference(string(resourceTypeName + "/" + resource.getId()))
                .build();
    }

    @Override
    protected void setTenant() throws Exception {
        FHIRRequestContext.get().setTenantId("default");

        // this might deserve its own method, but just use setTenant for now
        // since its called before creating any resources
        TimeZone.setDefault(TimeZone.getTimeZone("GMT-4:00"));
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

    @Test
    public void testSearchWholeSystemUsingIdAndLastUpdatedResourceWithSortGreaterThanEquals() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();
        List<String> savedId = Collections.singletonList(savedResource.getId());

        String dateTime = savedResource.getMeta().getLastUpdated().getValue().toString();
        List<String> savedLastUpdated = Collections.singletonList("ge" + dateTime);
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

    @Test
    public void testSearchWholeSystemUsingLastUpdatedResourceWithSortGreaterThanEquals() throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>();

        String dateTime = savedResource.getMeta().getLastUpdated().getValue().toString();
        List<String> savedLastUpdated = Collections.singletonList("ge" + dateTime);
        queryParms.put("_lastUpdated", savedLastUpdated);

        // Sort id and then lastUpdated
        queryParms.put("_sort", Collections.singletonList("-_lastUpdated"));

        if (DEBUG) {
            generateOutput(savedResource);
        }

        List<Resource> resources = runQueryTest(Basic.class, queryParms);
        assertNotNull(resources);
        assertEquals(resources.size(), 1, "Number of resources returned");
        assertTrue(isResourceInResponse(savedResource, resources), "Expected resource not found in the response");
    }

    @Test()
    public void testPatientCompartmentForBulkData() throws Exception {

        Patient savedPatient;
        Device savedDevice;
        Encounter savedEncounter;
        Practitioner savedPractitioner;
        RelatedPerson savedRelatedPerson;
        Observation savedObservation;

        Observation.Builder observationBuilder =
                ((Observation) TestUtil.getMinimalResource(Observation.class)).toBuilder();

        Patient patient = TestUtil.getMinimalResource(Patient.class);
        savedPatient = persistence.create(getDefaultPersistenceContext(), patient).getResource();
        observationBuilder.subject(buildReference(savedPatient));
        observationBuilder.performer(buildReference(savedPatient));

        Device device = TestUtil.getMinimalResource(Device.class);
        savedDevice = persistence.create(getDefaultPersistenceContext(), device).getResource();
        observationBuilder.device(buildReference(savedDevice));

        Encounter encounter = TestUtil.getMinimalResource(Encounter.class);
        savedEncounter = persistence.create(getDefaultPersistenceContext(), encounter).getResource();
        observationBuilder.encounter(buildReference(savedEncounter));

        Practitioner practitioner = TestUtil.getMinimalResource(Practitioner.class);
        savedPractitioner = persistence.create(getDefaultPersistenceContext(), practitioner).getResource();
        observationBuilder.performer(buildReference(savedPractitioner));

        RelatedPerson relatedPerson = TestUtil.getMinimalResource(RelatedPerson.class);
        savedRelatedPerson = persistence.create(getDefaultPersistenceContext(), relatedPerson).getResource();
        observationBuilder.performer(buildReference(savedRelatedPerson));

        savedObservation = persistence.create(getDefaultPersistenceContext(), observationBuilder.build()).getResource();
        assertNotNull(savedObservation);
        assertNotNull(savedObservation.getId());
        assertNotNull(savedObservation.getMeta());
        assertNotNull(savedObservation.getMeta().getVersionId().getValue());
        assertEquals("1", savedObservation.getMeta().getVersionId().getValue());

        Map<String, List<String>> queryParms = new HashMap<String, List<String>>(2);
        String parmName = "_lastUpdated";
        queryParms.put(parmName, Collections.singletonList("ge2000"));
        queryParms.put("_sort", Collections.singletonList("_id"));

        FHIRSearchContext searchContext =
                SearchUtil.parseCompartmentQueryParameters("Patient", savedPatient.getId(), Observation.class, queryParms);
        FHIRPersistenceContext persistenceContext = getPersistenceContextForSearch(searchContext);
        MultiResourceResult<Resource> result = persistence.search(persistenceContext, Observation.class);
        assertNotNull(result.getResource());
        assertTrue(result.getResource().size() > 0);
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
            fail("unable to generate the fhir resource to JSON (io problem) ", e1);
        }
    }
}