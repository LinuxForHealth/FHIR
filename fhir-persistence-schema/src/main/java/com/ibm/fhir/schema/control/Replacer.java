/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Replaces values in the line passed to the process method. 
 */
public class Replacer {

    private final Pattern pattern;
    private final String value;

    /**
     * Public constructor
     * @param token the name of the token to substitute
     * @param value the value to substitute
     */
    public Replacer(String token, String value) {
        this.pattern = Pattern.compile("\\{\\{" + token + "\\}\\}");
        this.value = value;
    }

    /**
     * Perform the substitution
     * @param line
     * @return
     */
    public String process(String line) {
        Matcher matcher = this.pattern.matcher(line);
        if (matcher.find()) {
            return matcher.replaceAll(this.value);
        }
        else {
            return line;
        }
    }

}
