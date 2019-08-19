/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.CodeSystemDAO;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.ICodeSystemCache;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.IParameterNameCache;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.IParameterTypeCache;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.ParameterNameDAO;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.ParameterNormalizedDAO;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dto.IParameterVisitor;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;
import com.ibm.watsonhealth.fhir.schema.control.FhirSchemaConstants;

/**
 * Batch insert into the parameter values tables. Avoids having to create one stored procedure
 * per resource type, because it the row type array approach apparently won't work with dynamic
 * SQL (EXECUTE ... USING ...). Unfortunately this means we have more database round-trips, we
 * don't have a choice.
 * @author rarnold
 *
 */
public class ParameterVisitorBatchDAO implements IParameterVisitor, AutoCloseable {
    private static final Logger logger = Logger.getLogger(ParameterVisitorBatchDAO.class.getName());

    // the max number of rows we accumulate for a given statement before we submit the batch
    private final int batchSize;
    
    // FK to the logical resource for the parameters being added
    private final long logicalResourceId;
    
    // Maintainers: remember to close all statements in AutoCloseable#close()
    private final PreparedStatement strings;
    private int stringCount;
    
    private final PreparedStatement numbers;
    private int numberCount;
    
    private final PreparedStatement dates;
    private int dateCount;
    
    private final PreparedStatement tokens;
    private int tokenCount;
    
    private final PreparedStatement quantities;
    private int quantityCount;
    
    private final PreparedStatement locations;
    private int locationCount;

    // For looking up parameter name ids
    private final IParameterNameCache parameterNameCache;

    // For looking up code system ids
    private final ICodeSystemCache codeSystemCache;
    
    /**
     * Public constructor
     * @param c
     * @param resourceId
     */
    public ParameterVisitorBatchDAO(Connection c, String adminSchemaName, String tablePrefix, boolean multitenant, long logicalResourceId, int batchSize,
        IParameterNameCache pnc, ICodeSystemCache csc) throws SQLException {
        if (batchSize < 1) {
            throw new IllegalArgumentException("batchSize must be >= 1");
        }
        
        this.logicalResourceId = logicalResourceId;
        this.batchSize = batchSize;
        this.parameterNameCache = pnc;
        this.codeSystemCache = csc;

        String insert;
        insert = multitenant ? "INSERT INTO " + tablePrefix + "_str_values (mt_id, parameter_name_id, str_value, str_value_lcase, logical_resource_id) VALUES (" + adminSchemaName + ".sv_tenant_id,?,?,?,?)"
                : "INSERT INTO " + tablePrefix + "_str_values (parameter_name_id, str_value, str_value_lcase, logical_resource_id) VALUES (?,?,?,?)";
        strings = c.prepareStatement(insert);
        
        insert = multitenant ? "INSERT INTO " + tablePrefix + "_number_values (mt_id, parameter_name_id, number_value, logical_resource_id) VALUES (" + adminSchemaName + ".sv_tenant_id,?,?,?)"
                :"INSERT INTO " + tablePrefix + "_number_values (parameter_name_id, number_value, logical_resource_id) VALUES (?,?,?)";
        numbers = c.prepareStatement(insert);
        
        insert = multitenant ? "INSERT INTO " + tablePrefix + "_date_values (mt_id, parameter_name_id, date_value, date_start, date_end, logical_resource_id) VALUES (" + adminSchemaName + ".sv_tenant_id,?,?,?,?,?)"
                : "INSERT INTO " + tablePrefix + "_date_values (parameter_name_id, date_value, date_start, date_end, logical_resource_id) VALUES (?,?,?,?,?)";
        dates = c.prepareStatement(insert);
        
        insert = multitenant ? "INSERT INTO " + tablePrefix + "_token_values (mt_id, parameter_name_id, code_system_id, token_value, logical_resource_id) VALUES (" + adminSchemaName + ".sv_tenant_id,?,?,?,?)"
                : "INSERT INTO " + tablePrefix + "_token_values (parameter_name_id, code_system_id, token_value, logical_resource_id) VALUES (?,?,?,?)";
        tokens = c.prepareStatement(insert);
        
        insert = multitenant ? "INSERT INTO " + tablePrefix + "_quantity_values (mt_id, parameter_name_id, code_system_id, code, quantity_value, quantity_value_low, quantity_value_high, logical_resource_id) VALUES (" + adminSchemaName + ".sv_tenant_id,?,?,?,?,?,?,?)"
                : "INSERT INTO " + tablePrefix + "_quantity_values (parameter_name_id, code_system_id, code, quantity_value, quantity_value_low, quantity_value_high, logical_resource_id) VALUES (?,?,?,?,?,?,?)";
        quantities = c.prepareStatement(insert);
        
        insert = multitenant ? "INSERT INTO " + tablePrefix + "_latlng_values (mt_id, parameter_name_id, latitude_value, longitude_value, logical_resource_id) VALUES (" + adminSchemaName + ".sv_tenant_id,?,?,?,?)"
                : "INSERT INTO " + tablePrefix + "_latlng_values (parameter_name_id, latitude_value, longitude_value, logical_resource_id) VALUES (?,?,?,?)";
        locations = c.prepareStatement(insert);
    }

