/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.filter.enc;

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
    
    private CipherInputStream cis;

    public FHIRDecryptingInputStream() {
    }
    
    public FHIRDecryptingInputStream(CipherInputStream cis) {
        this.cis = cis;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletInputStream#isFinished()
     */
    @Override
    public boolean isFinished() {
        try {
            return cis.available() == 0;
        } catch (Throwable t) {
            throw new RuntimeException("Unexpected error while calling CipherInputStream.available().", t);
        }
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
        return cis.read();
    }
    
    @Override
    public int read(byte b[]) throws IOException {
        return cis.read(b);
    }
    
    @Override
    public int read(byte b[], int off, int len) throws IOException {
        return cis.read(b, off, len);
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
