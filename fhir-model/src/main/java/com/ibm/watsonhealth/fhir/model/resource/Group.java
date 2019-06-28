/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.annotation.Constraint;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.Boolean;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.type.Element;
import com.ibm.watsonhealth.fhir.model.type.Extension;
import com.ibm.watsonhealth.fhir.model.type.GroupType;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Identifier;
import com.ibm.watsonhealth.fhir.model.type.Meta;
import com.ibm.watsonhealth.fhir.model.type.Narrative;
import com.ibm.watsonhealth.fhir.model.type.Period;
import com.ibm.watsonhealth.fhir.model.type.Quantity;
import com.ibm.watsonhealth.fhir.model.type.Range;
import com.ibm.watsonhealth.fhir.model.type.Reference;
import com.ibm.watsonhealth.fhir.model.type.String;
import com.ibm.watsonhealth.fhir.model.type.UnsignedInt;
import com.ibm.watsonhealth.fhir.model.type.Uri;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * Represents a defined collection of entities that may be discussed or acted upon collectively but which are not 
 * expected to act collectively, and are not formally or legally recognized; i.e. a collection of entities that isn't an 
 * Organization.
 * </p>
 */
@Constraint(
    id = "grp-1",
    level = "Rule",
    location = "(base)",
    description = "Can only have members if group is \"actual\"",
    expression = "member.empty() or (actual = true)"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Group extends DomainResource {
    private final List<Identifier> identifier;
    private final Boolean active;
    private final GroupType type;
    private final Boolean actual;
    private final CodeableConcept code;
    private final String name;
    private final UnsignedInt quantity;
    private final Reference managingEntity;
    private final List<Characteristic> characteristic;
    private final List<Member> member;

    private Group(Builder builder) {
        super(builder);
        this.identifier = builder.identifier;
        this.active = builder.active;
        this.type = ValidationSupport.requireNonNull(builder.type, "type");
        this.actual = ValidationSupport.requireNonNull(builder.actual, "actual");
        this.code = builder.code;
        this.name = builder.name;
        this.quantity = builder.quantity;
        this.managingEntity = builder.managingEntity;
        this.characteristic = builder.characteristic;
        this.member = builder.member;
    }

    /**
     * <p>
     * A unique business identifier for this group.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Identifier}.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * <p>
     * Indicates whether the record for the group is available for use or is merely being retained for historical purposes.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Boolean}.
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * <p>
     * Identifies the broad classification of the kind of resources the group includes.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link GroupType}.
     */
    public GroupType getType() {
        return type;
    }

    /**
     * <p>
     * If true, indicates that the resource refers to a specific group of real individuals. If false, the group defines a set 
     * of intended individuals.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Boolean}.
     */
    public Boolean getActual() {
        return actual;
    }

    /**
     * <p>
     * Provides a specific type of resource the group includes; e.g. "cow", "syringe", etc.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getCode() {
        return code;
    }

    /**
     * <p>
     * A label assigned to the group for human identification and communication.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link String}.
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     * A count of the number of resource instances that are part of the group.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link UnsignedInt}.
     */
    public UnsignedInt getQuantity() {
        return quantity;
    }

    /**
     * <p>
     * Entity responsible for defining and maintaining Group characteristics and/or registered members.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Reference}.
     */
    public Reference getManagingEntity() {
        return managingEntity;
    }

    /**
     * <p>
     * Identifies traits whose presence r absence is shared by members of the group.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Characteristic}.
     */
    public List<Characteristic> getCharacteristic() {
        return characteristic;
    }

    /**
     * <p>
     * Identifies the resource instances that are members of the group.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link Member}.
     */
    public List<Member> getMember() {
        return member;
    }

    @Override
    public void accept(java.lang.String elementName, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, this);
            if (visitor.visit(elementName, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(meta, "meta", visitor);
                accept(implicitRules, "implicitRules", visitor);
                accept(language, "language", visitor);
                accept(text, "text", visitor);
                accept(contained, "contained", visitor, Resource.class);
                accept(extension, "extension", visitor, Extension.class);
                accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                accept(identifier, "identifier", visitor, Identifier.class);
                accept(active, "active", visitor);
                accept(type, "type", visitor);
                accept(actual, "actual", visitor);
                accept(code, "code", visitor);
                accept(name, "name", visitor);
                accept(quantity, "quantity", visitor);
                accept(managingEntity, "managingEntity", visitor);
                accept(characteristic, "characteristic", visitor, Characteristic.class);
                accept(member, "member", visitor, Member.class);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder(type, actual);
        builder.id = id;
        builder.meta = meta;
        builder.implicitRules = implicitRules;
        builder.language = language;
        builder.text = text;
        builder.contained.addAll(contained);
        builder.extension.addAll(extension);
        builder.modifierExtension.addAll(modifierExtension);
        builder.identifier.addAll(identifier);
        builder.active = active;
        builder.code = code;
        builder.name = name;
        builder.quantity = quantity;
        builder.managingEntity = managingEntity;
        builder.characteristic.addAll(characteristic);
        builder.member.addAll(member);
        return builder;
    }

    public static Builder builder(GroupType type, Boolean actual) {
        return new Builder(type, actual);
    }

    public static class Builder extends DomainResource.Builder {
        // required
        private final GroupType type;
        private final Boolean actual;

        // optional
        private List<Identifier> identifier = new ArrayList<>();
        private Boolean active;
        private CodeableConcept code;
        private String name;
        private UnsignedInt quantity;
        private Reference managingEntity;
        private List<Characteristic> characteristic = new ArrayList<>();
        private List<Member> member = new ArrayList<>();

        private Builder(GroupType type, Boolean actual) {
            super();
            this.type = type;
            this.actual = actual;
        }

        /**
         * <p>
         * The logical id of the resource, as used in the URL for the resource. Once assigned, this value never changes.
         * </p>
         * 
         * @param id
         *     Logical id of this artifact
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder id(Id id) {
            return (Builder) super.id(id);
        }

        /**
         * <p>
         * The metadata about the resource. This is content that is maintained by the infrastructure. Changes to the content 
         * might not always be associated with version changes to the resource.
         * </p>
         * 
         * @param meta
         *     Metadata about the resource
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder meta(Meta meta) {
            return (Builder) super.meta(meta);
        }

        /**
         * <p>
         * A reference to a set of rules that were followed when the resource was constructed, and which must be understood when 
         * processing the content. Often, this is a reference to an implementation guide that defines the special rules along 
         * with other profiles etc.
         * </p>
         * 
         * @param implicitRules
         *     A set of rules under which this content was created
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder implicitRules(Uri implicitRules) {
            return (Builder) super.implicitRules(implicitRules);
        }

        /**
         * <p>
         * The base language in which the resource is written.
         * </p>
         * 
         * @param language
         *     Language of the resource content
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder language(Code language) {
            return (Builder) super.language(language);
        }

        /**
         * <p>
         * A human-readable narrative that contains a summary of the resource and can be used to represent the content of the 
         * resource to a human. The narrative need not encode all the structured data, but is required to contain sufficient 
         * detail to make it "clinically safe" for a human to just read the narrative. Resource definitions may define what 
         * content should be represented in the narrative to ensure clinical safety.
         * </p>
         * 
         * @param text
         *     Text summary of the resource, for human interpretation
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder text(Narrative text) {
            return (Builder) super.text(text);
        }

        /**
         * <p>
         * These resources do not have an independent existence apart from the resource that contains them - they cannot be 
         * identified independently, and nor can they have their own independent transaction scope.
         * </p>
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder contained(Resource... contained) {
            return (Builder) super.contained(contained);
        }

        /**
         * <p>
         * These resources do not have an independent existence apart from the resource that contains them - they cannot be 
         * identified independently, and nor can they have their own independent transaction scope.
         * </p>
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder contained(Collection<Resource> contained) {
            return (Builder) super.contained(contained);
        }

        /**
         * <p>
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * </p>
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder extension(Extension... extension) {
            return (Builder) super.extension(extension);
        }

        /**
         * <p>
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * </p>
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder extension(Collection<Extension> extension) {
            return (Builder) super.extension(extension);
        }

        /**
         * <p>
         * May be used to represent additional information that is not part of the basic definition of the resource and that 
         * modifies the understanding of the element that contains it and/or the understanding of the containing element's 
         * descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe and 
         * manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
         * implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the 
         * definition of the extension. Applications processing a resource are required to check for modifier extensions.
         * </p>
         * <p>
         * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
         * change the meaning of modifierExtension itself).
         * </p>
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder modifierExtension(Extension... modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * <p>
         * May be used to represent additional information that is not part of the basic definition of the resource and that 
         * modifies the understanding of the element that contains it and/or the understanding of the containing element's 
         * descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe and 
         * manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
         * implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the 
         * definition of the extension. Applications processing a resource are required to check for modifier extensions.
         * </p>
         * <p>
         * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
         * change the meaning of modifierExtension itself).
         * </p>
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance.
         */
        @Override
        public Builder modifierExtension(Collection<Extension> modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * <p>
         * A unique business identifier for this group.
         * </p>
         * 
         * @param identifier
         *     Unique id
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder identifier(Identifier... identifier) {
            for (Identifier value : identifier) {
                this.identifier.add(value);
            }
            return this;
        }

        /**
         * <p>
         * A unique business identifier for this group.
         * </p>
         * 
         * @param identifier
         *     Unique id
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder identifier(Collection<Identifier> identifier) {
            this.identifier.addAll(identifier);
            return this;
        }

        /**
         * <p>
         * Indicates whether the record for the group is available for use or is merely being retained for historical purposes.
         * </p>
         * 
         * @param active
         *     Whether this group's record is in active use
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder active(Boolean active) {
            this.active = active;
            return this;
        }

        /**
         * <p>
         * Provides a specific type of resource the group includes; e.g. "cow", "syringe", etc.
         * </p>
         * 
         * @param code
         *     Kind of Group members
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder code(CodeableConcept code) {
            this.code = code;
            return this;
        }

        /**
         * <p>
         * A label assigned to the group for human identification and communication.
         * </p>
         * 
         * @param name
         *     Label for Group
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * <p>
         * A count of the number of resource instances that are part of the group.
         * </p>
         * 
         * @param quantity
         *     Number of members
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder quantity(UnsignedInt quantity) {
            this.quantity = quantity;
            return this;
        }

        /**
         * <p>
         * Entity responsible for defining and maintaining Group characteristics and/or registered members.
         * </p>
         * 
         * @param managingEntity
         *     Entity that is the custodian of the Group's definition
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder managingEntity(Reference managingEntity) {
            this.managingEntity = managingEntity;
            return this;
        }

        /**
         * <p>
         * Identifies traits whose presence r absence is shared by members of the group.
         * </p>
         * 
         * @param characteristic
         *     Include / Exclude group members by Trait
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder characteristic(Characteristic... characteristic) {
            for (Characteristic value : characteristic) {
                this.characteristic.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Identifies traits whose presence r absence is shared by members of the group.
         * </p>
         * 
         * @param characteristic
         *     Include / Exclude group members by Trait
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder characteristic(Collection<Characteristic> characteristic) {
            this.characteristic.addAll(characteristic);
            return this;
        }

        /**
         * <p>
         * Identifies the resource instances that are members of the group.
         * </p>
         * 
         * @param member
         *     Who or what is in group
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder member(Member... member) {
            for (Member value : member) {
                this.member.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Identifies the resource instances that are members of the group.
         * </p>
         * 
         * @param member
         *     Who or what is in group
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder member(Collection<Member> member) {
            this.member.addAll(member);
            return this;
        }

        @Override
        public Group build() {
            return new Group(this);
        }
    }

    /**
     * <p>
     * Identifies traits whose presence r absence is shared by members of the group.
     * </p>
     */
    public static class Characteristic extends BackboneElement {
        private final CodeableConcept code;
        private final Element value;
        private final Boolean exclude;
        private final Period period;

        private Characteristic(Builder builder) {
            super(builder);
            this.code = ValidationSupport.requireNonNull(builder.code, "code");
            this.value = ValidationSupport.requireChoiceElement(builder.value, "value", CodeableConcept.class, Boolean.class, Quantity.class, Range.class, Reference.class);
            this.exclude = ValidationSupport.requireNonNull(builder.exclude, "exclude");
            this.period = builder.period;
        }

        /**
         * <p>
         * A code that identifies the kind of trait being asserted.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}.
         */
        public CodeableConcept getCode() {
            return code;
        }

        /**
         * <p>
         * The value of the trait that holds (or does not hold - see 'exclude') for members of the group.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getValue() {
            return value;
        }

        /**
         * <p>
         * If true, indicates the characteristic is one that is NOT held by members of the group.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Boolean}.
         */
        public Boolean getExclude() {
            return exclude;
        }

        /**
         * <p>
         * The period over which the characteristic is tested; e.g. the patient had an operation during the month of June.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Period}.
         */
        public Period getPeriod() {
            return period;
        }

        @Override
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(code, "code", visitor);
                    accept(value, "value", visitor, true);
                    accept(exclude, "exclude", visitor);
                    accept(period, "period", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(CodeableConcept code, Element value, Boolean exclude) {
            return new Builder(code, value, exclude);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final CodeableConcept code;
            private final Element value;
            private final Boolean exclude;

            // optional
            private Period period;

            private Builder(CodeableConcept code, Element value, Boolean exclude) {
                super();
                this.code = code;
                this.value = value;
                this.exclude = exclude;
            }

            /**
             * <p>
             * Unique id for the element within a resource (for internal references). This may be any string value that does not 
             * contain spaces.
             * </p>
             * 
             * @param id
             *     Unique id for inter-element referencing
             * 
             * @return
             *     A reference to this Builder instance.
             */
            @Override
            public Builder id(java.lang.String id) {
                return (Builder) super.id(id);
            }

            /**
             * <p>
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
             * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
             * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
             * of the definition of the extension.
             * </p>
             * 
             * @param extension
             *     Additional content defined by implementations
             * 
             * @return
             *     A reference to this Builder instance.
             */
            @Override
            public Builder extension(Extension... extension) {
                return (Builder) super.extension(extension);
            }

            /**
             * <p>
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
             * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
             * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
             * of the definition of the extension.
             * </p>
             * 
             * @param extension
             *     Additional content defined by implementations
             * 
             * @return
             *     A reference to this Builder instance.
             */
            @Override
            public Builder extension(Collection<Extension> extension) {
                return (Builder) super.extension(extension);
            }

            /**
             * <p>
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * </p>
             * <p>
             * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * </p>
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance.
             */
            @Override
            public Builder modifierExtension(Extension... modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * <p>
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * </p>
             * <p>
             * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * </p>
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance.
             */
            @Override
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * <p>
             * The period over which the characteristic is tested; e.g. the patient had an operation during the month of June.
             * </p>
             * 
             * @param period
             *     Period over which characteristic is tested
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder period(Period period) {
                this.period = period;
                return this;
            }

            @Override
            public Characteristic build() {
                return new Characteristic(this);
            }

            private static Builder from(Characteristic characteristic) {
                Builder builder = new Builder(characteristic.code, characteristic.value, characteristic.exclude);
                builder.id = characteristic.id;
                builder.extension.addAll(characteristic.extension);
                builder.modifierExtension.addAll(characteristic.modifierExtension);
                builder.period = characteristic.period;
                return builder;
            }
        }
    }

    /**
     * <p>
     * Identifies the resource instances that are members of the group.
     * </p>
     */
    public static class Member extends BackboneElement {
        private final Reference entity;
        private final Period period;
        private final Boolean inactive;

        private Member(Builder builder) {
            super(builder);
            this.entity = ValidationSupport.requireNonNull(builder.entity, "entity");
            this.period = builder.period;
            this.inactive = builder.inactive;
        }

        /**
         * <p>
         * A reference to the entity that is a member of the group. Must be consistent with Group.type. If the entity is another 
         * group, then the type must be the same.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Reference}.
         */
        public Reference getEntity() {
            return entity;
        }

        /**
         * <p>
         * The period that the member was in the group, if known.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Period}.
         */
        public Period getPeriod() {
            return period;
        }

        /**
         * <p>
         * A flag to indicate that the member is no longer in the group, but previously may have been a member.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Boolean}.
         */
        public Boolean getInactive() {
            return inactive;
        }

        @Override
        public void accept(java.lang.String elementName, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, this);
                if (visitor.visit(elementName, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(entity, "entity", visitor);
                    accept(period, "period", visitor);
                    accept(inactive, "inactive", visitor);
                }
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder(Reference entity) {
            return new Builder(entity);
        }

        public static class Builder extends BackboneElement.Builder {
            // required
            private final Reference entity;

            // optional
            private Period period;
            private Boolean inactive;

            private Builder(Reference entity) {
                super();
                this.entity = entity;
            }

            /**
             * <p>
             * Unique id for the element within a resource (for internal references). This may be any string value that does not 
             * contain spaces.
             * </p>
             * 
             * @param id
             *     Unique id for inter-element referencing
             * 
             * @return
             *     A reference to this Builder instance.
             */
            @Override
            public Builder id(java.lang.String id) {
                return (Builder) super.id(id);
            }

            /**
             * <p>
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
             * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
             * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
             * of the definition of the extension.
             * </p>
             * 
             * @param extension
             *     Additional content defined by implementations
             * 
             * @return
             *     A reference to this Builder instance.
             */
            @Override
            public Builder extension(Extension... extension) {
                return (Builder) super.extension(extension);
            }

            /**
             * <p>
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
             * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
             * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
             * of the definition of the extension.
             * </p>
             * 
             * @param extension
             *     Additional content defined by implementations
             * 
             * @return
             *     A reference to this Builder instance.
             */
            @Override
            public Builder extension(Collection<Extension> extension) {
                return (Builder) super.extension(extension);
            }

            /**
             * <p>
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * </p>
             * <p>
             * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * </p>
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance.
             */
            @Override
            public Builder modifierExtension(Extension... modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * <p>
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * </p>
             * <p>
             * Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * </p>
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance.
             */
            @Override
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * <p>
             * The period that the member was in the group, if known.
             * </p>
             * 
             * @param period
             *     Period member belonged to the group
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder period(Period period) {
                this.period = period;
                return this;
            }

            /**
             * <p>
             * A flag to indicate that the member is no longer in the group, but previously may have been a member.
             * </p>
             * 
             * @param inactive
             *     If member is no longer in group
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder inactive(Boolean inactive) {
                this.inactive = inactive;
                return this;
            }

            @Override
            public Member build() {
                return new Member(this);
            }

            private static Builder from(Member member) {
                Builder builder = new Builder(member.entity);
                builder.id = member.id;
                builder.extension.addAll(member.extension);
                builder.modifierExtension.addAll(member.modifierExtension);
                builder.period = member.period;
                builder.inactive = member.inactive;
                return builder;
            }
        }
    }
}
