/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path.evaluator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.antlr.v4.runtime.tree.ParseTree;

import com.ibm.watsonhealth.fhir.model.path.FHIRPathBaseVisitor;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.AdditiveExpressionContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.AndExpressionContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.BooleanLiteralContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.DateTimeLiteralContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.DateTimePrecisionContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.EqualityExpressionContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.ExpressionContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.ExternalConstantContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.ExternalConstantTermContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.FunctionContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.FunctionInvocationContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.IdentifierContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.ImpliesExpressionContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.IndexerExpressionContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.InequalityExpressionContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.InvocationExpressionContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.InvocationTermContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.LiteralTermContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.MemberInvocationContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.MembershipExpressionContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.MultiplicativeExpressionContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.NullLiteralContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.NumberLiteralContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.OrExpressionContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.ParamListContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.ParenthesizedTermContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.PluralDateTimePrecisionContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.PolarityExpressionContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.QualifiedIdentifierContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.QuantityContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.QuantityLiteralContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.StringLiteralContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.TermExpressionContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.ThisInvocationContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.TimeLiteralContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.TypeExpressionContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.TypeSpecifierContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.UnionExpressionContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.UnitContext;
import com.ibm.watsonhealth.fhir.model.path.function.FHIRPathFunction;
import com.ibm.watsonhealth.fhir.model.path.function.FHIRPathFunctionRegistry;
import com.ibm.watsonhealth.fhir.model.type.Base64Binary;
import com.ibm.watsonhealth.fhir.model.type.Code;
import com.ibm.watsonhealth.fhir.model.type.Date;
import com.ibm.watsonhealth.fhir.model.type.DateTime;
import com.ibm.watsonhealth.fhir.model.type.Decimal;
import com.ibm.watsonhealth.fhir.model.type.Id;
import com.ibm.watsonhealth.fhir.model.type.Instant;
import com.ibm.watsonhealth.fhir.model.type.Markdown;
import com.ibm.watsonhealth.fhir.model.type.Oid;
import com.ibm.watsonhealth.fhir.model.type.PositiveInt;
import com.ibm.watsonhealth.fhir.model.type.Time;
import com.ibm.watsonhealth.fhir.model.type.UnsignedInt;
import com.ibm.watsonhealth.fhir.model.type.Uri;

public class FHIRPathEvaluator extends FHIRPathBaseVisitor<Object> {
    public static boolean DEBUG = false;

    private ExpressionContext expressionContext = null;
    private int indentLevel = 0;
    private Stack<Object> contextStack = null;
    private static final List<Class<?>> PRIMITIVE_TYPES =
            Arrays.asList(Base64Binary.class, com.ibm.watsonhealth.fhir.model.type.Boolean.class, Code.class, Date.class, DateTime.class, Decimal.class, Id.class, Instant.class, com.ibm.watsonhealth.fhir.model.type.Integer.class, Markdown.class, Oid.class, PositiveInt.class, com.ibm.watsonhealth.fhir.model.type.String.class, Time.class, UnsignedInt.class, Uri.class);

    public FHIRPathEvaluator(ExpressionContext expressionContext) {
        this.expressionContext = expressionContext;
        contextStack = new Stack<Object>();
    }

    public Object evaluate(Object initialContext) {
        pushContext(initialContext);
        Object result = visit(expressionContext);
        popContext();
        return result;
    }

    private Object getCurrentContext() {
        if (!contextStack.isEmpty()) {
            return contextStack.peek();
        }
        return null;
    }

    private void setCurrentContext(Object currentContext) {
        if (!contextStack.isEmpty()) {
            contextStack.pop();
        }
        pushContext(currentContext);
    }

    public void pushContext(Object context) {
        if (context != null) {
            contextStack.push(context);
        }
    }

