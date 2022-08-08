/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.config.test;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.config.SystemConfigHelper;

/**
 * Unit tests for {@link SystemConfigHelper}
 */
public class SystemConfigHelperTest {

    @Test
    public void testDurations() {
        assertEquals(SystemConfigHelper.convertToSeconds("86400s"), 86400);
        assertEquals(SystemConfigHelper.convertToSeconds("1440m"), 86400);
        assertEquals(SystemConfigHelper.convertToSeconds("24h"), 86400);
        assertEquals(SystemConfigHelper.convertToSeconds("1439m60s"), 86400);
        assertEquals(SystemConfigHelper.convertToSeconds("23h3600s"), 86400);
        assertEquals(SystemConfigHelper.convertToSeconds("23h59m60s"), 86400);
        assertEquals(SystemConfigHelper.convertToSeconds("59s"), 59);
        assertEquals(SystemConfigHelper.convertToSeconds("1M"), 60);
        assertEquals(SystemConfigHelper.convertToSeconds("1M1S"), 61);
        assertEquals(SystemConfigHelper.convertToSeconds("11"), 11);
    }
    
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testBadDurationArg() {
        assertEquals(SystemConfigHelper.convertToSeconds("s"), 61);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testEmptyDurationArg() {
        assertEquals(SystemConfigHelper.convertToSeconds(""), 61);
    }
}
