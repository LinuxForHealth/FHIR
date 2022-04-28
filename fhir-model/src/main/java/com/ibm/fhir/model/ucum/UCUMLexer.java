/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

// Generated from fhir-model/UCUM.g4 by ANTLR 4.10.1
package com.ibm.fhir.model.ucum;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class UCUMLexer extends Lexer {
    static { RuntimeMetaData.checkVersion("4.10.1", RuntimeMetaData.VERSION); }

    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache =
        new PredictionContextCache();
    public static final int
        T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9,
        T__9=10, T__10=11, T__11=12, T__12=13, NON_DIGIT_TERMINAL_UNIT_SYMBOL=14,
        DIGIT_SYMBOL=15;
    public static String[] channelNames = {
        "DEFAULT_TOKEN_CHANNEL", "HIDDEN"
    };

    public static String[] modeNames = {
        "DEFAULT_MODE"
    };

    private static String[] makeRuleNames() {
        return new String[] {
            "T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8",
            "T__9", "T__10", "T__11", "T__12", "NON_DIGIT_TERMINAL_UNIT_SYMBOL",
            "DIGIT_SYMBOL"
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


    public UCUMLexer(CharStream input) {
        super(input);
        _interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
    }

    @Override
    public String getGrammarFileName() { return "UCUM.g4"; }

    @Override
    public String[] getRuleNames() { return ruleNames; }

    @Override
    public String getSerializedATN() { return _serializedATN; }

    @Override
    public String[] getChannelNames() { return channelNames; }

    @Override
    public String[] getModeNames() { return modeNames; }

    @Override
    public ATN getATN() { return _ATN; }

    public static final String _serializedATN =
        "\u0004\u0000\u000f=\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
        "\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
        "\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"+
        "\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b"+
        "\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0001"+
        "\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001"+
        "\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001"+
        "\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\t\u0001"+
        "\t\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\r\u0001"+
        "\r\u0001\u000e\u0001\u000e\u0000\u0000\u000f\u0001\u0001\u0003\u0002\u0005"+
        "\u0003\u0007\u0004\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011\t\u0013\n"+
        "\u0015\u000b\u0017\f\u0019\r\u001b\u000e\u001d\u000f\u0001\u0000\u0001"+
        "\n\u0000!!#\'**,,:<>Z\\\\^z||~~<\u0000\u0001\u0001\u0000\u0000\u0000\u0000"+
        "\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000"+
        "\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b"+
        "\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001"+
        "\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001"+
        "\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0000\u0017\u0001"+
        "\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000\u0000\u0000\u001b\u0001"+
        "\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000\u0000\u0001\u001f\u0001"+
        "\u0000\u0000\u0000\u0003!\u0001\u0000\u0000\u0000\u0005#\u0001\u0000\u0000"+
        "\u0000\u0007%\u0001\u0000\u0000\u0000\t\'\u0001\u0000\u0000\u0000\u000b"+
        ")\u0001\u0000\u0000\u0000\r+\u0001\u0000\u0000\u0000\u000f-\u0001\u0000"+
        "\u0000\u0000\u0011/\u0001\u0000\u0000\u0000\u00131\u0001\u0000\u0000\u0000"+
        "\u00153\u0001\u0000\u0000\u0000\u00175\u0001\u0000\u0000\u0000\u00197"+
        "\u0001\u0000\u0000\u0000\u001b9\u0001\u0000\u0000\u0000\u001d;\u0001\u0000"+
        "\u0000\u0000\u001f \u0005/\u0000\u0000 \u0002\u0001\u0000\u0000\u0000"+
        "!\"\u0005.\u0000\u0000\"\u0004\u0001\u0000\u0000\u0000#$\u0005(\u0000"+
        "\u0000$\u0006\u0001\u0000\u0000\u0000%&\u0005)\u0000\u0000&\b\u0001\u0000"+
        "\u0000\u0000\'(\u0005{\u0000\u0000(\n\u0001\u0000\u0000\u0000)*\u0005"+
        "}\u0000\u0000*\f\u0001\u0000\u0000\u0000+,\u0005[\u0000\u0000,\u000e\u0001"+
        "\u0000\u0000\u0000-.\u0005]\u0000\u0000.\u0010\u0001\u0000\u0000\u0000"+
        "/0\u0005 \u0000\u00000\u0012\u0001\u0000\u0000\u000012\u0005\"\u0000\u0000"+
        "2\u0014\u0001\u0000\u0000\u000034\u0005+\u0000\u00004\u0016\u0001\u0000"+
        "\u0000\u000056\u0005-\u0000\u00006\u0018\u0001\u0000\u0000\u000078\u0005"+
        "=\u0000\u00008\u001a\u0001\u0000\u0000\u00009:\u0007\u0000\u0000\u0000"+
        ":\u001c\u0001\u0000\u0000\u0000;<\u000209\u0000<\u001e\u0001\u0000\u0000"+
        "\u0000\u0001\u0000\u0000";
    public static final ATN _ATN =
        new ATNDeserializer().deserialize(_serializedATN.toCharArray());
    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}