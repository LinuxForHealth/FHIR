/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.operation.test;

import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.string;

import com.ibm.watsonhealth.fhir.model.ObjectFactory;
import com.ibm.watsonhealth.fhir.model.OperationDefinition;
import com.ibm.watsonhealth.fhir.model.Parameters;
import com.ibm.watsonhealth.fhir.model.ParametersParameter;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.operation.AbstractOperation;
import com.ibm.watsonhealth.fhir.operation.exception.FHIROperationException;
import com.ibm.watsonhealth.fhir.persistence.FHIRPersistence;

public class MyOperation extends AbstractOperation {
    @Override
    public String getName() {
        return "hello";
    }

    @Override
    public Resource invoke(Resource resource, FHIRPersistence persistence) throws FHIROperationException {
        try {
            Parameters inputParameters = (Parameters) resource;
            ParametersParameter inputParameter = inputParameters.getParameter().get(0);
            String message = inputParameter.getValueString().getValue();
            ObjectFactory f = new ObjectFactory();
            Parameters parameters = f.createParameters();
            ParametersParameter parameter = f.createParametersParameter();
            parameter.setName(string("output"));
            parameter.setValueString(string(message));
            parameters.getParameter().add(parameter);
            return parameters;
        } catch (Exception e) {
            throw new FHIROperationException("An error occured invoking operation: " + getName(), e);
        }
    }

    @Override
    public OperationDefinition getDefinition() {
        return null;
    }
}
