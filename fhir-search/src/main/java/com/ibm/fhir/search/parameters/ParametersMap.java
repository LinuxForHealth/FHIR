/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.search.parameters;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.ibm.fhir.model.resource.SearchParameter;

/**
 * A object for storing multiple on a common list of SearchParameter 
 */
public class ParametersMap {
    private final Map<String, SearchParameter> codeMap;
    private final Map<String, SearchParameter> urlMap;

    /**
     * Construct a ParametersMap from a Bunlde of SearchParameter
     */
    public ParametersMap() {
        // LinkedHashMaps to preserve insertion order
        codeMap = new LinkedHashMap<>();
        urlMap = new LinkedHashMap<>();
    }

    /**
     * @implSpec package-private to prevent insertion from outside the package
     */
    void insert(String code, String url, SearchParameter parameter) {
        codeMap.put(code, parameter);
        urlMap.put(url, parameter);
    }

    /**
     * @implSpec package-private to prevent insertion from outside the package
     */
    void insertAll(ParametersMap map) {
        codeMap.putAll(map.codeMap);
        urlMap.putAll(map.urlMap);
    }

    public SearchParameter lookupByCode(String searchParameterCode) {
        return codeMap.get(searchParameterCode);
    }

    public SearchParameter lookupByUrl(String searchParameterUrl) {
        return urlMap.get(searchParameterUrl);
    }

    public Collection<SearchParameter> values() {
        return codeMap.values();
    }

    public boolean isEmpty() {
        return codeMap.isEmpty();
    }

    public int size() {
        return codeMap.size();
    }
}
