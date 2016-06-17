/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.filter.enc;

import java.io.IOException;
import java.security.AlgorithmParameters;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * This is a wrapper for an incoming HTTP servlet request, which is responsible for
 * decrypting the request body and serving up the decrypted bytes on demand.
 * 
 * @author padams
 */
public class FHIRDecryptingRequestWrapper extends HttpServletRequestWrapper {

    private Cipher cipher;
    
    /**
     * Ctor which takes in the request, the encryption key and the initialization vector.
     * @param request the incoming HTTP servlet request
     * @param aesKey the AES encryption key to use for decrypting the request body
     * @param iv the initialization vector to be used when decrypting the request body
     * @throws Exception 
     */
    public FHIRDecryptingRequestWrapper(HttpServletRequest request, SecretKey aesKey, byte[] iv) throws Exception {
        super(request);
        
        // Create the cipher to be used to decrypt the body.
        AlgorithmParameters params = AlgorithmParameters.getInstance("AES", "IBMJCEFIPS");
        params.init(new IvParameterSpec(iv));
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "IBMJCEFIPS");
        cipher.init(Cipher.DECRYPT_MODE, aesKey, params);
    }

    /**
     * Returns the original request input stream wrapped in a CipherInputStream so that
     * the request data will be decrypted before it is returned to the user of the input stream.
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new FHIRDecryptingInputStream(new CipherInputStream(super.getInputStream(), cipher));
    }
}
