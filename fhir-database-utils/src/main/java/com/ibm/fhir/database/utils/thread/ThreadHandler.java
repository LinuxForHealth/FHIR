/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.thread;


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
}
