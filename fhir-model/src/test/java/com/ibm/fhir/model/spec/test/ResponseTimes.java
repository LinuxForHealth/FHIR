/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.spec.test;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Used to collect response time metrics during development
 * (e.h. test-case runs) to identify hotspots or concurrency issues. 
 * Not intended for runtime instrumentation.
 * 
 * We use long, assuming that the times given are in ms, which is
 * sufficiently accurate for the sort of measurements we're dealing
 * with.
 */
public class ResponseTimes {
    // collection of samples
    private final ConcurrentLinkedQueue<Long> samples = new ConcurrentLinkedQueue<>();

    // Keep track of the max value
    private final AtomicLong maxValue = new AtomicLong(-1);

    /**
     * Add the response time to the collection of samples
     * @param readTime
     */
    public void addSample(long sample) {
        if (sample < 0) {
            throw new IllegalArgumentException("sample must be >= 0.0");
        }
        this.samples.add(sample);

        // use -1 for initial state because we know response times can never be negative
        maxValue.getAndUpdate(v -> { return v < 0 || sample > v ? sample : v;});
    }
    
    public int getCount() {
        return this.samples.size();
    }
    
    /**
     * Calculate the average value of the samples
     * @return
     */
    public double getAverage() {
        return Analytics.average(samples);
    }
    
    /**
     * Calculate the nth percentile (e.g. 95th percentile)
     * which is a more representative metric for 
     * @param n
     * @return
     */
    public double percentile(int nth) {
        if (samples.size() > 0) {
            return Analytics.percentile(nth, this.samples);
        }
        else {
            return Double.NaN;
        }
    }

    /**
     * The maximum value of the sample
     * @return
     */
    public long maxValue() {
        return maxValue.get();
    }
}
