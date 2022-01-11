/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.blob;

import com.azure.storage.blob.BlobContainerClient;

/**
 * A blob container managed by the BlobContainerManager
 */
public class BlobManagedContainer {
    private final BlobContainerClient client;
    private final BlobPropertyGroupAdapter properties;
    
    /**
     * Package protected constructor
     * @param client
     * @param properties
     */
    protected BlobManagedContainer(BlobContainerClient client, BlobPropertyGroupAdapter properties) {
        this.client = client;
        this.properties = properties;
    }

    /**
     * Get the client
     * @return
     */
    public BlobContainerClient getClient() {
        return this.client;
    }

    /**
     * Get the properties
     * @return
     */
    public BlobPropertyGroupAdapter getProperties() {
        return this.properties;
    }
}