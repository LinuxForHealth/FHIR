/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.core;

public interface TenantIdProvider {
    public static final TenantIdProvider DEFAULT = new TenantIdProvider() {
        @Override
        public String getTenantId() {
            return "default";
        }
    };
    String getTenantId();
}
