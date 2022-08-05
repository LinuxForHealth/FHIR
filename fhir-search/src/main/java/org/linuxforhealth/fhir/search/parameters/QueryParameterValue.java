/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.search.parameters;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.linuxforhealth.fhir.search.SearchConstants;
import org.linuxforhealth.fhir.search.SearchConstants.Prefix;

/**
 * A search parameter value for a given search parameter that was passed in a search query
 */
public class QueryParameterValue {

    private boolean hidden = false;
    private boolean ofTypeModifier = false;

    private Prefix prefix = null;

    private String valueString = null;

    private String valueDate = null;
    private Instant valueDateLowerBound = null;
    private Instant valueDateUpperBound = null;

    // Used for number and quantity search parameters
    private BigDecimal valueNumber = null;

    // Used for quantity and token search parameters
    private String valueSystem = null;
    private String valueCode = null;

    // Used for composite search parameters
    private List<QueryParameter> component = new ArrayList<>();

    public QueryParameterValue() {
        // No Operation
    }

    public void setPrefix(Prefix prefix) {
        this.prefix = prefix;
    }

    public QueryParameterValue withPrefix(Prefix prefix) {
        setPrefix(prefix);
        return this;
    }

    public Prefix getPrefix() {
        return prefix;
    }

    public String getValueString() {
        return valueString;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
    }

    public BigDecimal getValueNumber() {
        return valueNumber;
    }

    public void setValueNumber(BigDecimal valueNumber) {
        this.valueNumber = valueNumber;
    }

    /**
     * @implSpec if null, it means that no system was specified for this query parameter.
     *      if empty, it means that the query should look for tokens with no system.
     */
    public String getValueSystem() {
        return valueSystem;
    }

    /**
     * @implSpec if null, it means that no system was specified for this query parameter.
     *      if empty, it means that the query should look for tokens with no system.
     */
    public void setValueSystem(String valueSystem) {
        this.valueSystem = valueSystem;
    }

    public String getValueCode() {
        return valueCode;
    }

    public void setValueCode(String valueCode) {
        this.valueCode = valueCode;
    }

    public String getValueDate() {
        return valueDate;
    }

    public void setValueDate(String valueDate) {
        this.valueDate = valueDate;
    }

    public Instant getValueDateLowerBound() {
        return valueDateLowerBound;
    }

    public void setValueDateLowerBound(Instant valueDateLowerBound) {
        this.valueDateLowerBound = valueDateLowerBound;
    }

    public Instant getValueDateUpperBound() {
        return valueDateUpperBound;
    }

    public void setValueDateUpperBound(Instant valueDateUpperBound) {
        this.valueDateUpperBound = valueDateUpperBound;
    }

    /**
     * @return the component
     */
    public List<QueryParameter> getComponent() {
        return component;
    }

    /**
     * @param component the components to add
     */
    public void addComponent(QueryParameter... component) {
        for (QueryParameter c : component) {
            this.component.add(c);
        }
    }

    /**
     * @param component the component to set
     */
    public void setComponent(Collection<QueryParameter> component) {
        this.component = new ArrayList<>(component);
    }

    /**
     * @return the hidden
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * @param b
     */
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    /**
     * Gets whether the value is of an :of-type modifier.
     * @return true or false
     */
    public boolean isOfTypeModifier() {
        return ofTypeModifier;
    }

    /**
     * Sets whether the value is of an :of-type modifier.
     * @param ofTypeModifier true if value is of an :of-type modifier, otherwise false
     */
    public void setOfTypeModifier(boolean ofTypeModifier) {
        this.ofTypeModifier = ofTypeModifier;
    }

    /**
     * Serialize the ParameterValue to a query parameter string
     */
    @Override
    public String toString() {
        StringBuilder returnString = new StringBuilder();

        if (prefix != null) {
            returnString.append(prefix.value());
        }

        // The delimiter starts off as EMPTY and goes to SearchConstants.PARAMETER_DELIMETER
        String delim = "";

        // Order is important; currently this ordering works for all search parameter types
        // quantity: [prefix]number|system|code
        // token: system|code
        // string/uri/reference: string
        // number: [prefix]number
        // date: [prefix]date

        // token search like "|code" is supported by setting the valueSystem to "" (NO_SYSTEM)
        delim = outputBuilder(returnString, delim, valueNumber);
        delim = outputBuilder(returnString, delim, valueSystem);
        delim = outputBuilder(returnString, delim, valueCode);
        delim = outputBuilder(returnString, delim, valueString);
        delim = outputBuilder(returnString, delim, valueDate);

        // Special handling required for token search of form "system|". In that case, valueSystem
        // will not be null, but all other values will be null, so the above processing will
        // not append the delimiter. Check if we have that case, and if so, append the delimiter.
        if (valueSystem != null && valueNumber == null && valueCode == null && valueString == null && valueDate == null) {
            returnString.append(SearchConstants.PARAMETER_DELIMITER);
        }

        // token search with :of-type modifier is handled internally as a composite search
        if (component != null && !component.isEmpty()) {
            String componentDelim = "";
            for (QueryParameter componentParam : component) {
                List<QueryParameterValue> componentValues = componentParam.getValues();
                if (componentValues.size() == 0) {
                    // It is possible that there will be no component value in the case of a
                    // composite parm with :missing modifier, so just ignore and continue.
                    continue;
                } else if (componentValues.size() > 1) {
                    throw new IllegalStateException("Components of a composite search parameter may only have a single value");
                }
                returnString.append(componentDelim).append(componentValues.get(0));
                // Since the self/next/previous links generated by UriBuilder for the search response Bundle use this output,
                // this needs to use "|" instead of "$" as the component delimiter
                componentDelim = ofTypeModifier ? SearchConstants.PARAMETER_DELIMITER : SearchConstants.COMPOSITE_DELIMITER;
            }
        }
        return returnString.toString();
    }

    /**
     * Simple build method to apply consistent usage of StringBuilder.
     *
     * @param outputBuilder the output builder to update
     * @param delim the delimiter
     * @param value the value
     * @return the updated delimiter
     */
    private String outputBuilder(StringBuilder outputBuilder, String delim, Object value) {
        if (value != null) {
            outputBuilder.append(delim);
            outputBuilder.append(value);
            delim = SearchConstants.PARAMETER_DELIMITER;
        }
        return delim;
    }
}