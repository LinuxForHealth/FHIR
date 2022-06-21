/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.server.index.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.ProducerConfig;

/**
 * Wrapper around a {@link Properties} making them easier to consume
 */
public class KafkaPropertyAdapter {
    private final Properties properties;
    private final String topicName;
    private final String instanceIdentifier;
    private final Mode mode;
    
    public static enum Mode {
        ACTIVE,
        LOGONLY
    }

    /**
     * Public constructor
     * 
     * @param instanceIdentifier
     * @param topicName
     * @param properties
     * @param mode
     */
    public KafkaPropertyAdapter(String instanceIdentifier, String topicName, Properties properties, Mode mode) {
        this.instanceIdentifier = instanceIdentifier;
        this.topicName = topicName;
        this.properties = properties;
        this.mode = mode;
    }

    /**
     * Fill the given kafkaProperties object with the configuration properties
     * held by this adapter
     * @param kafkaProps
     */
    public void putPropertiesTo(Properties kafkaProps) {
        kafkaProps.put(ProducerConfig.CLIENT_ID_CONFIG, "fhir-server");
        kafkaProps.putAll(this.properties);
        // Make sure we always use these serializers, even if the property
        // has been defined in this.properties
        kafkaProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
    }

    /**
     * @return the topicName
     */
    public String getTopicName() {
        return topicName;
    }

    /**
     * @return the mode
     */
    public Mode getMode() {
        return mode;
    }

    /**
     * @return the instanceIdentifier
     */
    public String getInstanceIdentifier() {
        return instanceIdentifier;
    }
}
