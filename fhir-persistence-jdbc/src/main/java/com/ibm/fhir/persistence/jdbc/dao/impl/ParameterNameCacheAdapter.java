/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.impl;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.dao.api.IParameterNameCache;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterDAO;

/**
 * Adapter to provide access to the cached parameter name ids managed
 * by the {@link ParameterDAO} implementation.
 */
public class ParameterNameCacheAdapter implements IParameterNameCache {

    private final ParameterDAO delegate;

    public ParameterNameCacheAdapter(ParameterDAO delegate) {
        this.delegate = delegate;
    }

    @Override
    public int readOrAddParameterNameId(String parameterName) throws FHIRPersistenceException {
        return delegate.acquireParameterNameId(parameterName);
    }

}
