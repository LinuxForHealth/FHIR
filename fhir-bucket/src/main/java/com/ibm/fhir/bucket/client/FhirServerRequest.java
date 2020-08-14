/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.client;


/**
 * Interface for defining requests which can operate with a {@link FhirClient}
 */
public interface FhirServerRequest<T> {

    /**
     * Run the request using the given {@link FhirClient} and return the
     * type T.
     * @param client used to make the request
     * @return response transformed to type T
     */
    public T run(FhirClient client);
}
