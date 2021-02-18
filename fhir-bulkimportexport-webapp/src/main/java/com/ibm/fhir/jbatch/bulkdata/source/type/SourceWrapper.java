/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.jbatch.bulkdata.source.type;

import java.util.List;

import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.jbatch.bulkdata.export.data.TransientUserData;
import com.ibm.fhir.jbatch.bulkdata.load.data.ImportTransientUserData;
import com.ibm.fhir.model.resource.Resource;

/**
 *
 */
public interface SourceWrapper {

    /**
     * gets the size of the given work item.
     *
     * @param workItem
     * @return
     * @throws FHIRException
     */
    long getSize(String workItem) throws FHIRException;

    void readResources(long numOfLinesToSkip, String workItem) throws FHIRException;

    default void writeResources(String mediaType, List<Resource> resources) throws Exception{
        // No Operation
    }

    /**
     * creates the base output location for the type.
     *
     * @throws FHIRException
     */
    default void createSource() throws FHIRException {
        // No Operation
    }

    List<Resource> getResources() throws FHIRException;

    long getNumberOfParseFailures() throws FHIRException;

    long getNumberOfLoaded() throws FHIRException;

    void registerTransient(ImportTransientUserData transientUserData);

    void registerTransient(long executionId, TransientUserData transientUserData, String cosBucketPathPrefix, String fhirResourceType, boolean isExportPublic) throws Exception;

    /**
     * Pushes the Operation Outcomes
     */
    default void pushOperationOutcomes() throws FHIRException {
        // No Operation;
    }

    void close() throws Exception;
}