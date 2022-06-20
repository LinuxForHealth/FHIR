/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Static support functions related to logging
 */
public class LogSupport {
    private static final String MASK = "*****";
    private static final Pattern PASSWORD_EQ_PATTERN = Pattern.compile("[^\"]password[= ]*\"([^\"]*)\"", Pattern.CASE_INSENSITIVE);
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("\"password\"[: ]*\"([^\"]*)\"", Pattern.CASE_INSENSITIVE);

    /**
     * Hide any text in quotes following the token "password" to avoid writing secrets to log files
     * @param input
     * @return
     */
    public static String hidePassword(String input) {
        String result = hidePassword(input, PASSWORD_EQ_PATTERN);
        result = hidePassword(result, PASSWORD_PATTERN);
        return result;
      }
    
    /**
     * Replace any text matching the given pattern with the MASK value
     * @param input
     * @param pattern
     * @return
     */
    private static String hidePassword(String input, Pattern pattern) {
        final Matcher m = pattern.matcher(input);
        final StringBuffer result = new StringBuffer();
        while (m.find()) {
            final String match = m.group();
            final int start = m.start();
            m.appendReplacement(result, 
                match.substring(0, 
                    m.start(1) - start)
                    + MASK
                    + match.substring(m.end(1) - start, m.end() - start));
        }
        m.appendTail(result);
        return result.toString();
    }
}
