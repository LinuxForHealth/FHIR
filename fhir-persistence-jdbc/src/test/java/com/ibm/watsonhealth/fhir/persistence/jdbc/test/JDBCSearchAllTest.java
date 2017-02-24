/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.test;

import java.util.Properties;

import com.ibm.watsonhealth.fhir.persistence.FHIRPersistence;
import com.ibm.watsonhealth.fhir.persistence.jdbc.impl.FHIRPersistenceJDBCImpl;
import com.ibm.watsonhealth.fhir.persistence.jdbc.test.util.DerbyInitializer;
import com.ibm.watsonhealth.fhir.persistence.test.common.AbstractSearchAllTest;


public class JDBCSearchAllTest extends AbstractSearchAllTest {
	
	private Properties testProps;
	
	public JDBCSearchAllTest() throws Exception {
		this.testProps = readTestProperties();
	}

	@Override
	public void bootstrapDatabase() throws Exception {
		DerbyInitializer derbyInit;
		String dbDriverName = this.testProps.getProperty("dbDriverName");
		if (dbDriverName != null && dbDriverName.contains("derby")) {
			derbyInit = new DerbyInitializer(this.testProps);
			derbyInit.bootstrapDb();
		}
	}
	
    @Override
    public FHIRPersistence getPersistenceImpl() throws Exception {
    	return new FHIRPersistenceJDBCImpl(this.testProps);
    }
}
