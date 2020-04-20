/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.postgresql;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.dao.api.CodeSystemDAO;
import com.ibm.fhir.persistence.jdbc.dao.api.ICodeSystemCache;

/**
 * caches the code system artifacts
 */
public class CodeSystemCacheAdapter implements ICodeSystemCache {

    private final CodeSystemDAO delegate;

    public CodeSystemCacheAdapter(CodeSystemDAO delegate) {
        this.delegate = delegate;
    }

    @Override
    public int readOrAddCodeSystem(String codeSystem) throws FHIRPersistenceException {
        return delegate.readOrAddCodeSystem(codeSystem);
    }

}
