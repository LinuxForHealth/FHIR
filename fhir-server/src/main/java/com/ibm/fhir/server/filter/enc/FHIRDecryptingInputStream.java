/*
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.filter.enc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.crypto.CipherInputStream;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

/**
 * This class is used to decrypt the servlet request body as the data is read.
 * It implements the required ServletInputStream methods, and then delegates
 * to the wrapped CipherInputStream for all the rest of the InputStream methods.
 * This allows us to insert this wrapper input stream implementation into 
 * the servlet container's pipeline and perform decryption in a streaming manner.
 * 
 * @author padams
 */
public class FHIRDecryptingInputStream extends ServletInputStream {
    
    private static int EOF = -1;

    private CipherInputStream cis;
    
    // Used for tracing only
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();

    protected FHIRDecryptingInputStream() {
    }
    
    public FHIRDecryptingInputStream(CipherInputStream cis) {
        this.cis = cis;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletInputStream#isFinished()
     */
    @Override
    public boolean isFinished() {
        return false;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletInputStream#isReady()
     */
    @Override
    public boolean isReady() {
        return true;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletInputStream#setReadListener(javax.servlet.ReadListener)
     */
    @Override
    public void setReadListener(ReadListener readListener) {
        throw new RuntimeException("The setReadListener method is not implemented.");
    }

    /* (non-Javadoc)
     * @see java.io.InputStream#read()
     */
    @Override
    public int read() throws IOException {
        int b = cis.read();
        if (b != EOF) {
            baos.write(b);
        }
        return b;
    }
    
    @Override
    public int read(byte b[]) throws IOException {
        int n = cis.read(b);
        if (n != EOF) {
            baos.write(b, 0, n);
        }
        return n;
    }
    
    @Override
    public int read(byte b[], int off, int len) throws IOException {
        int n = cis.read(b, off, len);
        if (n != EOF) {
            baos.write(b, off, n);
        }
        return n;
    }
    
    @Override
    public long skip(long n) throws IOException {
        return cis.skip(n);
    }
    
    @Override
    public int available() throws IOException {
        return cis.available();
    }
    
    @Override
    public void close() throws IOException {
        // String readBuf = baos.toString();
        cis.close();
    }
    
    @Override
    public synchronized void mark(int readlimit) {
        cis.mark(readlimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        cis.reset();
    }

    @Override
    public boolean markSupported() {
        return cis.markSupported();
    }
}
