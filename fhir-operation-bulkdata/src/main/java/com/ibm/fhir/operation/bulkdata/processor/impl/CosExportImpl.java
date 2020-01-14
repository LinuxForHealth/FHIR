/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.bulkdata.processor.impl;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.operation.bulkdata.BulkDataConstants.ExportType;
import com.ibm.fhir.operation.bulkdata.client.BulkDataClient;
import com.ibm.fhir.operation.bulkdata.config.cache.BulkDataTenantSpecificCache;
import com.ibm.fhir.operation.bulkdata.model.PollingLocationResponse;
import com.ibm.fhir.operation.bulkdata.processor.ExportBulkData;
import com.ibm.fhir.operation.bulkdata.processor.ImportBulkData;
import com.ibm.fhir.operation.bulkdata.util.BulkDataUtil;
import com.ibm.fhir.operation.context.FHIROperationContext;
import com.ibm.fhir.operation.util.FHIROperationUtil;
import com.ibm.fhir.rest.FHIRResourceHelpers;

public class CosExportImpl implements ExportBulkData, ImportBulkData {

    private static final String CLASSNAME = CosExportImpl.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

    @Override
    public Parameters exportBase(MediaType outputFormat, Instant since, List<String> types,
        List<String> typeFilters, FHIRRequestContext ctx, FHIRResourceHelpers resourceHelper,
        FHIROperationContext operationContext, BulkDataTenantSpecificCache cache)
        throws FHIROperationException {

        try {
            log.info("Using the COS Implementation");

            // Resource type(s) is required.
            if (types == null) {
                throw BulkDataUtil.buildOperationException("Missing resource type(s)!");
            }

            Map<String, String> properties =
                    cache.getCachedObjectForTenant(FHIRConfiguration.DEFAULT_TENANT_ID);

            /*
             * Submit Job
             */
            BulkDataClient client = new BulkDataClient(properties);
            String url = client.submit(outputFormat, since, types, properties, ExportType.SYSTEM);

            /*
             * As we are now 'corrupting' the response, we're PUSHING it into the operation context. The
             * OperationContext is checked for ACCEPTED, and picks out the custom response.
             */
            Response response =
                    Response.status(Status.ACCEPTED).header("Content-Location", url).build();

            operationContext.setProperty(FHIROperationContext.PROPNAME_STATUS_TYPE, Response.Status.ACCEPTED);
            operationContext.setProperty(FHIROperationContext.PROPNAME_RESPONSE, response);

            return FHIROperationUtil.getOutputParameters(null);
        } catch (FHIROperationException fe) {
            throw fe;
        } catch (Exception e) {
            // Need to printStackTrace for debugging (eventually we'll shove into logger with debug/fine/finest)
            e.printStackTrace();
            throw new FHIROperationException("", e);
        }
    }

    @Override
    public Parameters statusExport(String job, FHIROperationContext operationContext,
        BulkDataTenantSpecificCache cache) throws FHIROperationException {

        try {
            log.info("Using the COS Implementation for Polling");

            Map<String, String> properties =
                    cache.getCachedObjectForTenant(FHIRConfiguration.DEFAULT_TENANT_ID);

            /*
             * Status of the Job
             */
            BulkDataClient client = new BulkDataClient(properties);
            PollingLocationResponse pollingResponse = client.status(job);

            /*
             * As we are now 'manipulating' the response, we're PUSHING it into the operation context. The
             * OperationContext is checked for ACCEPTED, and picks out the custom response. We do NOT to add the header
             * X-Progress.
             */
            Response response = null;
            if (pollingResponse != null) {
                response =
                        Response.status(Status.OK).entity(pollingResponse.toJsonString()).type(MediaType.APPLICATION_JSON).build();
            } else {
                // Technically we should also do 429 - Throttled when we get too many repeated requests.
                // We don't do that right now.
                response = Response.status(Status.ACCEPTED).header("Retry-After", "120").build();
            }

            // Set to accepted for signaling purposes, it does not OVERRIDE the above Status
            operationContext.setProperty(FHIROperationContext.PROPNAME_STATUS_TYPE, Response.Status.ACCEPTED);
            operationContext.setProperty(FHIROperationContext.PROPNAME_RESPONSE, response);

            return FHIROperationUtil.getOutputParameters(null);

        } catch (FHIROperationException fe) {
            throw fe;
        } catch (Exception e) {
            // Need to printStackTrace for debugging (eventually we'll shove into logger with debug/fine/finest)
            e.printStackTrace();
            throw new FHIROperationException("", e);
        }
    }

    // Patient export

    @Override
    public Parameters exportPatient(String logicalId, MediaType outputFormat, Instant since,
        List<String> types, List<String> typeFilters, FHIRRequestContext ctx,
        FHIRResourceHelpers resourceHelper, FHIROperationContext operationContext,
        BulkDataTenantSpecificCache cache) throws FHIROperationException{

        try {
            log.fine("Using the COS Implementation");

            Map<String, String> properties =
                    cache.getCachedObjectForTenant(FHIRConfiguration.DEFAULT_TENANT_ID);

            /*
             * Submit Job
             */
            BulkDataClient client = new BulkDataClient(properties);
            String url = client.submit(outputFormat, since, types, properties, ExportType.PATIENT);

            /*
             * As we are now 'corrupting' the response, we're PUSHING it into the operation context. The
             * OperationContext is checked for ACCEPTED, and picks out the custom response.
             */
            Response response =
                    Response.status(Status.ACCEPTED).header("Content-Location", url).build();

            operationContext.setProperty(FHIROperationContext.PROPNAME_STATUS_TYPE, Response.Status.ACCEPTED);
            operationContext.setProperty(FHIROperationContext.PROPNAME_RESPONSE, response);

            return FHIROperationUtil.getOutputParameters(null);
        } catch (FHIROperationException fe) {
            throw fe;
        } catch (Exception e) {
            throw new FHIROperationException("", e);
        }
    }

    // not implemented yet
    @Override
    public Parameters exportGroup(String logicalId, MediaType outputFormat, Instant since,
        List<String> types, List<String> typeFilters, FHIRRequestContext ctx,
        FHIRResourceHelpers resourceHelper, FHIROperationContext operationContext,
        BulkDataTenantSpecificCache cache) throws FHIROperationException {
        throw new FHIROperationException("No $export group operation right now");

    }

    @Override
    public Parameters deleteExport(MediaType outputFormat, Instant since, List<String> types,
        List<String> typeFilters, FHIRRequestContext ctx, FHIRResourceHelpers resourceHelper)
        throws FHIROperationException {
        throw new FHIROperationException("No $export delete operation right now");
    }

    @Override
    public Parameters importBase(Parameters parameters, FHIRRequestContext ctx,
        FHIRResourceHelpers resourceHelper) throws FHIROperationException {
        throw new FHIROperationException("No $import operation right now");
    }

}
