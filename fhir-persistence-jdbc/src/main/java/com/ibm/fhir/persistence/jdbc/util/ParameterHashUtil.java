/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util;


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import com.ibm.fhir.persistence.jdbc.dto.ExtractedParameterValue;

/**
 * Utility methods for generating Base64-encoded SHA-256 hash for search parameters.
 */
public class ParameterHashUtil {
    private static final Charset UTF_8 = StandardCharsets.UTF_8;
    private static final String SHA_256 = "SHA-256";
    private final Encoder encoder;
    private final MessageDigest digest;

    public ParameterHashUtil() {
        encoder = Base64.getEncoder();
        try {
            digest = MessageDigest.getInstance(SHA_256);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MessageDigest not found: " + SHA_256, e);
        }
    }

    /**
     * Gets a Base64-encoded SHA-256 hash for a list of ExtractedParameterValues.
     * This is used to quickly determine if the search parameters changed
     * from the existing values in the database, which can then be used to
     * avoid deleting and re-inserting the search parameters into the database.
     * @param parameters extracted search parameters
     * @return the Base64-encoded SHA-256 hash
     */
    public String getParametersHash(List<ExtractedParameterValue> parameterValues) {
        // Sort hashes to make it deterministic
        List<String> sortedList = new ArrayList<>(parameterValues.size());
        for (ExtractedParameterValue parameterValue : parameterValues) {
            sortedList.add(Objects.toString(parameterValue.getHash(this), ""));
        }
        sortedList.sort(Comparator.comparing(String::toString));

        StringBuilder sb = new StringBuilder("|");
        for (String hash : sortedList) {
            sb.append(hash).append("|");
        }

        byte[] hashBytes = digest.digest(sb.toString().getBytes(UTF_8));
        return bytesToB64(hashBytes);
    }

    /**
     * Gets a Base64-encoded SHA-256 hash for a name-value pair.
     * @param name the name
     * @param value the value
     * @return the Base64-encoded SHA-256 hash
     */
    public String getNameValueHash(String name, String value) {
        StringBuilder sb = new StringBuilder("[");
        sb.append(Objects.toString(name, "")).append("]=[").append(Objects.toString(value, "")).append("]");
        byte[] hashBytes = digest.digest(sb.toString().getBytes(UTF_8));
        return bytesToB64(hashBytes);
    }

    /**
     * Convert bytes to Base64-encoded string.
     * @param bytes the bytes
     * @return the Base64-encoded string
     */
    private String bytesToB64(byte[] bytes) {
        return new String(encoder.encode(bytes));
    }
}
