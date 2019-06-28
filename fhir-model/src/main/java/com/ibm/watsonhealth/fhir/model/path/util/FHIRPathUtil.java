/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path.util;

/**
 * IBM Confidential OCO Source Materials
 * 5737-D31, 5737-A56
 * (C) Copyright IBM Corp. 2019
 * 
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has
 * been deposited with the U.S. Copyright Office.
 * 
 */

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import com.ibm.watsonhealth.fhir.model.path.FHIRPathLexer;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.ExpressionContext;
import com.ibm.watsonhealth.fhir.model.path.evaluator.FHIRPathEvaluator;

public class FHIRPathUtil {
    private static ConcurrentMap<String, ExpressionContext> expressionCache = new ConcurrentHashMap<>();

    private FHIRPathUtil() {
    }

    public static <T> T eval(String expr) {
        return eval(expr, null);
    }

    @SuppressWarnings("unchecked")
    public static <T> T eval(String expr, Object context) {
        ExpressionContext expressionContext = expressionCache.computeIfAbsent(expr, k -> compile(expr));
        FHIRPathEvaluator evaluator = new FHIRPathEvaluator(expressionContext);
        return (T) evaluator.evaluate(context);
    }

    private static ExpressionContext compile(String expr) {
        FHIRPathLexer lexer = new FHIRPathLexer(new ANTLRInputStream(expr));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        FHIRPathParser parser = new FHIRPathParser(tokens);
        return parser.expression();
    }
}
