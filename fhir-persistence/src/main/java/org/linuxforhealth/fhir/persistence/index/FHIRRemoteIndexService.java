/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.index;

import org.linuxforhealth.fhir.config.FHIRRequestContext;

/**
 * Service interface to support shipping resource search parameter values to an
 * external service where they can be loaded into the database asynchronously.
 * Implementations are expected to be tenant aware. They must use the tenantId
 * value from the current {@link FHIRRequestContext}
 */
public abstract class FHIRRemoteIndexService {

    // For now we just publish this as a static service
    // TODO we should be injecting these services to something like the request context
    private static FHIRRemoteIndexService serviceInstance;

    /**
     * Initialize the serviceInstance value
     * @param instance
     */
    public static void setServiceInstance(FHIRRemoteIndexService instance) {
        serviceInstance = instance;
    }

    /**
     * Get the serviceInstance value
     * @return
     */
    public static FHIRRemoteIndexService getServiceInstance() {
        return serviceInstance;
    }

    /**
     * Submit the index data request to the async indexing service we represent
     * @implNote implementations must use tenantId from {@link FHIRRequestContext}
     * @param data
     * @return A wrapper for a CompletableFuture which completes when the request is acknowledged to have been received by the async service
     */
    public abstract IndexProviderResponse submit(RemoteIndexData data);
}
