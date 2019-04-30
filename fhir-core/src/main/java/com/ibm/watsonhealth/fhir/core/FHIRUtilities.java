/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.Key;
import java.security.KeyStore;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.io.IOUtils;

/**
 * A collection of miscellaneous utility functions used by the various fhir-* projects.
 */
public class FHIRUtilities {
    private static final String VALID_CHARACTERS_FOR_DATETIME = "^([\\-\\:\\.TZ0123456789\\+]+)$";
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
    private static final ThreadLocal<SimpleDateFormat> calendarSimpleDateFormatGMT = new ThreadLocal<SimpleDateFormat>() {
        @Override
        public SimpleDateFormat initialValue() {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
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
                String divWithoutNewLine = divContent.replace("\\r", "").replace("\\n", "").replaceAll(">\\s*<", "><");
                String strNewLineStrippedInDiv = str.replace(divContent, divWithoutNewLine);
                // String divWithoutNewLineWhiteSpace = divWithoutNewLine.replace(" ", "");
                return strNewLineStrippedInDiv;// .replace(divWithoutNewLine, divWithoutNewLineWhiteSpace);
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
        return s != null && s.startsWith("{xor}");
    }

    public static XMLGregorianCalendar parseDateTime(String lexicalRepresentation, boolean defaults) throws IllegalArgumentException {
        validateInputCharacters(lexicalRepresentation);
        XMLGregorianCalendar calendar = datatypeFactory.newXMLGregorianCalendar(lexicalRepresentation);
        if (defaults) {
            setDefaults(calendar);
        }
        return calendar;
    }

    private static void validateInputCharacters(String lexicalRepresentation) {
        String input = Optional.of(lexicalRepresentation).orElse("");
        if (!input.matches(VALID_CHARACTERS_FOR_DATETIME)) {
            throw new IllegalArgumentException("Illegal input identified: '" + input + "'.");
        }
    }

    public static Timestamp convertToTimestamp(XMLGregorianCalendar calendar) {
        return Timestamp.valueOf(formatTimestamp(calendar.toGregorianCalendar().getTime()));
    }

    public static XMLGregorianCalendar convertToCalendar(Timestamp timestamp, TimeZone zone) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(timestamp);
        XMLGregorianCalendar xmlCalendar = datatypeFactory.newXMLGregorianCalendar(calendar);
        xmlCalendar.setTimezone(zone.getRawOffset());
        return xmlCalendar;
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
            calendar.setMillisecond(0);
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
    
    public static String formatCalendarGMT(XMLGregorianCalendar calendar) {
        return calendarSimpleDateFormatGMT.get().format(calendar.toGregorianCalendar().getTimeInMillis());
    }

    /**
     * Retrieves an encryption key from the specified keystore file, using the specified
     * alias and password values.
     * @param keystoreLocation the name of the keystore file
     * @param keystorePassword the keystore's password
     * @param keyAlias the alias name of the entry containing the desired key
     * @param keyPassword the password associated with the key's entry in the keystore file
     * @return a SecretKeySpec object containing the AES encryption key retrieved from the keystore file
     */
    public static SecretKeySpec retrieveEncryptionKeyFromKeystore(String keystoreLocation, String keystorePassword, String keyAlias, String keyPassword,
        String storeType, String keyAlgorithm) throws Exception {
        InputStream is = null;
        SecretKeySpec secretKey = null;

        try {
            // First, search the classpath for the keystore file.
            URL url = Thread.currentThread().getContextClassLoader().getResource(keystoreLocation);
            if (url != null) {
                is = url.openStream();
            }

            // If the classpath search failed, try to open the file directly.
            if (is == null) {
                File f = new File(keystoreLocation);
                if (f.exists()) {
                    is = new FileInputStream(f);
                }
            }

            // If we couldn't open the file, throw an exception now.
            if (is == null) {
                throw new FileNotFoundException("Keystore file '" + keystoreLocation + "' was not found.");
            }

            // Load up the keystore.
            KeyStore keystore = KeyStore.getInstance(storeType);
            keystore.load(is, keystorePassword.toCharArray());

            // Retrieve the key entry using the keyAlias
            if (keystore.containsAlias(keyAlias)) {
                Key keyEntry = keystore.getKey(keyAlias, keyPassword.toCharArray());
                if (keyEntry == null) {
                    throw new IllegalStateException("Could not retrieve encryption key for alias: " + keyAlias);
                }
                byte[] key = keyEntry.getEncoded();

                // Create our secret key object from the key's byte array stored in the keystore file.
                secretKey = new SecretKeySpec(key, keyAlgorithm);
            } else {
                throw new IllegalArgumentException("Keystore file does not contain the required key alias: " + keyAlias);
            }
            return secretKey;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
    
    /**
     * Determines whether or not the passed byte array was previously gzip compressed.
     * @param inputBytes - A byte array
     * @return boolean - true if the input stream is gzip compressed; false otherwise.
     */
    public static boolean isGzipCompressed(byte[] inputBytes) {
        int head = ((int) inputBytes[0] & 0xff) | ((inputBytes[1] << 8) & 0xff00);
        return (GZIPInputStream.GZIP_MAGIC == head);
    }
    
    /**
     * Performs a gzip compression of the passed byte array.
     * @param input - Any byte array
     * @return byte[] - A gzip'd byte array representation of the input byte array.
     * @throws IOException 
     * 
     */
    public static byte[] gzipCompress(byte[] input) throws IOException {
        
        Objects.requireNonNull(input, "input cannot be null");
                
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
         
        gzip = new GZIPOutputStream(byteOutputStream);
        gzip.write(input);
        gzip.close();
                
        return byteOutputStream.toByteArray();
    }
    
    /**
     * Decompresses a previously gzip'd compressed byte array.
     * If the input byte array is not gzip compressed, the input byte array is returned.
     * @param compressedInput - A byte array previously created by a gzip compression.
     * @return byte[] - The decompressed bytes.
     * @throws IOException 
     * 
     */
    public static byte[] gzipDecompress(byte[] compressedInput) throws IOException {
        byte[] output = compressedInput;
        Objects.requireNonNull(compressedInput, "compressedInput cannot be null");
        if (isGzipCompressed(compressedInput)) {
            try (GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(compressedInput))) {
            output = IOUtils.toByteArray(gzip);
            }
        }
                                
        return output;
    }

}
