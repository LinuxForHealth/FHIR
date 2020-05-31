/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.term;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.ConceptMap;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.operation.context.FHIROperationContext;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.rest.FHIRResourceHelpers;
import com.ibm.fhir.term.service.FHIRTermService;
import com.ibm.fhir.term.spi.TranslationOutcome;
import com.ibm.fhir.term.spi.TranslationParameters;

public class TranslateOperation extends AbstractTermOperation {
    @Override
    protected OperationDefinition buildOperationDefinition() {
        return FHIRRegistry.getInstance().getResource("http://hl7.org/fhir/OperationDefinition/ConceptMap-translate", OperationDefinition.class);
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
            ConceptMap conceptMap = getResource(operationContext, logicalId, parameters, resourceHelper, ConceptMap.class);
            FHIRTermService service = FHIRTermService.getInstance();
            Coding coding = getCodedElement(parameters);
            TranslationOutcome outcome = service.translate(conceptMap, coding, TranslationParameters.from(parameters));
            return outcome.toParameters();
        } catch (FHIROperationException e) {
            throw e;
        } catch (Exception e) {
            throw new FHIROperationException("An error occurred during the ConceptMap translate operation", e);
        }
    }

    @Override
    protected Coding getCodedElement(Parameters parameters) throws FHIROperationException {
        Element codedElement = super.getCodedElement(parameters);
        if (codedElement instanceof CodeableConcept) {
            throw new FHIROperationException("Parameter with name 'codeableConcept' is not supported");
        }
        return (Coding) codedElement;
    }
}