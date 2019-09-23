/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.dao.impl;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.dao.api.ICodeSystemCache;
import com.ibm.fhir.persistence.jdbc.dao.api.ParameterNormalizedDAO;

/**
 * Adapter to provide access to cached code system values managed by
 * the {@link ParameterNormalizedDAO} implementation.
 * @author rarnold
 *
 */
public class CodeSystemCacheAdapter implements ICodeSystemCache {
    
    private final ParameterNormalizedDAO delegate;
    
    public CodeSystemCacheAdapter(ParameterNormalizedDAO delegate) {
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
