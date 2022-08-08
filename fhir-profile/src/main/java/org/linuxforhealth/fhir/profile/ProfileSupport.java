/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.profile;

import static org.linuxforhealth.fhir.cache.CacheKey.key;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.linuxforhealth.fhir.cache.CacheKey;
import org.linuxforhealth.fhir.cache.CacheManager;
import org.linuxforhealth.fhir.cache.CacheManager.Configuration;
import org.linuxforhealth.fhir.model.annotation.Constraint;
import org.linuxforhealth.fhir.model.constraint.spi.ConstraintProvider;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.resource.StructureDefinition;
import org.linuxforhealth.fhir.model.resource.StructureDefinition.Differential;
import org.linuxforhealth.fhir.model.type.Canonical;
import org.linuxforhealth.fhir.model.type.ElementDefinition;
import org.linuxforhealth.fhir.model.type.ElementDefinition.Binding;
import org.linuxforhealth.fhir.model.type.ElementDefinition.Type;
import org.linuxforhealth.fhir.model.type.Meta;
import org.linuxforhealth.fhir.model.type.code.TypeDerivationRule;
import org.linuxforhealth.fhir.model.util.ModelSupport;
import org.linuxforhealth.fhir.profile.constraint.spi.ProfileConstraintProvider;
import org.linuxforhealth.fhir.registry.FHIRRegistry;

public final class ProfileSupport {
    private static final Logger log = Logger.getLogger(ProfileSupport.class.getName());

    public static final String HL7_STRUCTURE_DEFINITION_URL_PREFIX = "http://hl7.org/fhir/StructureDefinition/";
    public static final String HL7_VALUE_SET_URL_PREFIX = "http://hl7.org/fhir/ValueSet/";

    public static final java.lang.String CONSTRAINT_CACHE_NAME = "org.linuxforhealth.fhir.profile.ProfileSupport.constraintCache";
    public static final java.lang.String ELEMENT_DEF_CACHE_NAME = "org.linuxforhealth.fhir.profile.ProfileSupport.elementDefinitionCache";
    public static final java.lang.String BINDING_CACHE_NAME = "org.linuxforhealth.fhir.profile.ProfileSupport.bindingCache";
    public static final Configuration CONSTRAINT_CACHE_CONFIG = Configuration.of(128);
    public static final Configuration ELEMENT_DEF_CACHE_CONFIG = Configuration.of(128);
    public static final Configuration BINDING_CACHE_CONFIG = Configuration.of(128);

    private static final Comparator<Constraint> CONSTRAINT_COMPARATOR = new Comparator<Constraint>() {
        @Override
        public int compare(Constraint first, Constraint second) {
            return first.id().compareTo(second.id());
        }
    };
    private static final List<ProfileConstraintProvider> PROFILE_CONSTRAINT_PROVIDERS = ConstraintProvider.providers(ProfileConstraintProvider.class);

    private ProfileSupport() { }

