/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.util.group;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.Assert.fail;

import java.io.IOException;
import java.io.StringWriter;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Group;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Observation.Component;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.ContactPoint;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.ContactPointSystem;
import com.ibm.fhir.model.type.code.ContactPointUse;
import com.ibm.fhir.model.type.code.ObservationStatus;
import com.ibm.fhir.operation.bulkdata.util.group.examples.AgeRangeExample;
import com.ibm.fhir.operation.bulkdata.util.group.examples.AnnualObGynExample;
import com.ibm.fhir.operation.bulkdata.util.group.examples.AnnualWellnessExample;
import com.ibm.fhir.operation.bulkdata.util.group.examples.GroupExample;
import com.ibm.fhir.path.FHIRPathNode;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator;
import com.ibm.fhir.path.evaluator.FHIRPathEvaluator.EvaluationContext;
import com.ibm.fhir.path.exception.FHIRPathException;

/**
 * This class automates the generation of Dynamic Groups (Cohorts) for testing the Epic:
 * <a href="https://github.com/IBM/FHIR/issues/566">Bulk export for dynamic Groups based on characteristics #566</a>
 * <br>
 */
public class DynamicGroupGeneratorMain {

    private Bundle.Builder bundleBuilder = Bundle.builder();

    private List<Group> groups = new ArrayList<>();

    private List<GroupExample> groupExamples = Arrays.asList(new AnnualWellnessExample(), new AnnualObGynExample());

    public DynamicGroupGeneratorMain() {
        // No Op
    }

    // Profile: http://hl7.org/fhir/actualgroup.html
    // Enforces an actual group, rather than a definitional group
    public void generateActualGroup() {

    }

    // Profile: http://hl7.org/fhir/groupdefinition-definitions.html
    // Enforces a descriptive group that can be used in definitional resources
    public void generateDescriptiveGroup() throws FHIRPathException {
        AgeRangeExample range = new AgeRangeExample();
        generateOutput(range.groups().get(0));

        Group group = range.groups().get(0);
        FHIRPathEvaluator evaluator = FHIRPathEvaluator.evaluator();
        EvaluationContext evaluationContext = new EvaluationContext(group);
        String EXPR = "characteristic.value.extension";
        Collection<FHIRPathNode> tmpResults = evaluator.evaluate(evaluationContext, EXPR);
        String code = "";
        String value = "";
        List<String> searchParams = new ArrayList<>();
        for (FHIRPathNode node : tmpResults) {
            Extension searchElement = node.asElementNode().element().as(Extension.class);
            if ("http://www.ibm.com/search/code".equals(searchElement.getUrl())) {
                code = searchElement.getValue().as(com.ibm.fhir.model.type.String.class).getValue();
            }
            if ("http://www.ibm.com/search/value".equals(searchElement.getUrl())) {
                value = searchElement.getValue().as(com.ibm.fhir.model.type.String.class).getValue();
                if(code.equals("age")) {
                    // Needs to process prefix.
                    String lower = value.split(",")[0];
                    String upper = value.split(",")[1];


                    ZonedDateTime zdtLower = ZonedDateTime.now().minusYears(Integer.parseInt(lower));
                    ZonedDateTime zdtUpper = ZonedDateTime.now().minusYears(Integer.parseInt(upper));
                    searchParams.add("birthdate=ge" + zdtUpper.getYear());
                    searchParams.add("birthdate=lt" + zdtLower.getYear());
                } else {
                    searchParams.add(code + "=" + value);
                }
            }
        }
        System.out.println("Search Query -> " + searchParams.stream().collect(Collectors.joining("&")));
    }


    public static Patient buildPatient() {
        Patient patient = Patient.builder().name(HumanName.builder()
                        .family(string("Doe"))
                        .given(string("John")).build())
                .birthDate(com.ibm.fhir.model.type.Date.of("1950-01-01"))
                .telecom(ContactPoint.builder().system(ContactPointSystem.PHONE)
                        .use(ContactPointUse.HOME).value(string("555-1234")).build())
                .build();
        return patient;
    }

    public static Observation buildObservation(String patientId) {
        CodeableConcept code = CodeableConcept.builder().coding(Coding.builder().code(Code.of("55284-4"))
            .system(Uri.of("http://loinc.org")).build())
            .text(string("Blood pressure systolic & diastolic"))
            .build();

        Observation observation = Observation.builder().status(ObservationStatus.FINAL).bodySite(
                CodeableConcept.builder().coding(Coding.builder().code(Code.of("55284-4"))
                        .system(Uri.of("http://loinc.org")).build())
                        .text(string("Blood pressure systolic & diastolic")).build())
                .category(CodeableConcept.builder().coding(Coding.builder().code(Code.of("signs"))
                        .system(Uri.of("http://hl7.org/fhir/observation-category")).build())
                        .text(string("Vital Signs")).build())
                .code(code)
                .subject(Reference.builder().reference(string("Patient/" + patientId)).build())
                .component(Component.builder().code(CodeableConcept.builder().coding(Coding.builder().code(Code.of("8459-0"))
                        .system(Uri.of("http://loinc.org")).build())
                        .text(string("Systolic")).build())
                        .value(Quantity.builder().value(Decimal.of(124.9)).unit(string("mmHg")).build()).build())
                .component(Component.builder().code(CodeableConcept.builder().coding(Coding.builder().code(Code.of("8453-3"))
                        .system(Uri.of("http://loinc.org")).build())
                        .text(string("Diastolic")).build())
                        .value(Quantity.builder().value(Decimal.of(93.7)).unit(string("mmHg")).build()).build())
             .build();
        return observation;
    }

    /*
     * generates the output into a resource.
     */
    public static void generateOutput(Resource resource) {
        try (StringWriter writer = new StringWriter();) {
            FHIRGenerator.generator(Format.JSON, true).generate(resource, System.out);
            System.out.println(writer.toString());
        } catch (FHIRGeneratorException e) {

            e.printStackTrace();
            fail("unable to generate the fhir resource to JSON");

        } catch (IOException e1) {
            e1.printStackTrace();
            fail("unable to generate the fhir resource to JSON (io problem) ");
        }

    }

    /**
     * @param args
     * @throws FHIRPathException
     */
    public static void main(String[] args) throws FHIRPathException {
        DynamicGroupGeneratorMain main = new DynamicGroupGeneratorMain();
        main.generateActualGroup();
        main.generateDescriptiveGroup();
        generateOutput(buildPatient());
        generateOutput(buildObservation("1-2-3-4"));
    }
}
