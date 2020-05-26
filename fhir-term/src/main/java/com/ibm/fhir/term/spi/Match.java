/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.spi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.ConceptMapEquivalence;

/**
 * This class represents a match result per the translate operation: <a href="http://hl7.org/fhir/conceptmap-operation-translate.html">http://hl7.org/fhir/conceptmap-operation-translate.html</a>
 */
public class Match {
    private final ConceptMapEquivalence equivalence;
    private final Coding concept;
    private final List<Product> product;
    private final Uri source;

    public Match(Builder builder) {
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

    @Override
    public String toString() {
        return "Match [equivalence=" + equivalence + ", concept=" + concept + ", product=" + product + ", source=" + source + "]";
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

        public Product(Builder builder) {
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

        @Override
        public String toString() {
            return "Product [element=" + element + ", concept=" + concept + "]";
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
