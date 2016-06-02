/**
 * (C) Copyright IBM Corp. 2016,2017,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.notifications.kafka.impl;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.ibm.watsonhealth.fhir.notification.FHIRNotificationEvent;
import com.ibm.watsonhealth.fhir.notification.FHIRNotificationService;
import com.ibm.watsonhealth.fhir.notification.FHIRNotificationSubscriber;
import com.ibm.watsonhealth.fhir.notification.exception.FHIRNotificationException;
import com.ibm.watsonhealth.fhir.notification.util.FHIRNotificationUtil;

/**
 * This class implements the FHIR server notification service via a Kafka topic.
 */
public class FHIRNotificationKafkaPublisher implements FHIRNotificationSubscriber {
    private static final Logger log = Logger.getLogger(FHIRNotificationKafkaPublisher.class.getName());

    private static FHIRNotificationService service = FHIRNotificationService.getInstance();

    private String topicName = null;
    private String connectionInfo = null;
    private Producer<String, String> producer = null;
    private Properties connectionProps = null;

    // "Hide" the default ctor.
    protected FHIRNotificationKafkaPublisher() {
    }

    public FHIRNotificationKafkaPublisher(String topicName, String connInfo) {
        log.entering(this.getClass().getName(), "ctor", new Object[] { topicName, connInfo });
        try {
            init(topicName, connInfo);
        } finally {
            log.exiting(this.getClass().getName(), "ctor");
        }
    }

    /**
     * Performs any required initialization to allow us to publish events to the topic.
     */
    private void init(String topic, String connInfo) {
        log.entering(this.getClass().getName(), "init");
        try {
            this.topicName = topic;
            this.connectionInfo = connInfo;

            // Set up our properties for connecting to the kafka server.
            connectionProps = new Properties();
            connectionProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, connectionInfo);
            connectionProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
            connectionProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
            connectionProps.put(ProducerConfig.CLIENT_ID_CONFIG, "fhir-server");
            // connectionProps.put(ProducerConfig.ACKS_CONFIG, "1");
            // connectionProps.put(ProducerConfig.RETRIES_CONFIG, "0");
            // connectionProps.put(ProducerConfig.LINGER_MS_CONFIG, "0");
            // connectionProps.put(ProducerConfig.BATCH_SIZE_CONFIG, "16384");

            // Create our producer object to be used for publishing.
            producer = new KafkaProducer<String, String>(connectionProps);

            // Register this Kafka implementation as a "subscriber" with our Notification Service.
            // This means that our "notify" method will be called when the server publishes an event.
            service.subscribe(this);
            log.info("Initialized Kafka publisher for topic '" + connectionInfo + "/" + topicName + "'.");
        } catch (Throwable t) {
            log.log(Level.SEVERE, "Caught exception while initializing Kafka publisher: ", t);
        } finally {
            log.exiting(this.getClass().getName(), "init");
        }
    }

    /**
     * Performs any necessary "shutdown" logic to disconnect from the topic.
     */
    public void shutdown() {
        log.entering(this.getClass().getName(), "shutdown");

        try {
            log.fine("Shutting down Kafka publisher for topic: " + connectionInfo + "/" + topicName);
            if (producer != null) {
                producer.close();
            }
        } finally {
            log.exiting(this.getClass().getName(), "shutdown");
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.watsonhealth.fhir.notification.FHIRNotificationSubscriber#notify(com.ibm.watsonhealth.fhir.notification.
     * util.FHIRNotificationEvent)
     */
    @Override
    public void notify(FHIRNotificationEvent event) throws FHIRNotificationException {
        log.entering(this.getClass().getName(), "notify");
        String topicId = connectionInfo + "/" + topicName;
        try {
            String jsonString = FHIRNotificationUtil.toJsonString(event, true);
            log.fine("Publishing kafka notification event to topic '" + topicId + "', message: " + jsonString);
            producer.send(new ProducerRecord<String, String>(topicName, jsonString));
            log.fine("message sent...");
        } catch (Throwable e) {
            String msg = "Error publishing kafka notification event to topic '" + topicId;
            log.log(Level.SEVERE, msg, e);
            throw new FHIRNotificationException(msg, e);
        } finally {
            log.exiting(this.getClass().getName(), "notify");
        }
    }
}
