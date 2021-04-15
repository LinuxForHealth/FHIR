/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.erase;

import java.io.InputStream;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.operation.erase.impl.Erase;
import com.ibm.fhir.operation.erase.impl.EraseFactory;
import com.ibm.fhir.server.operation.spi.AbstractOperation;
import com.ibm.fhir.server.operation.spi.FHIROperationContext;
import com.ibm.fhir.server.operation.spi.FHIRResourceHelpers;

/**
 * Custom Operation to Erase a specific instance of the FHIR Resource and it's history and values table on
 * the IBM FHIR Server.
 */
public class EraseOperation extends AbstractOperation {
    public EraseOperation() {
        super();
    }

    @Override
    protected OperationDefinition buildOperationDefinition() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("erase.json")) {
            return FHIRParser.parser(Format.JSON).parse(in);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    @Override
    protected Parameters doInvoke(FHIROperationContext operationContext, Class<? extends Resource> resourceType,
            String logicalId, String versionId, Parameters parameters, FHIRResourceHelpers resourceHelper) throws FHIROperationException {
        Erase erase = EraseFactory.getInstance(operationContext, resourceHelper, parameters, resourceType, logicalId, versionId);
        erase.authorize(); // Should be executed first so it is the first error before enabled check.
        erase.enabled();
        erase.verifyMethod();
        erase.verifyParameters();
        erase.exists();
        return erase.erase();
    }
}