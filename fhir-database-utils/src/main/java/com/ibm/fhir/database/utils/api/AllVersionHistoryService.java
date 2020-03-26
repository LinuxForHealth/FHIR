/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.api;

/**
 * Useful implementation for when you want to apply all versions (e.g. for tests)
 */
public class AllVersionHistoryService implements IVersionHistoryService {

    @Override
    public void addVersion(String objectSchema, String objectType, String objectName, int version) {
        // NOP...this implementation doesn't do any version tracking
    }

    @Override
    public boolean applies(String objectSchema, String objectType, String objectName, int version) {
        // we always say yes
        return true;
    }

    @Override
    public Integer getVersion(String objectSchema, String objectType, String objectName) {
        // always return null
        return null;
    }

}
