/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.spec.test;

import java.io.PrintStream;

/*
 * Renders a simple report using data from DriverMetrics
 *
 */
public class DriverStats {
    
    // Where to render the report output
    private final PrintStream out;
    
    // The number of seconds covered by the samples
    private final double secs;

    /**
     * Public constructor
     * @param out
     */
    public DriverStats(PrintStream out, double secs) {
        this.out = out;
        this.secs = secs;
    }

    /**
     * Render the report using the given values
     * @param readTimeAverage
     * @param readTimeMax
     * @param readTimeP95
     * @param readCount
     * @param validateTimeAverage
     * @param validateTimeMax
     * @param validateTimeP95
     * @param validateCount
     * @param processTimeAverage
     * @param processTimeMax
     * @param processTimeP95
     * @param processCount
     * @param postTimeAverage
     * @param postTimeMax
     * @param postTimeP95
     * @param postCount
     * @param getTimeAverage
     * @param getTimeMax
     * @param getTimeP95
     * @param getCount
     */
    public void render(double readTimeAverage, long readTimeMax, double readTimeP95, int readCount,
        double validateTimeAverage, long validateTimeMax, double validateTimeP95, int validateCount,
        double processTimeAverage, long processTimeMax, double processTimeP95, int processCount,
        double postTimeAverage, long postTimeMax, double postTimeP95, int postCount,
        double getTimeAverage, long getTimeMax, double getTimeP95, int getCount) {

        out.println(String.format("         %5s %7s %7s %7s %7s", "REQS", "MAX(ms)", "AVG(ms)", "95TH(ms)", "CALLS/s"));
        out.println(String.format("   PARSE %5d %7d %7.0f %7.0f %7.1f", readCount, readTimeMax, readTimeAverage, readTimeP95, readCount / secs));
        out.println(String.format("VALIDATE %5d %7d %7.0f %7.0f %7.1f", validateCount, validateTimeMax, validateTimeAverage, validateTimeP95, validateCount / secs));
        out.println(String.format("  CREATE %5d %7d %7.0f %7.0f %7.1f", postCount, postTimeMax, postTimeAverage, postTimeP95, postCount / secs));
        out.println(String.format("    READ %5d %7d %7.0f %7.0f %7.1f", getCount, getTimeMax, getTimeAverage, getTimeP95, getCount / secs));
        out.println(String.format(" PROCESS %5d %7d %7.0f %7.0f %7.1f", processCount, processTimeMax, processTimeAverage, processTimeP95, processCount / secs));

    }
}
