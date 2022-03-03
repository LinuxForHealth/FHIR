/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bulkdata.provider.impl.unicode;

import java.util.ArrayList;
import java.util.List;

/**
 * The UnicodeChar class wraps a val, and when called converts the character to a set of bytes.
 * These bytes are either 4, 3, 2 or 1 byte segments.
 *
 * A useful reference for Unicode is https://www.utf8-chartable.de/
 */
public class UnicodeChar {

    private int val;

    /**
     * public constructor
     * @param val the codepoint value of the unicode character
     */
    public UnicodeChar(int val) {
        this.val = val;
    }

    /**
     * generates the byte representation of the unicode character at a specific code point.
     * @return
     */
    public byte[] getBytes() {
        return getCharactersString().getBytes();
    }

    /**
     * gets the characters referenced by the charater codepoint
     * @return
     */
    public String getCharactersString() {
        char[] inter = Character.toChars(val);
        return String.valueOf(inter);
    }

    /**
     * gets the value as an html entity
     * @return
     */
    public String getHtmlEntityValue() {
        StringBuilder builder = new StringBuilder();
        builder.append('&');
        builder.append(val);
        builder.append(';');
        return builder.toString();
    }

    /**
     * gets the value as an escaped string
     * @return
     */
    public String getEscapedValue() {
        StringBuilder builder = new StringBuilder();
        builder.append('\\');
        String t = Integer.toString(val);
        for (int idx = t.length(); idx <= 4; idx++) {
            builder.append('0');
        }
        builder.append(val);
        return builder.toString();
    }

    /**
     * generates a list of the forbidden unicode characters in the specification
     *
     * @return List of UnicodeChars (either in utf8, utf16)
     */
    public static List<UnicodeChar> forbidden() {
        // Strings SHOULD not contain Unicode character points below 32
        // , except for u0009 (horizontal tab), u0010 (carriage return)
        // and u0013 (line feed).
        List<UnicodeChar> forbidden = new ArrayList<>();
        for (int i = 0; i < 32; i++) {
            if (i != 9 && i != 10 && i != 13) {
                forbidden.add(new UnicodeChar(i));
            }
        }
        return forbidden;
    }
}