/**
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.util;

import java.util.Map;
import java.util.logging.Logger;

/**
 * This cache updater writes its Parameter Names cache candidates to the ParameterNamesCache upon a transaction commit.
 * @author markd
 *
 */
public class ParameterNamesCacheUpdater extends CacheUpdater {
    private static final String CLASSNAME = ParameterNamesCacheUpdater.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

    public ParameterNamesCacheUpdater(Map<String, Integer> newCacheCandidates) {
        super(newCacheCandidates);
    }

    
    @Override
    public void commitCacheCandidates() {
        final String METHODNAME = "commitCacheCandidates";
        log.entering(CLASSNAME, METHODNAME);
        
        ParameterNamesCache.putParameterNameIds(this.getCacheCandidates());
        
        log.exiting(CLASSNAME, METHODNAME);

    }

}
