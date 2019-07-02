/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.annotation.Constraint;
import com.ibm.watsonhealth.fhir.model.type.TriggerType;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * A description of a triggering event. Triggering events can be named events, data events, or periodic, as determined by 
 * the type element.
 * </p>
 */
@Constraint(
    id = "trd-1",
    level = "Rule",
    location = "(base)",
    description = "Either timing, or a data requirement, but not both",
    expression = "data.empty() or timing.empty()"
)
@Constraint(
    id = "trd-2",
    level = "Rule",
    location = "(base)",
    description = "A condition only if there is a data requirement",
    expression = "condition.exists() implies data.exists()"
)
@Constraint(
    id = "trd-3",
    level = "Rule",
    location = "(base)",
    description = "A named event requires a name, a periodic event requires timing, and a data event requires data",
    expression = "(type = 'named-event' implies name.exists()) and (type = 'periodic' implies timing.exists()) and (type.startsWith('data-') implies data.exists())"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class TriggerDefinition extends Element {
    private final TriggerType type;
    private final String name;
    private final Element timing;
    private final List<DataRequirement> data;
    private final Expression condition;

    private volatile int hashCode;

    private TriggerDefinition(Builder builder) {
        super(builder);
        type = ValidationSupport.requireNonNull(builder.type, "type");
        name = builder.name;
        timing = ValidationSupport.choiceElement(builder.timing, "timing", Timing.class, Reference.class, Date.class, DateTime.class);
        data = Collections.unmodifiableList(builder.data);
        condition = builder.condition;
    }

    /**
     * <p>
     * The type of triggering event.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link TriggerType}.
     */
    public TriggerType getType() {
        return type;
    }

    /**
     * <p>
     * A formal name for the event. This may be an absolute URI that identifies the event formally (e.g. from a trigger 
     * registry), or a simple relative URI that identifies the event in a local context.
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
     * The timing of the event (if this is a periodic trigger).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Element}.
     */
    public Element getTiming() {
        return timing;
    }

    /**
     * <p>
     * The triggering data of the event (if this is a data trigger). If more than one data is requirement is specified, then 
     * all the data requirements must be true.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link DataRequirement}.
     */
    public List<DataRequirement> getData() {
        return data;
    }

    /**
     * <p>
     * A boolean-valued expression that is evaluated in the context of the container of the trigger definition and returns 
     * whether or not the trigger fires.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Expression}.
     */
    public Expression getCondition() {
        return condition;
    }

    @Override
    public void accept(java.lang.String elementName, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, this);
            if (visitor.visit(elementName, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(type, "type", visitor);
                accept(name, "name", visitor);
                accept(timing, "timing", visitor);
                accept(data, "data", visitor, DataRequirement.class);
                accept(condition, "condition", visitor);
            }
            visitor.visitEnd(elementName, this);
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
        return new Builder(type).from(this);
    }

    public Builder toBuilder(TriggerType type) {
        return new Builder(type).from(this);
    }

    public static Builder builder(TriggerType type) {
        return new Builder(type);
    }

    public static class Builder extends Element.Builder {
        // required
        private final TriggerType type;

        // optional
        private String name;
        private Element timing;
        private List<DataRequirement> data = new ArrayList<>();
        private Expression condition;

        private Builder(TriggerType type) {
            super();
            this.type = type;
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
         * A formal name for the event. This may be an absolute URI that identifies the event formally (e.g. from a trigger 
         * registry), or a simple relative URI that identifies the event in a local context.
         * </p>
         * 
         * @param name
         *     Name or URI that identifies the event
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
         * The timing of the event (if this is a periodic trigger).
         * </p>
         * 
         * @param timing
         *     Timing of the event
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder timing(Element timing) {
            this.timing = timing;
            return this;
        }

        /**
         * <p>
         * The triggering data of the event (if this is a data trigger). If more than one data is requirement is specified, then 
         * all the data requirements must be true.
         * </p>
         * 
         * @param data
         *     Triggering data of the event (multiple = 'and')
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder data(DataRequirement... data) {
            for (DataRequirement value : data) {
                this.data.add(value);
            }
            return this;
        }

        /**
         * <p>
         * The triggering data of the event (if this is a data trigger). If more than one data is requirement is specified, then 
         * all the data requirements must be true.
         * </p>
         * 
         * @param data
         *     Triggering data of the event (multiple = 'and')
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder data(Collection<DataRequirement> data) {
            this.data.addAll(data);
            return this;
        }

        /**
         * <p>
         * A boolean-valued expression that is evaluated in the context of the container of the trigger definition and returns 
         * whether or not the trigger fires.
         * </p>
         * 
         * @param condition
         *     Whether the event triggers (boolean expression)
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder condition(Expression condition) {
            this.condition = condition;
            return this;
        }

        @Override
        public TriggerDefinition build() {
            return new TriggerDefinition(this);
        }

        private Builder from(TriggerDefinition triggerDefinition) {
            id = triggerDefinition.id;
            extension.addAll(triggerDefinition.extension);
            name = triggerDefinition.name;
            timing = triggerDefinition.timing;
            data.addAll(triggerDefinition.data);
            condition = triggerDefinition.condition;
            return this;
        }
    }
}
