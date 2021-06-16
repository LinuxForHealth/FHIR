/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.test;

import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_TRUE;
import static org.testng.Assert.assertEquals;

import java.util.Collection;

import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.Bundle.Entry.Request;
import com.ibm.fhir.model.resource.HealthcareService;
import com.ibm.fhir.model.resource.Organization;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.model.type.code.HTTPVerb;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.FHIRPathResourceNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;

public class ResolveInternalFragmentReferenceTest {
    @Test
    public void testResolveInternalFragmentReference() throws Exception {
        Bundle bundle = Bundle.builder()
                .type(BundleType.TRANSACTION)
                .entry(Entry.builder()
                    .resource(Organization.builder()
                        .id("1")
                        .name(string("Test Organization"))
                        .contained(HealthcareService.builder()
                            .providedBy(Reference.builder()
                                .reference(string("#"))
                                .build())
                            .build(),
                            Organization.builder()
                                .id("parentOrg")
                                .name(string("Parent Organization"))
                            .build())
                        .partOf(Reference.builder()
                            .reference(string("#parentOrg"))
                            .build())
                        .build())
                    .request(Request.builder()
                        .method(HTTPVerb.PUT)
                        .url(Uri.of("Organization/1"))
                        .build())
                    .build())
                .build();

        EvaluationContext evaluationContext = new EvaluationContext(bundle);
        FHIRPathResourceNode resourceNode = evaluationContext.getTree().getNode("Bundle.entry[0].resource.contained[0]").asResourceNode();

        Collection<FHIRPathNode> result = FHIRPathEvaluator.evaluator().evaluate(evaluationContext, "providedBy.exists() and providedBy.resolve().partOf.resolve().is(Organization)", resourceNode);

        assertEquals(result, SINGLETON_TRUE);
    }
}