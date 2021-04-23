/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.service;

import static com.ibm.fhir.term.service.util.FHIRTermServiceUtil.getParameter;
import static com.ibm.fhir.term.service.util.FHIRTermServiceUtil.getParameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.DateTime;

/**
 * This class is used to represent the optional input parameters of the lookup operation:
 * <a href="http://hl7.org/fhir/codesystem-operation-lookup.html">http://hl7.org/fhir/codesystem-operation-lookup.html</a>
 */
public class LookupParameters {
    public static final LookupParameters EMPTY = LookupParameters.builder().build();

    private final DateTime date;
    private final Code displayLanguage;
    private final List<Code> property;

    private LookupParameters(Builder builder) {
        date = builder.date;
        displayLanguage = builder.displayLanguage;
        property = Collections.unmodifiableList(builder.property);
    }

    public DateTime getDate() {
        return date;
    }

    public Code getDisplayLanguage() {
        return displayLanguage;
    }

    public List<Code> getProperty() {
        return property;
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
        LookupParameters other = (LookupParameters) obj;
        return Objects.equals(date, other.date) &&
                Objects.equals(displayLanguage, other.displayLanguage) &&
                Objects.equals(property, other.property);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, displayLanguage, property);
    }

    public Builder toBuilder() {
        return new Builder().from(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static LookupParameters from(Parameters parameters) {
        Parameter date = getParameter(parameters, "date");
        Parameter displayLanguage = getParameter(parameters, "displayLanguage");
        return LookupParameters.builder()
                .date((date != null) ? date.getValue().as(DateTime.class): null)
                .displayLanguage((displayLanguage != null) ? displayLanguage.getValue().as(Code.class) : null)
                .property(getParameters(parameters, "property").stream()
                    .map(parameter -> parameter.getValue().as(Code.class))
                    .collect(Collectors.toList()))
                .build();
    }

    public static class Builder {
        private DateTime date;
        private Code displayLanguage;
        private List<Code> property = new ArrayList<>();

        private Builder() { }

        public Builder date(DateTime date) {
            this.date = date;
            return this;
        }

        public Builder displayLanguage(Code displayLanguage) {
            this.displayLanguage = displayLanguage;
            return this;
        }

        public Builder property(Code...property) {
            for (Code value : property) {
                this.property.add(value);
            }
            return this;
        }

        public Builder property(Collection<Code> property) {
            this.property = new ArrayList<>(property);
            return this;
        }

        public LookupParameters build() {
            return new LookupParameters(this);
        }

        protected Builder from(LookupParameters lookupParameters) {
            date = lookupParameters.date;
            displayLanguage = lookupParameters.displayLanguage;
            property.addAll(lookupParameters.property);
            return this;
        }
    }
}
