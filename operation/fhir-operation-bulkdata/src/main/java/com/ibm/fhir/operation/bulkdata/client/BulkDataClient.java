/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.bulkdata.client;

import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64.Encoder;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.KeyGenerator;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.operation.bulkdata.OperationConstants;
import com.ibm.fhir.operation.bulkdata.OperationConstants.ExportType;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.model.JobExecutionResponse;
import com.ibm.fhir.operation.bulkdata.model.JobInstanceRequest;
import com.ibm.fhir.operation.bulkdata.model.JobInstanceResponse;
import com.ibm.fhir.operation.bulkdata.model.PollingLocationResponse;
import com.ibm.fhir.operation.bulkdata.model.transformer.JobIdEncodingTransformer;
import com.ibm.fhir.operation.bulkdata.model.type.Input;
import com.ibm.fhir.operation.bulkdata.model.type.JobParameter;
import com.ibm.fhir.operation.bulkdata.model.type.JobType;
import com.ibm.fhir.operation.bulkdata.model.type.StorageDetail;
import com.ibm.fhir.operation.bulkdata.util.BulkDataExportUtil;

/**
 * BulkData Client to connect to the other server.
 * <a href=
 * "https://www.ibm.com/support/knowledgecenter/en/SSEQTP_liberty/com.ibm.websphere.wlp.doc/ae/rwlp_batch_rest_api.html#rwlp_batch_rest_api__http_return_codes">
 */
public class BulkDataClient {

    private static final String CLASSNAME = BulkDataClient.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

    private static final Encoder encoder = java.util.Base64.getUrlEncoder().withoutPadding();

    private static final SecureRandom RANDOM = new SecureRandom();

    private static final HttpWrapper wrapper = new HttpWrapper();

    private static final JobIdEncodingTransformer transformer = new JobIdEncodingTransformer();

    // @formatter:off
    private static final DateTimeFormatter DATE_TIME_PARSER_FORMATTER =
            new DateTimeFormatterBuilder()
                    .appendPattern("yyyy")
                    .optionalStart()
                        .appendLiteral('/')
                        .appendPattern("MM")
                        .optionalStart()
                            .appendLiteral('/')
                            .appendPattern("dd")
                            .optionalStart()
                                .appendLiteral(" ")
                                .optionalStart()
                                .appendPattern("HH")
                                .optionalStart()
                                    .appendLiteral(':')
                                    .appendPattern("mm")
                                    .optionalStart()
                                        .appendLiteral(':')
                                        .appendPattern("ss")
                                        .optionalStart()
                                            .appendLiteral('.')
                                            .appendPattern("SSS")
                                        .optionalEnd()
                                    .optionalEnd()
                                .optionalEnd()
                                .optionalStart()
                                    .appendLiteral(' ')
                                    .appendPattern("Z")
                                .optionalEnd()
                            .optionalEnd()
                            .optionalEnd()
                        .optionalEnd()
                    .optionalEnd()
                    .toFormatter();
    // @formatter:on

    private String source = null;
    private String outcome = null;
    private String incomingUrl = null;
    private String baseUri = null;
    private ConfigurationAdapter adapter = null;

    /**
     *
     * @param bulkdataSource the source
     * @param outcomeSource
     * @param incomingUrl
     * @param baseUri
     * @param adapter
     */
    public BulkDataClient(String bulkdataSource, String outcomeSource, String incomingUrl, String baseUri, ConfigurationAdapter adapter) {
        this.source = bulkdataSource;
        this.outcome = outcomeSource;
        this.incomingUrl = incomingUrl;
        this.baseUri = baseUri;
        this.adapter = adapter;
    }

