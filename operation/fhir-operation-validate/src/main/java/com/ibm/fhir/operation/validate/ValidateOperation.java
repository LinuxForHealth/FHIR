/*
 * (C) Copyright IBM Corp. 2016, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.validate;

import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.model.type.Xhtml.xhtml;

import java.util.Collections;
import java.util.List;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.type.code.NarrativeStatus;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.search.util.SearchHelper;
import com.ibm.fhir.server.spi.operation.AbstractOperation;
import com.ibm.fhir.server.spi.operation.FHIROperationContext;
import com.ibm.fhir.server.spi.operation.FHIROperationUtil;
import com.ibm.fhir.server.spi.operation.FHIRResourceHelpers;
import com.ibm.fhir.validation.FHIRValidator;

public class ValidateOperation extends AbstractOperation {
    @Override
    protected OperationDefinition buildOperationDefinition() {
        return FHIRRegistry.getInstance().getResource("http://hl7.org/fhir/OperationDefinition/Resource-validate",
                OperationDefinition.class);
    }

    @Override
    protected Parameters doInvoke(FHIROperationContext operationContext, Class<? extends Resource> resourceType, String logicalId,
            String versionId, Parameters parameters, FHIRResourceHelpers resourceHelper, SearchHelper searchHelper) throws FHIROperationException {
        try {
            Parameter resourceParameter = getParameter(parameters, "resource");
            if (resourceParameter == null) {
                throw buildExceptionWithIssue("Input parameter 'resource' is required for the $validate operation", IssueType.INVALID);
            }
            Resource resource = resourceParameter.getResource() ;

            List<OperationOutcome.Issue> issues;
            Parameter profileParameter = getParameter(parameters, "profile");
            Parameter modeParameter = getParameter(parameters, "mode");

            if (profileParameter != null && profileParameter.getValue() != null) {
                // If the 'profile' parameter is specified, always validate the resource against that profile.
                Uri profileUri = profileParameter.getValue().as(Uri.class);
                String profile = profileUri == null ? null : profileUri.getValue();
                issues = FHIRValidator.validator().validate(resource, profile);
            } else if (modeParameter != null && modeParameter.getValue() != null
                    && ("create".equals(modeParameter.getValue().as(Code.class).getValue())
                            || "update".equals(modeParameter.getValue().as(Code.class).getValue()))) {
                // If the 'mode' parameter is specified and its value is 'create' or 'update', validate the resource
                // against the FHIR server config's profile properties and the resource's asserted profiles.
                issues = resourceHelper.validateResource(resource);
            } else {
                // Standard validation against the resource's asserted profiles.
                issues = FHIRValidator.validator().validate(resource);
            }

            return FHIROperationUtil.getOutputParameters(buildResourceValidOperationOutcome(issues));
        } catch (FHIROperationException e) {
            throw e;
        } catch (Exception e) {
            throw new FHIROperationException("An error occurred during validation", e);
        }
    }

    private OperationOutcome buildResourceValidOperationOutcome(List<Issue> issues) {
        if (issues.isEmpty()) {
            issues = Collections.singletonList(Issue.builder()
                        .severity(IssueSeverity.INFORMATION)
                        .code(IssueType.INFORMATIONAL)
                        .details(CodeableConcept.builder()
                            .text(string("All OK"))
                            .build())
                        .build());
        }

        boolean hasError = issues.stream().anyMatch(issue -> IssueSeverity.ERROR.equals(issue.getSeverity())
                || IssueSeverity.FATAL.equals(issue.getSeverity()));

        OperationOutcome operationOutcome = OperationOutcome.builder()
                .id(hasError? "Error" : "NoError")
                .text(Narrative.builder()
                    .status(NarrativeStatus.ADDITIONAL)
                    .div(xhtml(hasError? "<div xmlns=\"http://www.w3.org/1999/xhtml\"><p>ERROR</p></div>"
                            : "<div xmlns=\"http://www.w3.org/1999/xhtml\"><p>No error</p></div>" ))
                    .build())
                .issue(issues)
                .build();

        return operationOutcome;
    }
}
