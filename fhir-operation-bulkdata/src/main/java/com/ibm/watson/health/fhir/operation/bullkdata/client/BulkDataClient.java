/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.watson.health.fhir.operation.bullkdata.client;

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
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ibm.watson.health.fhir.client.impl.FHIRBasicAuthenticator;
import com.ibm.watson.health.fhir.model.type.Instant;
import com.ibm.watson.health.fhir.operation.bullkdata.config.BulkDataConfigUtil;
import com.ibm.watson.health.fhir.operation.bullkdata.model.BulkExportJobInstanceRequest;
import com.ibm.watson.health.fhir.operation.bullkdata.model.BulkExportJobInstanceResponse;

/**
 * BulkData Client to connect to the other server.
 * 
 * @link https://www.ibm.com/support/knowledgecenter/en/SSEQTP_liberty/com.ibm.websphere.wlp.doc/ae/rwlp_batch_rest_api.html#rwlp_batch_rest_api__http_return_codes
 * 
 * @author pbastide
 *
 */
public class BulkDataClient {
    
    private static final String CLASSNAME = BulkDataClient.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

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
        
        cb.keyStore(loadKeyStoreFile(trustStore,trustStorePass), trustStorePass);
        cb.trustStore(loadKeyStoreFile(trustStore,trustStorePass));
        
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
                throw new FileNotFoundException("KeyStore file '" + ksFilename + "' was not found.");
            }

            // Load up the truststore file.
            ks.load(is, ksPassword.toCharArray());

            return ks;
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            throw new IllegalStateException("Error loading keystore file '" + ksFilename + "' : " + e);
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
     * @param outputFormat
     * @param since
     * @param types
     * @param properties
     * @return
     * @throws Exception
     */
    public String submit(MediaType outputFormat, Instant since, List<String> types,
        Map<String, String> properties) throws Exception {
        String baseUrl = properties.get(BulkDataConfigUtil.JOB_PARAMETERS_ENDPOINT);
        String bucket = properties.get(BulkDataConfigUtil.JOB_PARAMETERS_BUCKET);

        // Need to push this into a property.
        WebTarget target = getWebTarget(properties.get(BulkDataConfigUtil.BATCH_URL));

        BulkExportJobInstanceRequest.Builder builder = BulkExportJobInstanceRequest.builder();
        builder.applicationName("fhir-bulkimportexport-webapp");
        builder.moduleName("fhir-bulkimportexport-webapp-4.0.0-SNAPSHOT.war");
        builder.jobXMLName("FhirBulkExportChunkJob");
        
        builder.cosBucketName(properties.get(BulkDataConfigUtil.JOB_PARAMETERS_BUCKET));
        builder.cosLocation(properties.get(BulkDataConfigUtil.JOB_PARAMETERS_LOCATION));
        builder.cosEndpointUrl(properties.get(BulkDataConfigUtil.JOB_PARAMETERS_ENDPOINT));
        builder.cosCredentialIbm(properties.get(BulkDataConfigUtil.JOB_PARAMETERS_IBM));
        builder.cosApiKey(properties.get(BulkDataConfigUtil.JOB_PARAMETERS_KEY));
        builder.cosSrvInstId(properties.get(BulkDataConfigUtil.JOB_PARAMETERS_ENDPOINT));
        builder.fhirResourceType(types.get(0));
        
        if( since != null) {
            builder.fhirSearchFromDate(since.getValue().format(Instant.PARSER_FORMATTER));
        } else { 
            builder.fhirSearchFromDate("1970-01-01");
        }
        
        
        Entity<String> entity =
                Entity.json(BulkExportJobInstanceRequest.Writer.generate(builder.build(),true));
        Response r = target.request().post(entity);
        
        String responseStr = r.readEntity(String.class);

        // Debug / Dev only 
        log.warning("JSON -> \n" + responseStr);

        BulkExportJobInstanceResponse response = BulkExportJobInstanceResponse.Parser.parse(responseStr);

        // From the response
        String jobId = Integer.toString(response.getInstanceId());
        String resourceType = types.get(0);

        return baseUrl + "/" + bucket + "/" + jobId + "_" + resourceType + ".ndjson";
    }
}
