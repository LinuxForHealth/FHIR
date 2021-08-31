/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.client.action.batch;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
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
 * <code>
 * DELETE [polling content location]
 * Response - Success
 * HTTP Status Code of 202 Accepted
 * Optionally a FHIR OperationOutcome resource in the body
 * Response - Error Status
 * HTTP status code of 4XX or 5XX
 * The body SHALL be a FHIR OperationOutcome resource in JSON format
 * </code>
 *
 * @see http://hl7.org/fhir/uv/bulkdata/STU1/export/index.html#bulk-data-delete-request
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
        List<JobExecutionResponse> jobExecutions = readJobExecutionsDetails(job);

        // Stop the Job's execution across all JobExecutions
        stopJobExecutions(jobExecutions);

        // Delete The Job
        deleteJob(job);

        // Check for a server-side error
        if (Status.INTERNAL_SERVER_ERROR == result) {
            throw export.buildOperationException("Canceling the Bulk Data Request has failed for the job; the content is not abandonded", IssueType.EXCEPTION);
        }
    }

    /**
     * Gets the Job Executions details for the specific job, so we can iterate over and stop the specific JobExecutions
     *
     * @param job identifier of the job which matrixed to the JobExecutions
     * @return
     * @throws FHIROperationException
     *
     * @implNote we re-read this as the job **could be** in flight
     */
    private List<JobExecutionResponse> readJobExecutionsDetails(String job) throws FHIROperationException {
        List<JobExecutionResponse> responses = new ArrayList<>();
        try {
            // URL: https://{{SERVER_HOSTNAME}}/ibm/api/batch/jobinstances/140/jobexecutions
            // Status:
            // 200 - Array and Iterate over the Array
            // 400 - Job Id doesn't exist... flow back as NOT_FOUND
            // 500 - Server Side error, flow back as 500
            String baseUrl = batchUrl + "/v4/jobinstances/" + job + "/jobexecutions";
            HttpGet get = new HttpGet(baseUrl);

            try (CloseableHttpResponse getResponse = cli.execute(get)) {
                HttpEntity entity = getResponse.getEntity();

                int status = getResponse.getStatusLine().getStatusCode();
                switch (status) {
                    case 200:
                        String responseString = IOUtils.toString(new InputStreamReader(getResponse.getEntity().getContent()));
                        LOG.fine(() -> "Job Execution Detail for Tenant Status [" + status + "] body is [" + responseString + "]");
                        responses.addAll(JobExecutionResponse.Parser.parseArray(responseString));
                        break;
                    case 400:
                        LOG.fine(() -> "Job is not found while reading the job execution response, this is actually OK");
                        break;
                    default:
                        throw export.buildOperationException("An unexpected error has occurred while reading the job details during a stop/delete", IssueType.TRANSIENT);
                }

                EntityUtils.consume(entity);
            } finally {
                get.releaseConnection();
            }
        } catch (FHIROperationException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.throwing(CLASSNAME, "readJobExecutionsDetails", ex);
            throw export.buildOperationException("An unexpected error has occurred while reading the job details during a stop/delete", IssueType.TRANSIENT);
        }
        return responses;
    }

    /**
     * Executes over the list of JobExecutionResponses the stopJobExecution.
     *
     * @param jobExecutions a list of the Job's executions (a number may have been stopped, failed, et cetra)
     * @throws FHIROperationException
     */
    private void stopJobExecutions(List<JobExecutionResponse> jobExecutions) throws FHIROperationException {
        // Some of the JobExecutions might... be stopped, we only care if they are inflight.
        for (JobExecutionResponse jobExecution : jobExecutions) {
            if (INFLIGHT_STATUS.contains(jobExecution.getBatchStatus())) {
                // If it's inflight, we want to stop it.
                stopJobExecution(jobExecution);
            }
        }
    }

    /**
     * execute a specific stop on a jobExecution.
     *
     * @param jobExecution
     * @throws FHIROperationException
     */
    private void stopJobExecution(JobExecutionResponse jobExecution) throws FHIROperationException {
        // @see https://github.com/OpenLiberty/open-liberty/blob/2fd4a880754c37a988c5ed9ac4f1ea5988e465d6/dev/com.ibm.ws.jbatch.rest/src/com/ibm/ws/jbatch/rest/internal/resources/JobExecutions.java#L289
        // @see https://github.com/OpenLiberty/open-liberty/blob/2fd4a880754c37a988c5ed9ac4f1ea5988e465d6/dev/com.ibm.ws.jbatch.rest/src/com/ibm/ws/jbatch/rest/internal/resources/JobExecutions.java#L647
        // @see https://www.ibm.com/support/knowledgecenter/SSEQTP_liberty/com.ibm.websphere.wlp.doc/ae/rwlp_batch_rest_api.html#rwlp_batch_rest_api__http_return_codes

        try {
            // URL: <code>/batch/jobexecutions/{jobexecutionid}</code>
            // Uses permitRedirect=false (it'll execute the call from the server side)
            String baseUrl = batchUrl + "/jobexecutions/" + jobExecution.getExecutionId() + "?action=stop&purgeJobStoreOnly=false&permitRedirect=false";

            HttpPut stop = new HttpPut(baseUrl);
            try (CloseableHttpResponse stopResponse = cli.execute(stop)){
                HttpEntity entity = stop.getEntity();

                int statusCode = stopResponse.getStatusLine().getStatusCode();
                switch (statusCode) {
                    case 200:
                        String responseString = new BasicResponseHandler().handleResponse(stopResponse);
                        // Check if the Status is GOOD
                        JobExecutionResponse response = JobExecutionResponse.Parser.parse(responseString);
                        if (response.getBatchStatus().contains("STOPPING")) {
                            // Signal that we're in a stopping condition.
                            // we're going to wait up to 100ms
                            Thread.sleep(100);
                        }
                        break;
                    case 400: // Bad Request - NoSuchJobExecutionException
                    case 409: // Conflict - JobExecutionNotRunningException
                        // SC_CONFLICT is used by the Open Liberty JBatch container to signal that the job is NOT RUNNING.
                        LOG.fine("NoSuchJobExecutionException is hit, and therefore we are treating this as stopped, and flow to the next step.");
                        break;
                    case 302: // SC_FOUND - Redirect - BatchJobNotLocalException
                        stopAtRemoteLocation(stopResponse);
                        break;
                    default:
                        throw export.buildOperationException("An unexpected error has occurred while stopping/deleting the job", IssueType.TRANSIENT);
                }

                EntityUtils.consume(entity);
            } finally {
                stop.releaseConnection();
            }
        } catch (FHIROperationException ex) {
            throw ex;
        } catch (Exception ex) {
            LOG.throwing(CLASSNAME, "stopJobExecution", ex);
            throw export.buildOperationException("An unexpected error has occurred while deleting the job", IssueType.TRANSIENT);
        }
    }

    /**
     * stops the job at a remote location
     * @param stopResponse
     * @throws ClientProtocolException
     * @throws IOException
     */
    public void stopAtRemoteLocation(CloseableHttpResponse stopResponse) throws ClientProtocolException, IOException {
     // It's on a different server, and must be in the cluster.
        // If not, we're going to connect to location, and try a put.
        String location = stopResponse.getFirstHeader("location").getValue();
        if (location != null) {
            // No matter what, tell people we accepted the call.
            String baseUrl = location + "?action=stop";
            HttpPut stopAgain = new HttpPut(baseUrl);

            try (CloseableHttpResponse stopAgainResponse = cli.execute(stopAgain)) {
                // Skip consuming the body
                int statusCodeAgain = stopAgainResponse.getStatusLine().getStatusCode();
                if (LOG.isLoggable(Level.FINE)) {
                    LOG.fine("status code for stop on location is '" + statusCodeAgain + "' at location '" + location + "'");
                }
            } finally {
                stopAgain.releaseConnection();
            }
        }
        // Here, we could easily respond with an Exception/OperationOutcome, however An unexpected error has
        // occurred while stopping/deleting the job on a batch cluster member
        result = Response.Status.ACCEPTED;
    }

    /**
     * deletes the job
     *
     * @param job
     * @throws FHIROperationException
     */
    private void deleteJob(String job) throws FHIROperationException {
        // Manage the times it loops through the deleteJob when there are active executions.
        int timesContinued = 0;
        while (timesContinued <= 5 && result.getStatusCode() != 202) {
            try {
                // Example: https://localhost:9443/ibm/api/batch/jobinstances/9
                // We choose to purgeJobStoreOnly=false which is the default.
                // The logs are purged when deleted.
                // The tenant is known, and now we need to query to delete the Job.

                String baseUrl = batchUrl + "/v4/jobinstances/" + job + "?purgeJobStoreOnly=true";
                HttpDelete delete = new HttpDelete(baseUrl);

                try (CloseableHttpResponse deleteResponse = cli.execute(delete)) {
                    HttpEntity entity = deleteResponse.getEntity();

                    // Debug Logging outputs the API response.
                    String responseString = null;
                    int status = deleteResponse.getStatusLine().getStatusCode();
                    switch (status) {
                        case 200:
                        case 409:
                            result = Response.Status.ACCEPTED;
                            if (LOG.isLoggable(Level.FINE)) {
                                responseString =
                                        IOUtils.toString(new InputStreamReader(deleteResponse.getEntity().getContent()));
                                LOG.fine("Delete Job for Tenant Status " + status);
                                LOG.fine("The Response body is [" + responseString + "]");
                            }
                            break;
                        case 500:
                            if (timesContinued == 5) {
                                throw export.buildOperationException("The existing job has failed to delete", IssueType.EXCEPTION);
                            }
                            break;
                        default:
                            LOG.fine("Status [" + status + "] Times Continued [" + timesContinued + "]");
                    }

                    EntityUtils.consume(entity);
                } finally {
                    delete.releaseConnection();
                }
                timesContinued++;

                // Add a sleep to not hammer it.
                if (result.getStatusCode() == 202) {
                    Thread.sleep(100);
                }
            } catch (FHIROperationException ex) {
                throw ex;
            } catch (Exception ex) {
                LOG.throwing(CLASSNAME, "runDeleteJobForTenant", ex);
                throw export.buildOperationException("An unexpected error has occurred while deleting the job", IssueType.TRANSIENT);
            }
        }
    }
}