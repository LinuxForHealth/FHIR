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

import javax.annotation.Generated;

import com.ibm.watsonhealth.fhir.model.annotation.Constraint;
import com.ibm.watsonhealth.fhir.model.type.BackboneElement;
import com.ibm.watsonhealth.fhir.model.type.DayOfWeek;
import com.ibm.watsonhealth.fhir.model.type.EventTiming;
import com.ibm.watsonhealth.fhir.model.type.UnitsOfTime;
import com.ibm.watsonhealth.fhir.model.util.ValidationSupport;
import com.ibm.watsonhealth.fhir.model.visitor.Visitor;

/**
 * <p>
 * Specifies an event that may occur multiple times. Timing schedules are used to record when things are planned, 
 * expected or requested to occur. The most common usage is in dosage instructions for medications. They are also used 
 * when planning care of various kinds, and may be used for reporting the schedule to which past regular activities were 
 * carried out.
 * </p>
 */
@Constraint(
    id = "tim-1",
    level = "Rule",
    location = "Timing.repeat",
    description = "if there's a duration, there needs to be duration units",
    expression = "duration.empty() or durationUnit.exists()"
)
@Constraint(
    id = "tim-2",
    level = "Rule",
    location = "Timing.repeat",
    description = "if there's a period, there needs to be period units",
    expression = "period.empty() or periodUnit.exists()"
)
@Constraint(
    id = "tim-4",
    level = "Rule",
    location = "Timing.repeat",
    description = "duration SHALL be a non-negative value",
    expression = "duration.exists() implies duration >= 0"
)
@Constraint(
    id = "tim-5",
    level = "Rule",
    location = "Timing.repeat",
    description = "period SHALL be a non-negative value",
    expression = "period.exists() implies period >= 0"
)
@Constraint(
    id = "tim-6",
    level = "Rule",
    location = "Timing.repeat",
    description = "If there's a periodMax, there must be a period",
    expression = "periodMax.empty() or period.exists()"
)
@Constraint(
    id = "tim-7",
    level = "Rule",
    location = "Timing.repeat",
    description = "If there's a durationMax, there must be a duration",
    expression = "durationMax.empty() or duration.exists()"
)
@Constraint(
    id = "tim-8",
    level = "Rule",
    location = "Timing.repeat",
    description = "If there's a countMax, there must be a count",
    expression = "countMax.empty() or count.exists()"
)
@Constraint(
    id = "tim-9",
    level = "Rule",
    location = "Timing.repeat",
    description = "If there's an offset, there must be a when (and not C, CM, CD, CV)",
    expression = "offset.empty() or (when.exists() and ((when in ('C' | 'CM' | 'CD' | 'CV')).not()))"
)
@Constraint(
    id = "tim-10",
    level = "Rule",
    location = "Timing.repeat",
    description = "If there's a timeOfDay, there cannot be a when, or vice versa",
    expression = "timeOfDay.empty() or when.empty()"
)
@Generated("com.ibm.watsonhealth.fhir.tools.CodeGenerator")
public class Timing extends BackboneElement {
    private final List<DateTime> event;
    private final Repeat repeat;
    private final CodeableConcept code;

    private Timing(Builder builder) {
        super(builder);
        event = Collections.unmodifiableList(builder.event);
        repeat = builder.repeat;
        code = builder.code;
    }

    /**
     * <p>
     * Identifies specific times when the event occurs.
     * </p>
     * 
     * @return
     *     A list containing immutable objects of type {@link DateTime}.
     */
    public List<DateTime> getEvent() {
        return event;
    }

    /**
     * <p>
     * A set of rules that describe when the event is scheduled.
     * </p>
     * 
     * @return
     *     An immutable object of type {@link Repeat}.
     */
    public Repeat getRepeat() {
        return repeat;
    }

