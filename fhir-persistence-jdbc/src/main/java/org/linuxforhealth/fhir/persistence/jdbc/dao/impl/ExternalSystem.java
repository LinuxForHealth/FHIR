/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.dao.impl;


/**
 * DTO representing an external_systems record
 */
public class ExternalSystem {

    private final long externalSystemId;
    
    private final String externalSystemName;
    
    public ExternalSystem(long externalSystemId, String externalSystemName) {
        this.externalSystemId = externalSystemId;
        this.externalSystemName = externalSystemName;
    }

    /**
     * @return the externalSystemId
     */
    public long getExternalSystemId() {
        return externalSystemId;
    }

    /**
     * @return the externalSystemName
     */
    public String getExternalSystemName() {
        return externalSystemName;
    }
}