    private static Map<String, Binding> computeBindingMap(String url) {
        StructureDefinition structureDefinition = getStructureDefinition(url);
        if (structureDefinition != null) {
            Objects.requireNonNull(structureDefinition.getSnapshot(), "StructureDefinition.snapshot element is required");
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

    private static List<Constraint> computeConstraints(StructureDefinition profile, Class<?> type) {
        Objects.requireNonNull(profile.getSnapshot(), "StructureDefinition.snapshot element is required");
        List<Constraint> constraints = new ArrayList<>();
        Set<String> diffKeys = getConstraintKeys(profile.getDifferential());
        for (ElementDefinition elementDefinition : profile.getSnapshot().getElement()) {
            if (elementDefinition.getConstraint().isEmpty() || isSlice(elementDefinition)) {
                continue;
            }
            if (elementDefinition.getId().contains(":") && hasConstraintDifferential(elementDefinition)) {
                log.warning("Slice-specific constraints: " + getConstraintKeyDifferential(elementDefinition) + " found on element: " + elementDefinition.getId() + " are not supported");
                continue;
            }
            String path = elementDefinition.getPath().getValue();
            for (ElementDefinition.Constraint constraint : getConstraintDifferential(elementDefinition)) {
                constraints.add(createConstraint(path, constraint, diffKeys, profile.getUrl().getValue()));
            }
        }
        Collections.sort(constraints, CONSTRAINT_COMPARATOR);
        ConstraintGenerator generator = new ConstraintGenerator(profile);
        constraints.addAll(generator.generate());
        for (ProfileConstraintProvider provider : PROFILE_CONSTRAINT_PROVIDERS) {
            if (provider.appliesTo(getUrl(profile), getVersion(profile))) {
                for (Predicate<Constraint> removalPredicate : provider.getRemovalPredicates()) {
                    constraints.removeIf(removalPredicate);
                }
                constraints.addAll(provider.getConstraints());
            }
        }
        return constraints;
    }

    public static String getUrl(StructureDefinition profile) {
        return (profile.getUrl() != null) ? profile.getUrl().getValue() : null;
    }

    public static String getVersion(StructureDefinition profile) {
        return (profile.getVersion() != null) ? profile.getVersion().getValue() : null;
    }

    public static boolean isSlice(ElementDefinition elementDefinition) {
        return elementDefinition.getSliceName() != null;
    }

    public static boolean isSliceDefinition(ElementDefinition elementDefinition) {
        return elementDefinition.getSlicing() != null;
    }

    public static Set<String> getReferencedProfileConstraintKeys(ElementDefinition elementDefinition) {
        Set<String> profileKeys = new HashSet<>();
        for (Type type : elementDefinition.getType()) {
            for (Canonical canonical : type.getProfile()) {
                String url = canonical.getValue();
                if (url == null) {
                    continue;
                }
                StructureDefinition profile = getProfile(url);
                if (profile == null || profile.getSnapshot() == null) {
                    continue;
                }
                profileKeys.addAll(getConstraintKeys(profile.getSnapshot().getElement().get(0)));
            }
        }
        return profileKeys;
    }

    public static boolean hasConstraintDifferential(ElementDefinition elementDefinition) {
        return !getConstraintKeyDifferential(elementDefinition).isEmpty();
    }

    public static List<ElementDefinition.Constraint> getConstraintDifferential(ElementDefinition elementDefinition) {
        return getConstraints(elementDefinition, getConstraintKeyDifferential(elementDefinition));
    }

    private static Set<String> getConstraintKeyDifferential(ElementDefinition elementDefinition) {
        if (elementDefinition.getConstraint().isEmpty()) {
            return Collections.emptySet();
        }
        Set<String> keys = new HashSet<>(getConstraintKeys(elementDefinition));
        keys.removeAll(getConstraintKeys(getBaseDefinition(elementDefinition)));
        keys.removeAll(getReferencedProfileConstraintKeys(elementDefinition));
        return keys;
    }

    private static List<ElementDefinition.Constraint> getConstraints(ElementDefinition elementDefinition, Set<String> keys) {
        if (keys.isEmpty()) {
            return Collections.emptyList();
        }
        List<ElementDefinition.Constraint> constraints = new ArrayList<>();
        for (ElementDefinition.Constraint constraint : elementDefinition.getConstraint()) {
            if (keys.contains(constraint.getKey().getValue())) {
                constraints.add(constraint);
            }
        }
        return constraints;
    }

    private static ElementDefinition getBaseDefinition(ElementDefinition elementDefinition) {
        String basePath = elementDefinition.getBase().getPath().getValue();
        return getElementDefinition(basePath);
    }

    private static Map<String, ElementDefinition> computeElementDefinitionMap(String url) {
        StructureDefinition structureDefinition = getStructureDefinition(url);
        if (structureDefinition != null) {
            Objects.requireNonNull(structureDefinition.getSnapshot(), "StructureDefinition.snapshot element is required");
            Map<String, ElementDefinition> elementDefinitionMap = new LinkedHashMap<>();
            for (ElementDefinition elementDefinition : structureDefinition.getSnapshot().getElement()) {
                String path = elementDefinition.getPath().getValue();
                elementDefinitionMap.put(path, elementDefinition);
            }
            return Collections.unmodifiableMap(elementDefinitionMap);
        }
        return Collections.emptyMap();
    }

    private static Constraint createConstraint(String path, ElementDefinition.Constraint constraint, Set<String> diffKeys, String url) {
        String id = constraint.getKey().getValue();
        String level = "error".equals(constraint.getSeverity().getValue()) ? Constraint.LEVEL_RULE : Constraint.LEVEL_WARNING;
        String location = path.contains(".") ? path.replace(".div", ".`div`").replace("[x]", "") : Constraint.LOCATION_BASE;
        String description = constraint.getHuman().getValue();
        String expression = constraint.getExpression().getValue();
        String source = (constraint.getSource() != null) ? constraint.getSource().getValue() : (diffKeys.contains(constraint.getKey().getValue()) ? url : Constraint.SOURCE_UNKNOWN);
        return Constraint.Factory.createConstraint(id, level, location, description, expression, source, false, false);
    }

    public static Binding getBinding(String path) {
        String url = getUrl(path);
        Map<String, Binding> bindingMap = getBindingMap(url);
        return bindingMap.get(path);
    }

    public static Map<String, Binding> getBindingMap(String url) {
        Map<String, Map<String, Binding>> bindingMapCache = CacheManager.getCacheAsMap(BINDING_CACHE_NAME, BINDING_CACHE_CONFIG);
        try {
            return bindingMapCache.computeIfAbsent(url, ProfileSupport::computeBindingMap);
        } finally {
            CacheManager.reportCacheStats(log, BINDING_CACHE_NAME);
        }
    }

    public static List<Constraint> getConstraints(List<String> urls, Class<?> type) {
        List<Constraint> constraints = new ArrayList<>();
        for (String url : urls) {
            constraints.addAll(getConstraints(url, type));
        }
        return constraints;
    }

    public static List<Constraint> getConstraints(Resource resource) {
        return getConstraints(getResourceAssertedProfiles(resource), resource.getClass());
    }

    public static List<String> getResourceAssertedProfiles(Resource resource) {
        Meta meta = resource.getMeta();
        if (meta != null) {
            return meta.getProfile().stream()
                    .map(profile -> profile.getValue())
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public static boolean hasResourceAssertedProfile(Resource resource, StructureDefinition profile) {
        Meta meta = resource.getMeta();
        if (meta != null) {
            for (Canonical canonical : meta.getProfile()) {
                String value = canonical.getValue();

                if (value == null) {
                    continue;
                }

                String uri = value;
                String version = null;

                int index = value.indexOf("|");
                if (index != -1) {
                    uri = value.substring(0, index);
                    version = value.substring(index + 1);
                }

                if (uri.equals(profile.getUrl().getValue()) &&
                        (version == null || profile.getVersion() == null || version.equals(profile.getVersion().getValue()))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<Constraint> getConstraints(String url, Class<?> type) {
        StructureDefinition profile = getProfile(url, type);
        if (profile != null) {
            return getConstraints(profile, type);
        }
        return Collections.emptyList();
    }

    private static List<Constraint> getConstraints(StructureDefinition profile, Class<?> type) {
        String url = profile.getUrl().getValue();
        String version = profile.getVersion().getValue();
        CacheKey key = key(url + "|" + version);
        Map<CacheKey, List<Constraint>> constraintCache = CacheManager.getCacheAsMap(CONSTRAINT_CACHE_NAME, CONSTRAINT_CACHE_CONFIG);

        try {
            return constraintCache.computeIfAbsent(key, k -> computeConstraints(profile, type));
        } finally {
            CacheManager.reportCacheStats(log, CONSTRAINT_CACHE_NAME);
        }
    }

    public static ElementDefinition getElementDefinition(String path) {
        String url = getUrl(path);
        Map<String, ElementDefinition> elementDefinitionMap = getElementDefinitionMap(url);
        return elementDefinitionMap.get(path);
    }

    public static Map<String, ElementDefinition> getElementDefinitionMap(Class<?> type) {
        return getElementDefinitionMap(HL7_STRUCTURE_DEFINITION_URL_PREFIX + ModelSupport.getTypeName(type));
    }

    public static Map<String, ElementDefinition> getElementDefinitionMap(String url) {
        Map<String, Map<String, ElementDefinition>> elementDefinitionMapCache = CacheManager.getCacheAsMap(ELEMENT_DEF_CACHE_NAME, ELEMENT_DEF_CACHE_CONFIG);
        try {
            return elementDefinitionMapCache.computeIfAbsent(url, ProfileSupport::computeElementDefinitionMap);
        } finally {
            CacheManager.reportCacheStats(log, ELEMENT_DEF_CACHE_NAME);
        }
    }

    public static Set<String> getConstraintKeys(StructureDefinition structureDefinition) {
        Set<String> keys = new HashSet<>();
        Objects.requireNonNull(structureDefinition.getSnapshot(), "StructureDefinition.snapshot element is required");
        for (ElementDefinition elementDefinition : structureDefinition.getSnapshot().getElement()) {
            keys.addAll(getConstraintKeys(elementDefinition));
        }
        return keys;
    }

    public static Set<String> getConstraintKeys(Differential differential) {
        if (differential == null) {
            return Collections.emptySet();
        }
        Set<String> constraintKeys = new HashSet<>();
        for (ElementDefinition elementDefinition : differential.getElement()) {
            for (ElementDefinition.Constraint constraint : elementDefinition.getConstraint()) {
                constraintKeys.add(constraint.getKey().getValue());
            }
        }
        return constraintKeys;
    }

    public static Set<String> getConstraintKeys(ElementDefinition elementDefinition) {
        Set<String> keys = new HashSet<>();
        for (ElementDefinition.Constraint constraint : elementDefinition.getConstraint()) {
            keys.add(constraint.getKey().getValue());
        }
        return keys;
    }

    public static StructureDefinition getProfile(String url) {
        StructureDefinition structureDefinition = getStructureDefinition(url);
        return isProfile(structureDefinition) ? structureDefinition : null;
    }

    public static StructureDefinition getProfile(String url, Class<?> type) {
        StructureDefinition profile = getProfile(url);
        return (profile != null && isApplicable(profile, type)) ? profile : null;
    }

    public static StructureDefinition getStructureDefinition(Class<?> modelClass) {
        return getStructureDefinition(HL7_STRUCTURE_DEFINITION_URL_PREFIX + ModelSupport.getTypeName(modelClass));
    }

    public static StructureDefinition getStructureDefinition(String url) {
        return FHIRRegistry.getInstance().getResource(url, StructureDefinition.class);
    }

    private static String getUrl(String path) {
        int index = path.indexOf(".");
        String typeName = (index != -1) ? path.substring(0, index) : path;
        return HL7_STRUCTURE_DEFINITION_URL_PREFIX + typeName;
    }

    public static boolean isApplicable(StructureDefinition profile, Class<?> type) {
        if (profile == null || type == null) {
            return false;
        }
        return isApplicable(profile, ModelSupport.getTypeNames(type));
    }

    private static boolean isApplicable(StructureDefinition profile, Set<String> typeNames) {
        String type = profile.getType().getValue();
        return typeNames.contains(type.substring(type.lastIndexOf("/") + 1));
    }

    public static boolean isProfile(StructureDefinition structureDefinition) {
        return structureDefinition != null && TypeDerivationRule.CONSTRAINT.equals(structureDefinition.getDerivation());
    }
}