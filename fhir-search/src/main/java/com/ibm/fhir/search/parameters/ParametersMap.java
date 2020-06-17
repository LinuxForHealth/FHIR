/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.search.parameters;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.logging.Logger;

import com.ibm.fhir.model.resource.SearchParameter;

/**
 * A multi-key map that indexes a set of search parameters by SearchParameter.code and SearchParameter.url
 */
public class ParametersMap {
    private static final Logger log = Logger.getLogger(ParametersMap.class.getName());

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
    void insert(String code, SearchParameter parameter) {
        Objects.requireNonNull(code, "cannot insert a null code");
        Objects.requireNonNull(parameter, "cannot insert a null parameter");

        if (codeMap.containsKey(code)) {
            SearchParameter previous = codeMap.get(code);
            if (previous.getExpression() == null || previous.getExpression().equals(parameter.getExpression())) {
                log.info("SearchParameter with code '" + code + "' already exists with the same expression; "
                        + "adding additional url '" + previous.getUrl().getValue() + "'");
            } else {
                log.warning("SearchParameter with code '" + code + "' already exists with a different expression; "
                        + "replacing search parameter of id '" + previous.getId()
                        + "' with search parameter of id '" + parameter.getId() + "'");
            }
        }
        codeMap.put(code, parameter);

        String url = parameter.getUrl().getValue();
        if (urlMap.containsKey(url)) {
            SearchParameter previous = urlMap.get(url);
            if (previous.getExpression() == null || previous.getExpression().equals(parameter.getExpression())) {
                log.info("SearchParameter with url '" + url + "' already exists with the same expression; "
                        + "adding additional code '" + previous.getCode() + "'");
            } else {
                log.warning("SearchParameter with url '" + url + "' already exists with a different expression;"
                        + " replacing search parameter of id '" + previous.getId()
                        + "' with search parameter of id '" + parameter.getId() + "'");
            }
        }
        urlMap.put(url, parameter);
    }

    /**
     * @implSpec package-private to prevent insertion from outside the package
     */
    void insertAll(ParametersMap map) {
        for (Entry<String, SearchParameter> entry : map.codeMap.entrySet()) {
            insert(entry.getKey(), entry.getValue());
        }
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
