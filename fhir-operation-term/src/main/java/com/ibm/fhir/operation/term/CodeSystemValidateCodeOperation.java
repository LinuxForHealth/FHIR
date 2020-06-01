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
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.operation.context.FHIROperationContext;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.rest.FHIRResourceHelpers;
import com.ibm.fhir.term.spi.ValidationOutcome;
import com.ibm.fhir.term.spi.ValidationParameters;

public class CodeSystemValidateCodeOperation extends AbstractTermOperation {
    @Override
    protected OperationDefinition buildOperationDefinition() {
        return FHIRRegistry.getInstance().getResource("http://hl7.org/fhir/OperationDefinition/CodeSystem-validate-code", OperationDefinition.class);
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
            Element codedElement = getCodedElement(parameters, "codeableConcept", "coding", "code");
            ValidationOutcome outcome = codedElement.is(CodeableConcept.class) ?
                    service.validateCode(codedElement.as(CodeableConcept.class), ValidationParameters.from(parameters)) :
                    service.validateCode(codedElement.as(Coding.class), ValidationParameters.from(parameters));
            return outcome.toParameters();
        } catch (FHIROperationException e) {
            throw e;
        } catch (Exception e) {
            throw new FHIROperationException("An error occurred during the CodeSystem validate code operation", e);
        }
    }
}