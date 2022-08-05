/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.operation.cqf;

import static org.linuxforhealth.fhir.cql.helpers.ModelHelper.fhirstring;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.linuxforhealth.fhir.cql.engine.model.FHIRModelResolver;
import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.MeasureReport;
import org.linuxforhealth.fhir.model.resource.OperationDefinition;
import org.linuxforhealth.fhir.model.resource.Parameters;
import org.linuxforhealth.fhir.model.resource.Parameters.Parameter;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.type.Reference;
import org.linuxforhealth.fhir.model.util.ModelSupport;
import org.linuxforhealth.fhir.model.util.ModelSupport.ElementInfo;
import org.linuxforhealth.fhir.persistence.SingleResourceResult;
import org.linuxforhealth.fhir.registry.FHIRRegistry;
import org.linuxforhealth.fhir.search.util.SearchHelper;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationContext;
import org.linuxforhealth.fhir.server.spi.operation.FHIRResourceHelpers;

public class MeasureCollectDataOperation extends EvaluateMeasureOperation {

    public static final String PARAM_OUT_MEASURE_REPORT = "measureReport";
    public static final String PARAM_OUT_RESOURCE = "resource";

    private FHIRModelResolver resolver = new FHIRModelResolver();

    @Override
    protected OperationDefinition buildOperationDefinition() {
        return FHIRRegistry.getInstance().getResource("http://hl7.org/fhir/OperationDefinition/Measure-collect-data", OperationDefinition.class);

    }

    @Override
    protected Parameters doInvoke(FHIROperationContext operationContext, Class<? extends Resource> resourceType, String logicalId, String versionId,
        Parameters parameters, FHIRResourceHelpers resourceHelper, SearchHelper searchHelper) throws FHIROperationException {

        // This approach is modeled directly on the cqf-ruler implementation. It seems to me that
        // running the measure in order to get the data requirements is a cop out and duplication
        // of effort.
        Parameters intermediateOutParams = super.doInvoke(operationContext, resourceType, logicalId, versionId, parameters, resourceHelper, searchHelper);
        Parameter returnParam = intermediateOutParams.getParameter().get(0);
        MeasureReport measureReport = (MeasureReport) returnParam.getResource();

        Parameters.Builder outParams = Parameters.builder();
        outParams.parameter( Parameter.builder().name(fhirstring(PARAM_OUT_MEASURE_REPORT)).resource(measureReport).build());

        if ( measureReport.getContained() != null ) {
            for ( Resource containedResource : measureReport.getContained() ) {
                // This is exactly how the cqf-ruler implementation is coded.
                // Right now, this is never going to fire based on both the
                // cqf-ruler and cql-evaluator implementations. This needs to
                // be discussed with the spec team and revisited as appropriate.
                if ( containedResource instanceof Bundle ) {
                    addEvaluatedResourcesToParameters((Bundle) containedResource, outParams, resourceHelper);
                }
            }
        }

        return outParams.build();
    }

    protected void addEvaluatedResourcesToParameters( Bundle contained, Parameters.Builder parameters, FHIRResourceHelpers resourceHelper ) throws FHIROperationException {
        Map<String,Resource> resourceMap = new HashMap<>();
        if ( contained.getEntry() != null ) {
            for ( Bundle.Entry entry : contained.getEntry() ) {
                if ( entry.getResource() != null && !(entry.getResource() instanceof org.linuxforhealth.fhir.model.resource.List) ) {
                    if ( ! resourceMap.containsKey(entry.getResource().getId()) ) {
                        parameters.parameter(Parameter.builder().name(fhirstring(PARAM_OUT_RESOURCE)).resource(entry.getResource()).build());
                        resourceMap.put(entry.getResource().getId(), entry.getResource());

                        resolveReferences(entry.getResource(), parameters, resourceMap, resourceHelper);
                    }
                }
            }
        }
    }

    /**
     * Loop through all the data elements of the structure looking for things that are references. Pull
     * in each referenced resource and add it to the output.
     *
     * @throws FHIROperationException
     */
    protected void resolveReferences(Resource resource, Parameters.Builder parameters, Map<String, Resource> resourceMap, FHIRResourceHelpers resourceHelper) throws FHIROperationException {

        Collection<ElementInfo> elementInfos = ModelSupport.getElementInfo(resource.getClass());
        for ( ElementInfo elementInfo : elementInfos ) {
            Set<String> referenceTypes = elementInfo.getReferenceTypes();
            if ( referenceTypes.size() > 0 ) {
                Reference reference = (Reference) resolver.resolvePath(resource,  elementInfo.getName());
                if ( reference != null && reference.getReference() != null && reference.getReference().getValue() != null) {
                    String refValue = reference.getReference().getValue();
                    String[] parts = refValue.split("/");
                    if ( parts.length == 2 ) {
                        String type = parts[0];
                        String id = parts[1];

                        if ( ! resourceMap.containsKey(id) ) {
                            try {
                                SingleResourceResult<? extends Resource> readResult = resourceHelper.doRead(type, id);
                                if ( readResult.getResource() == null) {
                                    throw new FHIROperationException("Failed to read referenced resource: " + refValue);
                                }
                                Resource fetchedResource = readResult.getResource();
                                parameters.parameter(Parameter.builder().name(fhirstring(PARAM_OUT_RESOURCE)).resource(fetchedResource).build());
                                resourceMap.put(id, resource);
                            } catch ( FHIROperationException fex ) {
                                throw fex;
                            } catch ( Exception ex ) {
                                throw new FHIROperationException("Unexpected error while reading library: " + refValue, ex);
                            }
                        }
                    } else {
                        throw new FHIROperationException("Expected a relative reference with two parts, but found " + refValue);
                    }
                }
            }
        }
    }

}
