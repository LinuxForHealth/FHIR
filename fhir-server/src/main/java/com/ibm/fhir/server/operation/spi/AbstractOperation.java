/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.operation.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.jwt.JsonWebToken;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.type.code.OperationParameterUse;
import com.ibm.fhir.model.util.FHIRUtil;
import com.ibm.fhir.model.util.ModelSupport;

public abstract class AbstractOperation implements FHIROperation {

    protected OperationDefinition definition = null;

    public AbstractOperation() {
        definition = buildOperationDefinition();
    }

    @Override
    public OperationDefinition getDefinition() {
        return definition;
    }

    @Override
    public String getName() {
        if (definition.getCode() == null)
            return null;
        else
            return definition.getCode().getValue();
    }

    /**
     * Validate the input parameters, invoke the operation, validate the output parameters, and return the result.
     *
     * @throws FHIROperationException if input or output parameters fail validation or an exception occurs
     */
    @Override
    public final Parameters invoke(FHIROperationContext operationContext, Class<? extends Resource> resourceType,
            String logicalId, String versionId, Parameters parameters, FHIRResourceHelpers resourceHelper)
            throws FHIROperationException {
        validateOperationContext(operationContext, resourceType);
        validateInputParameters(operationContext, resourceType, logicalId, versionId, parameters);
        Parameters result = doInvoke(operationContext, resourceType, logicalId, versionId, parameters, resourceHelper);
        validateOutputParameters(result);
        return result;
    }

    protected abstract OperationDefinition buildOperationDefinition();

    protected int countParameters(Parameters parameters, String name) {
        return getParameters(parameters, name).size();
    }

    /**
     * This is the method that concrete subclasses must implement to perform the operation logic.
     *
     * @return The Parameters object to return or null if there is no response Parameters object to return
     * @throws FHIROperationException
     */
    protected abstract Parameters doInvoke(FHIROperationContext operationContext,
            Class<? extends Resource> resourceType, String logicalId, String versionId, Parameters parameters,
            FHIRResourceHelpers resourceHelper) throws FHIROperationException;

    protected Parameters.Parameter getParameter(Parameters parameters, String name) {
        for (Parameters.Parameter parameter : parameters.getParameter()) {
            if (name.equals(parameter.getName().getValue())) {
                return parameter;
            }
        }
        return null;
    }

    protected List<OperationDefinition.Parameter> getParameterDefinitions(OperationParameterUse use) {
        List<OperationDefinition.Parameter> parameterDefinitions = new ArrayList<>();
        OperationDefinition definition = getDefinition();
        for (OperationDefinition.Parameter parameter : definition.getParameter()) {
            if (use.getValue().equals(parameter.getUse().getValue())) {
                parameterDefinitions.add(parameter);
            }
        }
        return parameterDefinitions;
    }

    protected List<Parameters.Parameter> getParameters(Parameters parameters, String name) {
        List<Parameters.Parameter> result = new ArrayList<>();
        if (parameters == null) {
            return result;
        }

        for (Parameters.Parameter parameter : parameters.getParameter()) {
            if (parameter.getName() != null && name.equals(parameter.getName().getValue())) {
                result.add(parameter);
            }
        }
        return result;
    }

    protected List<String> getResourceTypeNames() {
        List<String> resourceTypeNames = new ArrayList<>();
        OperationDefinition definition = getDefinition();
        for (com.ibm.fhir.model.type.code.ResourceType type : definition.getResource()) {
            resourceTypeNames.add(type.getValue());
        }
        return resourceTypeNames;
    }

    protected void validateInputParameters(FHIROperationContext operationContext,
            Class<? extends Resource> resourceType, String logicalId, String versionId, Parameters parameters)
            throws FHIROperationException {
        validateParameters(parameters, OperationParameterUse.IN);
    }

