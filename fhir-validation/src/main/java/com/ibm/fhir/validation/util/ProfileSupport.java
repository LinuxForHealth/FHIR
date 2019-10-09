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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.ElementDefinition;
import com.ibm.fhir.model.type.ElementDefinition.Binding;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.TypeDerivationRule;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.registry.FHIRRegistry;

public final class ProfileSupport {
    public static final String STRUCTURE_DEFINITION_URL_PREFIX = "http://hl7.org/fhir/StructureDefinition/";

    private static final Map<String, List<Constraint>> CONSTRAINT_CACHE = new ConcurrentHashMap<>();
    private static final Map<String, Map<String, ElementDefinition>> ELEMENT_DEFINITION_CACHE = new ConcurrentHashMap<>();
    private static final Map<String, Map<String, Binding>> BINDING_CACHE = new ConcurrentHashMap<>();
    private static final Comparator<Constraint> CONSTRAINT_COMPARATOR = new Comparator<Constraint>() {
        @Override
        public int compare(Constraint first, Constraint second) {
            return first.id().compareTo(second.id());
        }
    };

    private ProfileSupport() { }

    private static Map<String, Binding> computeBindingMap(String url) {
        StructureDefinition structureDefinition = getStructureDefinition(url);
        if (structureDefinition != null) {
            Map<String, Binding> bindingMap = new LinkedHashMap<>();
            for (ElementDefinition elementDefinition : structureDefinition.getSnapshot().getElement()) {
                String path = elementDefinition.getPath().getValue();
                Binding binding = elementDefinition.getBinding();
                if (binding != null) {
                    bindingMap.put(path, binding);
                }
            }
            return Collections.unmodifiableMap(bindingMap);
        }
        return Collections.emptyMap();
    }

    private static List<Constraint> computeConstraints(StructureDefinition profile, Class<?> resourceType) {
        List<Constraint> constraints = new ArrayList<>();
        Set<String> difference = new HashSet<>(getKeys(profile));
        difference.removeAll(getKeys(getStructureDefinition(resourceType)));
        for (ElementDefinition elementDefinition : profile.getSnapshot().getElement()) {
            if (elementDefinition.getConstraint().isEmpty()) {
                continue;
            }
            String path = elementDefinition.getPath().getValue();
            for (ElementDefinition.Constraint constraint : elementDefinition.getConstraint()) {
                if (difference.contains(constraint.getKey().getValue())) {
                    constraints.add(createConstraint(path, constraint));
                }
            }
        }
        Collections.sort(constraints, CONSTRAINT_COMPARATOR);
        return constraints;
    }

    private static Map<String, ElementDefinition> computeElementDefinitionMap(String url) {
        StructureDefinition structureDefinition = getStructureDefinition(url);
        if (structureDefinition != null) {
            Map<String, ElementDefinition> elementDefinitionMap = new LinkedHashMap<>();
            for (ElementDefinition elementDefinition : structureDefinition.getSnapshot().getElement()) {
                String path = elementDefinition.getPath().getValue();
                elementDefinitionMap.put(path, elementDefinition);
            }
            return Collections.unmodifiableMap(elementDefinitionMap);
        }
        return Collections.emptyMap();
    }

