/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path.evaluator;

import static com.ibm.watsonhealth.fhir.model.path.FHIRPathBooleanNode.booleanNode;
import static com.ibm.watsonhealth.fhir.model.path.FHIRPathDecimalNode.decimalNode;
import static com.ibm.watsonhealth.fhir.model.path.FHIRPathIntegerNode.integerNode;
import static com.ibm.watsonhealth.fhir.model.path.FHIRPathStringNode.stringNode;
import static java.util.Collections.singletonList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.tree.ParseTree;

import com.ibm.watsonhealth.fhir.model.path.FHIRPathBaseVisitor;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathNode;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.ExpressionContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.ParamListContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathPrimitiveTypeNode;
import com.ibm.watsonhealth.fhir.model.path.exception.FHIRPathException;
import com.ibm.watsonhealth.fhir.model.path.util.FHIRPathUtil;

public class FHIRPathEvaluator {
    public static boolean DEBUG = false;
    
    private final ExpressionContext expressionContext;
    
    public FHIRPathEvaluator(ExpressionContext expressionContext) {
        this.expressionContext = expressionContext;
    }
    
    public Collection<FHIRPathNode> evaluate(Collection<FHIRPathNode> initialContext) throws FHIRPathException {
        try {
            EvaluatingVisitor visitor = new EvaluatingVisitor();
            visitor.pushContext(initialContext);
            Collection<FHIRPathNode> result = visitor.visit(expressionContext);
            visitor.popContext();
            return result;
        } catch (Exception e) {
            throw new FHIRPathException(e.getMessage(), e);
        }
    }
    
    private static class EvaluatingVisitor extends FHIRPathBaseVisitor<Collection<FHIRPathNode>> {
        private Stack<Collection<FHIRPathNode>> contextStack = new Stack<>();
        private int indentLevel = 0;
        
        private void pushContext(Collection<FHIRPathNode> context) {
            if (context != null) {
                contextStack.push(context);
            }
        }
    
        private Collection<FHIRPathNode> popContext() {
            if (!contextStack.isEmpty()) {
                return contextStack.pop();
            }
            return null;
        }
        
        private Collection<FHIRPathNode> getCurrentContext() {
            if (!contextStack.isEmpty()) {
                return contextStack.peek();
            }
            return Collections.emptyList();
        }
        
        private void setCurrentContext(Collection<FHIRPathNode> context) {
            popContext();
            pushContext(context);
        }
        
        private Collection<FHIRPathNode> singleton(FHIRPathNode node) {
            return singletonList(node);
        }
        
        @Override
        public Collection<FHIRPathNode> visitIndexerExpression(FHIRPathParser.IndexerExpressionContext ctx) {
            debug(ctx);
            indentLevel++;
            
            visit(ctx.expression(0));
            int index = getValue(visit(ctx.expression(1))).asNumberNode().asIntegerNode().integer();
            
            Collection<FHIRPathNode> currentContext = getCurrentContext();
            
            List<FHIRPathNode> list = new ArrayList<>(currentContext);
            if (index >= 0 && index < list.size()) {
                currentContext = singleton(list.get(index));
            } else {
                currentContext = Collections.emptyList();
            }
            
            setCurrentContext(currentContext);
            
            indentLevel--;
            return currentContext;
        }
        
        @Override
        public Collection<FHIRPathNode> visitPolarityExpression(FHIRPathParser.PolarityExpressionContext ctx) {
            debug(ctx);
            indentLevel++;
            
            Collection<FHIRPathNode> result = Collections.emptyList();
            
            FHIRPathPrimitiveTypeNode node = getValue(visit(ctx.expression()));
            String polarity = ctx.getChild(0).getText();
            
            if (node.isNumberNode()) {
                switch (polarity) {
                case "+":
                    result = singleton(node.asNumberNode().plus());
                    break;
                case "-":
                    result = singleton(node.asNumberNode().negate());
                    break;
                }
            }
            
            indentLevel--;
            return result;
        }
        
        private FHIRPathPrimitiveTypeNode getValue(Collection<FHIRPathNode> input) {
            /*
            if (input.size() > 1) {
                throw new IllegalArgumentException();
            }
            */
            Iterator<FHIRPathNode> iterator = input.iterator();
            if (iterator.hasNext()) {
                FHIRPathNode node = iterator.next();
                if (node.isPrimitiveTypeNode()) {
                    return node.asPrimitiveTypeNode();
                }
                if (node.isElementNode()) {
                    return node.asElementNode().getValue();
                }
            }
            throw new IllegalArgumentException();
        }
    
