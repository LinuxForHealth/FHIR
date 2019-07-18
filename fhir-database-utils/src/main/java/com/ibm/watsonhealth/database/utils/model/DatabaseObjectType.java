/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.database.utils.model;

/**
 * @author rarnold
 *
 */
public enum DatabaseObjectType {
    TABLE,
    INDEX,
    PROCEDURE,
    SEQUENCE,
    TYPE,
    PERMISSION,
    VARIABLE,
    NOP,
    GROUP,
    TABLESPACE
}
