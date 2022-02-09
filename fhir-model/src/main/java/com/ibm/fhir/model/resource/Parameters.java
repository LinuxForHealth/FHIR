/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Choice;
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.Address;
import com.ibm.fhir.model.type.Age;
import com.ibm.fhir.model.type.Annotation;
import com.ibm.fhir.model.type.Attachment;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Base64Binary;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.ContactDetail;
import com.ibm.fhir.model.type.ContactPoint;
import com.ibm.fhir.model.type.Contributor;
import com.ibm.fhir.model.type.Count;
import com.ibm.fhir.model.type.DataRequirement;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Distance;
import com.ibm.fhir.model.type.Dosage;
import com.ibm.fhir.model.type.Duration;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Expression;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Integer;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Money;
import com.ibm.fhir.model.type.Oid;
import com.ibm.fhir.model.type.ParameterDefinition;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.PositiveInt;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Range;
import com.ibm.fhir.model.type.Ratio;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.RelatedArtifact;
import com.ibm.fhir.model.type.SampledData;
import com.ibm.fhir.model.type.Signature;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Time;
import com.ibm.fhir.model.type.Timing;
import com.ibm.fhir.model.type.TriggerDefinition;
import com.ibm.fhir.model.type.UnsignedInt;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.Url;
import com.ibm.fhir.model.type.UsageContext;
import com.ibm.fhir.model.type.Uuid;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * This resource is a non-persisted resource used to pass information into and back from an [operation](operations.html). 
 * It has no other use, and there is no RESTful endpoint associated with it.
 * 
 * <p>Maturity level: FMM5 (Normative)
 */
