/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.remote.index.kafka;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.TopicPartition;

import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.remote.index.api.IMessageHandler;

/**
 * Kafka consumer reading remote index messages, batches the data and
 * loads it into the configured database.
 */
public class RemoteIndexConsumer implements Runnable, OffsetCommitCallback {

    private static final Logger logger = Logger.getLogger(RemoteIndexConsumer.class.getName());

    // Nanoseconds in a second
    private static final long NANOS = 1000000000L;

    // the remote index service Kafka topic name
    private final String topicName;

    // The max time to spend collecting a batch before submitting
    private final long maxBatchCollectTimeMs;

    // The consumer object representing the connection to Kafka
    private final KafkaConsumer<String, String> kafkaConsumer;

    // The handler we use to process messages we receive from Kafka
    private final IMessageHandler messageHandler;

    private final Duration pollWaitTime;

    // Flag used to exit kafka loop on termination
    private volatile boolean running = true;

    // The number of commit failures since the last successful one
    private int sequentialCommitFailures = 0;

    // If we get 5 failures in a row, we disconnect
    private static final int COMMIT_FAILURE_DISCONNECT_THRESHOLD = 5;

    // A callback used to signal that this consumer has failed
    private final Runnable consumerFailedCallback;

    /**
     * A listener to track the partitions assigned to this cluster member
     * Callbacks happen as part of the consumer#poll() call, so no concurrency concerns
     */
    private class PartitionChangeListener implements ConsumerRebalanceListener {

        @Override
        public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
            for (TopicPartition tp : partitions) {
                logger.info("Revoking partition: " + tp.topic() + ":" + tp.partition());
            }
        }

        @Override
        public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
            for (TopicPartition tp : partitions) {
                logger.info("Assigning partition: " + tp.topic() + ":" + tp.partition());
            }
        }
    }

    /**
     * Public constructor
     * 
     * @param kafkaConsumer
     * @param messageHandler
     * @param consumerFailedCallback
     * @param topicName
     * @param maxBatchCollectTimeMs
     * @param pollWaitTime
     */
    public RemoteIndexConsumer(KafkaConsumer<String, String> kafkaConsumer, IMessageHandler messageHandler,
            Runnable consumerFailedCallback, String topicName, long maxBatchCollectTimeMs, Duration pollWaitTime) {
        this.kafkaConsumer = kafkaConsumer;
        this.messageHandler = messageHandler;
        this.consumerFailedCallback = consumerFailedCallback;
        this.topicName = topicName;
        this.maxBatchCollectTimeMs = maxBatchCollectTimeMs;
        this.pollWaitTime = pollWaitTime;
    }

    /**
     * poll the consumer and forward any messages we receive to the message handler
     */
    private void consume() throws FHIRPersistenceException {
        ConsumerRecords<String, String> records = kafkaConsumer.poll(pollWaitTime);
        List<String> messages = new ArrayList<>();

        for (ConsumerRecord<String, String> record : records) {
            messages.add(record.value());
        }
        messageHandler.process(messages);
        // TODO, obviously
        // kafkaConsumer.commitAsync(this);
    }

    public void shutdown() {
        this.running = false;
        try {
            kafkaConsumer.wakeup();
        } catch (Throwable x) {
            logger.warning("Error waking up kafka consumer: " + x.getMessage());
        }
    }

    @Override
    public void run() {
        logger.info("Subscribing consumer to topic '" + this.topicName + "'");
        kafkaConsumer.subscribe(Collections.singletonList(topicName), new PartitionChangeListener());

        logger.info("Starting consumer loop");
        while (running) {
            try {
                if (this.sequentialCommitFailures > COMMIT_FAILURE_DISCONNECT_THRESHOLD) {
                    logger.severe("Too many commit failures. Stopping consumer");
                    this.running = false;
                } else {
                    consume();
                }
            } catch (Throwable t) {
                // If we end up here, it means we think it's an unrecoverable error so
                // clear the running flag allowing us to exit
                logger.log(Level.SEVERE, "unexpected error in consumer loop", t);
                this.running = false;
            } finally {
                if (!running) {
                    // explicitly closing the consumer here should allow for faster error recovery
                    // (assuming, of course, that the brokers are still reachable from this node)
                    kafkaConsumer.close();

                    // close the handler, cleaning up any resources it is holding onto
                    messageHandler.close();

                    // signal back to the main controller. If enough consumer
                    // threads fail, this will lead to the whole program to terminate
                    // which in typical deployments will mean that the container will
                    // hopefully be restarted
                    consumerFailedCallback.run();
                }
            }
        }
        logger.info("Consumer closed and thread terminated");
    }

    @Override
    public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
        // called on this consumer's thread, so no need for any synchronization
        if (exception == null) {
            // successful commit, so reset the failure counter
            this.sequentialCommitFailures = 0;
        } else {
            this.sequentialCommitFailures++;
            logger.warning("Commit failure sequential=[" + this.sequentialCommitFailures + "] reason=[" + exception.getMessage() + "]");
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE, "sequentialCommitFailures=" + sequentialCommitFailures, exception);
            }
        }
    }
}