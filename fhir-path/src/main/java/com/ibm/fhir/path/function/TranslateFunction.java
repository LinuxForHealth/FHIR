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

import com.ibm.fhir.model.resource.ConceptMap;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.path.FHIRPathElementNode;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.FHIRPathResourceNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.term.service.TranslationOutcome;
import com.ibm.fhir.term.service.TranslationParameters;

public class TranslateFunction extends FHIRPathAbstractTermFunction {
    @Override
    public String getName() {
        return "translate";
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
        map.put("reverse", Boolean::of);
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
        ConceptMap conceptMap = getResource(arguments, ConceptMap.class);
        FHIRPathElementNode codedElementNode = getElementNode(arguments.get(1));
        Element codedElement = getCodedElement(evaluationContext.getTree(), codedElementNode);
        Parameters parameters = getParameters(arguments);
        TranslationOutcome outcome = codedElement.is(CodeableConcept.class) ?
                service.translate(conceptMap, codedElement.as(CodeableConcept.class), TranslationParameters.from(parameters)) :
                service.translate(conceptMap, codedElement.as(Coding.class), TranslationParameters.from(parameters));
        if (outcome.getMessage() != null) {
            generateIssue(evaluationContext, IssueSeverity.ERROR, IssueType.NOT_FOUND, outcome.getMessage().getValue(), "%terminologies");
        }
        return singleton(FHIRPathResourceNode.resourceNode(outcome.toParameters()));
    }
}
