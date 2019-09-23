/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

public class EventTiming extends Code {
    /**
     * Morning
     */
    public static final EventTiming MORN = EventTiming.of(ValueSet.MORN);

    /**
     * Early Morning
     */
    public static final EventTiming MORN_EARLY = EventTiming.of(ValueSet.MORN_EARLY);

    /**
     * Late Morning
     */
    public static final EventTiming MORN_LATE = EventTiming.of(ValueSet.MORN_LATE);

    /**
     * Noon
     */
    public static final EventTiming NOON = EventTiming.of(ValueSet.NOON);

    /**
     * Afternoon
     */
    public static final EventTiming AFT = EventTiming.of(ValueSet.AFT);

    /**
     * Early Afternoon
     */
    public static final EventTiming AFT_EARLY = EventTiming.of(ValueSet.AFT_EARLY);

    /**
     * Late Afternoon
     */
    public static final EventTiming AFT_LATE = EventTiming.of(ValueSet.AFT_LATE);

    /**
     * Evening
     */
    public static final EventTiming EVE = EventTiming.of(ValueSet.EVE);

    /**
     * Early Evening
     */
    public static final EventTiming EVE_EARLY = EventTiming.of(ValueSet.EVE_EARLY);

    /**
     * Late Evening
     */
    public static final EventTiming EVE_LATE = EventTiming.of(ValueSet.EVE_LATE);

    /**
     * Night
     */
    public static final EventTiming NIGHT = EventTiming.of(ValueSet.NIGHT);

    /**
     * After Sleep
     */
    public static final EventTiming PHS = EventTiming.of(ValueSet.PHS);

    public static final EventTiming HS = EventTiming.of(ValueSet.HS);

    public static final EventTiming WAKE = EventTiming.of(ValueSet.WAKE);

    public static final EventTiming C = EventTiming.of(ValueSet.C);

    public static final EventTiming CM = EventTiming.of(ValueSet.CM);

    public static final EventTiming CD = EventTiming.of(ValueSet.CD);

    public static final EventTiming CV = EventTiming.of(ValueSet.CV);

    public static final EventTiming AC = EventTiming.of(ValueSet.AC);

    public static final EventTiming ACM = EventTiming.of(ValueSet.ACM);

    public static final EventTiming ACD = EventTiming.of(ValueSet.ACD);

    public static final EventTiming ACV = EventTiming.of(ValueSet.ACV);

    public static final EventTiming PC = EventTiming.of(ValueSet.PC);

    public static final EventTiming PCM = EventTiming.of(ValueSet.PCM);

    public static final EventTiming PCD = EventTiming.of(ValueSet.PCD);

    public static final EventTiming PCV = EventTiming.of(ValueSet.PCV);

    private volatile int hashCode;

    private EventTiming(Builder builder) {
        super(builder);
    }

    public static EventTiming of(java.lang.String value) {
        return EventTiming.builder().value(value).build();
    }

    public static EventTiming of(ValueSet value) {
        return EventTiming.builder().value(value).build();
    }

    public static String string(java.lang.String value) {
        return EventTiming.builder().value(value).build();
    }

    public static Code code(java.lang.String value) {
        return EventTiming.builder().value(value).build();
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
        EventTiming other = (EventTiming) obj;
        return Objects.equals(id, other.id) && Objects.equals(extension, other.extension) && Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, extension, value);
            hashCode = result;
        }
        return result;
    }

    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.id = id;
        builder.extension.addAll(extension);
        builder.value = value;
        return builder;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Code.Builder {
        private Builder() {
            super();
        }

        @Override
        public Builder id(java.lang.String id) {
            return (Builder) super.id(id);
        }

        @Override
        public Builder extension(Extension... extension) {
            return (Builder) super.extension(extension);
        }

        @Override
        public Builder extension(Collection<Extension> extension) {
            return (Builder) super.extension(extension);
        }

        @Override
        public Builder value(java.lang.String value) {
            return (Builder) super.value(ValueSet.from(value).value());
        }

        public Builder value(ValueSet value) {
            return (Builder) super.value(value.value());
        }

        @Override
        public EventTiming build() {
            return new EventTiming(this);
        }
    }

    public enum ValueSet {
        /**
         * Morning
         */
        MORN("MORN"),

        /**
         * Early Morning
         */
        MORN_EARLY("MORN.early"),

        /**
         * Late Morning
         */
        MORN_LATE("MORN.late"),

        /**
         * Noon
         */
        NOON("NOON"),

        /**
         * Afternoon
         */
        AFT("AFT"),

        /**
         * Early Afternoon
         */
        AFT_EARLY("AFT.early"),

        /**
         * Late Afternoon
         */
        AFT_LATE("AFT.late"),

        /**
         * Evening
         */
        EVE("EVE"),

        /**
         * Early Evening
         */
        EVE_EARLY("EVE.early"),

        /**
         * Late Evening
         */
        EVE_LATE("EVE.late"),

        /**
         * Night
         */
        NIGHT("NIGHT"),

        /**
         * After Sleep
         */
        PHS("PHS"),

        HS("HS"),

        WAKE("WAKE"),

        C("C"),

        CM("CM"),

        CD("CD"),

        CV("CV"),

        AC("AC"),

        ACM("ACM"),

        ACD("ACD"),

        ACV("ACV"),

        PC("PC"),

        PCM("PCM"),

        PCD("PCD"),

        PCV("PCV");

        private final java.lang.String value;

        ValueSet(java.lang.String value) {
            this.value = value;
        }

        public java.lang.String value() {
            return value;
        }

        public static ValueSet from(java.lang.String value) {
            for (ValueSet c : ValueSet.values()) {
                if (c.value.equals(value)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(value);
        }
    }
}
