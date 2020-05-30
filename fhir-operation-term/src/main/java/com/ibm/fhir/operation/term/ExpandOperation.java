/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.term;

import static com.ibm.fhir.operation.util.FHIROperationUtil.getOutputParameters;
import static com.ibm.fhir.term.util.ValueSetSupport.isExpanded;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.operation.AbstractOperation;
import com.ibm.fhir.operation.context.FHIROperationContext;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.rest.FHIRResourceHelpers;
import com.ibm.fhir.term.service.FHIRTermService;
import com.ibm.fhir.term.spi.ExpansionParameters;
import com.ibm.fhir.term.util.ValueSetSupport;

public class ExpandOperation extends AbstractOperation {
    @Override
    protected OperationDefinition buildOperationDefinition() {
        return FHIRRegistry.getInstance().getResource("http://hl7.org/fhir/OperationDefinition/ValueSet-expand", OperationDefinition.class);
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
            ValueSet valueSet = getValueSet(operationContext, logicalId, parameters, resourceHelper);
            FHIRTermService service = FHIRTermService.getInstance();

            if (!isExpanded(valueSet) && !service.isExpandable(valueSet)) {
                String url = (valueSet.getUrl() != null) ? valueSet.getUrl().getValue() : null;
                throw new FHIROperationException("ValueSet with url '" + url + "' is not expandable");
            }

            ValueSet expanded = service.expand(valueSet, ExpansionParameters.from(parameters));

            return getOutputParameters(expanded);
        } catch (FHIROperationException e) {
            throw e;
        } catch (Exception e) {
            throw new FHIROperationException("An error occurred during the ValueSet expand operation", e);
        }
    }

    private ValueSet getValueSet(FHIROperationContext operationContext, String logicalId, Parameters parameters, FHIRResourceHelpers resourceHelper) throws Exception {
        if (FHIROperationContext.Type.INSTANCE.equals(operationContext.getType())) {
            Resource resource = resourceHelper.doRead("ValueSet", logicalId, false, false, null, null);
            if (resource == null) {
                throw new FHIROperationException("ValueSet with id '" + logicalId + "' was not found");
            }
            return (ValueSet) resource;
        }
        Parameter urlParameter = getParameter(parameters, "url");
        if (urlParameter != null) {
            String url = urlParameter.getValue().as(Uri.class).getValue();
            ValueSet valueSet = ValueSetSupport.getValueSet(url);
            if (valueSet == null) {
                throw new FHIROperationException("ValueSet with url '" + url + "' is not available");
            }
            return valueSet;
        }
        Parameter valueSetParameter = getParameter(parameters, "valueSet");
        if (valueSetParameter != null) {
            Resource resource = valueSetParameter.getResource();
            if (!(resource instanceof ValueSet)) {
                throw new FHIROperationException("Parameter with name 'valueSet' does not contain a ValueSet resource");
            }
            return (ValueSet) resource;
        }
        throw new FHIROperationException("Parameter with name 'valueSet' was not found");
    }
}
