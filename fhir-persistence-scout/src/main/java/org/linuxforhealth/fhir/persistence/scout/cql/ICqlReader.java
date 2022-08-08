/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.scout.cql;


import com.datastax.oss.driver.api.core.CqlSession;

/**
 *
 */
public interface ICqlReader<T> {

    /**
     * Execute the statement using the connection and return the value
     * session connection to Cassandra
     */
    public T run(CqlSession session);
}
