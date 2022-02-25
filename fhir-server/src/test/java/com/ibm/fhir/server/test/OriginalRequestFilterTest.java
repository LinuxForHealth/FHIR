/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.server.resources.filters.OriginalRequestFilter;

public class OriginalRequestFilterTest {
    private static final AssertionError UNUSED_ERROR = new AssertionError("Unused");

    @BeforeClass
    public void setup() {
        FHIRConfiguration.setConfigHome("src/test/resources");
    }

    @BeforeMethod
    public void startMethod(Method method) throws FHIRException {
        // Configure the request context for our search tests
        FHIRRequestContext context = FHIRRequestContext.get();

        //Facilitate the switching of tenant configurations based on method name
        String tenant = "default";
        String methodName = method.getName();
        if (methodName.contains("_tenant_")) {
            int idx = methodName.indexOf("_tenant_") + "_tenant_".length();
            tenant = methodName.substring(idx);
        }
        context.setTenantId(tenant);
        context.setOriginalRequestUri("https://localhost:9443/fhir-server/api/v4");
    }

    @AfterMethod
    public void clearThreadLocal() {
        FHIRRequestContext.remove();
    }

    @Test
    void testOriginalRequestFilter_tenant_default() throws Exception {
        OriginalRequestFilter filter = new OriginalRequestFilter();
        filter.filter(generateRequestContext("Condition/1/_history", "https://localhost:9443/fhir-server/api/v4"));
        assertNotNull(FHIRRequestContext.get().getOriginalRequestUri());
        assertEquals(FHIRRequestContext.get().getOriginalRequestUri(), "https://localhost:9443/fhir-server/api/v4");
    }

    @Test
    void testOriginalRequestFilter_tenant_overrideuri() throws Exception {
        OriginalRequestFilter filter = new OriginalRequestFilter();
        filter.filter(generateRequestContext("Condition/1/_history", "http://example.com/.well-known/smart-configuration"));
        assertNotNull(FHIRRequestContext.get().getOriginalRequestUri());
        assertEquals(FHIRRequestContext.get().getOriginalRequestUri(), "https://chocolate.fudge/Condition/1/_history");
    }

    @Test
    void testOriginalRequestFilterOverride_tenant_overrideuri() throws Exception {
        FHIRRequestContext.get().setOriginalRequestUri("https://localhost:9443/fhir-server/api/v4?a=1");
        OriginalRequestFilter filter = new OriginalRequestFilter();
        filter.filter(generateRequestContext("Condition/1/_history", "http://example.com/.well-known/smart-configuration?a=1"));
        assertNotNull(FHIRRequestContext.get().getOriginalRequestUri());
        assertEquals(FHIRRequestContext.get().getOriginalRequestUri(), "https://chocolate.fudge/Condition/1/_history?a=1");
    }

