/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.database.utils.query.node;

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

    // newline character for clearer errors and warnings
    private static final String NEWLINE = System.lineSeparator();

    // Stacks used to hold expression nodes until they are ready to be shunted
    // TODO Stack is synchronized...consider alternatives
    private final Stack<ExpNode> operatorStack = new Stack<>();
    private final Stack<ExpNode> operandStack = new Stack<>();

    /**
     * Default constructor
     */
    public PredicateParser() {
    }

    /**
     * Check if anything has been added to this parser
     * @return true if nothing has been added
     */
    public boolean isEmpty() {
        return operatorStack.isEmpty() && operandStack.isEmpty();
    }

    /**
     * Applies the shunting-yard algorithm, collapsing the operatorStack
     * as much as possible. If the expression is valid, there should be
     * a single node sitting on the operandStack.
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
                .append(NEWLINE).append("Current state:")
                .append(NEWLINE).append(parserState());
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

    /**
     * Add a column to the expression
     * @param tableAlias
     * @param columnName
     */
    public void column(String tableAlias, String columnName) {
        addNode(new ColumnExpNode(tableAlias, columnName));
    }

    /**
     * Add a string literal to the expression
     * @param str
     */
    public void literal(String str) {
        addNode(new StringExpNode(str));
    }

    /**
     * Add a Double literal to the expression
     * @param dbl
     */
    public void literal(Double dbl) {
        addNode(new DoubleExpNode(dbl));
    }

    /**
     * Add a Long literal to the expression
     * @param lng
     */
    public void literal(Long lng) {
        addNode(new LongExpNode(lng));
    }

    /**
     * Push an AND node to the stack, taking into account precedence
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

    /**
     * Add an EQ node to the expression
     */
    public void eq() {
        addNode(new EqExpNode());
    }

    /**
     * Add a LIKE node to the expression
     */
    public void like() {
        addNode(new LikeExpNode());
    }

    /**
     * Add an ESCAPE node to the expression
     */
    public void escape() {
        addNode(new EscapeExpNode());
    }

    /**
     * Add a NEQ node to the expression
     */
    public void neq() {
        addNode(new NeqExpNode());
    }

    /**
     * Add a GT > node to the expression
     */
    public void gt() {
        addNode(new GreaterExpNode());
    }

    /**
     * Add a GTE >= node to the expression
     */
    public void gte() {
        addNode(new GreaterEqExpNode());
    }

    /**
     * Add a LT < node to the expression
     */
    public void lt() {
        addNode(new LessExpNode());
    }

    /**
     * Add a LTE <= node to the expression
     */
    public void lte() {
        addNode(new LessEqExpNode());
    }

    /**
     * Add a NOT node to the expression
     */
    public void not() {
        addNode(new NotExpNode());
    }

    /**
     * Add a BETWEEN node to the expression
     */
    public void between() {
        addNode(new BetweenExpNode());
    }

    /**
     * Add an addition + node to the expression
     */
    public void add() {
        addNode(new AddExpNode());
    }

    /**
     * Add a subtraction node to the expression
     */
    public void sub() {
        addNode(new SubExpNode());
    }

    /**
     * Add a multiplication node to the expression
     */
    public void mult() {
        addNode(new MultExpNode());
    }

    /**
     * Add a division node to the expression
     */
    public void div() {
        addNode(new DivExpNode());
    }

    /**
     * Add a LEFT PAREN ( node to the expression
     */
    public void leftParen() {
        addNode(new LeftParenExpNode());
    }

    /**
     * Add a RIGHT PAREN ) node to the expression
     */
    public void rightParen() {
        addNode(new RightParenExpNode());
    }

    /**
     * Add a bind marker ? and its value to the expression
     * @param node
     */
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
        // Parameters are encoded for security, but bind markers are always preferred and
        // recommended for security.
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