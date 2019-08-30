/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.server.filter.enc;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.cxf.jaxrs.utils.HttpUtils;
import org.owasp.encoder.Encode;

/**
 * This is a wrapper for an incoming HTTP servlet request, which is responsible for decrypting the request body and
 * serving up the decrypted bytes on demand.
 * 
 * @author padams
 */
public class FHIRDecryptingRequestWrapper extends HttpServletRequestWrapper {
    private static final Logger log = Logger.getLogger(FHIRDecryptingRequestWrapper.class.getName());
    private static final String MIMETYPE_FORM_URLENCODED = "application/x-www-form-urlencoded";
    private static final String PARAMETER_DELIM = "&";
    private static final String ATTR_VALUE_DELIM = "=";

    // The Cipher object used for decrypting the request body.
    private Cipher cipher;

    // Our cache of request parameters.
    private Map<String, String[]> _parameters = null;

    /**
     * Ctor which takes in the original request, the encryption key and the initialization vector.
     * 
     * @param request
     *            the incoming HTTP servlet request to be wrapped
     * @param aesKey
     *            the AES encryption key to use for decrypting the request body
     * @param iv
     *            the initialization vector to be used when decrypting the request body
     * @throws Exception
     */
    public FHIRDecryptingRequestWrapper(HttpServletRequest request, SecretKey aesKey, byte[] iv) throws Exception {
        super(request);
        log.finer("Initializing new FHIRDecryptingRequestWrapper instance.");

        // Create the cipher to be used to decrypt the body.
        AlgorithmParameters params = AlgorithmParameters.getInstance("AES", "IBMJCEFIPS");
        params.init(new IvParameterSpec(iv));
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "IBMJCEFIPS");
        cipher.init(Cipher.DECRYPT_MODE, aesKey, params);
        log.finer("Initialized Cipher for AES decryption.");
    }

    /**
     * Returns the original request input stream wrapped in a CipherInputStream so that the request data will be
     * decrypted before it is returned to the user of the input stream.
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        log.entering(this.getClass().getName(), "getInputStream");
        try {
            log.finer("Returning instance of FHIRDecryptingInputStream.");
            return new FHIRDecryptingInputStream(new CipherInputStream(super.getInputStream(), cipher));
        } finally {
            log.exiting(this.getClass().getName(), "getInputStream");
        }
    }

    /**
     * Given that this wrapper class will only be used in a situation where our other servlet filter is being used, we
     * can safely short-circuit this method. (famous last words)
     */
    @Override
    public BufferedReader getReader() throws IOException {
        throw new UnsupportedOperationException("Method 'getReader' is not implemented!");
        // return delegate.getReader();
    }

    @Override
    public String getParameter(String name) {
        log.entering(this.getClass().getName(), "getParameter", new Object[]{ name });
        try {
            parseParameters();
            String value = null;
            String[] values = _parameters.get(name);
            if (values != null && values.length > 0) {
                value = values[0];
            }
            return value;
        } finally {
            log.exiting(this.getClass().getName(), "getParameter");
        }
    }

    /**
     * Returns all request parameters (query string parameters, plus form parameters for a form-based request) as a Map.
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        log.entering(this.getClass().getName(), "getParameterMap");
        try {
            parseParameters();
            return _parameters;
        } finally {
            log.exiting(this.getClass().getName(), "getParameterMap");
        }
    }

    @Override
    public Enumeration<String> getParameterNames() {
        log.entering(this.getClass().getName(), "getParameterNames");
        try {
            parseParameters();
            return ((Hashtable<String, String[]>) _parameters).keys();

        } finally {
            log.exiting(this.getClass().getName(), "getParameterNames");
        }
    }

    @Override
    public String[] getParameterValues(String name) {
        log.entering(this.getClass().getName(), "getParameterValues");
        try {
            parseParameters();
            return _parameters.get(name);
        } finally {
            log.exiting(this.getClass().getName(), "getParameterValues");
        }
    }

    /**
     * Initializes our internal _parameters map by retrieving request parameters from the query string and optionally
     * from the request body (in the case of a "form" request).
     */
    private void parseParameters() {
        if (_parameters != null) {
            return;
        }

        log.entering(this.getClass().getName(), "parseParameters");
        try {

            _parameters = new Hashtable<String, String[]>();

            // First, extract any parameters from the query string and add to our map.
            parseQueryParameters();

            // Next, if this is a POST request, then we'll need to check to see if we should
            // read form parameters from the request body.
            String method = getMethod();
            if (method.equalsIgnoreCase("post")) {
                String contentType = getContentType();

                // If the content-type indicates form parameters, then parse them.
                if (contentType != null && contentType.startsWith(MIMETYPE_FORM_URLENCODED)) {
                    parseFormParameters();
                }
            }
        } catch (Throwable t) {
            throw new RuntimeException("Error while parsing request parameters", t);
        } finally {
            log.exiting(this.getClass().getName(), "parseParameters");
        }
    }

    /**
     * Parses the parameters from the request body (i.e. form) and inserts them into our internal _parameters map.
     * 
     * @throws Exception
     */
    private void parseFormParameters() throws Exception {
        log.entering(this.getClass().getName(), "parseFormParameters");
        try {

            // Read the data from the request body.
            String formData = null;
            formData = getPostBody(getInputStream(), getCharacterEncoding());

            // Parse the form data into request parameters and add to our internal _parameters map.
            parseParametersInString(formData);
        } finally {
            log.exiting(this.getClass().getName(), "parseFormParameters");
        }
    }

    /**
     * Reads the body of a POST request that does not include the Content-Length header.
     * 
     * @param is
     *            the InputStream used to read bytes from the request body
     * @param encoding
     *            the character encoding associated with the request body
     * @return
     */
    private String getPostBody(ServletInputStream is, String encoding) {
        log.entering(this.getClass().getName(), "getPostBody(ServletInputStream,String)");
        try {
            String bodyString = null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);

            // We'll read a buffer at a time (until EOF) and add the
            // bytes to the BAOS.
            byte[] bodyBytes = new byte[2048];
            int bytesRead = 0;
            do {
                bytesRead = is.read(bodyBytes, 0, bodyBytes.length);
                if (bytesRead > 0) {
                    baos.write(bodyBytes, 0, bytesRead);
                }
            } while (bytesRead >= 0);
            is.close();

            // Now convert the BAOS to a String using 'encoding' if specified.
            byte[] baosBytes = baos.toByteArray();
            try {
                if (encoding != null) {
                    log.finer("Converting bytes to String using encoding: " + encoding);
                    bodyString = new String(baosBytes, encoding);
                } else {
                    bodyString = new String(baosBytes);
                    log.finer("Converting bytes to String using default encoding.");
                }
            } catch (UnsupportedEncodingException e) {
                log.warning("Encountered request body with unsupported encoding: " + encoding);
                bodyString = new String(baosBytes);
            }

            return bodyString;
        } catch (Throwable t) {
            throw new RuntimeException("Error reading request body", t);
        } finally {
            log.exiting(this.getClass().getName(), "getPostBody(ServletInputStream,String)");
        }
    }

    /**
     * Parses the request's query string and inserts the parameters into our internal _parameters map.
     */
    private void parseQueryParameters() {
        log.entering(this.getClass().getName(), "parseQueryParameters");
        try {
            String queryString = getQueryString();
            log.finer("Query string: " + (queryString != null ? Encode.forHtml(queryString) : "<null>"));
            parseParametersInString(queryString);
        } finally {
            log.exiting(this.getClass().getName(), "parseQueryParameters");
        }
    }

    /**
     * Parses request parameters contained in the specified 'queryString'.
     * 
     * @param queryString
     *            the string containing one or more request parameters
     */
    private void parseParametersInString(String queryString) {
        log.entering(this.getClass().getName(), "parseParametersInString");
        try {
            if (queryString != null && !queryString.isEmpty()) {
                if (log.isLoggable(Level.FINER)) {
                    log.finer("Parsing parameter string: " + Encode.forHtml(queryString));
                }
                String[] attrValuePairs = queryString.split(PARAMETER_DELIM);
                if (attrValuePairs != null && attrValuePairs.length > 0) {
                    for (String attrValuePair : attrValuePairs) {
                        String[] tokens = attrValuePair.split(ATTR_VALUE_DELIM);
                        if (tokens != null && tokens.length >= 2) {
                            String name = HttpUtils.urlDecode(tokens[0]);
                            String value = HttpUtils.urlDecode(tokens[1]);
                            addParameterValue(_parameters, name, value);
                            if (log.isLoggable(Level.FINER)) {
                                log.finer("Added query parameter to _parameters map: " + name + "=" + value);
                            }
                        }
                    }
                }
            }
        } finally {
            log.exiting(this.getClass().getName(), "parseParametersInString");
        }
    }

    /**
     * Adds a mapping for the specified name/value pair to the map. If the name already exists in the map, then the
     * value will be added to the end of the existing array of values for that key. Otherwise, we'll create a new map
     * entry for the name/value pair.
     */
    private void addParameterValue(Map<String, String[]> map, String name, String value) {
        String[] newArray = null;

        // If the map already contains an entry for this key,
        // then we'll just allocate a new value array and add the new
        // value to the end.
        if (map.containsKey(name)) {
            String[] oldArray = map.get(name);
            newArray = new String[oldArray.length + 1];
            for (int i = 0; i < oldArray.length; i++) {
                newArray[i] = oldArray[i];
            }
            newArray[oldArray.length] = value;
        }

        // Otherwise, we'll create a new array for this value.
        else {
            newArray = new String[1];
            newArray[0] = value;
        }

        // In either case, we now need to create or update the mapping.
        map.put(name, newArray);
    }
}
