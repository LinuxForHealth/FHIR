/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.api;

/**
 * Tenant Status Enumeration
 */
public enum TenantStatus {
    PROVISIONING,
    FREE,
    ALLOCATED,
    FROZEN,
    DROPPED
}