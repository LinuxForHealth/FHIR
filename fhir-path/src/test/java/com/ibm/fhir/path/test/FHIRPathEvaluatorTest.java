/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.path.test;

import java.io.FilterOutputStream;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.UUID;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Integer;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;

public class FHIRPathEvaluatorTest {
    public static void main(java.lang.String[] args) throws Exception {
        java.lang.String id = UUID.randomUUID().toString();

        Meta meta = Meta.builder().versionId(Id.of("1"))
                .lastUpdated(Instant.now(ZoneOffset.UTC))
                .build();

        String given = String.builder().value("John")
                .extension(Extension.builder()
                    .url("http://www.ibm.com/someExtension")
                    .value(String.of("value and extension"))
                    .build())
                .build();

        String otherGiven = String.builder()
                .extension(Extension.builder()
                    .url("http://www.ibm.com/someExtension")
                    .value(String.of("extension only"))
                    .build())
                .build();

        HumanName name = HumanName.builder()
                .id("someId")
                .given(given)
                .given(otherGiven)
                .given(String.of("value no extension"))
                .family(String.of("Doe"))
                .build();

        Resource patient1 = TestUtil.readExampleResource("json/ibm/minimal/Patient-1.json").toBuilder().id("1").build();
        Resource patient2 = TestUtil.readExampleResource("json/ibm/minimal/Patient-2.json").toBuilder().id("2").build();

        Patient patient = Patient.builder()
                .id(id)
                .contained(patient1, patient2)
                .active(Boolean.TRUE)
                .deceased(Boolean.FALSE)
                .multipleBirth(Integer.of(2))
                .meta(meta)
                .name(name)
                .birthDate(Date.of(LocalDate.now()))
                .build();

        FilterOutputStream out = new FilterOutputStream(System.out) {
            @Override
            public void close() {
                // do nothing
            }
        };

        FHIRGenerator.generator(Format.JSON, true).generate(patient, out);

        System.out.println("");

        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        EvaluationContext evaluationContext = new EvaluationContext(patient);

        Collection<FHIRPathNode> result = evaluator.evaluate(evaluationContext, "%rootResource.contained.id");

        System.out.println("result: " + result);
    }
}
