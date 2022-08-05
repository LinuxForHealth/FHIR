/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.bucket.api;


/**
 * Process a ResourceEntry
 */
@FunctionalInterface
public interface IResourceEntryProcessor {

    /**
     * Process the resource entry (usually called from within a thread-pool
     * @param re
     */
    void process(ResourceEntry re);
}
