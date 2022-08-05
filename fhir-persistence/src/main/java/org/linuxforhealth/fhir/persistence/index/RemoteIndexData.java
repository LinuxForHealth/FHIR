/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.index;

/**
 * Supplier of index data
 */
public class RemoteIndexData {
    // The partition key used to route the data
    private final String partitionKey;

    // The data object holding all the extracted search parameters
    private final SearchParametersTransport searchParameters;

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append("partitionKey:[").append(partitionKey).append("]");
        result.append(" resource:[");
        result.append(searchParameters.getResourceType()).append("/").append(searchParameters.getLogicalId());
        result.append("]");
        return result.toString();
    }

    /**
     * Public constructor
     * @param partitionKey
     * @param searchParameters
     */
    public RemoteIndexData(String partitionKey, SearchParametersTransport searchParameters) {
        this.searchParameters = searchParameters;
        this.partitionKey = partitionKey;
    }

    /**
     * Get the search parameter block representing the data we want to send
     * to the remote indexing service
     * @return
     */
    public SearchParametersTransport getSearchParameters() {
        return this.searchParameters;
    }

    /**
     * Get the key used to select which partition we want to send to. Partitions
     * are important because we want to see the IndexData processed in order within
     * a particular partition
     * @return
     */
    public String getPartitionKey() {
        return this.partitionKey;
    }
}