/*
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test;

import java.util.Properties;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

import com.ibm.fhir.database.utils.derby.DerbyMaster;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.persistence.jdbc.test.util.DerbyInitializer;

/**
 * This sole purpose of this class is to delete and redefine the Derby database used by the JDBCNormXXX testng tests. 
 * This test class should run first in the suite of tests that gets run when the fhir-persistence-jdbc project is built.
 *
 */
public class RedefineDerbyDB {
    
    private Properties testProps;
    
    public RedefineDerbyDB() throws Exception {
        this.testProps = TestUtil.readTestProperties("test.jdbc.properties");
    }

//    @Test
    public void bootstrapDatabase() throws Exception {
        System.out.println("bootstrapping database for the fhir-persistence-jdbc unit tests");
        
        String dbDriverName = this.testProps.getProperty("dbDriverName");
        if (dbDriverName != null && dbDriverName.contains("derby")) {
            DerbyInitializer derbyInit = new DerbyInitializer(this.testProps);
            
            // start clean at the beginning of the test run
            derbyInit.bootstrapDb(true);
        }
    }
        
    @AfterSuite
    public void shutdownDerbyFhir() throws Exception {
        DerbyMaster.shutdown(DerbyInitializer.DB_NAME);
    }
    
}
