/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.search.parameters;

import static org.linuxforhealth.fhir.search.SearchConstants.NL;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.linuxforhealth.fhir.search.SearchConstants;
import org.linuxforhealth.fhir.search.SearchConstants.Modifier;
import org.linuxforhealth.fhir.search.SearchConstants.Prefix;
import org.linuxforhealth.fhir.search.SearchConstants.Type;

/**
 * general type of parameter.
 */
public class QueryParameter {
    private Type type = null;
    private String code = null;

    private Modifier modifier = null;
    private String modifierResourceTypeName = null;
    private List<QueryParameterValue> values = null;
    private QueryParameter nextParameter = null;
    private boolean isInclusionCriteria = false;
    private boolean isReverseChained = false;
    private boolean isCanonical = false;

    public QueryParameter(Type type, String code, Modifier modifier, String modifierResourceTypeName) {
        this.type = type;
        this.code = code;
        this.modifier = modifier;
        this.modifierResourceTypeName = modifierResourceTypeName;
        values = new ArrayList<>();
    }

    public QueryParameter(Type type, String code, Modifier modifier, String modifierResourceTypeName, boolean isInclusionCriteria) {
        this(type, code, modifier, modifierResourceTypeName);
        this.isInclusionCriteria = isInclusionCriteria;
    }

    public QueryParameter(Type type, String code, Modifier modifier, String modifierResourceTypeName, boolean isInclusionCriteria, boolean isReverseChained) {
        this(type, code, modifier, modifierResourceTypeName, isInclusionCriteria);
        this.isReverseChained = isReverseChained;
    }

    public QueryParameter(Type type, String code, Modifier modifier, String modifierResourceTypeName, boolean isInclusionCriteria, boolean isReverseChained,
            boolean isCanonical) {
        this(type, code, modifier, modifierResourceTypeName, isInclusionCriteria, isReverseChained);
        this.isCanonical = isCanonical;
    }

    public QueryParameter(Type type, String code, Modifier modifier, String modifierResourceTypeName, List<QueryParameterValue> parmValues) {
        this(type, code, modifier, modifierResourceTypeName);
        this.values = parmValues;
    }

    public Type getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    public Modifier getModifier() {
        return modifier;
    }

    public String getModifierResourceTypeName() {
        return modifierResourceTypeName;
    }

    public List<QueryParameterValue> getValues() {
        return values;
    }

    public boolean isChained() {
        return this.nextParameter != null && !this.isInclusionCriteria;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();

        if (type != null) {
            buffer.append("type: ");
            buffer.append(type.value());
            buffer.append(SearchConstants.NL);
        }

        if (code != null) {
            buffer.append("code: ");
            buffer.append(code);
            buffer.append(SearchConstants.NL);
        }

        if (modifier != null) {
            buffer.append("modifier: ");
            buffer.append(modifier.value());
            buffer.append(NL);
        }

        if (modifierResourceTypeName != null) {
            buffer.append("modifierTypeResourceName: ");
            buffer.append(modifierResourceTypeName);
            buffer.append(NL);
        }

        boolean chained = isChained();
        buffer.append("chained: ");
        buffer.append(chained);
        buffer.append(NL);

        boolean reverseChained = isReverseChained();
        buffer.append("reverseChained: ");
        buffer.append(reverseChained);
        buffer.append(NL);

        boolean inclusionCriteria = this.isInclusionCriteria();
        buffer.append("inclusionCriteria: ");
        buffer.append(inclusionCriteria);
        buffer.append(NL);

        boolean canonical = this.isCanonical();
        buffer.append("canonical: ");
        buffer.append(canonical);
        buffer.append(NL);

        List<QueryParameterValue> values = getValues();
        for (QueryParameterValue value : values) {
            Prefix prefix = value.getPrefix();
            if (prefix != null) {
                buffer.append("    prefix: ");
                buffer.append(prefix.value());
                buffer.append(NL);
            }

            String valueString = value.getValueString();
            if (valueString != null) {
                buffer.append("    valueString: " + valueString);
                buffer.append(NL);
            }

            BigDecimal valueNumber = value.getValueNumber();
            if (valueNumber != null) {
                buffer.append("    valueNumber: " + valueNumber.toPlainString());
                buffer.append(NL);
            }

            String valueSystem = value.getValueSystem();
            if (valueSystem != null) {
                buffer.append("    valueSystem: " + valueSystem);
                buffer.append(NL);
            }

            String valueCode = value.getValueCode();
            if (valueCode != null) {
                buffer.append("    valueCode: " + valueCode);
                buffer.append(NL);
            }
        }

        return buffer.toString();
    }

    public QueryParameter getNextParameter() {
        return nextParameter;
    }

    public void setNextParameter(QueryParameter nextParameter) {
        this.nextParameter = nextParameter;
    }

    /**
     * The returned value is intentionally not abstract. The order is important.
     *
     * @return A non-null linked list of parameters that starts from the nextParameter.
     */
    public LinkedList<QueryParameter> getChain() {

        LinkedList<QueryParameter> parameterChain = new LinkedList<>();
        QueryParameter currentParameter = this.getNextParameter();
        while (currentParameter != null) {
            parameterChain.addLast(currentParameter);
            currentParameter = currentParameter.getNextParameter();
        }

        return parameterChain;
    }

    public boolean isInclusionCriteria() {
        return isInclusionCriteria;
    }

    public boolean isReverseChained() {
        return isReverseChained;
    }

    public boolean isCanonical() {
        return isCanonical;
    }
}
