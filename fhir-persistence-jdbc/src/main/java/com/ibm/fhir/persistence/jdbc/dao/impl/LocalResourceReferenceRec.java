/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.impl;


/**
 * A DTO representing an external resource reference. This object is initialized
 * with names which are resolved to ids when passed through the cache. The idea
 * is to reduce the number of times we have to iterate over the list of records
 */
public class LocalResourceReferenceRec extends ResourceRefRec {

    // The loca ref resource type
    private final String refResourceType;
    private final int refResourceTypeId;
    
    // The local ref value and its normalized database id (when we have it)
    private final String refLogicalId;
    private long refLogicalResourceId = -1;

    public LocalResourceReferenceRec(int parameterNameId, String resourceType, long resourceTypeId, String logicalId,
        String refResourceType, int refResourceTypeId, String refLogicalId) {
        super(parameterNameId, resourceType, resourceTypeId, logicalId);
        this.refResourceType = refResourceType;
        this.refResourceTypeId = refResourceTypeId;
        this.refLogicalId = refLogicalId;
    }
    
    /**
     * @return the refLogicalResourceId
     */
    public long getRefLogicalResourceId() {
        return refLogicalResourceId;
    }
    
    /**
     * @param refLogicalResourceId the refLogicalResourceId to set
     */
    public void setRefLogicalResourceId(long refLogicalResourceId) {
        this.refLogicalResourceId = refLogicalResourceId;
    }
    
    /**
     * @return the refResourceType
     */
    public String getRefResourceType() {
        return refResourceType;
    }
    
    /**
     * @return the refResourceTypeId
     */
    public int getRefResourceTypeId() {
        return refResourceTypeId;
    }
    
    /**
     * @return the refLogicalId
     */
    public String getRefLogicalId() {
        return refLogicalId;
    }
}
