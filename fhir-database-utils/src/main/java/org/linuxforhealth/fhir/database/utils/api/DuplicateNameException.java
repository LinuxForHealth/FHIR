/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.api;

/**
 * The name of the object to be created is identical to an existing name.
 */
public class DuplicateNameException extends DataAccessException {
    // Generated serial number
    private static final long serialVersionUID = 4082017119186491651L;

    public DuplicateNameException(Throwable t) {
        super(t);
    }

    public DuplicateNameException(String msg, Throwable t) {
        super(msg, t);
    }
}