        @Override
        public Collection<FHIRPathNode> visitAdditiveExpression(FHIRPathParser.AdditiveExpressionContext ctx) {
            debug(ctx);
            indentLevel++;
            
            Collection<FHIRPathNode> result = Collections.emptyList();
                        
            FHIRPathPrimitiveTypeNode left = getValue(visit(ctx.expression(0)));
            FHIRPathPrimitiveTypeNode right = getValue(visit(ctx.expression(1)));
            
            String operator = ctx.getChild(1).getText();
            
            if (left.isNumberNode() && right.isNumberNode()) {            
                switch (operator) {
                case "+":
                    result = singleton(left.asNumberNode().add(right.asNumberNode()));
                    break;
                case "-":
                    result = singleton(left.asNumberNode().subtract(right.asNumberNode()));
                    break;
                }
            } else if (left.isStringNode() && right.isStringNode() && "+".equals(operator)) {
                // concatenation
                result = singleton(left.asStringNode().concat(right.asStringNode()));
            }
                                    
            indentLevel--;
            return result;
        }
    
        @Override
        public Collection<FHIRPathNode> visitMultiplicativeExpression(FHIRPathParser.MultiplicativeExpressionContext ctx) {
            debug(ctx);
            indentLevel++;
            
            Collection<FHIRPathNode> result = Collections.emptyList();
            
            FHIRPathPrimitiveTypeNode left = getValue(visit(ctx.expression(0)));
            FHIRPathPrimitiveTypeNode right = getValue(visit(ctx.expression(1)));
            
            String operator = ctx.getChild(1).getText();

            if (left.isNumberNode() && right.isNumberNode()) {
                switch (operator) {
                case "*":
                    result = singleton(left.asNumberNode().multiply(right.asNumberNode()));
                    break;
                case "/":
                    result = singleton(left.asNumberNode().divide(right.asNumberNode()));
                    break;
                case "div":
                    result = singleton(left.asNumberNode().div(right.asNumberNode()));
                    break;
                case "mod":
                    result = singleton(left.asNumberNode().mod(right.asNumberNode()));
                    break;
                }
            }
            
            indentLevel--;
            return result;
        }
    
        @Override
        public Collection<FHIRPathNode> visitUnionExpression(FHIRPathParser.UnionExpressionContext ctx) {
            debug(ctx);
            indentLevel++;
            
            Collection<FHIRPathNode> left = visit(ctx.expression(0));
            Collection<FHIRPathNode> right = visit(ctx.expression(1));
            
            Set<FHIRPathNode> union = new LinkedHashSet<>(left);
            union.addAll(right);
            
            indentLevel--;
            return new ArrayList<>(union);
        }
    
        @Override
        public Collection<FHIRPathNode> visitOrExpression(FHIRPathParser.OrExpressionContext ctx) {
            debug(ctx);
            indentLevel++;
            
            Collection<FHIRPathNode> result = Collections.emptyList();
            
            FHIRPathPrimitiveTypeNode left = getValue(visit(ctx.expression(0)));
            FHIRPathPrimitiveTypeNode right = getValue(visit(ctx.expression(1)));
            
            if (left.isBooleanNode() && right.isBooleanNode()) {
                result = singleton(left.asBooleanNode().or(right.asBooleanNode()));
            }
            
            indentLevel--;
            return result;
        }
    
        @Override
        public Collection<FHIRPathNode> visitAndExpression(FHIRPathParser.AndExpressionContext ctx) {
            debug(ctx);
            indentLevel++;
            
            Collection<FHIRPathNode> result = Collections.emptyList();
            
            FHIRPathPrimitiveTypeNode left = getValue(visit(ctx.expression(0)));
            FHIRPathPrimitiveTypeNode right = getValue(visit(ctx.expression(1)));
            
            if (left.isBooleanNode() && right.isBooleanNode()) {
                result = singleton(left.asBooleanNode().and(right.asBooleanNode()));
            }
            
            indentLevel--;
            return result;
        }
    
        @Override
        public Collection<FHIRPathNode> visitMembershipExpression(FHIRPathParser.MembershipExpressionContext ctx) {
            debug(ctx);
            indentLevel++;
            
            Collection<FHIRPathNode> result = Collections.emptyList();
            
            Collection<FHIRPathNode> left = visit(ctx.expression(0));
            Collection<FHIRPathNode> right = visit(ctx.expression(1));
            
            String operator = ctx.getChild(1).getText();

            switch (operator) {
            case "in":
                result = singleton(booleanNode(right.containsAll(left)));
                break;
            case "contains":
                result = singleton(booleanNode(left.containsAll(right)));
                break;
            }
            
            indentLevel--;
            return result;
        }
    
