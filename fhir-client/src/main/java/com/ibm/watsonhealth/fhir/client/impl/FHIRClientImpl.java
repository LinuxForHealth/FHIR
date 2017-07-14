/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.client.impl;

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

import javax.crypto.spec.SecretKeySpec;
import javax.json.JsonObject;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.ibm.watsonhealth.fhir.client.FHIRClient;
import com.ibm.watsonhealth.fhir.client.FHIRParameters;
import com.ibm.watsonhealth.fhir.client.FHIRResponse;
import com.ibm.watsonhealth.fhir.client.FHIRRequestHeader;
import com.ibm.watsonhealth.fhir.core.FHIRUtilities;
import com.ibm.watsonhealth.fhir.model.Bundle;
import com.ibm.watsonhealth.fhir.model.BundleType;
import com.ibm.watsonhealth.fhir.model.BundleTypeList;
import com.ibm.watsonhealth.fhir.model.Parameters;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.provider.FHIRJsonProvider;
import com.ibm.watsonhealth.fhir.provider.FHIRProvider;

/**
 * Provides an implementation of the FHIRClient interface, which can be used as a high-level API for invoking FHIR
 * REST APIs.
 */
public class FHIRClientImpl implements FHIRClient {
    
    private static final String KEYSTORE_TYPE_JKS = "JKS";
    private static final String KEYSTORE_TYPE_JCEKS = "JCEKS";
    private static final String ENCRYPTION_ALGORITHM_AES = "AES";

    private Client client = null;
    private Properties clientProperties = null;
    private String baseEndpointURL = null;
    private String defaultMimeType = "application/json+fhir";
    
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
    
    private boolean encryptionEnabled = false;
    private String encKeyStoreLocation = null;
    private String encKeyStorePassword = null;
    private String encKeyPassword = null;
    private SecretKeySpec encryptionKey = null;
    
    protected FHIRClientImpl() {
    }

