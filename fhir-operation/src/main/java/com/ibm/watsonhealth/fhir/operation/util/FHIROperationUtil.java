/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.operation.util;

import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.bool;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.decimal;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.integer;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.string;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.ibm.watsonhealth.fhir.model.ObjectFactory;
import com.ibm.watsonhealth.fhir.model.OperationDefinition;
import com.ibm.watsonhealth.fhir.model.OperationDefinitionParameter;
import com.ibm.watsonhealth.fhir.model.OperationParameterUseList;
import com.ibm.watsonhealth.fhir.model.Parameters;
import com.ibm.watsonhealth.fhir.model.ParametersParameter;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.model.ResourceContainer;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.operation.exception.FHIROperationException;

public class FHIROperationUtil {
    private static ObjectFactory factory = new ObjectFactory();
    
    private FHIROperationUtil() { }
    
    public static Parameters getInputParameters(OperationDefinition definition, Map<String, List<String>> queryParameters) throws FHIROperationException {
        try {
            Parameters parameters = factory.createParameters();
            if (definition != null) {
                for (OperationDefinitionParameter parameter : definition.getParameter()) {
                    if (OperationParameterUseList.IN.equals(parameter.getUse().getValue())) {
                        String name = parameter.getName().getValue();
                        String typeName = parameter.getType().getValue();                
                        List<String> values = queryParameters.get(name);
                        if (values != null) {
                            String value = values.get(0);
                            ParametersParameter parametersParameter = factory.createParametersParameter();
                            parametersParameter.setName(string(name));
                            if ("string".equals(typeName)) {
                                parametersParameter.setValueString(string(value));
                            } else if ("boolean".equals(typeName)) {
                                parametersParameter.setValueBoolean(bool(Boolean.valueOf(value)));
                            } else if ("decimal".equals(typeName)) {
                                parametersParameter.setValueDecimal(decimal(new BigDecimal(value)));
                            } else if ("integer".equals(typeName)) {
                                parametersParameter.setValueInteger(integer(Integer.valueOf(value)));
                            } else {
                                throw new FHIROperationException("Invalid parameter type: '" + typeName + "'");
                            }
                            parameters.getParameter().add(parametersParameter);
                        }
                    }
                }
            }
            return parameters;
        } catch (FHIROperationException e) {
            throw e;
        } catch (Exception e) {
            throw new FHIROperationException("Unable to process query parameters", e);
        }
    }

    public static Parameters getInputParameters(OperationDefinition definition, Resource resource) throws Exception {
        Parameters parameters = factory.createParameters();
        for (OperationDefinitionParameter parameterDefinition : definition.getParameter()) {
            String parameterTypeName = parameterDefinition.getType().getValue();
            String resourceTypeName = resource.getClass().getSimpleName();
            if (resourceTypeName.equals(parameterTypeName) && OperationParameterUseList.IN.equals(parameterDefinition.getUse().getValue())) {
                ParametersParameter parameter = factory.createParametersParameter();
                parameter.setName(string(parameterDefinition.getName().getValue()));
                ResourceContainer container = factory.createResourceContainer();
                FHIRUtil.setResourceContainerResource(container, resource);
                parameter.setResource(container);
                parameters.getParameter().add(parameter);
            }
        }
        return parameters;
    }
    
    
    public static Parameters getOutputParameters(Resource resource) throws Exception {
        Parameters parameters = factory.createParameters();        
        ParametersParameter parameter = factory.createParametersParameter();
        parameter.setName(string("return"));
        ResourceContainer container = factory.createResourceContainer();
        FHIRUtil.setResourceContainerResource(container, resource);
        parameter.setResource(container);
        parameters.getParameter().add(parameter);
        return parameters;
    }
    
    public static boolean hasSingleResourceOutputParameter(Parameters parameters) {
        return parameters.getParameter().size() == 1 && 
                "return".equals(parameters.getParameter().get(0).getName().getValue()) &&
                parameters.getParameter().get(0).getResource() != null;
    }
    
    public static Resource getSingleResourceOutputParameter(Parameters parameters) throws Exception {
        return FHIRUtil.getResourceContainerResource(parameters.getParameter().get(0).getResource());
    }
}
