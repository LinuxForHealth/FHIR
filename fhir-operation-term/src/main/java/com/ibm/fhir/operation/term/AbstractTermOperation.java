/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.term;

import static com.ibm.fhir.model.util.ModelSupport.FHIR_STRING;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.operation.AbstractOperation;
import com.ibm.fhir.operation.context.FHIROperationContext;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.rest.FHIRResourceHelpers;

public abstract class AbstractTermOperation extends AbstractOperation {
    @Override
    protected abstract OperationDefinition buildOperationDefinition();

    @Override
    protected abstract Parameters doInvoke(
            FHIROperationContext operationContext,
            Class<? extends Resource> resourceType,
            String logicalId,
            String versionId,
            Parameters parameters,
            FHIRResourceHelpers resourceHelper) throws FHIROperationException;

    protected <T extends Resource> T getResource(
            FHIROperationContext operationContext,
            String logicalId,
            Parameters parameters,
            FHIRResourceHelpers resourceHelper,
            Class<T> resourceType) throws Exception {
        String resourceTypeName = resourceType.getSimpleName();
        if (FHIROperationContext.Type.INSTANCE.equals(operationContext.getType())) {
            Resource resource = resourceHelper.doRead(resourceTypeName, logicalId, false, false, null, null);
            if (resource == null) {
                throw new FHIROperationException(resourceTypeName + " with id '" + logicalId + "' was not found");
            }
            return resourceType.cast(resource);
        }
        Parameter urlParameter = getParameter(parameters, "url");
        if (urlParameter != null) {
            String url = urlParameter.getValue().as(Uri.class).getValue();
            T resource = FHIRRegistry.getInstance().getResource(url, resourceType);
            if (resource == null) {
                throw new FHIROperationException(resourceTypeName + " with url '" + url + "' is not available");
            }
            return resource;
        }
        String resourceParameterName = resourceTypeName.substring(0, 1).toLowerCase() + resourceTypeName.substring(1);
        Parameter resourceParameter = getParameter(parameters, resourceParameterName);
        if (resourceParameter != null) {
            Resource resource = resourceParameter.getResource();
            if (!resourceType.isInstance(resource)) {
                throw new FHIROperationException("Parameter with name '" + resourceParameterName + "' does not contain a " + resourceTypeName + " resource");
            }
            return resourceType.cast(resource);
        }
        throw new FHIROperationException("Parameter with name '" + resourceParameterName + "' was not found");
    }

    protected Element getCodedElement(Parameters parameters) throws FHIROperationException {
        Parameter codeableConceptParameter = getParameter(parameters, "codeableConcept");
        if (codeableConceptParameter != null) {
            return codeableConceptParameter.getValue().as(CodeableConcept.class);
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
            throw new FHIROperationException("Parameter with name 'code' was not found");
        }
        Parameter versionParameter = getParameter(parameters, "version");
        Parameter displayParameter = getParameter(parameters, "display");
        return Coding.builder()
                .system(systemParameter.getValue().as(Uri.class))
                .version((versionParameter != null) ? versionParameter.getValue().as(FHIR_STRING) : null)
                .code(codeParameter.getValue().as(Code.class))
                .display((displayParameter != null) ? displayParameter.getValue().as(FHIR_STRING) : null)
                .build();
    }
}