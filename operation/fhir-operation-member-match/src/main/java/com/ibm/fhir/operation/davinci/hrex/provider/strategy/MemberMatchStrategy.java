/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.davinci.hrex.provider.strategy;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.server.spi.operation.FHIROperationContext;
import com.ibm.fhir.server.spi.operation.FHIRResourceHelpers;

/**
 * The Member Match Strategy
 */
public interface MemberMatchStrategy {

    /**
     * used to uniquely identify the strategy.
     * @implNote "default" is reserved.
     * @return the member match strategy identifier
     */
    String getMemberMatchIdentifier();

    /**
     * takes a set of input parameters
     * @param ctx
     * @param input
     * @param resourceHelper
     * @return
     * @throws FHIROperationException
     */
    Parameters execute(FHIROperationContext ctx, Parameters input, FHIRResourceHelpers resourceHelper) throws FHIROperationException;
}
