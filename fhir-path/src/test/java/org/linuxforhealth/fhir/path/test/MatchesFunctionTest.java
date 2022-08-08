/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.path.test;

import static org.linuxforhealth.fhir.model.type.String.string;
import static org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator.SINGLETON_TRUE;
import static org.testng.Assert.assertEquals;

import java.util.Collection;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.type.ContactPoint;
import org.linuxforhealth.fhir.model.type.code.ContactPointSystem;
import org.linuxforhealth.fhir.path.FHIRPathNode;
import org.linuxforhealth.fhir.path.evaluator.FHIRPathEvaluator;

public class MatchesFunctionTest {
    @Test
    public void testMatchesFunction() throws Exception {
        ContactPoint contactPoint = ContactPoint.builder()
                .system(ContactPointSystem.PHONE)
                .value(string("+1-703-362-1280"))
                .build();
        Collection<FHIRPathNode> result = FHIRPathEvaluator.evaluator().evaluate(contactPoint, "(system = 'phone' or system = 'fax') implies value.matches('^\\\\+[0-9]{1,3}-[0-9]{1,3}-[0-9]{3,4}-[0-9]{4}(;ext=[0-9]+)?$')");
        assertEquals(result, SINGLETON_TRUE);
    }
}
