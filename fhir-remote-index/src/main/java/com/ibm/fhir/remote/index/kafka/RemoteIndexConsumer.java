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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import com.ibm.fhir.remote.index.api.IMessageHandler;

/**
 * Kafka consumer reading remote index messages, batches the data and
 * loads it into the configured database.
 */
public class RemoteIndexConsumer implements Runnable {
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
	
	// The map we use to track offsets as we process messages
	private Map<TopicPartition,OffsetAndMetadata> trackingOffsetMap = new HashMap<>();
	private Map<TopicPartition,Long> rollbackOffsetMap = new HashMap<>();
	
	 // Use a list to stage the results of a poll()
    private final LinkedHashSet<ConsumerRecord<String, String>> recordBuffer = new LinkedHashSet<>();
    private final List<ConsumerRecord<String,String>> currentBatch = new ArrayList<>();
    private final Set<TopicPartition> assignedPartitions = new HashSet<>();
	
	// Track the number of sequential commit failures
	private int commitFailures = 0;
	
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
			for (TopicPartition tp: partitions) {
				logger.info("Revoking partition: " + tp.topic() + ":" + tp.partition());
			}
			assignedPartitions.removeAll(partitions);
		}

		@Override
		public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
			for (TopicPartition tp: partitions) {
				logger.info("Assigning partition: " + tp.topic() + ":" + tp.partition());				
			}
			assignedPartitions.addAll(partitions);
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
	public RemoteIndexConsumer(KafkaConsumer<String,String> kafkaConsumer, IMessageHandler messageHandler,
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
	private void consume() {
		ConsumerRecords<String, String> records = kafkaConsumer.poll(pollWaitTime);
		List<String> messages = new ArrayList<>();
		
		for (ConsumerRecord<String, String> record : records) {
		    messages.add(record.value());
		}
		messageHandler.process(messages);
		kafkaConsumer.commitAsync();
	}
	

	public void shutdown() {
		this.running = false;
		try {
			kafkaConsumer.wakeup();
		} catch (Throwable x) {
			logger.warning("Error waking up kafka consumer: " + x.getMessage());
		}
	}
	
    /**
     * Get an array of the TopicPartition tuples currently assigned to this node
     * @return the assigned TopicPartitions, or null if the consumer.assignment is empty
     */
    private TopicPartition[] getConsumerAssignment() {
        Set<TopicPartition> assignment = kafkaConsumer.assignment();
    
        if (assignment.size() > 0) {
            return assignment.toArray(new TopicPartition[assignment.size()]);
        }
        else {
            return null;
        }
    }

    @Override
    public void run() {
        logger.info("Subscribing consumer to topic '" + this.topicName + "'");
        kafkaConsumer.subscribe(Collections.singletonList(topicName), new PartitionChangeListener());

        logger.info("Starting consumer loop");
        while (running) {
            try {
                consume();
            } catch (Throwable t) {
                // If we end up here, it means we think it's an unrecoverable error which
                // we want to signal back to the main controller. If enough consumer 
                // threads fail, this will lead to the whole program to terminate
                // which in typical deployments will mean that the container will
                // hopefully be restarted
                logger.log(Level.SEVERE, "unexpected error in consumer loop", t);
                this.running = false;
                this.consumerFailedCallback.run();
            }
        }
        logger.info("Consumer thread terminated");
    }

}
