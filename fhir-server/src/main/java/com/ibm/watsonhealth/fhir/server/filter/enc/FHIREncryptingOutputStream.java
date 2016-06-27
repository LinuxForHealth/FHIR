/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.filter.enc;

import java.io.IOException;

import javax.crypto.CipherOutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

/**
 * This class is used to encrypt the servlet response body as the data is written.
 * It implements the required ServletOutputStream methods, and then delegates
 * to the wrapped CipherOutputStream for all the rest of the InputStream methods.
 * This allows us to insert this wrapper output stream implementation into 
 * the servlet container's pipeline and perform encryption in a streaming manner.
 * 
 * @author padams
 */
public class FHIREncryptingOutputStream extends ServletOutputStream {

    private CipherOutputStream cos;

    protected FHIREncryptingOutputStream() {
    }
    
    public FHIREncryptingOutputStream(CipherOutputStream cos) {
        this.cos = cos;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletOutputStream#isReady()
     */
    @Override
    public boolean isReady() {
        return true;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletOutputStream#setWriteListener(javax.servlet.WriteListener)
     */
    @Override
    public void setWriteListener(WriteListener writeListener) {
        throw new RuntimeException("The setWriteListener method is not implemented.");
    }

    /* (non-Javadoc)
     * @see java.io.OutputStream#write(int)
     */
    @Override
    public void write(int b) throws IOException {
        cos.write(b);
    }
    
    /* (non-Javadoc)
     * @see java.io.OutputStream#write(byte[])
     */
    @Override
    public void write(byte b[]) throws IOException {
        cos.write(b);
    }
    
    /* (non-Javadoc)
     * @see java.io.OutputStream#write(byte[],int,int)
     */
    @Override
    public void write(byte b[], int off, int len) throws IOException {
        cos.write(b, off, len);
    }
    
    /* (non-Javadoc)
     * @see java.io.OutputStream#flush()
     */
    @Override
    public void flush() throws IOException {
        cos.flush();
    }
    
    /* (non-Javadoc)
     * @see java.io.OutputStream#close()
     */
    @Override
    public void close() throws IOException {
        cos.close();
    }
}
