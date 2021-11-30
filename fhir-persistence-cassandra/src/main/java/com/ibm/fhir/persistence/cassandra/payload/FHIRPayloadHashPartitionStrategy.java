/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.cassandra.payload;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.annotation.concurrent.NotThreadSafe;

import com.ibm.fhir.persistence.payload.FHIRPayloadPartitionStrategy;


/**
 * Uses a hash function to derive the partition name
 */
@NotThreadSafe
public class FHIRPayloadHashPartitionStrategy implements FHIRPayloadPartitionStrategy {
    private static final String SHA_256 = "SHA-256";

    // The message digest function
    private final MessageDigest digester;

    // The number of digits to truncate the hash result (base64 string)
    private final int base64Digits;

    /**
     * Public constructor
     * @param base64Digits how many Base64 digits to truncate the hash to
     */
    public FHIRPayloadHashPartitionStrategy(int base64Digits) {
        if (base64Digits < 1) {
            throw new IllegalArgumentException("base64Digits cannot be less than 1");
        }

        // Base64 is 6 bits per digit
        // SHA-256 yields 32 bytes => 32 * 8 / 6 == 42.6 or 43 digits of content
        // Because of Base64 padding, the actual returned length will be 44
        // but the last digit does not contain any information
        if (base64Digits > 43) {
            throw new IllegalArgumentException("base64Digits cannot be more than 43");
        }
        this.base64Digits = base64Digits;
        try {
            this.digester = MessageDigest.getInstance(SHA_256);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MessageDigest not found: " + SHA_256, e);
        }
    }
    
    @Override
    public String getPartitionName(String resourceType, String logicalId) {

        final String result;
        try {
            digester.update(resourceType.getBytes(StandardCharsets.UTF_8));
            digester.update(logicalId.getBytes(StandardCharsets.UTF_8));
            byte[] hash = digester.digest();
            result = Base64.getEncoder().encodeToString(hash).substring(0, this.base64Digits);
        } finally {
            digester.reset();
        }
        return result;
    }
}