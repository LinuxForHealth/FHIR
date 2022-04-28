/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

// Generated from fhir-path/FHIRPath.g4 by ANTLR 4.10.1
package com.ibm.fhir.path;
import java.util.List;

import org.antlr.v4.runtime.FailedPredicateException;
import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class FHIRPathParser extends Parser {
    static { RuntimeMetaData.checkVersion("4.10.1", RuntimeMetaData.VERSION); }

    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache =
        new PredictionContextCache();
    public static final int
        T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9,
        T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17,
        T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24,
        T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31,
        T__31=32, T__32=33, T__33=34, T__34=35, T__35=36, T__36=37, T__37=38,
        T__38=39, T__39=40, T__40=41, T__41=42, T__42=43, T__43=44, T__44=45,
        T__45=46, T__46=47, T__47=48, T__48=49, T__49=50, T__50=51, T__51=52,
        T__52=53, T__53=54, DATE=55, DATETIME=56, TIME=57, IDENTIFIER=58, DELIMITEDIDENTIFIER=59,
        STRING=60, NUMBER=61, WS=62, COMMENT=63, LINE_COMMENT=64;
    public static final int
        RULE_expression = 0, RULE_term = 1, RULE_literal = 2, RULE_externalConstant = 3,
        RULE_invocation = 4, RULE_function = 5, RULE_paramList = 6, RULE_quantity = 7,
        RULE_unit = 8, RULE_dateTimePrecision = 9, RULE_pluralDateTimePrecision = 10,
        RULE_typeSpecifier = 11, RULE_qualifiedIdentifier = 12, RULE_identifier = 13;
    private static String[] makeRuleNames() {
        return new String[] {
            "expression", "term", "literal", "externalConstant", "invocation", "function",
            "paramList", "quantity", "unit", "dateTimePrecision", "pluralDateTimePrecision",
            "typeSpecifier", "qualifiedIdentifier", "identifier"
        };
    }
    public static final String[] ruleNames = makeRuleNames();

    private static String[] makeLiteralNames() {
        return new String[] {
            null, "'.'", "'['", "']'", "'+'", "'-'", "'*'", "'/'", "'div'", "'mod'",
            "'&'", "'is'", "'as'", "'|'", "'<='", "'<'", "'>'", "'>='", "'='", "'~'",
            "'!='", "'!~'", "'in'", "'contains'", "'and'", "'or'", "'xor'", "'implies'",
            "'('", "')'", "'{'", "'}'", "'true'", "'false'", "'%'", "'$this'", "'$index'",
            "'$total'", "','", "'year'", "'month'", "'week'", "'day'", "'hour'",
            "'minute'", "'second'", "'millisecond'", "'years'", "'months'", "'weeks'",
            "'days'", "'hours'", "'minutes'", "'seconds'", "'milliseconds'"
        };
    }
    private static final String[] _LITERAL_NAMES = makeLiteralNames();
    private static String[] makeSymbolicNames() {
        return new String[] {
            null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, "DATE", "DATETIME", "TIME",
            "IDENTIFIER", "DELIMITEDIDENTIFIER", "STRING", "NUMBER", "WS", "COMMENT",
            "LINE_COMMENT"
        };
    }
    private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
    public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

    /**
     * @deprecated Use {@link #VOCABULARY} instead.
     */
    @Deprecated
    public static final String[] tokenNames;
    static {
        tokenNames = new String[_SYMBOLIC_NAMES.length];
        for (int i = 0; i < tokenNames.length; i++) {
            tokenNames[i] = VOCABULARY.getLiteralName(i);
            if (tokenNames[i] == null) {
                tokenNames[i] = VOCABULARY.getSymbolicName(i);
            }

            if (tokenNames[i] == null) {
                tokenNames[i] = "<INVALID>";
            }
        }
    }

    @Override
    @Deprecated
    public String[] getTokenNames() {
        return tokenNames;
    }

    @Override

    public Vocabulary getVocabulary() {
        return VOCABULARY;
    }

    @Override
    public String getGrammarFileName() { return "FHIRPath.g4"; }

    @Override
    public String[] getRuleNames() { return ruleNames; }

    @Override
    public String getSerializedATN() { return _serializedATN; }

    @Override
    public ATN getATN() { return _ATN; }

    public FHIRPathParser(TokenStream input) {
        super(input);
        _interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
    }

    public static class ExpressionContext extends ParserRuleContext {
        public ExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_expression; }

        public ExpressionContext() { }
        public void copyFrom(ExpressionContext ctx) {
            super.copyFrom(ctx);
        }
    }
    public static class IndexerExpressionContext extends ExpressionContext {
        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }
        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class,i);
        }
        public IndexerExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitIndexerExpression(this);
            else return visitor.visitChildren(this);
        }
    }
    public static class PolarityExpressionContext extends ExpressionContext {
        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class,0);
        }
        public PolarityExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitPolarityExpression(this);
            else return visitor.visitChildren(this);
        }
    }
    public static class AdditiveExpressionContext extends ExpressionContext {
        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }
        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class,i);
        }
        public AdditiveExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitAdditiveExpression(this);
            else return visitor.visitChildren(this);
        }
    }
    public static class MultiplicativeExpressionContext extends ExpressionContext {
        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }
        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class,i);
        }
        public MultiplicativeExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitMultiplicativeExpression(this);
            else return visitor.visitChildren(this);
        }
    }
    public static class UnionExpressionContext extends ExpressionContext {
        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }
        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class,i);
        }
        public UnionExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitUnionExpression(this);
            else return visitor.visitChildren(this);
        }
    }
    public static class OrExpressionContext extends ExpressionContext {
        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }
        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class,i);
        }
        public OrExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitOrExpression(this);
            else return visitor.visitChildren(this);
        }
    }
    public static class AndExpressionContext extends ExpressionContext {
        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }
        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class,i);
        }
        public AndExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitAndExpression(this);
            else return visitor.visitChildren(this);
        }
    }
    public static class MembershipExpressionContext extends ExpressionContext {
        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }
        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class,i);
        }
        public MembershipExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitMembershipExpression(this);
            else return visitor.visitChildren(this);
        }
    }
    public static class InequalityExpressionContext extends ExpressionContext {
        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }
        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class,i);
        }
        public InequalityExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitInequalityExpression(this);
            else return visitor.visitChildren(this);
        }
    }
    public static class InvocationExpressionContext extends ExpressionContext {
        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class,0);
        }
        public InvocationContext invocation() {
            return getRuleContext(InvocationContext.class,0);
        }
        public InvocationExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitInvocationExpression(this);
            else return visitor.visitChildren(this);
        }
    }
    public static class EqualityExpressionContext extends ExpressionContext {
        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }
        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class,i);
        }
        public EqualityExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitEqualityExpression(this);
            else return visitor.visitChildren(this);
        }
    }
    public static class ImpliesExpressionContext extends ExpressionContext {
        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }
        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class,i);
        }
        public ImpliesExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitImpliesExpression(this);
            else return visitor.visitChildren(this);
        }
    }
    public static class TermExpressionContext extends ExpressionContext {
        public TermContext term() {
            return getRuleContext(TermContext.class,0);
        }
        public TermExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitTermExpression(this);
            else return visitor.visitChildren(this);
        }
    }
    public static class TypeExpressionContext extends ExpressionContext {
        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class,0);
        }
        public TypeSpecifierContext typeSpecifier() {
            return getRuleContext(TypeSpecifierContext.class,0);
        }
        public TypeExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitTypeExpression(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ExpressionContext expression() throws RecognitionException {
        return expression(0);
    }

    private ExpressionContext expression(int _p) throws RecognitionException {
        ParserRuleContext _parentctx = _ctx;
        int _parentState = getState();
        ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
        ExpressionContext _prevctx = _localctx;
        int _startState = 0;
        enterRecursionRule(_localctx, 0, RULE_expression, _p);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
            setState(32);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
            case T__10:
            case T__11:
            case T__21:
            case T__22:
            case T__27:
            case T__29:
            case T__31:
            case T__32:
            case T__33:
            case T__34:
            case T__35:
            case T__36:
            case DATE:
            case DATETIME:
            case TIME:
            case IDENTIFIER:
            case DELIMITEDIDENTIFIER:
            case STRING:
            case NUMBER:
                {
                _localctx = new TermExpressionContext(_localctx);
                _ctx = _localctx;
                _prevctx = _localctx;

                setState(29);
                term();
                }
                break;
            case T__3:
            case T__4:
                {
                _localctx = new PolarityExpressionContext(_localctx);
                _ctx = _localctx;
                _prevctx = _localctx;
                setState(30);
                _la = _input.LA(1);
                if ( !(_la==T__3 || _la==T__4) ) {
                _errHandler.recoverInline(this);
                }
                else {
                    if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
                setState(31);
                expression(11);
                }
                break;
            default:
                throw new NoViableAltException(this);
            }
            _ctx.stop = _input.LT(-1);
            setState(74);
            _errHandler.sync(this);
            _alt = getInterpreter().adaptivePredict(_input,2,_ctx);
            while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
                if ( _alt==1 ) {
                    if ( _parseListeners!=null ) triggerExitRuleEvent();
                    _prevctx = _localctx;
                    {
                    setState(72);
                    _errHandler.sync(this);
                    switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
                    case 1:
                        {
                        _localctx = new MultiplicativeExpressionContext(new ExpressionContext(_parentctx, _parentState));
                        pushNewRecursionContext(_localctx, _startState, RULE_expression);
                        setState(34);
                        if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
                        setState(35);
                        _la = _input.LA(1);
                        if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__5) | (1L << T__6) | (1L << T__7) | (1L << T__8))) != 0)) ) {
                        _errHandler.recoverInline(this);
                        }
                        else {
                            if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
                            _errHandler.reportMatch(this);
                            consume();
                        }
                        setState(36);
                        expression(11);
                        }
                        break;
                    case 2:
                        {
                        _localctx = new AdditiveExpressionContext(new ExpressionContext(_parentctx, _parentState));
                        pushNewRecursionContext(_localctx, _startState, RULE_expression);
                        setState(37);
                        if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
                        setState(38);
                        _la = _input.LA(1);
                        if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__4) | (1L << T__9))) != 0)) ) {
                        _errHandler.recoverInline(this);
                        }
                        else {
                            if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
                            _errHandler.reportMatch(this);
                            consume();
                        }
                        setState(39);
                        expression(10);
                        }
                        break;
                    case 3:
                        {
                        _localctx = new UnionExpressionContext(new ExpressionContext(_parentctx, _parentState));
                        pushNewRecursionContext(_localctx, _startState, RULE_expression);
                        setState(40);
                        if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
                        setState(41);
                        match(T__12);
                        setState(42);
                        expression(8);
                        }
                        break;
                    case 4:
                        {
                        _localctx = new InequalityExpressionContext(new ExpressionContext(_parentctx, _parentState));
                        pushNewRecursionContext(_localctx, _startState, RULE_expression);
                        setState(43);
                        if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
                        setState(44);
                        _la = _input.LA(1);
                        if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16))) != 0)) ) {
                        _errHandler.recoverInline(this);
                        }
                        else {
                            if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
                            _errHandler.reportMatch(this);
                            consume();
                        }
                        setState(45);
                        expression(7);
                        }
                        break;
                    case 5:
                        {
                        _localctx = new EqualityExpressionContext(new ExpressionContext(_parentctx, _parentState));
                        pushNewRecursionContext(_localctx, _startState, RULE_expression);
                        setState(46);
                        if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
                        setState(47);
                        _la = _input.LA(1);
                        if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20))) != 0)) ) {
                        _errHandler.recoverInline(this);
                        }
                        else {
                            if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
                            _errHandler.reportMatch(this);
                            consume();
                        }
                        setState(48);
                        expression(6);
                        }
                        break;
                    case 6:
                        {
                        _localctx = new MembershipExpressionContext(new ExpressionContext(_parentctx, _parentState));
                        pushNewRecursionContext(_localctx, _startState, RULE_expression);
                        setState(49);
                        if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
                        setState(50);
                        _la = _input.LA(1);
                        if ( !(_la==T__21 || _la==T__22) ) {
                        _errHandler.recoverInline(this);
                        }
                        else {
                            if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
                            _errHandler.reportMatch(this);
                            consume();
                        }
                        setState(51);
                        expression(5);
                        }
                        break;
                    case 7:
                        {
                        _localctx = new AndExpressionContext(new ExpressionContext(_parentctx, _parentState));
                        pushNewRecursionContext(_localctx, _startState, RULE_expression);
                        setState(52);
                        if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
                        setState(53);
                        match(T__23);
                        setState(54);
                        expression(4);
                        }
                        break;
                    case 8:
                        {
                        _localctx = new OrExpressionContext(new ExpressionContext(_parentctx, _parentState));
                        pushNewRecursionContext(_localctx, _startState, RULE_expression);
                        setState(55);
                        if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
                        setState(56);
                        _la = _input.LA(1);
                        if ( !(_la==T__24 || _la==T__25) ) {
                        _errHandler.recoverInline(this);
                        }
                        else {
                            if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
                            _errHandler.reportMatch(this);
                            consume();
                        }
                        setState(57);
                        expression(3);
                        }
                        break;
                    case 9:
                        {
                        _localctx = new ImpliesExpressionContext(new ExpressionContext(_parentctx, _parentState));
                        pushNewRecursionContext(_localctx, _startState, RULE_expression);
                        setState(58);
                        if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
                        setState(59);
                        match(T__26);
                        setState(60);
                        expression(2);
                        }
                        break;
                    case 10:
                        {
                        _localctx = new InvocationExpressionContext(new ExpressionContext(_parentctx, _parentState));
                        pushNewRecursionContext(_localctx, _startState, RULE_expression);
                        setState(61);
                        if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
                        setState(62);
                        match(T__0);
                        setState(63);
                        invocation();
                        }
                        break;
                    case 11:
                        {
                        _localctx = new IndexerExpressionContext(new ExpressionContext(_parentctx, _parentState));
                        pushNewRecursionContext(_localctx, _startState, RULE_expression);
                        setState(64);
                        if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
                        setState(65);
                        match(T__1);
                        setState(66);
                        expression(0);
                        setState(67);
                        match(T__2);
                        }
                        break;
                    case 12:
                        {
                        _localctx = new TypeExpressionContext(new ExpressionContext(_parentctx, _parentState));
                        pushNewRecursionContext(_localctx, _startState, RULE_expression);
                        setState(69);
                        if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
                        setState(70);
                        _la = _input.LA(1);
                        if ( !(_la==T__10 || _la==T__11) ) {
                        _errHandler.recoverInline(this);
                        }
                        else {
                            if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
                            _errHandler.reportMatch(this);
                            consume();
                        }
                        setState(71);
                        typeSpecifier();
                        }
                        break;
                    }
                    }
                }
                setState(76);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input,2,_ctx);
            }
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            unrollRecursionContexts(_parentctx);
        }
        return _localctx;
    }

    public static class TermContext extends ParserRuleContext {
        public TermContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_term; }

        public TermContext() { }
        public void copyFrom(TermContext ctx) {
            super.copyFrom(ctx);
        }
    }
    public static class ExternalConstantTermContext extends TermContext {
        public ExternalConstantContext externalConstant() {
            return getRuleContext(ExternalConstantContext.class,0);
        }
        public ExternalConstantTermContext(TermContext ctx) { copyFrom(ctx); }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitExternalConstantTerm(this);
            else return visitor.visitChildren(this);
        }
    }
    public static class LiteralTermContext extends TermContext {
        public LiteralContext literal() {
            return getRuleContext(LiteralContext.class,0);
        }
        public LiteralTermContext(TermContext ctx) { copyFrom(ctx); }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitLiteralTerm(this);
            else return visitor.visitChildren(this);
        }
    }
    public static class ParenthesizedTermContext extends TermContext {
        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class,0);
        }
        public ParenthesizedTermContext(TermContext ctx) { copyFrom(ctx); }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitParenthesizedTerm(this);
            else return visitor.visitChildren(this);
        }
    }
    public static class InvocationTermContext extends TermContext {
        public InvocationContext invocation() {
            return getRuleContext(InvocationContext.class,0);
        }
        public InvocationTermContext(TermContext ctx) { copyFrom(ctx); }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitInvocationTerm(this);
            else return visitor.visitChildren(this);
        }
    }

    public final TermContext term() throws RecognitionException {
        TermContext _localctx = new TermContext(_ctx, getState());
        enterRule(_localctx, 2, RULE_term);
        try {
            setState(84);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
            case T__10:
            case T__11:
            case T__21:
            case T__22:
            case T__34:
            case T__35:
            case T__36:
            case IDENTIFIER:
            case DELIMITEDIDENTIFIER:
                _localctx = new InvocationTermContext(_localctx);
                enterOuterAlt(_localctx, 1);
                {
                setState(77);
                invocation();
                }
                break;
            case T__29:
            case T__31:
            case T__32:
            case DATE:
            case DATETIME:
            case TIME:
            case STRING:
            case NUMBER:
                _localctx = new LiteralTermContext(_localctx);
                enterOuterAlt(_localctx, 2);
                {
                setState(78);
                literal();
                }
                break;
            case T__33:
                _localctx = new ExternalConstantTermContext(_localctx);
                enterOuterAlt(_localctx, 3);
                {
                setState(79);
                externalConstant();
                }
                break;
            case T__27:
                _localctx = new ParenthesizedTermContext(_localctx);
                enterOuterAlt(_localctx, 4);
                {
                setState(80);
                match(T__27);
                setState(81);
                expression(0);
                setState(82);
                match(T__28);
                }
                break;
            default:
                throw new NoViableAltException(this);
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    public static class LiteralContext extends ParserRuleContext {
        public LiteralContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_literal; }

        public LiteralContext() { }
        public void copyFrom(LiteralContext ctx) {
            super.copyFrom(ctx);
        }
    }
    public static class TimeLiteralContext extends LiteralContext {
        public TerminalNode TIME() { return getToken(FHIRPathParser.TIME, 0); }
        public TimeLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitTimeLiteral(this);
            else return visitor.visitChildren(this);
        }
    }
    public static class NullLiteralContext extends LiteralContext {
        public NullLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitNullLiteral(this);
            else return visitor.visitChildren(this);
        }
    }
    public static class DateTimeLiteralContext extends LiteralContext {
        public TerminalNode DATETIME() { return getToken(FHIRPathParser.DATETIME, 0); }
        public DateTimeLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitDateTimeLiteral(this);
            else return visitor.visitChildren(this);
        }
    }
    public static class StringLiteralContext extends LiteralContext {
        public TerminalNode STRING() { return getToken(FHIRPathParser.STRING, 0); }
        public StringLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitStringLiteral(this);
            else return visitor.visitChildren(this);
        }
    }
    public static class DateLiteralContext extends LiteralContext {
        public TerminalNode DATE() { return getToken(FHIRPathParser.DATE, 0); }
        public DateLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitDateLiteral(this);
            else return visitor.visitChildren(this);
        }
    }
    public static class BooleanLiteralContext extends LiteralContext {
        public BooleanLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitBooleanLiteral(this);
            else return visitor.visitChildren(this);
        }
    }
    public static class NumberLiteralContext extends LiteralContext {
        public TerminalNode NUMBER() { return getToken(FHIRPathParser.NUMBER, 0); }
        public NumberLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitNumberLiteral(this);
            else return visitor.visitChildren(this);
        }
    }
    public static class QuantityLiteralContext extends LiteralContext {
        public QuantityContext quantity() {
            return getRuleContext(QuantityContext.class,0);
        }
        public QuantityLiteralContext(LiteralContext ctx) { copyFrom(ctx); }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitQuantityLiteral(this);
            else return visitor.visitChildren(this);
        }
    }

    public final LiteralContext literal() throws RecognitionException {
        LiteralContext _localctx = new LiteralContext(_ctx, getState());
        enterRule(_localctx, 4, RULE_literal);
        int _la;
        try {
            setState(95);
            _errHandler.sync(this);
            switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
            case 1:
                _localctx = new NullLiteralContext(_localctx);
                enterOuterAlt(_localctx, 1);
                {
                setState(86);
                match(T__29);
                setState(87);
                match(T__30);
                }
                break;
            case 2:
                _localctx = new BooleanLiteralContext(_localctx);
                enterOuterAlt(_localctx, 2);
                {
                setState(88);
                _la = _input.LA(1);
                if ( !(_la==T__31 || _la==T__32) ) {
                _errHandler.recoverInline(this);
                }
                else {
                    if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
                }
                break;
            case 3:
                _localctx = new StringLiteralContext(_localctx);
                enterOuterAlt(_localctx, 3);
                {
                setState(89);
                match(STRING);
                }
                break;
            case 4:
                _localctx = new NumberLiteralContext(_localctx);
                enterOuterAlt(_localctx, 4);
                {
                setState(90);
                match(NUMBER);
                }
                break;
            case 5:
                _localctx = new DateLiteralContext(_localctx);
                enterOuterAlt(_localctx, 5);
                {
                setState(91);
                match(DATE);
                }
                break;
            case 6:
                _localctx = new DateTimeLiteralContext(_localctx);
                enterOuterAlt(_localctx, 6);
                {
                setState(92);
                match(DATETIME);
                }
                break;
            case 7:
                _localctx = new TimeLiteralContext(_localctx);
                enterOuterAlt(_localctx, 7);
                {
                setState(93);
                match(TIME);
                }
                break;
            case 8:
                _localctx = new QuantityLiteralContext(_localctx);
                enterOuterAlt(_localctx, 8);
                {
                setState(94);
                quantity();
                }
                break;
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ExternalConstantContext extends ParserRuleContext {
        public IdentifierContext identifier() {
            return getRuleContext(IdentifierContext.class,0);
        }
        public TerminalNode STRING() { return getToken(FHIRPathParser.STRING, 0); }
        public ExternalConstantContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_externalConstant; }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitExternalConstant(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ExternalConstantContext externalConstant() throws RecognitionException {
        ExternalConstantContext _localctx = new ExternalConstantContext(_ctx, getState());
        enterRule(_localctx, 6, RULE_externalConstant);
        try {
            enterOuterAlt(_localctx, 1);
            {
            setState(97);
            match(T__33);
            setState(100);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
            case T__10:
            case T__11:
            case T__21:
            case T__22:
            case IDENTIFIER:
            case DELIMITEDIDENTIFIER:
                {
                setState(98);
                identifier();
                }
                break;
            case STRING:
                {
                setState(99);
                match(STRING);
                }
                break;
            default:
                throw new NoViableAltException(this);
            }
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    public static class InvocationContext extends ParserRuleContext {
        public InvocationContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_invocation; }

        public InvocationContext() { }
        public void copyFrom(InvocationContext ctx) {
            super.copyFrom(ctx);
        }
    }
    public static class TotalInvocationContext extends InvocationContext {
        public TotalInvocationContext(InvocationContext ctx) { copyFrom(ctx); }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitTotalInvocation(this);
            else return visitor.visitChildren(this);
        }
    }
    public static class ThisInvocationContext extends InvocationContext {
        public ThisInvocationContext(InvocationContext ctx) { copyFrom(ctx); }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitThisInvocation(this);
            else return visitor.visitChildren(this);
        }
    }
    public static class IndexInvocationContext extends InvocationContext {
        public IndexInvocationContext(InvocationContext ctx) { copyFrom(ctx); }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitIndexInvocation(this);
            else return visitor.visitChildren(this);
        }
    }
    public static class FunctionInvocationContext extends InvocationContext {
        public FunctionContext function() {
            return getRuleContext(FunctionContext.class,0);
        }
        public FunctionInvocationContext(InvocationContext ctx) { copyFrom(ctx); }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitFunctionInvocation(this);
            else return visitor.visitChildren(this);
        }
    }
    public static class MemberInvocationContext extends InvocationContext {
        public IdentifierContext identifier() {
            return getRuleContext(IdentifierContext.class,0);
        }
        public MemberInvocationContext(InvocationContext ctx) { copyFrom(ctx); }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitMemberInvocation(this);
            else return visitor.visitChildren(this);
        }
    }

    public final InvocationContext invocation() throws RecognitionException {
        InvocationContext _localctx = new InvocationContext(_ctx, getState());
        enterRule(_localctx, 8, RULE_invocation);
        try {
            setState(107);
            _errHandler.sync(this);
            switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
            case 1:
                _localctx = new MemberInvocationContext(_localctx);
                enterOuterAlt(_localctx, 1);
                {
                setState(102);
                identifier();
                }
                break;
            case 2:
                _localctx = new FunctionInvocationContext(_localctx);
                enterOuterAlt(_localctx, 2);
                {
                setState(103);
                function();
                }
                break;
            case 3:
                _localctx = new ThisInvocationContext(_localctx);
                enterOuterAlt(_localctx, 3);
                {
                setState(104);
                match(T__34);
                }
                break;
            case 4:
                _localctx = new IndexInvocationContext(_localctx);
                enterOuterAlt(_localctx, 4);
                {
                setState(105);
                match(T__35);
                }
                break;
            case 5:
                _localctx = new TotalInvocationContext(_localctx);
                enterOuterAlt(_localctx, 5);
                {
                setState(106);
                match(T__36);
                }
                break;
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    public static class FunctionContext extends ParserRuleContext {
        public IdentifierContext identifier() {
            return getRuleContext(IdentifierContext.class,0);
        }
        public ParamListContext paramList() {
            return getRuleContext(ParamListContext.class,0);
        }
        public FunctionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_function; }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitFunction(this);
            else return visitor.visitChildren(this);
        }
    }

    public final FunctionContext function() throws RecognitionException {
        FunctionContext _localctx = new FunctionContext(_ctx, getState());
        enterRule(_localctx, 10, RULE_function);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
            setState(109);
            identifier();
            setState(110);
            match(T__27);
            setState(112);
            _errHandler.sync(this);
            _la = _input.LA(1);
            if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__4) | (1L << T__10) | (1L << T__11) | (1L << T__21) | (1L << T__22) | (1L << T__27) | (1L << T__29) | (1L << T__31) | (1L << T__32) | (1L << T__33) | (1L << T__34) | (1L << T__35) | (1L << T__36) | (1L << DATE) | (1L << DATETIME) | (1L << TIME) | (1L << IDENTIFIER) | (1L << DELIMITEDIDENTIFIER) | (1L << STRING) | (1L << NUMBER))) != 0)) {
                {
                setState(111);
                paramList();
                }
            }

            setState(114);
            match(T__28);
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ParamListContext extends ParserRuleContext {
        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }
        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class,i);
        }
        public ParamListContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_paramList; }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitParamList(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ParamListContext paramList() throws RecognitionException {
        ParamListContext _localctx = new ParamListContext(_ctx, getState());
        enterRule(_localctx, 12, RULE_paramList);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
            setState(116);
            expression(0);
            setState(121);
            _errHandler.sync(this);
            _la = _input.LA(1);
            while (_la==T__37) {
                {
                {
                setState(117);
                match(T__37);
                setState(118);
                expression(0);
                }
                }
                setState(123);
                _errHandler.sync(this);
                _la = _input.LA(1);
            }
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    public static class QuantityContext extends ParserRuleContext {
        public TerminalNode NUMBER() { return getToken(FHIRPathParser.NUMBER, 0); }
        public UnitContext unit() {
            return getRuleContext(UnitContext.class,0);
        }
        public QuantityContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_quantity; }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitQuantity(this);
            else return visitor.visitChildren(this);
        }
    }

    public final QuantityContext quantity() throws RecognitionException {
        QuantityContext _localctx = new QuantityContext(_ctx, getState());
        enterRule(_localctx, 14, RULE_quantity);
        try {
            enterOuterAlt(_localctx, 1);
            {
            setState(124);
            match(NUMBER);
            setState(126);
            _errHandler.sync(this);
            switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
            case 1:
                {
                setState(125);
                unit();
                }
                break;
            }
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    public static class UnitContext extends ParserRuleContext {
        public DateTimePrecisionContext dateTimePrecision() {
            return getRuleContext(DateTimePrecisionContext.class,0);
        }
        public PluralDateTimePrecisionContext pluralDateTimePrecision() {
            return getRuleContext(PluralDateTimePrecisionContext.class,0);
        }
        public TerminalNode STRING() { return getToken(FHIRPathParser.STRING, 0); }
        public UnitContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_unit; }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitUnit(this);
            else return visitor.visitChildren(this);
        }
    }

    public final UnitContext unit() throws RecognitionException {
        UnitContext _localctx = new UnitContext(_ctx, getState());
        enterRule(_localctx, 16, RULE_unit);
        try {
            setState(131);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
            case T__38:
            case T__39:
            case T__40:
            case T__41:
            case T__42:
            case T__43:
            case T__44:
            case T__45:
                enterOuterAlt(_localctx, 1);
                {
                setState(128);
                dateTimePrecision();
                }
                break;
            case T__46:
            case T__47:
            case T__48:
            case T__49:
            case T__50:
            case T__51:
            case T__52:
            case T__53:
                enterOuterAlt(_localctx, 2);
                {
                setState(129);
                pluralDateTimePrecision();
                }
                break;
            case STRING:
                enterOuterAlt(_localctx, 3);
                {
                setState(130);
                match(STRING);
                }
                break;
            default:
                throw new NoViableAltException(this);
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    public static class DateTimePrecisionContext extends ParserRuleContext {
        public DateTimePrecisionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_dateTimePrecision; }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitDateTimePrecision(this);
            else return visitor.visitChildren(this);
        }
    }

    public final DateTimePrecisionContext dateTimePrecision() throws RecognitionException {
        DateTimePrecisionContext _localctx = new DateTimePrecisionContext(_ctx, getState());
        enterRule(_localctx, 18, RULE_dateTimePrecision);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
            setState(133);
            _la = _input.LA(1);
            if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__38) | (1L << T__39) | (1L << T__40) | (1L << T__41) | (1L << T__42) | (1L << T__43) | (1L << T__44) | (1L << T__45))) != 0)) ) {
            _errHandler.recoverInline(this);
            }
            else {
                if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
                _errHandler.reportMatch(this);
                consume();
            }
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    public static class PluralDateTimePrecisionContext extends ParserRuleContext {
        public PluralDateTimePrecisionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_pluralDateTimePrecision; }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitPluralDateTimePrecision(this);
            else return visitor.visitChildren(this);
        }
    }

    public final PluralDateTimePrecisionContext pluralDateTimePrecision() throws RecognitionException {
        PluralDateTimePrecisionContext _localctx = new PluralDateTimePrecisionContext(_ctx, getState());
        enterRule(_localctx, 20, RULE_pluralDateTimePrecision);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
            setState(135);
            _la = _input.LA(1);
            if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__46) | (1L << T__47) | (1L << T__48) | (1L << T__49) | (1L << T__50) | (1L << T__51) | (1L << T__52) | (1L << T__53))) != 0)) ) {
            _errHandler.recoverInline(this);
            }
            else {
                if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
                _errHandler.reportMatch(this);
                consume();
            }
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    public static class TypeSpecifierContext extends ParserRuleContext {
        public QualifiedIdentifierContext qualifiedIdentifier() {
            return getRuleContext(QualifiedIdentifierContext.class,0);
        }
        public TypeSpecifierContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_typeSpecifier; }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitTypeSpecifier(this);
            else return visitor.visitChildren(this);
        }
    }

    public final TypeSpecifierContext typeSpecifier() throws RecognitionException {
        TypeSpecifierContext _localctx = new TypeSpecifierContext(_ctx, getState());
        enterRule(_localctx, 22, RULE_typeSpecifier);
        try {
            enterOuterAlt(_localctx, 1);
            {
            setState(137);
            qualifiedIdentifier();
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    public static class QualifiedIdentifierContext extends ParserRuleContext {
        public List<IdentifierContext> identifier() {
            return getRuleContexts(IdentifierContext.class);
        }
        public IdentifierContext identifier(int i) {
            return getRuleContext(IdentifierContext.class,i);
        }
        public QualifiedIdentifierContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_qualifiedIdentifier; }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitQualifiedIdentifier(this);
            else return visitor.visitChildren(this);
        }
    }

    public final QualifiedIdentifierContext qualifiedIdentifier() throws RecognitionException {
        QualifiedIdentifierContext _localctx = new QualifiedIdentifierContext(_ctx, getState());
        enterRule(_localctx, 24, RULE_qualifiedIdentifier);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
            setState(139);
            identifier();
            setState(144);
            _errHandler.sync(this);
            _alt = getInterpreter().adaptivePredict(_input,11,_ctx);
            while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
                if ( _alt==1 ) {
                    {
                    {
                    setState(140);
                    match(T__0);
                    setState(141);
                    identifier();
                    }
                    }
                }
                setState(146);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input,11,_ctx);
            }
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    public static class IdentifierContext extends ParserRuleContext {
        public TerminalNode IDENTIFIER() { return getToken(FHIRPathParser.IDENTIFIER, 0); }
        public TerminalNode DELIMITEDIDENTIFIER() { return getToken(FHIRPathParser.DELIMITEDIDENTIFIER, 0); }
        public IdentifierContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_identifier; }
        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if ( visitor instanceof FHIRPathVisitor ) return ((FHIRPathVisitor<? extends T>)visitor).visitIdentifier(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IdentifierContext identifier() throws RecognitionException {
        IdentifierContext _localctx = new IdentifierContext(_ctx, getState());
        enterRule(_localctx, 26, RULE_identifier);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
            setState(147);
            _la = _input.LA(1);
            if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__10) | (1L << T__11) | (1L << T__21) | (1L << T__22) | (1L << IDENTIFIER) | (1L << DELIMITEDIDENTIFIER))) != 0)) ) {
            _errHandler.recoverInline(this);
            }
            else {
                if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
                _errHandler.reportMatch(this);
                consume();
            }
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    @Override
    public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
        switch (ruleIndex) {
        case 0:
            return expression_sempred((ExpressionContext)_localctx, predIndex);
        }
        return true;
    }
    private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
        switch (predIndex) {
        case 0:
            return precpred(_ctx, 10);
        case 1:
            return precpred(_ctx, 9);
        case 2:
            return precpred(_ctx, 7);
        case 3:
            return precpred(_ctx, 6);
        case 4:
            return precpred(_ctx, 5);
        case 5:
            return precpred(_ctx, 4);
        case 6:
            return precpred(_ctx, 3);
        case 7:
            return precpred(_ctx, 2);
        case 8:
            return precpred(_ctx, 1);
        case 9:
            return precpred(_ctx, 13);
        case 10:
            return precpred(_ctx, 12);
        case 11:
            return precpred(_ctx, 8);
        }
        return true;
    }

    public static final String _serializedATN =
        "\u0004\u0001@\u0096\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
        "\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
        "\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
        "\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
        "\f\u0007\f\u0002\r\u0007\r\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
        "\u0003\u0000!\b\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
        "\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
        "\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
        "\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
        "\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
        "\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
        "\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0005\u0000I\b\u0000"+
        "\n\u0000\f\u0000L\t\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
        "\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u0001U\b\u0001\u0001\u0002"+
        "\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
        "\u0001\u0002\u0001\u0002\u0003\u0002`\b\u0002\u0001\u0003\u0001\u0003"+
        "\u0001\u0003\u0003\u0003e\b\u0003\u0001\u0004\u0001\u0004\u0001\u0004"+
        "\u0001\u0004\u0001\u0004\u0003\u0004l\b\u0004\u0001\u0005\u0001\u0005"+
        "\u0001\u0005\u0003\u0005q\b\u0005\u0001\u0005\u0001\u0005\u0001\u0006"+
        "\u0001\u0006\u0001\u0006\u0005\u0006x\b\u0006\n\u0006\f\u0006{\t\u0006"+
        "\u0001\u0007\u0001\u0007\u0003\u0007\u007f\b\u0007\u0001\b\u0001\b\u0001"+
        "\b\u0003\b\u0084\b\b\u0001\t\u0001\t\u0001\n\u0001\n\u0001\u000b\u0001"+
        "\u000b\u0001\f\u0001\f\u0001\f\u0005\f\u008f\b\f\n\f\f\f\u0092\t\f\u0001"+
        "\r\u0001\r\u0001\r\u0000\u0001\u0000\u000e\u0000\u0002\u0004\u0006\b\n"+
        "\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u0000\f\u0001\u0000\u0004"+
        "\u0005\u0001\u0000\u0006\t\u0002\u0000\u0004\u0005\n\n\u0001\u0000\u000e"+
        "\u0011\u0001\u0000\u0012\u0015\u0001\u0000\u0016\u0017\u0001\u0000\u0019"+
        "\u001a\u0001\u0000\u000b\f\u0001\u0000 !\u0001\u0000\'.\u0001\u0000/6"+
        "\u0003\u0000\u000b\f\u0016\u0017:;\u00a9\u0000 \u0001\u0000\u0000\u0000"+
        "\u0002T\u0001\u0000\u0000\u0000\u0004_\u0001\u0000\u0000\u0000\u0006a"+
        "\u0001\u0000\u0000\u0000\bk\u0001\u0000\u0000\u0000\nm\u0001\u0000\u0000"+
        "\u0000\ft\u0001\u0000\u0000\u0000\u000e|\u0001\u0000\u0000\u0000\u0010"+
        "\u0083\u0001\u0000\u0000\u0000\u0012\u0085\u0001\u0000\u0000\u0000\u0014"+
        "\u0087\u0001\u0000\u0000\u0000\u0016\u0089\u0001\u0000\u0000\u0000\u0018"+
        "\u008b\u0001\u0000\u0000\u0000\u001a\u0093\u0001\u0000\u0000\u0000\u001c"+
        "\u001d\u0006\u0000\uffff\uffff\u0000\u001d!\u0003\u0002\u0001\u0000\u001e"+
        "\u001f\u0007\u0000\u0000\u0000\u001f!\u0003\u0000\u0000\u000b \u001c\u0001"+
        "\u0000\u0000\u0000 \u001e\u0001\u0000\u0000\u0000!J\u0001\u0000\u0000"+
        "\u0000\"#\n\n\u0000\u0000#$\u0007\u0001\u0000\u0000$I\u0003\u0000\u0000"+
        "\u000b%&\n\t\u0000\u0000&\'\u0007\u0002\u0000\u0000\'I\u0003\u0000\u0000"+
        "\n()\n\u0007\u0000\u0000)*\u0005\r\u0000\u0000*I\u0003\u0000\u0000\b+"+
        ",\n\u0006\u0000\u0000,-\u0007\u0003\u0000\u0000-I\u0003\u0000\u0000\u0007"+
        "./\n\u0005\u0000\u0000/0\u0007\u0004\u0000\u00000I\u0003\u0000\u0000\u0006"+
        "12\n\u0004\u0000\u000023\u0007\u0005\u0000\u00003I\u0003\u0000\u0000\u0005"+
        "45\n\u0003\u0000\u000056\u0005\u0018\u0000\u00006I\u0003\u0000\u0000\u0004"+
        "78\n\u0002\u0000\u000089\u0007\u0006\u0000\u00009I\u0003\u0000\u0000\u0003"+
        ":;\n\u0001\u0000\u0000;<\u0005\u001b\u0000\u0000<I\u0003\u0000\u0000\u0002"+
        "=>\n\r\u0000\u0000>?\u0005\u0001\u0000\u0000?I\u0003\b\u0004\u0000@A\n"+
        "\f\u0000\u0000AB\u0005\u0002\u0000\u0000BC\u0003\u0000\u0000\u0000CD\u0005"+
        "\u0003\u0000\u0000DI\u0001\u0000\u0000\u0000EF\n\b\u0000\u0000FG\u0007"+
        "\u0007\u0000\u0000GI\u0003\u0016\u000b\u0000H\"\u0001\u0000\u0000\u0000"+
        "H%\u0001\u0000\u0000\u0000H(\u0001\u0000\u0000\u0000H+\u0001\u0000\u0000"+
        "\u0000H.\u0001\u0000\u0000\u0000H1\u0001\u0000\u0000\u0000H4\u0001\u0000"+
        "\u0000\u0000H7\u0001\u0000\u0000\u0000H:\u0001\u0000\u0000\u0000H=\u0001"+
        "\u0000\u0000\u0000H@\u0001\u0000\u0000\u0000HE\u0001\u0000\u0000\u0000"+
        "IL\u0001\u0000\u0000\u0000JH\u0001\u0000\u0000\u0000JK\u0001\u0000\u0000"+
        "\u0000K\u0001\u0001\u0000\u0000\u0000LJ\u0001\u0000\u0000\u0000MU\u0003"+
        "\b\u0004\u0000NU\u0003\u0004\u0002\u0000OU\u0003\u0006\u0003\u0000PQ\u0005"+
        "\u001c\u0000\u0000QR\u0003\u0000\u0000\u0000RS\u0005\u001d\u0000\u0000"+
        "SU\u0001\u0000\u0000\u0000TM\u0001\u0000\u0000\u0000TN\u0001\u0000\u0000"+
        "\u0000TO\u0001\u0000\u0000\u0000TP\u0001\u0000\u0000\u0000U\u0003\u0001"+
        "\u0000\u0000\u0000VW\u0005\u001e\u0000\u0000W`\u0005\u001f\u0000\u0000"+
        "X`\u0007\b\u0000\u0000Y`\u0005<\u0000\u0000Z`\u0005=\u0000\u0000[`\u0005"+
        "7\u0000\u0000\\`\u00058\u0000\u0000]`\u00059\u0000\u0000^`\u0003\u000e"+
        "\u0007\u0000_V\u0001\u0000\u0000\u0000_X\u0001\u0000\u0000\u0000_Y\u0001"+
        "\u0000\u0000\u0000_Z\u0001\u0000\u0000\u0000_[\u0001\u0000\u0000\u0000"+
        "_\\\u0001\u0000\u0000\u0000_]\u0001\u0000\u0000\u0000_^\u0001\u0000\u0000"+
        "\u0000`\u0005\u0001\u0000\u0000\u0000ad\u0005\"\u0000\u0000be\u0003\u001a"+
        "\r\u0000ce\u0005<\u0000\u0000db\u0001\u0000\u0000\u0000dc\u0001\u0000"+
        "\u0000\u0000e\u0007\u0001\u0000\u0000\u0000fl\u0003\u001a\r\u0000gl\u0003"+
        "\n\u0005\u0000hl\u0005#\u0000\u0000il\u0005$\u0000\u0000jl\u0005%\u0000"+
        "\u0000kf\u0001\u0000\u0000\u0000kg\u0001\u0000\u0000\u0000kh\u0001\u0000"+
        "\u0000\u0000ki\u0001\u0000\u0000\u0000kj\u0001\u0000\u0000\u0000l\t\u0001"+
        "\u0000\u0000\u0000mn\u0003\u001a\r\u0000np\u0005\u001c\u0000\u0000oq\u0003"+
        "\f\u0006\u0000po\u0001\u0000\u0000\u0000pq\u0001\u0000\u0000\u0000qr\u0001"+
        "\u0000\u0000\u0000rs\u0005\u001d\u0000\u0000s\u000b\u0001\u0000\u0000"+
        "\u0000ty\u0003\u0000\u0000\u0000uv\u0005&\u0000\u0000vx\u0003\u0000\u0000"+
        "\u0000wu\u0001\u0000\u0000\u0000x{\u0001\u0000\u0000\u0000yw\u0001\u0000"+
        "\u0000\u0000yz\u0001\u0000\u0000\u0000z\r\u0001\u0000\u0000\u0000{y\u0001"+
        "\u0000\u0000\u0000|~\u0005=\u0000\u0000}\u007f\u0003\u0010\b\u0000~}\u0001"+
        "\u0000\u0000\u0000~\u007f\u0001\u0000\u0000\u0000\u007f\u000f\u0001\u0000"+
        "\u0000\u0000\u0080\u0084\u0003\u0012\t\u0000\u0081\u0084\u0003\u0014\n"+
        "\u0000\u0082\u0084\u0005<\u0000\u0000\u0083\u0080\u0001\u0000\u0000\u0000"+
        "\u0083\u0081\u0001\u0000\u0000\u0000\u0083\u0082\u0001\u0000\u0000\u0000"+
        "\u0084\u0011\u0001\u0000\u0000\u0000\u0085\u0086\u0007\t\u0000\u0000\u0086"+
        "\u0013\u0001\u0000\u0000\u0000\u0087\u0088\u0007\n\u0000\u0000\u0088\u0015"+
        "\u0001\u0000\u0000\u0000\u0089\u008a\u0003\u0018\f\u0000\u008a\u0017\u0001"+
        "\u0000\u0000\u0000\u008b\u0090\u0003\u001a\r\u0000\u008c\u008d\u0005\u0001"+
        "\u0000\u0000\u008d\u008f\u0003\u001a\r\u0000\u008e\u008c\u0001\u0000\u0000"+
        "\u0000\u008f\u0092\u0001\u0000\u0000\u0000\u0090\u008e\u0001\u0000\u0000"+
        "\u0000\u0090\u0091\u0001\u0000\u0000\u0000\u0091\u0019\u0001\u0000\u0000"+
        "\u0000\u0092\u0090\u0001\u0000\u0000\u0000\u0093\u0094\u0007\u000b\u0000"+
        "\u0000\u0094\u001b\u0001\u0000\u0000\u0000\f HJT_dkpy~\u0083\u0090";
    public static final ATN _ATN =
        new ATNDeserializer().deserialize(_serializedATN.toCharArray());
    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}