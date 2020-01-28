/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.bulkdata.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;

import com.ibm.fhir.client.impl.FHIRBasicAuthenticator;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.operation.bulkdata.BulkDataConstants;
import com.ibm.fhir.operation.bulkdata.BulkDataConstants.ExportType;
import com.ibm.fhir.operation.bulkdata.config.BulkDataConfigUtil;
import com.ibm.fhir.operation.bulkdata.model.BulkExportJobExecutionResponse;
import com.ibm.fhir.operation.bulkdata.model.BulkExportJobInstanceRequest;
import com.ibm.fhir.operation.bulkdata.model.BulkExportJobInstanceResponse;
import com.ibm.fhir.operation.bulkdata.model.JobParameter;
import com.ibm.fhir.operation.bulkdata.model.PollingLocationResponse;
import com.ibm.fhir.operation.bulkdata.util.BulkDataUtil;

/**
 * BulkData Client to connect to the other server.
 * <a href=
 * "https://www.ibm.com/support/knowledgecenter/en/SSEQTP_liberty/com.ibm.websphere.wlp.doc/ae/rwlp_batch_rest_api.html#rwlp_batch_rest_api__http_return_codes">
 */
public class BulkDataClient {
    private static final String CLASSNAME = BulkDataClient.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

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

    private Map<String, String> properties;

    public BulkDataClient(Map<String, String> properties) {
        this.properties = properties;
    }

    public WebTarget getWebTarget(String baseURL) throws Exception {
        ClientBuilder cb = ClientBuilder.newBuilder();

        String trustStore = properties.get(BulkDataConfigUtil.BATCH_TRUSTSTORE);
        String trustStorePass = properties.get(BulkDataConfigUtil.BATCH_TRUSTSTORE_PASS);
        cb.keyStore(loadKeyStoreFile(trustStore, trustStorePass), trustStorePass);
        cb.trustStore(loadKeyStoreFile(trustStore, trustStorePass));

        String user = properties.get(BulkDataConfigUtil.BATCH_USER);
        String pass = properties.get(BulkDataConfigUtil.BATCH_USER_PASS);
        cb = cb.register(new FHIRBasicAuthenticator(user, pass));

        return cb.build().target(baseURL);
    }

