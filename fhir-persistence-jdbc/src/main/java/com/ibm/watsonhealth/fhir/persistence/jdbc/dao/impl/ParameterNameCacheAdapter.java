/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.dao.impl;

import com.ibm.watsonhealth.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.IParameterNameCache;
import com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.ParameterNormalizedDAO;
/**
 * Adapter to provide access to the cached parameter name ids managed
 * by the {@link ParameterNormalizedDAO} implementation.
 * @author rarnold
 */
public class ParameterNameCacheAdapter implements IParameterNameCache {
    
    private final ParameterNormalizedDAO delegate;
    
    public ParameterNameCacheAdapter(ParameterNormalizedDAO delegate) {
        this.delegate = delegate;
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.fhir.persistence.jdbc.dao.api.IParameterNameCache#readOrAddParameterNameId(java.lang.String)
     */
    @Override
    public int readOrAddParameterNameId(String parameterName) throws FHIRPersistenceException {
        return delegate.acquireParameterNameId(parameterName);
    }

}
