/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.validation.test;

import java.io.InputStream;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.type.BundleType;
import com.ibm.fhir.model.resource.ExplanationOfBenefit;
import com.ibm.fhir.validation.FHIRValidator;

public class ExplanationOfBenefitTest {
    public static void main(String[] args) throws Exception {
        try (InputStream in = ExplanationOfBenefitTest.class.getClassLoader().getResourceAsStream("JSON/explanationofbenefit.json")) {
            ExplanationOfBenefit explanationOfBenefit = FHIRParser.parser(Format.JSON).parse(in);
            FHIRGenerator.generator(Format.JSON, true).generate(explanationOfBenefit, System.out);
            FHIRValidator.validator().validate(explanationOfBenefit).forEach(System.out::println);
            Bundle bundle = Bundle.builder()
                .type(BundleType.COLLECTION)
                .entry(Entry.builder()
                    .resource(explanationOfBenefit)
                    .build())
                .build();
            FHIRGenerator.generator(Format.JSON, true).generate(bundle, System.out);
            FHIRValidator.validator().validate(bundle).forEach(System.out::println);
        }
    }
}