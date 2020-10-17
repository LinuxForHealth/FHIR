/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.audit.logging.api.impl;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.ibm.fhir.audit.logging.api.AuditLogService;
import com.ibm.fhir.audit.logging.api.impl.kafka.ConfigurationTranslator;
import com.ibm.fhir.audit.logging.beans.AuditLogEntry;
import com.ibm.fhir.audit.logging.mapper.MapperFactory;
import com.ibm.fhir.audit.logging.mapper.MapperType;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.exception.FHIRException;

/**
 * This class adds support for AuditEvent with a multiaware.
 * https://www.hl7.org/fhir/r4/auditevent.html
 */
public class KafkaService implements AuditLogService {

    private static final Logger logger = java.util.logging.Logger.getLogger(KafkaService.class.getName());
    private static final String CLASSNAME = KafkaService.class.getName();

    private Boolean enabled = Boolean.FALSE;

    private final ConfigurationTranslator translator = new ConfigurationTranslator();
    private KafkaProducer<String, String> producer = null;
    private String topic = "fhir-audit";

    private MapperType mapperType = null;

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void initialize(PropertyGroup auditLogProperties) throws Exception {
        final String METHODNAME = "initialize";
        logger.entering(CLASSNAME, METHODNAME);

        this.topic = translator.getTopic(auditLogProperties);
        this.mapperType = MapperFactory.getMapperType(auditLogProperties);

        this.producer = new KafkaProducer<>(translator.translate(auditLogProperties));
        if (this.producer == null) {
            throw new FHIRException("Failed to initialize the fhir-audit - KafkaProducer!");
        } else {
            logger.info("Initialized Audit Logger");
            this.enabled = true;
        }

        logger.exiting(CLASSNAME, METHODNAME);
    }

    @Override
    public void logEntry(AuditLogEntry logEntry) throws Exception {
        final String METHODNAME = "logEntry";
        logger.entering(CLASSNAME, METHODNAME);

        if (this.isLoggableOperation(logEntry)) {
            String eventString = MapperFactory.getMapper(mapperType).map(logEntry).serialize();
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, eventString);
            // Block till the message is sent to kafka server.
            this.producer.send(record).get();
        }

        logger.exiting(CLASSNAME, METHODNAME);
    }

    @Override
    public void stop(PropertyGroup auditLogProperties) throws Exception {
        this.producer.close(30, TimeUnit.SECONDS);
    }
}