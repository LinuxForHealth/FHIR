/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.config;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class is used to manage the runtime configuration of the FHIR terminology module.
 */
public final class FHIRTermConfig {
    private static final AtomicBoolean cachingDisabled = new AtomicBoolean();

    private FHIRTermConfig() { }

    /**
     * Set the caching disabled configuration property for the FHIR terminology module.
     *
     * @param disabled
     *     the disabled value to set
     */
    public static void setCachingDisabled(boolean disabled) {
        cachingDisabled.set(disabled);
    }

    /**
     * Indicates whether caching is disabled for the FHIR terminology module
     *
     * @return
     *     true if caching is disabled for the FHIR terminology module, false otherwise
     */
    public static boolean isCachingDisabled() {
        return cachingDisabled.get();
    }
}
