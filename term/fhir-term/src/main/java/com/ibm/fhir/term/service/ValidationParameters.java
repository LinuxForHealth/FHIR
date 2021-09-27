/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.service;

import static com.ibm.fhir.term.service.util.FHIRTermServiceUtil.getParameter;

import java.util.Objects;

import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.DateTime;

/**
 * This class is used to represent the optional input parameters of the validate-code operations:
 * <a href="http://hl7.org/fhir/codesystem-operation-validate-code.html">http://hl7.org/fhir/codesystem-operation-validate-code.html</a>
 * and <a href="http://hl7.org/fhir/valueset-operation-validate-code.html">http://hl7.org/fhir/valueset-operation-validate-code.html</a>
 */
public class ValidationParameters {
    public static final ValidationParameters EMPTY = ValidationParameters.builder().build();

    private final DateTime date;
    private final Boolean _abstract;
    private final Code displayLanguage;

    private ValidationParameters(Builder builder) {
        this.date = builder.date;
        this._abstract = builder._abstract;
        this.displayLanguage = builder.displayLanguage;
    }

    public DateTime getDate() {
        return date;
    }

    public Boolean getAbstract() {
        return _abstract;
    }

    public Code getDisplayLanguage() {
        return displayLanguage;
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
        ValidationParameters other = (ValidationParameters) obj;
        return Objects.equals(date, other.date) &&
                Objects.equals(_abstract, other._abstract) &&
                Objects.equals(displayLanguage, other.displayLanguage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, _abstract, displayLanguage);
    }

    public Builder toBuilder() {
        return new Builder().from(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static ValidationParameters from(Parameters parameters) {
        Parameter date = getParameter(parameters, "date");
        Parameter _abstract = getParameter(parameters, "abstract");
        Parameter displayLanguage = getParameter(parameters, "displayLanguage");
        return ValidationParameters.builder()
                .date((date != null) ? date.getValue().as(DateTime.class) : null)
                ._abstract((_abstract != null) ? _abstract.getValue().as(Boolean.class) : null)
                .displayLanguage((displayLanguage != null) ? displayLanguage.getValue().as(Code.class) : null)
                .build();
    }

    public static class Builder {
        private DateTime date;
        private Boolean _abstract;
        private Code displayLanguage;

        private Builder() { }

        public Builder date(DateTime date) {
            this.date = date;
            return this;
        }

        public Builder _abstract(Boolean _abstract) {
            this._abstract = _abstract;
            return this;
        }

        public Builder displayLanguage(Code displayLanguage) {
            this.displayLanguage = displayLanguage;
            return this;
        }

        public ValidationParameters build() {
            return new ValidationParameters(this);
        }

        protected Builder from(ValidationParameters validationParameters) {
            date = validationParameters.date;
            _abstract = validationParameters._abstract;
            displayLanguage = validationParameters.displayLanguage;
            return this;
        }
    }
}
