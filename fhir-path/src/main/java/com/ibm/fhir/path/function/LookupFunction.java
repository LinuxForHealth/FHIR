/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.function;

import static com.ibm.fhir.path.util.FHIRPathUtil.empty;
import static com.ibm.fhir.path.util.FHIRPathUtil.getElementNode;
import static com.ibm.fhir.path.util.FHIRPathUtil.isStringValue;
import static com.ibm.fhir.path.util.FHIRPathUtil.singleton;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.path.FHIRPathElementNode;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.FHIRPathResourceNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.term.service.LookupOutcome;
import com.ibm.fhir.term.service.LookupParameters;

public class LookupFunction extends FHIRPathAbstractTermFunction {
    @Override
    public String getName() {
        return "lookup";
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
    protected Map<String, Function<String, Element>> buildElementFactoryMap() {
        Map<String, Function<String, Element>> map = new HashMap<>();
        map.put("date", DateTime::of);
        map.put("displayLanguage", Code::of);
        map.put("property", Code::of);
        return Collections.unmodifiableMap(map);
    }

    @Override
    public Collection<FHIRPathNode> apply(EvaluationContext evaluationContext, Collection<FHIRPathNode> context, List<Collection<FHIRPathNode>> arguments) {
        if (!isTermServiceNode(context) ||
                !isCodedElementNode(arguments.get(0), Coding.class, Code.class) ||
                (arguments.size() == 2 && !isStringValue(arguments.get(1)))) {
            return empty();
        }
        FHIRPathElementNode codedElementNode = getElementNode(arguments.get(0));
        Coding coding = getCoding(evaluationContext.getTree(), codedElementNode);
        Parameters parameters = getParameters(arguments);
        LookupOutcome outcome = service.lookup(coding, LookupParameters.from(parameters));
        if (outcome == null) {
            generateIssue(evaluationContext, IssueSeverity.ERROR, IssueType.NOT_SUPPORTED, "Lookup cannot be performed", "%terminologies");
            return empty();
        }
        return singleton(FHIRPathResourceNode.resourceNode(outcome.toParameters()));
    }
}
