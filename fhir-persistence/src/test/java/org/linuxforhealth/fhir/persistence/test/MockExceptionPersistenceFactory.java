/*
 * (C) Copyright IBM Corp. 2017, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.test;

import org.linuxforhealth.fhir.persistence.FHIRPersistence;
import org.linuxforhealth.fhir.persistence.FHIRPersistenceFactory;
import org.linuxforhealth.fhir.persistence.exception.FHIRPersistenceException;
import org.linuxforhealth.fhir.search.util.SearchHelper;


/**
 * Mock persistence factory for use during testing.
 */
public class MockExceptionPersistenceFactory implements FHIRPersistenceFactory {

    /**
     * Always throws a NullPointerException
     */
    @SuppressWarnings("null")
    public MockExceptionPersistenceFactory() {
        String a = null;
        a.toString();
    }

    @Override
    public FHIRPersistence getInstance(SearchHelper searchHelper) throws FHIRPersistenceException {
        return null;
    }
}
