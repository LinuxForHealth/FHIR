/**
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.test;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.model.test.FHIRModelTestBase;
import com.ibm.watsonhealth.fhir.persistence.jdbc.test.util.DerbyInitializer;

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
    
    /**
     * Deletes the the passed file. If is is a directory, deletes the directory and all of its contents.
     * 
     * @throws IOException
     */
    private static void delete(File file) throws IOException {
        if(file.isDirectory()) {

            //directory is empty, then delete it
            if(file.list().length==0) {
                file.delete();
            } else {
                // list all the directory contents
                String files[] = file.list();

                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File(file, temp);

                    //recursive delete
                    delete(fileDelete);
                }

                //check the directory again, if empty then delete it
                if(file.list().length==0) {
                    file.delete();
                }
            }
        } 
        else {
            // if file exists, then delete it
            file.delete();
        }
    }

    @Test(groups = { "jdbc-normalized" })
    public void bootstrapDatabase() throws Exception {
        DerbyInitializer derbyInit;
        String dbDriverName = this.testProps.getProperty("dbDriverName");
        if (dbDriverName != null && dbDriverName.contains("derby")) {
            derbyInit = new DerbyInitializer(this.testProps);
            try {
                delete(new File("derby"));    //start clean for each test run
            } 
            catch(IOException e) {
            }
            derbyInit.bootstrapDb(true);
        }
    }
    
}
