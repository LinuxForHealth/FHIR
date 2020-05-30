/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.term;

import static com.ibm.fhir.model.util.ModelSupport.FHIR_STRING;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.ConceptMap;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.operation.AbstractOperation;
import com.ibm.fhir.operation.context.FHIROperationContext;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.rest.FHIRResourceHelpers;
import com.ibm.fhir.term.service.FHIRTermService;
import com.ibm.fhir.term.spi.TranslationOutcome;
import com.ibm.fhir.term.spi.TranslationParameters;
import com.ibm.fhir.term.util.ConceptMapSupport;

public class TranslateOperation extends AbstractOperation {
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
            ConceptMap conceptMap = getConceptMap(operationContext, logicalId, parameters, resourceHelper);
            FHIRTermService service = FHIRTermService.getInstance();
            Coding coding = getCoding(parameters);
            TranslationOutcome outcome = service.translate(conceptMap, coding, TranslationParameters.from(parameters));
            return outcome.toParameters();
        } catch (FHIROperationException e) {
            throw e;
        } catch (Exception e) {
            throw new FHIROperationException("An error occurred during the ConceptMap translate operation", e);
        }
    }

    private ConceptMap getConceptMap(FHIROperationContext operationContext, String logicalId, Parameters parameters, FHIRResourceHelpers resourceHelper) throws Exception {
        if (FHIROperationContext.Type.INSTANCE.equals(operationContext.getType())) {
            Resource resource = resourceHelper.doRead("ConceptMap", logicalId, false, false, null, null);
            if (resource == null) {
                throw new FHIROperationException("ConceptMap with id '" + logicalId + "' was not found");
            }
            return (ConceptMap) resource;
        }
        Parameter urlParameter = getParameter(parameters, "url");
        if (urlParameter != null) {
            String url = urlParameter.getValue().as(Uri.class).getValue();
            ConceptMap conceptMap = ConceptMapSupport.getConceptMap(url);
            if (conceptMap == null) {
                throw new FHIROperationException("ConceptMap with url '" + url + "' is not available");
            }
            return conceptMap;
        }
        Parameter conceptMapParameter = getParameter(parameters, "conceptMap");
        if (conceptMapParameter != null) {
            Resource resource = conceptMapParameter.getResource();
            if (!(resource instanceof ConceptMap)) {
                throw new FHIROperationException("Parameter with name 'conceptMap' does not contain a ConceptMap resource");
            }
            return (ConceptMap) resource;
        }
        throw new FHIROperationException("Parameter with name 'conceptMap' was not found");
    }

    private Coding getCoding(Parameters parameters) throws FHIROperationException {
        Parameter codeableConceptParameter = getParameter(parameters, "codeableConcept");
        if (codeableConceptParameter != null) {
            throw new FHIROperationException("Parameter with name 'codeableConcept' is not supported");
        }
        Parameter codingParameter = getParameter(parameters, "coding");
        if (codingParameter != null) {
            return codingParameter.getValue().as(Coding.class);
        }
        Parameter systemParameter = getParameter(parameters, "system");
        if (systemParameter == null) {
            throw new FHIROperationException("Parameter with name 'system' was not found");
        }
        Parameter codeParameter = getParameter(parameters, "code");
        if (codeParameter == null) {
            throw new FHIROperationException("Parameter with name 'system' was not found");
        }
        Parameter versionParameter = getParameter(parameters, "version");
        return Coding.builder()
                .system(systemParameter.getValue().as(Uri.class))
                .version((versionParameter != null) ? versionParameter.getValue().as(FHIR_STRING) : null)
                .code(codeParameter.getValue().as(Code.class))
                .build();
    }
}