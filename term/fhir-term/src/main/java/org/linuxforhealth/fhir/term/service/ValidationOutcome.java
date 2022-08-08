/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.term.service;

import static org.linuxforhealth.fhir.model.type.String.string;

import java.util.Objects;

import org.linuxforhealth.fhir.model.resource.Parameters;
import org.linuxforhealth.fhir.model.resource.Parameters.Parameter;
import org.linuxforhealth.fhir.model.type.Boolean;
import org.linuxforhealth.fhir.model.type.String;

/**
 * This class is used to represent the outcome of the validate-code operations:
 * <a href="http://hl7.org/fhir/codesystem-operation-validate-code.html">http://hl7.org/fhir/codesystem-operation-validate-code.html</a>
 * and <a href="http://hl7.org/fhir/valueset-operation-validate-code.html">http://hl7.org/fhir/valueset-operation-validate-code.html</a>
 */
public class ValidationOutcome {
    private final Boolean result;
    private final String message;
    private final String display;

    private ValidationOutcome(Builder builder) {
        result = Objects.requireNonNull(builder.result);
        message = builder.message;
        display = builder.display;
    }

    public Boolean getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public String getDisplay() {
        return display;
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
        ValidationOutcome other = (ValidationOutcome) obj;
        return Objects.equals(result, other.result) &&
                Objects.equals(message, other.message) &&
                Objects.equals(display, other.display);
    }

    @Override
    public int hashCode() {
        return Objects.hash(result, message, display);
    }

    public Parameters toParameters() {
        Parameters.Builder parametersBuilder = Parameters.builder();

        parametersBuilder.parameter(Parameter.builder()
            .name(string("result"))
            .value(result)
            .build());

        if (message != null) {
            parametersBuilder.parameter(Parameter.builder()
                .name(string("message"))
                .value(message)
                .build());
        }

        if (display != null) {
            parametersBuilder.parameter(Parameter.builder()
                .name(string("display"))
                .value(display)
                .build());
        }

        return parametersBuilder.build();
    }

    public Builder toBuilder() {
        return new Builder().from(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Boolean result;
        private String message;
        private String display;

        private Builder() { }

        public Builder result(Boolean result) {
            this.result = result;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder display(String display) {
            this.display = display;
            return this;
        }

        public ValidationOutcome build() {
            return new ValidationOutcome(this);
        }

        protected Builder from(ValidationOutcome validationOutcome) {
            result = validationOutcome.result;
            message = validationOutcome.message;
            display = validationOutcome.display;
            return this;
        }
    }

    @Override
    public java.lang.String toString() {
        return "ValidationOutcome [display=" + display + ", message=" + message + ", result=" + result + "]";
    }
}
