/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.api;

/**
 * Service interface used to update the version history table
 * (just before a transaction ends)
 */
public interface IVersionHistoryService {

    /**
     * Add this version (idempotent)
     * @param objectSchema
     * @param objectType
     * @param objectName
     * @param version
     */
    public void addVersion(String objectSchema, String objectType, String objectName, int version);

    /**
     * Check to see if we described object is newer than we have currently in the
     * database
     * @param objectSchema
     * @param objectType
     * @param objectName
     * @param version
     * @return
     */
    public boolean applies(String objectSchema, String objectType, String objectName, int version);

    /**
     * Get the current version of an object
     * @param objectSchema
     * @param objectType
     * @param objectName
     * @return the current version of the given object or null if it doesn't exist
     */
    public Integer getVersion(String objectSchema, String objectType, String objectName);
}
