/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.database.utils.common;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

/**
 * Utilities for helping with threads
 */
public class ThreadHelper {

    /**
     * Sleep the current thread for the given number of millis
     * without worrying about interrupts. If an interrupt wakes
     * us up early, we simply return
     * @param millis
     * @return true if the sleep was fully successful, false if interrupted
     */
    public static boolean safeSleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException x) {
            return false;
        }
        
        return true;
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