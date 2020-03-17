/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.function;

import static com.ibm.fhir.core.util.LRUCache.createLRUCache;
import static com.ibm.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_FALSE;
import static com.ibm.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_TRUE;
import static com.ibm.fhir.path.util.FHIRPathUtil.getElementNode;
import static com.ibm.fhir.path.util.FHIRPathUtil.getString;
import static com.ibm.fhir.path.util.FHIRPathUtil.hasElementNode;
import static com.ibm.fhir.path.util.FHIRPathUtil.hasStringValue;
import static com.ibm.fhir.profile.ValueSetSupport.expand;
import static com.ibm.fhir.profile.ValueSetSupport.getContains;
import static com.ibm.fhir.profile.ValueSetSupport.getValueSet;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.model.resource.ValueSet.Expansion;
import com.ibm.fhir.model.resource.ValueSet.Expansion.Contains;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.path.FHIRPathElementNode;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.FHIRPathType;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.registry.FHIRRegistry;

/**
 * Implementation of the 'memberOf' FHIRPath function per: http://hl7.org/fhir/fhirpath.html#functions
 */
public class MemberOfFunction extends FHIRPathAbstractFunction {
    private static final Logger log = Logger.getLogger(MemberOfFunction.class.getName());

    private static final String VERSION_UNKNOWN = "<version unknown>";
    private static final Map<String, Map<String, Set<String>>> CODE_SET_MAP_CACHE = createLRUCache(1024);
    
    @Override
    public String getName() {
        return "memberOf";
    }

    @Override
    public int getMinArity() {
        return 1;
    }

    @Override
    public int getMaxArity() {
        return 1;
    }
    
    @Override
    public Collection<FHIRPathNode> apply(EvaluationContext evaluationContext, Collection<FHIRPathNode> context, List<Collection<FHIRPathNode>> arguments) {
        if (!hasElementNode(context)) {
            throw new IllegalArgumentException("The 'memberOf' function can only be invoked on an Element node");
        }
        
        FHIRPathElementNode elementNode = getElementNode(context);
        Element element = elementNode.element();
        
        if (!isCodedElement(element)) {
            throw new IllegalArgumentException("The 'memberOf' function can only be invoked on a coded element");
        }
        
        if (!hasStringValue(arguments.get(0))) {
            throw new IllegalArgumentException("The argument to the 'memberOf' function must be a string");
        }
        
        String url = getString(arguments.get(0));
                
        if (FHIRRegistry.getInstance().hasResource(url)) {
            Map<String, Set<String>> codeSetMap = getCodeSetMap(url);
            if (!codeSetMap.isEmpty()) {
                if (element.is(Code.class)) {
                    String system = getSystem(evaluationContext.getTree().getParent(elementNode));
                    String version = FHIRRegistry.getInstance().getLatestVersion(system);
                    String code = element.as(Code.class).getValue();
                    return contains(codeSetMap, system, version, code) ? SINGLETON_TRUE : SINGLETON_FALSE;
                } else if (element.is(Coding.class)) {
                    Coding coding = element.as(Coding.class);
                    return contains(codeSetMap, coding) ? SINGLETON_TRUE : SINGLETON_FALSE;
                } else if (element.is(CodeableConcept.class)) {
                    CodeableConcept codeableConcept = element.as(CodeableConcept.class);
                    for (Coding coding : codeableConcept.getCoding()) {
                        if (contains(codeSetMap, coding)) {
                            return SINGLETON_TRUE;
                        }
                    }
                    return SINGLETON_FALSE;
                }
            }
        }
        
        return SINGLETON_TRUE;
    }
    
    private boolean contains(Map<String, Set<String>> codeSetMap, Coding coding) {
        String system = (coding.getSystem() != null) ? coding.getSystem().getValue() : null;
        String version = (coding.getVersion() != null) ? coding.getVersion().getValue() : FHIRRegistry.getInstance().getLatestVersion(system);
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
            String key = system + "|" + version;
            Set<String> codeSet = codeSetMap.get(key);
            if (codeSet != null) {
                if (codeSet.contains(code)) {
                    return true;
                } else {
                    key = system + "|" + VERSION_UNKNOWN;
                    codeSet = codeSetMap.get(key);
                    if (codeSet != null) {
                        return codeSet.contains(code);
                    }
                }
            }
        } else if (system != null) {
            for (String key : codeSetMap.keySet()) {
                if (key.startsWith(system)) {
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
    
    /**
     * Get a URI-typed child node of the input parameter with name "system".
     * 
     * @param node
     *     the parent node
     * @return
     *     the URI-typed child node with name "system", or null if no such child node exists
     */
    private String getSystem(FHIRPathNode node) {
        if (node == null || !node.isElementNode()) {
            return null;
        }
        for (FHIRPathNode child : node.children()) {
            if ("system".equals(child.name()) && FHIRPathType.FHIR_URI.equals(node.type())) {
                return child.asElementNode().element().as(Uri.class).getValue();
            }
        }
        return null;
    }
    
    private Map<String, Set<String>> getCodeSetMap(String url) {
        return CODE_SET_MAP_CACHE.computeIfAbsent(url, k -> computeCodeSetMap(getValueSet(url)));
    }
    
    private boolean isCodedElement(Element element) {
        return (element instanceof Code) || 
                (element instanceof Coding) || 
                (element instanceof CodeableConcept);
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
                String version = (contains.getVersion() != null) ? contains.getVersion().getValue() : VERSION_UNKNOWN;
                String code = (contains.getCode() != null) ? contains.getCode().getValue() : null;
                if (system != null && version != null && code != null) {
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
