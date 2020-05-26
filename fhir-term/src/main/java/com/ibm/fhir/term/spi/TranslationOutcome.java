/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.spi;

import static com.ibm.fhir.model.type.String.string;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.ConceptMapEquivalence;
import com.ibm.fhir.term.spi.TranslationOutcome.Match.Product;

/**
 * This class represents the outcome of translation per: <a href="http://hl7.org/fhir/conceptmap-operation-translate.html">http://hl7.org/fhir/conceptmap-operation-translate.html</a>
 */
public class TranslationOutcome {
    private final Boolean result;
    private final String message;
    private final List<Match> match;

    private TranslationOutcome(Builder builder) {
        result = Objects.requireNonNull(builder.result);
        message = builder.message;
        match = Collections.unmodifiableList(builder.match);
    }

    public Boolean getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public List<Match> getMatch() {
        return match;
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
        TranslationOutcome other = (TranslationOutcome) obj;
        return Objects.equals(result, other.result) &&
                Objects.equals(message, other.message) &&
                Objects.equals(match, other.match);
    }

    @Override
    public int hashCode() {
        return Objects.hash(result, message, match);
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

        for (Match match : this.match) {
            Parameter.Builder matchParameterBuilder = Parameter.builder();

            matchParameterBuilder.name(string("match"));

            if (match.equivalence != null) {
                matchParameterBuilder.part(Parameter.builder()
                    .name(string("equivalence"))
                    .value(match.equivalence)
                    .build());
            }

            if (match.concept != null) {
                matchParameterBuilder.part(Parameter.builder()
                    .name(string("concept"))
                    .value(match.concept)
                    .build());
            }

            for (Product product : match.product) {
                Parameter.Builder productParameterBuilder = Parameter.builder();

                productParameterBuilder.name(string("product"));

                if (product.element != null) {
                    productParameterBuilder.part(Parameter.builder()
                        .name(string("element"))
                        .value(product.element)
                        .build());
                }

                if (product.concept != null) {
                    productParameterBuilder.part(Parameter.builder()
                        .name(string("concept"))
                        .value(product.concept)
                        .build());
                }

                matchParameterBuilder.part(productParameterBuilder.build());
            }

            parametersBuilder.parameter(matchParameterBuilder.build());
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
        private List<Match> match = new ArrayList<>();

        private Builder() { }

        public Builder result(Boolean result) {
            this.result = result;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder match(Match... match) {
            for (Match value : match) {
                this.match.add(value);
            }
            return this;
        }

        public Builder match(Collection<Match> match) {
            this.match = new ArrayList<>(match);
            return this;
        }

        public TranslationOutcome build() {
            return new TranslationOutcome(this);
        }

        protected Builder from(TranslationOutcome translationOutcome) {
            result = translationOutcome.result;
            message = translationOutcome.message;
            match.addAll(translationOutcome.match);
            return this;
        }
    }

    public static class Match {
        private final ConceptMapEquivalence equivalence;
        private final Coding concept;
        private final List<Product> product;
        private final Uri source;

        private Match(Builder builder) {
            equivalence = builder.equivalence;
            concept = builder.concept;
            product = Collections.unmodifiableList(builder.product);
            source = builder.source;
        }

        public ConceptMapEquivalence getEquivalence() {
            return equivalence;
        }

        public Coding getConcept() {
            return concept;
        }

        public List<Product> getProduct() {
            return product;
        }

        public Uri getSource() {
            return source;
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
            Match other = (Match) obj;
            return Objects.equals(equivalence, other.equivalence) &&
                    Objects.equals(concept, other.concept) &&
                    Objects.equals(product, other.product) &&
                    Objects.equals(source, source);
        }

        @Override
        public int hashCode() {
            return Objects.hash(equivalence, concept, product, source);
        }

        public Builder toBuilder() {
            return new Builder().from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private ConceptMapEquivalence equivalence;
            private Coding concept;
            private List<Product> product = new ArrayList<>();
            private Uri source;

            private Builder() { }

            public Builder equivalence(ConceptMapEquivalence equivalence) {
                this.equivalence = equivalence;
                return this;
            }

            public Builder concept(Coding concept) {
                this.concept = concept;
                return this;
            }

            public Builder product(Product... product) {
                for (Product value : product) {
                    this.product.add(value);
                }
                return this;
            }

            public Builder product(Collection<Product> product) {
                this.product = new ArrayList<>(product);
                return this;
            }

            public Builder source(Uri source) {
                this.source = source;
                return this;
            }

            public Match build() {
                return new Match(this);
            }

            protected Builder from(Match match) {
                equivalence = match.equivalence;
                concept = match.concept;
                product.addAll(match.product);
                source = match.source;
                return this;
            }
        }

        public static class Product {
            private final Uri element;
            private final Coding concept;

            private Product(Builder builder) {
                element = builder.element;
                concept = builder.concept;
            }

            public Uri getElement() {
                return element;
            }

            public Coding getConcept() {
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
                Product other = (Product) obj;
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

            public static class Builder {
                private Uri element;
                private Coding concept;

                private Builder() { }

                public Builder element(Uri element) {
                    this.element = element;
                    return this;
                }

                public Builder concept(Coding concept) {
                    this.concept = concept;
                    return this;
                }

                public Product build() {
                    return new Product(this);
                }

                protected Builder from(Product product) {
                    this.element = product.element;
                    this.concept = product.concept;
                    return this;
                }
            }
        }
    }
}
