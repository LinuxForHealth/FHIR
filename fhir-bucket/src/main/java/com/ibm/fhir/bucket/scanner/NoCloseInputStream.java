/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.bucket.scanner;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

/**
 * Intercepts the close call so we can process the stream
 * using the FHIR resource parser
 */
public class NoCloseInputStream extends PushbackInputStream {
    
    /**
     * Public Constructor
     * @param delegate the delegate stream we are decorating
     */
    public NoCloseInputStream(InputStream delegate) {
        super(delegate);
    }
    

    @Override
    public void close() throws IOException {
        // Don't close
    }
}
