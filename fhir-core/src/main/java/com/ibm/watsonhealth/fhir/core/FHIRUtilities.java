/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * A collection of miscellaneous utility functions used by the various fhir-* projects.
 */
public class FHIRUtilities {
	
	protected static final String NL = System.getProperty("line.separator");
    
	/**
	 * Returns the specified object's handle in hex format.
	 */
    public static String getObjectHandle(Object o) {
        return Integer.toHexString(System.identityHashCode(o));
    }

    /**
     * Retrieves the current thread's stacktrace and returns it as a string.
     */
    public static String getCurrentStacktrace() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StringBuilder sb = new StringBuilder();
        sb.append("Current stacktrace:\n");
        for (StackTraceElement frame : stackTrace) {
            sb.append("\tat " + frame.getClassName() + "." + frame.getMethodName() + "(" + frame.getFileName() + ":" + frame.getLineNumber() + ")\n");
        }

        return sb.toString();
    }
    
    /**
     *  Read data for a resource from a control document (JSON/XML file)
     */
    public static String readFromFile(String filePath) {
        try {
            StringBuffer buffer = new StringBuffer();
            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                		FHIRUtilities.class.getClassLoader().getResourceAsStream(filePath)
                )
            );
            String line = null;
            while ((line = in.readLine()) != null) {
                buffer.append(line);
                buffer.append(NL);
            }
            in.close();
            return buffer.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * This function will remove any whitspace characters which appear in a '<div>...</div>' section
     * within the specified string.
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
     * This function will remove any newlines which appear in a '<div>...</div>' section
     * within the specified string.
     * @param str the string to process
     * @return the input string with the 'div' new lines removed
     */
    public static String stripNewLineWhitespaceIfPresentInDiv(String str) {
        int startIndex = str.indexOf("<div>");
        if (startIndex != -1) {
            int endIndex = str.indexOf("</div>", startIndex);
            if(endIndex != -1) {
            	String divContent = str.substring(startIndex+5, endIndex);
                String divWithoutNewLine = divContent.replace("\\n", "");
                String strNewLineStrippedInDiv = str.replace(divContent, divWithoutNewLine);
                String divWithoutNewLineWhiteSpace = divWithoutNewLine.replace(" ", "");
                return strNewLineStrippedInDiv.replace(divWithoutNewLine, divWithoutNewLineWhiteSpace);
            }
        }
        return str;
    }
    
    /**
     * This function can be used to decode an xor-encoded value that was produced by the 
     * WebSphere Liberty 'securityUtility' command.
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
     * Returns true if and only if the specified string 's' is an encoded value, which means it starts
     * with the string "{xor}".
     * @param s the string value to check
     */
    public static boolean isEncoded(String s) {
        return s.startsWith("{xor}");
    }
    
    public static Date parseDate(String source) {
        List<String> patterns = Arrays.asList(
            "yyyy-MM-dd'T'HH:mm:ssXXX", 
            "yyyy-MM-dd", 
            "yyyy-MM", 
            "yyyy"
        );
        for (String pattern : patterns) {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            try {
                return format.parse(source);
            } catch (ParseException e) {
            }
        }
        return null;
    }
}
