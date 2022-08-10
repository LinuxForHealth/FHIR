/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.search.tool;

import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

import org.linuxforhealth.fhir.core.ResourceType;
import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.generator.FHIRGenerator;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.resource.SearchParameter;
import org.linuxforhealth.fhir.model.test.TestUtil;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.Element;
import org.linuxforhealth.fhir.model.type.Extension;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.ResourceTypeCode;
import org.linuxforhealth.fhir.model.util.ModelSupport;
import org.linuxforhealth.fhir.path.FHIRPathNode;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator;
import org.linuxforhealth.fhir.registry.FHIRRegistry;
import org.linuxforhealth.fhir.search.SearchConstants;

/**
 * A utility for adding the http://fhir.linuxforhealth.org/extension/implicit-system extension
 * to search parameters which always reference code values with a particular system
 */
public class SearchParameterAugmenter {
    private static final FHIRGenerator generator = FHIRGenerator.generator(Format.JSON, true);

    public static void main(String[] args) throws Exception {
        Collection<SearchParameter> tokenParams = FHIRRegistry.getInstance().getSearchParameters("token");

        for (SearchParameter searchParameter : tokenParams) {
            List<ResourceTypeCode> base = searchParameter.getBase();
            if (base.size() != 1 || base.get(0).getValueAsEnum() == ResourceType.RESOURCE) {
                continue; // too complicated to handle this case right now
            }

            Resource sampleResource = TestUtil.readExampleResource("json/complete-mock/" + base.get(0).getValue() + "-1.json");

            Collection<FHIRPathNode> nodes = FHIRPathEvaluator.evaluator().evaluate(sampleResource, searchParameter.getExpression().getValue());

            if (nodes.size() != 1) {
                continue; // too complicated to handle this case right now
            }

            FHIRPathNode node = nodes.iterator().next();

            if (!node.isElementNode()) {
                continue; // too complicated to handle this case right now
            }

            Element element = node.asElementNode().element();
            if (element instanceof Code) {
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

                    Path path = Paths.get("../conformance/fhir-core-r4b/src/main/resources/hl7/fhir/core/430/package/SearchParameter-"
                            + searchParameter.getId() + ".json");
                    BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"));
                    generator.generate(searchParameter, writer);
                }
            }
        }
    }

    public static Extension buildImplicitSystemExtension(String implicitSystemValue) {
        if (implicitSystemValue == null) return null;

        return Extension.builder()
                .url(SearchConstants.IMPLICIT_SYSTEM_EXT_URL)
                .value(Uri.of(implicitSystemValue))
                .build();
    }
}
