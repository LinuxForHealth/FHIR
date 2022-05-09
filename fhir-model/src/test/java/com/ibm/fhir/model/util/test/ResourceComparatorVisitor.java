/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.util.test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.ibm.fhir.model.visitor.PathAwareVisitor;

/**
 * Simple visitor to flatten the structure of a resource and compare
 * it with another instance
 */
public class ResourceComparatorVisitor extends PathAwareVisitor {
    public boolean compare = false;

    // First set of values we collect
    public Map<String, Comparer> values = new HashMap<>();

    public static interface Comparer {
        public boolean compare(Comparer other);
    }

    public static class ByteArrayComparer implements Comparer {
        final byte[] value;
        public ByteArrayComparer(byte[] value) {
            this.value = value;
        }

        @Override
        public boolean compare(Comparer other) {
            if (other instanceof ByteArrayComparer) {
                return Arrays.equals(value, ((ByteArrayComparer)other).value);
            }
            else {
                throw new IllegalArgumentException("Type mismatch");
            }
        }
        @Override
        public String toString() {
            return Base64.getEncoder().encodeToString(value);
        }
    }

    public static class IntComparer implements Comparer {
        final Integer value;
        public IntComparer(Integer value) {
            this.value = value;
        }

        @Override
        public boolean compare(Comparer other) {
            if (other instanceof IntComparer) {
                return Objects.equals(value, ((IntComparer)other).value);
            }
            else {
                throw new IllegalArgumentException("Type mismatch");
            }
        }
        @Override
        public String toString() {
            return value == null ? "null" : value.toString();
        }
    }

    public static class StringComparer implements Comparer {
        final String value;
        public StringComparer(String value) {
            this.value = value;
        }

        @Override
        public boolean compare(Comparer other) {
            if (other instanceof StringComparer) {
                return Objects.equals(value, ((StringComparer)other).value);
            }
            else {
                throw new IllegalArgumentException("Type mismatch");
            }
        }
        @Override
        public String toString() {
            return value == null ? "null" : value.toString();
        }
    }


    public static class BigDecimalComparer implements Comparer {
        final BigDecimal value;
        public BigDecimalComparer(BigDecimal value) {
            this.value = value;
        }

        @Override
        public boolean compare(Comparer other) {
            if (other instanceof BigDecimalComparer) {
                return Objects.equals(value, ((BigDecimalComparer)other).value);
            }
            else {
                throw new IllegalArgumentException("Type mismatch");
            }
        }
        @Override
        public String toString() {
            return value == null ? "null" : value.toString();
        }
    }

    public static class BooleanComparer implements Comparer {
        final Boolean value;
        public BooleanComparer(Boolean value) {
            this.value = value;
        }

        @Override
        public boolean compare(Comparer other) {
            if (other instanceof BooleanComparer) {
                return Objects.equals(value, ((BooleanComparer)other).value);
            }
            else {
                throw new IllegalArgumentException("Type mismatch");
            }
        }
        @Override
        public String toString() {
            return value == null ? "null" : value.toString();
        }
    }

    public static class LocalDateComparer implements Comparer {
        final LocalDate value;
        public LocalDateComparer(LocalDate value) {
            this.value = value;
        }

        @Override
        public boolean compare(Comparer other) {
            if (other instanceof LocalDateComparer) {
                return Objects.equals(value, ((LocalDateComparer)other).value);
            }
            else {
                throw new IllegalArgumentException("Type mismatch");
            }
        }
        @Override
        public String toString() {
            return value == null ? "null" : value.toString();
        }
    }

    public static class LocalTimeComparer implements Comparer {
        final LocalTime value;
        public LocalTimeComparer(LocalTime value) {
            this.value = value;
        }

        @Override
        public boolean compare(Comparer other) {
            if (other instanceof LocalTimeComparer) {
                return Objects.equals(value, ((LocalTimeComparer)other).value);
            }
            else {
                throw new IllegalArgumentException("Type mismatch");
            }
        }
        @Override
        public String toString() {
            return value == null ? "null" : value.toString();
        }
    }

