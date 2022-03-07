/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.term;

import static com.ibm.fhir.model.type.String.string;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.code.ConceptSubsumptionOutcome;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.search.util.SearchHelper;
import com.ibm.fhir.server.spi.operation.FHIROperationContext;
import com.ibm.fhir.server.spi.operation.FHIRResourceHelpers;
import com.ibm.fhir.term.service.exception.FHIRTermServiceException;

public class SubsumesOperation extends AbstractTermOperation {
    @Override
    protected OperationDefinition buildOperationDefinition() {
        return FHIRRegistry.getInstance().getResource("http://hl7.org/fhir/OperationDefinition/CodeSystem-subsumes", OperationDefinition.class);
    }

    @Override
    protected Parameters doInvoke(
            FHIROperationContext operationContext,
            Class<? extends Resource> resourceType,
            String logicalId,
            String versionId,
            Parameters parameters,
            FHIRResourceHelpers resourceHelper,
            SearchHelper searchHelper) throws FHIROperationException {
        try {
            CodeSystem codeSystem = FHIROperationContext.Type.INSTANCE.equals(operationContext.getType()) ?
                    getResource(operationContext, logicalId, parameters, resourceHelper, CodeSystem.class) : null;
            Coding codingA = getCoding(parameters, "codingA", "codeA", (codeSystem == null));
            Coding codingB = getCoding(parameters, "codingB", "codeB", (codeSystem == null));
            if (codeSystem != null) {
                codingA = codingA.toBuilder().system(codeSystem.getUrl()).build();
                codingB = codingB.toBuilder().system(codeSystem.getUrl()).build();
            }
            ConceptSubsumptionOutcome outcome = service.subsumes(codingA, codingB);
            if (outcome == null) {
                throw buildExceptionWithIssue("Subsumption cannot be tested", IssueType.NOT_SUPPORTED);
            }
            return Parameters.builder()
                    .parameter(Parameter.builder()
                        .name(string("outcome"))
                        .value(outcome)
                        .build())
                    .build();
        } catch (FHIROperationException e) {
            throw e;
        } catch (FHIRTermServiceException e) {
            throw new FHIROperationException(e.getMessage(), e.getCause()).withIssue(e.getIssues());
        } catch (UnsupportedOperationException e) {
            throw buildExceptionWithIssue(e.getMessage(), IssueType.NOT_SUPPORTED, e);
        } catch (Exception e) {
            throw new FHIROperationException("An error occurred during the CodeSystem subsumes operation", e);
        }
    }
}