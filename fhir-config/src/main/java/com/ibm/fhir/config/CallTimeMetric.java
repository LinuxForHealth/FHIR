/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.config;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple mechanism to track how much time we're spending in various calls
 * @implNote thread-safe
 */
public class CallTimeMetric {
    // The full path name of this metric
    private final String fullMetricName;
    // The name of the metric (relative to the parentPath)
    private final String metricName;

    // Use atomic values so we can be safely used across multiple threads if desired
    private AtomicLong accumulatedTime = new AtomicLong();
    private AtomicLong callCount = new AtomicLong();

    /**
     * Constructor to create a metric to track the performance of a call or span of code
     * @param fullMetricName
     * @param metricName
     */
    public CallTimeMetric(String fullMetricName, String metricName) {
        this.fullMetricName = fullMetricName;
        this.metricName = metricName;
    }

    /**
     * Get the full name of the metric (path + name)
     * @return
     */
    public String getFullMetricName() {
        return this.fullMetricName;
    }

    /**
     * @return the accumulatedTime
     */
    public long getAccumulatedTime() {
        return accumulatedTime.get();
    }

    /**
     * Add nanos to the accumulated time and increment the call count
     * @param nanos the time to add in nanoseconds
     */
    public void accumulateTime(long nanos) {
        this.accumulatedTime.addAndGet(nanos);
        this.callCount.addAndGet(1);
    }

    /**
     * @return the callCount
     */
    public long getCallCount() {
        return callCount.get();
    }

    /**
     * @return the metricName
     */
    public String getMetricName() {
        return metricName;
    }

    /**
     * Render the collection of metrics as a series of fine log messages
     * @param logger
     * @param metrics
     */
    public static void render(Logger logger, List<CallTimeMetric> metrics) {
        final double NANOS = 1e9;
        if (!logger.isLoggable(Level.FINE)) {
            return;
        }
        for (CallTimeMetric m: metrics) {
            StringBuilder msg = new StringBuilder();
            double totalElapsed = m.getAccumulatedTime() / NANOS;
            double averageElapsed = m.getCallCount() > 0 ? m.getAccumulatedTime() / NANOS / m.getCallCount() : Double.NaN;
            msg.append("#METRIC")
                .append(" ").append(String.format("%6d", m.getCallCount()))
                .append(" ").append(String.format("%8.3f", totalElapsed))
                .append(" ").append(String.format("%6.3f", averageElapsed))
                .append(" ").append(m.getFullMetricName()) // at the end so the numbers line up for better readability
                ;
            logger.fine(msg.toString());
        }
    }
}
