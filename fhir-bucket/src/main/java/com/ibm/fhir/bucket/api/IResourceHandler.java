/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.api;


/**
 * Interface for handling the processing of resources read from COS
 */
public interface IResourceHandler {

    /**
     * Add the resource entry to the thread-pool for processing, subject to the
     * rate limiting we have to make sure memory consumption is kept in check
     * @param entry
     * @return
     */
    public boolean process(ResourceEntry entry);

}
