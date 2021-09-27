/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.service;

import static com.ibm.fhir.term.service.util.FHIRTermServiceUtil.getParameter;
import static com.ibm.fhir.term.service.util.FHIRTermServiceUtil.getParameters;
import static com.ibm.fhir.term.service.util.FHIRTermServiceUtil.getPart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Uri;

/**
 * This class is used to represent the optional input parameters of the translate operation:
 * <a href="http://hl7.org/fhir/conceptmap-operation-translate.html">http://hl7.org/fhir/conceptmap-operation-translate.html</a>
 */
public class TranslationParameters {
    public static final TranslationParameters EMPTY = TranslationParameters.builder().build();

    private final List<Dependency> dependency;
    private final Boolean reverse;

    private TranslationParameters(Builder builder) {
        dependency = Collections.unmodifiableList(builder.dependency);
        reverse = builder.reverse;
    }

    public List<Dependency> getDependency() {
        return dependency;
    }

    public Boolean getReverse() {
        return reverse;
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
        TranslationParameters other = (TranslationParameters) obj;
        return Objects.equals(dependency, other.dependency) &&
                Objects.equals(reverse, other.reverse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dependency, reverse);
    }

    public Builder toBuilder() {
        return new Builder().from(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static TranslationParameters from(Parameters parameters) {
        Parameter reverse = getParameter(parameters, "reverse");
        return TranslationParameters.builder()
                .dependency(getParameters(parameters, "dependency").stream()
                    .map(Dependency::from)
                    .collect(Collectors.toList()))
                .reverse((reverse != null) ? reverse.getValue().as(Boolean.class) : null)
                .build();
    }

    public static class Builder {
        private List<Dependency> dependency = new ArrayList<>();
        private Boolean reverse;

        private Builder() { }

        public Builder dependency(Dependency...dependency) {
            for (Dependency value : dependency) {
                this.dependency.add(value);
            }
            return this;
        }

        public Builder dependency(Collection<Dependency> dependency) {
            this.dependency = new ArrayList<>(dependency);
            return this;
        }

        public Builder reverse(Boolean reverse) {
            this.reverse = reverse;
            return this;
        }

        public TranslationParameters build() {
            return new TranslationParameters(this);
        }

        protected Builder from(TranslationParameters translationParameters) {
            dependency.addAll(translationParameters.dependency);
            reverse = translationParameters.reverse;
            return this;
        }
    }

    public static class Dependency {
        private final Uri element;
        private final CodeableConcept concept;

        private Dependency(Builder builder) {
            this.element = builder.element;
            this.concept = builder.concept;
        }

        public Uri getElement() {
            return element;
        }

        public CodeableConcept getConcept() {
            return concept;
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
            Dependency other = (Dependency) obj;
            return Objects.equals(element, other.element) &&
                    Objects.equals(concept, other.concept);
        }

        @Override
        public int hashCode() {
            return Objects.hash(element, concept);
        }

        public Builder toBuilder() {
            return new Builder().from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static Dependency from(Parameter parameter) {
            Parameter element = getPart(parameter, "element");
            Parameter concept = getPart(parameter, "concept");
            return Dependency.builder()
                    .element((element != null) ? element.getValue().as(Uri.class) : null)
                    .concept((concept != null) ? concept.getValue().as(CodeableConcept.class) : null)
                    .build();
        }

        public static class Builder {
            private Uri element;
            private CodeableConcept concept;

            private Builder() { }

            public Builder element(Uri element) {
                this.element = element;
                return this;
            }

            public Builder concept(CodeableConcept concept) {
                this.concept = concept;
                return this;
            }

            public Dependency build() {
                return new Dependency(this);
            }

            protected Builder from(Dependency dependency) {
                element = dependency.element;
                concept = dependency.concept;
                return this;
            }
        }
    }
}
