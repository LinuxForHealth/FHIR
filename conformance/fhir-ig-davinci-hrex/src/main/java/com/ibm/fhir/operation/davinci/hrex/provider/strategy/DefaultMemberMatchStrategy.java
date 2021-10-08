/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.davinci.hrex.provider.strategy;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.operation.davinci.hrex.provider.strategy.MemberMatchResult.ResponseType;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;

/**
 *
 */
public class DefaultMemberMatchStrategy extends AbstractMemberMatch {

    @Override
    public String getMemberMatchIdentifier() {
        return "default";
    }

    @Override
    MemberMatchResult executeMemberMatch() throws FHIROperationException {
        return MemberMatchResult.builder().responseType(ResponseType.SINGLE).value("55678").build();
    }

    @Override
    void validate(EvaluationContext evaluationContext) throws FHIROperationException {

    }

}
