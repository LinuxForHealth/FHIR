/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.audit.cadf.model;

/**
 * Outcome reason. Provides additional information to describe the event outcome
 */
public final class CadfReason {
    private final String reasonType;
    private final String reasonCode;
    private final String policyType;
    private final String policyId;

    /**
     * Create a CADF Reason object
     * 
     * @param reasonType - String. The reason code domain URI. Must be present if
     *                   reasonCode is present.
     * @param reasonCode - String. Detailed result code as described by the domain
     *                   identifier (reason type). Must be specified if policyId is
     *                   not specified.
     * @param policyType - String. The policy domain URI. Must be present if
     *                   policyId is present.
     * @param policyId   - String. An optional identifier that indicates which
     *                   policy or algorithm was applied in order to achieve the
     *                   described OUTCOME. Must be specified if reasonCode is not
     *                   specified.
     */
    public CadfReason(String reasonType, String reasonCode, String policyType, String policyId) {
        this.reasonType = reasonType;
        this.reasonCode = reasonCode;
        this.policyId = policyId;
        this.policyType = policyType;
    }

    /**
     * Validate contents of the Reason type.
     * 
     * The logic is determined by the CADF specification.
     * 
     * @return This object for chaining.
     * @throws IllegalStateException when the properties do not meet the
     *                               specification.
     */
    protected CadfReason validate() throws IllegalStateException {
        if (policyId == null && reasonCode == null) {
            throw new IllegalStateException("at least one of: policyId, reasonCode must be specified");
        }
        if (reasonCode != null) {
            // it must not be empty, and reasonType must be specified and non-empty
            if (reasonCode.length() == 0) {
                throw new IllegalStateException("invalid reasonCode");
            }
            if (reasonType == null || reasonType.length() == 0) {
                throw new IllegalStateException("invalid reasonType");
            }
        }
        if (policyId != null) {
            // it must not be empty, and policyType must be specified and non-empty
            if (policyId.length() == 0) {
                throw new IllegalStateException("invalid policyId");
            }
            if (policyType == null || policyType.length() == 0) {
                throw new IllegalStateException("invalid policyType");
            }
        }
        return this;
    }

}
