/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.profile.test;

import static com.ibm.fhir.profile.ProfileBuilder.binding;
import static com.ibm.fhir.profile.ProfileBuilder.constraint;
import static com.ibm.fhir.profile.ProfileBuilder.discriminator;
import static com.ibm.fhir.profile.ProfileBuilder.profile;
import static com.ibm.fhir.profile.ProfileBuilder.slicing;
import static com.ibm.fhir.profile.ProfileBuilder.targetProfile;
import static com.ibm.fhir.profile.ProfileBuilder.type;
import static org.testng.Assert.assertEquals;

import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.resource.MessageHeader;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Organization;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.resource.StructureDefinition;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.ConstraintSeverity;
import com.ibm.fhir.model.type.code.DiscriminatorType;
import com.ibm.fhir.model.type.code.SlicingRules;
import com.ibm.fhir.profile.ConstraintGenerator;
import com.ibm.fhir.profile.ProfileBuilder;
import com.ibm.fhir.profile.ProfileSupport;
import com.ibm.fhir.registry.FHIRRegistry;

public class ConstraintGeneratorTest {
    // maintain a strong reference to the logger configured for these unit tests
    private static Logger logger = Logger.getLogger(ConstraintGenerator.class.getName());

    @BeforeClass
    public void beforeClass() {
        configureLogging();
    }

    @BeforeClass
    public void before() {
        FHIRRegistry.getInstance();
        FHIRRegistry.init();
    }

    @Test
    public static void testConstraintGenerator1() throws Exception {
        StructureDefinition profile = new ProfileBuilder(Organization.class, "http://ibm.com/fhir/StructureDefinition/TestOrganization", "1.0.0")
            .slicing("Organization.contained", slicing(discriminator(DiscriminatorType.PROFILE, "$this"), SlicingRules.OPEN))
            .slice("Organization.contained", "ProfileA", Resource.class, 1, "1")
            .type("Organization.contained:ProfileA", type("Resource", profile("http://ibm.com/fhir/StructureDefinition/ProfileA")))
            .build();
        ConstraintGenerator generator = new ConstraintGenerator(profile);
        List<Constraint> constraints = generator.generate();
        assertEquals(constraints.size(), 1);
        assertEquals(constraints.get(0).expression(), "contained.where(conformsTo('http://ibm.com/fhir/StructureDefinition/ProfileA')).count() = 1");
    }

    @Test
    public static void testConstraintGenerator2() throws Exception {
        StructureDefinition profile = new ProfileBuilder(Organization.class, "http://ibm.com/fhir/StructureDefinition/TestOrganization", "1.0.0")
            .slicing("Organization.contained", slicing(discriminator(DiscriminatorType.PROFILE, "$this"), SlicingRules.OPEN))
            .slice("Organization.contained", "ProfileA", Resource.class, 0, "1")
            .type("Organization.contained:ProfileA", type("Resource", profile("http://ibm.com/fhir/StructureDefinition/ProfileA")))
            .build();
        ConstraintGenerator generator = new ConstraintGenerator(profile);
        List<Constraint> constraints = generator.generate();
        assertEquals(constraints.size(), 1);
        assertEquals(constraints.get(0).expression(), "contained.where(conformsTo('http://ibm.com/fhir/StructureDefinition/ProfileA')).exists() implies (contained.where(conformsTo('http://ibm.com/fhir/StructureDefinition/ProfileA')).count() = 1)");
    }

    @Test
    public static void testConstraintGenerator3() throws Exception {
        StructureDefinition profile = new ProfileBuilder(Organization.class, "http://ibm.com/fhir/StructureDefinition/TestOrganization", "1.0.0")
            .slicing("Organization.identifier", slicing(discriminator(DiscriminatorType.VALUE, "system"), SlicingRules.OPEN))
            .slice("Organization.identifier", "SliceA", Identifier.class, 1, "1")
            .cardinality("Organization.identifier:SliceA.system", 1, "1")
            .pattern("Organization.identifier:SliceA.system", Uri.of("http://ibm.com/fhir/system/system-1"))
            .build();
        ConstraintGenerator generator = new ConstraintGenerator(profile);
        List<Constraint> constraints = generator.generate();
        assertEquals(constraints.size(), 1);
        assertEquals(constraints.get(0).expression(), "identifier.where(system = 'http://ibm.com/fhir/system/system-1').count() = 1");
    }

