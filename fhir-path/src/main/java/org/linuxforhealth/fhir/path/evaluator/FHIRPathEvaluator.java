/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.path.evaluator;

import static org.linuxforhealth.fhir.cache.CacheKey.key;
import static org.linuxforhealth.fhir.cache.util.CacheSupport.createCacheAsMap;
import static org.linuxforhealth.fhir.path.FHIRPathDateTimeValue.dateTimeValue;
import static org.linuxforhealth.fhir.path.FHIRPathDateValue.dateValue;
import static org.linuxforhealth.fhir.path.FHIRPathDecimalValue.decimalValue;
import static org.linuxforhealth.fhir.path.FHIRPathIntegerValue.integerValue;
import static org.linuxforhealth.fhir.path.FHIRPathStringValue.EMPTY_STRING;
import static org.linuxforhealth.fhir.path.FHIRPathStringValue.stringValue;
import static org.linuxforhealth.fhir.path.FHIRPathTimeValue.timeValue;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.empty;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.evaluatesToBoolean;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.evaluatesToTrue;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.getInteger;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.getNumberValue;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.getQuantityNode;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.getQuantityValue;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.getSingleton;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.getString;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.getStringValue;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.getSystemValue;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.getTemporalValue;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.hasNumberValue;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.hasQuantityValue;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.hasStringValue;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.hasSystemValue;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.hasTemporalValue;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.isFalse;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.isQuantityNode;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.isSingleton;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.isTypeCompatible;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.singleton;
import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.unescape;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;

import org.linuxforhealth.fhir.cache.CacheKey;
import org.linuxforhealth.fhir.model.annotation.Constraint;
import org.linuxforhealth.fhir.model.resource.OperationOutcome.Issue;
import org.linuxforhealth.fhir.model.resource.Resource;
import org.linuxforhealth.fhir.model.type.Element;
import org.linuxforhealth.fhir.model.visitor.Visitable;
import org.linuxforhealth.fhir.path.FHIRPathBaseVisitor;
import org.linuxforhealth.fhir.path.FHIRPathBooleanValue;
import org.linuxforhealth.fhir.path.FHIRPathNode;
import org.linuxforhealth.fhir.path.FHIRPathParser;
import org.linuxforhealth.fhir.path.FHIRPathParser.ExpressionContext;
import org.linuxforhealth.fhir.path.FHIRPathParser.ParamListContext;
import org.linuxforhealth.fhir.path.FHIRPathQuantityNode;
import org.linuxforhealth.fhir.path.FHIRPathQuantityValue;
import org.linuxforhealth.fhir.path.FHIRPathStringValue;
import org.linuxforhealth.fhir.path.FHIRPathSystemValue;
import org.linuxforhealth.fhir.path.FHIRPathTemporalValue;
import org.linuxforhealth.fhir.path.FHIRPathTermServiceNode;
import org.linuxforhealth.fhir.path.FHIRPathTree;
import org.linuxforhealth.fhir.path.FHIRPathType;
import org.linuxforhealth.fhir.path.exception.FHIRPathException;
import org.linuxforhealth.fhir.path.function.FHIRPathFunction;
import org.linuxforhealth.fhir.path.util.FHIRPathUtil;

import net.jcip.annotations.NotThreadSafe;

/**
 * A FHIRPath evaluation engine that implements the FHIRPath 2.0.0 <a href="http://hl7.org/fhirpath/N1/">specification</a>
 */
@NotThreadSafe
public class FHIRPathEvaluator {
    private static final Logger log = Logger.getLogger(FHIRPathEvaluator.class.getName());

    public static final Collection<FHIRPathNode> SINGLETON_TRUE = singleton(FHIRPathBooleanValue.TRUE);
    public static final Collection<FHIRPathNode> SINGLETON_FALSE = singleton(FHIRPathBooleanValue.FALSE);

    private static final int EXPRESSION_CONTEXT_CACHE_MAX_ENTRIES = 512;
    private static final Map<String, ExpressionContext> EXPRESSION_CONTEXT_CACHE = createCacheAsMap(EXPRESSION_CONTEXT_CACHE_MAX_ENTRIES);

    private final EvaluatingVisitor visitor = new EvaluatingVisitor();

    private FHIRPathEvaluator() { }

    /**
     * Get the EvaluationContext associated with this FHIRPathEvaluator
     *
     * @return
     *     get the EvaluationContext associated with this FHIRPathEvaluator
     */
    public EvaluationContext getEvaluationContext() {
        return visitor.getEvaluationContext();
    }

    /**
     * Evaluate a FHIRPath expression
     *
     * @param expr
     *     the FHIRPath expression to evaluate
     * @return
     *     the result of evaluation as a non-null, potentially empty collection of FHIRPath nodes
     * @throws NullPointerException
     *     if any of the parameters are null
     * @throws FHIRPathException
     *     if an exception occurs during evaluation
     */
    public Collection<FHIRPathNode> evaluate(String expr) throws FHIRPathException {
        return evaluate(new EvaluationContext(), expr, empty());
    }

    /**
     * Evaluate a FHIRPath expression against a {@link Resource} or {@link Element}
     *
     * @param resourceOrElement
     *     the {@link Resource} or {@link Element}
     * @param expr
     *     the FHIRPath expression to evaluate
     * @return
     *     the result of evaluation as a non-null, potentially empty collection of FHIRPath nodes
     * @throws NullPointerException
     *     if any of the parameters are null
     * @throws FHIRPathException
     *     if an exception occurs during evaluation
     */
    public Collection<FHIRPathNode> evaluate(Visitable resourceOrElement, String expr) throws FHIRPathException {
        Objects.requireNonNull(resourceOrElement, "resourceOrElement");
        if (resourceOrElement instanceof Resource) {
            return evaluate((Resource) resourceOrElement, expr);
        }
        if (resourceOrElement instanceof Element) {
            return evaluate((Element) resourceOrElement, expr);
        }
        throw new AssertionError();
    }

    /**
     * Evaluate a FHIRPath expression against a {@link Resource}
     *
     * @param resource
     *     the resource
     * @param expr
     *     the FHIRPath expression to evaluate
     * @return
     *     the result of evaluation as a non-null, potentially empty collection of FHIRPath nodes
     * @throws NullPointerException
     *     if any of the parameters are null
     * @throws FHIRPathException
     *     if an exception occurs during evaluation
     */
    public Collection<FHIRPathNode> evaluate(Resource resource, String expr) throws FHIRPathException {
        return evaluate(new EvaluationContext(resource), expr);
    }

    /**
     * Evaluate a FHIRPath expression against an {@link Element}
     *
     * @param element
     *     the element
     * @param expr
     *     the FHIRPath expression to evaluate
     * @return
     *     the result of evaluation as a non-null, potentially empty collection of FHIRPath nodes
     * @throws NullPointerException
     *     if any of the parameters are null
     * @throws FHIRPathException
     *     if an exception occurs during evaluation
     */
    public Collection<FHIRPathNode> evaluate(Element element, String expr) throws FHIRPathException {
        return evaluate(new EvaluationContext(element), expr);
    }

    /**
     * Evaluate a FHIRPath expression using an existing evaluation context
     *
     * @param evaluationContext
     *     the evaluation context
     * @param expr
     *     the FHIRPath expression to evaluate
     * @return
     *     the result of evaluation as a non-null, potentially empty collection of FHIRPath nodes
     * @throws NullPointerException
     *     if any of the parameters are null
     * @throws FHIRPathException
     *     if an exception occurs during evaluation
     */
    public Collection<FHIRPathNode> evaluate(EvaluationContext evaluationContext, String expr) throws FHIRPathException {
        return evaluate(evaluationContext, expr, evaluationContext.getTree().getRoot());
    }

