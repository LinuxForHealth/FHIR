/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.processor.impl;

import java.util.List;
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
import com.ibm.fhir.operation.bulkdata.client.BulkDataClient.Result;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.operation.bulkdata.model.PollingLocationResponse;
import com.ibm.fhir.operation.bulkdata.model.type.StorageDetail;
import com.ibm.fhir.operation.bulkdata.processor.ExportImportBulkData;
import com.ibm.fhir.persistence.bulkdata.InputDTO;
import com.ibm.fhir.persistence.bulkdata.JobManager;
import com.ibm.fhir.persistence.bulkdata.JobStatusDTO;
import com.ibm.fhir.persistence.jdbc.util.TimestampPrefixedUUID;
import com.ibm.fhir.persistence.util.LogicalIdentityProvider;
import com.ibm.fhir.server.spi.operation.FHIROperationContext;
import com.ibm.fhir.server.spi.operation.FHIROperationUtil;

/**
 * Import from or export to the Storage Provider
 */
public class ExportImportImpl implements ExportImportBulkData {
    private static final String CLASSNAME = ExportImportImpl.class.getName();
    private static final Logger LOG = Logger.getLogger(CLASSNAME);

    private final LogicalIdentityProvider logicalIdentityProvider = new TimestampPrefixedUUID();

    private String source = null;
    private String outcome = null;
    private String baseUri = null;
    private String incomingUrl = null;

    public ExportImportImpl(String bulkdataSource, String outcomeSource, String baseUri) {
        this.source = bulkdataSource;
        this.outcome = outcomeSource;
        this.baseUri = baseUri;

        String incomingUrl = FHIRRequestContext.get().getOriginalRequestUri();
        this.incomingUrl = incomingUrl;
    }

