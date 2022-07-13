/*
 * (C) Copyright IBM Corp. 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.audit.configuration.type;

import java.util.Objects;
import java.util.Properties;

import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.config.PropertyGroup.PropertyEntry;

/**
 * Kafka Environment Type
 */
public final class KafkaType {

    public static final String KAFKA_SASL_JAAS_CONFIG = "sasl.jaas.config";
    public static final String KAFKA_BOOTSTRAP_SERVERS = "bootstrap.servers";

    public static final String KAFKA_KEY_SERIALIZER = "key.serializer";
    public static final String KAFKA_VALUE_SERIALIZER = "value.serializer";
    public static final String KAFKA_DEFAULT_SERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";

    public static final String KAFKA_SASL_MECHANISM = "sasl.mechanism";
    public static final String KAFKA_DEFAULT_SASL_MECHANISM = "PLAIN";

    public static final String KAFKA_SECURITY_PROTOCOL = "security.protocol";
    public static final String KAFKA_DEFAULT_SECURITY_PROTOCOL = "SASL_SSL";

    public static final String KAFKA_SSL_PROTOCOLS = "ssl.protocol";
    public static final String KAFKA_SSL_ENABLED_PROTOCOLS = "ssl.enabled.protocols";
    public static final String KAFKA_DEFAULT_SSL_PROTOCOL= "TLSv1.2";

    public static final String KAFKA_SSL_ENDPOINT_IDENTIFICATION_ALGORITHM = "ssl.endpoint.identification.algorithm";
    public static final String KAFKA_DEFAULT_SSL_ENDPOINT_IDENTIFICATION_ALGORITHM = "HTTPS";

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
        props.put(KAFKA_KEY_SERIALIZER, KAFKA_DEFAULT_SERIALIZER);
        props.put(KAFKA_VALUE_SERIALIZER, KAFKA_DEFAULT_SERIALIZER);
        return props;
    }
}