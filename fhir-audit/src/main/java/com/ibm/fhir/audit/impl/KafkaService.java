/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.audit.impl;

import static com.ibm.fhir.audit.AuditLogServiceConstants.IGNORED_AUDIT_EVENT_TYPE;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.errors.InterruptException;

import com.ibm.fhir.audit.AuditLogService;
import com.ibm.fhir.audit.beans.AuditLogEntry;
import com.ibm.fhir.audit.configuration.ConfigurationTranslator;
import com.ibm.fhir.audit.mapper.MapperFactory;
import com.ibm.fhir.audit.mapper.MapperType;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.exception.FHIRException;

/**
 * KafkaService orchestrates the call to Kafka so
 * mappers and translators can be swapped out.
 */
public class KafkaService implements AuditLogService {
    private static final String CLASSNAME = KafkaService.class.getName();
    private static final Logger logger = java.util.logging.Logger.getLogger(CLASSNAME);

    private Boolean enabled = Boolean.FALSE;

    private final ConfigurationTranslator translator = new ConfigurationTranslator();
    private KafkaProducer<String, String> producer = null;
    private String topic = "FHIR_AUDIT";

    private MapperType mapperType = null;
    private PropertyGroup auditLogProperties = null;

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void initialize(PropertyGroup auditLogProperties) throws Exception {
        final String METHODNAME = "initialize";
        logger.entering(CLASSNAME, METHODNAME);

        this.topic = translator.getTopic(auditLogProperties);
        this.mapperType = translator.getMapperType(auditLogProperties);

        this.producer = new KafkaProducer<>(translator.translate(auditLogProperties));
        if (this.producer == null) {
            throw new FHIRException("Failed to initialize the fhir-audit - KafkaProducer!");
        } else {
            logger.info("Initialized Audit Logger");
            this.enabled = true;
        }

        this.auditLogProperties = auditLogProperties;

        logger.exiting(CLASSNAME, METHODNAME);
    }

    @Override
    public void logEntry(AuditLogEntry logEntry) throws Exception {
        final String METHODNAME = "logEntry";
        logger.entering(CLASSNAME, METHODNAME);

        if (this.isLoggableOperation(logEntry)) {
            // Skip Ignored Audit Event Types
            // Eventually this should be picked from the Properties files
            // Rather than this map
            if (!IGNORED_AUDIT_EVENT_TYPE.contains(logEntry.getEventType())) {
                String eventString = MapperFactory.getMapper(mapperType)
                        .init(auditLogProperties)
                        .map(logEntry)
                        .serialize();

                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("Sending to Topic '" + topic + "'" + size(eventString));
                }
                ProducerRecord<String, String> record = new ProducerRecord<>(topic, eventString);
                // Block till the message is sent to kafka server.
                RecordMetadata metadata = this.producer.send(record).get();
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine(" Record Produced to Topic '" + metadata.topic() + "' at time " + metadata.timestamp());
                }
            }
        }

        logger.exiting(CLASSNAME, METHODNAME);
    }

    @Override
    public void stop(PropertyGroup auditLogProperties) throws Exception {
        try{
            this.producer.close(Duration.of(30, ChronoUnit.SECONDS));
        } catch(InterruptException ie) {
            logger.warning("During shutdown... stopping the producer");
        }
    }

    /*
     * calculates the length of the eventString and the null is treated as -1
     */
    private long size(String eventString) {
        return eventString == null ? -1 : eventString.length();
    }
}