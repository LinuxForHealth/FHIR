/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.thread;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * ThreadHandler is a common pattern used to control the
 * safe handling of a sleeping thread.
 */
public final class ThreadHandler {
    public static final long SECOND = 1000;
    public static final long FIVE_SECONDS = 5000;
    public static final long TEN_SECONDS = 10000;
    public static final long MINUTE = 60000;

    private ThreadHandler() {
        // NOP
    }

    /**
     * Sleep for the requested number of milliseconds
     * @param millis
     */
    public static void safeSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException x) {
            // NOP. Being woken up early, probably for exit
        }
    }
    
    /**
     * Sleep until we reach the target wakeUpTime
     * @param wakeUpTime
     * @return true if we woke up naturally, false if our sleep was interrupted
     */
    public static boolean sleepUntil(Instant wakeUpTime) {
        boolean wokeUpEarly = false;
        long gap = Instant.now().until(wakeUpTime, ChronoUnit.MILLIS);
        while (!wokeUpEarly && gap > 0) {
            try {
                Thread.sleep(gap);
            } catch (InterruptedException x) {
                wokeUpEarly = true;
            }
            
            // recompute the gap to make sure we fully reach our intended
            // wake-up time
            gap = Instant.now().until(wakeUpTime, ChronoUnit.MILLIS);
        }
        
        return !wokeUpEarly;
    }
}