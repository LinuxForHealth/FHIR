/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watson.health.fhir.persistence.test;

import com.ibm.watson.health.fhir.persistence.FHIRPersistence;
import com.ibm.watson.health.fhir.persistence.FHIRPersistenceFactory;
import com.ibm.watson.health.fhir.persistence.exception.FHIRPersistenceException;


/**
 * Mock persistence factory for use during testing.
 */
public class MockExceptionPersistenceFactory implements FHIRPersistenceFactory {
    
    public MockExceptionPersistenceFactory() {
        String a = null;
        a.toString();
    }

    /* (non-Javadoc)
     * @see com.ibm.watson.health.fhir.persistence.FHIRPersistenceFactory#getInstance()
     */
    @Override
    public FHIRPersistence getInstance() throws FHIRPersistenceException {
        return null;
    }
}
