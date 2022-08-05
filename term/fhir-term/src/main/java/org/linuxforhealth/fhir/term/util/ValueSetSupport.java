/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.term.util;

import static org.linuxforhealth.fhir.model.type.String.string;
import static org.linuxforhealth.fhir.term.util.CodeSystemSupport.getCodeSystem;
import static org.linuxforhealth.fhir.term.util.CodeSystemSupport.isCaseSensitive;
import static org.linuxforhealth.fhir.term.util.CodeSystemSupport.normalize;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.linuxforhealth.fhir.cache.CacheManager;
import org.linuxforhealth.fhir.cache.CacheManager.Configuration;
import org.linuxforhealth.fhir.model.resource.CodeSystem;
import org.linuxforhealth.fhir.model.resource.CodeSystem.Concept;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.resource.ValueSet;
import org.linuxforhealth.fhir.model.resource.ValueSet.Compose;
import org.linuxforhealth.fhir.model.resource.ValueSet.Compose.Include;
import org.linuxforhealth.fhir.model.resource.ValueSet.Expansion;
import org.linuxforhealth.fhir.model.type.Canonical;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.Coding;
import org.linuxforhealth.fhir.model.type.DateTime;
import org.linuxforhealth.fhir.model.type.Integer;
import org.linuxforhealth.fhir.model.type.String;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.registry.FHIRRegistry;
import org.linuxforhealth.fhir.registry.resource.FHIRRegistryResource.Version;
import org.linuxforhealth.fhir.term.config.FHIRTermConfig;
import org.linuxforhealth.fhir.term.service.FHIRTermService;
import org.linuxforhealth.fhir.term.service.exception.FHIRTermServiceException;

/**
 * A utility class for expanding FHIR value sets
 */
public final class ValueSetSupport {
    public static final java.lang.String CODE_SET_MAP_CACHE_NAME = "org.linuxforhealth.fhir.term.util.ValueSetSupport.codeSetMapCache";
    public static final Configuration CODE_SET_MAP_CACHE_CONFIG = Configuration.of(1024);

    private static final Logger log = Logger.getLogger(ValueSetSupport.class.getName());
    private static final java.lang.String VERSION_UNKNOWN = "<version unknown>";

    private ValueSetSupport() { }

    /**
     * Expand the given value set per the algorithm here: http://hl7.org/fhir/valueset.html#expansion
     *
     * @param valueSet
     *     the value set to be expanded
     * @return
     *     the expanded value set, or the original value set if already expanded or unable to expand
     */
    public static ValueSet expand(ValueSet valueSet) {
        if (!isExpanded(valueSet) && isExpandable(valueSet)) {
            Set<Contains> result = expand(valueSet.getCompose());
            return valueSet.toBuilder()
                .expansion(Expansion.builder()
                    .total(Integer.of(result.size()))
                    .timestamp(DateTime.now(ZoneOffset.UTC))
                    .contains(unwrap(result))
                    .build())
                .build();
        }
        return valueSet;
    }

    /**
     * Get the code set map for the given value set.
     *
     * @param valueSet
     *     the value set
     * @return
     *     the code set map for the given value set, or an empty map if no such code set map exists
     */
    public static Map<java.lang.String, Set<java.lang.String>> getCodeSetMap(ValueSet valueSet) {
        if (valueSet.getUrl() == null || valueSet.getVersion() == null || FHIRTermConfig.isCachingDisabled()) {
            return computeCodeSetMap(valueSet);
        }
        java.lang.String url = cacheKey(valueSet);
        Map<java.lang.String, Map<java.lang.String, Set<java.lang.String>>> cacheAsMap =
                CacheManager.getCacheAsMap(CODE_SET_MAP_CACHE_NAME, CODE_SET_MAP_CACHE_CONFIG);
        try {
            return cacheAsMap.computeIfAbsent(url, k -> computeCodeSetMap(valueSet));
        } finally {
            CacheManager.reportCacheStats(log, CODE_SET_MAP_CACHE_NAME);
        }
    }

    /**
     * Clear the code set map cache for the given value set.
     *
     * @param valueSet
     *     the value set
     */
    public static void clearCache(ValueSet valueSet) {
        CacheManager.invalidate(CODE_SET_MAP_CACHE_NAME, cacheKey(valueSet));
    }

    /**
     * Compute the code set map cache key for the given value set.
     *
     * @param valueSet
     *     the value set
     *
     * @return
     *     computed cache key
     */
    private static java.lang.String cacheKey(ValueSet valueSet) {
        return valueSet.getUrl().getValue() + "|" + valueSet.getVersion().getValue();
    }

