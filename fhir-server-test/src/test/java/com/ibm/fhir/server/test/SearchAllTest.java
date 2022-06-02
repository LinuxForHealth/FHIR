/*
 * (C) Copyright IBM Corp. 2017, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import static com.ibm.fhir.model.test.TestUtil.isResourceInResponse;
import static com.ibm.fhir.model.type.Code.code;
import static com.ibm.fhir.model.type.Uri.uri;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import com.ibm.fhir.client.FHIRParameters;
import com.ibm.fhir.client.FHIRRequestHeader;
import com.ibm.fhir.client.FHIRResponse;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Basic;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.Bundle.Link;
import com.ibm.fhir.model.resource.Condition;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Uri;

public class SearchAllTest extends FHIRServerTestBase {

    private static final boolean DEBUG_SEARCH = false;

    private String patientId, patientId2, observationId;
    private Instant lastUpdated;
    private Patient patientForDuplicationTest = null;
    private String strUniqueTag = UUID.randomUUID().toString();
    // By default, the tests runs on the default data store of the default tenant, can be changed to test
    // other data store or other tenant.
    private final String tenantName = "default";
    private final String dataStoreId = "default";

    private final FHIRRequestHeader headerTenant =
            new FHIRRequestHeader("X-FHIR-TENANT-ID", tenantName);
    private final FHIRRequestHeader headerDataStore =
            new FHIRRequestHeader("X-FHIR-DSID", dataStoreId);

    @Test(groups = { "server-search-all" })
    public void testCreatePatient() throws Exception {
        WebTarget target = getWebTarget();

        // Build a new Patient with 2 tags and one duplicated tag and then call the 'create' API.
        Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");

        Coding security = Coding.builder().system(uri("http://ibm.com/fhir/security")).code(code("security")).build();
        Coding tag = Coding.builder().system(uri("http://ibm.com/fhir/tag")).code(code("tag")).build();
        Coding tag2 = Coding.builder().system(uri("http://ibm.com/fhir/tag")).code(code("tag2")).build();

        patient =
                patient.toBuilder()
                        .meta(Meta.builder()
                                .security(security)
                                .tag(tag)
                                .tag(tag)
                                .tag(tag2)
                                .profile(Canonical.of("http://ibm.com/fhir/profile/Profile"))
                                .source(Uri.of("http://ibm.com/fhir/source/Source"))
                                .build())
                        .build();
        printOutResource(DEBUG_SEARCH, patient);

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient").request()
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        patientId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response  = target.path("Patient/" + patientId).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        patientForDuplicationTest = response.readEntity(Patient.class);
        TestUtil.assertResourceEquals(patient, patientForDuplicationTest);

        lastUpdated = patientForDuplicationTest.getMeta().getLastUpdated();
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatient" })
    public void testCreateObservation() throws Exception {
        WebTarget target = getWebTarget();

        Observation observation =
                TestUtil.buildPatientObservation(patientId, "Observation1.json");

        Entity<Observation> entity =
                Entity.entity(observation, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response =
                target.path("Observation").request()
                        .header("X-FHIR-TENANT-ID", tenantName)
                        .header("X-FHIR-DSID", dataStoreId)
                        .post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        observationId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new observation and verify it.
        response  = target.path("Observation/" + observationId).request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Observation createdObservation = response.readEntity(Observation.class);
        TestUtil.assertResourceEquals(observation, createdObservation);
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingId() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_id", patientId);
        FHIRResponse response = client.searchAll(parameters, false, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingLastUpdated() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_lastUpdated", lastUpdated.getValue().toString());

        FHIRResponse response = client.searchAll(parameters, false, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingLastUpdatedGeLe() throws Exception {
        // ge2018-09-01T00:00:00Z&
        // le2018-09-01T00:00:00Z&
        FHIRParameters parameters = new FHIRParameters();
        ZoneId zoneId = ZoneId.from(lastUpdated.getValue());
        ZonedDateTime beforeOneHourZdt = ZonedDateTime.ofInstant(lastUpdated.getValue().toInstant().minus(1, ChronoUnit.HOURS),zoneId);
        ZonedDateTime afterOneHourZdt = ZonedDateTime.ofInstant(lastUpdated.getValue().toInstant().plus(1, ChronoUnit.HOURS),zoneId);
        parameters.searchParam("_lastUpdated", "ge" + beforeOneHourZdt.toString());
        parameters.searchParam("_lastUpdated", "le" + afterOneHourZdt.toString());

        FHIRResponse response = client.searchAll(parameters, false, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingLastUpdatedMultipleGeLe() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        ZoneId zoneId = ZoneId.from(lastUpdated.getValue());
        ZonedDateTime beforeOneHourZdt = ZonedDateTime.ofInstant(lastUpdated.getValue().toInstant().minus(1, ChronoUnit.HOURS),zoneId);
        ZonedDateTime afterOneHourZdt = ZonedDateTime.ofInstant(lastUpdated.getValue().toInstant().plus(1, ChronoUnit.HOURS),zoneId);
        parameters.searchParam("_lastUpdated", "ge" + beforeOneHourZdt.toString() + ",ge2018");
        parameters.searchParam("_lastUpdated", "le" + afterOneHourZdt.toString() + ",le2029");

        FHIRResponse response = client.searchAll(parameters, false, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingLastUpdatedMultipleGeLeOneInvalidEach() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        ZoneId zoneId = ZoneId.from(lastUpdated.getValue());
        ZonedDateTime beforeOneHourZdt = ZonedDateTime.ofInstant(lastUpdated.getValue().toInstant().minus(1, ChronoUnit.HOURS),zoneId);
        ZonedDateTime afterOneHourZdt = ZonedDateTime.ofInstant(lastUpdated.getValue().toInstant().plus(1, ChronoUnit.HOURS),zoneId);
        parameters.searchParam("_lastUpdated", "ge" + beforeOneHourZdt.toString() + ",ge2029");
        parameters.searchParam("_lastUpdated", "le" + afterOneHourZdt.toString() + ",le2018");

        FHIRResponse response = client.searchAll(parameters, false, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingTag() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_tag", "tag");
        FHIRResponse response = client.searchAll(parameters, false, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);

        /*
         * "expression" : "Resource.meta.tag", <br/> "xpath" : "f:Resource/f:meta/f:tag",
         */
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingTagSystemOnly() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_tag", "http://ibm.com/fhir/tag|");
        FHIRResponse response = client.searchAll(parameters, false, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);

        assertNotNull(bundle);
        printOutResource(DEBUG_SEARCH, bundle);

        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingSecurity() throws Exception {
        // <expression value="Resource.meta.security"/>
        // <xpath value="f:Resource/f:meta/f:security"/>

        FHIRParameters parameters = new FHIRParameters();

        // Original - "http://ibm.com/fhir/security|security"
        parameters.searchParam("_security", "security");
        FHIRResponse response = client.searchAll(parameters, false, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);

        assertNotNull(bundle);
        printOutResource(DEBUG_SEARCH, bundle);

        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingSecuritySystemOnly() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_security", "http://ibm.com/fhir/security|");
        FHIRResponse response = client.searchAll(parameters, false, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);

        assertNotNull(bundle);
        printOutResource(DEBUG_SEARCH, bundle);

        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingSource() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_source", "http://ibm.com/fhir/source/Source");
        FHIRResponse response = client.searchAll(parameters, false, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);

        assertNotNull(bundle);
        printOutResource(DEBUG_SEARCH, bundle);

        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingProfileAndSecurityAndSource() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_profile", "http://ibm.com/fhir/profile/Profile");
        parameters.searchParam("_security", "security");
        parameters.searchParam("_source", "http://ibm.com/fhir/source/Source");
        FHIRResponse response = client.searchAll(parameters, false, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);

        assertNotNull(bundle);
        printOutResource(DEBUG_SEARCH, bundle);

        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingProfile() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_profile", "http://ibm.com/fhir/profile/Profile");
        FHIRResponse response = client.searchAll(parameters, false, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreateObservation" })
    public void testSearchAllWithTagMissing_Results() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_tag:missing", "true");
        FHIRResponse response = client.searchAll(parameters, false, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreateObservation" })
    public void testSearchAllWithTagMissing_NoResults() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_id", observationId);
        parameters.searchParam("_tag:missing", "false");
        FHIRResponse response = client.searchAll(parameters, false, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().isEmpty());
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllWithTagNotMissing_Results() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_id", patientId);
        parameters.searchParam("_tag:missing", "false");
        FHIRResponse response = client.searchAll(parameters, false, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertEquals(bundle.getEntry().size(), 1);
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllWithTagNotMissing_NoResults() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_id", patientId);
        parameters.searchParam("_tag:missing", "true");
        FHIRResponse response = client.searchAll(parameters, false, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().isEmpty());
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreateObservation" })
    public void testSearchAllChainWithTagNotMissing_Results() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_type", "Observation");
        parameters.searchParam("_id", observationId);
        parameters.searchParam("subject:Patient._tag:missing", "false");
        FHIRResponse response = client.searchAll(parameters, false, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertEquals(bundle.getEntry().size(), 1);
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreateObservation" })
    public void testSearchAllChainWithTagMissing_NoResults() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_type", "Observation");
        parameters.searchParam("_id", observationId);
        parameters.searchParam("subject:Patient._tag:missing", "true");
        FHIRResponse response = client.searchAll(parameters, false, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().isEmpty());
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreateObservation" })
    public void testSearchAllWithTagNot_Results() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_tag:not", "tag3");
        FHIRResponse response = client.searchAll(parameters, false, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreateObservation" })
    public void testSearchAllWithTagNot_NoResults() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_id", patientId);
        parameters.searchParam("_tag:not", "tag2");
        FHIRResponse response = client.searchAll(parameters, false, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().isEmpty());
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsing2TagsAndNoExistingTag() throws Exception {
        int firstRunNumber;
        FHIRParameters parameters = new FHIRParameters();
        // tag88 doesn't exist, this case is created according to a reported test failure.
        parameters.searchParam("_tag", "http://ibm.com/fhir/tag|tag88,tag2,tag");
        parameters.searchParam("_count", "1000");
        parameters.searchParam("_page", "1");
        FHIRResponse response = client.searchAll(parameters, false, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);

        firstRunNumber = bundle.getEntry().size();
        assertTrue(firstRunNumber >= 1);
        // Create one more patient with 2 tags: "tag" and "tag2".
        testCreatePatient();
        response = client.searchAll(parameters, false, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        bundle = response.getResource(Bundle.class);
        // The second run should only have one more new record found.
        // Because we limit the page size to 1000, so we only do this check when firstRunNumber < 1000.
        if (firstRunNumber < 1000) {
            assertTrue(bundle.getEntry().size() == firstRunNumber + 1);
            List<Resource> lstRes = new ArrayList<Resource>();
            for (Bundle.Entry entry : bundle.getEntry()) {
                lstRes.add(entry.getResource());
            }
            assertTrue(isResourceInResponse(patientForDuplicationTest, lstRes));
        } else {
            // Just in case there are more than 1000 matches, then simply verify that there is
            // no duplicated resource in the search results, Just need to do the verification for the second run.
            HashSet<String> resourceIdSet = new HashSet<String>();
            for (Entry entry : bundle.getEntry()) {
                resourceIdSet.add(entry.getResource().getClass().getSimpleName()
                        + ":" + entry.getResource().getId());
            }
            assertTrue(bundle.getEntry().size() == resourceIdSet.size());
        }
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsing2Tags() throws Exception {
        int firstRunNumber;
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_tag", "http://ibm.com/fhir/tag|tag2,tag");
        parameters.searchParam("_count", "1001");
        parameters.searchParam("_page", "1");
        FHIRResponse response = client.searchAll(parameters, false, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        // Check that count is set to the maxPageSize (1000) for the tenant
        String selfLink = getSelfLink(bundle);
        assertTrue(selfLink.contains("_count=1000"));

        firstRunNumber = bundle.getEntry().size();
        assertTrue(firstRunNumber >= 1);
        // create one more patient with 2 tags: "tag" and "tag2".
        testCreatePatient();
        response = client.searchAll(parameters, false, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        bundle = response.getResource(Bundle.class);
        // The second run should only have one more new record found.
        // Because we limit the page size to 1000, so we only do this check when firstRunNumber < 1000.
        if (firstRunNumber < 1000) {
            assertTrue(bundle.getEntry().size() == firstRunNumber + 1);
            List<Resource> lstRes = new ArrayList<Resource>();
            for (Bundle.Entry entry : bundle.getEntry()) {
                lstRes.add(entry.getResource());
            }
            assertTrue(isResourceInResponse(patientForDuplicationTest, lstRes));
        } else {
            // Just in case there are more than 1000 matches, then simply verify that there is
            // no duplicated resource in the search results, Just need to do the verification for the second run.
            HashSet<String> resourceIdSet = new HashSet<String>();
            for (Entry entry : bundle.getEntry()) {
                resourceIdSet.add(entry.getResource().getClass().getSimpleName()
                        + ":" + entry.getResource().getId());
            }
            assertTrue(bundle.getEntry().size() == resourceIdSet.size());
        }
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsing2FullTags() throws Exception {
        int firstRunNumber;
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_tag", "http://ibm.com/fhir/tag|tag2,http://ibm.com/fhir/tag|tag");
        parameters.searchParam("_count", "1000");
        parameters.searchParam("_page", "1");
        FHIRResponse response = client.searchAll(parameters, false, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);

        firstRunNumber = bundle.getEntry().size();
        assertTrue(firstRunNumber >= 1);
        // Create one more patient with 2 tags: "tag" and "tag2".
        testCreatePatient();
        response = client.searchAll(parameters, false, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        bundle = response.getResource(Bundle.class);
        // The second run should only have one more new record found.
        // Because we limit the page size to 1000, so we only do this check when firstRunNumber < 1000.
        if (firstRunNumber < 1000) {
            assertTrue(bundle.getEntry().size() == firstRunNumber + 1);
            List<Resource> lstRes = new ArrayList<Resource>();
            for (Bundle.Entry entry : bundle.getEntry()) {
                lstRes.add(entry.getResource());
            }
            assertTrue(isResourceInResponse(patientForDuplicationTest, lstRes));
        } else {
            // Just in case there are more than 1000 matches, then simply verify that there is
            // no duplicated resource in the search results, Just need to do the verification for the second run.
            HashSet<String> resourceIdSet = new HashSet<String>();
            for (Entry entry : bundle.getEntry()) {
                resourceIdSet.add(entry.getResource().getClass().getSimpleName()
                        + ":" + entry.getResource().getId());
            }
            assertTrue(bundle.getEntry().size() == resourceIdSet.size());
        }
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingOneTag() throws Exception {
        int firstRunNumber;
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_tag", "tag");
        parameters.searchParam("_count", "1000");
        parameters.searchParam("_page", "1");
        FHIRResponse response = client.searchAll(parameters, false, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);

        firstRunNumber = bundle.getEntry().size();
        assertTrue(firstRunNumber >= 1);
        // Create one more patient with 2 tags: "tag" and "tag2".
        testCreatePatient();
        response = client.searchAll(parameters, false, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        bundle = response.getResource(Bundle.class);
        // The second run should only have one more new record found.
        // Because we limit the page size to 1000, so we only do this check when firstRunNumber < 1000.
        if (firstRunNumber < 1000) {
            assertTrue(bundle.getEntry().size() == firstRunNumber + 1);
            List<Resource> lstRes = new ArrayList<Resource>();
            for (Bundle.Entry entry : bundle.getEntry()) {
                lstRes.add(entry.getResource());
            }
            assertTrue(isResourceInResponse(patientForDuplicationTest, lstRes));
        } else {
            // Just in case there are more than 1000 matches, then simply verify that there is
            // no duplicated resource in the search results, Just need to do the verification for the second run.
            HashSet<String> resourceIdSet = new HashSet<String>();
            for (Entry entry : bundle.getEntry()) {
                resourceIdSet.add(entry.getResource().getClass().getSimpleName()
                        + ":" + entry.getResource().getId());
            }
            assertTrue(bundle.getEntry().size() == resourceIdSet.size());
        }
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testSearchAllUsingOneTag" })
    public void testSearchAllUsingOneTagForPrevNext() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_count", "1");
        parameters.searchParam("_page", "2");
        parameters.searchParam("_lastUpdated", "ge" + LocalDate.now().getYear() +"-10-04T00:00:00+00:00");
        parameters.searchParam("_lastUpdated", "lt" + (LocalDate.now().getYear() + 1) + "-10-04T00:00:00+00:00");
        FHIRResponse response = client.searchAll(parameters, false, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        List<Bundle.Link> links = bundle.getLink();
        assertFalse(links.isEmpty());
        for (Bundle.Link link : links) {
            assertFalse(link.getUrl().getValue().contains("+"));
        }
    }

    @Test(groups = { "server-search-all" })
    public void testCreatePatientAndObservationWithUniqueTag() throws Exception {
        Coding uniqueTag = Coding.builder().system(uri("http://ibm.com/fhir/tag")).code(code(strUniqueTag)).build();
        WebTarget target = getWebTarget();

        // Create a new Patient with the unique tag.
        Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");
        Coding security = Coding.builder().system(uri("http://ibm.com/fhir/security")).code(code("security")).build();

        patient =
                patient.toBuilder()
                        .meta(Meta.builder()
                                .security(security)
                                .tag(uniqueTag)
                                .profile(Canonical.of("http://ibm.com/fhir/profile/Profile"))
                                .source(Uri.of("http://ibm.com/fhir/source/Source"))
                                .build())
                        .build();

        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient").request()
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the patient's logical id value.
        patientId2 = getLocationLogicalId(response);

        // Create a new observation with the unique tag for the above patient.
        Observation observation =
                TestUtil.buildPatientObservation(patientId2, "Observation1.json");
        observation =
                observation.toBuilder()
                        .meta(Meta.builder()
                                .tag(uniqueTag)
                                .build())
                        .build();

        Entity<Observation> entity2 =
                Entity.entity(observation, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response2 =
                target.path("Observation").request()
                        .header("X-FHIR-TENANT-ID", tenantName)
                        .header("X-FHIR-DSID", dataStoreId)
                        .post(entity2, Response.class);
        assertResponse(response2, Response.Status.CREATED.getStatusCode());

        // Create a Condition with subject points to the created patient.
        Condition condition = buildCondition(patientId2, "Condition.json");
        Entity<Condition> obs = Entity.entity(condition, FHIRMediaType.APPLICATION_FHIR_JSON);
        response = target.path("Condition").request()
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .post(obs, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatientAndObservationWithUniqueTag" })
    public void testSearchAll_UsingUniqueTag() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_tag", strUniqueTag);
        FHIRResponse response = client.searchAll(parameters, true, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 2);
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatientAndObservationWithUniqueTag" })
    public void testSearchAll_UsingUniqueTag_OneType() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_tag", strUniqueTag);
        parameters.searchParam("_type", "Patient");
        FHIRResponse response = client.searchAll(parameters, true, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 1);
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatientAndObservationWithUniqueTag" })
    public void testSearchAll_UsingUniqueTag_TwoTypes() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_tag", strUniqueTag);
        parameters.searchParam("_type", "Patient,Observation");
        parameters.searchParam("_sort", "_lastUpdated");
        FHIRResponse response = client.searchAll(parameters, true, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 2);
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatientAndObservationWithUniqueTag" })
    public void testSearchAll_TwoTypes_ChainedParameter() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("subject:Patient._tag", strUniqueTag);
        parameters.searchParam("_type", "Observation,Condition");
        parameters.searchParam("_sort", "_id");
        FHIRResponse response = client.searchAll(parameters, true, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assert (bundle.getEntry().size() == 2);
        // verify self link in the response bundle
        assertTrue(bundle.getLink().size() == 1);
        String selfLink = getSelfLink(bundle);
        assertTrue(selfLink.contains("subject:Patient._tag"));
        // Check that count is set to defaultPageSize (10) for the tenant
        assertTrue(selfLink.contains("_count=10"));
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatientAndObservationWithUniqueTag" })
    public void testSearchAll_TwoTypes_InvalidChainedParameter() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("subject:PractitionerRole.active", "true");
        parameters.searchParam("_type", "Account,Observation");
        FHIRResponse response = client.searchAll(parameters, true, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.getResponse().readEntity(OperationOutcome.class),
                "Modifier resource type [PractitionerRole] is not allowed for search parameter [subject] of resource type [Observation]");
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatientAndObservationWithUniqueTag" })
    public void testSearchAll_UsingUniqueTag_TwoTypes_Summary() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_tag", strUniqueTag);
        parameters.searchParam("_type", "Patient,Observation");
        parameters.searchParam("_sort", "_lastUpdated");
        parameters.searchParam("_summary", "true");
        FHIRResponse response = client.searchAll(parameters, true, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 2);
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatientAndObservationWithUniqueTag" })
    public void testSearchAll_UsingUniqueTag_TwoTypes_elements() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_tag", strUniqueTag);
        parameters.searchParam("_type", "Patient,Observation");
        parameters.searchParam("_sort", "_lastUpdated");
        parameters.searchParam("_elements", "id");
        FHIRResponse response = client.searchAll(parameters, true, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 2);
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatientAndObservationWithUniqueTag" })
    public void testSearchAll_UsingUniqueTag_TwoTypes_profile() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_tag", strUniqueTag);
        parameters.searchParam("_type", "Patient,Observation");
        parameters.searchParam("_profile", "http://ibm.com/fhir/profile/Profile");
        FHIRResponse response = client.searchAll(parameters, true, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 1);
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatientAndObservationWithUniqueTag" })
    public void testSearchAll_UsingUniqueTag_TwoTypes_security() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_tag", strUniqueTag);
        parameters.searchParam("_type", "Patient,Observation");
        parameters.searchParam("_security", "security");
        FHIRResponse response = client.searchAll(parameters, true, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 1);
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatientAndObservationWithUniqueTag" })
    public void testSearchAll_UsingUniqueTag_TwoTypes_source() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_tag", strUniqueTag);
        parameters.searchParam("_type", "Patient,Observation");
        parameters.searchParam("_source", "http://ibm.com/fhir/source/Source");
        FHIRResponse response = client.searchAll(parameters, true, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 1);
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatientAndObservationWithUniqueTag" })
    public void testSearchAll_UsingUniqueTag_TwoTypes_profile_security_source() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_tag", strUniqueTag);
        parameters.searchParam("_type", "Patient,Observation");
        parameters.searchParam("_profile", "http://ibm.com/fhir/profile/Profile");
        parameters.searchParam("_security", "security");
        parameters.searchParam("_source", "http://ibm.com/fhir/source/Source");
        FHIRResponse response = client.searchAll(parameters, true, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() == 1);
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatientAndObservationWithUniqueTag" })
    public void testSearchAll_TwoTypes_InvalidInclude() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("subject:Practitioner.name", "John");
        parameters.searchParam("_type", "Account,Observation");
        parameters.searchParam("_include", "Observation:subject");
        FHIRResponse response = client.searchAll(parameters, true, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.BAD_REQUEST.getStatusCode());
        assertExceptionOperationOutcome(response.getResponse().readEntity(OperationOutcome.class),
                "system search not supported with _include or _revinclude");
    }

    @Test(groups = { "server-search-all" })
    public void testSearchAll_UrlReflexsivityUsingLastUpdated() throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        String lastUpdated = "ge2000";
        parameters.searchParam("_lastUpdated", lastUpdated);
        parameters.searchParam("_count", "5");
        FHIRResponse response = client.searchAll(parameters, false, headerTenant, headerDataStore);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);

        // Check to see that we have at least a page (_count=5)
        if (bundle.getTotal().getValue() <= 5) {
            // Load 5 Resources so we go over the page size
            for (int i = 0; i < 5; i++) {
                Basic basic = TestUtil.readExampleResource("json/ibm/minimal/Basic-1.json");
                String id = this.createResourceAndReturnTheLogicalId("Basic", basic);
                this.addToResourceRegistry("Basic", id);
            }

            // Reload
            response = client.searchAll(parameters, false, headerTenant, headerDataStore);
            assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
            bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
        }

        assertTrue(bundle.getEntry().size() >= 1);
        List<Link> links = bundle.getLink();

        /*
         * Runs through the links and checks for self and rel.
         * It subsequently connects to the self to verify it's 200.
         */
        boolean validSelf = false;
        boolean validRel = false;
        for (Link link : links) {
            String type = link.getRelation().getValue();
            String uri = link.getUrl().getValue();
            if ("self".equals(type)) {
                verifyReflexsiveUrl(uri, lastUpdated);
                validSelf = true;
            } else if ("next".equals(type)) {
                verifyReflexsiveUrl(uri, lastUpdated);
                validRel = true;
            }
        }

        assertTrue(validSelf);
        assertTrue(validRel);

        /*
         * Runs through the fullUrl of the entries and checks for appropriate values
         */
        for (Entry entry : bundle.getEntry()) {
            Resource resource = entry.getResource();
            // The client always ensures that the baseUrl ends with a '/'
            String expectedBase = client.getWebTarget().getUri() + resource.getClass().getSimpleName();
            assertTrue(entry.getFullUrl().getValue().startsWith(expectedBase),
                    "fullUrl " + entry.getFullUrl().getValue() + " should start with " + expectedBase);
        }
    }

    @Test(groups = { "server-search-all" }, dependsOnMethods = { "testCreatePatient" })
    public void testSearchAllUsingId_tenant1() throws Exception {
        // tenant1 is configured to support search on only a subset of resource types
        FHIRRequestHeader altTenantHeader = new FHIRRequestHeader("X-FHIR-TENANT-ID", "tenant1");
        FHIRRequestHeader altDatastoreHeader = new FHIRRequestHeader("X-FHIR-DSID", "profile");

        FHIRParameters parameters = new FHIRParameters();
        parameters.searchParam("_id", patientId);
        FHIRResponse response = client.searchAll(parameters, false, altTenantHeader, altDatastoreHeader);
        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);

        boolean validSelf = false;
        for (Link link : bundle.getLink()) {
            String type = link.getRelation().getValue();
            String uri = link.getUrl().getValue();
            if ("self".equals(type)) {
                assertTrue(uri.contains("_type"), "self link should contain the implicitly add _type parameter");
                validSelf = true;
            }
        }

        assertTrue(validSelf, "missing self link");
    }

    @Test
    public void testSystemHistoryInvalidType_tenant1() throws Exception {
        FHIRRequestHeader altTenantHeader = new FHIRRequestHeader("X-FHIR-TENANT-ID", "tenant1");
        FHIRRequestHeader altDatastoreHeader = new FHIRRequestHeader("X-FHIR-DSID", "profile");


        FHIRParameters parameters = new FHIRParameters();
        // for tenant1, Patient is configured for search but CompartmentDefinition is not
        parameters.searchParam("_type", "Patient,CompartmentDefinition");
        FHIRResponse response = client.searchAll(parameters, false, altTenantHeader, altDatastoreHeader);
        assertResponse(response.getResponse(), Response.Status.BAD_REQUEST.getStatusCode());

        OperationOutcome oo = response.getResource(OperationOutcome.class);
        assertNotNull(oo);
        assertEquals(oo.getIssue().get(0).getDetails().getText().getValue(),
                "Search interaction is not supported for _type parameter value: CompartmentDefinition");
    }

    /*
     * Queries based on the URI the endpoint with the query parameter and value.
     */
    private void verifyReflexsiveUrl(String uri, String expectedLastUpdated) throws Exception {
        FHIRParameters parameters = new FHIRParameters();
        WebTarget target = client.getWebTarget();
        String queryParameterStrings = uri.replaceAll(target.getUri().toString() + "_search\\?", "");
        String[] queryParams = queryParameterStrings.split("&");
        String actualLastUpdated = null;
        for (String queryParam : queryParams) {
            String name = queryParam.split("=")[0];
            String value = queryParam.split("=")[1];
            if ("_lastUpdated".equals(name)) {
                actualLastUpdated = value;
            }
            parameters.searchParam(name, value);
        }
        assertEquals(actualLastUpdated, expectedLastUpdated);

        FHIRResponse response = client.searchAll(parameters, false, headerTenant, headerDataStore);

        assertResponse(response.getResponse(), Response.Status.OK.getStatusCode());
        Bundle bundle = response.getResource(Bundle.class);
        assertNotNull(bundle);
        assertTrue(bundle.getEntry().size() >= 1);
    }
}