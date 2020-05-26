/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.service.provider;

import static com.ibm.fhir.core.util.LRUCache.createLRUCache;
import static com.ibm.fhir.term.util.CodeSystemSupport.findConcept;
import static com.ibm.fhir.term.util.CodeSystemSupport.getCodeSystem;
import static com.ibm.fhir.term.util.CodeSystemSupport.getConcepts;
import static com.ibm.fhir.term.util.ValueSetSupport.getContains;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.model.resource.CodeSystem.Concept;
import com.ibm.fhir.model.resource.ConceptMap;
import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.model.resource.ValueSet.Expansion;
import com.ibm.fhir.model.resource.ValueSet.Expansion.Contains;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.code.CodeSystemHierarchyMeaning;
import com.ibm.fhir.model.type.code.ConceptSubsumptionOutcome;
import com.ibm.fhir.term.spi.FHIRTermServiceProvider;
import com.ibm.fhir.term.spi.TranslationOutcome;
import com.ibm.fhir.term.util.ConceptMapSupport;
import com.ibm.fhir.term.util.ValueSetSupport;

public class DefaultTermServiceProvider implements FHIRTermServiceProvider {
    private static final Logger log = Logger.getLogger(DefaultTermServiceProvider.class.getName());

    private static final String VERSION_UNKNOWN = "<version unknown>";
    private static final Map<String, Map<String, Set<String>>> CODE_SET_MAP_CACHE = createLRUCache(1024);

    @Override
    public boolean isExpandable(ValueSet valueSet) {
        return ValueSetSupport.isExpandable(valueSet);
    }

    @Override
    public ValueSet expand(ValueSet valueSet) {
        return ValueSetSupport.expand(valueSet);
    }

    @Override
    public Concept lookup(Coding coding) {
        String system = (coding.getSystem() != null) ? coding.getSystem().getValue() : null;
        String version = (coding.getVersion() != null) ? coding.getVersion().getValue() : null;
        String code = (coding.getCode() != null) ? coding.getCode().getValue() : null;

        if (system != null && code != null) {
            String url = (version != null) ? system + "|" + version : system;
            CodeSystem codeSystem = getCodeSystem(url);
            if (codeSystem != null) {
                return findConcept(codeSystem, Code.of(code));
            }
        }

        return null;
    }

    @Override
    public ConceptSubsumptionOutcome subsumes(Coding codingA, Coding codingB) {
        String systemA = (codingA.getSystem() != null) ? codingA.getSystem().getValue() : null;
        String versionA = (codingA.getVersion() != null) ? codingA.getVersion().getValue() : null;
        String codeA = (codingA.getCode() != null) ? codingA.getCode().getValue() : null;

        String systemB = (codingB.getSystem() != null) ? codingB.getSystem().getValue() : null;
        String versionB = (codingB.getVersion() != null) ? codingB.getVersion().getValue() : null;
        String codeB = (codingB.getCode() != null) ? codingB.getCode().getValue() : null;

        if (systemA != null && systemB != null && codeA != null && codeB != null && systemA.equals(systemB)) {
            String url = systemA;

            if (versionA != null || versionB != null) {
                if (versionA != null && versionB != null && !versionA.equals(versionB)) {
                    return null;
                }
                url = (versionA != null) ? (url + "|" + versionA) : (url + "|" + versionB);
            }

            CodeSystem codeSystem = getCodeSystem(url);
            if (codeSystem != null && CodeSystemHierarchyMeaning.IS_A.equals(codeSystem.getHierarchyMeaning())) {
                Concept conceptA = findConcept(codeSystem, Code.of(codeA));
                if (conceptA != null) {
                    Concept conceptB = findConcept(conceptA, Code.of(codeB));
                    if (conceptB != null) {
                        return conceptA.equals(conceptB) ? ConceptSubsumptionOutcome.EQUIVALENT : ConceptSubsumptionOutcome.SUBSUMES;
                    }
                    conceptB = findConcept(codeSystem, Code.of(codeB));
                    if (conceptB != null) {
                        conceptA = findConcept(conceptB, Code.of(codeA));
                        return (conceptA != null) ? ConceptSubsumptionOutcome.SUBSUMED_BY : ConceptSubsumptionOutcome.NOT_SUBSUMED;
                    }
                }
            }
        }

        return null;
    }