    @Test
    public static void testConstraintGenerator4() throws Exception {
        StructureDefinition profile = new ProfileBuilder(Organization.class, "http://ibm.com/fhir/StructureDefinition/TestOrganization", "1.0.0")
            .slicing("Organization.identifier", slicing(discriminator(DiscriminatorType.VALUE, "system"), SlicingRules.OPEN))
            .slice("Organization.identifier", "SliceA", Identifier.class, 0, "1")
            .cardinality("Organization.identifier:SliceA.system", 1, "1")
            .pattern("Organization.identifier:SliceA.system", Uri.of("http://ibm.com/fhir/system/system-1"))
            .build();
        ConstraintGenerator generator = new ConstraintGenerator(profile);
        List<Constraint> constraints = generator.generate();
        assertEquals(constraints.size(), 1);
        assertEquals(constraints.get(0).expression(), "identifier.where(system = 'http://ibm.com/fhir/system/system-1').exists() implies (identifier.where(system = 'http://ibm.com/fhir/system/system-1').count() = 1)");
    }

    @Test
    public static void testConstraintGenerator5() throws Exception {
        StructureDefinition profile = new ProfileBuilder(Organization.class, "http://ibm.com/fhir/StructureDefinition/TestOrganization", "1.0.0")
            .slicing("Organization.identifier", slicing(discriminator(DiscriminatorType.VALUE, "system"), SlicingRules.OPEN))
            .slice("Organization.identifier", "SliceA", Identifier.class, 0, "1")
            .cardinality("Organization.identifier:SliceA.system", 1, "1")
            .pattern("Organization.identifier:SliceA.system", Uri.of("http://ibm.com/fhir/system/system-1"))
            .constraint("Organization.identifier:SliceA", constraint("test-1", ConstraintSeverity.ERROR, "The organization SliceB identifier value length SHALL be greater than 9 characters", "value.length() > 9"))
            .build();
        ConstraintGenerator generator = new ConstraintGenerator(profile);
        List<Constraint> constraints = generator.generate();
        assertEquals(constraints.size(), 1);
        assertEquals(constraints.get(0).expression(), "identifier.where(system = 'http://ibm.com/fhir/system/system-1').exists() implies (identifier.where(system = 'http://ibm.com/fhir/system/system-1').count() = 1 and identifier.where(system = 'http://ibm.com/fhir/system/system-1').all(system = 'http://ibm.com/fhir/system/system-1' and (value.length() > 9)))");
    }

    @Test
    public static void testConstraintGenerator6() throws Exception {
        StructureDefinition profile = new ProfileBuilder(Observation.class, "http://ibm.com/fhir/StructureDefinition/TestObservation", "1.0.0")
            .slicing("Observation.value[x]", slicing(discriminator(DiscriminatorType.TYPE, "$this"), SlicingRules.OPEN))
            .slice("Observation.value[x]", "SliceA", Quantity.class, 1, "1")
            .type("Observation.value[x]:SliceA", type("Quantity"))
            .build();
        ConstraintGenerator generator = new ConstraintGenerator(profile);
        List<Constraint> constraints = generator.generate();
        assertEquals(constraints.size(), 1);
        assertEquals(constraints.get(0).expression(), "value.where(is(Quantity)).count() = 1");
    }

    @Test
    public static void testConstraintGenerator7() throws Exception {
        StructureDefinition profile = new ProfileBuilder(Observation.class, "http://ibm.com/fhir/StructureDefinition/TestObservation", "1.0.0")
            .type("Observation.value[x]", type("Quantity"))
            .cardinality("Observation.value[x]", 1, "1")
            .build();
        ConstraintGenerator generator = new ConstraintGenerator(profile);
        List<Constraint> constraints = generator.generate();
        assertEquals(constraints.size(), 1);
        assertEquals(constraints.get(0).expression(), "value.as(Quantity).exists()");
    }

    @Test
    public static void testConstraintGenerator8() throws Exception {
        StructureDefinition profile = new ProfileBuilder(Patient.class, "http://ibm.com/fhir/StructureDefinition/TestPatient", "1.0.0")
            .type("Patient.generalPractitioner", type("Reference", profile(), targetProfile(ProfileSupport.HL7_STRUCTURE_DEFINITION_URL_PREFIX + "Organization")))
            .build();
        ConstraintGenerator generator = new ConstraintGenerator(profile);
        List<Constraint> constraints = generator.generate();
        assertEquals(constraints.size(), 1);
        assertEquals(constraints.get(0).expression(), "generalPractitioner.exists() implies (generalPractitioner.count() >= 1 and generalPractitioner.all(resolve().is(Organization)))");
    }