    protected void validateOperationContext(FHIROperationContext operationContext,
            Class<? extends Resource> resourceType) throws FHIROperationException {
        OperationDefinition definition = getDefinition();
        switch (operationContext.getType()) {
        case INSTANCE:
            if (definition.getInstance().getValue() == false) {
                String msg = "Operation context INSTANCE is not allowed for operation: '" + getName() + "'";
                throw buildExceptionWithIssue(msg, IssueType.INVALID);
            }
            break;
        case RESOURCE_TYPE:
            if (definition.getType().getValue() == false) {
                String msg = "Operation context RESOURCE_TYPE is not allowed for operation: '" + getName() + "'";
                throw buildExceptionWithIssue(msg, IssueType.INVALID);
            } else {
                String resourceTypeName = resourceType.getSimpleName();
                List<String> resourceTypeNames = getResourceTypeNames();
                if (!resourceTypeNames.contains(resourceTypeName) && !resourceTypeNames.contains("Resource")) {
                    String msg =
                            "Resource type: '" + resourceTypeName + "' is not allowed for operation: '" + getName()
                                    + "'";
                    throw buildExceptionWithIssue(msg, IssueType.INVALID);
                }
            }
            break;
        case SYSTEM:
            if (definition.getSystem().getValue() == false) {
                String msg = "Operation context SYSTEM is not allowed for operation: '" + getName() + "'";
                throw buildExceptionWithIssue(msg, IssueType.INVALID);
            }
            break;
        default:
            break;
        }
    }

    protected void validateOutputParameters(Parameters result) throws FHIROperationException {
        validateParameters(result, OperationParameterUse.OUT);
    }

    protected void validateParameters(Parameters parameters, OperationParameterUse use) throws FHIROperationException {
        String direction = OperationParameterUse.IN.equals(use) ? "input" : "output";

        // Retrieve the set of parameters from the OperationDefinition matching the specified use (in/out).
        List<OperationDefinition.Parameter> opDefParameters = getParameterDefinitions(use);

        // Verify that each defined parameter is specified appropriately in the Parameters object.
        for (OperationDefinition.Parameter parameterDefinition : opDefParameters) {
            String name = parameterDefinition.getName().getValue();
            int min = parameterDefinition.getMin().getValue();
            String max = parameterDefinition.getMax().getValue();
            int count = countParameters(parameters, name);
            if (count < min) {
                String msg = "Missing required " + direction + " parameter: '" + name + "'";
                throw buildExceptionWithIssue(msg, IssueType.REQUIRED);
            }
            if (!"*".equals(max)) {
                int maxValue = Integer.parseInt(max);
                if (count > maxValue) {
                    String msg =
                            "Number of occurrences of " + direction + " parameter: '" + name
                                    + "' greater than allowed maximum: " + maxValue;
                    throw buildExceptionWithIssue(msg, IssueType.INVALID);
                }
            }
            if (count > 0) {
                List<Parameters.Parameter> inputParameters = getParameters(parameters, name);
                for (Parameters.Parameter inputParameter : inputParameters) {
                    // Check to see if it's a parameter
                    if (inputParameter.getPart() == null || inputParameter.getPart().isEmpty()) {
                        String parameterValueTypeName =
                                inputParameter.getResource() != null ? inputParameter.getResource().getClass().getName()
                                        : inputParameter.getValue().getClass().getName();
                        String parameterDefinitionTypeName = parameterDefinition.getType().getValue();
                        parameterDefinitionTypeName =
                                parameterDefinitionTypeName.substring(0, 1).toUpperCase()
                                        + parameterDefinitionTypeName.substring(1);
                        try {
                            Class<?> parameterValueType, parameterDefinitionType;
                            parameterValueType = Class.forName(parameterValueTypeName);

                            if (ModelSupport.isResourceType(parameterDefinitionTypeName)) {
                                parameterDefinitionType =
                                        Class.forName("com.ibm.fhir.model.resource." + parameterDefinitionTypeName);
                            } else {
                                parameterDefinitionType =
                                        Class.forName("com.ibm.fhir.model.type." + parameterDefinitionTypeName);
                            }

                            if (!parameterDefinitionType.isAssignableFrom(parameterValueType)) {
                                String msg =
                                        "Invalid type: '" + parameterValueTypeName + "' for " + direction
                                                + " parameter: '" + name + "'";
                                throw buildExceptionWithIssue(msg, IssueType.INVALID);
                            }
                        } catch (FHIROperationException e) {
                            throw e;
                        } catch (Exception e) {
                            throw new FHIROperationException("An unexpected error occurred during type checking", e);
                        }
                        /*
                         * if (!parameterValueTypeName.equalsIgnoreCase(parameterDefinition.getType().getValue())) {
                         * throw new FHIROperationException("Invalid type: '" + parameterValueTypeName + "' for " +
                         * direction + " parameter: '" + name + "'");
                         * }
                         */
                    }
                }
            }
        }

        // Next, if parameters is not null, verify that each parameter contained in the Parameters object is defined
        // in the OperationDefinition. This will root out any extaneous parameters included
        // in the Parameters object.
        if (parameters == null) {
            return;
        }
        for (Parameters.Parameter p : parameters.getParameter()) {
            String name = p.getName().getValue();

            OperationDefinition.Parameter opDefParameter = findOpDefParameter(opDefParameters, name);
            if (opDefParameter == null) {
                // Avoid throwing the exception for an OUT parameter called "return".
                if (!(OperationParameterUse.OUT.equals(use) && "return".equals(name))) {
                    String msg = "Unexpected " + direction + " parameter found: " + name;
                    throw buildExceptionWithIssue(msg, IssueType.INVALID);
                }
            }
        }
    }

