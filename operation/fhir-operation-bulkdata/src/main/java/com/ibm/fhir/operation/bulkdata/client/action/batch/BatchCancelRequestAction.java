/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.client.action.batch;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.operation.bulkdata.client.action.BulkDataClientAction;
import com.ibm.fhir.operation.bulkdata.model.JobExecutionResponse;
import com.ibm.fhir.operation.bulkdata.util.BulkDataExportUtil;

/**
 * Per the specification, after a Bulk Data Request starts, the client may DELETE the request using the Content-Location (polling location)
 *
 * DELETE [polling content location]
 * Response - Success
 * HTTP Status Code of 202 Accepted
 * Optionally a FHIR OperationOutcome resource in the body
 * Response - Error Status
 * HTTP status code of 4XX or 5XX
 * The body SHALL be a FHIR OperationOutcome resource in JSON format
 *
 * @see http://hl7.org/fhir/uv/bulkdata/STU1/export/index.html#bulk-data-delete-request
 *
 *         // <b>Code Flow</b>
        // 1 - Check if the Job Exists and is Valid
        // 2 - Check if the Tenant is expected
        // 3 - Try to immediately DELETE (Optimistic)
        // A - SC_NO_CONTENT = SUCCESS GO_TO End
        // B - !SC_INTERNAL_ERROR GO_TO STOP
        // C - ERROR GO_TO Error
        // 4 - Try to stop the PUT /ibm/api/batch/jobinstances/<job>?action=stop
        // A - SC_CONTENT_REDIRECT (302) GO_TO Location to PUT
        // - Stop requests sent to the batch REST API must be sent directly to the executor where the job is running.
        // - Link -
        // https://www.ibm.com/support/knowledgecenter/SSEQTP_liberty/com.ibm.websphere.wlp.doc/ae/rwlp_batch_rest_api.html#rwlp_batch_rest_api__STOP_requests
        // B - SC_ACCEPTED (202) GO_TO ACCEPTED
        // RETURN - ACCEPTED: SC_ACCEPTED - STILL PROCESSING
        // RETURN - DONE: SC_OK - DONE DELETING

        // 1 and 2 (We know it exists)

        // - batchStatus - STARTING, STARTED, STOPPING, STOPPED, FAILED, COMPLETED, ABANDONED
 */
public class BatchCancelRequestAction implements BulkDataClientAction {
    private static final String CLASSNAME = BatchCancelRequestAction.class.getName();
    private static final Logger LOG = Logger.getLogger(CLASSNAME);

    private static final BulkDataExportUtil export = new BulkDataExportUtil();

    private static final List<String> INFLIGHT_STATUS = Arrays.asList("STARTING", "STARTED");

    private CloseableHttpClient cli = null;
    private String batchUrl = null;

    private Response.Status result = Response.Status.FORBIDDEN;

    @Override
    public void prepare(CloseableHttpClient cli, String batchUrl, String batchUser, String batchPass) {
        this.cli = cli;
        this.batchUrl = batchUrl;
    }

    @Override
    public Response.Status getResult() {
        return result;
    }

    @Override
    public void close() throws FHIROperationException {
        if (cli != null) {
            try {
                cli.close();
            } catch (IOException ex) {
                LOG.throwing(CLASSNAME, "close", ex);
                throw export.buildOperationException("Unable to close the Batch Client", IssueType.TRANSIENT);
            }
        }
    }

    @Override
    public void run(JobExecutionResponse jobDetails, String job) throws FHIROperationException {
        if (INFLIGHT_STATUS.contains(jobDetails.getBatchStatus())) {
            // If it's inflight, we want to stop it.
            stopJob(jobDetails, job);
        }

        // Delete IT
        deleteJob(jobDetails, job);

        // Check for a server-side error
        if (Status.INTERNAL_SERVER_ERROR == result) {
            throw export.buildOperationException("Canceling the Bulk Data Request has failed for the job; the content is not abandonded", IssueType.EXCEPTION);
        }
    }

