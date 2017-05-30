/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.test;

import java.util.List;

import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.persistence.FHIRPersistence;
import com.ibm.watsonhealth.fhir.persistence.FHIRPersistenceTransaction;
import com.ibm.watsonhealth.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceResourceDeletedException;

/**
 * Mock implementation of FHIRPersistence for use during testing.
 *
 */
public class MockPersistenceImpl implements FHIRPersistence {

    @Override
    public void create(FHIRPersistenceContext context, Resource resource) throws FHIRPersistenceException {
    }

    @Override
    public Resource read(FHIRPersistenceContext context, Class<? extends Resource> resourceType, String logicalId)
        throws FHIRPersistenceException, FHIRPersistenceResourceDeletedException {
        return null;
    }

    @Override
    public Resource vread(FHIRPersistenceContext context, Class<? extends Resource> resourceType, String logicalId, String versionId)
        throws FHIRPersistenceException, FHIRPersistenceResourceDeletedException {
        return null;
    }

    @Override
    public void update(FHIRPersistenceContext context, String logicalId, Resource resource) throws FHIRPersistenceException {
    }

    @Override
    public List<Resource> history(FHIRPersistenceContext context, Class<? extends Resource> resourceType, String logicalId) throws FHIRPersistenceException {
        return null;
    }

    @Override
    public List<Resource> search(FHIRPersistenceContext context, Class<? extends Resource> resourceType) throws FHIRPersistenceException {
        return null;
    }

    @Override
    public boolean isTransactional() {
        return false;
    }

    @Override
    public FHIRPersistenceTransaction getTransaction() {
        return null;
    }
}
