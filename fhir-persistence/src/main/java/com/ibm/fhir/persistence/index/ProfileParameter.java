/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.index;

import java.util.Objects;

/**
 * A profile search parameter value
 */
public class ProfileParameter extends SearchParameterValue {
    
    private String url;

    // profile version value
    private String version;

    // profile fragment value
    private String fragment;

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Profile[");
        addDescription(result);
        result.append(",");
        result.append(url);
        result.append(",");
        result.append(version);
        result.append(",");
        result.append(fragment);
        result.append("]");
        return result.toString();
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the fragment
     */
    public String getFragment() {
        return fragment;
    }

    /**
     * @param fragment the fragment to set
     */
    public void setFragment(String fragment) {
        this.fragment = fragment;
    }

    @Override
    public int hashCode() {
        // yeah I know I could include the super hashCode in the list of values,
        // but this avoids unnecessary boxing
        return 31 * super.hashCode() + Objects.hash(url, version, fragment);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProfileParameter) {
            ProfileParameter that = (ProfileParameter)obj;
            return super.equals(obj)
                    && Objects.equals(this.url, that.url)
                    && Objects.equals(this.version, that.version)
                    && Objects.equals(this.fragment, that.fragment);
        }
        return false;
    }
}
