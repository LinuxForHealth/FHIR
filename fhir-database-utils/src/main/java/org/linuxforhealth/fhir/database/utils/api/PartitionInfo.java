/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.api;

/**
 * DTO for pertinent data from SYSCAT.DATAPARTITIONS
 */
public class PartitionInfo {
    
    private String tableName;
    private String dataPartitionName;
    private int seqno;
    private String lowValue;
    private boolean lowInclusive;
    private String highValue;
    private boolean highInclusive;
    
    /**
     * @return the seqno
     */
    public int getSeqno() {
        return seqno;
    }
    /**
     * @param seqno the seqno to set
     */
    public void setSeqno(int seqno) {
        this.seqno = seqno;
    }
    /**
     * @return the lowValue
     */
    public String getLowValue() {
        return lowValue;
    }
    /**
     * @param lowValue the lowValue to set
     */
    public void setLowValue(String lowValue) {
        this.lowValue = lowValue;
    }
    /**
     * @return the lowInclusive
     */
    public boolean isLowInclusive() {
        return lowInclusive;
    }
    /**
     * @param lowInclusive the lowInclusive to set
     */
    public void setLowInclusive(boolean lowInclusive) {
        this.lowInclusive = lowInclusive;
    }
    /**
     * @return the highValue
     */
    public String getHighValue() {
        return highValue;
    }
    /**
     * @param highValue the highValue to set
     */
    public void setHighValue(String highValue) {
        this.highValue = highValue;
    }
    /**
     * @return the highInclusive
     */
    public boolean isHighInclusive() {
        return highInclusive;
    }
    /**
     * @param highInclusive the highInclusive to set
     */
    public void setHighInclusive(boolean highInclusive) {
        this.highInclusive = highInclusive;
    }
    /**
     * @return the dataPartitionName
     */
    public String getDataPartitionName() {
        return dataPartitionName;
    }
    /**
     * @param dataPartitionName the dataPartitionName to set
     */
    public void setDataPartitionName(String dataPartitionName) {
        this.dataPartitionName = dataPartitionName;
    }
    public String getTableName() {
        return tableName;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
