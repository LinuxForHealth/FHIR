/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.config;

import java.util.concurrent.atomic.AtomicBoolean;

public final class FHIRTermConfig {
    private static final AtomicBoolean cachingDisabled = new AtomicBoolean();

    private FHIRTermConfig() { }

    public static void setCachingDisabled(boolean disabled) {
        cachingDisabled.set(disabled);
    }

    public static boolean isCachingDisabled() {
        return cachingDisabled.get();
    }
}
