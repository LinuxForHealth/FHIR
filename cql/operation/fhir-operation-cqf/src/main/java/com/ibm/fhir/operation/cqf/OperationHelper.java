/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.cqf;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.opencds.cqf.cql.engine.execution.InMemoryLibraryLoader;
import org.opencds.cqf.cql.engine.execution.LibraryLoader;

import com.ibm.fhir.cql.helpers.LibraryHelper;
import com.ibm.fhir.cql.translator.CqlTranslationProvider;
import com.ibm.fhir.cql.translator.FHIRLibraryLibrarySourceProvider;
import com.ibm.fhir.cql.translator.impl.InJVMCqlTranslationProvider;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.Library;
import com.ibm.fhir.model.resource.Measure;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.code.ResourceType;
import com.ibm.fhir.persistence.SingleResourceResult;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.server.spi.operation.FHIRResourceHelpers;

public class OperationHelper {
    /**
     * Create a library loader that will server up the CQL library content of the
     * provided list of FHIR Library resources.
     * 
     * @param libraries
     *            FHIR library resources
     * @return LibraryLoader that will serve the CQL Libraries for the provided FHIR resources
     */
    public static LibraryLoader createLibraryLoader(List<Library> libraries) {
        List<org.cqframework.cql.elm.execution.Library> result = loadCqlLibraries(libraries);
        return new InMemoryLibraryLoader(result);
    }

    /**
     * Load the CQL Library content for each of the provided FHIR Library resources with
     * translation as needed for Libraries with CQL attachments and no corresponding
     * ELM attachment.
     * 
     * @param libraries
     *            FHIR Libraries
     * @return CQL Libraries
     */
    public static List<org.cqframework.cql.elm.execution.Library> loadCqlLibraries(List<Library> libraries) {
        FHIRLibraryLibrarySourceProvider sourceProvider = new FHIRLibraryLibrarySourceProvider(libraries);
        CqlTranslationProvider translator = new InJVMCqlTranslationProvider(sourceProvider);

        List<org.cqframework.cql.elm.execution.Library> result =
                libraries.stream().flatMap(fl -> LibraryHelper.loadLibrary(translator, fl).stream()).filter(Objects::nonNull).collect(Collectors.toList());
        return result;
    }
    
    public static Measure loadMeasureByReference(FHIRResourceHelpers resourceHelper, String reference) throws FHIROperationException {
        return loadResourceByReference(resourceHelper, ResourceType.MEASURE, Measure.class, reference);
    }
    

    public static Measure loadMeasureById(FHIRResourceHelpers resourceHelper, String reference) throws FHIROperationException {
        return loadResourceById(resourceHelper, ResourceType.MEASURE, reference);
    }
    
    public static Library loadLibraryByReference(FHIRResourceHelpers resourceHelper, String reference) throws FHIROperationException {
        return loadResourceByReference(resourceHelper, ResourceType.LIBRARY, Library.class, reference);
    }
    

    public static Library loadLibraryById(FHIRResourceHelpers resourceHelper, String reference) throws FHIROperationException {
        return loadResourceById(resourceHelper, ResourceType.LIBRARY, reference);
    }
    
    @SuppressWarnings("unchecked")
    public static <T extends Resource> T loadResourceByReference(FHIRResourceHelpers resourceHelper, ResourceType resourceType, Class<T> resourceClass, String reference) throws FHIROperationException {
        T resource;
        int pos = reference.indexOf('/');
        if( pos == -1 || reference.startsWith(resourceType.getValue() + "/") ) {
            String resourceId = reference;
            if( pos > -1 ) {
                resourceId = reference.substring(pos + 1);
            } 
            resource = (T) loadResourceById(resourceHelper, resourceType, resourceId);
        } else {
            resource = FHIRRegistry.getInstance().getResource(reference, resourceClass);
            if( resource == null ) {
                throw new FHIROperationException(String.format("Failed to resolve %s resource \"%s\"", resourceType.getValue(), reference));
            }
        }
        
        return resource;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Resource> T loadResourceById(FHIRResourceHelpers resourceHelper, ResourceType resourceType, String reference) throws FHIROperationException {
        T resource;
        try {
            SingleResourceResult<?> readResult = resourceHelper.doRead(resourceType.getValue(), reference, true, false, null);
            resource = (T) readResult.getResource();
        } catch (Exception ex) {
            throw new FHIROperationException(String.format("Failed to resolve %s resource \"%s\"", resourceType.getValue(), reference), ex);
        }
        return resource;
    }
}