    private static Constraint createConstraint(String path, ElementDefinition.Constraint constraint) {
        String id = constraint.getKey().getValue();
        String level = "error".equals(constraint.getSeverity().getValue()) ? Constraint.LEVEL_RULE : Constraint.LEVEL_WARNING;
        String location = path.contains(".") ? path.replace("[x]", "") : Constraint.LOCATION_BASE;
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

    public static Binding getBinding(String path) {
        String url = getUrl(path);
        Map<String, Binding> bindingMap = getBindingMap(url);
        return bindingMap.get(path);
    }

    public static Map<String, Binding> getBindingMap(String url) {
        Map<String, Binding> bindingMap = BINDING_CACHE.get(url);
        if (bindingMap == null) {
            bindingMap = BINDING_CACHE.computeIfAbsent(url, ProfileSupport::computeBindingMap);
        }
        return bindingMap;
    }

    public static List<Constraint> getConstraints(List<String> urls, Class<?> resourceType) {
        List<Constraint> constraints = new ArrayList<>();
        for (String url : urls) {
            StructureDefinition profile = getProfile(url, resourceType);
            if (profile != null) {
                constraints.addAll(getConstraints(profile, resourceType));
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

    private static List<Constraint> getConstraints(StructureDefinition profile, Class<?> resourceType) {
        String url = profile.getUrl().getValue();
        List<Constraint> constraints = CONSTRAINT_CACHE.get(url);
        if (constraints == null) {
            constraints = CONSTRAINT_CACHE.computeIfAbsent(url, key -> computeConstraints(profile, resourceType));
        }
        return constraints;
    }

    public static ElementDefinition getElementDefinition(String path) {
        String url = getUrl(path);
        Map<String, ElementDefinition> elementDefinitionMap = getElementDefinitionMap(url);
        return elementDefinitionMap.get(path);
    }
    
    public static Map<String, ElementDefinition> getElementDefinitionMap(Class<?> modelClass) {
        return getElementDefinitionMap(STRUCTURE_DEFINITION_URL_PREFIX + ModelSupport.getTypeName(modelClass));
    }
    
    public static Map<String, ElementDefinition> getElementDefinitionMap(String url) {
        Map<String, ElementDefinition> elementDefinitionMap = ELEMENT_DEFINITION_CACHE.get(url);
        if (elementDefinitionMap == null) {
            elementDefinitionMap = ELEMENT_DEFINITION_CACHE.computeIfAbsent(url, ProfileSupport::computeElementDefinitionMap);
        }
        return elementDefinitionMap;
    }
    
    private static Set<String> getKeys(StructureDefinition structureDefinition) {
        Set<String> keys = new HashSet<>();
        for (ElementDefinition elementDefinition : structureDefinition.getSnapshot().getElement()) {
            for (ElementDefinition.Constraint constraint : elementDefinition.getConstraint()) {
                keys.add(constraint.getKey().getValue());
            }
        }
        return keys;
    }
    
    public static StructureDefinition getProfile(String url) {
        StructureDefinition structureDefinition = getStructureDefinition(url);
        return isProfile(structureDefinition) ? structureDefinition : null;
    }
    
    public static StructureDefinition getProfile(String url, Class<?> resourceType) {
        StructureDefinition profile = getProfile(url);
        return (profile != null && isApplicable(profile, resourceType)) ? profile : null;
    }

    private static StructureDefinition getStructureDefinition(Class<?> modelClass) {
        return getStructureDefinition(STRUCTURE_DEFINITION_URL_PREFIX + ModelSupport.getTypeName(modelClass));
    }
    
    public static StructureDefinition getStructureDefinition(String url) {
        Resource resource = FHIRRegistry.getInstance().getResource(url, Resource.class);
        if (resource instanceof StructureDefinition) {
            return (StructureDefinition) resource;
        }
        return null;
    }
    
    private static String getUrl(String path) {
        int index = path.indexOf(".");
        String typeName = (index != -1) ? path.substring(0, index) : path;
        return STRUCTURE_DEFINITION_URL_PREFIX + typeName;
    }
    
    public static boolean isApplicable(StructureDefinition profile, Class<?> resourceType) {
        return isApplicable(profile, ModelSupport.getTypeNames(resourceType));
    }
    
    private static boolean isApplicable(StructureDefinition profile, Set<String> typeNames) {
        String type = profile.getType().getValue();
        return typeNames.contains(type.substring(type.lastIndexOf("/") + 1));
    }
    
    public static boolean isProfile(StructureDefinition structureDefinition) {
        return structureDefinition != null && TypeDerivationRule.CONSTRAINT.equals(structureDefinition.getDerivation());
    }
}