    /**
     * stops the job (local or remote batch processor).
     *
     * @param jobDetails
     * @param job
     * @throws FHIROperationException
     */
    private void stopJob(JobExecutionResponse jobDetails, String job) throws FHIROperationException {
        // 4 - Try to stop the PUT /ibm/api/batch/jobinstances/<job>?action=stop
        // A - SC_CONTENT_REDIRECT (302) GO_TO Location to PUT
        // - Stop requests sent to the batch REST API must be sent directly to the executor where the job is running.
        // - Link -
        // https://www.ibm.com/support/knowledgecenter/SSEQTP_liberty/com.ibm.websphere.wlp.doc/ae/rwlp_batch_rest_api.html#rwlp_batch_rest_api__STOP_requests
        // B - SC_ACCEPTED (202) GO_TO ACCEPTED

        try {
            // NOT_LOCAL - follow location
            String baseUrl = batchUrl + "/jobexecutions/" + job + "?action=stop&purgeJobStoreOnly=false";

            // The documentation says this is a PUT and confirmed in the source code.
            // @see
            // https://www.ibm.com/support/knowledgecenter/SSEQTP_liberty/com.ibm.websphere.wlp.doc/ae/rwlp_batch_rest_api.html#rwlp_batch_rest_api__http_return_codes
            // Intentionally setting a null on the put entity.
            HttpPut stop = new HttpPut(baseUrl);
            CloseableHttpResponse stopResponse = cli.execute(stop);

            int statusCode = stopResponse.getStatusLine().getStatusCode();
            String responseString = null;
            try {
                HttpEntity entity = stop.getEntity();

                // Only if 200.
                if (statusCode == Status.OK.getStatusCode()) {
                    responseString = new BasicResponseHandler().handleResponse(stopResponse);
                }

                EntityUtils.consume(entity);
            } finally {
                stop.releaseConnection();
                stopResponse.close();
            }

            if (Status.FOUND.getStatusCode() == statusCode) {
                // It's on a different server, and must be in the cluster.
                // If not, we're going to connect to location, and try a put.
                String location = stopResponse.getFirstHeader("location").getValue();
                if (location != null) {
                    // No matter what, tell people we accepted the call.
                    baseUrl = location + "?action=stop";
                    HttpPut stopAgain = new HttpPut(baseUrl);
                    CloseableHttpResponse stopAgainResponse = cli.execute(stopAgain);

                    try {
                        // Skip consuming the body
                        int statusCodeAgain = stopAgainResponse.getStatusLine().getStatusCode();
                        if (LOG.isLoggable(Level.FINE)) {
                            LOG.fine("status code for stop on location is '" + statusCodeAgain + "' at location '" + location + "'");
                        }
                    } finally {
                        stop.releaseConnection();
                        stopResponse.close();
                    }
                }
                // Here, we could easily respond with an Exception/OperationOutcome, however An unexpected error has
                // occurred while stopping/deleting the job on a batch cluster member
                result = Response.Status.ACCEPTED;
            } else if (Status.OK.getStatusCode() == statusCode) {
                // Check if the Status is GOOD
                JobExecutionResponse response = JobExecutionResponse.Parser.parse(responseString);
                if (response.getBatchStatus().contains("STOPPING")) {
                    // Signal that we're in a stopping condition.
                    result = Response.Status.ACCEPTED;
                }
                // Don't Delete, let the client flow through again and DELETE.
            } else if (Status.CONFLICT.getStatusCode() == statusCode) {
                // SC_CONFLICT is used by the Open Liberty JBatch container to signal that the job is NOT RUNNING.
                // This is generally due to a conflicting identical call, we're responding immediately
                result = Response.Status.ACCEPTED;
            } else {
                // Error Condition (should be 400, but capturing here as a general error including Server Error).
                throw export.buildOperationException("An unexpected error has occurred while stopping/deleting the job", IssueType.TRANSIENT);
            }

        } catch (FHIROperationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw export.buildOperationException("An unexpected error has occurred while deleting the job", IssueType.TRANSIENT);
        }
    }

    /**
     * deletes the job
     *
     * @param jobDetails
     * @param job
     * @throws FHIROperationException
     */
    private void deleteJob(JobExecutionResponse jobDetails, String job) throws FHIROperationException {
        // Manage the times it loops through the deleteJob when there are active executions.
        int timesContinued = 0;
        while (timesContinued <= 2) {
            try {
                // Example: https://localhost:9443/ibm/api/batch/jobinstances/9
                // We choose to purgeJobStoreOnly=false which is the default.
                // The logs are purged when deleted.
                // The tenant is known, and now we need to query to delete the Job.

                String baseUrl = batchUrl + "/v4/jobinstances/" + job;
                HttpDelete delete = new HttpDelete(baseUrl);

                try (CloseableHttpResponse deleteResponse = cli.execute(delete)) {
                    HttpEntity entity = deleteResponse.getEntity();

                    // Debug Logging outputs the API response.
                    String responseString = null;
                    int status = deleteResponse.getStatusLine().getStatusCode();
                    if (LOG.isLoggable(Level.FINE)) {
                        responseString =
                                IOUtils.toString(new InputStreamReader(deleteResponse.getEntity().getContent()));
                        LOG.fine("Delete Job for Tenant Status " + status);
                        LOG.fine("The Response body is [" + responseString + "]");
                    }

                    // 409 or 200
                    if (Status.CONFLICT.getStatusCode() == status || Status.OK.getStatusCode() == status) {
                        result = Response.Status.ACCEPTED;
                        return;
                    } else if (status == Status.INTERNAL_SERVER_ERROR.getStatusCode() && timesContinued == 2) {
                        throw export.buildOperationException("The existing job has failed to delete", IssueType.EXCEPTION);
                    }
                    // Anything other than NO_CONTENT and BAD_REQUEST then fall through... else, we retry...
                    else if (Status.NO_CONTENT.getStatusCode() != status
                            && Status.BAD_REQUEST.getStatusCode() != status) {
                        LOG.fine(() -> "CONTENT STATUS -> " + status);
                        EntityUtils.consume(entity);
                        result = Response.Status.ACCEPTED;
                        return;
                    }

                    // Finally, when !LOGGING we want to make sure the responseString is populated.
                    if (responseString == null) {
                        responseString = new BasicResponseHandler().handleResponse(deleteResponse);
                    }

                    EntityUtils.consume(entity);
                } finally {
                    delete.releaseConnection();
                }
                timesContinued++;
            } catch (HttpResponseException ex) {
                // org.apache.http.client.HttpResponseException: status code: 409, reason phrase: Conflict
                LOG.throwing(CLASSNAME, "runDeleteJobForTenant", ex);
                stopJob(jobDetails, job);
                timesContinued++;
            } catch (FHIROperationException ex) {
                throw ex;
            } catch (Exception ex) {
                LOG.throwing(CLASSNAME, "runDeleteJobForTenant", ex);
                throw export.buildOperationException("An unexpected error has occurred while deleting the job", IssueType.TRANSIENT);
            }
        }
    }
}