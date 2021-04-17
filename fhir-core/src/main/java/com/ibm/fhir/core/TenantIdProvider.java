/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.core;

import java.util.ServiceLoader;

public interface TenantIdProvider {
    static final TenantIdProvider DEFAULT = new TenantIdProvider() {
        @Override
        public String getTenantId() {
            return "default";
        }
    };

    String getTenantId();

    static TenantIdProvider provider() {
        for (TenantIdProvider provider : ServiceLoader.load(TenantIdProvider.class)) {
            return provider;
        }
        return DEFAULT;
    }
}