    private KeyStore loadKeyStoreFile(String ksFilename, String ksPassword) {
        InputStream is = null;
        try {
            KeyStore ks = KeyStore.getInstance("JKS");

            // First, search the classpath for the truststore file.
            URL tsURL = Thread.currentThread().getContextClassLoader().getResource(ksFilename);
            if (tsURL != null) {
                is = tsURL.openStream();
            }

            // If the classpath search failed, try to open the file directly.
            if (is == null) {
                File tsFile = new File(ksFilename);
                if (tsFile.exists()) {
                    is = new FileInputStream(tsFile);
                }
            }

            // If we couldn't open the file, throw an exception now.
            if (is == null) {
                throw new FileNotFoundException("KeyStore file '" + ksFilename
                        + "' was not found.");
            }

            // Load up the truststore file.
            ks.load(is, ksPassword.toCharArray());

            return ks;
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException
                | IOException e) {
            throw new IllegalStateException("Error loading keystore file '" + ksFilename + "' : "
                    + e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Throwable t) {
                    // absorb any exceptions while closing the stream.
                }
            }
        }
    }

    /**
     * @param since
     * @param types
     * @param properties
     * @param exportType
     * @return
     * @throws Exception
     */
    public String submit(Instant since, List<String> types, Map<String, String> properties, ExportType exportType)
            throws Exception {
        WebTarget target = getWebTarget(properties.get(BulkDataConfigUtil.BATCH_URL));

        BulkExportJobInstanceRequest.Builder builder = BulkExportJobInstanceRequest.builder();
        builder.applicationName(properties.get(BulkDataConfigUtil.APPLICATION_NAME));
        builder.moduleName(properties.get(BulkDataConfigUtil.MODULE_NAME));
        builder.cosBucketName(properties.get(BulkDataConfigUtil.JOB_PARAMETERS_BUCKET));
        builder.cosLocation(properties.get(BulkDataConfigUtil.JOB_PARAMETERS_LOCATION));
        builder.cosEndpointUrl(properties.get(BulkDataConfigUtil.JOB_PARAMETERS_ENDPOINT));
        builder.cosCredentialIbm(properties.get(BulkDataConfigUtil.JOB_PARAMETERS_IBM));
        builder.cosApiKey(properties.get(BulkDataConfigUtil.JOB_PARAMETERS_KEY));
        builder.cosSrvInstId(properties.get(BulkDataConfigUtil.JOB_PARAMETERS_ENDPOINT));

        // Fetch a string generated from random 32 bytes
        builder.cosBucketPathPrefix(BulkDataUtil.getRandomKey("AES"));

        // Export Type - FHIR
        switch (exportType) {
        case PATIENT:
            builder.jobXMLName("FhirBulkExportPatientChunkJob");
            break;
        case GROUP:
            builder.jobXMLName("FhirBulkExportGroupChunkJob");
            builder.fhirPatientGroupId(properties.get(BulkDataConstants.PARAM_GROUP_ID));
            break;
        default:
            builder.jobXMLName("FhirBulkExportChunkJob");
            break;
        }

        String fhirTenant = FHIRRequestContext.get().getTenantId();
        builder.fhirTenant(fhirTenant);

        String fhirDataStoreId = FHIRRequestContext.get().getDataStoreId();
        builder.fhirDataStoreId(fhirDataStoreId);

        String resourceType = String.join(", ", types);
        builder.fhirResourceType(resourceType);

        if (since != null) {
            builder.fhirSearchFromDate(since.getValue().format(Instant.PARSER_FORMATTER));
        } else {
            builder.fhirSearchFromDate("1970-01-01");
        }

        String entityStr = BulkExportJobInstanceRequest.Writer.generate(builder.build(), true);
        Entity<String> entity = Entity.json(entityStr);
        Response r = target.request().post(entity);

        String responseStr = r.readEntity(String.class);

        // Debug / Dev only
        if (log.isLoggable(Level.FINE)) {
            log.warning("JSON -> \n" + responseStr);
        }

        BulkExportJobInstanceResponse response =
                BulkExportJobInstanceResponse.Parser.parse(responseStr);

        // From the response
        String jobId = Integer.toString(response.getInstanceId());

        String hostname = properties.get(BulkDataConfigUtil.SERVER_HOSTNAME);
        String contextRoot = properties.get(BulkDataConfigUtil.CONTEXT_ROOT);

        return "https://" + hostname + contextRoot + "/$export-status?job=" + BulkDataUtil.encryptStr(jobId);
    }

    /**
     * @param job
     * @return
     * @throws Exception
     */
    public PollingLocationResponse status(String job) throws Exception {
        // Example: https://localhost:9443/ibm/api/batch/jobexecutions/9
        String baseUrl =
                properties.get(BulkDataConfigUtil.BATCH_URL).replace("jobinstances", "jobexecutions")
                        + "/" + job;

        WebTarget target = getWebTarget(baseUrl);
        Response r = target.request().get();

        String responseStr = r.readEntity(String.class);

        if (responseStr == null || responseStr.isEmpty()
                || responseStr.startsWith("Unexpected request/response.")) {
            throw BulkDataUtil.buildOperationException("Invalid job id sent to the server to $export-status");
        }

        PollingLocationResponse result = null;
        // Intermediate Response is - BulkExportJobExecutionResponse
        try {
            BulkExportJobExecutionResponse response =
                    BulkExportJobExecutionResponse.Parser.parse(responseStr);
            verifyTenant(response.getJobParameters());

            if (log.isLoggable(Level.FINE)) {
                log.warning("Logging the BulkExportJobExecutionResponse Details -> \n "
                        + BulkExportJobExecutionResponse.Writer.generate(response, false));
            }

            String batchStatus = response.getBatchStatus();
            if (batchStatus == null) {
                throw BulkDataUtil.buildOperationException("batch status check is null");
            } else if (BulkDataConstants.SUCCESS_STATUS.contains(batchStatus)) {
                result = process(response);
            } else if (BulkDataConstants.FAILED_STATUS.contains(batchStatus)) {
                throw BulkDataUtil.buildOperationException("batch status check is failed");
            }
        } catch (FHIROperationException fe) {
            throw fe;
        } catch (Exception ex) {
            throw BulkDataUtil.buildOperationException("dredded general exception on status check");
        }

        return result;
    }

    /**
     * @param job
     * @return
     * @throws Exception
     */
    public void delete(String job) throws Exception {
        // Example: https://localhost:9443/ibm/api/batch/jobexecutions/9
        String baseUrl =
                properties.get(BulkDataConfigUtil.BATCH_URL).replace("jobinstances", "jobexecutions")
                        + "/" + job;

        WebTarget target = getWebTarget(baseUrl);
        Response r = target.request().get();

        String responseStr = r.readEntity(String.class);

        if (responseStr == null || responseStr.isEmpty()
                || responseStr.startsWith("Unexpected request/response.")) {
            throw BulkDataUtil.buildOperationException("Invalid job id sent to the server to $export-status");
        }

        try {
            BulkExportJobExecutionResponse response =
                    BulkExportJobExecutionResponse.Parser.parse(responseStr);
            verifyTenant(response.getJobParameters());

            // The tenant is known, and now we need to query to delete the Job.
            r = target.request().delete();
            if (r.getStatus() != HttpStatus.SC_NO_CONTENT) {
                throw BulkDataUtil.buildOperationException("the content is not abandonded.");
            }
        } catch (FHIROperationException fe) {
            throw fe;
        } catch (Exception ex) {
            throw BulkDataUtil.buildOperationException("dredded general exception on status check");
        }
    }

    /**
     * verifies the tenant based on the job parameters.
     *
     * @param jobParameters
     * @throws FHIROperationException
     */
    public void verifyTenant(JobParameter jobParameters) throws FHIROperationException {
        String fhirTenant = FHIRRequestContext.get().getTenantId();
        if (jobParameters == null || jobParameters.getFhirTenant() == null
                || !jobParameters.getFhirTenant().equals(fhirTenant)) {
            log.warning(
                    "Tenant not authorized to access job [" + fhirTenant + "] jobParameter [" + jobParameters + "]");
            throw BulkDataUtil.buildOperationException("Tenant not authorized to access job");
        }
    }

    /**
     * @param response
     * @return
     */
    private PollingLocationResponse process(BulkExportJobExecutionResponse response) {
        PollingLocationResponse result = new PollingLocationResponse();

        // Assemble the URL
        String resourceTypes = response.getJobParameters().getFhirResourceType();
        String cosBucketPathPrefix = response.getJobParameters().getCosBucketPathPrefix();

        String baseCosUrl = properties.get(BulkDataConfigUtil.JOB_PARAMETERS_ENDPOINT);
        String bucket = properties.get(BulkDataConfigUtil.JOB_PARAMETERS_BUCKET);

        // Request
        String request = "/$export?_type=" + resourceTypes;
        result.setRequest(request);
        result.setRequiresAccessToken(false);

        // Outputs lastUpdatedTime as yyyy-MM-dd'T'HH:mm:ss
        String lastUpdatedTime = response.getLastUpdatedTime();
        TemporalAccessor acc = DATE_TIME_PARSER_FORMATTER.parse(lastUpdatedTime);
        result.setTransactionTime(Instant.PARSER_FORMATTER.format(acc));

        // Compose outputs for all exported ndjson files from the batch job exit status,
        // e.g, Patient[1000,1000,200]:Observation[1000,1000,200],
        //      COMPLETED means no file exported.
        String exitStatus = response.getExitStatus();
        if (!"COMPLETED".contentEquals(exitStatus)) {
            List<String> resourceTypeInfs = Arrays.asList(exitStatus.split("\\s*:\\s*"));
            List<PollingLocationResponse.Output> outputList = new ArrayList<>();
            for (String resourceTypeInf : resourceTypeInfs) {
                String resourceType = resourceTypeInf.substring(0, resourceTypeInf.indexOf("["));
                String[] resourceCounts =
                        resourceTypeInf.substring(resourceTypeInf.indexOf("[") + 1, resourceTypeInf.indexOf("]"))
                                .split("\\s*,\\s*");
                for (int i = 0; i < resourceCounts.length; i++) {
                    String downloadUrl =
                            baseCosUrl + "/" + bucket + "/" + cosBucketPathPrefix + "/" + resourceType + "_" + (i + 1)
                                    + ".ndjson";
                    outputList.add(new PollingLocationResponse.Output(resourceType, downloadUrl, resourceCounts[i]));
                }
            }
            result.setOutput(outputList);
        }
        return result;
    }
}