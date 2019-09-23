/*
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.filter.enc;

import java.io.IOException;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * This is a wrapper for an outgoing  HTTP servlet response, which is responsible for
 * encrypting the request body and serving up the encrypted bytes on demand.
 * 
 * @author padams
 */
public class FHIREncryptingResponseWrapper extends HttpServletResponseWrapper {
    private static final Logger log = Logger.getLogger(FHIREncryptingResponseWrapper.class.getName());

    private Cipher cipher;
    private byte[] iv;
    
    public FHIREncryptingResponseWrapper(HttpServletResponse response, SecretKey aesKey) throws Exception {
        super(response);
        
        // Create the cipher to be used to encrypt the body and retrieve the random IV.
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "IBMJCEFIPS");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        iv = cipher.getIV();
        log.finer("Initialized Cipher for AES encryption.");
    }
    
    public byte[] getInitializationVector() {
        return iv;
    }

    /**
     * Returns the original request output stream wrapped in a CipherOutputStream so that
     * the response data will be encrypted before it is returned to the client application.
     */
    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        log.entering(this.getClass().getName(), "getOutputStream");
        try {
            return new FHIREncryptingOutputStream(new CipherOutputStream(super.getOutputStream(), cipher));
        } finally {
            log.exiting(this.getClass().getName(), "getOutputStream");
        }
    }
}
