/**
 * (C) Copyright IBM Corp. 2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.jdbc.util;

import java.util.Map;
import java.util.logging.Logger;

/**
 * This cache updater writes its Resource Types cache candidates to the ResourceTypesCache upon a transaction commit.
 * @author markd
 *
 */
public class ResourceTypesCacheUpdater extends CacheUpdater {
    private static final String CLASSNAME = ResourceTypesCacheUpdater.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

   public ResourceTypesCacheUpdater(Map<String, Integer> newCacheCandidates) {
        super(newCacheCandidates);
    }

    @Override
    public void commitCacheCandidates() {
        final String METHODNAME = "commitCacheCandidates";
        log.entering(CLASSNAME, METHODNAME);
        
        ResourceTypesCache.putResourceTypeIds(this.getCacheCandidates());
        
        log.exiting(CLASSNAME, METHODNAME);

    }
}

 
