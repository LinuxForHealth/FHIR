/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc;

/**
 * Used to hold data accumulated by the JDBC persistence layer in the current
 * transaction. The data is persisted immediately prior to the transaction
 * being committed.
 */
public interface TransactionData {

    /**
     * Persist the data. Don't propagate any exceptions. If an exception occurs, log
     * the error and mark the transaction for rollback.
     */
    void persist();
}