/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.function;

import static com.ibm.fhir.path.util.FHIRPathUtil.empty;
import static com.ibm.fhir.path.util.FHIRPathUtil.getElementNode;
import static com.ibm.fhir.path.util.FHIRPathUtil.isResourceNode;
import static com.ibm.fhir.path.util.FHIRPathUtil.isStringValue;
import static com.ibm.fhir.path.util.FHIRPathUtil.singleton;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.path.FHIRPathElementNode;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.FHIRPathResourceNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.term.service.ValidationOutcome;
import com.ibm.fhir.term.service.ValidationParameters;

public class ValidateCSFunction extends FHIRPathAbstractTermFunction {
    @Override
    public String getName() {
        return "validateCS";
    }

    @Override
    public int getMinArity() {
        return 2;
    }

    @Override
    public int getMaxArity() {
        return 3;
    }

    @Override
    protected Map<String, Function<String, Element>> buildElementFactoryMap() {
        Map<String, Function<String, Element>> map = new HashMap<>();
        map.put("date", DateTime::of);
        map.put("abstract", Boolean::of);
        map.put("displayLanguage", Code::of);
        return Collections.unmodifiableMap(map);
    }

    @Override
    public Collection<FHIRPathNode> apply(EvaluationContext evaluationContext, Collection<FHIRPathNode> context, List<Collection<FHIRPathNode>> arguments) {
        if (!isTermServiceNode(context) ||
                (!isResourceNode(arguments.get(0)) && !isStringValue(arguments.get(0))) ||
                !isCodedElementNode(arguments.get(1)) ||
                (arguments.size() == 3 && !isStringValue(arguments.get(2)))) {
            return empty();
        }
        CodeSystem codeSystem = getResource(arguments, CodeSystem.class);
        FHIRPathElementNode codedElementNode = getElementNode(arguments.get(1));
        Element codedElement = getCodedElement(evaluationContext.getTree(), codedElementNode);
        if (!validate(evaluationContext, codeSystem, codedElement)) {
            return empty();
        }
        Parameters parameters = getParameters(arguments);
        ValidationOutcome outcome = codedElement.is(CodeableConcept.class) ?
                service.validateCode(codeSystem, codedElement.as(CodeableConcept.class), ValidationParameters.from(parameters)) :
                service.validateCode(codeSystem, codedElement.as(Coding.class), ValidationParameters.from(parameters));
        if (Boolean.FALSE.equals(outcome.getResult()) && outcome.getMessage() != null) {
            generateIssue(evaluationContext, IssueSeverity.ERROR, IssueType.CODE_INVALID, outcome.getMessage().getValue(), "%terminologies");
        }
        return singleton(FHIRPathResourceNode.resourceNode(outcome.toParameters()));
    }

    private boolean validate(EvaluationContext evaluationContext, CodeSystem codeSystem, Element codedElement) {
        if (codedElement.is(CodeableConcept.class)) {
            CodeableConcept codeableConcept = codedElement.as(CodeableConcept.class);
            for (Coding coding : codeableConcept.getCoding()) {
                if (coding.getSystem() != null && codeSystem.getUrl() != null && coding.getSystem().equals(codeSystem.getUrl()) && (coding.getVersion() == null ||
                        codeSystem.getVersion() == null ||
                        coding.getVersion().equals(codeSystem.getVersion()))) {
                    return true;
                }
            }
            generateIssue(evaluationContext, IssueSeverity.ERROR, IssueType.CODE_INVALID, "CodeableConcept does not contain a coding element that matches the specified CodeSystem url and/or version", "%terminologies");
            return false;
        }
        // codedElement.is(Coding.class)
        Coding coding = codedElement.as(Coding.class);
        if (coding.getSystem() != null && codeSystem.getUrl() != null && !coding.getSystem().equals(codeSystem.getUrl())) {
            generateIssue(evaluationContext, IssueSeverity.ERROR, IssueType.CODE_INVALID, "Coding system does not match the specified CodeSystem url", "%terminologies");
            return false;
        }
        if (coding.getVersion() != null && codeSystem.getVersion() != null && !coding.getVersion().equals(codeSystem.getVersion())) {
            generateIssue(evaluationContext, IssueSeverity.ERROR, IssueType.CODE_INVALID, "Coding version does not match the specified CodeSystem version", "%terminologies");
            return false;
        }
        return true;
    }
}
