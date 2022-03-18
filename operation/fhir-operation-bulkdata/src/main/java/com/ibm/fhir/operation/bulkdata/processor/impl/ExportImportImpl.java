/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.processor.impl;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.operation.bulkdata.OperationConstants;
import com.ibm.fhir.operation.bulkdata.OperationConstants.ExportType;
import com.ibm.fhir.operation.bulkdata.client.BulkDataClient;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.operation.bulkdata.model.PollingLocationResponse;
import com.ibm.fhir.operation.bulkdata.model.type.Input;
import com.ibm.fhir.operation.bulkdata.model.type.StorageDetail;
import com.ibm.fhir.operation.bulkdata.processor.ExportImportBulkData;
import com.ibm.fhir.server.spi.operation.FHIROperationContext;
import com.ibm.fhir.server.spi.operation.FHIROperationUtil;

/**
 * Import from or export to IBM Cloud Object Storage (COS) or similar S3-compatible object stores
 */
public class ExportImportImpl implements ExportImportBulkData {
    private static final String CLASSNAME = ExportImportImpl.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

    private String bulkdataSource = null;
    private String outcomeSource = null;
    private String baseUri = null;
    private String incomingUrl = null;

    public ExportImportImpl(String bulkdataSource, String outcomeSource, String baseUri) {
        this.bulkdataSource = bulkdataSource;
        this.outcomeSource = outcomeSource;
        this.baseUri = baseUri;

        String incomingUrl = FHIRRequestContext.get().getOriginalRequestUri();
        this.incomingUrl = incomingUrl;
    }

    @Override
    public Parameters export(String logicalId, OperationConstants.ExportType exportType, MediaType outputFormat, Instant since, 
            Set<String> types, List<String> typeFilters, FHIROperationContext operationContext) throws FHIROperationException {
        try {
            // Check if this is a group export
            String groupId = null;
            if (ExportType.GROUP.equals(exportType)) {
                if (logicalId == null || logicalId.isEmpty()) {
                    throw new FHIROperationException("Group export requires group id!");
                }
                groupId = logicalId;
            }

            // Convert to a String for the TypeFilters
            String typeFiltersStr = null;
            if (typeFilters != null && !typeFilters.isEmpty()) {
                typeFiltersStr = String.join(",", typeFilters);
            }

            // Check the Output Format
            String outputFormatStr;
            if (outputFormat == null) {
                outputFormatStr = FHIRMediaType.APPLICATION_NDJSON;
            } else {
                outputFormatStr = outputFormat.toString();
            }

            // Submit Job
            ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
            BulkDataClient client = new BulkDataClient(bulkdataSource, outcomeSource, incomingUrl, baseUri, adapter);
            String url = client.submitExport(since, types, exportType, outputFormatStr, typeFiltersStr, groupId);

            // As we are now 'modifying' the response, we're PUSHING it into the operation context. The
            // OperationContext is checked for ACCEPTED, and picks out the custom response.
            Response response = Response.status(Status.ACCEPTED).header("Content-Location", url).build();
            operationContext.setProperty(FHIROperationContext.PROPNAME_STATUS_TYPE, Response.Status.ACCEPTED);
            operationContext.setProperty(FHIROperationContext.PROPNAME_RESPONSE, response);

            return FHIROperationUtil.getOutputParameters(null);
        } catch (FHIROperationException fe) {
            throw fe;
        } catch (Exception e) {
            // Conditionally output the log detail:
            if (log.isLoggable(Level.FINE)) {
                log.log(Level.FINE, "Exception is " + e.getMessage(), e);
            }
            throw new FHIROperationException("Error while processing the $export request", e);
        }
    }

    @Override
    public Parameters status(String job, FHIROperationContext operationContext) throws FHIROperationException {
        try {
            // Check on the Job's Status
            ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
            BulkDataClient client = new BulkDataClient(bulkdataSource, outcomeSource, incomingUrl, baseUri, adapter);

            PollingLocationResponse pollingResponse = client.status(job);

            /*
             * As we are now 'manipulating' the response, we're PUSHING it into the operation context. The
             * OperationContext is checked for ACCEPTED, and picks out the custom response. We do NOT add the header
             * X-Progress.
             */
            Response response = null;
            if (pollingResponse != null) {
                response =
                        Response.status(Status.OK).entity(PollingLocationResponse.Writer.generate(pollingResponse))
                                .type(MediaType.APPLICATION_JSON).build();
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
            // Conditionally output the log detail:
            if (log.isLoggable(Level.FINE)) {
                log.log(Level.FINE, "Exception is " + e.getMessage(), e);
            }
            throw new FHIROperationException("Status Check failed", e);
        }
    }

    @Override
    public Parameters delete(String job, FHIROperationContext operationContext) throws FHIROperationException {
        try {
            // Send the DELETE
            ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
            BulkDataClient client = new BulkDataClient(bulkdataSource, outcomeSource, incomingUrl, baseUri, adapter);

            Response.Status status = client.delete(job);

            // Set to accepted for signaling purposes, it does not OVERRIDE the above Status
            Response response = Response.status(Status.ACCEPTED).build();
            operationContext.setProperty(FHIROperationContext.PROPNAME_STATUS_TYPE, status);
            operationContext.setProperty(FHIROperationContext.PROPNAME_RESPONSE, response);
            return FHIROperationUtil.getOutputParameters(null);
        } catch (FHIROperationException fe) {
            throw fe;
        } catch (Exception e) {
            // Conditionally output the log detail:
            if (log.isLoggable(Level.FINE)) {
                log.log(Level.FINE, "Exception is " + e.getMessage(), e);
            }
            throw new FHIROperationException("exception with $export delete operation", e);
        }
    }

    @Override
    public Parameters importBulkData(String inputFormat, String inputSource, List<Input> inputs, StorageDetail storageDetail, FHIROperationContext operationContext) throws FHIROperationException {
        try {
            // Submit Job
            ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
            BulkDataClient client = new BulkDataClient(bulkdataSource, outcomeSource, incomingUrl, baseUri, adapter);

            // If we add multiple formats, shove the media-type into a properties map.
            String url = client.submitImport(inputFormat, inputSource, inputs, storageDetail);

            // As we are now 'modifying' the response, we're PUSHING it into the operation context. The
            // OperationContext is checked for ACCEPTED, and picks out the custom response.
            Response response = Response.status(Status.ACCEPTED).header("Content-Location", url).build();
            operationContext.setProperty(FHIROperationContext.PROPNAME_STATUS_TYPE, Response.Status.ACCEPTED);
            operationContext.setProperty(FHIROperationContext.PROPNAME_RESPONSE, response);

            return FHIROperationUtil.getOutputParameters(null);
        } catch (FHIROperationException fe) {
            throw fe;
        } catch (Exception e) {
            // Conditionally output the log detail:
            if (log.isLoggable(Level.FINE)) {
                log.log(Level.FINE, "Exception is " + e.getMessage(), e);
            }
            throw new FHIROperationException("Error while processing the $import request", e);
        }
    }
}