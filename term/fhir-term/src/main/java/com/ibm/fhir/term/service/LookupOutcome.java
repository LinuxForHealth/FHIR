/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.service;

import static com.ibm.fhir.model.type.String.string;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.String;

/**
 * This class is used to represent the outcome of the lookup operation:
 * <a href="http://hl7.org/fhir/codesystem-operation-lookup.html">http://hl7.org/fhir/codesystem-operation-lookup.html</a>
 */
public class LookupOutcome {
    private final String name;
    private final String version;
    private final String display;
    private final List<Designation> designation;
    private final List<Property> property;

    private LookupOutcome(Builder builder) {
        name = Objects.requireNonNull(builder.name);
        version = builder.version;
        display = Objects.requireNonNull(builder.display);
        designation = Collections.unmodifiableList(builder.designation);
        property = Collections.unmodifiableList(builder.property);
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getDisplay() {
        return display;
    }

    public List<Designation> getDesignation() {
        return designation;
    }

    public List<Property> getProperty() {
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
        LookupOutcome other = (LookupOutcome) obj;
        return Objects.equals(name, other.name) &&
                Objects.equals(version, other.version) &&
                Objects.equals(display, other.display) &&
                Objects.equals(designation, other.designation) &&
                Objects.equals(property, other.property);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, version, display, designation, property);
    }

