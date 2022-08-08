/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.schema.control;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.linuxforhealth.fhir.database.utils.api.IVersionHistoryService;

/**
 * Stub so that we can test schema creation
 */
public class VersionHistoryServiceTest implements IVersionHistoryService {

    // history information set up by the test case
    private final Map<String,Integer> versionHistoryMap = new HashMap<>();

    // history information collected during schema application
    private final ConcurrentHashMap<String, Integer> newHistory = new ConcurrentHashMap<>();

    @Override
    public void addVersion(String objectSchema, String objectType, String objectName, int version) {
        final String key = makeKey(objectSchema, objectType, objectName);
        newHistory.put(key, version);
    }

    @Override
    public boolean applies(String objectSchema, String objectType, String objectName, int changeVersion) {
        final String key = makeKey(objectSchema, objectType, objectName);
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
    public void addTestHistory(String objectSchema, String objectType, String objectName, int version) {
        final String key = makeKey(objectSchema, objectType, objectName);
        versionHistoryMap.put(key, version);
    }

    /**
     * Key generator function
     * @param objectType
     * @param objectName
     * @return a concatenated string containing objectType and objectName
     */
    public static String makeKey(String objectSchema, String objectType, String objectName) {
        return objectSchema + ":" + objectType + ":" + objectName;
    }

    /**
     * Always returns null
     */
    @Override
    public Integer getVersion(String objectSchema, String objectType, String objectName) {
        return null;
    }
}
