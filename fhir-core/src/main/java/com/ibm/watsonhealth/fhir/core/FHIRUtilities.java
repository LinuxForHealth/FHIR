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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * A collection of miscellaneous utility functions used by the various fhir-* projects.
 */
public class FHIRUtilities {
    protected static final String NL = System.getProperty("line.separator");
    private static final DatatypeFactory datatypeFactory = createDatatypeFactory();
    private static final ThreadLocal<SimpleDateFormat> timestampSimpleDateFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        public SimpleDateFormat initialValue() {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            return format;
        }
    };
    private static final ThreadLocal<SimpleDateFormat> calendarSimpleDateFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        public SimpleDateFormat initialValue() {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            return format;
        }
    };

    private static DatatypeFactory createDatatypeFactory() {
        try {
            return DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            throw new Error(e);
        }
    }

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
     * Read data for a resource from a control document (JSON/XML file)
     */
    public static String readFromFile(String filePath) {
        try {
            StringBuffer buffer = new StringBuffer();
            BufferedReader in = new BufferedReader(new InputStreamReader(FHIRUtilities.class.getClassLoader().getResourceAsStream(filePath)));
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
     * This function will remove any whitspace characters which appear in a '<div>...</div>' section within the
     * specified string.
     * 
     * @param str
     *            the string to process
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
     * This function will remove any newlines which appear in a '<div>...</div>' section within the specified string.
     * 
     * @param str
     *            the string to process
     * @return the input string with the 'div' new lines removed
     */
    public static String stripNewLineWhitespaceIfPresentInDiv(String str) {
        int startIndex = str.indexOf("<div>");
        if (startIndex != -1) {
            int endIndex = str.indexOf("</div>", startIndex);
            if (endIndex != -1) {
                String divContent = str.substring(startIndex + 5, endIndex);
                String divWithoutNewLine = divContent.replace("\\n", "");
                String strNewLineStrippedInDiv = str.replace(divContent, divWithoutNewLine);
                String divWithoutNewLineWhiteSpace = divWithoutNewLine.replace(" ", "");
                return strNewLineStrippedInDiv.replace(divWithoutNewLine, divWithoutNewLineWhiteSpace);
            }
        }
        return str;
    }

    /**
     * This function can be used to decode an xor-encoded value that was produced by the WebSphere Liberty
     * 'securityUtility' command.
     * 
     * @param encodedString
     *            the encoded string to be decoded
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
     * Returns true if and only if the specified string 's' is an encoded value, which means it starts with the string
     * "{xor}".
     * 
     * @param s
     *            the string value to check
     */
    public static boolean isEncoded(String s) {
        return s.startsWith("{xor}");
    }

    public static XMLGregorianCalendar parseDateTime(String lexicalRepresentation, boolean defaults) {
        try {
            XMLGregorianCalendar calendar = datatypeFactory.newXMLGregorianCalendar(lexicalRepresentation);
            if (defaults) {
                setDefaults(calendar);
            }
            return calendar;
        } catch (Exception e) {
        }
        return null;
    }
    
    public static Timestamp convertToTimestamp(XMLGregorianCalendar calendar) {
        return Timestamp.valueOf(formatTimestamp(calendar.toGregorianCalendar().getTime()));
    }
    
    public static XMLGregorianCalendar convertToCalendar(Timestamp timestamp) {
        return parseDateTime(formatCalendar(timestamp), false);
    }
    
    public static void setDefaults(XMLGregorianCalendar calendar) {
        if (isYear(calendar)) {
            calendar.setMonth(DatatypeConstants.JANUARY);
        }
        if (isYear(calendar) || isYearMonth(calendar)) {
            calendar.setDay(1);
        }
        if (isYear(calendar) || isYearMonth(calendar) || isDate(calendar)) {
            calendar.setHour(0);
            calendar.setMinute(0);
            calendar.setSecond(0);
            calendar.setTimezone(0);
        }
    }

    public static Duration createDuration(XMLGregorianCalendar calendar) {
        int years = 0;
        if (isYear(calendar)) {
            years = 1;
        }
        int months = 0;
        if (isYearMonth(calendar)) {
            months = 1;
        }
        int days = 0;
        if (isDate(calendar)) {
            days = 1;
        }
        return datatypeFactory.newDuration(true, years, months, days, 0, 0, 0);
    }

    public static boolean isDateTime(XMLGregorianCalendar calendar) {
        return calendar != null && calendar.getYear() != DatatypeConstants.FIELD_UNDEFINED && calendar.getMonth() != DatatypeConstants.FIELD_UNDEFINED
                && calendar.getDay() != DatatypeConstants.FIELD_UNDEFINED && calendar.getHour() != DatatypeConstants.FIELD_UNDEFINED
                && calendar.getMinute() != DatatypeConstants.FIELD_UNDEFINED && calendar.getSecond() != DatatypeConstants.FIELD_UNDEFINED
                && calendar.getTimezone() != DatatypeConstants.FIELD_UNDEFINED;
    }

    public static boolean isPartialDate(XMLGregorianCalendar calendar) {
        return isYear(calendar) || isYearMonth(calendar) || isDate(calendar);
    }

    public static boolean isYear(XMLGregorianCalendar calendar) {
        return calendar != null && calendar.getYear() != DatatypeConstants.FIELD_UNDEFINED && calendar.getMonth() == DatatypeConstants.FIELD_UNDEFINED
                && calendar.getDay() == DatatypeConstants.FIELD_UNDEFINED && calendar.getHour() == DatatypeConstants.FIELD_UNDEFINED
                && calendar.getMinute() == DatatypeConstants.FIELD_UNDEFINED && calendar.getSecond() == DatatypeConstants.FIELD_UNDEFINED
                && calendar.getTimezone() == DatatypeConstants.FIELD_UNDEFINED;
    }

    public static boolean isYearMonth(XMLGregorianCalendar calendar) {
        return calendar != null && calendar.getYear() != DatatypeConstants.FIELD_UNDEFINED && calendar.getMonth() != DatatypeConstants.FIELD_UNDEFINED
                && calendar.getDay() == DatatypeConstants.FIELD_UNDEFINED && calendar.getHour() == DatatypeConstants.FIELD_UNDEFINED
                && calendar.getMinute() == DatatypeConstants.FIELD_UNDEFINED && calendar.getSecond() == DatatypeConstants.FIELD_UNDEFINED
                && calendar.getTimezone() == DatatypeConstants.FIELD_UNDEFINED;
    }

    public static boolean isDate(XMLGregorianCalendar calendar) {
        return calendar != null && calendar.getYear() != DatatypeConstants.FIELD_UNDEFINED && calendar.getMonth() != DatatypeConstants.FIELD_UNDEFINED
                && calendar.getDay() != DatatypeConstants.FIELD_UNDEFINED && calendar.getHour() == DatatypeConstants.FIELD_UNDEFINED
                && calendar.getMinute() == DatatypeConstants.FIELD_UNDEFINED && calendar.getSecond() == DatatypeConstants.FIELD_UNDEFINED
                && calendar.getTimezone() == DatatypeConstants.FIELD_UNDEFINED;
    }

    public static String formatTimestamp(Date date) {
        return timestampSimpleDateFormat.get().format(date);
    }
    
    public static String formatCalendar(Timestamp timestamp) {
        return calendarSimpleDateFormat.get().format(timestamp);
    }
}
