/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.util;

import static com.ibm.fhir.model.util.ModelSupport.FHIR_STRING;
import static com.ibm.fhir.path.FHIRPathDateTimeValue.dateTimeValue;
import static com.ibm.fhir.path.FHIRPathDateValue.dateValue;
import static com.ibm.fhir.path.FHIRPathTimeValue.timeValue;
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
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAmount;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import com.ibm.fhir.model.patch.exception.FHIRPatchException;
import com.ibm.fhir.model.resource.DomainResource;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.CodeableReference;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.model.util.ModelSupport.ElementInfo;
import com.ibm.fhir.model.visitor.Visitable;
import com.ibm.fhir.path.ClassInfo;
import com.ibm.fhir.path.ClassInfoElement;
import com.ibm.fhir.path.FHIRPathBooleanValue;
import com.ibm.fhir.path.FHIRPathDateTimeValue;
import com.ibm.fhir.path.FHIRPathDateValue;
import com.ibm.fhir.path.FHIRPathDecimalValue;
import com.ibm.fhir.path.FHIRPathElementNode;
import com.ibm.fhir.path.FHIRPathIntegerValue;
import com.ibm.fhir.path.FHIRPathLexer;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.FHIRPathNumberValue;
import com.ibm.fhir.path.FHIRPathParser;
import com.ibm.fhir.path.FHIRPathParser.ExpressionContext;
import com.ibm.fhir.path.FHIRPathQuantityNode;
import com.ibm.fhir.path.FHIRPathQuantityValue;
import com.ibm.fhir.path.FHIRPathResourceNode;
import com.ibm.fhir.path.FHIRPathStringValue;
import com.ibm.fhir.path.FHIRPathSystemValue;
import com.ibm.fhir.path.FHIRPathTemporalValue;
import com.ibm.fhir.path.FHIRPathTimeValue;
import com.ibm.fhir.path.FHIRPathTree;
import com.ibm.fhir.path.FHIRPathType;
import com.ibm.fhir.path.SimpleTypeInfo;
import com.ibm.fhir.path.TupleTypeInfo;
import com.ibm.fhir.path.TupleTypeInfoElement;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.exception.FHIRPathException;

public final class FHIRPathUtil {
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

