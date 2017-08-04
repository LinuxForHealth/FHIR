/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.operation.test;

import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.bool;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.code;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.integer;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.string;

import com.ibm.watsonhealth.fhir.model.ObjectFactory;
import com.ibm.watsonhealth.fhir.model.OperationDefinition;
import com.ibm.watsonhealth.fhir.model.OperationDefinitionParameter;
import com.ibm.watsonhealth.fhir.model.OperationKind;
import com.ibm.watsonhealth.fhir.model.OperationKindList;
import com.ibm.watsonhealth.fhir.model.OperationParameterUseList;
import com.ibm.watsonhealth.fhir.model.Parameters;
import com.ibm.watsonhealth.fhir.model.ParametersParameter;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.operation.AbstractOperation;
import com.ibm.watsonhealth.fhir.operation.context.FHIROperationContext;
import com.ibm.watsonhealth.fhir.operation.exception.FHIROperationException;
import com.ibm.watsonhealth.fhir.persistence.FHIRPersistence;

public class MyOperation extends AbstractOperation {
    @Override
    protected OperationDefinition buildOperationDefinition() {
        OperationDefinition definition = factory.createOperationDefinition();
        definition.setName(string("My Operation"));
        definition.setStatus(code("draft"));
        definition.setKind(factory.createOperationKind().withValue(OperationKindList.OPERATION));
        definition.setCode(code("hello"));
        definition.setSystem(bool(true));
        definition.setInstance(bool(false));
        
        OperationDefinitionParameter inputParameter = factory.createOperationDefinitionParameter();
        inputParameter.setName(code("input"));
        inputParameter.setType(code("string"));
        inputParameter.setMin(integer(1));
        inputParameter.setMax(string("1"));
        inputParameter.setUse(factory.createOperationParameterUse().withValue(OperationParameterUseList.IN));
        definition.getParameter().add(inputParameter);
        
        OperationDefinitionParameter outputParameter = factory.createOperationDefinitionParameter();
        outputParameter.setName(code("output"));
        outputParameter.setType(code("string"));
        outputParameter.setMin(integer(1));
        outputParameter.setMax(string("1"));
        outputParameter.setUse(factory.createOperationParameterUse().withValue(OperationParameterUseList.OUT));
        definition.getParameter().add(outputParameter);
        
        return definition;
    }

    @Override
    protected Parameters doInvoke(FHIROperationContext context, Class<? extends Resource> resourceType, String logicalId, String versionId, Parameters parameters, FHIRPersistence persistence)
        throws FHIROperationException {
        try {
            ParametersParameter inputParameter = parameters.getParameter().get(0);
            String message = inputParameter.getValueString().getValue();
            ObjectFactory f = new ObjectFactory();
            Parameters result = f.createParameters();
            ParametersParameter parameter = f.createParametersParameter();
            parameter.setName(string("output"));
            parameter.setValueString(string(message));
            result.getParameter().add(parameter);
            return result;
        } catch (Exception e) {
            throw new FHIROperationException("An error occured invoking operation: " + getName(), e);
        }
    }
}
