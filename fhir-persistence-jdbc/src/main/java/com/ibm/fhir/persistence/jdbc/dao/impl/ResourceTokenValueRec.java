/*
 * (C) Copyright IBM Corp. 2020
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
    private long commonTokenValueId = -1;

    public ResourceTokenValueRec(int parameterNameId, String resourceType, long resourceTypeId, long logicalResourceId,
        String externalSystemName, String externalRefValue) {
        super(parameterNameId, resourceType, resourceTypeId, logicalResourceId);
        this.codeSystemValue = externalSystemName;
        this.tokenValue = externalRefValue;
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
     * @return the commonTokenValueId
     */
    public long getCommonTokenValueId() {
        return commonTokenValueId;
    }

    /**
     * @param externalRefValueId the externalRefValueId to set
     */
    public void setCommonTokenValueId(long commonTokenValueId) {
        this.commonTokenValueId = commonTokenValueId;
    }
}
