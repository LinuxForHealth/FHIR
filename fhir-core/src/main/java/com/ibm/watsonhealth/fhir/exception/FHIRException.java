/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.exception;

import java.util.UUID;

import javax.ws.rs.core.Response.Status;

/**
 * Common FHIR Server exception base class.
 */
public class FHIRException extends Exception {
    private static final long serialVersionUID = 1L;
    
    private Status httpStatus = null;
    private String uniqueId = null;
    
    public FHIRException() {
        super();
    }

    public FHIRException(String message) {
        super(message);
    }

    public FHIRException(String message, Throwable cause) {
        super(message, cause);
    }

    public FHIRException(String message, Status httpStatus, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }
    public FHIRException(Throwable cause) {
        super(cause);
    }


    public FHIRException(String message, Status httpStatus) {
        this(message, httpStatus, null);
    }

    public Status getHttpStatus() {
        return (httpStatus != null ? httpStatus : Status.BAD_REQUEST);
    }

	public String getUniqueId() {
		if (this.uniqueId == null) {
			this.uniqueId = UUID.randomUUID().toString();
		}
		return uniqueId;
	}
	
	@Override
	public String getMessage() {
		String superMsg = super.getMessage();
		StringBuilder myMsg = new StringBuilder();
		if(superMsg != null && !superMsg.isEmpty()) {
			myMsg.append(super.getMessage()).append("  FHIRException.uniqueId=").append(this.getUniqueId());
		}
		else {
			myMsg.append("FHIRException.uniqueId=").append(this.getUniqueId());
		}
		return myMsg.toString();
	}
}
