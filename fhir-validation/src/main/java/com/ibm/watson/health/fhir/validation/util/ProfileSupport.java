/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.validation.util;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ibm.watson.health.fhir.model.annotation.Constraint;
import com.ibm.watson.health.fhir.model.resource.Resource;
import com.ibm.watson.health.fhir.model.resource.StructureDefinition;
import com.ibm.watson.health.fhir.model.type.Canonical;
import com.ibm.watson.health.fhir.model.type.ElementDefinition;
import com.ibm.watson.health.fhir.model.type.Meta;
import com.ibm.watson.health.fhir.model.type.TypeDerivationRule;
import com.ibm.watson.health.fhir.registry.FHIRRegistry;

/**
 * A utility class used to access / collect constraints within one or more profile differentials
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
    
    public static StructureDefinition getProfile(String url) {
        return FHIRRegistry.getInstance().getResource(url, StructureDefinition.class);
    }
    
    public static List<StructureDefinition> getProfiles(String url) {
        StructureDefinition profile = getProfile(url);        
        List<StructureDefinition> profiles = new ArrayList<>();
        while (profile != null && TypeDerivationRule.CONSTRAINT.equals(profile.getDerivation())) {
            profiles.add(profile);
            if (profile.getBaseDefinition() == null) {
                break;
            }
            profile = FHIRRegistry.getInstance().getResource(profile.getBaseDefinition().getValue(), StructureDefinition.class);
        }
        Collections.reverse(profiles);
        return Collections.unmodifiableList(profiles);
    }
    
    public static List<Constraint> getConstraints(Resource resource) {
        List<Constraint> constraints = new ArrayList<>();
        Meta meta = resource.getMeta();
        if (meta != null) {
            for (Canonical canonical : meta.getProfile()) {
                constraints.addAll(getConstraints(canonical.getValue()));
            }
        }
        return constraints;
    }
    
    public static List<Constraint> getConstraints(String url) {
        return PROFILE_CONSTRAINT_CACHE.computeIfAbsent(url, ProfileSupport::generateConstraints);
    }
    
    private static List<Constraint> generateConstraints(String url) {
        List<Constraint> constraints = new ArrayList<>();
        for (StructureDefinition profile : getProfiles(url)) {
            constraints.addAll(getConstraints(profile));
        }
        return Collections.unmodifiableList(constraints);
    }
    
    private static List<Constraint> getConstraints(StructureDefinition profile) {
        List<Constraint> constraints = new ArrayList<>();
        for (ElementDefinition elementDefinition : profile.getDifferential().getElement()) {
            constraints.addAll(getConstraints(elementDefinition));
        }
        Collections.sort(constraints, CONSTRAINT_COMPARATOR);
        return constraints;
    }

    private static List<Constraint> getConstraints(ElementDefinition elementDefinition) {
        List<Constraint> constraints = new ArrayList<>();
        String path = elementDefinition.getPath().getValue();
        for (ElementDefinition.Constraint constraint : elementDefinition.getConstraint()) {
            constraints.add(createConstraint(path, constraint));
        }
        return constraints;
    }

    private static Constraint createConstraint(String path, ElementDefinition.Constraint constraint) {
        String id = constraint.getKey().getValue();
        String level = "error".equals(constraint.getSeverity().getValue()) ? "Rule" : "Warning";
        String location = path.contains(".") ? path.replace("[x]", "") : "(base)";
        String description = constraint.getHuman().getValue();
        String expression = constraint.getExpression().getValue();
        return createConstraint(id, level, location, description, expression, false);
    }

    public static Constraint createConstraint(String id, String level, String location, String description, String expression, boolean modelChecked) {
        return new Constraint() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Constraint.class;
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
            public String description() {
                return description;
            }

            @Override
            public String expression() {
                return expression;
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
}