/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.function;

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
import static com.ibm.fhir.term.util.ValueSetSupport.getValueSet;
import static com.ibm.fhir.term.util.ValueSetSupport.isExpandable;
import static com.ibm.fhir.term.util.ValueSetSupport.isExpanded;

import java.util.Collection;
import java.util.List;

import com.ibm.fhir.model.resource.ValueSet;
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
import com.ibm.fhir.term.service.FHIRTermService;

/**
 * Implementation of the 'memberOf' FHIRPath function per: http://hl7.org/fhir/fhirpath.html#functions
 *
 * <p>This implementation supports an optional second argument (binding strength). The binding strength
 * is used to determine whether or not to add a warning to the evaluation context if the membership check fails.
 */
public class MemberOfFunction extends FHIRPathAbstractFunction {
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
            ValueSet valueSet = getValueSet(url);
            if (isExpanded(valueSet) || isExpandable(valueSet)) {
                FHIRTermService service = FHIRTermService.getInstance();
                if (element.is(Code.class)) {
                    String system = getSystem(evaluationContext.getTree().getParent(elementNode));
                    String code = element.as(Code.class).getValue();
                    if (service.validateCode(valueSet, system, null, code)) {
                        return SINGLETON_TRUE;
                    }
                } else if (element.is(Coding.class)) {
                    Coding coding = element.as(Coding.class);
                    if (service.validateCode(coding)) {
                        return SINGLETON_TRUE;
                    }
                } else if (element.is(CodeableConcept.class)) {
                    CodeableConcept codeableConcept = element.as(CodeableConcept.class);
                    if (service.validateCode(valueSet, codeableConcept)) {
                        return SINGLETON_TRUE;
                    }
                } else {
                    // element.is(FHIR_STRING) || element.is(Uri.class)
                    String value = element.is(FHIR_STRING) ? element.as(FHIR_STRING).getValue() : element.as(Uri.class).getValue();
                    if (service.validateCode(valueSet, null, null, value)) {
                        return SINGLETON_TRUE;
                    }
                }
                return membershipCheckFailed(evaluationContext, elementNode, url, strength);
            } else {
                generateIssue(evaluationContext, IssueSeverity.WARNING, IssueType.INCOMPLETE, "Membership check was not performed: value set '" + url + "' is empty or could not be expanded", elementNode);
            }
        } else {
            generateIssue(evaluationContext, IssueSeverity.WARNING, IssueType.NOT_SUPPORTED, "Membership check was not performed: value set '" + url + "' is not supported", elementNode);
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
}
