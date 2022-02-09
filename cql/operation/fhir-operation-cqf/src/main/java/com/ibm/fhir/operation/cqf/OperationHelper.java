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

import com.ibm.fhir.core.ResourceType;
import com.ibm.fhir.cql.helpers.LibraryHelper;
import com.ibm.fhir.cql.translator.CqlTranslationProvider;
import com.ibm.fhir.cql.translator.FHIRLibraryLibrarySourceProvider;
import com.ibm.fhir.cql.translator.impl.InJVMCqlTranslationProvider;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.Library;
import com.ibm.fhir.model.resource.Measure;
import com.ibm.fhir.model.resource.Resource;
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

    /**
     * Load a Measure resource by reference.
     * @see loadResourceByReference
     * @param resourceHelper FHIRResourceHelpers for resource reads
     * @param reference Resource reference either in ResourceType/ID or canonical URL format
     * @return loaded resource
     * @throws FHIROperationException when resource is not found
     */
    public static Measure loadMeasureByReference(FHIRResourceHelpers resourceHelper, String reference) throws FHIROperationException {
        return loadResourceByReference(resourceHelper, ResourceType.MEASURE, Measure.class, reference);
    }

    /**
     * Load a Measure resource by ID.
     *
     * @see loadResourceById
     * @param resourceHelper FHIRResourceHelpers for resource reads
     * @param resourceId Resource ID
     * @return loaded resource
     * @throws FHIROperationException when resource is not found
     */
    public static Measure loadMeasureById(FHIRResourceHelpers resourceHelper, String resourceId) throws FHIROperationException {
        return loadResourceById(resourceHelper, ResourceType.MEASURE, resourceId);
    }

    /**
     * Load a Library resource by reference.
     * @see loadResourceByReference
     * @param resourceHelper FHIRResourceHelpers for resource reads
     * @param reference Resource reference either in ResourceType/ID or canonical URL format
     * @return loaded resource
     * @throws FHIROperationException when resource is not found
     */
    public static Library loadLibraryByReference(FHIRResourceHelpers resourceHelper, String reference) throws FHIROperationException {
        return loadResourceByReference(resourceHelper, ResourceType.LIBRARY, Library.class, reference);
    }

    /**
     * Load a Library resource by ID.
     *
     * @see loadResourceById
     * @param resourceHelper FHIRResourceHelpers for resource reads
     * @param resourceId Resource ID
     * @return loaded resource
     * @throws FHIROperationException when resource is not found
     */
    public static Library loadLibraryById(FHIRResourceHelpers resourceHelper, String resourceId) throws FHIROperationException {
        return loadResourceById(resourceHelper, ResourceType.LIBRARY, resourceId);
    }

    /**
     * Load a resource by reference. If the reference is of the format, ResourceType/ID or
     * does not contain any forward slash characters, it will be loaded directly. Otherwise,
     * the reference will be treated as a canonical URL and resolved from the FHIR registry.
     *
     * @param resourceHelper FHIRResourceHelpers for resource reads
     * @param reference Resource reference either in ResourceType/ID or canonical URL format
     * @return loaded resource
     * @throws FHIROperationException when resource is not found
     */
    @SuppressWarnings("unchecked")
    public static <T extends Resource> T loadResourceByReference(FHIRResourceHelpers resourceHelper, ResourceType resourceType, Class<T> resourceClass, String reference) throws FHIROperationException {
        T resource;
        int pos = reference.indexOf('/');
        if( pos == -1 || reference.startsWith(resourceType.value() + "/") ) {
            String resourceId = reference;
            if( pos > -1 ) {
                resourceId = reference.substring(pos + 1);
            }
            resource = (T) loadResourceById(resourceHelper, resourceType, resourceId);
        } else {
            resource = FHIRRegistry.getInstance().getResource(reference, resourceClass);
            if( resource == null ) {
                throw new FHIROperationException(String.format("Failed to resolve %s resource \"%s\"", resourceType.value(), reference));
            }
        }

        return resource;
    }

    /**
     * Load a resource by ID. ID values are expected to already be separated
     * from the ResourceType in a reference.
     *
     * @param <T> Resource class to load
     * @param resourceHelper FHIRResourceHelpers for resource reads
     * @param resourceType ResourceType of the resource to load
     * @param resourceId ID
     * @return
     * @throws FHIROperationException
     */
    @SuppressWarnings("unchecked")
    public static <T extends Resource> T loadResourceById(FHIRResourceHelpers resourceHelper, ResourceType resourceType, String resourceId) throws FHIROperationException {
        T resource;
        try {
            SingleResourceResult<?> readResult = resourceHelper.doRead(resourceType.value(), resourceId, true, false, null);
            resource = (T) readResult.getResource();
        } catch (Exception ex) {
            throw new FHIROperationException(String.format("Failed to resolve %s resource \"%s\"", resourceType.value(), resourceId), ex);
        }
        return resource;
    }
}
