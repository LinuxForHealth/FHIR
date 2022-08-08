/*
 * (C) Copyright IBM Corp. 2016, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.operation.test;

import static org.linuxforhealth.fhir.model.type.String.string;

import java.util.Arrays;
import java.util.List;

import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.resource.OperationDefinition;
import org.linuxforhealth.fhir.model.resource.Parameters;
import org.linuxforhealth.fhir.model.resource.Parameters.Parameter;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.type.Boolean;
import org.linuxforhealth.fhir.model.type.Code;
import org.linuxforhealth.fhir.model.type.Integer;
import org.linuxforhealth.fhir.model.type.Uri;
import org.linuxforhealth.fhir.model.type.code.FHIRAllTypes;
import org.linuxforhealth.fhir.model.type.code.OperationKind;
import org.linuxforhealth.fhir.model.type.code.OperationParameterUse;
import org.linuxforhealth.fhir.model.type.code.PublicationStatus;
import org.linuxforhealth.fhir.search.util.SearchHelper;
import org.linuxforhealth.fhir.server.spi.operation.AbstractOperation;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationContext;
import org.linuxforhealth.fhir.server.spi.operation.FHIRResourceHelpers;


public class MyOperation extends AbstractOperation {
    @Override
    protected OperationDefinition buildOperationDefinition() {
        OperationDefinition.Builder operationDefinitionBuilder =  OperationDefinition.builder()
                .url(Uri.of("http://ibm.com/fhir/example/my-operation"))
                .version("4.1.0")
                .name("My Operation")
                .status(PublicationStatus.DRAFT)
                .kind(OperationKind.OPERATION)
                .code(Code.of("hello"))
                .affectsState(Boolean.of(false))
                .system(Boolean.of(true))
                .type(Boolean.of(false))
                .experimental(Boolean.of(true))
                .instance(Boolean.of(false));

        // All primitives except "xhtml" and "base64Binary"
        List<FHIRAllTypes> primitives = Arrays.asList(
            FHIRAllTypes.BOOLEAN, FHIRAllTypes.CANONICAL, FHIRAllTypes.CODE, FHIRAllTypes.DATE,
            FHIRAllTypes.DATE_TIME, FHIRAllTypes.ID, FHIRAllTypes.INSTANT, FHIRAllTypes.INTEGER,
            FHIRAllTypes.OID, FHIRAllTypes.POSITIVE_INT, FHIRAllTypes.STRING, FHIRAllTypes.TIME,
            FHIRAllTypes.UNSIGNED_INT, FHIRAllTypes.URI, FHIRAllTypes.URL, FHIRAllTypes.UUID);

        for (FHIRAllTypes primitive : primitives) {
            OperationDefinition.Parameter inputParameter = OperationDefinition.Parameter.builder()
                    .name(Code.of("input-" + primitive.getValue()))
                    .use(OperationParameterUse.IN)
                    .min(Integer.of(0))
                    .max(string("1"))
                    .type(primitive)
                    .id("1")
                    .build();
            operationDefinitionBuilder.parameter(inputParameter);

            OperationDefinition.Parameter outputParameter = OperationDefinition.Parameter.builder()
                    .name(Code.of("output-" + primitive.getValue()))
                    .use(OperationParameterUse.OUT)
                    .min(Integer.of(0))
                    .max(string("1"))
                    .type(primitive)
                    .id("1")
                    .build();
            operationDefinitionBuilder.parameter(outputParameter);
        }

        return operationDefinitionBuilder.build();
    }

    @Override
    protected Parameters doInvoke(FHIROperationContext context, Class<? extends Resource> resourceType, String logicalId, String versionId,
            Parameters parameters, FHIRResourceHelpers resourceHelper, SearchHelper searchHelper) throws FHIROperationException {
        try {
            if (parameters.getParameter().isEmpty()) {
                return null;
            }

            Parameters.Builder returnParametersBuilder = Parameters.builder();

            for (Parameter inputParameter : parameters.getParameter()) {
                returnParametersBuilder.parameter(Parameter.builder()
                    .name(string(inputParameter.getName().getValue().replace("input", "output")))
                    .value(inputParameter.getValue())
                    .build());
            }

            return returnParametersBuilder.build();
        } catch (Exception e) {
            throw new FHIROperationException("An error occured invoking operation: " + getName(), e);
        }
    }
}