        @Override
        public Collection<FHIRPathNode> visitInequalityExpression(FHIRPathParser.InequalityExpressionContext ctx) {
            debug(ctx);
            indentLevel++;
            
            Collection<FHIRPathNode> result = Collections.emptyList();
            
            FHIRPathPrimitiveTypeNode left = getValue(visit(ctx.expression(0)));
            FHIRPathPrimitiveTypeNode right = getValue(visit(ctx.expression(1)));
            
            String operator = ctx.getChild(1).getText();

            if (left.isNumberNode() && right.isNumberNode()) {
                switch (operator) {
                case ">":
                    result = singleton(booleanNode(left.asNumberNode().greaterThan(right.asNumberNode())));
                    break;
                case ">=":
                    result = singleton(booleanNode(left.asNumberNode().greaterThanOrEqual(right.asNumberNode())));
                    break;
                case "<":
                    result = singleton(booleanNode(left.asNumberNode().lessThan(right.asNumberNode())));
                    break;
                case "<=":
                    result = singleton(booleanNode(left.asNumberNode().lessThanOrEqual(right.asNumberNode())));
                    break;
                }
            }

            indentLevel--;
            return result;
        }
    
        @Override
        public Collection<FHIRPathNode> visitInvocationExpression(FHIRPathParser.InvocationExpressionContext ctx) {
            debug(ctx);
            indentLevel++;
            
            setCurrentContext(visit(ctx.expression()));
            Collection<FHIRPathNode> result = visit(ctx.invocation());
            
            indentLevel--;
            return result;
        }
    
        @Override
        public Collection<FHIRPathNode> visitEqualityExpression(FHIRPathParser.EqualityExpressionContext ctx) {
            debug(ctx);
            indentLevel++;
            
            Collection<FHIRPathNode> result = Collections.emptyList();

            Collection<FHIRPathNode> left = visit(ctx.expression(0));
            Collection<FHIRPathNode> right = visit(ctx.expression(1));
            
            System.out.println("left: " + left);
            System.out.println("right: " + right);
            
            String operator = ctx.getChild(1).getText();
            
            // TODO: this needs some work as "equal to" and "equivalent to" have different semantics

            switch (operator) {
            case "=": {
                result = singleton(booleanNode(getValue(left).equals(getValue(right))));
                break;
            }
            case "~":
                result = singleton(booleanNode(left.equals(right)));
                break;
            case "!=": {
                result = singleton(booleanNode(!getValue(left).equals(getValue(right))));
                break;
            }
            case "!~":
                result = singleton(booleanNode(!left.equals(right)));
                break;
            }

            indentLevel--;
            return result;
        }
    
        @Override
        public Collection<FHIRPathNode> visitImpliesExpression(FHIRPathParser.ImpliesExpressionContext ctx) {
            debug(ctx);
            indentLevel++;
            
            Collection<FHIRPathNode> result = Collections.emptyList();
            
            FHIRPathPrimitiveTypeNode left = getValue(visit(ctx.expression(0)));
            FHIRPathPrimitiveTypeNode right = getValue(visit(ctx.expression(1)));
            
            if (left.isBooleanNode() && right.isBooleanNode()) {
                result = singleton(left.asBooleanNode().implies(right.asBooleanNode()));
            }
            
            indentLevel--;
            return result;
        }
    
        @Override
        public Collection<FHIRPathNode> visitTermExpression(FHIRPathParser.TermExpressionContext ctx) {
            debug(ctx);
            indentLevel++;
            Collection<FHIRPathNode> result = visitChildren(ctx);
            indentLevel--;
            return result;
        }
    
        @Override
        public Collection<FHIRPathNode> visitTypeExpression(FHIRPathParser.TypeExpressionContext ctx) {
            debug(ctx);
            indentLevel++;
            Collection<FHIRPathNode> result = visitChildren(ctx);
            indentLevel--;
            return result;
        }
    
        @Override
        public Collection<FHIRPathNode> visitInvocationTerm(FHIRPathParser.InvocationTermContext ctx) {
            debug(ctx);
            indentLevel++;
            Collection<FHIRPathNode> result = visitChildren(ctx);
            indentLevel--;
            return result;
        }
    
        @Override
        public Collection<FHIRPathNode> visitLiteralTerm(FHIRPathParser.LiteralTermContext ctx) {
            debug(ctx);
            indentLevel++;
            Collection<FHIRPathNode> result = visitChildren(ctx);
            indentLevel--;
            return result;
        }
    
