/**
 * (C) Copyright IBM Corp. 2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.server.test;

import static org.testng.AssertJUnit.assertEquals;

import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.watsonhealth.fhir.core.MediaType;
import com.ibm.watsonhealth.fhir.model.Observation;
import com.ibm.watsonhealth.fhir.model.Patient;

public class KafkaNotificationTest extends FHIRServerTestBase {

    private static final Logger log = Logger.getLogger(KafkaNotificationTest.class.getName());
    private Patient savedCreatedPatient;
    private Observation savedCreatedObservation;
    private KafkaConsumer<String, String> consumer = null;
    private Properties connectionProps = null;
    private boolean keepRunning = true;
    private int createCounter = 0;
    private int updateCounter = 0;
    private int expectedCreateCounterValue = 0;
    private int expectedUpdatedCounterValue = 0;

    @BeforeClass
    public void setup() {

        Thread thread = new Thread(new Runnable() {
            public void run() {
                messageConsumer();
            }
        });
        thread.start();
        
    }

    // method for setting up the kafka-consumer properties
    public void messageConsumer() {
        log.entering(this.getClass().getName(), "messageConsumer");
        try {
            // Set up our properties for connecting to the kafka server.
            connectionProps = new Properties();
            connectionProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, getKafkaConnectionInfo());
            connectionProps.put(ConsumerConfig.CLIENT_ID_CONFIG, "fhir-server");
            connectionProps.put("group.id", "test-group");
            connectionProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                    "org.apache.kafka.common.serialization.StringDeserializer");
            connectionProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                    "org.apache.kafka.common.serialization.StringDeserializer");

            consumer = new KafkaConsumer<String, String>(connectionProps);
            consumer.subscribe(Arrays.asList(getKafkaTopicName()));
            System.out.println("Subscribed to topic " + getKafkaTopicName());
            while (keepRunning) {

                ConsumerRecords<String, String> records = consumer.poll(100);
                System.out.println("polling msges : " + records.count());
                for (ConsumerRecord<String, String> record : records) {

                    System.out.println("kafka record : " + record.value());
                    JSONObject myObject = new JSONObject(record.value());
                    String operationType = myObject.getString("operationType");

                    if (operationType.equalsIgnoreCase("create")){
                        createCounter = createCounter + 1;
                    }
                    if (operationType.equalsIgnoreCase("update")) {
                        updateCounter = updateCounter + 1;
                    }
                    System.out.println("createCounter" + createCounter);

                }

            }
        } catch (Throwable t) {
            log.log(Level.SEVERE, "Caught exception while initializing Kafka consumer: ", t);
        } finally {
            log.exiting(this.getClass().getName(), "messageConsumer");
        }
    }

    @Test(dependsOnMethods = { "testCreateandUpdate" })
    public void verifyResults() {
        assertEquals(expectedCreateCounterValue, createCounter);
        assertEquals(expectedUpdatedCounterValue, updateCounter);
    }

    /**
     * Create a Patient,Observation and update of the original observation.
     */
    @Test(groups = { "kafka-notifications" })
    public void testCreateandUpdate() throws Exception {

        WebTarget target = getWebTarget();
        // Build a new Patient and then call the 'create' API.
        Patient patient = readResource(Patient.class, "Patient_JohnDoe.json");
        Entity<Patient> entity = Entity.entity(patient, MediaType.APPLICATION_JSON_FHIR);
        Response response = target.path("Patient").request().post(entity, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        String patientId = getLocationLogicalId(response);
        response = target.path("Patient/" + patientId).request(MediaType.APPLICATION_JSON_FHIR).get();
        Patient responsePatient = response.readEntity(Patient.class);
        savedCreatedPatient = responsePatient;
        expectedCreateCounterValue++;

        // Next, create an Observation belonging to the new patient.
        patientId = savedCreatedPatient.getId().getValue();
        Observation observation = buildObservation(patientId, "Observation1.json");
        Entity<Observation> obs = Entity.entity(observation, MediaType.APPLICATION_JSON_FHIR);
        response = target.path("Observation").request().post(obs, Response.class);
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        String observationId = getLocationLogicalId(response);
        response = target.path("Observation/" + observationId).request(MediaType.APPLICATION_JSON_FHIR).get();
        Observation responseObs = response.readEntity(Observation.class);
        savedCreatedObservation = responseObs;
        expectedCreateCounterValue++;

        // Create an updated Observation based on the original saved observation
        patientId = savedCreatedPatient.getId().getValue();
        observation = buildObservation(patientId, "Observation2.json");
        observation.setId(savedCreatedObservation.getId());
        obs = Entity.entity(observation, MediaType.APPLICATION_JSON_FHIR);

        // Call the 'update' API.
        String targetPath = "Observation/" + observation.getId().getValue();
        response = target.path(targetPath).request().put(obs, Response.class);
        assertResponse(response, Response.Status.OK.getStatusCode());
        observationId = getLocationLogicalId(response);
        response = target.path("Observation/" + observationId).request(MediaType.APPLICATION_JSON_FHIR).get();
        expectedUpdatedCounterValue++;
        System.out.println("expectedUpdatedCounterValue---------" + expectedUpdatedCounterValue);
        System.out.println("expectedCreateCounterValue---------" + expectedCreateCounterValue);
        keepRunning = false;

    }
    


}
