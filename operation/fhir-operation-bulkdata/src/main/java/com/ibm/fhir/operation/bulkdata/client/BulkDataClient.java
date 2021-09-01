/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64.Encoder;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.crypto.KeyGenerator;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
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
import com.ibm.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.operation.bulkdata.OperationConstants;
import com.ibm.fhir.operation.bulkdata.OperationConstants.ExportType;
import com.ibm.fhir.operation.bulkdata.client.action.batch.BatchCancelRequestAction;
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
import com.ibm.fhir.operation.bulkdata.model.type.StorageType;
import com.ibm.fhir.operation.bulkdata.model.url.DownloadUrl;
import com.ibm.fhir.operation.bulkdata.util.BulkDataExportUtil;
import com.ibm.fhir.search.compartment.CompartmentUtil;

/**
 * BulkData Client to connect to the other server.
 * <a href=
 * "https://www.ibm.com/support/knowledgecenter/en/SSEQTP_liberty/com.ibm.websphere.wlp.doc/ae/rwlp_batch_rest_api.html#rwlp_batch_rest_api__http_return_codes">
 */
public class BulkDataClient {

    private static final String CLASSNAME = BulkDataClient.class.getName();
    private static final Logger LOG = Logger.getLogger(CLASSNAME);

    private static final Encoder encoder = java.util.Base64.getUrlEncoder().withoutPadding();

    private static final SecureRandom RANDOM = new SecureRandom();

    private static final HttpWrapper wrapper = new HttpWrapper();

    private static final BulkDataExportUtil export = new BulkDataExportUtil();

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
     * @param bulkdataSource
     *            the source
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
    public String submitExport(Instant since, List<String> types, ExportType exportType, String outputFormat, String typeFilters, String groupId)
        throws Exception {
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
        types = types.stream().filter(t -> !t.isEmpty()).collect(Collectors.toList());
        String resourceType = String.join(",", types);
        switch (exportType) {
        case PATIENT:
            builder.jobXMLName(JobType.EXPORT_PATIENT.value());
            if (resourceType == null || resourceType.isEmpty() ) {
                resourceType = String.join(",", CompartmentUtil.getCompartmentResourceTypes("Patient"));
            }
            break;
        case GROUP:
            builder.jobXMLName(JobType.EXPORT_GROUP.value());
            builder.fhirPatientGroupId(groupId);
            if (resourceType == null || resourceType.isEmpty()) {
                resourceType = String.join(",", CompartmentUtil.getCompartmentResourceTypes("Patient"));
            }
            break;
        default:
            // We have two implementations for system export, but the "fast" version
            // does not support typeFilters. We also allow the configuration to
            // force use of the legacy implementation for those who don't like the change

            if (typeFilters != null
                    || !adapter.isFastExport()
                    || FHIRMediaType.APPLICATION_PARQUET.equals(outputFormat)
                    || StorageType.FILE.equals(adapter.getStorageProviderStorageType(source))) {
                // Use the legacy implementation
                builder.jobXMLName(JobType.EXPORT.value());
            } else {
                // No typeFilter, so we use the fast export which bypasses FHIR search
                builder.jobXMLName(JobType.EXPORT_FAST.value());
            }
            if (resourceType == null || resourceType.isEmpty()) {
                resourceType = ModelSupport.getResourceTypes()
                            .stream()
                            .map(r -> r.getSimpleName())
                            .collect(Collectors.joining(","));
            }
            break;
        }

        builder.fhirResourceType(resourceType);

        /*
         * There used to be an else path here where since is null, then set <code>builder.fhirSearchFromDate("1970-01-01T00:00:00Z");</code>.
         * It drove needless parameters in the query. By removing this path, we let the db use the optimal selectivity.
         */
        if (since != null) {
            builder.fhirSearchFromDate(since.getValue().format(Instant.PARSER_FORMATTER));
        }

        if (typeFilters != null) {
            builder.fhirTypeFilters(typeFilters);
        }

        String entityStr = JobInstanceRequest.Writer.generate(builder.build(), true);
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("Job instance request: " + entityStr);
        }

        String baseUrl = adapter.getCoreApiBatchUrl() + "/jobinstances";