        @Override
        public Collection<FHIRPathNode> visitExternalConstantTerm(FHIRPathParser.ExternalConstantTermContext ctx) {
            debug(ctx);
            indentLevel++;
            Collection<FHIRPathNode> result = visitChildren(ctx);
            indentLevel--;
            return result;
        }
    
        @Override
        public Collection<FHIRPathNode> visitParenthesizedTerm(FHIRPathParser.ParenthesizedTermContext ctx) {
            debug(ctx);
            indentLevel++;
            Collection<FHIRPathNode> result = visit(ctx.expression());
            indentLevel--;
            return result;
        }
    
        @Override
        public Collection<FHIRPathNode> visitNullLiteral(FHIRPathParser.NullLiteralContext ctx) {
            debug(ctx);
            return Collections.emptyList();
        }
    
        @Override
        public Collection<FHIRPathNode> visitBooleanLiteral(FHIRPathParser.BooleanLiteralContext ctx) {
            debug(ctx);
            return singleton(booleanNode(Boolean.valueOf(ctx.getText())));
        }
    
        @Override
        public Collection<FHIRPathNode> visitStringLiteral(FHIRPathParser.StringLiteralContext ctx) {
            debug(ctx);
            return singleton(stringNode(ctx.getText().substring(1, ctx.getText().length() - 1)));
        }
    
        @Override
        public Collection<FHIRPathNode> visitNumberLiteral(FHIRPathParser.NumberLiteralContext ctx) {
            debug(ctx);
            BigDecimal decimal = new BigDecimal(ctx.getText());
            try {
                Integer integer = decimal.intValueExact();
                return singleton(integerNode(integer));
            } catch (ArithmeticException e) {
                return singleton(decimalNode(decimal));
            }
        }
    
        @Override
        public Collection<FHIRPathNode> visitDateTimeLiteral(FHIRPathParser.DateTimeLiteralContext ctx) {
            debug(ctx);
            indentLevel++;
            Collection<FHIRPathNode> result = visitChildren(ctx);
            indentLevel--;
            return result;
        }
    
        @Override
        public Collection<FHIRPathNode> visitTimeLiteral(FHIRPathParser.TimeLiteralContext ctx) {
            debug(ctx);
            indentLevel++;
            Collection<FHIRPathNode> result = visitChildren(ctx);
            indentLevel--;
            return result;
        }
    
        @Override
        public Collection<FHIRPathNode> visitQuantityLiteral(FHIRPathParser.QuantityLiteralContext ctx) {
            debug(ctx);
            indentLevel++;
            Collection<FHIRPathNode> result = visitChildren(ctx);
            indentLevel--;
            return result;
        }
    
        @Override
        public Collection<FHIRPathNode> visitExternalConstant(FHIRPathParser.ExternalConstantContext ctx) {
            debug(ctx);
            indentLevel++;
            Collection<FHIRPathNode> result = visitChildren(ctx);
            indentLevel--;
            return result;
        }
    
        @Override
        public Collection<FHIRPathNode> visitMemberInvocation(FHIRPathParser.MemberInvocationContext ctx) {
            debug(ctx);
            indentLevel++;
            
            Collection<FHIRPathNode> currentContext = getCurrentContext();
            String identifier = getValue(visit(ctx.identifier())).asStringNode().string();
            currentContext = currentContext.stream()
                    .flatMap(node -> node.children().stream())
                    .filter(node -> identifier.equals(node.name()))
                    .collect(Collectors.toList());
            setCurrentContext(currentContext);
            
            indentLevel--;
            return currentContext;
        }
    
        @Override
        public Collection<FHIRPathNode> visitFunctionInvocation(FHIRPathParser.FunctionInvocationContext ctx) {
            debug(ctx);
            indentLevel++;
            Collection<FHIRPathNode> result = visitChildren(ctx);
            indentLevel--;
            return result;
        }
    
        @Override
        public Collection<FHIRPathNode> visitThisInvocation(FHIRPathParser.ThisInvocationContext ctx) {
            debug(ctx);
            indentLevel++;
            Collection<FHIRPathNode> result = visitChildren(ctx);
            indentLevel--;
            return result;
        }
    
        @Override
        public Collection<FHIRPathNode> visitIndexInvocation(FHIRPathParser.IndexInvocationContext ctx) {
            debug(ctx);
            indentLevel++;
            Collection<FHIRPathNode> result = visitChildren(ctx);
            indentLevel--;
            return result;
        }
    
