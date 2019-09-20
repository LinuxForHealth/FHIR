/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.operation.bullkdata;

import static com.ibm.watson.health.fhir.model.type.String.string;
import static com.ibm.watson.health.fhir.model.type.Xhtml.xhtml;
import static com.ibm.watson.health.fhir.model.util.FHIRUtil.isFailure;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import com.ibm.watson.health.fhir.exception.FHIROperationException;
import com.ibm.watson.health.fhir.model.format.Format;
import com.ibm.watson.health.fhir.model.parser.FHIRParser;
import com.ibm.watson.health.fhir.model.resource.OperationDefinition;
import com.ibm.watson.health.fhir.model.resource.OperationOutcome;
import com.ibm.watson.health.fhir.model.resource.Parameters;
import com.ibm.watson.health.fhir.model.resource.Resource;
import com.ibm.watson.health.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.watson.health.fhir.model.resource.Parameters.Parameter;
import com.ibm.watson.health.fhir.model.type.CodeableConcept;
import com.ibm.watson.health.fhir.model.type.Id;
import com.ibm.watson.health.fhir.model.type.IssueSeverity;
import com.ibm.watson.health.fhir.model.type.IssueType;
import com.ibm.watson.health.fhir.model.type.Narrative;
import com.ibm.watson.health.fhir.model.type.NarrativeStatus;
import com.ibm.watson.health.fhir.operation.AbstractOperation;
import com.ibm.watson.health.fhir.operation.context.FHIROperationContext;
import com.ibm.watson.health.fhir.operation.util.FHIROperationUtil;
import com.ibm.watson.health.fhir.rest.FHIRResourceHelpers;
import com.ibm.watson.health.fhir.validation.FHIRValidator;

public class ImportOperation extends AbstractOperation {

    private static final String FILE = "import.json";

    public ImportOperation() {
        super();
    }

    @Override
    protected OperationDefinition buildOperationDefinition() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(FILE);) {
            return FHIRParser.parser(Format.JSON).parse(in);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    @Override
    protected Parameters doInvoke(FHIROperationContext operationContext,
        Class<? extends Resource> resourceType, String logicalId, String versionId,
        Parameters parameters,
        FHIRResourceHelpers resourceHelper) throws FHIROperationException {
        try {
            Parameter parameter = getParameter(parameters, "resource");
            if (parameter == null) {
                throw buildExceptionWithIssue("Input parameter 'resource' is required for the $validate operation", IssueType.ValueSet.INVALID);
            }

            Resource resource = parameter.getResource();
            List<Issue> issues = FHIRValidator.validator().validate(resource);

            if (issues.stream().anyMatch(issue -> isFailure(issue.getSeverity()))) {
                throw new FHIROperationException("Input resource failed validation.").withIssue(issues);
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
            issues = Collections.singletonList(Issue.builder().severity(IssueSeverity.INFORMATION).code(IssueType.INFORMATIONAL).details(CodeableConcept.builder().text(string("All OK")).build()).build());
        }

        OperationOutcome operationOutcome =
                OperationOutcome.builder().id(Id.of("NoError")).text(Narrative.builder().status(NarrativeStatus.ADDITIONAL).div(xhtml("<div xmlns=\"http://www.w3.org/1999/xhtml\"><p>No ERROR</p></div>")).build()).issue(issues).build();

        return operationOutcome;
    }
}