    /**
     * Evaluate a FHIRPath expression using an existing evaluation context against a FHIRPath node
     *
     * @param evaluationContext
     *     the evaluation context
     * @param expr
     *     the FHIRPath expression to evaluate
     * @param node
     *     the FHIRPath node
     * @return
     *     the result of evaluation as a non-null, potentially empty collection of FHIRPath nodes
     * @throws NullPointerException
     *     if any of the parameters are null
     * @throws FHIRPathException
     *     if an exception occurs during evaluation
     */
    public Collection<FHIRPathNode> evaluate(EvaluationContext evaluationContext, String expr, FHIRPathNode node) throws FHIRPathException {
        return evaluate(evaluationContext, expr, singleton(node));
    }

    /**
     * Evaluate a FHIRPathExpression using an existing EvaluationContext against a collection of FHIRPath nodes
     *
     * @param evaluationContext
     *     the evaluation context
     * @param expr
     *     the FHIRPath expression to evaluate
     * @param initialContext
     *     the initial context as a non-null, potentially empty collection of FHIRPath nodes
     * @return
     *     the result of evaluation as a collection of FHIRPath nodes
     * @throws NullPointerException
     *     if any of the parameters are null
     * @throws FHIRPathException
     *     if an exception occurs during evaluation
     */
    public Collection<FHIRPathNode> evaluate(EvaluationContext evaluationContext, String expr, Collection<FHIRPathNode> initialContext) throws FHIRPathException {
        Objects.requireNonNull(evaluationContext);
        Objects.requireNonNull(initialContext);
        try {
            evaluationContext.setExternalConstant("context", initialContext);
            setDateTimeConstants(evaluationContext);
            return visitor.evaluate(evaluationContext, getExpressionContext(expr), initialContext);
        } catch (Exception e) {
            throw new FHIRPathException("An error occurred while evaluating expression: " + expr, e);
        }
    }

    private void setDateTimeConstants(EvaluationContext evaluationContext) {
        ZonedDateTime now = ZonedDateTime.now();
        evaluationContext.setExternalConstant("now", singleton(dateTimeValue(now)));
        evaluationContext.setExternalConstant("today", singleton(dateValue(LocalDate.from(now))));
        evaluationContext.setExternalConstant("timeOfDay", singleton(timeValue(LocalTime.from(now))));
    }

    private static ExpressionContext getExpressionContext(String expr) {
        return EXPRESSION_CONTEXT_CACHE.computeIfAbsent(Objects.requireNonNull(expr), FHIRPathUtil::compile);
    }

    /**
     * Static factory method for creating FHIRPathEvaluator instances
     *
     * @return
     *     a new FHIRPathEvaluator instance
     */
    public static FHIRPathEvaluator evaluator() {
        return new FHIRPathEvaluator();
    }

    public static class EvaluatingVisitor extends FHIRPathBaseVisitor<Collection<FHIRPathNode>> {
        private static final String SYSTEM_NAMESPACE = "System";

        private static final int IDENTIFIER_CACHE_MAX_ENTRIES = 2048;
        private static final int LITERAL_CACHE_MAX_ENTRIES = 128;

        private static final Map<String, Collection<FHIRPathNode>> IDENTIFIER_CACHE = createCacheAsMap(IDENTIFIER_CACHE_MAX_ENTRIES);
        private static final Map<String, Collection<FHIRPathNode>> LITERAL_CACHE = createCacheAsMap(LITERAL_CACHE_MAX_ENTRIES);

        private final Stack<Collection<FHIRPathNode>> contextStack = new Stack<>();

        private EvaluationContext evaluationContext;
        private int indentLevel = 0;

        private EvaluatingVisitor() { }

        private Collection<FHIRPathNode> evaluate(EvaluationContext evaluationContext, ExpressionContext expressionContext, Collection<FHIRPathNode> initialContext) {
            reset();
            this.evaluationContext = evaluationContext;
            contextStack.push(initialContext);
            Collection<FHIRPathNode> result = expressionContext.accept(this);
            contextStack.pop();
            return Collections.unmodifiableCollection(result);
        }

        private EvaluationContext getEvaluationContext() {
            return evaluationContext;
        }

        private void reset() {
            contextStack.clear();
            indentLevel = 0;
        }

        /**
         * https://hl7.org/fhirpath/N1/#allcriteria-expression-boolean
         */
        private Collection<FHIRPathNode> all(List<ExpressionContext> arguments) {
            if (arguments.size() != 1) {
                throw unexpectedNumberOfArguments(arguments.size(), "all");
            }
            ExpressionContext criteria = arguments.get(0);
            for (FHIRPathNode node : getCurrentContext()) {
                pushContext(singleton(node));
                Collection<FHIRPathNode> result = visit(criteria);
                if (evaluatesToBoolean(result) && isFalse(result)) {
                    popContext();
                    return SINGLETON_FALSE;
                }
                popContext();
            }
            return SINGLETON_TRUE;
        }

        /**
         * https://hl7.org/fhirpath/N1/#as-type-specifier
         * https://hl7.org/fhirpath/N1/#astype-type-specifier
         */
        private Collection<FHIRPathNode> as(List<ExpressionContext> arguments) {
            if (arguments.size() != 1) {
                throw unexpectedNumberOfArguments(arguments.size(), "as");
            }
            Collection<FHIRPathNode> result = new ArrayList<>();
            ExpressionContext typeName = arguments.get(0);
            String identifier = typeName.getText().replace("`", "");
            FHIRPathType type = FHIRPathType.from(identifier);
            if (type == null) {
                throw new IllegalArgumentException(String.format("Argument '%s' cannot be resolved to a valid type identifier", identifier));
            }
            for (FHIRPathNode node : getCurrentContext()) {
                FHIRPathType nodeType = node.type();
                if (SYSTEM_NAMESPACE.equals(type.namespace()) && node.hasValue()) {
                    nodeType = node.getValue().type();
                }
                if (type.isAssignableFrom(nodeType)) {
                    result.add(node);
                }
            }
            return result;
        }

        private Set<String> closure(FHIRPathType type) {
            if (SYSTEM_NAMESPACE.equals(type.namespace())) {
                return Collections.emptySet();
            }
            // compute type name closure
            Set<String> closure = new HashSet<>();
            while (!FHIRPathType.FHIR_ANY.equals(type)) {
                closure.add(type.getName());
                type = type.baseType();
            }
            return closure;
        }

        /**
         * https://hl7.org/fhirpath/N1/#existscriteria-expression-boolean
         */
        private Collection<FHIRPathNode> exists(List<ExpressionContext> arguments) {
            if (arguments.size() < 0 || arguments.size() > 1) {
                throw unexpectedNumberOfArguments(arguments.size(), "exists");
            }
            if (arguments.isEmpty()) {
                return !getCurrentContext().isEmpty() ? SINGLETON_TRUE : SINGLETON_FALSE;
            }

            for (FHIRPathNode node : getCurrentContext()) {
                pushContext(singleton(node));
                if (evaluatesToTrue(visit(arguments.get(0)))) {
                    popContext();
                    return SINGLETON_TRUE;
                }
                popContext();
            }

            return SINGLETON_FALSE;
        }

        private Collection<FHIRPathNode> getCurrentContext() {
            if (!contextStack.isEmpty()) {
                return contextStack.peek();
            }
            return empty();
        }

