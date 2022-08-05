/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

import org.linuxforhealth.fhir.model.annotation.Binding;
import org.linuxforhealth.fhir.model.annotation.Choice;
import org.linuxforhealth.fhir.model.annotation.Constraint;
import org.linuxforhealth.fhir.model.annotation.ReferenceTarget;
import org.linuxforhealth.fhir.model.annotation.Required;
import org.linuxforhealth.fhir.model.annotation.Summary;
import org.linuxforhealth.fhir.model.type.code.BindingStrength;
import org.linuxforhealth.fhir.model.type.code.TriggerType;
import org.linuxforhealth.fhir.model.util.ValidationSupport;
import org.linuxforhealth.fhir.model.visitor.Visitor;

/**
 * A description of a triggering event. Triggering events can be named events, data events, or periodic, as determined by 
 * the type element.
 */
@Constraint(
    id = "trd-1",
    level = "Rule",
    location = "(base)",
    description = "Either timing, or a data requirement, but not both",
    expression = "data.empty() or timing.empty()",
    source = "http://hl7.org/fhir/StructureDefinition/TriggerDefinition"
)
@Constraint(
    id = "trd-2",
    level = "Rule",
    location = "(base)",
    description = "A condition only if there is a data requirement",
    expression = "condition.exists() implies data.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/TriggerDefinition"
)
@Constraint(
    id = "trd-3",
    level = "Rule",
    location = "(base)",
    description = "A named event requires a name, a periodic event requires timing, and a data event requires data",
    expression = "(type = 'named-event' implies name.exists()) and (type = 'periodic' implies timing.exists()) and (type.startsWith('data-') implies data.exists())",
    source = "http://hl7.org/fhir/StructureDefinition/TriggerDefinition"
)
@Generated("org.linuxforhealth.fhir.tools.CodeGenerator")
public class TriggerDefinition extends Element {
    @Summary
    @Binding(
        bindingName = "TriggerType",
        strength = BindingStrength.Value.REQUIRED,
        valueSet = "http://hl7.org/fhir/ValueSet/trigger-type|4.3.0"
    )
    @Required
    private final TriggerType type;
    @Summary
    private final String name;
    @Summary
    @ReferenceTarget({ "Schedule" })
    @Choice({ Timing.class, Reference.class, Date.class, DateTime.class })
    private final Element timing;
    @Summary
    private final List<DataRequirement> data;
    @Summary
    private final Expression condition;

    private TriggerDefinition(Builder builder) {
        super(builder);
        type = builder.type;
        name = builder.name;
        timing = builder.timing;
        data = Collections.unmodifiableList(builder.data);
        condition = builder.condition;
    }

    /**
     * The type of triggering event.
     * 
     * @return
     *     An immutable object of type {@link TriggerType} that is non-null.
     */
    public TriggerType getType() {
        return type;
    }

    /**
     * A formal name for the event. This may be an absolute URI that identifies the event formally (e.g. from a trigger 
     * registry), or a simple relative URI that identifies the event in a local context.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getName() {
        return name;
    }

    /**
     * The timing of the event (if this is a periodic trigger).
     * 
     * @return
     *     An immutable object of type {@link Timing}, {@link Reference}, {@link Date} or {@link DateTime} that may be null.
     */
    public Element getTiming() {
        return timing;
    }

    /**
     * The triggering data of the event (if this is a data trigger). If more than one data is requirement is specified, then 
     * all the data requirements must be true.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link DataRequirement} that may be empty.
     */
    public List<DataRequirement> getData() {
        return data;
    }

    /**
     * A boolean-valued expression that is evaluated in the context of the container of the trigger definition and returns 
     * whether or not the trigger fires.
     * 
     * @return
     *     An immutable object of type {@link Expression} that may be null.
     */
    public Expression getCondition() {
        return condition;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (type != null) || 
            (name != null) || 
            (timing != null) || 
            !data.isEmpty() || 
            (condition != null);
    }

