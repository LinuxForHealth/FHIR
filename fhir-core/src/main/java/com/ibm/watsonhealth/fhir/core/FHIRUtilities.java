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

/**
 * A collection of miscellaneous utility functions used by the various fhir-* projects.
 */
public class FHIRUtilities {
	
	protected static final String NL = System.getProperty("line.separator");
    
    public static String getObjectHandle(Object o) {
        return Integer.toHexString(System.identityHashCode(o));
    }

    /**
     * Retrieves the current thread's stacktrace and returns it as a string.
     * @return
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
    
    // Read data for a resource from a control document (JSON/XML file)
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
    
    public static String stripNamespaceIfPresentInDiv(String str) {
        int startIndex = str.indexOf("<div xmlns=");
        if (startIndex != -1) {
            int endIndex = str.indexOf(">", startIndex);
            String subString = str.substring(startIndex, endIndex + 1);
            return str.replace(subString, "<div>");
        }
        return str;
    }
    
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
}