    /**
     *
     * @param since
     * @param types
     * @param exportType
     * @param outputFormat
     * @param typeFilters
     * @param groupId
     * @return
     * @throws Exception
     */
    public String submitExport(Instant since, List<String> types, ExportType exportType, String outputFormat, String typeFilters, String groupId) throws Exception {
        JobInstanceRequest.Builder builder = JobInstanceRequest.builder();
        builder.applicationName(adapter.getApplicationName());
        builder.moduleName(adapter.getModuleName());

        String fhirTenant = FHIRRequestContext.get().getTenantId();
        builder.fhirTenant(fhirTenant);

        String fhirDataStoreId = FHIRRequestContext.get().getDataStoreId();
        builder.fhirDataStoreId(fhirDataStoreId);

        String incomingUrl = FHIRRequestContext.get().getOriginalRequestUri();
        builder.incomingUrl(incomingUrl);

        builder.outcome(outcome);
        builder.source(source);

        builder.cosBucketPathPrefix(getRandomPrefix());
        builder.fhirExportFormat(outputFormat);

        // Export Type - FHIR
        switch (exportType) {
        case PATIENT:
            builder.jobXMLName(JobType.EXPORT_PATIENT.value());
            break;
        case GROUP:
            builder.jobXMLName(JobType.EXPORT_GROUP.value());
            builder.fhirPatientGroupId(groupId);
            break;
        default:
            // We have two implementations for system export, but the "fast" version
            // does not support typeFilters. We also allow the configuration to
            // force use of the legacy implementation for those who don't like the change

            if (typeFilters != null
                    || !adapter.isFastExport()
                    || FHIRMediaType.APPLICATION_PARQUET.equals(outputFormat)) {
                // Use the legacy implementation
                builder.jobXMLName(JobType.EXPORT.value());
            } else {
                // No typeFilter, so we use the fast export which bypasses FHIR search
                builder.jobXMLName(JobType.EXPORT_FAST.value());
            }
            break;
        }

        String resourceType = String.join(",", types);
        builder.fhirResourceType(resourceType);

        if (since != null) {
            builder.fhirSearchFromDate(since.getValue().format(Instant.PARSER_FORMATTER));
        } else {
            builder.fhirSearchFromDate("1970-01-01T00:00:00Z");
        }

        if (typeFilters != null) {
            builder.fhirTypeFilters(typeFilters);
        }

        String entityStr = JobInstanceRequest.Writer.generate(builder.build(), true);
        if (log.isLoggable(Level.FINE)) {
            log.fine("Job instance request: " + entityStr);
        }

        String baseUrl = adapter.getCoreApiBatchUrl() + "/jobinstances";

        CloseableHttpClient cli = wrapper.getHttpClient(adapter.getCoreApiBatchUser(),adapter.getCoreApiBatchPassword());
        HttpPost jobPost = new HttpPost(baseUrl);
        StringEntity entity = new StringEntity(entityStr, ContentType.create("plain/text", Consts.UTF_8));
        jobPost.setEntity(entity);
        CloseableHttpResponse jobResponse = cli.execute(jobPost);

        int status = -1;
        String jobId = "-1";
        try {
            status = jobResponse.getStatusLine().getStatusCode();
            handleStandardResponseStatus(status);

            // Debug / Dev only
            if (log.isLoggable(Level.FINE)) {
                log.fine("$export response (HTTP " + status + ")");
            }

            if (status != 201) {
                // Job is not created
                throw BulkDataExportUtil.buildOperationException("Unable to create the $export job", IssueType.INVALID);
            }

            String responseString = new BasicResponseHandler().handleResponse(jobResponse);

            JobInstanceResponse response = JobInstanceResponse.Parser.parse(responseString);

            jobId = Integer.toString(response.getInstanceId());

        } finally {
            jobPost.releaseConnection();
            jobResponse.close();
        }
        cli.close();

        return baseUri + "/$bulkdata-status?job=" + transformer.endcodeJobId(jobId);
    }

