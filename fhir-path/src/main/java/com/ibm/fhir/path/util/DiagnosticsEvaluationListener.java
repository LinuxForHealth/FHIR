/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.util;

import static com.ibm.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_FALSE;
import static com.ibm.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_TRUE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Predicate;

import org.antlr.v4.runtime.ParserRuleContext;

import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.FHIRPathParser.AndExpressionContext;
import com.ibm.fhir.path.FHIRPathParser.EqualityExpressionContext;
import com.ibm.fhir.path.FHIRPathParser.FunctionContext;
import com.ibm.fhir.path.FHIRPathParser.FunctionInvocationContext;
import com.ibm.fhir.path.FHIRPathParser.IdentifierContext;
import com.ibm.fhir.path.FHIRPathParser.ImpliesExpressionContext;
import com.ibm.fhir.path.FHIRPathParser.InequalityExpressionContext;
import com.ibm.fhir.path.FHIRPathParser.InvocationExpressionContext;
import com.ibm.fhir.path.FHIRPathParser.MembershipExpressionContext;
import com.ibm.fhir.path.FHIRPathParser.OrExpressionContext;
import com.ibm.fhir.path.FHIRPathParser.TypeExpressionContext;
import com.ibm.fhir.path.util.EvaluationResultTree.BuildingListener;
import com.ibm.fhir.path.util.EvaluationResultTree.Node;

public class DiagnosticsEvaluationListener extends BuildingListener {
    private static final Set<String> FUNCTION_NAMES = new HashSet<>(Arrays.asList(
        "where",
        "not",
        "all",
        "exists",
        "is",
        "memberOf",
        "conformsTo"
    ));
    private static final Set<Class<?>> PARSER_RULE_CONTEXT_TYPES = new HashSet<>(Arrays.asList(
        OrExpressionContext.class,
        AndExpressionContext.class,
        MembershipExpressionContext.class,
        InequalityExpressionContext.class,
        EqualityExpressionContext.class,
        ImpliesExpressionContext.class
    ));
    private static final Predicate<ParserRuleContext> DIAGNOSTICS_PARSER_RULE_CONTEXT_PREDICATE = new Predicate<ParserRuleContext>() {
        @Override
        public boolean test(ParserRuleContext parserRuleContext) {
            if (parserRuleContext instanceof InvocationExpressionContext) {
                InvocationExpressionContext invocationExpressionContext = (InvocationExpressionContext) parserRuleContext;
                if (invocationExpressionContext.invocation() instanceof FunctionInvocationContext) {
                    FunctionInvocationContext functionInvocationContext = (FunctionInvocationContext) invocationExpressionContext.invocation();
                    FunctionContext functionContext = functionInvocationContext.function();
                    String identifier = getIdentifier(functionContext.identifier());
                    return FUNCTION_NAMES.contains(identifier);
                }
            }
            if (parserRuleContext instanceof TypeExpressionContext) {
                TypeExpressionContext typeExpressionContext = (TypeExpressionContext) parserRuleContext;
                String operator = typeExpressionContext.getChild(1).getText();
                return "is".equals(operator);
            }
            return PARSER_RULE_CONTEXT_TYPES.contains(parserRuleContext.getClass());
        }
    };

    public DiagnosticsEvaluationListener() {
       super(DIAGNOSTICS_PARSER_RULE_CONTEXT_PREDICATE);
    }

    public String getDiagnostics() {
        if (resultTree == null) {
            return null;
        }
        Node root = resultTree.getRoot();
        List<Node> nodes = traverse(root);
        if (!nodes.isEmpty()) {
            StringJoiner joiner = new StringJoiner(", ", "Caused by: [", "]");
            for (Node node : nodes) {
                List<String> paths = getPaths(node.getContext());
                joiner.add(String.format("[expression: %s, result: false, location: %s]", node.getText(), !paths.isEmpty() ? paths.get(0) : "<unknown>"));
            }
            return joiner.toString();
        }
        return null;
    }

    private List<String> getPaths(Collection<FHIRPathNode> context) {
        List<String> paths = new ArrayList<>();
        for (FHIRPathNode node : context) {
            paths.add(node.path());
        }
        return paths;
    }

    private boolean isBooleanResult(Collection<FHIRPathNode> result) {
        return SINGLETON_TRUE.equals(result) || SINGLETON_FALSE.equals(result);
    }

    private boolean isFunctionInvocation(InvocationExpressionContext invocationExpressionContext, String functionName) {
        if (invocationExpressionContext.invocation() instanceof FunctionInvocationContext) {
            FunctionInvocationContext functionInvocationContext = (FunctionInvocationContext) invocationExpressionContext.invocation();
            FunctionContext functionContext = functionInvocationContext.function();
            String identifier = getIdentifier(functionContext.identifier());
            return identifier.equals(functionName);
        }
        return false;
    }

    private boolean isFunctionInvocation(ParserRuleContext parserRuleContext, String functionName) {
        if (parserRuleContext instanceof InvocationExpressionContext) {
            return isFunctionInvocation((InvocationExpressionContext) parserRuleContext, functionName);
        }
        return false;
    }

    private List<Node> traverse(Node node) {
        List<Node> result = new ArrayList<>();
        if (SINGLETON_FALSE.equals(node.getResult()) || !isBooleanResult(node.getResult())) {
            if (SINGLETON_FALSE.equals(node.getResult()) && node.isLeaf()) {
                result.add(node);
            }
            if (!isFunctionInvocation(node.getParserRuleContext(), "where") || node.getResult().isEmpty()) {
                // traverse children
                for (Node child : node.getChildren()) {
                    result.addAll(traverse(child));
                }
            }
        }
        return result;
    }

    private static String getIdentifier(IdentifierContext identifierContext) {
        String identifier = identifierContext.getText();
        if (identifier.startsWith("`")) {
            identifier = identifier.substring(1, identifier.length() - 1);
        }
        return identifier;
    }
}