/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkimport;

import java.io.Serializable;

public class CheckPointData implements Serializable {
    private static final long serialVersionUID = 2189917861035732241L;

    private String importPartitionWorkitem;
    private int numOfLinesToSkip;

    public CheckPointData(String importPartitionWorkitem, int numOfLinesToSkip) {
        super();
        this.importPartitionWorkitem = importPartitionWorkitem;
        this.numOfLinesToSkip = numOfLinesToSkip;
    }

    public String getImportPartitionWorkitem() {
        return importPartitionWorkitem;
    }

    public void setImportPartitionWorkitem(String importPartitionWorkitem) {
        this.importPartitionWorkitem = importPartitionWorkitem;
    }

    public int getNumOfLinesToSkip() {
        return numOfLinesToSkip;
    }

    public void setNumOfLinesToSkip(int numOfLinesToSkip) {
        this.numOfLinesToSkip = numOfLinesToSkip;
    }

}