    public Builder toBuilder() {
        return new Builder().from(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public Parameters toParameters() {
        Parameters.Builder parametersBuilder = Parameters.builder();

        parametersBuilder.parameter(Parameter.builder()
            .name(string("name"))
            .value(name)
            .build());

        if (version != null) {
            parametersBuilder.parameter(Parameter.builder()
                .name(string("version"))
                .value(version)
                .build());
        }

        parametersBuilder.parameter(Parameter.builder()
            .name(string("display"))
            .value(display)
            .build());

        for (Designation designation : this.designation) {
            Parameter.Builder designationParameterBuilder = Parameter.builder();

            designationParameterBuilder.name(string("designation"));

            if (designation.language != null) {
                designationParameterBuilder.part(Parameter.builder()
                    .name(string("language"))
                    .value(designation.language)
                    .build());
            }

            if (designation.use != null) {
                designationParameterBuilder.part(Parameter.builder()
                    .name(string("use"))
                    .value(designation.use)
                    .build());
            }

            designationParameterBuilder.part(Parameter.builder()
                .name(string("value"))
                .value(designation.value)
                .build());

            parametersBuilder.parameter(designationParameterBuilder.build());
        }

        for (Property property : this.property) {
            Parameter.Builder propertyParameterBuilder = Parameter.builder();

            propertyParameterBuilder.name(string("property"));

            propertyParameterBuilder.part(Parameter.builder()
                .name(string("code"))
                .value(property.code)
                .build());

            propertyParameterBuilder.part(Parameter.builder()
                .name(string("value"))
                .value(property.value)
                .build());

            if (property.description != null) {
                propertyParameterBuilder.part(Parameter.builder()
                    .name(string("description"))
                    .value(property.description)
                    .build());
            }

            for (Property subproperty : property.subproperty) {
                Parameter.Builder subpropertyParameterBuilder = Parameter.builder();

                subpropertyParameterBuilder.name(string("subproperty"));

                subpropertyParameterBuilder.part(Parameter.builder()
                    .name(string("code"))
                    .value(subproperty.code)
                    .build());

                subpropertyParameterBuilder.part(Parameter.builder()
                    .name(string("value"))
                    .value(subproperty.value)
                    .build());

                if (subproperty.description != null) {
                    subpropertyParameterBuilder.part(Parameter.builder()
                        .name(string("description"))
                        .value(subproperty.description)
                        .build());
                }

                propertyParameterBuilder.part(subpropertyParameterBuilder.build());
            }

            parametersBuilder.parameter(propertyParameterBuilder.build());
        }

        return parametersBuilder.build();
    }

    public static class Builder {
        private String name;
        private String version;
        private String display;
        private List<Designation> designation = new ArrayList<>();
        private List<Property> property = new ArrayList<>();

        private Builder() { }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder display(String display) {
            this.display = display;
            return this;
        }

        public Builder designation(Designation... designation) {
            for (Designation value : designation) {
                this.designation.add(value);
            }
            return this;
        }

        public Builder designation(Collection<Designation> designation) {
            this.designation = new ArrayList<>(designation);
            return this;
        }

        public Builder property(Property... property) {
            for (Property value : property) {
                this.property.add(value);
            }
            return this;
        }

        public Builder property(Collection<Property> property) {
            this.property = new ArrayList<>(property);
            return this;
        }

        public LookupOutcome build() {
            return new LookupOutcome(this);
        }

        public Builder from(LookupOutcome lookupOutcome) {
            name = lookupOutcome.name;
            version = lookupOutcome.version;
            display = lookupOutcome.display;
            designation.addAll(lookupOutcome.designation);
            property.addAll(lookupOutcome.property);
            return this;
        }
    }

    public static class Designation {
        private final Code language;
        private final Coding use;
        private final String value;

        public Designation(Builder builder) {
            language = builder.language;
            use = builder.use;
            value = Objects.requireNonNull(builder.value);
        }

        public Code getLanguage() {
            return language;
        }

        public Coding getUse() {
            return use;
        }

        public String getValue() {
            return value;
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
            Designation other = (Designation) obj;
            return Objects.equals(language, other.language) &&
                    Objects.equals(use, other.use) &&
                    Objects.equals(value, other.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(language, use, value);
        }

        public Builder toBuilder() {
            return new Builder().from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private Code language;
            private Coding use;
            private String value;

            private Builder() { }

            public Builder language(Code language) {
                this.language = language;
                return this;
            }

            public Builder use(Coding use) {
                this.use = use;
                return this;
            }

            public Builder value(String value) {
                this.value = value;
                return this;
            }

            public Designation build() {
                return new Designation(this);
            }

            public Builder from(Designation designation) {
                language = designation.language;
                use = designation.use;
                value = designation.value;
                return this;
            }
        }
    }

    public static class Property {
        private final Code code;
        private final Element value;
        private final String description;
        private final List<Property> subproperty;

        public Property(Builder builder) {
            code = Objects.requireNonNull(builder.code);
            value = Objects.requireNonNull(builder.value);
            description = builder.description;
            subproperty = Collections.unmodifiableList(builder.subproperty);
        }

        public Code getCode() {
            return code;
        }

        public Element getValue() {
            return value;
        }

        public String getDescription() {
            return description;
        }

        public List<Property> getSubproperty() {
            return subproperty;
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
            Property other = (Property) obj;
            return Objects.equals(code, other.code) &&
                    Objects.equals(value, other.value) &&
                    Objects.equals(description, other.description) &&
                    Objects.equals(subproperty, other.subproperty);
        }

        @Override
        public int hashCode() {
            return Objects.hash(code, value, description, subproperty);
        }

        public Builder toBuilder() {
            return new Builder().from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private Code code;
            private Element value;
            private String description;
            private List<Property> subproperty = new ArrayList<>();

            private Builder() { }

            public Builder code(Code code) {
                this.code = code;
                return this;
            }

            public Builder value(Element value) {
                this.value = value;
                return this;
            }

            public Builder description(String description) {
                this.description = description;
                return this;
            }

            public Builder subproperty(Property... subproperty) {
                for (Property value : subproperty) {
                    this.subproperty.add(value);
                }
                return this;
            }

            public Builder subproperty(Collection<Property> subproperty) {
                this.subproperty = new ArrayList<>(subproperty);
                return this;
            }

            public Property build() {
                return new Property(this);
            }

            protected Builder from(Property property) {
                code = property.code;
                value = property.value;
                description = property.description;
                subproperty.addAll(property.subproperty);
                return this;
            }
        }
    }
}
