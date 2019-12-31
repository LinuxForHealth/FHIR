/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app.processor;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Base64.Encoder;

public class TenantUtil {
    // Random generator for new tenant keys and salts
    private static final SecureRandom random = new SecureRandom();

    /**
     * Generate a random 32 byte value encoded as a Base64 string (44 characters).
     * 
     * @return
     */
    public static String getRandomKey() {
        byte[] buffer = new byte[32];
        random.nextBytes(buffer);

        Encoder enc = Base64.getEncoder();
        return enc.encodeToString(buffer);
    }
}