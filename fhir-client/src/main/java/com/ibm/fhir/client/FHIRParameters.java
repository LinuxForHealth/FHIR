/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.client;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import com.ibm.fhir.core.ResourceType;

/**
 * This class is used as a container for request parameters associated with a FHIR Client API request.
 */
public class FHIRParameters {

    /**
     * Constants which define common parameter names.
     */
    public static final String COUNT = "_count";
    public static final String FORMAT = "_format";
    public static final String PAGE = "_page";
    public static final String SINCE = "_since";

    /**
     * The valid set of modifiers that can be used when constructing the parameters for a search operation.
     * @implNote please keep in sync with {@link com.ibm.fhir.search.SearchConstants.Modifier}
     */
    public enum Modifier {
        MISSING("missing"),
        EXACT("exact"),
        CONTAINS("contains"),
        TEXT("text"),
        IN("in"),
        BELOW("below"),
        ABOVE("above"),
        NOT("not"),
        NOT_IN("not-in"),
        // For TYPE, the ResourceType class is used instead
        IDENTIFIER("identifier"),
        OF_TYPE("of-type"),
        ITERATE("iterate");

        private String text;

        private Modifier(String m) {
            this.text = m;
        }

        public String text() {
            return text;
        }
    }

    /**
     * The valid set of value prefixes that can be used when constructing the parameters for a search operation.
     * @implNote please keep in sync with {@link com.ibm.fhir.search.SearchConstants.Prefix}
     */
    public static enum ValuePrefix {
        EQ("eq"),
        NE("ne"),
        GT("gt"),
        LT("lt"),
        GE("ge"),
        LE("le"),
        SA("sa"),
        EB("eb"),
        AP("ap");

        private String text;

        private ValuePrefix(String p) {
            this.text = p;
        }

        public String text() {
            return text;
        }
    }

    private MultivaluedMap<String, String> parameters = new MultivaluedHashMap<>();

    public FHIRParameters() {
    }

    /**
     * Convenience method which sets the "_format" query parameter.
     *
     * @param mimeType
     *            the mimeType to use for the request
     * @return a handle to the FHIRParameters object
     */
    public FHIRParameters format(String mimeType) {
        return queryParam(FORMAT, mimeType);
    }

    /**
     * Convenience method which sets the "_count" query parameter.
     *
     * @param count
     *            the count value to use for the request
     * @return a handle to the FHIRParameters object
     */
    public FHIRParameters count(int count) {
        return queryParam(COUNT, Integer.toString(count));
    }

    /**
     * Convenience method which sets the "_page" query parameter.
     *
     * @param page
     *            the page number to use for the request
     * @return a handle to the FHIRParameters object
     */
    public FHIRParameters page(int page) {
        return queryParam(PAGE, Integer.toString(page));
    }

    /**
     * Convenience method which sets the "_since" query parameter.
     *
     * @param since
     *            a string representing the "since" date value to use for the request
     * @return a handle to the FHIRParameters object
     */
    public FHIRParameters since(String since) {
        return queryParam(SINCE, since);
    }

    /**
     * Clears the set of parameters currently contained in the FHIRParameters object.
     *
     * @return a handle to the FHIRParameters object
     */
    public FHIRParameters clear() {
        parameters.clear();
        return this;
    }

    /**
     * Adds the specified query parameter name and value to 'this'.
     *
     * @param name
     *            the parameter name
     * @param value
     *            the parameter value
     * @return a handle to the FHIRParameters object
     */
    public FHIRParameters queryParam(String name, String value) {
        addSinglevaluedParameter(name, value);
        return this;
    }

    /**
     * Adds the specified search parameter (name, modifier, values) to 'this'.
     *
     * @param name
     *            the parameter name
     * @param modifier
     *            a modifier (e.g. CONTAINS)
     * @param values
     *            one or more values associated with the search parameter
     * @return a handle to the FHIRParameters object
     */
    public FHIRParameters searchParam(String name, Modifier modifier, String... values) {
        String parameterName = name + ":" + modifier.text();
        String value = getValueString(values);
        addMultivaluedParameter(parameterName, value);
        return this;
    }

