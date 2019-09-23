/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.test.spec;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.spec.test.IExampleProcessor;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.util.ResourceFingerprintVisitor;

/**
 * Reads R4 example resources and performs a sequence of persistance 
 * operations against each
 * @author rarnold
 *
 */
public class R4JDBCExamplesProcessor implements IExampleProcessor {
	
	// the list of operations we apply to reach resource
	private final List<ITestResourceOperation> operations = new ArrayList<>();
	
	// The persistence API
	private final FHIRPersistence persistence;
	
	// supplier of FHIRPersistenceContext for normal create/update/delete ops
	private final Supplier<FHIRPersistenceContext> persistenceContextSupplier;

	// supplier of FHIRPersistenceContext for history operations
	private final Supplier<FHIRPersistenceContext> historyContextSupplier;
	
	
	/**
	 * Public constructor. Initializes the list of operations
	 * @param persistence
	 */
	public R4JDBCExamplesProcessor(FHIRPersistence persistence, 
	    Supplier<FHIRPersistenceContext> persistenceContextSupplier,
	    Supplier<FHIRPersistenceContext> historyContextSupplier) {
		this.persistence = persistence;
		this.persistenceContextSupplier = persistenceContextSupplier;
		this.historyContextSupplier = historyContextSupplier;
		
		// The sequence of operations we want to apply to each resource
		operations.add(new CreateOperation());
		operations.add(new ReadOperation());
		operations.add(new UpdateOperation());
        operations.add(new UpdateOperation());
		operations.add(new ReadOperation());
		operations.add(new VReadOperation());
		operations.add(new HistoryOperation(3)); // create+update+update = 3 versions
		operations.add(new DeleteOperation());
        operations.add(new DeleteOperation());
        operations.add(new HistoryOperation(4)); // create+update+update+delete = 4 versions
	}

	/* (non-Javadoc)
	 * @see com.ibm.fhir.persistence.test.spec.IExampleProcessor#process(java.lang.String, com.ibm.fhir.model.resource.Resource)
	 */
	@Override
    public void process(String jsonFile, Resource resource) throws Exception {

    	// Initialize the test context. As we run through the sequence of operations, each 
	    // one will update the context which will then be used by the next operation
    	TestContext context = new TestContext(this.persistence, this.persistenceContextSupplier, this.historyContextSupplier);
    	
    	// Clear the id so that we can set it ourselves. The ids from the examples are reused
    	// even though the resources are supposed to be different
        resource = resource.toBuilder().id(null).build();
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
