/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.ibm.watsonhealth.fhir.persistence.FHIRPersistence;
import com.ibm.watsonhealth.fhir.persistence.impl.FHIRPersistenceImpl;
import com.ibm.watsonhealth.fhir.persistence.jpa.impl.FHIRPersistenceJPAImpl;
import com.ibm.watsonhealth.fhir.provider.FHIRProvider;
import com.ibm.watsonhealth.fhir.server.resources.FHIRResource;
import com.ibm.watsonhealth.fhir.validation.Validator;

public class FHIRApplication extends Application {
	private Set<Class<?>> classes = null;
	private Set<Object> singletons = null;
	
	public FHIRApplication() {
		classes = new HashSet<Class<?>>();
		
		Validator validator = new Validator();
//		FHIRPersistence persistence = new FHIRPersistenceImpl();	// basic in-memory implementation
		FHIRPersistence persistence = new FHIRPersistenceJPAImpl();
		
		singletons = new HashSet<Object>();
		singletons.add(new FHIRProvider());
		singletons.add(new FHIRResource(validator, persistence));
	}
	
	@Override
	public Set<Class<?>> getClasses() {
		return classes;
	}
	
	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
