/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.config;


/**
 * Measure the time of a call and accumulate that time in the attached
 * {@link CallTimeMetric} when this handle is closed.
 */
public class MetricHandle implements AutoCloseable {
    // the parent request context
    final FHIRRequestContext context;

    // The metric we'll add our accumulated time to when we are closed
    final CallTimeMetric metric;

    // The time this instance of the measured call started
    final long start;

    // The parent metric handle
    final MetricHandle parent;

    /**
     * Protected constructor for a root-level metric
     * @param context
     * @param metric
     * @param parent
     */
    protected MetricHandle(FHIRRequestContext context, CallTimeMetric metric, MetricHandle parent) {
        this.context = context;
        this.metric = metric;
        this.parent = parent;
        this.start = System.nanoTime();
    }

    /**
     * The shared metric value represented by this handle
     * @return
     */
    public CallTimeMetric getMetric() {
        return this.metric;
    }

    /**
     * Get the parent of this handle
     * @return
     */
    public MetricHandle getParent() {
        return this.parent;
    }

    /**
     * Get the full path of the parent of this
     * @return
     */
    public String getParentPath() {
        if (this.parent == null) {
            return "/";
        } else {
            return this.parent.getPath();
        }
    }

    /**
     * Convenience method to get the path name of the metric we are wrapping
     * @return
     */
    public String getPath() {
        return this.metric.getFullMetricName() + "/";
    }

    @Override
    public void close() {
        long elapsed = System.nanoTime() - this.start;
        metric.accumulateTime(elapsed);

        // inform the context that the call associated with this metric has ended
        context.endMetric(this);
    }

    /**
     * Convenience method to get a child MetricHandle by calling through to our
     * parent request context
     * @param name
     * @return
     */
    public MetricHandle getMetricHandle(String name) {
        return context.getMetricHandle(name);
    }
}
