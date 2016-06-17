/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.filter.enc;

import static com.ibm.watsonhealth.fhir.server.helper.FHIRServerUtils.getJNDIValue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import javax.xml.bind.DatatypeConverter;

//@formatter:off
/**
 * This class is a servlet filter which support the encryption/decryption of HTTP requests and responses.
 *
 * Use case:
 * A REST API consumer encrypts the body of their HTTP request (perhaps via a JAX-RS ClientRequestFilter 
 * implementation), and sets the following http headers in the HTTP request:
 * <ol>
 * <li>"Content-Encoding" should be set to the value "aescbc256" to indicate that the body
 * is encrypted with the AES algorithm (in CBC mode) using a 256-bit encryption key</li>
 * <li>"AES-Salt" should contain the 16-byte initialization vector (IV) used to encrypt the body.  This
 * byte sequence should be specified in 2-digit hex format (e.g. 000102030405060708090a0b0c0d0e0f).</li>
 * <li>[optional] "Accept-Encoding" should be set to "aescbc256" if the API consumer wants the response body
 * to be encrypted using the same encryption key.   The server will set the "AES-Salt" header to the 
 * 16-byte IV used to encrypt the response body.</li>
 * </ul>
 * 
 * The encryption key is generated or otherwise obtained by the deployer of the FHIR Server and is shared
 * with the API consumer (i.e. both parties must be using the same 256-bit AES encryption key).
 * 
 * For incoming reqeusts, this servlet filter will honor the Content-Encoding header value by decrypting 
 * the incoming HTTP request body using the AES/CBC/PKCS5Padding algorthm (IBMJCEFIPS provider) along with the
 * configured 256-bit encryption key and the IV found in the AES-Salt request header of the HTTP request, 
 * and then will provide the decrypted data to downstream filters and servlets (e.g. the JAX-RS servlet).
 * 
 * On the response path, this filter will honor the Accept-Encoding request header value by 
 * encrypting the response body using the same encryption algorithm, configured encryption key and a 
 * randomly-generated IV.  The IV will be specified in the AES-Salt response header. 
 * The encrypted response data will then be transmitted by the servlet container back to the API consumer,
 * which would then decrypt the response body (perhaps via a JAX-RS ClientResponseFilter).
 * 
 * Configuration:
 * To enable this filter, the 256-bit AES encryption key should be placed in a text file.
 * The encryption key should be stored as a sequence of bytes in 2-digit hex form
 * Here is an example of a file containing the AES 256-bit encryption key:
 * 1008416D24C518E240A6172BAF82402B35B24E085905CA1FD552207AB441BD65
 * 
 * The name of this file should then be configured in the liberty server.xml file with a jndi entry
 * like the following:
 * <jndiEntry id="encryptionConfig" jndiName="com.ibm.watsonhealth.fhir.encryption.aes.config" value="config/aesKey.txt"/>
 * 
 * @author padams
 */
//@formatter:on
public class FHIRAesEncryptionFilter implements Filter {
    private static final Logger log = Logger.getLogger(FHIRAesEncryptionFilter.class.getName());

    private static final String HEADERNAME_IVSTRING = "AES-Salt";
    private static final String JNDINAME_AESCONFIG = "com.ibm.watsonhealth.fhir.encryption.aes.config";
    private static final String ENCODING_ENCRYPTION_VALUE = "aescbc256";

    // This is our 256-bit AES encryption key.
    private SecretKeySpec aesKey;

    private boolean enabled = false;

