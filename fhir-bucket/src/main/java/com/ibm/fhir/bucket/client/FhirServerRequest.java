/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.client;


/**
 * Interface for defining requests which can operate with a {@link FHIRBucketClient}
 */
public interface FhirServerRequest<T> {

    /**
     * Run the request using the given {@link FHIRBucketClient} and return the
     * type T.
     * @param client used to make the request
     * @return response transformed to type T
     */
    public T run(FHIRBucketClient client);
}
