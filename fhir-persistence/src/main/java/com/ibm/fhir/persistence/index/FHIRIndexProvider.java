/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.index;

import java.util.concurrent.CompletableFuture;

/**
 * Interface to support dispatching of resource parameter blocks to another
 * service for offline processing.
 */
public interface FHIRIndexProvider {

    /**
     * Submit the index data request to the async indexing service we represent
     * @param data
     * @return A CompletableFuture which completes when the request is acknowledged to have been received by the async service
     */
    CompletableFuture<IndexProviderResponse> submit(RemoteIndexData data);
}
