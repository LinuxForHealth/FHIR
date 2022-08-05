/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.jdbc.dao.api;


/**
 * Represents a record in logical_resource_ident
 * @implNote no need to override hashCode or equals because logicalResourceId
 *           does not contribute to the identity of the record - it is just an
 *           attribute.
 */
public class LogicalResourceIdentValue extends LogicalResourceIdentKey {
    private Long logicalResourceId;

    /**
     * Public constructor
     * @param resourceTypeId
     * @param logicalId
     */
    public LogicalResourceIdentValue(int resourceTypeId, String logicalId) {
        super(resourceTypeId, logicalId);
    }

    /**
     * @return the logicalResourceId
     */
    public Long getLogicalResourceId() {
        return logicalResourceId;
    }

    /**
     * @param logicalResourceId the logicalResourceId to set
     */
    public void setLogicalResourceId(Long logicalResourceId) {
        this.logicalResourceId = logicalResourceId;
    }
}
