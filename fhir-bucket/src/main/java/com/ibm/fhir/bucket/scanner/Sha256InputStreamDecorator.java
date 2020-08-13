/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.scanner;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Decorates an InputStream with the ability to compute a SHA-256 hash
 * of the bytes read from the stream. Assumes that the entire stream is
 * read. If buffering is done further up-stream, then obviously the digest
 * may not represent what was actually read by the consumer, because the
 * buffer may read bytes from this stream than were actually consumed.
 * Caveat emptor.
 */
public class Sha256InputStreamDecorator extends InputStream {
    private static final String SHA_256 = "SHA-256";
    
    // The digest being accumulated as the stream is read
    private final MessageDigest digest;
    
    // The InputStream under decoration
    private final InputStream delegate;
    
    /**
     * Public Constructor
     * @param delegate the delegate stream we are decorating
     */
    public Sha256InputStreamDecorator(InputStream delegate) {
        this.delegate = delegate;
        
        try {
            this.digest = MessageDigest.getInstance(SHA_256);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MessageDigest not found: " + SHA_256, e);
        }
    }
    
    /**
     * Compute the digest. Resets the state
     * @return
     */
    public String getBase64Hash() {
        byte[] hash = digest.digest();
        return Base64.getEncoder().encodeToString(hash);
    }
    
    @Override
    public int read() throws IOException {
        int result = delegate.read();
        
        if (result >= 0) {
            digest.update((byte)result);
        }
        
        return result;
    }

    @Override
    public int read(byte b[], int off, int len) throws IOException {
        // note that we're calling the delegate read here, not super.read. This
        // is because we don't want to be calling our read() for every byte,
        // which would be double-counting.
        int result = delegate.read(b, off, len);
        
        if (result > 0) {
            digest.update(b, off, result);
        }
        return result;
    }
    
    @Override
    public void close() throws IOException {
        this.delegate.close();
    }
}
