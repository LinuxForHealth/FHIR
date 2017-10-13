/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.operation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.ibm.watsonhealth.fhir.model.Code;
import com.ibm.watsonhealth.fhir.model.ObjectFactory;
import com.ibm.watsonhealth.fhir.model.OperationDefinition;
import com.ibm.watsonhealth.fhir.model.OperationDefinitionParameter;
import com.ibm.watsonhealth.fhir.model.OperationParameterUseList;
import com.ibm.watsonhealth.fhir.model.Parameters;
import com.ibm.watsonhealth.fhir.model.ParametersParameter;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.operation.context.FHIROperationContext;
import com.ibm.watsonhealth.fhir.operation.exception.FHIROperationException;
import com.ibm.watsonhealth.fhir.rest.FHIRResourceHelpers;

public abstract class AbstractOperation implements FHIROperation {
    protected ObjectFactory factory = null;
    protected OperationDefinition definition = null;
    
    public AbstractOperation() {
        factory = new ObjectFactory();
        definition = buildOperationDefinition();
    }
    
    @Override
    public OperationDefinition getDefinition() {
        return definition;
    }

    @Override
    public String getName() {
        if (definition.getCode() == null) return null;
        else return definition.getCode().getValue();
    }

    @Override
    public final Parameters invoke(FHIROperationContext operationContext, Class<? extends Resource> resourceType, String logicalId, String versionId,
        Parameters parameters, FHIRResourceHelpers resourceHelper) throws FHIROperationException {
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

    protected abstract Parameters doInvoke(FHIROperationContext operationContext, Class<? extends Resource> resourceType, String logicalId, String versionId,
        Parameters parameters, FHIRResourceHelpers resourceHelper) throws FHIROperationException;

    protected ParametersParameter getParameter(Parameters parameters, String name) {
        for (ParametersParameter parameter : parameters.getParameter()) {
            if (name.equals(parameter.getName().getValue())) {
                return parameter;
            }
        }
        return null;
    }
    
    protected List<OperationDefinitionParameter> getParameterDefinitions(OperationParameterUseList use) {
        List<OperationDefinitionParameter> parameterDefinitions = new ArrayList<OperationDefinitionParameter>();
        OperationDefinition definition = getDefinition();
        for (OperationDefinitionParameter parameter : definition.getParameter()) {
            if (use.equals(parameter.getUse().getValue())) {
                parameterDefinitions.add(parameter);
            }
        }
        return parameterDefinitions;
    }

    protected List<ParametersParameter> getParameters(Parameters parameters, String name) {
        List<ParametersParameter> result = new ArrayList<ParametersParameter>();
        for (ParametersParameter parameter : parameters.getParameter()) {
            if (parameter.getName() != null && name.equals(parameter.getName().getValue())) {
                result.add(parameter);
            }
        }
        return result;
    }
    
    protected String getParameterValueTypeName(ParametersParameter parameter) throws FHIROperationException {
        if (parameter.getResource() != null) {
            try {
                Resource resource = FHIRUtil.getResourceContainerResource(parameter.getResource());
                return resource.getClass().getSimpleName();
            } catch (Exception e) {
            }
        }
        for (Method method : ParametersParameter.class.getMethods()) {
            String methodName = method.getName();
            if (methodName.startsWith("getValue")) {
                try {
                    Object value = method.invoke(parameter);
                    if (value != null) {
                        return methodName.substring("getValue".length());
                    }
                } catch (Exception e) {
                }
            }
        }
        throw new FHIROperationException("No parameter value found for parameter: '" + parameter.getName().getValue() + "'");
    }
    
    protected List<String> getResourceTypeNames() {
        List<String> resourceTypeNames = new ArrayList<String>();
        OperationDefinition definition = getDefinition();
        for (Code type : definition.getType()) {
            resourceTypeNames.add(type.getValue());
        }
        return resourceTypeNames;
    }
    
    protected void validateInputParameters(FHIROperationContext operationContext, Class<? extends Resource> resourceType, String logicalId, String versionId, Parameters parameters) throws FHIROperationException {
        validateParameters(parameters, OperationParameterUseList.IN);
    }

    protected void validateOperationContext(FHIROperationContext operationContext, Class<? extends Resource> resourceType) throws FHIROperationException {
        OperationDefinition definition = getDefinition();
        switch (operationContext.getType()) {
        case INSTANCE:
            if (definition.getInstance().isValue() == false) {
                throw new FHIROperationException("Operation context INSTANCE is not allowed for operation: '" + getName() + "'");
            }
            break;
        case RESOURCE_TYPE:
            if (definition.getType().isEmpty()) {
                throw new FHIROperationException("Operation context RESOURCE_TYPE is not allowed for operation: '" + getName() + "'");
            } else {
                String resourceTypeName = resourceType.getSimpleName();
                List<String> resourceTypeNames = getResourceTypeNames();
                if (!resourceTypeNames.contains(resourceTypeName) && !resourceTypeNames.contains("Resource")) {
                    throw new FHIROperationException("Resource type: '" + resourceTypeName + "' is not allowed for operation: '" + getName() + "'");
                }
            }
            break;
        case SYSTEM:
            if (definition.getSystem().isValue() == false) {
                throw new FHIROperationException("Operation context SYSTEM is not allowed for operation: '" + getName() + "'");
            }
            break;
        default:
            break;
        }
    }

    protected void validateOutputParameters(Parameters result) throws FHIROperationException {
        validateParameters(result, OperationParameterUseList.OUT);
    }
    
    protected void validateParameters(Parameters parameters, OperationParameterUseList use) throws FHIROperationException {        
        String direction = OperationParameterUseList.IN.equals(use) ? "input" : "output";
        for (OperationDefinitionParameter parameterDefinition : getParameterDefinitions(use)) {
            String name = parameterDefinition.getName().getValue();
            int min = parameterDefinition.getMin().getValue();
            String max = parameterDefinition.getMax().getValue();
            int count = countParameters(parameters, name);
            if (count < min) {
                throw new FHIROperationException("Missing required " + direction + " parameter: '" + name + "'");
            }
            if (!"*".equals(max)) {
                int maxValue = Integer.parseInt(max);                
                if (count > maxValue) {
                    throw new FHIROperationException("Number of occurrences of " + direction + " parameter: '" + name + "' greater than allowed maximum: " + maxValue);
                }
            }
            if (count > 0) {
                List<ParametersParameter> inputParameters = getParameters(parameters, name);
                for (ParametersParameter inputParameter : inputParameters) {
                    String parameterValueTypeName = getParameterValueTypeName(inputParameter);
                    String parameterDefinitionTypeName = parameterDefinition.getType().getValue();
                    parameterDefinitionTypeName = parameterDefinitionTypeName.substring(0, 1).toUpperCase() + parameterDefinitionTypeName.substring(1);
                    try {
                        Class<?> parameterValueType = Class.forName("com.ibm.watsonhealth.fhir.model." + parameterValueTypeName);
                        Class<?> parameterDefinitionType = Class.forName("com.ibm.watsonhealth.fhir.model." + parameterDefinitionTypeName);
                        if (!parameterDefinitionType.isAssignableFrom(parameterValueType)) {
                            throw new FHIROperationException("Invalid type: '" + parameterValueTypeName + "' for " + direction + " parameter: '" + name + "'");
                        }
                    } catch (Exception e) {
                        throw new FHIROperationException("An error occurred during type checking", e);
                    }
                    /*
                    if (!parameterValueTypeName.equalsIgnoreCase(parameterDefinition.getType().getValue())) {
                        throw new FHIROperationException("Invalid type: '" + parameterValueTypeName + "' for " + direction + " parameter: '" + name + "'");
                    }
                    */
                }
            }
        }
    }
}
