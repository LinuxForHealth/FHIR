/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type.code;

import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.String;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

@Generated("com.ibm.fhir.tools.CodeGenerator")
public class EventTiming extends Code {
    /**
     * Morning
     */
    public static final EventTiming MORN = EventTiming.builder().value(ValueSet.MORN).build();

    /**
     * Early Morning
     */
    public static final EventTiming MORN_EARLY = EventTiming.builder().value(ValueSet.MORN_EARLY).build();

    /**
     * Late Morning
     */
    public static final EventTiming MORN_LATE = EventTiming.builder().value(ValueSet.MORN_LATE).build();

    /**
     * Noon
     */
    public static final EventTiming NOON = EventTiming.builder().value(ValueSet.NOON).build();

    /**
     * Afternoon
     */
    public static final EventTiming AFT = EventTiming.builder().value(ValueSet.AFT).build();

    /**
     * Early Afternoon
     */
    public static final EventTiming AFT_EARLY = EventTiming.builder().value(ValueSet.AFT_EARLY).build();

    /**
     * Late Afternoon
     */
    public static final EventTiming AFT_LATE = EventTiming.builder().value(ValueSet.AFT_LATE).build();

    /**
     * Evening
     */
    public static final EventTiming EVE = EventTiming.builder().value(ValueSet.EVE).build();

    /**
     * Early Evening
     */
    public static final EventTiming EVE_EARLY = EventTiming.builder().value(ValueSet.EVE_EARLY).build();

    /**
     * Late Evening
     */
    public static final EventTiming EVE_LATE = EventTiming.builder().value(ValueSet.EVE_LATE).build();

    /**
     * Night
     */
    public static final EventTiming NIGHT = EventTiming.builder().value(ValueSet.NIGHT).build();

    /**
     * After Sleep
     */
    public static final EventTiming PHS = EventTiming.builder().value(ValueSet.PHS).build();

    public static final EventTiming HS = EventTiming.builder().value(ValueSet.HS).build();

    public static final EventTiming WAKE = EventTiming.builder().value(ValueSet.WAKE).build();

    public static final EventTiming C = EventTiming.builder().value(ValueSet.C).build();

    public static final EventTiming CM = EventTiming.builder().value(ValueSet.CM).build();

    public static final EventTiming CD = EventTiming.builder().value(ValueSet.CD).build();

    public static final EventTiming CV = EventTiming.builder().value(ValueSet.CV).build();

    public static final EventTiming AC = EventTiming.builder().value(ValueSet.AC).build();

    public static final EventTiming ACM = EventTiming.builder().value(ValueSet.ACM).build();

    public static final EventTiming ACD = EventTiming.builder().value(ValueSet.ACD).build();

    public static final EventTiming ACV = EventTiming.builder().value(ValueSet.ACV).build();

    public static final EventTiming PC = EventTiming.builder().value(ValueSet.PC).build();

    public static final EventTiming PCM = EventTiming.builder().value(ValueSet.PCM).build();

    public static final EventTiming PCD = EventTiming.builder().value(ValueSet.PCD).build();

    public static final EventTiming PCV = EventTiming.builder().value(ValueSet.PCV).build();

    private volatile int hashCode;

    private EventTiming(Builder builder) {
        super(builder);
    }

    public static EventTiming of(ValueSet value) {
        switch (value) {
        case MORN:
            return MORN;
        case MORN_EARLY:
            return MORN_EARLY;
        case MORN_LATE:
            return MORN_LATE;
        case NOON:
            return NOON;
        case AFT:
            return AFT;
        case AFT_EARLY:
            return AFT_EARLY;
        case AFT_LATE:
            return AFT_LATE;
        case EVE:
            return EVE;
        case EVE_EARLY:
            return EVE_EARLY;
        case EVE_LATE:
            return EVE_LATE;
        case NIGHT:
            return NIGHT;
        case PHS:
            return PHS;
        case HS:
            return HS;
        case WAKE:
            return WAKE;
        case C:
            return C;
        case CM:
            return CM;
        case CD:
            return CD;
        case CV:
            return CV;
        case AC:
            return AC;
        case ACM:
            return ACM;
        case ACD:
            return ACD;
        case ACV:
            return ACV;
        case PC:
            return PC;
        case PCM:
            return PCM;
        case PCD:
            return PCD;
        case PCV:
            return PCV;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    public static EventTiming of(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    public static String string(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    public static Code code(java.lang.String value) {
        return of(ValueSet.from(value));
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
        builder.id(id);
        builder.extension(extension);
        builder.value(value);
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
            return (value != null) ? (Builder) super.value(ValueSet.from(value).value()) : this;
        }

        public Builder value(ValueSet value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
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
