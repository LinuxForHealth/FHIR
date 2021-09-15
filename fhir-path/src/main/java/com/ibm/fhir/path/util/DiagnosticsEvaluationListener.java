/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.util;

import static com.ibm.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_FALSE;
import static com.ibm.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_TRUE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;

import org.antlr.v4.runtime.ParserRuleContext;

import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.FHIRPathParser.ExpressionContext;
import com.ibm.fhir.path.FHIRPathParser.FunctionContext;
import com.ibm.fhir.path.FHIRPathParser.FunctionInvocationContext;
import com.ibm.fhir.path.FHIRPathParser.InvocationExpressionContext;
import com.ibm.fhir.path.FHIRPathParser.TermExpressionContext;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationListener;
import com.ibm.fhir.path.util.EvaluationResultTree.BuildingEvaluationListener;
import com.ibm.fhir.path.util.EvaluationResultTree.Node;

/**
 * An {@link EvaluationListener} that produces diagnostics from an {@link EvaluationResultTree}
 */
public class DiagnosticsEvaluationListener extends BuildingEvaluationListener {
    public DiagnosticsEvaluationListener() {
        super(t -> ExpressionContext.class.isAssignableFrom(t.getClass()) && !TermExpressionContext.class.equals(t.getClass()));
    }

    /**
     * Get the diagnostics produced by this evaluation listener.
     *
     * @return
     *     the diagnostics
     */
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

    private boolean hasChildResult(Node node, Collection<FHIRPathNode> result) {
        for (Node child : node.getChildren()) {
            if (child.getResult().equals(result)) {
                return true;
            }
        }
        return false;
    }

    private boolean isBooleanResult(Collection<FHIRPathNode> result) {
        return SINGLETON_TRUE.equals(result) || SINGLETON_FALSE.equals(result);
    }

    private boolean isFunctionInvocation(ParserRuleContext parserRuleContext, String functionName) {
        if (parserRuleContext instanceof InvocationExpressionContext) {
            InvocationExpressionContext invocationExpressionContext = (InvocationExpressionContext) parserRuleContext;
            if (invocationExpressionContext.invocation() instanceof FunctionInvocationContext) {
                FunctionInvocationContext functionInvocationContext = (FunctionInvocationContext) invocationExpressionContext.invocation();
                FunctionContext functionContext = functionInvocationContext.function();
                String identifier = functionContext.identifier().getText();
                if (identifier.startsWith("`")) {
                    identifier = identifier.substring(1, identifier.length() - 1);
                }
                return identifier.equals(functionName);
            }
        }
        return false;
    }

    private List<Node> traverse(Node node) {
        List<Node> result = new ArrayList<>();
        if (SINGLETON_FALSE.equals(node.getResult()) || !isBooleanResult(node.getResult())) {
            if (SINGLETON_FALSE.equals(node.getResult()) && (node.isLeaf() || !hasChildResult(node, SINGLETON_FALSE))) {
                result.add(node);
            } else if (!isFunctionInvocation(node.getParserRuleContext(), "where") || node.getResult().isEmpty()) {
                // traverse children
                for (Node child : node.getChildren()) {
                    result.addAll(traverse(child));
                }
            }
        }
        return result;
    }
}