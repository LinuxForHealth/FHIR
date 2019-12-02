/*
 * (C) Copyright IBM Corp. 2019
 * 
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.util;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAmount;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.model.util.ModelSupport.ElementInfo;
import com.ibm.fhir.path.ClassInfo;
import com.ibm.fhir.path.ClassInfoElement;
import com.ibm.fhir.path.FHIRPathBooleanValue;
import com.ibm.fhir.path.FHIRPathDecimalValue;
import com.ibm.fhir.path.FHIRPathElementNode;
import com.ibm.fhir.path.FHIRPathIntegerValue;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.FHIRPathNumberValue;
import com.ibm.fhir.path.FHIRPathQuantityNode;
import com.ibm.fhir.path.FHIRPathQuantityValue;
import com.ibm.fhir.path.FHIRPathResourceNode;
import com.ibm.fhir.path.FHIRPathStringValue;
import com.ibm.fhir.path.FHIRPathSystemValue;
import com.ibm.fhir.path.FHIRPathTemporalValue;
import com.ibm.fhir.path.FHIRPathType;
import com.ibm.fhir.path.SimpleTypeInfo;
import com.ibm.fhir.path.TupleTypeInfo;
import com.ibm.fhir.path.TupleTypeInfoElement;

public final class FHIRPathUtil {
    private static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList(
        "$index", 
        "$this", 
        "$total", 
        "and", 
        "as", 
        "contains", 
        "day", 
        "days", 
        "div", 
        "false", 
        "hour", 
        "hours", 
        "implies", 
        "in", 
        "is", 
        "millisecond", 
        "milliseconds", 
        "minute", 
        "minutes", 
        "mod", 
        "month", 
        "months", 
        "or", 
        "seconds", 
        "true", 
        "week", 
        "weeks", 
        "xor", 
        "year", 
        "years", 
        "second"
    ));
    public static final Set<String> STRING_TRUE_VALUES = new HashSet<>(Arrays.asList("true", "t", "yes", "y", "1", "1.0"));
    public static final Set<String> STRING_FALSE_VALUES = new HashSet<>(Arrays.asList("false", "f", "no", "n", "0", "0.0"));
    public static final Integer INTEGER_TRUE = 1;
    public static final Integer INTEGER_FALSE = 0;
    public static final BigDecimal DECIMAL_TRUE = new BigDecimal("1.0");
    public static final BigDecimal DECIMAL_FALSE = new BigDecimal("0.0");
    private static final Map<FHIRPathType, Set<FHIRPathType>> TYPE_COMPATIBILITY_MAP = new HashMap<>();
    static {
        TYPE_COMPATIBILITY_MAP.put(FHIRPathType.SYSTEM_BOOLEAN, new HashSet<>(Arrays.asList(FHIRPathType.SYSTEM_BOOLEAN)));
        TYPE_COMPATIBILITY_MAP.put(FHIRPathType.SYSTEM_INTEGER, new HashSet<>(Arrays.asList(FHIRPathType.SYSTEM_INTEGER, FHIRPathType.SYSTEM_DECIMAL)));
        TYPE_COMPATIBILITY_MAP.put(FHIRPathType.SYSTEM_DECIMAL, new HashSet<>(Arrays.asList(FHIRPathType.SYSTEM_DECIMAL, FHIRPathType.SYSTEM_INTEGER)));
        TYPE_COMPATIBILITY_MAP.put(FHIRPathType.SYSTEM_QUANTITY, new HashSet<>(Arrays.asList(FHIRPathType.SYSTEM_QUANTITY, FHIRPathType.SYSTEM_DECIMAL, FHIRPathType.SYSTEM_INTEGER)));
        TYPE_COMPATIBILITY_MAP.put(FHIRPathType.SYSTEM_STRING, new HashSet<>(Arrays.asList(FHIRPathType.SYSTEM_STRING)));
        TYPE_COMPATIBILITY_MAP.put(FHIRPathType.SYSTEM_DATE, new HashSet<>(Arrays.asList(FHIRPathType.SYSTEM_DATE, FHIRPathType.SYSTEM_DATE_TIME)));
        TYPE_COMPATIBILITY_MAP.put(FHIRPathType.SYSTEM_DATE_TIME, new HashSet<>(Arrays.asList(FHIRPathType.SYSTEM_DATE_TIME, FHIRPathType.SYSTEM_DATE)));
        TYPE_COMPATIBILITY_MAP.put(FHIRPathType.SYSTEM_TIME, new HashSet<>(Arrays.asList(FHIRPathType.SYSTEM_TIME)));
    }
    
    private FHIRPathUtil() { }

    public static boolean isTypeCompatible(FHIRPathSystemValue leftValue, FHIRPathSystemValue rightValue) {
        return TYPE_COMPATIBILITY_MAP.get(leftValue.type()).contains(rightValue.type());
    }
    
    public static boolean isKeyword(String identifier) {
        return KEYWORDS.contains(identifier);
    }
    
    public static String delimit(String identifier) {
        return String.format("`%s`", identifier);
    }
    
    public static boolean hasResourceNode(Collection<FHIRPathNode> nodes) {
        if (isSingleton(nodes)) {
            FHIRPathNode node = getSingleton(nodes);
            return (node instanceof FHIRPathResourceNode);
        }
        return false;
    }
    
    public static FHIRPathResourceNode getResourceNode(Collection<FHIRPathNode> nodes) {
        return getSingleton(nodes).asResourceNode();
    }
    
    public static boolean hasElementNode(Collection<FHIRPathNode> nodes) {
        if (isSingleton(nodes)) {
            FHIRPathNode node = getSingleton(nodes);
            return (node instanceof FHIRPathElementNode);
        }
        return false;
    }
    
    public static FHIRPathElementNode getElementNode(Collection<FHIRPathNode> nodes) {
        return getSingleton(nodes).asElementNode();
    }
    
    public static boolean hasQuantityNode(Collection<FHIRPathNode> nodes) {
        if (isSingleton(nodes)) {
            FHIRPathNode node = getSingleton(nodes);
            return (node instanceof FHIRPathQuantityNode);
        }
        return false;
    }
    
    public static FHIRPathQuantityNode getQuantityNode(Collection<FHIRPathNode> nodes) {
        return getSingleton(nodes).asElementNode().asQuantityNode();
    }
    
    public static BigDecimal getDecimal(Collection<FHIRPathNode> nodes) {
        return getSystemValue(nodes).asNumberValue().decimal();
    }
    
    public static Integer getInteger(Collection<FHIRPathNode> nodes) {
        return getSystemValue(nodes).asNumberValue().asIntegerValue().integer();
    }
    
    public static String getString(Collection<FHIRPathNode> nodes) {
        return getSystemValue(nodes).asStringValue().string();
    }
    
    public static Boolean getBoolean(Collection<FHIRPathNode> nodes) {
        return getSystemValue(nodes).asBooleanValue()._boolean();
    }
    
    public static TemporalAccessor getDate(Collection<FHIRPathNode> nodes) {
        return getSystemValue(nodes).asTemporalValue().asDateValue().date();
    }
    
    public static TemporalAccessor getDateTime(Collection<FHIRPathNode> nodes) {
        return getSystemValue(nodes).asTemporalValue().asDateTimeValue().dateTime();
    }
    
    public static TemporalAccessor getTime(Collection<FHIRPathNode> nodes) {
        return getSystemValue(nodes).asTemporalValue().asTimeValue().time();
    }
    
    public static boolean hasSystemValue(Collection<FHIRPathNode> nodes) {
        if (isSingleton(nodes)) {
            FHIRPathNode node = getSingleton(nodes);
            return (node instanceof FHIRPathSystemValue) || node.hasValue();
        }
        return false;
    }
    
    public static FHIRPathSystemValue getSystemValue(Collection<FHIRPathNode> nodes) {
        if (!hasSystemValue(nodes)) {
            throw new IllegalArgumentException();
        }
        FHIRPathNode node = getSingleton(nodes);
        if (node instanceof FHIRPathSystemValue) {
            return (FHIRPathSystemValue) node;
        }
        return node.getValue();
    }
    
    public static boolean hasSystemValue(FHIRPathNode node) {
        return node.isSystemValue() || node.hasValue();
    }
    
    public static FHIRPathSystemValue getSystemValue(FHIRPathNode node) {
        if (node.isSystemValue()) {
            return node.asSystemValue();
        }
        return node.getValue();
    }
    
    public static FHIRPathStringValue getStringValue(Collection<FHIRPathNode> nodes) {
        return getSystemValue(nodes).asStringValue();
    }
    
    public static FHIRPathQuantityValue getQuantityValue(Collection<FHIRPathNode> nodes) {
        return getSystemValue(nodes).asQuantityValue();
    }
    
    public static FHIRPathIntegerValue getIntegerValue(Collection<FHIRPathNode> nodes) {
        return getNumberValue(nodes).asIntegerValue();
    }
    
    public static FHIRPathDecimalValue getDecimalValue(Collection<FHIRPathNode> nodes) {
        return getNumberValue(nodes).asDecimalValue();
    }
    
    public static FHIRPathNumberValue getNumberValue(Collection<FHIRPathNode> nodes) {
        return getSystemValue(nodes).asNumberValue();
    }
    
    public static FHIRPathTemporalValue getTemporalValue(Collection<FHIRPathNode> nodes) {
        return getSystemValue(nodes).asTemporalValue();
    }
    
    public static FHIRPathBooleanValue getBooleanValue(Collection<FHIRPathNode> nodes) {
        return getSystemValue(nodes).asBooleanValue();
    }
    
    public static boolean hasBooleanValue(Collection<FHIRPathNode> nodes) {
        return hasSystemValue(nodes) && getSystemValue(nodes).isBooleanValue();
    }
    
    public static boolean hasNumberValue(Collection<FHIRPathNode> nodes) {
        return hasSystemValue(nodes) && getSystemValue(nodes).isNumberValue();
    }
    
    public static boolean hasDecimalValue(Collection<FHIRPathNode> nodes) {
        return hasNumberValue(nodes) && getNumberValue(nodes).isDecimalValue();
    }
    
    public static boolean hasIntegerValue(Collection<FHIRPathNode> nodes) {
        return hasNumberValue(nodes) && getNumberValue(nodes).isIntegerValue();
    }
    
    public static boolean hasTemporalValue(Collection<FHIRPathNode> nodes) {
        return hasSystemValue(nodes) && getSystemValue(nodes).isTemporalValue();
    }
    
    public static boolean hasStringValue(Collection<FHIRPathNode> nodes) {
        return hasSystemValue(nodes) && getSystemValue(nodes).isStringValue();
    }
    
    public static boolean hasQuantityValue(Collection<FHIRPathNode> nodes) {
        return hasSystemValue(nodes) && getSystemValue(nodes).isQuantityValue();
    }
    
    public static boolean evaluatesToBoolean(Collection<FHIRPathNode> nodes) {
        return isTrue(nodes) || isFalse(nodes);
    }
    
    public static boolean isTrue(Collection<FHIRPathNode> nodes) {
        if (hasBooleanValue(nodes)) {
            return getBooleanValue(nodes).isTrue();
        }
        if (hasStringValue(nodes)) {
            return STRING_TRUE_VALUES.contains(getStringValue(nodes).string());
        }
        if (hasIntegerValue(nodes)) {
            return getIntegerValue(nodes).integer() == INTEGER_TRUE;
        }
        if (hasDecimalValue(nodes)) {
            return getDecimalValue(nodes).decimal().equals(DECIMAL_TRUE);
        }
        return isSingleton(nodes);
    }
    
    public static boolean isFalse(Collection<FHIRPathNode> nodes) {
        if (hasBooleanValue(nodes)) {
            return getBooleanValue(nodes).isFalse();
        }
        if (hasStringValue(nodes)) {
            return STRING_FALSE_VALUES.contains(getStringValue(nodes).string());
        }
        if (hasIntegerValue(nodes)) {
            return getIntegerValue(nodes).integer() == INTEGER_FALSE;
        }
        if (hasDecimalValue(nodes)) {
            return getDecimalValue(nodes).decimal().equals(DECIMAL_FALSE);
        }
        return false;
    }

    public static boolean isSingleton(Collection<FHIRPathNode> nodes) {
        return nodes.size() == 1;
    }

    public static FHIRPathNode getSingleton(Collection<FHIRPathNode> nodes) {
        if (!isSingleton(nodes)) {
            throw new IllegalArgumentException();
        }
        if (nodes instanceof List) {
            List<?> list = (List<?>) nodes;
            return (FHIRPathNode) list.get(0);
        }
        return nodes.iterator().next();
    }
    
    public static Collection<FHIRPathNode> singleton(FHIRPathNode node) {
        return singletonList(node);
    }
    
    public static Collection<FHIRPathNode> empty() {
        return emptyList();
    }
    
    public static TemporalAccessor getTemporalAccessor(Temporal temporal, Class<?> targetType) {
        if (temporal.getClass().equals(targetType)) {
            return temporal;
        }
        if (Year.class.equals(targetType)) {
            return Year.from(temporal);
        } else if (YearMonth.class.equals(targetType)) {
            return YearMonth.from(temporal);
        } else if (LocalDate.class.equals(targetType)) {
            return LocalDate.from(temporal);
        } else if (LocalDateTime.class.equals(targetType)) {
            return LocalDateTime.from(temporal);
        } else if (ZonedDateTime.class.equals(targetType)){
            return ZonedDateTime.from(temporal);
        } else if (LocalTime.class.equals(targetType)) {
            return LocalTime.from(temporal);
        }
        throw new IllegalArgumentException();
    }
    
    public static Temporal getTemporal(TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof Year) {
            Year year = (Year) temporalAccessor;
            return year.atMonth(1).atDay(1);
        } else if (temporalAccessor instanceof YearMonth) {
            YearMonth yearMonth = (YearMonth) temporalAccessor;
            return yearMonth.atDay(1);
        } else if (temporalAccessor instanceof LocalDate) {
            return (LocalDate) temporalAccessor;
        } else if (temporalAccessor instanceof LocalDateTime) {
            return (LocalDateTime) temporalAccessor;
        } else if (temporalAccessor instanceof ZonedDateTime) {
            return (ZonedDateTime) temporalAccessor;
        } else if (temporalAccessor instanceof LocalTime) {
            return (LocalTime) temporalAccessor;
        }
        throw new IllegalArgumentException();
    }

    public static TemporalAmount getTemporalAmount(FHIRPathQuantityValue quantityValue) {
        int value = quantityValue.value().intValue();
        String unit = quantityValue.unit();        
        switch (unit) {
        case "year":
        case "years":
            return Period.ofYears(value);            
        case "month":
        case "months":
            return Period.ofMonths(value);            
        case "week":
        case "weeks":
            return Period.ofWeeks(value);            
        case "day":
        case "days":
            return Period.ofDays(value);            
        case "hour":
        case "hours":
            return Duration.ofHours(value);            
        case "minute":
        case "minutes":
            return Duration.ofMinutes(value);            
        case "second":
        case "seconds":
            return Duration.ofSeconds(value);            
        case "millisecond":
        case "milliseconds":
            return Duration.ofMillis(value);
        default:
            throw new IllegalArgumentException();
        }
    }

    public static SimpleTypeInfo buildSimpleTypeInfo(FHIRPathType type) {
        return new SimpleTypeInfo(type.namespace(), type.getName(), "System.Any");
    }

    public static TupleTypeInfo buildTupleTypeInfo(Class<?> modelClass) {
        List<TupleTypeInfoElement> element = new ArrayList<>();
        for (ElementInfo elementInfo : ModelSupport.getElementInfo(modelClass)) {
            if (elementInfo.isDeclaredBy(modelClass)) {
                element.add(buildTupleTypeInfoElement(elementInfo));
            }
        }
        return new TupleTypeInfo(element);
    }

    public static TupleTypeInfoElement buildTupleTypeInfoElement(ElementInfo elementInfo) {
        FHIRPathType type = FHIRPathType.from(elementInfo.getType());
        if (elementInfo.isRepeating()) {
            return new TupleTypeInfoElement(elementInfo.getName(), "List<" + type.namespace() + "." + type.getName() + ">", false);
        }
        return new TupleTypeInfoElement(elementInfo.getName(), type.namespace() + "." + type.getName());
    }

    public static ClassInfo buildClassInfo(FHIRPathType type) {
        List<ClassInfoElement> element = new ArrayList<>();
        Class<?> modelClass = type.modelClass();
        for (ElementInfo elementInfo : ModelSupport.getElementInfo(modelClass)) {
            if (elementInfo.isDeclaredBy(modelClass)) {
                element.add(buildClassInfoElement(elementInfo));
            }
        }
        return new ClassInfo(type.namespace(), type.getName(), type.baseType().namespace() + "." + type.baseType().getName(), element);
    }

    public static ClassInfoElement buildClassInfoElement(ElementInfo elementInfo) {
        FHIRPathType type = FHIRPathType.from(elementInfo.getType());
        String typeName;
        if (FHIRPathType.isSystemType(type) || 
                FHIRPathType.getSystemTypes().stream()
                    .map(t -> t.getName())
                    .anyMatch(name -> name.equalsIgnoreCase(type.getName()))) {
            typeName = type.namespace() + "." + type.getName();
        } else {
            typeName = type.getName();
        }
        if (elementInfo.isRepeating()) {
            return new ClassInfoElement(elementInfo.getName(), "List<" + typeName + ">", false);
        }
        return new ClassInfoElement(elementInfo.getName(), typeName);
    }
        
    public static Collection<FHIRPathNode> unordered(Collection<FHIRPathNode> nodes) {
        return new UnorderedCollection(nodes);
    }
    
    public static boolean isOrdered(Collection<FHIRPathNode> nodes) {
        return (nodes instanceof List);
    }
    
    public static boolean isUnordered(Collection<FHIRPathNode> nodes) {
        return (nodes instanceof UnorderedCollection);
    }
    
    public static class UnorderedCollection extends AbstractCollection<FHIRPathNode> {
        private final Collection<FHIRPathNode> nodes;
        
        public UnorderedCollection(Collection<FHIRPathNode> nodes) {
            Objects.requireNonNull(nodes);
            this.nodes = nodes;
        }

        @Override
        public Iterator<FHIRPathNode> iterator() {
            return nodes.iterator();
        }

        @Override
        public int size() {
            return nodes.size();
        }
    }
    
    public enum TimePrecision { HOURS, MINUTES, SECONDS, NONE };
    
    public static TimePrecision getTimePrecision(String text) {
        if (text == null || text.endsWith("T") || !text.contains("T")) {
            return TimePrecision.NONE;
        }
        String time = text.substring(text.indexOf("T") + 1);
        if (time.contains("+")) {
            time = time.substring(0, time.indexOf("+"));
        } else if (time.contains("-")) {
            time = time.substring(0, time.indexOf("-"));
        } else if (time.endsWith("Z")) {
            time = time.substring(0, time.length() - 1);
        }
        int count = count(text, ':');
        switch (count) {
        case 0:
            return TimePrecision.HOURS;
        case 1:
            return TimePrecision.MINUTES;
        case 2:
            return TimePrecision.SECONDS;
        default:
            return TimePrecision.NONE;
        }
    }
    
    public static TimePrecision getTimePrecision(TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof ZonedDateTime || 
                temporalAccessor instanceof LocalDateTime || 
                temporalAccessor instanceof LocalTime) {
            return TimePrecision.SECONDS;
        }
        return TimePrecision.NONE;
    }
    
    private static int count(String text, char ch) {
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ch) {
                count++;
            }
        }
        return count;
    }
    
    public static boolean isComparableTo(Collection<FHIRPathNode> left, Collection<FHIRPathNode> right) {
        if (left.size() != right.size()) {
            throw new IllegalArgumentException();
        }
        Iterator<FHIRPathNode> leftIterator = left.iterator();
        Iterator<FHIRPathNode> rightIterator = right.iterator();
        while (leftIterator.hasNext() && rightIterator.hasNext()) {
            FHIRPathNode leftNode = leftIterator.next();
            FHIRPathNode rightNode = rightIterator.next();
            if (hasTemporalValue(leftNode) && hasTemporalValue(rightNode) && 
                    !getTemporalValue(leftNode).isComparableTo(getTemporalValue(rightNode))) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean hasTemporalValue(FHIRPathNode node) {
        return (node instanceof FHIRPathTemporalValue) || 
                (node.getValue() instanceof FHIRPathTemporalValue);
    }
    
    public static FHIRPathTemporalValue getTemporalValue(FHIRPathNode node) {
        if (!hasTemporalValue(node)) {
            throw new IllegalArgumentException();
        }
        if (node instanceof FHIRPathTemporalValue) {
            return (FHIRPathTemporalValue) node;
        }
        return (FHIRPathTemporalValue) node.getValue();
    }
}