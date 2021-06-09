/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.bulkdata.provider;

import java.util.List;

import com.ibm.fhir.bulkdata.dto.ReadResultDTO;
import com.ibm.fhir.bulkdata.jbatch.export.data.ExportTransientUserData;
import com.ibm.fhir.bulkdata.jbatch.load.data.ImportTransientUserData;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.model.resource.Resource;

/**
 * Wraps the Complexities of a Source for the Given Type
 */
public interface Provider {

    /**
     * gets the size of the given work item.
     *
     * @param workItem
     * @return
     * @throws FHIRException
     */
    long getSize(String workItem) throws FHIRException;

    /**
     * reads from a given workitem (or file) in a source
     * and skips a certain noumber of lines
     * @param numOfLinesToSkip
     * @param workItem
     * @throws FHIRException
     */
    void readResources(long numOfLinesToSkip, String workItem) throws FHIRException;

    /**
     * gets the read resources.
     *
     * @return
     * @throws FHIRException
     */
    List<Resource> getResources() throws FHIRException;

    /**
     * wraps the complexity of writing FHIR Resources out to a target
     *
     * @param mediaType
     * @param dtos
     * @throws Exception
     */
    default void writeResources(String mediaType, List<ReadResultDTO> dtos) throws Exception{
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


    long getNumberOfParseFailures() throws FHIRException;

    long getNumberOfLoaded() throws FHIRException;

    void registerTransient(ImportTransientUserData transientUserData);

    void registerTransient(long executionId, ExportTransientUserData transientUserData, String cosBucketPathPrefix, String fhirResourceType) throws Exception;

    /**
     * Pushes the Operation Outcomes
     */
    default void pushOperationOutcomes() throws FHIRException {
        // No Operation
    }

    void close() throws Exception;
}