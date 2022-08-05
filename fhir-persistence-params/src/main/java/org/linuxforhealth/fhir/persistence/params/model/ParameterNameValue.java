/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.params.model;


/**
 * Represents a record in parameter_names for which we don't know
 * the parameter_name_id value, or which we need to create
 */
public class ParameterNameValue implements Comparable<ParameterNameValue> {
    private final String parameterName;
    private Integer parameterNameId;

    /**
     * Public constructor
     * @param parameterName
     */
    public ParameterNameValue(String parameterName) {
        this.parameterName = parameterName;
    }

    @Override
    public int compareTo(ParameterNameValue that) {
        return this.parameterName.compareTo(that.parameterName);
    }
    
    /**
     * @return the parameterName
     */
    public String getParameterName() {
        return parameterName;
    }

    /**
     * @return the parameterNameId
     */
    public Integer getParameterNameId() {
        return parameterNameId;
    }

    /**
     * @param parameterNameId the parameterNameId to set
     */
    public void setParameterNameId(Integer parameterNameId) {
        this.parameterNameId = parameterNameId;
    }

}
