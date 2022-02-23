/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.blob;

import com.azure.storage.blob.BlobContainerAsyncClient;

/**
 * A blob container managed by the BlobContainerManager
 */
public class BlobManagedContainer {
    private final BlobContainerAsyncClient client;
    private final BlobPropertyGroupAdapter properties;
    
    /**
     * Package protected constructor
     * @param client
     * @param properties
     */
    protected BlobManagedContainer(BlobContainerAsyncClient client, BlobPropertyGroupAdapter properties) {
        this.client = client;
        this.properties = properties;
    }

    /**
     * Get the client
     * @return
     */
    public BlobContainerAsyncClient getClient() {
        return this.client;
    }

    /**
     * Get the properties
     * @return
     */
    public BlobPropertyGroupAdapter getProperties() {
        return this.properties;
    }

    /**
     * @return the containerName property value
     */
    public String getContainerName() {
        return properties.getContainerName();
    }
}