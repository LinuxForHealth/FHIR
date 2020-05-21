/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.omop.generator.factory;

import java.util.Objects;

import com.ibm.fhir.omop.generator.Generator;
import com.ibm.fhir.omop.generator.impl.V5GeneratorImpl;
import com.ibm.fhir.omop.generator.impl.V6GeneratorImpl;
import com.ibm.fhir.omop.version.Version;
import com.ibm.fhir.omop.vocab.VocabService;

public class GeneratorFactory {
    private GeneratorFactory() { }

    public static Generator generator(Version version, VocabService vocabService) {
        Objects.requireNonNull(version);
        Objects.requireNonNull(vocabService);
        switch (version) {
        case OMOP_CDM_V5_3_1:
            return new V5GeneratorImpl(vocabService);
        case OMOP_CDM_V6_0:
            return new V6GeneratorImpl(vocabService);
        default:
            throw new IllegalArgumentException("Unsupported version: " + version);
        }
    }
}
