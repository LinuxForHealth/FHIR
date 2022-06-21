/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.impl;


/**
 * A DTO representing a mapping of a resource and reference value. The
 * record is used to drive the population of the xx_ref_values tables
 */
public class ResourceReferenceValueRec extends ResourceRefRec {

    private final String refResourceType;
    private final int refResourceTypeId;

    // The external ref value and its normalized database id (when we have it)
    private final String refLogicalId;
    private Long refLogicalResourceId;
    private final Integer refVersionId;

    // Issue 1683 - optional composite id used to correlate parameters
    private final Integer compositeId;

    /**
     * Public constructor. Used to create a versioned reference record
     * @param parameterName
     * @param resourceType
     * @param resourceTypeId
     * @param logicalResourceId
     * @param refResourceType
     * @param refResourceTypeId
     * @param refLogicalId
     * @param refVersionId
     * @param compositeId
     */
    public ResourceReferenceValueRec(String parameterName, String resourceType, long resourceTypeId, long logicalResourceId,
        String refResourceType, int refResourceTypeId, String refLogicalId, Integer refVersionId, Integer compositeId) {
        super(parameterName, resourceType, resourceTypeId, logicalResourceId);
        this.refResourceType = refResourceType;
        this.refResourceTypeId = refResourceTypeId;
        this.refLogicalId = refLogicalId;
        this.refVersionId = refVersionId;
        this.compositeId = compositeId;
    }

    /**
     * Get the refLogicalResourceId
     * @return
     */
    public Long getRefLogicalResourceId() {
        return refLogicalResourceId;
    }

    /**
     * Sets the database id for the referenced logical resource
     * @param refLogicalResourceId to set
     */
    public void setRefLogicalResourceId(long refLogicalResourceId) {
        // because we're setting this, it can no longer be null
        this.refLogicalResourceId = refLogicalResourceId;
    }

    /**
     * @return the refVersionId
     */
    public Integer getRefVersionId() {
        return refVersionId;
    }

    /**
     * @return the compositeId
     */
    public Integer getCompositeId() {
        return compositeId;
    }
    /**
     * @return the refResourceType
     */
    public String getRefResourceType() {
        return refResourceType;
    }

    
    /**
     * @return the refLogicalId
     */
    public String getRefLogicalId() {
        return refLogicalId;
    }

    /**
     * @return the refResourceTypeId
     */
    public int getRefResourceTypeId() {
        return refResourceTypeId;
    }
}