        /**
         * https://hl7.org/fhirpath/N1/#iifcriterion-expression-true-result-collection-otherwise-result-collection-collection
         */
        private Collection<FHIRPathNode> iif(List<ExpressionContext> arguments) {
            if (arguments.size() < 2 || arguments.size() > 3) {
                throw unexpectedNumberOfArguments(arguments.size(), "iif");
            }
            Collection<FHIRPathNode> criterion = visit(arguments.get(0));
            if (!evaluatesToBoolean(criterion) && !criterion.isEmpty()) {
                throw new IllegalArgumentException("'iff' function criterion must evaluate to a boolean or empty");
            }
            // criterion
            if (evaluatesToTrue(criterion)) {
                // true-result
                return visit(arguments.get(1));
            } else if (arguments.size() == 3) {
                // otherwise-result (optional)
                return visit(arguments.get(2));
            }
            return empty();
        }

        /**
         * https://hl7.org/fhirpath/N1/#is-type-specifier
         * https://hl7.org/fhirpath/N1/#istype-type-specifier
         */
        private Collection<FHIRPathNode> is(Collection<ExpressionContext> arguments) {
            if (arguments.size() != 1) {
                throw unexpectedNumberOfArguments(arguments.size(), "is");
            }

            Collection<FHIRPathNode> currentContext = getCurrentContext();
            if (currentContext.isEmpty()) {
                return SINGLETON_FALSE;
            } else if (currentContext.size() > 1) {
                throw new IllegalArgumentException(String.format("Input collection has %d items, but only 1 is allowed", currentContext.size()));
            }

            ExpressionContext typeName = arguments.iterator().next();
            String identifier = typeName.getText().replace("`", "");
            FHIRPathType type = FHIRPathType.from(identifier);
            if (type == null) {
                return SINGLETON_FALSE;
            }
            FHIRPathNode node = getSingleton(currentContext);
            return type.isAssignableFrom(node.type()) ? SINGLETON_TRUE : SINGLETON_FALSE;
        }

        /**
         * https://hl7.org/fhirpath/N1/#oftypetype-type-specifier-collection
         */
        private Collection<FHIRPathNode> ofType(List<ExpressionContext> arguments) {
            if (arguments.size() != 1) {
                throw unexpectedNumberOfArguments(arguments.size(), "ofType");
            }
            Collection<FHIRPathNode> result = new ArrayList<>();
            ExpressionContext typeName = arguments.get(0);
            String identifier = typeName.getText().replace("`", "");
            FHIRPathType type = FHIRPathType.from(identifier);
            if (type == null) {
                throw new IllegalArgumentException(String.format("Argument '%s' cannot be resolved to a valid type identifier", identifier));
            }
            for (FHIRPathNode node : getCurrentContext()) {
                FHIRPathType nodeType = node.type();
                if (SYSTEM_NAMESPACE.equals(type.namespace()) && node.hasValue()) {
                    nodeType = node.getValue().type();
                }
                if (type.isAssignableFrom(nodeType)) {
                    result.add(node);
                }
            }
            return result;
        }

        private Collection<FHIRPathNode> popContext() {
            if (!contextStack.isEmpty()) {
                return contextStack.pop();
            }
            return null;
        }

        private void pushContext(Collection<FHIRPathNode> context) {
            if (context != null) {
                contextStack.push(context);
            }
        }

        /**
         * https://hl7.org/fhirpath/N1/#repeatprojection-expression-collection
         */
        private Collection<FHIRPathNode> repeat(List<ExpressionContext> arguments) {
            if (arguments.size() != 1) {
                throw unexpectedNumberOfArguments(arguments.size(), "repeat");
            }
            Collection<FHIRPathNode> result = new ArrayList<>();
            Collection<FHIRPathNode> selectedNodes = select(arguments);
            while (result.addAll(selectedNodes)) {
                pushContext(selectedNodes);
                selectedNodes = select(arguments);
                popContext();
            }
            return result;
        }

        /**
         * https://hl7.org/fhirpath/N1/#selectprojection-expression-collection
         */
        private Collection<FHIRPathNode> select(List<ExpressionContext> arguments) {
            if (arguments.size() != 1) {
                throw unexpectedNumberOfArguments(arguments.size(), "select");
            }
            ExpressionContext projection = arguments.get(0);
            Collection<FHIRPathNode> result = new ArrayList<>();
            for (FHIRPathNode node : getCurrentContext()) {
                pushContext(singleton(node));
                result.addAll(visit(projection));
                popContext();
            }
            return result;
        }

        /**
         * https://hl7.org/fhirpath/N1/#tracename-string-projection-expression-collection
         */
        private Collection<FHIRPathNode> trace(List<ExpressionContext> arguments) {
            if (arguments.size() < 1 || arguments.size() > 2) {
                throw unexpectedNumberOfArguments(arguments.size(), "trace");
            }
            String name = getString(visit(arguments.get(0)));
            Collection<FHIRPathNode> currentContext = getCurrentContext();
            Collection<FHIRPathNode> nodes = (arguments.size() == 1) ? currentContext : visit(arguments.get(1));
            if (!nodes.isEmpty()) {
                if (log.isLoggable(Level.FINER)) {
                    log.finer(name + ": " + nodes);
                }
            }
            return currentContext;
        }

        private IllegalArgumentException unexpectedNumberOfArguments(int arity, String functionName) {
            return new IllegalArgumentException(String.format("Unexpected number of arguments: %d for function: '%s'", arity, functionName));
        }

        /**
         * https://hl7.org/fhirpath/N1/#wherecriteria-expression-collection
         */
        private Collection<FHIRPathNode> where(List<ExpressionContext> arguments) {
            if (arguments.size() != 1) {
                throw unexpectedNumberOfArguments(arguments.size(), "where");
            }
            ExpressionContext criteria = arguments.get(0);
            Collection<FHIRPathNode> result = new ArrayList<>();
            for (FHIRPathNode node : getCurrentContext()) {
                pushContext(singleton(node));
                if (evaluatesToTrue(visit(criteria))) {
                    result.add(node);
                }
                popContext();
            }
            return result;
        }

        @Override
        public Collection<FHIRPathNode> visitIndexerExpression(FHIRPathParser.IndexerExpressionContext ctx) {
            beforeEvaluation(ctx);

            Collection<FHIRPathNode> result = empty();

            Collection<FHIRPathNode> nodes = visit(ctx.expression(0));

            List<?> list = (nodes instanceof List) ? (List<?>) nodes : new ArrayList<>(nodes);
            int index = getInteger(visit(ctx.expression(1)));

            if (index >= 0 && index < list.size()) {
                result = singleton((FHIRPathNode) list.get(index));
            }

            return afterEvaluation(ctx, result);
        }

        @Override
        public Collection<FHIRPathNode> visitPolarityExpression(FHIRPathParser.PolarityExpressionContext ctx) {
            beforeEvaluation(ctx);

            Collection<FHIRPathNode> nodes = visit(ctx.expression());

            if (!isSingleton(nodes)) {
                return afterEvaluation(ctx, empty());
            }

            Collection<FHIRPathNode> result = empty();

            FHIRPathSystemValue value = getSystemValue(nodes);
            String polarity = ctx.getChild(0).getText();

            if (value.isNumberValue()) {
                switch (polarity) {
                case "+":
                    result = singleton(value.asNumberValue().plus());
                    break;
                case "-":
                    result = singleton(value.asNumberValue().negate());
                    break;
                }
            }

            return afterEvaluation(ctx, result);
        }

