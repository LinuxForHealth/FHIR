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
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationContext.Type;
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
            String versionId, Parameters parameters, FHIRResourceHelpers resourceHelper, SearchHelper searchHelper) throws FHIROperationException {
        try {
            Parameter resourceParameter = getParameter(parameters, "resource");
            String resourceTypeName = resourceType != null ? resourceType.getSimpleName() : null;
            Parameter modeParameter = getParameter(parameters, "mode");
            ModeType modeType = getModeType(modeParameter);
            // Validate the resource parameter
            Resource resource = validateResource(operationContext, logicalId, resourceHelper, resourceParameter, resourceTypeName, modeType);
            
            List<OperationOutcome.Issue> issues;
            Parameter profileParameter = getParameter(parameters, "profile");
            
            FHIRPersistence persistence =
                    (FHIRPersistence) operationContext.getProperty(FHIROperationContext.PROPNAME_PERSISTENCE_IMPL);
            // Validate mode parameter.
            validateModeParameter(modeParameter, resourceHelper, resourceTypeName, operationContext.getType(), persistence);

            if (profileParameter != null && profileParameter.getValue() != null) {
                // If the 'profile' parameter is specified, always validate the resource against that profile.
                Uri profileUri = profileParameter.getValue().as(Uri.class);
                String profile = profileUri == null ? null : profileUri.getValue();
                issues = FHIRValidator.validator().validate(resource, profile);
            } else if (modeType != null
                    && ("create".equals(modeType.value())
                            || "update".equals(modeType.value()))) {
                // If the 'mode' parameter is specified and its value is 'create' or 'update', validate the resource
                // against the FHIR server config's profile properties and the resource's asserted profiles.
                issues = resourceHelper.validateResource(resource);
            } else if (modeType != null
                    && ModeType.DELETE.value().equals(modeType.value())
                    && FHIROperationContext.Type.INSTANCE.equals(operationContext.getType())) {
                // If the 'mode' parameter is specified and its value is 'delete' and delete is invoked at the resource-instance level,
                // validate if the persistence layer implementation supports the "delete" operation
                issues = validateDeleteResource(resourceTypeName, logicalId, operationContext, persistence);
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
    
    
    /**
     * This method does the following validations.
     * 1. Validate if a resource validation mode code is valid.
     * 2. Validate an interaction for a specified resource type.
     * 3. Validate if persistence layer implementation supports update/create mode if mode = update/create.
     * 4. Validate if modes update and delete are only be used when the operation is invoked at the resource instance level
     *
     * @param modeParameter resource validation mode code to be validated
     * @param resourceHelper Resource operation provider for loading related Library resources
     * @param resourceType a valid resource type string
     * @param type the invocation type:  SYSTEM, RESOURCE_TYPE, INSTANCE
     * @param persistence the FHIRPersistence implementation to facilitate the calls to the underlying db 
     * @throws FHIROperationException when the resource validation mode code is invalid
     */
    private void validateModeParameter(Parameter modeParameter, FHIRResourceHelpers resourceHelper, String resourceType, Type type, FHIRPersistence persistence) throws FHIROperationException {
        if (modeParameter != null && modeParameter.getValue() != null) {
            ModeType modetype;
            try {
                // Validate if a resource validation mode code is valid
                modetype =  ModeType.from(modeParameter.getValue().as(Code.class).getValue());
            } catch (IllegalArgumentException e) {
                String msg = "'" + modeParameter.getValue().as(Code.class).getValue() + "' is not a valid resource validation mode";
                throw buildExceptionWithIssue(IssueSeverity.ERROR, IssueType.INVALID, msg, null);
            }
            
            // Validate an interaction for a specified resource type.
            if (ModeType.CREATE.value().equals(modetype.value())  
                    || ModeType.UPDATE.value().equals(modetype.value()) 
                    || ModeType.DELETE.value().equals(modetype.value())) {
                FHIRResourceHelpers.Interaction interaction = FHIRResourceHelpers.Interaction.from(modetype.value());
                resourceHelper.validateInteraction(interaction, resourceType);
                
            }
            
            // Validate if persistence layer implementation supports update/create mode if mode = update/create.
            if (FHIROperationContext.Type.INSTANCE.equals(type) && 
                    (ModeType.CREATE.value().equals(modetype.value()) || ModeType.UPDATE.value().equals(modetype.value())) 
                    && !persistence.isUpdateCreateEnabled()) {
                throw buildExceptionWithIssue(IssueSeverity.ERROR, IssueType.NOT_SUPPORTED, "Resource " + modetype.value() +  ", of type '"
                        + resourceType + "', is not supported.", null);
            }
            
            // Validate if modes update and delete are only be used when the operation is invoked at the resource instance level
            if (FHIROperationContext.Type.RESOURCE_TYPE.equals(type) && 
                    (ModeType.UPDATE.value().equals(modetype.value()) || ModeType.DELETE.value().equals(modetype.value()))) {
                
                throw buildExceptionWithIssue(IssueSeverity.ERROR, IssueType.NOT_SUPPORTED, "Modes update and delete can only be used when the operation is invoked at the resource instance level.", null);
            }
        }
    }

    /**
     * Method to get a valid resource validation mode code from ModeType enum
     * @param modeParameter resource validation mode code
     * @return The corresponding ModeType or null if a null value was passed
     * @throws FHIROperationException
     */
    private ModeType getModeType(Parameter modeParameter) throws FHIROperationException {
        if (modeParameter != null && modeParameter.getValue() != null) {
            ModeType modetype;
            try {
                modetype =  ModeType.from(modeParameter.getValue().as(Code.class).getValue());
            } catch (IllegalArgumentException e) {
                String msg = "'" + modeParameter.getValue().as(Code.class).getValue() + "' is not a valid resource validation mode";
                throw buildExceptionWithIssue(IssueSeverity.ERROR, IssueType.NOT_SUPPORTED, msg, null);
            }
            return modetype;
            
        }
        return null; 
        
    }
    
    
    /**
     * Validate if the persistence layer implementation supports the "delete" operation.
     *
     * @param type the resource type 
     * @param id the resource logical ID
     * @param operationContext the FHIROperationContext associated with the request
     * @param persistence the FHIRPersistence implementation to facilitate the calls to the underlying db
     * @return A list of validation errors and warnings
     * @throws FHIROperationException the FHIR operation exception
     */
    public List<Issue> validateDeleteResource(String type, String id, FHIROperationContext operationContext, FHIRPersistence persistence) throws FHIROperationException {
        List<Issue> warnings = new ArrayList<>();
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
     * Method to validate the resource parameter. The below validations are performed by this method.
     * 1. if $validate is invoked at instance level and mode = create, check if the resource already exists.
     * 2. resource parameter must be present unless the mode is "delete"
     * 3. if mode=profile AND no resource parameter value is provided then the resource at this id is read and validated against the nominated profile
     * @param operationContext
     * @param logicalId
     * @param resourceHelper
     * @param resourceParameter
     * @param resourceTypeName
     * @param modeType
     * @return
     * @throws Exception
     * @throws FHIROperationException
     */
    private Resource validateResource(FHIROperationContext operationContext, String logicalId, FHIRResourceHelpers resourceHelper, Parameter resourceParameter,
        String resourceTypeName, ModeType modeType) throws Exception, FHIROperationException {
        Resource resource = null;
        // if $validate is invoked at instance level and mode = create, check if the resource already exists. 
        if (operationContext != null && FHIROperationContext.Type.INSTANCE.equals(operationContext.getType()) 
                && modeType != null && ModeType.CREATE.value().equals(modeType.value())) {
            Resource existingResource = resourceHelper.doRead(resourceTypeName, logicalId).getResource();
            if (existingResource != null) {
                throw buildExceptionWithIssue(IssueSeverity.ERROR, IssueType.NOT_SUPPORTED, resourceTypeName + " with id '" + logicalId + "' already exists", null);
            }
        }
        // resource parameter must be present unless the mode is "delete"
        if (resourceParameter == null 
                && (modeType == null 
                || ModeType.CREATE.value().equals(modeType.value()) 
                || ModeType.UPDATE.value().equals(modeType.value()))) {
            throw buildExceptionWithIssue(IssueSeverity.ERROR, IssueType.INVALID, "Input parameter 'resource' must be present unless the mode is 'delete'", null);
        }
        // if mode=profile AND no resource parameter value is provided then the resource at this id is read and validated against the nominated profile
        if (resourceParameter == null && operationContext != null && FHIROperationContext.Type.INSTANCE.equals(operationContext.getType()) 
                && (modeType != null && ModeType.PROFILE.value().equals(modeType.value()))) {
            resource = resourceHelper.doRead(resourceTypeName, logicalId).getResource();
            if (resource == null) {
                throw buildExceptionWithIssue(IssueSeverity.ERROR, IssueType.INVALID, resourceTypeName + " with id '" + logicalId + "' does not exist", null);
            }
        }
        
        if (resourceParameter != null) {
            resource = resourceParameter.getResource();
        }
        return resource;
    }
    
}
