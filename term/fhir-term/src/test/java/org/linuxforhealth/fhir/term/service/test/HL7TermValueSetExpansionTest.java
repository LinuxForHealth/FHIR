/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.linuxforhealth.fhir.term.service.test;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.format.Format;
import org.linuxforhealth.fhir.model.parser.FHIRParser;
import org.linuxforhealth.fhir.model.resource.ValueSet;
import org.linuxforhealth.fhir.term.util.ValueSetSupport;

public class HL7TermValueSetExpansionTest {
    FHIRParser parser = FHIRParser.parser(Format.JSON);

    /**
     * Compare FHIRTermService ValueSet expansion to expanded ValueSets obtained from https://tx.fhir.org
     *
     * Note:  tx.fhir.org seems to have some stale expansions. In all of the following cases, our expansion
     * differed and I think our expansion is right for the version of the hl7.terminology package we're using (3.1.0).
     *
     * <ul>
     * <li> ValueSet-parent-relationship-codes.json
     * <li> ValueSet-v3-Confidentiality.json
     * <li> ValueSet-v3-PurposeOfUse.json
     * </ul>
     */
    @Test
    void testHl7TermValueSetExpansion() throws Exception {
        Path expandedValueSetsDir = Path.of("src/test/resources/tx-expanded-valuesets");

        Set<Path> valueSetFiles = Files.list(expandedValueSetsDir).collect(Collectors.toSet());
        for (Path path : valueSetFiles) {
            try (Reader reader = Files.newBufferedReader(path)) {
                ValueSet txExpandedValueSet = parser.parse(reader);
                String url = txExpandedValueSet.getUrl().getValue();

                // the version might not match, but these ValueSets shouldn't change much and so thats probably ok
                ValueSet ourValueSet = ValueSetSupport.getValueSet(url);
                ValueSet ourExpandedValueSet = ValueSetSupport.expand(ourValueSet);

                ValueSet.Expansion ourExpansion = ourExpandedValueSet.getExpansion();
                assertNotNull(ourExpansion);

                Set<String> ourConcepts = new HashSet<>();
                for (ValueSet.Expansion.Contains concept : ourExpandedValueSet.getExpansion().getContains()) {
                    ourConcepts.add(concept.getSystem().getValue() + "|" + concept.getCode().getValue());
                }

                Set<String> theirConcepts = new HashSet<>();
                for (ValueSet.Expansion.Contains concept : txExpandedValueSet.getExpansion().getContains()) {
                    theirConcepts.add(concept.getSystem().getValue() + "|" + concept.getCode().getValue());
                }

                Set<String> diffSet;

                diffSet = new HashSet<>(theirConcepts);
                diffSet.removeAll(ourConcepts);
                assertTrue(diffSet.isEmpty(), "all their codes are in our set: " + diffSet.toString());

                diffSet = new HashSet<>(ourConcepts);
                diffSet.removeAll(theirConcepts);
                assertTrue(diffSet.isEmpty(), "all our codes are in their set: " + diffSet.toString());
            }
        }
    }
}
