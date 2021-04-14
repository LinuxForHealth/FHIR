/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.config;

import java.util.concurrent.atomic.AtomicBoolean;

public final class FHIRTermConfig {
    private static final AtomicBoolean cachingEnabled = new AtomicBoolean();

    private FHIRTermConfig() { }

    public static void setCachingEnabled(boolean enabled) {
        cachingEnabled.set(enabled);
    }

    public static boolean isCachingEnabled() {
        return cachingEnabled.get();
    }
}
