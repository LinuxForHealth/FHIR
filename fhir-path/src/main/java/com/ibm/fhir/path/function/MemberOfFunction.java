/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.function;

import static com.ibm.fhir.core.util.LRUCache.createLRUCache;
import static com.ibm.fhir.model.util.ModelSupport.FHIR_STRING;
import static com.ibm.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_FALSE;
import static com.ibm.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_TRUE;
import static com.ibm.fhir.path.util.FHIRPathUtil.empty;
import static com.ibm.fhir.path.util.FHIRPathUtil.getElementNode;
import static com.ibm.fhir.path.util.FHIRPathUtil.getString;
import static com.ibm.fhir.path.util.FHIRPathUtil.isCodedElementNode;
import static com.ibm.fhir.path.util.FHIRPathUtil.isStringElementNode;
import static com.ibm.fhir.path.util.FHIRPathUtil.isStringValue;
import static com.ibm.fhir.path.util.FHIRPathUtil.isUriElementNode;
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

import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.model.resource.ValueSet.Expansion;
import com.ibm.fhir.model.resource.ValueSet.Expansion.Contains;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.path.FHIRPathElementNode;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.FHIRPathType;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.registry.FHIRRegistry;

/**
 * Implementation of the 'memberOf' FHIRPath function per: http://hl7.org/fhir/fhirpath.html#functions
 *
 * <p>This implementation supports an optional second argument (binding strength). The binding strength
 * is used to determine whether or not to add a warning to the evaluation context if the membership check fails.
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
        return 2;
    }

    @Override
    public Collection<FHIRPathNode> apply(EvaluationContext evaluationContext, Collection<FHIRPathNode> context, List<Collection<FHIRPathNode>> arguments) {
        if (context.isEmpty()) {
            return empty();
        }

        if (!isCodedElementNode(context) && !isStringElementNode(context) && !isUriElementNode(context)) {
            throw new IllegalArgumentException("The 'memberOf' function must be invoked on a coded element node, string element node, or uri element node");
        }

        if (!isStringValue(arguments.get(0))) {
            throw new IllegalArgumentException("The argument to the 'memberOf' function must be a string value");
        }

        if (arguments.size() == 2 && !isStringValue(arguments.get(1))) {
            throw new IllegalArgumentException("The optional second argument to the 'memberOf' function must be a string value");
        }

        FHIRPathElementNode elementNode = getElementNode(context);
        Element element = elementNode.element();
        String url = getString(arguments.get(0));
        String strength = (arguments.size() == 2) ? getString(arguments.get(1)) : null;

        if (FHIRRegistry.getInstance().hasResource(url, ValueSet.class)) {
            Map<String, Set<String>> codeSetMap = getCodeSetMap(url);
            if (!codeSetMap.isEmpty()) {
                if (element.is(Code.class)) {
                    String system = getSystem(evaluationContext.getTree().getParent(elementNode));
                    String version = FHIRRegistry.getInstance().getLatestVersion(system, CodeSystem.class);
                    String code = element.as(Code.class).getValue();
                    if (contains(codeSetMap, system, version, code)) {
                        return SINGLETON_TRUE;
                    }
                } else if (element.is(Coding.class)) {
                    Coding coding = element.as(Coding.class);
                    if (contains(codeSetMap, coding)) {
                        return SINGLETON_TRUE;
                    }
                } else if (element.is(CodeableConcept.class)) {
                    CodeableConcept codeableConcept = element.as(CodeableConcept.class);
                    for (Coding coding : codeableConcept.getCoding()) {
                        if (contains(codeSetMap, coding)) {
                            return SINGLETON_TRUE;
                        }
                    }
                } else {
                    // element.is(FHIR_STRING) || element.is(Uri.class)
                    String value = element.is(FHIR_STRING) ? element.as(FHIR_STRING).getValue() : element.as(Uri.class).getValue();
                    if (contains(codeSetMap, null, null, value)) {
                        return SINGLETON_TRUE;
                    }
                }
                return membershipCheckFailed(evaluationContext, elementNode, url, strength);
            } else {
                generateIssue(evaluationContext, IssueSeverity.WARNING, IssueType.INCOMPLETE, "Value set '" + url + "' is empty or could not be expanded", elementNode);
            }
        } else {
            generateIssue(evaluationContext, IssueSeverity.WARNING, IssueType.NOT_SUPPORTED, "Value set '" + url + "' is not supported", elementNode);
        }

        return SINGLETON_TRUE;
    }

    private Collection<FHIRPathNode> membershipCheckFailed(EvaluationContext evaluationContext, FHIRPathElementNode elementNode, String url, String strength) {
        if ("extensible".equals(strength) || "preferred".equals(strength)) {
            String prefix = evaluationContext.hasConstraint() ? evaluationContext.getConstraint().id() + ": " : "";
            String description = prefix + "The concept in this element " + ("extensible".equals(strength) ? "must" : "should") + " be from the specified value set '" + url + "' if possible";
            generateIssue(evaluationContext, IssueSeverity.WARNING, IssueType.CODE_INVALID, description, elementNode);
            return SINGLETON_TRUE;
        }
        return SINGLETON_FALSE;
    }

    private boolean contains(Map<String, Set<String>> codeSetMap, Coding coding) {
        String system = (coding.getSystem() != null) ? coding.getSystem().getValue() : null;
        String version = (coding.getVersion() != null) ? coding.getVersion().getValue() : FHIRRegistry.getInstance().getLatestVersion(system, CodeSystem.class);
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
