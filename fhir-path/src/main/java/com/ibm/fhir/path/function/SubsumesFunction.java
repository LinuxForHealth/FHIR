/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.function;

import static com.ibm.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_FALSE;
import static com.ibm.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_TRUE;
import static com.ibm.fhir.path.util.FHIRPathUtil.empty;
import static com.ibm.fhir.path.util.FHIRPathUtil.getElementNode;
import static com.ibm.fhir.path.util.FHIRPathUtil.singleton;

import java.util.Collection;
import java.util.List;

import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.code.ConceptSubsumptionOutcome;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.path.FHIRPathElementNode;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.term.service.FHIRTermService;

public class SubsumesFunction extends FHIRPathAbstractTermFunction {
    @Override
    public String getName() {
        return "subsumes";
    }

    @Override
    public int getMinArity() {
        return 1;
    }

    @Override
    public int getMaxArity() {
        return 3;
    }

    @Override
    protected Collection<FHIRPathNode> apply(
            EvaluationContext evaluationContext,
            Collection<FHIRPathNode> context,
            List<Collection<FHIRPathNode>> arguments,
            FHIRTermService service,
            Parameters parameters) {
        if (!isCodedElementNode(arguments.get(0), Coding.class, Code.class) || !isCodedElementNode(arguments.get(1), Coding.class, Code.class)) {
            return empty();
        }

        Coding codingA = getCoding(evaluationContext.getTree(), getElementNode(arguments.get(0)));
        Coding codingB = getCoding(evaluationContext.getTree(), getElementNode(arguments.get(1)));

        ConceptSubsumptionOutcome outcome = service.subsumes(codingA, codingB);

        if (outcome == null) {
            generateIssue(evaluationContext, IssueSeverity.WARNING, IssueType.NOT_SUPPORTED, "Subsumption cannot be tested", isCodedElementNode(context) ? getElementNode(context).path() : "%terminologies");
            return empty();
        }

        if (isCodedElementNode(context)) {
            switch (outcome.getValueAsEnumConstant()) {
            case EQUIVALENT:
            case SUBSUMES:
                return SINGLETON_TRUE;
            case SUBSUMED_BY:
            case NOT_SUBSUMED:
                return SINGLETON_FALSE;
            }
        }

        return singleton(FHIRPathElementNode.elementNode(outcome));
    }
}
