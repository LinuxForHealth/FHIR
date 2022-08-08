/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.config;


/**
 * Helper to read system level properties (not from fhir-server-config)
 */
public class SystemConfigHelper {

    /**
     * Convert the given duration into the equivalent number of seconds.
     * A duration can be specified as a combination of second, minute and
     * hour values. For example:
     *   86400s
     *   1440m
     *   24h
     *   1439m60s
     *   23h3600s
     *   23h59m60s
     *   etc.
     * @param duration
     * @return
     */
    public static long convertToSeconds(String duration) {
        if (duration == null || duration.isEmpty()) {
            throw new IllegalArgumentException("Invalid duration - is empty");
        }
        long result = 0;
        
        int start = 0;
        for (int idx=0; idx<duration.length(); idx++) {
            final int mult;
            switch (duration.charAt(idx)) {
            case 'H':
            case 'h':
                mult = 3600;
                break;
            case 'M':
            case 'm':
                mult = 60;
                break;
            case 'S':
            case 's':
                mult = 1;
                break;
             default:
                 mult = 0;
            }
            
            if (mult > 0) {
                if (idx == start) {
                    throw new IllegalArgumentException("Invalid duration - no value before value type");
                }
                String valueStr = duration.substring(start, idx);
                Integer value = Integer.parseInt(valueStr);
                result += value * mult;
                start = idx + 1;
            } else if (idx == duration.length()-1) {
                // if no type is given but we've reached the end, assume seconds
                String valueStr = duration.substring(start);
                Integer value = Integer.parseInt(valueStr);
                result += value;
            }
        }
        
        return result;
    }

    /**
     * Get the value of the environment variable and convert to number of seconds
     * @param envVariable
     * @return
     */
    public static long getDurationFromEnv(String envVariable, String defaultValue) {
        String valueStr = System.getenv(envVariable);
        if (valueStr == null || valueStr.isEmpty()) {
            valueStr = defaultValue;
        }
        
        if (valueStr == null || valueStr.isEmpty()) {
            return -1;
        } else {
            return convertToSeconds(valueStr);
        }
    }
}