@Maturity(
    level = 5,
    status = StandardsStatus.Value.NORMATIVE
)
@Constraint(
    id = "inv-1",
    level = "Rule",
    location = "Parameters.parameter",
    description = "A parameter must have one and only one of (value, resource, part)",
    expression = "(part.exists() and value.empty() and resource.empty()) or (part.empty() and (value.exists() xor resource.exists()))",
    source = "http://hl7.org/fhir/StructureDefinition/Parameters"
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Parameters extends Resource {
    @Summary
    private final List<Parameter> parameter;

    private Parameters(Builder builder) {
        super(builder);
        parameter = Collections.unmodifiableList(builder.parameter);
    }

    /**
     * A parameter passed to or received from the operation.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Parameter} that may be empty.
     */
    public List<Parameter> getParameter() {
        return parameter;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !parameter.isEmpty();
    }

    @Override
    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, elementIndex, this);
            if (visitor.visit(elementName, elementIndex, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(meta, "meta", visitor);
                accept(implicitRules, "implicitRules", visitor);
                accept(language, "language", visitor);
                accept(parameter, "parameter", visitor, Parameter.class);
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
        Parameters other = (Parameters) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(parameter, other.parameter);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                meta, 
                implicitRules, 
                language, 
                parameter);
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

    public static class Builder extends Resource.Builder {
        private List<Parameter> parameter = new ArrayList<>();

        private Builder() {
            super();
        }

        /**
         * The logical id of the resource, as used in the URL for the resource. Once assigned, this value never changes.
         * 
         * @param id
         *     Logical id of this artifact
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder id(java.lang.String id) {
            return (Builder) super.id(id);
        }

        /**
         * The metadata about the resource. This is content that is maintained by the infrastructure. Changes to the content 
         * might not always be associated with version changes to the resource.
         * 
         * @param meta
         *     Metadata about the resource
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder meta(Meta meta) {
            return (Builder) super.meta(meta);
        }

        /**
         * A reference to a set of rules that were followed when the resource was constructed, and which must be understood when 
         * processing the content. Often, this is a reference to an implementation guide that defines the special rules along 
         * with other profiles etc.
         * 
         * @param implicitRules
         *     A set of rules under which this content was created
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder implicitRules(Uri implicitRules) {
            return (Builder) super.implicitRules(implicitRules);
        }

        /**
         * The base language in which the resource is written.
         * 
         * @param language
         *     Language of the resource content
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder language(Code language) {
            return (Builder) super.language(language);
        }

        /**
         * A parameter passed to or received from the operation.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param parameter
         *     Operation Parameter
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder parameter(Parameter... parameter) {
            for (Parameter value : parameter) {
                this.parameter.add(value);
            }
            return this;
        }

        /**
         * A parameter passed to or received from the operation.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param parameter
         *     Operation Parameter
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder parameter(Collection<Parameter> parameter) {
            this.parameter = new ArrayList<>(parameter);
            return this;
        }

        /**
         * Build the {@link Parameters}
         * 
         * @return
         *     An immutable object of type {@link Parameters}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Parameters per the base specification
         */
        @Override
        public Parameters build() {
            Parameters parameters = new Parameters(this);
            if (validating) {
                validate(parameters);
            }
            return parameters;
        }

        protected void validate(Parameters parameters) {
            super.validate(parameters);
            ValidationSupport.checkList(parameters.parameter, "parameter", Parameter.class);
        }

        protected Builder from(Parameters parameters) {
            super.from(parameters);
            parameter.addAll(parameters.parameter);
            return this;
        }
    }

    /**
     * A parameter passed to or received from the operation.
     */
    public static class Parameter extends BackboneElement {
        @Summary
        @Required
        private final String name;
        @Summary
        @Choice({ Base64Binary.class, Boolean.class, Canonical.class, Code.class, Date.class, DateTime.class, Decimal.class, Id.class, Instant.class, Integer.class, Markdown.class, Oid.class, PositiveInt.class, String.class, Time.class, UnsignedInt.class, Uri.class, Url.class, Uuid.class, Address.class, Age.class, Annotation.class, Attachment.class, CodeableConcept.class, Coding.class, ContactPoint.class, Count.class, Distance.class, Duration.class, HumanName.class, Identifier.class, Money.class, Period.class, Quantity.class, Range.class, Ratio.class, Reference.class, SampledData.class, Signature.class, Timing.class, ContactDetail.class, Contributor.class, DataRequirement.class, Expression.class, ParameterDefinition.class, RelatedArtifact.class, TriggerDefinition.class, UsageContext.class, Dosage.class, Meta.class })
        private final Element value;
        @Summary
        private final Resource resource;
        @Summary
        private final List<Parameters.Parameter> part;

        private Parameter(Builder builder) {
            super(builder);
            name = builder.name;
            value = builder.value;
            resource = builder.resource;
            part = Collections.unmodifiableList(builder.part);
        }

        /**
         * The name of the parameter (reference to the operation definition).
         * 
         * @return
         *     An immutable object of type {@link String} that is non-null.
         */
        public String getName() {
            return name;
        }

        /**
         * Conveys the content if the parameter is a data type.
         * 
         * @return
         *     An immutable object of type {@link Base64Binary}, {@link Boolean}, {@link Canonical}, {@link Code}, {@link Date}, 
         *     {@link DateTime}, {@link Decimal}, {@link Id}, {@link Instant}, {@link Integer}, {@link Markdown}, {@link Oid}, {@link 
         *     PositiveInt}, {@link String}, {@link Time}, {@link UnsignedInt}, {@link Uri}, {@link Url}, {@link Uuid}, {@link 
         *     Address}, {@link Age}, {@link Annotation}, {@link Attachment}, {@link CodeableConcept}, {@link Coding}, {@link 
         *     ContactPoint}, {@link Count}, {@link Distance}, {@link Duration}, {@link HumanName}, {@link Identifier}, {@link 
         *     Money}, {@link Period}, {@link Quantity}, {@link Range}, {@link Ratio}, {@link Reference}, {@link SampledData}, {@link 
         *     Signature}, {@link Timing}, {@link ContactDetail}, {@link Contributor}, {@link DataRequirement}, {@link Expression}, 
         *     {@link ParameterDefinition}, {@link RelatedArtifact}, {@link TriggerDefinition}, {@link UsageContext}, {@link Dosage} 
         *     or {@link Meta} that may be null.
         */
        public Element getValue() {
            return value;
        }

        /**
         * If the parameter is a whole resource.
         * 
         * @return
         *     An immutable object of type {@link Resource} that may be null.
         */
        public Resource getResource() {
            return resource;
        }

        /**
         * A named part of a multi-part parameter.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Parameter} that may be empty.
         */
        public List<Parameters.Parameter> getPart() {
            return part;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (name != null) || 
                (value != null) || 
                (resource != null) || 
                !part.isEmpty();
        }

        @Override
        public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, elementIndex, this);
                if (visitor.visit(elementName, elementIndex, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(name, "name", visitor);
                    accept(value, "value", visitor);
                    accept(resource, "resource", visitor);
                    accept(part, "part", visitor, Parameters.Parameter.class);
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
            Parameter other = (Parameter) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(name, other.name) && 
                Objects.equals(value, other.value) && 
                Objects.equals(resource, other.resource) && 
                Objects.equals(part, other.part);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    name, 
                    value, 
                    resource, 
                    part);
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

        public static class Builder extends BackboneElement.Builder {
            private String name;
            private Element value;
            private Resource resource;
            private List<Parameters.Parameter> part = new ArrayList<>();

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
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * 
             * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder modifierExtension(Extension... modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * 
             * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            @Override
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * Convenience method for setting {@code name}.
             * 
             * <p>This element is required.
             * 
             * @param name
             *     Name from the definition
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #name(com.ibm.fhir.model.type.String)
             */
            public Builder name(java.lang.String name) {
                this.name = (name == null) ? null : String.of(name);
                return this;
            }

            /**
             * The name of the parameter (reference to the operation definition).
             * 
             * <p>This element is required.
             * 
             * @param name
             *     Name from the definition
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder name(String name) {
                this.name = name;
                return this;
            }

            /**
             * Convenience method for setting {@code value} with choice type Boolean.
             * 
             * @param value
             *     If parameter is a data type
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #value(Element)
             */
            public Builder value(java.lang.Boolean value) {
                this.value = (value == null) ? null : Boolean.of(value);
                return this;
            }

            /**
             * Convenience method for setting {@code value} with choice type Date.
             * 
             * @param value
             *     If parameter is a data type
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #value(Element)
             */
            public Builder value(java.time.LocalDate value) {
                this.value = (value == null) ? null : Date.of(value);
                return this;
            }

            /**
             * Convenience method for setting {@code value} with choice type Instant.
             * 
             * @param value
             *     If parameter is a data type
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #value(Element)
             */
            public Builder value(java.time.ZonedDateTime value) {
                this.value = (value == null) ? null : Instant.of(value);
                return this;
            }

            /**
             * Convenience method for setting {@code value} with choice type Integer.
             * 
             * @param value
             *     If parameter is a data type
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #value(Element)
             */
            public Builder value(java.lang.Integer value) {
                this.value = (value == null) ? null : Integer.of(value);
                return this;
            }

            /**
             * Convenience method for setting {@code value} with choice type String.
             * 
             * @param value
             *     If parameter is a data type
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #value(Element)
             */
            public Builder value(java.lang.String value) {
                this.value = (value == null) ? null : String.of(value);
                return this;
            }

            /**
             * Convenience method for setting {@code value} with choice type Time.
             * 
             * @param value
             *     If parameter is a data type
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #value(Element)
             */
            public Builder value(java.time.LocalTime value) {
                this.value = (value == null) ? null : Time.of(value);
                return this;
            }

            /**
             * Conveys the content if the parameter is a data type.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Base64Binary}</li>
             * <li>{@link Boolean}</li>
             * <li>{@link Canonical}</li>
             * <li>{@link Code}</li>
             * <li>{@link Date}</li>
             * <li>{@link DateTime}</li>
             * <li>{@link Decimal}</li>
             * <li>{@link Id}</li>
             * <li>{@link Instant}</li>
             * <li>{@link Integer}</li>
             * <li>{@link Markdown}</li>
             * <li>{@link Oid}</li>
             * <li>{@link PositiveInt}</li>
             * <li>{@link String}</li>
             * <li>{@link Time}</li>
             * <li>{@link UnsignedInt}</li>
             * <li>{@link Uri}</li>
             * <li>{@link Url}</li>
             * <li>{@link Uuid}</li>
             * <li>{@link Address}</li>
             * <li>{@link Age}</li>
             * <li>{@link Annotation}</li>
             * <li>{@link Attachment}</li>
             * <li>{@link CodeableConcept}</li>
             * <li>{@link Coding}</li>
             * <li>{@link ContactPoint}</li>
             * <li>{@link Count}</li>
             * <li>{@link Distance}</li>
             * <li>{@link Duration}</li>
             * <li>{@link HumanName}</li>
             * <li>{@link Identifier}</li>
             * <li>{@link Money}</li>
             * <li>{@link Period}</li>
             * <li>{@link Quantity}</li>
             * <li>{@link Range}</li>
             * <li>{@link Ratio}</li>
             * <li>{@link Reference}</li>
             * <li>{@link SampledData}</li>
             * <li>{@link Signature}</li>
             * <li>{@link Timing}</li>
             * <li>{@link ContactDetail}</li>
             * <li>{@link Contributor}</li>
             * <li>{@link DataRequirement}</li>
             * <li>{@link Expression}</li>
             * <li>{@link ParameterDefinition}</li>
             * <li>{@link RelatedArtifact}</li>
             * <li>{@link TriggerDefinition}</li>
             * <li>{@link UsageContext}</li>
             * <li>{@link Dosage}</li>
             * <li>{@link Meta}</li>
             * </ul>
             * 
             * @param value
             *     If parameter is a data type
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder value(Element value) {
                this.value = value;
                return this;
            }

            /**
             * If the parameter is a whole resource.
             * 
             * @param resource
             *     If parameter is a whole resource
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder resource(Resource resource) {
                this.resource = resource;
                return this;
            }

            /**
             * A named part of a multi-part parameter.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param part
             *     Named part of a multi-part parameter
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder part(Parameters.Parameter... part) {
                for (Parameters.Parameter value : part) {
                    this.part.add(value);
                }
                return this;
            }

            /**
             * A named part of a multi-part parameter.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param part
             *     Named part of a multi-part parameter
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder part(Collection<Parameters.Parameter> part) {
                this.part = new ArrayList<>(part);
                return this;
            }

            /**
             * Build the {@link Parameter}
             * 
             * <p>Required elements:
             * <ul>
             * <li>name</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Parameter}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Parameter per the base specification
             */
            @Override
            public Parameter build() {
                Parameter parameter = new Parameter(this);
                if (validating) {
                    validate(parameter);
                }
                return parameter;
            }

            protected void validate(Parameter parameter) {
                super.validate(parameter);
                ValidationSupport.requireNonNull(parameter.name, "name");
                ValidationSupport.choiceElement(parameter.value, "value", Base64Binary.class, Boolean.class, Canonical.class, Code.class, Date.class, DateTime.class, Decimal.class, Id.class, Instant.class, Integer.class, Markdown.class, Oid.class, PositiveInt.class, String.class, Time.class, UnsignedInt.class, Uri.class, Url.class, Uuid.class, Address.class, Age.class, Annotation.class, Attachment.class, CodeableConcept.class, Coding.class, ContactPoint.class, Count.class, Distance.class, Duration.class, HumanName.class, Identifier.class, Money.class, Period.class, Quantity.class, Range.class, Ratio.class, Reference.class, SampledData.class, Signature.class, Timing.class, ContactDetail.class, Contributor.class, DataRequirement.class, Expression.class, ParameterDefinition.class, RelatedArtifact.class, TriggerDefinition.class, UsageContext.class, Dosage.class, Meta.class);
                ValidationSupport.checkList(parameter.part, "part", Parameters.Parameter.class);
                ValidationSupport.requireValueOrChildren(parameter);
            }

            protected Builder from(Parameter parameter) {
                super.from(parameter);
                name = parameter.name;
                value = parameter.value;
                resource = parameter.resource;
                part.addAll(parameter.part);
                return this;
            }
        }
    }
}
