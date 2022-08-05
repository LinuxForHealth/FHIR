/*
 * (C) Copyright IBM Corp. 2018, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.provider;

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.RuntimeType;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.config.FHIRConfiguration;

public class FHIRProviderTest {

    /*
     * generate multivalued map
     * @param value
     * @return
     */
    public MultivaluedMap<String, String> generateMulivaluedMap(String value) {
        return new MultivaluedMap<String, String>() {

            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean containsKey(Object key) {
                return false;
            }

            @Override
            public boolean containsValue(Object value) {
                return false;
            }

            @Override
            public List<String> get(Object key) {
                return null;
            }

            @Override
            public List<String> put(String key, List<String> value) {
                return null;
            }

            @Override
            public List<String> remove(Object key) {
                return null;
            }

            @Override
            public void putAll(Map<? extends String, ? extends List<String>> m) {

            }

            @Override
            public void clear() {

            }

            @Override
            public Set<String> keySet() {
                return null;
            }

            @Override
            public Collection<List<String>> values() {
                return null;
            }

            @Override
            public Set<Entry<String, List<String>>> entrySet() {
                return null;
            }

            @Override
            public void putSingle(String key, String value) {

            }

            @Override
            public void add(String key, String value) {

            }

            @Override
            public String getFirst(String key) {
                return value;
            }

            @Override
            public void addAll(String key, String... newValues) {

            }

            @Override
            public void addAll(String key, List<String> valueList) {

            }

            @Override
            public void addFirst(String key, String value) {

            }

            @Override
            public boolean equalsIgnoreValueOrder(MultivaluedMap<String, String> otherMap) {
                return false;
            }

        };
    }

    /*
     * generate uri info
     */
    public UriInfo generatePrettyParameterUriInfo(String value) {
        return new UriInfo() {

            @Override
            public String getPath() {
                return null;
            }

            @Override
            public String getPath(boolean decode) {
                return null;
            }

            @Override
            public List<PathSegment> getPathSegments() {
                return null;
            }

            @Override
            public List<PathSegment> getPathSegments(boolean decode) {
                return null;
            }

            @Override
            public URI getRequestUri() {
                return null;
            }

            @Override
            public UriBuilder getRequestUriBuilder() {
                return null;
            }

            @Override
            public URI getAbsolutePath() {
                return null;
            }

            @Override
            public UriBuilder getAbsolutePathBuilder() {
                return null;
            }

            @Override
            public URI getBaseUri() {
                return null;
            }

            @Override
            public UriBuilder getBaseUriBuilder() {
                return null;
            }

            @Override
            public MultivaluedMap<String, String> getPathParameters() {
                return null;
            }

            @Override
            public MultivaluedMap<String, String> getPathParameters(boolean decode) {
                return null;
            }

            @Override
            public MultivaluedMap<String, String> getQueryParameters() {
                return generateMulivaluedMap(value);
            }

            @Override
            public MultivaluedMap<String, String> getQueryParameters(boolean decode) {
                return generateMulivaluedMap(value);
            }

            @Override
            public List<String> getMatchedURIs() {
                return null;
            }

            @Override
            public List<String> getMatchedURIs(boolean decode) {
                return null;
            }

            @Override
            public List<Object> getMatchedResources() {

                return null;
            }

            @Override
            public URI resolve(URI uri) {
                return null;
            }

            @Override
            public URI relativize(URI uri) {
                return null;
            }

        };
    }

    @Test
    public void isPrettyDefaultsFalse() {
        HttpHeaders headers = createHeaders();
        FHIRProvider fhirProvider = new FHIRProvider(RuntimeType.SERVER);

        // when empty
        assertFalse(fhirProvider.isPretty(headers, generatePrettyParameterUriInfo("false")));

        // when populated but misspelled
        headers.getRequestHeaders().putSingle(FHIRConfiguration.DEFAULT_PRETTY_RESPONSE_HEADER_NAME, "nottrue");
        assertFalse(fhirProvider.isPretty(headers, generatePrettyParameterUriInfo("false")));
    }

    @Test
    public void isPrettyTrueForTrue() {
        HttpHeaders headers = createHeaders();
        FHIRProvider fhirProvider = new FHIRProvider(RuntimeType.SERVER);

        // when populated to true
        headers.getRequestHeaders().putSingle(FHIRConfiguration.DEFAULT_PRETTY_RESPONSE_HEADER_NAME, "true");
        assertTrue(fhirProvider.isPretty(headers, generatePrettyParameterUriInfo("false")));
    }

    @Test
    public void isPrettyFalseForFalse() {
        HttpHeaders headers = createHeaders();
        FHIRProvider fhirProvider = new FHIRProvider(RuntimeType.SERVER);

        // when populated to true
        headers.getRequestHeaders().putSingle(FHIRConfiguration.DEFAULT_PRETTY_RESPONSE_HEADER_NAME, "false");
        assertFalse(fhirProvider.isPretty(headers, generatePrettyParameterUriInfo("false")));
    }

    private static HttpHeaders createHeaders() {
        return new HttpHeaders() {
            private MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();

            @Override
            public List<String> getRequestHeader(String s) {
                return headers.get(s);
            }

            @Override
            public String getHeaderString(String s) {
                if (headers.get(s) == null) {
                    return null;
                }
                return headers.get(s).stream().collect(Collectors.joining(","));
            }

            @Override
            public MultivaluedMap<String, String> getRequestHeaders() {
                return headers;
            }

            @Override
            public List<MediaType> getAcceptableMediaTypes() {
                return null;
            }

            @Override
            public List<Locale> getAcceptableLanguages() {
                return null;
            }

            @Override
            public MediaType getMediaType() {
                return null;
            }

            @Override
            public Locale getLanguage() {
                return null;
            }

            @Override
            public Map<String, Cookie> getCookies() {
                return null;
            }

            @Override
            public Date getDate() {
                return null;
            }

            @Override
            public int getLength() {
                return 0;
            }
        };
    }
}
