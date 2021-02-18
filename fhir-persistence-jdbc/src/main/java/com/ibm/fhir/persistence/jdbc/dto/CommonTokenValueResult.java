/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dto;


/**
 * DTO representing the result of a lookup on common token value id and the
 * corresponding code system id
 */
public class CommonTokenValueResult {
    private final int codeSystemId;
    private final long commonTokenValueId;

    /**
     * Public constructor
     * @param codeSystemId
     * @param commonTokenValueId
     */
    public CommonTokenValueResult(int codeSystemId, long commonTokenValueId) {
        this.codeSystemId = codeSystemId;
        this.commonTokenValueId = commonTokenValueId;
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