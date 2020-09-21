/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.impl;


/**
 * DTO base representing a reference from a resource to another resource (local or external)
 */
public class ResourceRef {

    // Id of the resource to which this reference belongs (originates from)
    private final long logicalResourceId;
    
    // The id of the parameter name representing this relationship
    private final int parameterNameId;
    
    public ResourceRef(long logicalResourceId, int parameterNameId) {
        this.logicalResourceId = logicalResourceId;
        this.parameterNameId = parameterNameId;
    }

    /**
     * @return the logicalResourceId
     */
    public long getLogicalResourceId() {
        return logicalResourceId;
    }

    /**
     * @return the parameterNameId
     */
    public int getParameterNameId() {
        return parameterNameId;
    }
}
