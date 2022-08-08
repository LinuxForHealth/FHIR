/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.config.test;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.config.MetricHandle;

/**
 * Unit tests for the FHIRRequestContext CallTimeMetric performance tracker
 */
public class CallTimeMetricTest {

    /**
     * The metrics map is static, so we need to reset for each test to ensure
     * the call counts are deterministic
     */
    @BeforeMethod
    public void reset() {
        FHIRRequestContext.getAndResetMetrics();
    }

    @Test
    public void testSingle() throws Exception {
        FHIRRequestContext context = new FHIRRequestContext("default", "default");
        MetricHandle h = context.getMetricHandle("one");
        assertEquals(h.getParentPath(), "/");
        assertEquals(h.getMetric().getMetricName(), "one");
        assertEquals(h.getMetric().getFullMetricName(), "/one");
        h.close();
        h = context.getMetricHandle("two");
        assertEquals(h.getParentPath(), "/");
        assertEquals(h.getMetric().getMetricName(), "two");
        assertEquals(h.getMetric().getFullMetricName(), "/two");
        h.close();
        assertEquals(h.getMetric().getCallCount(), 1);
        h = context.getMetricHandle("one");
        assertEquals(h.getParentPath(), "/");
        assertEquals(h.getMetric().getMetricName(), "one");
        assertEquals(h.getMetric().getFullMetricName(), "/one");
        h.close();
        assertEquals(h.getMetric().getCallCount(), 2);
    }

    @Test
    public void testNested() throws Exception {
        FHIRRequestContext context = new FHIRRequestContext("default", "default");
        MetricHandle h = context.getMetricHandle("one");
        assertEquals(h.getParentPath(), "/");
        assertEquals(h.getMetric().getMetricName(), "one");
        assertEquals(h.getMetric().getFullMetricName(), "/one");
        h = context.getMetricHandle("two");
        assertEquals(h.getParentPath(), "/one/");
        assertEquals(h.getMetric().getMetricName(), "two");
        assertEquals(h.getMetric().getCallCount(), 0);
        assertEquals(h.getMetric().getFullMetricName(), "/one/two");
        h.close();
        assertEquals(h.getMetric().getCallCount(), 1);
        h = context.getMetricHandle("three");
        assertEquals(h.getParentPath(), "/one/");
        assertEquals(h.getMetric().getMetricName(), "three");
        assertEquals(h.getMetric().getFullMetricName(), "/one/three");
        h.close();
        assertEquals(h.getMetric().getCallCount(), 1);
    }

    @Test
    public void testException() throws Exception {
        FHIRRequestContext context = new FHIRRequestContext("default", "default");
        try (MetricHandle h = context.getMetricHandle("one")) {
            throw new Exception("exception");
        } catch (Exception x) {
            // The above handle should be closed, so our new metric here should be a root-level metric
            MetricHandle h2 = context.getMetricHandle("new");
            assertEquals(h2.getMetric().getFullMetricName(), "/new");
        }
    }

    @Test
    public void testClose() throws Exception {
        FHIRRequestContext context = new FHIRRequestContext("default", "default");
        try (MetricHandle h1 = context.getMetricHandle("one");
             MetricHandle h2 = context.getMetricHandle("two")) {
            assertEquals(h1.getMetric().getFullMetricName(), "/one");
            assertEquals(h2.getMetric().getFullMetricName(), "/one/two");
        }

        // check the previous two metrics were closed as expected
        try (MetricHandle h1 = context.getMetricHandle("one");
             MetricHandle h2 = context.getMetricHandle("two")) {
               assertEquals(h1.getMetric().getFullMetricName(), "/one");
               assertEquals(h2.getMetric().getFullMetricName(), "/one/two");
           }
    }

    @Test
    public void testCloseWithException() throws Exception {
        FHIRRequestContext context = new FHIRRequestContext("default", "default");

        try {
            try (MetricHandle h1 = context.getMetricHandle("one");
                 MetricHandle h2 = context.getMetricHandle("two")) {
                   assertEquals(h1.getMetric().getFullMetricName(), "/one");
                   assertEquals(h2.getMetric().getFullMetricName(), "/one/two");
                   throw new IllegalArgumentException("a problem");
            }
        } catch (IllegalArgumentException x) {
            // suppress
        }

        // check that the above handles were both closed even though we hit an exception
        try (MetricHandle h1 = context.getMetricHandle("one");
                MetricHandle h2 = context.getMetricHandle("two")) {
            assertEquals(h1.getMetric().getFullMetricName(), "/one");
            assertEquals(h2.getMetric().getFullMetricName(), "/one/two");
        }
    }

    @Test
    public void testCloseException() throws Exception {
        FHIRRequestContext context = new FHIRRequestContext("default", "default");

        try {
            try (MetricHandle h1 = context.getMetricHandle("one");
                 Uncloseable h2 = new Uncloseable()) {
                   assertEquals(h1.getMetric().getFullMetricName(), "/one");
            }
        } catch (IllegalArgumentException x) {
            assertEquals(x.getMessage(), "error during close");
        }

        // check that h1 was still closed even though we hit an exception
        // trying to close h2 first. We check this by creating a new metric
        // handle and verifying that its metric is at the root level.
        try (MetricHandle h1 = context.getMetricHandle("one");
                MetricHandle h2 = context.getMetricHandle("two")) {
            assertEquals(h1.getMetric().getFullMetricName(), "/one");
            assertEquals(h2.getMetric().getFullMetricName(), "/one/two");
        }
    }

    /**
     * A class which will generate an exception when {@link #clone()} is called
     */
    private static class Uncloseable implements AutoCloseable {

        @Override
        public void close() {
            throw new IllegalArgumentException("error during close");
        }
        
    }
}