        @Override
        public Collection<FHIRPathNode> visitAdditiveExpression(FHIRPathParser.AdditiveExpressionContext ctx) {
            beforeEvaluation(ctx);

            Collection<FHIRPathNode> left = visit(ctx.expression(0));
            Collection<FHIRPathNode> right = visit(ctx.expression(1));

            Collection<FHIRPathNode> result = empty();

            String operator = ctx.getChild(1).getText();

            if ((hasNumberValue(left) && hasNumberValue(right)) || (hasStringValue(left) && hasStringValue(right))) {
                if (hasNumberValue(left) && hasNumberValue(right)) {
                    switch (operator) {
                    case "+":
                        result = singleton(getNumberValue(left).add(getNumberValue(right)));
                        break;
                    case "-":
                        result = singleton(getNumberValue(left).asNumberValue().subtract(getNumberValue(right)));
                        break;
                    }
                } else if (hasStringValue(left) && hasStringValue(right)) {
                    if ("+".equals(operator) || "&".equals(operator)) {
                        // concatenation
                        result = singleton(getStringValue(left).concat(getStringValue(right)));
                    } else {
                        throw new IllegalArgumentException("Invalid argument(s) for '" + operator + "' operator");
                    }
                }
            } else if (((hasStringValue(left) && right.isEmpty()) || (left.isEmpty() && hasStringValue(right))) && ("+".equals(operator) || "&".equals(operator))) {
                if ("&".equals(operator)) {
                    // concatenation where an empty collection is treated as an empty string
                    if (hasStringValue(left) && right.isEmpty()) {
                        FHIRPathStringValue leftValue = getStringValue(left);
                        result = singleton(leftValue.asStringValue().concat(EMPTY_STRING));
                    } else if (left.isEmpty() && hasStringValue(right)) {
                        FHIRPathStringValue rightValue = getStringValue(right);
                        result = singleton(EMPTY_STRING.concat(rightValue.asStringValue()));
                    } else if (left.isEmpty() && right.isEmpty()) {
                        result = singleton(EMPTY_STRING);
                    }
                }
            } else if (hasQuantityValue(left) && hasQuantityValue(right)) {
                FHIRPathQuantityValue leftValue = getQuantityValue(left);
                FHIRPathQuantityValue rightValue = getQuantityValue(right);
                switch (operator) {
                case "+":
                    result = singleton(leftValue.add(rightValue));
                    break;
                case "-":
                    result = singleton(leftValue.subtract(rightValue));
                    break;
                }
            } else if ((hasTemporalValue(left) && hasQuantityValue(right)) ||
                    (hasQuantityValue(left) && hasTemporalValue(right))) {
                FHIRPathTemporalValue temporalValue = hasTemporalValue(left) ? getTemporalValue(left) : getTemporalValue(right);
                FHIRPathQuantityValue quantityValue = hasQuantityValue(left) ? getQuantityValue(left) : getQuantityValue(right);
                switch (operator) {
                case "+":
                    result = singleton(temporalValue.add(quantityValue));
                    break;
                case "-":
                    result = singleton(temporalValue.subtract(quantityValue));
                    break;
                }
            } else if (isQuantityNode(left) && isQuantityNode(right)) {
                FHIRPathQuantityNode leftNode = getQuantityNode(left);
                FHIRPathQuantityNode rightNode = getQuantityNode(right);
                switch(operator) {
                case "+":
                    result = singleton(leftNode.add(rightNode));
                    break;
                case "-":
                    result = singleton(leftNode.subtract(rightNode));
                    break;
                }
            } else if (!left.isEmpty() && !right.isEmpty()){
                throw new IllegalArgumentException("Invalid argument(s) for '" + operator + "' operator");
            }

            return afterEvaluation(ctx, result);
        }

        @Override
        public Collection<FHIRPathNode> visitMultiplicativeExpression(FHIRPathParser.MultiplicativeExpressionContext ctx) {
            beforeEvaluation(ctx);

            Collection<FHIRPathNode> left = visit(ctx.expression(0));
            Collection<FHIRPathNode> right = visit(ctx.expression(1));

            if (!hasSystemValue(left) || !hasSystemValue(right)) {
                return afterEvaluation(ctx, empty());
            }

            Collection<FHIRPathNode> result = empty();

            FHIRPathSystemValue leftValue = getSystemValue(left);
            FHIRPathSystemValue rightValue = getSystemValue(right);

            String operator = ctx.getChild(1).getText();

            if (leftValue.isNumberValue() && rightValue.isNumberValue()) {
                try {
                    switch (operator) {
                    case "*":
                        result = singleton(leftValue.asNumberValue().multiply(rightValue.asNumberValue()));
                        break;
                    case "/":
                        result = singleton(leftValue.asNumberValue().divide(rightValue.asNumberValue()));
                        break;
                    case "div":
                        result = singleton(leftValue.asNumberValue().div(rightValue.asNumberValue()));
                        break;
                    case "mod":
                        result = singleton(leftValue.asNumberValue().mod(rightValue.asNumberValue()));
                        break;
                    }
                } catch (ArithmeticException e) {
                    // TODO: log this
                }
            }

            return afterEvaluation(ctx, result);
        }

        @Override
        public Collection<FHIRPathNode> visitUnionExpression(FHIRPathParser.UnionExpressionContext ctx) {
            beforeEvaluation(ctx);

            Collection<FHIRPathNode> left = visit(ctx.expression(0));
            Collection<FHIRPathNode> right = visit(ctx.expression(1));

            Set<FHIRPathNode> union = new LinkedHashSet<>(left);
            union.addAll(right);

            return afterEvaluation(ctx, new ArrayList<>(union));
        }

        @Override
        public Collection<FHIRPathNode> visitOrExpression(FHIRPathParser.OrExpressionContext ctx) {
            beforeEvaluation(ctx);

            Collection<FHIRPathNode> result = empty();

            // evaluate left operand
            Collection<FHIRPathNode> left = visit(ctx.expression(0));

            String operator = ctx.getChild(1).getText();

            switch (operator) {
            case "or":
                // Returns false if both operands evaluate to false, true if either operand evaluates to true, and empty ({ }) otherwise:
                if (evaluatesToBoolean(left) && evaluatesToTrue(left)) {
                    // short-circuit evaluation
                    result = SINGLETON_TRUE;
                } else {
                    // evaluate right operand
                    Collection<FHIRPathNode> right = visit(ctx.expression(1));
                    if (evaluatesToBoolean(right) && evaluatesToTrue(right)) {
                        result = SINGLETON_TRUE;
                    } else if (evaluatesToBoolean(left) && evaluatesToBoolean(right) &&
                            isFalse(left) && isFalse(right)) {
                        result = SINGLETON_FALSE;
                    }
                }
                break;
            case "xor":
                // evaluate right operand
                Collection<FHIRPathNode> right = visit(ctx.expression(1));

                // Returns true if exactly one of the operands evaluates to true, false if either both operands evaluate to true or both operands evaluate to false, and the empty collection ({ }) otherwise:
                if (evaluatesToBoolean(left) && evaluatesToBoolean(right)) {
                    result = ((evaluatesToTrue(left) || evaluatesToTrue(right)) && !(evaluatesToTrue(left) && evaluatesToTrue(right))) ? SINGLETON_TRUE : SINGLETON_FALSE;
                }
                break;
            }

            return afterEvaluation(ctx, result);
        }

