/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.util;

import java.util.Arrays;
import java.util.Base64;

/**
 * Envelope for holding the salt and hash together for convenience

 *
 */
public class SaltHash {

    private final byte[] salt;
    
    private final byte[] hash;
    
    /**
     * Public constructor
     * @param salt
     * @param hash
     */
    public SaltHash(byte[] salt, byte[] hash) {
        this.salt = salt;
        this.hash = hash;
    }

    /**
     * Public constructor
     * @param tuple
     */
    public SaltHash(String tuple) {
        String[] values = tuple.split(":");
        if (values.length != 2) {
            throw new IllegalArgumentException("String must be salt:hash encoded as two Base64 encoded strings");
        }
        
        salt = Base64.getDecoder().decode(values[0]);
        hash = Base64.getDecoder().decode(values[1]);
    }

    /**
     * Getter for the salt value
     * @return
     */
    public byte[] getSalt() {
        return this.salt;
    }

    /**
     * Getter for the hash (digest) value
     * @return
     */
    public byte[] getHash() {
        return this.hash;
    }

    @Override
    public boolean equals(Object other) {
        if (other != null && other instanceof SaltHash) {
            return Arrays.equals(this.salt, ((SaltHash) other).salt)
                    && Arrays.equals(this.hash, ((SaltHash) other).hash);
        }
        else {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(Base64.getEncoder().encodeToString(salt));
        result.append(":");
        result.append(Base64.getEncoder().encodeToString(hash));
        return result.toString();
    }
    
}
