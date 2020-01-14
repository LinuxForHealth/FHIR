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
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.KeyGenerator;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ibm.fhir.client.impl.FHIRBasicAuthenticator;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.operation.bulkdata.BulkDataConstants.ExportType;
import com.ibm.fhir.operation.bulkdata.config.BulkDataConfigUtil;
import com.ibm.fhir.operation.bulkdata.model.BulkExportJobExecutionResponse;
import com.ibm.fhir.operation.bulkdata.model.BulkExportJobInstanceRequest;
import com.ibm.fhir.operation.bulkdata.model.BulkExportJobInstanceResponse;
import com.ibm.fhir.operation.bulkdata.model.PollingLocationResponse;
import com.ibm.fhir.operation.bulkdata.util.BulkDataUtil;

/**
 * BulkData Client to connect to the other server.
 *
 * @link https://www.ibm.com/support/knowledgecenter/en/SSEQTP_liberty/com.ibm.websphere.wlp.doc/ae/rwlp_batch_rest_api.html#rwlp_batch_rest_api__http_return_codes
 *
 *
 */
public class BulkDataClient {

    private static final String CLASSNAME = BulkDataClient.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

    private static final List<String> SUCCESS_STATUS = Arrays.asList("COMPLETED");
    private static final List<String> FAILED_STATUS = Arrays.asList("FAILED", "ABANDONED");

    // Random generator for COS path prefix
    private static final SecureRandom random = new SecureRandom();

    private Map<String, String> properties;

    public BulkDataClient(Map<String, String> properties) {
        this.properties = properties;
    }

    public WebTarget getWebTarget(String baseURL) throws Exception {

        ClientBuilder cb =
                ClientBuilder.newBuilder();

        /*
         * uses the jvm's default keystore/truststore.
         */
        String trustStore = properties.get(BulkDataConfigUtil.BATCH_TRUSTSTORE);
        String trustStorePass = properties.get(BulkDataConfigUtil.BATCH_TRUSTSTORE_PASS);

        cb.keyStore(loadKeyStoreFile(trustStore, trustStorePass), trustStorePass);
        cb.trustStore(loadKeyStoreFile(trustStore, trustStorePass));

        String user = properties.get(BulkDataConfigUtil.BATCH_USER);
        String pass = properties.get(BulkDataConfigUtil.BATCH_USER_PASS);

        cb = cb.register(new FHIRBasicAuthenticator(user, pass));
        Client client = cb.build();

        return client.target(baseURL);
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
     * Generate a random AES key or 32 byte value encoded as a Base64 string.
     *
     * @return
     */
    private String getRandomKey() {
        KeyGenerator keyGen;
        try {
            keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256);
            return Base64.getEncoder().encodeToString(keyGen.generateKey().getEncoded());

        } catch (NoSuchAlgorithmException e) {
            byte[] buffer = new byte[32];
            random.setSeed(System.currentTimeMillis());
            random.nextBytes(buffer);
            return Base64.getEncoder().encodeToString(buffer);
        }
    }

    /**
     * @param outputFormat
     * @param since
     * @param types
     * @param properties
     * @return
     * @throws Exception
     */
    public String submit(MediaType outputFormat, Instant since, List<String> types,
        Map<String, String> properties, ExportType exportType) throws Exception {

        // Need to push this into a property.
        WebTarget target = getWebTarget(properties.get(BulkDataConfigUtil.BATCH_URL));

        log.info("-> " + properties.get(BulkDataConfigUtil.BATCH_URL));

        BulkExportJobInstanceRequest.Builder builder = BulkExportJobInstanceRequest.builder();
        builder.applicationName(properties.get(BulkDataConfigUtil.APPLICATION_NAME));
        builder.moduleName(properties.get(BulkDataConfigUtil.MODULE_NAME));
        switch (exportType) {
        case PATIENT:
            builder.jobXMLName("FhirBulkExportPatientChunkJob");
            break;
        default:
            builder.jobXMLName("FhirBulkExportChunkJob");
            break;
        }

        builder.cosBucketName(properties.get(BulkDataConfigUtil.JOB_PARAMETERS_BUCKET));
        builder.cosLocation(properties.get(BulkDataConfigUtil.JOB_PARAMETERS_LOCATION));
        builder.cosEndpointUrl(properties.get(BulkDataConfigUtil.JOB_PARAMETERS_ENDPOINT));
        builder.cosCredentialIbm(properties.get(BulkDataConfigUtil.JOB_PARAMETERS_IBM));
        builder.cosApiKey(properties.get(BulkDataConfigUtil.JOB_PARAMETERS_KEY));
        builder.cosSrvInstId(properties.get(BulkDataConfigUtil.JOB_PARAMETERS_ENDPOINT));

        // Fetch a string generated from random 32 bytes
        builder.cosBucketPathPrefix(getRandomKey());

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

        Entity<String> entity =
                Entity.json(BulkExportJobInstanceRequest.Writer.generate(builder.build(), true));
        Response r = target.request().post(entity);

        String responseStr = r.readEntity(String.class);

        // Debug / Dev only
        if (log.isLoggable(Level.WARNING)) {
            log.warning("JSON -> \n" + responseStr);
        }

        BulkExportJobInstanceResponse response =
                BulkExportJobInstanceResponse.Parser.parse(responseStr);

        // From the response
        String jobId = Integer.toString(response.getInstanceId());

        String hostname = properties.get(BulkDataConfigUtil.SERVER_HOSTNAME);
        String contextRoot = properties.get(BulkDataConfigUtil.CONTEXT_ROOT);
        return "https://" + hostname + contextRoot + "/$export-status?job=" + jobId;
    }

