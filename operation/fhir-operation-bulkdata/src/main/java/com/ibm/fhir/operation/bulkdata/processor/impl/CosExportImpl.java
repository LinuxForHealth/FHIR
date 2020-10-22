/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.processor.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.operation.bulkdata.BulkDataConstants;
import com.ibm.fhir.operation.bulkdata.BulkDataConstants.ExportType;
import com.ibm.fhir.operation.bulkdata.client.BulkDataClient;
import com.ibm.fhir.operation.bulkdata.model.PollingLocationResponse;
import com.ibm.fhir.operation.bulkdata.model.type.Input;
import com.ibm.fhir.operation.bulkdata.model.type.StorageDetail;
import com.ibm.fhir.operation.bulkdata.processor.ExportImportBulkData;
import com.ibm.fhir.server.operation.spi.FHIROperationContext;
import com.ibm.fhir.server.operation.spi.FHIRResourceHelpers;
import com.ibm.fhir.server.util.FHIROperationUtil;

/**
 * Import from or export to IBM Cloud Object Storage (COS) or similar S3-compatible object stores
 */
public class CosExportImpl implements ExportImportBulkData {
    private static final String CLASSNAME = CosExportImpl.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

    private Map<String, String> properties = null;

    public CosExportImpl(Map<String, String> properties) {
        this.properties = properties;
        log.fine("Using the COS Implementation");
    }

    @Override
    public Parameters export(String logicalId, BulkDataConstants.ExportType exportType, MediaType outputFormat,
            Instant since, List<String> types, List<String> typeFilters, FHIROperationContext operationContext,
            FHIRResourceHelpers resourceHelper) throws FHIROperationException {
        try {
            Objects.requireNonNull(outputFormat, "outputFormat");

            Map<String, String> tmpProperties = new HashMap<>();
            tmpProperties.putAll(properties);

            String incomingUrl = FHIRRequestContext.get().getOriginalRequestUri();
            tmpProperties.put("incomingUrl", incomingUrl);

            if (ExportType.GROUP.equals(exportType)) {
                if (logicalId == null || logicalId.isEmpty()) {
                    throw new FHIROperationException("Group export requires group id!");
                }
                tmpProperties.put(BulkDataConstants.PARAM_GROUP_ID, logicalId);
            }

            if (typeFilters != null && !typeFilters.isEmpty()) {
                tmpProperties.put(BulkDataConstants.PARAM_TYPE_FILTER, String.join(",", typeFilters));
            }

            tmpProperties.put(BulkDataConstants.PARAM_OUTPUT_FORMAT, outputFormat.toString());
            addBaseUri(operationContext, tmpProperties);

            // Submit Job
            BulkDataClient client = new BulkDataClient(tmpProperties);
            // If we add multiple formats, shove the mediatype into a properties map.
            String url = client.submitExport(since, types, tmpProperties, exportType);

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

    public void addBaseUri(FHIROperationContext operationContext, Map<String, String> tmpProperties) {
        // Grab the URI
        String baseUri = (String) operationContext.getProperty(FHIROperationContext.PROPNAME_REQUEST_BASE_URI);
        tmpProperties.put("base-uri", baseUri);
    }

    @Override
    public Parameters status(String job, FHIROperationContext operationContext) throws FHIROperationException {
        try {
            // Check on the Job's Status
            Map<String, String> tmpProperties = new HashMap<>();
            tmpProperties.putAll(properties);
            addBaseUri(operationContext, tmpProperties);
            BulkDataClient client = new BulkDataClient(tmpProperties);
            PollingLocationResponse pollingResponse = client.status(job);

            /*
             * As we are now 'manipulating' the response, we're PUSHING it into the operation context. The
             * OperationContext is checked for ACCEPTED, and picks out the custom response. We do NOT to add the header
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
            throw new FHIROperationException("", e);
        }
    }

    @Override
    public Parameters delete(String job, FHIROperationContext operationContext) throws FHIROperationException {
        try {
            // Send the DELETE
            Map<String, String> tmpProperties = new HashMap<>();
            tmpProperties.putAll(properties);
            addBaseUri(operationContext, tmpProperties);
            BulkDataClient client = new BulkDataClient(tmpProperties);
            Response.Status status = client.delete(job);

            // Set to accepted for signaling purposes, it does not OVERRIDE the above Status
            Response response = Response.status(Status.ACCEPTED).build();
            operationContext.setProperty(FHIROperationContext.PROPNAME_STATUS_TYPE, status);
            operationContext.setProperty(FHIROperationContext.PROPNAME_RESPONSE, response);
            return FHIROperationUtil.getOutputParameters(null);
        } catch (FHIROperationException fe) {
            throw fe;
        } catch (Exception e) {
            throw new FHIROperationException("exception with $export delete operation", e);
        }
    }

    @Override
    public Parameters importBulkData(String inputFormat, String inputSource, List<Input> inputs,
            StorageDetail storageDetail, FHIROperationContext operationContext) throws FHIROperationException {
        try {
            Map<String, String> tmpProperties = new HashMap<>();
            tmpProperties.putAll(properties);
            addBaseUri(operationContext, tmpProperties);

            String incomingUrl = FHIRRequestContext.get().getOriginalRequestUri();
            tmpProperties.put("incomingUrl", incomingUrl);

            // Submit Job
            BulkDataClient client = new BulkDataClient(tmpProperties);
            // If we add multiple formats, shove the media-type into a properties map.
            String url = client.submitImport(inputFormat, inputSource, inputs, storageDetail, tmpProperties);

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
