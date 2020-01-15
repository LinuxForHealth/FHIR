/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.util.type;

import static com.ibm.fhir.persistence.jdbc.JDBCConstants.AND;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.BIND_VAR;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.DOT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.EQ;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.GT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.GTE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LEFT_PAREN;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LT;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.LTE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.NE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.NUMBER_VALUE;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.OR;
import static com.ibm.fhir.persistence.jdbc.JDBCConstants.RIGHT_PAREN;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;

import com.ibm.fhir.model.annotation.Choice;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.path.FHIRPathLexer;
import com.ibm.fhir.path.FHIRPathParser;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.util.JDBCQueryBuilder;
import com.ibm.fhir.search.SearchConstants.Prefix;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.parameters.QueryParameterValue;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * Number Parameter Behavior Util encapsulates the logic specific to Prefix
 * treatment of the Search queries related to numbers, and the implied ranges
 * therein.
 */
public class NumberParmBehaviorUtil {
    private static final Logger log = java.util.logging.Logger.getLogger(NumberParmBehaviorUtil.class.getName());

    protected static final BigDecimal FACTOR = new BigDecimal(".1");

    private static final List<String> SKIPPED_TOKENS = Arrays.asList(".", "<EOF>");
    private static final List<String> INTEGER_TYPE = Arrays.asList("Integer", "UnsignedInt", "PositiveInt");

    private NumberParmBehaviorUtil() {
        // No operation
    }

    public static void executeBehavior(StringBuilder whereClauseSegment, QueryParameter queryParm,
            List<Object> bindVariables, Class<?> resourceType, String tableAlias, JDBCQueryBuilder queryBuilder)
            throws FHIRPersistenceException {
        // Start the Clause 
        // Query: AND (
        whereClauseSegment.append(AND).append(LEFT_PAREN);

        // Process each parameter value in the query parameter
        boolean parmValueProcessed = false;
        Set<String> seen = new HashSet<>();
        for (QueryParameterValue value : queryParm.getValues()) {

            Prefix prefix = value.getPrefix();

            // Default to EQ
            if (prefix == null) {
                prefix = Prefix.EQ;
            }
            BigDecimal originalNumber = value.getValueNumber();

            // seen is used to optimize against a repeated value passed in. 
            // the hash must use the prefix and original number. 
            String hash = prefix.value() + originalNumber.toPlainString();
            if (!seen.contains(hash)) {
                seen.add(hash);
                // Check for valid conditions and return if it needs to be treated as an INTEGER:
                boolean isInteger = checkIntegerSearchWithSaEb(prefix, resourceType, queryParm, originalNumber);

                // If multiple values are present, we need to OR them together.
                if (parmValueProcessed) {
                    // ) OR (
                    whereClauseSegment.append(RIGHT_PAREN)
                            .append(OR)
                            .append(LEFT_PAREN);
                } else {
                    // Signal to the downstream to treat any subsequent value as an OR condition 
                    parmValueProcessed = true;
                }

                // Branch behavior based on the type of prefix. 
                // EQ/NE/AP have special handling
                switch (prefix) {
                case EQ:
                    // eq - equals is a specific range search.
                    if (isInteger) {
                        bindVariables.add(originalNumber);
                        whereClauseSegment.append(tableAlias).append(DOT).append(NUMBER_VALUE);
                        whereClauseSegment.append(EQ).append(BIND_VAR);
                    } else {
                        // Not an Integer search. 
                        // Bounds are based on precision.
                        bindVariables.add(generateLowerBound(originalNumber));
                        bindVariables.add(generateUpperBound(originalNumber));

                        // <CODE>BASIC_NUMBER_VALUE > ? AND BASIC_NUMBER_VALUE <= ?</CODE> 
                        whereClauseSegment.append(tableAlias).append(DOT).append(NUMBER_VALUE);
                        whereClauseSegment.append(GT).append(BIND_VAR).append(AND);
                        whereClauseSegment.append(tableAlias).append(DOT).append(NUMBER_VALUE);
                        whereClauseSegment.append(LTE).append(BIND_VAR);
                    }

                    break;
                case NE:
                    // ne - not equals is a specific not in range search.
                    if (isInteger) {
                        bindVariables.add(originalNumber);
                        whereClauseSegment.append(tableAlias).append(DOT).append(NUMBER_VALUE);
                        whereClauseSegment.append(NE).append(BIND_VAR);
                    } else {
                        // Bounds are based on precision.

                        bindVariables.add(generateLowerBound(originalNumber));
                        bindVariables.add(generateUpperBound(originalNumber));

                        // <CODE>BASIC_NUMBER_VALUE <= ? OR BASIC_NUMBER_VALUE > ?</CODE> 
                        whereClauseSegment.append(tableAlias).append(DOT).append(NUMBER_VALUE);
                        whereClauseSegment.append(LTE).append(BIND_VAR);
                        whereClauseSegment.append(OR);
                        whereClauseSegment.append(tableAlias).append(DOT).append(NUMBER_VALUE);
                        whereClauseSegment.append(GT).append(BIND_VAR);
                    }
                    break;
                case AP:
                    // ap - approximate
                    // -10% of the Lower Bound
                    BigDecimal lowerBound = generateLowerBound(originalNumber);
                    BigDecimal upperBound = generateUpperBound(originalNumber);
                    BigDecimal factor = originalNumber.multiply(FACTOR);
                    bindVariables.add(lowerBound.subtract(factor));
                    // +10% of the UPPER Bound
                    bindVariables.add(upperBound.add(factor));

                    // <CODE>BASIC_NUMBER_VALUE >= ? AND BASIC_NUMBER_VALUE <= ?</CODE> 
                    whereClauseSegment.append(tableAlias).append(DOT).append(NUMBER_VALUE);
                    whereClauseSegment.append(GTE).append(BIND_VAR);
                    whereClauseSegment.append(AND);
                    whereClauseSegment.append(tableAlias).append(DOT).append(NUMBER_VALUE);
                    whereClauseSegment.append(LTE).append(BIND_VAR);
                    break;
                default:
                    // gt, lt, ge, le, sa, eb
                    // take the default behavior.
                    // Build this piece: p1.value_string {operator} search-attribute-value
                    String operator = EQ;
                    switch (prefix) {
                    case EB:
                        // EB - Ends Before
                        operator = LT;
                        break;
                    case SA:
                        // SA - Starts After
                        operator = GT;
                        break;
                    case GE:
                        // GE - Greater Than Equal
                        operator = GTE;
                        break;
                    case GT:
                        // GT - Greater Than
                        operator = GT;
                        break;
                    case LE:
                        // LE - Less Than Equal
                        operator = LTE;
                        break;
                    case LT:
                    default:
                        // LT - Less Than
                        operator = LT;
                        break;
                    }

                    whereClauseSegment.append(tableAlias).append(DOT).append(NUMBER_VALUE).append(operator)
                            .append(BIND_VAR);
                    bindVariables.add(originalNumber);
                    break;
                }
            }
        }

        // End the Clause started above, and closes the parameter expression. 
        // Query: )) 
        whereClauseSegment.append(RIGHT_PAREN).append(RIGHT_PAREN);
    }

