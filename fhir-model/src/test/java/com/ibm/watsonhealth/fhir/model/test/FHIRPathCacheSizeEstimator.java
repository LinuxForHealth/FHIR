/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.model.test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import com.ibm.watsonhealth.fhir.model.annotation.Constraint;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathBaseVisitor;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathLexer;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathParser.ExpressionContext;
import com.ibm.watsonhealth.fhir.model.path.FHIRPathVisitor;
import com.ibm.watsonhealth.fhir.model.path.function.FHIRPathFunction;
import com.ibm.watsonhealth.fhir.model.path.util.FHIRPathUtil;
import com.ibm.watsonhealth.fhir.model.util.ModelSupport;

public class FHIRPathCacheSizeEstimator {
    public static void main(String[] args) {        
        Set<String> identifiers = new HashSet<>();
        for (Class<?> modelClass : ModelSupport.getModelClasses()) {
            String typeName = ModelSupport.getTypeName(modelClass);
            if (FHIRPathUtil.isKeyword(typeName)) {
                typeName = FHIRPathUtil.delimit(typeName);
            }
            identifiers.add(typeName);
            for (String elementName : ModelSupport.getElementNames(modelClass)) {
                if (FHIRPathUtil.isKeyword(elementName)) {
                    elementName = FHIRPathUtil.delimit(elementName);
                }
                identifiers.add(elementName);
            }
        }
        for (String functionName : FHIRPathFunction.registry().getFunctionNames()) {   
            if (FHIRPathUtil.isKeyword(functionName)) {
                functionName = FHIRPathUtil.delimit(functionName);
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
        FHIRPathLexer lexer = new FHIRPathLexer(new ANTLRInputStream(expr));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        FHIRPathParser parser = new FHIRPathParser(tokens);
        return parser.expression();
    }
}
