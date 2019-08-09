/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.derby;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.FhirSequenceDAO;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.impl.ParameterNameDAOImpl;
import com.ibm.watsonhealth.fhir.persistence.jdbc.exception.FHIRPersistenceDataAccessException;

/**
 * For R4 we have replaced the old Derby (Java) stored procedure with
 * plain old JDBC statements - much simpler and easier to debug.
 * @author rarnold
 */
public class DerbyParameterNamesDAO extends ParameterNameDAOImpl {
    private final FhirSequenceDAO fhirSequenceDAO;
    
    public DerbyParameterNamesDAO(Connection c, FhirSequenceDAO fsd) {
        super(c);
        this.fhirSequenceDAO = fsd;
    }
    
    @Override
    public Integer readOrAddParameterNameId(String parameterName) throws FHIRPersistenceDataAccessException  {
        // As the system is concurrent, we have to handle cases where another thread
        // might create the entry after we selected and found nothing
        Integer result = getParameterId(parameterName);
         
        // Create the resource if we don't have it already (set by the continue handler)
        if (result == null) {
            try {
                result = (int)fhirSequenceDAO.nextValueFromFhirSequence();
             
                String INS = "INSERT INTO parameter_names (parameter_name_id, parameter_name) VALUES (?, ?)";
                try (PreparedStatement stmt = getConnection().prepareStatement(INS)) {
                    // bind parameters
                    stmt.setInt(1, result);
                    stmt.setString(2, parameterName);
                    stmt.executeUpdate();
                }
            } catch (SQLException e) {
                if ("23505".equals(e.getSQLState())) {
                    // another thread snuck in and created the record, so we need to fetch the correct id
                    result = getParameterId(parameterName);
                }
                else {
                    throw new FHIRPersistenceDataAccessException("parameterName=" + parameterName, e);
                }
            }

        }
        
        return result;
    }

    /**
     * Read the id for the named type
     * @param resourceTypeName
     * @return the database id, or null if the named record is not found
     * @throws SQLException
     */
    protected Integer getParameterId(String parameterName) throws FHIRPersistenceDataAccessException {
        Integer result;
        
        String sql1 = "SELECT parameter_name_id FROM parameter_names WHERE parameter_name = ?";
        
        try (PreparedStatement stmt = getConnection().prepareStatement(sql1)) {
            stmt.setString(1, parameterName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            } 
            else {
                result = null;
            }
        }
        catch (SQLException x) {
            throw new FHIRPersistenceDataAccessException("parameterName=" + parameterName, x);
        }
        
        return result;
    }

}
