/*
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test;

import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.model.type.Uri.uri;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;

import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.resource.AllergyIntolerance;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.Composition;
import com.ibm.fhir.model.resource.Condition;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Practitioner;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Base64Binary;
import com.ibm.fhir.model.type.BundleType;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.CompositionStatus;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;

// TODO create tests for invoking toCda transform on saved resources (building on $document operation)
public class FHIRToCdaOperationTest extends FHIRServerTestBase {
    private Bundle savedCreatedBundle = null;

    public FHIRToCdaOperationTest() {
        DEBUG_JSON = false;
    }

    @Test
    public void testCreateDocument() throws Exception {
        WebTarget target = getWebTarget();

        Bundle document = buildDocument();

        FHIRGenerator.generator(Format.JSON, false).generate(document, System.out);

        Entity<Bundle> entity = Entity.entity(document, FHIRMediaType.APPLICATION_FHIR_JSON);
        Response response = target.path("Bundle").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        // Get the bundle's assigned logical id.
        String bundleId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new composition and verify it.
        response = target.path("Bundle/" + bundleId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        savedCreatedBundle = responseBundle;

        assertResourceEquals(document, responseBundle);
    }

    @Test(dependsOnMethods = { "testCreateDocument" })
    public void testStoredBundleToCdaOperation() throws Exception {
        WebTarget target = getWebTarget();

        Response response = target.path("Bundle/" + savedCreatedBundle.getId().getValue() + "/$tocda")
                .request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
        assertResponse(response, Response.Status.OK.getStatusCode());

        Base64Binary valueBase64Binary = extractResponseDocument(response);
        System.out.println(new String(valueBase64Binary.getValue(), "UTF-8"));
    }

    @Test
    public void testToCdaOperation() throws Exception {
        Bundle document = buildDocument();

        Parameters parameters = createParametersWithDocument(document);

        Response response = sendRequest(parameters);
        assertResponse(response, Response.Status.OK.getStatusCode());

        Base64Binary valueBase64Binary = extractResponseDocument(response);
        System.out.println(new String(valueBase64Binary.getValue(), "UTF-8"));
    }

    private Base64Binary extractResponseDocument(Response response) {
        Parameters responseParameters = response.readEntity(Parameters.class);
        assertNotNull(responseParameters);
        assertTrue(responseParameters.getParameter().size() == 1);
        Parameters.Parameter responseParameter = responseParameters.getParameter().get(0);
        assertNotNull(responseParameter.getValue());
        Base64Binary valueBase64Binary = (Base64Binary) responseParameter.getValue();
        return valueBase64Binary;
    }

    @Test
    public void testInvalidDocumentWithValidation() throws Exception {
        DateTime datetime = DateTime.of("Invalid Datetime");
        com.ibm.fhir.model.type.String title = string("Invalid Document");
        Bundle documentBundle = createCCD(datetime, title, CompositionStatus.FINAL);

        Parameters parameters = createParameters(documentBundle, true);

        Response response = sendRequest(parameters);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());

        OperationOutcome outcome = response.readEntity(OperationOutcome.class);
        assertNotNull(outcome);
        List<Issue> issues = outcome.getIssue();
        assertTrue(issues.size() > 0);
        FHIRGenerator.generator(Format.JSON, false).generate(outcome, System.out);
    }

    /*
     * Without validation, the server does its best to return valid C-CDA, but not
     * all scenarios have been covered
     */
    @Test
    public void testInvalidDocumentWithoutValidation() throws Exception {
        DateTime datetime = DateTime.of("Invalid Datetime");
        com.ibm.fhir.model.type.String title = string("Invalid Document");
        Bundle documentBundle = createCCD(datetime, title, CompositionStatus.FINAL);

        Parameters parameters = createParameters(documentBundle, false);
        FHIRGenerator.generator(Format.JSON, false).generate(parameters, System.out);

        Response response = sendRequest(parameters);
        assertResponse(response, Response.Status.OK.getStatusCode());

        Base64Binary valueBase64Binary = extractResponseDocument(response);
        System.out.println(new String(valueBase64Binary.getValue(), "UTF-8"));
    }

    @Test
    public void testNonDocumentBundle() throws Exception {
        // use an empty bundle to ensure its invalid
        Bundle documentBundle = Bundle.builder().build();

        Parameters parameters = createParametersWithDocument(documentBundle);

        Response response = sendRequest(parameters);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());

        OperationOutcome outcome = response.readEntity(OperationOutcome.class);
        assertNotNull(outcome);
        List<Issue> issues = outcome.getIssue();
        assertTrue(issues.size() > 0);
        FHIRGenerator.generator(Format.JSON, false).generate(outcome, System.out);
    }

    /**
     * Builds a FHIR document from canned resources
     * 
     * @return
     * @throws Exception
     */
    private Bundle buildDocument() throws Exception {
        DateTime datetime = DateTime.of("2015-02-14T13:42:00+10:00");
        com.ibm.fhir.model.type.String title = string("Sample Document");
        Bundle document = createCCD(datetime, title, CompositionStatus.FINAL);

        // Create a practitioner and add it to the document.
        Practitioner practitioner = readResource(Practitioner.class, "Practitioner.json");
        Uri practitionerUri = createUUID();
        document = addAuthor(document, practitioner, practitionerUri);

        // Create and add a patient with that practitioner as a care provider.
        Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");
        patient = patient.toBuilder()
                .generalPractitioner(Reference.builder().reference(string(practitionerUri.getValue())).build()).build();
        Uri patientUri = createUUID();
        document = addSubject(document, patient, patientUri);

        // Next, an Observation belonging to the new patient.
        Observation observation = buildObservation(patientUri, "Observation1.json");

        Composition.Section vitalsSection = createSection("Vital Signs",
                CodeableConcept.builder().coding(
                        Coding.builder().system(uri(LOINC.URI)).code(Code.of(LOINC.CCDA_SECTION_VITALSIGNS)).build())
                        .build());

        Uri uri = createUUID();
        document = addSection(document, vitalsSection, uri);
        document = addResourceToDocument(document, observation, uri);

        // Next, a Condition belonging to the new patient.
        Condition condition = buildCondition(patientUri, "Condition.json");
        Composition.Section problemsSection = createSection("Problem List",
                CodeableConcept.builder().coding(
                        Coding.builder().system(uri(LOINC.URI)).code(Code.of(LOINC.CCDA_SECTION_PROBLEMLIST)).build())
                        .build());

        uri = createUUID();
        document = addSection(document, problemsSection, uri);
        document = addResourceToDocument(document, condition, uri);

        // Next, an AllergyIntolerance belonging to the new patient.
        AllergyIntolerance allergyIntolerance = buildAllergyIntolerance(patientUri, "AllergyIntolerance.json");
        Composition.Section allergiesSection = createSection("Allergies",
                CodeableConcept.builder().coding(
                        Coding.builder().system(uri(LOINC.URI)).code(Code.of(LOINC.CCDA_SECTION_ALLERGIES)).build())
                        .build());

        uri = createUUID();
        document = addSection(document, allergiesSection, uri);
        document = addResourceToDocument(document, allergyIntolerance, uri);

        return document;
    }

    private Bundle addSubject(Bundle document, Patient patient, Uri uri) throws Exception {
        document = addResourceToBundle(document, patient, uri);

        List<Entry> entryList = new ArrayList<Entry>();
        for (int i = 0; i < document.getEntry().size(); i++) {
            Entry entry = document.getEntry().get(i);
            if (i == 0) {
                Composition composition = (Composition) document.getEntry().get(i).getResource();
                composition = composition.toBuilder()
                        .subject(Reference.builder().reference(string(uri.getValue())).build()).build();

                entry = entry.toBuilder().resource(composition).build();
            }
            entryList.add(entry);
        }

        return document.toBuilder().entry(entryList).build();
    }

    private Bundle addAuthor(Bundle document, Practitioner practitioner, Uri uri) throws Exception {
        document = addResourceToBundle(document, practitioner, uri);

        List<Entry> entryList = new ArrayList<Entry>();
        for (int i = 0; i < document.getEntry().size(); i++) {
            Entry entry = document.getEntry().get(i);
            if (i == 0) {
                Composition composition = (Composition) document.getEntry().get(i).getResource();
                List<Reference> authorList = new ArrayList<Reference>();
                authorList.addAll(composition.getAuthor());
                authorList.add(Reference.builder().reference(string(uri.getValue())).build());

                composition = composition.toBuilder()
                        .status(composition.getStatus())
                        .type(composition.getType())
                        .date(composition.getDate())
                        .author(authorList)
                        .title(composition.getTitle())
                        .build()
                        ;

                entry = entry.toBuilder().resource(composition).build();
            }
            entryList.add(entry);
        }

        return document.toBuilder().entry(entryList).build();

    }

    /**
     * Creates and returns new FHIR bundle of type document with an empty
     * Composition resource entry
     * 
     * @return
     * @throws Exception
     */
    private Bundle createCCD(DateTime date, com.ibm.fhir.model.type.String title, CompositionStatus status)
            throws Exception {
        Bundle document = Bundle.builder().type(BundleType.DOCUMENT).build();

        List<Reference> authorList = new ArrayList<Reference>();
        Composition composition = Composition.builder()
                .status(status)
                .type(CodeableConcept.builder()
                        .coding(Coding.builder().system(Uri.of(LOINC.URI))
                                .code(Code.of(LOINC.CCDA_CCD)).build()).build())
                .author(authorList)
                .date(date)
                .title(title)
                .build();
        Uri uri = createUUID();
        document = addResourceToBundle(document, composition, uri);
        return document;
    }

    /**
     * Creates a section with @sectionTitle and @sectionCode
     * 
     * @return
     * @throws Exception
     */
    private Composition.Section createSection(String sectionTitle, CodeableConcept sectionCode) throws Exception {
        Composition.Section section = Composition.Section.builder().title(string(sectionTitle)).code(sectionCode)
                .build();
        return section;
    }

    /**
     * Adds @section to the composition resource of the document
     * 
     * @return
     * @throws Exception
     */
    private Bundle addSection(Bundle document, Composition.Section section, Uri uri) {
        section = section.toBuilder().entry(Reference.builder().reference(string(uri.getValue())).build()).build();

        List<Entry> entryList = new ArrayList<Entry>();
        for (int i = 0; i < document.getEntry().size(); i++) {
            Entry entry = document.getEntry().get(i);
            if (i == 0) {
                Composition composition = (Composition) document.getEntry().get(i).getResource();
                composition = composition.toBuilder().section(section).build();

                entry = entry.toBuilder().resource(composition).build();
            }
            entryList.add(entry);
        }

        return document.toBuilder().entry(entryList).build();
    }

    protected Observation buildObservation(Uri patientUri, String fileName) throws Exception {
        Observation observation = readResource(Observation.class, fileName);
        observation = observation.toBuilder()
                .subject(Reference.builder().reference(string(patientUri.getValue())).build()).build();
        return observation;
    }

    private Condition buildCondition(Uri patientUri, String fileName) throws Exception {
        Condition condition = readResource(Condition.class, fileName);
        condition = condition.toBuilder().subject(Reference.builder().reference(string(patientUri.getValue())).build()).build();
        return condition;
    }

    private AllergyIntolerance buildAllergyIntolerance(Uri patientUri, String fileName) throws Exception {
        AllergyIntolerance allergyIntolerance = readResource(AllergyIntolerance.class, fileName);
        allergyIntolerance.toBuilder().patient(Reference.builder().reference(string(patientUri.getValue())).build()).build();
        return allergyIntolerance;
    }

    /**
     * Adds the resource to the bundle and adds a reference to it in
     * section @section
     * 
     * @param document a FHIR bundle whose first entry is a Composition
     * @param section  the section to add the reference to
     * @param resource the resource to add
     * @return the fullUrl of the newly added bundle entry for @resource
     * @throws Exception
     */
    private Bundle addResourceToDocument(Bundle document, Resource resource, Uri uri) throws Exception {
        document = addResourceToBundle(document, resource, uri);
        return document;
    }

    /**
     * Create a new bundleEntry within @bundle and return the fullUrl
     * 
     * @param bundle
     * @param resource
     * @return the fullUrl of the newly added bundle entry for @resource
     * @throws Exception
     */
    private Bundle addResourceToBundle(Bundle bundle, Resource resource, Uri uri) throws Exception {
        Bundle.Entry entry = Entry.builder().fullUrl(uri).resource(resource).build();

        return bundle.toBuilder().entry(entry).build();
    }

    private Uri createUUID() {
        return Uri.of("urn:uuid:" + UUID.randomUUID());
    }

    /**
     * Wrap the FHIR document in a Parameters object and invoke the 'to CDA'
     * operation with it
     * 
     * @param documentBundle
     * @return
     * @throws Exception
     */
    private Parameters createParametersWithDocument(Bundle documentBundle) throws Exception {
        return createParameters(documentBundle, (Boolean) null);
    }

    /**
     * Wrap the FHIR document in a Parameters object and invoke the 'to CDA'
     * operation with it
     * 
     * @param documentBundle
     * @param validate       if non-null, include the validateFHIR parameter with
     *                       the request
     * @return
     * @throws Exception
     */
    private Parameters createParameters(Bundle documentBundle, Boolean validate) throws Exception {
        Parameters.Builder parametersBuilder = Parameters.builder()
                .parameter(Parameter.builder().name(string("document")).resource(documentBundle).build());

        if (validate != null) {
            parametersBuilder.parameter(Parameter.builder().name(string("validateFHIR"))
                    .value(com.ibm.fhir.model.type.Boolean.of(validate)).build());
        }

        return parametersBuilder.build();
    }

    private Response sendRequest(Parameters parameters) throws JAXBException, FHIRException {
        FHIRGenerator.generator(Format.JSON, false).generate(parameters, System.out);
        System.out.println();
        Entity<Parameters> entity = Entity.entity(parameters, FHIRMediaType.APPLICATION_FHIR_JSON);

        WebTarget target = getWebTarget();
        return target.path("Bundle/$tocda").request().post(entity, Response.class);
    }
}
