/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.ig.us.core.tool.v311;

import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.ibm.fhir.core.FHIRConstants;
import com.ibm.fhir.core.ResourceType;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.model.resource.ValueSet.Compose.Include;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.ElementDefinition;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.FHIRDefinedType;
import com.ibm.fhir.model.type.code.ResourceTypeCode;
import com.ibm.fhir.profile.ProfileSupport;
import com.ibm.fhir.registry.FHIRRegistry;

/**
 * A utility for adding the http://ibm.com/fhir/extension/implicit-system extension
 * to search parameters which always reference code values with a particular system
 */
public class SearchParameterAugmenter {
    private static final String IMPLICIT_SYSTEM_EXT_URL = FHIRConstants.EXT_BASE + "implicit-system";
    private static final FHIRGenerator generator = FHIRGenerator.generator(Format.JSON, false);

    public static void main(String[] args) throws Exception {
        Collection<SearchParameter> tokenParams = FHIRRegistry.getInstance().getSearchParameters("token");

        for (SearchParameter searchParameter : tokenParams) {
            if (searchParameter.getUrl() != null && searchParameter.getUrl().hasValue()
                    && searchParameter.getUrl().getValue().startsWith("http://hl7.org/fhir/SearchParameter")
                    && searchParameter.getVersion().getValue() != "3.1.1") {
                continue; // skip the parameters defined in the base spec
            }

            List<ResourceTypeCode> base = searchParameter.getBase();
            if (base.size() != 1 || base.get(0).getValueAsEnum() == ResourceType.RESOURCE) {
                continue; // too complicated to handle this case right now
            }

            String implicitSystem = getImplicitSystem(searchParameter);

            if (implicitSystem != null) {
                System.out.println(searchParameter.getId() + ": " + implicitSystem);

                String currentValue = searchParameter.getExtension().stream()
                        .filter(e -> IMPLICIT_SYSTEM_EXT_URL.equals(e.getUrl()) && e.getValue() != null)
                        .reduce((a, b) -> {
                            throw new IllegalStateException("Multiple existing extension values: " + a + ", " + b);
                        })
                        .map(e -> e.getValue().as(Uri.class).getValue())
                        .orElse(null);

                if (currentValue == null) {
                    searchParameter = searchParameter.toBuilder()
                            .extension(buildImplicitSystemExtension(implicitSystem))
                            .build();
                } else if (currentValue.equals(implicitSystem)){
                    continue;
                } else {
                    throw new IllegalStateException("Existing SearchParameter '" + searchParameter.getId() +
                            "' already has an implicity system extension and it doesn't match '" + implicitSystem + "'");
                }

                Path path = Paths.get("src/main/resources/hl7/fhir/us/core/311/package/SearchParameter-"
                        + searchParameter.getId() + ".json");
                BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"));
                generator.generate(searchParameter, writer);
            }
        }
    }

    private static String getImplicitSystem(SearchParameter searchParameter) {
        String system = null;
        ElementDefinition def = ProfileSupport.getElementDefinition(searchParameter.getExpression().getValue());

        if (def != null &&
                def.getBinding() != null &&
                def.getType().size() == 1 &&
                FHIRDefinedType.CODE.getValue().equals(def.getType().get(0).getCode().getValue()) &&
                BindingStrength.Value.REQUIRED == def.getBinding().getStrength().getValueAsEnum()) {
            Canonical valueSetRef = def.getBinding().getValueSet();
            ValueSet valueSet = FHIRRegistry.getInstance().getResource(valueSetRef.getValue(), ValueSet.class);

            if (valueSet.getCompose() != null) {
                Set<Include> systems = valueSet.getCompose().getInclude().stream().collect(Collectors.toSet());
                if (systems.size() == 1) {
                    Uri systemUri = systems.iterator().next().getSystem();
                    if (systemUri != null) {
                        system = systemUri.getValue();
                    }
                }
            }
        }
        return system;
    }

    public static Extension buildImplicitSystemExtension(String implicitSystemValue) {
        if (implicitSystemValue == null) return null;

        return Extension.builder()
                .url(IMPLICIT_SYSTEM_EXT_URL)
                .value(Uri.of(implicitSystemValue))
                .build();
    }
}
