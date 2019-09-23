/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.audit.cadf.model;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Representation of the CADF Credential object
 */
public class CadfCredential {
    private final String type;
    private final String token;
    private final String authority;
    private final ArrayList<CadfMapItem> assertions;

    private CadfCredential(CadfCredential.Builder builder) {
        type = builder.type;
        token = builder.token;
        authority = builder.authority;
        assertions = builder.assertions;
    }

    /**
     * Validate contents of the CadfCredential object.
     * 
     * The logic is determined by the CADF specification.
     * 
     * @throws IllegalStateException when the event does not meet the specification.
     */
    private void validate() throws IllegalStateException {
        // The only required property is token
        if (this.token == null || this.token.length() == 0) {
            throw new IllegalStateException("token is required");
        }
        // if we are here, everything seems to be ok
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the authority
     */
    public String getAuthority() {
        return authority;
    }

    /**
     * @return the assertions
     */
    public ArrayList<CadfMapItem> getAssertions() {
        return assertions;
    }

    public static class Builder {
        private String type;
        private String token;
        private String authority;
        private ArrayList<CadfMapItem> assertions;

        /**
         * Constructs a Builder instance based on the Credential token
         * 
         * @param token -- String. The primary opaque or non-opaque identity or security
         *              token (e.g., an opaque or obfuscated user ID, opaque security
         *              token string, or security certificate).
         */
        public Builder(String token) {
            this.token = token;
        }

        /**
         * Set the optional credential type property.
         * 
         * @param type -- String. Type of credential. (e.g., auth. token, identity
         *             token, etc.)
         * @return Builder instance
         */
        public Builder withType(String type) {
            this.type = type;
            return this;
        }

        /**
         * Set the optional authority property.
         * 
         * @param authority - String. The trusted authority (a service) that understands
         *                  and can verify the credential.
         * @return Builder instance
         */
        public Builder withAuthority(String authority) {
            this.authority = authority;
            return this;
        }

        /**
         * Set the optional assertions property. This property contains a list of
         * additional opaque or non-opaque assertions or attributes that belong to the
         * credential. One example might be the certificate trust chaing if the
         * credential is a certificate.
         * 
         * @param assertions -- Array of CadfMapItem. Optional list of additional opaque
         *                   or non-opaque assertions or attributes that belong to the
         *                   credential.
         * @return Builder instance
         */
        public Builder withAssertions(CadfMapItem[] assertions) {
            this.assertions = new ArrayList<CadfMapItem>(Arrays.asList(assertions));
            return this;
        }

        /**
         * Set the optional assertions property. This property contains a list of
         * additional opaque or non-opaque assertions or attributes that belong to the
         * credential. One example might be the certificate trust chaing if the
         * credential is a certificate.
         * 
         * @param assertions -- Array of CadfMapItem. Optional list of additional opaque
         *                   or non-opaque assertions or attributes that belong to the
         *                   credential.
         * @return Builder instance
         */
        public Builder withAssertions(ArrayList<CadfMapItem> assertions) {
            this.assertions = assertions;
            return this;
        }

        /**
         * Add an assertion to the assertion list, one at a time.
         * 
         * @see withAssertions()
         * 
         * @param assertion -- A single CadfMapItem.
         * @return Builder instance
         */
        public Builder withAssertion(CadfMapItem assertion) {
            if (this.assertions == null) {
                this.assertions = new ArrayList<CadfMapItem>();
            }
            this.assertions.add(assertion);
            return this;
        }

        /**
         * Builds the CadfCredential object
         * 
         * @return {@link CadfCredential}
         * @throws IllegalStateException when the event does not meet the specification.
         */
        public CadfCredential build() throws IllegalStateException {
            CadfCredential cred = new CadfCredential(this);
            cred.validate();
            return cred;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    public String getToken() {
        return token;
    }
}
