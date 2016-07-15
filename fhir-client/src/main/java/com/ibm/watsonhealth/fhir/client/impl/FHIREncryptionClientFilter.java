/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.client.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.AlgorithmParameters;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import javax.xml.bind.DatatypeConverter;

/**
 * JAX-RS client-side writer and reader interceptors for encrypting/decrypting REST API requests/responses.
 */
public class FHIREncryptionClientFilter implements WriterInterceptor, ReaderInterceptor {
    
    private static final String ENCRYPTION_ALGORITHM_FULL = "AES/CBC/PKCS5Padding";
    private static final String ENCRYPTION_ALGORITHM = "AES";
    private static final String ENCRYPTION_PROVIDER = "IBMJCEFIPS";
    private static final String ENCRYPTION_TOKEN = "aescbc256";
    private static final String HTTPHDR_CONTENT_ENCODING = "Content-Encoding";
    private static final String HTTPHDR_ACCEPT_ENCODING = "Accept-Encoding";
    private static final String HTTPHDR_IV = "AES-Salt";

    private SecretKey aesKey;

    public FHIREncryptionClientFilter(SecretKeySpec aesKey) throws Exception {
        this.aesKey = aesKey;
        if (aesKey == null) {
            throw new IllegalArgumentException("Encryption key must be non-null.");
        }
    }

    /**
     * This function is invoked for an outbound request. We'll wrap the existing output stream with a CipherOutputStream
     * and add the required headers to the outgoing request.
     */
    @Override
    public void aroundWriteTo(WriterInterceptorContext ctxt) throws IOException, WebApplicationException {
        try {
            // System.out.println("Inside aroundWriteTo()");
            // Create the cipher to be used to encrypt the body.
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM_FULL, ENCRYPTION_PROVIDER);
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);

            // Retrieve the initialization vector so we can set the AES-Salt header.
            byte[] encryptIV = cipher.getIV();
            String iv = DatatypeConverter.printHexBinary(encryptIV);

            // Set the outbound HTTP request headers.
            ctxt.getHeaders().putSingle(HTTPHDR_CONTENT_ENCODING, ENCRYPTION_TOKEN);
            ctxt.getHeaders().putSingle(HTTPHDR_ACCEPT_ENCODING, ENCRYPTION_TOKEN);
            ctxt.getHeaders().putSingle(HTTPHDR_IV, iv);

            // Wrap the jax-rs output stream with one which will accumulates the bytes and then encrypt
            // them when close() is called.
            OutputStream ebaos = new EncryptingByteArrayOutputStream(ctxt, ctxt.getOutputStream(), cipher);

            ctxt.setOutputStream(ebaos);
            ctxt.proceed();
        } catch (Throwable t) {
            throw new WebApplicationException(t);
        }
    }

    /**
     * This function is invoked for an inbound response. We'll wrap the existing input stream with a CipherInputStream.
     */
    @Override
    public Object aroundReadFrom(ReaderInterceptorContext ctxt) throws IOException, WebApplicationException {
        try {
            String contentEncoding = ctxt.getHeaders().getFirst(HTTPHDR_CONTENT_ENCODING);

            // If the Content-Encoding header indicates that the response body is encrypted,
            // then we'll need to wrap the existing input stream.
            if (contentEncoding != null && contentEncoding.contains(ENCRYPTION_TOKEN)) {
                // Retrieve the IV from the AES-Salt header.
                String ivString = ctxt.getHeaders().getFirst(HTTPHDR_IV);
                byte[] iv = DatatypeConverter.parseHexBinary(ivString);
                AlgorithmParameters params = AlgorithmParameters.getInstance(ENCRYPTION_ALGORITHM, ENCRYPTION_PROVIDER);
                params.init(new IvParameterSpec(iv));

                // Initialize the Cipher object for decryption.
                Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM_FULL, ENCRYPTION_PROVIDER);
                cipher.init(Cipher.DECRYPT_MODE, aesKey, params);

                // Set the input stream.
                ctxt.setInputStream(new CipherInputStream(ctxt.getInputStream(), cipher));
            }
            return ctxt.proceed();
        } catch (Throwable t) {
            throw new RuntimeException("Unexpected error in client response filter.", t);
        }
    }

    public static class EncryptingByteArrayOutputStream extends OutputStream {

        private WriterInterceptorContext ctxt;
        private OutputStream os;
        private ByteArrayOutputStream baos;
        private Cipher cipher;
        private boolean wasClosed = false;
        private int bytesProcessed = 0;

        public EncryptingByteArrayOutputStream(WriterInterceptorContext ctxt, OutputStream os, Cipher cipher) {
            this.ctxt = ctxt;
            this.os = os;
            this.cipher = cipher;
            this.baos = new ByteArrayOutputStream();
        }

        public void write(int b) throws IOException {
            baos.write(b);
        }

        public void write(byte[] b) throws IOException {
            baos.write(b);
        }

        public void write(byte[] b, int off, int len) throws IOException {
            baos.write(b, off, len);
        }

        public void flush() throws IOException {
            // System.out.println("Inside flush()!!! current output size: " + baos.toByteArray().length);
            close();
        }

        /**
         * Time to encrypt the plaintext bytes (baos) and then write the ciphertext (encrypted bytes) to the original
         * output stream. then set the Content-Length header appropriately.
         */
        public void close() throws IOException {
            byte[] plainText = baos.toByteArray();
            int currentBytes = plainText.length;
            // System.out.println("Inside close(), current accumulated bytes = " + currentBytes);
            
            // Guard against a second close that contains additional data.
            if (wasClosed) {
                if (currentBytes > bytesProcessed) {
                    throw new IOException("close() called again after more data added to output stream!");
                } else {
                    return;
                }
            }
            
            try {
                wasClosed = true;
                bytesProcessed = currentBytes;
                
                // System.out.println("Encrypting " + plainText.length + " bytes.");
                byte[] cipherText = cipher.doFinal(plainText);
                os.write(cipherText);
                
                String len = Integer.toString(cipherText.length);
                ctxt.getHeaders().add("Content-Length", len);
                // System.out.println("Set Content-Length to " + len);
                
                os.flush();
                os.close();
                // System.out.println("Closed jax-rs OutputStream.");
            } catch (Throwable t) {
                throw new IOException("Error while encrypting data: " + t);
            }
        }
    }
}