    /**
     * @param job
     * @return
     * @throws Exception
     */
    public PollingLocationResponse status(String job) throws Exception {

        // Example: https://localhost:9443/ibm/api/batch/jobinstances/9
        // Get the job instance status, we need it to get the current job execution id of the job instance.
        // It's highly unlikely at this point the job is any other job than the client's job.
        String baseUrl = adapter.getCoreApiBatchUrl() + "/jobinstances/" + job;

        CloseableHttpClient cli = wrapper.getHttpClient(
                adapter.getCoreApiBatchUser(),
                adapter.getCoreApiBatchPassword());
        HttpGet statusGet = new HttpGet(baseUrl);
        CloseableHttpResponse statusResponse = cli.execute(statusGet);

        JobInstanceResponse bulkExportJobInstanceResponse = null;
        try {
            handleStandardResponseStatus(statusResponse.getStatusLine().getStatusCode());
            HttpEntity entity = statusResponse.getEntity();

            try(InputStream is = entity.getContent()){
                bulkExportJobInstanceResponse = JobInstanceResponse.Parser.parse(is);
            }
            EntityUtils.consume(entity);
        } finally {
            statusGet.releaseConnection();
            statusResponse.close();
        }

        PollingLocationResponse result = null;
        try {
            // Example: https://localhost:9443/ibm/api/batch/jobinstances/9/jobexecutions/2
            // Get the current job execution status of the job instance.
            baseUrl = adapter.getCoreApiBatchUrl() + "/jobinstances/" + job +
                    "/jobexecutions/" + bulkExportJobInstanceResponse.getExecutionId();

            HttpGet executionStatusGet = new HttpGet(baseUrl);
            CloseableHttpResponse executionStatusResponse = cli.execute(executionStatusGet);

            JobExecutionResponse bulkExportJobExecutionResponse = null;
            try {
                handleStandardResponseStatus(executionStatusResponse.getStatusLine().getStatusCode());
                HttpEntity entity = executionStatusResponse.getEntity();

                try(InputStream is = entity.getContent()){
                    bulkExportJobExecutionResponse = JobExecutionResponse.Parser.parse(is);
                }
                EntityUtils.consume(entity);
            } finally {
                executionStatusGet.releaseConnection();
                executionStatusResponse.close();
            }

            verifyTenant(bulkExportJobExecutionResponse.getJobParameters());

            if (log.isLoggable(Level.FINE)) {
                log.warning("Logging the JobExecutionResponse Details -> \n"
                        + JobExecutionResponse.Writer.generate(bulkExportJobExecutionResponse, false));
            }

            String batchStatus = bulkExportJobExecutionResponse.getBatchStatus();
            if (batchStatus == null) {
                throw BulkDataExportUtil.buildOperationException("Error while reading the bulk export status", IssueType.INVALID);
            } else if (OperationConstants.SUCCESS_STATUS.contains(batchStatus)) {
                result = process(bulkExportJobExecutionResponse);
            } else if (OperationConstants.FAILED_STATUS.contains(batchStatus)) {
                /*
                 * @implNote we should handle this in the Job not in the client.
                 * - In the case of partial success a useful response should be sent back.
                 *
                 * In the case of a partial success, the server SHALL use a 200 status code instead of 4XX or 5XX.
                 * The choice of when to determine that an export job has failed in its entirety (error status) vs
                 * returning a partial success (complete status) is left up to the implementer.
                 * XXX Can we do something better like return a 2XX response with a link to a file that explains the
                 * error?
                 * What if we couldn't connect with S3 / Cloud object store in the first place?
                 *
                 */
                throw BulkDataExportUtil.buildOperationException("The job has failed", IssueType.EXCEPTION);
            } else if (OperationConstants.STOPPED_STATUS.contains(batchStatus)) {
                // If the job is stopped, then restart the job.
                baseUrl = adapter.getCoreApiBatchUrl() + "/jobinstances/" + job + "?action=restart&reusePreviousParams=true";

                HttpPut restart = new HttpPut(baseUrl);
                CloseableHttpResponse restartResponse = cli.execute(restart);

                try {
                    HttpEntity entity = restartResponse.getEntity();

                    if (restartResponse.getStatusLine().getStatusCode() != Status.OK.getStatusCode()) {
                        throw BulkDataExportUtil.buildOperationException("The job has failed to restart", IssueType.EXCEPTION);
                    }
                    EntityUtils.consume(entity);
                } finally {
                    restart.releaseConnection();
                    restartResponse.close();
                }
            }

            // Finally close it out.
            cli.close();
        } catch (FHIROperationException fe) {
            throw fe;
        } catch (Exception ex) {
            throw BulkDataExportUtil.buildOperationException("An unexpected error has ocurred while checking the status - " + ex.getMessage(), IssueType.TRANSIENT);
        }

        return result;
    }

