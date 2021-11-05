/*
 * (C) Copyright IBM Corp. 2019, 2021
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
import java.util.stream.Collectors;

import com.ibm.fhir.model.resource.SearchParameter;

/**
 * A multi-key map that indexes a set of search parameters by SearchParameter.code and
 * SearchParameter.url / canonical (url + "|" + version)
 */
public class ParametersMap {
    private static final Logger log = Logger.getLogger(ParametersMap.class.getName());

    private final Map<String, Set<SearchParameter>> codeMap;
    private final Map<String, SearchParameter> canonicalMap;

    /**
     * Construct a ParametersMap from a Bundle of SearchParameter
     */
    public ParametersMap() {
        // LinkedHashMaps to preserve insertion order
        codeMap = new LinkedHashMap<>();
        canonicalMap = new LinkedHashMap<>();
    }

    public void insert(String code, SearchParameter parameter) {
        Objects.requireNonNull(code, "cannot insert a null code");
        Objects.requireNonNull(parameter, "cannot insert a null parameter");

        String url = parameter.getUrl().getValue();
        String version = (parameter.getVersion() == null) ? null : parameter.getVersion().getValue();

        Set<SearchParameter> previousParams = codeMap.get(code);
        if (previousParams != null && previousParams.size() > 0) {
            if (log.isLoggable(Level.FINE)) {
                String canonical = (version == null) ? url : url + "|" + version;
                log.fine("SearchParameter with code '" + code + "' already exists; adding additional parameter '" + canonical + "'");
            }
        }
        codeMap.computeIfAbsent(code, k -> new HashSet<>()).add(parameter);

        // for versioned search params, we store them in the canonicalMap twice:
        // once with their version and once without it
        if (canonicalMap.containsKey(url)) {
            SearchParameter previous = canonicalMap.get(url);
            if (previous.getExpression() == null || previous.getExpression().equals(parameter.getExpression())) {
                if (!code.equals(previous.getCode().getValue())) {
                    log.info("SearchParameter '" + url + "' already exists with the same expression; "
                            + "adding additional code '" + code + "'");
                }
            } else {
                String thatVersion = (previous.getVersion() == null) ? null : previous.getVersion().getValue();
                log.info("SearchParameter '" + url + "' already exists with a different expression;\n"
                        + "replacing [id=" + previous.getId() + ", version=" + thatVersion + ", expression=" + previous.getExpression().getValue()
                        + "] with [id=" + parameter.getId() + ", version=" + version + ", expression=" + parameter.getExpression().getValue() + "]");
            }
        }
        canonicalMap.put(url, parameter);

        if (version != null) {
            String canonical = url + "|" + version;
            if (canonicalMap.containsKey(canonical)) {
                SearchParameter previous = canonicalMap.get(canonical);
                if (previous.getExpression() == null || previous.getExpression().equals(parameter.getExpression())) {
                    if (!code.equals(previous.getCode().getValue())) {
                        log.info("SearchParameter '" + canonical + "' already exists with the same expression; "
                                + "adding additional code '" + code + "'");
                    }
                } else {
                    log.warning("SearchParameter '" + canonical + "' already exists with a different expression;\n"
                            + "replacing [id=" + previous.getId() + ", expression=" + previous.getExpression().getValue()
                            + "] with [id=" + parameter.getId() + ", expression=" + parameter.getExpression().getValue() + "]");
                }
            }
            canonicalMap.put(canonical, parameter);
        }
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

    public SearchParameter lookupByCanonical(String searchParameterCanonical) {
        return canonicalMap.get(searchParameterCanonical);
    }

    public Collection<SearchParameter> values() {
        // use List to preserve order
        return Collections.unmodifiableList(canonicalMap.entrySet().stream()
                .filter(e -> !e.getKey().contains("|"))
                .map(e -> e.getValue())
                .collect(Collectors.toList()));
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

    /**
     * @implSpec Note that versioned search parameters will be listed twice;
     *      once with their version and once without
     */
    public Set<Entry<String, SearchParameter>> canonicalEntries() {
        return Collections.unmodifiableSet(canonicalMap.entrySet());
    }
}
