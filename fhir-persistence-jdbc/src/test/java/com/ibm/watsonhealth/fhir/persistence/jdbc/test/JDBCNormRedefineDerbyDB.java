/**
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.test;

import java.util.Properties;
import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.persistence.jdbc.test.util.DerbyInitializer;
import com.ibm.watsonhealth.fhir.persistence.test.common.FHIRModelTestBase;

/**
 * This sole purpose of this class is to delete and redefine the Derby database used by the JDBCNormXXX testng tests. This test class should run first in
 * the suite of tests that gets run when the fhir-persistence-jdbc project is built.
 *
 */
public class JDBCNormRedefineDerbyDB extends FHIRModelTestBase {
    
    private Properties testProps;
    
    public JDBCNormRedefineDerbyDB() throws Exception {
        this.testProps = readTestProperties("test.normalized.properties");
    }

    @Test(groups = { "jdbc-normalized" })
    public void bootstrapDatabase() throws Exception {
        System.out.println("bootstrapping database for jdbc-normalized test group");
        
        String dbDriverName = this.testProps.getProperty("dbDriverName");
        if (dbDriverName != null && dbDriverName.contains("derby")) {
            DerbyInitializer derbyInit = new DerbyInitializer(this.testProps);
            
            // start clean at the beginning of the test run
            derbyInit.bootstrapDb(true);
        }
    }
    
}
