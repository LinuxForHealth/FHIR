/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package org.linuxforhealth.fhir.server.index.kafka;

import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import org.linuxforhealth.fhir.config.FHIRRequestContext;
import org.linuxforhealth.fhir.persistence.helper.RemoteIndexSupport;
import org.linuxforhealth.fhir.persistence.index.FHIRRemoteIndexService;
import org.linuxforhealth.fhir.persistence.index.IndexProviderResponse;
import org.linuxforhealth.fhir.persistence.index.RemoteIndexConstants;
import org.linuxforhealth.fhir.persistence.index.RemoteIndexData;
import org.linuxforhealth.fhir.persistence.index.RemoteIndexMessage;
import org.linuxforhealth.fhir.server.index.kafka.KafkaPropertyAdapter.Mode;


/**
 * Forwards parameter blocks to a partitioned Kafka topic. This allows us to
 * skip the expensive parameter insert operations during ingestion and offload
 * them to a separate process where we can process the operations more efficiently
 * by using larger batches and different concurrency control mechanisms
 */
public class FHIRRemoteIndexKafkaService extends FHIRRemoteIndexService {
    private static final Logger logger = Logger.getLogger(FHIRRemoteIndexKafkaService.class.getName());

    private String topicName = null;
    private Producer<String, String> producer;
    private KafkaPropertyAdapter.Mode mode;
    private String instanceIdentifier;

    /**
     * Default constructor
     */
    public FHIRRemoteIndexKafkaService() {
    }

    /**
     * Initialize the provider
     * @param properties
     */
    public void init(KafkaPropertyAdapter properties) {
        this.mode = properties.getMode();
        this.topicName = properties.getTopicName();
        this.instanceIdentifier = properties.getInstanceIdentifier();
        Properties kafkaProps = new Properties();
        properties.putPropertiesTo(kafkaProps);

        if (logger.isLoggable(Level.FINER)) {
            logger.finer("Kafka async index publisher is configured with the following properties:\n" + kafkaProps.toString());
            logger.finer("Topic name: " + this.topicName);
        }

        String bootstrapServers = kafkaProps.getProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG);
        if (bootstrapServers == null) {
            throw new IllegalStateException("The " + ProducerConfig.BOOTSTRAP_SERVERS_CONFIG + " property was missing from the index service Kafka connection properties.");
        }

        if (this.mode != Mode.LOGONLY) {
            producer = new KafkaProducer<>(kafkaProps);
        }
    }

    /**
     * Performs any necessary "shutdown" logic to disconnect from the topic.
     */
    public void shutdown() {
        logger.entering(this.getClass().getName(), "shutdown");

        try {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Shutting down Kafka index service for topic: '" + topicName + "'.");
            }
            if (producer != null) {
                producer.close();
            }
        } finally {
            logger.exiting(this.getClass().getName(), "shutdown");
        }
    }

    @Override
    public IndexProviderResponse submit(final RemoteIndexData data) {
        // We rely on the default Kafka partitioner, which in our case will
        // select a partition based on a hash of the key, which should be
        // something like "Patient/a-patient-logical-id"
        final String tenantId = FHIRRequestContext.get().getTenantId();
        RemoteIndexMessage msg = new RemoteIndexMessage();
        msg.setMessageVersion(RemoteIndexConstants.MESSAGE_VERSION);
        msg.setInstanceIdentifier(this.instanceIdentifier);
        msg.setTenantId(tenantId);
        msg.setData(data.getSearchParameters());
        final String message = RemoteIndexSupport.marshallToString(msg);

        if (this.mode == Mode.ACTIVE) {
            ProducerRecord<String, String> record = new ProducerRecord<String, String>(topicName, data.getPartitionKey(), message);
            Future<RecordMetadata> rmd = producer.send(record);
            
            // convert the future we get from the producer into a CompletableFuture
            // and then map this to the response type we use (information hiding...
            // the caller shouldn't be exposed to the fact that we're using Kafka)
            CompletableFuture<Void> cf = backToThe(rmd)
                    .thenApply(v -> {
                        return null;
                    });
            return new IndexProviderResponse(data, cf);
        } else {
            // just log the message to help debug
            logger.info("Remote index request: " + message);
            return new IndexProviderResponse(data, CompletableFuture.completedFuture(null));
        }
    }

    /**
     * Convert a Future into a CompletableFuture
     * @param <T>
     * @param f
     * @return
     */
    public static <T> CompletableFuture<T> backToThe(Future<T> f) {
        // This composition means we don't call f.get() until the get
        // is called on the CompletableFuture result
        return CompletableFuture.completedFuture(null).thenCompose(noValue -> {
            try {
                logger.finest("Waiting for index service Kafka send to complete");
                return CompletableFuture.completedFuture(f.get());
            } catch (InterruptedException e) {
                // the only time we should be interrupted is during shutdown, so no
                // need to log here because it should be expected that we'll fail
                return CompletableFuture.failedFuture(e);
            } catch (ExecutionException e) {
                // Log the issue right away so that it can be time-correlated with other issues
                logger.log(Level.SEVERE, "Failed async submission to Kafka", e);
                return CompletableFuture.failedFuture(e.getCause());
            } finally {
                logger.finest("completed");
            }
        });
    }
}