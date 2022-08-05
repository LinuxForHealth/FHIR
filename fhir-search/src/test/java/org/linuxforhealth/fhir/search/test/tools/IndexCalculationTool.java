/*
 * (C) Copyright IBM Corp. 2019, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.search.test.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.linuxforhealth.fhir.exception.FHIRException;
import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.generator.FHIRGenerator;
import org.linuxforhealth.fhir.model.generator.exception.FHIRGeneratorException;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.resource.Bundle;
import org.linuxforhealth.fhir.model.resource.SearchParameter;
import org.linuxforhealth.fhir.model.type.code.BundleType;
import org.linuxforhealth.fhir.path.FHIRPathElementNode;
import org.linuxforhealth.fhir.path.FHIRPathNode;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import org.linuxforhealth.fhir.search.SearchConstants;
import org.linuxforhealth.fhir.search.parameters.ParametersHelper;

/**
 * A standalone program for extracting and printing search parameter values
 */
public class IndexCalculationTool {

    public static final String FHIR_PATH = "Bundle.entry.resource.expression";

    /*
     * used in test to make the compiled code accessible.
     */
    static {
        System.setProperty("javax.xml.accessExternalSchema", "file");
    }

    /**
     * @param args
     * @throws FHIRGeneratorException
     * @throws IOException
     */
    public static void main(String[] args) throws FHIRGeneratorException, IOException {
        try (InputStream stream = ParametersHelper.class.getClassLoader().getResourceAsStream(ParametersHelper.FHIR_DEFAULT_SEARCH_PARAMETERS_FILE)) {
            if (stream == null) {
                System.out.println(String.format(ParametersHelper.ERROR_EXCEPTION, ParametersHelper.FHIR_DEFAULT_SEARCH_PARAMETERS_FILE));
            }
            Set<String> sets = process(stream);
            process(sets);
        }


    }

    /**
     * simple output method.
     *
     * @param params
     * @param out
     * @throws FHIRGeneratorException
     */
    public static void output(List<SearchParameter> params, java.io.PrintStream out) throws FHIRGeneratorException {
        Bundle.Builder build = Bundle.builder().type(BundleType.COLLECTION);
        for (SearchParameter param : params) {
            build.entry(Bundle.Entry.builder().resource(param).fullUrl(param.getUrl()).build());

        }

        Bundle bundle = build.build();
        FHIRGenerator.generator(Format.JSON, true).generate(bundle, out);
        out.println(bundle.toString());
    }

    /*
     * processes the search parameters expressions split into an Set.
     */
    private static Set<String> process(InputStream stream) {
        Set<String> searchExpressions = new HashSet<>();

        try {
            // The code is agnostic to format.
            Bundle bundle = FHIRParser.parser(Format.JSON).parse(stream);

            FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
            EvaluationContext evaluationContext = new EvaluationContext(bundle);
            Collection<FHIRPathNode> result = evaluator.evaluate(evaluationContext, FHIR_PATH);

            int count = 0;
            for (FHIRPathElementNode node : result.stream().map(a -> a.as(FHIRPathElementNode.class)).collect(Collectors.toList())) {
                String[] exprs = node.getValue().asStringValue().string().split("\\" + SearchConstants.PARAMETER_DELIMITER);
                for(String expr : exprs) {
                    searchExpressions.add(expr.trim());
                    count++;
                }

            }
            System.out.println("Total Expressions: " + count);
            System.out.println("Total Unique Expressions: " + searchExpressions.size());
        } catch (FHIRException fe) {
            // This exception is highly unlikely, but still possible.
            System.out.println(String.format(ParametersHelper.ERROR_EXCEPTION, ParametersHelper.FROM_STEAM));
        }

        return Collections.unmodifiableSet(searchExpressions);
    }

    private static void process(Set<String> uniques) {

        List<String> sorted = uniques.stream().sorted().collect(Collectors.toList());

        for(String uniq : sorted) {
            System.out.println(uniq);
        }
    }

}
