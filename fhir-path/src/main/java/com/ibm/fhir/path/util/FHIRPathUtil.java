/*
 * (C) Copyright IBM Corp. 2019, 2020
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

import com.ibm.fhir.model.patch.exception.FHIRPatchException;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.model.util.ModelSupport.ElementInfo;
import com.ibm.fhir.model.visitor.Visitable;
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
import com.ibm.fhir.path.FHIRPathTree;
import com.ibm.fhir.path.FHIRPathType;
import com.ibm.fhir.path.SimpleTypeInfo;
import com.ibm.fhir.path.TupleTypeInfo;
import com.ibm.fhir.path.TupleTypeInfoElement;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.exception.FHIRPathException;

public final class FHIRPathUtil {
    private static FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
    
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
    public static final Map<String, String> UNESCAPED = new HashMap<>();
    static {
        UNESCAPED.put("\\`", "`");
        UNESCAPED.put("\\'", "'");
        UNESCAPED.put("\\\\", "\\");
        UNESCAPED.put("\\/", "/");
        UNESCAPED.put("\\f", "\f");
        UNESCAPED.put("\\n", "\n");
        UNESCAPED.put("\\r", "\r");
        UNESCAPED.put("\\t", "\t");
    }
    
    private FHIRPathUtil() { }

    public static boolean isTypeCompatible(FHIRPathSystemValue leftValue, FHIRPathSystemValue rightValue) {
        return TYPE_COMPATIBILITY_MAP.get(leftValue.type()).contains(rightValue.type());
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

    public static String unescape(String s) {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        while (index < s.length()) {
            if (s.regionMatches(index, "\\u", 0, 2)) {
                int hex = Integer.parseInt(s.substring(index + 2, index + 6), 16);
                sb.append(Character.toChars(hex));
                index += 6;
            } else if (s.regionMatches(index, "\\", 0, 1)) {
                String escaped = s.substring(index, index + 2);
                if (UNESCAPED.containsKey(escaped)) {
                    sb.append(UNESCAPED.get(escaped));
                    index += 2;
                } else {
                    sb.append(s.charAt(index++));
                }
            } else {
                sb.append(s.charAt(index++));
            }
        }
        return sb.toString();
    }
    
    /**
     * The content will be appended to the element identified in the path, using the name specified.
     * Add can used for non-repeating elements as long as they do not already exist.
     * @throws FHIRPathException 
     * @throws FHIRPatchException 
     */
    public static <T extends Visitable> T add(T elementOrResource, String fhirPath, String elementName, Element value) throws FHIRPathException, FHIRPatchException {
        FHIRPathNode node = evaluateToSingle(elementOrResource, fhirPath);
        Visitable parent = node.isResourceNode() ? 
                node.asResourceNode().resource() : node.asElementNode().element();

        AddingVisitor<T> addingVisitor = new AddingVisitor<T>(parent, elementName, value);

        try {
            elementOrResource.accept(addingVisitor);
        } catch (IllegalStateException e) {
            throw new FHIRPatchException("An error occurred while adding the value", fhirPath, e);
        }

        return addingVisitor.getResult();
    }
    
    /**
     * Only a single element can be deleted
     * @throws FHIRPathException 
     * @throws FHIRPatchException 
     */
    public static <T extends Visitable> T delete(T elementOrResource, String fhirPath, String elementName) throws FHIRPathException, FHIRPatchException {
        FHIRPathNode node = evaluateToSingle(elementOrResource, fhirPath);
        Visitable toDelete = node.isResourceNode() ? 
                node.asResourceNode().resource() : node.asElementNode().element();

        FHIRPathTree tree = evaluator.getEvaluationContext().getTree();
        FHIRPathNode parentNode = tree.getParent(node);
        Visitable parent = parentNode.isResourceNode() ? 
                parentNode.asResourceNode().resource() : parentNode.asElementNode().element();

        DeletingVisitor<T> deletingVisitor = new DeletingVisitor<T>(parent, elementName, toDelete);

        elementOrResource.accept(deletingVisitor);

        return deletingVisitor.getResult();
    }

    /**
     * @param fhirPath
     * @param value
     * @throws FHIRPathException 
     * @throws FHIRPatchException 
     */
    public static <T extends Visitable> T replace(T elementOrResource, String fhirPath, String elementName, Element value) throws FHIRPathException, FHIRPatchException {
        FHIRPathNode node = evaluateToSingle(elementOrResource, fhirPath);
        Visitable toReplace = node.isResourceNode() ? 
                node.asResourceNode().resource() : node.asElementNode().element();
                
        FHIRPathTree tree = evaluator.getEvaluationContext().getTree();
        FHIRPathNode parentNode = tree.getParent(node);
        Visitable parent = parentNode.isResourceNode() ? 
                parentNode.asResourceNode().resource() : parentNode.asElementNode().element();

        ReplacingVisitor<T> replacingVisitor = new ReplacingVisitor<T>(parent, elementName, toReplace, value);

        elementOrResource.accept(replacingVisitor);

        return replacingVisitor.getResult();
    }

    private static FHIRPathNode evaluateToSingle(Visitable elementOrResource, String fhirPath) throws FHIRPathException, FHIRPatchException {
        /*
         * 1. The FHIRPath statement must return a single element.
         * 2. The FHIRPath statement SHALL NOT cross resources using the resolve() function 
         *    (e.g. like Observation.subject.resolve().identifier).
         *    Resolve() SHALL only be used to refer to contained resource within the resource being patched.
         *    Servers SHALL NOT allow patch operations to alter other resources than the nominated target, 
         *    and SHOULD return an error if the patch operation tries.
         * 3. The type of the value must be correct for the place at which it will be added/inserted.
         *    Servers SHALL return an error if the type is wrong.
         * 4. Servers SHALL return an error if the outcome of the patch operation is a not a valid resource.
         * 5. Except for the delete operation, it is an error if no element matches the specified path.
         */
        Collection<FHIRPathNode> nodes = elementOrResource instanceof Resource ?
                evaluator.evaluate((Resource) elementOrResource, fhirPath) :
                evaluator.evaluate((Element) elementOrResource, fhirPath);
        if (!isSingleton(nodes)) {
            throw new FHIRPatchException("The FHIRPath statement must return a single element", fhirPath);
        }
        return nodes.iterator().next();
    }

    /**
     * The content will be inserted into the nominated list at the index specified (0 based).
     * The index is mandatory and must be equal or less than the number of elements in the list.
     * Note: add is easier than insert at the end of the list
     * @throws FHIRPathException 
     * @throws FHIRPatchException 
     */
    public static <T extends Visitable> T insert(T elementOrResource, String fhirPath, String elementName, int index, Element value) throws FHIRPathException, FHIRPatchException {
        Collection<FHIRPathNode> nodes = evaluator.evaluate(elementOrResource, fhirPath);
        if (index > nodes.size()) {
            throw new FHIRPatchException("index must be equal or less than the number of elements in the list", fhirPath);
        }
        
        // The parent is only used to ensure that we're adding the new item to the right level of the tree
        // XXX: this will stay null if the FHIRPath resolves to an empty set. Is there a better way to reliably 
        //      compute the parent of the repeating element?
        Visitable parent = null;
        FHIRPathTree tree = evaluator.getEvaluationContext().getTree();
        for (FHIRPathNode fhirPathNode : nodes) {
            FHIRPathNode parentNode = tree.getParent(fhirPathNode);
            Visitable currentParent = parentNode.isResourceNode() ? 
                    parentNode.asResourceNode().resource() : parentNode.asElementNode().element();
            
            // Make sure all the found nodes have a common parent
            if (parent != null) {
                if (!parent.equals(currentParent)) {
                    throw new FHIRPatchException("The FHIRPath statement must return a single element", fhirPath);
                }
            } else {
                parent = currentParent;
            }
        }
        
        InsertingVisitor<T> insertingVisitor = new InsertingVisitor<T>(parent, elementName, index, value);
        
        elementOrResource.accept(insertingVisitor);
        
        return insertingVisitor.getResult();
    }

    /**
     * Move an element within a single list
     * @throws FHIRPathException 
     * @throws FHIRPatchException 
     */
    public static <T extends Visitable> T move(T elementOrResource, String fhirPath, String elementName, int source, int target) throws FHIRPathException, FHIRPatchException {
        Collection<FHIRPathNode> nodes = evaluator.evaluate(elementOrResource, fhirPath);
        if (source > nodes.size() || target > nodes.size()) {
            throw new FHIRPatchException("source and target indices must be equal or less than the number of elements in the list", fhirPath);
        } else if (source == target) {
            return elementOrResource;
        }
        
        FHIRPathTree tree = evaluator.getEvaluationContext().getTree();
        Visitable parent = null;
        for (FHIRPathNode fhirPathNode : nodes) {
            FHIRPathNode parentNode = tree.getParent(fhirPathNode);
            Visitable currentParent = parentNode.isResourceNode() ? 
                    parentNode.asResourceNode().resource() : parentNode.asElementNode().element();
            
            // Make sure all the found nodes have a common parent
            if (parent != null) {
                if (!parent.equals(currentParent)) {
                    throw new FHIRPatchException("The FHIRPath statement must return a single element", fhirPath);
                }
            } else {
                parent = currentParent;
            }
        }
        
        MovingVisitor<T> movingVisitor = new MovingVisitor<T>(parent, elementName, source, target);
        
        elementOrResource.accept(movingVisitor);
        
        return movingVisitor.getResult();
    }
}