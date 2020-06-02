/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.term;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.operation.context.FHIROperationContext;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.rest.FHIRResourceHelpers;
import com.ibm.fhir.term.spi.LookupOutcome;
import com.ibm.fhir.term.spi.LookupParameters;

public class LookupOperation extends AbstractTermOperation {
    @Override
    protected OperationDefinition buildOperationDefinition() {
        return FHIRRegistry.getInstance().getResource("http://hl7.org/fhir/OperationDefinition/CodeSystem-lookup", OperationDefinition.class);
    }

    @Override
    protected Parameters doInvoke(
            FHIROperationContext operationContext,
            Class<? extends Resource> resourceType,
            String logicalId,
            String versionId,
            Parameters parameters,
            FHIRResourceHelpers resourceHelper) throws FHIROperationException {
        try {
            Coding coding = getCoding(parameters, "coding", "code");
            LookupOutcome outcome = service.lookup(coding, LookupParameters.from(parameters));
            if (outcome == null) {
                throw new FHIROperationException("Lookup cannot be performed");
            }
            return outcome.toParameters();
        } catch (Exception e) {
            throw new FHIROperationException("An error occurred during the CodeSystem lookup operation", e);
        }
    }
}