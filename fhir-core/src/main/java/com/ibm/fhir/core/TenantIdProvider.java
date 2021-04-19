/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.core;

import java.util.ServiceLoader;

/**
 * An interface for providing the tenant id to the application for a particular context
 */
public interface TenantIdProvider {
    /**
     * The default tenant id provider
     */
    static final TenantIdProvider DEFAULT = new TenantIdProvider() {
        @Override
        public String getTenantId() {
            return "default";
        }
    };

    /**
     * Get the tenant id for a particular context
     *
     * @return
     *     the tenant id
     */
    String getTenantId();

    /**
     * A factory method for getting a tenant id provider from the service loader
     * or the default tenant id provider
     *
     * @return
     *     the tenant id provider from the service loader or the default provider
     */
    static TenantIdProvider provider() {
        for (TenantIdProvider provider : ServiceLoader.load(TenantIdProvider.class)) {
            return provider;
        }
        return DEFAULT;
    }
}
