/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.operation.term;

import static org.linuxforhealth.fhir.model.type.String.string;

import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.resource.CodeSystem;
import org.linuxforhealth.fhir.model.resource.OperationDefinition;
import org.linuxforhealth.fhir.model.resource.Parameters;
import org.linuxforhealth.fhir.model.resource.Parameters.Parameter;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.type.Coding;
import org.linuxforhealth.fhir.model.type.code.ConceptSubsumptionOutcome;
import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.registry.FHIRRegistry;
import org.linuxforhealth.fhir.search.util.SearchHelper;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationContext;
import org.linuxforhealth.fhir.server.spi.operation.FHIRResourceHelpers;
import org.linuxforhealth.fhir.term.service.exception.FHIRTermServiceException;

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