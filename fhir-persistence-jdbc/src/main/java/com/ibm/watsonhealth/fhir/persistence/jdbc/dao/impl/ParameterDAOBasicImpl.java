/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.ParameterDAO;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dto.Parameter;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDBConnectException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;

/**
 * This Data Access Object class provides method implementations for creating, updating, and retrieving rows in the FHIR Parameter table.
 * @author markd
 *
 */
public class ParameterDAOBasicImpl extends FHIRDbDAOBasicImpl<Parameter> implements ParameterDAO {
    
    private static final Logger log = Logger.getLogger(ParameterDAOBasicImpl.class.getName());
    private static final String CLASSNAME = ParameterDAOBasicImpl.class.getName(); 
    
    private static final String SQL_INSERT = "INSERT INTO PARAMETER (NAME, RESOURCE_TYPE, VALUE_CODE, VALUE_DATE, VALUE_DATE_END, VALUE_DATE_START, " +
                                                "VALUE_LATITUDE, VALUE_LONGITUDE, VALUE_NUMBER, VALUE_NUMBER_HIGH, VALUE_NUMBER_LOW, " +
                                                "VALUE_STRING, VALUE_SYSTEM, RESOURCE_ID) " + 
                                                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    
    private static final String SQL_DELETE_BY_RESOURCE_ID = "DELETE FROM PARAMETER WHERE RESOURCE_ID = ?";
    
    
    /**
     * Constructs a DAO instance suitable for acquiring connections from a JDBC Datasource object.
     */
    public ParameterDAOBasicImpl() {
        super();
    }
    
    /**
     * Constructs a DAO instance suitable for acquiring connections based on the passed database type specific properties.
     * @param dbProperties
     */
    public ParameterDAOBasicImpl(Properties dbProperties) {
        super(dbProperties);
    }
    
    /**
     * Constructs a DAO using the passed externally managed database connection.
     * The connection used by this instance for all DB operations will be the passed connection.
     * @param Connection - A database connection that will be managed by the caller.
     */
    public ParameterDAOBasicImpl(Connection managedConnection) {
        super(managedConnection);
    }
    
    @Override
    public void insert(List<Parameter> parameters) throws FHIRPersistenceDataAccessException, FHIRPersistenceDBConnectException {
        final String METHODNAME = "insert(List<Parameter>)";
        log.entering(CLASSNAME, METHODNAME);
                
        Connection connection = null;
        PreparedStatement stmt = null;
                        
        try {
            connection = this.getConnection();
            stmt = connection.prepareStatement(SQL_INSERT);
            
            for (Parameter parameter: parameters) {
                stmt.setString(1, parameter.getName());
                stmt.setString(2, parameter.getResourceType());
                stmt.setString(3, parameter.getValueCode());
                stmt.setTimestamp(4, parameter.getValueDate());
                stmt.setTimestamp(5,  parameter.getValueDateEnd());
                stmt.setTimestamp(6, parameter.getValueDateStart());
                stmt.setObject(7, parameter.getValueLatitude(), Types.DOUBLE);
                stmt.setObject(8, parameter.getValueLongitude(), Types.DOUBLE);
                stmt.setObject(9, parameter.getValueNumber(), Types.DOUBLE);
                stmt.setObject(10, parameter.getValueNumberHigh(), Types.DOUBLE);
                stmt.setObject(11, parameter.getValueNumberLow(), Types.DOUBLE);
                stmt.setString(12, parameter.getValueString());
                stmt.setString(13, parameter.getValueSystem());
                stmt.setLong(14, parameter.getResourceId());
                stmt.addBatch();
            }
            
            stmt.executeBatch();
            
        }
        catch(FHIRPersistenceDBConnectException e) {
            throw e;
        }
        catch(Throwable e) {
            FHIRPersistenceDataAccessException fx = new FHIRPersistenceDataAccessException("Failure inserting Parameter batch.");
            log.log(Level.SEVERE, fx.getMessage(), e);
            throw fx;            
        }
        finally {
            this.cleanup(stmt, connection);
            log.exiting(CLASSNAME, METHODNAME);
        }
    }

 

    
    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.persistence.jdbc.dao.impl.ParameterDAO#deleteByResource(long)
     */
    @Override
    public void deleteByResource(long resourceId) throws FHIRPersistenceDBConnectException, FHIRPersistenceDataAccessException {
        final String METHODNAME = "delete";
        log.entering(CLASSNAME, METHODNAME);
        
        Connection connection = null;
        PreparedStatement stmt = null;
                        
        try {
            connection = this.getConnection();
            stmt = connection.prepareStatement(SQL_DELETE_BY_RESOURCE_ID);
            stmt.setLong(1, resourceId);
                    
            stmt.executeUpdate();
            if (log.isLoggable(Level.FINE)) {
                log.fine("Successfully delete Parameters for Resource id=" + resourceId);
            }
        }
        catch(FHIRPersistenceDBConnectException e) {
            throw e;
        }
        catch(Throwable e) {
            FHIRPersistenceDataAccessException fx = new FHIRPersistenceDataAccessException("Failure deleting Paramaters for Resource id=" + resourceId);
            log.log(Level.SEVERE, fx.getMessage(), e);
            throw fx;            
        }
        finally {
            this.cleanup(stmt, connection);
            log.exiting(CLASSNAME, METHODNAME);
        }
        
    }

}
