/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.profile;

import static com.ibm.fhir.model.type.String.string;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ibm.fhir.model.resource.CapabilityStatement.Rest.Resource;
import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.model.resource.StructureDefinition.Snapshot;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.ElementDefinition;
import com.ibm.fhir.model.type.ElementDefinition.Binding;
import com.ibm.fhir.model.type.ElementDefinition.Constraint;
import com.ibm.fhir.model.type.ElementDefinition.Slicing;
import com.ibm.fhir.model.type.ElementDefinition.Slicing.Discriminator;
import com.ibm.fhir.model.type.ElementDefinition.Type;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.UnsignedInt;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.ConstraintSeverity;
import com.ibm.fhir.model.type.code.DiscriminatorType;
import com.ibm.fhir.model.type.code.PublicationStatus;
import com.ibm.fhir.model.type.code.SlicingRules;
import com.ibm.fhir.model.type.code.TypeDerivationRule;
import com.ibm.fhir.model.util.ModelSupport;

/**
 * A utility class for building profiles from a base structure definition
 */
public class ProfileBuilder {
    private final Class<?> type;
    private final StructureDefinition structureDefinition;
    private final String url;
    private final String version;
    private final List<ElementDefinition> element;
    private final Map<String, ElementDefinition> elementDefinitionMap;

    public ProfileBuilder(Class<?> type, String url, String version) {
        this.type = type;
        structureDefinition = ProfileSupport.getStructureDefinition(type);
        this.url = url;
        this.version = version;
        element = new ArrayList<>(structureDefinition.getSnapshot().getElement());
        elementDefinitionMap = buildElementDefinitionMap(structureDefinition);
    }

    public ProfileBuilder binding(String id, Binding binding) {
        ElementDefinition elementDefinition = getElementDefinition(id).toBuilder()
            .binding(binding)
            .build();
        element.set(indexOf(id), elementDefinition);
        return this;
    }

    public StructureDefinition build() {
        return StructureDefinition.builder()
            .url(Uri.of(url))
            .version(string(version))
            .name(string(url.substring(url.lastIndexOf("/") + 1)))
            ._abstract(com.ibm.fhir.model.type.Boolean.FALSE)
            .type(Uri.of(type.getSimpleName()))
            .status(PublicationStatus.DRAFT)
            .kind(structureDefinition.getKind())
            .baseDefinition(Canonical.of(structureDefinition.getUrl().getValue()))
            .derivation(TypeDerivationRule.CONSTRAINT)
            .snapshot(structureDefinition.getSnapshot().toBuilder()
                .element(element)
                .build())
            .differential(null)
            .build();
    }

    public ProfileBuilder cardinality(String id, int min, String max) {
        ElementDefinition elementDefinition = getElementDefinition(id).toBuilder()
            .min(UnsignedInt.of(min))
            .max(string(max))
            .build();
        element.set(indexOf(id), elementDefinition);
        return this;
    }

    public ProfileBuilder constraint(String id, Constraint... constraint) {
        ElementDefinition elementDefinition = getElementDefinition(id);
        elementDefinition = elementDefinition.toBuilder()
            .constraint(elementDefinition.getConstraint())
            .constraint(constraint)
            .build();
        element.set(indexOf(id), elementDefinition);
        return this;
    }

    public ProfileBuilder fixed(String id, Element fixed) {
        ElementDefinition elementDefinition = getElementDefinition(id).toBuilder()
            .fixed(fixed)
            .build();
        element.set(indexOf(id), elementDefinition);
        return this;
    }

    public ProfileBuilder pattern(String id, Element pattern) {
        ElementDefinition elementDefinition = getElementDefinition(id).toBuilder()
            .pattern(pattern)
            .build();
        element.set(indexOf(id), elementDefinition);
        return this;
    }

    public ProfileBuilder slice(String id, String sliceName, Class<?> type, int min, String max) {
        element.addAll(findIndex(id), createSliceElementDefinitions(id, sliceName, type, min, max));
        return this;
    }

    public ProfileBuilder slicing(String id, Slicing slicing) {
        ElementDefinition elementDefinition = getElementDefinition(id).toBuilder()
            .slicing(slicing)
            .build();
        element.set(indexOf(id), elementDefinition);
        return this;
    }

    public ProfileBuilder type(String id, Type... type) {
        ElementDefinition elementDefinition = getElementDefinition(id).toBuilder()
            .type(Arrays.asList(type))
            .build();
        element.set(indexOf(id), elementDefinition);
        return this;
    }

