/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.util.test;

import static com.ibm.fhir.model.util.FHIRUtil.REFERENCE_PATTERN;

import java.util.regex.Matcher;

public class ReferencePatternTest {
    public static void main(String[] args) throws Exception {
        Matcher matcher = REFERENCE_PATTERN.matcher("Patient/12345/_history/1");
        System.out.println(matcher.groupCount());
        if (matcher.matches()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                System.out.println(i + ": " + matcher.group(i));
            }
        }
    }
}
