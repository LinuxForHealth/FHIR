/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.schema.control;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ibm.watsonhealth.database.utils.api.IVersionHistoryService;

/**
 * Stub so that we can test schema creation
 * @author rarnold
 *
 */
public class TestVersionHistoryService implements IVersionHistoryService {

    // history information set up by the test case
    private final Map<String,Integer> versionHistoryMap = new HashMap<>();

    // history information collected during schema application
    private final ConcurrentHashMap<String, Integer> newHistory = new ConcurrentHashMap<>();

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.api.IVersionHistoryService#addVersion(java.lang.String, java.lang.String, int)
     */
    @Override
    public void addVersion(String objectType, String objectName, int version) {
        final String key = objectType + ":" + objectName;
        newHistory.put(key, version);
    }

    /* (non-Javadoc)
     * @see com.ibm.watsonhealth.database.utils.api.IVersionHistoryService#applies(java.lang.String, java.lang.String, int)
     */
    @Override
    public boolean applies(String objectType, String objectName, int changeVersion) {
        final String key = makeKey(objectType, objectName);
        Integer currentVersion = this.versionHistoryMap.get(key);
        return currentVersion == null || currentVersion < changeVersion;

    }

    /**
     * For test support, add a currentHistory record so that we can test the applies()
     * feature
     * @param objectType
     * @param objectName
     * @param version
     */
    public void addTestHistory(String objectType, String objectName, int version) {
        final String key = makeKey(objectType, objectName);
        versionHistoryMap.put(key, version);        
    }

    /**
     * Key generator function
     * @param objectType
     * @param objectName
     * @return a concatenated string containing objectType and objectName
     */
    public static String makeKey(String objectType, String objectName) {
        return objectType + ":" + objectName;
    }
}