        @Override
        public Collection<FHIRPathNode> visitAndExpression(FHIRPathParser.AndExpressionContext ctx) {
            beforeEvaluation(ctx);

            Collection<FHIRPathNode> result = empty();

            // evaluate left operand
            Collection<FHIRPathNode> left = visit(ctx.expression(0));

            // Returns true if both operands evaluate to true, false if either operand evaluates to false, and the empty collection ({ }) otherwise.
            if (evaluatesToBoolean(left) && isFalse(left)) {
                // short-circuit evaluation
                result = SINGLETON_FALSE;
            } else {
                // evaluate right operand
                Collection<FHIRPathNode> right = visit(ctx.expression(1));
                if (evaluatesToBoolean(right) && isFalse(right)) {
                    result = SINGLETON_FALSE;
                } else if (evaluatesToBoolean(left) && evaluatesToBoolean(right) &&
                        evaluatesToTrue(left) && evaluatesToTrue(right)) {
                    result = SINGLETON_TRUE;
                }
            }

            return afterEvaluation(ctx, result);
        }

        @Override
        public Collection<FHIRPathNode> visitMembershipExpression(FHIRPathParser.MembershipExpressionContext ctx) {
            beforeEvaluation(ctx);

            Collection<FHIRPathNode> result = SINGLETON_FALSE;

            Collection<FHIRPathNode> left = visit(ctx.expression(0));
            Collection<FHIRPathNode> right = visit(ctx.expression(1));

            String operator = ctx.getChild(1).getText();

            switch (operator) {
            case "in":
                if (left.isEmpty()) {
                    result = empty();
                } else if (right.containsAll(left)) {
                    result = SINGLETON_TRUE;
                }
                break;
            case "contains":
                if (left.containsAll(right)) {
                    result = SINGLETON_TRUE;
                }
                break;
            }

            return afterEvaluation(ctx, result);
        }

        @Override
        public Collection<FHIRPathNode> visitInequalityExpression(FHIRPathParser.InequalityExpressionContext ctx) {
            beforeEvaluation(ctx);

            Collection<FHIRPathNode> left = visit(ctx.expression(0));
            Collection<FHIRPathNode> right = visit(ctx.expression(1));

            if (!isSingleton(left) || !isSingleton(right)) {
                return afterEvaluation(ctx, SINGLETON_FALSE);
            }

            Collection<FHIRPathNode> result = SINGLETON_FALSE;

            FHIRPathNode leftNode = getSingleton(left);
            FHIRPathNode rightNode = getSingleton(right);

            if (hasSystemValue(leftNode) && hasSystemValue(rightNode) &&
                    !isTypeCompatible(getSystemValue(leftNode), getSystemValue(rightNode))) {
                throw new IllegalArgumentException("Type: '" + leftNode.type().getName() + "' is not compatible with type: '" + rightNode.type().getName() + "'");
            }

            String operator = ctx.getChild(1).getText();

            if (leftNode.isComparableTo(rightNode)) {
                switch (operator) {
                case "<=":
                    if (leftNode.compareTo(rightNode) <= 0) {
                        result = SINGLETON_TRUE;
                    }
                    break;
                case "<":
                    if (leftNode.compareTo(rightNode) < 0) {
                        result = SINGLETON_TRUE;
                    }
                    break;
                case ">":
                    if (leftNode.compareTo(rightNode) > 0) {
                        result = SINGLETON_TRUE;
                    }
                    break;
                case ">=":
                    if (leftNode.compareTo(rightNode) >= 0) {
                        result = SINGLETON_TRUE;
                    }
                    break;
                }
            } else {
                result = empty();
            }

            return afterEvaluation(ctx, result);
        }

        @Override
        public Collection<FHIRPathNode> visitInvocationExpression(FHIRPathParser.InvocationExpressionContext ctx) {
            beforeEvaluation(ctx);

            pushContext(visit(ctx.expression()));
            Collection<FHIRPathNode> result = visit(ctx.invocation());
            popContext();

            return afterEvaluation(ctx, result);
        }

        @Override
        public Collection<FHIRPathNode> visitEqualityExpression(FHIRPathParser.EqualityExpressionContext ctx) {
            beforeEvaluation(ctx);

            Collection<FHIRPathNode> result = SINGLETON_FALSE;

            Collection<FHIRPathNode> left = visit(ctx.expression(0));
            Collection<FHIRPathNode> right = visit(ctx.expression(1));

            if (left.isEmpty() || right.isEmpty()) {
                return afterEvaluation(ctx, empty());
            }

            if (left.size() != right.size()) {
                return afterEvaluation(ctx, SINGLETON_FALSE);
            }

            // for quantity operands: Attempting to operate on quantities with invalid units will result in empty ({ }).
            // for temporal operands: If one input has a value for the precision and the other does not, the comparison stops and the result is empty ({ })
            if (!equalityOperandsAreValid(left, right)) {
                return afterEvaluation(ctx, empty());
            }

            String operator = ctx.getChild(1).getText();

            // TODO: "equals" and "equivalent" have different semantics
            switch (operator) {
            case "=":
            case "~":
                if (left.equals(right)) {
                    result = SINGLETON_TRUE;
                }
                break;
            case "!=":
            case "!~":
                if (!left.equals(right)) {
                    result = SINGLETON_TRUE;
                }
                break;
            }

            return afterEvaluation(ctx, result);
        }

        private boolean equalityOperandsAreValid(Collection<FHIRPathNode> left, Collection<FHIRPathNode> right) {
            if (left.size() != right.size()) {
                throw new IllegalArgumentException();
            }

            Iterator<FHIRPathNode> leftIterator = left.iterator();
            Iterator<FHIRPathNode> rightIterator = right.iterator();

            while (leftIterator.hasNext() && rightIterator.hasNext()) {
                FHIRPathNode leftNode = leftIterator.next();
                FHIRPathNode rightNode = rightIterator.next();

                if (hasTemporalValue(leftNode) && hasTemporalValue(rightNode) &&
                        !getTemporalValue(leftNode).precision().equals(getTemporalValue(rightNode).precision())) {
                    return false;
                }
            }

            return true;
        }

        @Override
        public Collection<FHIRPathNode> visitImpliesExpression(FHIRPathParser.ImpliesExpressionContext ctx) {
            beforeEvaluation(ctx);

            Collection<FHIRPathNode> result = empty();

            Collection<FHIRPathNode> left = visit(ctx.expression(0));
            Collection<FHIRPathNode> right = visit(ctx.expression(1));

            // If the left operand evaluates to true, this operator returns the boolean evaluation of the right operand. If the left operand evaluates to false, this operator returns true. Otherwise, this operator returns true if the right operand evaluates to true, and the empty collection ({ }) otherwise.
            if (evaluatesToBoolean(left) && evaluatesToBoolean(right)) {
                // !left || right
                result = (!evaluatesToTrue(left) || evaluatesToTrue(right)) ? SINGLETON_TRUE : SINGLETON_FALSE;
            } else if ((left.isEmpty() && evaluatesToBoolean(right) && evaluatesToTrue(right)) ||
                    (evaluatesToBoolean(left) && isFalse(left) && right.isEmpty())) {
                result = SINGLETON_TRUE;
            }

            return afterEvaluation(ctx, result);
        }

        @Override
        public Collection<FHIRPathNode> visitTermExpression(FHIRPathParser.TermExpressionContext ctx) {
            beforeEvaluation(ctx);
            Collection<FHIRPathNode> result = visitChildren(ctx);
            return afterEvaluation(ctx, result);
        }

