package com.ibm.fhir.search.tool;
/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */


import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

import com.ibm.fhir.core.ResourceType;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.ResourceTypeCode;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.search.SearchConstants;

/**
 * A utility for adding the http://ibm.com/fhir/extension/implicit-system extension
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

            Resource sampleResource = TestUtil.readExampleResource("json/ibm/complete-mock/" + base.get(0).getValue() + "-1.json");

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

                    Path path = Paths.get("../fhir-registry/src/main/resources/hl7/fhir/core/package/SearchParameter-"
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
