/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.index;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Used by a parameter value visitor to translate the parameter values
 * to a new form
 */
public interface ParameterValueVisitorAdapter {

    /**
     * @param name
     * @param valueString
     * @param compositeId
     * @param wholeSystem
     */
    void stringValue(String name, String valueString, Integer compositeId, boolean wholeSystem);

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
     * @param wholeSystem
     */
    void dateValue(String name, Instant valueDateStart, Instant valueDateEnd, Integer compositeId, boolean wholeSystem);

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
     * @param compositeId
     * @param wholeSystem
     */
    void tagValue(String name, String valueSystem, String valueCode, boolean wholeSystem);

    /**
     * @param name
     * @param url
     * @param version
     * @param fragment
     * @param wholeSystem
     */
    void profileValue(String name, String url, String version, String fragment, boolean wholeSystem);

    /**
     * @param name
     * @param valueSystem
     * @param valueCode
     * @param wholeSystem
     */
    void securityValue(String name, String valueSystem, String valueCode, boolean wholeSystem);
    
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
     * @param refResourceType
     * @param refLogicalId
     * @param refVersion
     * @param compositeId
     */
    void referenceValue(String name, String refResourceType, String refLogicalId, Integer refVersion, Integer compositeId);
}
