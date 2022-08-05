/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.jdbc.dao.api;


/**
 * Constants used by DAOs
 */
public class FHIRDAOConstants {

    // Thrown by the stored procedures when the resource version
    // does not match the next version in the database (due to
    // concurrent updates)
    public static final String SQLSTATE_WRONG_VERSION = "99001";
    
    // Thrown if the stored procedure detects data corruption
    // such as a row missing in XX_LOGICAL_RESOURCES
    public static final String SQLSTATE_CORRUPT_SCHEMA = "99002";
    
    // Thrown if the stored procedure finds an existing record
    // when If-None-Match is specified
    public static final String SQLSTATE_MATCHES = "99003";

    // Thrown if the procedure sees that the current record
    // is deleted and the request deletion flag is set
    public static final String SQLSTATE_CURRENTLY_DELETED = "99004";
}