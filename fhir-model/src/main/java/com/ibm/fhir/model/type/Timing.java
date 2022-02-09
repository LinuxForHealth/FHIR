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
import com.ibm.fhir.model.annotation.Choice;
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.DayOfWeek;
import com.ibm.fhir.model.type.code.EventTiming;
import com.ibm.fhir.model.type.code.UnitsOfTime;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Specifies an event that may occur multiple times. Timing schedules are used to record when things are planned, 
 * expected or requested to occur. The most common usage is in dosage instructions for medications. They are also used 
 * when planning care of various kinds, and may be used for reporting the schedule to which past regular activities were 
 * carried out.
 */
@Constraint(
    id = "tim-1",
    level = "Rule",
    location = "Timing.repeat",
    description = "if there's a duration, there needs to be duration units",
    expression = "duration.empty() or durationUnit.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/Timing"
)
@Constraint(
    id = "tim-2",
    level = "Rule",
    location = "Timing.repeat",
    description = "if there's a period, there needs to be period units",
    expression = "period.empty() or periodUnit.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/Timing"
)
@Constraint(
    id = "tim-4",
    level = "Rule",
    location = "Timing.repeat",
    description = "duration SHALL be a non-negative value",
    expression = "duration.exists() implies duration >= 0",
    source = "http://hl7.org/fhir/StructureDefinition/Timing"
)
@Constraint(
    id = "tim-5",
    level = "Rule",
    location = "Timing.repeat",
    description = "period SHALL be a non-negative value",
    expression = "period.exists() implies period >= 0",
    source = "http://hl7.org/fhir/StructureDefinition/Timing"
)
@Constraint(
    id = "tim-6",
    level = "Rule",
    location = "Timing.repeat",
    description = "If there's a periodMax, there must be a period",
    expression = "periodMax.empty() or period.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/Timing"
)
@Constraint(
    id = "tim-7",
    level = "Rule",
    location = "Timing.repeat",
    description = "If there's a durationMax, there must be a duration",
    expression = "durationMax.empty() or duration.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/Timing"
)
@Constraint(
    id = "tim-8",
    level = "Rule",
    location = "Timing.repeat",
    description = "If there's a countMax, there must be a count",
    expression = "countMax.empty() or count.exists()",
    source = "http://hl7.org/fhir/StructureDefinition/Timing"
)
@Constraint(
    id = "tim-9",
    level = "Rule",
    location = "Timing.repeat",
    description = "If there's an offset, there must be a when (and not C, CM, CD, CV)",
    expression = "offset.empty() or (when.exists() and ((when in ('C' | 'CM' | 'CD' | 'CV')).not()))",
    source = "http://hl7.org/fhir/StructureDefinition/Timing"
)
@Constraint(
    id = "tim-10",
    level = "Rule",
    location = "Timing.repeat",
    description = "If there's a timeOfDay, there cannot be a when, or vice versa",
    expression = "timeOfDay.empty() or when.empty()",
    source = "http://hl7.org/fhir/StructureDefinition/Timing"
)
@Constraint(
    id = "timing-11",
    level = "Warning",
    location = "(base)",
    description = "SHOULD contain a code from value set http://hl7.org/fhir/ValueSet/timing-abbreviation",
    expression = "code.exists() implies (code.memberOf('http://hl7.org/fhir/ValueSet/timing-abbreviation', 'preferred'))",
    source = "http://hl7.org/fhir/StructureDefinition/Timing",
    generated = true
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Timing extends BackboneElement {
    @Summary
    private final List<DateTime> event;
    @Summary
    private final Repeat repeat;
    @Summary
    @Binding(
        bindingName = "TimingAbbreviation",
        strength = BindingStrength.Value.PREFERRED,
        valueSet = "http://hl7.org/fhir/ValueSet/timing-abbreviation"
    )
    private final CodeableConcept code;

    private Timing(Builder builder) {
        super(builder);
        event = Collections.unmodifiableList(builder.event);
        repeat = builder.repeat;
        code = builder.code;
    }

    /**
     * Identifies specific times when the event occurs.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link DateTime} that may be empty.
     */
    public List<DateTime> getEvent() {
        return event;
    }

    /**
     * A set of rules that describe when the event is scheduled.
     * 
     * @return
     *     An immutable object of type {@link Repeat} that may be null.
     */
    public Repeat getRepeat() {
        return repeat;
    }

    /**
     * A code for the timing schedule (or just text in code.text). Some codes such as BID are ubiquitous, but many 
     * institutions define their own additional codes. If a code is provided, the code is understood to be a complete 
     * statement of whatever is specified in the structured timing data, and either the code or the data may be used to 
     * interpret the Timing, with the exception that .repeat.bounds still applies over the code (and is not contained in the 
     * code).
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getCode() {
        return code;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !event.isEmpty() || 
            (repeat != null) || 
            (code != null);
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
                accept(event, "event", visitor, DateTime.class);
                accept(repeat, "repeat", visitor);
                accept(code, "code", visitor);
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
        Timing other = (Timing) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(event, other.event) && 
            Objects.equals(repeat, other.repeat) && 
            Objects.equals(code, other.code);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                modifierExtension, 
                event, 
                repeat, 
                code);
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
        private List<DateTime> event = new ArrayList<>();
        private Repeat repeat;
        private CodeableConcept code;

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
         * Identifies specific times when the event occurs.
         * 
         * <p>Adds new element(s) to the existing list.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param event
         *     When the event occurs
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder event(DateTime... event) {
            for (DateTime value : event) {
                this.event.add(value);
            }
            return this;
        }

        /**
         * Identifies specific times when the event occurs.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection.
         * If any of the elements are null, calling {@link #build()} will fail.
         * 
         * @param event
         *     When the event occurs
         * 
         * @return
         *     A reference to this Builder instance
         * 
         * @throws NullPointerException
         *     If the passed collection is null
         */
        public Builder event(Collection<DateTime> event) {
            this.event = new ArrayList<>(event);
            return this;
        }

        /**
         * A set of rules that describe when the event is scheduled.
         * 
         * @param repeat
         *     When the event is to occur
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder repeat(Repeat repeat) {
            this.repeat = repeat;
            return this;
        }

        /**
         * A code for the timing schedule (or just text in code.text). Some codes such as BID are ubiquitous, but many 
         * institutions define their own additional codes. If a code is provided, the code is understood to be a complete 
         * statement of whatever is specified in the structured timing data, and either the code or the data may be used to 
         * interpret the Timing, with the exception that .repeat.bounds still applies over the code (and is not contained in the 
         * code).
         * 
         * @param code
         *     BID | TID | QID | AM | PM | QD | QOD | +
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder code(CodeableConcept code) {
            this.code = code;
            return this;
        }

        /**
         * Build the {@link Timing}
         * 
         * @return
         *     An immutable object of type {@link Timing}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Timing per the base specification
         */
        @Override
        public Timing build() {
            Timing timing = new Timing(this);
            if (validating) {
                validate(timing);
            }
            return timing;
        }

        protected void validate(Timing timing) {
            super.validate(timing);
            ValidationSupport.checkList(timing.event, "event", DateTime.class);
            ValidationSupport.requireValueOrChildren(timing);
        }

        protected Builder from(Timing timing) {
            super.from(timing);
            event.addAll(timing.event);
            repeat = timing.repeat;
            code = timing.code;
            return this;
        }
    }

    /**
     * A set of rules that describe when the event is scheduled.
     */
    public static class Repeat extends BackboneElement {
        @Summary
        @Choice({ Duration.class, Range.class, Period.class })
        private final Element bounds;
        @Summary
        private final PositiveInt count;
        @Summary
        private final PositiveInt countMax;
        @Summary
        private final Decimal duration;
        @Summary
        private final Decimal durationMax;
        @Summary
        @Binding(
            bindingName = "UnitsOfTime",
            strength = BindingStrength.Value.REQUIRED,
            valueSet = "http://hl7.org/fhir/ValueSet/units-of-time|4.3.0-CIBUILD"
        )
        private final UnitsOfTime durationUnit;
        @Summary
        private final PositiveInt frequency;
        @Summary
        private final PositiveInt frequencyMax;
        @Summary
        private final Decimal period;
        @Summary
        private final Decimal periodMax;
        @Summary
        @Binding(
            bindingName = "UnitsOfTime",
            strength = BindingStrength.Value.REQUIRED,
            valueSet = "http://hl7.org/fhir/ValueSet/units-of-time|4.3.0-CIBUILD"
        )
        private final UnitsOfTime periodUnit;
        @Summary
        @Binding(
            bindingName = "DayOfWeek",
            strength = BindingStrength.Value.REQUIRED,
            valueSet = "http://hl7.org/fhir/ValueSet/days-of-week|4.3.0-CIBUILD"
        )
        private final List<DayOfWeek> dayOfWeek;
        @Summary
        private final List<Time> timeOfDay;
        @Summary
        @Binding(
            bindingName = "EventTiming",
            strength = BindingStrength.Value.REQUIRED,
            valueSet = "http://hl7.org/fhir/ValueSet/event-timing|4.3.0-CIBUILD"
        )
        private final List<EventTiming> when;
        @Summary
        private final UnsignedInt offset;

        private Repeat(Builder builder) {
            super(builder);
            bounds = builder.bounds;
            count = builder.count;
            countMax = builder.countMax;
            duration = builder.duration;
            durationMax = builder.durationMax;
            durationUnit = builder.durationUnit;
            frequency = builder.frequency;
            frequencyMax = builder.frequencyMax;
            period = builder.period;
            periodMax = builder.periodMax;
            periodUnit = builder.periodUnit;
            dayOfWeek = Collections.unmodifiableList(builder.dayOfWeek);
            timeOfDay = Collections.unmodifiableList(builder.timeOfDay);
            when = Collections.unmodifiableList(builder.when);
            offset = builder.offset;
        }

        /**
         * Either a duration for the length of the timing schedule, a range of possible length, or outer bounds for start and/or 
         * end limits of the timing schedule.
         * 
         * @return
         *     An immutable object of type {@link Duration}, {@link Range} or {@link Period} that may be null.
         */
        public Element getBounds() {
            return bounds;
        }

        /**
         * A total count of the desired number of repetitions across the duration of the entire timing specification. If countMax 
         * is present, this element indicates the lower bound of the allowed range of count values.
         * 
         * @return
         *     An immutable object of type {@link PositiveInt} that may be null.
         */
        public PositiveInt getCount() {
            return count;
        }

        /**
         * If present, indicates that the count is a range - so to perform the action between [count] and [countMax] times.
         * 
         * @return
         *     An immutable object of type {@link PositiveInt} that may be null.
         */
        public PositiveInt getCountMax() {
            return countMax;
        }

        /**
         * How long this thing happens for when it happens. If durationMax is present, this element indicates the lower bound of 
         * the allowed range of the duration.
         * 
         * @return
         *     An immutable object of type {@link Decimal} that may be null.
         */
        public Decimal getDuration() {
            return duration;
        }

        /**
         * If present, indicates that the duration is a range - so to perform the action between [duration] and [durationMax] 
         * time length.
         * 
         * @return
         *     An immutable object of type {@link Decimal} that may be null.
         */
        public Decimal getDurationMax() {
            return durationMax;
        }

        /**
         * The units of time for the duration, in UCUM units.
         * 
         * @return
         *     An immutable object of type {@link UnitsOfTime} that may be null.
         */
        public UnitsOfTime getDurationUnit() {
            return durationUnit;
        }

        /**
         * The number of times to repeat the action within the specified period. If frequencyMax is present, this element 
         * indicates the lower bound of the allowed range of the frequency.
         * 
         * @return
         *     An immutable object of type {@link PositiveInt} that may be null.
         */
        public PositiveInt getFrequency() {
            return frequency;
        }

        /**
         * If present, indicates that the frequency is a range - so to repeat between [frequency] and [frequencyMax] times within 
         * the period or period range.
         * 
         * @return
         *     An immutable object of type {@link PositiveInt} that may be null.
         */
        public PositiveInt getFrequencyMax() {
            return frequencyMax;
        }

        /**
         * Indicates the duration of time over which repetitions are to occur; e.g. to express "3 times per day", 3 would be the 
         * frequency and "1 day" would be the period. If periodMax is present, this element indicates the lower bound of the 
         * allowed range of the period length.
         * 
         * @return
         *     An immutable object of type {@link Decimal} that may be null.
         */
        public Decimal getPeriod() {
            return period;
        }

        /**
         * If present, indicates that the period is a range from [period] to [periodMax], allowing expressing concepts such as 
         * "do this once every 3-5 days.
         * 
         * @return
         *     An immutable object of type {@link Decimal} that may be null.
         */
        public Decimal getPeriodMax() {
            return periodMax;
        }

        /**
         * The units of time for the period in UCUM units.
         * 
         * @return
         *     An immutable object of type {@link UnitsOfTime} that may be null.
         */
        public UnitsOfTime getPeriodUnit() {
            return periodUnit;
        }

        /**
         * If one or more days of week is provided, then the action happens only on the specified day(s).
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link DayOfWeek} that may be empty.
         */
        public List<DayOfWeek> getDayOfWeek() {
            return dayOfWeek;
        }

        /**
         * Specified time of day for action to take place.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Time} that may be empty.
         */
        public List<Time> getTimeOfDay() {
            return timeOfDay;
        }

        /**
         * An approximate time period during the day, potentially linked to an event of daily living that indicates when the 
         * action should occur.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link EventTiming} that may be empty.
         */
        public List<EventTiming> getWhen() {
            return when;
        }

        /**
         * The number of minutes from the event. If the event code does not indicate whether the minutes is before or after the 
         * event, then the offset is assumed to be after the event.
         * 
         * @return
         *     An immutable object of type {@link UnsignedInt} that may be null.
         */
        public UnsignedInt getOffset() {
            return offset;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (bounds != null) || 
                (count != null) || 
                (countMax != null) || 
                (duration != null) || 
                (durationMax != null) || 
                (durationUnit != null) || 
                (frequency != null) || 
                (frequencyMax != null) || 
                (period != null) || 
                (periodMax != null) || 
                (periodUnit != null) || 
                !dayOfWeek.isEmpty() || 
                !timeOfDay.isEmpty() || 
                !when.isEmpty() || 
                (offset != null);
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
                    accept(bounds, "bounds", visitor);
                    accept(count, "count", visitor);
                    accept(countMax, "countMax", visitor);
                    accept(duration, "duration", visitor);
                    accept(durationMax, "durationMax", visitor);
                    accept(durationUnit, "durationUnit", visitor);
                    accept(frequency, "frequency", visitor);
                    accept(frequencyMax, "frequencyMax", visitor);
                    accept(period, "period", visitor);
                    accept(periodMax, "periodMax", visitor);
                    accept(periodUnit, "periodUnit", visitor);
                    accept(dayOfWeek, "dayOfWeek", visitor, DayOfWeek.class);
                    accept(timeOfDay, "timeOfDay", visitor, Time.class);
                    accept(when, "when", visitor, EventTiming.class);
                    accept(offset, "offset", visitor);
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
            Repeat other = (Repeat) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(bounds, other.bounds) && 
                Objects.equals(count, other.count) && 
                Objects.equals(countMax, other.countMax) && 
                Objects.equals(duration, other.duration) && 
                Objects.equals(durationMax, other.durationMax) && 
                Objects.equals(durationUnit, other.durationUnit) && 
                Objects.equals(frequency, other.frequency) && 
                Objects.equals(frequencyMax, other.frequencyMax) && 
                Objects.equals(period, other.period) && 
                Objects.equals(periodMax, other.periodMax) && 
                Objects.equals(periodUnit, other.periodUnit) && 
                Objects.equals(dayOfWeek, other.dayOfWeek) && 
                Objects.equals(timeOfDay, other.timeOfDay) && 
                Objects.equals(when, other.when) && 
                Objects.equals(offset, other.offset);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    bounds, 
                    count, 
                    countMax, 
                    duration, 
                    durationMax, 
                    durationUnit, 
                    frequency, 
                    frequencyMax, 
                    period, 
                    periodMax, 
                    periodUnit, 
                    dayOfWeek, 
                    timeOfDay, 
                    when, 
                    offset);
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
            private Element bounds;
            private PositiveInt count;
            private PositiveInt countMax;
            private Decimal duration;
            private Decimal durationMax;
            private UnitsOfTime durationUnit;
            private PositiveInt frequency;
            private PositiveInt frequencyMax;
            private Decimal period;
            private Decimal periodMax;
            private UnitsOfTime periodUnit;
            private List<DayOfWeek> dayOfWeek = new ArrayList<>();
            private List<Time> timeOfDay = new ArrayList<>();
            private List<EventTiming> when = new ArrayList<>();
            private UnsignedInt offset;

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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
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
             * Either a duration for the length of the timing schedule, a range of possible length, or outer bounds for start and/or 
             * end limits of the timing schedule.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Duration}</li>
             * <li>{@link Range}</li>
             * <li>{@link Period}</li>
             * </ul>
             * 
             * @param bounds
             *     Length/Range of lengths, or (Start and/or end) limits
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder bounds(Element bounds) {
                this.bounds = bounds;
                return this;
            }

            /**
             * A total count of the desired number of repetitions across the duration of the entire timing specification. If countMax 
             * is present, this element indicates the lower bound of the allowed range of count values.
             * 
             * @param count
             *     Number of times to repeat
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder count(PositiveInt count) {
                this.count = count;
                return this;
            }

            /**
             * If present, indicates that the count is a range - so to perform the action between [count] and [countMax] times.
             * 
             * @param countMax
             *     Maximum number of times to repeat
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder countMax(PositiveInt countMax) {
                this.countMax = countMax;
                return this;
            }

            /**
             * How long this thing happens for when it happens. If durationMax is present, this element indicates the lower bound of 
             * the allowed range of the duration.
             * 
             * @param duration
             *     How long when it happens
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder duration(Decimal duration) {
                this.duration = duration;
                return this;
            }

            /**
             * If present, indicates that the duration is a range - so to perform the action between [duration] and [durationMax] 
             * time length.
             * 
             * @param durationMax
             *     How long when it happens (Max)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder durationMax(Decimal durationMax) {
                this.durationMax = durationMax;
                return this;
            }

            /**
             * The units of time for the duration, in UCUM units.
             * 
             * @param durationUnit
             *     s | min | h | d | wk | mo | a - unit of time (UCUM)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder durationUnit(UnitsOfTime durationUnit) {
                this.durationUnit = durationUnit;
                return this;
            }

            /**
             * The number of times to repeat the action within the specified period. If frequencyMax is present, this element 
             * indicates the lower bound of the allowed range of the frequency.
             * 
             * @param frequency
             *     Event occurs frequency times per period
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder frequency(PositiveInt frequency) {
                this.frequency = frequency;
                return this;
            }

            /**
             * If present, indicates that the frequency is a range - so to repeat between [frequency] and [frequencyMax] times within 
             * the period or period range.
             * 
             * @param frequencyMax
             *     Event occurs up to frequencyMax times per period
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder frequencyMax(PositiveInt frequencyMax) {
                this.frequencyMax = frequencyMax;
                return this;
            }

            /**
             * Indicates the duration of time over which repetitions are to occur; e.g. to express "3 times per day", 3 would be the 
             * frequency and "1 day" would be the period. If periodMax is present, this element indicates the lower bound of the 
             * allowed range of the period length.
             * 
             * @param period
             *     Event occurs frequency times per period
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder period(Decimal period) {
                this.period = period;
                return this;
            }

            /**
             * If present, indicates that the period is a range from [period] to [periodMax], allowing expressing concepts such as 
             * "do this once every 3-5 days.
             * 
             * @param periodMax
             *     Upper limit of period (3-4 hours)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder periodMax(Decimal periodMax) {
                this.periodMax = periodMax;
                return this;
            }

            /**
             * The units of time for the period in UCUM units.
             * 
             * @param periodUnit
             *     s | min | h | d | wk | mo | a - unit of time (UCUM)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder periodUnit(UnitsOfTime periodUnit) {
                this.periodUnit = periodUnit;
                return this;
            }

            /**
             * If one or more days of week is provided, then the action happens only on the specified day(s).
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param dayOfWeek
             *     mon | tue | wed | thu | fri | sat | sun
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder dayOfWeek(DayOfWeek... dayOfWeek) {
                for (DayOfWeek value : dayOfWeek) {
                    this.dayOfWeek.add(value);
                }
                return this;
            }

            /**
             * If one or more days of week is provided, then the action happens only on the specified day(s).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param dayOfWeek
             *     mon | tue | wed | thu | fri | sat | sun
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder dayOfWeek(Collection<DayOfWeek> dayOfWeek) {
                this.dayOfWeek = new ArrayList<>(dayOfWeek);
                return this;
            }

            /**
             * Convenience method for setting {@code timeOfDay}.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param timeOfDay
             *     Time of day for action
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @see #timeOfDay(com.ibm.fhir.model.type.Time)
             */
            public Builder timeOfDay(java.time.LocalTime... timeOfDay) {
                for (java.time.LocalTime value : timeOfDay) {
                    this.timeOfDay.add((value == null) ? null : Time.of(value));
                }
                return this;
            }

            /**
             * Specified time of day for action to take place.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param timeOfDay
             *     Time of day for action
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder timeOfDay(Time... timeOfDay) {
                for (Time value : timeOfDay) {
                    this.timeOfDay.add(value);
                }
                return this;
            }

            /**
             * Specified time of day for action to take place.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param timeOfDay
             *     Time of day for action
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder timeOfDay(Collection<Time> timeOfDay) {
                this.timeOfDay = new ArrayList<>(timeOfDay);
                return this;
            }

            /**
             * An approximate time period during the day, potentially linked to an event of daily living that indicates when the 
             * action should occur.
             * 
             * <p>Adds new element(s) to the existing list.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param when
             *     Code for time period of occurrence
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder when(EventTiming... when) {
                for (EventTiming value : when) {
                    this.when.add(value);
                }
                return this;
            }

            /**
             * An approximate time period during the day, potentially linked to an event of daily living that indicates when the 
             * action should occur.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection.
             * If any of the elements are null, calling {@link #build()} will fail.
             * 
             * @param when
             *     Code for time period of occurrence
             * 
             * @return
             *     A reference to this Builder instance
             * 
             * @throws NullPointerException
             *     If the passed collection is null
             */
            public Builder when(Collection<EventTiming> when) {
                this.when = new ArrayList<>(when);
                return this;
            }

            /**
             * The number of minutes from the event. If the event code does not indicate whether the minutes is before or after the 
             * event, then the offset is assumed to be after the event.
             * 
             * @param offset
             *     Minutes from event (before or after)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder offset(UnsignedInt offset) {
                this.offset = offset;
                return this;
            }

            /**
             * Build the {@link Repeat}
             * 
             * @return
             *     An immutable object of type {@link Repeat}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Repeat per the base specification
             */
            @Override
            public Repeat build() {
                Repeat repeat = new Repeat(this);
                if (validating) {
                    validate(repeat);
                }
                return repeat;
            }

            protected void validate(Repeat repeat) {
                super.validate(repeat);
                ValidationSupport.choiceElement(repeat.bounds, "bounds", Duration.class, Range.class, Period.class);
                ValidationSupport.checkList(repeat.dayOfWeek, "dayOfWeek", DayOfWeek.class);
                ValidationSupport.checkList(repeat.timeOfDay, "timeOfDay", Time.class);
                ValidationSupport.checkList(repeat.when, "when", EventTiming.class);
                ValidationSupport.requireValueOrChildren(repeat);
            }

            protected Builder from(Repeat repeat) {
                super.from(repeat);
                bounds = repeat.bounds;
                count = repeat.count;
                countMax = repeat.countMax;
                duration = repeat.duration;
                durationMax = repeat.durationMax;
                durationUnit = repeat.durationUnit;
                frequency = repeat.frequency;
                frequencyMax = repeat.frequencyMax;
                period = repeat.period;
                periodMax = repeat.periodMax;
                periodUnit = repeat.periodUnit;
                dayOfWeek.addAll(repeat.dayOfWeek);
                timeOfDay.addAll(repeat.timeOfDay);
                when.addAll(repeat.when);
                offset = repeat.offset;
                return this;
            }
        }
    }
}
