/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.api;


/**
 * Represents an item found in a COS bucket
 */
public class CosItem {
    // The name of the bucket holding the item
    private final String bucketName;

    // The name (key) of the object in the bucket
    private final String itemName;
    
    // size of the object in bytes
    private final long size;
    
    public CosItem(String bucketName, String itemName, long size) {
        this.bucketName = bucketName;
        this.itemName = itemName;
        this.size = size;
    }
    
    @Override
    public String toString() {
        return bucketName + ":" + itemName + "[" + size + "]";
    }

    
    /**
     * @return the bucketName
     */
    public String getBucketName() {
        return bucketName;
    }

    
    /**
     * @return the itemName
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * @return the size
     */
    public long getSize() {
        return this.size;
    }
}
