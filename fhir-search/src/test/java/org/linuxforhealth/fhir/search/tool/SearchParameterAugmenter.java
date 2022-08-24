/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.search.tool;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.linuxforhealth.fhir.core.ResourceType;
import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.generator.FHIRGenerator;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.resource.SearchParameter;
import org.linuxforhealth.fhir.model.test.TestUtil;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.CodeableReference;
import org.linuxforhealth.fhir.model.type.Element;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.ResourceTypeCode;
import org.linuxforhealth.fhir.model.type.code.SearchParamType;
import org.linuxforhealth.fhir.model.util.ModelSupport;
import org.linuxforhealth.fhir.path.FHIRPathNode;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator;
import org.linuxforhealth.fhir.path.exception.FHIRPathException;
import org.linuxforhealth.fhir.registry.FHIRRegistry;
import org.linuxforhealth.fhir.search.SearchConstants;

/**
 * A utility for adding the http://fhir.linuxforhealth.org/extension/implicit-system extension
 * to search parameters which always reference code values with a particular system
 */
public class SearchParameterAugmenter {
    private static final FHIRGenerator generator = FHIRGenerator.generator(Format.JSON, true);

    public static void main(String[] args) throws Exception {
        Set<SearchParameter> params = new HashSet<>();
        params.addAll(FHIRRegistry.getInstance().getSearchParameters("token"));
        params.addAll(FHIRRegistry.getInstance().getSearchParameters("reference"));

        for (SearchParameter searchParameter : params) {
            List<ResourceTypeCode> base = searchParameter.getBase();
            if (base.size() != 1 || base.get(0).getValueAsEnum() == ResourceType.RESOURCE) {
                continue; // too complicated to handle this case right now
            }

            String expression = searchParameter.getExpression().getValue();

            FHIRPathNode node = getExampleNodeIfPossible(searchParameter, base);
            if (node == null) {
                System.err.println("Skipping " + searchParameter.getId() + " because we couldn't infer the element type from "
                        + "'" + searchParameter.getExpression().getValue() + "'");
                continue;
            }
            Element element = node.asElementNode().element();

            Path pathToSPDef = Paths.get("../conformance/fhir-core-r4b/src/main/resources/hl7/fhir/core/430/package/SearchParameter-"
                    + searchParameter.getId() + ".json");
            if (element instanceof Code && SearchParamType.Value.TOKEN == searchParameter.getType().getValueAsEnum()) {
                String system = ModelSupport.getSystem(element.as(Code.class));

                if (system != null) {
                    System.out.println(searchParameter.getId() + ": " + system);

                    String currentValue = searchParameter.getExtension().stream()
                            .filter(e -> SearchConstants.IMPLICIT_SYSTEM_EXT_URL.equals(e.getUrl()) && e.getValue() != null)
                            .reduce((a, b) -> {
                                throw new IllegalStateException("Multiple existing extension values: " + a + ", " + b);
                            })
                            .map(e -> e.getValue().as(Uri.class).getValue())
                            .orElse(null);

                    if (currentValue == null) {
                        searchParameter = searchParameter.toBuilder()
                                .extension(buildImplicitSystemExtension(system))
                                .build();
                    } else if (currentValue.equals(system)){
                        continue;
                    } else {
                        throw new IllegalStateException("Existing SearchParameter '" + searchParameter.getId() +
                                "' already has an implicity system extension and it doesn't match '" + system + "'");
                    }

                    try (BufferedWriter writer = Files.newBufferedWriter(pathToSPDef, Charset.forName("UTF-8"))) {
                        generator.generate(searchParameter, writer);
                    }
                }
            } else if (element instanceof CodeableReference) {
                // https://jira.hl7.org/browse/FHIR-37867
                if (SearchParamType.Value.REFERENCE == searchParameter.getType().getValueAsEnum()) {
                    System.out.println("patching CodeableReference search parameter expression to select the reference");
                    searchParameter = searchParameter.toBuilder()
                            .expression(expression + ".reference")
                            .build();
                } else if (SearchParamType.Value.TOKEN == searchParameter.getType().getValueAsEnum()) {
                    System.out.println("patching CodeableReference search parameter expression to select the concept");
                    searchParameter = searchParameter.toBuilder()
                            .expression(expression + ".concept")
                            .build();
                }

                try (BufferedWriter writer = Files.newBufferedWriter(pathToSPDef, Charset.forName("UTF-8"))) {
                    generator.generate(searchParameter, writer);
                }
            }
        }
    }

    private static FHIRPathNode getExampleNodeIfPossible(SearchParameter searchParameter, List<ResourceTypeCode> base)
            throws Exception, FHIRPathException {
        FHIRPathNode node = null;
        // try up to five examples to find the node
        try {
            for (int i = 1; i <= 5; i++) {
                Resource sampleResource = TestUtil.readExampleResource("json/complete-mock/" + base.get(0).getValue() + "-" + i + ".json");

                Collection<FHIRPathNode> nodes = FHIRPathEvaluator.evaluator().evaluate(sampleResource, searchParameter.getExpression().getValue());

                if (nodes.size() == 0) {
                    // keep looking in other examples for this element
                    continue;
                } else if (nodes.size() > 1) {
                    System.out.println("Skipping " + searchParameter.getId() + " because the complete-mock example has multiple elements for "
                            + "'" + searchParameter.getExpression().getValue() + "'");
                    continue;
                }

                FHIRPathNode selectedNode = nodes.iterator().next();

                if (selectedNode.isElementNode()) {
                    node = selectedNode;
                } else {
                    System.out.println("Skipping " + searchParameter.getId() + " because the complete-mock example has a non-element node for "
                            + "'" + searchParameter.getExpression().getValue() + "'");
                }
            }
        } catch (FileNotFoundException e) {
            // we didn't find the node before running out of examples
        }
        return node;
    }

    public static Extension buildImplicitSystemExtension(String implicitSystemValue) {
        if (implicitSystemValue == null) return null;

        return Extension.builder()
                .url(SearchConstants.IMPLICIT_SYSTEM_EXT_URL)
                .value(Uri.of(implicitSystemValue))
                .build();
    }
}
