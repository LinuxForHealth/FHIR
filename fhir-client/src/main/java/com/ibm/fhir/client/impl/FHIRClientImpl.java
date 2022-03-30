/*
 * (C) Copyright IBM Corp. 2016, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.client.impl;

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
import java.util.Properties;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.ws.rs.RuntimeType;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.ext.logging.event.LogEvent;
import org.apache.cxf.ext.logging.event.LogEventSender;
import org.apache.cxf.ext.logging.event.LogMessageFormatter;

import com.ibm.fhir.client.FHIRClient;
import com.ibm.fhir.client.FHIRParameters;
import com.ibm.fhir.client.FHIRRequestHeader;
import com.ibm.fhir.client.FHIRResponse;
import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.core.FHIRUtilities;
import com.ibm.fhir.core.HTTPReturnPreference;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.provider.FHIRJsonPatchProvider;
import com.ibm.fhir.provider.FHIRJsonProvider;
import com.ibm.fhir.provider.FHIRProvider;

import jakarta.json.JsonObject;

/**
 * Provides an implementation of the FHIRClient interface, which can be used as a high-level API for invoking FHIR REST
 * APIs.
 */
public class FHIRClientImpl implements FHIRClient {

    private static final String KEYSTORE_TYPE = "pkcs12";

    private Client client = null;
    private Properties clientProperties = null;
    private String baseEndpointURL = null;
    private String defaultMimeType = FHIRMediaType.APPLICATION_FHIR_JSON;

    private boolean basicAuthEnabled = false;
    private String basicAuthUsername = null;
    private String basicAuthPassword = null;

    private boolean clientAuthEnabled = false;
    private String trustStoreLocation = null;
    private String trustStorePassword = null;
    private String keyStoreLocation = null;
    private String keyStorePassword = null;
    private String keyStoreKeyPassword = null;

    private boolean oAuth2Enabled = false;
    private String accessToken = null;

    private KeyStore trustStore = null;
    private KeyStore keyStore = null;

    private boolean loggingEnabled = false;

    private boolean hostnameVerificationEnabled = true;

    private HTTPReturnPreference httpReturnPref = HTTPReturnPreference.MINIMAL;

    private int httpTimeout;

    // The tenantId to pass with the X-FHIR-TENANT-ID header
    private String tenantId;

