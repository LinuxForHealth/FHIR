/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.server.test.bulkdata;

import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.model.type.Xhtml.xhtml;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.fhir.model.resource.Group;
import com.ibm.fhir.model.resource.Group.Characteristic;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Observation.Component;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.ContactPoint;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.AdministrativeGender;
import com.ibm.fhir.model.type.code.ContactPointSystem;
import com.ibm.fhir.model.type.code.ContactPointUse;
import com.ibm.fhir.model.type.code.GroupType;
import com.ibm.fhir.model.type.code.NarrativeStatus;
import com.ibm.fhir.model.type.code.ObservationStatus;
import com.ibm.fhir.server.test.FHIRServerTestBase;

/**
 * These tests exercise the $export operation, a BulkData specification defined operation
 */
public class CohortTest extends FHIRServerTestBase {
    public static final String TEST_GROUP_NAME = "export-operation";
    public static final String PATIENT_VALID_URL = "Patient/$export";
    public static final String GROUP_VALID_URL = "Group/?/$export";
    public static final String BASE_VALID_URL = "/$export";
    public static final String BASE_VALID_STATUS_URL = "/$bulkdata-status";
    public static final String FORMAT = "application/fhir+ndjson";

    // Disabled by default
    private static boolean ON = false;

    public static final boolean DEBUG = false;
    private String savedPatientId;

    @BeforeClass
    public void setup() throws Exception {
        Properties testProperties = TestUtil.readTestProperties("test.properties");
        ON = Boolean.parseBoolean(testProperties.getProperty("test.bulkdata.export.enabled", "true"));
    }

    public Response doPost(String path, String mimeType, String outputFormat, Instant since, List<String> types, List<String> typeFilters)
        throws FHIRGeneratorException, IOException {
        WebTarget target = getWebTarget();
        target = target.path(path);
        target = addQueryParameter(target, "_outputFormat", outputFormat);
        // target = addQueryParameter(target, "_since", since);
        target = addQueryParameterList(target, "_type", types);
        target = addQueryParameterList(target, "_typeFilter", typeFilters);
        if (DEBUG) {
            System.out.println("URL -> " + target.getUri());
        }
        Parameters parameters = generateParameters(outputFormat, since, types, null);
        Entity<Parameters> entity = Entity.entity(parameters, FHIRMediaType.APPLICATION_FHIR_JSON);
        return target.request(mimeType).post(entity, Response.class);

    }

    /*
     * @param outputFormat
     * @param since
     * @param types
     * @param typeFilters
     * @return
     * @throws FHIRGeneratorException
     * @throws IOException
     */
    private Parameters generateParameters(String outputFormat, Instant since, List<String> types, List<String> typeFilters)
        throws FHIRGeneratorException, IOException {
        List<Parameter> parameters = new ArrayList<>();

        if (outputFormat != null) {
            parameters.add(Parameter.builder().name(string("_outputFormat")).value(string(outputFormat)).build());
        }

        if (since != null) {
            parameters.add(Parameter.builder().name(string("_since")).value(since).build());
        }

        if (types != null) {
            parameters.add(Parameter.builder().name(string("_type")).value(string(types.stream().collect(Collectors.joining(",")))).build());
        }

        if (typeFilters != null) {
            parameters.add(Parameter.builder().name(string("_typeFilters")).value(string(types.stream().collect(Collectors.joining(",")))).build());
        }

        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        try (StringWriter writer = new StringWriter();) {
            FHIRGenerator.generator(Format.JSON, true).generate(ps, writer);
            if (DEBUG) {
                System.out.println(writer.toString());
            }
        }
        return ps;
    }

    /**
     * add query parameter list
     */
    public WebTarget addQueryParameterList(WebTarget target, String header, List<String> vals) {
        if (header != null && vals != null && !vals.isEmpty()) {
            target = target.queryParam(header, vals.stream().collect(Collectors.joining(",")));
        }
        return target;
    }

    /**
     * adds the query parameter
     */
    public WebTarget addQueryParameter(WebTarget target, String header, String val) {
        if (header != null && val != null) {
            target = target.queryParam(header, val);
        }
        return target;
    }

    public Response doGet(String path, String mimeType) {
        WebTarget target = getWebTarget();
        target = target.path(path);
        return target.request(mimeType).get(Response.class);
    }

