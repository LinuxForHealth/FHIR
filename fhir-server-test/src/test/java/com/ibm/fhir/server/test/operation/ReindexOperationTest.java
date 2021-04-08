/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.operation;

import static com.ibm.fhir.model.type.Integer.of;
import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.server.test.FHIRServerTestBase;
import com.ibm.fhir.server.test.bulkdata.ExportOperationTest;

/**
 * This class tests the $reindex operation and the custom authorization for admin only.
 */
public class ReindexOperationTest extends FHIRServerTestBase {
    private static final String CLASSNAME = ExportOperationTest.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

    private static final int TIMEOUT = 10000;

    private static final HttpRequestRetryHandler rh = new HttpRequestRetryHandler(){
        @Override
        public boolean retryRequest(IOException exception, int executionCount,HttpContext context){
            return executionCount<2;
        }
    };

    private static final RequestConfig config = RequestConfig.custom()
            .setConnectTimeout(TIMEOUT)
            .setConnectionRequestTimeout(TIMEOUT)
            .setSocketTimeout(TIMEOUT)
            .build();


    /**
     * generates a static SSL Connection socket factory.
     * @return
     */
    public static SSLConnectionSocketFactory generateSSF() {
        try {
            org.apache.http.ssl.SSLContextBuilder sslContextBuilder = org.apache.http.ssl.SSLContextBuilder.create();

            HostnameVerifier verifier = new org.apache.http.conn.ssl.NoopHostnameVerifier();
            TrustStrategy strategy = new org.apache.http.conn.ssl.TrustAllStrategy();
            sslContextBuilder.loadTrustMaterial(strategy);

            SSLContext sslContext = sslContextBuilder.build();

            return new SSLConnectionSocketFactory(sslContext, verifier);
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            log.warning("Default Algorithm for Http Client not found " + e.getMessage());
        }
        return SSLConnectionSocketFactory.getSocketFactory();
    }

    @Test
    public void testForbidden() {
        WebTarget target = getWebTarget();
        target = target.path("/$reindex");
        Response r = target.request(FHIRMediaType.APPLICATION_FHIR_JSON)
                     .header("X-FHIR-TENANT-ID", "tenant1")
                     .header("X-FHIR-DSID", "default")
                     .get(Response.class);
        assertEquals(r.getStatus(), Status.FORBIDDEN.getStatusCode());
    }

    @Test
    public void testAllowed() throws Exception {
        Properties testProperties = TestUtil.readTestProperties("test.properties");
        String url = testProperties.getProperty("fhirclient.rest.base.url") + "/$reindex";
        String username = testProperties.getProperty("fhirclient.basicauth.admin.username");
        String password = testProperties.getProperty("fhirclient.basicauth.admin.password");

        HttpHost target = new HttpHost("localhost", 9443, "https");
        CredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials( AuthScope.ANY, new UsernamePasswordCredentials(username, password));

        AuthCache authCache = new BasicAuthCache();
        authCache.put(target, new BasicScheme());

        HttpClientContext localContext = HttpClientContext.create();
        localContext.setAuthCache(authCache);

        try (CloseableHttpClient cli = HttpClients.custom()
                .setSSLSocketFactory(generateSSF())
                .setRetryHandler(rh)
                .setDefaultRequestConfig(config)
                .setDefaultCredentialsProvider(provider)
                .build()){
            HttpPost post = new HttpPost(url);
            post.setHeader("Content-Type", "application/fhir+json");
            post.setHeader("X-FHIR-TENANT-ID", "tenant1");

            List<Parameter> parameters = new ArrayList<>();
            parameters.add(
                Parameter.builder()
                    .name(string("resourceCount"))
                    .value(of(5))
                    .build());

            Parameters.Builder builder = Parameters.builder();
            builder.id(UUID.randomUUID().toString());
            builder.parameter(parameters);
            Parameters ps = builder.build();

            try (StringWriter writer = new StringWriter();) {
                FHIRGenerator.generator(Format.JSON, true).generate(ps, writer);
                post.setEntity(new StringEntity(writer.toString()));

            }
            CloseableHttpResponse postResponse = cli.execute(target, post, localContext);

            try {
                HttpEntity entity = postResponse.getEntity();
                assertEquals(postResponse.getStatusLine().getStatusCode(),
                    Status.OK.getStatusCode());

                EntityUtils.consume(entity);
            } finally {
                post.releaseConnection();
                postResponse.close();
            }
        }
    }
}