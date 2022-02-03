/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.versions;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.server.spi.operation.AbstractOperation;
import com.ibm.fhir.server.spi.operation.FHIROperationContext;
import com.ibm.fhir.server.spi.operation.FHIRResourceHelpers;

public class VersionsOperation extends AbstractOperation {

    private static final String PARAM_VERSION = "version";
    private static final String PARAM_DEFAULT = "default";


    public VersionsOperation() {
        super();
    }

    @Override
    protected OperationDefinition buildOperationDefinition() {
        return FHIRRegistry.getInstance().getResource("http://hl7.org/fhir/OperationDefinition/CapabilityStatement-versions",
                OperationDefinition.class);
    }

    @Override
    protected Parameters doInvoke(FHIROperationContext operationContext, Class<? extends Resource> resourceType,
            String logicalId, String versionId, Parameters parameters, FHIRResourceHelpers resourceHelper)
            throws FHIROperationException {
        try {

            Parameters.Builder builder = Parameters.builder();
            builder.parameter(Parameter.builder()
                .name(PARAM_VERSION)
                .value(Code.of("4.0"))
                .build());
            builder.parameter(Parameter.builder()
                .name(PARAM_DEFAULT)
                .value(Code.of("4.0"))
                .build());
            return builder.build();

        } catch (Throwable t) {
            throw new FHIROperationException("Unexpected error occurred while processing request for operation '"
                    + getName() + "': " + getCausedByMessage(t), t);
        }
    }

    private String getCausedByMessage(Throwable throwable) {
        return throwable.getClass().getName() + ": " + throwable.getMessage();
    }
}