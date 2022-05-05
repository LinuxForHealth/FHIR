/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.index;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.ibm.fhir.search.util.ReferenceValue;

/**
 * Used by a parameter value visitor to translate the parameter values
 * to a new form
 */
public interface ParameterValueVisitorAdapter {

    /**
     * @param name
     * @param valueString
     * @param compositeId
     */
    void stringValue(String name, String valueString, Integer compositeId);

    /**
     * @param name
     * @param valueNumber
     * @param valueNumberLow
     * @param valueNumberHigh
     * @param compositeId
     */
    void numberValue(String name, BigDecimal valueNumber, BigDecimal valueNumberLow, BigDecimal valueNumberHigh, Integer compositeId);

    /**
     * @param name
     * @param valueDateStart
     * @param valueDateEnd
     * @param compositeId
     */
    void dateValue(String name, Timestamp valueDateStart, Timestamp valueDateEnd, Integer compositeId);

    /**
     * @param name
     * @param valueSystem
     * @param valueCode
     * @param compositeId
     */
    void tokenValue(String name, String valueSystem, String valueCode, Integer compositeId);

    /**
     * @param name
     * @param valueSystem
     * @param valueCode
     * @param valueNumber
     * @param valueNumberLow
     * @param valueNumberHigh
     * @param compositeId
     */
    void quantityValue(String name, String valueSystem, String valueCode, BigDecimal valueNumber, BigDecimal valueNumberLow, BigDecimal valueNumberHigh,
        Integer compositeId);

    /**
     * @param name
     * @param valueLatitude
     * @param valueLongitude
     * @param compositeId
     */
    void locationValue(String name, Double valueLatitude, Double valueLongitude, Integer compositeId);

    /**
     * @param name
     * @param refValue
     * @param compositeId
     */
    void referenceValue(String name, ReferenceValue refValue, Integer compositeId);
}
