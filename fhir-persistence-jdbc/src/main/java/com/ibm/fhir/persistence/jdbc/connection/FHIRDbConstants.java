/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.connection;


/**
 * Constants used with FHIR DB datasources and connections
 * 
 * @implNote extracted from FHIRDbDAO
 */
public class FHIRDbConstants {
    public static final String FHIRDB_JNDI_NAME_DEFAULT = "jdbc/fhirProxyDataSource";
    public static final String PROPERTY_DB_DRIVER = "dbDriverName";
    public static final String PROPERTY_DB_URL = "dbUrl";
    public static final String PROPERTY_DB_USER = "user";
    public static final String PROPERTY_DB_PSWD = "password";
    public static final String PROPERTY_SCHEMA_NAME = "schemaName";
}
