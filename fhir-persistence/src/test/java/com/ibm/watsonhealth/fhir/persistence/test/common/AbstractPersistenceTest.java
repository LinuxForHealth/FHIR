/**
 * (C) Copyright IBM Corp. 2016,2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.test.common;

import org.testng.annotations.BeforeClass;

import com.ibm.watsonhealth.fhir.model.test.FHIRModelTestBase;
import com.ibm.watsonhealth.fhir.persistence.FHIRPersistence;

/**
 * This is a common abstract base class for all persistence-related tests.
 */
public abstract class AbstractPersistenceTest extends FHIRModelTestBase {
    
    // The persistence layer instance to be used by the tests. 
    protected static FHIRPersistence persistence = null;
    
    // Each concrete subclass needs to implement this to obtain the appropriate persistence layer instance.
    public abstract FHIRPersistence getPersistenceImpl() throws Exception;
    
    @BeforeClass
    public void setUp() throws Exception {
        persistence = getPersistenceImpl();
    }
}