    /**
     * generates container request context.
     *
     * @param path
     * @param requestUri
     * @return
     */
    private ContainerRequestContext generateRequestContext(String path, String requestUri) {
        return new ContainerRequestContext() {
            @Override
            public UriInfo getUriInfo() {
                return generateUriInfo(path, requestUri);
            }

            @Override
            public void abortWith(Response response) {
                throw UNUSED_ERROR;
            }

            @Override
            public Object getProperty(String name) {
                throw UNUSED_ERROR;
            }

            @Override
            public Collection<String> getPropertyNames() {
                throw UNUSED_ERROR;
            }

            @Override
            public void setProperty(String name, Object object) {
                throw UNUSED_ERROR;
            }

            @Override
            public void removeProperty(String name) {
                throw UNUSED_ERROR;
            }

            @Override
            public void setRequestUri(URI requestUri) {
                throw UNUSED_ERROR;
            }

            @Override
            public void setRequestUri(URI baseUri, URI requestUri) {
                throw UNUSED_ERROR;
            }

            @Override
            public Request getRequest() {
                throw UNUSED_ERROR;
            }

            @Override
            public String getMethod() {
                throw UNUSED_ERROR;
            }

            @Override
            public void setMethod(String method) {
                throw UNUSED_ERROR;
            }

            @Override
            public MultivaluedMap<String, String> getHeaders() {
                throw UNUSED_ERROR;
            }

            @Override
            public String getHeaderString(String name) {
                throw UNUSED_ERROR;
            }

            @Override
            public Date getDate() {
                throw UNUSED_ERROR;
            }

            @Override
            public Locale getLanguage() {
                throw UNUSED_ERROR;
            }

            @Override
            public int getLength() {
                throw UNUSED_ERROR;
            }

            @Override
            public MediaType getMediaType() {
                throw UNUSED_ERROR;
            }

            @Override
            public List<MediaType> getAcceptableMediaTypes() {
                throw UNUSED_ERROR;
            }

            @Override
            public List<Locale> getAcceptableLanguages() {
                throw UNUSED_ERROR;
            }

            @Override
            public Map<String, Cookie> getCookies() {
                throw UNUSED_ERROR;
            }

            @Override
            public boolean hasEntity() {
                throw UNUSED_ERROR;
            }

            @Override
            public InputStream getEntityStream() {
                throw UNUSED_ERROR;
            }

            @Override
            public void setEntityStream(InputStream input) {
                throw UNUSED_ERROR;
            }

            @Override
            public SecurityContext getSecurityContext() {
                throw UNUSED_ERROR;
            }

            @Override
            public void setSecurityContext(SecurityContext context) {
                throw UNUSED_ERROR;
            }
        };
    }

    /**
     * generate uri info
     * @param path
     * @param requestUri
     * @return
     */
    public UriInfo generateUriInfo(String path, String requestUri) {
        return new UriInfo() {
            @Override
            public String getPath() {
                return path;
            }

            @Override
            public URI getRequestUri() {
                try {
                    return new URI(requestUri);
                } catch (URISyntaxException e) {
                    throw UNUSED_ERROR;
                }
            }

            @Override
            public String getPath(boolean decode) {
                throw UNUSED_ERROR;
            }

            @Override
            public List<PathSegment> getPathSegments() {
                throw UNUSED_ERROR;
            }

            @Override
            public List<PathSegment> getPathSegments(boolean decode) {
                throw UNUSED_ERROR;
            }

            @Override
            public UriBuilder getRequestUriBuilder() {
                throw UNUSED_ERROR;
            }

            @Override
            public URI getAbsolutePath() {
                throw UNUSED_ERROR;
            }

            @Override
            public UriBuilder getAbsolutePathBuilder() {
                throw UNUSED_ERROR;
            }

            @Override
            public URI getBaseUri() {
                throw UNUSED_ERROR;
            }

            @Override
            public UriBuilder getBaseUriBuilder() {
                throw UNUSED_ERROR;
            }

            @Override
            public MultivaluedMap<String, String> getPathParameters() {
                throw UNUSED_ERROR;
            }

            @Override
            public MultivaluedMap<String, String> getPathParameters(boolean decode) {
                throw UNUSED_ERROR;
            }

            @Override
            public MultivaluedMap<String, String> getQueryParameters() {
                throw UNUSED_ERROR;
            }

            @Override
            public MultivaluedMap<String, String> getQueryParameters(boolean decode) {
                throw UNUSED_ERROR;
            }

            @Override
            public List<String> getMatchedURIs() {
                throw UNUSED_ERROR;
            }

            @Override
            public List<String> getMatchedURIs(boolean decode) {
                throw UNUSED_ERROR;
            }

            @Override
            public List<Object> getMatchedResources() {
                throw UNUSED_ERROR;
            }

            @Override
            public URI resolve(URI uri) {
                throw UNUSED_ERROR;
            }

            @Override
            public URI relativize(URI uri) {
                throw UNUSED_ERROR;
            }
        };
    }
}