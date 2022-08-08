/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.operation.davinci.hrex.provider.strategy;

import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.resource.Parameters;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationContext;
import org.linuxforhealth.fhir.server.spi.operation.FHIRResourceHelpers;

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
