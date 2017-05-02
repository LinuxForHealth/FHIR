/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.test;

import java.util.Properties;

import com.ibm.watsonhealth.fhir.persistence.FHIRPersistence;
import com.ibm.watsonhealth.fhir.persistence.jdbc.impl.FHIRPersistenceJDBCImpl;
import com.ibm.watsonhealth.fhir.persistence.jdbc.impl.FHIRPersistenceJDBCNormalizedImpl;
import com.ibm.watsonhealth.fhir.persistence.jdbc.test.util.DerbyInitializer;
import com.ibm.watsonhealth.fhir.persistence.test.common.AbstractQueryPatientTest;


public class JDBCNormQueryPatientTest extends AbstractQueryPatientTest {
	
	private Properties testProps;
	
	public JDBCNormQueryPatientTest() throws Exception {
		this.testProps = readTestProperties();
	}

	@Override
	public void bootstrapDatabase() throws Exception {
		DerbyInitializer derbyInit;
		String dbDriverName = this.testProps.getProperty("dbDriverName");
		if (dbDriverName != null && dbDriverName.contains("derby")) {
			this.testProps.setProperty("schemaType", "normalized");
			derbyInit = new DerbyInitializer(this.testProps);
			derbyInit.bootstrapDb(false);
		}
	}
	
    @Override
    public FHIRPersistence getPersistenceImpl() throws Exception {
    	return new FHIRPersistenceJDBCNormalizedImpl(this.testProps);
    }
}
