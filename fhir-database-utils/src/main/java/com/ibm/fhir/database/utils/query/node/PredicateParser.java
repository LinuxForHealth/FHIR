/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Collects ExpNode elements and hold in a List, allowing the lookahead we need
 * to support precedence when parsing the expression
 */
public class PredicateParser {
    private static final Logger logger = Logger.getLogger(PredicateParser.class.getName());

    private static final String NEWLINE = System.lineSeparator();

    // collect expression tokens as a list
    private final List<ExpNode> tokens = new ArrayList<>();

    /**
     * Parse the collected tokens returning the root expression node.
     * @implNote this is a modified version of Dijkstra's shunting yard algorithm
     *           which maintains parentheses in the expression tree to make sure
     *           these are included when the query is rendered to a string.
     * @return
     */
    public ExpNode parse() {
        // We're not evaluating the expression here, just parsing it properly
        // into an expression tree matching precedence rules
        Stack<ExpNode> operatorStack = new Stack<>();
        Stack<ExpNode> operandStack = new Stack<>();

        int idx = 0;
        try {
            while (idx < tokens.size()) {
                ExpNode token = tokens.get(idx++);
                if (token.isOperand()) {
                    operandStack.push(token);
                } else if (token.isLeftParen()) {
                    operatorStack.push(token);
                } else if (token.isRightParen()) {
                    // Keep resolving the stack until we hit the matching open paren pushed earlier
                    while (!operatorStack.peek().isLeftParen()) {
                        ExpNode operator = operatorStack.pop();
                        // The operator knows how many operands it needs
                        operator.popOperands(operandStack);

                        // The operator now becomes an operand for the next operator
                        operandStack.push(operator);
                    }

                    // In order to correctly render the statement as a string, we need to
                    // wrap the node we've just built in a ParenExpNode
                    operandStack.push(new ParenExpNode(operandStack.pop()));

                    // discard the left paren node still on the operatorStack
                    operatorStack.pop();
                } else if (token.isOperator()) {
                    // Keep processing the current stack while the precedence of the operators is
                    // higher than the node
                    while (!operatorStack.isEmpty() && !operatorStack.peek().isLeftParen() && operatorStack.peek().precedence() <= token.precedence()) {
                        ExpNode operator = operatorStack.pop();
                        operator.popOperands(operandStack);
                        operandStack.push(operator);
                    }
                    operatorStack.push(token);
                } else {
                    // This is a programming error, not an invalid expression
                    throw new IllegalStateException("Invalid ExpNode token implementation: [" + token.getClass().getName() + "] '" + token.toString() + "'");
                }
            }

            while (!operatorStack.isEmpty()) {
                ExpNode operator = operatorStack.pop();
                operator.popOperands(operandStack);
                operandStack.push(operator);
            }
        } catch (Exception x) {
            // probably an empty stack. Dump the state of the parser
            StringBuilder msg = new StringBuilder();
            msg.append("parse failed on token [" + (idx+1) + "]: ").append(x.getMessage())
                .append(NEWLINE).append("              tokens := ").append(toTokenString())
                .append(NEWLINE).append(" operandStack.size() := ").append(operandStack.size())
                .append(NEWLINE).append("operatorStack.size() := ").append(operatorStack.size());
            logger.severe(msg.toString());
            throw x;
        }

        // stack should contain a single node which represents the root of the expression tree
        final ExpNode result;
        if (!operandStack.isEmpty()) {
            result = operandStack.pop();
        } else {
            // nothing to process
            result = null;
        }

        return result;
    }

    /**
     * Get a list of the nodes as a string, to help debug expression building and parsing.
     * We don't use the visitor, because the expression node might not be fully configured
     * @return
     */
    public String toTokenString() {
        final StringBuilder result = new StringBuilder();
        for (ExpNode token: this.tokens) {
            if (result.length() > 0) {
                result.append(" ");
            }

            if (token != null) {
                result.append(token.getClass().getSimpleName()).append("(");
                result.append(token.toString());
                result.append(")");
            } else {
                // I don't think we can have null tokens...but
                result.append("~");
            }
        }

        return result.toString();
    }