        @Override
        public Collection<FHIRPathNode> visitTypeExpression(FHIRPathParser.TypeExpressionContext ctx) {
            beforeEvaluation(ctx);

            Collection<FHIRPathNode> nodes = visit(ctx.expression());

            String operator = ctx.getChild(1).getText();

            Collection<FHIRPathNode> result = "is".equals(operator) ? SINGLETON_FALSE : new ArrayList<>();

            String qualifiedIdentifier = getString(visit(ctx.typeSpecifier()));
            FHIRPathType type = FHIRPathType.from(qualifiedIdentifier);
            if (type == null) {
                throw new IllegalArgumentException(String.format("Argument '%s' cannot be resolved to a valid type identifier", qualifiedIdentifier));
            }

            switch (operator) {
            case "is":
                if (nodes.size() > 1) {
                    throw new IllegalArgumentException(String.format("Input collection has %d items, but only 1 is allowed", nodes.size()));
                } else if (!nodes.isEmpty()) {
                    FHIRPathNode node = getSingleton(nodes);
                    if (type.isAssignableFrom(node.type())) {
                        result = SINGLETON_TRUE;
                    }
                }
                break;
            case "as":
                for (FHIRPathNode node : nodes) {
                    FHIRPathType nodeType = node.type();
                    if (SYSTEM_NAMESPACE.equals(type.namespace()) && node.hasValue()) {
                        nodeType = node.getValue().type();
                    }
                    if (type.isAssignableFrom(nodeType)) {
                        result.add(node);
                    }
                }
                break;
            }

            return afterEvaluation(ctx, result);
        }

        @Override
        public Collection<FHIRPathNode> visitInvocationTerm(FHIRPathParser.InvocationTermContext ctx) {
            beforeEvaluation(ctx);
            Collection<FHIRPathNode> result = visitChildren(ctx);
            return afterEvaluation(ctx, result);
        }

        @Override
        public Collection<FHIRPathNode> visitLiteralTerm(FHIRPathParser.LiteralTermContext ctx) {
            beforeEvaluation(ctx);
            Collection<FHIRPathNode> result = LITERAL_CACHE.computeIfAbsent(ctx.getText(), t -> visitChildren(ctx));
            return afterEvaluation(ctx, result);
        }

        @Override
        public Collection<FHIRPathNode> visitExternalConstantTerm(FHIRPathParser.ExternalConstantTermContext ctx) {
            beforeEvaluation(ctx);
            Collection<FHIRPathNode> result = visitChildren(ctx);
            return afterEvaluation(ctx, result);
        }

        @Override
        public Collection<FHIRPathNode> visitParenthesizedTerm(FHIRPathParser.ParenthesizedTermContext ctx) {
            beforeEvaluation(ctx);
            Collection<FHIRPathNode> result = visit(ctx.expression());
            return afterEvaluation(ctx, result);
        }

        @Override
        public Collection<FHIRPathNode> visitNullLiteral(FHIRPathParser.NullLiteralContext ctx) {
            beforeEvaluation(ctx);
            return afterEvaluation(ctx, empty());
        }

        @Override
        public Collection<FHIRPathNode> visitBooleanLiteral(FHIRPathParser.BooleanLiteralContext ctx) {
            beforeEvaluation(ctx);
            Boolean _boolean = Boolean.valueOf(ctx.getText());
            return afterEvaluation(ctx, _boolean ? SINGLETON_TRUE : SINGLETON_FALSE);
        }

        @Override
        public Collection<FHIRPathNode> visitStringLiteral(FHIRPathParser.StringLiteralContext ctx) {
            beforeEvaluation(ctx);
            String text = unescape(ctx.getText());
            return afterEvaluation(ctx, singleton(stringValue(text.substring(1, text.length() - 1))));
        }

        @Override
        public Collection<FHIRPathNode> visitNumberLiteral(FHIRPathParser.NumberLiteralContext ctx) {
            beforeEvaluation(ctx);
            String text = ctx.getText();
            if (text.contains(".")) {
                return afterEvaluation(ctx, singleton(decimalValue(new BigDecimal(text))));
            }
            return afterEvaluation(ctx, singleton(integerValue(Integer.parseInt(text))));
        }

        @Override
        public Collection<FHIRPathNode> visitDateLiteral(FHIRPathParser.DateLiteralContext ctx) {
            beforeEvaluation(ctx);
            return afterEvaluation(ctx, singleton(dateValue(ctx.getText().substring(1))));
        }

        @Override
        public Collection<FHIRPathNode> visitDateTimeLiteral(FHIRPathParser.DateTimeLiteralContext ctx) {
            beforeEvaluation(ctx);
            return afterEvaluation(ctx, singleton(dateTimeValue(ctx.getText().substring(1))));
        }

        @Override
        public Collection<FHIRPathNode> visitTimeLiteral(FHIRPathParser.TimeLiteralContext ctx) {
            beforeEvaluation(ctx);
            return afterEvaluation(ctx, singleton(timeValue(ctx.getText().substring(2))));
        }

        @Override
        public Collection<FHIRPathNode> visitQuantityLiteral(FHIRPathParser.QuantityLiteralContext ctx) {
            beforeEvaluation(ctx);
            Collection<FHIRPathNode> result = visitChildren(ctx);
            return afterEvaluation(ctx, result);
        }

        @Override
        public Collection<FHIRPathNode> visitExternalConstant(FHIRPathParser.ExternalConstantContext ctx) {
            beforeEvaluation(ctx);
            String identifier = getString(visit(ctx.identifier()));
            return afterEvaluation(ctx, evaluationContext.getExternalConstant(identifier));
        }

        @Override
        public Collection<FHIRPathNode> visitMemberInvocation(FHIRPathParser.MemberInvocationContext ctx) {
            beforeEvaluation(ctx);

            Collection<FHIRPathNode> currentContext = getCurrentContext();
            String identifier = getString(visit(ctx.identifier()));

            if (isSingleton(currentContext)) {
                FHIRPathNode node = getSingleton(currentContext);
                if (closure(node.type()).contains(identifier)) {
                    return afterEvaluation(ctx, currentContext);
                }
            }

            List<FHIRPathNode> result = new ArrayList<>();
            for (FHIRPathNode node : currentContext) {
                for (FHIRPathNode child : node.children()) {
                    if (identifier.equals(child.name())) {
                        result.add(child);
                    }
                }
            }

            return afterEvaluation(ctx, result);
        }

        @Override
        public Collection<FHIRPathNode> visitFunctionInvocation(FHIRPathParser.FunctionInvocationContext ctx) {
            beforeEvaluation(ctx);
            Collection<FHIRPathNode> result = visitChildren(ctx);
            return afterEvaluation(ctx, result);
        }

        @Override
        public Collection<FHIRPathNode> visitThisInvocation(FHIRPathParser.ThisInvocationContext ctx) {
            beforeEvaluation(ctx);
            return afterEvaluation(ctx, getCurrentContext());
        }

        @Override
        public Collection<FHIRPathNode> visitIndexInvocation(FHIRPathParser.IndexInvocationContext ctx) {
            beforeEvaluation(ctx);
            Collection<FHIRPathNode> result = visitChildren(ctx);
            return afterEvaluation(ctx, result);
        }

        @Override
        public Collection<FHIRPathNode> visitTotalInvocation(FHIRPathParser.TotalInvocationContext ctx) {
            beforeEvaluation(ctx);
            Collection<FHIRPathNode> result = visitChildren(ctx);
            return afterEvaluation(ctx, result);
        }

