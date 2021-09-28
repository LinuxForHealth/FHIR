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
import com.ibm.fhir.model.resource.StructureDefinition.Context;
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
    protected final Class<?> type;
    protected final StructureDefinition structureDefinition;
    protected final String url;
    protected final String version;
    protected final List<ElementDefinition> element;
    protected final Map<String, ElementDefinition> elementDefinitionMap;
    protected final List<Context> context = new ArrayList<>();

    public ProfileBuilder(Class<?> type, String url, String version) {
        this.type = type;
        structureDefinition = ProfileSupport.getStructureDefinition(type);
        this.url = url;
        this.version = version;
        element = new ArrayList<>(structureDefinition.getSnapshot().getElement());
        elementDefinitionMap = buildElementDefinitionMap(structureDefinition);
    }

    /**
     * Add value set binding to the element definition with the given id.
     *
     * @param id
     *     the id
     * @param binding
     *     the value set binding
     * @return
     *     this profile builder
     */
    public ProfileBuilder binding(String id, Binding binding) {
        ElementDefinition elementDefinition = getElementDefinition(id).toBuilder()
            .binding(binding)
            .build();
        element.set(indexOf(id), elementDefinition);
        return this;
    }

    /**
     * Create a {@link StructureDefinition} instance from this profile builder.
     *
     * @return
     *     a {@link StructureDefinition} instance
     */
    public StructureDefinition build() {
        return StructureDefinition.builder()
            .url(Uri.of(url))
            .version(string(version))
            .name(string(url.substring(url.lastIndexOf("/") + 1)))
            ._abstract(com.ibm.fhir.model.type.Boolean.FALSE)
            .type(Uri.of(type.getSimpleName()))
            .status(PublicationStatus.DRAFT)
            .kind(structureDefinition.getKind())
            .context(context)
            .baseDefinition(Canonical.of(structureDefinition.getUrl().getValue()))
            .derivation(TypeDerivationRule.CONSTRAINT)
            .snapshot(structureDefinition.getSnapshot().toBuilder()
                .element(element)
                .build())
            .differential(null)
            .build();
    }

    /**
     * Add a cardinality constraint to the element definition with the given id.
     *
     * @param id
     *     the id
     * @param min
     *     the minimum cardinality
     * @param max
     *     the maximum cardinality
     * @return
     *     this profile builder
     */
    public ProfileBuilder cardinality(String id, int min, String max) {
        ElementDefinition elementDefinition = getElementDefinition(id).toBuilder()
            .min(UnsignedInt.of(min))
            .max(string(max))
            .build();
        element.set(indexOf(id), elementDefinition);
        return this;
    }

    /**
     * Add constraint objects to the element definition with the given id,
     *
     * @param id
     *     the id
     * @param constraint
     *     the constraint object
     * @return
     *     this profile builder
     */
    public ProfileBuilder constraint(String id, Constraint... constraint) {
        ElementDefinition elementDefinition = getElementDefinition(id);
        elementDefinition = elementDefinition.toBuilder()
            .constraint(elementDefinition.getConstraint())
            .constraint(constraint)
            .build();
        element.set(indexOf(id), elementDefinition);
        return this;
    }

    /**
     * Add a fixed value constraint to the element definition with the given id.
     *
     * @param id
     *     the id
     * @param fixed
     *     the fixed value
     * @return
     *     this profile builder
     */
    public ProfileBuilder fixed(String id, Element fixed) {
        ElementDefinition elementDefinition = getElementDefinition(id).toBuilder()
            .fixed(fixed)
            .build();
        element.set(indexOf(id), elementDefinition);
        return this;
    }

    /**
     * Add a pattern value constraint to the element definition with the given id.
     *
     * @param id
     *     the id
     * @param pattern
     *     the pattern value
     * @return
     *     this profile builder
     */
    public ProfileBuilder pattern(String id, Element pattern) {
        ElementDefinition elementDefinition = getElementDefinition(id).toBuilder()
            .pattern(pattern)
            .build();
        element.set(indexOf(id), elementDefinition);
        return this;
    }

    /**
     * Add a slice to the element definition the given id.
     *
     * @param id
     *     the id
     * @param sliceName
     *     the slice name
     * @param type
     *     the type
     * @param min
     *     the minimum cardinality
     * @param max
     *     the maximum cardinality
     * @return
     *     this profile builder
     */
    public ProfileBuilder slice(String id, String sliceName, Class<?> type, int min, String max) {
        element.addAll(findIndex(id), createSliceElementDefinitions(id, sliceName, type, min, max));
        return this;
    }

    /**
     * Add a slice definition to the element definition with the given id.
     *
     * @param id
     *     the id
     * @param slicing
     *     the slicing
     * @return
     *     this profile builder
     */
    public ProfileBuilder slicing(String id, Slicing slicing) {
        ElementDefinition elementDefinition = getElementDefinition(id).toBuilder()
            .slicing(slicing)
            .build();
        element.set(indexOf(id), elementDefinition);
        return this;
    }

    /**
     * Add a type constraint to the element definition with the given id.
     *
     * @param id
     *     the id
     * @param type
     *     the type
     * @return
     *     this profile builder
     */
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
                builder.constraint(elementDefinitionMap.get(id).getConstraint());
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

    /**
     * Create a value set binding from the given strength and valueSet.
     *
     * @param strength
     *     the strength
     * @param valueSet
     *     the value set
     * @return
     *     a value set binding
     */
    public static Binding binding(BindingStrength strength, String valueSet) {
        return Binding.builder()
            .strength(strength)
            .valueSet(Canonical.of(valueSet))
            .build();
    }

    /**
     * Create a value set binding from the given strength, value set, and max value set.
     *
     * @param strength
     *     the strength
     * @param valueSet
     *     the value set
     * @param maxValueSet
     *     the max value set
     * @return
     *     a value set binding
     */
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

    /**
     * Create a constraint from the given key, severity, human-readable description, and FHIRPath expression.
     *
     * @param key
     *     the key
     * @param severity
     *     the severity
     * @param human
     *     the human-readable description
     * @param expression
     *     the FHIRPath expression
     * @return
     *     a constraint
     */
    public static Constraint constraint(String key, ConstraintSeverity severity, String human, String expression) {
        return Constraint.builder()
                .key(Id.of(key))
            .severity(severity)
            .human(string(human))
            .expression(string(expression))
            .build();
    }

    /**
     * Create a discriminator from the given discriminator type and path.
     *
     * @param type
     *     the discriminator type
     * @param path
     *     the path
     * @return
     *     a discriminator
     */
    public static Discriminator discriminator(DiscriminatorType type, String path) {
        return Discriminator.builder()
            .type(type)
            .path(string(path))
            .build();
    }

    /**
     * Create a slicing from the given discriminator and slicing rules.
     *
     * @param discriminator
     *     the discriminator
     * @param rules
     *     the slicing rules
     * @return
     *     a slicing
     */
    public static Slicing slicing(Discriminator discriminator, SlicingRules rules) {
        return Slicing.builder()
            .discriminator(discriminator)
            .rules(rules)
            .build();
    }

    /**
     * Create a list of profiles (FHIR string) from the provided Java strings.
     *
     * @param profile
     *     the profile
     * @return
     *     a list of profiles (FHIR string)
     */
    public static List<String> profile(String... profile) {
        return Arrays.asList(profile);
    }

    /**
     * Create a list of target profiles (FHIR string) from the provided Java strings.
     *
     * @param targetProfile
     *     the target profile
     * @return
     *     a list of target profiles (FHIR string)
     */
    public static List<String> targetProfile(String...targetProfile) {
        return Arrays.asList(targetProfile);
    }

    /**
     * Create a type from the given code.
     *
     * @param code
     *     the code
     * @return
     *     a type
     */
    public static Type type(String code) {
        return type(code, Collections.emptyList(), Collections.emptyList());
    }

    /**
     * Create a type from the given code and profile.
     *
     * @param code
     *     the code
     * @param profile
     *     the profile
     * @return
     *     a type
     */
    public static Type type(String code, List<String> profile) {
        return type(code, profile, Collections.emptyList());
    }

    /**
     * Create a type from the given code, profile, and target profile.
     *
     * @param code
     *     the code
     * @param profile
     *     the profile
     * @param targetProfile
     *     the target profile
     * @return
     *     a type
     */
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
