/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.test.tools;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.SearchParameter;
import com.ibm.fhir.model.type.code.ResourceType;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.search.parameters.ParametersUtil;
import com.ibm.fhir.search.valuetypes.impl.ValueTypesR4Impl;

/**
 * Builds the output properties file for the ValueTypesUtil's cache.
 * 
 * @author pbastide
 *
 */
public class ValueTypesTool {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        
        // The code block is isolating the effects of exceptions by capturing the exceptions here
        // and returning an empty searchParameterMap, and continuing operation.
        // The failure is logged out.
        try (InputStream stream = ParametersUtil.class.getClassLoader().getResourceAsStream(ParametersUtil.FHIR_DEFAULT_SEARCH_PARAMETERS_FILE)) {
            // The code is agnostic to format.
            Bundle bundle = FHIRParser.parser(Format.JSON).parse(stream);  

            FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
            EvaluationContext evaluationContext = new EvaluationContext(bundle);
            Collection<FHIRPathNode> result = evaluator.evaluate(evaluationContext, ParametersUtil.FHIR_PATH_BUNDLE_ENTRY);

            outputJsonHeader();
            for (SearchParameter parameter : result.stream().map(node -> node.asResourceNode().resource().as(SearchParameter.class)).collect(Collectors.toList())) {

                // Only add the ones that have expressions.
                if (parameter.getType().getValue().compareTo("composite") != 0 && parameter.getExpression() != null
                        && parameter.getExpression().getValue().compareTo("Patient.deceased.exists() and Patient.deceased != false") != 0) {

                    List<ResourceType> types = parameter.getBase();
                    for (ResourceType type : types) {
                        String base = type.getValue();

                        // Need - name|base
                        String hash = ValueTypesR4Impl.hash(base, parameter.getCode().getValue());
                        outputJson(hash, getFieldTypesForExpression(parameter.getExpression().getValue()));

                    }
                }

            }
            outputJsonFooter();
        } catch (FHIRException fe) {

            fe.printStackTrace();
        }

    }

    public static void outputJsonHeader() {
        // Header:
        System.out.println("{");
        System.out.println("\"value-types\": \"default\", ");
        System.out.println("\"mappings\": [");
    }

    public static void outputJsonFooter() {
        // Header:
        System.out.println("]}");
    }

    public static void outputJson(String hash, String value) {

        // {
        // "resourceType": "Observation",
        // "name": "combo-code",
        // "targetClasses": ["CodeableConcept"]
        // }
        System.out.println("{\"resourceType\": \""+ hash.split("\\.")[0]+"\",");
        System.out.println("\"name\": \""+hash.split("\\.")[1] + "\",");
        System.out.println("\"targetClasses\": [" + value.replace(",","\",\"").replace("[", "\"").replace("]", "\"") + "]");
        System.out.println("},");

    }

    public static String getFieldTypesForExpression(String expressions) {
        String[] exprs = expressions.split("\\|");
        // System.out.println("#Uncleansed->" + expressions);
        return "" + Arrays.asList(exprs).stream().map(e -> cleanse(e)).map(e -> discoverType(e)).collect(Collectors.toSet());
    }

    /**
     * discovers the type through a walk of the Structured definition. 
     * @param expression
     * @return
     */
    public static String discoverType(String expression) {
        // Specific to InsurancePlan.
        if ("name".compareTo(expression.trim()) == 0 || "alias".compareTo(expression.trim()) == 0) {
            expression = "InsurancePlan." + expression;
        }

        // System.out.println("#Cleansed->" + expression);
        String[] components = expression.split("\\.");

        Class<?> newClass = processClass("com.ibm.fhir.model.resource", components[0]);
        if (newClass == null) {
            newClass = processClass("com.ibm.fhir.model.type", components[0]);
        }

        try {
            // System.out.println(expression);
            return generateFields(newClass, components);
        } catch (ClassNotFoundException e) {
            return "";
        }
    }

    /**
     * navigates down the path from one class down to through to the end results.
     * 
     * @param c
     * @param paths
     * @return
     * @throws ClassNotFoundException
     */
    public static String generateFields(Class<?> c, String... paths) throws ClassNotFoundException {
        // Where in the path we're at in the processing:
        Class<?> outputClass = c;

        if (paths.length == 1) {
            String[] tmpPath = new String[2];
            tmpPath[0] = c.getSimpleName();
            tmpPath[1] = paths[0];
            paths = tmpPath;
        }

        for (int current = 1; current < paths.length; current++) {
            // System.out.println(outputClass);
            // Specific to InsurancePlan.

            outputClass = getNextClass(outputClass, paths[current]);
        }

        if (outputClass == null) {
            return paths[0];
        }

        return outputClass.getSimpleName();
    }

    /**
     * get Next Class in the path based on the field.
     * 
     * @param inputClass
     * @param field
     * @return
     * @throws ClassNotFoundException
     */
    public static Class<?> getNextClass(Class<?> inputClass, String field) throws ClassNotFoundException {

        for (Field f : inputClass.getDeclaredFields()) {
            String fStr = f.getName();
            if (field.compareTo("class") == 0) {
                field = field.replace("class", "clazz");
            }
            // System.out.println("f-> " + fStr + " " + field);
            if (fStr.compareTo(field) == 0) {

                Class<?> tmpClass = inputClass;
                // System.out.println("-->" + inputClass.getSimpleName() + "-->" + f.getName());
                java.lang.reflect.Type genericType = f.getGenericType();

                if (genericType instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) genericType;
                    tmpClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];

                } else {
                    tmpClass = Class.forName(genericType.getTypeName());
                }

                return tmpClass;
            }

        }
        return null;
    }

    /**
     * cleanses the input expression
     * 
     * @param path
     * @return
     */
    public static String cleanse(String path) {

        // Trim left and right
        path = path.trim();

        // Specific to InsurancePlan.
        if ("name".compareTo(path.trim()) == 0 || "alias".compareTo(path.trim()) == 0) {
            path = "InsurancePlan." + path;
        }

        if (path.startsWith("(")) {
            int start = path.indexOf('(');
            int end = path.lastIndexOf(')');
            path = path.substring(start + 1, end);
        }

        if (path.contains(" as ")) {
            // System.out.println("as is FOUND");
            // This value may be the target class (keep this in mind)
            int start = path.indexOf(" as ");
            int end = path.indexOf(start + " as ".length(), ' ');

            if (end == -1) {
                end = path.length();
            } else {
                end++;
            }

            path = removeAsType(path);
        }

        path = path.replace(".where(", "(");
        path = path.replace(".as(", "(");

        path = processBalanced(path, '(', ')');
        path = processBalanced(path, '[', ']');

        // Find where
        while (path.contains(".where(")) {
            // May have a problem in a nested where
            int start = path.indexOf(".where(");
            int end = path.indexOf(')', start) + 1;

            String tmpPath = path.substring(0, start) + path.substring(end, path.length());
            path = tmpPath;

        }

        return path;
    }

    public static String removeAsType(String path) {

        while (path.indexOf(" as ") > 0) {
            int startIdx = path.indexOf(" as ");
            int startScanIdx = path.indexOf(" as ") + 4;
            int endIdx = startScanIdx;

            boolean found = false;
            while (!found && endIdx != path.length() - 1) {
                char charAt = path.charAt(endIdx);

                if (charAt == ')') {
                    found = true;
                } else if (charAt == ' ') {
                    found = true;

                }

                endIdx++;
            }

            path = path.substring(0, startIdx) + path.substring(endIdx + 1, path.length());

        }
        return path;
    }

    public static String processBalanced(String path, char left, char right) {
        StringBuilder buildWithoutNesting = new StringBuilder();
        if (path.indexOf(left) > 0) {
            int leftBracket = 0;
            int rightBracket = 0;
            String tmpPath = path;

            for (int idx = 0; idx < tmpPath.length(); idx++) {
                char charAt = tmpPath.charAt(idx);
                if (charAt == left) {
                    leftBracket++;
                } else if (charAt == right) {
                    rightBracket++;
                } else {
                    if ((leftBracket - rightBracket) % 2 == 0 || leftBracket == 0) {
                        buildWithoutNesting.append(charAt);
                    }
                }
            }
            path = buildWithoutNesting.toString();
        }
        return path;
    }

    /*
     * helper function processes for the given package name and class name.
     * @param pkg
     * @param clzVal
     * @return
     */
    public static Class<?> processClass(String pkg, String clzVal) {
        try {
            return Class.forName(pkg + "." + clzVal);
        } catch (java.lang.IllegalArgumentException | ClassNotFoundException iae) {
            // no op
            return null;
        }
    }

}
