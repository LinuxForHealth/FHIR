/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

// Generated from UCUM.g4 by ANTLR 4.9.1
package com.ibm.fhir.model.ucum;

import java.util.List;

import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
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
import org.antlr.v4.runtime.tree.TerminalNode;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class UCUMParser extends Parser {
    static { RuntimeMetaData.checkVersion("4.9.1", RuntimeMetaData.VERSION); }

    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache =
        new PredictionContextCache();
    public static final int
        T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9,
        T__9=10, T__10=11, T__11=12, T__12=13, NON_DIGIT_TERMINAL_UNIT_SYMBOL=14,
        DIGIT_SYMBOL=15;
    public static final int
        RULE_mainTerm = 0, RULE_term = 1, RULE_component = 2, RULE_annotatable = 3,
        RULE_simpleUnit = 4, RULE_simpleUnitSymbols = 5, RULE_annotationSymbols = 6,
        RULE_squareBracketsSymbols = 7, RULE_withinSbSymbol = 8, RULE_withinCbSymbol = 9,
        RULE_withinCbOrSbSymbol = 10, RULE_terminalUnitSymbol = 11, RULE_exponent = 12,
        RULE_digitSymbols = 13;
    private static String[] makeRuleNames() {
        return new String[] {
            "mainTerm", "term", "component", "annotatable", "simpleUnit", "simpleUnitSymbols",
            "annotationSymbols", "squareBracketsSymbols", "withinSbSymbol", "withinCbSymbol",
            "withinCbOrSbSymbol", "terminalUnitSymbol", "exponent", "digitSymbols"
        };
    }
    public static final String[] ruleNames = makeRuleNames();

    private static String[] makeLiteralNames() {
        return new String[] {
            null, "'/'", "'.'", "'('", "')'", "'{'", "'}'", "'['", "']'", "' '",
            "'\"'", "'+'", "'-'", "'='"
        };
    }
    private static final String[] _LITERAL_NAMES = makeLiteralNames();
    private static String[] makeSymbolicNames() {
        return new String[] {
            null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, "NON_DIGIT_TERMINAL_UNIT_SYMBOL", "DIGIT_SYMBOL"
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
    public String getGrammarFileName() { return "UCUM.g4"; }

    @Override
    public String[] getRuleNames() { return ruleNames; }

    @Override
    public String getSerializedATN() { return _serializedATN; }

    @Override
    public ATN getATN() { return _ATN; }

    public UCUMParser(TokenStream input) {
        super(input);
        _interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
    }

    public static class MainTermContext extends ParserRuleContext {
        public TermContext term() {
            return getRuleContext(TermContext.class,0);
        }
        public TerminalNode EOF() { return getToken(UCUMParser.EOF, 0); }
        public MainTermContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_mainTerm; }
    }

    public final MainTermContext mainTerm() throws RecognitionException {
        MainTermContext _localctx = new MainTermContext(_ctx, getState());
        enterRule(_localctx, 0, RULE_mainTerm);
        try {
            enterOuterAlt(_localctx, 1);
            {
            setState(28);
            term();
            setState(29);
            match(EOF);
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

    public static class TermContext extends ParserRuleContext {
        public ComponentContext component() {
            return getRuleContext(ComponentContext.class,0);
        }
        public TermContext term() {
            return getRuleContext(TermContext.class,0);
        }
        public TermContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_term; }
    }

    public final TermContext term() throws RecognitionException {
        TermContext _localctx = new TermContext(_ctx, getState());
        enterRule(_localctx, 2, RULE_term);
        try {
            setState(42);
            _errHandler.sync(this);
            switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
            case 1:
                enterOuterAlt(_localctx, 1);
                {
                setState(31);
                component();
                }
                break;
            case 2:
                enterOuterAlt(_localctx, 2);
                {
                setState(32);
                match(T__0);
                setState(33);
                term();
                }
                break;
            case 3:
                enterOuterAlt(_localctx, 3);
                {
                setState(34);
                component();
                setState(35);
                match(T__0);
                setState(36);
                term();
                }
                break;
            case 4:
                enterOuterAlt(_localctx, 4);
                {
                setState(38);
                component();
                setState(39);
                match(T__1);
                setState(40);
                term();
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

    public static class ComponentContext extends ParserRuleContext {
        public TermContext term() {
            return getRuleContext(TermContext.class,0);
        }
        public AnnotationSymbolsContext annotationSymbols() {
            return getRuleContext(AnnotationSymbolsContext.class,0);
        }
        public AnnotatableContext annotatable() {
            return getRuleContext(AnnotatableContext.class,0);
        }
        public DigitSymbolsContext digitSymbols() {
            return getRuleContext(DigitSymbolsContext.class,0);
        }
        public ComponentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_component; }
    }

    public final ComponentContext component() throws RecognitionException {
        ComponentContext _localctx = new ComponentContext(_ctx, getState());
        enterRule(_localctx, 4, RULE_component);
        try {
            setState(59);
            _errHandler.sync(this);
            switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
            case 1:
                enterOuterAlt(_localctx, 1);
                {
                setState(44);
                match(T__2);
                setState(45);
                term();
                setState(46);
                match(T__3);
                }
                break;
            case 2:
                enterOuterAlt(_localctx, 2);
                {
                setState(48);
                match(T__2);
                setState(49);
                term();
                setState(50);
                match(T__3);
                setState(51);
                annotationSymbols();
                }
                break;
            case 3:
                enterOuterAlt(_localctx, 3);
                {
                setState(53);
                annotatable();
                setState(54);
                annotationSymbols();
                }
                break;
            case 4:
                enterOuterAlt(_localctx, 4);
                {
                setState(56);
                annotationSymbols();
                }
                break;
            case 5:
                enterOuterAlt(_localctx, 5);
                {
                setState(57);
                annotatable();
                }
                break;
            case 6:
                enterOuterAlt(_localctx, 6);
                {
                setState(58);
                digitSymbols();
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

    public static class AnnotatableContext extends ParserRuleContext {
        public SimpleUnitContext simpleUnit() {
            return getRuleContext(SimpleUnitContext.class,0);
        }
        public ExponentContext exponent() {
            return getRuleContext(ExponentContext.class,0);
        }
        public AnnotatableContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_annotatable; }
    }

    public final AnnotatableContext annotatable() throws RecognitionException {
        AnnotatableContext _localctx = new AnnotatableContext(_ctx, getState());
        enterRule(_localctx, 6, RULE_annotatable);
        try {
            setState(65);
            _errHandler.sync(this);
            switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
            case 1:
                enterOuterAlt(_localctx, 1);
                {
                setState(61);
                simpleUnit();
                }
                break;
            case 2:
                enterOuterAlt(_localctx, 2);
                {
                setState(62);
                simpleUnit();
                setState(63);
                exponent();
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

    public static class SimpleUnitContext extends ParserRuleContext {
        public List<SimpleUnitSymbolsContext> simpleUnitSymbols() {
            return getRuleContexts(SimpleUnitSymbolsContext.class);
        }
        public SimpleUnitSymbolsContext simpleUnitSymbols(int i) {
            return getRuleContext(SimpleUnitSymbolsContext.class,i);
        }
        public SquareBracketsSymbolsContext squareBracketsSymbols() {
            return getRuleContext(SquareBracketsSymbolsContext.class,0);
        }
        public SimpleUnitContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_simpleUnit; }
    }

    public final SimpleUnitContext simpleUnit() throws RecognitionException {
        SimpleUnitContext _localctx = new SimpleUnitContext(_ctx, getState());
        enterRule(_localctx, 8, RULE_simpleUnit);
        try {
            setState(79);
            _errHandler.sync(this);
            switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
            case 1:
                enterOuterAlt(_localctx, 1);
                {
                setState(67);
                simpleUnitSymbols();
                }
                break;
            case 2:
                enterOuterAlt(_localctx, 2);
                {
                setState(68);
                squareBracketsSymbols();
                }
                break;
            case 3:
                enterOuterAlt(_localctx, 3);
                {
                setState(69);
                squareBracketsSymbols();
                setState(70);
                simpleUnitSymbols();
                }
                break;
            case 4:
                enterOuterAlt(_localctx, 4);
                {
                setState(72);
                simpleUnitSymbols();
                setState(73);
                squareBracketsSymbols();
                }
                break;
            case 5:
                enterOuterAlt(_localctx, 5);
                {
                setState(75);
                simpleUnitSymbols();
                setState(76);
                squareBracketsSymbols();
                setState(77);
                simpleUnitSymbols();
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

    public static class SimpleUnitSymbolsContext extends ParserRuleContext {
        public List<TerminalUnitSymbolContext> terminalUnitSymbol() {
            return getRuleContexts(TerminalUnitSymbolContext.class);
        }
        public TerminalUnitSymbolContext terminalUnitSymbol(int i) {
            return getRuleContext(TerminalUnitSymbolContext.class,i);
        }
        public SimpleUnitSymbolsContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_simpleUnitSymbols; }
    }

    public final SimpleUnitSymbolsContext simpleUnitSymbols() throws RecognitionException {
        SimpleUnitSymbolsContext _localctx = new SimpleUnitSymbolsContext(_ctx, getState());
        enterRule(_localctx, 10, RULE_simpleUnitSymbols);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
            setState(82);
            _errHandler.sync(this);
            _alt = 1;
            do {
                switch (_alt) {
                case 1:
                    {
                    {
                    setState(81);
                    terminalUnitSymbol();
                    }
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
                }
                setState(84);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input,4,_ctx);
            } while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
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

    public static class AnnotationSymbolsContext extends ParserRuleContext {
        public List<WithinCbSymbolContext> withinCbSymbol() {
            return getRuleContexts(WithinCbSymbolContext.class);
        }
        public WithinCbSymbolContext withinCbSymbol(int i) {
            return getRuleContext(WithinCbSymbolContext.class,i);
        }
        public AnnotationSymbolsContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_annotationSymbols; }
    }

    public final AnnotationSymbolsContext annotationSymbols() throws RecognitionException {
        AnnotationSymbolsContext _localctx = new AnnotationSymbolsContext(_ctx, getState());
        enterRule(_localctx, 12, RULE_annotationSymbols);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
            setState(86);
            match(T__4);
            setState(88);
            _errHandler.sync(this);
            _la = _input.LA(1);
            do {
                {
                {
                setState(87);
                withinCbSymbol();
                }
                }
                setState(90);
                _errHandler.sync(this);
                _la = _input.LA(1);
            } while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3) | (1L << T__6) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << NON_DIGIT_TERMINAL_UNIT_SYMBOL) | (1L << DIGIT_SYMBOL))) != 0) );
            setState(92);
            match(T__5);
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

    public static class SquareBracketsSymbolsContext extends ParserRuleContext {
        public List<WithinSbSymbolContext> withinSbSymbol() {
            return getRuleContexts(WithinSbSymbolContext.class);
        }
        public WithinSbSymbolContext withinSbSymbol(int i) {
            return getRuleContext(WithinSbSymbolContext.class,i);
        }
        public SquareBracketsSymbolsContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_squareBracketsSymbols; }
    }

    public final SquareBracketsSymbolsContext squareBracketsSymbols() throws RecognitionException {
        SquareBracketsSymbolsContext _localctx = new SquareBracketsSymbolsContext(_ctx, getState());
        enterRule(_localctx, 14, RULE_squareBracketsSymbols);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
            setState(94);
            match(T__6);
            setState(96);
            _errHandler.sync(this);
            _la = _input.LA(1);
            do {
                {
                {
                setState(95);
                withinSbSymbol();
                }
                }
                setState(98);
                _errHandler.sync(this);
                _la = _input.LA(1);
            } while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << NON_DIGIT_TERMINAL_UNIT_SYMBOL) | (1L << DIGIT_SYMBOL))) != 0) );
            setState(100);
            match(T__7);
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

    public static class WithinSbSymbolContext extends ParserRuleContext {
        public WithinCbOrSbSymbolContext withinCbOrSbSymbol() {
            return getRuleContext(WithinCbOrSbSymbolContext.class,0);
        }
        public WithinSbSymbolContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_withinSbSymbol; }
    }

    public final WithinSbSymbolContext withinSbSymbol() throws RecognitionException {
        WithinSbSymbolContext _localctx = new WithinSbSymbolContext(_ctx, getState());
        enterRule(_localctx, 16, RULE_withinSbSymbol);
        try {
            setState(105);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
            case T__0:
            case T__1:
            case T__2:
            case T__3:
            case T__9:
            case T__10:
            case T__11:
            case T__12:
            case NON_DIGIT_TERMINAL_UNIT_SYMBOL:
            case DIGIT_SYMBOL:
                enterOuterAlt(_localctx, 1);
                {
                setState(102);
                withinCbOrSbSymbol();
                }
                break;
            case T__4:
                enterOuterAlt(_localctx, 2);
                {
                setState(103);
                match(T__4);
                }
                break;
            case T__5:
                enterOuterAlt(_localctx, 3);
                {
                setState(104);
                match(T__5);
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

    public static class WithinCbSymbolContext extends ParserRuleContext {
        public WithinCbOrSbSymbolContext withinCbOrSbSymbol() {
            return getRuleContext(WithinCbOrSbSymbolContext.class,0);
        }
        public WithinCbSymbolContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_withinCbSymbol; }
    }

    public final WithinCbSymbolContext withinCbSymbol() throws RecognitionException {
        WithinCbSymbolContext _localctx = new WithinCbSymbolContext(_ctx, getState());
        enterRule(_localctx, 18, RULE_withinCbSymbol);
        try {
            setState(111);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
            case T__0:
            case T__1:
            case T__2:
            case T__3:
            case T__9:
            case T__10:
            case T__11:
            case T__12:
            case NON_DIGIT_TERMINAL_UNIT_SYMBOL:
            case DIGIT_SYMBOL:
                enterOuterAlt(_localctx, 1);
                {
                setState(107);
                withinCbOrSbSymbol();
                }
                break;
            case T__8:
                enterOuterAlt(_localctx, 2);
                {
                setState(108);
                match(T__8);
                }
                break;
            case T__6:
                enterOuterAlt(_localctx, 3);
                {
                setState(109);
                match(T__6);
                }
                break;
            case T__7:
                enterOuterAlt(_localctx, 4);
                {
                setState(110);
                match(T__7);
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

    public static class WithinCbOrSbSymbolContext extends ParserRuleContext {
        public TerminalUnitSymbolContext terminalUnitSymbol() {
            return getRuleContext(TerminalUnitSymbolContext.class,0);
        }
        public WithinCbOrSbSymbolContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_withinCbOrSbSymbol; }
    }

    public final WithinCbOrSbSymbolContext withinCbOrSbSymbol() throws RecognitionException {
        WithinCbOrSbSymbolContext _localctx = new WithinCbOrSbSymbolContext(_ctx, getState());
        enterRule(_localctx, 20, RULE_withinCbOrSbSymbol);
        try {
            setState(122);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
            case NON_DIGIT_TERMINAL_UNIT_SYMBOL:
            case DIGIT_SYMBOL:
                enterOuterAlt(_localctx, 1);
                {
                setState(113);
                terminalUnitSymbol();
                }
                break;
            case T__9:
                enterOuterAlt(_localctx, 2);
                {
                setState(114);
                match(T__9);
                }
                break;
            case T__2:
                enterOuterAlt(_localctx, 3);
                {
                setState(115);
                match(T__2);
                }
                break;
            case T__3:
                enterOuterAlt(_localctx, 4);
                {
                setState(116);
                match(T__3);
                }
                break;
            case T__10:
                enterOuterAlt(_localctx, 5);
                {
                setState(117);
                match(T__10);
                }
                break;
            case T__11:
                enterOuterAlt(_localctx, 6);
                {
                setState(118);
                match(T__11);
                }
                break;
            case T__1:
                enterOuterAlt(_localctx, 7);
                {
                setState(119);
                match(T__1);
                }
                break;
            case T__0:
                enterOuterAlt(_localctx, 8);
                {
                setState(120);
                match(T__0);
                }
                break;
            case T__12:
                enterOuterAlt(_localctx, 9);
                {
                setState(121);
                match(T__12);
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

    public static class TerminalUnitSymbolContext extends ParserRuleContext {
        public TerminalNode NON_DIGIT_TERMINAL_UNIT_SYMBOL() { return getToken(UCUMParser.NON_DIGIT_TERMINAL_UNIT_SYMBOL, 0); }
        public TerminalNode DIGIT_SYMBOL() { return getToken(UCUMParser.DIGIT_SYMBOL, 0); }
        public TerminalUnitSymbolContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_terminalUnitSymbol; }
    }

    public final TerminalUnitSymbolContext terminalUnitSymbol() throws RecognitionException {
        TerminalUnitSymbolContext _localctx = new TerminalUnitSymbolContext(_ctx, getState());
        enterRule(_localctx, 22, RULE_terminalUnitSymbol);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
            setState(124);
            _la = _input.LA(1);
            if ( !(_la==NON_DIGIT_TERMINAL_UNIT_SYMBOL || _la==DIGIT_SYMBOL) ) {
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

    public static class ExponentContext extends ParserRuleContext {
        public DigitSymbolsContext digitSymbols() {
            return getRuleContext(DigitSymbolsContext.class,0);
        }
        public ExponentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_exponent; }
    }

    public final ExponentContext exponent() throws RecognitionException {
        ExponentContext _localctx = new ExponentContext(_ctx, getState());
        enterRule(_localctx, 24, RULE_exponent);
        int _la;
        try {
            setState(129);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
            case T__10:
            case T__11:
                enterOuterAlt(_localctx, 1);
                {
                setState(126);
                _la = _input.LA(1);
                if ( !(_la==T__10 || _la==T__11) ) {
                _errHandler.recoverInline(this);
                }
                else {
                    if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
                setState(127);
                digitSymbols();
                }
                break;
            case DIGIT_SYMBOL:
                enterOuterAlt(_localctx, 2);
                {
                setState(128);
                digitSymbols();
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

    public static class DigitSymbolsContext extends ParserRuleContext {
        public List<TerminalNode> DIGIT_SYMBOL() { return getTokens(UCUMParser.DIGIT_SYMBOL); }
        public TerminalNode DIGIT_SYMBOL(int i) {
            return getToken(UCUMParser.DIGIT_SYMBOL, i);
        }
        public DigitSymbolsContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }
        @Override public int getRuleIndex() { return RULE_digitSymbols; }
    }

    public final DigitSymbolsContext digitSymbols() throws RecognitionException {
        DigitSymbolsContext _localctx = new DigitSymbolsContext(_ctx, getState());
        enterRule(_localctx, 26, RULE_digitSymbols);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
            setState(132);
            _errHandler.sync(this);
            _la = _input.LA(1);
            do {
                {
                {
                setState(131);
                match(DIGIT_SYMBOL);
                }
                }
                setState(134);
                _errHandler.sync(this);
                _la = _input.LA(1);
            } while ( _la==DIGIT_SYMBOL );
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

    public static final String _serializedATN =
        "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\21\u008b\4\2\t\2"+
        "\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
        "\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\3\2\3\2\3\2\3\3\3\3\3\3\3\3"+
        "\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3-\n\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4"+
        "\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4>\n\4\3\5\3\5\3\5\3\5\5\5D\n\5\3\6\3\6"+
        "\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\5\6R\n\6\3\7\6\7U\n\7\r\7\16"+
        "\7V\3\b\3\b\6\b[\n\b\r\b\16\b\\\3\b\3\b\3\t\3\t\6\tc\n\t\r\t\16\td\3\t"+
        "\3\t\3\n\3\n\3\n\5\nl\n\n\3\13\3\13\3\13\3\13\5\13r\n\13\3\f\3\f\3\f\3"+
        "\f\3\f\3\f\3\f\3\f\3\f\5\f}\n\f\3\r\3\r\3\16\3\16\3\16\5\16\u0084\n\16"+
        "\3\17\6\17\u0087\n\17\r\17\16\17\u0088\3\17\2\2\20\2\4\6\b\n\f\16\20\22"+
        "\24\26\30\32\34\2\4\3\2\20\21\3\2\r\16\2\u009b\2\36\3\2\2\2\4,\3\2\2\2"+
        "\6=\3\2\2\2\bC\3\2\2\2\nQ\3\2\2\2\fT\3\2\2\2\16X\3\2\2\2\20`\3\2\2\2\22"+
        "k\3\2\2\2\24q\3\2\2\2\26|\3\2\2\2\30~\3\2\2\2\32\u0083\3\2\2\2\34\u0086"+
        "\3\2\2\2\36\37\5\4\3\2\37 \7\2\2\3 \3\3\2\2\2!-\5\6\4\2\"#\7\3\2\2#-\5"+
        "\4\3\2$%\5\6\4\2%&\7\3\2\2&\'\5\4\3\2\'-\3\2\2\2()\5\6\4\2)*\7\4\2\2*"+
        "+\5\4\3\2+-\3\2\2\2,!\3\2\2\2,\"\3\2\2\2,$\3\2\2\2,(\3\2\2\2-\5\3\2\2"+
        "\2./\7\5\2\2/\60\5\4\3\2\60\61\7\6\2\2\61>\3\2\2\2\62\63\7\5\2\2\63\64"+
        "\5\4\3\2\64\65\7\6\2\2\65\66\5\16\b\2\66>\3\2\2\2\678\5\b\5\289\5\16\b"+
        "\29>\3\2\2\2:>\5\16\b\2;>\5\b\5\2<>\5\34\17\2=.\3\2\2\2=\62\3\2\2\2=\67"+
        "\3\2\2\2=:\3\2\2\2=;\3\2\2\2=<\3\2\2\2>\7\3\2\2\2?D\5\n\6\2@A\5\n\6\2"+
        "AB\5\32\16\2BD\3\2\2\2C?\3\2\2\2C@\3\2\2\2D\t\3\2\2\2ER\5\f\7\2FR\5\20"+
        "\t\2GH\5\20\t\2HI\5\f\7\2IR\3\2\2\2JK\5\f\7\2KL\5\20\t\2LR\3\2\2\2MN\5"+
        "\f\7\2NO\5\20\t\2OP\5\f\7\2PR\3\2\2\2QE\3\2\2\2QF\3\2\2\2QG\3\2\2\2QJ"+
        "\3\2\2\2QM\3\2\2\2R\13\3\2\2\2SU\5\30\r\2TS\3\2\2\2UV\3\2\2\2VT\3\2\2"+
        "\2VW\3\2\2\2W\r\3\2\2\2XZ\7\7\2\2Y[\5\24\13\2ZY\3\2\2\2[\\\3\2\2\2\\Z"+
        "\3\2\2\2\\]\3\2\2\2]^\3\2\2\2^_\7\b\2\2_\17\3\2\2\2`b\7\t\2\2ac\5\22\n"+
        "\2ba\3\2\2\2cd\3\2\2\2db\3\2\2\2de\3\2\2\2ef\3\2\2\2fg\7\n\2\2g\21\3\2"+
        "\2\2hl\5\26\f\2il\7\7\2\2jl\7\b\2\2kh\3\2\2\2ki\3\2\2\2kj\3\2\2\2l\23"+
        "\3\2\2\2mr\5\26\f\2nr\7\13\2\2or\7\t\2\2pr\7\n\2\2qm\3\2\2\2qn\3\2\2\2"+
        "qo\3\2\2\2qp\3\2\2\2r\25\3\2\2\2s}\5\30\r\2t}\7\f\2\2u}\7\5\2\2v}\7\6"+
        "\2\2w}\7\r\2\2x}\7\16\2\2y}\7\4\2\2z}\7\3\2\2{}\7\17\2\2|s\3\2\2\2|t\3"+
        "\2\2\2|u\3\2\2\2|v\3\2\2\2|w\3\2\2\2|x\3\2\2\2|y\3\2\2\2|z\3\2\2\2|{\3"+
        "\2\2\2}\27\3\2\2\2~\177\t\2\2\2\177\31\3\2\2\2\u0080\u0081\t\3\2\2\u0081"+
        "\u0084\5\34\17\2\u0082\u0084\5\34\17\2\u0083\u0080\3\2\2\2\u0083\u0082"+
        "\3\2\2\2\u0084\33\3\2\2\2\u0085\u0087\7\21\2\2\u0086\u0085\3\2\2\2\u0087"+
        "\u0088\3\2\2\2\u0088\u0086\3\2\2\2\u0088\u0089\3\2\2\2\u0089\35\3\2\2"+
        "\2\16,=CQV\\dkq|\u0083\u0088";
    public static final ATN _ATN =
        new ATNDeserializer().deserialize(_serializedATN.toCharArray());
    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}