    /**
     * Push a node representing the column reference onto the stack
     * @param columnName
     */
    public void column(String columnName) {
        tokens.add(new ColumnExpNode(columnName));
    }

    public void column(String tableAlias, String columnName) {
        tokens.add(new ColumnExpNode(tableAlias, columnName));
    }

    public void literal(String str) {
        tokens.add(new StringExpNode(str));
    }

    public void literal(Double dbl) {
        tokens.add(new DoubleExpNode(dbl));
    }

    public void literal(Long lng) {
        tokens.add(new LongExpNode(lng));
    }

    /**
     * Push an and node to the stack, taking into account precedence
     */
    public void and() {
        tokens.add(new AndExpNode());
    }

    /**
     * Push an or node to the stack, taking into account precedence
     */
    public void or() {
        tokens.add(new OrExpNode());
    }

    public void eq() {
        tokens.add(new EqExpNode());
    }

    public void like() {
        tokens.add(new LikeExpNode());
    }

    public void escape() {
        tokens.add(new EscapeExpNode());
    }

    public void neq() {
        tokens.add(new NeqExpNode());
    }

    public void gt() {
        tokens.add(new GreaterExpNode());
    }

    public void gte() {
        tokens.add(new GreaterEqExpNode());
    }

    public void lt() {
        tokens.add(new LessExpNode());
    }

    public void lte() {
        tokens.add(new LessEqExpNode());
    }

    public void not() {
        tokens.add(new NotExpNode());
    }

    public void between() {
        tokens.add(new BetweenExpNode());
    }

    public void add() {
        tokens.add(new AddExpNode());
    }

    public void sub() {
        tokens.add(new SubExpNode());
    }

    public void mult() {
        tokens.add(new MultExpNode());
    }

    public void div() {
        tokens.add(new DivExpNode());
    }

    public void leftParen() {
        tokens.add(new LeftParenExpNode());
    }

    public void rightParen() {
        tokens.add(new RightParenExpNode());
    }

    public void bindMarker(BindMarkerNode node) {
        tokens.add(node);
    }

    /**
     * Convenience function to add an IN list of literal string values. This should be used
     * only where the string values are internal values, never input data. If the values
     * come from input data, then for security it is important to use bind markers instead.
     * @param inList
     */
    public void inLiteral(String[] inList) {
        List<ExpNode> args = Arrays.asList(inList).stream().map(v -> new StringExpNode(v)).collect(Collectors.toList());
        tokens.add(new InListExpNode(args));
    }

    /**
     * Convenience function to add an IN list of literal long values.
     * @param inList
     */
    public void inLiteral(Long[] inList) {
        List<ExpNode> args = Arrays.asList(inList).stream().map(v -> new LongExpNode(v)).collect(Collectors.toList());
        tokens.add(new InListExpNode(args));
    }

    /**
     * Convenience function to add an IN list of `<bindMarkerCount>` bind markers
     * @param bindMarkerCount
     */
    public void inString(List<String> inList) {
        List<ExpNode> args = inList.stream().map(v -> new StringBindMarkerNode(v)).collect(Collectors.toList());
        tokens.add(new InListExpNode(args));
    }

    /**
     * Convenience function to add an IN list of `<bindMarkerCount>` bind markers
     * @param bindMarkerCount
     */
    public void inLong(List<Long> inList) {
        List<ExpNode> args = inList.stream().map(v -> new LongBindMarkerNode(v)).collect(Collectors.toList());
        tokens.add(new InListExpNode(args));
    }

    /**
     * Allow expression nodes to be added directly to the list of tokens. Wrap
     * non-operands in a paren to ensure the expression is treated atomically,
     * which is the intent of this method.
     * @param node
     */
    public void addToken(ExpNode token) {
        if (token.isOperand()) {
            tokens.add(token);
        } else {
            tokens.add(new ParenExpNode(token));
        }
    }

    /**
     * Add an IS NOT NULL node to the expression tokens
     */
    public void isNotNull() {
        tokens.add(new IsNotNullExpNode());
    }

    /**
     * Add an IS NULL node to the expression tokens
     */
    public void isNull() {
        tokens.add(new IsNullExpNode());
    }
}