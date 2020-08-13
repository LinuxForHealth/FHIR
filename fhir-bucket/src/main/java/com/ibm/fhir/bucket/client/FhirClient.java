/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.bucket.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.pool.PoolStats;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

/**
 * Handles pooled HTTP/S connections to a FHIR server.
 */
public class FhirClient {

    private static final Logger logger = Logger.getLogger(FhirClient.class.getName());
    private static final String USER_AGENT = "FHIR_BUCKET_LOADER";

    // Connection pool managing FHIR server HTTPS connections
    private PoolingHttpClientConnectionManager connManager;
    
    // HTTP client used to POST/PUT/GET FHIR server requests
    private CloseableHttpClient client;
    private String[] enabledCiphers;

    // connection properties encapsulated in an adapter for easy access
    private final ClientPropertyAdapter propertyAdapter;
    
    /**
     * Public constructor
     * @param cpa
     */
    public FhirClient(ClientPropertyAdapter cpa) {
        this.propertyAdapter = cpa;
    }
    
    /**
     * Initialize the SSL connection pool after all the required field values have been injected
     */
    public void init() {
        if (connManager != null) {
            throw new IllegalStateException("Already initialied");
        }

        
        String enabledCiphersValue = propertyAdapter.getEnabledCiphers();
        if (enabledCiphersValue != null && !enabledCiphersValue.isEmpty()) {
            enabledCiphers = enabledCiphersValue.split(",");
        }

        ConnectionKeepAliveStrategy connKeepAliveStrategy = new ConnectionKeepAliveStrategy() {

            @Override
            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                return 60000*60;
            }
        };
        
        try {
            // SSLContext sslContext = SSLContexts.custom().build();
            
            SSLContextBuilder sslContextBuilder = SSLContextBuilder.create();
            // sslContextBuilder.loadKeyMaterial(new File(keystoreFilename), keystorePass.toCharArray(), keyPass.toCharArray());
            sslContextBuilder.loadTrustMaterial(new File(propertyAdapter.getTruststore()), propertyAdapter.getTruststorePass().toCharArray());
            SSLContext sslContext = sslContextBuilder.build();
        
            SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslContext, new String[]{"TLSv1.2"}, enabledCiphers,
                    SSLConnectionSocketFactory.getDefaultHostnameVerifier());

            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https",factory).build();

            connManager = new PoolingHttpClientConnectionManager(registry);
            connManager.setMaxTotal(propertyAdapter.getPoolConnectionsMax());
            connManager.setDefaultMaxPerRoute(propertyAdapter.getPoolConnectionsMax());
            connManager.setValidateAfterInactivity(60000);
            connManager.setDefaultSocketConfig(SocketConfig.custom().build());
            
