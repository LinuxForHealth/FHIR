/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.bucket.app;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Tracks the incremental changes to the FHIR BUCKET schema as it evolves.
 */
public enum FhirBucketSchemaVersion {

    // Make sure the vid values are unique...this cannot be done programmatically with an enum
     V0001(1, "Initial version")
    ;

    // The version number recorded in the VERSION_HISTORY
    private final int vid;

    // A meaningful description of the schema change
    private final String description;

    /**
     * Constructor for the enum
     * @param vn the version number
     * @param description the description
     */
    private FhirBucketSchemaVersion(int vn, String description) {
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

    /**
     * Get the max FhirBucketSchemaVersion based on vid
     * @return
     */
    public static FhirBucketSchemaVersion getLatestSchemaVersion() {
        Optional<FhirBucketSchemaVersion> maxVersion = Stream.of(FhirBucketSchemaVersion.values()).max((l,r) -> {
            return Integer.compare(l.vid(), r.vid());
        });
        return maxVersion.get();
    }
}