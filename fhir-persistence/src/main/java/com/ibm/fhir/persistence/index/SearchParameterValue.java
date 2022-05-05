/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.index;


/**
 * The base class for our search parameter values. These index model classes
 * are designed to reflect the raw values we want the remote indexing
 * service to store
 */
public class SearchParameterValue {
    // The name of the parameter
    private String name;

    // The composite id used to tie together values belonging to the same composite parameter. Null for ordinary params.
    private Integer compositeId;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the compositeId
     */
    public Integer getCompositeId() {
        return compositeId;
    }

    /**
     * @param compositeId the compositeId to set
     */
    public void setCompositeId(Integer compositeId) {
        this.compositeId = compositeId;
    }
}
