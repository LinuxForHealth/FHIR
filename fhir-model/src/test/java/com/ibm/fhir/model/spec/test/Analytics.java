/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.spec.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Captures the the averages and analysis data for a report
 */
public class Analytics {
    
    /**
     * Compute the average from the list of metrics
     * @param metrics
     * @return
     */
    public static double average(Collection<Long> metrics) {
        int count = 0;
        double total = 0.0;
        for (Long m: metrics) {
            count++;
            total += m;
        }

        if (count > 0) {
            return total / count;
        }
        else {
            return Double.NaN;
        }
        
    }

    /**
     * Compute the nth percentile from the list of metrics
     * @param nth
     * @param metrics
     * @return
     */
    public static double percentile(int nth, Collection<Long> samples) {
        double result;
        if (nth < 1 || nth > 100) {
                throw new IllegalArgumentException("Percentile must be in range (0, 100]");
        }
        
        if (samples.isEmpty()) {
                throw new IllegalArgumentException("metrics list cannot be empty");
        }

        // Calculate the Nth percentile. To do this, we need to convert into a list and sort it
        List<Long> metrics = new ArrayList<>(samples);
        Collections.sort(metrics);
        final int N = metrics.size();
        if (N == 1) {
                // Single value, so no interpolation
                result = metrics.get(0);
        }
        else if (nth == 100) {
                result = metrics.get(metrics.size()-1);
        }
        else {
                // Using the NIST recommendation C=0
                double x = nth / 100.0 * (N+1);
                if (x < 1.0) {
                        result = metrics.get(0);
                }
                else if (x >= N) {
                        result = metrics.get(N-1);
                }
                else {
                        // Interpolate. If there's no fractional part, that's fine because the value
                        // will simply be v1 + 0 * (v2-v1) = v1.
                        double xmod1 = x - (long)x;

                        // The lower index is the floor of the rank position (x)
                        int index = (int)x - 1;

                        // interpolate
                        double v1 = metrics.get(index);
                        double v2 = metrics.get(index + 1);
                        result = v1 + xmod1 * (v2-v1);
                }
        }
        
        return result;
    }
    

}
