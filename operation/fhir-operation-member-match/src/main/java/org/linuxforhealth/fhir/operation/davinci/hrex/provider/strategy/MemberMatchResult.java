/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.operation.davinci.hrex.provider.strategy;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.jcip.annotations.NotThreadSafe;

/**
 * MemberMatch Responses are in three types.
 *
 * @see http://build.fhir.org/ig/HL7/davinci-ehrx/StructureDefinition-hrex-parameters-member-match-out.html
 */
@NotThreadSafe
public class MemberMatchResult {

    /**
     * Type of the Response
     */
    public enum ResponseType {
        NO_MATCH,
        SINGLE,
        MULTIPLE
    }

    private ResponseType responseType;
    private Map<String,String> types;
    private String system;
    private String value;

    /**
     * the MemberMatchResult Response Type
     * @return
     */
    public ResponseType getResponseType() {
        return responseType;
    }

    /**
     * gets the types of Identifiers
     * @return
     */
    public Map<String,String> getTypes(){
        return types;
    }

    /**
     * Get the system
     * @return
     */
    public String getSystem() {
        return system;
    }

    /**
     * Get the value
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     * creates a MemberMatchResult builder
     *
     * @return
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for the MemberMatchResult object type.
     */
    public static class Builder {

        private ResponseType responseType;
        private Map<String,String> types = new HashMap<>();
        private String system;
        private String value;

        private Builder() {
            // Private to the MemberMatchResult class.
        }

        /**
         * adds a system and code type
         *
         * @param system
         * @param code
         * @return
         */
        public Builder type(String system, String code) {
            types.put(system, code);
            return this;
        }

        /**
         * adds a value
         *
         * @param value
         * @return
         */
        public Builder value(String value) {
            this.value = value;
            return this;
        }

        /**
         * adds a system for the Member Identifier
         *
         * @param system
         * @return
         */
        public Builder system(String system) {
            if (system != null) {
                this.system = system;
            }
            return this;
        }

        /**
         * adds a ResponseType
         *
         * @param responseType
         * @return
         */
        public Builder responseType(ResponseType responseType) {
            this.responseType = responseType;
            return this;
        }

        /**
         * verifies the responseType field is populated.
         *
         * @throws IllegalArgumentException
         */
        private void validate() throws IllegalArgumentException {
            if (responseType == null) {
                throw new IllegalArgumentException("MemberMatchResults must specify a responseType");
            }
            if (responseType == ResponseType.SINGLE && (value == null || value.isEmpty())) {
                throw new IllegalArgumentException("MemberMatchResults must have a value when successful");
            }
        }

        /**
         * Builds a new MemberMatchResult and include the required fixed type.coding
         *
         * @return a fully constructed MemberMatch object
         * @throws IllegalArgumentException
         */
        public MemberMatchResult build() {
            validate();
            MemberMatchResult result = new MemberMatchResult();
            result.responseType = responseType;

            // Coding.system and Coding.code are fixed values
            // @see http://build.fhir.org/ig/HL7/davinci-ehrx/StructureDefinition-hrex-parameters-member-match-out.html
            types.put("http://terminology.hl7.org/CodeSystem/v2-0203", "MB");
            result.types = Collections.unmodifiableMap(types);
            result.system = system;
            result.value = value;
            return result;
        }
    }
}
