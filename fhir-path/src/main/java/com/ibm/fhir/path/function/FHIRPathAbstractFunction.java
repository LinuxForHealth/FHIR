/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.function;

import static com.ibm.fhir.model.type.String.string;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;

public abstract class FHIRPathAbstractFunction implements FHIRPathFunction {
    @Override
    public abstract String getName();

    @Override
    public abstract int getMinArity();

    @Override
    public abstract int getMaxArity();

    @Override
    public Collection<FHIRPathNode> apply(EvaluationContext evaluationContext, Collection<FHIRPathNode> context, List<Collection<FHIRPathNode>> arguments) {
        throw new UnsupportedOperationException("Function: '" + getName() + "' is not supported");
    }

    protected void generateSupplementalWarning(
            EvaluationContext evaluationContext,
            IssueSeverity severity,
            IssueType code,
            String description,
            String expression) {
        evaluationContext.getSupplementalWarnings().add(Issue.builder()
            .severity(severity)
            .code(code)
            .details(CodeableConcept.builder()
                .text(string(description))
                .build())
            .expression(string(expression))
            .build());
    }

    protected void generateErrorDetail(
        EvaluationContext evaluationContext,
        IssueType code,
        String description,
        String expression) {
    evaluationContext.getErrorDetails().add(Issue.builder()
        .severity(IssueSeverity.ERROR)
        .code(code)
        .details(CodeableConcept.builder()
            .text(string(description))
            .build())
        .expression(string(expression))
        .build());
}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FHIRPathFunction)) {
            return false;
        }
        FHIRPathFunction other = (FHIRPathFunction) obj;
        return Objects.equals(getName(), other.getName()) &&
                Objects.equals(getMinArity(), other.getMinArity()) &&
                Objects.equals(getMaxArity(), other.getMaxArity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getMinArity(), getMaxArity());
    }
}
