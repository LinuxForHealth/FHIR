/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.core.util;

import static com.ibm.fhir.core.util.LRUCache.createLRUCache;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A utility class for working with URLs
 */
public class URLSupport {
    private static final Map<String, URL> URL_CACHE = createLRUCache(128);

    private URLSupport() { }

    public static String decode(String s) {
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getFirst(Map<String, List<String>> queryParameters, String key) {
        List<String> values = queryParameters.get(key);
        return (values != null && !values.isEmpty()) ? values.get(0) : null;
    }

    public static String getPath(String url) {
        return getURL(url).getPath();
    }

    public static List<String> getPathSegments(String url) {
        return getPathSegments(url, true);
    }

    public static List<String> getPathSegments(String url, boolean decode) {
        return parsePath(getPath(url), decode);
    }

    public static String getQuery(String url) {
        return getURL(url).getQuery();
    }

    public static Map<String, List<String>> getQueryParameters(String url) {
        return getQueryParameters(url, true);
    }

    public static Map<String, List<String>> getQueryParameters(String url, boolean decode) {
        return parseQuery(getQuery(url), decode);
    }

    public static URL getURL(String url) {
        return URL_CACHE.computeIfAbsent(url, k -> computeURL(url));
    }

    public static List<String> parsePath(String path) {
        return parsePath(path, true);
    }

    public static List<String> parsePath(String path, boolean decode) {
        return Arrays.stream(path.split("/"))
            .skip(1)
            .map(s -> decode ? decode(s) : s)
            .collect(Collectors.toList());
    }

    public static Map<String, List<String>> parseQuery(String query) {
        return parseQuery(query, true);
    }

    public static Map<String, List<String>> parseQuery(String query, boolean decode) {
        if (query == null || query.isEmpty()) {
            return Collections.emptyMap();
        }
        return Arrays.stream(query.split("&"))
            .map(pair -> Arrays.asList(pair.split("=", 2)))
            .collect(Collectors.collectingAndThen(
                Collectors.toMap(
                    // key mapping function
                    pair -> decode ? decode(pair.get(0)) : pair.get(0),
                    // value mapping function
                    pair -> (pair.size() > 1) ? Collections.unmodifiableList(Arrays.stream(pair.get(1).split(","))
                        .map(s -> decode ? decode(s) : s)
                        .collect(Collectors.toList())) : Collections.<String>emptyList(),
                    // merge function
                    (u, v) -> {
                        List<String> merged = new ArrayList<>(u);
                        merged.addAll(v);
                        return Collections.unmodifiableList(merged);
                    },
                    // map supplier
                    LinkedHashMap::new),
                Collections::unmodifiableMap));
    }

    private static URL computeURL(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException (e);
        }
    }
}
