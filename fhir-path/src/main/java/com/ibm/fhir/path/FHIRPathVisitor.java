/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

// Generated from FHIRPath.g4 by ANTLR 4.9.1
package com.ibm.fhir.path;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link FHIRPathParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface FHIRPathVisitor<T> extends ParseTreeVisitor<T> {
    /**
     * Visit a parse tree produced by the {@code indexerExpression}
     * labeled alternative in {@link FHIRPathParser#expression}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIndexerExpression(FHIRPathParser.IndexerExpressionContext ctx);
    /**
     * Visit a parse tree produced by the {@code polarityExpression}
     * labeled alternative in {@link FHIRPathParser#expression}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitPolarityExpression(FHIRPathParser.PolarityExpressionContext ctx);
    /**
     * Visit a parse tree produced by the {@code additiveExpression}
     * labeled alternative in {@link FHIRPathParser#expression}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitAdditiveExpression(FHIRPathParser.AdditiveExpressionContext ctx);
    /**
     * Visit a parse tree produced by the {@code multiplicativeExpression}
     * labeled alternative in {@link FHIRPathParser#expression}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitMultiplicativeExpression(FHIRPathParser.MultiplicativeExpressionContext ctx);
    /**
     * Visit a parse tree produced by the {@code unionExpression}
     * labeled alternative in {@link FHIRPathParser#expression}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitUnionExpression(FHIRPathParser.UnionExpressionContext ctx);
    /**
     * Visit a parse tree produced by the {@code orExpression}
     * labeled alternative in {@link FHIRPathParser#expression}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitOrExpression(FHIRPathParser.OrExpressionContext ctx);
    /**
     * Visit a parse tree produced by the {@code andExpression}
     * labeled alternative in {@link FHIRPathParser#expression}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitAndExpression(FHIRPathParser.AndExpressionContext ctx);
    /**
     * Visit a parse tree produced by the {@code membershipExpression}
     * labeled alternative in {@link FHIRPathParser#expression}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitMembershipExpression(FHIRPathParser.MembershipExpressionContext ctx);
    /**
     * Visit a parse tree produced by the {@code inequalityExpression}
     * labeled alternative in {@link FHIRPathParser#expression}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitInequalityExpression(FHIRPathParser.InequalityExpressionContext ctx);
    /**
     * Visit a parse tree produced by the {@code invocationExpression}
     * labeled alternative in {@link FHIRPathParser#expression}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitInvocationExpression(FHIRPathParser.InvocationExpressionContext ctx);
    /**
     * Visit a parse tree produced by the {@code equalityExpression}
     * labeled alternative in {@link FHIRPathParser#expression}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitEqualityExpression(FHIRPathParser.EqualityExpressionContext ctx);
    /**
     * Visit a parse tree produced by the {@code impliesExpression}
     * labeled alternative in {@link FHIRPathParser#expression}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitImpliesExpression(FHIRPathParser.ImpliesExpressionContext ctx);
    /**
     * Visit a parse tree produced by the {@code termExpression}
     * labeled alternative in {@link FHIRPathParser#expression}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitTermExpression(FHIRPathParser.TermExpressionContext ctx);
    /**
     * Visit a parse tree produced by the {@code typeExpression}
     * labeled alternative in {@link FHIRPathParser#expression}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitTypeExpression(FHIRPathParser.TypeExpressionContext ctx);
    /**
     * Visit a parse tree produced by the {@code invocationTerm}
     * labeled alternative in {@link FHIRPathParser#term}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitInvocationTerm(FHIRPathParser.InvocationTermContext ctx);
    /**
     * Visit a parse tree produced by the {@code literalTerm}
     * labeled alternative in {@link FHIRPathParser#term}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitLiteralTerm(FHIRPathParser.LiteralTermContext ctx);
    /**
     * Visit a parse tree produced by the {@code externalConstantTerm}
     * labeled alternative in {@link FHIRPathParser#term}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitExternalConstantTerm(FHIRPathParser.ExternalConstantTermContext ctx);
    /**
     * Visit a parse tree produced by the {@code parenthesizedTerm}
     * labeled alternative in {@link FHIRPathParser#term}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitParenthesizedTerm(FHIRPathParser.ParenthesizedTermContext ctx);
    /**
     * Visit a parse tree produced by the {@code nullLiteral}
     * labeled alternative in {@link FHIRPathParser#literal}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNullLiteral(FHIRPathParser.NullLiteralContext ctx);
    /**
     * Visit a parse tree produced by the {@code booleanLiteral}
     * labeled alternative in {@link FHIRPathParser#literal}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitBooleanLiteral(FHIRPathParser.BooleanLiteralContext ctx);
    /**
     * Visit a parse tree produced by the {@code stringLiteral}
     * labeled alternative in {@link FHIRPathParser#literal}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitStringLiteral(FHIRPathParser.StringLiteralContext ctx);
    /**
     * Visit a parse tree produced by the {@code numberLiteral}
     * labeled alternative in {@link FHIRPathParser#literal}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNumberLiteral(FHIRPathParser.NumberLiteralContext ctx);
    /**
     * Visit a parse tree produced by the {@code dateLiteral}
     * labeled alternative in {@link FHIRPathParser#literal}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitDateLiteral(FHIRPathParser.DateLiteralContext ctx);
    /**
     * Visit a parse tree produced by the {@code dateTimeLiteral}
     * labeled alternative in {@link FHIRPathParser#literal}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitDateTimeLiteral(FHIRPathParser.DateTimeLiteralContext ctx);
    /**
     * Visit a parse tree produced by the {@code timeLiteral}
     * labeled alternative in {@link FHIRPathParser#literal}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitTimeLiteral(FHIRPathParser.TimeLiteralContext ctx);
    /**
     * Visit a parse tree produced by the {@code quantityLiteral}
     * labeled alternative in {@link FHIRPathParser#literal}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitQuantityLiteral(FHIRPathParser.QuantityLiteralContext ctx);
    /**
     * Visit a parse tree produced by {@link FHIRPathParser#externalConstant}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitExternalConstant(FHIRPathParser.ExternalConstantContext ctx);
    /**
     * Visit a parse tree produced by the {@code memberInvocation}
     * labeled alternative in {@link FHIRPathParser#invocation}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitMemberInvocation(FHIRPathParser.MemberInvocationContext ctx);
    /**
     * Visit a parse tree produced by the {@code functionInvocation}
     * labeled alternative in {@link FHIRPathParser#invocation}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitFunctionInvocation(FHIRPathParser.FunctionInvocationContext ctx);
    /**
     * Visit a parse tree produced by the {@code thisInvocation}
     * labeled alternative in {@link FHIRPathParser#invocation}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitThisInvocation(FHIRPathParser.ThisInvocationContext ctx);
    /**
     * Visit a parse tree produced by the {@code indexInvocation}
     * labeled alternative in {@link FHIRPathParser#invocation}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIndexInvocation(FHIRPathParser.IndexInvocationContext ctx);
    /**
     * Visit a parse tree produced by the {@code totalInvocation}
     * labeled alternative in {@link FHIRPathParser#invocation}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitTotalInvocation(FHIRPathParser.TotalInvocationContext ctx);
    /**
     * Visit a parse tree produced by {@link FHIRPathParser#function}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitFunction(FHIRPathParser.FunctionContext ctx);
    /**
     * Visit a parse tree produced by {@link FHIRPathParser#paramList}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitParamList(FHIRPathParser.ParamListContext ctx);
    /**
     * Visit a parse tree produced by {@link FHIRPathParser#quantity}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitQuantity(FHIRPathParser.QuantityContext ctx);
    /**
     * Visit a parse tree produced by {@link FHIRPathParser#unit}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitUnit(FHIRPathParser.UnitContext ctx);
    /**
     * Visit a parse tree produced by {@link FHIRPathParser#dateTimePrecision}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitDateTimePrecision(FHIRPathParser.DateTimePrecisionContext ctx);
    /**
     * Visit a parse tree produced by {@link FHIRPathParser#pluralDateTimePrecision}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitPluralDateTimePrecision(FHIRPathParser.PluralDateTimePrecisionContext ctx);
    /**
     * Visit a parse tree produced by {@link FHIRPathParser#typeSpecifier}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitTypeSpecifier(FHIRPathParser.TypeSpecifierContext ctx);
    /**
     * Visit a parse tree produced by {@link FHIRPathParser#qualifiedIdentifier}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitQualifiedIdentifier(FHIRPathParser.QualifiedIdentifierContext ctx);
    /**
     * Visit a parse tree produced by {@link FHIRPathParser#identifier}.
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIdentifier(FHIRPathParser.IdentifierContext ctx);
}