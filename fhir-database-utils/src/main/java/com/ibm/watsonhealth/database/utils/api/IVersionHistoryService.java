/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.database.utils.api;

/**
 * Service interface used to update the version history table 
 * (just before a transaction ends)
 * @author rarnold
 *
 */
public interface IVersionHistoryService {

    /**
     * Add this version (idempotent)
     * @param objectType
     * @param objectName
     * @param version
     */
    public void addVersion(String objectType, String objectName, int version);
    
    /**
     * Check to see if we described object is newer than we have currently in the
     * database
     * @param objectType
     * @param objectName
     * @param version
     * @return
     */
    public boolean applies(String objectType, String objectName, int version);
}
