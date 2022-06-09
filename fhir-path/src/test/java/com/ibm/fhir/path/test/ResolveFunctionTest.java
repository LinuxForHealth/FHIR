/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.test;

import static com.ibm.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_TRUE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.io.InputStream;
import java.util.Collection;

import org.testng.annotations.Test;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.Bundle.Entry.Request;
import com.ibm.fhir.model.resource.HealthcareService;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Organization;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.model.type.code.HTTPVerb;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.FHIRPathResourceNode;
import com.ibm.fhir.path.FHIRPathType;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.path.util.FHIRPathUtil;

/**
 * Tests for the FHIRPath ResolveFunction implementation
 */
public class ResolveFunctionTest {
    /**
     * The default resolve() implementation returns a FHIRPathResourceNode that can work with the 'is' operator.
     */
    @Test
    public static void testResolve() throws Exception {
        try (InputStream in = ResolveFunctionTest.class.getClassLoader().getResourceAsStream("JSON/observation-example-f001-glucose.json")) {
            Observation observation = FHIRParser.parser(Format.JSON).parse(in);

            FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
            EvaluationContext evaluationContext = new EvaluationContext(observation);

            // resolve of "Patient/f001" should return a Patient
            Collection<FHIRPathNode> result = evaluator.evaluate(evaluationContext, "Observation.subject.where(resolve() is Patient)");
            assertNotNull(result);
            assertEquals(result.size(), 1);
            String value = result.iterator().next().asElementNode().element().as(Reference.class).getReference().getValue();
            assertEquals(value, "Patient/f001", "Observation.subject.reference");

            // resolve of "Patient/f001" should not return a Device
            result = evaluator.evaluate(evaluationContext, "Observation.subject.where(resolve() is Device)");
            assertNotNull(result);
            assertEquals(result.size(), 0);
        }
    }

    /**
     * Resolve works when there are multiple elements in the input context
     */
    @Test
    public void testResolveMultiple() throws Exception {
        Patient patient = Patient.builder()
                .generalPractitioner(Reference.builder()
                        .reference("Practitioner/1")
                        .build())
                .generalPractitioner(Reference.builder()
                        .reference("Practitioner/2")
                        .build())
                .generalPractitioner(Reference.builder()
                        .display("display-only ref")
                        .build())
                .build();
        Collection<FHIRPathNode> result = FHIRPathEvaluator.evaluator().evaluate(patient,
                "Patient.generalPractitioner.resolve()");
        assertEquals(result.size(), 3);
    }

    /**
     * The default resolve() implementation can resolve fragment references in both directions
     * <pre>
     * contained -(#)-> container
     * container -(#parentOrg)-> contained
     * </pre>
     */
    @Test
    public void testResolveInternalFragmentReference() throws Exception {
        Bundle bundle = Bundle.builder()
                .type(BundleType.TRANSACTION)
                .entry(Entry.builder()
                    .resource(Organization.builder()
                        .id("1")
                        .name("Test Organization")
                        .contained(HealthcareService.builder()
                            .providedBy(Reference.builder()
                                .reference("#")
                                .build())
                            .build(),
                            Organization.builder()
                                .id("parentOrg")
                                .name("Parent Organization")
                            .build())
                        .partOf(Reference.builder()
                            .reference("#parentOrg")
                            .build())
                        .build())
                    .request(Request.builder()
                        .method(HTTPVerb.PUT)
                        .url(Uri.of("Organization/1"))
                        .build())
                    .build())
                .build();

        EvaluationContext evaluationContext = new EvaluationContext(bundle);
        FHIRPathResourceNode resourceNode = evaluationContext.getTree()
                .getNode("Bundle.entry[0].resource.contained[0]").asResourceNode();

        Collection<FHIRPathNode> result = FHIRPathEvaluator.evaluator().evaluate(evaluationContext,
                "providedBy.exists() and providedBy.resolve().partOf.resolve().is(Organization)", resourceNode);

        assertEquals(result, SINGLETON_TRUE);
    }