    public Object popContext() {
        if (!contextStack.isEmpty()) {
            return contextStack.pop();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object visitIndexerExpression(IndexerExpressionContext ctx) {
        print(ctx);
        indentLevel++;

        visit(ctx.expression(0));
        BigDecimal number = (BigDecimal) visit(ctx.expression(1));
        int index = number.intValue();

        Object currentContext = getCurrentContext();

        List<Object> list = null;
        if (currentContext instanceof List) {
            list = (List<Object>) currentContext;
        } else {
            list = Collections.singletonList(currentContext);
        }

        if (index >= 0 && index < list.size()) {
            currentContext = list.get(index);
        } else {
            currentContext = Collections.emptyList();
        }

        currentContext = convert(currentContext);
        setCurrentContext(currentContext);

        indentLevel--;
        return currentContext;
    }

    @Override
    public Object visitPolarityExpression(PolarityExpressionContext ctx) {
        print(ctx);
        indentLevel++;

        Object result = visit(ctx.expression());
        String polarity = ctx.getChild(0).getText();

        switch (polarity) {
        case "+":
            result = ((BigDecimal) result).plus();
            break;
        case "-":
            result = ((BigDecimal) result).negate();
            break;
        }

        indentLevel--;
        return result;
    }

    @Override
    public Object visitAdditiveExpression(AdditiveExpressionContext ctx) {
        print(ctx);
        indentLevel++;

        Object result = null;

        Object left = visit(ctx.expression(0));
        Object right = visit(ctx.expression(1));
        String operator = ctx.getChild(1).getText();

        if (left instanceof Number && !(left instanceof BigDecimal)) {
            left = new BigDecimal(left.toString());
        }

        if (right instanceof Number && !(right instanceof BigDecimal)) {
            right = new BigDecimal(right.toString());
        }

        switch (operator) {
        case "+":
            if (left instanceof BigDecimal && right instanceof BigDecimal) {
                result = ((BigDecimal) left).add((BigDecimal) right);
            } else {
                // concatenation
                result = left.toString() + right.toString();
            }
            break;
        case "-":
            result = ((BigDecimal) left).subtract((BigDecimal) right);
            break;
        }

        indentLevel--;
        return result;
    }

    @Override
    public Object visitMultiplicativeExpression(MultiplicativeExpressionContext ctx) {
        print(ctx);
        indentLevel++;

        Object result = null;

        Object left = visit(ctx.expression(0));
        Object right = visit(ctx.expression(1));
        String operator = ctx.getChild(1).getText();

        if (left instanceof Number && !(left instanceof BigDecimal)) {
            left = new BigDecimal(left.toString());
        }

        if (right instanceof Number && !(right instanceof BigDecimal)) {
            right = new BigDecimal(right.toString());
        }

        switch (operator) {
        case "*":
            result = ((BigDecimal) left).multiply((BigDecimal) right);
            break;
        case "/":
            result = ((BigDecimal) left).divide((BigDecimal) right);
            break;
        case "div":
            result = ((BigDecimal) left).divideToIntegralValue((BigDecimal) right);
            break;
        case "mod":
            result = ((BigDecimal) left).remainder((BigDecimal) right);
            break;
        }

        indentLevel--;
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object visitUnionExpression(UnionExpressionContext ctx) {
        print(ctx);
        indentLevel++;

        List<Object> left = (List<Object>) visit(ctx.expression(0));
        if (left == null) {
            left = Collections.emptyList();
        }
        List<Object> right = (List<Object>) visit(ctx.expression(1));
        if (right == null) {
            right = Collections.emptyList();
        }

        Set<Object> union = new HashSet<Object>(left);
        union.addAll(right);

        indentLevel--;
        return new ArrayList<Object>(union);
    }

    @Override
    public Object visitOrExpression(OrExpressionContext ctx) {
        print(ctx);
        indentLevel++;

        Boolean left = (Boolean) visit(ctx.expression(0));
        Boolean right = (Boolean) visit(ctx.expression(1));

        indentLevel--;
        return left || right;
    }

    @Override
    public Object visitAndExpression(AndExpressionContext ctx) {
        print(ctx);
        indentLevel++;

        Boolean left = (Boolean) visit(ctx.expression(0));
        Boolean right = (Boolean) visit(ctx.expression(1));

        indentLevel--;
        return left && right;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object visitMembershipExpression(MembershipExpressionContext ctx) {
        print(ctx);
        indentLevel++;

        Boolean result = false;

        Object left = visit(ctx.expression(0));
        Object right = visit(ctx.expression(1));
        String operator = ctx.getChild(1).getText();

        switch (operator) {
        case "in":
            if (left instanceof List) {
                result = ((List<Object>) right).containsAll((List<Object>) left);
            } else {
                result = ((List<Object>) right).contains(left);
            }
            break;
        case "contains":
            if (right instanceof List) {
                result = ((List<Object>) left).containsAll((List<Object>) right);
            } else {
                result = ((List<Object>) left).contains(right);
            }
            break;
        }

        indentLevel--;
        return result;
    }

    @Override
    public Object visitInequalityExpression(InequalityExpressionContext ctx) {
        print(ctx);
        indentLevel++;

        Boolean result = false;

        Object left = visit(ctx.expression(0));
        Object right = visit(ctx.expression(1));
        String operator = ctx.getChild(1).getText();

        switch (operator) {
        case ">":
            result = ((BigDecimal) left).compareTo((BigDecimal) right) > 0;
            break;
        case ">=":
            result = ((BigDecimal) left).compareTo((BigDecimal) right) >= 0;
            break;
        case "<":
            result = ((BigDecimal) left).compareTo((BigDecimal) right) < 0;
            break;
        case "<=":
            result = ((BigDecimal) left).compareTo((BigDecimal) right) <= 0;
            break;
        }

        indentLevel--;
        return result;
    }

    @Override
    public Object visitInvocationExpression(InvocationExpressionContext ctx) {
        print(ctx);
        indentLevel++;

        Object result = visit(ctx.expression());
        setCurrentContext(result);
        result = visit(ctx.invocation());

        indentLevel--;
        return result;
    }

    @Override
    public Object visitEqualityExpression(EqualityExpressionContext ctx) {
        print(ctx);
        indentLevel++;

        Boolean result = false;

        Object left = visit(ctx.expression(0));
        Object right = visit(ctx.expression(1));

        if (left == null || right == null) {
            return result;
        }

        String operator = ctx.getChild(1).getText();

        switch (operator) {
        case "=":
        case "~":
            result = left.equals(right);
            break;
        case "!=":
        case "!~":
            result = !left.equals(right);
            break;
        }

        indentLevel--;
        return result;
    }

    @Override
    public Object visitImpliesExpression(ImpliesExpressionContext ctx) {
        print(ctx);
        indentLevel++;

        Boolean left = (Boolean) visit(ctx.expression(0));
        Boolean right = (Boolean) visit(ctx.expression(1));

        indentLevel--;
        return !left || right;
    }

    @Override
    public Object visitTermExpression(TermExpressionContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitTypeExpression(TypeExpressionContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitInvocationTerm(InvocationTermContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitLiteralTerm(LiteralTermContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitExternalConstantTerm(ExternalConstantTermContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitParenthesizedTerm(ParenthesizedTermContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visit(ctx.expression());
        indentLevel--;
        return result;
    }

    @Override
    public Object visitNullLiteral(NullLiteralContext ctx) {
        print(ctx);
        return null;
    }

    @Override
    public Object visitBooleanLiteral(BooleanLiteralContext ctx) {
        print(ctx);
        return Boolean.valueOf(ctx.getText());
    }

    @Override
    public Object visitStringLiteral(StringLiteralContext ctx) {
        print(ctx);
        String literal = ctx.getText();
        literal = literal.substring(1, literal.length() - 1);
        return literal;
    }

    @Override
    public Object visitNumberLiteral(NumberLiteralContext ctx) {
        print(ctx);
        return new BigDecimal(ctx.getText());
    }

    @Override
    public Object visitDateTimeLiteral(DateTimeLiteralContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitTimeLiteral(TimeLiteralContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitQuantityLiteral(QuantityLiteralContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitExternalConstant(ExternalConstantContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object visitMemberInvocation(MemberInvocationContext ctx) {
        print(ctx);
        indentLevel++;

        String identifier = (String) visit(ctx.identifier());

        Object currentContext = getCurrentContext();
        if (currentContext == null) {
            return null;
        }
        Class<?> currentContextClass = currentContext.getClass();

        if (!currentContextClass.getSimpleName().equals(identifier)) {
            try {
                String methodName = "get" + identifier.substring(0, 1).toUpperCase() + identifier.substring(1);

                List<Object> results = new ArrayList<Object>();

                if (!(currentContext instanceof List)) {
                    currentContext = Collections.singletonList(currentContext);
                }

                List<Object> list = (List<Object>) currentContext;

                for (Object item : list) {
                    Class<?> itemClass = item.getClass();
                    Method method = itemClass.getMethod(methodName);
                    Object o = method.invoke(item);
                    if (o instanceof List) {
                        results.addAll((List<Object>) o);
                    } else {
                        results.add(o);
                    }
                }

                currentContext = results.size() == 1 ? results.get(0) : results;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        currentContext = convert(currentContext);
        setCurrentContext(currentContext);

        indentLevel--;
        return currentContext;
    }

    @Override
    public Object visitFunctionInvocation(FunctionInvocationContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitThisInvocation(ThisInvocationContext ctx) {
        print(ctx);
        return getCurrentContext();
    }

    @Override
    public Object visitFunction(FunctionContext ctx) {
        print(ctx);
        indentLevel++;

        Object result = null;

        String functionName = (String) visit(ctx.identifier());

        List<ExpressionContext> arguments = new ArrayList<ExpressionContext>();
        ParamListContext paramList = ctx.paramList();
        if (paramList != null) {
            arguments.addAll(ctx.paramList().expression());
        }

        FHIRPathFunction function = FHIRPathFunctionRegistry.getInstance().getFunction(functionName);
        try {
            Object currentContext = getCurrentContext();
            result = function.invoke(this, currentContext, arguments.toArray(new ExpressionContext[arguments.size()]));
            result = convert(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        indentLevel--;
        return result;
    }

    @Override
    public Object visitParamList(ParamListContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitQuantity(QuantityContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitUnit(UnitContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitDateTimePrecision(DateTimePrecisionContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitPluralDateTimePrecision(PluralDateTimePrecisionContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitTypeSpecifier(TypeSpecifierContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitQualifiedIdentifier(QualifiedIdentifierContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitIdentifier(IdentifierContext ctx) {
        print(ctx);
        String identifier = ctx.getChild(0).getText();
        return identifier;
    }

    @SuppressWarnings("unchecked")
    private Object convert(Object result) {
        if (result == null) {
            return null;
        }
        Class<?> resultClass = result.getClass();
        if (result instanceof List) {
            List<Object> list = (List<Object>) result;
            List<Object> newResult = new ArrayList<Object>();
            for (Object item : list) {
                Class<?> itemClass = item.getClass();
                if (isPrimitiveType(itemClass) || isEnumerationWrapper(itemClass) || isStringWrapper(itemClass)) {
                    newResult.add(getValue(item));
                } else {
                    newResult.add(item);
                }
            }
            result = newResult;
        } else {
            if (isPrimitiveType(resultClass) || isEnumerationWrapper(resultClass) || isStringWrapper(resultClass)) {
                result = getValue(result);
            }
        }
        return result;
    }

    private String indent() {
        StringBuffer buffer = new StringBuffer();
        int spaces = indentLevel * 4;
        for (int i = 0; i < spaces; i++) {
            buffer.append(" ");
        }
        return buffer.toString();
    }

    private boolean isEnumerationWrapper(Class<?> valueType) {
        try {
            Field valueField = valueType.getDeclaredField("value");
            if (valueField.getType().isEnum()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    private boolean isPrimitiveType(Class<?> type) {
        return PRIMITIVE_TYPES.contains(type);
    }

    private boolean isStringWrapper(Class<?> valueType) {
        try {
            Field valueField = valueType.getDeclaredField("value");
            if (String.class.equals(valueField.getType()) && valueType.getDeclaredFields().length == 1 && !isPrimitiveType(valueType)) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    protected Object getValue(Object wrapper) {
        try {
            Object value = wrapper.getClass().getMethod("getValue").invoke(wrapper);
            Class<?> valueType = value.getClass();
            if (valueType.isEnum()) {
                return value.getClass().getMethod("value").invoke(wrapper);
            }
            return value;
        } catch (Exception e) {
        }
        return null;
    }

    private void print(ParseTree ctx) {
        if (DEBUG) {
            System.out.println(indent() + ctx.getClass().getName() + ": " + ctx.getText() + ", childCount: " + ctx.getChildCount());
        }
    }
}
