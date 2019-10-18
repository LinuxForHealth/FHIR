/*
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc;

import static com.ibm.fhir.config.FHIRConfiguration.PROPERTY_JDBC_SCHEMA_TYPE;

import java.util.logging.Logger;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.FHIRPersistenceFactory;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.exception.FHIRPersistenceNotSupportedException;
import com.ibm.fhir.persistence.jdbc.impl.FHIRPersistenceJDBCImpl;
import com.ibm.fhir.persistence.jdbc.impl.FHIRPersistenceJDBCNormalizedImpl;

/**
 * Factory which serves up instances of the JDBC persistence implementation.
 */
public class FHIRPersistenceJDBCFactory implements FHIRPersistenceFactory {
    private static final Logger log = Logger.getLogger(FHIRPersistenceJDBCFactory.class.getName());

    /*
     * (non-Javadoc)
     * @see com.ibm.fhir.persistence.FHIRPersistenceFactory#getInstance()
     */
    @Override
    public FHIRPersistence getInstance() throws FHIRPersistenceException {
        
        FHIRPersistence persistenceImpl = null;
        
        try {
            SchemaType schemaType = SchemaType.fromValue(FHIRConfiguration.getInstance().loadConfiguration().getStringProperty(PROPERTY_JDBC_SCHEMA_TYPE, SchemaType.BASIC.value()));
            switch (schemaType) {
                case BASIC:         persistenceImpl = new FHIRPersistenceJDBCImpl();
                                    break;
                        
                case NORMALIZED:     persistenceImpl = new FHIRPersistenceJDBCNormalizedImpl();
                                    break;

                                    // TODO. Use updated persistence impl
                case MULTITENANT:     persistenceImpl = new FHIRPersistenceJDBCNormalizedImpl();
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
