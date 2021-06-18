/*
 * (C) Copyright IBM Corp. 2017, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dto;

import java.util.Objects;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.util.ParameterHashUtil;

/**
 * Not used
 */
@Deprecated
public class UriParmVal extends ExtractedParameterValue {

    private String valueString;

    /**
     * Public constructor
     */
    public UriParmVal() {
        super();
    }

    public String getValueString() {
        return valueString;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
    }

    /**
     * We know our type, so we can call the correct method on the visitor
     */
    @Override
    public void accept(ExtractedParameterValueVisitor visitor) throws FHIRPersistenceException {
//        visitor.visit(this);
    }

    @Override
    public String getHash(ParameterHashUtil parameterHashUtil) {
        return parameterHashUtil.getNameValueHash(getHashHeader(), Objects.toString(valueString, ""));
    }
}