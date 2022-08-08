/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.api;

/**
 * Tenant Status Enumeration
 */
public enum TenantStatus {
    PROVISIONING, // in the process of adding the partitions to the schema
    FREE,         // unused tenant, available for allocation (e.g. tenant pooling)
    ALLOCATED,    // allocated and in use
    FROZEN,       // about to be dropped
    DROPPED       // all tenant resources have been release
}
