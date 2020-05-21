/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.omop.test;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.Condition;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;

public class ConditionAnalysis {
    public static void main(String[] args) throws Exception {

        Map<CodeableConcept, AtomicInteger> conditionCodeMap = new HashMap<>();

        File dir = new File("src/test/resources/fhir/");
        for (File file : dir.listFiles()) {
            if (!file.isDirectory()) {
                System.out.println(file.getName());
                try (FileReader reader = new FileReader(file)) {
                    try {
                        Bundle bundle = FHIRParser.parser(Format.JSON).parse(reader);
                        for (Entry entry : bundle.getEntry()) {
                            Resource resource = entry.getResource();
                            if (resource instanceof Condition) {
                                Condition condition = (Condition) resource;
                                CodeableConcept code = condition.getCode();
                                conditionCodeMap.computeIfAbsent(code, k -> new AtomicInteger()).incrementAndGet();
                            }

                        }
                    } catch (FHIRParserException e) {
                        System.err.println("Unable to load: " + file.getName() + " due to exception: " + e.getMessage());
                    }
                }
            }
        }

        List<Map.Entry<CodeableConcept, AtomicInteger>> entries = new ArrayList<>(conditionCodeMap.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<CodeableConcept, AtomicInteger>>() {
            @Override
            public int compare(Map.Entry<CodeableConcept, AtomicInteger> first, Map.Entry<CodeableConcept, AtomicInteger> second) {
                return second.getValue().get() - first.getValue().get();
            }
        });

        for (Map.Entry<CodeableConcept, AtomicInteger> entry : entries) {
            CodeableConcept key = entry.getKey();
            Coding coding = key.getCoding().get(0);
            String code = coding.getCode().getValue();
            String display = coding.getDisplay().getValue();
            AtomicInteger value = entry.getValue();
            System.out.println(code + "|" + display + "| : " + value.get());
        }
    }
}
