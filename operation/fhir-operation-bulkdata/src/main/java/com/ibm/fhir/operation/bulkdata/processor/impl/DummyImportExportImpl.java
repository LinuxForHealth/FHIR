/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.processor.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.operation.bulkdata.BulkDataConstants;
import com.ibm.fhir.operation.bulkdata.BulkDataConstants.ExportType;
import com.ibm.fhir.operation.bulkdata.model.PollingLocationResponse;
import com.ibm.fhir.operation.bulkdata.model.type.Input;
import com.ibm.fhir.operation.bulkdata.model.type.StorageDetail;
import com.ibm.fhir.operation.bulkdata.processor.ExportImportBulkData;
import com.ibm.fhir.server.operation.spi.FHIROperationContext;
import com.ibm.fhir.server.operation.spi.FHIRResourceHelpers;
import com.ibm.fhir.server.util.FHIROperationUtil;

/**
 * The Dummy implementation is used to Import / Export.
 */
public class DummyImportExportImpl implements ExportImportBulkData {
    private static AtomicInteger jobCounter = new AtomicInteger(0);

    public DummyImportExportImpl() {
        // NO Operation
    }

    @Override
    public Parameters export(String logicalId, BulkDataConstants.ExportType exportType, MediaType outputFormat,
            Instant since, List<String> types, List<String> typeFilters, FHIROperationContext operationContext,
            FHIRResourceHelpers resourceHelper, String systemExportImpl) throws FHIROperationException {
        try {
            int count = jobCounter.incrementAndGet();
            if (count % 3 == 0) {
                if (ExportType.GROUP.equals(exportType)) {
                    if (logicalId == null || logicalId.isEmpty()) {
                        throw new FHIROperationException("Group export requires group id!");
                    }
                }

                String url = generateBaseUri(operationContext) + "$bulkdata-status?job=" + count;

                Response response = Response.status(Status.ACCEPTED).header("Content-Location", url).build();
                operationContext.setProperty(FHIROperationContext.PROPNAME_STATUS_TYPE, Response.Status.ACCEPTED);
                operationContext.setProperty(FHIROperationContext.PROPNAME_RESPONSE, response);

                return FHIROperationUtil.getOutputParameters(null);
            } else {
                throw new FHIROperationException("$export operation injected failure");
            }
        } catch (Exception e) {
            throw new FHIROperationException("$export operation", e);
        }
    }

    public String generateBaseUri(FHIROperationContext operationContext) {
        // Grab the URI
        return (String) operationContext.getProperty(FHIROperationContext.PROPNAME_REQUEST_BASE_URI);
    }

    @Override
    public Parameters status(String job, FHIROperationContext operationContext) throws FHIROperationException {
        try {
            Integer curJob = Integer.parseInt(job);

            PollingLocationResponse result = new PollingLocationResponse();
            result.setRequest("/$export?_type=Patient");
            result.setRequiresAccessToken(false);
            result.setTransactionTime(Instant.now().toString());
            List<String> resourceTypeInfs = Arrays.asList("Patient[10,10,10]");
            List<PollingLocationResponse.Output> outputList = new ArrayList<>();
            for (String resourceTypeInf : resourceTypeInfs) {
                String resourceType = resourceTypeInf.substring(0, resourceTypeInf.indexOf("["));
                String[] resourceCounts =
                        resourceTypeInf.substring(resourceTypeInf.indexOf("[") + 1, resourceTypeInf.indexOf("]"))
                                .split("\\s*,\\s*");
                for (int i = 0; i < resourceCounts.length; i++) {
                    String downloadUrl = "https://mysite/mybucket/Patient_" + (i + 1) + ".ndjson";
                    outputList.add(new PollingLocationResponse.Output(resourceType, downloadUrl, resourceCounts[i]));
                }
            }
            result.setOutput(outputList);

            String jsonString = PollingLocationResponse.Writer.generate(result);
            Response response = null;
            if (curJob % 3 == 0) {
                response = Response.status(Status.OK).entity(jsonString).type(MediaType.APPLICATION_JSON).build();
            } else if (curJob % 2 == 0) {
                throw new FHIROperationException("$export operation injected failure");
            } else {
                response = Response.status(Status.ACCEPTED).header("Retry-After", "120").build();
            }

            // Set to accepted for signaling purposes, it does not OVERRIDE the above Status
            operationContext.setProperty(FHIROperationContext.PROPNAME_STATUS_TYPE, Response.Status.ACCEPTED);
            operationContext.setProperty(FHIROperationContext.PROPNAME_RESPONSE, response);
            return FHIROperationUtil.getOutputParameters(null);
        } catch (Exception e) {
            throw new FHIROperationException("", e);
        }
    }

    @Override
    public Parameters delete(String job, FHIROperationContext operationContext) throws FHIROperationException {
        try {
            Integer curJob = Integer.parseInt(job);
            if (curJob % 2 != 0) {
                throw new FHIROperationException("$export operation injected failure");
            } else {
                // Set to accepted for signaling purposes, it does not OVERRIDE the above Status
                Response response = Response.status(Status.ACCEPTED).build();
                operationContext.setProperty(FHIROperationContext.PROPNAME_STATUS_TYPE, Response.Status.ACCEPTED);
                operationContext.setProperty(FHIROperationContext.PROPNAME_RESPONSE, response);
                return FHIROperationUtil.getOutputParameters(null);
            }
        } catch (Exception e) {
            throw new FHIROperationException("exception with $export delete operation", e);
        }
    }

    @Override
    public Parameters importBulkData(String inputFormat, String inputSource, List<Input> inputs,
            StorageDetail storageDetails, FHIROperationContext operationContext) throws FHIROperationException {
        try {
            int count = jobCounter.incrementAndGet();
            if (count % 3 == 0) {
                String url = generateBaseUri(operationContext) + "$bulkdata-status?job=" + count;

                Response response = Response.status(Status.ACCEPTED).header("Content-Location", url).build();
                operationContext.setProperty(FHIROperationContext.PROPNAME_STATUS_TYPE, Response.Status.ACCEPTED);
                operationContext.setProperty(FHIROperationContext.PROPNAME_RESPONSE, response);

                return FHIROperationUtil.getOutputParameters(null);
            } else {
                throw new FHIROperationException("$import operation injected failure");
            }
        } catch (Exception e) {
            throw new FHIROperationException("$import operation", e);
        }
    }
}