/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.validation.util;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.ElementDefinition;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.code.TypeDerivationRule;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.registry.FHIRRegistry;

/**
 * A utility class used to access and generate constraints from a profile differential
 */
public final class ProfileSupport {
    private static final Map<String, List<Constraint>> PROFILE_CONSTRAINT_CACHE = new ConcurrentHashMap<>();
    private static final Comparator<Constraint> CONSTRAINT_COMPARATOR = new Comparator<Constraint>() {
        @Override
        public int compare(Constraint first, Constraint second) {
            return first.id().compareTo(second.id());
        }
    };

    private ProfileSupport() { }

    public static List<Constraint> getConstraints(List<String> urls, Class<?> resourceType) {
        List<Constraint> constraints = new ArrayList<>();
        for (String url : urls) {
            StructureDefinition structureDefinition = getStructureDefinition(url);
            List<StructureDefinition> profiles = getProfiles(structureDefinition, resourceType);
            for (StructureDefinition profile : profiles) {
                constraints.addAll(getConstraints(profile));
            }
        }
        return constraints;
    }

    public static List<Constraint> getConstraints(Resource resource) {
        List<Constraint> constraints = new ArrayList<>();
        Meta meta = resource.getMeta();
        if (meta != null) {
            List<String> urls = new ArrayList<>();
            for (Canonical canonical : meta.getProfile()) {
                if (canonical.getValue() != null) {
                    urls.add(canonical.getValue());
                }
            }
            return getConstraints(urls, resource.getClass());
        }
        return constraints;
    }

    public static List<Constraint> getConstraints(String url, Class<?> resourceType) {
        return getConstraints(Collections.singletonList(url), resourceType);
    }

    public static StructureDefinition getProfile(String url) {
        StructureDefinition structureDefinition = getStructureDefinition(url);
        return isProfile(structureDefinition) ? structureDefinition : null;
    }

    public static List<StructureDefinition> getProfiles(StructureDefinition structureDefinition, Class<?> resourceType) {
        List<StructureDefinition> profiles = new ArrayList<>();
        Set<String> typeNames = ModelSupport.getTypeNames(resourceType);
        while (isProfile(structureDefinition) && isApplicable(structureDefinition, typeNames)) {
            profiles.add(structureDefinition);
            if (!hasBaseDefinition(structureDefinition)) {
                break;
            }
            structureDefinition = getStructureDefinition(structureDefinition.getBaseDefinition().getValue());
        }
        Collections.reverse(profiles);
        return Collections.unmodifiableList(profiles);
    }

    public static boolean isProfile(StructureDefinition structureDefinition) {
        return structureDefinition != null && TypeDerivationRule.CONSTRAINT.equals(structureDefinition.getDerivation());
    }

    public static Constraint createConstraint(String id, String level, String location, String description, String expression, boolean modelChecked) {
        return new Constraint() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Constraint.class;
            }
    
            @Override
            public String description() {
                return description;
            }
    
            @Override
            public String expression() {
                return expression;
            }
    
            @Override
            public String id() {
                return id;
            }
    
            @Override
            public String level() {
                return level;
            }
    
            @Override
            public String location() {
                return location;
            }
    
            @Override
            public boolean modelChecked() {
                return modelChecked;
            }
    
            @Override
            public String toString() {
                return new StringBuilder()
                    .append("Constraint [")
                    .append("id=").append(id).append(", ")
                    .append("level=").append(level).append(", ")
                    .append("location=").append(location).append(", ")
                    .append("description=").append(description).append(", ")
                    .append("expression=").append(expression).append(", ")
                    .append("modelChecked=").append(modelChecked)
                    .append("]")
                    .toString();
            }
        };
    }

    private static Constraint createConstraint(String path, ElementDefinition.Constraint constraint) {
        String id = constraint.getKey().getValue();
        String level = "error".equals(constraint.getSeverity().getValue()) ? Constraint.LEVEL_RULE : Constraint.LEVEL_WARNING;
        String location = path.contains(".") ? path.replace("[x]", "") : Constraint.LOCATION_BASE;
        String description = constraint.getHuman().getValue();
        String expression = constraint.getExpression().getValue();
        return createConstraint(id, level, location, description, expression, false);
    }

    private static List<Constraint> generate(ElementDefinition elementDefinition) {
        List<Constraint> constraints = new ArrayList<>();
        String path = elementDefinition.getPath().getValue();
        for (ElementDefinition.Constraint constraint : elementDefinition.getConstraint()) {
            constraints.add(createConstraint(path, constraint));
        }
        return constraints;
    }
    
    private static List<Constraint> generate(StructureDefinition profile) {
        List<Constraint> constraints = new ArrayList<>();
        for (ElementDefinition elementDefinition : profile.getDifferential().getElement()) {
            constraints.addAll(generate(elementDefinition));
        }
        Collections.sort(constraints, CONSTRAINT_COMPARATOR);
        return constraints;
    }
    
    private static List<Constraint> getConstraints(StructureDefinition profile) {
        String url = profile.getUrl().getValue();
        List<Constraint> constraints = PROFILE_CONSTRAINT_CACHE.get(url);
        if (constraints == null) {
            constraints = PROFILE_CONSTRAINT_CACHE.computeIfAbsent(url, key -> generate(profile));
        }
        return constraints;
    }
    
    private static StructureDefinition getStructureDefinition(String url) {
        Resource resource = FHIRRegistry.getInstance().getResource(url, Resource.class);
        if (resource instanceof StructureDefinition) {
            return (StructureDefinition) resource;
        }
        return null;
    }

    private static boolean hasBaseDefinition(StructureDefinition structureDefinition) {
        return structureDefinition.getBaseDefinition() != null && structureDefinition.getBaseDefinition().getValue() != null;
    }

    private static boolean isApplicable(StructureDefinition profile, Set<String> typeNames) {
        String type = profile.getType().getValue();
        return typeNames.contains(type.substring(type.lastIndexOf("/") + 1));
    }
}