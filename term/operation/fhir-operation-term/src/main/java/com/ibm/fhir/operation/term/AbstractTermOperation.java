/*
 * (C) Copyright IBM Corp. 2020, 2022
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
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.search.util.SearchHelper;
import com.ibm.fhir.server.spi.operation.AbstractOperation;
import com.ibm.fhir.server.spi.operation.FHIROperationContext;
import com.ibm.fhir.server.spi.operation.FHIRResourceHelpers;
import com.ibm.fhir.term.service.FHIRTermService;

public abstract class AbstractTermOperation extends AbstractOperation {
    protected final FHIRTermService service;

    public AbstractTermOperation() {
        service = FHIRTermService.getInstance();
    }

    @Override
    protected abstract OperationDefinition buildOperationDefinition();

    @Override
    protected abstract Parameters doInvoke(
            FHIROperationContext operationContext,
            Class<? extends Resource> resourceType,
            String logicalId,
            String versionId,
            Parameters parameters,
            FHIRResourceHelpers resourceHelper,
            SearchHelper searchHelper) throws FHIROperationException;

    protected <T extends Resource> T getResource(
            FHIROperationContext operationContext,
            String logicalId,
            Parameters parameters,
            FHIRResourceHelpers resourceHelper,
            Class<T> resourceType) throws Exception {
        String resourceTypeName = resourceType.getSimpleName();
        String resourceParameterName = resourceTypeName.substring(0, 1).toLowerCase() + resourceTypeName.substring(1);
        String resourceVersionParameterName = resourceParameterName + "Version";
        if (FHIROperationContext.Type.INSTANCE.equals(operationContext.getType())) {
            Resource resource = resourceHelper.doRead(resourceTypeName, logicalId, false).getResource();
            if (resource == null) {
                throw buildExceptionWithIssue(resourceTypeName + " with id '" + logicalId + "' was not found", IssueType.NOT_FOUND);
            }
            return resourceType.cast(resource);
        }
        Parameter urlParameter = getParameter(parameters, "url");
        if (urlParameter != null) {
            String url = urlParameter.getValue().as(Uri.class).getValue();
            Parameter resourceVersionParameter = getParameter(parameters, resourceVersionParameterName);
            if (resourceVersionParameter != null) {
                url = url + "|" + resourceVersionParameter.getValue().as(FHIR_STRING).getValue();
            }
            T resource = FHIRRegistry.getInstance().getResource(url, resourceType);
            if (resource == null) {
                throw buildExceptionWithIssue(resourceTypeName + " with url '" + url + "' is not available", IssueType.NOT_SUPPORTED);
            }
            return resource;
        }
        Parameter resourceParameter = getParameter(parameters, resourceParameterName);
        if (resourceParameter != null) {
            Resource resource = resourceParameter.getResource();
            if (!resourceType.isInstance(resource)) {
                throw buildExceptionWithIssue("Parameter with name '" + resourceParameterName + "' does not contain a " + resourceTypeName + " resource", IssueType.INVALID);
            }
            return resourceType.cast(resource);
        }
        throw buildExceptionWithIssue("Parameter with name '" + resourceParameterName + "' was not found", IssueType.INVALID);
    }

    protected Element getCodedElement(
            Parameters parameters,
            String codeableConceptParameterName,
            String codingParameterName,
            String codeParameterName) throws FHIROperationException {
        return getCodedElement(parameters, codeableConceptParameterName, codingParameterName, codeParameterName, true);
    }

    protected Element getCodedElement(
            Parameters parameters,
            String codeableConceptParameterName,
            String codingParameterName,
            String codeParameterName,
            boolean systemRequired) throws FHIROperationException {
        Parameter codeableConceptParameter = getParameter(parameters, codeableConceptParameterName);
        if (codeableConceptParameter != null) {
            return codeableConceptParameter.getValue().as(CodeableConcept.class);
        }
        return getCoding(parameters, codingParameterName, codeParameterName, systemRequired);
    }

    protected Coding getCoding(
        Parameters parameters,
        String codingParameterName,
        String codeParameterName) throws FHIROperationException {
        return getCoding(parameters, codingParameterName, codeParameterName, true);
    }

    protected Coding getCoding(
            Parameters parameters,
            String codingParameterName,
            String codeParameterName,
            boolean systemRequired) throws FHIROperationException {
        Parameter codingParameter = getParameter(parameters, codingParameterName);
        if (codingParameter != null) {
            return codingParameter.getValue().as(Coding.class);
        }
        Parameter systemParameter = getParameter(parameters, "system");
        if (systemRequired && systemParameter == null) {
            throw buildExceptionWithIssue("Parameter with name 'system' was not found", IssueType.INVALID);
        }
        Parameter codeParameter = getParameter(parameters, codeParameterName);
        if (codeParameter == null) {
            throw buildExceptionWithIssue("Parameter with name '" + codeParameterName + "' was not found", IssueType.INVALID);
        }
        Parameter versionParameter = getParameter(parameters, "version");
        Parameter displayParameter = getParameter(parameters, "display");
        return Coding.builder()
                .system((systemParameter != null) ? systemParameter.getValue().as(Uri.class) : null)
                .version((versionParameter != null) ? versionParameter.getValue().as(FHIR_STRING) : null)
                .code(codeParameter.getValue().as(Code.class))
                .display((displayParameter != null) ? displayParameter.getValue().as(FHIR_STRING) : null)
                .build();
    }
}
