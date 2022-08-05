/*
 * (C) Copyright IBM Corp. 2016, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.spi.operation;

import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.resource.OperationDefinition;
import org.linuxforhealth.fhir.model.resource.Parameters;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.search.util.SearchHelper;

public interface FHIROperation {
    String getName();

    /**
     * Invoke the operation.
     *
     * @throws FHIROperationException
     *     if input or output parameters fail validation or an exception occurs
     */
    Parameters invoke(FHIROperationContext operationContext, Class<? extends Resource> resourceType, String logicalId, String versionId, Parameters parameters,
        FHIRResourceHelpers resourceHelpers, SearchHelper searchHelper) throws FHIROperationException;

    OperationDefinition getDefinition();
}
