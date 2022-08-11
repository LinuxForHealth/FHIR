/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.validation.test;


import static org.linuxforhealth.fhir.path.util.FHIRPathUtil.compile;
import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.Test;

import org.linuxforhealth.fhir.model.annotation.Constraint;
import org.linuxforhealth.fhir.model.resource.StructureDefinition;
import org.linuxforhealth.fhir.profile.ConstraintGenerator;
import org.linuxforhealth.fhir.registry.FHIRRegistry;

public class ConditionProfileTest {
    @Test
    public void testConditionProfile() throws Exception {
        StructureDefinition profile = FHIRRegistry.getInstance().getResource("http://example.com/fhir/StructureDefinition/my-condition",  StructureDefinition.class);
        ConstraintGenerator generator = new ConstraintGenerator(profile);
        List<Constraint> constraints = generator.generate();
        constraints.forEach(System.out::println);
        constraints.forEach(constraint -> compile(constraint.expression()));
        assertEquals(constraints.size(), 4);
        assertEquals(constraints.get(0).expression(), "category.exists() implies (category.count() >= 1 and category.all(memberOf('http://hl7.org/fhir/ValueSet/condition-category', 'required')))");
        assertEquals(constraints.get(1).expression(), "severity.exists() implies (severity.exists() and severity.all(memberOf('http://hl7.org/fhir/ValueSet/condition-severity', 'required')))");
        assertEquals(constraints.get(2).expression(), "code.exists() and code.all(memberOf('http://hl7.org/fhir/ValueSet/condition-code', 'required'))");
        assertEquals(constraints.get(3).expression(), "bodySite.count() >= 1 and bodySite.all(memberOf('http://hl7.org/fhir/ValueSet/body-site', 'required'))");
    }
}