    public FHIRClientImpl(Properties props) throws Exception {
        initProperties(props);
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRClient#metadata()
     */
    @Override
    public FHIRResponse metadata(FHIRRequestHeader... headers) throws Exception {
        WebTarget endpoint = getWebTarget();
        Invocation.Builder builder = endpoint.path("metadata").request(getDefaultMimeType());
        builder = addRequestHeaders(builder, headers);
        Response response = builder.get();
        return new FHIRResponseImpl(response);
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRClient#create(com.ibm.watsonhealth.fhir.model.Resource)
     */
    @Override
    public FHIRResponse create(Resource resource, FHIRRequestHeader... headers) throws Exception {
        if (resource == null) {
            throw new IllegalArgumentException("The 'resource' argument is required but was null.");
        }
        String resourceType = resource.getClass().getSimpleName();
        return _create(resource, resourceType, null, headers);
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRClient#create(javax.json.JsonObject, java.lang.String)
     */
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


    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRClient#conditionalCreate(com.ibm.watsonhealth.fhir.model.Resource, com.ibm.watsonhealth.fhir.client.FHIRParameters, com.ibm.watsonhealth.fhir.client.FHIRRequestHeader[])
     */
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

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRClient#conditionalCreate(javax.json.JsonObject, com.ibm.watsonhealth.fhir.client.FHIRParameters, com.ibm.watsonhealth.fhir.client.FHIRRequestHeader[])
     */
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
    

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRClient#update(java.lang.Object, java.lang.Class, java.lang.String)
     */
    @Override
    public FHIRResponse update(Resource resource, FHIRRequestHeader... headers) throws Exception {
        if (resource == null) {
            throw new IllegalArgumentException("The 'resource' argument is required but was null.");
        }
        String resourceType = resource.getClass().getSimpleName();
        String resourceId = (resource.getId() != null ? resource.getId().getValue() : null);
        if (resourceId == null || resourceId.isEmpty()) {
            throw new IllegalArgumentException("Unable to retrieve the resource id from the resource.");
        }
        return _update(resource, resourceType, resourceId, null, headers);
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRClient#update(javax.json.JsonObject, java.lang.String, java.lang.String)
     */
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
    
    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRClient#conditionalUpdate(com.ibm.watsonhealth.fhir.model.Resource, com.ibm.watsonhealth.fhir.client.FHIRParameters, com.ibm.watsonhealth.fhir.client.FHIRRequestHeader[])
     */
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

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRClient#conditionalUpdate(javax.json.JsonObject, com.ibm.watsonhealth.fhir.client.FHIRParameters, com.ibm.watsonhealth.fhir.client.FHIRRequestHeader[])
     */
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

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRClient#delete(java.lang.String, java.lang.String, com.ibm.watsonhealth.fhir.client.FHIRRequestHeader[])
     */
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

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRClient#conditionalDelete(java.lang.String, com.ibm.watsonhealth.fhir.client.FHIRParameters, com.ibm.watsonhealth.fhir.client.FHIRRequestHeader[])
     */
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
            // Otherwise, for a conditional delte, add the search query parameter(s) to the request.
            endpoint = addParametersToWebTarget(endpoint, parameters);
        }

        Invocation.Builder builder = endpoint.request(getDefaultMimeType());
        builder = addRequestHeaders(builder, headers);
        Response response = builder.delete();
        return new FHIRResponseImpl(response);
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRClient#read(java.lang.String, java.lang.String)
     */
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

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRClient#vread(java.lang.String, java.lang.String, int)
     */
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
    
    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRClient#history(java.lang.String, java.lang.String, com.ibm.watsonhealth.fhir.client.FHIRParameters)
     */
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

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRClient#search(java.lang.String, com.ibm.watsonhealth.fhir.client.FHIRParameters)
     */
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
    public FHIRResponse searchAll(FHIRParameters parameters, FHIRRequestHeader... headers) throws Exception {
        WebTarget endpoint = getWebTarget();
        endpoint = endpoint.path("_search");
        endpoint = addParametersToWebTarget(endpoint, parameters);
        Invocation.Builder builder = endpoint.request(getDefaultMimeType());
        builder = addRequestHeaders(builder, headers);
        Response response = builder.get();
        return new FHIRResponseImpl(response);
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRClient#validate(com.ibm.watsonhealth.fhir.model.Resource)
     */
    @Override
    public FHIRResponse validate(Resource resource, FHIRRequestHeader... headers) throws Exception {
        if (resource == null) {
            throw new IllegalArgumentException("The 'resource' argument is required but was null.");
        }
        return _validate(resource, headers);
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRClient#validate(javax.json.JsonObject)
     */
    @Override
    public FHIRResponse validate(JsonObject resource, FHIRRequestHeader... headers) throws Exception {
        if (resource == null) {
            throw new IllegalArgumentException("The 'resource' argument is required but was null.");
        }
        return _validate(resource, headers);
    }
    
    private <T> FHIRResponse _validate(T resource, FHIRRequestHeader...headers) throws Exception {
        WebTarget endpoint = getWebTarget();
        Entity<T> entity = Entity.entity(resource, getDefaultMimeType());
        Invocation.Builder builder = endpoint.path("Resource").path("$validate").request(getDefaultMimeType());
        builder = addRequestHeaders(builder, headers);
        Response response = builder.post(entity);
        return new FHIRResponseImpl(response);
        
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRClient#batch(com.ibm.watsonhealth.fhir.model.Bundle)
     */
    @Override
    public FHIRResponse batch(Bundle bundle, FHIRRequestHeader... headers) throws Exception {
        if (bundle == null) {
            throw new IllegalArgumentException("The 'bundle' argument is required but was null.");
        }
        return _bundle(bundle, BundleTypeList.BATCH, headers);
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRClient#transaction(com.ibm.watsonhealth.fhir.model.Bundle)
     */
    @Override
    public FHIRResponse transaction(Bundle bundle, FHIRRequestHeader... headers) throws Exception {
        if (bundle == null) {
            throw new IllegalArgumentException("The 'bundle' argument is required but was null.");
        }
        return _bundle(bundle, BundleTypeList.TRANSACTION, headers);
    }
    
    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRClient#invoke(String, com.ibm.watsonhealth.fhir.client.FHIRParameters)
     */
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
    
    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRClient#invoke(String, com.ibm.watsonhealth.fhir.model.Resource)
     */
    @Override
    public FHIRResponse invoke(String operationName, Resource resource, FHIRRequestHeader... headers) throws Exception {
        if (operationName == null) {
            throw new IllegalArgumentException("The 'operationName' argument is required but was null.");
        }
        if (resource == null) {
            throw new IllegalArgumentException("The 'resource' argument is required but was null.");
        }
        WebTarget endpoint = getWebTarget();
        Entity<Parameters> entity = Entity.entity((Parameters)resource, getDefaultMimeType());
        Invocation.Builder builder = endpoint.path(operationName).request();
        builder = addRequestHeaders(builder, headers);
        Response response = builder.post(entity);
        return new FHIRResponseImpl(response);
    }
    
    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRClient#invoke(String, String)
     */
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
    
    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRClient#invoke(String, String, com.ibm.watsonhealth.fhir.model.Resource)
     */
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
    
    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRClient#invoke(String, String, String)
     */
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
    
    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRClient#invoke(String, String, String, com.ibm.watsonhealth.fhir.model.Resource)
     */
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
    
    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRClient#invoke(String, String, String, String)
     */
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
    
    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRClient#invoke(String, String, String, String, com.ibm.watsonhealth.fhir.model.Resource)
     */
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
    
    private FHIRResponse _bundle(Bundle bundle, BundleTypeList bundleType, FHIRRequestHeader... headers) throws Exception {
        bundle.setType(new BundleType().withValue(bundleType));
        WebTarget endpoint = getWebTarget();
        Entity<Bundle> entity = Entity.entity(bundle, getDefaultMimeType());
        Invocation.Builder builder = endpoint.request(getDefaultMimeType());
        builder = addRequestHeaders(builder, headers);
        Response response = builder.post(entity);
        return new FHIRResponseImpl(response);
    }
    
    /**
     * This function adds each of the parameters contained in the FHIRParameters object 
     * to the specified WebTarget as a query parameter.
     * @param endpoint the WebTarget which will receive the query parameters
     * @param parameters the FHIRParameters object that contains the query parameters to be added
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
     * @param builder the Invocation.Builder used to build up the request
     * @param headers the array of headers to be added to the request
     */
    private Builder addRequestHeaders(Builder builder, FHIRRequestHeader[] headers) {
        if (headers != null) {
            for (FHIRRequestHeader header : headers) {
                if (header.getName() != null && header.getValue() != null) {
                    builder = builder.header(header.getName(), header.getValue());
                }
            }
        }
        return builder;
    }

    /**
     * Retrieves a jax-rs Client from the ClientBuilder object.  The Client instance is created if necessary.
     */
    protected synchronized Client getClient() throws Exception {
        if (client == null) {
            ClientBuilder cb = ClientBuilder.newBuilder()
                    .register(new FHIRProvider())
                    .register(new FHIRJsonProvider());
            
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
            
            // Add support for encryption/decryption if enabled.
            if (isEncryptionEnabled()) {
                try {
                    cb = cb.register(new FHIREncryptionClientFilter(getEncryptionKey()));
                } catch (Throwable t) {
                    throw new Exception("Unexpected error while registering encryption client filter: ", t);
                }
            }
            
            // Finally, add a hostname verifier if we're using an ssl transport.
            if (usingSSLTransport()) {
                cb = cb.hostnameVerifier(new HostnameVerifier() {
                    public boolean verify(String s, SSLSession sslSession) {
                        return true;
                    }
                });
            }

            // Save off our cached Client instance.
            client = cb.build();
            
        }
        return client;
    }
    
    /*
     * (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRClient#getWebTarget()
     */
    @Override
    public WebTarget getWebTarget() throws Exception {
        
        return getClient().target(getBaseEndpointURL());
    }
    
    /*
     * (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRClient#getWebTarget()
     */
    @Override
    public WebTarget getWebTarget(String baseURL) throws Exception {
    	ClientBuilder cb = ClientBuilder.newBuilder()
                .register(new FHIRProvider())
                .register(new FHIRJsonProvider())
        	    .keyStore(getKeyStore(), getKeyStoreKeyPassword());
    	
    	KeyStore ts = getTrustStore();
    	
    	if(ts != null) {
    		cb = cb.trustStore(ts);
    	}
        Client client = cb.build();
        return client.target(baseURL);
    }
    
    /*
     * (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRClient#getWebTargetUsingBasicAuth()
     */
    @Override
    public WebTarget getWebTargetUsingBasicAuth(String baseURL, String username, String pwd) throws Exception {
        Client client = ClientBuilder.newBuilder()
                .register(new FHIRProvider())
                .register(new FHIRJsonProvider())
        		.register(new FHIRBasicAuthenticator(username, pwd))
        	    .keyStore(getKeyStore(), getKeyStoreKeyPassword())
        	    .trustStore(getTrustStore())
        	    .build();
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
            	if(trustStoreLoc != null && trustStoreEncodedPwd != null) {
            		setTrustStoreLocation(trustStoreLoc);
                	setTrustStorePassword(FHIRUtilities.decode(trustStoreEncodedPwd));
                	setTrustStore(loadKeyStoreFile(getTrustStoreLocation(), getTrustStorePassword(), KEYSTORE_TYPE_JKS));
            	}
            }
            
            // If necessary, load the keystore-related properties.
            if (isOAuth2Enabled() || isClientAuthEnabled()) {
                setKeyStoreLocation(getRequiredProperty(PROPNAME_KEYSTORE_LOCATION));
                setKeyStorePassword(FHIRUtilities.decode(getRequiredProperty(PROPNAME_KEYSTORE_PASSWORD)));
                setKeyStoreKeyPassword(FHIRUtilities.decode(getRequiredProperty(PROPNAME_KEYSTORE_KEY_PASSWORD)));
                setKeyStore(loadKeyStoreFile(getKeyStoreLocation(), getKeyStorePassword(), KEYSTORE_TYPE_JKS));
            }
            
            // Process the encryption-related properties.
            setEncryptionEnabled(Boolean.parseBoolean(getProperty(PROPNAME_ENCRYPTION_ENABLED, "false")));
            if (isEncryptionEnabled()) {
                setEncKeyStoreLocation(getRequiredProperty(PROPNAME_ENCRYPTION_KSLOC));
                setEncKeyStorePassword(FHIRUtilities.decode(getRequiredProperty(PROPNAME_ENCRYPTION_KSPW)));
                setEncKeyPassword(FHIRUtilities.decode(getRequiredProperty(PROPNAME_ENCRYPTION_KEYPW)));
                setEncryptionKey(FHIRUtilities.retrieveEncryptionKeyFromKeystore(getEncKeyStoreLocation(), getEncKeyStorePassword(), ENCRYPTION_KEY_ALIAS, getEncKeyPassword(), KEYSTORE_TYPE_JCEKS, ENCRYPTION_ALGORITHM_AES));
            }
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
        try {
            KeyStore ks = KeyStore.getInstance(ksType);

            // First, search the classpath for the truststore file.
            InputStream is = null;
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
        }
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

    private SecretKeySpec getEncryptionKey() {
        return encryptionKey;
    }

    private void setEncryptionKey(SecretKeySpec encryptionKey) {
        this.encryptionKey = encryptionKey;
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

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRClient#setDefaultMimeType(java.lang.String)
     */
    @Override
    public void setDefaultMimeType(String mimeType) throws Exception {
        this.defaultMimeType = mimeType;
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRClient#getDefaultMimeType()
     */
    @Override
    public String getDefaultMimeType() throws Exception {
        return defaultMimeType;
    }
    
    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRClient#setOAuth2AccessToken(java.lang.String)
     */
    @Override
    public void setOAuth2AccessToken(String accessToken) throws Exception {
        this.accessToken = accessToken;
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.client.FHIRClient#getOAuth2AccessToken()
     */
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

    private KeyStore getTrustStore() {
        return trustStore;
    }

    private void setTrustStore(KeyStore trustStore) {
        this.trustStore = trustStore;
    }

    private boolean isEncryptionEnabled() {
        return encryptionEnabled;
    }

    private void setEncryptionEnabled(boolean encryptionEnabled) {
        this.encryptionEnabled = encryptionEnabled;
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

    public String getEncKeyStoreLocation() {
        return encKeyStoreLocation;
    }

    public void setEncKeyStoreLocation(String encKeyStoreLocation) {
        this.encKeyStoreLocation = encKeyStoreLocation;
    }

    public String getEncKeyStorePassword() {
        return encKeyStorePassword;
    }

    public void setEncKeyStorePassword(String encKeyStorePassword) {
        this.encKeyStorePassword = encKeyStorePassword;
    }

    public String getEncKeyPassword() {
        return encKeyPassword;
    }

    public void setEncKeyPassword(String encKeyPassword) {
        this.encKeyPassword = encKeyPassword;
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
}
