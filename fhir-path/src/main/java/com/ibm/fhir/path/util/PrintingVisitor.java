/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.util;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import org.antlr.v4.runtime.tree.ParseTree;

import com.ibm.fhir.path.FHIRPathBaseVisitor;
import com.ibm.fhir.path.FHIRPathParser;

public class PrintingVisitor extends FHIRPathBaseVisitor<Object> {
    int indentLevel = 0;

    private final PrintWriter writer;

    public PrintingVisitor(OutputStream out) {
        this(new OutputStreamWriter(out, StandardCharsets.UTF_8));
    }

    public PrintingVisitor(Writer writer) {
        this.writer = new PrintWriter(writer, true);
    }

    private String indent() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < indentLevel; i++) {
            builder.append("    ");
        }
        return builder.toString();
    }

    private void print(ParseTree ctx) {
        writer.println(indent() + ctx.getClass().getSimpleName() + ": " + ctx.getText() + ", childCount: " + ctx.getChildCount());
    }

    @Override
    public Object visitIndexerExpression(FHIRPathParser.IndexerExpressionContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitPolarityExpression(FHIRPathParser.PolarityExpressionContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitAdditiveExpression(FHIRPathParser.AdditiveExpressionContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitMultiplicativeExpression(FHIRPathParser.MultiplicativeExpressionContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitUnionExpression(FHIRPathParser.UnionExpressionContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitOrExpression(FHIRPathParser.OrExpressionContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitAndExpression(FHIRPathParser.AndExpressionContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitMembershipExpression(FHIRPathParser.MembershipExpressionContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitInequalityExpression(FHIRPathParser.InequalityExpressionContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitInvocationExpression(FHIRPathParser.InvocationExpressionContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitEqualityExpression(FHIRPathParser.EqualityExpressionContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitImpliesExpression(FHIRPathParser.ImpliesExpressionContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitTermExpression(FHIRPathParser.TermExpressionContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitTypeExpression(FHIRPathParser.TypeExpressionContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitInvocationTerm(FHIRPathParser.InvocationTermContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitLiteralTerm(FHIRPathParser.LiteralTermContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitExternalConstantTerm(FHIRPathParser.ExternalConstantTermContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitParenthesizedTerm(FHIRPathParser.ParenthesizedTermContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitNullLiteral(FHIRPathParser.NullLiteralContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitBooleanLiteral(FHIRPathParser.BooleanLiteralContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitStringLiteral(FHIRPathParser.StringLiteralContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitNumberLiteral(FHIRPathParser.NumberLiteralContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitDateTimeLiteral(FHIRPathParser.DateTimeLiteralContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitTimeLiteral(FHIRPathParser.TimeLiteralContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitQuantityLiteral(FHIRPathParser.QuantityLiteralContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitExternalConstant(FHIRPathParser.ExternalConstantContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitMemberInvocation(FHIRPathParser.MemberInvocationContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitFunctionInvocation(FHIRPathParser.FunctionInvocationContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitThisInvocation(FHIRPathParser.ThisInvocationContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitIndexInvocation(FHIRPathParser.IndexInvocationContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }


    @Override
    public Object visitTotalInvocation(FHIRPathParser.TotalInvocationContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitFunction(FHIRPathParser.FunctionContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitParamList(FHIRPathParser.ParamListContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitQuantity(FHIRPathParser.QuantityContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitUnit(FHIRPathParser.UnitContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitDateTimePrecision(FHIRPathParser.DateTimePrecisionContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitPluralDateTimePrecision(FHIRPathParser.PluralDateTimePrecisionContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitTypeSpecifier(FHIRPathParser.TypeSpecifierContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitQualifiedIdentifier(FHIRPathParser.QualifiedIdentifierContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }

    @Override
    public Object visitIdentifier(FHIRPathParser.IdentifierContext ctx) {
        print(ctx);
        indentLevel++;
        Object result = visitChildren(ctx);
        indentLevel--;
        return result;
    }
}