    /**
     * Returns the OperationDefinitionParameter with the specified name or null if it wasn't found.
     *
     * @param parameters the list of parameters from the OperationDefinition
     * @param name       the name of the parameter to find
     * @return
     */
    protected OperationDefinition.Parameter findOpDefParameter(List<OperationDefinition.Parameter> parameters,
            String name) {
        for (OperationDefinition.Parameter p : parameters) {
            if (p.getName().getValue().equals(name)) {
                return p;
            }
        }
        return null;
    }

    protected FHIROperationException buildExceptionWithIssue(String msg, IssueType issueType)
            throws FHIROperationException {
        return buildExceptionWithIssue(msg, issueType, null);
    }

    protected FHIROperationException buildExceptionWithIssue(String msg, IssueType issueType, Throwable cause)
            throws FHIROperationException {
        OperationOutcome.Issue ooi = FHIRUtil.buildOperationOutcomeIssue(msg, issueType);
        return new FHIROperationException(msg, cause).withIssue(ooi);
    }

    /**
     * verifies the authorization to an administrative operation.
     *
     * @param operation
     * @param operationContext
     * @throws FHIROperationException
     */
    public void authorize(String operation, FHIROperationContext operationContext) throws FHIROperationException {
        Object securityContext = operationContext.getProperty(FHIROperationContext.PROPNAME_SECURITY_CONTEXT);
        Object jwt = operationContext.getProperty(FHIROperationContext.PROPNAME_JWT);

        boolean authorize = false;

        if (jwt != null && jwt instanceof JsonWebToken) {
            JsonWebToken jwtObj = (JsonWebToken) jwt;
            Set<String> groups = jwtObj.getGroups();
            if (groups != null) {
                for (String group : groups) {
                    if ("FHIROperationAdmin".equals(group)) {
                        authorize = true;
                        return;
                    }
                }
            }
        }

        if (!authorize && securityContext != null && securityContext instanceof SecurityContext) {
            SecurityContext ctx = (SecurityContext) securityContext;
            authorize = ctx.isUserInRole("FHIROperationAdmin");
        }

        if (!authorize) {
            throw buildExceptionWithIssue("Access to operation [$" + operation + "] is forbidden", IssueType.FORBIDDEN);
        }
    }
}