    /**
     * Get a list containing {@link ValueSet.Expansion.Contains} instances where all structural
     * hierarchies have been flattened.
     *
     * @param expansion
     *     the expansion containing the list of Contains instances to flatten
     * @return
     *     flattened list of Contains instances for the given expansion
     */
    public static List<Expansion.Contains> getContains(Expansion expansion) {
        if (expansion == null) {
            return Collections.emptyList();
        }
        List<Expansion.Contains> result = (expansion.getTotal() != null) ? new ArrayList<>(expansion.getTotal().getValue()) : new ArrayList<>();
        for (Expansion.Contains contains : expansion.getContains()) {
            result.addAll(getContains(contains));
        }
        return result;
    }

    /**
     * Get the value set associated with the given url from the FHIR registry.
     *
     * @param url
     *     the url of the value set (with optional version postfix and optional fragment id for contained resources)
     * @return
     *     the value set associated with the given input parameter, or null if no such value set exists
     */
    public static ValueSet getValueSet(java.lang.String url) {
        return FHIRRegistry.getInstance().getResource(url, ValueSet.class);
    }

    public static boolean isExpandable(ValueSet valueSet) {
        if (valueSet == null || valueSet.getCompose() == null) {
            return false;
        }

        Compose compose = valueSet.getCompose();

        List<Include> includesAndExcludes = new ArrayList<>(compose.getInclude());
        includesAndExcludes.addAll(compose.getExclude());

        for (java.lang.String codeSystemReference : getCodeSystemReferences(includesAndExcludes)) {
            if (!hasResource(codeSystemReference, CodeSystem.class)) {
                return false;
            }
            CodeSystem codeSystem = getCodeSystem(codeSystemReference);
            if (!FHIRTermService.getInstance().isSupported(codeSystem)) {
                return false;
            }
        }

        for (java.lang.String valueSetReference : getValueSetReferences(includesAndExcludes)) {
            if (!hasResource(valueSetReference, ValueSet.class)) {
                return false;
            }
            ValueSet vs = getValueSet(valueSetReference);
            if (!isExpanded(vs) && !isExpandable(vs)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isExpanded(ValueSet valueSet) {
        return valueSet != null && valueSet.getExpansion() != null;
    }

    public static boolean validateCode(ValueSet valueSet, Code code) {
        return validateCode(getCodeSetMap(valueSet), code);
    }

    public static boolean validateCode(ValueSet valueSet, Coding coding) {
        return validateCode(getCodeSetMap(valueSet), coding);
    }

    private static Contains buildContains(Uri system, String version, Code code, String display) {
        return wrap(Expansion.Contains.builder()
            .system(system)
            .version(version)
            .code(code)
            .display(display)
            .build());
    }

    private static Contains buildContains(Uri system, String version, Concept concept) {
        Code code = (concept.getCode() != null) ? concept.getCode() : null;
        if (code != null) {
            return buildContains(system, version, code, concept.getDisplay());
        }
        return null;
    }

    private static Map<java.lang.String, Set<java.lang.String>> computeCodeSetMap(ValueSet valueSet) {
        try {
            ValueSet expanded = expand(valueSet);
            if (expanded == null || expanded.getExpansion() == null) {
                return Collections.emptyMap();
            }
            Map<java.lang.String, Set<java.lang.String>> codeSetMap = new LinkedHashMap<>();
            Expansion expansion = expanded.getExpansion();
            for (Expansion.Contains contains : getContains(expansion)) {
                java.lang.String system = (contains.getSystem() != null) ? contains.getSystem().getValue() : null;
                java.lang.String version = (contains.getVersion() != null && contains.getVersion().getValue() != null) ? contains.getVersion().getValue() : VERSION_UNKNOWN;
                java.lang.String code = (contains.getCode() != null) ? contains.getCode().getValue() : null;
                if (system != null && code != null) {
                    java.lang.String url = !VERSION_UNKNOWN.equals(version) ? system + "|" + version : system;
                    if (!isCaseSensitive(url)) {
                        code = normalize(code);
                    }
                    codeSetMap.computeIfAbsent(system + "|" + version, k -> new LinkedHashSet<>()).add(code);
                }
            }
            return codeSetMap;
        } catch (FHIRTermServiceException e) {
            throw e;
        } catch (Exception e) {
            java.lang.String url = (valueSet.getUrl() != null) ? valueSet.getUrl().getValue() : "<no url>";
            java.lang.String version = (valueSet.getVersion() != null) ? valueSet.getVersion().getValue() : "<no version>";
            log.log(Level.WARNING, java.lang.String.format("Unable to expand value set with url: %s and version: %s", url, version), e);
        }
        return Collections.emptyMap();
    }

    private static Set<Contains> expand(Compose compose) {
        if (compose == null) {
            return Collections.emptySet();
        }

        Set<Contains> result = new LinkedHashSet<>();

        Set<Contains> included = new LinkedHashSet<>();
        for (Include include : compose.getInclude()) {
            included.addAll(expand(include));
        }

        Set<Contains> excluded = new LinkedHashSet<>();
        for (Include exclude : compose.getExclude()) {
            excluded.addAll(expand(exclude));
        }

        Set<Contains> difference = new LinkedHashSet<>(included);
        difference.removeAll(excluded);
        result.addAll(difference);

        return result;
    }

    private static Set<Contains> expand(Include includeOrExclude) {
        if (includeOrExclude == null) {
            return Collections.emptySet();
        }

        Set<Contains> codeSystemContains = new LinkedHashSet<>();
        if (includeOrExclude.getSystem() != null) {
            Uri system = includeOrExclude.getSystem();
            String version = (includeOrExclude.getVersion() != null) ?
                    includeOrExclude.getVersion() : getDefaultVersion(system);
            if (!includeOrExclude.getConcept().isEmpty()) {
                for (Include.Concept concept : includeOrExclude.getConcept()) {
                    Code code = (concept.getCode() != null) ? concept.getCode() : null;
                    if (code != null) {
                        codeSystemContains.add(buildContains(system, version, code, concept.getDisplay()));
                    }
                }
            } else {
                java.lang.String url = system.getValue();
                if (version != null) {
                    url = url + "|" + version.getValue();
                }
                CodeSystem codeSystem = getCodeSystem(url);
                if (codeSystem != null) {
                    for (Concept concept : FHIRTermService.getInstance().getConcepts(codeSystem, includeOrExclude.getFilter())) {
                        Contains contains = buildContains(system, version, concept);
                        if (contains != null) {
                            codeSystemContains.add(contains);
                        }
                    }
                }
            }
        }

        Set<Contains> valueSetContains = new LinkedHashSet<>();
        for (Canonical valueSet : includeOrExclude.getValueSet()) {
            java.lang.String url = valueSet.getValue();
            if (hasResource(url, ValueSet.class)) {
                valueSetContains.addAll(wrap(getContains(expand(getValueSet(url)).getExpansion())));
            }
        }

        if (!codeSystemContains.isEmpty() && !valueSetContains.isEmpty()) {
            Set<Contains> intersection = new LinkedHashSet<>(codeSystemContains);
            intersection.retainAll(valueSetContains);
            return intersection;
        }

        return !codeSystemContains.isEmpty() ? codeSystemContains : valueSetContains;
    }

    private static java.lang.String getCodeSystemReference(Include includeOrExclude) {
        if (includeOrExclude.getSystem() != null && includeOrExclude.getSystem().getValue() != null) {
            StringBuilder sb = new StringBuilder(includeOrExclude.getSystem().getValue());
            if (includeOrExclude.getVersion() != null && includeOrExclude.getVersion().getValue() != null) {
                sb.append("|").append(includeOrExclude.getVersion().getValue());
            }
            return sb.toString();
        }
        return null;
    }

    private static Set<java.lang.String> getCodeSystemReferences(List<Include> includesAndExcludes) {
        Set<java.lang.String> codeSystemReferences = new LinkedHashSet<>();
        for (Include includeOrExclude : includesAndExcludes) {
            if (includeOrExclude.getConcept().isEmpty()) {
                java.lang.String codeSystemReference = getCodeSystemReference(includeOrExclude);
                if (codeSystemReference != null) {
                    codeSystemReferences.add(codeSystemReference);
                }
            }
        }
        return codeSystemReferences;
    }

    private static List<Expansion.Contains> getContains(Expansion.Contains contains) {
        if (contains == null) {
            return Collections.emptyList();
        }
        List<Expansion.Contains> result = new ArrayList<>();
        result.add(contains);
        for (Expansion.Contains c : contains.getContains()) {
            result.addAll(getContains(c));
        }
        return result;
    }

    private static String getDefaultVersion(Uri system) {
        java.lang.String version = FHIRRegistry.getInstance().getDefaultVersion(system.getValue(), CodeSystem.class);
        return (version != null && !Version.NO_VERSION.toString().equals(version)) ? string(version) : null;
    }

    private static Set<java.lang.String> getValueSetReferences(List<Include> includesAndExcludes) {
        Set<java.lang.String> valueSetReferences = new LinkedHashSet<>();
        for (Include includeOrExclude : includesAndExcludes) {
            for (Canonical canonical : includeOrExclude.getValueSet()) {
                if (canonical.getValue() != null) {
                    valueSetReferences.add(canonical.getValue());
                }
            }
        }
        return valueSetReferences;
    }

    private static boolean hasResource(java.lang.String url, Class<? extends Resource> resourceType) {
        return FHIRRegistry.getInstance().hasResource(url, resourceType);
    }

    private static List<Expansion.Contains> unwrap(Collection<Contains> wrapped) {
        List<Expansion.Contains> unwrapped = new ArrayList<>(wrapped.size());
        for (Contains contains : wrapped) {
            unwrapped.add(unwrap(contains));
        }
        return unwrapped;
    }

    private static Expansion.Contains unwrap(Contains contains) {
        return contains.getContains();
    }

    private static boolean validateCode(Map<java.lang.String, Set<java.lang.String>> codeSetMap, Code code) {
        java.lang.String codeValue = (code != null) ? code.getValue() : null;
        if (codeValue != null) {
            java.lang.String normalizedCodeValue = normalize(codeValue);
            for (Set<java.lang.String> codeSet : codeSetMap.values()) {
                if (codeSet.contains(codeValue) || codeSet.contains(normalizedCodeValue)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean validateCode(Map<java.lang.String, Set<java.lang.String>> codeSetMap, Coding coding) {
        java.lang.String system = (coding.getSystem() != null) ? coding.getSystem().getValue() : null;
        java.lang.String version = (coding.getVersion() != null) ? coding.getVersion().getValue() : null;
        java.lang.String code = (coding.getCode() != null) ? coding.getCode().getValue() : null;
        return validateCode(codeSetMap, system, version, code);
    }

    /**
     * Determine whether the provided code is in the codeSet associated with the provided system and version.
     *
     * <p>If the system or code is null, return false. If the version is non-null, it is concatenated with the
     * system to form a key into the codeSetMap. If not found, then the system is concatenated with the
     * "VERSION_UNKNOWN" constant (in cases where the expanded value set did not have a version available during the
     * expansion). If only the system is non-null, then the codeSetMap keys are checked for startsWith(system).
     *
     * <p>If the system and version are non-null, then they are concatenated to form a key into the codeSetMap. If
     * not found, then the system is concatenated with the "VERSION_UNKNOWN" constant (in cases where the expanded
     * value set did not have a version available during the expansion). If only the system is non-null, then the
     * codeSetMap keys are checked for startsWith(system). Finally, if both system and version are null, map keys
     * are ignored and the values of the map are checked directly.
     *
     * @param codeSetMap
     *     the code set map
     * @param system
     *     the system of the focal coded element
     * @param version
     *     the version of the focal coded element (can be null)
     * @param code
     *     the code used in the membership check
     * @return
     *     true if a codeSet is found and the provided code is a member of that codeSet, false otherwise
     */
    private static boolean validateCode(Map<java.lang.String, Set<java.lang.String>> codeSetMap, java.lang.String system, java.lang.String version, java.lang.String code) {
        if (system == null || code == null) {
            return false;
        }
        java.lang.String url = (version != null) ? system + "|" + version : system;
        if (!isCaseSensitive(url)) {
            code = normalize(code);
        }
        if (version != null) {
            Set<java.lang.String> codeSet = codeSetMap.get(system + "|" + version);
            if (codeSet != null) {
                if (codeSet.contains(code)) {
                    return true;
                } else {
                    codeSet = codeSetMap.get(system + "|" + VERSION_UNKNOWN);
                    if (codeSet != null) {
                        return codeSet.contains(code);
                    }
                }
            }
        } else {
            // coding didn't specify a version, so try them all
            java.lang.String prefix = system + "|";
            for (java.lang.String key : codeSetMap.keySet()) {
                if (key.startsWith(prefix) && codeSetMap.get(key).contains(code)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static List<Contains> wrap(Collection<Expansion.Contains> unwrapped) {
        List<Contains> wrapped = new ArrayList<>(unwrapped.size());
        for (Expansion.Contains contains : unwrapped) {
            wrapped.add(wrap(contains));
        }
        return wrapped;
    }

    private static Contains wrap(Expansion.Contains contains) {
        return new Contains(contains);
    }

    private static class Contains {
        private final Expansion.Contains contains;
        private final int hashCode;

        public Contains(Expansion.Contains contains) {
            this.contains = contains;
            hashCode = Objects.hash(contains.getSystem(), contains.getVersion(), contains.getCode());
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            Contains other = (Contains) obj;
            return Objects.equals(contains.getSystem(), other.contains.getSystem()) &&
                    Objects.equals(contains.getVersion(), other.contains.getVersion()) &&
                    Objects.equals(contains.getCode(), other.contains.getCode());
        }

        public Expansion.Contains getContains() {
            return contains;
        }

        @Override
        public int hashCode() {
            return hashCode;
        }
    }
}