    public void handleStandardResponseStatus(int httpStatus) throws FHIROperationException {
        // e.g. if it comes back with 404 it may fail on the JobInstanceResponse.Parser.parse!

        if(httpStatus == 401) {
            throw BulkDataExportUtil.buildOperationException("Unauthorized to access the framework", IssueType.FORBIDDEN);
        }

        if (httpStatus == 400) {
            throw BulkDataExportUtil.buildOperationException("Batch Job not found", IssueType.NOT_FOUND);
        }

        if (httpStatus == 404) {
            throw BulkDataExportUtil.buildOperationException("Bad URL for Batch Framework", IssueType.FORBIDDEN);
        }

        if (httpStatus == 500) {
            //if (responseStr == null || responseStr.isEmpty() || responseStr.startsWith("Unexpected request/response.")) {
            //    throw BulkDataExportUtil.buildOperationException("Invalid job id sent to $bulkdata-status", IssueType.INVALID);
            //}
            throw BulkDataExportUtil.buildOperationException("Server Side Error for Batch Framework", IssueType.EXCEPTION);
        }

        if (httpStatus == 200 && log.isLoggable(Level.FINE)) {
            log.fine("Successuflly accessed job");
        }
    }

    /**
     * @param job
     * @return status code
     * @throws Exception
     */
    public Response.Status delete(String job) throws Exception {
        // <b>Code Flow</b>
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
        runVerificationOfTenantForJob(job);

        // 3 optimistic delete (empty)
        Response.Status status = runDeleteJobForTenant(job);

        // Check 3 (B), else fall through (A).
        // Default Response is Accepted (202)
        // The Alternative Response is 200

        // Check for a server-side error
        if (Status.INTERNAL_SERVER_ERROR == status) {
            // 3.C - ERROR Condition
            // The Server hit an error
            throw BulkDataExportUtil.buildOperationException("Deleting the job has failed; the content is not abandonded", IssueType.EXCEPTION);
        } else if (Status.NO_CONTENT != status) {
            // The Delete was unsuccessful
            // 3.B - STOP Condition and now step 4 in the flow.
            status = runStopOfTenantJob(job);
        }
        return status;
    }