    /**
     * <p>
     * A code for the timing schedule (or just text in code.text). Some codes such as BID are ubiquitous, but many 
     * institutions define their own additional codes. If a code is provided, the code is understood to be a complete 
     * statement of whatever is specified in the structured timing data, and either the code or the data may be used to 
     * interpret the Timing, with the exception that .repeat.bounds still applies over the code (and is not contained in the 
     * code).
     * </p>
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept}.
     */
    public CodeableConcept getCode() {
        return code;
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
                accept(event, "event", visitor, DateTime.class);
                accept(repeat, "repeat", visitor);
                accept(code, "code", visitor);
            }
            visitor.visitEnd(elementName, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.id = id;
        builder.extension.addAll(extension);
        builder.modifierExtension.addAll(modifierExtension);
        builder.event.addAll(event);
        builder.repeat = repeat;
        builder.code = code;
        return builder;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends BackboneElement.Builder {
        // optional
        private List<DateTime> event = new ArrayList<>();
        private Repeat repeat;
        private CodeableConcept code;

        private Builder() {
            super();
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
         * Identifies specific times when the event occurs.
         * </p>
         * 
         * @param event
         *     When the event occurs
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder event(DateTime... event) {
            for (DateTime value : event) {
                this.event.add(value);
            }
            return this;
        }

        /**
         * <p>
         * Identifies specific times when the event occurs.
         * </p>
         * 
         * @param event
         *     When the event occurs
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder event(Collection<DateTime> event) {
            this.event.addAll(event);
            return this;
        }

        /**
         * <p>
         * A set of rules that describe when the event is scheduled.
         * </p>
         * 
         * @param repeat
         *     When the event is to occur
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder repeat(Repeat repeat) {
            this.repeat = repeat;
            return this;
        }

        /**
         * <p>
         * A code for the timing schedule (or just text in code.text). Some codes such as BID are ubiquitous, but many 
         * institutions define their own additional codes. If a code is provided, the code is understood to be a complete 
         * statement of whatever is specified in the structured timing data, and either the code or the data may be used to 
         * interpret the Timing, with the exception that .repeat.bounds still applies over the code (and is not contained in the 
         * code).
         * </p>
         * 
         * @param code
         *     BID | TID | QID | AM | PM | QD | QOD | +
         * 
         * @return
         *     A reference to this Builder instance.
         */
        public Builder code(CodeableConcept code) {
            this.code = code;
            return this;
        }

        @Override
        public Timing build() {
            return new Timing(this);
        }
    }

    /**
     * <p>
     * A set of rules that describe when the event is scheduled.
     * </p>
     */
    public static class Repeat extends BackboneElement {
        private final Element bounds;
        private final PositiveInt count;
        private final PositiveInt countMax;
        private final Decimal duration;
        private final Decimal durationMax;
        private final UnitsOfTime durationUnit;
        private final PositiveInt frequency;
        private final PositiveInt frequencyMax;
        private final Decimal period;
        private final Decimal periodMax;
        private final UnitsOfTime periodUnit;
        private final List<DayOfWeek> dayOfWeek;
        private final List<Time> timeOfDay;
        private final List<EventTiming> when;
        private final UnsignedInt offset;

        private Repeat(Builder builder) {
            super(builder);
            bounds = ValidationSupport.choiceElement(builder.bounds, "bounds", Duration.class, Range.class, Period.class);
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
         * <p>
         * Either a duration for the length of the timing schedule, a range of possible length, or outer bounds for start and/or 
         * end limits of the timing schedule.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Element}.
         */
        public Element getBounds() {
            return bounds;
        }

        /**
         * <p>
         * A total count of the desired number of repetitions across the duration of the entire timing specification. If countMax 
         * is present, this element indicates the lower bound of the allowed range of count values.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link PositiveInt}.
         */
        public PositiveInt getCount() {
            return count;
        }

        /**
         * <p>
         * If present, indicates that the count is a range - so to perform the action between [count] and [countMax] times.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link PositiveInt}.
         */
        public PositiveInt getCountMax() {
            return countMax;
        }

        /**
         * <p>
         * How long this thing happens for when it happens. If durationMax is present, this element indicates the lower bound of 
         * the allowed range of the duration.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Decimal}.
         */
        public Decimal getDuration() {
            return duration;
        }

        /**
         * <p>
         * If present, indicates that the duration is a range - so to perform the action between [duration] and [durationMax] 
         * time length.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Decimal}.
         */
        public Decimal getDurationMax() {
            return durationMax;
        }

        /**
         * <p>
         * The units of time for the duration, in UCUM units.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link UnitsOfTime}.
         */
        public UnitsOfTime getDurationUnit() {
            return durationUnit;
        }

        /**
         * <p>
         * The number of times to repeat the action within the specified period. If frequencyMax is present, this element 
         * indicates the lower bound of the allowed range of the frequency.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link PositiveInt}.
         */
        public PositiveInt getFrequency() {
            return frequency;
        }

        /**
         * <p>
         * If present, indicates that the frequency is a range - so to repeat between [frequency] and [frequencyMax] times within 
         * the period or period range.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link PositiveInt}.
         */
        public PositiveInt getFrequencyMax() {
            return frequencyMax;
        }

        /**
         * <p>
         * Indicates the duration of time over which repetitions are to occur; e.g. to express "3 times per day", 3 would be the 
         * frequency and "1 day" would be the period. If periodMax is present, this element indicates the lower bound of the 
         * allowed range of the period length.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Decimal}.
         */
        public Decimal getPeriod() {
            return period;
        }

        /**
         * <p>
         * If present, indicates that the period is a range from [period] to [periodMax], allowing expressing concepts such as 
         * "do this once every 3-5 days.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link Decimal}.
         */
        public Decimal getPeriodMax() {
            return periodMax;
        }

        /**
         * <p>
         * The units of time for the period in UCUM units.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link UnitsOfTime}.
         */
        public UnitsOfTime getPeriodUnit() {
            return periodUnit;
        }

        /**
         * <p>
         * If one or more days of week is provided, then the action happens only on the specified day(s).
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link DayOfWeek}.
         */
        public List<DayOfWeek> getDayOfWeek() {
            return dayOfWeek;
        }

        /**
         * <p>
         * Specified time of day for action to take place.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link Time}.
         */
        public List<Time> getTimeOfDay() {
            return timeOfDay;
        }

        /**
         * <p>
         * An approximate time period during the day, potentially linked to an event of daily living that indicates when the 
         * action should occur.
         * </p>
         * 
         * @return
         *     A list containing immutable objects of type {@link EventTiming}.
         */
        public List<EventTiming> getWhen() {
            return when;
        }

        /**
         * <p>
         * The number of minutes from the event. If the event code does not indicate whether the minutes is before or after the 
         * event, then the offset is assumed to be after the event.
         * </p>
         * 
         * @return
         *     An immutable object of type {@link UnsignedInt}.
         */
        public UnsignedInt getOffset() {
            return offset;
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
                    accept(bounds, "bounds", visitor, true);
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
                visitor.visitEnd(elementName, this);
                visitor.postVisit(this);
            }
        }

        @Override
        public Builder toBuilder() {
            return Builder.from(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends BackboneElement.Builder {
            // optional
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
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
             * extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions 
             * SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of 
             * modifierExtension itself).
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
             * Either a duration for the length of the timing schedule, a range of possible length, or outer bounds for start and/or 
             * end limits of the timing schedule.
             * </p>
             * 
             * @param bounds
             *     Length/Range of lengths, or (Start and/or end) limits
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder bounds(Element bounds) {
                this.bounds = bounds;
                return this;
            }

            /**
             * <p>
             * A total count of the desired number of repetitions across the duration of the entire timing specification. If countMax 
             * is present, this element indicates the lower bound of the allowed range of count values.
             * </p>
             * 
             * @param count
             *     Number of times to repeat
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder count(PositiveInt count) {
                this.count = count;
                return this;
            }

            /**
             * <p>
             * If present, indicates that the count is a range - so to perform the action between [count] and [countMax] times.
             * </p>
             * 
             * @param countMax
             *     Maximum number of times to repeat
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder countMax(PositiveInt countMax) {
                this.countMax = countMax;
                return this;
            }

            /**
             * <p>
             * How long this thing happens for when it happens. If durationMax is present, this element indicates the lower bound of 
             * the allowed range of the duration.
             * </p>
             * 
             * @param duration
             *     How long when it happens
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder duration(Decimal duration) {
                this.duration = duration;
                return this;
            }

            /**
             * <p>
             * If present, indicates that the duration is a range - so to perform the action between [duration] and [durationMax] 
             * time length.
             * </p>
             * 
             * @param durationMax
             *     How long when it happens (Max)
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder durationMax(Decimal durationMax) {
                this.durationMax = durationMax;
                return this;
            }

            /**
             * <p>
             * The units of time for the duration, in UCUM units.
             * </p>
             * 
             * @param durationUnit
             *     s | min | h | d | wk | mo | a - unit of time (UCUM)
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder durationUnit(UnitsOfTime durationUnit) {
                this.durationUnit = durationUnit;
                return this;
            }

            /**
             * <p>
             * The number of times to repeat the action within the specified period. If frequencyMax is present, this element 
             * indicates the lower bound of the allowed range of the frequency.
             * </p>
             * 
             * @param frequency
             *     Event occurs frequency times per period
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder frequency(PositiveInt frequency) {
                this.frequency = frequency;
                return this;
            }

            /**
             * <p>
             * If present, indicates that the frequency is a range - so to repeat between [frequency] and [frequencyMax] times within 
             * the period or period range.
             * </p>
             * 
             * @param frequencyMax
             *     Event occurs up to frequencyMax times per period
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder frequencyMax(PositiveInt frequencyMax) {
                this.frequencyMax = frequencyMax;
                return this;
            }

            /**
             * <p>
             * Indicates the duration of time over which repetitions are to occur; e.g. to express "3 times per day", 3 would be the 
             * frequency and "1 day" would be the period. If periodMax is present, this element indicates the lower bound of the 
             * allowed range of the period length.
             * </p>
             * 
             * @param period
             *     Event occurs frequency times per period
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder period(Decimal period) {
                this.period = period;
                return this;
            }

            /**
             * <p>
             * If present, indicates that the period is a range from [period] to [periodMax], allowing expressing concepts such as 
             * "do this once every 3-5 days.
             * </p>
             * 
             * @param periodMax
             *     Upper limit of period (3-4 hours)
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder periodMax(Decimal periodMax) {
                this.periodMax = periodMax;
                return this;
            }

            /**
             * <p>
             * The units of time for the period in UCUM units.
             * </p>
             * 
             * @param periodUnit
             *     s | min | h | d | wk | mo | a - unit of time (UCUM)
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder periodUnit(UnitsOfTime periodUnit) {
                this.periodUnit = periodUnit;
                return this;
            }

            /**
             * <p>
             * If one or more days of week is provided, then the action happens only on the specified day(s).
             * </p>
             * 
             * @param dayOfWeek
             *     mon | tue | wed | thu | fri | sat | sun
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder dayOfWeek(DayOfWeek... dayOfWeek) {
                for (DayOfWeek value : dayOfWeek) {
                    this.dayOfWeek.add(value);
                }
                return this;
            }

            /**
             * <p>
             * If one or more days of week is provided, then the action happens only on the specified day(s).
             * </p>
             * 
             * @param dayOfWeek
             *     mon | tue | wed | thu | fri | sat | sun
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder dayOfWeek(Collection<DayOfWeek> dayOfWeek) {
                this.dayOfWeek.addAll(dayOfWeek);
                return this;
            }

            /**
             * <p>
             * Specified time of day for action to take place.
             * </p>
             * 
             * @param timeOfDay
             *     Time of day for action
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder timeOfDay(Time... timeOfDay) {
                for (Time value : timeOfDay) {
                    this.timeOfDay.add(value);
                }
                return this;
            }

            /**
             * <p>
             * Specified time of day for action to take place.
             * </p>
             * 
             * @param timeOfDay
             *     Time of day for action
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder timeOfDay(Collection<Time> timeOfDay) {
                this.timeOfDay.addAll(timeOfDay);
                return this;
            }

            /**
             * <p>
             * An approximate time period during the day, potentially linked to an event of daily living that indicates when the 
             * action should occur.
             * </p>
             * 
             * @param when
             *     Code for time period of occurrence
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder when(EventTiming... when) {
                for (EventTiming value : when) {
                    this.when.add(value);
                }
                return this;
            }

            /**
             * <p>
             * An approximate time period during the day, potentially linked to an event of daily living that indicates when the 
             * action should occur.
             * </p>
             * 
             * @param when
             *     Code for time period of occurrence
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder when(Collection<EventTiming> when) {
                this.when.addAll(when);
                return this;
            }

            /**
             * <p>
             * The number of minutes from the event. If the event code does not indicate whether the minutes is before or after the 
             * event, then the offset is assumed to be after the event.
             * </p>
             * 
             * @param offset
             *     Minutes from event (before or after)
             * 
             * @return
             *     A reference to this Builder instance.
             */
            public Builder offset(UnsignedInt offset) {
                this.offset = offset;
                return this;
            }

            @Override
            public Repeat build() {
                return new Repeat(this);
            }

            private static Builder from(Repeat repeat) {
                Builder builder = new Builder();
                builder.bounds = repeat.bounds;
                builder.count = repeat.count;
                builder.countMax = repeat.countMax;
                builder.duration = repeat.duration;
                builder.durationMax = repeat.durationMax;
                builder.durationUnit = repeat.durationUnit;
                builder.frequency = repeat.frequency;
                builder.frequencyMax = repeat.frequencyMax;
                builder.period = repeat.period;
                builder.periodMax = repeat.periodMax;
                builder.periodUnit = repeat.periodUnit;
                builder.dayOfWeek.addAll(repeat.dayOfWeek);
                builder.timeOfDay.addAll(repeat.timeOfDay);
                builder.when.addAll(repeat.when);
                builder.offset = repeat.offset;
                return builder;
            }
        }
    }
}
