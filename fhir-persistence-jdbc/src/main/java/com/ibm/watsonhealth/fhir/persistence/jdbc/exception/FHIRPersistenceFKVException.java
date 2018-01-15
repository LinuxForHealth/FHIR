/**
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.exception;

import javax.ws.rs.core.Response.Status;

/**
 * This exception class is thrown when Foreign Key violations are encountered while attempting to access data in the FHIR DB.
 * @author markd
 *
 */
public class FHIRPersistenceFKVException extends FHIRPersistenceDataAccessException {

    private static final long serialVersionUID = 4303342119803229856L;

    public FHIRPersistenceFKVException() {
        super();
    }

    public FHIRPersistenceFKVException(String message) {
        super(message);
        
    }

   public FHIRPersistenceFKVException(String message, Throwable cause) {
        super(message, cause);
    
    }

    public FHIRPersistenceFKVException(String message, Status httpStatus, Throwable cause) {
        super(message, httpStatus, cause);
       
    }

    public FHIRPersistenceFKVException(String message, Status httpStatus) {
        super(message, httpStatus);
        
    }

    public FHIRPersistenceFKVException(Throwable cause) {
        super(cause);
    
    }

}