    public FHIRClientImpl(Properties props) throws Exception {
        initProperties(props);

        ClientBuilder cb =
                ClientBuilder.newBuilder()
                    .register(new FHIRProvider(RuntimeType.CLIENT))
                    .register(new FHIRJsonProvider(RuntimeType.CLIENT))
                    .register(new FHIRJsonPatchProvider(RuntimeType.CLIENT));

        // Add support for basic auth if enabled.
        if (isBasicAuthEnabled()) {
            cb = cb.register(new FHIRBasicAuthenticator(getBasicAuthUsername(), getBasicAuthPassword()));
        }

        // Add support for OAuth 2.0 if enabled.
        if (isOAuth2Enabled()) {
            cb = cb.register(new FHIROAuth2Authenticator(getOAuth2AccessToken()));
        }

        // If using oAuth 2.0 or clientauth, then we need to attach our Keystore.
        if (isOAuth2Enabled() || isClientAuthEnabled()) {
            cb = cb.keyStore(getKeyStore(), getKeyStoreKeyPassword());
        }

        // If using oAuth 2.0 or clientauth or an https endpoint, then we need to attach our Truststore.
        KeyStore ks = getTrustStore();
        if (ks != null) {
            cb = cb.trustStore(ks);
        }

        // Add a hostname verifier if we're using an ssl transport.
        if (usingSSLTransport() && !isHostnameVerificationEnabled()) {
            cb = cb.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });
        }

        // Set the http client's receive timeout setting
        cb.property("http.receive.timeout", getHttpTimeout()); // defaults to 60s

        // true: If need, tell Apache CXF to use the Async HTTP conduit for PATCH operation as the
        // default HTTP conduit does not support PATCH
        // false(default): To avoid the http async client time out issue (http://mail-archives.apache.org
        // /mod_mbox/hc-dev/201909.mbox/%3CJIRA.13256372.1568301069000.62179.1568450580088@Atlassian.JIRA%3E),
        // please set this to false.
        cb.property("use.async.http.conduit", false);

        // Add request/response logging if enabled.
        if (isLoggingEnabled()) {
            cb.property("org.apache.cxf.logging.enable", "true");
            LoggingFeature feat = new LoggingFeature();
            feat.setPrettyLogging(true);
            feat.setVerbose(true);
            feat.setSender(new LF());
            cb.register(feat);
        }

        cb.property("thread.safe.client", true);

        // Save off our cached Client instance.
        client = cb.build();

    }

    /**
     * Remaps the LogEventSender
     */
    public static class LF implements LogEventSender {
        @Override
        public void send(LogEvent event) {
            String msg = LogMessageFormatter.format(event);
            String id = "ID: " + event.getExchangeId();
            System.err.println(id + " " + msg);
        }
    }

    @Override
    public FHIRResponse metadata(FHIRRequestHeader... headers) throws Exception {
        WebTarget endpoint = getWebTarget();
        Invocation.Builder builder = endpoint.path("metadata").request(getDefaultMimeType());
        builder = addRequestHeaders(builder, headers);
        Response response = builder.get();
        return new FHIRResponseImpl(response);
    }

    @Override
    public FHIRResponse create(Resource resource, FHIRRequestHeader... headers) throws Exception {
        if (resource == null) {
            throw new IllegalArgumentException("The 'resource' argument is required but was null.");
        }
        String resourceType = resource.getClass().getSimpleName();
        return _create(resource, resourceType, null, headers);
    }

    @Override
    public FHIRResponse create(JsonObject resource, FHIRRequestHeader... headers) throws Exception {
        if (resource == null) {
            throw new IllegalArgumentException("The 'resource' argument is required but was null.");
        }
        String resourceType = resource.getString("resourceType");
        if (resourceType == null || resourceType.isEmpty()) {
            throw new IllegalArgumentException("Unable to retrieve the resource type from the resource.");
        }
        return _create(resource, resourceType, null, headers);
    }

    @Override
    public FHIRResponse conditionalCreate(Resource resource, FHIRParameters parameters, FHIRRequestHeader... headers) throws Exception {
        if (resource == null) {
            throw new IllegalArgumentException("The 'resource' argument is required but was null.");
        }
        String resourceType = resource.getClass().getSimpleName();
        if (parameters == null || parameters.getParameterMap() == null || parameters.getParameterMap().isEmpty()) {
            throw new IllegalArgumentException("The 'parameters' argument must be non-null and must contain at least one search query parameter");
        }
        return _create(resource, resourceType, parameters, headers);
    }

    @Override
    public FHIRResponse conditionalCreate(JsonObject resource, FHIRParameters parameters, FHIRRequestHeader... headers) throws Exception {
        if (resource == null) {
            throw new IllegalArgumentException("The 'resource' argument is required but was null.");
        }
        String resourceType = resource.getString("resourceType");
        if (resourceType == null || resourceType.isEmpty()) {
            throw new IllegalArgumentException("Unable to retrieve the resource type from the resource.");
        }
        if (parameters == null || parameters.getParameterMap() == null || parameters.getParameterMap().isEmpty()) {
            throw new IllegalArgumentException("The 'parameters' argument must be non-null and must contain at least one search query parameter");
        }
        return _create(resource, resourceType, parameters, headers);
    }

    /**
     * Common "create" implementations used by the "create()" and "conditionalCreate()" methods.
     */
    private <T> FHIRResponse _create(T resource, String resourceType, FHIRParameters parameters, FHIRRequestHeader... headers) throws Exception {
        WebTarget endpoint = getWebTarget();
        Entity<T> entity = Entity.entity(resource, getDefaultMimeType());
        Invocation.Builder builder = endpoint.path(resourceType).request(getDefaultMimeType());
        headers = addIfNoneExistHeader(headers, parameters);
        headers = addHttpPreferHeader(headers, getHttpReturnPref());
        builder = addRequestHeaders(builder, headers);
        Response response = builder.post(entity);
        return new FHIRResponseImpl(response);
    }

    /**
     * This function will add a "If-None-Exist" request header to the specified array of headers.
     * @param headers a possibly null array of FHIRRequestHeader objects
     * @param ifNoneExistQuery a string representing the search query to be used for a conditional create operation
     * @return a new array of FHIRRequestHeader objects containing the additional "If-None-Exist" request header
     */
    private FHIRRequestHeader[] addIfNoneExistHeader(FHIRRequestHeader[] headers, FHIRParameters ifNoneExistQuery) {
        if (ifNoneExistQuery == null) {
            return headers;
        }

        // Create a "If-None-Exist" request header whose value is the stringified version of "ifNoneExistQuery".
        FHIRRequestHeader ifNoneExistHeader = new FHIRRequestHeader("If-None-Exist", ifNoneExistQuery.queryString(false));

        // Create a new array that has room for the new "If-None-Exist" header.
        int headersSize = (headers != null ? headers.length : 0);
        FHIRRequestHeader[] result = new FHIRRequestHeader[headersSize + 1];
        if (headers != null) {
            for (int i = 0; i < headers.length; i++) {
                result[i] = headers[i];
            }
        }

        // Add the new header at the end.
        result[result.length - 1] = ifNoneExistHeader;

        return result;
    }

    /**
     * This function will add a "Prefer" request header to the specified array of headers.
     * @param headers a possibly null array of FHIRRequestHeader objects
     * @param  returnPref a value representing the HTTP return preference to be used for the request
     * @return a new array of FHIRRequestHeader objects containing the additional "Prefer" request header
     */
    private FHIRRequestHeader[] addHttpPreferHeader(FHIRRequestHeader[] headers, HTTPReturnPreference returnPref) {
        if (headers != null ) {
            for (FHIRRequestHeader fhirRequestHeader : headers) {
                if ("Prefer".equals(fhirRequestHeader.getName())) {
                    // User has provided an explicit Prefer header, so don't overwrite that
                    return headers;
                }
            }
        }

        FHIRRequestHeader preferHeader = new FHIRRequestHeader("Prefer", "return=" + returnPref.value());

        // Create a new array that has room for the new "Prefer" header.
        int headersSize = (headers != null ? headers.length : 0);
        FHIRRequestHeader[] result = new FHIRRequestHeader[headersSize + 1];
        if (headers != null) {
            for (int i = 0; i < headers.length; i++) {
                result[i] = headers[i];
            }
        }

        // Add the new header at the end.
        result[result.length - 1] = preferHeader;

        return result;
    }

    /*
     * (non-Javadoc)
     * @see com.ibm.fhir.client.FHIRClient#update(java.lang.Object, java.lang.Class, java.lang.String)
     */
    @Override
    public FHIRResponse update(Resource resource, FHIRRequestHeader... headers) throws Exception {
        if (resource == null) {
            throw new IllegalArgumentException("The 'resource' argument is required but was null.");
        }
        String resourceType = resource.getClass().getSimpleName();
        String resourceId = (resource.getId() != null ? resource.getId() : null);
        if (resourceId == null || resourceId.isEmpty()) {
            throw new IllegalArgumentException("Unable to retrieve the resource id from the resource.");
        }
        return _update(resource, resourceType, resourceId, null, headers);
    }

    @Override
    public FHIRResponse update(JsonObject resource, FHIRRequestHeader... headers) throws Exception {
        if (resource == null) {
            throw new IllegalArgumentException("The 'resource' argument is required but was null.");
        }
        String resourceType = resource.getString("resourceType");
        if (resourceType == null || resourceType.isEmpty()) {
            throw new IllegalArgumentException("Unable to retrieve the resource type from the resource.");
        }
        String resourceId = resource.getString("id");
        if (resourceId == null || resourceId.isEmpty()) {
            throw new IllegalArgumentException("Unable to retrieve the resource id from the resource.");
        }
        return _update(resource, resourceType, resourceId, null, headers);
    }

    @Override
    public FHIRResponse conditionalUpdate(Resource resource, FHIRParameters parameters, FHIRRequestHeader... headers) throws Exception {
        if (resource == null) {
            throw new IllegalArgumentException("The 'resource' argument is required but was null.");
        }
        String resourceType = resource.getClass().getSimpleName();
        if (parameters == null || parameters.getParameterMap() == null || parameters.getParameterMap().isEmpty()) {
            throw new IllegalArgumentException("The 'parameters' argument must be non-null and must contain at least one search query parameter");
        }
        return _update(resource, resourceType, null, parameters, headers);
    }

    @Override
    public FHIRResponse conditionalUpdate(JsonObject resource, FHIRParameters parameters, FHIRRequestHeader... headers) throws Exception {
        if (resource == null) {
            throw new IllegalArgumentException("The 'resource' argument is required but was null.");
        }
        String resourceType = resource.getString("resourceType");
        if (resourceType == null || resourceType.isEmpty()) {
            throw new IllegalArgumentException("Unable to retrieve the resource type from the resource.");
        }
        if (parameters == null || parameters.getParameterMap() == null || parameters.getParameterMap().isEmpty()) {
            throw new IllegalArgumentException("The 'parameters' argument must be non-null and must contain at least one search query parameter");
        }
        return _update(resource, resourceType, null, parameters, headers);
    }

    /**
     * Common "update" implementations used by the "update()" and "conditionalUpdate()" methods.
     */
    private <T> FHIRResponse _update(T resource, String resourceType, String resourceId, FHIRParameters parameters, FHIRRequestHeader... headers) throws Exception {
        WebTarget endpoint = getWebTarget();
        Entity<T> entity = Entity.entity(resource, getDefaultMimeType());
        endpoint = endpoint.path(resourceType);

        if (resourceId != null) {
            // For a normal update operation, add the resource id to the URL pattern of the request.
            endpoint = endpoint.path(resourceId);
        } else {
            // Otherwise, for a conditional update, add the search query parameter(s) to the request.
            endpoint = addParametersToWebTarget(endpoint, parameters);
        }

        Invocation.Builder builder = endpoint.request(getDefaultMimeType());
        builder = addRequestHeaders(builder, headers);
        Response response = builder.put(entity);
        return new FHIRResponseImpl(response);
    }

    @Override
    public FHIRResponse delete(String resourceType, String resourceId, FHIRRequestHeader... headers) throws Exception {
        if (resourceType == null) {
            throw new IllegalArgumentException("The 'resourceType' argument is required but was null.");
        }
        if (resourceId == null) {
            throw new IllegalArgumentException("The 'resourceId' argument is required but was null.");
        }
        return _delete(resourceType, resourceId, null, headers);
    }

    @Override
    public FHIRResponse conditionalDelete(String resourceType, FHIRParameters parameters, FHIRRequestHeader... headers) throws Exception {
        if (resourceType == null) {
            throw new IllegalArgumentException("The 'resourceType' argument is required but was null.");
        }
        if (parameters == null || parameters.getParameterMap() == null || parameters.getParameterMap().isEmpty()) {
            throw new IllegalArgumentException("The 'parameters' argument must be non-null and must contain at least one search query parameter");
        }
        return _delete(resourceType, null, parameters, headers);
    }
    private FHIRResponse _delete(String resourceType, String resourceId, FHIRParameters parameters, FHIRRequestHeader... headers) throws Exception {
        WebTarget endpoint = getWebTarget();
        endpoint = endpoint.path(resourceType);

        if (resourceId != null) {
            // For a normal delete operation, add the resource id to the URL pattern of the request.
            endpoint = endpoint.path(resourceId);
        } else {
            // Otherwise, for a conditional delete, add the search query parameter(s) to the request.
            endpoint = addParametersToWebTarget(endpoint, parameters);
        }

        Invocation.Builder builder = endpoint.request(getDefaultMimeType());
        builder = addRequestHeaders(builder, headers);
        Response response = builder.delete();
        return new FHIRResponseImpl(response);
    }

    @Override
    public FHIRResponse read(String resourceType, String resourceId, FHIRRequestHeader... headers) throws Exception {
        if (resourceType == null) {
            throw new IllegalArgumentException("The 'resourceType' argument is required but was null.");
        }
        if (resourceId == null) {
            throw new IllegalArgumentException("The 'resourceId' argument is required but was null.");
        }
        WebTarget endpoint = getWebTarget();
        Invocation.Builder builder = endpoint.path(resourceType).path(resourceId).request(getDefaultMimeType());
        builder = addRequestHeaders(builder, headers);
        Response response = builder.get();
        return new FHIRResponseImpl(response);
    }

    @Override
    public FHIRResponse vread(String resourceType, String resourceId, String versionId, FHIRRequestHeader... headers) throws Exception {
        if (resourceType == null) {
            throw new IllegalArgumentException("The 'resourceType' argument is required but was null.");
        }
        if (resourceId == null) {
            throw new IllegalArgumentException("The 'resourceId' argument is required but was null.");
        }
        if (versionId == null) {
            throw new IllegalArgumentException("The 'versionId' argument is required but was null.");
        }
        WebTarget endpoint = getWebTarget();
        Invocation.Builder builder = endpoint.path(resourceType).path(resourceId).path("_history").path(versionId).request(getDefaultMimeType());
        builder = addRequestHeaders(builder, headers);
        Response response = builder.get();
        return new FHIRResponseImpl(response);
    }

    @Override
    public FHIRResponse history(String resourceType, String resourceId, FHIRParameters parameters, FHIRRequestHeader... headers) throws Exception {
        if (resourceType == null) {
            throw new IllegalArgumentException("The 'resourceType' argument is required but was null.");
        }
        if (resourceId == null) {
            throw new IllegalArgumentException("The 'resourceId' argument is required but was null.");
        }
        WebTarget endpoint = getWebTarget();
        endpoint = endpoint.path(resourceType).path(resourceId).path("_history");
        endpoint = addParametersToWebTarget(endpoint, parameters);
        Invocation.Builder builder = endpoint.request(getDefaultMimeType());
        builder = addRequestHeaders(builder, headers);
        Response response = builder.get();
        return new FHIRResponseImpl(response);
    }

    @Override
    public FHIRResponse history(FHIRParameters parameters, FHIRRequestHeader... headers) throws Exception {
        // System level history request [base]/_history?...
        WebTarget endpoint = getWebTarget();
        endpoint = endpoint.path("_history");
        endpoint = addParametersToWebTarget(endpoint, parameters);
        Invocation.Builder builder = endpoint.request(getDefaultMimeType());
        builder = addRequestHeaders(builder, headers);
        Response response = builder.get();
        return new FHIRResponseImpl(response);
    }

    @Override
    public FHIRResponse search(String resourceType, FHIRParameters parameters, FHIRRequestHeader... headers) throws Exception {
        if (resourceType == null) {
            throw new IllegalArgumentException("The 'resourceType' argument is required but was null.");
        }
        WebTarget endpoint = getWebTarget();
        endpoint = endpoint.path(resourceType);
        endpoint = addParametersToWebTarget(endpoint, parameters);
        Invocation.Builder builder = endpoint.request(getDefaultMimeType());
        builder = addRequestHeaders(builder, headers);
        Response response = builder.get();
        return new FHIRResponseImpl(response);
    }

    @Override
    public FHIRResponse _search(String resourceType, FHIRParameters parameters, FHIRRequestHeader... headers) throws Exception {
        if (resourceType == null) {
            throw new IllegalArgumentException("The 'resourceType' argument is required but was null.");
        }
        WebTarget endpoint = getWebTarget();
        endpoint = endpoint.path(resourceType).path("_search");
        Form form = buildForm(parameters);
        Entity<Form> entity = Entity.form(form);
        Invocation.Builder builder = endpoint.request(getDefaultMimeType());
        builder = addRequestHeaders(builder, headers);
        Response response = builder.post(entity);
        return new FHIRResponseImpl(response);
    }

    @Override
    public FHIRResponse searchAll(FHIRParameters parameters, boolean isPost, FHIRRequestHeader... headers) throws Exception {
        Invocation.Builder builder;
        Response response;
        WebTarget endpoint = getWebTarget();
        if (isPost) {
            endpoint = endpoint.path("/");
            endpoint = addParametersToWebTarget(endpoint, parameters);
            builder = endpoint.request(getDefaultMimeType());
            builder = addRequestHeaders(builder, headers);
            response = builder.get();
        } else {
            endpoint = endpoint.path("_search");
            builder = endpoint.request(getDefaultMimeType());
            builder = addRequestHeaders(builder, headers);
            Entity<Form> entity = Entity.form(parameters.getParameterMap());
            response = builder.post(entity);
        }

        return new FHIRResponseImpl(response);
    }


    @Override
    public FHIRResponse validate(Resource resource, FHIRRequestHeader... headers) throws Exception {
        if (resource == null) {
            throw new IllegalArgumentException("The 'resource' argument is required but was null.");
        }
        String resourceType = resource.getClass().getSimpleName();
        return _validate(resource, resourceType, headers);
    }

    @Override
    public FHIRResponse validate(JsonObject resource, FHIRRequestHeader... headers) throws Exception {
        if (resource == null) {
            throw new IllegalArgumentException("The 'resource' argument is required but was null.");
        }
        String resourceType = resource.getString("resourceType");
        return _validate(resource, resourceType, headers);
    }

    private <T> FHIRResponse _validate(T resource, String resourceType, FHIRRequestHeader...headers) throws Exception {
        WebTarget endpoint = getWebTarget();
        Entity<T> entity = Entity.entity(resource, getDefaultMimeType());
        Invocation.Builder builder = endpoint.path(resourceType).path("$validate").request(getDefaultMimeType());
        builder = addRequestHeaders(builder, headers);
        Response response = builder.post(entity);
        return new FHIRResponseImpl(response);

    }

    @Override
    public FHIRResponse batch(Bundle bundle, FHIRRequestHeader... headers) throws Exception {
        if (bundle == null) {
            throw new IllegalArgumentException("The 'bundle' argument is required but was null.");
        }
        return _bundle(bundle, BundleType.BATCH, headers);
    }

    @Override
    public FHIRResponse transaction(Bundle bundle, FHIRRequestHeader... headers) throws Exception {
        if (bundle == null) {
            throw new IllegalArgumentException("The 'bundle' argument is required but was null.");
        }
        return _bundle(bundle, BundleType.TRANSACTION, headers);
    }

    @Override
    public FHIRResponse invoke(String operationName, FHIRParameters parameters, FHIRRequestHeader... headers) throws Exception {
        if (operationName == null) {
            throw new IllegalArgumentException("The 'operationName' argument is required but was null.");
        }
        WebTarget endpoint = getWebTarget();
        endpoint = endpoint.path(operationName);
        endpoint = addParametersToWebTarget(endpoint, parameters);
        Invocation.Builder builder = endpoint.request(getDefaultMimeType());
        builder = addRequestHeaders(builder, headers);
        Response response = builder.get();
        return new FHIRResponseImpl(response);
    }

    @Override
    public FHIRResponse invoke(String operationName, Resource resource, FHIRRequestHeader... headers) throws Exception {
        if (operationName == null) {
            throw new IllegalArgumentException("The 'operationName' argument is required but was null.");
        }
        if (resource == null) {
            throw new IllegalArgumentException("The 'resource' argument is required but was null.");
        }
        WebTarget endpoint = getWebTarget();
        Entity<Parameters> entity = Entity.entity((Parameters) resource, getDefaultMimeType());
        Invocation.Builder builder = endpoint.path(operationName).request();
        builder = addRequestHeaders(builder, headers);
        Response response = builder.post(entity);
        return new FHIRResponseImpl(response);
    }

    @Override
    public FHIRResponse invoke(String resourceType, String operationName, FHIRParameters parameters, FHIRRequestHeader... headers) throws Exception {
        if (resourceType == null) {
            throw new IllegalArgumentException("The 'resourceType' argument is required but was null.");
        }
        if (operationName == null) {
            throw new IllegalArgumentException("The 'operationName' argument is required but was null.");
        }
        WebTarget endpoint = getWebTarget();
        endpoint = endpoint.path(resourceType).path(operationName);
        endpoint = addParametersToWebTarget(endpoint, parameters);
        Invocation.Builder builder = endpoint.request(getDefaultMimeType());
        builder = addRequestHeaders(builder, headers);
        Response response = builder.get();
        return new FHIRResponseImpl(response);
    }

    @Override
    public FHIRResponse invoke(String resourceType, String operationName, Resource resource, FHIRRequestHeader... headers) throws Exception {
        if (resourceType == null) {
            throw new IllegalArgumentException("The 'resourceType' argument is required but was null.");
        }
        if (operationName == null) {
            throw new IllegalArgumentException("The 'operationName' argument is required but was null.");
        }
        WebTarget endpoint = getWebTarget();
        Entity<Parameters> entity = Entity.entity((Parameters)resource, getDefaultMimeType());
        Invocation.Builder builder = endpoint.path(resourceType).path(operationName).request();
        builder = addRequestHeaders(builder, headers);
        Response response = builder.post(entity);
        return new FHIRResponseImpl(response);
    }

    @Override
    public FHIRResponse invoke(String resourceType, String operationName, String resourceId, FHIRParameters parameters, FHIRRequestHeader... headers) throws Exception {
        if (resourceType == null) {
            throw new IllegalArgumentException("The 'resourceType' argument is required but was null.");
        }
        if (operationName == null) {
            throw new IllegalArgumentException("The 'operationName' argument is required but was null.");
        }
        if (resourceId == null) {
            throw new IllegalArgumentException("The 'resourceId' argument is required but was null.");
        }
        WebTarget endpoint = getWebTarget();
        endpoint = endpoint.path(resourceType).path(resourceId).path(operationName);
        endpoint = addParametersToWebTarget(endpoint, parameters);
        Invocation.Builder builder = endpoint.request(getDefaultMimeType());
        builder = addRequestHeaders(builder, headers);
        Response response = builder.get();
        return new FHIRResponseImpl(response);
    }

    @Override
    public FHIRResponse invoke(String resourceType, String operationName, String resourceId, Resource resource, FHIRRequestHeader... headers) throws Exception {
        if (resourceType == null) {
            throw new IllegalArgumentException("The 'resourceType' argument is required but was null.");
        }
        if (operationName == null) {
            throw new IllegalArgumentException("The 'operationName' argument is required but was null.");
        }
        if (resourceId == null) {
            throw new IllegalArgumentException("The 'resourceId' argument is required but was null.");
        }
        if (resource == null) {
            throw new IllegalArgumentException("The 'resource' argument is required but was null.");
        }
        WebTarget endpoint = getWebTarget();
        Entity<Parameters> entity = Entity.entity((Parameters)resource, getDefaultMimeType());
        Invocation.Builder builder = endpoint.path(resourceType).path(resourceId).path(operationName).request();
        builder = addRequestHeaders(builder, headers);
        Response response = builder.post(entity);
        return new FHIRResponseImpl(response);
    }

    @Override
    public FHIRResponse invoke(String resourceType, String operationName, String resourceId, String versionId, FHIRParameters parameters, FHIRRequestHeader... headers) throws Exception {
        if (resourceType == null) {
            throw new IllegalArgumentException("The 'resourceType' argument is required but was null.");
        }
        if (operationName == null) {
            throw new IllegalArgumentException("The 'operationName' argument is required but was null.");
        }
        if (resourceId == null) {
            throw new IllegalArgumentException("The 'resourceId' argument is required but was null.");
        }
        if (versionId == null) {
            throw new IllegalArgumentException("The 'versionId' argument is required but was null.");
        }
        WebTarget endpoint = getWebTarget();
        endpoint = endpoint.path(resourceType).path(resourceId).path("_history").path(versionId).path(operationName);
        endpoint = addParametersToWebTarget(endpoint, parameters);
        Invocation.Builder builder = endpoint.request(getDefaultMimeType());
        builder = addRequestHeaders(builder, headers);
        Response response = builder.get();
        return new FHIRResponseImpl(response);
    }

    @Override
    public FHIRResponse invoke(String resourceType, String operationName, String resourceId, String versionId, Resource resource, FHIRRequestHeader... headers) throws Exception {
        if (resourceType == null) {
            throw new IllegalArgumentException("The 'resourceType' argument is required but was null.");
        }
        if (operationName == null) {
            throw new IllegalArgumentException("The 'operationName' argument is required but was null.");
        }
        if (resourceId == null) {
            throw new IllegalArgumentException("The 'resourceId' argument is required but was null.");
        }
        if (versionId == null) {
            throw new IllegalArgumentException("The 'versionId' argument is required but was null.");
        }
        if (resource == null) {
            throw new IllegalArgumentException("The 'resource' argument is required but was null.");
        }
        WebTarget endpoint = getWebTarget();
        Entity<Parameters> entity = Entity.entity((Parameters)resource, getDefaultMimeType());
        Invocation.Builder builder = endpoint.path(resourceType).path(resourceId).path("_history").path(versionId).path(operationName).request();
        builder = addRequestHeaders(builder, headers);
        Response response = builder.post(entity);
        return new FHIRResponseImpl(response);
    }

    private FHIRResponse _bundle(Bundle bundle, BundleType bundleType, FHIRRequestHeader... headers) throws Exception {
        Bundle bundleNew = bundle.toBuilder().type(bundleType).build();

        WebTarget endpoint = getWebTarget();
        Entity<Bundle> entity = Entity.entity(bundleNew, getDefaultMimeType());
        Invocation.Builder builder = endpoint.request(getDefaultMimeType());
        builder = addRequestHeaders(builder, headers);
        Response response = builder.post(entity);
        return new FHIRResponseImpl(response);
    }

    /**
     * This function adds each of the parameters contained in the FHIRParameters object to the specified WebTarget as a
     * query parameter.
     *
     * @param endpoint
     *            the WebTarget which will receive the query parameters
     * @param parameters
     *            the FHIRParameters object that contains the query parameters to be added
     */
    private WebTarget addParametersToWebTarget(WebTarget endpoint, FHIRParameters parameters) {
        if (parameters != null) {
            MultivaluedMap<String, String> parameterMap = parameters.getParameterMap();
            if (parameterMap != null && !parameterMap.isEmpty()) {

                // Each entry in the multivalued map represents a query parameter and its value(s).
                // So for each entry, we'll add a query parameter to the WebTarget
                for (Map.Entry<String, List<String>> mapEntry : parameterMap.entrySet()) {
                    for (String value : mapEntry.getValue()) {
                        endpoint = endpoint.queryParam(mapEntry.getKey(), value);
                    }
                }
            }
        }

        return endpoint;
    }

    private Form buildForm(FHIRParameters parameters) {
        Form form = new Form();
        if (parameters != null) {
            MultivaluedMap<String, String> parameterMap = parameters.getParameterMap();
            if (parameterMap != null && !parameterMap.isEmpty()) {
                for (Map.Entry<String, List<String>> mapEntry : parameterMap.entrySet()) {
                    for (String value : mapEntry.getValue()) {
                        form.param(mapEntry.getKey(), value);
                    }
                }
            }
        }
        return form;
    }

    /**
     * This function will add each of the specified request headers to the Invocation Builder object.
     *
     * @param builder
     *            the Invocation.Builder used to build up the request
     * @param headers
     *            the array of headers to be added to the request
     */
    private Builder addRequestHeaders(Builder builder, FHIRRequestHeader[] headers) {
        if (headers != null) {
            for (FHIRRequestHeader header : headers) {
                if (header.getName() != null && header.getValue() != null) {
                    builder = builder.header(header.getName(), header.getValue());
                }
            }
        }

        // Set the tenantId, if we have one
        if (tenantId != null) {
            builder = builder.header(FHIRConfiguration.DEFAULT_TENANT_ID_HEADER_NAME, tenantId);
        }

        return builder;
    }

    @Override
    public WebTarget getWebTarget() throws Exception {
        return client.target(getBaseEndpointURL());
    }

    @Override
    public WebTarget getWebTarget(String baseURL) throws Exception {
        return client.target(baseURL);
    }

    /**
     * Process all the required properties found in the Properties object.
     *
     * @param props
     */
    private void initProperties(Properties props) throws Exception {
        try {
            setClientProperties(props);

            // Get the base endpoint URL and make sure it ends with a /.
            String s = getRequiredProperty(PROPNAME_BASE_URL);
            if (!s.endsWith("/")) {
                s += "/";
            }
            setBaseEndpointURL(s);

            // Process the default mimetype property.
            s = getProperty(PROPNAME_DEFAULT_MIMETYPE, null);
            if (s != null && !s.isEmpty()) {
                setDefaultMimeType(s);
            }

            // Process the basic auth properties (temporary until client auth is fully working).
            setBasicAuthEnabled(Boolean.parseBoolean(getProperty(PROPNAME_BASIC_AUTH_ENABLED, "false")));
            if (isBasicAuthEnabled()) {
                setBasicAuthUsername(getRequiredProperty(PROPNAME_CLIENT_USERNAME));
                setBasicAuthPassword(FHIRUtilities.decode(getRequiredProperty(PROPNAME_CLIENT_PASSWORD)));
            }

            // Process the OAuth 2.0 properties
            setOAuth2Enabled(Boolean.parseBoolean(getProperty(PROPNAME_OAUTH2_ENABLED, "false")));
            if (isOAuth2Enabled()) {
                setOAuth2AccessToken(FHIRUtilities.decode(getRequiredProperty(PROPNAME_OAUTH2_TOKEN)));
            }

            // If necessary, load the truststore-related properties.
            setClientAuthEnabled(Boolean.parseBoolean(getProperty(PROPNAME_CLIENT_AUTH_ENABLED, "false")));
            if (isOAuth2Enabled() || isClientAuthEnabled() || usingSSLTransport()) {
                String trustStoreLoc = getProperty(PROPNAME_TRUSTSTORE_LOCATION, null);
                String trustStoreEncodedPwd = getProperty(PROPNAME_TRUSTSTORE_PASSWORD, null);
                if (trustStoreLoc != null && trustStoreEncodedPwd != null) {
                    setTrustStoreLocation(trustStoreLoc);
                    setTrustStorePassword(FHIRUtilities.decode(trustStoreEncodedPwd));
                    setTrustStore(loadKeyStoreFile(getTrustStoreLocation(), getTrustStorePassword(), KEYSTORE_TYPE));
                }
            }

            // If necessary, load the keystore-related properties.
            if (isOAuth2Enabled() || isClientAuthEnabled()) {
                setKeyStoreLocation(getRequiredProperty(PROPNAME_KEYSTORE_LOCATION));
                setKeyStorePassword(FHIRUtilities.decode(getRequiredProperty(PROPNAME_KEYSTORE_PASSWORD)));
                setKeyStoreKeyPassword(FHIRUtilities.decode(getRequiredProperty(PROPNAME_KEYSTORE_KEY_PASSWORD)));
                setKeyStore(loadKeyStoreFile(getKeyStoreLocation(), getKeyStorePassword(), KEYSTORE_TYPE));
            }

            setLoggingEnabled(Boolean.parseBoolean(getProperty(PROPNAME_LOGGING_ENABLED, "false")));

            setHostnameVerificationEnabled(Boolean.parseBoolean(getProperty(PROPNAME_HOSTNAME_VERIFICATION_ENABLED, "true")));

            // Use a default that's longer than the default Liberty transaction timeout
            setHttpTimeout(Integer.parseUnsignedInt(getProperty(PROPNAME_HTTP_TIMEOUT, "130000")));

            setTenantId(getProperty(PROPNAME_TENANT_ID, null));
        } catch (Throwable t) {
            throw new Exception("Unexpected error while processing client properties.", t);
        }
    }

    private boolean usingSSLTransport() {
        return getBaseEndpointURL().startsWith("https:");
    }

    /**
     * Loads the client trust store file for use with https endpoints.
     */
    private KeyStore loadKeyStoreFile(String ksFilename, String ksPassword, String ksType) {
        InputStream is = null;
        try {
            KeyStore ks = KeyStore.getInstance(ksType);

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
     * Setter for the tenantId
     */
    private void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }


    /**
     * Retrieves the specified property from the client properties object.
     */
    private String getProperty(String propertyName, String defaultValue) {
        return clientProperties.getProperty(propertyName, defaultValue);
    }

    private String getRequiredProperty(String propertyName) throws Exception {
        String s = getProperty(propertyName, null);
        if (s == null) {
            throw new IllegalStateException("Required property '" + propertyName + "' not found in client properties object.");
        }

        return s;
    }

    private void setClientProperties(Properties clientProperties) {
        this.clientProperties = clientProperties;
    }

    private String getBaseEndpointURL() {
        return baseEndpointURL;
    }

    private void setBaseEndpointURL(String baseEndpointURL) {
        this.baseEndpointURL = baseEndpointURL;
    }

    @Override
    public void setDefaultMimeType(String mimeType) throws Exception {
        this.defaultMimeType = mimeType;
    }

    @Override
    public String getDefaultMimeType() throws Exception {
        return defaultMimeType;
    }

    @Override
    public void setOAuth2AccessToken(String accessToken) throws Exception {
        this.accessToken = accessToken;
    }

    @Override
    public String getOAuth2AccessToken() throws Exception {
        return accessToken;
    }

    private String getTrustStoreLocation() {
        return trustStoreLocation;
    }

    private void setTrustStoreLocation(String trustStoreLocation) {
        this.trustStoreLocation = trustStoreLocation;
    }

    private String getTrustStorePassword() {
        return trustStorePassword;
    }

    private void setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }

    @Override
    public KeyStore getTrustStore() {
        return trustStore;
    }

    private void setTrustStore(KeyStore trustStore) {
        this.trustStore = trustStore;
    }

    private boolean isOAuth2Enabled() {
        return oAuth2Enabled;
    }

    private void setOAuth2Enabled(boolean oAuth2Enabled) {
        this.oAuth2Enabled = oAuth2Enabled;
    }

    private boolean isBasicAuthEnabled() {
        return basicAuthEnabled;
    }

    private void setBasicAuthEnabled(boolean basicAuthEnabled) {
        this.basicAuthEnabled = basicAuthEnabled;
    }

    private String getBasicAuthUsername() {
        return basicAuthUsername;
    }

    private void setBasicAuthUsername(String basicAuthUsername) {
        this.basicAuthUsername = basicAuthUsername;
    }

    private String getBasicAuthPassword() {
        return basicAuthPassword;
    }

    private void setBasicAuthPassword(String basicAuthPassword) {
        this.basicAuthPassword = basicAuthPassword;
    }

    private boolean isClientAuthEnabled() {
        return clientAuthEnabled;
    }

    private void setClientAuthEnabled(boolean clientAuthEnabled) {
        this.clientAuthEnabled = clientAuthEnabled;
    }

    private KeyStore getKeyStore() {
        return keyStore;
    }

    private void setKeyStore(KeyStore keyStore) {
        this.keyStore = keyStore;
    }

    public String getKeyStoreLocation() {
        return keyStoreLocation;
    }

    public void setKeyStoreLocation(String keyStoreLocation) {
        this.keyStoreLocation = keyStoreLocation;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public String getKeyStoreKeyPassword() {
        return keyStoreKeyPassword;
    }

    public void setKeyStoreKeyPassword(String keyStoreKeyPassword) {
        this.keyStoreKeyPassword = keyStoreKeyPassword;
    }

    public boolean isLoggingEnabled() {
        return loggingEnabled;
    }

    public void setLoggingEnabled(boolean loggingEnabled) {
        this.loggingEnabled = loggingEnabled;
    }

    public boolean isHostnameVerificationEnabled() {
        return hostnameVerificationEnabled;
    }

    public void setHostnameVerificationEnabled(boolean hostnameVerficationEnabled) {
        this.hostnameVerificationEnabled = hostnameVerficationEnabled;
    }

    public int getHttpTimeout() {
        return httpTimeout;
    }

    public void setHttpTimeout(int httpTimeout) {
        this.httpTimeout = httpTimeout;
    }

    public HTTPReturnPreference getHttpReturnPref() {
        return httpReturnPref;
    }

    public void setHttpReturnPref(HTTPReturnPreference returnPref) {
        this.httpReturnPref = returnPref;
    }

    @Override
    public String getTenantId() {
        return this.tenantId == null ? "default" : this.tenantId;
    }
}
