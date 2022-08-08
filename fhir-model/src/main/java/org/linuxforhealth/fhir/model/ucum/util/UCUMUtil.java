/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.model.ucum.util;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import org.linuxforhealth.fhir.model.ucum.UCUMLexer;
import org.linuxforhealth.fhir.model.ucum.UCUMParser;
import org.linuxforhealth.fhir.model.ucum.UCUMParser.MainTermContext;

/**
 * Utility class for UCUM.
 */
public class UCUMUtil {

    private static final ANTLRErrorListener SYNTAX_ERROR_LISTENER = new BaseErrorListener() {

        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
            throw new ParseCancellationException(String.format("line %d:%d %s", line, charPositionInLine, msg), e);
        }
    };

    private UCUMUtil() {
    }

    /**
     * Determines if the string is a valid UCUM string.
     * 
     * @param ucumString
     *            the string
     * @return true or false
     */
    public static boolean isValidUcum(String ucumString) {
        try {
            UCUMLexer lexer = new UCUMLexer(CharStreams.fromString(ucumString));
            lexer.removeErrorListeners();
            lexer.addErrorListener(SYNTAX_ERROR_LISTENER);

            CommonTokenStream tokens = new CommonTokenStream(lexer);

            UCUMParser parser = new UCUMParser(tokens);
            parser.removeErrorListeners();
            parser.addErrorListener(SYNTAX_ERROR_LISTENER);

            MainTermContext expression = parser.mainTerm();
            return expression != null;
        } catch (ParseCancellationException e) {
            return false;
        }
    }
}
