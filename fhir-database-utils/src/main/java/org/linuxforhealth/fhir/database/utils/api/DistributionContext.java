/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.database.utils.api;


/**
 * Carrier for the distribution context passed to some adapter methods
 */
public class DistributionContext {
    // The type of distribution to be applied for a particular table
    private final DistributionType distributionType;
    // The column name to be used for distribution when the distributionType is DISTRIBUTED
    private final String distributionColumnName;

    /**
     * Public constructor
     * @param distributionType
     * @param distributionColumnName
     */
    public DistributionContext(DistributionType distributionType, String distributionColumnName) {
        this.distributionType = distributionType;
        this.distributionColumnName = distributionColumnName;
    }

    /**
     * @return the distributionType
     */
    public DistributionType getDistributionType() {
        return distributionType;
    }

    /**
     * @return the distributionColumnName
     */
    public String getDistributionColumnName() {
        return distributionColumnName;
    }
}
