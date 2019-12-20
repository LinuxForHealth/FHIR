/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.core;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.TimeZone;


/**
 * A collection of miscellaneous utility functions used by the various fhir-*
 * projects.
 */
public class FHIRUtilities {
    
    // For R4, we transition to using java.time
    private static final ThreadLocal<SimpleDateFormat> timestampSimpleDateFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        public SimpleDateFormat initialValue() {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            return format;
        }
    };

    /**
     * Returns the specified object's handle in hex format.
     */
    public static String getObjectHandle(Object o) {
        return Integer.toHexString(System.identityHashCode(o));
    }

    /**
     * This function will remove any whitspace characters which appear in a
     * '<div>...</div>' section within the specified string.
     * 
     * @param str the string to process
     * @return the input string with the 'div' whitespace characters removed
     */
    public static String stripNamespaceIfPresentInDiv(String str) {
        int startIndex = str.indexOf("<div xmlns=");
        if (startIndex != -1) {
            int endIndex = str.indexOf(">", startIndex);
            String subString = str.substring(startIndex, endIndex + 1);
            return str.replace(subString, "<div>");
        }
        return str;
    }

    /**
     * This function will remove any newlines which appear in a '<div>...</div>'
     * section within the specified string.
     * 
     * @param str the string to process
     * @return the input string with the 'div' new lines removed
     */
    public static String stripNewLineWhitespaceIfPresentInDiv(String str) {
        int startIndex = str.indexOf("<div>");
        if (startIndex != -1) {
            int endIndex = str.indexOf("</div>", startIndex);
            if (endIndex != -1) {
                String divContent = str.substring(startIndex + 5, endIndex);
                String divWithoutNewLine = divContent.replace("\\r", "").replace("\\n", "").replaceAll(">\\s*<", "><");
                String strNewLineStrippedInDiv = str.replace(divContent, divWithoutNewLine);
                // String divWithoutNewLineWhiteSpace = divWithoutNewLine.replace(" ", "");
                return strNewLineStrippedInDiv;// .replace(divWithoutNewLine, divWithoutNewLineWhiteSpace);
            }
        }
        return str;
    }

    /**
     * This function can be used to decode an xor-encoded value that was produced by
     * the WebSphere Liberty 'securityUtility' command.
     * 
     * @param encodedString the encoded string to be decoded
     * @return the decoded version of the input string
     * @throws Exception
     */
    public static String decode(String encodedString) throws Exception {
        String decodedString = null;
        if (isEncoded(encodedString)) {
            String withoutTag = encodedString.substring(5);
            byte[] bytes = withoutTag.getBytes("UTF-8");
            byte[] decodedBytes = Base64.getDecoder().decode(bytes);
            byte[] xor_bytes = new byte[decodedBytes.length];
            for (int i = 0; i < decodedBytes.length; i++) {
                xor_bytes[i] = (byte) (0x5F ^ decodedBytes[i]);
            }
            decodedString = new String(xor_bytes, "UTF-8");
        } else {
            decodedString = encodedString;
        }
        return decodedString;
    }

    /**
     * Returns true if and only if the specified string 's' is an encoded value,
     * which means it starts with the string "{xor}".
     * 
     * @param s the string value to check
     */
    public static boolean isEncoded(String s) {
        return s != null && s.startsWith("{xor}");
    }

    /**
     * For R4 model, generate a sql timestamp
     * 
     * @param zdt
     * @return
     */
    public static Timestamp convertToTimestamp(java.time.ZonedDateTime zdt) {
        return Timestamp.from(zdt.toInstant());
    }


    public static String formatTimestamp(Date date) {
        return timestampSimpleDateFormat.get().format(date);
    }

}
