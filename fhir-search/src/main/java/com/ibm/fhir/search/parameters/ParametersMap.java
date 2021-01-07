/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.search.parameters;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.fhir.model.resource.SearchParameter;

/**
 * A multi-key map that indexes a set of search parameters by SearchParameter.code and SearchParameter.url
 */
public class ParametersMap {
    private static final Logger log = Logger.getLogger(ParametersMap.class.getName());

    private final Map<String, Set<SearchParameter>> codeMap;
    private final Map<String, SearchParameter> urlMap;

    /**
     * Construct a ParametersMap from a Bundle of SearchParameter
     */
    public ParametersMap() {
        // LinkedHashMaps to preserve insertion order
        codeMap = new LinkedHashMap<>();
        urlMap = new LinkedHashMap<>();
    }

    public void insert(String code, SearchParameter parameter) {
        Objects.requireNonNull(code, "cannot insert a null code");
        Objects.requireNonNull(parameter, "cannot insert a null parameter");

        String url = parameter.getUrl().getValue();
        Set<SearchParameter> previousParams = codeMap.get(code);
        if (previousParams != null && previousParams.size() > 0) {
            if (log.isLoggable(Level.FINE)) {
                log.fine("SearchParameter with code '" + code + "' already exists; adding additional parameter with url '" + url + "'");
            }
        }
        codeMap.computeIfAbsent(code, k -> new HashSet<>()).add(parameter);

        if (urlMap.containsKey(url)) {
            SearchParameter previous = urlMap.get(url);
            if (previous.getExpression() == null || previous.getExpression().equals(parameter.getExpression())) {
                if (!code.equals(previous.getCode().getValue())) {
                    log.info("SearchParameter with url '" + url + "' already exists with the same expression; "
                            + "adding additional code '" + code + "'");
                }
            } else {
                log.warning("SearchParameter with url '" + url + "' already exists with a different expression;\n"
                        + "replacing [id=" + previous.getId() + ", expression=" + previous.getExpression().getValue()
                        + "] with [id=" + parameter.getId() + ", expression=" + parameter.getExpression().getValue() + "]");
            }
        }
        urlMap.put(url, parameter);
    }

    public void insertAll(ParametersMap map) {
        for (Entry<String, Set<SearchParameter>> entry : map.codeEntries()) {
            for (SearchParameter sp : entry.getValue()) {
                insert(entry.getKey(), sp);
            }
        }
    }

    public Set<SearchParameter> lookupByCode(String searchParameterCode) {
        return codeMap.get(searchParameterCode);
    }

    public SearchParameter lookupByUrl(String searchParameterUrl) {
        return urlMap.get(searchParameterUrl);
    }

    public Collection<SearchParameter> values() {
        return urlMap.values();
    }

    public boolean isEmpty() {
        return codeMap.isEmpty();
    }

    public int size() {
        return codeMap.size();
    }

    public Set<Entry<String, Set<SearchParameter>>> codeEntries() {
        return Collections.unmodifiableSet(codeMap.entrySet());
    }

    public Set<Entry<String, SearchParameter>> urlEntries() {
        return Collections.unmodifiableSet(urlMap.entrySet());
    }
}
