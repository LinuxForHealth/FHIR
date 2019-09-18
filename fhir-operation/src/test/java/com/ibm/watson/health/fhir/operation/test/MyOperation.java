/**
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.operation.test;

import static com.ibm.watson.health.fhir.model.type.String.string;

import com.ibm.watson.health.fhir.exception.FHIROperationException;
import com.ibm.watson.health.fhir.model.resource.OperationDefinition;
import com.ibm.watson.health.fhir.model.resource.Parameters;
import com.ibm.watson.health.fhir.model.resource.Resource;
import com.ibm.watson.health.fhir.model.resource.Parameters.Parameter;
import com.ibm.watson.health.fhir.model.type.Boolean;
import com.ibm.watson.health.fhir.model.type.Code;
import com.ibm.watson.health.fhir.model.type.FHIRAllTypes;
import com.ibm.watson.health.fhir.model.type.Integer;
import com.ibm.watson.health.fhir.model.type.OperationKind;
import com.ibm.watson.health.fhir.model.type.OperationParameterUse;
import com.ibm.watson.health.fhir.model.type.PublicationStatus;
import com.ibm.watson.health.fhir.model.type.String;
import com.ibm.watson.health.fhir.operation.AbstractOperation;
import com.ibm.watson.health.fhir.operation.context.FHIROperationContext;
import com.ibm.watson.health.fhir.rest.FHIRResourceHelpers;


public class MyOperation extends AbstractOperation {
    @Override
    protected OperationDefinition buildOperationDefinition() {
        OperationDefinition.Builder OperationDefinitionBuilder =  OperationDefinition.builder()
                                                                            .name(String.of("My Operation"))
                                                                            .status(PublicationStatus.of(PublicationStatus.ValueSet.DRAFT))
                                                                            .kind(OperationKind.of(OperationKind.ValueSet.OPERATION))
                                                                            .code(Code.of("hello"))
                                                                            .affectsState(Boolean.of(true))
                                                                            .system(Boolean.of(true))
                                                                            .type(Boolean.of(false))
                                                                            .experimental(Boolean.of(true))
                                                                            .instance(Boolean.of(false));
        
        OperationDefinition.Parameter inputParameter = OperationDefinition.Parameter.builder()
                                                                            .name(Code.of("input"))
                                                                            .use(OperationParameterUse.IN)
                                                                            .min(Integer.of(1))
                                                                            .max(string("1"))
                                                                            .type(FHIRAllTypes.STRING)
                                                                            .id("1")
                                                                            .build();
        OperationDefinitionBuilder.parameter(inputParameter);
               
        OperationDefinition.Parameter outputParameter = OperationDefinition.Parameter.builder()
                                                                            .name(Code.of("output"))
                                                                            .use(OperationParameterUse.OUT)
                                                                            .min(Integer.of(1))
                                                                            .max(string("1"))
                                                                            .type(FHIRAllTypes.STRING)
                                                                            .id("1")
                                                                            .build();
        OperationDefinitionBuilder.parameter(outputParameter);

        return OperationDefinitionBuilder.build();
    }

    @Override
    protected Parameters doInvoke(FHIROperationContext context, Class<? extends Resource> resourceType, java.lang.String logicalId, java.lang.String versionId, Parameters parameters, FHIRResourceHelpers resourceHelper)
        throws FHIROperationException {
        try {
            Parameter inputParameter = parameters.getParameter().get(0);

            return Parameters.builder()
                        .parameter(Parameter.builder()
                                    .name(String.of("output"))
                                    .value(inputParameter.getValue())
                                    .build())
                        .build();
        } catch (Exception e) {
            throw new FHIROperationException("An error occured invoking operation: " + getName(), e);
        }
    }
}
