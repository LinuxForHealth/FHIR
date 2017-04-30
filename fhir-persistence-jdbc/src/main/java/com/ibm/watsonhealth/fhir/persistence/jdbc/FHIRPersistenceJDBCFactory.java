/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc;

import static com.ibm.watsonhealth.fhir.config.FHIRConfiguration.PROPERTY_JDBC_SCHEMA_TYPE;

import java.util.logging.Logger;

import com.ibm.watsonhealth.fhir.config.FHIRConfigHelper;
import com.ibm.watsonhealth.fhir.persistence.FHIRPersistence;
import com.ibm.watsonhealth.fhir.persistence.FHIRPersistenceFactory;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceNotSupportedException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.impl.FHIRPersistenceJDBCImpl;
import com.ibm.watsonhealth.fhir.persistence.jdbc.impl.FHIRPersistenceJDBCNormalizedImpl;

/**
 * Factory which serves up instances of the JDBC persistence implementation.
 */
public class FHIRPersistenceJDBCFactory implements FHIRPersistenceFactory {
    private static final Logger log = Logger.getLogger(FHIRPersistenceJDBCFactory.class.getName());

    /*
     * (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.persistence.FHIRPersistenceFactory#getInstance()
     */
    @Override
    public FHIRPersistence getInstance() throws FHIRPersistenceException {
    	
    	FHIRPersistence persistenceImpl = null;
    	
        try {
    		SchemaType schemaType = SchemaType.fromValue(FHIRConfigHelper.getStringProperty(PROPERTY_JDBC_SCHEMA_TYPE, SchemaType.BASIC.value()));
    		switch (schemaType) {
    			case BASIC: 		persistenceImpl = new FHIRPersistenceJDBCImpl();
    								break;
    					
    			case NORMALIZED: 	persistenceImpl = new FHIRPersistenceJDBCNormalizedImpl();
    								break;
    			
    			default: throw new FHIRPersistenceNotSupportedException("Unsupported schema type: " + schemaType.value());
    		}
        	
        	return persistenceImpl;
        } 
        catch (FHIRPersistenceNotSupportedException e) {
        	throw e;
        }
        catch (Throwable t) {
            String msg = "Unexpected exception while creating JDBC persistence layer: ";
            log.severe(msg + t);
            throw new FHIRPersistenceException(msg, t);
        }
    }
}