    /**
     * The default resolve() implementation resolves local bundle references
     * based on the EvaluationContext
     */
    @Test
    public void testResolveBundleReference_NestedBundle() throws Exception {
            String baseUrl = "https://example.com/";
            String orgEndpoint = baseUrl + "Organization/";
            Bundle innerBundle = buildTestBundleWithFullUrlPrefix(orgEndpoint);
            Bundle outerBundle = Bundle.builder()
                    .id("outer")
                    .type(BundleType.TRANSACTION)
                    .entry(Entry.builder()
                        .fullUrl(Uri.of("https://example.com/Bundle/inner"))
                        .resource(innerBundle)
                        .build())
                    .build();

            // When the outer bundle is the context,
            // the bundle-local references of the resources within the inner bundle are expected to not resolve
            EvaluationContext evaluationContext = new EvaluationContext(outerBundle);
            Collection<FHIRPathNode> result = FHIRPathEvaluator.evaluator().evaluate(evaluationContext,
                    "Bundle.entry[0].resource.entry[1].resource.partOf.resolve()");
            assertEquals(result.size(), 1);
            assertNull(result.iterator().next().asResourceNode().resource());

            // When the inner bundle is the context,
            // the bundle-local references within the bundle resolve to the targets within that bundle
            evaluationContext = new EvaluationContext(innerBundle);
            result = FHIRPathEvaluator.evaluator().evaluate(evaluationContext,
                    "Bundle.entry[1].resource.partOf.resolve()");
            assertEquals(result.size(), 1);

            Resource testOrg = innerBundle.getEntry().get(0).getResource();
            assertEquals(result.iterator().next().asResourceNode().resource(), testOrg);

            // move https://example.com/Organization/test to the outer bundle and now the reference from the inner bundle should work
            outerBundle = FHIRPathUtil.add(outerBundle, "Bundle", "entry", innerBundle.getEntry().get(0));
            outerBundle = FHIRPathUtil.delete(outerBundle, "Bundle.entry[0].resource.entry[0]");

            evaluationContext = new EvaluationContext(outerBundle);
            result = FHIRPathEvaluator.evaluator().evaluate(evaluationContext,
                    "Bundle.entry[0].resource.entry[0].resource.partOf.resolve()");
            assertEquals(result.size(), 1);
            testOrg = outerBundle.getEntry().get(1).getResource();
            assertEquals(result.iterator().next().asResourceNode().resource(), testOrg);
    }

    /**
     * The default resolve() implementation resolves local bundle references
     * with the http/https scheme and uses the Bundle.entry.fullUrl to interpret
     * relative reference values
     */
    @Test
    public void testResolveBundleReference_HttpFullUrl() throws Exception {
        String baseUrl = "https://example.com/";
        String orgEndpoint = baseUrl + "Organization/";
        Bundle bundle = buildTestBundleWithFullUrlPrefix(orgEndpoint);

        // add an extra entry to test relative path resolution
        bundle = FHIRPathUtil.add(bundle, "Bundle", "entry", Entry.builder()
            .fullUrl(Uri.of(orgEndpoint + "2"))
            .resource(Organization.builder()
                .name("Child Organization 2")
                .partOf(Reference.builder()
                    .reference("Organization/test")
                    .build())
                .build())
            .request(Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Organization"))
                .build())
            .build());

        // add an extra entry to test absolute path resolution with different base url
        bundle = FHIRPathUtil.add(bundle, "Bundle", "entry", Entry.builder()
            .fullUrl(Uri.of(orgEndpoint + "3"))
            .resource(Organization.builder()
                .name("Unrelated Organization 3")
                .partOf(Reference.builder()
                    .reference("https://some.other.base/Organization/test")
                    .build())
                .build())
            .request(Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Organization"))
                .build())
            .build());

        // add an extra entry to test absolute path resolution with different base url
        bundle = FHIRPathUtil.add(bundle, "Bundle", "entry", Entry.builder()
            .fullUrl(Uri.of("https://some.other.base/Organization/4"))
            .resource(Organization.builder()
                .name("Unrelated Organization 4")
                .partOf(Reference.builder()
                    .reference("Organization/test")
                    .build())
                .build())
            .request(Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Organization"))
                .build())
            .build());

        EvaluationContext evaluationContext = new EvaluationContext(bundle);

        Collection<FHIRPathNode> result = FHIRPathEvaluator.evaluator().evaluate(evaluationContext,
                "Bundle.entry.resource.partOf.resolve()");
        assertEquals(result.size(), 5);
        for (FHIRPathNode n : result) {
            assertEquals(n.type(), FHIRPathType.FHIR_ORGANIZATION);
        }

        Object[] resources = result.stream()
                .map(n -> n.asResourceNode().resource())
                .toArray();

        Resource testOrg = bundle.getEntry().get(0).getResource();
        assertNotEquals(resources[0], testOrg);
        assertEquals(resources[1], testOrg);
        assertEquals(resources[2], testOrg);
        assertNotEquals(resources[3], testOrg);
        assertNotEquals(resources[4], testOrg);
    }

