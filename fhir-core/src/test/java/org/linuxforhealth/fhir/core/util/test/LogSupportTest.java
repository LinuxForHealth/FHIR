/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.core.util.test;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.core.util.LogSupport;

/**
 * Unit tests for {@link LogSupport} methods
 */
public class LogSupportTest {

    @Test
    public void testPassReplaceEquals() {
        assertEquals(LogSupport.hidePassword("something password=\"change-password\" something else"), "something password=\"*****\" something else");
    }

    @Test
    public void testPassReplaceEqualsWithSpace() {
        assertEquals(LogSupport.hidePassword("something password = \"change-password\" something else"), "something password = \"*****\" something else");
    }

    @Test
    public void testPassReplaceJson() {
        assertEquals(LogSupport.hidePassword("something \"password\": \"change-password\" something else"), "something \"password\": \"*****\" something else");
    }

    @Test
    public void testPassReplaceJsonCompact() {
        assertEquals(LogSupport.hidePassword("something \"password\":\"change-password\" something else"), "something \"password\":\"*****\" something else");
    }

    @Test
    public void testPassReplaceMixed() {
        final String src = "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"token\" password=\"change-password-\";";
        final String tgt = src.replace("change-password-", "*****");
        assertEquals(LogSupport.hidePassword(src), tgt);
    }
}
