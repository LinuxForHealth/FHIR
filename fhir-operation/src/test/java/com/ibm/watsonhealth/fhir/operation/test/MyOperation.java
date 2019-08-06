/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.operation.test;

import com.ibm.watsonhealth.fhir.exception.FHIROperationException;
import com.ibm.watsonhealth.fhir.model.resource.OperationDefinition;
import com.ibm.watsonhealth.fhir.model.type.OperationKind;
import com.ibm.watsonhealth.fhir.model.type.OperationParameterUse;
import com.ibm.watsonhealth.fhir.model.type.PublicationStatus;
import com.ibm.watsonhealth.fhir.model.resource.Parameters;
import com.ibm.watsonhealth.fhir.model.resource.Parameters.Parameter;
import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.operation.AbstractOperation;
import com.ibm.watsonhealth.fhir.operation.context.FHIROperationContext;
import com.ibm.watsonhealth.fhir.rest.FHIRResourceHelpers;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.FHIRAllTypes;
import com.ibm.watsonhealth.fhir.model.type.Integer;


public class MyOperation extends AbstractOperation {
    @Override
    protected OperationDefinition buildOperationDefinition() {
        OperationDefinition.Builder OperationDefinitionBuilder =  OperationDefinition.builder().name(String.of("My Operation")).status( 
        		PublicationStatus.of(PublicationStatus.ValueSet.DRAFT)).kind(OperationKind.of(OperationKind.ValueSet.OPERATION)).code(Code.of("hello")).affectsState(Boolean.of(true)).experimental( Boolean.of(true)).instance(Boolean.of(false));
        
        OperationDefinition.Parameter.Builder inputParameterBuilder = OperationDefinition.Parameter.builder().name(Code.of("input")).use( 
            OperationParameterUse.OUT).min(Integer.of(1)).id("1");
        
        OperationDefinitionBuilder.parameter(inputParameterBuilder.type(FHIRAllTypes.STRING).build());
               
        OperationDefinition.Parameter.Builder outputParameterBuilder = OperationDefinition.Parameter.builder().name(Code.of("output")).use( 
        		OperationParameterUse.OUT).min(Integer.of(1)).id("1");
        
        OperationDefinitionBuilder.parameter(outputParameterBuilder.type(FHIRAllTypes.STRING).build());

        
        return OperationDefinitionBuilder.build();
    }

    @Override
    protected Parameters doInvoke(FHIROperationContext context, Class<? extends Resource> resourceType, java.lang.String logicalId, java.lang.String versionId, Parameters parameters, FHIRResourceHelpers resourceHelper)
        throws FHIROperationException {
        try {
            Parameter inputParameter = parameters.getParameter().get(0);

            return Parameters.builder().parameter(Parameter.builder().name(String.of("output")).value(inputParameter.getValue()).build()).build();
        } catch (Exception e) {
            throw new FHIROperationException("An error occured invoking operation: " + getName(), e);
        }
    }
}
