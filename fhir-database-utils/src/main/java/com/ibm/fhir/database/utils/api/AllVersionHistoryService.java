/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.api;

/**
 * Useful implementation for when you want to apply all versions (e.g. for tests)
 * @author rarnold
 *
 */
public class AllVersionHistoryService implements IVersionHistoryService {

    /* (non-Javadoc)
     * @see com.ibm.fhir.database.utils.api.IVersionHistoryService#addVersion(java.lang.String, java.lang.String, int)
     */
    @Override
    public void addVersion(String objectSchema, String objectType, String objectName, int version) {
        // NOP...this implementation doesn't do any version tracking
    }

    /* (non-Javadoc)
     * @see com.ibm.fhir.database.utils.api.IVersionHistoryService#applies(java.lang.String, java.lang.String, int)
     */
    @Override
    public boolean applies(String objectSchema, String objectType, String objectName, int version) {
        // we always say yes
        return true;
    }

}
