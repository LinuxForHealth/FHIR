/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.impl;


/**
 * A DTO representing a record in the external_system_names table
 */
public class ExternalSystemNameRec {

    private final int externalSystemNameId;
    
    private final String externalSystemName;

    public ExternalSystemNameRec(int externalSystemNameId, String externalSystemName) {
        this.externalSystemNameId = externalSystemNameId;
        this.externalSystemName = externalSystemName;
    }

    /**
     * @return the externalSystemNameId
     */
    public int getExternalSystemNameId() {
        return externalSystemNameId;
    }

    /**
     * @return the externalSystemName
     */
    public String getExternalSystemName() {
        return externalSystemName;
    }
}
