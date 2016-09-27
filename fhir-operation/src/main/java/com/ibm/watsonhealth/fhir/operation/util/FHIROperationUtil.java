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
import com.ibm.watsonhealth.fhir.model.Parameters;
import com.ibm.watsonhealth.fhir.model.ParametersParameter;
import com.ibm.watsonhealth.fhir.operation.exception.FHIROperationException;

public class FHIROperationUtil {
    private static ObjectFactory factory = new ObjectFactory();
    
    private FHIROperationUtil() { }
    
    public static Parameters getParameters(OperationDefinition definition, Map<String, List<String>> queryParameters) throws FHIROperationException {
        try {
            if (definition == null) {
                return null;
            }
            
            Parameters parameters = factory.createParameters();
            
            for (OperationDefinitionParameter parameter : definition.getParameter()) {
                String name = parameter.getName().getValue();
                String typeName = parameter.getType().getValue();
                
                List<String> values = queryParameters.get(name);
                String value = values.get(0);   // first value only
                
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
            
            return !parameters.getParameter().isEmpty() ? parameters : null;
        } catch (FHIROperationException e) {
            throw e;
        } catch (Exception e) {
            throw new FHIROperationException("Unable to process query parameters", e);
        }
    }
}
