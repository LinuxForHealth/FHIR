/*
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util;

import java.util.Map;
import java.util.logging.Logger;

/**
 * This cache update writes its Code Systems cache candidates to the CodeSystemsCache upon a transaction commit.
 */
@Deprecated
public class CodeSystemsCacheUpdater extends CacheUpdater {
    private static final String CLASSNAME = CodeSystemsCacheUpdater.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

    public CodeSystemsCacheUpdater(String tenantDatastoreCacheName, Map<String, Integer> newCacheCandidates) {
        super(tenantDatastoreCacheName, newCacheCandidates);
    }


    @Override
    public void commitCacheCandidates() {
        final String METHODNAME = "commitCacheCandidates";
        log.entering(CLASSNAME, METHODNAME);

        CodeSystemsCache.putCodeSystemIds(this.getTenantDatastoreCacheName(), this.getCacheCandidates());

        log.exiting(CLASSNAME, METHODNAME);
    }

}
