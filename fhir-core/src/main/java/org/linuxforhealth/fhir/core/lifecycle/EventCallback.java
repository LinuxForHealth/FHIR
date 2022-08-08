/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.core.lifecycle;

/**
 * Callback used to receive server startup and shutdown events
 */
public interface EventCallback {

    /**
     * Called after the server startup processing is done and all services are ready
     */
    public void serverReady();

    /**
     * Called at the start of shutdown. Must not block. This method is called for
     * all registered EventCallback objects before finalShutdown() is called.
     * Implementations may initiate, but not wait for, shutdown of the
     * services they manage.
     */
    public void startShutdown();

    /**
     * Called after all startShutdown() calls have been processed. Implementations should
     * terminate internal services immediately at this point. Waiting for termination should
     * be kept to a minimum, unless critical for stability.
     */
    public void finalShutdown();
}