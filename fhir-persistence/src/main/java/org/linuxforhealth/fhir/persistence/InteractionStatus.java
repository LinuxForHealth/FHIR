/*
 * (C) Copyright IBM Corp. 2021, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence;


/**
 * Describes what change, if any, was made to a resource at the database level and why
 */
public enum InteractionStatus {
    READ,                 // The resource was read
    MODIFIED,             // The resource was inserted or updated
    IF_NONE_MATCH_EXISTED // The resource existed and matched the If-None-Match value - was not updated
}