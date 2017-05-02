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
import com.ibm.watsonhealth.fhir.persistence.test.common.AbstractQueryRiskAssmtTest;


public class JDBCQueryRiskAssmtTest extends AbstractQueryRiskAssmtTest {
	
	private Properties testProps;
	
	public JDBCQueryRiskAssmtTest() throws Exception {
		this.testProps = readTestProperties();
	}

	@Override
	public void bootstrapDatabase() throws Exception {
		DerbyInitializer derbyInit;
		String dbDriverName = this.testProps.getProperty("dbDriverName");
		if (dbDriverName != null && dbDriverName.contains("derby")) {
			this.testProps.setProperty("schemaType", "basic");
			derbyInit = new DerbyInitializer(this.testProps);
			derbyInit.bootstrapDb(false);
		}
	}
	
    @Override
    public FHIRPersistence getPersistenceImpl() throws Exception {
    	return new FHIRPersistenceJDBCImpl(this.testProps);
    }
}