            client = obtainCloseableHttpClient(connKeepAliveStrategy);
        } 
        catch (KeyManagementException e) {
            throw new IllegalStateException("Failed to initialize connection manager", e);
        } 
        catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Failed to initialize connection manager", e);
        }        
        catch (IOException x) {
            throw new IllegalStateException("Failed to initialize connection manager", x);
        }
        catch (CertificateException x) {
            throw new IllegalStateException("Failed to initialize connection manager", x);
        }
        catch (KeyStoreException x) {
            throw new IllegalStateException("Failed to initialize connection manager", x);
        }

    }
    
    public FhirServerResponse get(String sUrl, Map<String, String> headers) {
        
        if(logger.isLoggable(Level.FINE)){
            logger.fine("REQUEST GET "+ sUrl);
        }

        HttpGet getRequest = new HttpGet(sUrl);
        
        if (headers != null) {
            for(String attribute:headers.keySet()) {
                getRequest.addHeader(attribute, headers.get(attribute));
                
                if(logger.isLoggable(Level.FINE)){
                    logger.fine(attribute+": "+headers.get(attribute));
                }
            }
        }
        
        for (int i = 1; ; i++) {
            try {
                long startTime = System.currentTimeMillis();
                HttpResponse response = client.execute(getRequest);
                if(logger.isLoggable(Level.FINE)){
                    Header responseHeaders[] = response.getAllHeaders();
                    logger.fine("Response HTTP Headers: ");
                    for (Header responseHeader : responseHeaders) {
                        logger.fine("\t" + responseHeader.getName() + ": " + responseHeader.getValue());
                    }
                }
                return mapToSimulatorResponse(response, startTime);
            } catch (NoHttpResponseException e) {
                logger.warning("Encountered an org.apache.http.NoHttpResponseException during GET request. " + e);
                logger.warning("GET URL: "+sUrl);
                logger.warning("Will retry this request for the Nth time. N = " + i);
            } catch (IOException e) {
                logger.severe("Error while executing the GET request. " + e);
                logger.warning("GET URL: "+sUrl);
                logger.warning("Skipping this request.");
                return null;
            }
        }        
    }
    
    public FhirServerResponse post(String sUrl, Map<String, String> headers, String body) {
        if(logger.isLoggable(Level.FINE)){
            logger.fine("REQUEST POST "+ sUrl);
        }
        
        try {
            
            HttpPost postRequest = new HttpPost(sUrl);
            postRequest.setEntity(new StringEntity(body));
            
            if(headers != null){
                for(String attribute:headers.keySet()){
                    postRequest.addHeader(attribute, headers.get(attribute));
                    
                    if(logger.isLoggable(Level.FINE)){
                        logger.fine(attribute+": "+headers.get(attribute));
                    }
                }
            }
            
            if(logger.isLoggable(Level.FINE)){
                logger.fine("REQUEST POST BODY - "+body);
            }
            
            
            long startTime = System.currentTimeMillis();
            HttpResponse response = client.execute(postRequest);
            if(logger.isLoggable(Level.FINE)){
                Header responseHeaders[] = response.getAllHeaders();
                logger.fine("Response HTTP Headers: ");
                for (Header responseHeader : responseHeaders) {
                    logger.fine("\t" + responseHeader.getName() + ": " + responseHeader.getValue());
                }
            }
            return mapToSimulatorResponse(response, startTime);
            
        } catch (UnsupportedEncodingException e) {
            logger.severe("Can't encode json string into entity. "+e);
            logger.warning("POST URL: "+sUrl+"\nRequest Body: "+body);
        } catch (ClientProtocolException e) {
            logger.severe("Error while executing the POST request. "+e);
            logger.warning("POST URL: "+sUrl+"\nRequest Body: "+body);
        } catch (IOException e) {
            logger.severe("Error while executing the POST request. "+e);
            logger.warning("POST URL: "+sUrl+"\nRequest Body: "+body);
        }
        
        return null;
    }
    
    public FhirServerResponse put(String sUrl, Map<String, String> headers, String body) {
            
        if(logger.isLoggable(Level.FINE)){
            logger.fine("REQUEST PUT "+sUrl);
        }
        try {
            
            HttpPut putRequest = new HttpPut(sUrl);
            putRequest.setEntity(new StringEntity(body));
            
            if(headers != null){
                for(String attribute:headers.keySet()){
                    putRequest.addHeader(attribute, headers.get(attribute));
                    
                    if(logger.isLoggable(Level.FINE)){
                        logger.fine(attribute+": "+headers.get(attribute));
                    }
                }
            }
            
            if(logger.isLoggable(Level.FINE)){
                logger.fine("REQUEST PUT BODY - "+body);
            }
            
            long startTime = System.currentTimeMillis();
            HttpResponse response = client.execute(putRequest);
            if(logger.isLoggable(Level.FINE)){
                Header responseHeaders[] = response.getAllHeaders();
                logger.fine("Response HTTP Headers: ");
                for (Header responseHeader : responseHeaders) {
                    logger.fine("\t" + responseHeader.getName() + ": " + responseHeader.getValue());
                }
            }
            return mapToSimulatorResponse(response, startTime);
            
        } catch (UnsupportedEncodingException e) {
            logger.severe("Can't encode json string into entity. "+e);
            logger.warning("PUT URL: "+sUrl+"\nRequest Body: "+body);
        } catch (ClientProtocolException e) {
            logger.severe("Error while executing the PUT request. "+e);
            logger.warning("PUT URL: "+sUrl+"\nRequest Body: "+body);
        } catch (IOException e) {
            logger.severe("Error while executing the PUT request. "+e);
            logger.warning("PUT URL: "+sUrl+"\nRequest Body: "+body);
        }
        
        return null;
    }
    
    public void shutdown() {
        if (client != null) {
            try {
                connManager.shutdown();
                client.close();
            } catch (IOException e) {
                throw new IllegalStateException("Unable to shutdown HTTP clients and Connection Manager successfully. ", e);
            }
        }
        
    }

    public PoolStats getPoolInformation() {
        if(connManager != null){
            return connManager.getTotalStats();
        } 
        
        return null;
    }
    
    private FhirServerResponse mapToSimulatorResponse(HttpResponse response, long startTime){
        try {
            
            FhirServerResponse sr = new FhirServerResponse();
            sr.setStatusCode(response.getStatusLine().getStatusCode());
            sr.setStatusMessage(response.getStatusLine().getReasonPhrase());
            
            HttpEntity entity = response.getEntity();
            if(entity != null){
                InputStream instream = entity.getContent();
                StringBuilder builder = new StringBuilder();
                try (Reader reader = new BufferedReader(new InputStreamReader(instream))){
                    
                    int c = 0;
                    while((c = reader.read()) != -1){
                        builder.append((char) c);
                    }
                    
                } finally {
                    instream.close();
                    EntityUtils.consume(entity);
                }
                
                // TODO parse the response
            }
            
            long endTime = System.currentTimeMillis();
            
            
            if (response.getFirstHeader("Location") != null) {
                sr.setLocationHeader(response.getFirstHeader("Location").getValue());
            }
            
            // Last-Modified: 2018-11-26T05:07:00.954Z
            // TODO
//            Header lastModifiedHeader = response.getFirstHeader("Last-Modified");
//            if (lastModifiedHeader != null) {
//                sr.setLastModified(TimeUtil.getFhirTime(lastModifiedHeader.getValue()));
//            }
//            else {
//                // TODO. Must have a lastModified, but we don't know what it is
//                sr.setLastModified(new Date(0));
//            }
            
            sr.setResponseTime(endTime-startTime);
                    
            return sr;
        } catch (ParseException | IOException e) {
            logger.severe("Error while executing request. "+e);
        } finally {
            try {
                EntityUtils.consume(response.getEntity());
            } catch (IOException e) {
                logger.severe("Error while consuming response. "+e);
            }
        }
        
        return null;
    }

    
    /**
     * Overridden by child classes for specialized behavior.
     * @param connKeepAliveStrategy
     */
    protected CloseableHttpClient obtainCloseableHttpClient(ConnectionKeepAliveStrategy connKeepAliveStrategy) {
        return HttpClients.custom().setConnectionReuseStrategy(DefaultConnectionReuseStrategy.INSTANCE)
                .setUserAgent(USER_AGENT).setKeepAliveStrategy(connKeepAliveStrategy)
                .setConnectionManager(connManager).setRetryHandler(new DefaultHttpRequestRetryHandler(1,true))
                .setUserTokenHandler(new UserTokenHandler() {
                    @Override
                    public Object getUserToken(HttpContext context) {
                        return null;
                    }        
                })
                .build();
    }        
}
