/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.watson.health.fhir.operation.bullkdata.processor.impl;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ibm.watson.health.fhir.config.FHIRConfiguration;
import com.ibm.watson.health.fhir.config.FHIRRequestContext;
import com.ibm.watson.health.fhir.exception.FHIROperationException;
import com.ibm.watson.health.fhir.model.resource.Parameters;
import com.ibm.watson.health.fhir.model.type.Instant;
import com.ibm.watson.health.fhir.operation.bullkdata.client.BulkDataClient;
import com.ibm.watson.health.fhir.operation.bullkdata.config.cache.BulkDataTenantSpecificCache;
import com.ibm.watson.health.fhir.operation.bullkdata.processor.ExportBulkData;
import com.ibm.watson.health.fhir.operation.bullkdata.processor.ImportBulkData;
import com.ibm.watson.health.fhir.operation.bullkdata.util.BulkDataUtil;
import com.ibm.watson.health.fhir.operation.context.FHIROperationContext;
import com.ibm.watson.health.fhir.operation.util.FHIROperationUtil;
import com.ibm.watson.health.fhir.rest.FHIRResourceHelpers;

/**
 * @author pbastide
 *
 */
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

            // More than one Resource type is being used.
            if (types == null || types.size() != 1) {
                throw BulkDataUtil.buildOperationException("We currently only support one type exported at a time");
            }

            Map<String, String> properties =
                    cache.getCachedObjectForTenant(FHIRConfiguration.DEFAULT_TENANT_ID);

            /*
             * Submit Job
             */
            BulkDataClient client = new BulkDataClient(properties);
            String url = client.submit(outputFormat, since, types, properties);

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

    // The following operations are not yet implemented.

    @Override
    public Parameters exportPatient(String logicalId, MediaType outputFormat, Instant since,
        List<String> types, List<String> typeFilters, FHIRRequestContext ctx,
        FHIRResourceHelpers resourceHelper) throws FHIROperationException {
        throw new FHIROperationException("No $export delete operation right now");
    }

    @Override
    public Parameters exportGroup(String logicalId, MediaType outputFormat, Instant since,
        List<String> types, List<String> typeFilters, FHIRRequestContext ctx,
        FHIRResourceHelpers resourceHelper) throws FHIROperationException {
        throw new FHIROperationException("No $export delete operation right now");

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
