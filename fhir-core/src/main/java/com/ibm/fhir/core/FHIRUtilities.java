/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.security.Key;
import java.security.KeyStore;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.TimeZone;

import javax.crypto.spec.SecretKeySpec;


/**
 * A collection of miscellaneous utility functions used by the various fhir-*
 * projects.
 */
public class FHIRUtilities {
    
    // For R4, we transition to using java.time
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

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
     * Retrieves the current thread's stacktrace and returns it as a string.
     */
    public static String getCurrentStacktrace() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StringBuilder sb = new StringBuilder();
        sb.append("Current stacktrace:\n");
        for (StackTraceElement frame : stackTrace) {
            sb.append("\tat " + frame.getClassName() + "." + frame.getMethodName() + "(" + frame.getFileName() + ":"
                    + frame.getLineNumber() + ")\n");
        }

        return sb.toString();
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
        return new Timestamp(zdt.toInstant().toEpochMilli());
    }

    /**
     * Parse the UTC timestamp value
     * @param lastUpdated
     * @return
     */
    public static Instant convertToInstant(String lastUpdated) {
        return Instant.from(DATE_TIME_FORMATTER.parse(lastUpdated));
    }

    public static String formatTimestamp(Date date) {
        return timestampSimpleDateFormat.get().format(date);
    }

    /**
     * Retrieves an encryption key from the specified keystore file, using the
     * specified alias and password values.
     * 
     * @param keystoreLocation the name of the keystore file
     * @param keystorePassword the keystore's password
     * @param keyAlias         the alias name of the entry containing the desired
     *                         key
     * @param keyPassword      the password associated with the key's entry in the
     *                         keystore file
     * @return a SecretKeySpec object containing the AES encryption key retrieved
     *         from the keystore file
     */
    public static SecretKeySpec retrieveEncryptionKeyFromKeystore(String keystoreLocation, String keystorePassword,
            String keyAlias, String keyPassword, String storeType, String keyAlgorithm) throws Exception {
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

                // Create our secret key object from the key's byte array stored in the keystore
                // file.
                secretKey = new SecretKeySpec(key, keyAlgorithm);
            } else {
                throw new IllegalArgumentException(
                        "Keystore file does not contain the required key alias: " + keyAlias);
            }
            return secretKey;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

}
