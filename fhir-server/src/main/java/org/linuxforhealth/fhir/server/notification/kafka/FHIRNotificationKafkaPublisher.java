/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.linuxforhealth.fhir.server.notification.kafka;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import org.linuxforhealth.fhir.config.FHIRConfigHelper;
import org.linuxforhealth.fhir.config.FHIRConfiguration;
import org.linuxforhealth.fhir.server.notification.FHIRNotificationEvent;
import org.linuxforhealth.fhir.server.notification.FHIRNotificationException;
import org.linuxforhealth.fhir.server.notification.FHIRNotificationService;
import org.linuxforhealth.fhir.server.notification.FHIRNotificationSubscriber;
import org.linuxforhealth.fhir.server.notification.FHIRNotificationUtil;

/**
 * This class implements the FHIR server notification service via a Kafka topic.
 */
public class FHIRNotificationKafkaPublisher implements FHIRNotificationSubscriber {
    private static final Logger LOG = Logger.getLogger(FHIRNotificationKafkaPublisher.class.getName());

    private static FHIRNotificationService service = FHIRNotificationService.getInstance();

    private String topicName = null;
    private Producer<String, String> producer = null;

    private Properties kafkaProps = null;

    // "Hide" the default ctor.
    protected FHIRNotificationKafkaPublisher() {
    }

    public FHIRNotificationKafkaPublisher(String topicName, Properties kafkaProps) {
        LOG.entering(this.getClass().getName(), "ctor");
        try {
            init(topicName, kafkaProps);
        } finally {
            LOG.exiting(this.getClass().getName(), "ctor");
        }
    }

    /**
     * Performs any required initialization to allow us to publish events to the topic.
     */
    private void init(String topicName, Properties kafkaProps) {
        LOG.entering(this.getClass().getName(), "init");
        try {
            this.topicName = topicName;
            this.kafkaProps = kafkaProps;
            if (LOG.isLoggable(Level.FINER)) {
                LOG.finer("Kafka publisher is configured with the following properties:\n" + this.kafkaProps.toString());
                LOG.finer("Topic name: " + this.topicName);
            }

            // We'll hard-code some properties to ensure they are set correctly.
            this.kafkaProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
            this.kafkaProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
            this.kafkaProps.put(ProducerConfig.CLIENT_ID_CONFIG, "fhir-server");

            // Make sure that the properties file contains the bootstrap.servers property at a minimum.
            String bootstrapServers = this.kafkaProps.getProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG);
            if (bootstrapServers == null) {
                throw new IllegalStateException("The " + ProducerConfig.BOOTSTRAP_SERVERS_CONFIG + " property was missing from the Kafka connection properties.");
            }

            // Create our producer object to be used for publishing.
            producer = new KafkaProducer<>(this.kafkaProps);

            // Register this Kafka implementation as a "subscriber" with our Notification Service.
            // This means that our "notify" method will be called when the server publishes an event.
            service.subscribe(this);
            LOG.info("Initialized Kafka publisher for topic '" + topicName + "' using bootstrap servers: " + bootstrapServers + ".");
        } catch (Throwable t) {
            String msg = "Caught exception while initializing Kafka publisher.";
            LOG.log(Level.SEVERE, msg, t);
            throw new IllegalStateException(msg, t);
        } finally {
            LOG.exiting(this.getClass().getName(), "init");
        }
    }

    /**
     * Performs any necessary "shutdown" logic to disconnect from the topic.
     */
    public void shutdown() {
        LOG.entering(this.getClass().getName(), "shutdown");

        try {
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("Shutting down Kafka publisher for topic: '" + topicName + "'.");
            }
            if (producer != null) {
                producer.close();
            }
        } finally {
            LOG.exiting(this.getClass().getName(), "shutdown");
        }
    }

    @Override
    public void notify(FHIRNotificationEvent event) throws FHIRNotificationException {
        LOG.entering(this.getClass().getName(), "notify");
        String topicId = "[" + this.kafkaProps.getProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG) + "]/" + topicName;
        String jsonString = null;
        try {
            jsonString = FHIRNotificationUtil.toJsonString(event, true);

            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("Publishing kafka notification event to topic '" + topicId + "',\nmessage: " + jsonString);
            }

            boolean sync = FHIRConfigHelper.getBooleanProperty(FHIRConfiguration.PROPERTY_KAFKA_SYNC, false);
            if (!sync) {
                producer.send(new ProducerRecord<String, String>(topicName, jsonString), new KafkaPublisherCallback(event, jsonString, topicId));
                if (LOG.isLoggable(Level.FINE)) {
                    LOG.fine("Returned from async kafka send...");
                }
            } else {
                RecordMetadata metadata = producer.send(new ProducerRecord<String, String>(topicName, jsonString), new KafkaPublisherCallback(event, jsonString, topicId)).get();
                if (LOG.isLoggable(Level.FINE)) {
                    LOG.fine("Record Produced to Topic '" + metadata.topic() + "' at time " + metadata.timestamp());
                }
            }
        } catch (Throwable e) {
            String msg = buildNotificationErrorMessage(topicId, (jsonString == null ? "<null>" : jsonString));
            LOG.log(Level.SEVERE, msg , e);
            throw new FHIRNotificationException(msg, e);
        } finally {
            LOG.exiting(this.getClass().getName(), "notify");
        }
    }

    public class KafkaPublisherCallback implements Callback {
        private FHIRNotificationEvent event;
        private String notificationEvent;
        private String topicId;

        public KafkaPublisherCallback(FHIRNotificationEvent event, String notificationEvent, String topicId) {
            super();
            this.event = event;
            this.notificationEvent = notificationEvent;
            this.topicId = topicId;
        }

        /**
         * This method is called by the KafkaProducer API after a successful *or* unsuccessful send.
         */
        @Override
        public void onCompletion(RecordMetadata recordMetadata, Exception exception) {
            LOG.entering(this.getClass().getName(), "onCompletion");

            try {
                // No exception implies that the send operation succeeded, so log an info message.
                if (exception == null) {
                    LOG.info("Successfully published kafka notification event for resource: " + event.getLocation());
                } else {
                    // If we detected a 'send' failure, then log an error message that includes the notification message
                    // that we tried to send.
                    String msg = buildNotificationErrorMessage(topicId, notificationEvent);
                    LOG.log(Level.SEVERE, msg, exception);
                }
            } finally {
                LOG.exiting(this.getClass().getName(), "onCompletion");
            }
        }
    }

    /**
     * Builds a formatted error message to indicate a notification publication failure.
     */
    private String buildNotificationErrorMessage(String topicId, String notificationEvent) {
        return String.format("Kafka publication failure; topic '%s'\nNotification event: %s\n.", topicId, notificationEvent);
    }
}
