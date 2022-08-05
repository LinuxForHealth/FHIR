/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.bucket.api;


/**
 * Describes an error which occured whilst processing a resource bundle
 */
public class ResourceBundleError {
    
    // The line number within the bundle on which the error occurred. First line is 0
    private final int lineNumber;

    // The text of the error
    private final String errorText;
    
    // The FHIR server response time, if we got that far
    private final Integer responseTimeMs;

    // The returned HTTP status code, if we made the call
    private final Integer httpStatusCode;
    
    // The returned HTTP status message, if we made the call
    private final String httpStatusText;

    /**
     * Public constructor
     * @param lineNumber
     * @param errorText
     * @param responseTimeMs
     * @param httpStatusCode
     * @param httpStatusText
     */
    public ResourceBundleError(int lineNumber, String errorText, Integer responseTimeMs, Integer httpStatusCode, String httpStatusText) {
        this.lineNumber = lineNumber;

        // Error text can't be null
        if (errorText != null && !errorText.isEmpty()) {
            this.errorText = errorText;
        } else {
            this.errorText = "<unknown>";
        }
        this.responseTimeMs = responseTimeMs;
        this.httpStatusCode = httpStatusCode;
        this.httpStatusText = httpStatusText;
    }

    /**
     * Constructor for errors which occur before the FHIR call is made and so there
     * won't be any HTTP error codes or response time.
     * @param lineNumber
     * @param errorText
     */
    public ResourceBundleError(int lineNumber, String errorText) {
        this.lineNumber = lineNumber;
        if (errorText != null && !errorText.isEmpty()) {
            this.errorText = errorText;
        } else {
            this.errorText = "<unknown>";
        }
        this.responseTimeMs = null;
        this.httpStatusCode = null;
        this.httpStatusText = null;
        
    }

    
    /**
     * @return the lineNumber
     */
    public int getLineNumber() {
        return lineNumber;
    }

    
    /**
     * @return the errorText
     */
    public String getErrorText() {
        return errorText;
    }

    
    /**
     * @return the responseTimeMs
     */
    public Integer getResponseTimeMs() {
        return responseTimeMs;
    }

    
    /**
     * @return the httpStatusCode
     */
    public Integer getHttpStatusCode() {
        return httpStatusCode;
    }

    
    /**
     * @return the httpStatusText
     */
    public String getHttpStatusText() {
        return httpStatusText;
    }
}