/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.search.parameters;

import java.util.Collection;
import java.util.Collections;
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

    public static final String MISSING_EXPRESSION_WARNING = "Skipping parameter '%s' with missing expression";

    private final Map<String, SearchParameter> codeMap;
    private final Map<String, SearchParameter> canonicalMap;

    private final Map<String, SearchParameter> inclusionParamMap;

    /**
     * Construct a ParametersMap from a Bundle of SearchParameter
     */
    public ParametersMap() {
        // LinkedHashMaps to preserve insertion order
        codeMap = new LinkedHashMap<>();
        canonicalMap = new LinkedHashMap<>();

        // Inclusion parameters are stored separately because they may be internal-only
        // i.e. not externally searchable except through compartment search
        inclusionParamMap = new LinkedHashMap<>();
    }

    /**
     * @param code
     * @param parameter
     * @implSpec Any existing parameters will be replaced and a warning will be logged; last insert wins
     */
    public void insert(String code, SearchParameter parameter) {
        Objects.requireNonNull(code, "cannot insert a null code");
        Objects.requireNonNull(parameter, "cannot insert a null parameter");

        String url = parameter.getUrl().getValue();
        String version = (parameter.getVersion() == null) ? null : parameter.getVersion().getValue();
        String canonical = (version == null) ? url : url + "|" + version;

        if (parameter.getExpression() == null || !parameter.getExpression().hasValue()) {
            if (log.isLoggable(Level.FINE)) {
                log.fine(String.format(MISSING_EXPRESSION_WARNING, canonical));
            }
            return;
        }

        if (codeMap.containsKey(code)) {
            SearchParameter previous = codeMap.get(code);
            logParamConflict("with code '" + code + "'", parameter, canonical, previous);
        }
        codeMap.put(code, parameter);

        // for versioned search params, we store them in the canonicalMap twice:
        // once with their version and once without it
        if (canonicalMap.containsKey(url)) {
            SearchParameter previous = canonicalMap.get(url);
            logParamConflict("with url '" + url + "'", parameter, canonical, previous);
        }
        canonicalMap.put(url, parameter);

        if (version != null) {
            if (canonicalMap.containsKey(canonical)) {
                SearchParameter previous = canonicalMap.get(canonical);
                logParamConflict("'" + canonical + "'", parameter, canonical, previous);
            }
            canonicalMap.put(canonical, parameter);
        }
    }

    /**
     * @param code
     * @param parameter
     * @implSpec Any existing parameters will be replaced and a warning will be logged; last insert wins
     */
    public void insertInclusionParam(String code, SearchParameter parameter) {
        Objects.requireNonNull(code, "cannot insert a null code");
        Objects.requireNonNull(parameter, "cannot insert a null parameter");

        if (inclusionParamMap.containsKey(code)) {
            SearchParameter previous = inclusionParamMap.get(code);
            logParamConflict("inclusion criteria '" + code + "'", parameter, ParametersUtil.getCanonicalUrl(parameter), previous);
        }
        inclusionParamMap.put(code, parameter);
    }

    private void logParamConflict(String distinguisher, SearchParameter parameter, String canonical, SearchParameter previous) {
        if (previous.getExpression().equals(parameter.getExpression())) {
            if (log.isLoggable(Level.FINE)) {
                String thatCanonical = ParametersUtil.getCanonicalUrl(previous);
                log.fine("SearchParameter " + distinguisher + " exists with the same expression"
                        + "; use search parameter filtering to disambiguate.");
                log.fine("Replacing " + thatCanonical + " with " + canonical);
            }
        } else {
            String thatCanonical = ParametersUtil.getCanonicalUrl(previous);
            log.warning("SearchParameter " + distinguisher + " exists with a different expression" +
                    "; use search parameter filtering to disambiguate.");
            log.warning("Replacing " + thatCanonical + " [" + previous.getExpression().getValue() + "] with "
                    + canonical + " [" + parameter.getExpression().getValue() + "]");
        }
    }

    public void insertAll(ParametersMap map) {
        for (Entry<String, SearchParameter> entry : map.codeEntries()) {
            insert(entry.getKey(), entry.getValue());
        }
    }

    public Set<String> getCodes() {
        return codeMap.keySet();
    }

    public SearchParameter lookupByCode(String searchParameterCode) {
        return codeMap.get(searchParameterCode);
    }

    public SearchParameter lookupByCanonical(String searchParameterCanonical) {
        return canonicalMap.get(searchParameterCanonical);
    }

    public SearchParameter getInclusionParam(String searchParameterCode) {
        return inclusionParamMap.get(searchParameterCode);
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

    public Set<Entry<String, SearchParameter>> codeEntries() {
        return Collections.unmodifiableSet(codeMap.entrySet());
    }

    public Collection<SearchParameter> inclusionValues() {
        // use List to preserve order
        return Collections.unmodifiableList(inclusionParamMap.entrySet().stream()
                .filter(e -> !e.getKey().contains("|"))
                .map(e -> e.getValue())
                .collect(Collectors.toList()));
    }

    /**
     * @implSpec Note that versioned search parameters will be listed twice;
     *      once with their version and once without
     */
    public Set<Entry<String, SearchParameter>> canonicalEntries() {
        return Collections.unmodifiableSet(canonicalMap.entrySet());
    }
}
