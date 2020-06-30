/*
 * (C) Copyright IBM Corp. 2020
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
