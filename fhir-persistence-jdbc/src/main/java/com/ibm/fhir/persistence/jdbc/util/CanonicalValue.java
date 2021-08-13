/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util;

/**
 * A canonical value, with processing applied to identify the uri, version, and fragment
 */
public class CanonicalValue {

    private final String uri;
    private final String version;
    private final String fragment;

    /**
     * @param uri
     *          The uri portion of the canonical value
     * @param version
     *          The version portion of the canonical value, can be null
     * @param fragment
     *          The fragment portion of the canonical value, can be null
     */
    public CanonicalValue(String uri, String version, String fragment) {
        this.uri = uri;
        this.version = version;
        this.fragment = fragment;
    }

    /**
     * @return the uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @return the fragment
     */
    public String getFragment() {
        return fragment;
    }

}
