/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

// Generated from fhir-model/UCUM.g4 by ANTLR 4.10.1
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
    static { RuntimeMetaData.checkVersion("4.10.1", RuntimeMetaData.VERSION); }

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
        "\u0004\u0001\u000f\u0089\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
        "\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004"+
        "\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007"+
        "\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b"+
        "\u0002\f\u0007\f\u0002\r\u0007\r\u0001\u0000\u0001\u0000\u0001\u0000\u0001"+
        "\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
        "\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0003\u0001+\b"+
        "\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
        "\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
        "\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0003\u0002<\b\u0002\u0001"+
        "\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0003\u0003B\b\u0003\u0001"+
        "\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001"+
        "\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0003"+
        "\u0004P\b\u0004\u0001\u0005\u0004\u0005S\b\u0005\u000b\u0005\f\u0005T"+
        "\u0001\u0006\u0001\u0006\u0004\u0006Y\b\u0006\u000b\u0006\f\u0006Z\u0001"+
        "\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0004\u0007a\b\u0007\u000b"+
        "\u0007\f\u0007b\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\b\u0003"+
        "\bj\b\b\u0001\t\u0001\t\u0001\t\u0001\t\u0003\tp\b\t\u0001\n\u0001\n\u0001"+
        "\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0003\n{\b\n\u0001"+
        "\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f\u0003\f\u0082\b\f\u0001\r\u0004"+
        "\r\u0085\b\r\u000b\r\f\r\u0086\u0001\r\u0000\u0000\u000e\u0000\u0002\u0004"+
        "\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u0000\u0002\u0001"+
        "\u0000\u000e\u000f\u0001\u0000\u000b\f\u0099\u0000\u001c\u0001\u0000\u0000"+
        "\u0000\u0002*\u0001\u0000\u0000\u0000\u0004;\u0001\u0000\u0000\u0000\u0006"+
        "A\u0001\u0000\u0000\u0000\bO\u0001\u0000\u0000\u0000\nR\u0001\u0000\u0000"+
        "\u0000\fV\u0001\u0000\u0000\u0000\u000e^\u0001\u0000\u0000\u0000\u0010"+
        "i\u0001\u0000\u0000\u0000\u0012o\u0001\u0000\u0000\u0000\u0014z\u0001"+
        "\u0000\u0000\u0000\u0016|\u0001\u0000\u0000\u0000\u0018\u0081\u0001\u0000"+
        "\u0000\u0000\u001a\u0084\u0001\u0000\u0000\u0000\u001c\u001d\u0003\u0002"+
        "\u0001\u0000\u001d\u001e\u0005\u0000\u0000\u0001\u001e\u0001\u0001\u0000"+
        "\u0000\u0000\u001f+\u0003\u0004\u0002\u0000 !\u0005\u0001\u0000\u0000"+
        "!+\u0003\u0002\u0001\u0000\"#\u0003\u0004\u0002\u0000#$\u0005\u0001\u0000"+
        "\u0000$%\u0003\u0002\u0001\u0000%+\u0001\u0000\u0000\u0000&\'\u0003\u0004"+
        "\u0002\u0000\'(\u0005\u0002\u0000\u0000()\u0003\u0002\u0001\u0000)+\u0001"+
        "\u0000\u0000\u0000*\u001f\u0001\u0000\u0000\u0000* \u0001\u0000\u0000"+
        "\u0000*\"\u0001\u0000\u0000\u0000*&\u0001\u0000\u0000\u0000+\u0003\u0001"+
        "\u0000\u0000\u0000,-\u0005\u0003\u0000\u0000-.\u0003\u0002\u0001\u0000"+
        "./\u0005\u0004\u0000\u0000/<\u0001\u0000\u0000\u000001\u0005\u0003\u0000"+
        "\u000012\u0003\u0002\u0001\u000023\u0005\u0004\u0000\u000034\u0003\f\u0006"+
        "\u00004<\u0001\u0000\u0000\u000056\u0003\u0006\u0003\u000067\u0003\f\u0006"+
        "\u00007<\u0001\u0000\u0000\u00008<\u0003\f\u0006\u00009<\u0003\u0006\u0003"+
        "\u0000:<\u0003\u001a\r\u0000;,\u0001\u0000\u0000\u0000;0\u0001\u0000\u0000"+
        "\u0000;5\u0001\u0000\u0000\u0000;8\u0001\u0000\u0000\u0000;9\u0001\u0000"+
        "\u0000\u0000;:\u0001\u0000\u0000\u0000<\u0005\u0001\u0000\u0000\u0000"+
        "=B\u0003\b\u0004\u0000>?\u0003\b\u0004\u0000?@\u0003\u0018\f\u0000@B\u0001"+
        "\u0000\u0000\u0000A=\u0001\u0000\u0000\u0000A>\u0001\u0000\u0000\u0000"+
        "B\u0007\u0001\u0000\u0000\u0000CP\u0003\n\u0005\u0000DP\u0003\u000e\u0007"+
        "\u0000EF\u0003\u000e\u0007\u0000FG\u0003\n\u0005\u0000GP\u0001\u0000\u0000"+
        "\u0000HI\u0003\n\u0005\u0000IJ\u0003\u000e\u0007\u0000JP\u0001\u0000\u0000"+
        "\u0000KL\u0003\n\u0005\u0000LM\u0003\u000e\u0007\u0000MN\u0003\n\u0005"+
        "\u0000NP\u0001\u0000\u0000\u0000OC\u0001\u0000\u0000\u0000OD\u0001\u0000"+
        "\u0000\u0000OE\u0001\u0000\u0000\u0000OH\u0001\u0000\u0000\u0000OK\u0001"+
        "\u0000\u0000\u0000P\t\u0001\u0000\u0000\u0000QS\u0003\u0016\u000b\u0000"+
        "RQ\u0001\u0000\u0000\u0000ST\u0001\u0000\u0000\u0000TR\u0001\u0000\u0000"+
        "\u0000TU\u0001\u0000\u0000\u0000U\u000b\u0001\u0000\u0000\u0000VX\u0005"+
        "\u0005\u0000\u0000WY\u0003\u0012\t\u0000XW\u0001\u0000\u0000\u0000YZ\u0001"+
        "\u0000\u0000\u0000ZX\u0001\u0000\u0000\u0000Z[\u0001\u0000\u0000\u0000"+
        "[\\\u0001\u0000\u0000\u0000\\]\u0005\u0006\u0000\u0000]\r\u0001\u0000"+
        "\u0000\u0000^`\u0005\u0007\u0000\u0000_a\u0003\u0010\b\u0000`_\u0001\u0000"+
        "\u0000\u0000ab\u0001\u0000\u0000\u0000b`\u0001\u0000\u0000\u0000bc\u0001"+
        "\u0000\u0000\u0000cd\u0001\u0000\u0000\u0000de\u0005\b\u0000\u0000e\u000f"+
        "\u0001\u0000\u0000\u0000fj\u0003\u0014\n\u0000gj\u0005\u0005\u0000\u0000"+
        "hj\u0005\u0006\u0000\u0000if\u0001\u0000\u0000\u0000ig\u0001\u0000\u0000"+
        "\u0000ih\u0001\u0000\u0000\u0000j\u0011\u0001\u0000\u0000\u0000kp\u0003"+
        "\u0014\n\u0000lp\u0005\t\u0000\u0000mp\u0005\u0007\u0000\u0000np\u0005"+
        "\b\u0000\u0000ok\u0001\u0000\u0000\u0000ol\u0001\u0000\u0000\u0000om\u0001"+
        "\u0000\u0000\u0000on\u0001\u0000\u0000\u0000p\u0013\u0001\u0000\u0000"+
        "\u0000q{\u0003\u0016\u000b\u0000r{\u0005\n\u0000\u0000s{\u0005\u0003\u0000"+
        "\u0000t{\u0005\u0004\u0000\u0000u{\u0005\u000b\u0000\u0000v{\u0005\f\u0000"+
        "\u0000w{\u0005\u0002\u0000\u0000x{\u0005\u0001\u0000\u0000y{\u0005\r\u0000"+
        "\u0000zq\u0001\u0000\u0000\u0000zr\u0001\u0000\u0000\u0000zs\u0001\u0000"+
        "\u0000\u0000zt\u0001\u0000\u0000\u0000zu\u0001\u0000\u0000\u0000zv\u0001"+
        "\u0000\u0000\u0000zw\u0001\u0000\u0000\u0000zx\u0001\u0000\u0000\u0000"+
        "zy\u0001\u0000\u0000\u0000{\u0015\u0001\u0000\u0000\u0000|}\u0007\u0000"+
        "\u0000\u0000}\u0017\u0001\u0000\u0000\u0000~\u007f\u0007\u0001\u0000\u0000"+
        "\u007f\u0082\u0003\u001a\r\u0000\u0080\u0082\u0003\u001a\r\u0000\u0081"+
        "~\u0001\u0000\u0000\u0000\u0081\u0080\u0001\u0000\u0000\u0000\u0082\u0019"+
        "\u0001\u0000\u0000\u0000\u0083\u0085\u0005\u000f\u0000\u0000\u0084\u0083"+
        "\u0001\u0000\u0000\u0000\u0085\u0086\u0001\u0000\u0000\u0000\u0086\u0084"+
        "\u0001\u0000\u0000\u0000\u0086\u0087\u0001\u0000\u0000\u0000\u0087\u001b"+
        "\u0001\u0000\u0000\u0000\f*;AOTZbioz\u0081\u0086";
    public static final ATN _ATN =
        new ATNDeserializer().deserialize(_serializedATN.toCharArray());
    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}