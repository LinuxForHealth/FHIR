/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Binding;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.NameUse;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A human's name with the ability to identify parts and usage.
 */
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class HumanName extends Element {
    @Summary
    @Binding(
        bindingName = "NameUse",
        strength = BindingStrength.Value.REQUIRED,
        valueSet = "http://hl7.org/fhir/ValueSet/name-use|4.3.0"
    )
    private final NameUse use;
    @Summary
    private final String text;
    @Summary
    private final String family;
    @Summary
    private final List<String> given;
    @Summary
    private final List<String> prefix;
    @Summary
    private final List<String> suffix;
    @Summary
    private final Period period;

    private HumanName(Builder builder) {
        super(builder);
        use = builder.use;
        text = builder.text;
        family = builder.family;
        given = Collections.unmodifiableList(builder.given);
        prefix = Collections.unmodifiableList(builder.prefix);
        suffix = Collections.unmodifiableList(builder.suffix);
        period = builder.period;
    }

    /**
     * Identifies the purpose for this name.
     * 
     * @return
     *     An immutable object of type {@link NameUse} that may be null.
     */
    public NameUse getUse() {
        return use;
    }

    /**
     * Specifies the entire name as it should be displayed e.g. on an application UI. This may be provided instead of or as 
     * well as the specific parts.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getText() {
        return text;
    }

    /**
     * The part of a name that links to the genealogy. In some cultures (e.g. Eritrea) the family name of a son is the first 
     * name of his father.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getFamily() {
        return family;
    }

    /**
     * Given name.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
     */
    public List<String> getGiven() {
        return given;
    }

    /**
     * Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that 
     * appears at the start of the name.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
     */
    public List<String> getPrefix() {
        return prefix;
    }

    /**
     * Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that 
     * appears at the end of the name.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link String} that may be empty.
     */
    public List<String> getSuffix() {
        return suffix;
    }

    /**
     * Indicates the period of time when this name was valid for the named person.
     * 
     * @return
     *     An immutable object of type {@link Period} that may be null.
     */
    public Period getPeriod() {
        return period;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (use != null) || 
            (text != null) || 
            (family != null) || 
            !given.isEmpty() || 
            !prefix.isEmpty() || 
            !suffix.isEmpty() || 
            (period != null);
    }

    @Override
    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, elementIndex, this);
            if (visitor.visit(elementName, elementIndex, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(use, "use", visitor);
                accept(text, "text", visitor);
                accept(family, "family", visitor);
                accept(given, "given", visitor, String.class);
                accept(prefix, "prefix", visitor, String.class);
                accept(suffix, "suffix", visitor, String.class);
                accept(period, "period", visitor);
            }
            visitor.visitEnd(elementName, elementIndex, this);
            visitor.postVisit(this);
        }
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
        HumanName other = (HumanName) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(use, other.use) && 
            Objects.equals(text, other.text) && 
            Objects.equals(family, other.family) && 
            Objects.equals(given, other.given) && 
            Objects.equals(prefix, other.prefix) && 
            Objects.equals(suffix, other.suffix) && 
            Objects.equals(period, other.period);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                use, 
                text, 
                family, 
                given, 
                prefix, 
                suffix, 
                period);
            hashCode = result;
        }
        return result;
    }

    @Override
    public Builder toBuilder() {
        return new Builder().from(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Element.Builder {
        private NameUse use;
        private String text;
        private String family;
        private List<String> given = new ArrayList<>();
        private List<String> prefix = new ArrayList<>();
        private List<String> suffix = new ArrayList<>();
        private Period period;

        private Builder() {
            super();
        }

        /**
         * Unique id for the element within a resource (for internal references). This may be any string value that does not 
         * contain spaces.
         * 
         * @param id
         *     Unique id for inter-element referencing
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder id(java.lang.String id) {
            return (Builder) super.id(id);
        }

        /**
         * May be used to represent additional information that is not part of the basic definition of the element. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder extension(Extension... extension) {
            return (Builder) super.extension(extension);
        }

        /**
         * May be used to represent additional information that is not part of the basic definition of the element. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        @Override
        public Builder extension(Collection<Extension> extension) {
            return (Builder) super.extension(extension);
        }

        /**
         * Identifies the purpose for this name.
         * 
         * @param use
         *     usual | official | temp | nickname | anonymous | old | maiden
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder use(NameUse use) {
            this.use = use;
            return this;
        }

        /**
         * Convenience method for setting {@code text}.
         * 
         * @param text
         *     Text representation of the full name
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #text(com.ibm.fhir.model.type.String)
         */
        public Builder text(java.lang.String text) {
            this.text = (text == null) ? null : String.of(text);
            return this;
        }

        /**
         * Specifies the entire name as it should be displayed e.g. on an application UI. This may be provided instead of or as 
         * well as the specific parts.
         * 
         * @param text
         *     Text representation of the full name
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder text(String text) {
            this.text = text;
            return this;
        }

        /**
         * Convenience method for setting {@code family}.
         * 
         * @param family
         *     Family name (often called 'Surname')
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #family(com.ibm.fhir.model.type.String)
         */
        public Builder family(java.lang.String family) {
            this.family = (family == null) ? null : String.of(family);
            return this;
        }

        /**
         * The part of a name that links to the genealogy. In some cultures (e.g. Eritrea) the family name of a son is the first 
         * name of his father.
         * 
         * @param family
         *     Family name (often called 'Surname')
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder family(String family) {
            this.family = family;
            return this;
        }

        /**
         * Convenience method for setting {@code given}.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param given
         *     Given names (not always 'first'). Includes middle names
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #given(com.ibm.fhir.model.type.String)
         */
        public Builder given(java.lang.String... given) {
            for (java.lang.String value : given) {
                this.given.add((value == null) ? null : String.of(value));
            }
            return this;
        }

        /**
         * Given name.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param given
         *     Given names (not always 'first'). Includes middle names
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder given(String... given) {
            for (String value : given) {
                this.given.add(value);
            }
            return this;
        }

        /**
         * Given name.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param given
         *     Given names (not always 'first'). Includes middle names
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder given(Collection<String> given) {
            this.given = new ArrayList<>(given);
            return this;
        }

        /**
         * Convenience method for setting {@code prefix}.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param prefix
         *     Parts that come before the name
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #prefix(com.ibm.fhir.model.type.String)
         */
        public Builder prefix(java.lang.String... prefix) {
            for (java.lang.String value : prefix) {
                this.prefix.add((value == null) ? null : String.of(value));
            }
            return this;
        }

        /**
         * Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that 
         * appears at the start of the name.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param prefix
         *     Parts that come before the name
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder prefix(String... prefix) {
            for (String value : prefix) {
                this.prefix.add(value);
            }
            return this;
        }

        /**
         * Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that 
         * appears at the start of the name.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param prefix
         *     Parts that come before the name
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder prefix(Collection<String> prefix) {
            this.prefix = new ArrayList<>(prefix);
            return this;
        }

        /**
         * Convenience method for setting {@code suffix}.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param suffix
         *     Parts that come after the name
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #suffix(com.ibm.fhir.model.type.String)
         */
        public Builder suffix(java.lang.String... suffix) {
            for (java.lang.String value : suffix) {
                this.suffix.add((value == null) ? null : String.of(value));
            }
            return this;
        }

        /**
         * Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that 
         * appears at the end of the name.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param suffix
         *     Parts that come after the name
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder suffix(String... suffix) {
            for (String value : suffix) {
                this.suffix.add(value);
            }
            return this;
        }

        /**
         * Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that 
         * appears at the end of the name.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param suffix
         *     Parts that come after the name
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder suffix(Collection<String> suffix) {
            this.suffix = new ArrayList<>(suffix);
            return this;
        }

        /**
         * Indicates the period of time when this name was valid for the named person.
         * 
         * @param period
         *     Time period when name was/is in use
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder period(Period period) {
            this.period = period;
            return this;
        }

        /**
         * Build the {@link HumanName}
         * 
         * @return
         *     An immutable object of type {@link HumanName}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid HumanName per the base specification
         */
        @Override
        public HumanName build() {
            HumanName humanName = new HumanName(this);
            if (validating) {
                validate(humanName);
            }
            return humanName;
        }

        protected void validate(HumanName humanName) {
            super.validate(humanName);
            ValidationSupport.checkList(humanName.given, "given", String.class);
            ValidationSupport.checkList(humanName.prefix, "prefix", String.class);
            ValidationSupport.checkList(humanName.suffix, "suffix", String.class);
            ValidationSupport.requireValueOrChildren(humanName);
        }

        protected Builder from(HumanName humanName) {
            super.from(humanName);
            use = humanName.use;
            text = humanName.text;
            family = humanName.family;
            given.addAll(humanName.given);
            prefix.addAll(humanName.prefix);
            suffix.addAll(humanName.suffix);
            period = humanName.period;
            return this;
        }
    }
}