        @Override
        public Collection<FHIRPathNode> visitTotalInvocation(FHIRPathParser.TotalInvocationContext ctx) {
            debug(ctx);
            indentLevel++;
            Collection<FHIRPathNode> result = visitChildren(ctx);
            indentLevel--;
            return result;
        }
    
        @Override
        public Collection<FHIRPathNode> visitFunction(FHIRPathParser.FunctionContext ctx) {
            debug(ctx);
            indentLevel++;
            
            Collection<FHIRPathNode> result = Collections.emptyList();
            
            String functionName = (String) getValue(visit(ctx.identifier())).asStringNode().string();

            List<ExpressionContext> arguments = new ArrayList<ExpressionContext>();
            ParamListContext paramList = ctx.paramList();
            if (paramList != null) {
                arguments.addAll(ctx.paramList().expression());
            }
            
            Collection<FHIRPathNode> currentContext = getCurrentContext();
            
            switch (functionName) {
            case "where":
                result = new ArrayList<>();
                ExpressionContext criteria = arguments.get(0);
                for (FHIRPathNode node : currentContext) {
                    pushContext(singleton(node));
                    if (getValue(visit(criteria)).asBooleanNode()._boolean()) {
                        result.add(node);
                    }
                    popContext();
                }
                break;
            case "exists":
                result = singleton(booleanNode(!currentContext.isEmpty()));
                break;
            case "empty":
                result = singleton(booleanNode(currentContext.isEmpty()));
                break;
            case "count":
                result = singleton(integerNode(currentContext.size()));
                break;
            }
            
            indentLevel--;
            return result;
        }
    
        @Override
        public Collection<FHIRPathNode> visitParamList(FHIRPathParser.ParamListContext ctx) {
            debug(ctx);
            indentLevel++;
            Collection<FHIRPathNode> result = visitChildren(ctx);
            indentLevel--;
            return result;
        }
    
        @Override
        public Collection<FHIRPathNode> visitQuantity(FHIRPathParser.QuantityContext ctx) {
            debug(ctx);
            indentLevel++;
            Collection<FHIRPathNode> result = visitChildren(ctx);
            indentLevel--;
            return result;
        }
    
        @Override
        public Collection<FHIRPathNode> visitUnit(FHIRPathParser.UnitContext ctx) {
            debug(ctx);
            indentLevel++;
            Collection<FHIRPathNode> result = visitChildren(ctx);
            indentLevel--;
            return result;
        }
    
        @Override
        public Collection<FHIRPathNode> visitDateTimePrecision(FHIRPathParser.DateTimePrecisionContext ctx) {
            debug(ctx);
            indentLevel++;
            Collection<FHIRPathNode> result = visitChildren(ctx);
            indentLevel--;
            return result;
        }
    
        @Override
        public Collection<FHIRPathNode> visitPluralDateTimePrecision(FHIRPathParser.PluralDateTimePrecisionContext ctx) {
            debug(ctx);
            indentLevel++;
            Collection<FHIRPathNode> result = visitChildren(ctx);
            indentLevel--;
            return result;
        }
    
        @Override
        public Collection<FHIRPathNode> visitTypeSpecifier(FHIRPathParser.TypeSpecifierContext ctx) {
            debug(ctx);
            indentLevel++;
            Collection<FHIRPathNode> result = visitChildren(ctx);
            indentLevel--;
            return result;
        }
    
        @Override
        public Collection<FHIRPathNode> visitQualifiedIdentifier(FHIRPathParser.QualifiedIdentifierContext ctx) {
            debug(ctx);
            indentLevel++;
            Collection<FHIRPathNode> result = visitChildren(ctx);
            indentLevel--;
            return result;
        }
    
        @Override
        public Collection<FHIRPathNode> visitIdentifier(FHIRPathParser.IdentifierContext ctx) {
            debug(ctx);
            return singleton(stringNode(ctx.getChild(0).getText()));
        }
    
        private String indent() {
            StringBuilder builder = new StringBuilder();
            for (int i = 0;i < indentLevel; i++) {
                builder.append("    ");
            }
            return builder.toString();
        }
    
        private void debug(ParseTree ctx) {
            if (DEBUG) {
                System.out.println(indent() + ctx.getClass().getName() + ": " + ctx.getText() + ", childCount: " + ctx.getChildCount());
            }
        }
    }
    
    public static void main(String[] args) throws Exception {
        FHIRPathEvaluator.DEBUG = true;
        Collection<FHIRPathNode> result = FHIRPathUtil.eval("(1 + 2).exists()");
        System.out.println(result);        
    }
}