    public static class YearComparer implements Comparer {
        final Year value;
        public YearComparer(Year value) {
            this.value = value;
        }

        @Override
        public boolean compare(Comparer other) {
            if (other instanceof YearComparer) {
                return Objects.equals(value, ((YearComparer)other).value);
            }
            else {
                throw new IllegalArgumentException("Type mismatch");
            }
        }
        @Override
        public String toString() {
            return value == null ? "null" : value.toString();
        }
    }

    public static class YearMonthComparer implements Comparer {
        final YearMonth value;
        public YearMonthComparer(YearMonth value) {
            this.value = value;
        }

        @Override
        public boolean compare(Comparer other) {
            if (other instanceof YearMonthComparer) {
                return Objects.equals(value, ((YearMonthComparer)other).value);
            }
            else {
                throw new IllegalArgumentException("Type mismatch");
            }
        }
        @Override
        public String toString() {
            return value == null ? "null" : value.toString();
        }
    }

    public static class ZonedDateTimeComparer implements Comparer {
        final ZonedDateTime value;
        public ZonedDateTimeComparer(ZonedDateTime value) {
            this.value = value;
        }

        @Override
        public boolean compare(Comparer other) {
            if (other instanceof ZonedDateTimeComparer) {
                return Objects.equals(value, ((ZonedDateTimeComparer)other).value);
            }
            else {
                throw new IllegalArgumentException("Type mismatch");
            }
        }
        @Override
        public String toString() {
            return value == null ? "null" : value.toString();
        }
    }

    /**
     * Perform the bi-directional comparison between the original other values
     */
    public static boolean compare(Map<String, Comparer> originals, Map<String, Comparer> others) {
        boolean result = true;

        for (Map.Entry<String, Comparer> entry: originals.entrySet()) {

            Comparer other = others.get(entry.getKey());
            if (other != null) {
                if (!entry.getValue().compare(other)) {
                    System.out.println("'" + entry.getKey() + "' mismatch: " + entry.getValue().toString() + " != " + other.toString());
                    result = false;
                }
            }
            else {
                System.out.println("original key '" + entry.getKey() + "' not found in others");
                result = false;
            }
        }

        // Now check the other direction for anything which exists in others, but not in originals
        for (Map.Entry<String, Comparer> entry: others.entrySet()) {

            // We only need to check for existence. If found, we'll have already compared above
            Comparer other = others.get(entry.getKey());
            if (other == null) {
                System.out.println("other key '" + entry.getKey() + "' not found in originals");
                result = false;
            }
        }


        return result;
    }

    public Map<String, Comparer> getValues() {
        return this.values;
    }

    @Override
    public void doVisit(java.lang.String elementName, byte[] value) {
        this.values.put(getPath(), new ByteArrayComparer(value));
    }

    @Override
    public void doVisit(java.lang.String elementName, BigDecimal value) {
        this.values.put(getPath(), new BigDecimalComparer(value));
    }

    @Override
    public void doVisit(java.lang.String elementName, java.lang.Boolean value) {
        this.values.put(getPath(), new BooleanComparer(value));
    }

    @Override
    public void doVisit(java.lang.String elementName, java.lang.Integer value) {
        this.values.put(getPath(), new IntComparer(value));
    }

    @Override
    public void doVisit(java.lang.String elementName, LocalDate value) {
        this.values.put(getPath(), new LocalDateComparer(value));
    }

    @Override
    public void doVisit(java.lang.String elementName, LocalTime value) {
        this.values.put(getPath(), new LocalTimeComparer(value));
    }

    @Override
    public void doVisit(java.lang.String elementName, java.lang.String value) {
        this.values.put(getPath(), new StringComparer(value));
    }

    @Override
    public void doVisit(java.lang.String elementName, Year value) {
        this.values.put(getPath(), new YearComparer(value));
    }

    @Override
    public void doVisit(java.lang.String elementName, YearMonth value) {
        this.values.put(getPath(), new YearMonthComparer(value));
    }

    @Override
    public void doVisit(java.lang.String elementName, ZonedDateTime value) {
        this.values.put(getPath(), new ZonedDateTimeComparer(value));
    }
}
