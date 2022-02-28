/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.client;

import java.util.logging.Logger;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;

import com.ibm.cloud.objectstorage.ApacheHttpClientConfig;
import com.ibm.cloud.objectstorage.ClientConfiguration;
import com.ibm.cloud.objectstorage.SDKGlobalConfiguration;
import com.ibm.cloud.objectstorage.auth.AWSCredentials;
import com.ibm.cloud.objectstorage.auth.AWSStaticCredentialsProvider;
import com.ibm.cloud.objectstorage.auth.BasicAWSCredentials;
import com.ibm.cloud.objectstorage.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.ibm.cloud.objectstorage.oauth.BasicIBMOAuthCredentials;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3;
import com.ibm.cloud.objectstorage.services.s3.AmazonS3ClientBuilder;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;

/**
 * Generates an S3 Client.
 */
public class S3ClientGenerator {

    private static final Logger LOG = Logger.getLogger(S3ClientGenerator.class.getName());

    public S3ClientGenerator() {
        // NOP
    }

    /**
     * Gets a client based on the values related to a storage provider.
     *
     * @param iam identifies the access credentials are for IAM
     * @param accessKey (user or access Key or apiKey) for the credentials
     * @param accessSecret (resourceId, secretKey or password) for the credentials
     * @param cosEndpointUrl the endpoint url
     * @param cosLocation the location/region of the S3 bucket
     * @param useFhirServerTrustStore should use trust store
     * @param withPathStyle path or host
     * @return
     */
    public AmazonS3 getClient(boolean iam, String accessKey, String accessSecret, String cosEndpointUrl,
        String cosLocation, boolean useFhirServerTrustStore, boolean withPathStyle) {
        ConfigurationAdapter configAdapter = ConfigurationFactory.getInstance();

        AWSCredentials credentials;
        if (iam) {
            SDKGlobalConfiguration.IAM_ENDPOINT = configAdapter.getCoreIamEndpoint();
            credentials = new BasicIBMOAuthCredentials(accessKey, accessSecret);
        } else {
            credentials = new BasicAWSCredentials(accessKey, accessSecret);
        }

        ClientConfiguration clientConfig =
                new ClientConfiguration()
                    .withRequestTimeout(configAdapter.getCoreCosRequestTimeout())
                    .withTcpKeepAlive(configAdapter.getCoreCosTcpKeepAlive())
                    .withSocketTimeout(configAdapter.getCoreCosSocketTimeout());

        if (useFhirServerTrustStore) {
            ApacheHttpClientConfig apacheClientConfig = clientConfig.getApacheHttpClientConfig();
            // The following line configures COS/S3 SDK to use SSLConnectionSocketFactory of liberty server,
            // it makes sure the certs added in fhirTrustStore.p12 can be used for SSL connection with any S3
            // compatible object store, e.g, minio object store with self signed cert.
            if (configAdapter.shouldCoreApiBatchTrustAll()) {
                apacheClientConfig.setSslSocketFactory(HttpWrapper.generateSSF());
            } else {
                apacheClientConfig.setSslSocketFactory(SSLConnectionSocketFactory.getSystemSocketFactory());
            }
        }

        LOG.fine(() -> "The Path Style access is '" + withPathStyle + "'");

        // A useful link for the builder describing the pathStyle:
        // https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/s3/AmazonS3Builder.html#withPathStyleAccessEnabled-java.lang.Boolean-
        return AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new EndpointConfiguration(cosEndpointUrl, cosLocation))
                    .withClientConfiguration(clientConfig)
                    .withPathStyleAccessEnabled(withPathStyle)
                    .build();
    }
}
