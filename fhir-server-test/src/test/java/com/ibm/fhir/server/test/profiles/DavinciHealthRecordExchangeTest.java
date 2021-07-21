/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.profiles;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.client.FHIRParameters;
import com.ibm.fhir.client.FHIRResponse;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.test.TestUtil;

/**
 * Tests using Davinci Health Record Exchange (HREX) profile.
 */
public class DavinciHealthRecordExchangeTest extends ProfilesTestBase {
    private static final String CLASSNAME = DavinciHealthRecordExchangeTest.class.getName();
    private static final Logger LOG = Logger.getLogger(CLASSNAME);

    String coverageId = null;

    public Boolean skip = Boolean.TRUE;

    @Override
    public List<String> getRequiredProfiles() {
        //@formatter:off
        return Arrays.asList(
            "http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-coverage|0.2.0",
            "http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-organization|0.2.0",
            "http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-parameters-member-match-out|0.2.0",
            "http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-parameters-member-match-in|0.2.0",
            "http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-practitioner|0.2.0",
            "http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-practitionerrole|0.2.0",
            "http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-provenance|0.2.0",
            "http://hl7.org/fhir/us/davinci-hrex/StructureDefinition/hrex-task-data-request|0.2.0");
        //@formatter:on
    }

    @Override
    public void setCheck(Boolean check) {
        this.skip = check;

        if (skip) {
            LOG.info("Skipping Tests for HREX");
        }
    }

    public void loadCoverage() throws Exception {
        String resource = "json/profiles/fhir-ig-davinci-hrex/Coverage-full.json";
        WebTarget target = getWebTarget();
        com.ibm.fhir.model.resource.Coverage list = TestUtil.readExampleResource(resource);
        Entity<com.ibm.fhir.model.resource.Coverage> entity = Entity.entity(list, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Coverage").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        coverageId = getLocationLogicalId(response);
        response = target.path("Coverage/" + coverageId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        addToResourceRegistry("Coverage", coverageId);
    }

    // Load Resources
    @BeforeClass
    public void loadResources() throws Exception {
        if (!skip) {
            loadCoverage();
        }
    }

    @Test
    public void testCoverageBySubscriberId() throws Exception {
        if (!skip) {
            FHIRParameters parameters = new FHIRParameters();
            parameters.searchParam("subscriber-id", "97531");
            FHIRResponse response = client.search(com.ibm.fhir.model.resource.Coverage.class.getSimpleName(), parameters);
            assertSearchResponse(response, Response.Status.OK.getStatusCode());
            Bundle bundle = response.getResource(Bundle.class);
            assertNotNull(bundle);
            assertTrue(bundle.getEntry().size() >= 1);
            assertContainsIds(bundle, coverageId);
        }
    }
}