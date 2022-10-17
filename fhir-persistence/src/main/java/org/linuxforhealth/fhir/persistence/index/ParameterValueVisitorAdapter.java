/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.persistence.index;

import java.math.BigDecimal;
import java.time.Instant;

import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;

/**
 * Used by a parameter value visitor to translate the parameter values
 * to a new form
 */
public interface ParameterValueVisitorAdapter {

    /**
     * Process a string parameter
     * 
     * @param name
     * @param valueString
     * @param compositeId
     * @param wholeSystem
     * @param maxBytes the maximum allowed size of input String in bytes
     */
    void stringValue(String name, String valueString, Integer compositeId, boolean wholeSystem, int maxBytes) throws FHIRPersistenceException;

    /**
     * Process a number parameter
     * 
     * @param name
     * @param valueNumber
     * @param valueNumberLow
     * @param valueNumberHigh
     * @param compositeId
     */
    void numberValue(String name, BigDecimal valueNumber, BigDecimal valueNumberLow, BigDecimal valueNumberHigh, Integer compositeId);

    /**
     * Process a date parameter
     * 
     * @param name
     * @param valueDateStart
     * @param valueDateEnd
     * @param compositeId
     * @param wholeSystem
     */
    void dateValue(String name, Instant valueDateStart, Instant valueDateEnd, Integer compositeId, boolean wholeSystem);

    /**
     * Process a token parameter
     * 
     * @param name
     * @param valueSystem
     * @param valueCode
     * @param compositeId
     * @param wholeSystem
     */
    void tokenValue(String name, String valueSystem, String valueCode, Integer compositeId, boolean wholeSystem);

    /**
     * Process a tag parameter
     * 
     * @param name
     * @param valueSystem
     * @param valueCode
     * @param compositeId
     * @param wholeSystem
     */
    void tagValue(String name, String valueSystem, String valueCode, boolean wholeSystem);

    /**
     * Process a profile parameter
     * 
     * @param name
     * @param url
     * @param version
     * @param fragment
     * @param wholeSystem
     */
    void profileValue(String name, String url, String version, String fragment, boolean wholeSystem);

    /**
     * Process a security parameter
     * 
     * @param name
     * @param valueSystem
     * @param valueCode
     * @param wholeSystem
     */
    void securityValue(String name, String valueSystem, String valueCode, boolean wholeSystem);
    
    /**
     * Process a quantity parameter
     * 
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
     * Process a location parameter
     * 
     * @param name
     * @param valueLatitude
     * @param valueLongitude
     * @param compositeId
     */
    void locationValue(String name, Double valueLatitude, Double valueLongitude, Integer compositeId);

    /**
     * Process a reference parameter
     * 
     * @param name
     * @param refResourceType
     * @param refLogicalId
     * @param refVersion
     * @param compositeId
     */
    void referenceValue(String name, String refResourceType, String refLogicalId, Integer refVersion, Integer compositeId);
}
