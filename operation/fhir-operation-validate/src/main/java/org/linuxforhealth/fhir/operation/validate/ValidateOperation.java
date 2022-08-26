/*
 * (C) Copyright IBM Corp. 2016, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.operation.validate;

import static org.linuxforhealth.fhir.model.type.String.string;
import static org.linuxforhealth.fhir.model.type.Xhtml.xhtml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.resource.OperationDefinition;
import org.linuxforhealth.fhir.model.resource.OperationOutcome;
import org.linuxforhealth.fhir.model.resource.OperationOutcome.Issue;
import org.linuxforhealth.fhir.model.resource.Parameters;
import org.linuxforhealth.fhir.model.resource.Parameters.Parameter;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.CodeableConcept;
import org.linuxforhealth.fhir.model.type.Narrative;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.IssueSeverity;
import org.linuxforhealth.fhir.model.type.code.IssueType;
import org.linuxforhealth.fhir.model.type.code.NarrativeStatus;
import org.linuxforhealth.fhir.model.util.FHIRUtil;
import org.linuxforhealth.fhir.operation.validate.type.ModeType;
import org.linuxforhealth.fhir.persistence.FHIRPersistence;
import org.linuxforhealth.fhir.registry.FHIRRegistry;
import org.linuxforhealth.fhir.search.util.SearchHelper;
import org.linuxforhealth.fhir.server.spi.operation.AbstractOperation;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationContext;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationUtil;
import org.linuxforhealth.fhir.server.spi.operation.FHIRResourceHelpers;
import org.linuxforhealth.fhir.validation.FHIRValidator;

public class ValidateOperation extends AbstractOperation {
    @Override
    protected OperationDefinition buildOperationDefinition() {
        return FHIRRegistry.getInstance().getResource("http://hl7.org/fhir/OperationDefinition/Resource-validate",
                OperationDefinition.class);
    }

    @Override
    protected Parameters doInvoke(FHIROperationContext operationContext, Class<? extends Resource> resourceType, String logicalId,
            String versionId, Parameters parameters, FHIRResourceHelpers resourceHelper, SearchHelper searchHelper)
            throws FHIROperationException {
        Objects.requireNonNull(operationContext, "operations framework prevents operationContext from being null");
        Objects.requireNonNull(resourceType, "operations framework should prevent resourceType from being null "
                + "because $validate must be invoked at the resource type or instance level");

        try {
            List<OperationOutcome.Issue> issues = new ArrayList<>();

            Parameter resourceParameter = getParameter(parameters, "resource");
            Parameter modeParameter = getParameter(parameters, "mode");
            Parameter profileParameter = getParameter(parameters, "profile");

            String resourceTypeName = resourceType.getSimpleName();
            ModeType modeType = getModeType(modeParameter);

            // Validate the resource parameter
            Resource resource = getResource(operationContext, logicalId, resourceHelper, resourceParameter, resourceTypeName, modeType);

            // Validate the mode parameter
            issues.addAll(validateModeAndInteraction(modeType, resourceHelper, resourceTypeName, operationContext, logicalId));

            // Additional checks when invoked at the instance level
            if (operationContext.getType() == FHIROperationContext.Type.INSTANCE) {
                issues.addAll(validateInstanceLevelInvoke(operationContext, logicalId, resourceHelper, resource, resourceTypeName, modeType));
            }

            if (profileParameter != null && profileParameter.getValue() != null) {
                // If the 'profile' parameter is specified, always validate the resource against that profile.
                Uri profileUri = profileParameter.getValue().as(Uri.class);
                String profile = profileUri == null ? null : profileUri.getValue();
                issues.addAll(FHIRValidator.validator().validate(resource, profile));
            } else if (modeType == ModeType.CREATE || modeType == ModeType.UPDATE) {
                // If the 'mode' parameter is specified and its value is 'create' or 'update', validate the resource
                // against the FHIR server config's profile properties and the resource's asserted profiles.
                issues.addAll(resourceHelper.validateResource(resource));
            } else if (modeType == ModeType.DELETE && operationContext.getType() == FHIROperationContext.Type.INSTANCE) {
                // If the 'mode' parameter is specified and its value is 'delete' and delete is invoked at the resource-instance level,
                // validate if the persistence layer implementation supports the "delete" operation
                issues.addAll(validateDeleteResource(resourceTypeName, logicalId, operationContext));
            } else {
                // Standard validation against the resource's asserted profiles.
                issues.addAll(FHIRValidator.validator().validate(resource));
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


    /**
     * Validate that the mode is supported for this invocation (update and delete are only supported at the instance level)
     * and that the corresponding interaction is enabled for the passed resource type.
     * @param modeType resource validation mode code to be validated
     * @param resourceHelper Resource operation provider for loading related Library resources
     * @param resourceType a valid resource type string
     * @param operationContext the FHIROperationContext associated with the request; never null
     * @param logicalId the resource id included in the invocation request (if invoked at the instance level)
     * @return List<Issue> - a non-null, possibly-empty list of issues that indicate why the resource is not valid for the passed modeType
     * @throws FHIROperationException if the operation was invoked at the resource type level with mode = update or delete
     */
    private List<Issue> validateModeAndInteraction(ModeType modeType, FHIRResourceHelpers resourceHelper, String resourceType,
            FHIROperationContext operationContext, String logicalId) throws FHIROperationException {
        if (modeType == null) {
            return Collections.emptyList();
        }

        // Validate if modes update and delete are only be used when the operation is invoked at the resource instance level
        if (operationContext.getType() == FHIROperationContext.Type.RESOURCE_TYPE &&
                (modeType == ModeType.UPDATE || modeType == ModeType.DELETE)) {

            throw buildExceptionWithIssue(IssueSeverity.ERROR, IssueType.NOT_SUPPORTED,
                    "Modes update and delete can only be used when the operation is invoked at the resource instance level.", null);
        }

        List<Issue> issues = new ArrayList<>();

        // Validate the interaction type is allowed for the specified resource type.
        if ((modeType == ModeType.CREATE || modeType == ModeType.UPDATE || modeType == ModeType.DELETE)) {
            FHIRResourceHelpers.Interaction interaction = FHIRResourceHelpers.Interaction.from(modeType.value());
            try {
                resourceHelper.validateInteraction(interaction, resourceType);
            } catch (FHIROperationException e) {
                issues.addAll(e.getIssues());
            }
        }

        return issues;
    }

    /**
     * This method when invoked checks if the persistence layer implementation supports update/create.
     * If the persistence layer implementation does not support update/create, an issue is added to the list of issues that was passed.
     * @param modetype
     * @param resourceType
     * @param operationContext
     * @param issues
     * @throws FHIROperationException
     */
    private void validateUpdateCreateEnabled(ModeType modetype, String resourceType, FHIROperationContext operationContext, List<Issue> issues)
            throws FHIROperationException {
        FHIRPersistence persistence =
                (FHIRPersistence) operationContext.getProperty(FHIROperationContext.PROPNAME_PERSISTENCE_IMPL);
        if (!persistence.isUpdateCreateEnabled()) {
            issues.add(FHIRUtil.buildOperationOutcomeIssue(IssueSeverity.ERROR, IssueType.NOT_SUPPORTED,
                    "Resource " + modetype.value() +  ", of type '" + resourceType + "', is not supported."));
        }
    }

    /**
     * If the mode parameter is not null, this method validates that the resource validation mode code
     * is valid and returns the corresponding value from the ModeType enum.
     * @param modeParameter resource validation mode code
     * @return The corresponding ModeType or null if a null value was passed
     * @throws FHIROperationException If the mode parameter has an invalid value
     */
    private ModeType getModeType(Parameter modeParameter) throws FHIROperationException {
        if (modeParameter == null || modeParameter.getValue() == null) {
            return null;
        }

        ModeType modetype;
        try {
            modetype =  ModeType.from(modeParameter.getValue().as(Code.class).getValue());
        } catch (IllegalArgumentException e) {
            String msg = "'" + modeParameter.getValue().as(Code.class).getValue() + "' is not a valid resource validation mode";
            throw buildExceptionWithIssue(IssueSeverity.ERROR, IssueType.NOT_SUPPORTED, msg, null);
        }
        return modetype;
    }


    /**
     * Validate if the persistence layer implementation supports the "delete" operation.
     *
     * @param type the resource type
     * @param id the resource logical ID
     * @param operationContext the FHIROperationContext associated with the request
     * @return A list of validation errors and warnings
     * @throws FHIROperationException the FHIR operation exception
     */
    public List<Issue> validateDeleteResource(String type, String id, FHIROperationContext operationContext) throws FHIROperationException {
        List<Issue> warnings = new ArrayList<>();
        FHIRPersistence persistence =
                (FHIRPersistence) operationContext.getProperty(FHIROperationContext.PROPNAME_PERSISTENCE_IMPL);
        if (!persistence.isDeleteSupported()) {
            warnings.add(FHIRUtil.buildOperationOutcomeIssue(IssueSeverity.ERROR, IssueType.NOT_SUPPORTED, "Resource deletion, of type '"
                    + type + "', with id '" + id + "', is not supported."));
        }
        return warnings;
    }

    /**
     * Method to build the OperationOutcome error response
     * @param severity the issue severity
     * @param issueType describes the type of issue
     * @param msg describes the error message
     * @param cause the throwable that causes this OperationOutcome/Exception
     * @return FHIROperationException with issues
     * @throws FHIROperationException
     */
    protected FHIROperationException buildExceptionWithIssue(IssueSeverity severity, IssueType issueType, String msg, Throwable cause) throws FHIROperationException {
        OperationOutcome.Issue ooi = FHIRUtil.buildOperationOutcomeIssue(severity, issueType, msg);
        return new FHIROperationException(msg, cause).withIssue(ooi);
    }

    /**
     * Get the resource from the input resource parameter or from the database if mode=profile and invoked at instance level.
     * @param operationContext - the FHIROperationContext associated with the request
     * @param logicalId - the logical id of the FHIR resource
     * @param resourceHelper - Resource operation provider for loading related Library resources
     * @param resourceParameter - the input resource parameter
     * @param resourceType  - a valid resource type string
     * @param modeType - a valid resource validation mode code
     * @return Resource - one of:
     *      1. the value of the resource parameter
     *      2. the resource that was read if mode=profile and no resource parameter value is provided; or
     *      3. null if mode=delete
     * @throws FHIROperationException if either:
     *      1. the mode is "delete" (or "profile" per https://jira.hl7.org/browse/FHIR-37998) and the resource parameter is not present; or
     *      2. the mode is "profile" but a resource with the passed logicalId doesn't exist (or couldn't be read)
     */
    private Resource getResource(FHIROperationContext operationContext, String logicalId, FHIRResourceHelpers resourceHelper, Parameter resourceParameter,
            String resourceType, ModeType modeType) throws Exception, FHIROperationException {
        Resource resource = null;

        // resource parameter must be present unless the mode is "delete" (or "profile" per https://jira.hl7.org/browse/FHIR-37998)
        if (resourceParameter == null) {
            if (modeType == null || modeType == ModeType.CREATE || modeType == ModeType.UPDATE) {
                throw buildExceptionWithIssue(IssueSeverity.ERROR, IssueType.INVALID,
                        "Input parameter 'resource' must be present unless the mode is 'delete' or 'profile'", null);
            }

            // if mode=profile AND no resource parameter value is provided
            // then use resource at this id is read and validated against the nominated profile
            if (operationContext != null && operationContext.getType() == FHIROperationContext.Type.INSTANCE
                    && modeType == ModeType.PROFILE) {
                resource = resourceHelper.doRead(resourceType, logicalId).getResource();
                if (resource == null) {
                    throw buildExceptionWithIssue(IssueSeverity.ERROR, IssueType.NOT_FOUND,
                            resourceType + " with id '" + logicalId + "' does not exist", null);
                }
            }
        } else {
            resource = resourceParameter.getResource();
        }

        return resource;
    }

    /**
     * When $validate is invoked at the instance level, we need to perform some additional validations:
     * <ul>
     * <li>if mode = create or update, check that the id of the passed resource matches the id in the URL
     * <li>if mode = create, check if the resource already exists.
     * <li>if mode = create or mode = update and the resource doesn't exist, check that "create-on-update" is enabled
     * </ul>
     * @param operationContext - the FHIROperationContext associated with the request
     * @param logicalId - the logical id of the FHIR resource
     * @param resourceHelper - Resource operation provider for loading related Library resources
     * @param resource - the resource to be validated
     * @param resourceType  - a valid resource type string
     * @param modeType - a valid resource validation mode code
     * @return List<Issue> - a non-null, possibly-empty list of issues that indicate why the resource is not valid for the passed modeType
     */
    private List<Issue> validateInstanceLevelInvoke(FHIROperationContext operationContext, String logicalId, FHIRResourceHelpers resourceHelper,
            Resource resource, String resourceType, ModeType modeType) throws Exception, FHIROperationException {
        List<Issue> issues = new ArrayList<>();

        if (modeType == ModeType.CREATE || modeType == ModeType.UPDATE) {
            // if mode = create or update, check that the id of the passed resource matches the id in the URL
            validateResourceId(logicalId, resource, issues);

            boolean resourceExists = resourceHelper.doRead(resourceType, logicalId).getResource() != null;

            // if mode = create, check if the resource already exists
            if (modeType == ModeType.CREATE && resourceExists) {
                issues.add(FHIRUtil.buildOperationOutcomeIssue(IssueSeverity.ERROR, IssueType.INVALID,
                        resourceType + " with id '" + logicalId + "' already exists"));
            }

            // if mode = create or mode = update and the resource doesn't exist yet,
            // check if the persistence layer implementation supports update/create mode
            if (modeType == ModeType.CREATE || !resourceExists) {
                validateUpdateCreateEnabled(modeType, resourceType, operationContext, issues);
            }
        }

        return issues;
    }

    private void validateResourceId(String logicalId, Resource resource, List<Issue> issues) {
        String resourceId = resource.getId();
        if (resourceId == null) {
            issues.add(FHIRUtil.buildOperationOutcomeIssue(IssueSeverity.ERROR, IssueType.INVALID,
                    "Input resource 'id' field is not set"));
        } else if (!resourceId.equals(logicalId)) {
            issues.add(FHIRUtil.buildOperationOutcomeIssue(IssueSeverity.ERROR, IssueType.INVALID,
                    "Input resource 'id' field must match the 'id' path parameter."));
        }
    }
}
