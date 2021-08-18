/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.core.util;

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
    private URLSupport() { }

    /**
     * URL decode the input string
     *
     * @param s
     *     the string to URL decode
     * @return
     *     the URL decoded string
     */
    public static String decode(String s) {
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the first value of the list for the specified key from the provided multivalued map
     *
     * @param map
     *     the multivalued map
     * @param key
     *     the key
     * @return
     *     the first value of the list for the specified key from the provided multivalued map or null if not exists
     */
    public static String getFirst(Map<String, List<String>> map, String key) {
        List<String> values = map.get(key);
        return (values != null && !values.isEmpty()) ? values.get(0) : null;
    }

    /**
     * Get the path part of the provided URL
     *
     * @param url
     *     the url
     * @return
     *     the path part or empty if not exists
     * @see
     *     URL#getPath()
     */
    public static String getPath(String url) {
        return getURL(url).getPath();
    }

    /**
     * Get a list containing the path segments from the provided URL
     *
     * <p>The path segments are URL decoded
     *
     * @param url
     *     the url
     * @return
     *     a list containing the path segments from the provided URL
     */
    public static List<String> getPathSegments(String url) {
        return getPathSegments(url, true);
    }

    /**
     * Get a list containing the path segments from the provided URL
     *
     * <p>The path segments are URL decoded according to the specified parameter
     *
     * @param url
     *     the url
     * @param decode
     *     indicates whether to decode the path segments
     * @return
     *     a list containing the path segments from the provided URL
     */
    public static List<String> getPathSegments(String url, boolean decode) {
        return parsePath(getPath(url), decode);
    }

    /**
     * Get the query part of the provided URL
     *
     * @param url
     *     the URL
     * @return
     *     the query part of the provided URL or empty if not exists
     * @see
     *     URL#getQuery()
     */
    public static String getQuery(String url) {
        return getURL(url).getQuery();
    }

    /**
     * Get a multivalued map containing the query parameters for the provided URL
     *
     * <p>The keys and values of the multivalued map are URL decoded
     *
     * @param url
     *     the URL
     * @return
     *     a multivalued map containing the query parameters for the provided URL
     */
    public static Map<String, List<String>> getQueryParameters(String url) {
        return getQueryParameters(url, true);
    }

    /**
     * Get a multivalued map containing the query parameters for the provided URL
     *
     * <p>The keys and values of the multivalued map are URL decoded according the specified parameter
     *
     * @param url
     *     the URL
     * @param decode
     *     indicates whether to decode the keys and values of the multivalued map should be decoded
     * @return
     *     a multivalued map containing the query parameters for the provided URL
     */
    public static Map<String, List<String>> getQueryParameters(String url, boolean decode) {
        return parseQuery(getQuery(url), decode);
    }

    /**
     * Get a {@link URL} instance that represents the specified parameter
     *
     * @param url
     *     the url
     * @return
     *     a {@link URL} instance that represents the specified parameter
     * @see
     *     URL
     */
    public static URL getURL(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parse the provided path part into a List of path segments
     *
     * <p>The path segments are URL decoded
     *
     * @param path
     *     the path part
     * @return
     *     a list of path segments
     */
    public static List<String> parsePath(String path) {
        return parsePath(path, true);
    }

    /**
     * Parse the provided path part into a list of path segments
     *
     * <p>The path segments are decoded according to the specified parameter
     *
     * @param path
     *     the path part
     * @param decode
     *     indicates whether the path segments should be URL decoded
     * @return
     *     a list of path segments
     */
    public static List<String> parsePath(String path, boolean decode) {
        return Arrays.stream(path.split("/"))
            .skip(1)
            .map(s -> decode ? decode(s) : s)
            .collect(Collectors.toList());
    }

    /**
     * Parse the provided query part into a multivalued map of query parameters
     *
     * <p>The keys and values of the multivalued map are URL decoded
     *
     * @param query
     *     the query part
     * @return
     *     a multivalued map containing the query parameters for the provided URL
     *
     */
    public static Map<String, List<String>> parseQuery(String query) {
        return parseQuery(query, true);
    }

    /**
     * Parse the provided query part into a multivalued map of query parameters
     *
     * <p>The keys and values of the multivalued map are URL decoded according to the specified parameter
     *
     * @param query
     *     the query part
     * @param decode
     *     indicates whether to decode the keys and values of the multivalued map should be decoded
     * @return
     *     a multivalued map containing the query parameters for the provided URL
     *
     */
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
}
