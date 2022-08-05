/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.dto;


/**
 * DTO representing the result of a lookup on common token value id and the
 * corresponding code system id
 */
public class CommonTokenValueResult {
    private final String tokenValue;
    private final int codeSystemId;
    private final long commonTokenValueId;

    /**
     * Public constructor
     * @param tokenValue
     * @param codeSystemId
     * @param commonTokenValueId
     */
    public CommonTokenValueResult(String tokenValue, int codeSystemId, long commonTokenValueId) {
        this.tokenValue = tokenValue;
        this.codeSystemId = codeSystemId;
        this.commonTokenValueId = commonTokenValueId;
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
     * @return the codeSystemId
     */
    public int getCodeSystemId() {
        return codeSystemId;
    }
}