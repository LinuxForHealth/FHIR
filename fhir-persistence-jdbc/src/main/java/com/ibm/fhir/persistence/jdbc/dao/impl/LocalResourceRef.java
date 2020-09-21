/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.impl;


/**
 * represents a reference from a logical resource to another logical resource
 * within the same server
 */
public class LocalResourceRef extends ResourceRef {

    // The logical_resource_id of the referenced resource
    private final long refLogicalResourceId;
    
    /**
     * Public constructor
     * @param fromLogicalResourceId
     */
    public LocalResourceRef(long fromLogicalResourceId, int parameterNameId, long refLogicalResourceId) {
        super(fromLogicalResourceId, parameterNameId);
        this.refLogicalResourceId = refLogicalResourceId;
    }

    /**
     * @return the refResourceTypeId
     */
    public long getRefLogicalResourceId() {
        return refLogicalResourceId;
    }
}