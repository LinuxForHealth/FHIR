/*
 * (C) Copyright IBM Corp. 2020, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Tracks the incremental changes to the FHIR schema as it evolves. Incremental
 * changes to the schema should be recorded here to create a new version number
 * and this enum can then be used to identify the schema objects associated with
 * a particular version.
 */
public enum FhirSchemaVersion {

    // Make sure the vid values are unique...this cannot be done programmatically with an enum
     V0001(1, "Initial version", true)
    ,V0002(2, "Composite search value support", true)
    ,V0003(3, "issue-1263 fhir_ref_sequence start with 20000", true)
    ,V0004(4, "row_id sequence cache 20 to 1000", true)
    ,V0005(5, "issue-1331 add index for resource.last_updated", true)
    ,V0006(6, "issue-1366 normalized schema for storing resource references", true)
    ,V0007(7, "issue-1273 add ref_version_id column to xxx_TOKEN_VALUES_V view", true)
    ,V0008(8, "issue-1929 expose common_token_value_id in xxx_TOKEN_VALUES_V view", true)
    ,V0009(9, "issue-1683 refactor composite values", true)
    ,V0010(10, "issue-1958 add IS_DELETED flag to each xxx_LOGICAL_RESOURCES table", true)
    ,V0011(11, "issue-2011 add LAST_UPDATED to each xxx_LOGICAL_RESOURCES table", true)
    ,V0012(12, "issue-2109 add VERSION_ID to each xxx_LOGICAL_RESOURCES table", true)
    ,V0013(13, "Add $erase operation for hard delete scenarios", true)
    ,V0014(14, "whole-system search and canonical references", true)
    ,V0015(15, "issue-2155 add parameter hash to bypass update during reindex", true)
    ,V0016(16, "issue-1921 add dedicated common_token_values mapping table for security", true)
    ,V0017(17, "issue-1822 add initial vacuum settings", true)
    ,V0018(18, "issue-1822 add optimized settings for postgres vacuum tables", true)
    ,V0019(19, "issue-1822 changes per the IBM Cloud Database Team", true)
    ,V0020(20, "issue-1834 Set PostgreSQL fillfactor", true)
    ,V0021(21, "issue-713 remove Resource_LOGICAL_RESOURCES, DomainResource_LOGICAL_RESOURCES tables", false)
    ,V0022(22, "issue-2979 stored procedure update for 2050 ifNoneMatch", false)
    ,V0023(23, "issue-2900 erased_resources to support $erase when offloading payloads", false)
    ,V0024(24, "issue-2900 for offloading add resource_payload_key to xx_resources", false)
    ,V0025(25, "issue-3158 stored proc updates to prevent deleting currently deleted resources", false)
    ,V0026(26, "issue-nnnn Add new resource types for FHIR R4B", false)
    ,V0027(27, "issue-3437 extensions to support distribution/sharding", true)
    ;

    // The version number recorded in the VERSION_HISTORY
    private final int vid;

    // A meaningful description of the schema change
    private final String description;

    // Version change affects parameter storage, which would require reindex of all resources
    // even if the search parameters and extracted values for those resources did not change
    private final boolean parameterStorageUpdated;

    /**
     * Constructor for the enum
     * @param vn the version number
     * @param description the description
     * @param parameterStorageUpdated true if version change affects parameter storage, otherwise false
     */
    private FhirSchemaVersion(int vn, String description, boolean parameterStorageUpdated) {
        this.vid = vn;
        this.description = description;
        this.parameterStorageUpdated = parameterStorageUpdated;
    }

    /**
     * Getter for the version number used by VERSION_HISTORY. We use vid
     * here as a short name to improve readability where this value is used
     * @return
     */
    public int vid() {
        return this.vid;
    }

    /**
     * Getter for the text description of the schema change
     * @return
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Determines if the version change affects parameter storage
     * @return
     */
    public boolean isParameterStorageUpdated() {
        return this.parameterStorageUpdated;
    }

    /**
     * Gets the latest version that included a parameter storage update, which
     * would require all resources to reindex all search parameters, even if the
     * search parameters and extracted values did not change.
     * @return latest version that included a parameter storage update
     */
    public static FhirSchemaVersion getLatestParameterStorageUpdate() {
        Optional<FhirSchemaVersion> version = Arrays.stream(FhirSchemaVersion.values())
                .filter(k -> k.isParameterStorageUpdated())
                .sorted(Comparator.comparing(FhirSchemaVersion::vid).reversed())
                .findFirst();
        return version.isPresent() ? version.get() : null;
    }

    /**
     * Get the max FhirSchemaVersion based on vid
     * @return
     */
    public static FhirSchemaVersion getLatestFhirSchemaVersion() {
        Optional<FhirSchemaVersion> maxVersion = Stream.of(FhirSchemaVersion.values()).max((l,r) -> {
            return Integer.compare(l.vid(), r.vid());
        });
        return maxVersion.get();
    }
}