    public static BigDecimal generateLowerBound(BigDecimal original) {

        int factor = calculateSignificantFigures(original);
        if (factor < 0) {
            factor = Math.abs(factor) - 1;
        } else {
            factor = original.scale() + 1;

            // Mutate it to the ORIGINAL string
            String t = "" + original;
            int loc = t.indexOf('E');
            if (loc > -1) {
                factor = -1 * (loc - 2);
            }
        }

        BigDecimal scaleFactor = new BigDecimal("5e" + -1 * factor);
        return original.subtract(scaleFactor);
    }

    public static BigDecimal generateUpperBound(BigDecimal original) {
        int factor = calculateSignificantFigures(original);
        if (factor < 0) {
            factor = Math.abs(factor) - 1;
        } else {
            factor = original.scale() + 1;

            // Mutate it to the ORIGINAL string
            String t = "" + original;
            int loc = t.indexOf('E');
            if (loc > -1) {
                factor = -1 * (loc - 2);
            }
        }
        BigDecimal scaleFactor = new BigDecimal("5e" + -1 * factor);
        return original.add(scaleFactor);
    }

    /**
     * calculates the significant figures
     * based on
     * <a href="https://en.wikipedia.org/wiki/Significant_figures">Significant
     * Figures</a>
     * 
     * @param original
     * @return
     */
    public static int calculateSignificantFigures(BigDecimal original) {
        int count = original.precision();
        if (original.scale() <= 0) {
            // Common pattern is to strip zeros... <code>.stripTrailingZeros()</code> 
            // We don't per the pattern in FHIR. 
            count += original.scale();
        }
        return count;
    }

    /**
     * per the specification we DO NOT process EB/SA integers.
     * <a href="https://hl7.org/fhir/search.html#prefix">FHIR Specification: Search
     * - Prefixes</a>
     * <code> 
     * sa (starts-after) and eb (ends-before) are not used with integer values but are used for decimals. 
     * </code>
     * 
     * @param prefix
     * @param resourceType
     * @param queryParm
     * @param originalNumber
     * @return boolean indicating that an integer search is being run.
     * @throws FHIRPersistenceException
     */
    public static boolean checkIntegerSearchWithSaEb(Prefix prefix, Class<?> resourceType, QueryParameter queryParm,
            BigDecimal originalNumber)
            throws FHIRPersistenceException {
        boolean isIntegerSearch = isIntegerSearch(resourceType, queryParm);

        if (isIntegerSearch) {
            if (prefix == Prefix.EB || prefix == Prefix.SA) {
                throw new FHIRPersistenceException(
                        "Search prefixes '" + Prefix.EB.value() + "' and '" + Prefix.SA.value()
                                + "' are not supported for integer searches.");
            } else {
                /*
                 * Per Specification: <br>
                 * When a number search is used against a resource element that stores a simple
                 * integer (e.g. ImmunizationRecommendation.recommendation.doseNumber), and the
                 * search parameter is not expressed using the exponential forms, and does not
                 * include any non-zero digits after a decimal point, the significance issues
                 * cancel out and searching is based on exact matches. Note that if there are
                 * non-zero digits after a decimal point, there cannot be any matches
                 */

                // Conditions:
                // We know the target is an integer.
                // if integer and ! exponential form ('E') and no non-zero
                // if indexOf 'E' and indexOf '.' are zero... then it's a specific equals search

                // we need to mutate into a string to test the conditions
                String num = "" + originalNumber;
                if (num.indexOf('E') > -1 || num.indexOf('.') > -1) {
                    isIntegerSearch = false;
                }
            }
        }

        return isIntegerSearch;
    }

