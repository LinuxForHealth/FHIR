/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.spec.test;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

/**
 * Simple unit test of the analytics
 */
public class AnalyticsTest {

    @Test
    public void testAverage() {
        List<Long> samples = new ArrayList<>();
        samples.add(0L);
        assertEquals(Analytics.average(samples),  0.0);
        
        samples.add(10L);
        assertEquals(Analytics.average(samples),  5.0);
        
        samples.add(20L);
        assertEquals(Analytics.average(samples), 10.0);
    }

    @Test
    public void testPercentile() {
        List<Long> samples = new ArrayList<>();
        samples.add(0L);
        samples.add(10L);
        assertEquals(Analytics.percentile(50, samples), 5.0);
    }

    @Test
    public void testPercentile100() {
        List<Long> samples = new ArrayList<>();
        
        for (int i=0; i<100; i++) {
            samples.add((long)i);
        }
        assertEquals(Analytics.percentile(50, samples), 49.5);
        assertEquals(Analytics.percentile(95, samples), 94.95, 0.001);
        assertEquals(Analytics.percentile(99, samples), 98.99, 0.001);
    }

}
