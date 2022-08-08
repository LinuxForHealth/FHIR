/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.persistence.jdbc.util;

import java.util.Objects;

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

    @Override
    public int hashCode() {
        return Objects.hash(fragment, uri, version);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CanonicalValue other = (CanonicalValue) obj;
        if (fragment == null) {
            if (other.fragment != null) {
                return false;
            }
        } else if (!fragment.equals(other.fragment)) {
            return false;
        }
        if (uri == null) {
            if (other.uri != null) {
                return false;
            }
        } else if (!uri.equals(other.uri)) {
            return false;
        }
        if (version == null) {
            if (other.version != null) {
                return false;
            }
        } else if (!version.equals(other.version)) {
            return false;
        }
        return true;
    }

}
