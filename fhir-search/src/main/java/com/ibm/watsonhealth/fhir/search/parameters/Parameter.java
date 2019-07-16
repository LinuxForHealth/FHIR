/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.parameters;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.search.SearchConstants;
import com.ibm.watsonhealth.fhir.search.SearchConstants.Modifier;
import com.ibm.watsonhealth.fhir.search.SearchConstants.Prefix;
import com.ibm.watsonhealth.fhir.search.SearchConstants.Type;

public class Parameter {
    
    private Type type = null;
    private String name = null;
    
    private Modifier modifier = null;
    private String modifierResourceTypeName = null;
    private List<ParameterValue> values = null;
    private Parameter nextParameter = null;
    private boolean isInclusionCriteria = false;
    
    public Parameter(Type type, String name, Modifier modifier, String modifierResourceTypeName) {
        this.type = type;
        this.name = name;
        this.modifier = modifier;
        this.modifierResourceTypeName = modifierResourceTypeName;
        values = new ArrayList<>();
    }
    
    public Parameter(Type type, String name, Modifier modifier, String modifierResourceTypeName, 
                     boolean isInclusionCriteria) {
        this(type, name, modifier, modifierResourceTypeName);
        this.isInclusionCriteria = isInclusionCriteria;
    }
    
    public Parameter(Type type, String name, Modifier modifier, String modifierResourceTypeName, 
                     List<ParameterValue> parmValues) {
        this(type, name, modifier, modifierResourceTypeName);
        this.values = parmValues;
    }
    
    public Type getType() {
        return type;
    }
    
    public String getName() {
        return name;
    }
    
    public Modifier getModifier() {
        return modifier;
    }
    
    public String getModifierResourceTypeName() {
        return modifierResourceTypeName;
    }
    
    public List<ParameterValue> getValues() {
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
        
        if (name != null) {
            buffer.append("name: ");
            buffer.append(name);
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
        
        List<ParameterValue> values = getValues();
        for (ParameterValue value : values) {
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

    public Parameter getNextParameter() {
        return nextParameter;
    }

    public void setNextParameter(Parameter nextParameter) {
        this.nextParameter = nextParameter;
    }
    
    /**
     * The returned value is intentionally not abstract. 
     * The order is important. 
     * 
     * @return
     */
    public LinkedList<Parameter> getChain() {
        
        LinkedList<Parameter> parameterChain = new LinkedList<>();
        Parameter currentParameter = this.getNextParameter();
        while(currentParameter != null) {
            parameterChain.addLast(currentParameter);
            currentParameter = currentParameter.getNextParameter();
        }
         
        return parameterChain;
    }

    public boolean isInclusionCriteria() {
        return isInclusionCriteria;
    }
}
