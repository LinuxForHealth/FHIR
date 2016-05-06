/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.search.exception;

import com.ibm.watsonhealth.fhir.exception.FHIRException;

public class FHIRSearchException extends FHIRException {
    private static final long serialVersionUID = 1L;
    
    public FHIRSearchException() {
        super();
    }
    public FHIRSearchException(String message) {
        super(message);
    }
    
    public FHIRSearchException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public FHIRSearchException(Throwable cause) {
        super(cause);
    }
}
