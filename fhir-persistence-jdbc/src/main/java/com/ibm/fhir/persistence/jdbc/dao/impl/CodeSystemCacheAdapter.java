/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.impl;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.dao.api.ICodeSystemCache;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterDAO;

/**
 * Adapter to provide access to cached code system values managed by
 * the {@link ParameterDAO} implementation.
 * @author rarnold
 *
 */
public class CodeSystemCacheAdapter implements ICodeSystemCache {
    
    private final ParameterDAO delegate;
    
    public CodeSystemCacheAdapter(ParameterDAO delegate) {
        this.delegate = delegate;
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.persistence.jdbc.dao.api.ICodeSystemCache#readOrAddCodeSystem(java.lang.String)
     */
    @Override
    public int readOrAddCodeSystem(String codeSystem) throws FHIRPersistenceException {
        return delegate.acquireCodeSystemId(codeSystem);
    }
}
