/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import static com.ibm.fhir.core.FHIRMediaType.FHIR_VERSION_PARAMETER;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;
import static org.testng.AssertJUnit.assertNotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.resource.CapabilityStatement;
import com.ibm.fhir.model.resource.CapabilityStatement.Rest;
import com.ibm.fhir.model.resource.CapabilityStatement.Rest.Resource;
import com.ibm.fhir.model.type.code.ResourceType;
import com.ibm.fhir.path.exception.FHIRPathException;
import com.ibm.fhir.validation.exception.FHIRValidationException;

public class CapabilitiesVersionTest extends FHIRServerTestBase {
    private static final Set<ResourceType.Value> R4B_ONLY_RESOURCES = new HashSet<>();
    {
        R4B_ONLY_RESOURCES.add(ResourceType.Value.ADMINISTRABLE_PRODUCT_DEFINITION);
        R4B_ONLY_RESOURCES.add(ResourceType.Value.CITATION);
        R4B_ONLY_RESOURCES.add(ResourceType.Value.CLINICAL_USE_DEFINITION);
        R4B_ONLY_RESOURCES.add(ResourceType.Value.EVIDENCE_REPORT);
        R4B_ONLY_RESOURCES.add(ResourceType.Value.INGREDIENT);
        R4B_ONLY_RESOURCES.add(ResourceType.Value.MANUFACTURED_ITEM_DEFINITION);
        R4B_ONLY_RESOURCES.add(ResourceType.Value.MEDICINAL_PRODUCT_DEFINITION);
        R4B_ONLY_RESOURCES.add(ResourceType.Value.NUTRITION_PRODUCT);
        R4B_ONLY_RESOURCES.add(ResourceType.Value.PACKAGED_PRODUCT_DEFINITION);
        R4B_ONLY_RESOURCES.add(ResourceType.Value.REGULATED_AUTHORIZATION);
        R4B_ONLY_RESOURCES.add(ResourceType.Value.SUBSCRIPTION_STATUS);
        R4B_ONLY_RESOURCES.add(ResourceType.Value.SUBSCRIPTION_TOPIC);
        R4B_ONLY_RESOURCES.add(ResourceType.Value.SUBSTANCE_DEFINITION);
        // The following resource types existed in R4, but have breaking changes in R4B.
        // Because we only support the R4B version, we don't want to advertise these in our 4.0.1 statement.
        R4B_ONLY_RESOURCES.add(ResourceType.Value.DEVICE_DEFINITION);
        R4B_ONLY_RESOURCES.add(ResourceType.Value.EVIDENCE);
        R4B_ONLY_RESOURCES.add(ResourceType.Value.EVIDENCE_VARIABLE);
        // TODO: make final decision on whether to lump these in with the breaking resources
        // R4B_ONLY_RESOURCES.add(ResourceType.Value.PLAN_DEFINITION);
        // R4B_ONLY_RESOURCES.add(ResourceType.Value.ACTIVITY_DEFINITION);
    }

    /**
     * Verify the 'metadata' API.
     */
    @Test(dataProvider = "dataMethod")
    public void testWithTenantAndFHIRVersion(String tenant, String fhirVersion) throws FHIRPathException, FHIRValidationException {
        WebTarget target = getWebTarget();
        Map<String,String> fhirVersionParameterMap = (fhirVersion == null) ? null : Collections.singletonMap(FHIR_VERSION_PARAMETER, fhirVersion);
        MediaType mediaType = new MediaType("application", FHIRMediaType.SUBTYPE_FHIR_JSON, fhirVersionParameterMap);

        Response response = target.path("metadata")
                .request(mediaType)
                .header("X-FHIR-TENANT-ID", tenant)
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());

        CapabilityStatement conf = response.readEntity(CapabilityStatement.class);
        assertNotNull(conf);
        assertNotNull(conf.getFhirVersion());
        if (fhirVersion != null) {
            assertTrue(conf.getFhirVersion().getValue().startsWith(fhirVersion));
        }

        switch (conf.getFhirVersion().getValueAsEnum()) {
        case VERSION_4_0_1:
            // verify it has no "R4B-only" resource types
            for (Rest rest : conf.getRest()) {
                for (Resource resource : rest.getResource()) {
                    assertFalse(R4B_ONLY_RESOURCES.contains(resource.getType().getValueAsEnum()),
                            "unexpected resource type: " + resource.getType().getValue());
                }
            }
            break;
        case VERSION_4_3_0_CIBUILD:
            // nothing to verify at the moment
            break;
        default:
            fail("unexpected fhirVersion: " + conf.getFhirVersion().getValue());
        }
    }

    /**
     * tenant, fhirVersion
     */
    @DataProvider
    public static Object[][] dataMethod() {
        String[] tenants = new String[] {
                "default", // defaultFhirVersion=4.0
                "tenant1", // defaultFhirVersion=4.3
                "tenant2"  // no defaultFhirVersion configured
            };
        String[] versions = new String[] {null, "4.0", "4.3"};

        // compute the cartesian product
        Object[][] inputs = new Object[tenants.length * versions.length][2];
        int i = 0;
        for (String tenant : tenants) {
            for (String version : versions) {
                inputs[i++] = new Object[] {tenant, version};
            }
        }

        return inputs;
    }
}