    /**
     * the code navigates from a resourcetype down the hierarchy of the object model and the path of the
     * search parameter code to determine if the queried parameter is an integer.
     * 
     * @param resourceType
     * @param queryParm
     * @return
     */
    public static boolean isIntegerSearch(Class<?> resourceType, QueryParameter queryParm) {
        boolean result = false;
        try {
            String code = queryParm.getCode();
            SearchParameter searchParameter = SearchUtil.getSearchParameter(resourceType, code);

            // Only process if the expression exists, for instance a DomainResource would not. 
            if (searchParameter != null && searchParameter.getExpression() != null) {
                result =
                        processTheExpressionStringToComponents(resourceType,
                                searchParameter.getExpression().getValue());
            }
        } catch (Exception e) {
            log.warning("Exception retrieving the search parameter " + e);
            e.printStackTrace();
        }
        return result;
    }

    protected static boolean processTheExpressionStringToComponents(Class<?> resourceType, String expressionsString) {
        boolean result = false;
        String[] exprs = expressionsString.split("\\|");
        for (String expr : exprs) {
            FHIRPathLexer lexer = new FHIRPathLexer(CharStreams.fromString(expr));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            FHIRPathParser parser = new FHIRPathParser(tokens);
            // An intentional call to generate an expression, which populates the tokens in the CommonTokenStream            
            parser.expression();

            boolean isAs = false;
            boolean isWhere = false;
            int count = 0;
            String cast = "";
            List<String> fields = new ArrayList<>();
            for (Token token : tokens.getTokens()) {
                String tokenStr = token.getText();
                if (!SKIPPED_TOKENS.contains(tokenStr) && !tokenStr.trim().isEmpty()) {
                    if ("as".equals(tokenStr)) {
                        isAs = true;
                    } else if ("where".equals(tokenStr)) {
                        isWhere = true;
                    } else if ("(".equals(tokenStr)) {
                        count++;
                    } else if (")".equals(tokenStr)) {
                        count--;
                    } else if (isAs && count > 0) {
                        cast = tokenStr;
                        isAs = false;
                    } else if (isWhere && count > 0) {
                        isWhere = false;
                    } else if (count == 0) {
                        fields.add(tokenStr);
                    }
                }
            }

            // If it's cast or the path needs to be checked.
            if (INTEGER_TYPE.contains(cast) || (cast.isEmpty() && followPathToCheckInteger(resourceType, fields))) {
                return true;
            }
        }
        return result;
    }

    // Navigates down the path using the fields from the Expression.
    protected static boolean followPathToCheckInteger(Class<?> resourceType, List<String> fields) {
        String simpleName = resourceType.getSimpleName();

        // Only if the context is the class, we remove from the list. 
        if (!fields.isEmpty() && fields.get(0).equals(simpleName)) {
            fields.remove(simpleName);
        }

        Class<?> clz = resourceType;
        for (String fieldStr : fields) {
            if (log.isLoggable(Level.FINE)) {
                log.fine(" - " + fieldStr + " " + fields + " " + clz.getSimpleName());
            }

            Class<?> found = processField(clz, fieldStr);
            if (found == null) {
                clz = clz.getSuperclass();
                clz = processField(clz, fieldStr);
            } else {
                clz = found;
            }
        }

        // Checks to see if is of Integer type 
        return INTEGER_TYPE.contains(clz.getSimpleName());
    }

    public static Class<?> processField(Class<?> clz, String fieldStr) {
        Class<?> found = null;
        for (Field f : clz.getDeclaredFields()) {
            // Logs the field tests
            if (log.isLoggable(Level.FINE)) {
                log.fine("\t" + f.getName() + " -<>- " + f.getName());
            }

            if (f.getName().equals(fieldStr.trim())) {

                java.lang.reflect.Type genericType = f.getGenericType();
                if (genericType instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) genericType;
                    found = (Class<?>) parameterizedType.getActualTypeArguments()[0];
                } else {
                    try {
                        found = Class.forName(genericType.getTypeName());
                    } catch (ClassNotFoundException e) {
                        return null;
                    }
                }

                // Check for a choice type. 
                for (Annotation anno : f.getAnnotationsByType(Choice.class)) {
                    Choice choice = (Choice) anno;
                    Class<?>[] clzs = choice.value();
                    for (Class<?> t : clzs) {
                        if (t.getSimpleName().contains("Int")) {
                            found = t;
                            break;
                        }
                    }
                }

                break;
            }
        }

        // Logs the field tests
        if (log.isLoggable(Level.FINE)) {
            log.fine(" Discovered the simple class -> " + found);
        }

        return found;
    }
}