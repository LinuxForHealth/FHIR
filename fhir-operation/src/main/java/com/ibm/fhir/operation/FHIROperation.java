/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.operation.context.FHIROperationContext;
import com.ibm.fhir.rest.FHIRResourceHelpers;

public interface FHIROperation {
    String getName();

    Parameters invoke(FHIROperationContext operationContext, Class<? extends Resource> resourceType, String logicalId, String versionId, Parameters parameters,
        FHIRResourceHelpers resourceHelpers) throws FHIROperationException;

    OperationDefinition getDefinition();
}
