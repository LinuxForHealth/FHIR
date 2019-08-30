/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.operation.util;

import static com.ibm.watson.health.fhir.model.type.String.string;

import java.util.List;
import java.util.Map;

import com.ibm.watson.health.fhir.exception.FHIROperationException;
import com.ibm.watson.health.fhir.model.resource.OperationDefinition;
import com.ibm.watson.health.fhir.model.resource.Parameters;
import com.ibm.watson.health.fhir.model.resource.Resource;
import com.ibm.watson.health.fhir.model.resource.Parameters.Parameter;
import com.ibm.watson.health.fhir.model.type.Id;
import com.ibm.watson.health.fhir.model.type.OperationParameterUse;

public class FHIROperationUtil {

    private FHIROperationUtil() {
    }

    public static Parameters getInputParameters(OperationDefinition definition,
        Map<String, List<String>> queryParameters) throws FHIROperationException {
        try {
            Parameters.Builder parametersBuilder = Parameters.builder();
            parametersBuilder.id(Id.of("InputParameters"));
            if (definition != null) {

                for (OperationDefinition.Parameter parameter : definition.getParameter()) {
                    if (OperationParameterUse.IN.getValue().equals(parameter.getUse().getValue())) {
                        String name = parameter.getName().getValue();
                        String typeName = parameter.getType().getValue();
                        List<String> values = queryParameters.get(name);
                        if (values != null) {
                            String value = values.get(0);
                            Parameter.Builder parameterBuilder =
                                    Parameter.builder().name(string(name));
                            if ("string".equals(typeName)) {
                                parameterBuilder.value(string(value));
                            } else if ("boolean".equals(typeName)) {
                                parameterBuilder.value(com.ibm.watson.health.fhir.model.type.Boolean.of(value));
                            } else if ("decimal".equals(typeName)) {
                                parameterBuilder.value(com.ibm.watson.health.fhir.model.type.Decimal.of(value));
                            } else if ("integer".equals(typeName)) {
                                parameterBuilder.value(com.ibm.watson.health.fhir.model.type.Integer.of(value));
                            } else {
                                throw new FHIROperationException("Invalid parameter type: '"
                                        + typeName + "'");
                            }
                            parametersBuilder.parameter(parameterBuilder.build());
                        }
                    }
                }
            }
            return parametersBuilder.build();
        } catch (FHIROperationException e) {
            throw e;
        } catch (Exception e) {
            throw new FHIROperationException("Unable to process query parameters", e);
        }
    }


    public static Parameters getInputParameters(OperationDefinition definition, Resource resource)
        throws Exception {
        Parameters.Builder parametersBuilder = Parameters.builder();
        parametersBuilder.id(Id.of("InputParameters"));
        for (OperationDefinition.Parameter parameterDefinition : definition.getParameter()) {
            String parameterTypeName = parameterDefinition.getType().getValue();
            String resourceTypeName = resource.getClass().getSimpleName();
            if ((resourceTypeName.equals(parameterTypeName) || "Resource".equals(parameterTypeName))
                    && OperationParameterUse.IN.getValue().equals(parameterDefinition.getUse().getValue())) {
                Parameter.Builder parameterBuilder =
                        Parameter.builder().name(string(parameterDefinition.getName().getValue()));
                parametersBuilder.parameter(parameterBuilder.resource(resource).build());
            }
        }
        return parametersBuilder.build();
    }

    public static Parameters getOutputParameters(Resource resource) throws Exception {
        Parameters.Builder parametersBuilder = Parameters.builder();
        parametersBuilder.parameter(Parameter.builder().name(string("return")).resource(resource).build());
        return parametersBuilder.build();
    }

    public static boolean hasSingleResourceOutputParameter(Parameters parameters) {
        return parameters.getParameter().size() == 1 &&
                "return".equals(parameters.getParameter().get(0).getName().getValue()) &&
                parameters.getParameter().get(0).getResource() != null;
    }

    public static Resource getSingleResourceOutputParameter(Parameters parameters)
        throws Exception {
        return parameters.getParameter().get(0).getResource();
    }
}