    /*
     * stops the job (local or remote batch processor).
     */
    private Response.Status runStopOfTenantJob(String job) throws Exception {
        // 4 - Check the State of the Job (batchStatus)
        // - batchStatus - STARTING, STARTED, STOPPING, STOPPED, FAILED, COMPLETED, ABANDONED

        // 4 - Try to stop the PUT /ibm/api/batch/jobinstances/<job>?action=stop
        // A - SC_CONTENT_REDIRECT (302) GO_TO Location to PUT
        // - Stop requests sent to the batch REST API must be sent directly to the executor where the job is running.
        // - Link -
        // https://www.ibm.com/support/knowledgecenter/SSEQTP_liberty/com.ibm.websphere.wlp.doc/ae/rwlp_batch_rest_api.html#rwlp_batch_rest_api__STOP_requests
        // B - SC_ACCEPTED (202) GO_TO ACCEPTED
        Response.Status status = Response.Status.NO_CONTENT;
        try {
            // NOT_LOCAL - follow location
            String baseUrl =  adapter.getCoreApiBatchUrl() + "/jobexecutions/" + job + "?action=stop";

            // The documentation says this is a PUT and confirmed in the source code.
            // @see
            // https://www.ibm.com/support/knowledgecenter/SSEQTP_liberty/com.ibm.websphere.wlp.doc/ae/rwlp_batch_rest_api.html#rwlp_batch_rest_api__http_return_codes
            // Intentionally setting a null on the put entity.
            CloseableHttpClient cli = wrapper.getHttpClient(adapter.getCoreApiBatchUser(), adapter.getCoreApiBatchPassword());
            HttpPut stop = new HttpPut(baseUrl);
            CloseableHttpResponse stopResponse = cli.execute(stop);

            int statusCode = stopResponse.getStatusLine().getStatusCode();
            String responseString = null;
            try {
                HttpEntity entity = stop.getEntity();

                if (statusCode != Status.OK.getStatusCode()) {
                    throw BulkDataExportUtil.buildOperationException("The job has failed to stop", IssueType.EXCEPTION);
                }
                responseString = new BasicResponseHandler().handleResponse(stopResponse);
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
                        if(log.isLoggable(Level.FINE)) {
                            log.fine("status code for stop on location is '" + statusCodeAgain + "' at location '" + location + "'");
                        }
                    } finally {
                        stop.releaseConnection();
                        stopResponse.close();
                    }
                }
                // Here, we could easily respond with an Exception/OperationOutcome, however An unexpected error has
                // occurred while stopping/deleting the job on a batch cluster member
                status = Response.Status.ACCEPTED;
            } else if (Status.OK.getStatusCode() == statusCode) {
                // Check if the Status is GOOD
                JobExecutionResponse response = JobExecutionResponse.Parser.parse(responseString);
                if (response.getBatchStatus().contains("STOPPING")) {
                    // Signal that we're in a stopping condition.
                    status = Response.Status.ACCEPTED;
                }
                // Don't Delete, let the client flow through again and DELETE.
            } else if (Status.CONFLICT.getStatusCode() == statusCode) {
                // SC_CONFLICT is used by the Open Liberty JBatch container to signal that the job is NOT RUNNING.
                // This is generally due to a conflicting identical call, we're responding immediately
                status = Response.Status.ACCEPTED;
            } else {
                // Error Condition (should be 400, but capturing here as a general error including Server Error).
                throw BulkDataExportUtil.buildOperationException("An unexpected error has ocurred while stopping/deleting the job", IssueType.TRANSIENT);
            }
            cli.close();
        } catch (FHIROperationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw BulkDataExportUtil.buildOperationException("An unexpected error has ocurred while deleting the job", IssueType.TRANSIENT);
        }
        return status;
    }

    /*
     * deletes the job
     */
    private Response.Status runDeleteJobForTenant(String job) throws Exception {
        Response.Status status = Response.Status.NO_CONTENT;

        try {
            // Example: https://localhost:9443/ibm/api/batch/jobexecutions/9
            // We choose to purgeJobStoreOnly=false which is the default.
            // The logs are purged when deleted.
            // The tenant is known, and now we need to query to delete the Job.

            HttpWrapper wrapper = new HttpWrapper();
            CloseableHttpClient cli = wrapper.getHttpClient(adapter.getCoreApiBatchUser(),adapter.getCoreApiBatchPassword());

            String baseUrl = adapter.getCoreApiBatchUrl() + "/jobexecutions/" + job;
            HttpDelete delete = new HttpDelete(baseUrl);
            CloseableHttpResponse deleteResponse = cli.execute(delete);

            try {
                HttpEntity entity = deleteResponse.getEntity();

                // Debug Logging outputs the API response.
                if (log.isLoggable(Level.FINE)) {
                    String responseString = new BasicResponseHandler().handleResponse(deleteResponse);
                    log.fine("Delete Job for Tenant Status " + deleteResponse.getStatusLine().getStatusCode());
                    log.fine("The Response body is [" + responseString + "]");
                }

                if (Status.NO_CONTENT.getStatusCode() != deleteResponse.getStatusLine().getStatusCode() && Status.BAD_REQUEST.getStatusCode() != deleteResponse.getStatusLine().getStatusCode()) {
                    status = Response.Status.fromStatusCode(deleteResponse.getStatusLine().getStatusCode());
                }

                if (deleteResponse.getStatusLine().getStatusCode() != Status.OK.getStatusCode()) {
                    throw BulkDataExportUtil.buildOperationException("The existing job has failed to delete", IssueType.EXCEPTION);
                }
                EntityUtils.consume(entity);
            } finally {
                delete.releaseConnection();
                deleteResponse.close();
            }
        } catch (FHIROperationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw BulkDataExportUtil.buildOperationException("An unexpected error has ocurred while deleting the job", IssueType.TRANSIENT);
        }
        return status;
    }

    /*
     * checks the job is owned by the tenant.
     */
    private void runVerificationOfTenantForJob(String job) throws Exception {
        String baseUrl = adapter.getCoreApiBatchUrl() + "/jobexecutions/" + job;

        try {
            JobExecutionResponse response = null;
            HttpWrapper wrapper = new HttpWrapper();
            CloseableHttpClient cli = wrapper.getHttpClient(adapter.getCoreApiBatchUser(),adapter.getCoreApiBatchPassword());

            HttpGet get = new HttpGet(baseUrl);
            CloseableHttpResponse getResponse = cli.execute(get);

            try {
                HttpEntity entity = getResponse.getEntity();

                handleStandardResponseStatus(getResponse.getStatusLine().getStatusCode());

                String responseString = new BasicResponseHandler().handleResponse(getResponse);
                // Debug Logging outputs the API response.
                if (log.isLoggable(Level.FINE)) {

                    log.fine("Delete Job for Tenant Status " + getResponse.getStatusLine().getStatusCode());
                    log.fine("The Response body is [" + responseString + "]");
                }

                if (responseString == null || responseString.isEmpty() || responseString.startsWith("Unexpected request/response.")) {
                    throw BulkDataExportUtil.buildOperationException("Invalid job id sent to $bulkdata-status", IssueType.INVALID);
                }

                response = JobExecutionResponse.Parser.parse(responseString);
                verifyTenant(response.getJobParameters());

                EntityUtils.consume(entity);
            } finally {
                get.releaseConnection();
                getResponse.close();
            }

        } catch (FHIROperationException fe) {
            throw fe;
        } catch (Exception ex) {
            throw BulkDataExportUtil.buildOperationException("An unexpected error has ocurred while deleting the job", IssueType.EXCEPTION);
        }
    }

    /**
     * verifies the tenant based on the job parameters.
     *
     * @param jobParameters
     * @throws FHIROperationException
     */
    public void verifyTenant(JobParameter jobParameters) throws FHIROperationException {
        String fhirTenant = adapter.getTenant();
        if (jobParameters == null || jobParameters.getFhirTenant() == null
                || !jobParameters.getFhirTenant().equals(fhirTenant)) {
            log.warning("Tenant not authorized to access job [" + fhirTenant + "] jobParameter [" + jobParameters.getFhirTenant() + "]");
            throw BulkDataExportUtil.buildOperationException("Tenant not authorized to access job", IssueType.FORBIDDEN);
        }
    }

    /**
     * @param response
     * @return
     */
    private PollingLocationResponse process(JobExecutionResponse response) {
        PollingLocationResponse result = new PollingLocationResponse();

        // Assemble the URL
        String resourceTypes = response.getJobParameters().getFhirResourceType();
        String cosBucketPathPrefix = response.getJobParameters().getCosBucketPathPrefix();

        String baseUrl = adapter.getSourceEndpointExternal(source);

        String request = "$import";
        if (resourceTypes != null) {
            request = "$export?_type=" + resourceTypes;
        }
        result.setRequest(request);
        result.setRequiresAccessToken(false);

        // Outputs lastUpdatedTime as yyyy-MM-dd'T'HH:mm:ss
        String lastUpdatedTime = response.getLastUpdatedTime();
        TemporalAccessor acc = DATE_TIME_PARSER_FORMATTER.parse(lastUpdatedTime);
        result.setTransactionTime(Instant.PARSER_FORMATTER.format(acc));

        // Compose outputs for all exported ndjson files from the batch job exit status,
        // e.g, Patient[1000,1000,200]:Observation[1000,1000,200],
        // COMPLETED means no file exported.
        String exitStatus = response.getExitStatus();
        log.info(exitStatus);
        if (!"COMPLETED".equals(exitStatus) && request.contains("$export")) {
            List<String> resourceTypeInfs = Arrays.asList(exitStatus.split("\\s*:\\s*"));
            List<PollingLocationResponse.Output> outputList = new ArrayList<>();
            for (String resourceTypeInf : resourceTypeInfs) {
                String resourceType = resourceTypeInf.substring(0, resourceTypeInf.indexOf("["));
                String[] resourceCounts =
                        resourceTypeInf.substring(resourceTypeInf.indexOf("[") + 1, resourceTypeInf.indexOf("]")).split("\\s*,\\s*");
                for (int i = 0; i < resourceCounts.length; i++) {
                    String downloadUrl = baseUrl + "/" + cosBucketPathPrefix + "/" + resourceType + "_" + (i + 1)
                            + ".ndjson";
                    outputList.add(new PollingLocationResponse.Output(resourceType, downloadUrl, resourceCounts[i]));
                }
            }
            result.setOutput(outputList);
        }

        if (request.contains("$import")) {
            // Currently there is no output
            log.fine("Hit the case where we don't form output with counts");
            List<Input> inputs = response.getJobParameters().getInputs();

            List<PollingLocationResponse.Output> outputs = new ArrayList<>();
            List<PollingLocationResponse.Output> errors = new ArrayList<>();
            List<String> responseCounts = Arrays.asList(exitStatus.split(","));
            Iterator<String> iter = responseCounts.iterator();
            for (Input input : inputs) {
                String[] counts = iter.next().replace("[", "").replace("]", "").split(":");
                outputs.add(new PollingLocationResponse.Output("OperationOutcome", input.getUrl() + "_oo_success.ndjson", counts[0]));
                errors.add(new PollingLocationResponse.Output("OperationOutcome", input.getUrl() + "_oo_errors.ndjson", counts[1]));
            }
            result.setOutput(outputs);
            result.setError(errors);
        }

        return result;
    }

    /**
     * submit import job with bulkdata.
     *
     * @param inputFormat
     * @param inputSource
     * @param inputs
     * @param storageDetail
     * @return
     * @throws Exception
     */
    public String submitImport(String inputFormat, String inputSource, List<Input> inputs, StorageDetail storageDetail) throws Exception {
        JobInstanceRequest.Builder builder = JobInstanceRequest.builder();
        builder.applicationName(adapter.getApplicationName());
        builder.moduleName(adapter.getModuleName());

        builder.incomingUrl(incomingUrl);
        builder.jobXMLName(JobType.IMPORT.value());

        // Add import specific: fhir.dataSourcesInfo
        // Base64 conversion is done in the builder method
        builder.fhirDataSourcesInfo(inputs);

        // Add import specific storage type
        // import.fhir.storagetype
        builder.fhirStorageType(storageDetail);

        // Fetch a string generated from random 32 bytes
        builder.cosBucketPathPrefix(getRandomPrefix());

        String fhirTenant = FHIRRequestContext.get().getTenantId();
        builder.fhirTenant(fhirTenant);

        builder.outcome(outcome);

        builder.source(source);

        String fhirDataStoreId = FHIRRequestContext.get().getDataStoreId();
        builder.fhirDataStoreId(fhirDataStoreId);

        String entityStr = JobInstanceRequest.Writer.generate(builder.build(), true);

        String baseUrl = adapter.getCoreApiBatchUrl() + "/jobinstances";
        if (log.isLoggable(Level.FINE)) {
            log.fine("The Base URL " + baseUrl);
            log.fine("The Entity posted to the server " + entityStr);
        }

        CloseableHttpClient cli = wrapper.getHttpClient(adapter.getCoreApiBatchUser(),adapter.getCoreApiBatchPassword());
        HttpPost jobPost = new HttpPost(baseUrl);
        StringEntity entity = new StringEntity(entityStr, ContentType.create("plain/text", Consts.UTF_8));
        jobPost.setEntity(entity);
        CloseableHttpResponse jobResponse = cli.execute(jobPost);

        int status = -1;
        String jobId = "-1";
        try {
            status = jobResponse.getStatusLine().getStatusCode();
            handleStandardResponseStatus(status);

            // Debug / Dev only
            if (log.isLoggable(Level.FINE)) {
                log.fine("$import response (HTTP " + status + ")");
            }

            if (status != 201) {
                // Job is not created
                throw BulkDataExportUtil.buildOperationException("Unable to create the $import job", IssueType.INVALID);
            }

            String responseString = new BasicResponseHandler().handleResponse(jobResponse);

            JobInstanceResponse response = JobInstanceResponse.Parser.parse(responseString);

            jobId = Integer.toString(response.getInstanceId());

        } finally {
            jobPost.releaseConnection();
            jobResponse.close();
        }
        cli.close();

        return baseUri + "/$bulkdata-status?job=" + transformer.endcodeJobId(jobId);
    }

    /*
     * Generate a random key using the passed algorithm or, if that algorithm isn't supported, a random 32 byte value.
     * In either case, the resulting value is encoded as URL Encoded string before returning.
     *
     * @implNote S3 API barfs on '/' thus URL Encoding... Also isolating this code as private and self-contained
     * in order to justify AppScan
     * @return a url-encoded random prefix
     */
    private static String getRandomPrefix() {
        byte[] bytes = null;
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256);
            bytes = keyGen.generateKey().getEncoded();
        } catch (NoSuchAlgorithmException e) {
            log.warning("Algorithm 'AES' is not supported; using SecureRandom instead");
            bytes = new byte[32];
            RANDOM.nextBytes(bytes);
        }
        return encoder.encodeToString(bytes);
    }
}