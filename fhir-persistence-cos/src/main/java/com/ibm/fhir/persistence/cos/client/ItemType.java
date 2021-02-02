/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.cos.client;


/**
 * The type of file/object we are processing
 */
public enum ItemType {
    FHIR_JSON
    // ,FHIR_XML // consider supporting XML in the future?
}
