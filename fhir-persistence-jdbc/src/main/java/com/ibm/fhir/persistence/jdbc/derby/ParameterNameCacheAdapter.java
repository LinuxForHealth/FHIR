/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.derby;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.dao.api.IParameterNameCache;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterNameDAO;

/**
 * Adapater to manage the cache. 
 */
public class ParameterNameCacheAdapter implements IParameterNameCache {
    private final ParameterNameDAO delegate;

    public ParameterNameCacheAdapter(ParameterNameDAO delegate) {
        this.delegate = delegate;
    }

    @Override
    public int readOrAddParameterNameId(String parameterName) throws FHIRPersistenceException {
        return delegate.readOrAddParameterNameId(parameterName);
    }

}