        @Override
        public Collection<FHIRPathNode> visitFunction(FHIRPathParser.FunctionContext ctx) {
            beforeEvaluation(ctx);

            Collection<FHIRPathNode> result = empty();

            String functionName = getString(visit(ctx.identifier()));

            List<ExpressionContext> arguments = new ArrayList<ExpressionContext>();
            ParamListContext paramList = ctx.paramList();
            if (paramList != null) {
                arguments.addAll(ctx.paramList().expression());
            }

            Collection<FHIRPathNode> currentContext = getCurrentContext();

            // some built-in functions are implemented right here in the visitor;
            // others are looked up in the registry (the "default" case)
            switch (functionName) {
            case "all":
                result = all(arguments);
                break;
            case "as":
                result = as(arguments);
                break;
            case "exists":
                result = exists(arguments);
                break;
            case "iif":
                result = iif(arguments);
                break;
            case "is":
                result = is(arguments);
                break;
            case "ofType":
                result = ofType(arguments);
                break;
            case "repeat":
                result = repeat(arguments);
                break;
            case "select":
                result = select(arguments);
                break;
            case "trace":
                result = trace(arguments);
                break;
            case "where":
                result = where(arguments);
                break;
            default:
                FHIRPathFunction function = FHIRPathFunction.registry().getFunction(functionName);
                if (function == null) {
                    throw new IllegalArgumentException("Function: '" + functionName + "' not found");
                }
                if (arguments.size() < function.getMinArity() || arguments.size() > function.getMaxArity()) {
                    throw unexpectedNumberOfArguments(arguments.size(), functionName);
                }
                result = function.apply(evaluationContext, currentContext, arguments.stream()
                    // evaluate arguments: ExpressionContext -> Collection<FHIRPathNode>
                    .map(expressionContext -> visit(expressionContext))
                    .collect(Collectors.toList()));
                break;
            }

            return afterEvaluation(ctx, result);
        }

        @Override
        public Collection<FHIRPathNode> visitParamList(FHIRPathParser.ParamListContext ctx) {
            beforeEvaluation(ctx);
            Collection<FHIRPathNode> result = visitChildren(ctx);
            return afterEvaluation(ctx, result);
        }

        @Override
        public Collection<FHIRPathNode> visitQuantity(FHIRPathParser.QuantityContext ctx) {
            beforeEvaluation(ctx);
            String number = ctx.NUMBER().getText();
            String text = ctx.unit().getText();
            String unit = text.startsWith("'") ? text.substring(1, text.length() - 1) : text;
            return afterEvaluation(ctx, singleton(FHIRPathQuantityValue.quantityValue(new BigDecimal(number), unit)));
        }

        @Override
        public Collection<FHIRPathNode> visitUnit(FHIRPathParser.UnitContext ctx) {
            beforeEvaluation(ctx);
            Collection<FHIRPathNode> result = visitChildren(ctx);
            return afterEvaluation(ctx, result);
        }

        @Override
        public Collection<FHIRPathNode> visitDateTimePrecision(FHIRPathParser.DateTimePrecisionContext ctx) {
            beforeEvaluation(ctx);
            Collection<FHIRPathNode> result = visitChildren(ctx);
            return afterEvaluation(ctx, result);
        }

        @Override
        public Collection<FHIRPathNode> visitPluralDateTimePrecision(FHIRPathParser.PluralDateTimePrecisionContext ctx) {
            beforeEvaluation(ctx);
            Collection<FHIRPathNode> result = visitChildren(ctx);
            return afterEvaluation(ctx, result);
        }

        @Override
        public Collection<FHIRPathNode> visitTypeSpecifier(FHIRPathParser.TypeSpecifierContext ctx) {
            beforeEvaluation(ctx);
            Collection<FHIRPathNode> result = visitChildren(ctx);
            return afterEvaluation(ctx, result);
        }

        @Override
        public Collection<FHIRPathNode> visitQualifiedIdentifier(FHIRPathParser.QualifiedIdentifierContext ctx) {
            beforeEvaluation(ctx);
            String text = ctx.getText().replace("`", "");
            return afterEvaluation(ctx, singleton(stringValue(text)));
        }

        @Override
        public Collection<FHIRPathNode> visitIdentifier(FHIRPathParser.IdentifierContext ctx) {
            beforeEvaluation(ctx);
            String text = ctx.getText();
            Collection<FHIRPathNode> result = IDENTIFIER_CACHE.computeIfAbsent(text, t -> singleton(stringValue(text.startsWith("`") ? text.substring(1, text.length() - 1) : text)));
            return afterEvaluation(ctx, result);
        }

        private String indent() {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < indentLevel; i++) {
                builder.append("    ");
            }
            return builder.toString();
        }

        private void beforeEvaluation(ParserRuleContext ctx) {
            debug(ctx);
            for (EvaluationListener listener : evaluationContext.getEvaluationListeners()) {
                listener.beforeEvaluation(ctx, getCurrentContext());
            }
            indentLevel++;
        }

        private Collection<FHIRPathNode> afterEvaluation(ParserRuleContext ctx, Collection<FHIRPathNode> result) {
            indentLevel--;
            for (EvaluationListener listener : evaluationContext.getEvaluationListeners()) {
                listener.afterEvaluation(ctx, result);
            }
            return result;
        }

        private void debug(ParserRuleContext ctx) {
            if (log.isLoggable(Level.FINEST)) {
                log.finest(indent() + ctx.getClass().getSimpleName() + ": " + getText(ctx) + ", childCount: " + ctx.getChildCount());
            }
        }

