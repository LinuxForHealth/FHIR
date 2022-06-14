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
public class ResourceTokenValueRec extends ResourceRefRec {

    // The external system name and its normalized database id (when we have it)
    private final String codeSystemValue;
    private int codeSystemValueId = -1;

    // The external ref value and its normalized database id (when we have it)
    private final String tokenValue;
    private Long commonTokenValueId;

    // Issue 1683 - optional composite id used to correlate parameters
    private final Integer compositeId;

    // Is this a system-level search param?
    private final boolean systemLevel;

    /**
     * Public constructor.
     * @param parameterName
     * @param resourceType
     * @param resourceTypeId
     * @param logicalResourceId
     * @param externalSystemName
     * @param externalRefValue
     * @param compositeId
     * @param systemLevel
     */
    public ResourceTokenValueRec(String parameterName, String resourceType, long resourceTypeId, long logicalResourceId,
        String externalSystemName, String externalRefValue, Integer compositeId, boolean systemLevel) {
        super(parameterName, resourceType, resourceTypeId, logicalResourceId);
        this.codeSystemValue = externalSystemName;
        this.tokenValue = externalRefValue;
        this.compositeId = compositeId;
        this.systemLevel = systemLevel;
    }

    /**
     * @return the codeSystemValue
     */
    public String getCodeSystemValue() {
        return codeSystemValue;
    }

    /**
     * @return the codeSystemValueId
     */
    public int getCodeSystemValueId() {
        return codeSystemValueId;
    }

    /**
     * @param externalSystemNameId the externalSystemNameId to set
     */
    public void setCodeSystemValueId(int codeSystemValueId) {
        this.codeSystemValueId = codeSystemValueId;
    }

    /**
     * @return the tokenValue
     */
    public String getTokenValue() {
        return tokenValue;
    }

    /**
     * @return the commonTokenValueId, can be null if the value has not been set
     */
    public Long getCommonTokenValueId() {
        return commonTokenValueId;
    }

    /**
     * Sets the database id for the commonTokenValue record.
     * @param commontTokenValueId to set
     */
    public void setCommonTokenValueId(long commonTokenValueId) {
        // because we're setting this, it can no longer be null
        this.commonTokenValueId = commonTokenValueId;
    }

    /**
     * @return the compositeId
     */
    public Integer getCompositeId() {
        return compositeId;
    }

    /**
     * @return the systemLevel
     */
    public boolean isSystemLevel() {
        return systemLevel;
    }
}
