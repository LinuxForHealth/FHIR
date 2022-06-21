/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.index;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Response from submitting IndexData to a FHIRIndexProvider implementation
 */
public class IndexProviderResponse {
    private final RemoteIndexData data;
    private final CompletableFuture<Void> ack;
    /**
     * Public constructor for a successful response
     * @param tenantId
     * @param data
     */
    public IndexProviderResponse(RemoteIndexData data, CompletableFuture<Void> ack) {
        this.data = data;
        this.ack = ack;
    }

    /**
     * Get the request data for which this is the response
     * @return
     */
    public RemoteIndexData getData() {
        return this.data;
    }

    /**
     * Get acknowledgement that the message was received by the service
     * we sent it to
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public void getAck() throws InterruptedException, ExecutionException {
        ack.get();
    }
}