    @Override
    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, elementIndex, this);
            if (visitor.visit(elementName, elementIndex, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(type, "type", visitor);
                accept(name, "name", visitor);
                accept(timing, "timing", visitor);
                accept(data, "data", visitor, DataRequirement.class);
                accept(condition, "condition", visitor);
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
        TriggerDefinition other = (TriggerDefinition) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(type, other.type) && 
            Objects.equals(name, other.name) && 
            Objects.equals(timing, other.timing) && 
            Objects.equals(data, other.data) && 
            Objects.equals(condition, other.condition);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                type, 
                name, 
                timing, 
                data, 
                condition);
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
        private TriggerType type;
        private String name;
        private Element timing;
        private List<DataRequirement> data = new ArrayList<>();
        private Expression condition;

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
         * The type of triggering event.
         * 
         * <p>This element is required.
         * 
         * @param type
         *     named-event | periodic | data-changed | data-added | data-modified | data-removed | data-accessed | data-access-ended
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(TriggerType type) {
            this.type = type;
            return this;
        }

        /**
         * Convenience method for setting {@code name}.
         * 
         * @param name
         *     Name or URI that identifies the event
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #name(org.linuxforhealth.fhir.model.type.String)
         */
        public Builder name(java.lang.String name) {
            this.name = (name == null) ? null : String.of(name);
            return this;
        }

        /**
         * A formal name for the event. This may be an absolute URI that identifies the event formally (e.g. from a trigger 
         * registry), or a simple relative URI that identifies the event in a local context.
         * 
         * @param name
         *     Name or URI that identifies the event
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Convenience method for setting {@code timing} with choice type Date.
         * 
         * @param timing
         *     Timing of the event
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @see #timing(Element)
         */
        public Builder timing(java.time.LocalDate timing) {
            this.timing = (timing == null) ? null : Date.of(timing);
            return this;
        }

        /**
         * The timing of the event (if this is a periodic trigger).
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link Timing}</li>
         * <li>{@link Reference}</li>
         * <li>{@link Date}</li>
         * <li>{@link DateTime}</li>
         * </ul>
         * 
         * When of type {@link Reference}, the allowed resource types for this reference are:
         * <ul>
         * <li>{@link Schedule}</li>
         * </ul>
         * 
         * @param timing
         *     Timing of the event
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder timing(Element timing) {
            this.timing = timing;
            return this;
        }

        /**
         * The triggering data of the event (if this is a data trigger). If more than one data is requirement is specified, then 
         * all the data requirements must be true.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param data
         *     Triggering data of the event (multiple = 'and')
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder data(DataRequirement... data) {
            for (DataRequirement value : data) {
                this.data.add(value);
            }
            return this;
        }

        /**
         * The triggering data of the event (if this is a data trigger). If more than one data is requirement is specified, then 
         * all the data requirements must be true.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param data
         *     Triggering data of the event (multiple = 'and')
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder data(Collection<DataRequirement> data) {
            this.data = new ArrayList<>(data);
            return this;
        }

        /**
         * A boolean-valued expression that is evaluated in the context of the container of the trigger definition and returns 
         * whether or not the trigger fires.
         * 
         * @param condition
         *     Whether the event triggers (boolean expression)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder condition(Expression condition) {
            this.condition = condition;
            return this;
        }

        /**
         * Build the {@link TriggerDefinition}
         * 
         * <p>Required elements:
         * <ul>
         * <li>type</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link TriggerDefinition}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid TriggerDefinition per the base specification
         */
        @Override
        public TriggerDefinition build() {
            TriggerDefinition triggerDefinition = new TriggerDefinition(this);
            if (validating) {
                validate(triggerDefinition);
            }
            return triggerDefinition;
        }

        protected void validate(TriggerDefinition triggerDefinition) {
            super.validate(triggerDefinition);
            ValidationSupport.requireNonNull(triggerDefinition.type, "type");
            ValidationSupport.choiceElement(triggerDefinition.timing, "timing", Timing.class, Reference.class, Date.class, DateTime.class);
            ValidationSupport.checkList(triggerDefinition.data, "data", DataRequirement.class);
            ValidationSupport.checkReferenceType(triggerDefinition.timing, "timing", "Schedule");
            ValidationSupport.requireValueOrChildren(triggerDefinition);
        }

        protected Builder from(TriggerDefinition triggerDefinition) {
            super.from(triggerDefinition);
            type = triggerDefinition.type;
            name = triggerDefinition.name;
            timing = triggerDefinition.timing;
            data.addAll(triggerDefinition.data);
            condition = triggerDefinition.condition;
            return this;
        }
    }
}
