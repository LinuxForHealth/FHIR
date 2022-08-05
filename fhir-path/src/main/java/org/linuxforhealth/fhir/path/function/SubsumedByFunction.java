/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.path.function;

import static org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_FALSE;
import static org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_TRUE;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.empty;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.getElementNode;

import java.util.Collection;
import java.util.List;

import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.Coding;
import org.linuxforhealth.fhir.model.type.code.ConceptSubsumptionOutcome;
import org.linuxforhealth.fhir.model.type.code.IssueSeverity;
import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.path.FHIRPathNode;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;

public class SubsumedByFunction extends FHIRPathAbstractTermFunction {
    @Override
    public String getName() {
        return "subsumedBy";
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
        if (!isCodedElementNode(context, Coding.class, Code.class) || !isCodedElementNode(arguments.get(0), Coding.class, Code.class)) {
            return empty();
        }

        Coding codingA = getCoding(evaluationContext.getTree(), getElementNode(context));
        Coding codingB = getCoding(evaluationContext.getTree(), getElementNode(arguments.get(0)));

        ConceptSubsumptionOutcome outcome = service.subsumes(codingA, codingB);

        if (outcome == null) {
            generateIssue(evaluationContext, IssueSeverity.ERROR, IssueType.NOT_SUPPORTED, "Subsumption cannot be tested", getElementNode(context).path());
            return empty();
        }

        switch (outcome.getValueAsEnum()) {
        case EQUIVALENT:
        case SUBSUMED_BY:
            return SINGLETON_TRUE;
        default:
            return SINGLETON_FALSE;
        }
    }
}
