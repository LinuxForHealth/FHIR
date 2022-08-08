/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.dao.impl;


/**
 * DTO representing an external_reference_values record
 */
public class ExternalReferenceValue {

    private final long externalReferenceValueId;
    
    private final String externalReferenceValue;
    
    public ExternalReferenceValue(long externalReferenceValueId, String externalReferenceValue) {
        this.externalReferenceValueId = externalReferenceValueId;
        this.externalReferenceValue = externalReferenceValue;
    }

    /**
     * @return the externalReferenceValueId
     */
    public long getExternalReferenceValueId() {
        return externalReferenceValueId;
    }

    /**
     * @return the externalReferenceValue
     */
    public String getExternalReferenceValue() {
        return externalReferenceValue;
    }
}