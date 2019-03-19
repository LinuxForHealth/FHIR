/**
 * (C) Copyright IBM Corp. 2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.test;

import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.*;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.UUID;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;

import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.core.MediaType;
import com.ibm.watsonhealth.fhir.model.AllergyIntolerance;
import com.ibm.watsonhealth.fhir.model.Base64Binary;
import com.ibm.watsonhealth.fhir.model.Bundle;
import com.ibm.watsonhealth.fhir.model.BundleEntry;
import com.ibm.watsonhealth.fhir.model.BundleType;
import com.ibm.watsonhealth.fhir.model.BundleTypeList;
import com.ibm.watsonhealth.fhir.model.CodeableConcept;
import com.ibm.watsonhealth.fhir.model.Composition;
import com.ibm.watsonhealth.fhir.model.CompositionSection;
import com.ibm.watsonhealth.fhir.model.CompositionStatus;
import com.ibm.watsonhealth.fhir.model.CompositionStatusList;
import com.ibm.watsonhealth.fhir.model.Condition;
import com.ibm.watsonhealth.fhir.model.DateTime;
import com.ibm.watsonhealth.fhir.model.Observation;
import com.ibm.watsonhealth.fhir.model.OperationOutcome;
import com.ibm.watsonhealth.fhir.model.OperationOutcomeIssue;
import com.ibm.watsonhealth.fhir.model.Parameters;
import com.ibm.watsonhealth.fhir.model.ParametersParameter;
import com.ibm.watsonhealth.fhir.model.Patient;
import com.ibm.watsonhealth.fhir.model.Practitioner;
import com.ibm.watsonhealth.fhir.model.Resource;
import com.ibm.watsonhealth.fhir.model.ResourceContainer;
import com.ibm.watsonhealth.fhir.model.Uri;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil.Format;

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
        
        FHIRUtil.write(document, Format.XML, System.out);
        
        Entity<Bundle> entity = Entity.entity(document, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.path("Bundle").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        
        // Get the bundle's assigned logical id.
        String bundleId = getLocationLogicalId(response);
        
        // Next, call the 'read' API to retrieve the new composition and verify it.
        response = target.path("Bundle/" + bundleId).request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        Bundle responseBundle = response.readEntity(Bundle.class);
        savedCreatedBundle = responseBundle;
        
        assertResourceEquals(document, responseBundle);
    }
    
    @Test(dependsOnMethods = { "testCreateDocument" })
    public void testStoredBundleToCdaOperation() throws Exception {
        WebTarget target = getWebTarget();
        
        Response response = target.path("Bundle/" + savedCreatedBundle.getId().getValue() + "/$tocda").request(MediaType.APPLICATION_JSON_FHIR).get();
        assertResponse(response, Response.Status.OK.getStatusCode());
        
        Base64Binary valueBase64Binary = extractResponseDocument(response);
        System.out.println(new String(valueBase64Binary.getValue(),"UTF-8"));
    }
    
    @Test
    public void testToCdaOperation() throws Exception {
        Bundle document = buildDocument();
        
        Parameters parameters = createParametersWithDocument(document);
        
        Response response = sendRequest(parameters);
        assertResponse(response, Response.Status.OK.getStatusCode());
        
        Base64Binary valueBase64Binary = extractResponseDocument(response);
        System.out.println(new String(valueBase64Binary.getValue(),"UTF-8"));
    }

    private Base64Binary extractResponseDocument(Response response) {
        Parameters responseParameters = response.readEntity(Parameters.class);
        assertNotNull(responseParameters);
        assertTrue(responseParameters.getParameter().size() == 1);
        ParametersParameter responseParameter = responseParameters.getParameter().get(0);
        assertNotNull(responseParameter.getValueBase64Binary());
        Base64Binary valueBase64Binary = responseParameter.getValueBase64Binary();
        return valueBase64Binary;
    }
    
    @Test
    public void testInvalidDocumentWithValidation() throws Exception {
        CompositionStatus status = f.createCompositionStatus().withValue(CompositionStatusList.FINAL);
        DateTime datetime = dateTime("Invalid Datetime");
        com.ibm.watsonhealth.fhir.model.String title = string("Invalid Document");
        Bundle documentBundle = createCCD(datetime, title, status);
        
        Parameters parameters = createParameters(documentBundle, true);
        
        Response response = sendRequest(parameters);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        
        OperationOutcome outcome = response.readEntity(OperationOutcome.class);
        assertNotNull(outcome);
        List<OperationOutcomeIssue> issues = outcome.getIssue();
        assertTrue(issues.size() > 0);
        FHIRUtil.write(outcome, Format.JSON, System.out);
    }
    
    /*
     * Without validation, the server does its best to return valid C-CDA, but not all scenarios have been covered
     */
    @Test
    public void testInvalidDocumentWithoutValidation() throws Exception {
        CompositionStatus status = f.createCompositionStatus().withValue(CompositionStatusList.FINAL);
        DateTime datetime = dateTime("Invalid Datetime");
        com.ibm.watsonhealth.fhir.model.String title = string("Invalid Document");
        Bundle documentBundle = createCCD(datetime, title, status);
        
        Parameters parameters = createParameters(documentBundle, false);
        FHIRUtil.write(parameters, Format.JSON, System.out);
        
        Response response = sendRequest(parameters);
        assertResponse(response, Response.Status.OK.getStatusCode());
        
        Base64Binary valueBase64Binary = extractResponseDocument(response);
        System.out.println(new String(valueBase64Binary.getValue(),"UTF-8"));
    }

    @Test
    public void testNonDocumentBundle() throws Exception {
        // use an empty bundle to ensure its invalid
        Bundle documentBundle = f.createBundle();
        
        Parameters parameters = createParametersWithDocument(documentBundle);
        
        Response response = sendRequest(parameters);
        assertResponse(response, Response.Status.BAD_REQUEST.getStatusCode());
        
        OperationOutcome outcome = response.readEntity(OperationOutcome.class);
        assertNotNull(outcome);
        List<OperationOutcomeIssue> issues = outcome.getIssue();
        assertTrue(issues.size() > 0);
        FHIRUtil.write(outcome, Format.JSON, System.out);
    }
    
    /**
     * Builds a FHIR document from canned resources
     * @return
     * @throws Exception
     */
    private Bundle buildDocument() throws Exception {
        CompositionStatus status = f.createCompositionStatus().withValue(CompositionStatusList.FINAL);
        DateTime datetime = dateTime("2015-02-14T13:42:00+10:00");
        com.ibm.watsonhealth.fhir.model.String title = string("Sample Document");
        Bundle document = createCCD(datetime, title, status);
        
        // Create a practitioner and add it to the document.
        Practitioner practitioner = readResource(Practitioner.class, "Practitioner.json");
        Uri practitionerUri = addAuthor(document, practitioner);
        
        // Create and add a patient with that practitioner as a care provider.
        Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");
        patient.withCareProvider(reference(practitionerUri));
        Uri patientUri = addSubject(document, patient);
        
        // Next, an Observation belonging to the new patient.
        Observation observation = buildObservation(patientUri, "Observation1.json");
        codeableConcept(LOINC.URI, LOINC.CCDA_SECTION_VITALSIGNS);
        CompositionSection vitalsSection = createSection("Vital Signs", codeableConcept(LOINC.URI, LOINC.CCDA_SECTION_VITALSIGNS));
        addSection(document, vitalsSection);
        addResourceToDocument(document, vitalsSection, observation);
        
        // Next, a Condition belonging to the new patient.
        Condition condition = buildCondition(patientUri, "Condition.json");
        CompositionSection problemsSection = createSection("Problem List", codeableConcept(LOINC.URI, LOINC.CCDA_SECTION_PROBLEMLIST));
        addSection(document, problemsSection);
        addResourceToDocument(document, problemsSection, condition);
        
        // Next, an AllergyIntolerance belonging to the new patient.
        AllergyIntolerance allergyIntolerance = buildAllergyIntolerance(patientUri, "AllergyIntolerance.json");
        CompositionSection allergiesSection = createSection("Allergies", codeableConcept(LOINC.URI, LOINC.CCDA_SECTION_ALLERGIES));
        addSection(document, allergiesSection);
        addResourceToDocument(document, allergiesSection, allergyIntolerance);
        
        return document;
    }

    private Uri addSubject(Bundle document, Patient patient) throws Exception {
        Uri patientUri = addResourceToBundle(document, patient);
        getComposition(document).setSubject(reference(patientUri));
        return patientUri;
    }

    private Uri addAuthor(Bundle document, Practitioner practitioner) throws Exception {
        Uri practitionerUri = addResourceToBundle(document, practitioner);
        getComposition(document).getAuthor().add(reference(practitionerUri));
        return practitionerUri;
    }
    
    /**
     * Creates and returns new FHIR bundle of type document with an empty Composition resource entry
     * @return
     * @throws Exception
     */
    private Bundle createCCD(DateTime date, com.ibm.watsonhealth.fhir.model.String title, CompositionStatus status) throws Exception {
        BundleType documentBundleType = objFactory.createBundleType().withValue(BundleTypeList.DOCUMENT);
        Bundle document = objFactory.createBundle().withType(documentBundleType);
        
        Composition composition = objFactory.createComposition();
        
        composition.setDate(date);
        composition.setType(codeableConcept(LOINC.URI, LOINC.CCDA_CCD));
        composition.setTitle(title);
        composition.setStatus(status);
        
        addResourceToBundle(document, composition);
        return document;
    }
    
    /**
     * Creates a section with @sectionTitle and @sectionCode
     * @return
     * @throws Exception
     */
    private CompositionSection createSection(String sectionTitle, CodeableConcept sectionCode) throws Exception {
        CompositionSection section = objFactory.createCompositionSection();
        section.setTitle(string(sectionTitle));
        section.setCode(sectionCode);
        return section;
    }
    
    /**
     * Adds @section to the composition resource of the document
     * @return
     * @throws Exception
     */
    private void addSection(Bundle document, CompositionSection section) {
        Composition composition = getComposition(document);
        composition.getSection().add(section);
    }
    
    protected Observation buildObservation(Uri patientUri, String fileName) throws Exception {
        Observation observation = readResource(Observation.class, fileName)
            .withSubject(reference(patientUri));
        return observation;
    } 
    
    private Condition buildCondition(Uri patientUri, String fileName) throws Exception {
        Condition condition = readResource(Condition.class, fileName)
            .withPatient(reference(patientUri));
        return condition;
    }
    
    private AllergyIntolerance buildAllergyIntolerance(Uri patientUri, String fileName) throws Exception {
        AllergyIntolerance allergyIntolerance = readResource(AllergyIntolerance.class, fileName)
            .withPatient(reference(patientUri));
        return allergyIntolerance;
    }
    
    /**
     * Adds the resource to the bundle and adds a reference to it in section @section
     * @param document a FHIR bundle whose first entry is a Composition 
     * @param section the section to add the reference to
     * @param resource the resource to add
     * @return the fullUrl of the newly added bundle entry for @resource
     * @throws Exception 
     */
    private Uri addResourceToDocument(Bundle document, CompositionSection section, Resource resource) throws Exception {
        Uri uri = addResourceToBundle(document, resource);
        section.getEntry().add(reference(uri.getValue()));
        return uri;
    }
    
    /**
     * Create a new bundleEntry within @bundle and return the fullUrl
     * @param bundle 
     * @param resource
     * @return the fullUrl of the newly added bundle entry for @resource
     * @throws Exception 
     */
    private Uri addResourceToBundle(Bundle bundle, Resource resource) throws Exception {
        Uri uri = createUUID();
        
        ResourceContainer resourceContainer = objFactory.createResourceContainer();
        FHIRUtil.setResourceContainerResource(resourceContainer, resource);
        
        BundleEntry entry = objFactory.createBundleEntry()
            .withFullUrl(uri)
            .withResource(resourceContainer);
        bundle.getEntry().add(entry);
        
        return uri;
    }
    
    private Uri createUUID() {
        return objFactory.createUri().withValue("urn:uuid:" + UUID.randomUUID());
    }
    
    private Composition getComposition(Bundle document) {
        return document.getEntry().get(0).getResource().getComposition();
    }
    
    /**
     * Wrap the FHIR document in a Parameters object and invoke the 'to CDA' operation with it
     * @param documentBundle
     * @return
     * @throws Exception
     */
    private Parameters createParametersWithDocument(Bundle documentBundle) throws Exception {
        return createParameters(documentBundle, (Boolean) null);
    }
    
    /**
     * Wrap the FHIR document in a Parameters object and invoke the 'to CDA' operation with it
     * @param documentBundle
     * @param validate if non-null, include the validateFHIR parameter with the request
     * @return
     * @throws Exception
     */
    private Parameters createParameters(Bundle documentBundle, Boolean validate) throws Exception {
        Parameters parameters = objFactory.createParameters();
        
        ParametersParameter documentParameter = objFactory.createParametersParameter();
        documentParameter.setName(string("document"));
        ResourceContainer container = objFactory.createResourceContainer();
        FHIRUtil.setResourceContainerResource(container, documentBundle);
        documentParameter.setResource(container);
        parameters.getParameter().add(documentParameter);
        
        if (validate != null) {
            ParametersParameter validateParameter = objFactory.createParametersParameter();
            validateParameter.setName(string("validateFHIR"));
            validateParameter.setValueBoolean(bool(validate));
            parameters.getParameter().add(validateParameter);
        }
        
        return parameters;
    }
    
    private Response sendRequest(Parameters parameters) throws JAXBException {
        FHIRUtil.write(parameters, Format.XML, System.out);
        System.out.println();
        Entity<Parameters> entity = Entity.entity(parameters, MediaType.APPLICATION_JSON_FHIR);
        
        WebTarget target = getWebTarget();
        return target.path("Bundle/$tocda").request().post(entity, Response.class);
    }
}
