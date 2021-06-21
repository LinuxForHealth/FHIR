/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.control;

/**
 * Tracks the incremental changes to the FHIR schema as it evolves. Incremental
 * changes to the schema should be recorded here to create a new version number
 * and this enum can then be used to identify the schema objects associated with
 * a particular version.
 */
public enum FhirSchemaVersion {

    // Make sure the vid values are unique...this cannot be done programmatically with an enum
     V0001(1, "Initial version")
    ,V0002(2, "Composite search value support")
    ,V0003(3, "issue-1263 fhir_ref_sequence start with 20000")
    ,V0004(4, "row_id sequence cache 20 to 1000")
    ,V0005(5, "issue-1331 add index for resource.last_updated")
    ,V0006(6, "issue-1366 normalized schema for storing resource references")
    ,V0007(7, "issue-1273 add ref_version_id column to xxx_TOKEN_VALUES_V view")
    ,V0008(8, "issue-1929 expose common_token_value_id in xxx_TOKEN_VALUES_V view")
    ,V0009(9, "issue-1683 refactor composite values")
    ,V0010(10, "issue-1958 add IS_DELETED flag to each xxx_LOGICAL_RESOURCES table")
    ,V0011(11, "issue-2011 add LAST_UPDATED to each xxx_LOGICAL_RESOURCES table")
    ,V0012(12, "issue-2109 add VERSION_ID to each xxx_LOGICAL_RESOURCES table")
    ,V0013(13, "Add $erase operation for hard delete scenarios")
    ,V0014(14, "whole-system search and canonical references")
    ,V0015(15, "issue-2155 add parameter hash to bypass update during reindex")
    ,V0016(16, "issue-1921 add dedicated common_token_values mapping table for security")
    ;

    // The version number recorded in the VERSION_HISTORY
    private final int vid;

    // A meaningful description of the schema change
    private final String description;

    /**
     * Constructor for the enum
     * @param vn
     * @param description
     */
    private FhirSchemaVersion(int vn, String description) {
        this.vid = vn;
        this.description = description;
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
}
