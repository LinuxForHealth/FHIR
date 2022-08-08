/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.test;

import static org.linuxforhealth.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Collections;
import java.util.Set;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.core.FHIRMediaType;
import org.linuxforhealth.fhir.model.resource.CodeSystem;
import org.linuxforhealth.fhir.model.resource.CodeSystem.Concept;
import org.linuxforhealth.fhir.model.resource.ValueSet.Compose.Include.Filter;
import org.linuxforhealth.fhir.model.test.TestUtil;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.code.FilterOperator;
import org.linuxforhealth.fhir.term.remote.provider.RemoteTermServiceProvider;
import org.linuxforhealth.fhir.term.remote.provider.RemoteTermServiceProvider.Configuration;
import org.linuxforhealth.fhir.term.remote.provider.RemoteTermServiceProvider.Configuration.BasicAuth;
import org.linuxforhealth.fhir.term.remote.provider.RemoteTermServiceProvider.Configuration.Header;
import org.linuxforhealth.fhir.term.remote.provider.RemoteTermServiceProvider.Configuration.Supports;
import org.linuxforhealth.fhir.term.remote.provider.RemoteTermServiceProvider.Configuration.TrustStore;
import org.linuxforhealth.fhir.term.util.CodeSystemSupport;

public class RemoteTermServiceProviderTest extends FHIRServerTestBase {
    private CodeSystem codeSystem = null;
    private RemoteTermServiceProvider provider = null;

    @Test
    public void testCreateCodeSystem() throws Exception {
        WebTarget target = getWebTarget();

        CodeSystem codeSystem = TestUtil.readLocalResource("CodeSystem-test.json");
        Entity<CodeSystem> entity = Entity.entity(codeSystem, FHIRMediaType.APPLICATION_FHIR_JSON);

        Response response = target.path("CodeSystem").path("test")
                .request()
                .header("X-FHIR-TENANT-ID", "tenant1")
                .header("X-FHIR-DSID", "profile")
                .put(entity);
        int status = response.getStatus();
        assertTrue(status == Response.Status.CREATED.getStatusCode() || status == Response.Status.OK.getStatusCode());

        response = target.path("CodeSystem").path("test")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", "tenant1")
                .header("X-FHIR-DSID", "profile")
                .get();
        assertResponse(response, Response.Status.OK.getStatusCode());

        CodeSystem responseCodeSystem = response.readEntity(CodeSystem.class);
        TestUtil.assertResourceEquals(codeSystem, responseCodeSystem);

        this.codeSystem = responseCodeSystem;
    }

    @Test(dependsOnMethods = { "testCreateCodeSystem" })
    public void testCreateRemoteTermServiceProvider() {
        Configuration configuration = Configuration.builder()
            .base(getRestBaseURL())
            .trustStore(TrustStore.builder()
                .location(getTsLocation())
                .password(getTsPassword())
                .build())
            .basicAuth(BasicAuth.builder()
                .username(getFhirUser())
                .password(getFhirPassword())
                .build())
            .headers(Header.builder()
                    .name("X-FHIR-TENANT-ID")
                    .value("tenant1")
                    .build(),
                Header.builder()
                    .name("X-FHIR-DSID")
                    .value("profile")
                    .build())
            .supports(Supports.builder()
                .system(codeSystem.getUrl().getValue())
                .version(codeSystem.getVersion().getValue())
                .build())
            .build();
        provider = new RemoteTermServiceProvider(configuration);
    }

    @Test(dependsOnMethods = { "testCreateCodeSystem", "testCreateRemoteTermServiceProvider" })
    public void testRemoteTermServiceProviderGetConcept() {
        Concept concept = provider.getConcept(codeSystem, Code.of("a"));
        assertNotNull(concept);
        assertEquals(concept.getCode(), Code.of("a"));
    }

    @Test(dependsOnMethods = { "testCreateCodeSystem", "testCreateRemoteTermServiceProvider" })
    public void testRemoteTermServiceProviderGetConceptNotFound() {
        Concept concept = provider.getConcept(codeSystem, Code.of("zzz"));
        assertNull(concept);
    }

    @Test(dependsOnMethods = { "testCreateCodeSystem", "testCreateRemoteTermServiceProvider" })
    public void testRemoteTermServiceProviderHasConcept() {
        assertTrue(provider.hasConcept(codeSystem, Code.of("a")));
    }

    @Test(dependsOnMethods = { "testCreateCodeSystem", "testCreateRemoteTermServiceProvider" })
    public void testRemoteTermServiceProviderGetConcepts() {
        Set<Concept> concepts = provider.getConcepts(codeSystem);
        assertEquals(concepts, CodeSystemSupport.getConcepts(codeSystem, CodeSystemSupport.SIMPLE_CONCEPT_FUNCTION));
    }

    @Test(dependsOnMethods = { "testCreateCodeSystem", "testCreateRemoteTermServiceProvider" })
    public void testRemoteTermServiceProviderClosure() {
        Set<Concept> concepts = provider.closure(codeSystem, Code.of("a"));
        assertEquals(concepts, CodeSystemSupport.getConcepts(codeSystem, Collections.singletonList(Filter.builder()
            .property(Code.of("concept"))
            .op(FilterOperator.IS_A)
            .value(string("a"))
            .build()), CodeSystemSupport.SIMPLE_CONCEPT_FUNCTION));
    }

    @Test(dependsOnMethods = { "testCreateCodeSystem", "testCreateRemoteTermServiceProvider" })
    public void testRemoteTermServiceProviderSubsumes() {
        assertTrue(provider.subsumes(codeSystem, Code.of("a"), Code.of("k")));
    }

    @AfterClass
    public void afterClass() {
        if (provider != null) {
            provider.close();
        }
    }
}
