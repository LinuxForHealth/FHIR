/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.core.clock;

import java.time.Clock;

/**
 * Allows controlling the current date and time during tests.
 */
public class ClockFactory {
    private static Clock FIXED_CLOCK = null;
    
    public static Clock getDefaultClock() {
        if (FIXED_CLOCK != null) {
            return FIXED_CLOCK;
        } else {
            return Clock.systemUTC();
        }
    }
    
    public static void setDefaultClock(Clock clock) {
        FIXED_CLOCK = clock;
    }
}