    @Override
    public Set<Concept> closure(Coding coding) {
        String system = (coding.getSystem() != null) ? coding.getSystem().getValue() : null;
        String version = (coding.getVersion() != null) ? coding.getVersion().getValue() : null;
        String code = (coding.getCode() != null) ? coding.getCode().getValue() : null;

        if (system != null && code != null) {
            String url = (version != null) ? system + "|" + version : system;
            CodeSystem codeSystem = getCodeSystem(url);
            if (codeSystem != null && CodeSystemHierarchyMeaning.IS_A.equals(codeSystem.getHierarchyMeaning())) {
                Concept concept = findConcept(codeSystem, Code.of(code));
                if (concept != null) {
                    return getConcepts(concept);
                }
            }
        }

        return Collections.emptySet();
    }

    @Override
    public boolean validateCode(Coding coding) {
        return lookup(coding) != null;
    }

    @Override
    public boolean validateCode(ValueSet valueSet, String system, String version, String code) {
        return contains(getCodeSetMap(valueSet), system, version, code);
    }

    @Override
    public boolean validateCode(ValueSet valueSet, Coding coding) {
        return contains(getCodeSetMap(valueSet), coding);
    }

    @Override
    public boolean validateCode(ValueSet valueSet, CodeableConcept codeableConcept) {
        Map<String, Set<String>> codeSetMap = getCodeSetMap(valueSet);
        for (Coding coding : codeableConcept.getCoding()) {
            if (contains(codeSetMap, coding)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public TranslationOutcome translate(ConceptMap conceptMap, Coding coding) {
        return ConceptMapSupport.translate(conceptMap, coding);
    }

    private boolean contains(Map<String, Set<String>> codeSetMap, Coding coding) {
        String system = (coding.getSystem() != null) ? coding.getSystem().getValue() : null;
        String version = (coding.getVersion() != null) ? coding.getVersion().getValue() : null;
        String code = (coding.getCode() != null) ? coding.getCode().getValue() : null;
        return contains(codeSetMap, system, version, code);
    }

    /**
     * Determine whether the provided code is in the codeSet associated with the provided system and version.
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
     *     the system of the focal coded element (can be null)
     * @param version
     *     the version of the focal coded element (can be null)
     * @param code
     *     the code used in the membership check
     * @return
     *     true if a codeSet is found and the provided code is a member of that codeSet, false otherwise
     */
    private boolean contains(Map<String, Set<String>> codeSetMap, String system, String version, String code) {
        if (system != null && version != null) {
            Set<String> codeSet = codeSetMap.get(system + "|" + version);
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
        } else if (system != null) {
            String prefix = system + "|";
            for (String key : codeSetMap.keySet()) {
                if (key.startsWith(prefix)) {
                    return codeSetMap.get(key).contains(code);
                }
            }
        } else {
            for (Set<String> codeSet : codeSetMap.values()) {
                if (codeSet.contains(code)) {
                    return true;
                }
            }
        }
        return false;
    }

    private Map<String, Set<String>> getCodeSetMap(ValueSet valueSet) {
        if (valueSet.getUrl() == null || valueSet.getVersion() == null) {
            return computeCodeSetMap(valueSet);
        }
        String url = valueSet.getUrl().getValue() + "|" + valueSet.getVersion().getValue();
        return CODE_SET_MAP_CACHE.computeIfAbsent(url, k -> computeCodeSetMap(valueSet));
    }

    private Map<String, Set<String>> computeCodeSetMap(ValueSet valueSet) {
        try {
            ValueSet expanded = expand(valueSet);
            if (expanded == null || expanded.getExpansion() == null) {
                return Collections.emptyMap();
            }
            Map<String, Set<String>> codeSetMap = new LinkedHashMap<>();
            Expansion expansion = expanded.getExpansion();
            for (Contains contains : getContains(expansion)) {
                String system = (contains.getSystem() != null) ? contains.getSystem().getValue() : null;
                String version = (contains.getVersion() != null && contains.getVersion().getValue() != null) ? contains.getVersion().getValue() : VERSION_UNKNOWN;
                String code = (contains.getCode() != null) ? contains.getCode().getValue() : null;
                if (system != null && code != null) {
                    codeSetMap.computeIfAbsent(system + "|" + version, k -> new LinkedHashSet<>()).add(code);
                }
            }
            return codeSetMap;
        } catch (Exception e) {
            String url = (valueSet.getUrl() != null) ? valueSet.getUrl().getValue() : "<no url>";
            String version = (valueSet.getVersion() != null) ? valueSet.getVersion().getValue() : "<no version>";
            log.log(Level.WARNING, String.format("Unable to expand value set with url: %s and version: %s", url, version), e);
        }
        return Collections.emptyMap();
    }
}
