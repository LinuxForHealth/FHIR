/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.operation.cqf;

import java.util.Arrays;
import java.util.List;

import org.linuxforhealth.fhir.core.ResourceType;
import org.linuxforhealth.fhir.cql.helpers.LibraryHelper;
import org.linuxforhealth.fhir.ecqm.r4.MeasureHelper;
import org.linuxforhealth.fhir.exception.FHIROperationException;
import org.linuxforhealth.fhir.model.resource.Library;
import org.linuxforhealth.fhir.model.resource.Measure;
import org.linuxforhealth.fhir.model.resource.OperationDefinition;
import org.linuxforhealth.fhir.model.resource.Parameters;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.type.RelatedArtifact;
import org.linuxforhealth.fhir.model.type.code.RelatedArtifactType;
import org.linuxforhealth.fhir.persistence.SingleResourceResult;
import org.linuxforhealth.fhir.registry.FHIRRegistry;
import org.linuxforhealth.fhir.search.util.SearchHelper;
import org.linuxforhealth.fhir.server.spi.operation.FHIROperationContext;
import org.linuxforhealth.fhir.server.spi.operation.FHIRResourceHelpers;

public class MeasureDataRequirementsOperation extends AbstractDataRequirementsOperation {

    @Override
    protected OperationDefinition buildOperationDefinition() {
        return FHIRRegistry.getInstance().getResource("http://hl7.org/fhir/OperationDefinition/Measure-data-requirements", OperationDefinition.class);
    }

    @Override
    public Parameters doInvoke(FHIROperationContext operationContext, Class<? extends Resource> resourceType, String logicalId, String versionId,
            Parameters parameters, FHIRResourceHelpers resourceHelper, SearchHelper searchHelper) throws FHIROperationException {

        Measure measure = null;
        try {
            SingleResourceResult<?> readResult = resourceHelper.doRead(ResourceType.MEASURE.value(), logicalId);
            measure = (Measure) readResult.getResource();
            if (measure == null) {
                throw new FHIROperationException("Failed to resolve measure with resource id: " + logicalId);
            }
        } catch (FHIROperationException fex) {
            throw fex;
        } catch (Exception ex) {
            throw new FHIROperationException("Failed to read resource", ex);
        }

        int numLibraries = (measure.getLibrary() != null) ? measure.getLibrary().size() : 0;
        if (numLibraries != 1) {
            throw new IllegalArgumentException(String.format("Unexpected number of libraries '%d' referenced by measure '%s'", numLibraries, measure.getId()));
        }

        String primaryLibraryId = MeasureHelper.getPrimaryLibraryId(measure);
        Library primaryLibrary = OperationHelper.loadLibraryByReference(resourceHelper, primaryLibraryId);
        List<Library> fhirLibraries = LibraryHelper.loadLibraries(primaryLibrary);

        RelatedArtifact related = RelatedArtifact.builder().type(RelatedArtifactType.DEPENDS_ON).resource(measure.getLibrary().get(0)).build();
        return doDataRequirements(fhirLibraries, () -> Arrays.asList(related) );
    }
}
