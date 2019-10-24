/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.spec.test;

/**
 * Used to collect response time metrics during development
 * (e.h. test-case runs) to identify hotspots or concurrency issues. 
 * Not intended for runtime instrumentation.
 */
public class DriverMetrics {
    // Number of nanoseconds in a second
    public static final double NANOS = 1e9;
    
    // Number of nanoseconds in a millisecond
    public static final long NANOS_MS = 1000000L;
    
    // collection of read time samples
    private final ResponseTimes readTimes = new ResponseTimes();
    
    // collection of validation time samples
    private final ResponseTimes validateTimes = new ResponseTimes();
    
    // collection of process time samples
    private final ResponseTimes processTimes = new ResponseTimes();

    // collection of POST (create resource) response times
    private final ResponseTimes postTimes = new ResponseTimes();

    // collection of GET (read resource) response times
    private final ResponseTimes getTimes = new ResponseTimes();

    /**
     * Add a sample to the collection of read times
     * @param readTime in milliseconds
     */
    public void addReadTime(long readTime) {
        readTimes.addSample(readTime);
    }

    /**
     * Add a sample to the colleciton of validate times
     * @param ms
     */
    public void addValidateTime(long ms) {
        validateTimes.addSample(ms);
    }

    /**
     * Add a sample to the collection of response times
     * @param processTime in milliseconds
     */
    public void addProcessTime(long processTime) {
        processTimes.addSample(processTime);
    }

    /**
     * Add a sample to the collection of post times
     * @param ms
     */
    public void addPostTime(long ms) {
        this.postTimes.addSample(ms);
    }

    /**
     * Add a sample to the collection of get times
     * @param ms
     */
    public void addGetTime(long ms) {
        this.getTimes.addSample(ms);
    }
    
    /**
     * Render a report using the given DriverStats report generator
     * @return
     */
    public void render(DriverStats reportGenerator) {
        int nth = 95; // 95th percentile statistic

        reportGenerator.render(readTimes.getAverage(), readTimes.maxValue(), readTimes.percentile(nth), readTimes.getCount(),
            validateTimes.getAverage(), validateTimes.maxValue(), validateTimes.percentile(nth), validateTimes.getCount(),
            processTimes.getAverage(), processTimes.maxValue(), processTimes.percentile(nth), processTimes.getCount(),
            postTimes.getAverage(), postTimes.maxValue(), postTimes.percentile(nth), postTimes.getCount(),
            getTimes.getAverage(), getTimes.maxValue(), getTimes.percentile(nth), getTimes.getCount()
        );
    }
}
