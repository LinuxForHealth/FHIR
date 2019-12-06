/*
 * (C) Copyright IBM Corp. 2016,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.client.test.mains;

import static com.ibm.fhir.client.FHIRRequestHeader.header;
import static com.ibm.fhir.model.type.String.string;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ibm.fhir.client.FHIRClient;
import com.ibm.fhir.client.FHIRClientFactory;
import com.ibm.fhir.client.FHIRParameters;
import com.ibm.fhir.client.FHIRRequestHeader;
import com.ibm.fhir.client.FHIRResponse;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.CapabilityStatement;
import com.ibm.fhir.model.resource.CapabilityStatement.Rest;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.code.RestfulCapabilityMode;
import com.ibm.fhir.model.type.code.TypeRestfulInteraction;

/**
 * This class is an implementation of the Patient track scenario defined as part of Connectathon 13, September 2016
 */
public class PatientTrackTest {

    private static final String CLIENT_PROPERTIES_FILENAME = "fhir-client.properties";
    private static final String PATIENT_TESTDATA = "patient.json";
//     private static final String PATIENT_TESTDATA = "patientNoDiv.json";

    private static final String EXTURI_FAVORITE_NFL = "http://com.ibm.fhir.sample/favorite-nfl";
    private static final String EXTURI_FAVORITE_NBA = "http://com.ibm.fhir.sample/favorite-nba";

    private FHIRClient client = null;
    private Patient patient = null;
    private String baseUrl = null;
    private String eTag = null;
    private boolean supportsUpdate = false;
    private boolean supportsHistory = false;
    private boolean supportsSearch = false;

    private static void log(String msg) {
        System.out.println(msg);
    }

    public void printHeader() {
        log("Starting execution of PatientTrackTest (C) IBM Corporation 2016,2019.");
    }

    public void connectToServer() throws Exception {
        // Load up our properties file.
        Properties clientProperties = readProperties(CLIENT_PROPERTIES_FILENAME);

        // Retrieve an instance of the FHIRClient interface.
        client = FHIRClientFactory.getClient(clientProperties);

        baseUrl = clientProperties.getProperty(FHIRClient.PROPNAME_BASE_URL);
        log("Connecting to FHIR endpoint: " + baseUrl);
    }

    public void inspectConformanceStatement() throws Exception {
        FHIRResponse response = client.metadata();
        if (response.getStatus() != Status.OK.getStatusCode()) {
            String msg = "Error retrieving conformance statement, status code = " + response.getStatus();
            log(msg);
            displayOperationOutcome(response);
            throw new Exception(msg);
        }

        CapabilityStatement conformance = response.getResource(CapabilityStatement.class);
        List<Rest> crList = conformance.getRest();
        for (Rest cr : crList) {
            if (cr.getMode() == RestfulCapabilityMode.SERVER) {
                List<Rest.Resource> resources = cr.getResource();
                for (Rest.Resource resource : resources) {
                    if (resource.getType().getValue().equals("Patient")) {
                        List<Rest.Resource.Interaction> interactions = resource.getInteraction();
                        for (Rest.Resource.Interaction interaction : interactions) {
                            if (interaction.getCode() == TypeRestfulInteraction.UPDATE) {
                                supportsUpdate = true;
                            }
                            if (interaction.getCode() == TypeRestfulInteraction.HISTORY_INSTANCE) {
                                supportsHistory = true;
                            }
                            if (interaction.getCode() == TypeRestfulInteraction.SEARCH_TYPE) {
                                supportsSearch = true;
                            }
                        }
                    }
                }
            }
        }

        log("Server supports update? " + (supportsUpdate ? "yes" : "no"));
        log("Server supports history? " + (supportsHistory ? "yes" : "no"));
        log("Server supports search? " + (supportsSearch ? "yes" : "no"));
    }

    public void registerPatient() throws Exception {

        String location;

        // Instantiate a new patient.
        patient = createNewPatient();

        log("\n1) Register new patient");
        displayResource("\nInitialized new patient:", patient);

        FHIRResponse response = client.create(patient);
        if (response.getStatus() != Status.CREATED.getStatusCode()) {
            String msg = "Error persisting patient, status code = " + response.getStatus();
            log(msg);
            displayOperationOutcome(response);
            throw new Exception(msg);
        }

        Response jaxrsResponse = response.getResponse();
        log("Response headers:\n" + jaxrsResponse.getHeaders().toString());

        // Retrieve the patient's location URI string.
        location = getLocation(response);
        log("\nNew patient was persisted, location = " + location);
        if (location == null) {
            throw new Exception("Location header not found");
        }
        
        // Retrieve the ETag value so we can use it in the update step.
        eTag = response.getETag();
        log("New patient's ETag = " + eTag);

        patient = displayResponsePatient("Retrieved newly-registered patient:", client, response);
    }

