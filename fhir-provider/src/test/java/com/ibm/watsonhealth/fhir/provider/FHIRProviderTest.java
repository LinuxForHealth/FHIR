/**
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.provider;

import com.ibm.watsonhealth.fhir.config.FHIRConfiguration;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import javax.ws.rs.core.*;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by rick.boyd on 11/19/18.
 */

public class FHIRProviderTest {

    @Test
    public void isPrettyDefaultsFalse() {
        HttpHeaders headers = createHeaders();
        // when empty
        assertFalse(FHIRProvider.isPretty(headers));
        // when populated but misspelled
        headers.getRequestHeaders().putSingle(FHIRConfiguration.DEFAULT_PRETTY_RESPONSE_HEADER_NAME, "nottrue");
        assertFalse(FHIRProvider.isPretty(headers));
    }

    @Test
    public void isPrettyTrueForTrue() {
        HttpHeaders headers = createHeaders();
        headers.getRequestHeaders().putSingle(FHIRConfiguration.DEFAULT_PRETTY_RESPONSE_HEADER_NAME, "true");
        assertTrue(FHIRProvider.isPretty(headers));
    }

    @Test
    public void isPrettyFalseForFalse() {
        HttpHeaders headers = createHeaders();
        headers.getRequestHeaders().putSingle(FHIRConfiguration.DEFAULT_PRETTY_RESPONSE_HEADER_NAME, "false");

        assertFalse(FHIRProvider.isPretty(headers));
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
