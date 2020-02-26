/*
 * (C) Copyright IBM Corp. 2017, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test;

import static org.testng.AssertJUnit.assertNotNull;

import java.sql.Connection;
import java.util.Properties;

import org.testng.annotations.Test;

import com.ibm.fhir.persistence.jdbc.dao.api.FHIRDbDAO;
import com.ibm.fhir.persistence.jdbc.dao.impl.FHIRDbDAOImpl;

/**
 * This class tests the functions of the FHIR DB Data Access Object class.
 */
public class FHIRDbDAOTest {
    /**
     * Tests acquiring a connection to a Derby FHIR database using the embedded Derby driver. 
     * If the database does not exist prior to the test, it should be created.
     * @throws Exception
     */
    @Test(groups = {"jdbc"})
    public void testGetDerbyConnection() throws Exception {
        
        Properties props = new Properties();
        props.setProperty(FHIRDbDAO.PROPERTY_DB_DRIVER, "org.apache.derby.jdbc.EmbeddedDriver");
        props.setProperty(FHIRDbDAO.PROPERTY_DB_URL, "jdbc:derby:target/derby/fhirDB;create=true");
        // Set the schemaName to the derby default so that it won't try using a non-existent FHIRDATA schema
        props.setProperty("schemaName", "APP");
        FHIRDbDAO dao = new FHIRDbDAOImpl(props);
        Connection connection = dao.getConnection();
        assertNotNull(connection);
    }
    
    /**
     * Tests acquiring a connection to an existing DB2 FHIR database. 
     * NOTE: This test will remain commented out. Since it has a specific dependency on an existing DB2 database, it will fail when run as part of the
     * maven build for the project. It can uncommented and run as a standalone testNg test, provided a DB2 FHIR database is pre-defined.
     * @throws Exception
     */
    //@Test(groups = {"jdbc"})
    public void testGetDB2Connection() throws Exception {
        
        Properties props = new Properties();
        props.setProperty(FHIRDbDAO.PROPERTY_DB_DRIVER, "com.ibm.db2.jcc.DB2Driver");
        props.setProperty(FHIRDbDAO.PROPERTY_DB_URL, "jdbc:db2://localhost:50000/fhirdb");
        props.setProperty(FHIRDbDAO.PROPERTY_DB2_USER, "user");
        props.setProperty(FHIRDbDAO.PROPERTY_DB2_PSWD, "password");
        
        FHIRDbDAO dao = new FHIRDbDAOImpl(props);
        Connection connection = dao.getConnection();
        assertNotNull(connection);
    }
}