    private static final ANTLRErrorListener SYNTAX_ERROR_LISTENER = new BaseErrorListener() {
        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
            throw new ParseCancellationException(String.format("line %d:%d %s", line, charPositionInLine, msg), e);
        }
    };

    private FHIRPathUtil() { }

    public static ExpressionContext compile(String expr) {
        int stopIndex = -1;
        for (int i = expr.length() - 1; i >= 0; i--) {
            if (!Character.isWhitespace(expr.charAt(i))) {
                stopIndex = i;
                break;
            }
        }

        if (stopIndex == -1) {
            throw new IllegalArgumentException("Invalid FHIRPath expression: '" + expr + "'");
        }

        FHIRPathLexer lexer = new FHIRPathLexer(CharStreams.fromString(expr));
        lexer.removeErrorListeners();
        lexer.addErrorListener(SYNTAX_ERROR_LISTENER);

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        FHIRPathParser parser = new FHIRPathParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(SYNTAX_ERROR_LISTENER);

        ExpressionContext expressionContext = parser.expression();
        List<Token> hiddenTokensToRight = tokens.getHiddenTokensToRight(expressionContext.getStop().getTokenIndex());
        if (hiddenTokensToRight == null && expressionContext.getStop().getStopIndex() != stopIndex) {
            throw new IllegalArgumentException("FHIRPath expression parsing error at: '" + expr.charAt(expressionContext.getStop().getStopIndex() + 1) + "'");
        }

        return expressionContext;
    }

    public static boolean isTypeCompatible(FHIRPathSystemValue leftValue, FHIRPathSystemValue rightValue) {
        return TYPE_COMPATIBILITY_MAP.get(leftValue.type()).contains(rightValue.type());
    }

    public static boolean isResourceNode(Collection<FHIRPathNode> nodes) {
        return isSingleton(nodes, FHIRPathResourceNode.class);
    }

    public static FHIRPathResourceNode getResourceNode(Collection<FHIRPathNode> nodes) {
        return getSingleton(nodes, FHIRPathResourceNode.class);
    }

    public static boolean isElementNode(Collection<FHIRPathNode> nodes) {
        return isSingleton(nodes, FHIRPathElementNode.class);
    }

    public static FHIRPathElementNode getElementNode(Collection<FHIRPathNode> nodes) {
        return getSingleton(nodes).asElementNode();
    }

    public static boolean isCodedElementNode(Collection<FHIRPathNode> nodes) {
        return isElementNode(nodes) && isCodedElementNode(getElementNode(nodes));
    }

    public static boolean isCodedElementNode(FHIRPathElementNode elementNode) {
        return isCodedElement(elementNode.element());
    }

    public static boolean isCodedElement(Element element) {
        return element.is(Code.class) || element.is(Coding.class) || element.is(CodeableConcept.class) || element.is(CodeableReference.class);
    }

    public static boolean isStringElementNode(Collection<FHIRPathNode> nodes) {
        return isElementNode(nodes) && isStringElementNode(getElementNode(nodes));
    }

    public static boolean isStringElementNode(FHIRPathElementNode elementNode) {
        return elementNode.element().is(FHIR_STRING);
    }

    public static boolean isUriElementNode(Collection<FHIRPathNode> nodes) {
        return isElementNode(nodes) && isUriElementNode(getElementNode(nodes));
    }

    private static boolean isUriElementNode(FHIRPathElementNode elementNode) {
        return elementNode.element().is(Uri.class);
    }

    public static boolean isQuantityNode(Collection<FHIRPathNode> nodes) {
        return isSingleton(nodes, FHIRPathQuantityNode.class);
    }

    public static FHIRPathQuantityNode getQuantityNode(Collection<FHIRPathNode> nodes) {
        return getSingleton(nodes, FHIRPathQuantityNode.class);
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

    public static boolean hasDateValue(Collection<FHIRPathNode> nodes) {
        return hasTemporalValue(nodes) && getTemporalValue(nodes).isDateValue();
    }

    public static FHIRPathDateValue getDateValue(Collection<FHIRPathNode> nodes) {
        return getTemporalValue(nodes).asDateValue();
    }

    public static boolean hasDateTimeValue(Collection<FHIRPathNode> nodes) {
        return hasTemporalValue(nodes) && getTemporalValue(nodes).isDateTimeValue();
    }

    public static FHIRPathDateTimeValue getDateTimeValue(Collection<FHIRPathNode> nodes) {
        return getTemporalValue(nodes).asDateTimeValue();
    }

    public static boolean hasTimeValue(Collection<FHIRPathNode> nodes) {
        return hasTemporalValue(nodes) && getTemporalValue(nodes).isTimeValue();
    }

    public static FHIRPathTimeValue getTimeValue(Collection<FHIRPathNode> nodes) {
        return getTemporalValue(nodes).asTimeValue();
    }

    public static boolean hasStringValue(Collection<FHIRPathNode> nodes) {
        return hasSystemValue(nodes) && getSystemValue(nodes).isStringValue();
    }

    public static boolean hasQuantityValue(Collection<FHIRPathNode> nodes) {
        return hasSystemValue(nodes) && getSystemValue(nodes).isQuantityValue();
    }

    public static boolean evaluatesToBoolean(Collection<FHIRPathNode> nodes) {
        return evaluatesToTrue(nodes) || isFalse(nodes);
    }

    /**
     * Indicates whether the input collection evaluates to a boolean value per:
     * <a href="http://hl7.org/fhirpath/N1/index.html#singleton-evaluation-of-collections">Singleton Evaluation of Collections</a>
     *
     * @param nodes
     *    the input collection
     * @return
     *    true if the input collection evaluates to a boolean value, false otherwise
     */
    public static boolean evaluatesToTrue(Collection<FHIRPathNode> nodes) {
        if (convertsToBoolean(nodes)) {
            return toBoolean(nodes) == true;
        }
        return isSingleton(nodes);
    }

    /**
     * Indicates whether {@link FHIRPathUtil#convertsToBoolean} returns a true value for the input collection
     *
     * @param nodes
     *     the input collection
     * @return
     *     true iff {@link FHIRPathUtil#convertsToBoolean} returns a true value for the input collection, false otherwise
     */
    public static boolean isTrue(Collection<FHIRPathNode> nodes) {
        return convertsToBoolean(nodes) && toBoolean(nodes) == true;
    }

    /**
     * Indicates whether {@link FHIRPathUtil#convertsToBoolean} returns a false value for the input collection
     *
     * @param nodes
     *     the input collection
     * @return
     *     true iff {@link FHIRPathUtil#convertsToBoolean} returns a false value for the input collection, false otherwise
     */
    public static boolean isFalse(Collection<FHIRPathNode> nodes) {
        return convertsToBoolean(nodes) && toBoolean(nodes) == false;
    }

    /**
     * Indicates whether the input collection can be implicitly or explicitly converted to a boolean value per:
     * <a href="http://hl7.org/fhirpath/N1/index.html#boolean-conversion-functions">Boolean Conversion Functions</a>
     *
     * @param nodes
     *     the input collection
     * @return
     *     true if the input collection can be explicitly or implicitly converted to a boolean value, false otherwise
     */
    public static boolean convertsToBoolean(Collection<FHIRPathNode> nodes) {
        if (hasBooleanValue(nodes)) {
            return true;
        }
        if (hasStringValue(nodes)) {
            String string = getStringValue(nodes).string().toLowerCase();
            return STRING_TRUE_VALUES.contains(string) || STRING_FALSE_VALUES.contains(string);
        }
        if (hasIntegerValue(nodes)) {
            Integer integer = getIntegerValue(nodes).integer();
            return integer == INTEGER_TRUE || integer == INTEGER_FALSE;
        }
        if (hasDecimalValue(nodes)) {
            BigDecimal decimal = getDecimalValue(nodes).decimal();
            return decimal.equals(DECIMAL_TRUE) || decimal.equals(DECIMAL_FALSE);
        }
        return false;
    }

    public static boolean toBoolean(Collection<FHIRPathNode> nodes) {
        if (!convertsToBoolean(nodes)) {
            throw new IllegalArgumentException();
        }
        if (hasBooleanValue(nodes)) {
            return getBooleanValue(nodes).isTrue();
        }
        if (hasStringValue(nodes)) {
            return STRING_TRUE_VALUES.contains(getStringValue(nodes).string().toLowerCase());
        }
        if (hasIntegerValue(nodes)) {
            return getIntegerValue(nodes).integer() == INTEGER_TRUE;
        }
        if (hasDecimalValue(nodes)) {
            return getDecimalValue(nodes).decimal().equals(DECIMAL_TRUE);
        }
        throw new AssertionError();
    }

    public static boolean convertsToDate(Collection<FHIRPathNode> nodes) {
        if (hasDateValue(nodes)) {
            return true;
        }
        if (hasDateTimeValue(nodes)) {
            return true;
        }
        if (hasStringValue(nodes)) {
            try {
                dateValue(getString(nodes));
                return true;
            } catch (DateTimeParseException e) { }
        }
        return false;
    }

    public static FHIRPathDateValue toDate(Collection<FHIRPathNode> nodes) {
        if (!convertsToDate(nodes)) {
            throw new IllegalArgumentException();
        }
        if (hasDateValue(nodes)) {
            return getDateValue(nodes);
        }
        if (hasDateTimeValue(nodes)) {
            TemporalAccessor dateTime = getDateTimeValue(nodes).dateTime();
            if (dateTime instanceof ZonedDateTime || dateTime instanceof LocalDateTime) {
                dateTime = LocalDate.from(dateTime);
            }
            return dateValue(dateTime);
        }
        if (hasStringValue(nodes)) {
            return dateValue(getString(nodes));
        }
        throw new AssertionError();
    }

    public static boolean convertsToDateTime(Collection<FHIRPathNode> nodes) {
        if (hasDateTimeValue(nodes)) {
            return true;
        }
        if (hasDateValue(nodes)) {
            return true;
        }
        if (hasStringValue(nodes)) {
            try {
                dateTimeValue(getString(nodes));
                return true;
            } catch (DateTimeParseException e) { }
        }
        return false;
    }

    public static FHIRPathDateTimeValue toDateTime(Collection<FHIRPathNode> nodes) {
        if (!convertsToDateTime(nodes)) {
            throw new IllegalArgumentException();
        }
        if (hasDateTimeValue(nodes)) {
            return getDateTimeValue(nodes);
        }
        if (hasDateValue(nodes)) {
            TemporalAccessor date = getDateValue(nodes).date();
            return dateTimeValue(date);
        }
        if (hasStringValue(nodes)) {
            return dateTimeValue(getString(nodes));
        }
        throw new AssertionError();
    }

    public static boolean convertsToTime(Collection<FHIRPathNode> nodes) {
        if (hasTimeValue(nodes)) {
            return true;
        }
        if (hasStringValue(nodes)) {
            try {
                timeValue(getString(nodes));
                return true;
            } catch (DateTimeParseException e) { }
        }
        return false;
    }

    public static FHIRPathTimeValue toTime(Collection<FHIRPathNode> nodes) {
        if (!convertsToTime(nodes)) {
            throw new IllegalArgumentException();
        }
        if (hasTimeValue(nodes)) {
            return getTimeValue(nodes);
        }
        if (hasStringValue(nodes)) {
            return timeValue(getString(nodes));
        }
        throw new AssertionError();
    }

    public static boolean isSingleton(Collection<FHIRPathNode> nodes) {
        return nodes.size() == 1;
    }

    public static <T extends FHIRPathNode> boolean isSingleton(Collection<FHIRPathNode> nodes, Class<T> nodeType) {
        return isSingleton(nodes) && getSingleton(nodes).is(nodeType);
    }

    public static boolean isSystemValue(Collection<FHIRPathNode> nodes) {
        return isSingleton(nodes, FHIRPathSystemValue.class);
    }

    public static boolean isStringValue(Collection<FHIRPathNode> nodes) {
        return isSingleton(nodes, FHIRPathStringValue.class);
    }

    public static boolean isBooleanValue(Collection<FHIRPathNode> nodes) {
        return isSingleton(nodes, FHIRPathBooleanValue.class);
    }

    public static boolean isIntegerValue(Collection<FHIRPathNode> nodes) {
        return isSingleton(nodes, FHIRPathIntegerValue.class);
    }

    public static boolean isDecimalValue(Collection<FHIRPathNode> nodes) {
        return isSingleton(nodes, FHIRPathDecimalValue.class);
    }

    /**
     * @throws IllegalArgumentException if the passed collection is not a singleton
     */
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

    public static <T extends FHIRPathNode> T getSingleton(Collection<FHIRPathNode> nodes, Class<T> nodeType) {
        return getSingleton(nodes).as(nodeType);
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

    public static ChronoUnit getChronoUnit(String unit) {
        switch (unit) {
        case "years":
            return ChronoUnit.YEARS;
        case "months":
            return ChronoUnit.MONTHS;
        case "weeks":
            return ChronoUnit.WEEKS;
        case "days":
            return ChronoUnit.DAYS;
        case "hours":
            return ChronoUnit.HOURS;
        case "minutes":
            return ChronoUnit.MINUTES;
        case "seconds":
            return ChronoUnit.SECONDS;
        case "milliseconds":
            return ChronoUnit.MILLIS;
        default:
            throw new IllegalArgumentException("Unsupported unit: " + unit);
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
        return new TupleTypeInfoElement(elementInfo.getName(), type.namespace() + "." + type.getName(), !elementInfo.isRepeating());
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
        return new ClassInfoElement(elementInfo.getName(), typeName, !elementInfo.isRepeating());
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
            this.nodes = Objects.requireNonNull(nodes);
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

    public static ChronoField getPrecision(TemporalAccessor temporalAccessor) {
        return getPrecision(temporalAccessor, null);
    }

    public static ChronoField getPrecision(TemporalAccessor temporalAccessor, String text) {
        if (temporalAccessor instanceof Year) {
            return ChronoField.YEAR;
        }

        if (temporalAccessor instanceof YearMonth) {
            return ChronoField.MONTH_OF_YEAR;
        }

        if (temporalAccessor instanceof LocalDate) {
            return ChronoField.DAY_OF_MONTH;
        }

        if (temporalAccessor instanceof LocalDateTime ||
                temporalAccessor instanceof ZonedDateTime ||
                temporalAccessor instanceof LocalTime) {
            if (text != null) {
                return getPrecision(text);
            }

            return (temporalAccessor instanceof ZonedDateTime) ?
                    ChronoField.OFFSET_SECONDS : ChronoField.MICRO_OF_SECOND;
        }

        throw new IllegalArgumentException();
    }

    private static ChronoField getPrecision(String text) {
        if (text.endsWith("T")) {
            return ChronoField.DAY_OF_YEAR;
        }

        String time = text.contains("T") ? text.substring(text.indexOf("T") + 1) : text;

        if (time.contains("+") || time.contains("-") || time.endsWith("Z")) {
            return ChronoField.OFFSET_SECONDS;
        }

        int count = count(time, ':');

        switch (count) {
        case 0:
            return ChronoField.HOUR_OF_DAY;
        case 1:
            return ChronoField.MINUTE_OF_HOUR;
        case 2:
            return ChronoField.MICRO_OF_SECOND;
        }

        throw new IllegalArgumentException(text);
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
     * @throws NullPointerException if any of the passed arguments are null
     */
    public static <T extends Visitable> T add(T elementOrResource, String fhirPath, String elementName, Visitable value)
            throws FHIRPathException, FHIRPatchException {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        FHIRPathNode node = evaluateToSingle(evaluator, elementOrResource, fhirPath);
        Visitable parent = node.isResourceNode() ?
                node.asResourceNode().resource() : node.asElementNode().element();

        try {
            AddingVisitor<T> addingVisitor = new AddingVisitor<>(parent, node.path(), elementName, value);
            elementOrResource.accept(addingVisitor);
            return addingVisitor.getResult();
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new FHIRPatchException("An error occurred while adding the value", fhirPath, e);
        }
    }

    /**
     * Only a single element can be deleted
     * @throws FHIRPathException
     * @throws FHIRPatchException
     * @throws NullPointerException if any of the passed arguments are null
     */
    public static <T extends Visitable> T delete(T elementOrResource, String fhirPath) throws FHIRPathException, FHIRPatchException {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        FHIRPathNode node = evaluateToSingle(evaluator, elementOrResource, fhirPath);

        try {
            DeletingVisitor<T> deletingVisitor = new DeletingVisitor<T>(node.path());
            elementOrResource.accept(deletingVisitor);
            return deletingVisitor.getResult();
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new FHIRPatchException("An error occurred while deleting the value", fhirPath, e);
        }
    }

    /**
     * @param elementOrResource
     * @param fhirPath
     * @param value
     * @throws FHIRPathException
     * @throws FHIRPatchException
     * @throws NullPointerException if any of the passed arguments are null
     */
    public static <T extends Visitable> T replace(T elementOrResource, String fhirPath, Visitable value) throws FHIRPathException, FHIRPatchException {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        FHIRPathNode node = evaluateToSingle(evaluator, elementOrResource, fhirPath);
        String elementName = node.name();

        FHIRPathTree tree = evaluator.getEvaluationContext().getTree();
        FHIRPathNode parentNode = tree.getParent(node);

        if (parentNode == null) {
            throw new FHIRPatchException("Unable to compute the parent for '" + elementName + "';" +
                    " a FHIRPathPatch replace FHIRPath must select a node with a parent", fhirPath);
        }
        Visitable parent = parentNode.isResourceNode() ?
                parentNode.asResourceNode().resource() : parentNode.asElementNode().element();

        try {
            ReplacingVisitor<T> replacingVisitor = new ReplacingVisitor<T>(parent, elementName, node.path(), value);
            elementOrResource.accept(replacingVisitor);
            return replacingVisitor.getResult();
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new FHIRPatchException("An error occurred while replacing the value", fhirPath, e);
        }
    }

    /**
     * @param evaluator
     * @param elementOrResource
     * @param fhirPath
     * @return
     * @throws FHIRPathException
     * @throws FHIRPatchException if the fhirPath does not evaluate to a single node
     */
    private static FHIRPathNode evaluateToSingle(FHIRPathEvaluator evaluator, Visitable elementOrResource, String fhirPath)
            throws FHIRPathException, FHIRPatchException {
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
        Collection<FHIRPathNode> nodes = evaluator.evaluate(elementOrResource, fhirPath);
        if (!isSingleton(nodes)) {
            throw new FHIRPatchException("The FHIRPath must return a single element but instead returned " + nodes.size(), fhirPath);
        }
        return getSingleton(nodes);
    }

    /**
     * The content will be inserted into the nominated list at the index specified (0 based).
     * The index is mandatory and must be equal or less than the number of elements in the list.
     * Note: add is easier than insert at the end of the list.
     * @throws FHIRPathException
     * @throws FHIRPatchException
     * @throws NullPointerException if any of the passed arguments are null
     */
    public static <T extends Visitable> T insert(T elementOrResource, String fhirPath, int index, Visitable value)
            throws FHIRPathException, FHIRPatchException {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> nodes = evaluator.evaluate(elementOrResource, fhirPath);
        if (index > nodes.size()) {
            throw new FHIRPatchException("Index must be equal or less than the number of elements in the list", fhirPath);
        } else if (nodes.size() == 0) {
            throw new FHIRPatchException("Cannot insert the first element of an empty list; use 'add' instead", fhirPath);
        }
        String elementName = getCommonName(fhirPath, nodes);

        FHIRPathTree tree = evaluator.getEvaluationContext().getTree();
        FHIRPathNode parentNode = getCommonParent(fhirPath, nodes, tree);
        Visitable parent = parentNode.isResourceNode() ?
                parentNode.asResourceNode().resource() : parentNode.asElementNode().element();

        try {
            InsertingVisitor<T> insertingVisitor = new InsertingVisitor<T>(parent, parentNode.path(), elementName, index, value);
            elementOrResource.accept(insertingVisitor);
            return insertingVisitor.getResult();
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new FHIRPatchException("An error occurred while inserting the value", fhirPath, e);
        }
    }

    /**
     * Move an element within a single list
     * @throws FHIRPathException
     * @throws FHIRPatchException
     * @throws NullPointerException if any of the passed arguments are null
     */
    public static <T extends Visitable> T move(T elementOrResource, String fhirPath, int source, int target)
            throws FHIRPathException, FHIRPatchException {
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        Collection<FHIRPathNode> nodes = evaluator.evaluate(elementOrResource, fhirPath);
        if (source > nodes.size() || target > nodes.size()) {
            throw new FHIRPatchException("Source and target indices must be less than or equal to"
                    + " the number of elements selected by the FHIRPath expression", fhirPath);
        } else if (source == target) {
            return elementOrResource;
        }
        // The statements above rule out any chance of nodes being empty at this point
        String elementName = getCommonName(fhirPath, nodes);

        FHIRPathTree tree = evaluator.getEvaluationContext().getTree();
        FHIRPathNode parent = getCommonParent(fhirPath, nodes, tree);

        try {
            MovingVisitor<T> movingVisitor = new MovingVisitor<T>(parent.path(), elementName, source, target);
            elementOrResource.accept(movingVisitor);
            return movingVisitor.getResult();
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new FHIRPatchException("An error occurred while moving the value", fhirPath, e);
        }
    }

    /**
     * Verifies that all the nodes have a common name and returns it
     *
     * @throws FHIRPatchException if the nodes have names that differ
     */
    private static String getCommonName(String fhirPath, Collection<FHIRPathNode> nodes) throws FHIRPatchException {
        String name = null;
        for (FHIRPathNode fhirPathNode : nodes) {
            String currentName = fhirPathNode.name();

            if (name != null) {
                if (!name.equals(currentName)) {
                    throw new FHIRPatchException("The FHIRPath expression must return a set of nodes with a "
                            + "single parent", fhirPath);
                }
            } else {
                name = currentName;
            }
        }
        return name;
    }

    /**
     * @param fhirPath
     *            The fhirPath executed to produce the nodes (only used for the error message)
     * @param nodes
     *            The nodes selected by the fhirPath expression
     * @param tree
     *            The tree computed for the FHIRPath EvaluationContext
     * @return
     * @throws FHIRPatchException if the selected nodes have different parents
     */
    private static FHIRPathNode getCommonParent(String fhirPath, Collection<FHIRPathNode> nodes, FHIRPathTree tree)
            throws FHIRPatchException {
        FHIRPathNode parent = null;
        for (FHIRPathNode fhirPathNode : nodes) {
            FHIRPathNode parentNode = tree.getParent(fhirPathNode);

            if (parent != null) {
                if (parent != parentNode) {
                    throw new FHIRPatchException("The FHIRPath expression must return a set of nodes with a "
                            + "single parent", fhirPath);
                }
            } else {
                parent = parentNode;
            }
        }
        return parent;
    }

    /**
     * Get the resource node to use as a value for the %resource external constant.
     *
     * @param tree
     *     the FHIRPath tree
     * @param node
     *     the context node
     * @return
     *     the resource node, or null if not exists
     */
    public static FHIRPathResourceNode getResourceNode(FHIRPathTree tree, FHIRPathNode node) {
        // get resource node ancestors for the context node
        List<FHIRPathResourceNode> resourceNodes = getResourceNodes(tree, node);

        if (!resourceNodes.isEmpty()) {
            // return nearest resource node ancestor
            return resourceNodes.get(0);
        }

        return null;
    }

    /**
     * Get the resource node to use as a value for the %rootResource external constant.
     *
     * @param tree
     *     the FHIRPath tree
     * @param node
     *     the context node
     * @return
     *     the rootResource node, or null if not exists
     */
    public static FHIRPathResourceNode getRootResourceNode(FHIRPathTree tree, FHIRPathNode node) {
        // get resource node ancestors for the context node
        List<FHIRPathResourceNode> resourceNodes = getResourceNodes(tree, node);

        if (!resourceNodes.isEmpty()) {
            if (isContained(resourceNodes)) {
                return resourceNodes.get(1);
            } else {
                return resourceNodes.get(0);
            }
        }

        return null;
    }

    /**
     * Determine whether the list of resource node ancestors indicates containment within a domain resource.
     *
     * @param resourceNodes
     *     the list of resource node ancestors
     * @return
     *     true if there is containment, false otherwise
     */
    private static boolean isContained(List<FHIRPathResourceNode> resourceNodes) {
        return resourceNodes.size() > 1 && resourceNodes.get(1).resource().is(DomainResource.class);
    }

    /**
     * Get the list of resource nodes in the path of the given context node (including the context node itself).
     * The ancestors are ordered from node to root (i.e. the node at index 0 is either the current node or
     * the nearest resource node ancestor).
     *
     * @param tree
     *     the FHIRPath tree
     * @param node
     *     the context node
     * @return
     *     the list of resource node ancestors, or empty if none exist
     */
    private static List<FHIRPathResourceNode> getResourceNodes(FHIRPathTree tree, FHIRPathNode node) {
        if (tree != null) {
            List<FHIRPathResourceNode> resourceNodes = new ArrayList<>();

            if (node.isResourceNode()) {
                resourceNodes.add(node.asResourceNode());
            }

            String path = node.path();
            int index = path.lastIndexOf(".");
            while (index != -1) {
                path = path.substring(0, index);
                node = tree.getNode(path);
                if (node.isResourceNode()) {
                    resourceNodes.add(node.asResourceNode());
                }
                index = path.lastIndexOf(".");
            }

            return resourceNodes;
        }
        return Collections.emptyList();
    }

    /**
     * Get the URI-typed sibling of the given element node with name "system".
     *
     * @param tree
     *     the tree
     * @param elementNode
     *     the element node
     * @return
     *     the URI-typed sibling of the given element node with name "system", or null if no such sibling exists
     */
    public static Uri getSystem(FHIRPathTree tree, FHIRPathElementNode elementNode) {
        if (tree != null) {
            FHIRPathNode systemNode = tree.getSibling(elementNode, "system");
            if (systemNode != null && FHIRPathType.FHIR_URI.equals(systemNode.type())) {
                return systemNode.asElementNode().element().as(Uri.class);
            }
        }
        return null;
    }

    /**
     * Get the String-typed sibling of the given element node with name "version".
     *
     * @param tree
     *     the tree
     * @param elementNode
     *     the element node
     * @return
     *     the String-typed sibling of the given element node with name "version", or null if no such sibling exists
     */
    public static com.ibm.fhir.model.type.String getVersion(FHIRPathTree tree, FHIRPathElementNode elementNode) {
        if (tree != null) {
            FHIRPathNode versionNode = tree.getSibling(elementNode, "version");
            if (versionNode != null && FHIRPathType.FHIR_STRING.equals(versionNode.type())) {
                return versionNode.asElementNode().element().as(FHIR_STRING);
            }
        }
        return null;
    }

    /**
     * Get the String-typed sibling of the given element node with name "display".
     *
     * @param tree
     *     the tree
     * @param elementNode
     *     the element node
     * @return
     *     the String-typed sibling of the given element node with name "display", or null if no such sibling exists
     */
    public static com.ibm.fhir.model.type.String getDisplay(FHIRPathTree tree, FHIRPathElementNode elementNode) {
        if (tree != null) {
            FHIRPathNode displayNode = tree.getSibling(elementNode, "display");
            if (displayNode != null && FHIRPathType.FHIR_STRING.equals(displayNode.type())) {
                return displayNode.asElementNode().element().as(FHIR_STRING);
            }
        }
        return null;
    }
    
    /**
     * returns true if the node is a String or if FHIR element can be automatically converted to System.String 
     * https://www.hl7.org/fhir/fhirpath.html#types
     * @param node
     * @return boolean
     */
    public static boolean isStringSubType(FHIRPathNode node) {
        return node.type() == FHIRPathType.FHIR_STRING 
                || node.type() == FHIRPathType.FHIR_URI 
                || node.type() == FHIRPathType.FHIR_CODE 
                || node.type() == FHIRPathType.FHIR_OID 
                || node.type() == FHIRPathType.FHIR_ID  // included for consistency with spec
                || node.type() == FHIRPathType.FHIR_UUID 
                || node.type() == FHIRPathType.FHIR_MARKDOWN 
                || node.type() == FHIRPathType.FHIR_BASE64BINARY;
    }
}