    /**
     * Adds the specified search parameter (name, resourceType, values) to 'this'.
     *
     * @param name
     *            the parameter name
     * @param resourceType
     *            a resource type (e.g. Patient) as a modifier
     * @param values
     *            one or more values associated with the search parameter
     * @return a handle to the FHIRParameters object
     */
    public FHIRParameters searchParam(String name, ResourceType resourceType, String... values) {
        String parameterName = name + ":" + resourceType.value();
        String value = getValueString(values);
        addMultivaluedParameter(parameterName, value);
        return this;
    }

    /**
     * Adds the specified search parameter (name, value-prefix, values) to 'this'.
     *
     * @param name
     *            the parameter name
     * @param prefix
     *            a value prefix (e.g. LE) to be applied to the values
     * @param values
     *            one or more values associated with the search parameter
     * @return a handle to the FHIRParameters object
     */
    public FHIRParameters searchParam(String name, ValuePrefix prefix, String... values) {
        String value = prefix.text() + getValueString(values);
        addMultivaluedParameter(name, value);
        return this;
    }

    /**
     * Returns a comma-separated string containing the elements of the 'values' array.
     *
     * @param values
     *            an array of strings containing parameter values
     */
    private String getValueString(String[] values) {
        StringBuilder sb = new StringBuilder();
        boolean needComma = false;
        for (int i = 0; i < values.length; i++) {
            if (needComma) {
                sb.append(",");
            }
            sb.append(values[i]);
            needComma = true;
        }
        return sb.toString();
    }

    /**
     * Adds the specified search parameter name and values to 'this'.
     *
     * @param name
     *            the name of the search parameter
     * @param values
     *            one or more values to be associated with the search parameter
     * @return handle to the FHIRParameters object
     */
    public FHIRParameters searchParam(String name, String... values) {
        for (int i = 0; i < values.length; i++) {
            addMultivaluedParameter(name, values[i]);
        }

        return this;
    }

    /**
     * Returns the collection of parameters that have been added to 'this'.
     *
     * @return a MultivalueMap where the key (query parameter name) maps to a list of string values
     */
    public final MultivaluedMap<String, String> getParameterMap() {
        return parameters;
    }

    /**
     * Retrieves the map containing 'this's parameters, creating if necessary.
     */
    private synchronized MultivaluedMap<String, String> getParameters() {
        if (parameters == null) {
            parameters = new MultivaluedHashMap<String, String>();
        }
        return parameters;
    }

    /**
     * Adds a multi-valued parameter to 'this'.
     */
    public void addMultivaluedParameter(String name, String value) {
        getParameters().add(name, value);
    }

    /**
     * Adds a single-valued parameter to 'this'.
     *
     * @param name
     * @param value
     */
    public void addSinglevaluedParameter(String name, String value) {
        getParameters().putSingle(name, value);
    }

    /**
     * This method returns the parameters contained in 'this' in the form of a query string.
     */
    public String queryString(boolean includeSeparator) {
        StringBuilder sb = new StringBuilder();
        if (getParameters() != null) {
            boolean needSeparator = includeSeparator;
            for (Map.Entry<String, List<String>> mapEntry : getParameters().entrySet()) {
                for (String value : mapEntry.getValue()) {
                    if (needSeparator) {
                        sb.append("&");
                    }
                    sb.append(mapEntry.getKey());
                    sb.append("=");
                    sb.append(value);
                    needSeparator = true;
                }
            }
        }
        return sb.toString();
    }

    public String queryString() {
        return queryString(true);
    }

    /**
     * This method returns a string representation of the FHIRParameters object.
     */
    @Override
    public String toString() {
        return "FHIRParameters[parameters=" + (getParameters() != null ? getParameters().toString() : "<null>" + "]");
    }
}