        private String getText(ParserRuleContext ctx) {
            return ctx.getStart().getInputStream().getText(Interval.of(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex()));
        }
    }

    /**
     * A context object used to pass information to/from the FHIRPath evaluation engine
     */
    public static class EvaluationContext {
        public static final boolean DEFAULT_RESOLVE_RELATIVE_REFERENCES = false;

        private static final String UCUM_SYSTEM = "http://unitsofmeasure.org";
        private static final String LOINC_SYSTEM = "http://loinc.org";
        private static final String SCT_SYSTEM = "http://snomed.info/sct";

        private static final Collection<FHIRPathNode> UCUM_SYSTEM_SINGLETON = singleton(stringValue(UCUM_SYSTEM));
        private static final Collection<FHIRPathNode> LOINC_SYSTEM_SINGLETON = singleton(stringValue(LOINC_SYSTEM));
        private static final Collection<FHIRPathNode> SCT_SYSTEM_SINGLETON = singleton(stringValue(SCT_SYSTEM));
        private static final Collection<FHIRPathNode> TERM_SERVICE_SINGLETON = singleton(FHIRPathTermServiceNode.termServiceNode());

        private final FHIRPathTree tree;
        private final Map<String, Collection<FHIRPathNode>> externalConstantMap = new HashMap<>();
        private final List<Issue> issues = new ArrayList<>();
        private final Map<CacheKey, Collection<FHIRPathNode>> functionResultCache = new HashMap<>();
        private final List<EvaluationListener> listeners = new ArrayList<>();

        private Constraint constraint;
        private boolean resolveRelativeReferences = DEFAULT_RESOLVE_RELATIVE_REFERENCES;

        /**
         * Create an empty evaluation context, evaluating stand-alone expressions
         */
        public EvaluationContext() {
            this((FHIRPathTree) null);
        }

        /**
         * Create an evaluation context where the passed resource is the context root.
         * Sets %resource and %rootResource external constants to the passed resource, but these can be overridden.
         *
         * @param resource
         *     the resource
         */
        public EvaluationContext(Resource resource) {
            this(FHIRPathTree.tree(resource));
            externalConstantMap.put("rootResource", singleton(tree.getRoot()));
            externalConstantMap.put("resource", singleton(tree.getRoot()));
        }

        /**
         * Create an evaluation context where the passed element is the context root.
         *
         * @param element
         *     the element
         */
        public EvaluationContext(Element element) {
            this(FHIRPathTree.tree(element));
        }

        private EvaluationContext(FHIRPathTree tree) {
            this.tree = tree;
        }

        /**
         * Get the FHIRPath tree associated with this EvaluationContext
         *
         * @return
         *     the FHIRPath tree associated with this EvaluationContext
         */
        public FHIRPathTree getTree() {
            return tree;
        }

        /**
         * Set an external constant using a name and FHIRPath node
         *
         * @param name
         *     the name
         * @param node
         *     the FHIRPath node
         */
        public void setExternalConstant(String name, FHIRPathNode node) {
            externalConstantMap.put(name, singleton(node));
        }

        /**
         * Set an external constant using a name and a collection of FHIRPath node
         *
         * @param name
         *     the name
         * @param nodes
         *     the collection of FHIRPath nodes
         */
        public void setExternalConstant(String name, Collection<FHIRPathNode> nodes) {
            externalConstantMap.put(name, nodes);
        }

        /**
         * Unset an external constant with the given name
         *
         * @param name
         *     the name
         */
        public void unsetExternalConstant(String name) {
            externalConstantMap.remove(name);
        }

        /**
         * Get an external constant with the given name
         *
         * @param name
         *     the name
         * @return
         *     the external constant as a collection of FHIRPath nodes or an empty collection
         */
        public Collection<FHIRPathNode> getExternalConstant(String name) {
            switch (name) {
            case "ucum":
                return UCUM_SYSTEM_SINGLETON;
            case "loinc":
                return LOINC_SYSTEM_SINGLETON;
            case "sct":
                return SCT_SYSTEM_SINGLETON;
            case "terminologies":
                return TERM_SERVICE_SINGLETON;
            default:
                if (name.startsWith("ext-")) {
                    return singleton(stringValue(name.replace("ext-", "http://hl7.org/fhir/StructureDefinition/")));
                }
                if (name.startsWith("vs-")) {
                    return singleton(stringValue(name.replace("vs-", "http://hl7.org/fhir/ValueSet/")));
                }
                return externalConstantMap.getOrDefault(name, empty());
            }
        }

        /**
         * Indicates whether this EvaluationContext has an external constant with the given name
         *
         * @param name
         *     the name
         * @return
         *     true if this EvaluationContext has an external constant with the given name, otherwise false
         */
        public boolean hasExternalConstant(String name) {
            return externalConstantMap.containsKey(name);
        }

        /**
         * Get the list of supplemental issues that were generated during evaluation
         *
         * <p>Supplemental issues are used to convey additional information about the evaluation to the client
         *
         * @return
         *     the list of supplemental issues that were generated during evaluation
         */
        public List<Issue> getIssues() {
            return issues;
        }

        /**
         * Clear the list of supplemental issues that were generated during evaluation
         */
        public void clearIssues() {
            issues.clear();
        }

        /**
         * Indicates whether this evaluation context has supplemental issues that were generated during evaluation
         *
         * @return
         *     true if this evaluation context has supplemental issues that were generated during evaluation, otherwise false
         */
        public boolean hasIssues() {
            return !issues.isEmpty();
        }

        /**
         * Set the constraint currently under evaluation
         *
         * <p>If a {@link Constraint} is the source of the expression under evaluation, then this method allows the
         * client to make it available to the evaluation engine to access additional information about the constraint
         * (e.g. id, level, location, description, etc.)
         *
         * @param constraint
         *     the constraint currently under evaluation
         */
        public void setConstraint(Constraint constraint) {
            this.constraint = constraint;
        }

        /**
         * Unset the constraint currently under evaluation
         */
        public void unsetConstraint() {
            constraint = null;
        }

        /**
         * Get the constraint currently under evaluation
         *
         * @return
         *     the constraint currently under evaluation if exists, otherwise null
         */
        public Constraint getConstraint() {
            return constraint;
        }

        /**
         * Indicates whether this evaluation context has an associated constraint
         *
         * @return
         *     true if this evaluation context has an associated constraint, otherwise false
         */
        public boolean hasConstraint() {
            return constraint != null;
        }

        /**
         * Set the resolve relative references indicator
         *
         * @param resolveRelativeReferences
         *     the resolve relative references indicator
         */
        public void setResolveRelativeReferences(boolean resolveRelativeReferences) {
            this.resolveRelativeReferences = resolveRelativeReferences;
        }

        /**
         * Indicates whether the evaluator using this evaluation context should resolve relative references (if possible)
         *
         * @return
         *     true if the evaluator using this evaluation context should resolve relative references (if possible), otherwise false
         */
        public boolean resolveRelativeReferences() {
            return resolveRelativeReferences;
        }

        /**
         * Get the cached function result for the given function name, context, and arguments.
         *
         * @param functionName
         *     the function name
         * @param context
         *     the context
         * @param arguments
         *     the arguments
         * @return
         *     the cached function result as a collection of FHIRPath nodes or an empty collection
         */
        public Collection<FHIRPathNode> getCachedFunctionResult(String functionName, Collection<FHIRPathNode> context, List<Collection<FHIRPathNode>> arguments) {
            CacheKey key = key(functionName, context, arguments);
            return functionResultCache.getOrDefault(key, empty());
        }

        /**
         * Cache the function result for the given function name, context, and arguments
         *
         * @param functionName
         *     the function name
         * @param context
         *     the context
         * @param arguments
         *     the arguments
         * @param result
         *     the function result
         */
        public void cacheFunctionResult(String functionName, Collection<FHIRPathNode> context, List<Collection<FHIRPathNode>> arguments, Collection<FHIRPathNode> result) {
            CacheKey key = key(functionName, context, arguments);
            functionResultCache.put(key, result);
        }

        /**
         * Indicates whether a function result has been cached for the given function name, context, and arguments.
         *
         * @param functionName
         *     the function name
         * @param context
         *     the context
         * @param arguments
         *     the arguments
         * @return
         *     true if the function result has been cached, otherwise false
         */
        public boolean hasCachedFunctionResult(String functionName, Collection<FHIRPathNode> context, List<Collection<FHIRPathNode>> arguments) {
            CacheKey key = key(functionName, context, arguments);
            return functionResultCache.containsKey(key);
        }

        /**
         * Add an evaluation listener to this context.
         *
         * @param listener
         *     the evaluation listener
         */
        public void addEvaluationListener(EvaluationListener listener) {
            listeners.add(listener);
        }

        /**
         * Remove an evaluation listener from this context.
         *
         * @param listener
         *     the evaluation listener
         * @return
         *     true if the evaluation listener being removed was present in the list of evaluation listeners for this context, otherwise false
         */
        public boolean removeEvaluationListener(EvaluationListener listener) {
            return listeners.remove(listener);
        }

        /**
         * Get the list of evaluation listeners for this context.
         *
         * @return
         *    the list of evaluation listeners
         */
        public List<EvaluationListener> getEvaluationListeners() {
            return listeners;
        }
    }

    /**
     * A listener interface for receiving notifications during evaluation
     */
    public interface EvaluationListener {
        /**
         * Called immediately before an expression, term, or literal associated with the given
         * parser rule context is evaluated
         *
         * @param parserRuleContext
         *     the parser rule context
         * @param context
         *     the evaluator context
         */
        void beforeEvaluation(ParserRuleContext parserRuleContext, Collection<FHIRPathNode> context);

        /**
         * Called immediately after an expression, term, or literal associated with the given
         * parser rule context is evaluated
         *
         * @param parserRuleContext
         *     the parser rule context
         * @param result
         *     the evaluation result
         */
        void afterEvaluation(ParserRuleContext parserRuleContext, Collection<FHIRPathNode> result);
    }
}
