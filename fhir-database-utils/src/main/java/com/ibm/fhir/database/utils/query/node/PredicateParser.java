/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.database.utils.query.node;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Basically follows Dijkstra's shunting yard algorithm to ensure
 * correct handling of operator precedence as the expression nodes
 * are added
 */
public class PredicateParser {
    private static final Logger logger = Logger.getLogger(PredicateParser.class.getName());

    private static final String NEWLINE = System.lineSeparator();

    // Stacks used to hold expression nodes until they are ready to be shunted
    private final Stack<ExpNode> operatorStack = new Stack<>();
    private final Stack<ExpNode> operandStack = new Stack<>();

    // The final expression node after we've parsed it
    // private ExpNode parsedExpression = null;

    /**
     * Check if anything has been added to this parser
     * @return true if nothing has been added
     */
    public boolean isEmpty() {
        return operatorStack.isEmpty() && operandStack.isEmpty();
    }

    /**
     * Assumes there are no more expression nodes, so process any operators remaining
     * on the stack.
     * @return
     */
    public ExpNode parse() {
        // finish processing any remaining operators on the stack
        while (!operatorStack.isEmpty()) {
            ExpNode operator = operatorStack.pop();

            try {
                operator.popOperands(operandStack);
            } catch (Exception x) {
                logger.severe("Operator " + operator.getClass().getSimpleName() + " failed to pop operands: " + x.getMessage());
                throw x;
            }
            operandStack.push(operator);
        }

        if (operandStack.size() == 1) {
            // stack should contain a single node which represents the root of the expression tree
            return operandStack.peek();
        } else {
            StringBuilder msg = new StringBuilder();
            msg.append("Invalid expression - parse failed.")
            .append(NEWLINE).append("Current state:").append(NEWLINE).append(parserState());
            logger.severe(msg.toString());
            throw new IllegalStateException("Invalid expression");
        }
    }

    /**
     * Parse the collected tokens returning the root expression node.
     * @implNote this is a modified version of Dijkstra's shunting yard algorithm
     *           which maintains parentheses in the expression tree to make sure
     *           these are included when the query is rendered to a string.
     * @return
     */
    private void addNode(ExpNode token) {
        // We're not evaluating the expression here, just parsing it properly
        // into an expression tree matching precedence rules
//        if (this.parsedExpression != null) {
//            throw new IllegalStateException("Cannot modify an expression after calling parse()");
//        }

        try {
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

        } catch (Exception x) {
            // probably an empty stack. Dump the state of the parser
            StringBuilder msg = new StringBuilder();
            msg.append("parse failed: " + x.getMessage())
                .append(NEWLINE).append("Current state:").append(NEWLINE).append(parserState());
            logger.severe(msg.toString());
            throw x;
        }
    }

    /**
     * Get a list of the nodes as a string, to help debug expression building and parsing.
     * We don't use the visitor, because the expression node might not be fully configured
     * @return
     */
    public String parserState() {
        final StringBuilder result = new StringBuilder();

        int posn = 0;
        for (ExpNode token: this.operatorStack) {
            result.append(String.format("operator[%3d]: [%20s] %s\n", posn++, token.getClass().getSimpleName(), token.toString()));
        }
        result.append(NEWLINE);
        posn = 0;
        for (ExpNode token: this.operandStack) {
            result.append(String.format(" operand[%3d]: [%20s] %s\n", posn++, token.getClass().getSimpleName(), token.toString()));
        }

        return result.toString();
    }

    /**
     * Push a node representing the column reference onto the stack
     * @param columnName
     */
    public void column(String columnName) {
        addNode(new ColumnExpNode(columnName));
    }

    public void column(String tableAlias, String columnName) {
        addNode(new ColumnExpNode(tableAlias, columnName));
    }

    public void literal(String str) {
        addNode(new StringExpNode(str));
    }

    public void literal(Double dbl) {
        addNode(new DoubleExpNode(dbl));
    }

    public void literal(Long lng) {
        addNode(new LongExpNode(lng));
    }

    /**
     * Push an and node to the stack, taking into account precedence
     */
    public void and() {
        addNode(new AndExpNode());
    }

    /**
     * Push an or node to the stack, taking into account precedence
     */
    public void or() {
        addNode(new OrExpNode());
    }

    public void eq() {
        addNode(new EqExpNode());
    }

    public void like() {
        addNode(new LikeExpNode());
    }

    public void escape() {
        addNode(new EscapeExpNode());
    }

    public void neq() {
        addNode(new NeqExpNode());
    }

    public void gt() {
        addNode(new GreaterExpNode());
    }

    public void gte() {
        addNode(new GreaterEqExpNode());
    }

    public void lt() {
        addNode(new LessExpNode());
    }

    public void lte() {
        addNode(new LessEqExpNode());
    }

    public void not() {
        addNode(new NotExpNode());
    }

    public void between() {
        addNode(new BetweenExpNode());
    }

    public void add() {
        addNode(new AddExpNode());
    }

    public void sub() {
        addNode(new SubExpNode());
    }

    public void mult() {
        addNode(new MultExpNode());
    }

    public void div() {
        addNode(new DivExpNode());
    }

    public void leftParen() {
        addNode(new LeftParenExpNode());
    }

    public void rightParen() {
        addNode(new RightParenExpNode());
    }

    public void bindMarker(BindMarkerNode node) {
        addNode(node);
    }

    /**
     * Convenience function to add an IN list of literal string values. This should be used
     * only where the string values are internal values, never input data. If the values
     * come from input data, then for security it is important to use bind markers instead.
     * @param inList
     */
    public void inLiteral(String[] inList) {
        List<ExpNode> args = Arrays.asList(inList).stream().map(v -> new StringExpNode(v)).collect(Collectors.toList());
        addNode(new InListExpNode(args));
    }

    /**
     * Convenience function to add an IN list of literal long values.
     * @param inList
     */
    public void inLiteral(Long[] inList) {
        List<ExpNode> args = Arrays.asList(inList).stream().map(v -> new LongExpNode(v)).collect(Collectors.toList());
        addNode(new InListExpNode(args));
    }

    /**
     * Convenience function to add an IN list of `<bindMarkerCount>` bind markers
     * @param bindMarkerCount
     */
    public void inString(List<String> inList) {
        List<ExpNode> args = inList.stream().map(v -> new StringBindMarkerNode(v)).collect(Collectors.toList());
        addNode(new InListExpNode(args));
    }

    /**
     * Convenience function to add an IN list of `<bindMarkerCount>` bind markers
     * @param bindMarkerCount
     */
    public void inLong(List<Long> inList) {
        List<ExpNode> args = inList.stream().map(v -> new LongBindMarkerNode(v)).collect(Collectors.toList());
        addNode(new InListExpNode(args));
    }

    /**
     * Allow expression nodes to be added directly to the list of tokens. Wrap
     * non-operands in a paren to ensure the expression is treated atomically,
     * which is the intent of this method.
     * @param node
     */
    public void addToken(ExpNode token) {
        if (token.isOperand()) {
            addNode(token);
        } else {
            addNode(new ParenExpNode(token));
        }
    }

    /**
     * Add an IS NOT NULL node to the expression tokens
     */
    public void isNotNull() {
        addNode(new IsNotNullExpNode());
    }

    /**
     * Add an IS NULL node to the expression tokens
     */
    public void isNull() {
        addNode(new IsNullExpNode());
    }
}