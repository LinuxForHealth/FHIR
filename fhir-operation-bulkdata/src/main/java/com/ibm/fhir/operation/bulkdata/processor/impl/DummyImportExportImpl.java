/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.bulkdata.processor.impl;

import static com.ibm.fhir.model.type.String.string;

import java.util.List;
import java.util.WeakHashMap;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.operation.bulkdata.config.cache.BulkDataTenantSpecificCache;
import com.ibm.fhir.operation.bulkdata.model.PollingLocationResponse;
import com.ibm.fhir.operation.bulkdata.processor.ExportBulkData;
import com.ibm.fhir.operation.bulkdata.processor.ImportBulkData;
import com.ibm.fhir.operation.context.FHIROperationContext;
import com.ibm.fhir.operation.util.FHIROperationUtil;
import com.ibm.fhir.rest.FHIRResourceHelpers;

public class DummyImportExportImpl implements ExportBulkData, ImportBulkData {

    /*
     * In the dummy implementation, the second request is an error condition. It's a dummy - testing only.
     */
    private enum Phase {
        START,
        IN_PROGRESS, // http://hl7.org/fhir/uv/bulkdata/export/index.html#response---in-progress-status
        ERROR, // http://hl7.org/fhir/uv/bulkdata/export/index.html#response---error-status-1
        COMPLETE,
        DELETING,
        DELETED,
        DELETED_ERROR
    }

    /*
     * The API appears to be 'single' executor / tenant / job.
     */
    @SuppressWarnings("unused")
    private static WeakHashMap<String, Phase> jobTracker = new WeakHashMap<>();

    public static Parameters getOutputParametersWithJson(PollingLocationResponse resource)
        throws Exception {
        Parameters.Builder parametersBuilder = Parameters.builder();
        parametersBuilder.parameter(Parameter.builder().name(string("return")).value(string(resource.toJsonString())).build());
        return parametersBuilder.build();
    }

    @Override
    public Parameters importBase(Parameters parameters, FHIRRequestContext ctx,
        FHIRResourceHelpers resourceHelper) throws FHIROperationException {
        throw new FHIROperationException("No $import operation right now");
    }

    @Override
    public Parameters exportBase(MediaType outputFormat, Instant since, List<String> types,
        List<String> typeFilters, FHIRRequestContext ctx, FHIRResourceHelpers resourceHelper,
        FHIROperationContext operationContext, BulkDataTenantSpecificCache cache)
        throws FHIROperationException {

        try {

            /*
             * Submit Job
             */

            /*
             * As we are now 'corrupting' the response, we're PUSHING it into the operation context. The
             * OperationContext is checked for ACCEPTED, and picks out the custom response.
             */
            String url = "Go-over-there";
            Response response =
                    Response.status(Status.ACCEPTED).header("Content-Location", url).build();
            operationContext.setProperty(FHIROperationContext.PROPNAME_STATUS_TYPE, Response.Status.ACCEPTED);
            operationContext.setProperty(FHIROperationContext.PROPNAME_RESPONSE, response);

            return FHIROperationUtil.getOutputParameters(null);
        } catch (Exception e) {
            throw new FHIROperationException("", e);
        }
    }

    @Override
    public Parameters exportPatient(String logicalId, MediaType outputFormat, Instant since,
        List<String> types, List<String> typeFilters, FHIRRequestContext ctx,
        FHIRResourceHelpers resourceHelper, FHIROperationContext operationContext,
        BulkDataTenantSpecificCache cache) throws FHIROperationException {

        try {
            return getOutputParametersWithJson(null);
        } catch (Exception e) {
            throw new FHIROperationException("", e);
        }
    }

    @Override
    public Parameters exportGroup(String logicalId, MediaType outputFormat, Instant since,
        List<String> types, List<String> typeFilters, FHIRRequestContext ctx,
        FHIRResourceHelpers resourceHelper, FHIROperationContext operationContext,
        BulkDataTenantSpecificCache cache) throws FHIROperationException {
        try {
            return FHIROperationUtil.getOutputParameters(null);
        } catch (Exception e) {
            throw new FHIROperationException("", e);
        }

    }

    @Override
    public Parameters deleteExport(MediaType outputFormat, Instant since, List<String> types,
        List<String> typeFilters, FHIRRequestContext ctx, FHIRResourceHelpers resourceHelper)
        throws FHIROperationException {

        try {
            return getOutputParametersWithJson(null);
        } catch (Exception e) {
            throw new FHIROperationException("", e);
        }
    }

    @Override
    public Parameters statusExport(String job, FHIROperationContext operationContext,
        BulkDataTenantSpecificCache cache) throws FHIROperationException {
        try {
            return getOutputParametersWithJson(null);
        } catch (Exception e) {
            throw new FHIROperationException("", e);
        }
    }

}
