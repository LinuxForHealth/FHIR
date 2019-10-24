/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.model;

/**
 * Basic set of privileges that can be granted to a database object
 */
public enum Privilege {

    SELECT,
    INSERT,
    UPDATE,
    DELETE,
    READ,
    WRITE,
    EXECUTE,
    USAGE
}
