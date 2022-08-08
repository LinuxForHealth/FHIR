/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.search.parameters;

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

import org.linuxforhealth.fhir.model.resource.SearchParameter;

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
            logParamConflict("inclusion criteria '" + code + "'", parameter, ParametersHelper.getCanonicalUrl(parameter), previous);
        }
        inclusionParamMap.put(code, parameter);
    }

    private void logParamConflict(String distinguisher, SearchParameter parameter, String canonical, SearchParameter previous) {
        if (previous.getExpression().equals(parameter.getExpression())) {
            if (log.isLoggable(Level.FINE)) {
                String thatCanonical = ParametersHelper.getCanonicalUrl(previous);
                log.fine("SearchParameter " + distinguisher + " exists with the same expression"
                        + "; use search parameter filtering to disambiguate.");
                log.fine("Replacing " + thatCanonical + " with " + canonical);
            }
        } else {
            String thatCanonical = ParametersHelper.getCanonicalUrl(previous);
            log.warning("SearchParameter " + distinguisher + " exists with a different expression" +
                    "; use search parameter filtering to disambiguate.");
            log.warning("Replacing " + thatCanonical + " [" + previous.getExpression().getValue() + "] with "
                    + canonical + " [" + parameter.getExpression().getValue() + "]");
        }
    }

    /**
     * Get the set of SearchParameter codes that have been added to this map.
     * @return
     * @implSpec This does not include any compartment inclusion criteria codes added
     *      via {@link #insertInclusionParam(String, SearchParameter)};
     *      use {@link org.linuxforhealth.fhir.search.compartment.CompartmentHelper} for those.
     */
    public Set<String> getCodes() {
        return codeMap.keySet();
    }

    /**
     * Look up a search parameter that has been added to this map by its code.
     * @param searchParameterCode
     * @return null if it doesn't exist
     */
    public SearchParameter lookupByCode(String searchParameterCode) {
        return codeMap.get(searchParameterCode);
    }

    /**
     * Look up a search parameter that has been added to this map by its canonical URL.
     * @param searchParameterCanonical
     * @return null if it doesn't exist
     */
    public SearchParameter lookupByCanonical(String searchParameterCanonical) {
        return canonicalMap.get(searchParameterCanonical);
    }

    /**
     * Get a SearchParameter that has been added to this map as an inclusion parameter by its code.
     * @param searchParameterCode
     * @return null if it doesn't exist
     */
    public SearchParameter getInclusionParam(String searchParameterCode) {
        return inclusionParamMap.get(searchParameterCode);
    }

    /**
     * @return the set of search parameters added to this map
     * @implSpec this set does not include SearchParameters added to the map via
     *      {@link ParametersMap#insertInclusionParam(String, SearchParameter)};
     *      those can be obtained from {@link ParametersMap#inlcusionValues()}
     */
    public Collection<SearchParameter> values() {
        // use List to preserve order
        return Collections.unmodifiableList(canonicalMap.entrySet().stream()
                .filter(e -> !e.getKey().contains("|"))
                .map(e -> e.getValue())
                .collect(Collectors.toList()));
    }

    /**
     * @return the set of search parameters in the map, indexed by code
     * @implSpec this set does not include SearchParameters added to the map via
     *      {@link ParametersMap#insertInclusionParam(String, SearchParameter)}
     */
    public Set<Entry<String, SearchParameter>> codeEntries() {
        return Collections.unmodifiableSet(codeMap.entrySet());
    }

    /**
     * @return the set of compartment inclusion criteria parameter values
     * @implSpec this set may overlap with the set of "normal" SearchParameters
     *      that can be obtained from {@link #values()}
     */
    public Collection<SearchParameter> inclusionValues() {
        return inclusionParamMap.values();
    }

    /**
     * @implSpec Note that versioned search parameters will be listed twice;
     *      once with their version and once without
     */
    public Set<Entry<String, SearchParameter>> canonicalEntries() {
        return Collections.unmodifiableSet(canonicalMap.entrySet());
    }
}
