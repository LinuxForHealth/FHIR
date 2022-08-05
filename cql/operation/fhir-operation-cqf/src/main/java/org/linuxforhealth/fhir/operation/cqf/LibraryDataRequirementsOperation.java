/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.operation.cqf;

import java.util.List;

import org.linuxforhealth.fhir.core.ResourceType;
import org.linuxforhealth.fhir.cql.helpers.LibraryHelper;
import org.linuxforhealth.fhir.cql.helpers.ParameterMap;
import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.resource.Library;
import org.linuxforhealth.fhir.model.resource.OperationDefinition;
import org.linuxforhealth.fhir.model.resource.Parameters;
import org.linuxforhealth.fhir.model.resource.Parameters.Parameter;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.persistence.SingleResourceResult;
import org.linuxforhealth.fhir.registry.FHIRRegistry;
import org.linuxforhealth.fhir.search.util.SearchHelper;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationContext;
import org.linuxforhealth.fhir.server.spi.operation.FHIRResourceHelpers;

public class LibraryDataRequirementsOperation extends AbstractDataRequirementsOperation {

    public static final String PARAM_IN_TARGET = "target";

    @Override
    protected OperationDefinition buildOperationDefinition() {
        return FHIRRegistry.getInstance().getResource("http://hl7.org/fhir/OperationDefinition/Library-data-requirements", OperationDefinition.class);
    }

    @Override
    public Parameters doInvoke(FHIROperationContext operationContext, Class<? extends Resource> resourceType, String logicalId, String versionId,
        Parameters parameters, FHIRResourceHelpers resourceHelper, SearchHelper searchHelper) throws FHIROperationException {

        Library library = null;

        try {
            if (operationContext.getType().equals(FHIROperationContext.Type.INSTANCE)) {
                SingleResourceResult<?> readResult = resourceHelper.doRead(ResourceType.LIBRARY.value(), logicalId);
                library = (Library) readResult.getResource();
                if (library == null) {
                    throw new FHIROperationException("Failed to resolve library with resource id: " + logicalId);
                }
            } else if (operationContext.getType().equals(FHIROperationContext.Type.SYSTEM)) {
                ParameterMap paramMap = new ParameterMap(parameters);
                Parameter p = paramMap.getSingletonParameter(PARAM_IN_TARGET);

                String target = ((org.linuxforhealth.fhir.model.type.String) p.getValue()).getValue();

                //The meaning of the target parameter is somewhat ambiguous in the base spec. I talked with the spec
                //author and the intention was for target to be a library ID. The operation should probably have been
                //defined instance level instead of system level. Things have improved in the QM IG, so I'm not going
                //to spend a lot of time trying to finagle this into something less confusing here. It can be improved
                //when we implement the IGs.
                //https://chat.fhir.org/#narrow/stream/179220-cql/topic/Library.20data-requirements.20operation

                String [] parts = target.split("/");

                logicalId = target;
                String targetType = ResourceType.LIBRARY.value();
                if( parts.length > 1 ) {
                    targetType = parts[ parts.length - 2 ];
                    logicalId = parts[ parts.length - 1 ];
                }

                SingleResourceResult<?> readResult = resourceHelper.doRead(targetType, logicalId);
                if (readResult.getResource() instanceof Library) {
                    library = (Library) readResult.getResource();
                } else {
                    throw new FHIROperationException("Failed to resolve library: " + target);
                }
            } else {
                throw new IllegalStateException("Unsupported context " + operationContext.getType().toString());
            }
        } catch (FHIROperationException fex) {
            throw fex;
        } catch (Exception ex) {
            throw new FHIROperationException("Unexpected error while reading Library", ex);
        }

        List<Library> fhirLibraries = LibraryHelper.loadLibraries(library);
        return doDataRequirements(fhirLibraries);
    }

}