    /**
     * Look up the normalized id for the parameter, adding it to the parameter_names table
     * if it doesn't yet exist
     * @param parameterName
     * @return
     */
    protected int getParameterNameId(String parameterName) throws FHIRPersistenceException {
        return parameterNameCache.readOrAddParameterNameId(parameterName);
        
    }
    
    /**
     * Look up the code system
     * @param codeSystem
     * @return
     */
    protected int getCodeSystemId(String codeSystem) throws FHIRPersistenceException {
        return codeSystemCache.readOrAddCodeSystem(codeSystem);
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.persistence.jdbc.dto.IParameterVisitor#stringValue(java.lang.String, java.lang.String)
     */
    @Override
    public void stringValue(String parameterName, String value) throws FHIRPersistenceException {
        while (value != null && value.getBytes().length > FhirSchemaConstants.MAX_SEARCH_STRING_BYTES) {
            // keep chopping the string in half until its byte representation fits inside
            // the VARCHAR
            value = value.substring(0, value.length() / 2);
        }
        
        try {
            int parameterNameId = getParameterNameId(parameterName);
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("stringValue: " + parameterName + "[" + parameterNameId + "], " + value);
            }
            
            strings.setInt(1, parameterNameId);
            if (value != null) {
                
                strings.setString(2, value);
                strings.setString(3, value.toLowerCase());
            }
            else {
                strings.setNull(2, Types.VARCHAR);
                strings.setNull(3, Types.VARCHAR);
            }
            strings.setLong(4, logicalResourceId);
            strings.addBatch();
            
            if (++stringCount == this.batchSize) {
                strings.executeBatch();
                stringCount = 0;
            }
        }
        catch (SQLException x) {
            throw new FHIRPersistenceDataAccessException(parameterName + "=" + value, x);
        }
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.persistence.jdbc.dto.IParameterVisitor#numberValue(java.lang.String, double)
     */
    @Override
    public void numberValue(String parameterName, BigDecimal value, BigDecimal valueLow, BigDecimal valueHigh) throws FHIRPersistenceException {
        try {
            numbers.setInt(1, getParameterNameId(parameterName));
            numbers.setBigDecimal(2, value);
            numbers.setLong(3, logicalResourceId);
            numbers.addBatch();
        
            if (++numberCount == this.batchSize) {
                numbers.executeBatch();
                numberCount = 0;
            }
        }
        catch (SQLException x) {
            throw new FHIRPersistenceDataAccessException(parameterName + "={" + value + "}", x);
        }
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.persistence.jdbc.dto.IParameterVisitor#dateValue(java.lang.String, java.sql.Timestamp, java.sql.Timestamp, java.sql.Timestamp)
     */
    @Override
    public void dateValue(String parameterName, Timestamp date, Timestamp dateStart, Timestamp dateEnd) throws FHIRPersistenceException {
        try {
            dates.setInt(1, getParameterNameId(parameterName));
            dates.setTimestamp(2, date);
            dates.setTimestamp(3, dateStart);
            dates.setTimestamp(4, dateEnd);
            dates.setLong(5, logicalResourceId);
            dates.addBatch();
            
            if (++dateCount == this.batchSize) {
                dates.executeBatch();
                dateCount = 0;
            }
        }
        catch (SQLException x) {
            throw new FHIRPersistenceDataAccessException(parameterName + "={" + date + ", " + dateStart + ", " + dateEnd + "}", x);
        }
        
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.persistence.jdbc.dto.IParameterVisitor#tokenValue(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void tokenValue(String parameterName, String codeSystem, String tokenValue) throws FHIRPersistenceException {
        
        try {
            int parameterNameId = getParameterNameId(parameterName);
            int codeSystemId = getCodeSystemId(codeSystem);
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("tokenValue: " + parameterName + "[" + parameterNameId + "], " 
                        + codeSystem + "[" + codeSystemId + "], " + tokenValue);
            }
            
            tokens.setInt(1, parameterNameId);
            tokens.setInt(2, codeSystemId);
            tokens.setString(3, tokenValue);
            tokens.setLong(4, logicalResourceId);
            tokens.addBatch();
            
            if (++tokenCount == this.batchSize) {
                tokens.executeBatch();
                tokenCount = 0;
            }
        }
        catch (FHIRPersistenceDataAccessException x) {
            throw new FHIRPersistenceDataAccessException(parameterName + "=" + codeSystem + ":" + tokenValue, x);
        }
        catch (SQLException x) {
            throw new FHIRPersistenceDataAccessException(parameterName + "=" + codeSystem + ":" + tokenValue, x);
        }
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.persistence.jdbc.dto.IParameterVisitor#quantityValue(java.lang.String, java.lang.String, java.lang.String, java.lang.Double, java.lang.Double, java.lang.Double)
     */
    @Override
    public void quantityValue(String parameterName, String code, String codeSystem, BigDecimal quantityValue, BigDecimal quantityLow, BigDecimal quantityHigh) throws FHIRPersistenceException {

        // Skip anything with a null code
        if (code == null || code.isEmpty()) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("CODELESS QUANTITY (skipped): " + parameterName + "=" + code + ":" + codeSystem + "{" + quantityValue + ", " + quantityLow + ", " + quantityHigh + "}");
            }
        }
        else {
            try {
                quantities.setInt(1, getParameterNameId(parameterName));
                quantities.setInt(2, getCodeSystemId(codeSystem));
                quantities.setString(3, code);
                quantities.setBigDecimal(4, quantityValue);
                quantities.setBigDecimal(5, quantityLow);
                quantities.setBigDecimal(6, quantityHigh);
                quantities.setLong(7, logicalResourceId);
                quantities.addBatch();
                
                if (++quantityCount == batchSize) {
                    quantities.executeBatch();
                    quantityCount = 0;
                }
            }
            catch (FHIRPersistenceDataAccessException x) {
                // wrap the exception so we have more context about the parameter causing the problem
                throw new FHIRPersistenceDataAccessException(parameterName + "=" + code + ":" + codeSystem + "{" + quantityValue + ", " + quantityLow + ", " + quantityHigh + "}", x);
            }
            catch (SQLException x) {
                throw new FHIRPersistenceDataAccessException(parameterName + "=" + code + ":" + codeSystem + "{" + quantityValue + ", " + quantityLow + ", " + quantityHigh + "}", x);
            }
        }
        
    }
    
    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.persistence.jdbc.dto.IParameterVisitor#locationValue(java.lang.String, double, double)
     */
    @Override
    public void locationValue(String parameterName, double lat, double lng) throws FHIRPersistenceException {
        try {
            locations.setInt(1, getParameterNameId(parameterName));
            locations.setDouble(2, lat);
            locations.setDouble(3, lng);
            locations.addBatch();
            
            if (++locationCount == this.batchSize) {
                locations.executeBatch();
                locationCount = 0;
            }
        }
        catch (SQLException x) {
            throw new FHIRPersistenceDataAccessException(parameterName + "={" + lat + ", " + lng + "}", x);
        }
    }

    /* (non-Javadoc)
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() throws Exception {
        // flush any stragglers, remembering to reset each count because
        // close() should be idempotent.
        if (stringCount > 0) {
            strings.executeBatch();
            stringCount = 0;
        }
        
        if (numberCount > 0) {
            numbers.executeBatch();
            numberCount = 0;
        }
        
        if (dateCount > 0) {
            dates.executeBatch();
            dateCount = 0;
        }
        
        if (tokenCount > 0) {
            tokens.executeBatch();
            tokenCount = 0;
        }
        
        if (quantityCount > 0) {
            quantities.executeBatch();
            quantityCount = 0;
        }
        
        if (locationCount > 0) {
            locations.executeBatch();
            locationCount = 0;
        }
        
        closeStatement(strings);
        closeStatement(numbers);
        closeStatement(dates);
        closeStatement(tokens);
        closeStatement(quantities);
        closeStatement(locations);
    }
    
    /**
     * Quietly close the given statement
     * @param ps
     */
    private void closeStatement(PreparedStatement ps) {
        try {
            ps.close();
        }
        catch (SQLException x) {
            logger.warning("failed to close statement");
        }
    }


}
