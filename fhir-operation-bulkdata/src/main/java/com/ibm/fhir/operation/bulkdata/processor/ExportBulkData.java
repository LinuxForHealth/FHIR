/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.bulkdata.processor;

import java.util.List;

import javax.ws.rs.core.MediaType;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.operation.bulkdata.config.cache.BulkDataTenantSpecificCache;
import com.ibm.fhir.operation.context.FHIROperationContext;
import com.ibm.fhir.rest.FHIRResourceHelpers;

/**
 * Bulkdata Import Signature to match @link http://hl7.org/fhir/uv/bulkdata/export/index.html
 *
 * Each implmentation is responsible for write the three possible outcomes.
 *
 * <li>In-Progress (202 Accepted)</li>
 * <li>Error (500 Server Error)</li>
 * <li>Complete (200 OK)</li>
 *
 */
public interface ExportBulkData {

    /**
     *
     * Pattern: GET [Base]/$export
     *
     * @param outputFormat
     * @param since
     * @param types
     * @param typeFilters
     * @param ctx
     * @param resourceHelper
     * @param operationContext
     * @param cache
     *
     * @return
     *
     * @throws FHIROperationException
     */
    public Parameters exportBase(MediaType outputFormat, Instant since, List<String> types,
        List<String> typeFilters, FHIRRequestContext ctx, FHIRResourceHelpers resourceHelper,
        FHIROperationContext operationContext, BulkDataTenantSpecificCache cache)
        throws FHIROperationException;

    /**
     *
     * Pattern: GET [Base]/Patient/$export
     *
     * @param logicalId
     * @param outputFormat
     * @param since
     * @param types
     * @param typeFilters
     * @param ctx
     * @param resourceHelper
     * @param operationContext
     * @param BulkDataTenantSpecificCache
     * @return
     *
     * @throws FHIROperationException
     */
    public Parameters exportPatient(String logicalId, MediaType outputFormat, Instant since,
        List<String> types, List<String> typeFilters, FHIRRequestContext ctx,
        FHIRResourceHelpers resourceHelper, FHIROperationContext operationContext,
        BulkDataTenantSpecificCache cache) throws FHIROperationException;

    /**
     *
     * Pattern: GET [Base]/Group/[id]/$export
     *
     * Internally, call - GET [base]/Patient?_has:Group:member:_id=GroupId
     *
     * @param logicalId
     * @param outputFormat
     * @param since
     * @param types
     * @param typeFilters
     * @param ctx
     * @param resourceHelper
     * @param operationContext
     * @param BulkDataTenantSpecificCache
     * @return
     *
     * @throws FHIROperationException
     */
    public Parameters exportGroup(String logicalId, MediaType outputFormat, Instant since,
        List<String> types, List<String> typeFilters, FHIRRequestContext ctx,
        FHIRResourceHelpers resourceHelper, FHIROperationContext operationContext,
        BulkDataTenantSpecificCache cache) throws FHIROperationException;

    /**
     * deletes the export job
     *
     * @param outputFormat
     * @param since
     * @param types
     * @param typeFilters
     * @param ctx
     * @param resourceHelper
     * @return
     * @throws Exception
     */
    public Parameters deleteExport(MediaType outputFormat, Instant since, List<String> types,
        List<String> typeFilters, FHIRRequestContext ctx, FHIRResourceHelpers resourceHelper)
        throws FHIROperationException;

    /**
     * checks the status of the export job
     *
     * @param job
     * @param operationContext
     * @param cache
     *
     * @return
     * @throws FHIROperationException
     */
    public Parameters statusExport(String job,
        FHIROperationContext operationContext, BulkDataTenantSpecificCache cache)
        throws FHIROperationException;
}
