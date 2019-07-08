/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.path.util;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import com.ibm.watsonhealth.fhir.model.path.FHIRPathLexer;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathNode;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.ExpressionContext;
import com.ibm.watsonhealth.fhir.model.path.evaluator.FHIRPathEvaluator;
import com.ibm.watsonhealth.fhir.model.path.exception.FHIRPathException;

public final class FHIRPathUtil {
    private static ConcurrentMap<String, ExpressionContext> expressionCache = new ConcurrentHashMap<>();

    private FHIRPathUtil() {
    }

    public static Collection<FHIRPathNode> eval(String expr) throws FHIRPathException {
        return eval(expr, null);
    }

    public static Collection<FHIRPathNode> eval(String expr, Collection<FHIRPathNode> context) throws FHIRPathException {
        ExpressionContext expressionContext = expressionCache.computeIfAbsent(expr, k -> compile(expr));
        FHIRPathEvaluator evaluator = new FHIRPathEvaluator(expressionContext);
        return evaluator.evaluate(context);
    }

    private static ExpressionContext compile(String expr) {
        FHIRPathLexer lexer = new FHIRPathLexer(new ANTLRInputStream(expr));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        FHIRPathParser parser = new FHIRPathParser(tokens);
        return parser.expression();
    }
}
