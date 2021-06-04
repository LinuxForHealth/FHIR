/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.ig.carin.bb.test;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.ExplanationOfBenefit;
import com.ibm.fhir.model.resource.ExplanationOfBenefit.Item;
import com.ibm.fhir.model.resource.ExplanationOfBenefit.Item.Adjudication;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.validation.FHIRValidator;
import com.ibm.fhir.validation.util.FHIRValidationUtil;

public class ExplanationOfBenefitTest {
    @Test
    public void testExplanationOfBenefit() throws Exception {
        ExplanationOfBenefit explanationOfBenefit = TestUtil.readExampleResource("json/profiles/fhir-ig-carin-bb/ExplanationOfBenefit-InpatientEOBExample1.json");
        List<Issue> issues = FHIRValidator.validator().validate(explanationOfBenefit);
        Assert.assertFalse(FHIRValidationUtil.hasErrors(issues));

        // add an 'allowedunits' adjudication slice to the first item
        List<Item> items = new ArrayList<>(explanationOfBenefit.getItem());
        Item item = items.get(0);
        item = item.toBuilder()
                .adjudication(Adjudication.builder()
                    .category(CodeableConcept.builder()
                        .coding(Coding.builder()
                            .system(Uri.of("http://hl7.org/fhir/us/carin-bb/CodeSystem/C4BBAdjudicationDiscriminator"))
                            .code(Code.of("allowedunits"))
                            .build())
                        .build())
                    .value(Decimal.of(1))
                    .build())
                .build();
        items.set(0, item);
        explanationOfBenefit = explanationOfBenefit
            .toBuilder()
            .item(items)
            .build();
        issues = FHIRValidator.validator().validate(explanationOfBenefit);
        Assert.assertFalse(FHIRValidationUtil.hasErrors(issues));
    }
}