    @Test
    public static void testConstraintGenerator9() throws Exception {
        StructureDefinition profile = new ProfileBuilder(Patient.class, "http://ibm.com/fhir/StructureDefinition/TestPatient", "1.0.0")
            .type("Patient.generalPractitioner", type("Reference", profile(), targetProfile(ProfileSupport.HL7_STRUCTURE_DEFINITION_URL_PREFIX + "Organization")))
            .cardinality("Patient.generalPractitioner", 1, "1")
            .build();
        ConstraintGenerator generator = new ConstraintGenerator(profile);
        List<Constraint> constraints = generator.generate();
        assertEquals(constraints.size(), 1);
        assertEquals(constraints.get(0).expression(), "generalPractitioner.count() = 1 and generalPractitioner.all(resolve().is(Organization))");
    }

    @Test
    public static void testConstraintGenerator10() throws Exception {
        StructureDefinition profile = new ProfileBuilder(Patient.class, "http://ibm.com/fhir/StructureDefinition/TestPatient", "1.0.0")
            .cardinality("Patient.name", 1, "1")
            .build();
        ConstraintGenerator generator = new ConstraintGenerator(profile);
        List<Constraint> constraints = generator.generate();
        assertEquals(constraints.size(), 1);
        assertEquals(constraints.get(0).expression(), "name.count() = 1");
    }

    @Test
    public static void testConstraintGenerator11() throws Exception {
        StructureDefinition profile = new ProfileBuilder(Observation.class, "http://ibm.com/fhir/StructureDefinition/TestObservation", "1.0.0")
            .binding("Observation.code", binding(BindingStrength.REQUIRED, "http://ibm.com/fhir/ValueSet/vs-1"))
            .build();
        ConstraintGenerator generator = new ConstraintGenerator(profile);
        List<Constraint> constraints = generator.generate();
        assertEquals(constraints.size(), 1);
        assertEquals(constraints.get(0).expression(), "code.exists() and code.all(memberOf('http://ibm.com/fhir/ValueSet/vs-1', 'required'))");
    }

    @Test
    public static void testConstraintGenerator12() throws Exception {
        StructureDefinition profile = new ProfileBuilder(Observation.class, "http://ibm.com/fhir/StructureDefinition/TestObservation", "1.0.0")
            .binding("Observation.code", binding(BindingStrength.REQUIRED, "http://ibm.com/fhir/ValueSet/vs-1", "http://ibm.com/fhir/ValueSet/max-vs-1"))
            .build();
        ConstraintGenerator generator = new ConstraintGenerator(profile);
        List<Constraint> constraints = generator.generate();
        assertEquals(constraints.size(), 1);
        assertEquals(constraints.get(0).expression(), "code.exists() and code.all(memberOf('http://ibm.com/fhir/ValueSet/vs-1', 'required') and memberOf('http://ibm.com/fhir/ValueSet/max-vs-1', 'required'))");
    }

    @Test
    public static void testConstraintGenerator13() throws Exception {
        StructureDefinition profile = new ProfileBuilder(MessageHeader.class, "http://ibm.com/fhir/StructureDefinition/TestMessageHeader", "1.0.0")
            .slicing("MessageHeader.focus", slicing(discriminator(DiscriminatorType.PROFILE, "$this.resolve()"), SlicingRules.OPEN))
            .slice("MessageHeader.focus", "SliceA", Reference.class, 0, "1")
            .type("MessageHeader.focus:SliceA", type("Reference", profile(), targetProfile("http://ibm.com/fhir/StructureDefinition/ProfileA")))
            .build();
        System.out.println(profile);
        ConstraintGenerator generator = new ConstraintGenerator(profile);
        List<Constraint> constraints = generator.generate();
        assertEquals(constraints.size(), 1);
        assertEquals(constraints.get(0).expression(), "focus.where(resolve().conformsTo('http://ibm.com/fhir/StructureDefinition/ProfileA')).exists() implies (focus.where(resolve().conformsTo('http://ibm.com/fhir/StructureDefinition/ProfileA')).count() = 1)");
    }

    private void configureLogging() {
        logger.setLevel(Level.FINEST);
        Handler h = new Handler() {
            @Override
            public void publish(LogRecord record) {
                System.out.println(record.getMessage());
            }

            @Override
            public void flush() { }

            @Override
            public void close() throws SecurityException { }
        };
        h.setLevel(Level.FINEST);
        logger.addHandler(h);
    }
}
