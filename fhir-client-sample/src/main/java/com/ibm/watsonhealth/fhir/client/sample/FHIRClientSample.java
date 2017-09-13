/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.client.sample;

import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.codeableConcept;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.coding;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.contactPoint;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.observationStatus;
import static com.ibm.watsonhealth.fhir.model.util.FHIRUtil.quantity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import javax.ws.rs.core.Response.Status;

import com.ibm.watsonhealth.fhir.client.FHIRClient;
import com.ibm.watsonhealth.fhir.client.FHIRClientFactory;
import com.ibm.watsonhealth.fhir.client.FHIRResponse;
import com.ibm.watsonhealth.fhir.model.ContactPointSystemList;
import com.ibm.watsonhealth.fhir.model.ContactPointUseList;
import com.ibm.watsonhealth.fhir.model.ObjectFactory;
import com.ibm.watsonhealth.fhir.model.Observation;
import com.ibm.watsonhealth.fhir.model.ObservationStatusList;
import com.ibm.watsonhealth.fhir.model.OperationOutcome;
import com.ibm.watsonhealth.fhir.model.Patient;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil;
import com.ibm.watsonhealth.fhir.model.util.FHIRUtil.Format;

/**
 * This class is sample code that demonstrates the use of the FHIR Server's Client API. This sample is not necessarily
 * expected to run correctly as certain details are omitted so that we can focus on the highlights of the FHIRClient
 * interface.
 */
public class FHIRClientSample {

    private ObjectFactory of;

    public static void main(String[] args) throws Exception {
        FHIRClientSample sample = new FHIRClientSample();
        sample.run();
    }

    public FHIRClientSample() {
        of = new ObjectFactory();
    }

    public void run() throws Exception {
        String location;

        // Load up our properties file.
        Properties clientProperties = readProperties();

        // Retrieve an instance of the FHIRClient interface.
        FHIRClient client = FHIRClientFactory.getClient(clientProperties);

        // Create and initialize a new patient.
        Patient patient = createPatient("John", "Doe", "512-555-1234");

        // Persist the patient.
        FHIRResponse response = client.create(patient);
        if (response.getStatus() != Status.CREATED.getStatusCode()) {
            String msg = "Error persisting patient, status code = " + response.getStatus();
            System.out.println(msg);
            displayOperationOutcome(response);
            throw new Exception(msg);
        }

        // Retrieve the patient's location URI string.
        location = response.getLocation();
        System.out.println("Patient resource was persisted, location = " + location);

        // Retrieve the patient's id from its location URI string.
        String patientId = getLocationLogicalId(location);

        // Retrieve the patient we just created.
        response = client.read("Patient", patientId);
        if (response.getStatus() != Status.OK.getStatusCode()) {
            String msg = "Error retrieving Patient/" + patientId + ", status code = " + response.getStatus();
            System.out.println(msg);
            displayOperationOutcome(response);
            throw new Exception(msg);
        }

        // Retrieve the resource from the API response.
        patient = response.getResource(Patient.class);

        // Create and initialize an observation to represent a glucose reading for the patient.
        Observation obs = createObservation(patient, 7.4);

        // Persist the observation.
        response = client.create(obs);
        if (response.getStatus() != Status.CREATED.getStatusCode()) {
            String msg = "Error persisting patient, status code = " + response.getStatus();
            System.out.println(msg);
            displayOperationOutcome(response);
            throw new Exception(msg);
        }

        // Retrieve the observation's location URI string.
        location = response.getLocation();
        System.out.println("Observation resource was persisted, location = " + location);

        // Retrieve the observation's id from its location URI string.
        String observationId = getLocationLogicalId(location);

        // Retrieve the observation that we just persisted.
        response = client.read("Observation", observationId);
        if (response.getStatus() != Status.OK.getStatusCode()) {
            String msg = "Error retrieving Observation/" + observationId + ", status code = " + response.getStatus();
            System.out.println(msg);
            displayOperationOutcome(response);
            throw new Exception(msg);
        }
    }

    /**
     * Reads in the 'fhir-client.properties' file and returns a Properties object.
     */
    private Properties readProperties() throws Exception {
        Properties props = new Properties();
        try (InputStream is = resolveFileLocation("fhir-client.properties")) {
            props.load(is);
            return props;
        }
    }

    /**
     * Returns an InputStream for reading the specified file.
     */
    private InputStream resolveFileLocation(String fileName) throws Exception {

        // First, try to use the filename as-is.
        File f = new File(fileName);
        if (f.exists()) {
            return new FileInputStream(f);
        }

        // Next, look in the classpath
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        if (is != null) {
            return is;
        }

        throw new FileNotFoundException("File '" + fileName + "' was not found.");
    }

    /**
     * Retrieves the location id value from a location URI string.
     */
    protected String getLocationLogicalId(String location) {
        String logicalId = null;
        String[] tokens = location.split("/");
        if (tokens != null && tokens.length >= 4) {
            logicalId = tokens[tokens.length - 3];
        }
        return logicalId;
    }

    private void displayOperationOutcome(FHIRResponse response) {
        try {
            OperationOutcome oo = response.getResource(OperationOutcome.class);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FHIRUtil.write(oo, Format.JSON, baos);
            System.out.println("OperationOutcome: \n" + baos.toString());
        } catch (Throwable t) {
            System.out.println("Could not display OperationOutcome from REST API response.");
        }
    }

    /**
     * Create a new Patient resource with the specified name and mobile
     * 
     * @param firstName
     * @param lastName
     * @param mobilePhoneNum
     * @return
     */
    private Patient createPatient(String firstName, String lastName, String mobilePhoneNum) {
        Patient p =
                of.createPatient().withName(of.createHumanName().withFamily(of.createString().withValue(lastName)).withGiven(of.createString().withValue(firstName))).withTelecom(contactPoint(ContactPointSystemList.fromValue("phone"), mobilePhoneNum, ContactPointUseList.fromValue("mobile")));
        return p;
    }

    private Observation createObservation(Patient p, double glucoseReading) {
        String subject = "Patient/" + p.getId().getValue();
        Observation o =
                of.createObservation().withSubject(of.createReference().withReference(of.createString().withValue(subject))).withStatus(observationStatus(ObservationStatusList.PRELIMINARY)).withCode(codeableConcept(coding("http://loinc.org", "15074-8", "Glucose [Moles/volume] in Blood"))).withValueQuantity(quantity(glucoseReading, "mmol/l", "http://unitsofmeasure.org", "mmol/L"));
        return o;
    }
}