    public void updatePatient() throws Exception {
        log("\n2) Update a patient");
        if (!supportsUpdate) {
            log("Bypassed");
            return;
        }

        patient = modifyPatientContents(patient);
        FHIRRequestHeader ifMatch = null;
        if (eTag != null) {
            ifMatch = header("If-Match", eTag);
        }
        FHIRResponse response = client.update(patient, ifMatch);
        if (response.getStatus() != Status.OK.getStatusCode()) {
            String msg = "Error updating Patient/" + patient.getId() + ", status code = " + response.getStatus();
            log(msg);
            displayOperationOutcome(response);
            throw new Exception(msg);
        }

        // Retrieve the patient's location URI string.
        String location = getLocation(response);
        log("\nUpdated patient was persisted, location = " + location);
        patient = displayResponsePatient("Retrieved updated patient:", client, response);

        // 2a) Next, we'll test whether the server implements version-aware updates.
        // change the patient's name, then try to update it.
        patient = patient.toBuilder().name(HumanName.builder().family(string("Failed!!!"))
                .given(string("Version-aware-update-check")).build()).build();
        
        response = client.update(patient, header("If-Match", "W/\"1\""));
        log("Performed version-aware-update-check, status=" + response.getStatus());
        if (response.getStatus() == Status.CONFLICT.getStatusCode()) {
            log("Version-aware update check succeeded!");
        } else {
            log("Version-aware update check failed; expected status code = " + Status.CONFLICT.getStatusCode() + ", actual status code = "
                    + response.getStatus());
        }
    }

    public void retrieveHistory() throws Exception {
        log("\n3) Retrieve patient history");
        if (!supportsHistory) {
            log("Bypassed");
            return;
        }

        String patientId = patient.getId();
        FHIRResponse response = client.history("Patient", patientId, null);
        if (response.getStatus() != Status.OK.getStatusCode()) {
            String msg = "Error retrieving resource history for Patient/" + patientId + ", status code = " + response.getStatus();
            log(msg);
            displayOperationOutcome(response);
            throw new Exception(msg);
        }

        Bundle bundle = response.getResource(Bundle.class);
        displayResource("Patient history results:", bundle);

    }

    public void searchByName() throws Exception {
        // 4) Search for a patient by name.
        log("\n4) Search for a patient by name");
        if (!supportsSearch) {
            log("Bypassed");
            return;
        }
        
        FHIRParameters parameters = new FHIRParameters();
        parameters = parameters.searchParam("given", "Peter").searchParam("family", "Chalmers");
        FHIRResponse response = client.search("Patient", parameters);
        if (response.getStatus() != Status.OK.getStatusCode()) {
            String msg = "Error searching for patients by name, status code = " + response.getStatus();
            log(msg);
            displayOperationOutcome(response);
            throw new Exception(msg);
        }

        Bundle bundle = response.getResource(Bundle.class);
        displayResource("Search results:", bundle);
    }

    /**
     * @param response
     * @return
     * @throws Exception
     */
    private String getLocation(FHIRResponse response) throws Exception {
        String location = response.getLocation();
        return location;
    }

    private Patient displayResponsePatient(String msg, FHIRClient client, FHIRResponse callerResponse) throws Exception {

        // Retrieve the patient's id from its location URI string.
        String [] tokens = callerResponse.parseLocation(callerResponse.getLocation());
        String patientId = tokens[1];
        log("Retrieved patient id from location value: " + patientId);

        // Retrieve the patient.
        FHIRResponse response = client.read("Patient", patientId);
        if (response.getStatus() != Status.OK.getStatusCode()) {
            String errmsg = "Error retrieving Patient/" + patientId + ", status code = " + response.getStatus();
            log(errmsg);
            displayOperationOutcome(response);
            throw new Exception(errmsg);
        }

        // Grab the resource from the API response and display it.
        Patient patient = response.getResource(Patient.class);
        displayResource(msg, patient);

        return patient;
    }

    private void displayResource(String msg, Resource resource) throws FHIRException {
        String s = TestUtil.writeResource(resource, Format.JSON, true);
        log(msg);
        log(s);
    }

    private Properties readProperties(String filename) throws Exception {
        Properties props = new Properties();
        InputStream is = TestUtil.resolveFileLocation(filename);
        props.load(is);
        return props;
    }

    private void displayOperationOutcome(FHIRResponse response) throws Exception {
        OperationOutcome oo = null;
        try {
            oo = response.getResource(OperationOutcome.class);
            displayResource("OperationOutcome: ", oo);
        } catch (Throwable t) {
            log("\n ***** Error: unable to extract OperationOutcome from response: " + t);
            String payload = response.getResponse().readEntity(String.class);
            log("Response payload as string:\n" + (payload != null ? payload.toString() : "<null>"));
        }
    }

    private Patient createNewPatient() throws Exception {
        Patient p = TestUtil.readLocalResource(PATIENT_TESTDATA);
        return p;
    }

    private Patient modifyPatientContents(Patient patient) {
        // Modify the patient's name.
        List<HumanName> newNames = new ArrayList<HumanName>();
        
        // change the first name.
        int i = 0;
        for (HumanName name: patient.getName()) {
            if (i == 0) {
                name = name.toBuilder().family(string("Chalmers")).given(string("Jimmy")).build();
            }
            i++;
            newNames.add(name);
        }
        
        List<Extension> newExtensions = new ArrayList<Extension>();
    
        // Next, change the favorite-nfl attribute to "Packers".
        for (Extension extension: patient.getExtension()) {
            if (extension.getUrl().equals(EXTURI_FAVORITE_NFL)) {
                extension = extension.toBuilder().value(string("Packers")).build();
            } 
            newExtensions.add(extension);
        }
        
        // Finally, add a new extension to indicate the patient's favorite NBA team.
        newExtensions.add(Extension.builder().url(EXTURI_FAVORITE_NBA).value(string("Spurs")).build());
        patient = patient.toBuilder().name(newNames).extension(newExtensions).build();

        return patient;
    }
    
    public static void main(String[] args) throws Exception {
        PatientTrackTest test = new PatientTrackTest();
        test.printHeader();
        test.connectToServer();
        test.inspectConformanceStatement();
        test.registerPatient();
        test.updatePatient();
        test.retrieveHistory();
        test.searchByName();
    }
}
