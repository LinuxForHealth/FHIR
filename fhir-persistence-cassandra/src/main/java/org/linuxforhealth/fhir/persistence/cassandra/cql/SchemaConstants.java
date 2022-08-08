/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.cassandra.cql;

/**
 * Constants used in the Cassandra payload persistence schema
 */
public class SchemaConstants {

    // Break binary data into bite-sized pieces when storing
    public static final int CHUNK_SIZE = 1024 * 1024;

    public static final String RESOURCE_PAYLOADS = "resource_payloads";
    public static final String RESOURCE_VERSIONS = "resource_versions";
    public static final String PAYLOAD_CHUNKS = "payload_chunks";
    public static final String PAYLOAD_TRACKING = "payload_tracking";
    public static final String PAYLOAD_RECONCILIATION = "payload_reconciliation";
}