    @Override
    public Parameters export(String logicalId, OperationConstants.ExportType exportType, MediaType outputFormat, Instant since, List<String> types,
            List<String> typeFilters, FHIROperationContext operationContext, JobManager dao)
            throws FHIROperationException {
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

            // If we add multiple formats, shove the media-type into a properties map.
            String extJobId = logicalIdentityProvider.createNewIdentityValue();

            // ThreadLocal values which need to be stored alongside.
            String tenant = FHIRRequestContext.get().getTenantId();
            String datastore = FHIRRequestContext.get().getDataStoreId();

            // Create the Export Job
            dao.createExportJob(tenant, datastore, datastore, datastore, logicalId, extJobId, tenant, typeFiltersStr, datastore, outputFormatStr, typeFiltersStr, groupId);

            BulkDataClient client = new BulkDataClient(source, outcome, incomingUrl, baseUri, adapter);
            Result result = client.submitExport(extJobId, since, types, exportType, outputFormatStr, typeFiltersStr, groupId);
            
            // The job is dispatched, now record it.
            String intJobId = result.intJobId;
            dao.updateInternalJobId(extJobId, intJobId);

            // As we are now 'modifying' the response, we're PUSHING it into the operation context. The
            // OperationContext is checked for ACCEPTED, and picks out the custom response.
            Response response = Response.status(Status.ACCEPTED).header("Content-Location", result.url).build();
            operationContext.setProperty(FHIROperationContext.PROPNAME_STATUS_TYPE, Response.Status.ACCEPTED);
            operationContext.setProperty(FHIROperationContext.PROPNAME_RESPONSE, response);

            return FHIROperationUtil.getOutputParameters(null);
        } catch (FHIROperationException fe) {
            throw fe;
        } catch (Exception e) {
            // Conditionally output the log detail:
            if (LOG.isLoggable(Level.FINE)) {
                LOG.log(Level.FINE, "Exception is " + e.getMessage(), e);
            }
            throw new FHIROperationException("Error while processing the $export request", e);
        }
    }

    @Override
    public Parameters status(String extJobId, FHIROperationContext operationContext, JobManager dao)
            throws FHIROperationException {
        try {
            // Check on the Job's Status
            ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
            BulkDataClient client = new BulkDataClient(source, outcome, incomingUrl, baseUri, adapter);

            JobStatusDTO jobStatus = dao.getStatus(extJobId);

            /*
             * As we are now 'manipulating' the response, we're PUSHING it into the
             * operation context. The OperationContext is checked for ACCEPTED, and picks
             * out the custom response. We do NOT add the header X-Progress.
             */
            Response response = null;
            if (jobStatus.shouldContinue) {
                // Technically we should also do 429 - Throttled when we get too many repeated
                // requests.
                // We don't do that right now.
                response = Response.status(Status.ACCEPTED).header("Retry-After", "120").build();
                operationContext.setProperty(FHIROperationContext.PROPNAME_STATUS_TYPE, Response.Status.ACCEPTED);
            } else if (jobStatus.complete) {
                PollingLocationResponse pollingResponse = client.translateResponse(jobStatus.blob);
                response =
                        Response.status(Status.OK).entity(PollingLocationResponse.Writer.generate(pollingResponse))
                                .type(MediaType.APPLICATION_JSON).build();
            } else {
                // Error Condition
                client.status(jobStatus.intJobId);
            }

            operationContext.setProperty(FHIROperationContext.PROPNAME_RESPONSE, response);
            return FHIROperationUtil.getOutputParameters(null);

        } catch (FHIROperationException fe) {
            throw fe;
        } catch (Exception e) {
            // Conditionally output the log detail:
            if (LOG.isLoggable(Level.FINE)) {
                LOG.log(Level.FINE, "Exception is " + e.getMessage(), e);
            }
            throw new FHIROperationException("Status Check failed", e);
        }
    }

    @Override
    public Parameters delete(String extJobId, FHIROperationContext operationContext, JobManager dao)
            throws FHIROperationException {
        try {
            // Send the DELETE
            ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
            BulkDataClient client = new BulkDataClient(source, outcome, incomingUrl, baseUri, adapter);

            String intJobId = dao.getInternalJobId(extJobId);
            Response.Status status = client.delete(intJobId);
            dao.updateJobStatus(extJobId);

            // Set to accepted for signaling purposes, it does not OVERRIDE the above Status
            Response response = Response.status(Status.ACCEPTED).build();
            operationContext.setProperty(FHIROperationContext.PROPNAME_STATUS_TYPE, status);
            operationContext.setProperty(FHIROperationContext.PROPNAME_RESPONSE, response);
            return FHIROperationUtil.getOutputParameters(null);
        } catch (FHIROperationException fe) {
            throw fe;
        } catch (Exception e) {
            // Conditionally output the log detail:
            if (LOG.isLoggable(Level.FINE)) {
                LOG.log(Level.FINE, "Exception is " + e.getMessage(), e);
            }
            throw new FHIROperationException("exception with $export delete operation", e);
        }
    }

    @Override
    public Parameters importBulkData(String inputFormat, String inputSource, List<InputDTO> inputs,
            StorageDetail storageDetail, FHIROperationContext operationContext, JobManager dao)
            throws FHIROperationException {
        try {
            // Submit Job
            ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
            BulkDataClient client = new BulkDataClient(source, outcome, incomingUrl, baseUri, adapter);

            // If we add multiple formats, shove the media-type into a properties map.
            String extJobId = logicalIdentityProvider.createNewIdentityValue();

            // ThreadLocal values which need to be stored alongside.
            String tenant = FHIRRequestContext.get().getTenantId();
            String datastore = FHIRRequestContext.get().getDataStoreId();

            dao.createImportJob(tenant, datastore, source, outcome, incomingUrl, extJobId, inputs);
            Result result = client.submitImport(inputFormat, inputSource, extJobId, storageDetail);

            // The job is dispatched, now record it.
            String intJobId = result.intJobId;
            dao.updateInternalJobId(extJobId, intJobId);

            // As we are now 'modifying' the response, we're PUSHING it into the operation context. The
            // OperationContext is checked for ACCEPTED, and picks out the custom response.
            Response response = Response.status(Status.ACCEPTED).header("Content-Location", result.url).build();
            operationContext.setProperty(FHIROperationContext.PROPNAME_STATUS_TYPE, Response.Status.ACCEPTED);
            operationContext.setProperty(FHIROperationContext.PROPNAME_RESPONSE, response);

            return FHIROperationUtil.getOutputParameters(null);
        } catch (FHIROperationException fe) {
            throw fe;
        } catch (Exception e) {
            // Conditionally output the log detail:
            if (LOG.isLoggable(Level.FINE)) {
                LOG.log(Level.FINE, "Exception is " + e.getMessage(), e);
            }
            throw new FHIROperationException("Error while processing the $import request", e);
        }
    }
}