/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.impl;


/**
 * A DTO representing a mapping of a resource and token value. The
 * record is used to drive the population of the CODE_SYSTEMS, COMMON_TOKEN_VALUES
 * TOKEN_VALUES_MAP and <resource-type>_TOKEN_VALUES_MAP tables.
 */
public class ResourceProfileRec extends ResourceRefRec {

    // The external system name and its normalized database id (when we have it)
    private final String canonicalValue;
    private int canonicalValueId = -1;

    // The optional version value of the canonical uri
    private final String version;

    // The optional fragment of the canonical uri
    private final String fragment;

    // Is this a system-level search param?
    private final boolean systemLevel;

    /**
     * Public constructor
     * @param parameterName
     * @param resourceType
     * @param resourceTypeId
     * @param logicalResourceId
     * @param canonicalValue
     * @param version
     * @param fragment
     * @param systemLevel
     */
    public ResourceProfileRec(String parameterName, String resourceType, long resourceTypeId, long logicalResourceId,
        String canonicalValue, String version, String fragment, boolean systemLevel) {
        super(parameterName, resourceType, resourceTypeId, logicalResourceId);
        this.canonicalValue = canonicalValue;
        this.systemLevel = systemLevel;
        this.version = version;
        this.fragment = fragment;
    }

    /**
     * @return the codeSystemValue
     */
    public String getCanonicalValue() {
        return canonicalValue;
    }

    /**
     * Getter for the database id
     * @return
     */
    public int getCanonicalValueId() {
        return this.canonicalValueId;
    }

    /**
     * Sets the database id for the canonicalValue record.
     * @param canonicalValueId to set
     */
    public void setCanonicalValueId(int canonicalValueId) {
        // because we're setting this, it can no longer be null
        this.canonicalValueId = canonicalValueId;
    }

    /**
     * @return the systemLevel
     */
    public boolean isSystemLevel() {
        return systemLevel;
    }

    /**
     * Get the optional string version of the canonical uri
     * @return
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * Get the optional string fragment of the canonical uri
     * @return
     */
    public String getFragment() {
        return this.fragment;
    }
}