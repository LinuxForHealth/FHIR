/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.bucket.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Additional request options to modify the FHIR request
 */
public class RequestOptions {
    private final Map<String,String> headers = new HashMap<>();
    private final boolean parseResource;
    protected RequestOptions(Map<String,String> headers, boolean parseResource) {
        this.headers.putAll(headers);
        this.parseResource = parseResource;
    }
    /**
     * Factory method to instantiate a {@link Builder} for this class
     * @return
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Getter for the parseResource value
     * @return
     */
    public boolean isParseResource() {
        return this.parseResource;
    }

    /**
     * Gets an immutable Map of header values
     * @return
     */
    public Map<String,String> getHeaders() {
        return Collections.unmodifiableMap(this.headers);
    }

    /**
     * Builder inner class for fluent construction of RequestOptions
     */
    public static class Builder {
        private final Map<String,String> headers = new HashMap<>();
        private boolean parseResource = true;

        /**
         * Add a request header
         * @param name
         * @param value
         * @return
         */
        public Builder header(String name, String value) {
            this.headers.put(name,  value);
            return this;
        }

        /**
         * Set the parseResource flag
         * @param flag
         * @return
         */
        public Builder parseResource(boolean flag) {
            this.parseResource = flag;
            return this;
        }

        /**
         * Instantiate a RequestOptions instance using the current state
         * of this builder
         * @return
         */
        public RequestOptions build() {
            return new RequestOptions(this.headers, this.parseResource);
        }
    }
}
