/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.remote.index.database;

import java.util.Objects;

/**
 * Key used to uniquely identify a logical resource
 */
public class LogicalResourceIdentKey {
    private final String resourceType;
    private final String logicalId;

    /**
     * Canonical constructor
     * @param resourceType
     * @param logicalId
     */
    public LogicalResourceIdentKey(String resourceType, String logicalId) {
        this.resourceType = resourceType;
        this.logicalId = logicalId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(resourceType, logicalId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LogicalResourceIdentKey) {
            LogicalResourceIdentKey that = (LogicalResourceIdentKey)obj;
            return this.resourceType.equals(that.resourceType)
                    && this.logicalId.equals(that.logicalId);
        }
        return false;
    }
}
