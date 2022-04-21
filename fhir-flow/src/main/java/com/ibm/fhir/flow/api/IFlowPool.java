/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.flow.api;

import java.util.concurrent.CompletableFuture;

import com.ibm.fhir.flow.impl.FlowFetchResult;

/**
 * Read resources from the upstream system
 */
public interface IFlowPool {

    /**
     * Initiate a request to VREAD the resource identified by riv
     * @param riv identifies the resource to read, including its version
     * @return a CompletableFuture which will become ready when the resource vread completes
     */
    CompletableFuture<FlowFetchResult> requestResource(ResourceIdentifierVersion riv);
}