    public static Patient buildPatient() {
        Patient patient = Patient.builder().name(HumanName.builder()
                        .family(string("Doe"))
                        .given(string("John")).build())
                .gender(AdministrativeGender.MALE)
                .birthDate(com.ibm.fhir.model.type.Date.of("1985-01-01"))
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

    @Test
    public void testCreateResources() throws Exception {
        WebTarget target = getWebTarget();
        Patient patient = buildPatient();
        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Patient").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        URI location = response.getLocation();
        assertNotNull(location);
        assertNotNull(location.toString());
        assertFalse(location.toString().isEmpty());

        // Get the patient's logical id value.
        savedPatientId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        response = target.path("Patient/" + savedPatientId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());

        // Next, create an Observation for patient1.
        Observation observation = buildObservation(savedPatientId);
        Entity<Observation> obs = Entity.entity(observation, FHIRMediaType.APPLICATION_FHIR_JSON);
        response = target.path("Observation").request().post(obs, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        String observationId = getLocationLogicalId(response);
        response = target.path("Observation/" + observationId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    public Group buildGroup() {
        java.lang.String div = "<div xmlns=\"http://www.w3.org/1999/xhtml\"><p><b>Generated Narrative</b></p></div>";

        java.lang.String id = UUID.randomUUID().toString();

        Meta meta = Meta.builder()
                .versionId(Id.of("1"))
                .lastUpdated(Instant.now(ZoneOffset.UTC))
                .build();

        Narrative text = Narrative.builder()
                .status(NarrativeStatus.GENERATED)
                .div(xhtml(div))
                .build();

        com.ibm.fhir.model.type.Boolean active = com.ibm.fhir.model.type.Boolean.of(true);

        com.ibm.fhir.model.type.Boolean actual = com.ibm.fhir.model.type.Boolean.of(false);

        Collection<Characteristic> characteristics = new ArrayList<>();
        // Blood Pressure
        characteristics.add(generateFirstCharacteristic());
        // Age
        characteristics.add(generateSecondCharacteristic());


        Group group = Group.builder()
                .id(id)
                .meta(meta)
                .text(text)
                .active(active)
                .type(GroupType.PERSON)
                .actual(actual)
                .name(string("age-example-with-bloodpressure"))
                .characteristic(characteristics)
                .build();

        return group;
    }

    private Characteristic generateFirstCharacteristic() {
        CodeableConcept code = CodeableConcept.builder().coding(Coding.builder().code(Code.of("55284-4"))
            .system(Uri.of("http://loinc.org")).build())
            .text(string("Blood pressure systolic & diastolic"))
            .extension(Extension.builder()
                .url("http://www.ibm.com/search/code")
                .value(string("component-value-quantity"))
                .build())
            .build();

        // Value -- CodeableConcept.class, Boolean.class, Quantity.class, Range.class, Reference.class)
        Collection<Extension> extensions = new ArrayList<>();
        extensions.add(Extension.builder()
            .url("http://www.ibm.com/search/code")
            .value(string("_has:Observation:patient:combo-value-quantity"))
            .build());
        extensions.add(Extension.builder()
            .url("http://www.ibm.com/search/value")
            .value(string("gt123.0||mmHg"))
            .build());

        CodeableConcept value = CodeableConcept.builder().coding(Coding.builder().code(Code.of("55284-4"))
            .system(Uri.of("http://loinc.org")).build())
            .text(string("Blood pressure systolic & diastolic"))
            .extension(extensions)
            .build();

        com.ibm.fhir.model.type.Boolean exclude = com.ibm.fhir.model.type.Boolean.of(false);
        Characteristic characteristic = Characteristic.builder()
            .code(code)
            .value(value)
            .exclude(exclude)
            .build();
        return characteristic;
    }

    private Characteristic generateSecondCharacteristic() {
        CodeableConcept code = CodeableConcept.builder().coding(Coding.builder().code(Code.of("29553-5"))
            .system(Uri.of("http://loinc.org")).build())
            .text(string("Age calculated"))
            .extension(Extension.builder()
                .url("http://www.ibm.com/search/code")
                .value(string("age"))
                .build())
            .build();

        Collection<Extension> extensions = new ArrayList<>();
        extensions.add(Extension.builder()
            .url("http://www.ibm.com/search/code")
            .value(string("age"))
            .build());
        extensions.add(Extension.builder()
            .url("http://www.ibm.com/search/value")
            .value(string("20,35"))
            .build());

        CodeableConcept value = CodeableConcept.builder().coding(Coding.builder().code(Code.of("55284-4"))
            .system(Uri.of("http://loinc.org")).build())
            .text(string("Blood pressure systolic & diastolic"))
            .extension(extensions)
            .build();

        com.ibm.fhir.model.type.Boolean exclude = com.ibm.fhir.model.type.Boolean.of(false);
        Characteristic characteristic = Characteristic.builder()
            .code(code)
            .value(value)
            .exclude(exclude)
            .build();
        return characteristic;
    }

    @Test(dependsOnMethods = { "testCreateResources" })
    public void testGroup() throws Exception {
        WebTarget target = getWebTarget();

        // (1) Build a new Group.
        Group group = buildGroup();

    }
}