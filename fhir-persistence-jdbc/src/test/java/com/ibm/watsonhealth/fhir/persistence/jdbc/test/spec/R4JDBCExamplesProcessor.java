/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.test.spec;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.ibm.watsonhealth.fhir.model.resource.Resource;
import com.ibm.watsonhealth.fhir.model.spec.test.IExampleProcessor;
import com.ibm.watsonhealth.fhir.persistence.FHIRPersistence;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.watsonhealth.fhir.persistence.util.ResourceFingerprintVisitor;

/**
 * Reads R4 example resources and performs a sequence of persistance 
 * operations against each
 * @author rarnold
 *
 */
public class R4JDBCExamplesProcessor implements IExampleProcessor {
	
	// the list of operations we apply to reach resource
	final List<ITestResourceOperation> operations = new ArrayList<>();
	
	// The persistence API
	final FHIRPersistence persistence;
	
	final Supplier<FHIRPersistenceContext> persistenceContextSupplier;
	
	
	/**
	 * Public constructor. Initializes the list of operations
	 * @param persistence
	 */
	public R4JDBCExamplesProcessor(FHIRPersistence persistence, Supplier<FHIRPersistenceContext> persistenceContextSupplier) {
		this.persistence = persistence;
		this.persistenceContextSupplier = persistenceContextSupplier;
		
		// The sequence of operations we want to apply to each resource
		operations.add(new CreateOperation());
		operations.add(new ReadOperation());
		operations.add(new UpdateOperation());
		operations.add(new ReadOperation());
		operations.add(new VReadOperation());
		operations.add(new HistoryOperation());
		operations.add(new DeleteOperation());
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.watsonhealth.fhir.persistence.test.spec.IExampleProcessor#process(java.lang.String, com.ibm.watsonhealth.fhir.model.resource.Resource)
	 */
	@Override
    public void process(String jsonFile, Resource resource) throws Exception {

    	// Initialize the test context. As we run through the sequence of operations, each 
	    // one will update the context which will then be used by the next operation
    	TestContext context = new TestContext(this.persistence, this.persistenceContextSupplier);
    	context.setResource(resource);
    	
    	// Compute a reference fingerprint of the resource before we perform
    	// any operations. We can use this fingerprint to check that operations
    	// don't distort the resource in any way
    	ResourceFingerprintVisitor v = new ResourceFingerprintVisitor();
    	resource.accept(resource.getClass().getSimpleName(), v);
    	context.setOriginalFingerprint(v.getSaltAndHash());

    	// ITestResourceOperation#process throws Exception, which precludes the
    	// use of forEach here...so going old-school keeps it simpler
    	for (ITestResourceOperation op: operations) {
    		op.process(context);
    	}
    }
}
