/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.test.mains;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.test.BaseSearchTest;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * Created prior to R4.
 * 
 * @author markd
 * @author pbastide
 *
 */
public class ExpressionTree extends BaseSearchTest {

    public static abstract class Expression {
        abstract void accept(Visitor visitor);
    }

    public static abstract class BinaryExpression extends Expression {
        public enum Operator {
            AND, OR, EQ, GT, LT, GE, LE
        };

        protected Operator operator = null;
        protected Expression left = null;
        protected Expression right = null;

        public BinaryExpression(Operator operator, Expression left, Expression right) {
            this.operator = operator;
            this.left = left;
            this.right = right;
        }

        public Operator getOperator() {
            return operator;
        }

        public Expression getLeft() {
            return left;
        }

        public Expression getRight() {
            return right;
        }

        public void accept(Visitor visitor) {
            visitor.visit(this);
        }
    }

    public static class UnaryExpression extends Expression {
        public enum Operator {
            NOT
        };

        protected Operator operator = null;
        protected Expression operand = null;

        public UnaryExpression(Operator operator, Expression operand) {
            this.operator = operator;
            this.operand = operand;
        }

        @Override
        void accept(Visitor visitor) {
            visitor.visit(operand);
        }
    }

    public static class AndExpression extends BinaryExpression {
        public AndExpression(Expression left, Expression right) {
            super(BinaryExpression.Operator.AND, left, right);
        }
    }

    public interface Visitor {
        void visit(Expression expression);

        void visit(BinaryExpression expression);

        void visit(UnaryExpression expression);

        void visit(AndExpression expression);

        void visit(ParameterReference expression);
    }

    public static class ParameterReference extends Expression {
        private QueryParameter parameter = null;

        public ParameterReference(QueryParameter parameter) {
            this.parameter = parameter;
        }

        public QueryParameter getParameter() {
            return parameter;
        }

        @Override
        void accept(Visitor visitor) {
            visitor.visit(this);
        }
    }

    /**
     * tests multiple languages
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Map<String, List<String>> queryParameters = new HashMap<String, List<String>>();
        queryParameters.put("language", Arrays.asList("FR,NL", "EN"));
        List<QueryParameter> parameters = SearchUtil.parseQueryParameters(Patient.class, queryParameters).getSearchParameters();

        Expression left = null, right = null;

        AndExpression and = null;

        for (QueryParameter parameter : parameters) {
            System.out.println(parameter);
            ParameterReference reference = new ParameterReference(parameter);
            if (left == null && right == null) {
                left = reference;
            } else if (left != null && right == null) {
                right = reference;
            } else if (left != null && right != null) {
                and = new AndExpression(left, right);
                left = and;
                right = null;
            }
        }

    }

}