    private Map<String, ElementDefinition> buildElementDefinitionMap(StructureDefinition structureDefinition) {
        Map<String, ElementDefinition> elementDefinitionMap = new LinkedHashMap<>();
        for (ElementDefinition elementDefinition : structureDefinition.getSnapshot().getElement()) {
            elementDefinitionMap.put(elementDefinition.getId(), elementDefinition);
        }
        return elementDefinitionMap;
    }

    private List<ElementDefinition> createSliceElementDefinitions(String id, String sliceName, Class<?> type, int min, String max) {
        if (Resource.class.equals(type)) {
            ElementDefinition elementDefinition = elementDefinitionMap.get(id);
            return Collections.singletonList(elementDefinition.toBuilder()
                .id(id + ":" + sliceName)
                .sliceName(string(sliceName))
                .min(UnsignedInt.of(min))
                .max(string(max))
                .build());
        }
        List<ElementDefinition> sliceElementDefinitions = new ArrayList<>();
        String typeName = ModelSupport.getTypeName(type);
        StructureDefinition structureDefinition = ProfileSupport.getStructureDefinition(type);
        Snapshot snapshot = structureDefinition.getSnapshot();
        for (int i = 0; i < snapshot.getElement().size(); i++) {
            ElementDefinition elementDefinition = snapshot.getElement().get(i);
            ElementDefinition.Builder builder = elementDefinition.toBuilder();
            builder.id(elementDefinition.getId().replace(typeName, id + ":" + sliceName));
            builder.path(string(elementDefinition.getPath().getValue().replace(typeName, id)));
            if (i == 0) {
                builder.sliceName(string(sliceName));
                builder.base(elementDefinition.getBase().toBuilder()
                    .path(string(id))
                    .build());
                builder.min(UnsignedInt.of(min));
                builder.max(string(max));
            }
            sliceElementDefinitions.add(builder.build());
        }
        return sliceElementDefinitions;
    }

    private int findIndex(String id) {
        return indexOf(id) + getElementDefinitions(id).size();
    }

    private ElementDefinition getElementDefinition(String id) {
        for (ElementDefinition elementDefinition : element) {
            if (elementDefinition.getId().equals(id)) {
                return elementDefinition;
            }
        }
        return null;
    }

    private List<ElementDefinition> getElementDefinitions(String prefix) {
        List<ElementDefinition> elementDefinitions = new ArrayList<>();
        for (ElementDefinition elementDefinition : element) {
            if (elementDefinition.getId().startsWith(prefix)) {
                elementDefinitions.add(elementDefinition);
            }
        }
        return elementDefinitions;
    }

    private int indexOf(String id) {
        for (int i = 0; i < element.size(); i++) {
            ElementDefinition elementDefinition = element.get(i);
            if (elementDefinition.getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    public static Binding binding(BindingStrength strength, String valueSet) {
        return Binding.builder()
            .strength(strength)
            .valueSet(Canonical.of(valueSet))
            .build();
    }

    public static Binding binding(BindingStrength strength, String valueSet, String maxValueSet) {
        return Binding.builder()
            .strength(strength)
            .valueSet(Canonical.of(valueSet))
            .extension(Extension.builder()
                .url("http://hl7.org/fhir/StructureDefinition/elementdefinition-maxValueSet")
                .value(Canonical.of(maxValueSet))
                .build())
            .build();
    }

    public static Constraint constraint(String key, ConstraintSeverity severity, String human, String expression) {
        return Constraint.builder()
                .key(Id.of(key))
            .severity(severity)
            .human(string(human))
            .expression(string(expression))
            .build();
    }

    public static Discriminator discriminator(DiscriminatorType type, String path) {
        return Discriminator.builder()
            .type(type)
            .path(string(path))
            .build();
    }

    public static Slicing slicing(Discriminator discriminator, SlicingRules rules) {
        return Slicing.builder()
            .discriminator(discriminator)
            .rules(rules)
            .build();
    }

    public static List<String> profile(String... profile) {
        return Arrays.asList(profile);
    }

    public static List<String> targetProfile(String...targetProfile) {
        return Arrays.asList(targetProfile);
    }

    public static Type type(String code) {
        return type(code, Collections.emptyList(), Collections.emptyList());
    }

    public static Type type(String code, List<String> profile) {
        return type(code, profile, Collections.emptyList());
    }

    public static Type type(String code, List<String> profile, List<String> targetProfile) {
        return Type.builder()
            .code(Uri.of(code))
            .profile(profile.stream()
                .map(p -> Canonical.of(p))
                .collect(Collectors.toList()))
            .targetProfile(targetProfile.stream()
                .map(p -> Canonical.of(p))
                .collect(Collectors.toList()))
            .build();
    }
}
