/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.audit.logging.api.configuration.type;

import java.util.Objects;
import java.util.Properties;

import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.config.PropertyGroup.PropertyEntry;

/**
 * Kafka Environment Type
 */
public final class KafkaType {

    private KafkaType() {
        // No Operation
    }

    /**
     * converts from a configuration such as fhir-server-config.json to a java.util.properties
     * object that is setup for Kafka.
     *
     * @param auditLogProperties
     * @return
     * @throws Exception
     */
    public static Properties getEnvironment(PropertyGroup auditLogProperties) throws Exception {
        if(Objects.isNull(auditLogProperties)) {
            throw new IllegalArgumentException("Expected Non Null audit log properties");
        }

        PropertyGroup kafka = auditLogProperties.getPropertyGroup("kafka");
        if(Objects.isNull(kafka)) {
            throw new IllegalArgumentException("Expected Non Null audit log properties for PropertyGroup - kafka");
        }

        Properties props = new Properties();
        for (PropertyEntry entry : kafka.getProperties()) {
            props.put(entry.getName(), entry.getValue());
        }

        // Every instance needs to be loaded:
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return props;
    }
}