    /**
     * The default resolve() implementation cannot resolve local references when not in a bundle
     * context, but doesn't blow up when you try.
     */
    @Test
    public void testResolveNonBundleReference() throws Exception {
        Organization org = Organization.builder()
            .name("Test Organization")
            .partOf(Reference.builder()
                .reference("resource:1")
                .build())
            .build();

        EvaluationContext evaluationContext = new EvaluationContext(org);

        Collection<FHIRPathNode> result = FHIRPathEvaluator.evaluator().evaluate(evaluationContext,
                "Organization.partOf.resolve()");
        assertEquals(result.size(), 1);
        assertNull(result.iterator().next().asResourceNode().resource());
    }

    /**
     * The default resolve() implementation resolves local bundle references with *any* scheme,
     * so long as the reference values match the fullUrl values
     */
    @Test
    public void testResolveBundleReference_localFullUrl() throws Exception {
        String[] prefixes = new String[] {"resource:", "urn:uuid:"};
        for (String prefix : prefixes) {
            Bundle bundle = buildTestBundleWithFullUrlPrefix(prefix);

            EvaluationContext evaluationContext = new EvaluationContext(bundle);

            Collection<FHIRPathNode> result = FHIRPathEvaluator.evaluator().evaluate(evaluationContext,
                    "Bundle.entry[1].resource.partOf.resolve()");
            assertEquals(result.size(), 1);

            Resource testOrg = bundle.getEntry().get(0).getResource();

            assertEquals(result.iterator().next().asResourceNode().resource(), testOrg, "prefix '" + prefix + "'");
        }
    }

    private Bundle buildTestBundleWithFullUrlPrefix(String prefix) {
        Bundle bundle = Bundle.builder()
                .type(BundleType.TRANSACTION)
                .entry(Entry.builder()
                    .fullUrl(Uri.of(prefix + "test"))
                    .resource(Organization.builder()
                        .name("Test Organization")
                        .contained(HealthcareService.builder()
                            .providedBy(Reference.builder()
                                .reference("#")
                                .build())
                            .build(),
                            Organization.builder()
                                .id("parentOrg")
                                .name("Parent Organization")
                            .build())
                        .partOf(Reference.builder()
                            .reference("#parentOrg")
                            .build())
                        .build())
                    .request(Request.builder()
                        .method(HTTPVerb.POST)
                        .url(Uri.of("Organization"))
                        .build())
                    .build())
                .entry(Entry.builder()
                    .fullUrl(Uri.of(prefix + "1"))
                    .resource(Organization.builder()
                        .name("Child Organization 1")
                        .partOf(Reference.builder()
                            .reference(prefix + "test")
                            .build())
                        .build())
                    .request(Request.builder()
                        .method(HTTPVerb.POST)
                        .url(Uri.of("Organization"))
                        .build())
                    .build())
                .build();
        return bundle;
    }
}