        CloseableHttpClient cli = wrapper.getHttpClient(adapter.getCoreApiBatchUser(), adapter.getCoreApiBatchPassword());
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
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("$export response (HTTP " + status + ")");
            }

            if (status != 201) {
                // Job is not created
                throw export.buildOperationException("Unable to create the $export job", IssueType.INVALID);
            }

            String responseString = new BasicResponseHandler().handleResponse(jobResponse);

            JobInstanceResponse response = JobInstanceResponse.Parser.parse(responseString);

            jobId = Integer.toString(response.getInstanceId());

        } finally {
            jobPost.releaseConnection();
            jobResponse.close();
        }
        cli.close();

        return baseUri + "/$bulkdata-status?job=" + JobIdEncodingTransformer.getInstance().encodeJobId(jobId);
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

        CloseableHttpClient cli = wrapper.getHttpClient(adapter.getCoreApiBatchUser(), adapter.getCoreApiBatchPassword());
        HttpGet statusGet = new HttpGet(baseUrl);
        CloseableHttpResponse statusResponse = cli.execute(statusGet);

        JobInstanceResponse bulkExportJobInstanceResponse = null;
        try {
            handleStandardResponseStatus(statusResponse.getStatusLine().getStatusCode());
            HttpEntity entity = statusResponse.getEntity();

            try (InputStream is = entity.getContent()) {
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

                try (InputStream is = entity.getContent()) {
                    bulkExportJobExecutionResponse = JobExecutionResponse.Parser.parse(is);
                }
                EntityUtils.consume(entity);
            } finally {
                executionStatusGet.releaseConnection();
                executionStatusResponse.close();
            }

            verifyTenant(bulkExportJobExecutionResponse.getJobParameters());

            if (LOG.isLoggable(Level.FINE)) {
                LOG.warning("Logging the JobExecutionResponse Details -> \n"
                        + JobExecutionResponse.Writer.generate(bulkExportJobExecutionResponse, false));
            }

            String batchStatus = bulkExportJobExecutionResponse.getBatchStatus();
            if (batchStatus == null) {
                throw export.buildOperationException("Error while reading the bulk export status", IssueType.INVALID);
            } else if (OperationConstants.SUCCESS_STATUS.contains(batchStatus)) {
                result = process(bulkExportJobExecutionResponse);
            } else if (OperationConstants.FAILED_STATUS.contains(batchStatus)) {
                /*
                 * @implNote we should handle this in the Job not in the client.
                 * - In the case of partial success a useful response should be sent back.
                 * In the case of a partial success, the server SHALL use a 200 status code instead of 4XX or 5XX.
                 * The choice of when to determine that an export job has failed in its entirety (error status) vs
                 * returning a partial success (complete status) is left up to the implementer.
                 * XXX Can we do something better like return a 2XX response with a link to a file that explains the
                 * error?
                 * What if we couldn't connect with S3 / Cloud object store in the first place?
                 */
                if (OperationConstants.FAILED_BAD_SOURCE.equals(bulkExportJobExecutionResponse.getExitStatus())) {
                    throw export.buildOperationException("A bad source input was used during a call to $import", IssueType.INVALID);
                } else if (OperationConstants.NO_SUCH_BUCKET.equals(bulkExportJobExecutionResponse.getExitStatus())) {
                    throw export.buildOperationException("No such bucket exists for the storageProvider", IssueType.NO_STORE);
                } else {
                    throw export.buildOperationException("The job has failed", IssueType.EXCEPTION);
                }
            } else if (OperationConstants.STOPPED_STATUS.contains(batchStatus)) {
                // If the job is stopped, then restart the job.
                baseUrl = adapter.getCoreApiBatchUrl() + "/jobinstances/" + job + "?action=restart&reusePreviousParams=true";

                HttpPut restart = new HttpPut(baseUrl);
                CloseableHttpResponse restartResponse = cli.execute(restart);

                try {
                    HttpEntity entity = restartResponse.getEntity();

                    if (restartResponse.getStatusLine().getStatusCode() != Status.OK.getStatusCode()) {
                        throw export.buildOperationException("The job has failed to restart", IssueType.EXCEPTION);
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
            LOG.throwing(CLASSNAME, "status", ex);
            throw export.buildOperationException("An unexpected error has occurred while checking the status - "
                    + ex.getMessage(), IssueType.TRANSIENT);
        }

        return result;
    }

    public void handleStandardResponseStatus(int httpStatus) throws FHIROperationException {
        // e.g. if it comes back with 404 it may fail on the JobInstanceResponse.Parser.parse!

        if (httpStatus == 401) {
            throw export.buildOperationException("Unauthorized to access the Batch framework", IssueType.FORBIDDEN);
        }

        if (httpStatus == 400) {
            throw export.buildOperationException("Batch Job not found", IssueType.NOT_FOUND);
        }

        if (httpStatus == 404) {
            throw export.buildOperationException("Bad URL for Batch Framework", IssueType.FORBIDDEN);
        }

        if (httpStatus == 500) {
            // if (responseStr == null || responseStr.isEmpty() || responseStr.startsWith("Unexpected
            // request/response.")) {
            // throw export.buildOperationException("Invalid job id sent to $bulkdata-status",
            // IssueType.INVALID);
            // }
            throw export.buildOperationException("Server Side Error for Batch Framework", IssueType.EXCEPTION);
        }

        if (httpStatus == 200 && LOG.isLoggable(Level.FINE)) {
            LOG.fine("Successuflly accessed job");
        }
    }

    /**
     * deletes the given job.
     *
     * @param job
     * @return status code
     * @throws FHIROperationException
     */
    public Response.Status delete(String job) throws FHIROperationException {
        try (BatchCancelRequestAction action = new BatchCancelRequestAction()) {
            action.prepare(wrapper.getHttpClient(adapter.getCoreApiBatchUser(), adapter.getCoreApiBatchPassword()),
                        adapter.getCoreApiBatchUrl(),
                        adapter.getCoreApiBatchUser(),
                        adapter.getCoreApiBatchPassword());
            action.run(adapter.getTenant(), job);
            return action.getResult();
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
            LOG.warning("Tenant not authorized to access job [" + fhirTenant + "] jobParameter [" + jobParameters.getFhirTenant() + "]");
            throw export.buildOperationException("Tenant not authorized to access job", IssueType.FORBIDDEN);
        }
    }

    /**
     * @param response
     * @return
     */
    private PollingLocationResponse process(JobExecutionResponse response) {
        PollingLocationResponse result = new PollingLocationResponse();

        // Override the source:
        String source = response.getJobParameters().getSource();

        // Assemble the URL
        String cosBucketPathPrefix = response.getJobParameters().getCosBucketPathPrefix();

        String baseUrl = adapter.getStorageProviderEndpointExternal(source);

        String request = response.getJobParameters().getIncomingUrl();
        LOG.fine(response.getJobName());
        result.setRequest(request);

        // Per the storageProvider setting the output to indicate that the use of an access token is required.
        boolean requireAccessToken = adapter.getStorageProviderUsesRequestAccessToken(source);
        result.setRequiresAccessToken(requireAccessToken);

        // Outputs lastUpdatedTime as yyyy-MM-dd'T'HH:mm:ss
        String lastUpdatedTime = response.getLastUpdatedTime();
        TemporalAccessor acc = DATE_TIME_PARSER_FORMATTER.parse(lastUpdatedTime);
        result.setTransactionTime(Instant.PARSER_FORMATTER.format(acc));

        // Compose outputs for all exported ndjson files from the batch job exit status,
        // e.g, Patient[1000,1000,200]:Observation[1000,1000,200],
        // COMPLETED means no file exported.
        String exitStatus = response.getExitStatus();
        LOG.fine(() -> "The Exit Status is '" + exitStatus + "'");

        // Export Jobs with data in the exitStatus field
        if (!"COMPLETED".equals(exitStatus) && !"bulkimportchunkjob".equals(response.getJobName())) {
            List<String> resourceTypeInfs = Arrays.asList(exitStatus.split("\\s*:\\s*"));
            List<PollingLocationResponse.Output> outputList = new ArrayList<>();
            for (String resourceTypeInf : resourceTypeInfs) {
                String resourceType = resourceTypeInf.substring(0, resourceTypeInf.indexOf("["));
                String[] resourceCounts =
                        resourceTypeInf.substring(resourceTypeInf.indexOf("[") + 1, resourceTypeInf.indexOf("]")).split("\\s*,\\s*");
                for (int i = 0; i < resourceCounts.length; i++) {
                    boolean parquet = adapter.isStorageProviderParquetEnabled(source);
                    StorageType storageType = adapter.getStorageProviderStorageType(source);
                    String sUrl;

                    LOG.fine(() -> "Storage Type is " + storageType + " " + StorageType.IBMCOS.equals(storageType) + " " + StorageType.AWSS3.equals(storageType));
                    if (StorageType.IBMCOS.equals(storageType) || StorageType.AWSS3.equals(storageType)) {
                        String region = adapter.getStorageProviderLocation(source);
                        String bucketName = adapter.getStorageProviderBucketName(source);
                        String objectKey = resourceType + "_" + (i + 1);
                        String accessKey = adapter.getStorageProviderAuthTypeHmacAccessKey(source);
                        String secretKey = adapter.getStorageProviderAuthTypeHmacSecretKey(source);
                        boolean presigned = adapter.isStorageProviderHmacPresigned(source);
                        DownloadUrl url = new DownloadUrl(baseUrl, region, bucketName, cosBucketPathPrefix, objectKey, accessKey, secretKey, parquet, presigned, adapter.getS3HostStyleByStorageProvider(source));
                        sUrl = url.getUrl();
                    } else {
                        // Must be File
                        String ext;
                        if (parquet) {
                            ext = ".parquet";
                        } else {
                            ext = ".ndjson";
                        }
                        // Originally we set i to resourceCounts[i], however we don't always know the count when create the file.
                        sUrl = cosBucketPathPrefix + File.separator + resourceType + "_" + (i + 1) + ext;
                    }
                    outputList.add(new PollingLocationResponse.Output(resourceType, sUrl, resourceCounts[i]));
                }
            }
            result.setOutput(outputList);

            // Errors need to be added.
            List<PollingLocationResponse.Output> errors = Collections.emptyList();
            result.setError(errors);
        }
        // Export that has no data exported
        else if ("COMPLETED".equals(exitStatus) && !"bulkimportchunkjob".equals(response.getJobName())) {
            LOG.fine(() -> "Outputlist is empty");
            try {
                result.addOperationOutcomeToExtension(PollingLocationResponse.EMPTY_RESULTS_DURING_EXPORT);
            } catch (FHIRGeneratorException | IOException e) {
                LOG.severe("Unexpected issue while serializing a fixed value");
            }
            List<PollingLocationResponse.Output> outputs = Collections.emptyList();
            result.setOutput(outputs);

            List<PollingLocationResponse.Output> errors = Collections.emptyList();
            result.setError(errors);
        }
        // Import Jobs
        else if ("bulkimportchunkjob".equals(response.getJobName())) {
            // Currently there is no output
            LOG.fine("Hit the case where we don't form output with counts");
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
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("The Base URL " + baseUrl);
            LOG.fine("The Entity posted to the server " + entityStr);
        }

        CloseableHttpClient cli = wrapper.getHttpClient(adapter.getCoreApiBatchUser(), adapter.getCoreApiBatchPassword());
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
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("$import response (HTTP " + status + ")");
            }

            if (status != 201) {
                // Job is not created
                throw export.buildOperationException("Unable to create the $import job", IssueType.INVALID);
            }

            String responseString = new BasicResponseHandler().handleResponse(jobResponse);

            JobInstanceResponse response = JobInstanceResponse.Parser.parse(responseString);

            jobId = Integer.toString(response.getInstanceId());

        } finally {
            jobPost.releaseConnection();
            jobResponse.close();
        }
        cli.close();

        return baseUri + "/$bulkdata-status?job=" + JobIdEncodingTransformer.getInstance().encodeJobId(jobId);
    }

    /**
     * Generate a random key using the passed algorithm or, if that algorithm isn't supported, a random 32 byte value.
     * In either case, the resulting value is encoded as URL Encoded string before returning.
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
            LOG.warning("Algorithm 'AES' is not supported; using SecureRandom instead");
            bytes = new byte[32];
            RANDOM.nextBytes(bytes);
        }
        return encoder.encodeToString(bytes);
    }
}