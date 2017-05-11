/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.test;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import com.ibm.watsonhealth.fhir.persistence.FHIRPersistence;
import com.ibm.watsonhealth.fhir.persistence.jdbc.impl.FHIRPersistenceJDBCNormalizedImpl;
import com.ibm.watsonhealth.fhir.persistence.jdbc.test.util.DerbyInitializer;
import com.ibm.watsonhealth.fhir.persistence.test.common.AbstractQueryAuditEventTest;


public class JDBCNormQueryAuditEventTest extends AbstractQueryAuditEventTest {
	
	private Properties testProps;
	
	public JDBCNormQueryAuditEventTest() throws Exception {
		this.testProps = readTestProperties("test.normalized.properties");
	}
	
	private static void delete(File file) throws IOException {
		if(file.isDirectory()){

			//directory is empty, then delete it
			if(file.list().length==0) {
				file.delete();
	    	} else {
	    		//list all the directory contents
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

	    }else{
	    	//if file, then delete it
	    	file.delete();
	    }
	}

	@Override
	public void bootstrapDatabase() throws Exception {
		DerbyInitializer derbyInit;
		String dbDriverName = this.testProps.getProperty("dbDriverName");
		if (dbDriverName != null && dbDriverName.contains("derby")) {
			derbyInit = new DerbyInitializer(this.testProps);
			try{
				delete(new File("derby"));	//start clean for each test run
	        } catch(IOException e){
	        }
			derbyInit.bootstrapDb(true);
		}
	}
	
    @Override
    public FHIRPersistence getPersistenceImpl() throws Exception {
    	return new FHIRPersistenceJDBCNormalizedImpl(this.testProps);
    }
}
