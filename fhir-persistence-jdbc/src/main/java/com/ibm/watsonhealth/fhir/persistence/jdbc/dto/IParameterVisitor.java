/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.dto;

import java.math.BigDecimal;

import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;

/**
 * A visitor passed to the parameter visit method
 * @author rarnold
 *
 */
public interface IParameterVisitor {

    /**
     * Process a string parameter value
     * @param parameterName
     * @param value
     * @throws SQLException
     */
    void stringValue(String parameterName, String value, boolean isBase) throws FHIRPersistenceException;

    /**
     * Process a number parameter value
     * @param parameterName
     * @param value
     * @param valueLow
     * @param valueHigh
     * @throws SQLException
     */
    void numberValue(String parameterName, BigDecimal value, BigDecimal valueLow, BigDecimal valueHigh) throws FHIRPersistenceException;

    /**
     * Process a date parameter value
     * @param parameterName
     * @param date
     * @param dateStart
     * @param dateEnd
     * @throws SQLException
     */
    void dateValue(String parameterName, java.sql.Timestamp date, java.sql.Timestamp dateStart, java.sql.Timestamp dateEnd, boolean isBase) throws FHIRPersistenceException;

    /**
     * Process a token parameter value
     * @param parameterName
     * @param codeSystem
     * @param tokenValue
     * @throws SQLException
     */
    void tokenValue(String parameterName, String codeSystem, String tokenValue, boolean isBase) throws FHIRPersistenceException;

    /**
     * Process a quantity parameter value
     * @param parameterName
     * @param code
     * @param codeSystem
     * @param quantityValue
     * @param quantityLow
     * @param quantityHigh
     * @throws SQLException
     */
    void quantityValue(String parameterName, String code, String codeSystem, BigDecimal quantityValue, BigDecimal quantityLow, BigDecimal quantityHigh) throws FHIRPersistenceException;

    /**
     * Process a location parameter value
     * @param parameterName
     * @param lat
     * @param lng
     * @throws SQLException
     */
    void locationValue(String parameterName, double lat, double lng) throws FHIRPersistenceException;
}
