/*
 * (C) Copyright IBM Corp. 2017, 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.test.kafka;

import static org.testng.AssertJUnit.assertEquals;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
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

import org.linuxforhealth.fhir.core.FHIRMediaType;
import org.linuxforhealth.fhir.model.resource.Observation;
import org.linuxforhealth.fhir.model.resource.Patient;
import org.linuxforhealth.fhir.model.test.TestUtil;
import org.linuxforhealth.fhir.server.test.FHIRServerTestBase;

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

    private static boolean ON = false;

    @BeforeClass
    public void setup() throws Exception {
        Properties testProperties = TestUtil.readTestProperties("test.properties");
        ON = Boolean.parseBoolean(testProperties.getProperty("test.kafka.enabled", "false"));

        if (ON) {
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    messageConsumer();
                }
            });
            thread.start();
        }
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
            connectionProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
            connectionProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

            consumer = new KafkaConsumer<String, String>(connectionProps);
            consumer.subscribe(Arrays.asList(getKafkaTopicName()));
            System.out.println("Subscribed to topic " + getKafkaTopicName());
            while (keepRunning) {

                ConsumerRecords<String, String> records = consumer.poll(Duration.of(100, ChronoUnit.MILLIS));
                System.out.println("polling msges : " + records.count());
                for (ConsumerRecord<String, String> record : records) {

                    System.out.println("kafka record : " + record.value());
                    JSONObject myObject = new JSONObject(record.value());
                    String operationType = myObject.getString("operationType");

                    if (operationType.equalsIgnoreCase("create")) {
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
        if (ON) {
            assertEquals(expectedCreateCounterValue, createCounter);
            assertEquals(expectedUpdatedCounterValue, updateCounter);
        } else {
            System.out.println("Skipping results verification, disabled");
        }
    }

    /**
     * Create a Patient,Observation and update of the original observation.
     */
    @Test(groups = { "kafka-notifications" })
    public void testCreateandUpdate() throws Exception {
        if (ON) {
            WebTarget target = getWebTarget();
            // Build a new Patient and then call the 'create' API.
            Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");
            Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
            Response response = target.path("Patient").request().post(entity, Response.class);
            assertResponse(response, Response.Status.CREATED.getStatusCode());
            String patientId = getLocationLogicalId(response);
            response = target.path("Patient/" + patientId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
            Patient responsePatient = response.readEntity(Patient.class);
            savedCreatedPatient = responsePatient;
            expectedCreateCounterValue++;

            // Next, create an Observation belonging to the new patient.
            patientId = savedCreatedPatient.getId();
            Observation observation = TestUtil.buildPatientObservation(patientId, "Observation1.json");
            Entity<Observation> obs = Entity.entity(observation, FHIRMediaType.APPLICATION_FHIR_JSON);
            response = target.path("Observation").request().post(obs, Response.class);
            assertResponse(response, Response.Status.CREATED.getStatusCode());
            String observationId = getLocationLogicalId(response);
            response = target.path("Observation/" + observationId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
            Observation responseObs = response.readEntity(Observation.class);
            savedCreatedObservation = responseObs;
            expectedCreateCounterValue++;

            // Create an updated Observation based on the original saved observation
            patientId = savedCreatedPatient.getId();
            observation = TestUtil.buildPatientObservation(patientId, "Observation2.json");
            observation = observation.toBuilder().id(savedCreatedObservation.getId()).build();
            obs = Entity.entity(observation, FHIRMediaType.APPLICATION_FHIR_JSON);

            // Call the 'update' API.
            String targetPath = "Observation/" + observation.getId();
            response = target.path(targetPath).request().put(obs, Response.class);
            assertResponse(response, Response.Status.OK.getStatusCode());
            observationId = getLocationLogicalId(response);
            response = target.path("Observation/" + observationId).request(FHIRMediaType.APPLICATION_FHIR_JSON).get();
            expectedUpdatedCounterValue++;
            System.out.println("expectedUpdatedCounterValue---------" + expectedUpdatedCounterValue);
            System.out.println("expectedCreateCounterValue---------" + expectedCreateCounterValue);
            keepRunning = false;
        } else {
            System.out.println("Skipping create and update for kafka, disabled");
        }
    }
}