    /*
     * (non-Javadoc)
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse,
     * javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.entering(this.getClass().getName(), "doFilter");

        try {
            boolean active = false;
            
            // For an incoming HTTP request we'll check various headers to determine
            // if we need to encrypt/decrypt the request and/or response body.
            if (enabled && request instanceof HttpServletRequest) {

                // If the request body is encrypted, then we'll wrap it with
                // our decrypting request wrapper.
                if (requestIsEncrypted((HttpServletRequest) request)) {
                    active = true;
                    // Retrieve the IV string from the request.
                    // The caller SHOULD have set it in the AES-Salt request header.
                    String ivString = getInitializationVector((HttpServletRequest) request);
                    byte[] iv = hexToBytes(ivString);
                    if (iv.length != 16) {
                        throw new ServletException("Initialization Vector must be 16 bytes in length.");
                    }
                    request = new FHIRDecryptingRequestWrapper((HttpServletRequest) request, aesKey, iv);
                }

                // If the response should be encrypted, then we'll wrap it with
                // our encrypting response wrapper.
                if (responseEncryptionRequired((HttpServletRequest) request)) {
                    active = true;
                    response = new FHIREncryptingResponseWrapper((HttpServletResponse) response, aesKey);

                    // We also need to retrieve the IV used by the cipher so we can set the AES-Salt response header.
                    byte[] iv = ((FHIREncryptingResponseWrapper) response).getInitializationVector();
                    ((HttpServletResponse) response).setHeader(HEADERNAME_IVSTRING, bytesToHex(iv));

                    // Also set the Content-Encoding response header to indicate that the response body is encrypted.
                    ((HttpServletResponse) response).setHeader(HttpHeaders.CONTENT_ENCODING, ENCODING_ENCRYPTION_VALUE);
                }
            }

            // Pass the request through to the next filter in the chain.
            if (active) {
                log.fine("Calling downstream filter chain...");
            }
            
            // Pass the request through to the next filter in the chain.
            chain.doFilter(request, response);

            if (active) {
                log.fine("Returned from downstream filter chain...");
            }
        } catch (Throwable t) {
            throw new ServletException("Unexpected error occurred while processing servlet request.", t);
        } finally {
            log.exiting(this.getClass().getName(), "doFilter");
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
        // Nothing to do here...
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(FilterConfig config) throws ServletException {
        log.entering(this.getClass().getName(), "init");

        try {
            // Read in the config file that contains the AES encryption key.
            String aesConfig = getJNDIValue(JNDINAME_AESCONFIG, null);
            if (aesConfig != null && !aesConfig.isEmpty()) {
                List<String> lines = Files.readAllLines(Paths.get(aesConfig));
                if (lines == null || lines.isEmpty()) {
                    throw new IllegalArgumentException("The AES config file does not contain the encryption key.");
                }

                // The first line is the AES 256-bit encryption key in hex format.
                // We need to create a SecretKey from this.
                if (lines.get(0).length() != 64) {
                    throw new IllegalArgumentException("The 256-bit AES encryption key must be a 32 byte value in 2-digit hex format.");
                }
                aesKey = new SecretKeySpec(hexToBytes(lines.get(0)), "AES");

                // Set our flag to indicate we're enabled for encryption/decryption.
                enabled = true;
                log.fine("Encryption filter is enabled");
            } else {
                log.fine("Encryption filter is diabled");
            }
        } catch (Throwable t) {
            throw new ServletException("Error during servlet filter initialization.", t);
        } finally {
            log.exiting(this.getClass().getName(), "init");
        }
    }

    /**
     * Retrieves the 'AES-Salt' header value which should contain the IV required to decrypt the request body.
     * 
     * @param request
     *            the incoming HTTP servlet request
     * @throws ServletException
     */
    private String getInitializationVector(HttpServletRequest request) throws ServletException {
        String iv = request.getHeader(HEADERNAME_IVSTRING);
        if (iv == null || iv.isEmpty()) {
            throw new ServletException("Initialization Vector should be present in the '" + HEADERNAME_IVSTRING + "' request header.");
        }
        return iv;
    }

    /**
     * Returns true iff the specified request indicates that its request body is encrypted. To determine that, we'll
     * look at the Content-Encoding header and check to see if it contains the "aescbc256" value.
     * 
     * @param request
     *            the incoming HTTP request
     */
    private boolean requestIsEncrypted(HttpServletRequest request) {
        String encoding = request.getHeader(HttpHeaders.CONTENT_ENCODING);
        return (encoding != null ? encoding.toLowerCase().contains(ENCODING_ENCRYPTION_VALUE) : false);
    }

    /**
     * Returns true iff the specified request indicates that the corresponding response should be encrypted. To
     * determine that, we'll look at the Accept-Encoding header and check to see if it contains the "aescbc256" value.
     * 
     * @param request
     *            the incoming HTTP request
     */
    private boolean responseEncryptionRequired(HttpServletRequest request) {
        String encoding = request.getHeader(HttpHeaders.ACCEPT_ENCODING);
        return (encoding != null ? encoding.toLowerCase().contains(ENCODING_ENCRYPTION_VALUE) : false);
    }

    /**
     * Converts the specified byte array to an equivalent sequence of 2-digit hex values.
     * 
     * @param bytes
     *            the byte array to convert
     * @return a String containing the hex-formatted byte values
     */
    private String bytesToHex(byte[] bytes) {
        return DatatypeConverter.printHexBinary(bytes);
    }

    /*
     * Converts the input string (formatted as a stream of 2-digit hex values) into an equivalent byte array.
     */
    private byte[] hexToBytes(String hexString) {
        return DatatypeConverter.parseHexBinary(hexString);
    }
}
