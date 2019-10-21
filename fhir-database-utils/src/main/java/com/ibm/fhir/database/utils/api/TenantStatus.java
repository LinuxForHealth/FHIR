/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.api;

/**
 *
 */
public enum TenantStatus {
    PROVISIONING,
    FREE,
    ALLOCATED,
    FROZEN,
    DROPPED
}
