/*
 * (C) Copyright IBM Corp. 2019, 2021
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

import com.ibm.fhir.model.annotation.Binding;
import com.ibm.fhir.model.annotation.Choice;
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Range;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.UnsignedInt;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.GroupType;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Represents a defined collection of entities that may be discussed or acted upon collectively but which are not 
 * expected to act collectively, and are not formally or legally recognized; i.e. a collection of entities that isn't an 
 * Organization.
 * 
 * <p>Maturity level: FMM1 (Trial Use)
 */
@Maturity(
    level = 1,
    status = StandardsStatus.Value.TRIAL_USE
)
@Constraint(
    id = "grp-1",
    level = "Rule",
    location = "(base)",
    description = "Can only have members if group is \"actual\"",
    expression = "member.empty() or (actual = true)",
    source = "http://hl7.org/fhir/StructureDefinition/Group"
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Group extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    private final Boolean active;
    @Summary
    @Binding(
        bindingName = "GroupType",
        strength = BindingStrength.Value.REQUIRED,
        description = "Types of resources that are part of group.",
        valueSet = "http://hl7.org/fhir/ValueSet/group-type|4.0.1"
    )
    @Required
    private final GroupType type;
    @Summary
    @Required
    private final Boolean actual;
    @Summary
    @Binding(
        bindingName = "GroupKind",
        strength = BindingStrength.Value.EXAMPLE,
        description = "Kind of particular resource; e.g. cow, syringe, lake, etc."
    )
    private final CodeableConcept code;
    @Summary
    private final String name;
    @Summary
    private final UnsignedInt quantity;
    @Summary
    @ReferenceTarget({ "Organization", "RelatedPerson", "Practitioner", "PractitionerRole" })
    private final Reference managingEntity;
    private final List<Characteristic> characteristic;
    private final List<Member> member;

    private Group(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(builder.identifier);
        active = builder.active;
        type = builder.type;
        actual = builder.actual;
        code = builder.code;
        name = builder.name;
        quantity = builder.quantity;
        managingEntity = builder.managingEntity;
        characteristic = Collections.unmodifiableList(builder.characteristic);
        member = Collections.unmodifiableList(builder.member);
    }

    /**
     * A unique business identifier for this group.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * Indicates whether the record for the group is available for use or is merely being retained for historical purposes.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that may be null.
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * Identifies the broad classification of the kind of resources the group includes.
     * 
     * @return
     *     An immutable object of type {@link GroupType} that is non-null.
     */
    public GroupType getType() {
        return type;
    }

    /**
     * If true, indicates that the resource refers to a specific group of real individuals. If false, the group defines a set 
     * of intended individuals.
     * 
     * @return
     *     An immutable object of type {@link Boolean} that is non-null.
     */
    public Boolean getActual() {
        return actual;
    }

    /**
     * Provides a specific type of resource the group includes; e.g. "cow", "syringe", etc.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getCode() {
        return code;
    }

    /**
     * A label assigned to the group for human identification and communication.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getName() {
        return name;
    }

    /**
     * A count of the number of resource instances that are part of the group.
     * 
     * @return
     *     An immutable object of type {@link UnsignedInt} that may be null.
     */
    public UnsignedInt getQuantity() {
        return quantity;
    }

    /**
     * Entity responsible for defining and maintaining Group characteristics and/or registered members.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getManagingEntity() {
        return managingEntity;
    }

    /**
     * Identifies traits whose presence r absence is shared by members of the group.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Characteristic} that may be empty.
     */
    public List<Characteristic> getCharacteristic() {
        return characteristic;
    }

    /**
     * Identifies the resource instances that are members of the group.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Member} that may be empty.
     */
    public List<Member> getMember() {
        return member;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (active != null) || 
            (type != null) || 
            (actual != null) || 
            (code != null) || 
            (name != null) || 
            (quantity != null) || 
            (managingEntity != null) || 
            !characteristic.isEmpty() || 
            !member.isEmpty();
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
        Group other = (Group) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(active, other.active) && 
            Objects.equals(type, other.type) && 
            Objects.equals(actual, other.actual) && 
            Objects.equals(code, other.code) && 
            Objects.equals(name, other.name) && 
            Objects.equals(quantity, other.quantity) && 
            Objects.equals(managingEntity, other.managingEntity) && 
            Objects.equals(characteristic, other.characteristic) && 
            Objects.equals(member, other.member);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                meta, 
                implicitRules, 
                language, 
                text, 
                contained, 
                extension, 
                modifierExtension, 
                identifier, 
                active, 
                type, 
                actual, 
                code, 
                name, 
                quantity, 
                managingEntity, 
                characteristic, 
                member);
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

    public static class Builder extends DomainResource.Builder {
        private List<Identifier> identifier = new ArrayList<>();
        private Boolean active;
        private GroupType type;
        private Boolean actual;
        private CodeableConcept code;
        private String name;
        private UnsignedInt quantity;
        private Reference managingEntity;
        private List<Characteristic> characteristic = new ArrayList<>();
        private List<Member> member = new ArrayList<>();

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
         * A human-readable narrative that contains a summary of the resource and can be used to represent the content of the 
         * resource to a human. The narrative need not encode all the structured data, but is required to contain sufficient 
         * detail to make it "clinically safe" for a human to just read the narrative. Resource definitions may define what 
         * content should be represented in the narrative to ensure clinical safety.
         * 
         * @param text
         *     Text summary of the resource, for human interpretation
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder text(Narrative text) {
            return (Builder) super.text(text);
        }

        /**
         * These resources do not have an independent existence apart from the resource that contains them - they cannot be 
         * identified independently, and nor can they have their own independent transaction scope.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder contained(Resource... contained) {
            return (Builder) super.contained(contained);
        }

        /**
         * These resources do not have an independent existence apart from the resource that contains them - they cannot be 
         * identified independently, and nor can they have their own independent transaction scope.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        @Override
        public Builder contained(Collection<Resource> contained) {
            return (Builder) super.contained(contained);
        }

        /**
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
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
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
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
         * May be used to represent additional information that is not part of the basic definition of the resource and that 
         * modifies the understanding of the element that contains it and/or the understanding of the containing element's 
         * descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe and 
         * manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
         * implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the 
         * definition of the extension. Applications processing a resource are required to check for modifier extensions.
         * 
         * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
         * change the meaning of modifierExtension itself).
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder modifierExtension(Extension... modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * May be used to represent additional information that is not part of the basic definition of the resource and that 
         * modifies the understanding of the element that contains it and/or the understanding of the containing element's 
         * descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe and 
         * manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
         * implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the 
         * definition of the extension. Applications processing a resource are required to check for modifier extensions.
         * 
         * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
         * change the meaning of modifierExtension itself).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
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
         * A unique business identifier for this group.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Unique id
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identifier(Identifier... identifier) {
            for (Identifier value : identifier) {
                this.identifier.add(value);
            }
            return this;
        }

        /**
         * A unique business identifier for this group.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param identifier
         *     Unique id
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder identifier(Collection<Identifier> identifier) {
            this.identifier = new ArrayList<>(identifier);
            return this;
        }

        /**
         * Convenience method for setting {@code active}.
         * 
         * @param active
         *     Whether this group's record is in active use
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #active(com.ibm.fhir.model.type.Boolean)
         */
        public Builder active(java.lang.Boolean active) {
            this.active = (active == null) ? null : Boolean.of(active);
            return this;
        }

        /**
         * Indicates whether the record for the group is available for use or is merely being retained for historical purposes.
         * 
         * @param active
         *     Whether this group's record is in active use
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder active(Boolean active) {
            this.active = active;
            return this;
        }

        /**
         * Identifies the broad classification of the kind of resources the group includes.
         * 
         * <p>This element is required.
         * 
         * @param type
         *     person | animal | practitioner | device | medication | substance
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(GroupType type) {
            this.type = type;
            return this;
        }

        /**
         * Convenience method for setting {@code actual}.
         * 
         * <p>This element is required.
         * 
         * @param actual
         *     Descriptive or actual
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #actual(com.ibm.fhir.model.type.Boolean)
         */
        public Builder actual(java.lang.Boolean actual) {
            this.actual = (actual == null) ? null : Boolean.of(actual);
            return this;
        }

        /**
         * If true, indicates that the resource refers to a specific group of real individuals. If false, the group defines a set 
         * of intended individuals.
         * 
         * <p>This element is required.
         * 
         * @param actual
         *     Descriptive or actual
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder actual(Boolean actual) {
            this.actual = actual;
            return this;
        }

        /**
         * Provides a specific type of resource the group includes; e.g. "cow", "syringe", etc.
         * 
         * @param code
         *     Kind of Group members
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder code(CodeableConcept code) {
            this.code = code;
            return this;
        }

        /**
         * Convenience method for setting {@code name}.
         * 
         * @param name
         *     Label for Group
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
         * A label assigned to the group for human identification and communication.
         * 
         * @param name
         *     Label for Group
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * A count of the number of resource instances that are part of the group.
         * 
         * @param quantity
         *     Number of members
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder quantity(UnsignedInt quantity) {
            this.quantity = quantity;
            return this;
        }

        /**
         * Entity responsible for defining and maintaining Group characteristics and/or registered members.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Organization}</li>
         * <li>{@link RelatedPerson}</li>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * </ul>
         * 
         * @param managingEntity
         *     Entity that is the custodian of the Group's definition
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder managingEntity(Reference managingEntity) {
            this.managingEntity = managingEntity;
            return this;
        }

        /**
         * Identifies traits whose presence r absence is shared by members of the group.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param characteristic
         *     Include / Exclude group members by Trait
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder characteristic(Characteristic... characteristic) {
            for (Characteristic value : characteristic) {
                this.characteristic.add(value);
            }
            return this;
        }

        /**
         * Identifies traits whose presence r absence is shared by members of the group.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param characteristic
         *     Include / Exclude group members by Trait
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder characteristic(Collection<Characteristic> characteristic) {
            this.characteristic = new ArrayList<>(characteristic);
            return this;
        }

        /**
         * Identifies the resource instances that are members of the group.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param member
         *     Who or what is in group
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder member(Member... member) {
            for (Member value : member) {
                this.member.add(value);
            }
            return this;
        }

        /**
         * Identifies the resource instances that are members of the group.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param member
         *     Who or what is in group
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder member(Collection<Member> member) {
            this.member = new ArrayList<>(member);
            return this;
        }

        /**
         * Build the {@link Group}
         * 
         * <p>Required elements:
         * <ul>
         * <li>type</li>
         * <li>actual</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link Group}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Group per the base specification
         */
        @Override
        public Group build() {
            Group group = new Group(this);
            if (validating) {
                validate(group);
            }
            return group;
        }

        protected void validate(Group group) {
            super.validate(group);
            ValidationSupport.checkList(group.identifier, "identifier", Identifier.class);
            ValidationSupport.requireNonNull(group.type, "type");
            ValidationSupport.requireNonNull(group.actual, "actual");
            ValidationSupport.checkList(group.characteristic, "characteristic", Characteristic.class);
            ValidationSupport.checkList(group.member, "member", Member.class);
            ValidationSupport.checkReferenceType(group.managingEntity, "managingEntity", "Organization", "RelatedPerson", "Practitioner", "PractitionerRole");
        }

        protected Builder from(Group group) {
            super.from(group);
            identifier.addAll(group.identifier);
            active = group.active;
            type = group.type;
            actual = group.actual;
            code = group.code;
            name = group.name;
            quantity = group.quantity;
            managingEntity = group.managingEntity;
            characteristic.addAll(group.characteristic);
            member.addAll(group.member);
            return this;
        }
    }

    /**
     * Identifies traits whose presence r absence is shared by members of the group.
     */
    public static class Characteristic extends BackboneElement {
        @Binding(
            bindingName = "GroupCharacteristicKind",
            strength = BindingStrength.Value.EXAMPLE,
            description = "List of characteristics used to describe group members; e.g. gender, age, owner, location, etc."
        )
        @Required
        private final CodeableConcept code;
        @Choice({ CodeableConcept.class, Boolean.class, Quantity.class, Range.class, Reference.class })
        @Binding(
            bindingName = "GroupCharacteristicValue",
            strength = BindingStrength.Value.EXAMPLE,
            description = "Value of descriptive member characteristic; e.g. red, male, pneumonia, Caucasian, etc."
        )
        @Required
        private final Element value;
        @Required
        private final Boolean exclude;
        private final Period period;

        private Characteristic(Builder builder) {
            super(builder);
            code = builder.code;
            value = builder.value;
            exclude = builder.exclude;
            period = builder.period;
        }

        /**
         * A code that identifies the kind of trait being asserted.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that is non-null.
         */
        public CodeableConcept getCode() {
            return code;
        }

        /**
         * The value of the trait that holds (or does not hold - see 'exclude') for members of the group.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept}, {@link Boolean}, {@link Quantity}, {@link Range} or {@link 
         *     Reference} that is non-null.
         */
        public Element getValue() {
            return value;
        }

        /**
         * If true, indicates the characteristic is one that is NOT held by members of the group.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that is non-null.
         */
        public Boolean getExclude() {
            return exclude;
        }

        /**
         * The period over which the characteristic is tested; e.g. the patient had an operation during the month of June.
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
                (code != null) || 
                (value != null) || 
                (exclude != null) || 
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
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(code, "code", visitor);
                    accept(value, "value", visitor);
                    accept(exclude, "exclude", visitor);
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
            Characteristic other = (Characteristic) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(code, other.code) && 
                Objects.equals(value, other.value) && 
                Objects.equals(exclude, other.exclude) && 
                Objects.equals(period, other.period);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    code, 
                    value, 
                    exclude, 
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

        public static class Builder extends BackboneElement.Builder {
            private CodeableConcept code;
            private Element value;
            private Boolean exclude;
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
             * A code that identifies the kind of trait being asserted.
             * 
             * <p>This element is required.
             * 
             * @param code
             *     Kind of characteristic
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder code(CodeableConcept code) {
                this.code = code;
                return this;
            }

            /**
             * Convenience method for setting {@code value} with choice type Boolean.
             * 
             * <p>This element is required.
             * 
             * @param value
             *     Value held by characteristic
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
             * The value of the trait that holds (or does not hold - see 'exclude') for members of the group.
             * 
             * <p>This element is required.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link CodeableConcept}</li>
             * <li>{@link Boolean}</li>
             * <li>{@link Quantity}</li>
             * <li>{@link Range}</li>
             * <li>{@link Reference}</li>
             * </ul>
             * 
             * @param value
             *     Value held by characteristic
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder value(Element value) {
                this.value = value;
                return this;
            }

            /**
             * Convenience method for setting {@code exclude}.
             * 
             * <p>This element is required.
             * 
             * @param exclude
             *     Group includes or excludes
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #exclude(com.ibm.fhir.model.type.Boolean)
             */
            public Builder exclude(java.lang.Boolean exclude) {
                this.exclude = (exclude == null) ? null : Boolean.of(exclude);
                return this;
            }

            /**
             * If true, indicates the characteristic is one that is NOT held by members of the group.
             * 
             * <p>This element is required.
             * 
             * @param exclude
             *     Group includes or excludes
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder exclude(Boolean exclude) {
                this.exclude = exclude;
                return this;
            }

            /**
             * The period over which the characteristic is tested; e.g. the patient had an operation during the month of June.
             * 
             * @param period
             *     Period over which characteristic is tested
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder period(Period period) {
                this.period = period;
                return this;
            }

            /**
             * Build the {@link Characteristic}
             * 
             * <p>Required elements:
             * <ul>
             * <li>code</li>
             * <li>value</li>
             * <li>exclude</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Characteristic}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Characteristic per the base specification
             */
            @Override
            public Characteristic build() {
                Characteristic characteristic = new Characteristic(this);
                if (validating) {
                    validate(characteristic);
                }
                return characteristic;
            }

            protected void validate(Characteristic characteristic) {
                super.validate(characteristic);
                ValidationSupport.requireNonNull(characteristic.code, "code");
                ValidationSupport.requireChoiceElement(characteristic.value, "value", CodeableConcept.class, Boolean.class, Quantity.class, Range.class, Reference.class);
                ValidationSupport.requireNonNull(characteristic.exclude, "exclude");
                ValidationSupport.requireValueOrChildren(characteristic);
            }

            protected Builder from(Characteristic characteristic) {
                super.from(characteristic);
                code = characteristic.code;
                value = characteristic.value;
                exclude = characteristic.exclude;
                period = characteristic.period;
                return this;
            }
        }
    }

    /**
     * Identifies the resource instances that are members of the group.
     */
    public static class Member extends BackboneElement {
        @ReferenceTarget({ "Patient", "Practitioner", "PractitionerRole", "Device", "Medication", "Substance", "Group" })
        @Required
        private final Reference entity;
        private final Period period;
        private final Boolean inactive;

        private Member(Builder builder) {
            super(builder);
            entity = builder.entity;
            period = builder.period;
            inactive = builder.inactive;
        }

        /**
         * A reference to the entity that is a member of the group. Must be consistent with Group.type. If the entity is another 
         * group, then the type must be the same.
         * 
         * @return
         *     An immutable object of type {@link Reference} that is non-null.
         */
        public Reference getEntity() {
            return entity;
        }

        /**
         * The period that the member was in the group, if known.
         * 
         * @return
         *     An immutable object of type {@link Period} that may be null.
         */
        public Period getPeriod() {
            return period;
        }

        /**
         * A flag to indicate that the member is no longer in the group, but previously may have been a member.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that may be null.
         */
        public Boolean getInactive() {
            return inactive;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (entity != null) || 
                (period != null) || 
                (inactive != null);
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
                    accept(entity, "entity", visitor);
                    accept(period, "period", visitor);
                    accept(inactive, "inactive", visitor);
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
            Member other = (Member) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(entity, other.entity) && 
                Objects.equals(period, other.period) && 
                Objects.equals(inactive, other.inactive);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    entity, 
                    period, 
                    inactive);
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
            private Reference entity;
            private Period period;
            private Boolean inactive;

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
             * A reference to the entity that is a member of the group. Must be consistent with Group.type. If the entity is another 
             * group, then the type must be the same.
             * 
             * <p>This element is required.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Patient}</li>
             * <li>{@link Practitioner}</li>
             * <li>{@link PractitionerRole}</li>
             * <li>{@link Device}</li>
             * <li>{@link Medication}</li>
             * <li>{@link Substance}</li>
             * <li>{@link Group}</li>
             * </ul>
             * 
             * @param entity
             *     Reference to the group member
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder entity(Reference entity) {
                this.entity = entity;
                return this;
            }

            /**
             * The period that the member was in the group, if known.
             * 
             * @param period
             *     Period member belonged to the group
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder period(Period period) {
                this.period = period;
                return this;
            }

            /**
             * Convenience method for setting {@code inactive}.
             * 
             * @param inactive
             *     If member is no longer in group
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #inactive(com.ibm.fhir.model.type.Boolean)
             */
            public Builder inactive(java.lang.Boolean inactive) {
                this.inactive = (inactive == null) ? null : Boolean.of(inactive);
                return this;
            }

            /**
             * A flag to indicate that the member is no longer in the group, but previously may have been a member.
             * 
             * @param inactive
             *     If member is no longer in group
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder inactive(Boolean inactive) {
                this.inactive = inactive;
                return this;
            }

            /**
             * Build the {@link Member}
             * 
             * <p>Required elements:
             * <ul>
             * <li>entity</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Member}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Member per the base specification
             */
            @Override
            public Member build() {
                Member member = new Member(this);
                if (validating) {
                    validate(member);
                }
                return member;
            }

            protected void validate(Member member) {
                super.validate(member);
                ValidationSupport.requireNonNull(member.entity, "entity");
                ValidationSupport.checkReferenceType(member.entity, "entity", "Patient", "Practitioner", "PractitionerRole", "Device", "Medication", "Substance", "Group");
                ValidationSupport.requireValueOrChildren(member);
            }

            protected Builder from(Member member) {
                super.from(member);
                entity = member.entity;
                period = member.period;
                inactive = member.inactive;
                return this;
            }
        }
    }
}
