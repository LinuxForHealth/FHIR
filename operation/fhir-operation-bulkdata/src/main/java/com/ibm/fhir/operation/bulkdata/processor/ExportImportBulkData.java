/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.processor;

import java.util.List;

import javax.ws.rs.core.MediaType;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.operation.bulkdata.OperationConstants;
import com.ibm.fhir.operation.bulkdata.model.type.Input;
import com.ibm.fhir.operation.bulkdata.model.type.StorageDetail;
import com.ibm.fhir.server.spi.operation.FHIROperationContext;

/**
 * The interfaces for the Backend Implementation of:
 * <li>Export</li>
 * <li>Import</li>
 * <li>polling location/status</li>
 */
public interface ExportImportBulkData {

    /**
     * Pattern: GET [Base]/$export
     * Pattern: GET [Base]/Patient/$export
     * Pattern: GET [Base]/Group/[id]/$export
     * Internally, call - GET [base]/Patient?_has:Group:member:_id=GroupId
     *
     * @param logicalId
     * @param exportType
     * @param outputFormat
     * @param since
     * @param types
     * @param typeFilters
     * @param operationContext used to signal a non-standard response
     * @return
     * @throws FHIROperationException
     */
    public Parameters export(String logicalId, OperationConstants.ExportType exportType, MediaType outputFormat,
            Instant since, List<String> types, List<String> typeFilters, FHIROperationContext operationContext) throws FHIROperationException;

    /**
     * Pattern: POST [Base]/$import
     *
     * @param inputFormat
     * @param inputSource
     * @param inputs
     * @param storageDetails
     * @param operationContext used to signal a non-standard response
     * @return
     * @throws FHIROperationException
     */
    public Parameters importBulkData(String inputFormat, String inputSource, List<Input> inputs, StorageDetail storageDetails, FHIROperationContext operationContext) throws FHIROperationException;

    /**
     * deletes the export/import job
     *
     * @param job
     * @param operationContext used to signal a non-standard response
     * @return
     * @throws FHIROperationException
     */
    public Parameters delete(String job, FHIROperationContext operationContext) throws FHIROperationException;

    /**
     * checks the status of the export/import job
     *
     * @param job
     * @param operationContext used to signal a non-standard response
     * @return
     * @throws FHIROperationException
     */
    public Parameters status(String job, FHIROperationContext operationContext) throws FHIROperationException;
}