    /**
     * @param job
     * @return
     * @throws Exception
     */
    public PollingLocationResponse status(String job) throws Exception {

        // Need to push this into a property, instead of assembling the whole thing here.
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
            // TODO: Here is where we NEED to check the tenantId = the tenant id on the job before responding.

            BulkExportJobExecutionResponse response =
                    BulkExportJobExecutionResponse.Parser.parse(responseStr);

            if (log.isLoggable(Level.WARNING)) {
                log.warning("Logging the BulkExportJobExecutionResponse Details -> \n "
                        + BulkExportJobExecutionResponse.Writer.generate(response, false));
            }

            String batchStatus = response.getBatchStatus();
            if (batchStatus == null) {
                throw BulkDataUtil.buildOperationException("batch status check is null");
            } else if (SUCCESS_STATUS.contains(batchStatus)) {
                result = process(response);
            } else if (FAILED_STATUS.contains(batchStatus)) {
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
     * @param response
     * @return
     */
    private PollingLocationResponse process(BulkExportJobExecutionResponse response) {

        PollingLocationResponse result = new PollingLocationResponse();

        // Assemble the URL
        String jobId = Integer.toString(response.getInstanceId());
        String resourceTypes = response.getJobParameters().getFhirResourceType();
        String cosBucketPathPrefix = response.getJobParameters().getCosBucketPathPrefix();

        String baseCosUrl = properties.get(BulkDataConfigUtil.JOB_PARAMETERS_ENDPOINT);
        String bucket = properties.get(BulkDataConfigUtil.JOB_PARAMETERS_BUCKET);


        // Request
        String request = "/$export?_type=" + resourceTypes;
        result.setRequest(request);
        result.setRequiresAccessToken(false);

        // TODO: Convert to yyyy-MM-dd'T'HH:mm:ss
        result.setTransactionTime(response.getLastUpdatedTime());
        // Compose outputs for all exported ndjson files from the batch job exit status,
        // e.g, Patient[1000,1000,200]:Observation[1000,1000,200],
        //      COMPLETED means no file exported.
        String exitStatus = response.getExitStatus();
        if (!exitStatus.contentEquals("COMPLETED")) {
            List<String> ResourceTypeInfs = Arrays.asList(exitStatus.split("\\s*:\\s*"));

            List< PollingLocationResponse.Output> outPutList = new ArrayList<>();
            for (String resourceTypeInf: ResourceTypeInfs) {
                String resourceType = resourceTypeInf.substring(0, resourceTypeInf.indexOf("["));
                String resourceCounts[] = resourceTypeInf.substring(resourceTypeInf.indexOf("[")+1, resourceTypeInf.indexOf("]"))
                        .split("\\s*,\\s*");
                for (int i = 0; i < resourceCounts.length; i++) {
                    String downloadUrl =
                            baseCosUrl + "/" + bucket + "/" + cosBucketPathPrefix + "/" + resourceType + "_" + (i+1) + ".ndjson";
                    outPutList.add(new PollingLocationResponse.Output(resourceType, downloadUrl, resourceCounts[i]));
                }
            }
            result.setOutput(outPutList);
        }

        return result;
    }
}
