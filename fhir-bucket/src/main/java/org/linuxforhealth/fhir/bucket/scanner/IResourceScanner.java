/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.bucket.scanner;


/**
 * A scanner active object which searches a repository looking for resources
 */
public interface IResourceScanner {
    
    /**
     * Start the scanner
     */
    void init();
    
    /**
     * Tells the scanner to stop
     */
    void signalStop();
    
    /**
     * Tell the main loop thread to stop if it hasn't already and wait a reasonable time
     * for the main thread loop to terminate
     */
    public void waitForStop();
}
