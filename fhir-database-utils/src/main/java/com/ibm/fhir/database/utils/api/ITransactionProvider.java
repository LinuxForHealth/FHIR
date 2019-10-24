/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.api;

/**
 * Abstraction of the service providing access to instances of
 * {@link ITransaction} which themselves are used to hide the
 * underlying transaction implementation. This makes it easier
 * to code for JEE and non-JEE environments.
 */
public interface ITransactionProvider {

    /**
     * Obtain the transaction to use on this thread
     * @return
     */
    public ITransaction getTransaction();
}
