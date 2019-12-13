/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.test;

import static com.ibm.fhir.model.util.ModelSupport.delimit;
import static com.ibm.fhir.model.util.ModelSupport.isKeyword;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.path.FHIRPathBaseVisitor;
import com.ibm.fhir.path.FHIRPathLexer;
import com.ibm.fhir.path.FHIRPathParser;
import com.ibm.fhir.path.FHIRPathParser.ExpressionContext;
import com.ibm.fhir.path.FHIRPathVisitor;
import com.ibm.fhir.path.function.FHIRPathFunction;

public class FHIRPathCacheSizeEstimator {
    public static void main(String[] args) {        
        Set<String> identifiers = new HashSet<>();
        for (Class<?> modelClass : ModelSupport.getModelClasses()) {
            String typeName = ModelSupport.getTypeName(modelClass);
            if (isKeyword(typeName)) {
                typeName = delimit(typeName);
            }
            identifiers.add(typeName);
            for (String elementName : ModelSupport.getElementNames(modelClass)) {
                if (isKeyword(elementName)) {
                    elementName = delimit(elementName);
                }
                identifiers.add(elementName);
            }
        }
        for (String functionName : FHIRPathFunction.registry().getFunctionNames()) {   
            if (isKeyword(functionName)) {
                functionName = delimit(functionName);
            }
            identifiers.add(functionName);
        }
        identifiers.addAll(Arrays.asList("all", "as", "exists", "iif", "is", "ofType", "select", "trace", "where"));
        System.out.println("identifiers: " + identifiers.size());
        
        Set<String> literals = new HashSet<>();
        FHIRPathVisitor<Void> visitor = new FHIRPathBaseVisitor<Void>() {
            public Void visitLiteralTerm(FHIRPathParser.LiteralTermContext ctx) {
                literals.add(ctx.getText());
                return null;
            }
        };
        
        Set<String> expressions = new HashSet<>();
        for (Class<?> modelClass : ModelSupport.getModelClasses()) {
            for (Constraint constraint : ModelSupport.getConstraints(modelClass)) {
                if (!Constraint.LOCATION_BASE.equals(constraint.location())) {
                    expressions.add(constraint.location());
                }
                expressions.add(constraint.expression());
                ExpressionContext expressionContext = compile(constraint.expression());
                expressionContext.accept(visitor);
            }
        }
        
        System.out.println("expressions: " + expressions.size());
        System.out.println("literals: " + literals.size());
    }
    
    private static ExpressionContext compile(String expr) {
        FHIRPathLexer lexer = new FHIRPathLexer(CharStreams.fromString(expr));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        FHIRPathParser parser = new FHIRPathParser(tokens);
        return parser.expression();
    }
}
