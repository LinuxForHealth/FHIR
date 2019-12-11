/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.parameters;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.search.SearchConstants;
import com.ibm.fhir.search.SearchConstants.Modifier;
import com.ibm.fhir.search.SearchConstants.Prefix;
import com.ibm.fhir.search.SearchConstants.Type;

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

    public boolean isComposite() {
        return false;
    }

    public boolean isChained() {
        return this.nextParameter != null && !this.isInclusionCriteria;
    }

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
            buffer.append(SearchConstants.NL);
        }

        if (modifierResourceTypeName != null) {
            buffer.append("modifierTypeResourceName: ");
            buffer.append(modifierResourceTypeName);
            buffer.append(SearchConstants.NL);
        }

        boolean composite = isComposite();
        buffer.append("composite: ");
        buffer.append(composite);
        buffer.append(SearchConstants.NL);

        boolean chained = isChained();
        buffer.append("chained: ");
        buffer.append(chained);
        buffer.append(SearchConstants.NL);

        boolean inclusionCriteria = this.isInclusionCriteria();
        buffer.append("inclusionCriteria: ");
        buffer.append(inclusionCriteria);
        buffer.append(SearchConstants.NL);

        List<QueryParameterValue> values = getValues();
        for (QueryParameterValue value : values) {
            Prefix prefix = value.getPrefix();
            if (prefix != null) {
                buffer.append("    prefix: ");
                buffer.append(prefix.value());
                buffer.append(SearchConstants.NL);
            }

            String valueString = value.getValueString();
            if (valueString != null) {
                buffer.append("    valueString: " + valueString);
                buffer.append(SearchConstants.NL);
            }

            DateTime valueDate = value.getValueDate();
            if (valueDate != null) {
                buffer.append("    valueDate: " + valueDate.getValue());
                buffer.append(SearchConstants.NL);
            }

            BigDecimal valueNumber = value.getValueNumber();
            if (valueNumber != null) {
                buffer.append("    valueNumber: " + valueNumber.toPlainString());
                buffer.append(SearchConstants.NL);
            }

            String valueSystem = value.getValueSystem();
            if (valueSystem != null) {
                buffer.append("    valueSystem: " + valueSystem);
                buffer.append(SearchConstants.NL);
            }

            String valueCode = value.getValueCode();
            if (valueCode != null) {
                buffer.append("    valueCode: " + valueCode);
                buffer.append(SearchConstants.NL);
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
     * @return
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
}
